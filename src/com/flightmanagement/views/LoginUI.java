/**
 * LoginUI.java
 * Displays the login interface for the Flight Management System.
 * Handles user input for email and password and passes credentials
 * to the AuthController for validation.
 *
 * Author: Kibru Menore
 * Date: 04/25/2026
 * Course: CS 3321 - Software Engineering
 */

package com.flightmanagement.views;

import com.flightmanagement.controllers.AuthController;

import java.util.Scanner;

public class LoginUI {

    private AuthController authController;
    private Scanner scanner;

    public LoginUI(AuthController authController) {
        this.authController = authController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the login form and prompts the user for
     * their email and password. Passes credentials to
     * AuthController for validation.
     * Returns true if login was successful.
     */
    public boolean display() {
        System.out.println("\n========== LOGIN ==========");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        // pass credentials to controller for authentication
        boolean success = authController.authenticate(email, password);

        if (!success) {
            System.out.println("Login failed. Please try again.");
        }

        return success;
    }
}