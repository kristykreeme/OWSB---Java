package main.java;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestAdminDashboard extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private List<String> allUsers = new ArrayList<>();
    private JTabbedPane tabbedPane;
    private JTextField searchField;
    private JComboBox<String> searchFilter;
    
    // UI Colors
    private Color primaryColor = new Color(66, 134, 244);
    private Color accentColor = new Color(33, 67, 122);
    private Color bgColor = new Color(245, 248, 252);
    private Color cardBgColor = Color.WHITE;
    private Color secondaryBgColor = new Color(240, 240, 245);
    private Color textColor = new Color(33, 33, 33);
    private Color secondaryTextColor = new Color(100, 100, 100);

    public TestAdminDashboard() {
        setTitle("Admin Dashboard - OWSB");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Try to set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create main content panel with modern padding
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContent.setBackground(bgColor);

        // Create tabbed pane with modern styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setBackground(bgColor);
        
        // Add tabs
        tabbedPane.addTab("👤 User Management", createUserManagementPanel());
        tabbedPane.addTab("📊 System Statistics", createStatisticsPanel());
        tabbedPane.addTab("📋 Audit Logs", createAuditLogsPanel());
        
        mainContent.add(createHeader(), BorderLayout.NORTH);
        mainContent.add(tabbedPane, BorderLayout.CENTER);
        
        // Add main content to frame
        add(mainContent);
        
        // Initial data load
        loadUsers();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBackground(cardBgColor);
        header.setBorder(createRoundedBorder());
        
        // Title and welcome message
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(cardBgColor);
        
        JLabel title = new JLabel("Welcome, Administrator");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(accentColor);
        title.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 0));
        
        JLabel subtitle = new JLabel("Manage your system effectively");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(secondaryTextColor);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 0));
        
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.CENTER);
        
        // Statistics in header
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        statsPanel.setBackground(cardBgColor);
        statsPanel.add(createStatItem("👥 Users", String.valueOf(allUsers.size())));
        statsPanel.add(createStatItem("⚡ Active", "5"));
        statsPanel.add(createStatItem("🕒 Today", "12"));
        
        // Quick actions panel
        JPanel quickActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        quickActions.setBackground(cardBgColor);
        
        JButton refreshBtn = createStyledButton("🔄 Refresh");
        JButton exportBtn = createStyledButton("📊 Export");
        JButton settingsBtn = createStyledButton("⚙️ Settings");
        
        refreshBtn.addActionListener(e -> loadUsers());
        exportBtn.addActionListener(e -> exportToCSV());
        settingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings coming soon!"));
        
        quickActions.add(refreshBtn);
        quickActions.add(exportBtn);
        quickActions.add(settingsBtn);
        
        // Add components to header
        header.add(titlePanel, BorderLayout.WEST);
        header.add(statsPanel, BorderLayout.CENTER);
        header.add(quickActions, BorderLayout.EAST);
        
        return header;
    }

    private JPanel createStatItem(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(5, 2));
        panel.setBackground(cardBgColor);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        
        JLabel nameLabel = new JLabel(label, JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        nameLabel.setForeground(secondaryTextColor);
        
        panel.add(valueLabel, BorderLayout.NORTH);
        panel.add(nameLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgColor);
        
        // Create search and filter panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(cardBgColor);
        topPanel.setBorder(createRoundedBorder());
        
        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(cardBgColor);
        
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        
        searchFilter = new JComboBox<>(new String[]{"All Fields", "Username", "Email", "Role"});
        searchFilter.setPreferredSize(new Dimension(120, 30));
        
        JButton searchButton = new JButton("🔍 Search");
        searchButton.setBackground(primaryColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> filterUsers());
        
        // Add document listener for real-time search
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterUsers(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterUsers(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterUsers(); }
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchFilter);
        searchPanel.add(searchButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        
        // Add top panel to main panel
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Table Panel with modern styling
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Username", "Email", "Contact", "Role"}, 0
        );
        
        userTable = new JTable(tableModel);
        userTable.setRowHeight(35);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        userTable.setSelectionBackground(new Color(232, 240, 254));
        userTable.setGridColor(new Color(230, 230, 230));
        userTable.setShowGrid(true);
        
        // Apply alternating row colors
        userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 252));
                }
                setBorder(new EmptyBorder(2, 10, 2, 10));
                return c;
            }
        });
        
        // Add sorting capability
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(createRoundedBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(bgColor);
        
        JButton addBtn = createStyledButton("➕ Add User");
        JButton editBtn = createStyledButton("✏️ Edit User");
        JButton deleteBtn = createStyledButton("❌ Delete User");
        
        addBtn.addActionListener(e -> addUser());
        editBtn.addActionListener(e -> editUser());
        deleteBtn.addActionListener(e -> deleteUser());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(bgColor);
        
        // Create statistics grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        statsGrid.setBackground(bgColor);
        statsGrid.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Add statistic cards
        statsGrid.add(createStatCard("User Distribution", "👥", createUserDistributionPanel()));
        statsGrid.add(createStatCard("Recent Activity", "📊", createRecentActivityPanel()));
        statsGrid.add(createStatCard("System Health", "🖥️", createSystemHealthPanel()));
        statsGrid.add(createStatCard("Quick Links", "🔗", createQuickLinksPanel()));
        
        panel.add(statsGrid, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUserDistributionPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 10));
        panel.setBackground(cardBgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Count users by role
        int adminCount = 0, smCount = 0, pmCount = 0, imCount = 0, fmCount = 0;
        
        for (String user : allUsers) {
            String[] parts = user.split(",");
            if (parts.length >= 6) {
                switch (parts[5]) {
                    case "Admin": adminCount++; break;
                    case "SalesManager": smCount++; break;
                    case "PurchaseManager": pmCount++; break;
                    case "InventoryManager": imCount++; break;
                    case "FinanceManager": fmCount++; break;
                }
            }
        }
        
        panel.add(createRoleBar("Admin", adminCount, Color.BLUE));
        panel.add(createRoleBar("Sales", smCount, Color.GREEN));
        panel.add(createRoleBar("Purchase", pmCount, Color.ORANGE));
        panel.add(createRoleBar("Inventory", imCount, Color.RED));
        panel.add(createRoleBar("Finance", fmCount, Color.MAGENTA));
        
        return panel;
    }
    
    private JPanel createRoleBar(String role, int count, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(cardBgColor);
        
        JLabel nameLabel = new JLabel(role);
        nameLabel.setPreferredSize(new Dimension(80, 20));
        
        JProgressBar bar = new JProgressBar(0, Math.max(10, allUsers.size()));
        bar.setValue(count);
        bar.setStringPainted(true);
        bar.setString(String.valueOf(count));
        bar.setForeground(color);
        
        panel.add(nameLabel, BorderLayout.WEST);
        panel.add(bar, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(cardBgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        panel.add(createActivityItem("👤 New user registered", "5 mins ago"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createActivityItem("🔄 System updated", "1 hour ago"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createActivityItem("📊 Reports generated", "3 hours ago"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createActivityItem("⚙️ Settings changed", "Yesterday"));
        
        return panel;
    }
    
    private JPanel createActivityItem(String activity, String time) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cardBgColor);
        
        JLabel activityLabel = new JLabel(activity);
        activityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        timeLabel.setForeground(secondaryTextColor);
        
        panel.add(activityLabel, BorderLayout.NORTH);
        panel.add(timeLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSystemHealthPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 15));
        panel.setBackground(cardBgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        panel.add(createHealthItem("System Status", "Good", Color.GREEN));
        panel.add(createHealthItem("Memory Usage", "45%", Color.ORANGE));
        panel.add(createHealthItem("Disk Space", "30%", Color.GREEN));
        
        return panel;
    }
    
    private JPanel createHealthItem(String name, String status, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(cardBgColor);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(color);
        
        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createQuickLinksPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(cardBgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        panel.add(createQuickLinkButton("User Guide", "📚"));
        panel.add(createQuickLinkButton("Reports", "📊"));
        panel.add(createQuickLinkButton("Settings", "⚙️"));
        panel.add(createQuickLinkButton("Help", "❓"));
        
        return panel;
    }
    
    private JButton createQuickLinkButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.addActionListener(e -> JOptionPane.showMessageDialog(this, text + " coming soon!"));
        return button;
    }
    
    private JPanel createStatCard(String title, String icon, JPanel content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cardBgColor);
        card.setBorder(createRoundedBorder());
        
        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createAuditLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        
        // Create a stylized placeholder
        JPanel logsPanel = new JPanel(new BorderLayout());
        logsPanel.setBackground(cardBgColor);
        logsPanel.setBorder(createRoundedBorder());
        
        // Mock audit log entries
        String[] columnNames = {"Timestamp", "Action", "User", "Details"};
        Object[][] data = {
            {"2023-07-01 09:30", "LOGIN", "admin", "Admin logged in"},
            {"2023-07-01 09:45", "CREATE", "admin", "Created new user 'john'"},
            {"2023-07-01 10:15", "UPDATE", "admin", "Updated user settings"},
            {"2023-07-01 11:20", "EXPORT", "admin", "Exported user data"}
        };
        
        JTable logsTable = new JTable(data, columnNames);
        logsTable.setRowHeight(30);
        logsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        logsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(logsTable);
        logsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add a note
        JLabel noteLabel = new JLabel("Note: This is a preview of the audit logs panel");
        noteLabel.setHorizontalAlignment(JLabel.CENTER);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        noteLabel.setForeground(secondaryTextColor);
        noteLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        logsPanel.add(noteLabel, BorderLayout.SOUTH);
        
        panel.add(logsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(130, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
            }
        });
        
        return button;
    }
    
    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
            new EmptyBorder(3, 3, 3, 3),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 240), 1, true),
                new EmptyBorder(10, 10, 10, 10)
            )
        );
    }

    private void loadUsers() {
        try {
            allUsers = readUserFile("users.txt");
            populateTable(allUsers);
            
            // Refresh stats
            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void writeUserFile(String filename, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void populateTable(List<String> lines) {
        tableModel.setRowCount(0);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                tableModel.addRow(new Object[]{parts[0], parts[1], parts[3], parts[4], parts[5]});
            }
        }
    }
    
    private void filterUsers() {
        String query = searchField.getText().toLowerCase().trim();
        String filterType = (String) searchFilter.getSelectedItem();
        
        if (query.isEmpty()) {
            populateTable(allUsers);
            return;
        }
        
        List<String> filtered = allUsers.stream()
            .filter(line -> {
                String[] parts = line.split(",");
                if (parts.length < 6) return false;
                
                String id = parts[0].toLowerCase();
                String username = parts[1].toLowerCase();
                String email = parts[3].toLowerCase();
                String contact = parts[4].toLowerCase();
                String role = parts[5].toLowerCase();
                
                switch (filterType) {
                    case "Username": return username.contains(query);
                    case "Email": return email.contains(query);
                    case "Role": return role.contains(query);
                    default: return username.contains(query) || 
                                  email.contains(query) || 
                                  role.contains(query) ||
                                  id.contains(query) ||
                                  contact.contains(query);
                }
            })
            .collect(Collectors.toList());
        
        populateTable(filtered);
    }
    
    private void addUser() {
        UserDialog dialog = new UserDialog(this, null);
        if (dialog.showDialog()) {
            String userData = dialog.getUserData();
            try {
                // Append to file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
                    writer.write(userData);
                    writer.newLine();
                }
                
                // Reload data
                loadUsers();
                JOptionPane.showMessageDialog(this, "User added successfully!");
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.",
                                         "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get selected user ID
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        
        UserDialog dialog = new UserDialog(this, userId);
        if (dialog.showDialog()) {
            try {
                // Update user in file
                List<String> lines = readUserFile("users.txt");
                List<String> updated = new ArrayList<>();
                
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6 && parts[0].equals(userId)) {
                        updated.add(dialog.getUserData());
                    } else {
                        updated.add(line);
                    }
                }
                
                writeUserFile("users.txt", updated);
                
                // Reload data
                loadUsers();
                JOptionPane.showMessageDialog(this, "User updated successfully!");
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.",
                                         "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get selected user ID
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this user?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete user from file
                List<String> lines = readUserFile("users.txt");
                List<String> updated = lines.stream()
                    .filter(line -> {
                        String[] parts = line.split(",");
                        return parts.length < 1 || !parts[0].equals(userId);
                    })
                    .collect(Collectors.toList());
                
                writeUserFile("users.txt", updated);
                
                // Reload data
                loadUsers();
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToCSV() {
        try {
            String filename = "exported_users.csv";
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                // Write header
                writer.write("ID,Username,Email,Contact,Role");
                writer.newLine();
                
                // Write data
                for (String line : allUsers) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        writer.write(String.format("%s,%s,%s,%s,%s", 
                                                  parts[0], parts[1], parts[3], parts[4], parts[5]));
                        writer.newLine();
                    }
                }
            }
            
            JOptionPane.showMessageDialog(this, "Users exported to " + filename + " successfully!",
                                         "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting users: " + e.getMessage(),
                                         "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestAdminDashboard dashboard = new TestAdminDashboard();
            dashboard.setVisible(true);
        });
    }
} 