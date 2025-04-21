package com.hexaware.dao;

import com.hexaware.bean.*;
import com.hexaware.exception.*;
import com.hexaware.service.IBookingSystemServiceProvider;
import com.hexaware.service.IBookingSystemRepository;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BookingSystemServiceProviderImpl extends EventServiceProviderImpl implements IBookingSystemServiceProvider {
    private IBookingSystemRepository repository = new BookingSystemRepositoryImpl();
    private Set<Event> events = new HashSet<>();

 
    public void addEvent(Event event) throws DatabaseException {
        try {
            String actor = "";
            String actress = "";
            if (event instanceof Movie) {
                actor = ((Movie) event).getActorName();
                actress = ((Movie) event).getActressName();
            }
            Event createdEvent = repository.createEvent(
                event.getEventName(),
                event.getEventDate(),
                event.getEventTime(),
                event.getTotalSeats(),
                event.getTicketPrice(),
                event.getEventType(),
                event.getVenue(),
                actor,
                actress
            );
            events.add(createdEvent);
        } catch (SQLException e) {
            throw new DatabaseException("Error while adding event", e);
        }
    }

    @Override
    public Set<Event> getEventDetails() throws DatabaseException {
        try {
            events.clear();
            events.addAll(repository.getEventDetails());
            return events;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching events", e);
        }
    }

    @Override
    public Booking bookTickets(String eventName, int numTickets, List<Customer> customers, String category) 
            throws EventNotFoundException, DatabaseException {
        try {
            Event event = events.stream()
                .filter(e -> e.getEventName().equalsIgnoreCase(eventName))
                .findFirst()
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventName));
                
            if (repository.getAvailableNoOfTickets(event.getEventName()) < numTickets) {
                throw new EventNotFoundException("Not enough seats available for event: " + eventName);
            }
            
            double priceModifier = switch (category.toLowerCase()) {
                case "silver" -> 1.0;
                case "gold" -> 1.5;
                case "diamond" -> 2.0;
                default -> 1.0;
            };
            double totalCost = event.getTicketPrice() * numTickets * priceModifier;
            
            Booking booking = new Booking(new HashSet<>(customers), event, numTickets, totalCost);
            return repository.bookTickets(eventName, numTickets, customers);
        } catch (SQLException e) {
            throw new DatabaseException("Database error during booking", e);
        }
    }

    @Override
    public void cancelBooking(int bookingId) throws InvalidBookingIDException, DatabaseException {
        try {
            repository.cancelBooking(bookingId);
        } catch (SQLException e) {
            throw new DatabaseException("Database error during cancellation", e);
        }
    }

    @Override
    public Booking getBookingDetails(int bookingId) throws InvalidBookingIDException, DatabaseException {
        try {
            Booking booking = repository.getBookingDetails(bookingId);
            if (booking == null) {
                throw new InvalidBookingIDException("Booking not found: " + bookingId);
            }
            return booking;
        } catch (SQLException e) {
            throw new DatabaseException("Database error retrieving booking", e);
        }
    }
}