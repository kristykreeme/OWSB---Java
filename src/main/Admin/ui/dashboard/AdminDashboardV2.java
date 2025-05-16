package Admin;

import models.User;
import utils.Constants;
import utils.FileHandler;
import utils.AuditLogger;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardV2 extends JFrame {
    private JPanel mainContent;
    private CardLayout cardLayout;
    private Map<String, JPanel> cards;
    private JLabel statusLabel;
    private Color primaryColor = new Color(47, 128, 237);    // Blue
    private Color secondaryColor = new Color(45, 55, 72);    // Dark Gray
    private Color accentColor = new Color(237, 100, 47);     // Orange
    private Color bgColor = new Color(247, 250, 252);        // Light Gray
    private Color textColor = new Color(45, 55, 72);         // Dark Gray
    private Font titleFont = new Font("Arial", Font.BOLD, 24);
    private Font menuFont = new Font("Arial", Font.PLAIN, 14);
    private Font statusFont = new Font("Arial", Font.ITALIC, 12);

    public AdminDashboardV2() {
        setTitle("OWSB Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        cards = new HashMap<>();
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(bgColor);

        // Create layout
        setLayout(new BorderLayout());
        createHeader();
        createSidebar();
        createMainContent();
        createStatusBar();

        // Show default card
        cardLayout.show(mainContent, "Overview");
    }

    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primaryColor);
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Logo/Title
        JLabel title = new JLabel("OWSB Admin Dashboard");
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        // User info & logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Admin User");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(menuFont);
        
        JButton logoutBtn = new JButton("Logout");
        styleButton(logoutBtn);
        logoutBtn.addActionListener(e -> logout());

        userPanel.add(userLabel);
        userPanel.add(logoutBtn);
        header.add(userPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(secondaryColor);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        String[] menuItems = {
            "Overview", "User Management", "Role Permissions",
            "System Settings", "Audit Logs", "Reports"
        };

        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(sidebar, BorderLayout.WEST);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFont(menuFont);
        button.setForeground(Color.WHITE);
        button.setBackground(secondaryColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);

        // Add icon based on menu item
        String iconPath = getIconPath(text);
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            button.setIcon(icon);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(primaryColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(secondaryColor);
            }
        });

        button.addActionListener(e -> cardLayout.show(mainContent, text));
        return button;
    }

    private String getIconPath(String menuItem) {
        // TODO: Add actual icons
        return null;
    }

    private void createMainContent() {
        // Overview Card
        cards.put("Overview", createOverviewPanel());
        
        // User Management Card
        cards.put("User Management", createUserManagementPanel());
        
        // Role Permissions Card
        JPanel rolePermPanel = (JPanel) new RolePermissionsManager().getContentPane();
        cards.put("Role Permissions", rolePermPanel);
        
        // System Settings Card
        JPanel settingsPanel = (JPanel) new SystemSettingsManager().getContentPane();
        cards.put("System Settings", settingsPanel);
        
        // Audit Logs Card
        JPanel auditPanel = (JPanel) new AuditLogViewer().getContentPane();
        cards.put("Audit Logs", auditPanel);
        
        // Reports Card
        cards.put("Reports", createReportsPanel());

        // Add all cards to main content
        for (Map.Entry<String, JPanel> entry : cards.entrySet()) {
            mainContent.add(entry.getValue(), entry.getKey());
        }

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Add stat cards
        String[] stats = {"Total Users", "Active Sessions", "System Health", "Recent Activities"};
        Color[] cardColors = {
            new Color(34, 139, 230),  // Blue
            new Color(72, 187, 120),  // Green
            new Color(237, 137, 54),  // Orange
            new Color(113, 128, 150)  // Gray
        };

        int row = 0;
        int col = 0;
        for (int i = 0; i < stats.length; i++) {
            JPanel card = createStatCard(stats[i], "123", cardColors[i]);
            gbc.gridx = col;
            gbc.gridy = row;
            gbc.weightx = 0.5;
            gbc.weighty = 0.5;
            panel.add(card, gbc);
            
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(textColor);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(bgColor);
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        styleButton(searchBtn);
        searchPanel.add(new JLabel("Search Users: "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Table
        String[] columns = {"ID", "Username", "Email", "Role", "Status"};
        Object[][] data = {
            {"A001", "admin1", "admin1@owsb.com", "Admin", "Active"},
            {"A002", "admin2", "admin2@owsb.com", "Admin", "Active"}
        };
        JTable userTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(bgColor);
        JButton addBtn = new JButton("Add User");
        JButton editBtn = new JButton("Edit User");
        JButton deleteBtn = new JButton("Delete User");
        styleButton(addBtn);
        styleButton(editBtn);
        styleButton(deleteBtn);
        buttonsPanel.add(addBtn);
        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bgColor);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Report Types Panel
        JPanel reportTypesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reportTypesPanel.setBackground(bgColor);
        String[] reportTypes = {"User Activity", "System Performance", "Audit Trail", "Custom Report"};
        JComboBox<String> reportTypeCombo = new JComboBox<>(reportTypes);
        reportTypesPanel.add(new JLabel("Report Type: "));
        reportTypesPanel.add(reportTypeCombo);

        // Date Range Panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(bgColor);
        datePanel.add(new JLabel("Date Range: "));
        datePanel.add(new JTextField("Start Date", 10));
        datePanel.add(new JTextField("End Date", 10));

        // Options Panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        optionsPanel.setBackground(bgColor);
        JButton generateBtn = new JButton("Generate Report");
        JButton exportBtn = new JButton("Export");
        styleButton(generateBtn);
        styleButton(exportBtn);
        optionsPanel.add(generateBtn);
        optionsPanel.add(exportBtn);

        // Combine top panels
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setBackground(bgColor);
        topPanel.add(reportTypesPanel);
        topPanel.add(datePanel);
        topPanel.add(optionsPanel);

        // Preview Area
        JTextArea previewArea = new JTextArea();
        previewArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(previewArea);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(statusFont);
        statusLabel.setForeground(textColor);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setBackground(primaryColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(menuFont);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
            }
        });
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginUI().setVisible(true);
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
} 