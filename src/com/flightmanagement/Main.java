/**
 * Main.java
 * Entry point for the Flight Management System.
 * Initializes the application by loading all seed data
 * into the DataStore and launching the home screen.
 *
 * Author: Kibru Menore
 * Date: 04/25/2026
 * Course: CS 3321 - Software Engineering
 */

package com.flightmanagement;

import com.flightmanagement.data.DataLoader;
import com.flightmanagement.views.HomeUI;

public class Main {

    public static void main(String[] args) {
        // load all sample data into memory before launching the UI
        DataLoader.loadAll();

        // launch the home screen to start the application
        HomeUI homeUI = new HomeUI();
        homeUI.display();
    }
}