
package com.hexaware.bean;

import java.time.LocalDate;
import java.time.LocalTime;


public class Sports extends Event {
    private String sportName;
    private String teamsName;

 
    public Sports(String eventName, LocalDate eventDate, LocalTime eventTime, Venue venue,
                  int totalSeats, int availableSeats, double ticketPrice, String eventType,
                  String sportName, String teamsName) {
        super(0, eventName, eventDate, eventTime, venue, totalSeats, availableSeats, ticketPrice, eventType);
        this.sportName = sportName;
        this.teamsName = teamsName;
    }

  
    @Override
    public void displayEventDetails() {
        System.out.println("🏟️ Sports Event: " + eventName +
            " | Sport: " + sportName +
            " | Teams: " + teamsName +
            " | Date: " + eventDate +
            " | Time: " + eventTime +
            " | Venue: " + venue.getVenueName() +
            " | Address: " + venue.getAddress() +
            " | Price: ₹" + ticketPrice +
            " | Available Seats: " + availableSeats + "/" + totalSeats);
    }

    // Getters and Setters
    public String getSportName() { return sportName; }
    public void setSportName(String sportName) { this.sportName = sportName; }
    public String getTeamsName() { return teamsName; }
    public void setTeamsName(String teamsName) { this.teamsName = teamsName; }
}
