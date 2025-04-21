
package com.hexaware.app;

import com.hexaware.bean.Booking;
import com.hexaware.bean.Concert;
import com.hexaware.bean.Customer;
import com.hexaware.bean.Event;
import com.hexaware.bean.EventComparator;
import com.hexaware.bean.Movie;
import com.hexaware.bean.Sports;
import com.hexaware.bean.Venue;
import com.hexaware.dao.BookingSystemServiceProviderImpl;
import com.hexaware.exception.DatabaseException;
import com.hexaware.exception.EventNotFoundException;
import com.hexaware.exception.InvalidBookingIDException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

/**
 * Main class for the Ticket Booking System with a menu-driven interface.
 */
public class TicketBookingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingSystemServiceProviderImpl service = new BookingSystemServiceProviderImpl();

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Create Event");
            System.out.println("2. Show Events");
            System.out.println("3. Book Tickets");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Booking");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            if (!sc.hasNextInt()) {
                System.out.println("❌ Error: Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
            case 1:
                try {
                    System.out.print("Enter event type (Movie/Concert/Sports): ");
                    String type = sc.nextLine();
                    if (!type.equalsIgnoreCase("Movie") && !type.equalsIgnoreCase("Concert") && !type.equalsIgnoreCase("Sports")) {
                        System.out.println("❌ Error: Invalid event type.");
                        break;
                    }

                    System.out.print("Event name: ");
                    String ename = sc.nextLine();
                    if (ename.trim().isEmpty()) {
                        System.out.println("❌ Error: Event name cannot be empty.");
                        break;
                    }

                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = sc.nextLine();
                    LocalDate eventDate;
                    try {
                        eventDate = LocalDate.parse(date);
                    } catch (Exception e) {
                        System.out.println("❌ Error: Invalid date format. Use YYYY-MM-DD.");
                        break;
                    }

                    System.out.print("Time (HH:MM): ");
                    String time = sc.nextLine();
                    LocalTime eventTime;
                    try {
                        eventTime = LocalTime.parse(time);
                    } catch (Exception e) {
                        System.out.println("❌ Error: Invalid time format. Use HH:MM.");
                        break;
                    }

                    System.out.print("Venue name: ");
                    String vname = sc.nextLine();
                    if (vname.trim().isEmpty()) {
                        System.out.println("❌ Error: Venue name cannot be empty.");
                        break;
                    }
                    System.out.print("Venue address: ");
                    String vaddr = sc.nextLine();
                    Venue venue = new Venue(vname, vaddr);

                    System.out.print("Total seats: ");
                    if (!sc.hasNextInt()) {
                        System.out.println("❌ Error: Total seats must be an integer.");
                        sc.nextLine();
                        break;
                    }
                    int total = sc.nextInt();
                    if (total <= 0) {
                        System.out.println("❌ Error: Total seats must be positive.");
                        sc.nextLine();
                        break;
                    }
                    System.out.print("Ticket price: ");
                    if (!sc.hasNextDouble()) {
                        System.out.println("❌ Error: Ticket price must be a number.");
                        sc.nextLine();
                        break;
                    }
                    double price = sc.nextDouble();
                    if (price <= 0) {
                        System.out.println("❌ Error: Ticket price must be positive.");
                        sc.nextLine();
                        break;
                    }
                    sc.nextLine();

                    Event event = null;
                    if (type.equalsIgnoreCase("Movie")) {
                        System.out.print("Genre: ");
                        String genre = sc.nextLine();
                        System.out.print("Actor: ");
                        String actor = sc.nextLine();
                        System.out.print("Actress: ");
                        String actress = sc.nextLine();
                        event = new Movie(ename, eventDate, eventTime, venue, total, total, price, type, genre, actor, actress);
                    } else if (type.equalsIgnoreCase("Concert")) {
                        System.out.print("Artist: ");
                        String artist = sc.nextLine();
                        System.out.print("Type (Theatrical/Classical/Rock/Recital): ");
                        String ctype = sc.nextLine();
                        event = new Concert(ename, eventDate, eventTime, venue, total, total, price, type, artist, ctype);
                    } else if (type.equalsIgnoreCase("Sports")) {
                        System.out.print("Sport: ");
                        String sport = sc.nextLine();
                        System.out.print("Teams: ");
                        String teams = sc.nextLine();
                        event = new Sports(ename, eventDate, eventTime, venue, total, total, price, type, sport, teams);
                    }

                    if (event != null) {
                        service.addEvent(event);
                        System.out.println("\u2705 Event created successfully.");
                    }
                } catch (DatabaseException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
                break;

                case 2:
                    try {
                        Set<Event> events = service.getEventDetails();
                        if (events.isEmpty()) {
                            System.out.println("❌ No events available.");
                        } else {
                            events.stream()
                                .sorted(new EventComparator())
                                .forEach(Event::displayEventDetails);
                        }
                    } catch (DatabaseException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        while (true) {
                            System.out.print("Event to book (or 'Exit' to stop): ");
                            String bookEvent = sc.nextLine();
                            if (bookEvent.equalsIgnoreCase("Exit")) break;
                            if (bookEvent.trim().isEmpty()) {
                                System.out.println("❌ Error: Event name cannot be empty.");
                                continue;
                            }
                            System.out.print("Tickets: ");
                            if (!sc.hasNextInt()) {
                                System.out.println("❌ Error: Number of tickets must be an integer.");
                                sc.nextLine();
                                continue;
                            }
                            int tickets = sc.nextInt();
                            sc.nextLine();
                            if (tickets <= 0) {
                                System.out.println("❌ Error: Number of tickets must be positive.");
                                continue;
                            }
                            System.out.print("Ticket Category (Silver/Gold/Diamond): ");
                            String category = sc.nextLine();
                            if (!category.equalsIgnoreCase("Silver") && !category.equalsIgnoreCase("Gold") && !category.equalsIgnoreCase("Diamond")) {
                                System.out.println("❌ Error: Invalid ticket category.");
                                continue;
                            }
                            List<Customer> customers = new ArrayList<>();
                            for (int i = 0; i < tickets; i++) {
                                System.out.println("Enter details for Customer " + (i + 1));
                                System.out.print("Name: ");
                                String cname = sc.nextLine();
                                if (cname.trim().isEmpty()) {
                                    System.out.println("❌ Error: Customer name cannot be empty.");
                                    i--;
                                    continue;
                                }
                                System.out.print("Email: ");
                                String email = sc.nextLine();
                                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                                    System.out.println("❌ Error: Invalid email format.");
                                    i--;
                                    continue;
                                }
                                System.out.print("Phone: ");
                                String phone = sc.nextLine();
                                if (!phone.matches("\\d{10}")) {
                                    System.out.println("❌ Error: Phone number must be 10 digits.");
                                    i--;
                                    continue;
                                }
                                customers.add(new Customer(cname, email, phone));
                            }
                            Booking booking = service.bookTickets(bookEvent, tickets, customers, category);
                            if (booking != null) {
                                System.out.println("\u2705 Booking successful. ID: " + booking.getBookingId());
                                booking.displayBookingDetails();
                            }
                        }
                    } catch (EventNotFoundException | DatabaseException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        System.out.print("Enter booking ID to cancel: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("❌ Error: Booking ID must be an integer.");
                            sc.nextLine();
                            break;
                        }
                        int bid = sc.nextInt();
                        sc.nextLine();
                        service.cancelBooking(bid);
                        System.out.println("\u2705 Booking cancelled successfully.");
                    } catch (InvalidBookingIDException | DatabaseException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        System.out.print("Enter booking ID to view: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("❌ Error: Booking ID must be an integer.");
                            sc.nextLine();
                            break;
                        }
                        int vid = sc.nextInt();
                        sc.nextLine();
                        Booking b = service.getBookingDetails(vid);
                        if (b == null) {
                            throw new InvalidBookingIDException("Booking not found: " + vid);
                        }
                        b.displayBookingDetails();
                    } catch (InvalidBookingIDException | DatabaseException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    } catch (NullPointerException e) {
                        System.out.println("❌ Error: Booking details not available. Please check the booking ID.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting system.");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Error: Invalid option.");
            }
        }
    }
}
