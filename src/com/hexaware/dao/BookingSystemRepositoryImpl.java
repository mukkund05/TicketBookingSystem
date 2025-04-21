package com.hexaware.dao;

import com.hexaware.bean.*;
import com.hexaware.exception.DatabaseException;
import com.hexaware.service.IBookingSystemRepository;
import com.hexaware.util.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BookingSystemRepositoryImpl implements IBookingSystemRepository {

    private int insertOrGetVenueId(Venue venue, Connection conn) throws SQLException {
        String selectSql = "SELECT venue_id FROM Venue WHERE venue_name = ?";
        PreparedStatement checkStmt = conn.prepareStatement(selectSql);
        checkStmt.setString(1, venue.getVenueName());
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("venue_id");
        }

        String insertSql = "INSERT INTO Venue (venue_name, address) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, venue.getVenueName());
        insertStmt.setString(2, venue.getAddress());
        insertStmt.executeUpdate();

        ResultSet keys = insertStmt.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }

        throw new SQLException("Failed to insert or retrieve venue ID.");
    }

    @Override
    public Event createEvent(String eventName, LocalDate date, LocalTime time, int totalSeats,
                             double ticketPrice, String eventType, Venue venue, String actor, String actress) 
                             throws SQLException, DatabaseException {
        String sql = "INSERT INTO Event (event_name, event_date, event_time, venue_id, total_seats, available_seats, ticket_price, event_type, actor, actress) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection()) {
            int venueId = insertOrGetVenueId(venue, conn);
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, eventName);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            pstmt.setTime(3, java.sql.Time.valueOf(time));
            pstmt.setInt(4, venueId);
            pstmt.setInt(5, totalSeats);
            pstmt.setInt(6, totalSeats);
            pstmt.setDouble(7, ticketPrice);
            pstmt.setString(8, eventType);
            if ("Movie".equalsIgnoreCase(eventType)) {
                pstmt.setString(9, actor != null ? actor : "");
                pstmt.setString(10, actress != null ? actress : "");
            } else {
                pstmt.setString(9, "");
                pstmt.setString(10, "");
            }
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            int eventId = keys.next() ? keys.getInt(1) : -1;

            Event event;
            if (eventType.equalsIgnoreCase("Movie")) {
                event = new Movie(eventName, date, time, venue, totalSeats, totalSeats, ticketPrice, eventType, "", actor, actress);
            } else if (eventType.equalsIgnoreCase("Concert")) {
                event = new Concert(eventName, date, time, venue, totalSeats, totalSeats, ticketPrice, eventType, "", "");
            } else {
                event = new Sports(eventName, date, time, venue, totalSeats, totalSeats, ticketPrice, eventType, "", "");
            }
            event.setEventId(eventId);
            return event;
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting event", e);
        }
    }

    @Override
    public Set<Event> getEventDetails() throws SQLException, DatabaseException {
        Set<Event> events = new HashSet<>();
        String sql = "SELECT e.*, v.venue_name, v.address FROM Event e JOIN Venue v ON e.venue_id = v.venue_id";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Venue venue = new Venue(rs.getInt("venue_id"), rs.getString("venue_name"), rs.getString("address"));
                Event event;
                String eventType = rs.getString("event_type");
                if (eventType.equalsIgnoreCase("Movie")) {
                    event = new Movie(
                        rs.getString("event_name"),
                        rs.getDate("event_date").toLocalDate(),
                        rs.getTime("event_time").toLocalTime(),
                        venue,
                        rs.getInt("total_seats"),
                        rs.getInt("available_seats"),
                        rs.getDouble("ticket_price"),
                        eventType,
                        rs.getString("actor"),
                        rs.getString("actress"),
                        ""
                    );
                } else if (eventType.equalsIgnoreCase("Concert")) {
                    event = new Concert(
                        rs.getString("event_name"),
                        rs.getDate("event_date").toLocalDate(),
                        rs.getTime("event_time").toLocalTime(),
                        venue,
                        rs.getInt("total_seats"),
                        rs.getInt("available_seats"),
                        rs.getDouble("ticket_price"),
                        eventType,
                        "",
                        ""
                    );
                } else {
                    event = new Sports(
                        rs.getString("event_name"),
                        rs.getDate("event_date").toLocalDate(),
                        rs.getTime("event_time").toLocalTime(),
                        venue,
                        rs.getInt("total_seats"),
                        rs.getInt("available_seats"),
                        rs.getDouble("ticket_price"),
                        eventType,
                        "",
                        ""
                    );
                }
                event.setEventId(rs.getInt("event_id"));
                events.add(event);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching events", e);
        }
        return events;
    }

    @Override
    public int getAvailableNoOfTickets(String eventName) throws SQLException, DatabaseException {
        String sql = "SELECT available_seats FROM Event WHERE event_name = ?";
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, eventName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_seats");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving available seats", e);
        }
        return -1;
    }

    @Override
    public Booking bookTickets(String eventName, int numTickets, List<Customer> customers) throws SQLException, DatabaseException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Event event = null;
            for (Event e : getEventDetails()) {
                if (e.getEventName().equalsIgnoreCase(eventName)) {
                    event = e;
                    break;
                }
            }
            if (event == null) {
                throw new DatabaseException("Event not found: " + eventName);
            }
            if (event.getAvailableSeats() < numTickets) {
                throw new DatabaseException("Not enough available seats for event: " + eventName);
            }

            String bookingSQL = "INSERT INTO Booking (event_id, num_tickets, total_cost, booking_date) VALUES (?, ?, ?, ?)";
            PreparedStatement bookingStmt = conn.prepareStatement(bookingSQL, Statement.RETURN_GENERATED_KEYS);
            bookingStmt.setInt(1, event.getEventId());
            bookingStmt.setInt(2, numTickets);
            bookingStmt.setDouble(3, event.getTicketPrice() * numTickets);
            bookingStmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            bookingStmt.executeUpdate();

            ResultSet keys = bookingStmt.getGeneratedKeys();
            if (!keys.next()) throw new DatabaseException("Failed to retrieve booking ID.");
            int bookingId = keys.getInt(1);

            String customerSQL = "INSERT INTO Customer (customer_name, email, phone_number, booking_id) VALUES (?, ?, ?, ?)";
            PreparedStatement customerStmt = conn.prepareStatement(customerSQL, Statement.RETURN_GENERATED_KEYS);
            for (Customer c : customers) {
                customerStmt.setString(1, c.getCustomerName());
                customerStmt.setString(2, c.getEmail());
                customerStmt.setString(3, c.getPhoneNumber());
                customerStmt.setInt(4, bookingId);
                customerStmt.executeUpdate();
                ResultSet customerKeys = customerStmt.getGeneratedKeys();
                if (customerKeys.next()) {
                    c.setCustomerId(customerKeys.getInt(1));
                }
            }

            String updateSeatsSQL = "UPDATE Event SET available_seats = available_seats - ? WHERE event_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSeatsSQL);
            updateStmt.setInt(1, numTickets);
            updateStmt.setInt(2, event.getEventId());
            updateStmt.executeUpdate();

            conn.commit();

            return new Booking(bookingId, new HashSet<>(customers), event, numTickets, event.getTicketPrice() * numTickets, LocalDateTime.now());
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Database error during booking", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void cancelBooking(int bookingId) throws SQLException, DatabaseException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            String fetchSQL = "SELECT event_id, num_tickets FROM Booking WHERE booking_id = ?";
            PreparedStatement fetchStmt = conn.prepareStatement(fetchSQL);
            fetchStmt.setInt(1, bookingId);
            ResultSet rs = fetchStmt.executeQuery();

            if (!rs.next()) {
                throw new DatabaseException("Booking ID not found: " + bookingId);
            }

            int eventId = rs.getInt("event_id");
            int tickets = rs.getInt("num_tickets");

            PreparedStatement delCustomers = conn.prepareStatement("DELETE FROM Customer WHERE booking_id = ?");
            delCustomers.setInt(1, bookingId);
            delCustomers.executeUpdate();

            PreparedStatement delBooking = conn.prepareStatement("DELETE FROM Booking WHERE booking_id = ?");
            delBooking.setInt(1, bookingId);
            delBooking.executeUpdate();

            PreparedStatement updateSeats = conn.prepareStatement("UPDATE Event SET available_seats = available_seats + ? WHERE event_id = ?");
            updateSeats.setInt(1, tickets);
            updateSeats.setInt(2, eventId);
            updateSeats.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw new DatabaseException("Database error during cancellation", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public Booking getBookingDetails(int bookingId) throws SQLException, DatabaseException {
        Connection conn = DBUtil.getConnection();
        try {
            String bookingSQL = "SELECT b.*, e.*, v.venue_name, v.address FROM Booking b JOIN Event e ON b.event_id = e.event_id JOIN Venue v ON e.venue_id = v.venue_id WHERE b.booking_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(bookingSQL);
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) return null;

            Venue venue = new Venue(rs.getInt("venue_id"), rs.getString("venue_name"), rs.getString("address"));
            Event event;
            String eventType = rs.getString("event_type");
            if (eventType.equalsIgnoreCase("Movie")) {
                event = new Movie(
                    rs.getString("event_name"),
                    rs.getDate("event_date").toLocalDate(),
                    rs.getTime("event_time").toLocalTime(),
                    venue,
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats"),
                    rs.getDouble("ticket_price"),
                    eventType,
                    rs.getString("actor"),
                    rs.getString("actress"),
                    ""
                );
            } else if (eventType.equalsIgnoreCase("Concert")) {
                event = new Concert(
                    rs.getString("event_name"),
                    rs.getDate("event_date").toLocalDate(),
                    rs.getTime("event_time").toLocalTime(),
                    venue,
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats"),
                    rs.getDouble("ticket_price"),
                    eventType,
                    "",
                    ""
                );
            } else {
                event = new Sports(
                    rs.getString("event_name"),
                    rs.getDate("event_date").toLocalDate(),
                    rs.getTime("event_time").toLocalTime(),
                    venue,
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats"),
                    rs.getDouble("ticket_price"),
                    eventType,
                    "",
                    ""
                );
            }
            event.setEventId(rs.getInt("event_id"));

            int numTickets = rs.getInt("num_tickets");
            double cost = rs.getDouble("total_cost");
            LocalDateTime bookingDate = rs.getTimestamp("booking_date").toLocalDateTime();

            String customerSQL = "SELECT * FROM Customer WHERE booking_id = ?";
            PreparedStatement custStmt = conn.prepareStatement(customerSQL);
            custStmt.setInt(1, bookingId);
            ResultSet crs = custStmt.executeQuery();

            Set<Customer> customers = new HashSet<>();
            while (crs.next()) {
                Customer c = new Customer(
                    crs.getInt("customer_id"),
                    crs.getString("customer_name"),
                    crs.getString("email"),
                    crs.getString("phone_number")
                );
                customers.add(c);
            }

            return new Booking(bookingId, customers, event, numTickets, cost, bookingDate);
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving booking", e);
        } finally {
            conn.close();
        }
    }
}