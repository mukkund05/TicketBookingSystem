package com.hexaware.dao;

import com.hexaware.bean.Concert;
import com.hexaware.bean.Event;
import com.hexaware.bean.Movie;
import com.hexaware.bean.Sports;
import com.hexaware.bean.Venue;
import com.hexaware.exception.DatabaseException;
import com.hexaware.service.IEventServiceProvider;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class EventServiceProviderImpl implements IEventServiceProvider {
    protected Set<Event> events = new HashSet<>();

    @Override
    public Event createEvent(String eventName, String date, String time, int totalSeats, 
                            double ticketPrice, String eventType, Venue venue) throws DatabaseException {
        LocalDate eventDate = LocalDate.parse(date);
        LocalTime eventTime = LocalTime.parse(time);
        Event event = null;
        if (eventType.equalsIgnoreCase("Movie")) {
            event = new Movie(eventName, eventDate, eventTime, venue, totalSeats, totalSeats, ticketPrice, eventType, "", "", "");
        } else if (eventType.equalsIgnoreCase("Concert")) {
            event = new Concert(eventName, eventDate, eventTime, venue, totalSeats, totalSeats, ticketPrice, eventType, "", "");
        } else {
            event = new Sports(eventName, eventDate, eventTime, venue, totalSeats, totalSeats, ticketPrice, eventType, "", "");
        }
        events.add(event);
        return event;
    }

    @Override
    public Set<Event> getEventDetails() throws DatabaseException {
        return events;
    }

    @Override
    public int getAvailableNoOfTickets(String eventName) throws DatabaseException {
        for (Event e : events) {
            if (e.getEventName().equalsIgnoreCase(eventName)) {
                return e.getAvailableSeats();
            }
        }
        return -1;
    }

    public Event findEventByName(String name) {
        for (Event e : events) {
            if (e.getEventName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}