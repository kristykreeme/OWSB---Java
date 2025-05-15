package PurchaseManager;

import models.Item;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PurchaseManagerGUI extends JFrame {

    public PurchaseManagerGUI() {
        setTitle("Purchase Manager Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Purchase Manager Panel", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnViewItems = new JButton("View Items");
        JButton btnViewSuppliers = new JButton("View Suppliers");
        JButton btnViewPRs = new JButton("View Purchase Requisitions");
        JButton btnGeneratePO = new JButton("Generate Purchase Order");
        JButton btnViewPOs = new JButton("View Purchase Orders");

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        panel.add(btnViewItems);
        panel.add(btnViewSuppliers);
        panel.add(btnViewPRs);
        panel.add(btnGeneratePO);
        panel.add(btnViewPOs);

        add(titleLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Only View Items is working for now
        btnViewItems.addActionListener(e -> viewItems());

        // Placeholder messages
        btnViewSuppliers.addActionListener(e -> JOptionPane.showMessageDialog(this, "View Suppliers not implemented yet."));
        btnViewPRs.addActionListener(e -> JOptionPane.showMessageDialog(this, "View PRs not implemented yet."));
        btnGeneratePO.addActionListener(e -> JOptionPane.showMessageDialog(this, "Generate PO not implemented yet."));
        btnViewPOs.addActionListener(e -> JOptionPane.showMessageDialog(this, "View POs not implemented yet."));
    }

    private List<Item> loadItemsFromFile() {
        List<Item> items = new ArrayList<>();

        try {
            File file = new File("data/items.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Item item = Item.fromFileString(line);
                if (item != null) {
                    items.add(item);
                }
            }

            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading items: " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    private void viewItems() {
        List<Item> items = loadItemsFromFile();

        String[] columnNames = { "Item ID", "Name", "Supplier ID", "Stock", "Reorder Level" };
        String[][] data = new String[items.size()][5];

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            data[i][0] = item.getItemID();
            data[i][1] = item.getName();
            data[i][2] = item.getSupplierID();
            data[i][3] = String.valueOf(item.getStockLevel());
            data[i][4] = String.valueOf(item.getReorderLevel());
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JOptionPane.showMessageDialog(this, scrollPane, "List of Items", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PurchaseManagerGUI().setVisible(true));
    }
}
