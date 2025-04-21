package com.hexaware.service;

import com.hexaware.bean.Booking;
import com.hexaware.bean.Customer;
import com.hexaware.bean.Event;
import com.hexaware.bean.Venue;
import com.hexaware.exception.DatabaseException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;


public interface IBookingSystemRepository {
    Event createEvent(String eventName, LocalDate date, LocalTime time, int totalSeats, double ticketPrice,
                      String eventType, Venue venue, String actor, String actress) throws SQLException, DatabaseException;
    Set<Event> getEventDetails() throws SQLException, DatabaseException;
    int getAvailableNoOfTickets(String eventName) throws SQLException, DatabaseException;
    Booking bookTickets(String eventName, int numTickets, List<Customer> customers) throws SQLException, DatabaseException;
    void cancelBooking(int bookingId) throws SQLException, DatabaseException;
    Booking getBookingDetails(int bookingId) throws SQLException, DatabaseException;
}




