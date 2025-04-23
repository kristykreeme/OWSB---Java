
package InventoryManager;

import models.Item;
import utils.ItemFileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryManagerUI extends JFrame {
    private InventoryManager inventoryManager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel totalItemsLabel;
    private JLabel lowStockLabel;

    public InventoryManagerUI() {
        inventoryManager = new InventoryManager();
        setTitle("Inventory Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Summary Panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalItemsLabel = new JLabel("Total Items: 0");
        lowStockLabel = new JLabel("Low Stock: 0");
        summaryPanel.add(totalItemsLabel);
        summaryPanel.add(lowStockLabel);
        add(summaryPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Item ID", "Item Name", "Supplier ID", "Stock Level", "Status"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Input Fields Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField supplierField = new JTextField();
        JTextField stockField = new JTextField();

        inputPanel.add(new JLabel("Item ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Supplier ID:"));
        inputPanel.add(supplierField);
        inputPanel.add(new JLabel("Stock Level:"));
        inputPanel.add(stockField);
        add(inputPanel, BorderLayout.SOUTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loadButton = new JButton("Load Items");
        JButton saveButton = new JButton("Save Items");
        JButton addButton = new JButton("Add Item");
        JButton reportButton = new JButton("Generate Stock Report");
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(addButton);
        buttonPanel.add(reportButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        // Action Listeners
        loadButton.addActionListener(e -> {
            inventoryManager.loadItemsFromFile("src/main/InventoryManager/items.txt");
            refreshTable();
            JOptionPane.showMessageDialog(this, "Items loaded from file.");
        });

        saveButton.addActionListener(e -> {
            inventoryManager.saveItemsToFile("src/main/InventoryManager/items.txt");
            JOptionPane.showMessageDialog(this, "Items saved to file.");
        });

        addButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                String supplier = supplierField.getText();
                int stock = Integer.parseInt(stockField.getText());

                Item item = new Item(id, name, supplier, stock);
                inventoryManager.addItem(item);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Item added.");

                idField.setText("");
                nameField.setText("");
                supplierField.setText("");
                stockField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for stock level.");
            }
        });

        reportButton.addActionListener(e -> {
            inventoryManager.generateStockReport("stock_report.csv");
            JOptionPane.showMessageDialog(this, "Stock report generated.");
        });

        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Item> items = inventoryManager.getItems();
        int lowStockCount = 0;
        for (Item item : items) {
            String status = item.getStockLevel() < 60 ? "Low Stock" : "OK";
            if (status.equals("Low Stock")) lowStockCount++;
            tableModel.addRow(new Object[]{
                    item.getItemID(),
                    item.getName(),
                    item.getSupplierID(),
                    item.getStockLevel(),
                    status
            });
        }
        totalItemsLabel.setText("Total Items: " + items.size());
        lowStockLabel.setText("Low Stock: " + lowStockCount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagerUI::new);
    }
}