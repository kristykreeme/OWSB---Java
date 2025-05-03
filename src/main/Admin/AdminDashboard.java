package Admin;

import models.User;
import utils.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton registerBtn = new JButton("Register New User");
        JButton viewBtn = new JButton("View Registered Users");
        JButton exitBtn = new JButton("Exit");

        registerBtn.addActionListener(e -> new UserRegistrationForm());
        viewBtn.addActionListener(e -> showUsers());
        exitBtn.addActionListener(e -> System.exit(0));

        add(registerBtn);
        add(viewBtn);
        add(exitBtn);

        setVisible(true);
    }

    private void showUsers() {
        List<User> users = FileHandler.loadUsers();
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.getUsername()).append(" - ").append(user.getRole()).append("\n");
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scrollPane, "Registered Users", JOptionPane.INFORMATION_MESSAGE);
    }
}
