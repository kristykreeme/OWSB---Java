package Admin;

import javax.swing.*;
import java.awt.*;

public class AdminProfilePage extends JFrame {

    public AdminProfilePage() {
        setTitle("Admin Profile - OMEGA WHOLESALE SDN BHD");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Admin Info Form
        createAdminForm();

        setLocationRelativeTo(null); // Center on screen
    }

    private void createAdminForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JLabel("Admin Username:"), gbc);

        gbc.gridx = 1;
        add(new JTextField(15), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        add(new JTextField(15), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        add(new JPasswordField(15), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton updateButton = new JButton("Update Profile");
        add(updateButton, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminProfilePage().setVisible(true);
        });
    }
}