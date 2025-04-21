package com.hexaware.service;

import com.hexaware.bean.Event;

public abstract class BookingSystem {
    public abstract Event createEvent(String eventName, String date, String time, int totalSeats,
                                     double ticketPrice, String eventType, com.hexaware.bean.Venue venue);
    public abstract void displayEventDetails(Event event);
    public abstract void bookTickets(Event event, int numTickets);
    public abstract void cancelTickets(Event event, int numTickets);
    public abstract int getAvailableSeats(Event event);
}