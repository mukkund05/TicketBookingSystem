
package com.hexaware.bean;

import java.time.LocalDate;
import java.time.LocalTime;


public class Concert extends Event {
    private String artist;
    private String type;

    public Concert(String eventName, LocalDate eventDate, LocalTime eventTime, Venue venue,
                   int totalSeats, int availableSeats, double ticketPrice, String eventType,
                   String artist, String type) {
        super(0, eventName, eventDate, eventTime, venue, totalSeats, availableSeats, ticketPrice, eventType);
        this.artist = artist;
        this.type = type;
    }

 
    @Override
    public void displayEventDetails() {
        System.out.println("🎵 Concert Event: " + eventName +
            " | Artist: " + artist +
            " | Type: " + type +
            " | Date: " + eventDate +
            " | Time: " + eventTime +
            " | Venue: " + venue.getVenueName() +
            " | Address: " + venue.getAddress() +
            " | Price: ₹" + ticketPrice +
            " | Available Seats: " + availableSeats + "/" + totalSeats);
    }

  
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
