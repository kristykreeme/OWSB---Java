package Admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard - OMEGA WHOLESALE SDN BHD");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Overview Panel
        JPanel overview = createOverviewPanel();
        add(overview, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center on screen
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        String[] options = {"Dashboard", "User Management", "Reports", "Logout"};
        for (String option : options) {
            JButton button = new JButton(option);
            button.setForeground(Color.WHITE);
            button.setBackground(Color.GRAY);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(button);
        }
        return panel;
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        String[] stats = {"Total Users: 50", "Total Sales Managers: 10", "Total Purchase Managers: 5", "Total Inventory Managers: 8"};
        for (String stat : stats) {
            panel.add(createOverviewCard(stat));
        }
        return panel;
    }

    private JPanel createOverviewCard(String text) {
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);
        card.setLayout(new GridBagLayout());
        JLabel label = new JLabel(text);
        label.setFont(new Font("Poppins", Font.BOLD, 16));
        card.add(label);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard().setVisible(true);
        });
    }
}