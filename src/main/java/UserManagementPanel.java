package main.java;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private Color primaryColor = new Color(66, 134, 244);
    private Color accentColor = new Color(33, 67, 122);
    
    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table model with columns
        String[] columns = {"ID", "Username", "Email", "Contact", "Role"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(30);
        userTable.getTableHeader().setBackground(primaryColor);
        userTable.getTableHeader().setForeground(Color.WHITE);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        deleteButton = new JButton("Delete User");
        
        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        addButton.addActionListener(e -> addUser());
        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteUser());
        
        // Load initial data
        loadUserData();
    }
    
    private void styleButton(JButton button) {
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
    }
    
    private void loadUserData() {
        tableModel.setRowCount(0);
        try {
            List<String> lines = readUserFile("users.txt");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Vector<String> row = new Vector<>();
                    row.add(parts[0]); // ID
                    row.add(parts[1]); // Username
                    row.add(parts[3]); // Email
                    row.add(parts[4]); // Contact
                    row.add(parts[5]); // Role
                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
    }
    
    private void addUser() {
        UserDialog dialog = new UserDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        if (dialog.showDialog()) {
            try {
                String userData = dialog.getUserData();
                appendToFile("users.txt", userData);
                loadUserData();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving user: " + e.getMessage());
            }
        }
    }
    
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit");
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        UserDialog dialog = new UserDialog((JFrame) SwingUtilities.getWindowAncestor(this), userId);
        
        if (dialog.showDialog()) {
            try {
                updateUserInFile("users.txt", dialog.getOriginalId(), dialog.getUserData());
                loadUserData();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
            }
        }
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete");
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this user?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                deleteUserFromFile("users.txt", userId);
                loadUserData();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
            }
        }
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
    
    private void appendToFile(String filename, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(data);
            writer.newLine();
        }
    }
    
    private void updateUserInFile(String filename, String userId, String newData) throws IOException {
        List<String> lines = readUserFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(userId)) {
                    writer.write(newData);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        }
    }
    
    private void deleteUserFromFile(String filename, String userId) throws IOException {
        List<String> lines = readUserFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].equals(userId)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
} 