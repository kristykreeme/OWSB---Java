package InventoryManager;

import models.Item;
import utils.ItemFileHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class
InventoryManagerUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private List<Item> items;

    private JLabel totalItemsLabel;
    private JLabel lowStockLabel;

    public InventoryManagerUI() {
        items = new ArrayList<>();

        setTitle("Inventory Management");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xFFF0F5));
        add(mainPanel, BorderLayout.CENTER);

        JPanel metricsPanel = createMetricsPanel();
        mainPanel.add(metricsPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        try {
            items = ItemFileHandler.readItemsFromFile("items.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading items: " + e.getMessage());
        }

        refreshTable();
        updateMetrics();
        setupStatusColumnColoring();

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0xF7C8E0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0xF7C8E0));

        JLabel titleLabel = new JLabel("Inventory Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0x7F5283));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        menuPanel.add(titleLabel);

        String[] menuItems = {"Dashboard", "Item Management", "Reports", "View Approved POs"};
        for (String menuItem : menuItems) {
            JButton button = new JButton(menuItem);
            styleSidebarButton(button);
            button.addActionListener(e -> handleSidebarButton(menuItem));
            menuPanel.add(button);
            menuPanel.add(Box.createVerticalStrut(10));
        }

        JButton logoutButton = new JButton("Logout");
        styleSidebarButton(logoutButton);
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging out...");
            System.exit(0);
        });

        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(logoutButton, BorderLayout.SOUTH);

        return sidebar;
    }

    private void styleSidebarButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0x7F5283));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createLineBorder(new Color(0xF7C8E0)));
    }

    private JPanel createMetricsPanel() {
        JPanel metricsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        metricsPanel.setBackground(new Color(0xFFF0F5));

        totalItemsLabel = createMetricLabel();
        lowStockLabel = createMetricLabel();

        metricsPanel.add(totalItemsLabel);
        metricsPanel.add(lowStockLabel);

        return metricsPanel;
    }

    private JLabel createMetricLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(new Color(0x7F5283));
        label.setBorder(BorderFactory.createLineBorder(new Color(0xF7C8E0)));
        return label;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setBackground(new Color(0xFFF0F5));

        tableModel = new DefaultTableModel(new String[] {
                "Item Code", "Item Name", "Supplier", "Stock Level", "Reorder Level", "Status", "Actions"
        }, 0);

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Action column is editable
            }
        };

        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());

        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(0xFAD4EA));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(0xFFF0F5));

        JButton exportButton = new JButton("Export");
        JButton importButton = new JButton("Import");
        JButton reportButton = new JButton("Generate Report");

        styleMainButton(exportButton);
        styleMainButton(importButton);
        styleMainButton(reportButton);

        exportButton.addActionListener(e -> {
            try {
                ItemFileHandler.writeItemsToFile("items.txt", items);
                JOptionPane.showMessageDialog(this, "Items exported successfully to items.txt");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting items: " + ex.getMessage());
            }
        });

        importButton.addActionListener(e -> {
            try {
                items = ItemFileHandler.readItemsFromFile("items.txt");
                refreshTable();
                updateMetrics();
                setupStatusColumnColoring();
                JOptionPane.showMessageDialog(this, "Items imported successfully from items.txt");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error importing items: " + ex.getMessage());
            }
        });

        reportButton.addActionListener(e -> displayStockReport());

        buttonPanel.add(exportButton);
        buttonPanel.add(importButton);
        buttonPanel.add(reportButton);

        return buttonPanel;
    }

    private void styleMainButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0x7F5283));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorder(BorderFactory.createLineBorder(new Color(0xF7C8E0)));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Item item : items) {
            String status = item.getStockLevel() < item.getReorderLevel() ? "Low Stock" : "OK";
            tableModel.addRow(new Object[] {
                    item.getItemID(),
                    item.getName(),
                    item.getSupplierID(),
                    item.getStockLevel() + " units",
                    item.getReorderLevel() + " units",
                    status,
                    "Edit"
            });
        }
    }

    private void updateMetrics() {
        int totalItems = items.size();
        int lowStockCount = 0;

        for (Item item : items) {
            if (item.getStockLevel() < item.getReorderLevel()) {
                lowStockCount++;
            }
        }

        totalItemsLabel.setText("Total Items: " + totalItems);
        lowStockLabel.setText("Low Stock: " + lowStockCount);
    }

    private void setupStatusColumnColoring() {
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;

                if ("Low Stock".equals(status)) {
                    cell.setForeground(Color.RED);
                } else if ("OK".equals(status)) {
                    cell.setForeground(new Color(0, 150, 0));
                } else {
                    cell.setForeground(Color.BLACK);
                }

                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setBackground(Color.WHITE);
                }

                return cell;
            }
        });
    }

    private void editItem(int rowIndex) {
        Item item = items.get(rowIndex);

        String newStock = JOptionPane.showInputDialog(this, "Enter new stock level:", item.getStockLevel());
        String newReorder = JOptionPane.showInputDialog(this, "Enter new reorder level:", item.getReorderLevel());

        try {
            item.setStockLevel(Integer.parseInt(newStock));
            item.setReorderLevel(Integer.parseInt(newReorder));
            refreshTable();
            updateMetrics();
            setupStatusColumnColoring();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.");
        }
    }

    private void handleSidebarButton(String buttonName) {
        switch (buttonName) {
            case "Dashboard":
                JOptionPane.showMessageDialog(this, "Dashboard coming soon!");
                break;
            case "Item Management":
                JOptionPane.showMessageDialog(this, "Item Management is currently active.");
                break;
            case "Reports":
                displayStockReport();
                break;
            case "View Approved POs":
                JOptionPane.showMessageDialog(this, "Feature not implemented yet: Approved POs.");
                break;
        }
    }

    private void displayStockReport() {
        StringBuilder report = new StringBuilder();
        report.append("*************************************************\n");
        report.append("                   STOCK REPORT                  \n");
        report.append("*************************************************\n\n");

        report.append(String.format("%-10s %-20s %-10s %-10s %-10s\n", "Item ID", "Name", "Stock", "Reorder", "Status"));
        report.append("-------------------------------------------------------------\n");

        int totalStock = 0;
        int lowStockCount = 0;
        for (Item item : items) {
            String status = item.getStockLevel() < item.getReorderLevel() ? "Low Stock" : "OK";
            if ("Low Stock".equals(status)) {
                lowStockCount++;
            }

            totalStock += item.getStockLevel();

            report.append(String.format("%-10s %-20s %-10d %-10d %-10s\n",
                    item.getItemID(),
                    item.getName(),
                    item.getStockLevel(),
                    item.getReorderLevel(),
                    status));
        }

        report.append("\n-------------------------------------------------------------\n");
        report.append(String.format("Total Items: %d\n", items.size()));
        report.append(String.format("Low Stock Items: %d\n", lowStockCount));
        report.append(String.format("Total Stock Count: %d\n", totalStock));
        report.append("*************************************************\n");

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        JDialog reportDialog = new JDialog(this, "Stock Report", true);
        reportDialog.setSize(700, 500);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.add(scrollPane);
        reportDialog.setVisible(true);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(0xFAD4EA));
            setForeground(new Color(0x7F5283));
            setFont(new Font("Arial", Font.BOLD, 12));
            setFocusPainted(false);
            setBorder(BorderFactory.createLineBorder(new Color(0xF7C8E0)));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText("Edit");
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private int row;

        public ButtonEditor() {
            button = new JButton("Edit");
            button.setBackground(new Color(0xFAD4EA));
            button.setForeground(new Color(0x7F5283));
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(new Color(0xF7C8E0)));
            button.addActionListener(e -> {
                editItem(row);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Edit";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagerUI::new);
    }
}