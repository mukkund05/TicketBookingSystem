package com.hexaware.service;

import com.hexaware.bean.Booking;
import com.hexaware.bean.Customer;
import com.hexaware.exception.DatabaseException;
import com.hexaware.exception.EventNotFoundException;
import com.hexaware.exception.InvalidBookingIDException;
import java.util.List;


public interface IBookingSystemServiceProvider {
    Booking bookTickets(String eventName, int numTickets, List<Customer> customers, String category) 
        throws EventNotFoundException, DatabaseException;
    void cancelBooking(int bookingId) throws InvalidBookingIDException, DatabaseException;
    Booking getBookingDetails(int bookingId) throws InvalidBookingIDException, DatabaseException;
}