
package com.hexaware.service;

import com.hexaware.bean.Event;
import com.hexaware.bean.Venue;
import com.hexaware.exception.DatabaseException;
import java.util.Set;

/**
 * Interface for event service operations.
 */
public interface IEventServiceProvider {
    Event createEvent(String eventName, String date, String time, int totalSeats, 
                     double ticketPrice, String eventType, Venue venue) throws DatabaseException;
    Set<Event> getEventDetails() throws DatabaseException;
    int getAvailableNoOfTickets(String eventName) throws DatabaseException;
}
