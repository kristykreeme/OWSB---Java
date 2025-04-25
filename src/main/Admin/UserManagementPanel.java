package admin;

import models.User;
import utils.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManagementPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(UserManagementPanel.class.getName());
    private DefaultTableModel model;

    public UserManagementPanel() {
        setLayout(new BorderLayout());

        // Create user form
        JPanel formPanel = createUserForm();
        add(formPanel, BorderLayout.NORTH);

        // Create user table
        JTable userTable = createUserTable();
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Load users from file on startup
        loadUsersToTable();
    }

    private JPanel createUserForm() {
        JPanel panel = new JPanel(new GridLayout(0, 2));

        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        // Add user button
        JButton addButton = new JButton("Add User");
        addButton.addActionListener(event -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Create a new user with dummy data
            User newUser = new User("1", username, password, "email@example.com", "1234567890", "Admin");
            try {
                FileUtils.addUser(newUser); // Save user using FileUtils
                loadUsersToTable();         // Refresh the table
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error adding user", ex);
                JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage());
            }
        });
        panel.add(addButton);

        return panel;
    }

    private JTable createUserTable() {
        String[] columnNames = {"Username", "Role", "Email", "Contact Number"};
        model = new DefaultTableModel(columnNames, 0);
        return new JTable(model);
    }

    private void loadUsersToTable() {
        try {
            List<User> users = FileUtils.readAllUsers();
            model.setRowCount(0); // Clear existing rows
            for (User user : users) {
                model.addRow(new Object[]{
                        user.getUsername(),
                        user.getRole(),
                        user.getEmail(),
                        user.getContactNumber()
                });
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error loading users", ex);
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }
}
