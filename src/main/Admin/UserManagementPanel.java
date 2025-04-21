package main.Admin;

import main.models.User;
import main.utils.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Manage Users");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ====== Table Panel ======
        tableModel = new DefaultTableModel(new String[]{"User ID", "Username", "Role"}, 0);
        userTable = new JTable(tableModel);
        loadUsers();

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        add(scrollPane, BorderLayout.CENTER);

        // ====== Form Panel ======
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        formPanel.setBackground(new Color(245, 245, 245));

        JTextField idField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Admin", "SalesManager", "PurchaseManager", "InventoryManager", "FinanceManager"});

        formPanel.add(new JLabel("User ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleBox);

        JButton addBtn = new JButton("Add User");
        JButton deleteBtn = new JButton("Delete Selected");

        formPanel.add(addBtn);
        formPanel.add(deleteBtn);
        add(formPanel, BorderLayout.SOUTH);

        // ====== Button Actions ======
        addBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (id.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            FileUtils.addUser(new User(id, username, password, role));
            tableModel.addRow(new Object[]{id, username, role});
            idField.setText(""); usernameField.setText(""); passwordField.setText("");
        });

        deleteBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row >= 0) {
                String idToDelete = (String) tableModel.getValueAt(row, 0);
                FileUtils.deleteUserById(idToDelete);
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            }
        });
    }

    private void loadUsers() {
        ArrayList<User> users = FileUtils.readAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole()});
        }
    }
}
