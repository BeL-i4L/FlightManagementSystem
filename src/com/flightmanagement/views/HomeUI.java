/**
 * HomeUI.java
 * Displays the main home interface for the Flight Management System.
 * Acts as the central navigation hub for all user roles.
 * Directs customers, airline staff, and administrators to their
 * respective menus after successful authentication.
 *
 * Author: Kibru Menore
 * Date: 04/25/2026
 * Course: CS 3321 - Software Engineering
 */

package com.flightmanagement.views;

import com.flightmanagement.controllers.AuthController;
import com.flightmanagement.controllers.BookingController;
import com.flightmanagement.controllers.FlightController;
import com.flightmanagement.controllers.PaymentController;
import com.flightmanagement.controllers.ReportController;
import com.flightmanagement.controllers.ReservationController;
import com.flightmanagement.controllers.SearchController;
import com.flightmanagement.controllers.UserController;
import com.flightmanagement.models.Booking;
import com.flightmanagement.models.Flight;

import java.util.List;
import java.util.Scanner;

public class HomeUI {

    private AuthController authController;
    private SearchController searchController;
    private BookingController bookingController;
    private PaymentController paymentController;
    private ReservationController reservationController;
    private FlightController flightController;
    private UserController userController;
    private ReportController reportController;
    private Scanner scanner;

    public HomeUI() {
        // initialize all controllers
        this.authController = new AuthController();
        this.searchController = new SearchController();
        this.bookingController = new BookingController();
        this.paymentController = new PaymentController();
        this.reservationController = new ReservationController();
        this.flightController = new FlightController();
        this.userController = new UserController();
        this.reportController = new ReportController();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the public home screen and handles navigation
     * for all user types. Public users can search flights
     * without logging in but must log in to book.
     */
    public void display() {
        boolean running = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println("   FLIGHT TICKET SALES AND MANAGEMENT  ");
            System.out.println("========================================");
            System.out.println("1. Search Flights");
            System.out.println("2. Login");
            System.out.println("3. Create Account");
            System.out.println("4. Exit");
            System.out.println("========================================");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // public users can search without logging in
                    handleFlightSearch();
                    break;

                case "2":
                    handleLogin();
                    break;

                case "3":
                    handleRegistration();
                    break;

                case "4":
                    running = false;
                    System.out.println("Thank you for using Flight Management System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Handles the flight search flow including displaying results
     * and prompting the user to book if they find a flight.
     * If user is not logged in, prompts them to login before booking.
     */
    private void handleFlightSearch() {
        FlightSearchUI flightSearchUI = new FlightSearchUI(searchController);
        List<Flight> results = flightSearchUI.display();

        if (results == null || results.isEmpty()) {
            return;
        }

        // ask if user wants to book one of the results
        System.out.print("\nWould you like to book a flight? (yes/no): ");
        String bookChoice = scanner.nextLine().trim().toLowerCase();

        if (!bookChoice.equals("yes")) {
            return;
        }

        // user must be logged in to proceed with booking
        if (!authController.isLoggedIn()) {
            System.out.println("\nYou need to log in to book a flight.");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Go Back");
            System.out.print("Select an option: ");

            String authChoice = scanner.nextLine().trim();
            if (authChoice.equals("1")) {
                if (!handleLogin()) return;
            } else if (authChoice.equals("2")) {
                if (!handleRegistration()) return;
                if (!handleLogin()) return;
            } else {
                return;
            }
        }

        // proceed to flight selection and booking
        handleBookingFlow(results);
    }

    /**
     * Handles the complete booking flow from flight selection
     * through payment and reservation creation.
     */
    private void handleBookingFlow(List<Flight> results) {
        // let customer select a flight from results
        FlightResultsUI flightResultsUI = new FlightResultsUI();
        Flight selectedFlight = flightResultsUI.display(results);

        if (selectedFlight == null) {
            return;
        }

        // get passenger count
        int passengerCount = 0;
        while (passengerCount < 1) {
            System.out.print("Number of passengers: ");
            try {
                passengerCount = Integer.parseInt(scanner.nextLine().trim());
                if (passengerCount < 1) {
                    System.out.println("Please enter at least 1 passenger.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // collect booking details and confirm booking
        BookingUI bookingUI = new BookingUI(bookingController);
        Booking booking = bookingUI.display(selectedFlight, passengerCount);

        if (booking == null) {
            return;
        }

        // process payment for the confirmed booking
        PaymentUI paymentUI = new PaymentUI(paymentController);
        boolean paymentSuccess = paymentUI.display(booking);

        if (paymentSuccess) {
            // create a reservation after successful payment
            reservationController.createReservation(booking.getBookingReference());
            System.out.println("\nYour booking is complete! Safe travels!");
        }
    }

    /**
     * Handles the login flow and redirects the user to their
     * role-specific menu after successful authentication.
     * Returns true if login was successful.
     */
    private boolean handleLogin() {
        LoginUI loginUI = new LoginUI(authController);
        boolean success = loginUI.display();

        if (success) {
            // redirect to the correct menu based on user role
            String role = authController.getUserRole();
            switch (role) {
                case "CUSTOMER":
                    showCustomerMenu();
                    break;
                case "STAFF":
                    StaffUI staffUI = new StaffUI(flightController);
                    staffUI.display();
                    authController.logout();
                    break;
                case "ADMIN":
                    AdminUI adminUI = new AdminUI(userController, reportController);
                    adminUI.display();
                    authController.logout();
                    break;
            }
        }
        return success;
    }

    /**
     * Displays the customer menu after successful login.
     * Allows customers to search flights, manage reservations,
     * or logout.
     */
    private void showCustomerMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. Search and Book Flights");
            System.out.println("2. My Reservations");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleFlightSearch();
                    break;

                case "2":
                    ReservationUI reservationUI = new ReservationUI(reservationController);
                    reservationUI.display();
                    break;

                case "3":
                    authController.logout();
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    /**
     * Handles the registration flow for new customers.
     * Returns true if registration was successful.
     */
    private boolean handleRegistration() {
        RegistrationUI registrationUI = new RegistrationUI(authController);
        return registrationUI.display();
    }
}