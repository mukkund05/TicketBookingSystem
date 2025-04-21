package com.hexaware.bean;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a movie event, extending Event.
 */
public class Movie extends Event {
    private String genre;
    private String actorName;
    private String actressName;

    
    public Movie(String eventName, LocalDate eventDate, LocalTime eventTime, Venue venue,
                 int totalSeats, int availableSeats, double ticketPrice, String eventType,
                 String genre, String actorName, String actressName) {
        super(0, eventName, eventDate, eventTime, venue, totalSeats, availableSeats, ticketPrice, eventType);
        this.genre = genre;
        this.actorName = actorName;
        this.actressName = actressName;
    }

    
    @Override
    public void displayEventDetails() {
        System.out.println("🎬 Movie Event: " + eventName +
            " | Genre: " + genre +
            " | Actor: " + actorName +
            " | Actress: " + actressName +
            " | Date: " + eventDate +
            " | Time: " + eventTime +
            " | Venue: " + venue.getVenueName() +
            " | Address: " + venue.getAddress() +
            " | Price: ₹" + ticketPrice +
            " | Available Seats: " + availableSeats + "/" + totalSeats);
    }

    // Getters and Setters
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getActorName() { return actorName; }
    public void setActorName(String actorName) { this.actorName = actorName; }
    public String getActressName() { return actressName; }
    public void setActressName(String actressName) { this.actressName = actressName; }
}