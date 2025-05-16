package Admin;

import models.User;
import utils.Constants;
import utils.FileHandler;
import utils.AuditLogger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterUserUI extends JFrame {
    private JTextField idField, usernameField, emailField, contactField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton saveBtn;
    private boolean isEditMode = false;
    private String originalUserId;
    private AdminDashboard parent;

    public RegisterUserUI(AdminDashboard parent, String userIdToEdit) {
        this.parent = parent;
        setTitle(userIdToEdit == null ? "Register New User" : "Edit User");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        contactField = new JTextField();
        roleBox = new JComboBox<>(new String[]{"SalesManager", "PurchaseManager", "InventoryManager", "FinanceManager", "Admin"});
        saveBtn = new JButton(userIdToEdit == null ? "Register" : "Update");

        panel.add(new JLabel("User ID:"));
        panel.add(idField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Contact No:"));
        panel.add(contactField);
        panel.add(new JLabel("Role:"));
        panel.add(roleBox);
        panel.add(new JLabel(""));
        panel.add(saveBtn);

        add(panel);

        if (userIdToEdit != null) {
            isEditMode = true;
            originalUserId = userIdToEdit;
            loadUserData(userIdToEdit);
            idField.setEnabled(false);
        }

        saveBtn.addActionListener(e -> handleSave());
    }

    private void handleSave() {
        String id = idField.getText().trim();
        String username = usernameField.getText().trim();
        String passwordRaw = new String(passwordField.getPassword()).trim();
        String password = FileHandler.hashPassword(passwordRaw);
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String role = roleBox.getSelectedItem().toString();

        if (id.isEmpty() || username.isEmpty() || passwordRaw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID, Username, and Password are required.");
            return;
        }

        User user = new User(id, username, password, email, contact, role);

        try {
            List<String> lines = FileHandler.readLines(Constants.USER_FILE);
            List<String> updated = new ArrayList<>();
            boolean idExists = false;

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    if (isEditMode) {
                        updated.add(user.toFileFormat());
                    } else {
                        idExists = true;
                        updated.add(line);
                    }
                } else {
                    updated.add(line);
                }
            }

            if (!isEditMode && idExists) {
                JOptionPane.showMessageDialog(this, "User ID already exists.");
                return;
            }

            if (!isEditMode) {
                updated.add(user.toFileFormat());
            }

            FileHandler.writeToFile(Constants.USER_FILE, String.join("\n", updated), false);
            AuditLogger.log("A001", isEditMode ? "EDIT" : "ADD", user.getId());
            JOptionPane.showMessageDialog(this, isEditMode ? "User updated." : "User registered.");
            if (parent != null) parent.refreshUserList();
            dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving user: " + ex.getMessage());
        }
    }

    private void loadUserData(String userId) {
        try {
            List<String> lines = FileHandler.readLines(Constants.USER_FILE);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equals(userId)) {
                    idField.setText(parts[0]);
                    usernameField.setText(parts[1]);
                    passwordField.setText(""); // password not loaded for security
                    emailField.setText(parts[3]);
                    contactField.setText(parts[4]);
                    roleBox.setSelectedItem(parts[5]);
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load user data.");
        }
    }
}
