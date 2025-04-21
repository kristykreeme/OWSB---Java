package main.Admin;

import main.models.User;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private JPanel contentPanel;

    public AdminDashboard(User user) {
        setTitle("OWSB - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ========== SIDEBAR ==========
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(34, 40, 49));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel appLabel = new JLabel("OWSB");
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        appLabel.setBorder(new EmptyBorder(30, 0, 30, 0));
        sidebar.add(appLabel);

        String[] menuItems = {
                "Dashboard", "Item Management", "Supplier Management",
                "Sales Data Entry", "Purchase Requisition",
                "Purchase Orders", "User Management", "Reports"
        };

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            styleSidebarButton(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(5));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("Logout");
        styleSidebarButton(logoutBtn);
        logoutBtn.setBackground(new Color(57, 62, 70));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(20));

        add(sidebar, BorderLayout.WEST);

        // ========== TOP BAR ==========
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        topBar.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pageTitle.setBorder(new EmptyBorder(10, 20, 10, 0));
        topBar.add(pageTitle, BorderLayout.WEST);

        JTextField searchBar = new JTextField(" Search...");
        searchBar.setPreferredSize(new Dimension(200, 30));
        topBar.add(searchBar, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ========== MAIN CONTENT ==========
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ==== Stat Cards ====
        JPanel statPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statPanel.setOpaque(false);
        statPanel.add(createStatCard("Total Items", "248", new Color(72, 133, 237)));
        statPanel.add(createStatCard("Active Suppliers", "35", new Color(52, 168, 83)));
        statPanel.add(createStatCard("Pending PRs", "12", new Color(251, 188, 5)));
        contentPanel.add(statPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // ==== Tables and Charts Row ====
        JPanel rowPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        rowPanel.setOpaque(false);

        rowPanel.add(createRecentPOsTable());
        rowPanel.add(createChartPlaceholder());

        contentPanel.add(rowPanel);

        add(contentPanel, BorderLayout.CENTER);
    }

    // ========== UTILITY METHODS ==========

    private void styleSidebarButton(JButton btn) {
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(44, 52, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRecentPOsTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Recent Purchase Orders"));

        String[] columns = {"PO ID", "Date", "Supplier", "Status"};
        Object[][] data = {
                {"PO-2025-001", "16/04/2025", "Supplier A", "Pending"},
                {"PO-2025-002", "15/04/2025", "Supplier B", "Approved"},
                {"PO-2025-003", "14/04/2025", "Supplier C", "Approved"},
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createChartPlaceholder() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Overview"));

        JLabel barChart = new JLabel("📊 Monthly Purchase Overview", SwingConstants.CENTER);
        barChart.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel pieChart = new JLabel("🥧 Top Sales Items", SwingConstants.CENTER);
        pieChart.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(barChart);
        panel.add(pieChart);

        return panel;
    }
}
