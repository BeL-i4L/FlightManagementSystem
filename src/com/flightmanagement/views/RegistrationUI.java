/**
 * RegistrationUI.java
 * Displays the registration interface for the Flight Management System.
 * Handles user input for creating a new customer account and passes
 * the details to the AuthController for processing.
 *
 * Author: Kibru Menore
 * Date: 04/25/2026
 * Course: CS 3321 - Software Engineering
 */

package com.flightmanagement.views;

import com.flightmanagement.controllers.AuthController;

import java.util.Scanner;

public class RegistrationUI {

    private AuthController authController;
    private Scanner scanner;

    public RegistrationUI(AuthController authController) {
        this.authController = authController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the registration form and collects all required
     * information from the new user. Passes the details to
     * AuthController to create the account.
     * Returns true if registration was successful.
     */
    public boolean display() {
        System.out.println("\n========== CREATE ACCOUNT ==========");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        // keep asking until passwords match
        String password = "";
        String confirmPassword = "";
        while (true) {
            System.out.print("Password: ");
            password = scanner.nextLine().trim();

            System.out.print("Confirm Password: ");
            confirmPassword = scanner.nextLine().trim();

            if (password.equals(confirmPassword)) {
                break;
            }
            System.out.println("Passwords do not match. Please try again.");
        }

        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine().trim();

        // pass all details to the controller to create the account
        boolean success = authController.register(fullName, email,
                                                  password, phoneNumber);
        if (success) {
            System.out.println("Account created! You can now log in.");
        } else {
            System.out.println("Registration failed. Email may already be in use.");
        }

        return success;
    }
}