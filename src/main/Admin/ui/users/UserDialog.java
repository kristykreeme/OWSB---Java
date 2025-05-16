package main.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Dialog for adding or editing users
 */
public class UserDialog extends JDialog {
    private JTextField idField, usernameField, emailField, contactField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private boolean isNewUser;
    private boolean result = false;
    private String originalId;
    
    // UI Colors
    private Color primaryColor = new Color(66, 134, 244);
    private Color accentColor = new Color(33, 67, 122);
    
    public UserDialog(JFrame parent, String userId) {
        super(parent, userId == null ? "Add New User" : "Edit User", true);
        this.isNewUser = (userId == null);
        this.originalId = userId;
        
        setSize(450, 400);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // ID Field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        idField = new JTextField(20);
        idField.setEnabled(isNewUser); // Only allow editing ID for new users
        panel.add(idField, gbc);
        
        // Username Field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);
        
        // Password Field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);
        
        // Email Field
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);
        
        // Contact Field
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Contact:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        contactField = new JTextField(20);
        panel.add(contactField, gbc);
        
        // Role Field
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Role:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        String[] roles = {"Admin", "SalesManager", "PurchaseManager", "InventoryManager", "FinanceManager"};
        roleComboBox = new JComboBox<>(roles);
        panel.add(roleComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(primaryColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            if (validateForm()) {
                result = true;
                dispose();
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        add(panel);
        
        // If editing, load existing user data
        if (!isNewUser) {
            loadUserData(userId);
        } else {
            // For new users, generate a new ID
            int maxId = getMaxUserId();
            idField.setText(String.valueOf(maxId + 1));
        }
    }
    
    private int getMaxUserId() {
        int maxId = 0;
        try {
            List<String> lines = readUserFile("users.txt");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException e) {
                        // Skip non-numeric IDs
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxId;
    }
    
    private void loadUserData(String userId) {
        try {
            List<String> lines = readUserFile("users.txt");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(userId)) {
                    idField.setText(parts[0]);
                    usernameField.setText(parts[1]);
                    passwordField.setText(parts[2]);
                    emailField.setText(parts[3]);
                    contactField.setText(parts[4]);
                    roleComboBox.setSelectedItem(parts[5]);
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
    }
    
    private boolean validateForm() {
        // Simple validation
        if (idField.getText().trim().isEmpty() || 
            usernameField.getText().trim().isEmpty() || 
            new String(passwordField.getPassword()).trim().isEmpty() || 
            emailField.getText().trim().isEmpty() ||
            contactField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return false;
        }
        return true;
    }
    
    public boolean showDialog() {
        setVisible(true);
        return result;
    }
    
    public String getUserData() {
        return String.join(",",
            idField.getText().trim(),
            usernameField.getText().trim(),
            new String(passwordField.getPassword()).trim(),
            emailField.getText().trim(),
            contactField.getText().trim(),
            (String) roleComboBox.getSelectedItem()
        );
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
    
    public String getOriginalId() {
        return originalId;
    }
} 