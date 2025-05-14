package Admin;

import models.User;
import utils.Constants;
import utils.FileHandler;
import utils.AuditLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RolePermissionsManager extends JFrame {
    private JTable permissionsTable;
    private DefaultTableModel tableModel;
    private Map<String, Set<String>> rolePermissions;
    private static final String[] DEFAULT_PERMISSIONS = {
        "VIEW_USERS", "ADD_USER", "EDIT_USER", "DELETE_USER",
        "VIEW_INVENTORY", "MANAGE_INVENTORY",
        "VIEW_SALES", "MANAGE_SALES",
        "VIEW_PURCHASES", "MANAGE_PURCHASES",
        "VIEW_FINANCE", "MANAGE_FINANCE",
        "VIEW_REPORTS", "EXPORT_DATA",
        "SYSTEM_SETTINGS"
    };

    public RolePermissionsManager() {
        setTitle("Role Permissions Manager - OWSB");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        loadPermissions();
    }

    private void initializeComponents() {
        // Create table model with role columns
        String[] roles = {"Admin", "SalesManager", "PurchaseManager", "InventoryManager", "FinanceManager"};
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Permission");
        for (String role : roles) {
            tableModel.addColumn(role);
        }

        permissionsTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? String.class : Boolean.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };

        JScrollPane scrollPane = new JScrollPane(permissionsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add save button
        JButton saveButton = new JButton("Save Permissions");
        saveButton.addActionListener(e -> savePermissions());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadPermissions() {
        rolePermissions = new HashMap<>();
        try {
            List<String> lines = FileHandler.readLines(Constants.PERMISSIONS_FILE);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String role = parts[0];
                    Set<String> permissions = new HashSet<>(Arrays.asList(parts).subList(1, parts.length));
                    rolePermissions.put(role, permissions);
                }
            }
        } catch (Exception e) {
            // If file doesn't exist, initialize with default permissions
            initializeDefaultPermissions();
        }
        updateTable();
    }

    private void initializeDefaultPermissions() {
        rolePermissions = new HashMap<>();
        
        // Admin has all permissions
        Set<String> adminPerms = new HashSet<>(Arrays.asList(DEFAULT_PERMISSIONS));
        rolePermissions.put("Admin", adminPerms);

        // Other roles get specific permissions
        rolePermissions.put("SalesManager", new HashSet<>(Arrays.asList(
            "VIEW_SALES", "MANAGE_SALES", "VIEW_INVENTORY", "VIEW_REPORTS")));
        
        rolePermissions.put("PurchaseManager", new HashSet<>(Arrays.asList(
            "VIEW_PURCHASES", "MANAGE_PURCHASES", "VIEW_INVENTORY", "VIEW_REPORTS")));
        
        rolePermissions.put("InventoryManager", new HashSet<>(Arrays.asList(
            "VIEW_INVENTORY", "MANAGE_INVENTORY", "VIEW_REPORTS")));
        
        rolePermissions.put("FinanceManager", new HashSet<>(Arrays.asList(
            "VIEW_FINANCE", "MANAGE_FINANCE", "VIEW_REPORTS", "EXPORT_DATA")));
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (String permission : DEFAULT_PERMISSIONS) {
            Object[] row = new Object[6];
            row[0] = permission;
            int col = 1;
            for (String role : new String[]{"Admin", "SalesManager", "PurchaseManager", "InventoryManager", "FinanceManager"}) {
                row[col++] = rolePermissions.getOrDefault(role, new HashSet<>()).contains(permission);
            }
            tableModel.addRow(row);
        }
    }

    private void savePermissions() {
        try {
            List<String> lines = new ArrayList<>();
            String[] roles = {"Admin", "SalesManager", "PurchaseManager", "InventoryManager", "FinanceManager"};
            
            for (String role : roles) {
                StringBuilder line = new StringBuilder(role);
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String permission = (String) tableModel.getValueAt(i, 0);
                    Boolean hasPermission = (Boolean) tableModel.getValueAt(i, Arrays.asList(roles).indexOf(role) + 1);
                    if (hasPermission) {
                        line.append(",").append(permission);
                    }
                }
                lines.add(line.toString());
            }
            
            FileHandler.writeToFile(Constants.PERMISSIONS_FILE, String.join("\n", lines), false);
            AuditLogger.log("A001", "UPDATE", "Updated role permissions");
            JOptionPane.showMessageDialog(this, "Permissions saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving permissions: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean hasPermission(String role, String permission) {
        try {
            List<String> lines = FileHandler.readLines(Constants.PERMISSIONS_FILE);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equals(role)) {
                    return Arrays.asList(parts).contains(permission);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} 