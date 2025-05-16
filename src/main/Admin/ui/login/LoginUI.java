package Admin;

import utils.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class LoginUI extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }

    public LoginUI() {
        setTitle("Admin Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        userIdField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("User ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> login());

        panel.add(new JLabel(""));
        panel.add(loginBtn);

        add(panel);
    }

    private void login() {
        String id = userIdField.getText().trim();
        String pwd = new String(passwordField.getPassword()).trim();
        String hashedInput = FileHandler.hashPassword(pwd);

        try {
            List<String> lines = FileHandler.readLines("users.txt");
            for (String line : lines) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[0].equals(id) && data[2].equals(hashedInput) && data[5].equals("Admin")) {
                    JOptionPane.showMessageDialog(this, "Login successful.");
                    dispose();
                    new AdminDashboardV2().setVisible(true);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid Admin credentials.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading user data.");
        }
    }
}
