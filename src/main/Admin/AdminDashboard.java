package Admin;

import models.User;
import utils.Constants;
import utils.FileHandler;
import utils.AuditLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDashboard extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel adminCount, smCount, pmCount, imCount, fmCount;
    private List<String> allUsers = new java.util.ArrayList<>();

    public AdminDashboard() {
        setTitle("Admin Dashboard - OWSB");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== Top Panel: Stats + Search =====
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        adminCount = new JLabel();
        smCount = new JLabel();
        pmCount = new JLabel();
        imCount = new JLabel();
        fmCount = new JLabel();
        statsPanel.add(adminCount);
        statsPanel.add(smCount);
        statsPanel.add(pmCount);
        statsPanel.add(imCount);
        statsPanel.add(fmCount);
        topPanel.add(statsPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.setToolTipText("Search by username or role");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterUsers(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterUsers(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterUsers(); }
        });
        searchPanel.add(new JLabel(" Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== Table Panel =====
        tableModel = new DefaultTableModel(new Object[]{"ID", "Username", "Email", "Contact", "Role"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Bottom Buttons Panel =====
        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton addBtn = new JButton("Add User");
        JButton editBtn = new JButton("Edit User");
        JButton deleteBtn = new JButton("Delete User");
        JButton exportBtn = new JButton("Export CSV");

        addBtn.addActionListener(e -> new RegisterUserUI(this, null).setVisible(true));
        editBtn.addActionListener(e -> editSelectedUser());
        deleteBtn.addActionListener(e -> deleteSelectedUser());
        exportBtn.addActionListener(e -> exportToCSV());

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(exportBtn);

        add(btnPanel, BorderLayout.SOUTH);

        refreshUserList();
    }

    public void refreshUserList() {
        try {
            allUsers = FileHandler.readLines(Constants.USER_FILE);
            updateStats(allUsers);
            populateTable(allUsers);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users.");
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
        String query = searchField.getText().trim().toLowerCase();
        List<String> filtered = allUsers.stream()
                .filter(line -> {
                    String[] parts = line.split(",");
                    if (parts.length < 6) return false;
                    String username = parts[1].toLowerCase();
                    String role = parts[5].toLowerCase();
                    return username.contains(query)
                            || role.contains(query)
                            || role.startsWith(getRoleKeyword(query));
                })
                .collect(Collectors.toList());
        populateTable(filtered);
    }

    private String getRoleKeyword(String query) {
        return switch (query) {
            case "admin", "a" -> "admin";
            case "sm", "sales" -> "salesmanager";
            case "pm", "purchase" -> "purchasemanager";
            case "im", "inventory" -> "inventorymanager";
            case "fm", "finance" -> "financemanager";
            default -> query;
        };
    }

    private void updateStats(List<String> lines) {
        int admin = 0, sm = 0, pm = 0, im = 0, fm = 0;
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                switch (parts[5]) {
                    case "Admin" -> admin++;
                    case "SalesManager" -> sm++;
                    case "PurchaseManager" -> pm++;
                    case "InventoryManager" -> im++;
                    case "FinanceManager" -> fm++;
                }
            }
        }
        adminCount.setText("👤 Admins: " + admin);
        smCount.setText("🛍️ Sales: " + sm);
        pmCount.setText("📦 Purchase: " + pm);
        imCount.setText("🏷️ Inventory: " + im);
        fmCount.setText("💰 Finance: " + fm);
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first.");
            return;
        }
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        new RegisterUserUI(this, userId).setVisible(true);
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.");
            return;
        }

        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        try {
            List<String> updated = allUsers.stream()
                    .filter(line -> !line.startsWith(userId + ","))
                    .collect(Collectors.toList());
            FileHandler.writeToFile(Constants.USER_FILE, String.join("\n", updated), false);
            AuditLogger.log("A001", "DELETE", userId); // replace with actual Admin ID if tracked
            refreshUserList();
            JOptionPane.showMessageDialog(this, "User deleted.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete.");
        }
    }

    private void exportToCSV() {
        try {
            String filePath = "exported_users.csv";
            List<String> lines = FileHandler.readLines(Constants.USER_FILE);
            List<String> csv = new java.util.ArrayList<>();
            csv.add("ID,Username,Email,Contact,Role");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    csv.add(String.join(",", parts[0], parts[1], parts[3], parts[4], parts[5]));
                }
            }
            FileHandler.writeToFile(filePath, String.join("\n", csv), false);
            JOptionPane.showMessageDialog(this, "Exported to " + filePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
        }
    }
}
