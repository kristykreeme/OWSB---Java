import utils.FileUtils;
import models.User;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("Login - OMEGA WHOLESALE SDN BHD");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("OMEGA WHOLESALE SDN BHD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        centerPanel.add(new JLabel("Username:"));
        centerPanel.add(usernameField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passwordField);
        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JButton loginButton = new JButton("Login");
        loginButton.setFocusPainted(false);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            User user = FileUtils.authenticateUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + user.getUsername());
                // You can now redirect based on user.getRole()
                dispose(); // Close the login window

                switch (user.getRole()) {
                    case "Admin":
                        new AdminDashboard(); // Or whatever your Admin panel class is
                        break;
                    case "SalesManager":
                        new SalesManagerPanel(); // Your sales UI class
                        break;
                    case "PurchaseManager":
                        new PurchaseManagerPanel(); // Your purchase UI class
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown role: " + user.getRole());
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Optional: Set modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        new LoginScreen();
    }
}
