
package com.hexaware.bean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a booking in the ticket booking system.
 */
public class Booking {
    private static int bookingIdCounter = 0;
    private int bookingId;
    private Set<Customer> customers; // Using Set to prevent duplicates
    private Event event;
    private int numTickets;
    private double totalCost;
    private LocalDateTime bookingDate;

    /**
     * Constructor for creating a booking without an ID (used before DB insertion).
     * @param customers Set of customers
     * @param event Event being booked
     * @param numTickets Number of tickets
     * @param totalCost Total cost of the booking
     */
    public Booking(Set<Customer> customers, Event event, int numTickets, double totalCost) {
        if (numTickets != customers.size()) {
            throw new IllegalArgumentException("Number of tickets must equal number of customers");
        }
        this.bookingId = ++bookingIdCounter;
        this.customers = new HashSet<>(customers);
        this.event = event;
        this.numTickets = numTickets;
        this.totalCost = totalCost;
        this.bookingDate = LocalDateTime.now();
        event.bookTickets(numTickets);
    }

    /**
     * Constructor for creating a booking with an ID (used after DB retrieval).
     * @param bookingId Booking ID
     * @param customers Set of customers
     * @param event Event being booked
     * @param numTickets Number of tickets
     * @param totalCost Total cost of the booking
     * @param bookingDate Date of booking
     */
    public Booking(int bookingId, Set<Customer> customers, Event event, int numTickets, double totalCost, LocalDateTime bookingDate) {
        if (numTickets != customers.size()) {
            throw new IllegalArgumentException("Number of tickets must equal number of customers");
        }
        this.bookingId = bookingId;
        this.customers = new HashSet<>(customers);
        this.event = event;
        this.numTickets = numTickets;
        this.totalCost = totalCost;
        this.bookingDate = bookingDate;
    }

    /**
     * Displays booking details.
     */
    public void displayBookingDetails() {
        System.out.println("\nBooking ID: " + bookingId);
        event.displayEventDetails();
        System.out.println("Tickets: " + numTickets + " | Total Cost: ₹" + totalCost);
        System.out.println("Customers:");
        for (Customer c : customers) {
            c.displayCustomerDetails();
        }
        System.out.println("Booking Date: " + bookingDate);
    }

    // Getters
    public int getBookingId() { return bookingId; }
    public Event getEventDetails() { return event; }
    public int getBookedNoOfTickets() { return numTickets; }
    public double getTotalCost() { return totalCost; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public Set<Customer> getCustomers() { return customers; }
}
