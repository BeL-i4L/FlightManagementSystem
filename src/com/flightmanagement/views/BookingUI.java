/**
 * BookingUI.java
 * Displays the booking interface for the Flight Management System.
 * Handles user input for entering passenger details and selecting
 * ancillary services. Passes all booking information to the
 * BookingController for processing.
 *
 * Author: Kibru Menore
 * Date: 04/25/2026
 * Course: CS 3321 - Software Engineering
 */

package com.flightmanagement.views;

import com.flightmanagement.controllers.BookingController;
import com.flightmanagement.models.Booking;
import com.flightmanagement.models.Flight;

import java.util.Scanner;

public class BookingUI {

    private BookingController bookingController;
    private Scanner scanner;

    public BookingUI(BookingController bookingController) {
        this.bookingController = bookingController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the booking form for the selected flight.
     * Collects passenger details and optional ancillary services.
     * Returns the created booking or null if booking failed.
     */
    public Booking display(Flight selectedFlight, int passengerCount) {
        System.out.println("\n========== BOOKING DETAILS ==========");
        System.out.println("Flight  : " + selectedFlight.getFlightID() +
                           " | " + selectedFlight.getAirlineName());
        System.out.println("Route   : " + selectedFlight.getDepartureAirportCode() +
                           " -> " + selectedFlight.getArrivalAirportCode());
        System.out.println("Date    : " + selectedFlight.getDepartureDateTime());
        System.out.println("Class   : " + selectedFlight.getTicketClass());
        System.out.println("Fare    : $" + selectedFlight.getFare() + " per passenger");
        System.out.println("======================================\n");

        // create the booking in the controller
        Booking booking = bookingController.createBooking(
                          selectedFlight.getFlightID(), passengerCount);

        if (booking == null) {
            System.out.println("Booking could not be created. Please try again.");
            return null;
        }

        // collect details for each passenger
        for (int i = 1; i <= passengerCount; i++) {
            System.out.println("\n--- Passenger " + i + " Details ---");
            collectPassengerDetails(i);
        }

        // ask if customer wants to add any ancillary services
        displayServiceOptions();

        // show the booking summary before confirming
        System.out.println("\n========== BOOKING SUMMARY ==========");
        System.out.println(booking.getBookingSummary());
        System.out.println("=====================================\n");

        System.out.print("Confirm booking? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            bookingController.confirmBooking();
            System.out.println("Booking confirmed! Reference: " +
                               booking.getBookingReference());
            return booking;
        }

        System.out.println("Booking cancelled by user.");
        return null;
    }

    /**
     * Collects personal details for a single passenger.
     * Called once for each passenger in the booking.
     */
    private void collectPassengerDetails(int passengerNumber) {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine().trim();

        System.out.print("Passport Number: ");
        String passportNumber = scanner.nextLine().trim();

        System.out.print("Nationality: ");
        String nationality = scanner.nextLine().trim();

        System.out.print("Seat Number (e.g. 12A): ");
        String seatNumber = scanner.nextLine().trim();

        // pass passenger details to the booking controller
        bookingController.addPassengerToBooking(firstName, lastName,
                                                dateOfBirth, passportNumber,
                                                nationality, seatNumber);
    }

    /**
     * Displays available ancillary services and allows the customer
     * to add them to their booking one at a time.
     */
    private void displayServiceOptions() {
        System.out.println("\nWould you like to add any services to your booking?");
        bookingController.displayAvailableServices();

        System.out.print("Enter service ID to add (or press Enter to skip): ");
        String serviceID = scanner.nextLine().trim();

        // keep asking until user skips or enters invalid input
        while (!serviceID.isEmpty()) {
            bookingController.addServiceToBooking(serviceID);
            System.out.print("Add another service? (Enter service ID or press Enter to skip): ");
            serviceID = scanner.nextLine().trim();
        }
    }
}