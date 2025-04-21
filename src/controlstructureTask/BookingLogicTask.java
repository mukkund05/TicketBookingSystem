package controlstructureTask;

import java.util.Scanner;

public class BookingLogicTask {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total available tickets: ");
        int availableTickets = sc.nextInt();

        System.out.print("Enter number of tickets to book: ");
        int ticketsToBook = sc.nextInt();

        if (ticketsToBook <= availableTickets) {
            int remaining = availableTickets - ticketsToBook;
            System.out.println("Booking successful! Remaining tickets: " + remaining);
        } else {
            System.out.println("Booking failed! Not enough tickets available.");
        }

        sc.close();
    }
}
