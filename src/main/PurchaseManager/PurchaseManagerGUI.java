package PurchaseManager;

import models.Item;
import models.Supplier;
import models.PR;
import models.PO;

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

        btnViewItems.addActionListener(e -> viewItems());

        btnViewSuppliers.addActionListener(e -> viewSuppliers());
        btnViewPRs.addActionListener(e -> viewPRs());
        btnGeneratePO.addActionListener(e -> generatePO());
        btnViewPOs.addActionListener(e -> viewPOs());

    }
    //Load items from items.txt
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

    //Display items in a table
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

    private List<Supplier> loadSuppliersFromFile() {
        List<Supplier> suppliers = new ArrayList<>();

        try {
            File file = new File("data/suppliers.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Supplier supplier = Supplier.fromFileString(line);
                if (supplier != null) {
                    suppliers.add(supplier);
                }
            }

            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage());
            e.printStackTrace();
        }

        return suppliers;
    }
    //Display Supplier list
    private void viewSuppliers() {
        List<Supplier> suppliers = loadSuppliersFromFile();

        String[] columnNames = { "Supplier ID", "Supplier Name" };
        String[][] data = new String[suppliers.size()][2];

        for (int i = 0; i < suppliers.size(); i++) {
            Supplier s = suppliers.get(i);
            data[i][0] = s.getSupplierID();
            data[i][1] = s.getSupplierName();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JOptionPane.showMessageDialog(this, scrollPane, "List of Suppliers", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PurchaseManagerGUI().setVisible(true));
    }

    private List<PR> loadPRsFromFile() {
        List<PR> prList = new ArrayList<>();

        try {
            File file = new File("data/prs.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                PR pr = PR.fromFileString(line);
                if (pr != null) {
                    prList.add(pr);
                }
            }

            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading PRs: " + e.getMessage());
            e.printStackTrace();
        }

        return prList;
    }
    //Display PRs in the table
    private void viewPRs() {
        List<PR> prList = loadPRsFromFile();

        String[] columnNames = { "PR ID", "Item ID", "Quantity", "Required Date", "Raised By" };
        String[][] data = new String[prList.size()][5];

        for (int i = 0; i < prList.size(); i++) {
            PR pr = prList.get(i);
            data[i][0] = pr.getPrID();
            data[i][1] = pr.getItemID();
            data[i][2] = String.valueOf(pr.getQuantity());
            data[i][3] = pr.getRequiredDate();
            data[i][4] = pr.getRaisedBy();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JOptionPane.showMessageDialog(this, scrollPane, "List of Purchase Requisitions", JOptionPane.INFORMATION_MESSAGE);
    }

    //Generate a new PO ID
    private String generateNextPOID() {
        List<PO> poList = loadPOsFromFile();
        int maxID = 0;
        for (PO po : poList) {
            String idNumStr = po.getPoID().replaceAll("[^0-9]", "");
            try {
                int idNum = Integer.parseInt(idNumStr);
                if (idNum > maxID) {
                    maxID = idNum;
                }
            } catch (NumberFormatException e) {
                // ignore invalid format
            }
        }
        return String.format("PO%03d", maxID + 1);
    }

    private List<PO> loadPOsFromFile() {
        List<PO> poList = new ArrayList<>();
        try {
            File file = new File("data/purchase_orders.txt");
            if (!file.exists()) return poList;  // empty list if file missing
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                PO po = PO.fromFileString(line);
                if (po != null) {
                    poList.add(po);
                }
            }
            scanner.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading POs: " + e.getMessage());
            e.printStackTrace();
        }
        return poList;
    }

    private void viewPOs() {
        List<PO> poList = loadPOsFromFile();
        if (poList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Purchase Orders found.");
            return;
        }

        String[] columnNames = { "PO ID", "PR ID", "Item ID", "Quantity", "Supplier ID", "Purchase Manager ID" };
        String[][] data = new String[poList.size()][6];

        for (int i = 0; i < poList.size(); i++) {
            PO po = poList.get(i);
            data[i][0] = po.getPoID();
            data[i][1] = po.getPrID();
            data[i][2] = po.getItemID();
            data[i][3] = String.valueOf(po.getQuantity());
            data[i][4] = po.getSupplierID();
            data[i][5] = po.getPurchaseManagerID();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JOptionPane.showMessageDialog(this, scrollPane, "List of Purchase Orders", JOptionPane.INFORMATION_MESSAGE);
    }


    private void savePO(PO po) {
        try (java.io.FileWriter fw = new java.io.FileWriter("data/purchase_orders.txt", true)) {
            fw.write(po.toFileString() + "\n");
            JOptionPane.showMessageDialog(this, "PO saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving PO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generatePO() {
        List<PR> prList = loadPRsFromFile();
        if (prList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Purchase Requisitions available to generate PO.");
            return;
        }

        String[] prOptions = new String[prList.size()];
        for (int i = 0; i < prList.size(); i++) {
            PR pr = prList.get(i);
            prOptions[i] = pr.getPrID() + " - ItemID: " + pr.getItemID() + ", Qty: " + pr.getQuantity();
        }

        String selectedPR = (String) JOptionPane.showInputDialog(this,
                "Select a Purchase Requisition to generate PO:",
                "Generate Purchase Order",
                JOptionPane.PLAIN_MESSAGE,
                null,
                prOptions,
                prOptions[0]);

        if (selectedPR == null) return; // Cancelled

        PR pr = null;
        for (PR p : prList) {
            if (selectedPR.startsWith(p.getPrID())) {
                pr = p;
                break;
            }
        }

        if (pr == null) {
            JOptionPane.showMessageDialog(this, "Invalid selection.");
            return;
        }

        Item item = null;
        for (Item it : loadItemsFromFile()) {
            if (it.getItemID().equals(pr.getItemID())) {
                item = it;
                break;
            }
        }

        if (item == null) {
            JOptionPane.showMessageDialog(this, "Item not found for selected PR.");
            return;
        }

        String poID = generateNextPOID();

        JTextField poIDField = new JTextField(poID);
        poIDField.setEditable(false);
        JTextField prIDField = new JTextField(pr.getPrID());
        prIDField.setEditable(false);
        JTextField itemIDField = new JTextField(pr.getItemID());
        itemIDField.setEditable(false);
        JTextField quantityField = new JTextField(String.valueOf(pr.getQuantity()));
        JTextField supplierIDField = new JTextField(item.getSupplierID());
        JTextField pmIDField = new JTextField(); // PM enters their own ID

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("PO ID:"));
        panel.add(poIDField);
        panel.add(new JLabel("PR ID:"));
        panel.add(prIDField);
        panel.add(new JLabel("Item ID:"));
        panel.add(itemIDField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Supplier ID:"));
        panel.add(supplierIDField);
        panel.add(new JLabel("Purchase Manager ID:"));
        panel.add(pmIDField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Purchase Order Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int qty = Integer.parseInt(quantityField.getText());
                String supplierID = supplierIDField.getText().trim();
                String pmID = pmIDField.getText().trim();

                if (supplierID.isEmpty() || pmID.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Supplier ID and PM ID cannot be empty.");
                    return;
                }

                PO po = new PO(poID, pr.getPrID(), pr.getItemID(), qty, supplierID, pmID);
                savePO(po);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.");
            }

        }
    }

}

