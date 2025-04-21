package main.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import main.models.User;
import main.utils.FileUtils;

public class LoginScreen extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginScreen() {
        setTitle("OWSB Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==== Left Panel: Blue Branding ====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(30, 100, 200));
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setLayout(new GridBagLayout());

        JLabel branding = new JLabel("<html><center><h2 style='color:white;'>OMEGA WHOLESALE</h2><h4 style='color:white;'>SDN BHD</h4></center></html>");
        branding.setForeground(Color.WHITE);
        branding.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(branding);

        // ==== Right Panel: Login Form ====
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(loginTitle, gbc);

        JLabel subtitle = new JLabel("Enter your credentials to access the system");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy++;
        rightPanel.add(subtitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        gbc.gridx = 0;
        rightPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        rightPanel.add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        rightPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        rightPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(30, 100, 200));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(this::handleLogin);
        rightPanel.add(loginBtn, gbc);

        gbc.gridy++;
        JLabel footer = new JLabel("© 2025 Omega Wholesale Sdn Bhd", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        rightPanel.add(footer, gbc);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());

        User user = FileUtils.authenticateUser(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            if (user.getRole().equalsIgnoreCase("Admin")) {
                new AdminDashboard(user).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Only admin can login from this screen.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
