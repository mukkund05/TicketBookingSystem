package controlstructureTask;

import java.util.Scanner;

public class TicketBookingLoopingTask {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String continueBooking;

        do {
            System.out.println("\nAvailable Ticket Types: Silver, Gold, Diamond");
            System.out.print("Enter ticket type (or 'exit' to stop): ");
            String ticketType = sc.nextLine().toLowerCase();

            if (ticketType.equals("exit")) {
                break;
            }

            System.out.print("Enter number of tickets to book: ");
            int numTickets = sc.nextInt();
            sc.nextLine(); // consume newline

            double ticketPrice = 0;

            if (ticketType.equals("silver")) {
                ticketPrice = 500;
            } else if (ticketType.equals("gold")) {
                ticketPrice = 1000;
            } else if (ticketType.equals("diamond")) {
                ticketPrice = 2000;
            } else {
                System.out.println("Invalid ticket type entered.");
                continue;
            }

            double totalCost = ticketPrice * numTickets;
            System.out.println("Booking Successful!");
            System.out.println("Total Cost: ₹" + totalCost);

        } while (true);

        System.out.println("Thank you for using the Ticket Booking System!");
        sc.close();
    }
}
