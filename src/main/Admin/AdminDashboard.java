package Admin;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AdminDashboard - Administrative interface for the OWSB system.
 * Handles user management, system monitoring, and audit logging.
 */
public class AdminDashboard extends JFrame {
    // UI Constants
    private static final Color PRIMARY_COLOR = new Color(66, 134, 244);
    private static final Color ACCENT_COLOR = new Color(33, 67, 122);
    private static final Color BG_COLOR = new Color(245, 248, 252);
    private static final Color CARD_BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color SECONDARY_TEXT_COLOR = new Color(100, 100, 100);
    private static final String USERS_FILE = "users.txt";

    // UI Components
    private final JTable userTable;
    private final DefaultTableModel tableModel;
    private final List<String> allUsers;
    private final JTabbedPane tabbedPane;
    private final JTextField searchField;
    private final JComboBox<String> searchFilter;

    // Static login components
    private static JFrame loginFrame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;

    /**
     * Shows the login dialog for admin access
     */
    public static void showLoginDialog() {
        loginFrame = new JFrame("OWSB System Login");
        loginFrame.setSize(400, 500);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(CARD_BG_COLOR);

        // Add title panel
        mainPanel.add(createLoginTitlePanel(), BorderLayout.NORTH);
        
        // Add login form
        mainPanel.add(createLoginFormPanel(), BorderLayout.CENTER);

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);
    }

    /**
     * Creates the title panel for the login screen
     */
    private static JPanel createLoginTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout(10, 10));
        titlePanel.setBackground(CARD_BG_COLOR);

        JLabel titleLabel = new JLabel("OWSB System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(ACCENT_COLOR);

        JLabel subtitleLabel = new JLabel("Admin Login");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        subtitleLabel.setForeground(SECONDARY_TEXT_COLOR);

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        return titlePanel;
    }

    /**
     * Creates the login form panel
     */
    private static JPanel createLoginFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Create form components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // Style components
        styleLoginComponents(usernameField, passwordField, loginButton);

        // Add components to form
        addLoginFormComponents(formPanel, gbc, usernameField, passwordField, loginButton);

        return formPanel;
    }

    /**
     * Styles the login form components
     */
    private static void styleLoginComponents(JTextField username, JPasswordField password, JButton loginBtn) {
        username.setPreferredSize(new Dimension(200, 35));
        password.setPreferredSize(new Dimension(200, 35));
        loginBtn.setBackground(PRIMARY_COLOR);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(200, 40));
        loginBtn.addActionListener(e -> performLogin());
    }

    /**
     * Adds components to the login form
     */
    private static void addLoginFormComponents(JPanel panel, GridBagConstraints gbc,
                                            JTextField username, JPasswordField password, JButton loginBtn) {
        gbc.gridy = 0;
        panel.add(new JLabel("Username"), gbc);
        gbc.gridy = 1;
        panel.add(username, gbc);
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy = 3;
        panel.add(new JLabel("Password"), gbc);
        gbc.gridy = 4;
        panel.add(password, gbc);
        gbc.gridy = 5;
        panel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy = 6;
        panel.add(loginBtn, gbc);
    }

    /**
     * Handles the login process
     */
    private static void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateLogin(username, password)) {
            loginFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                AdminDashboard dashboard = new AdminDashboard();
                dashboard.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(loginFrame,
                "Invalid username or password",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Validates user credentials
     */
    private static boolean validateLogin(String username, String password) {
        try {
            List<String> lines = readFile(USERS_FILE);
            return lines.stream()
                .map(line -> line.split(","))
                .anyMatch(parts -> parts.length >= 6 &&
                    parts[1].equals(username) &&
                    parts[2].equals(password) &&
                    parts[5].equals("Admin"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Constructor for the main dashboard
     */
    private AdminDashboard() {
        setTitle("Admin Dashboard - OWSB");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        allUsers = new ArrayList<>();
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Username", "Email", "Contact", "Role"}, 0
        );
        userTable = new JTable(tableModel);
        tabbedPane = new JTabbedPane();
        searchField = new JTextField(20);
        searchFilter = new JComboBox<>(new String[]{"All Fields", "Username", "Email", "Role"});

        // Setup UI
        setupMainUI();
        
        // Load initial data
        loadUsers();
    }

    /**
     * Sets up the main dashboard UI
     */
    private void setupMainUI() {
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContent.setBackground(BG_COLOR);

        // Setup tabbed pane
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setBackground(BG_COLOR);

        // Add tabs
        tabbedPane.addTab("👤 User Management", createUserManagementPanel());
        tabbedPane.addTab("📊 System Statistics", createStatisticsPanel());
        tabbedPane.addTab("📋 Audit Logs", createAuditLogsPanel());

        mainContent.add(createHeader(), BorderLayout.NORTH);
        mainContent.add(tabbedPane, BorderLayout.CENTER);

        add(mainContent);
    }

    /**
     * Reads lines from a file
     */
    private static List<String> readFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Creates the header panel for the dashboard
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(BG_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Title section
        JLabel titleLabel = new JLabel("OWSB Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(ACCENT_COLOR);

        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BG_COLOR);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchFilter);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        return header;
    }

    /**
     * Creates the user management panel
     */
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table setup
        userTable.setFillsViewportHeight(true);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setBackground(BG_COLOR);

        JButton addButton = createStyledButton("Add User", "➕");
        JButton editButton = createStyledButton("Edit User", "✏️");
        JButton deleteButton = createStyledButton("Delete User", "🗑️");
        JButton refreshButton = createStyledButton("Refresh", "🔄");

        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(refreshButton);

        // Add action listeners
        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> showEditUserDialog());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> loadUsers());

        panel.add(actionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the statistics panel
     */
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add statistic cards
        panel.add(createStatCard("Total Users", "👥", String.valueOf(allUsers.size())));
        panel.add(createStatCard("Active Sessions", "🔵", "12"));
        panel.add(createStatCard("System Uptime", "⏱️", "24h 35m"));
        panel.add(createStatCard("Pending Tasks", "📋", "5"));

        return panel;
    }

    /**
     * Creates the audit logs panel
     */
    private JPanel createAuditLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create table model for audit logs
        DefaultTableModel logsModel = new DefaultTableModel(
            new Object[]{"Timestamp", "User", "Action", "Details"}, 0
        );
        JTable logsTable = new JTable(logsModel);
        JScrollPane scrollPane = new JScrollPane(logsTable);

        // Add some sample data
        logsModel.addRow(new Object[]{"2024-03-21 10:30", "admin", "Login", "Successful login"});
        logsModel.addRow(new Object[]{"2024-03-21 10:35", "admin", "User Created", "Created user: john_doe"});
        logsModel.addRow(new Object[]{"2024-03-21 11:00", "admin", "System Update", "Updated user permissions"});

        // Add filter controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(BG_COLOR);
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(new JComboBox<>(new String[]{"All", "Login", "User Management", "System Updates"}));

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Loads users from the file into the table
     */
    private void loadUsers() {
        tableModel.setRowCount(0);
        allUsers.clear();
        
        try {
            List<String> lines = readFile(USERS_FILE);
            for (String line : lines) {
                allUsers.add(line);
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    tableModel.addRow(new Object[]{
                        parts[0], // ID
                        parts[1], // Username
                        parts[3], // Email
                        parts[4], // Contact
                        parts[5]  // Role
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading users: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a styled button with an icon
     */
    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    /**
     * Creates a statistics card panel
     */
    private JPanel createStatCard(String title, String icon, String value) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(SECONDARY_TEXT_COLOR);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(TEXT_COLOR);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Performs search based on the search field and filter
     */
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase();
        String filterType = (String) searchFilter.getSelectedItem();

        tableModel.setRowCount(0);
        for (String user : allUsers) {
            String[] parts = user.split(",");
            if (parts.length >= 6) {
                boolean matches = false;
                if ("All Fields".equals(filterType)) {
                    matches = String.join(",", parts).toLowerCase().contains(searchText);
                } else {
                    int columnIndex = getColumnIndexForFilter(filterType);
                    matches = parts[columnIndex].toLowerCase().contains(searchText);
                }
                if (matches) {
                    tableModel.addRow(new Object[]{
                        parts[0], parts[1], parts[3], parts[4], parts[5]
                    });
                }
            }
        }
    }

    /**
     * Gets the column index for the selected filter
     */
    private int getColumnIndexForFilter(String filterType) {
        switch (filterType) {
            case "Username": return 1;
            case "Email": return 3;
            case "Role": return 5;
            default: return 1;
        }
    }

    /**
     * Shows dialog for adding a new user
     */
    private void showAddUserDialog() {
        // Implementation for adding new user
        JOptionPane.showMessageDialog(this,
            "Add user functionality to be implemented",
            "Not Implemented",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows dialog for editing selected user
     */
    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Implementation for editing user
        JOptionPane.showMessageDialog(this,
            "Edit user functionality to be implemented",
            "Not Implemented",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Deletes the selected user
     */
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Implementation for deleting user
        JOptionPane.showMessageDialog(this,
            "Delete user functionality to be implemented",
            "Not Implemented",
            JOptionPane.INFORMATION_MESSAGE);
    }
} 