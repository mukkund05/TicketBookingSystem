
package com.hexaware.bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;


public abstract class Event {
    protected int eventId;
    protected String eventName;
    protected LocalDate eventDate;
    protected LocalTime eventTime;
    protected Venue venue;
    protected int totalSeats;
    protected int availableSeats;
    protected double ticketPrice;
    protected String eventType;

    
    public Event() {}

  
    public Event(int eventId, String eventName, LocalDate eventDate, LocalTime eventTime, Venue venue,
                 int totalSeats, int availableSeats, double ticketPrice, String eventType) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
        this.eventType = eventType;
    }

   
    public abstract void displayEventDetails();

    
    public double calculateTotalRevenue() {
        return (totalSeats - availableSeats) * ticketPrice;
    }

    
    public int getBookedNoOfTickets() {
        return totalSeats - availableSeats;
    }

  
    public void bookTickets(int numTickets) {
        if (numTickets <= availableSeats) {
            availableSeats -= numTickets;
        } else {
            throw new IllegalArgumentException("Not enough seats available.");
        }
    }

   
    public void cancelBooking(int numTickets) {
        availableSeats += numTickets;
    }

    // Getters and Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public LocalTime getEventTime() { return eventTime; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId &&
               totalSeats == event.totalSeats &&
               availableSeats == event.availableSeats &&
               Double.compare(event.ticketPrice, ticketPrice) == 0 &&
               Objects.equals(eventName, event.eventName) &&
               Objects.equals(eventDate, event.eventDate) &&
               Objects.equals(eventTime, event.eventTime) &&
               Objects.equals(venue, event.venue) &&
               Objects.equals(eventType, event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventName, eventDate, eventTime, venue, totalSeats, availableSeats, ticketPrice, eventType);
    }
}
