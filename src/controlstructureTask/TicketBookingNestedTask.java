package controlstructureTask;

import java.util.Scanner;

public class TicketBookingNestedTask {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Available Ticket Types: Silver, Gold, Diamond");
        System.out.print("Enter ticket type: ");
        String ticketType = sc.nextLine().toLowerCase();

        System.out.print("Enter number of tickets to book: ");
        int numTickets = sc.nextInt();

        double ticketPrice = 0;

        if (ticketType.equals("silver")) {
            ticketPrice = 500;
        } else if (ticketType.equals("gold")) {
            ticketPrice = 1000;
        } else if (ticketType.equals("diamond")) {
            ticketPrice = 2000;
        } else {
            System.out.println("Invalid ticket type entered.");
            sc.close();
            return;
        }

        double totalCost = ticketPrice * numTickets;
        System.out.println("Booking Successful!");
        System.out.println("Total Cost: ₹" + totalCost);

        sc.close();
    }
}
