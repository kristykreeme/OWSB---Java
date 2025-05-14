package main;

import Admin.AdminDashboard;
import javax.swing.*;

/**
 * Main application entry point for the OWSB System
 */
public class App {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            AdminDashboard.showLoginDialog();
        });
    }
} 