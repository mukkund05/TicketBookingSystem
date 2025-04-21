package com.hexaware.dao;

import com.hexaware.bean.Event;

public abstract class BookingSystem {
    public abstract Event createEvent();
    public abstract void displayEventDetails(Event event);
    public abstract void bookTickets(Event event, int numTickets);
    public abstract void cancelTickets(Event event, int numTickets);
    public abstract int getAvailableSeats(Event event);
}
