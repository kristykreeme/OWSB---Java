package Admin;

import models.User;
import utils.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class UserRegistrationForm extends JFrame {
    private JTextField usernameField, emailField, contactField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public UserRegistrationForm() {
        setTitle("User Registration");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Username:")); usernameField = new JTextField(); add(usernameField);
        add(new JLabel("Password:")); passwordField = new JPasswordField(); add(passwordField);
        add(new JLabel("Email:")); emailField = new JTextField(); add(emailField);
        add(new JLabel("Contact Number:")); contactField = new JTextField(); add(contactField);

        add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Admin", "Sales Manager", "Purchase Manager", "Inventory Manager", "Finance Manager"});
        add(roleBox);

        JButton registerBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Cancel");

        registerBtn.addActionListener(e -> registerUser());
        cancelBtn.addActionListener(e -> dispose());

        add(registerBtn); add(cancelBtn);

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String role = (String) roleBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (FileHandler.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
            return;
        }

        String userId = UUID.randomUUID().toString().substring(0, 8);
        User newUser = new User(userId, username, password, email, contact, role);
        FileHandler.saveUser(newUser);

        JOptionPane.showMessageDialog(this, "User registered successfully!");
        dispose();
    }
}
