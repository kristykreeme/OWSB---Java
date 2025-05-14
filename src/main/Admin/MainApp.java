package main.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class MainApp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Color primaryColor = new Color(66, 134, 244);
    private Color accentColor = new Color(33, 67, 122);
    
    public MainApp() {
        setTitle("OWSB System Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(Color.WHITE);
        
        // Logo/Title panel
        JPanel titlePanel = new JPanel(new BorderLayout(10, 10));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("OWSB System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(accentColor);
        
        JLabel subtitleLabel = new JLabel("Admin Dashboard");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        subtitleLabel.setForeground(Color.GRAY);
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 35));
        
        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 35));
        
        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(200, 40));
        
        // Add components to form
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);
        gbc.gridy = 2;
        formPanel.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy = 3;
        formPanel.add(passLabel, gbc);
        gbc.gridy = 4;
        formPanel.add(passwordField, gbc);
        gbc.gridy = 5;
        formPanel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy = 6;
        formPanel.add(loginButton, gbc);
        
        // Add action listener to login button
        loginButton.addActionListener(e -> performLogin());
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add key listener for Enter key
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (validateLogin(username, password)) {
            // Hide login window
            setVisible(false);
            
            // Show admin dashboard
            SwingUtilities.invokeLater(() -> {
                TestAdminDashboard dashboard = new TestAdminDashboard();
                dashboard.setVisible(true);
            });
            
            // Dispose login window
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateLogin(String username, String password) {
        try {
            List<String> lines = readUserFile("users.txt");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && 
                    parts[1].equals(username) && 
                    parts[2].equals(password) &&
                    parts[5].equals("Admin")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private List<String> readUserFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
} 