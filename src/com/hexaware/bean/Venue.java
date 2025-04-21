package com.hexaware.bean;

import java.util.Objects;


public class Venue {
    private int venueId;
    private String venueName;
    private String address;

  
    public Venue() {}

 
    public Venue(int venueId, String venueName, String address) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.address = address;
    }

   
    public Venue(String venueName, String address) {
        this.venueName = venueName;
        this.address = address;
    }

    
    public void displayVenueDetails() {
        System.out.println("Venue: " + venueName + " | Address: " + address);
    }

    // Getters and Setters
    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return venueId == venue.venueId &&
               Objects.equals(venueName, venue.venueName) &&
               Objects.equals(address, venue.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueId, venueName, address);
    }
}