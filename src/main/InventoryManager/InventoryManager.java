package InventoryManager;

import models.Item;
import utils.ItemFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Item> items;

    public InventoryManager() {
        items = new ArrayList<>();
    }

    // Load items from a file
    public void loadItems(String filePath) {
        try {
            items = ItemFileHandler.readItemsFromFile(filePath);
            System.out.println("Items loaded successfully from " + filePath);
        } catch (IOException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }
    }

    // Save items to a file
    public void saveItems(String filePath) {
        try {
            ItemFileHandler.writeItemsToFile(filePath, items);
            System.out.println("Items saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving items: " + e.getMessage());
        }
    }

    // Generate a stock report
    public void generateStockReport(String filePath) {
        try {
            StockReport.generate(filePath, items);
            System.out.println("Stock report generated successfully at " + filePath);
        } catch (IOException e) {
            System.err.println("Error generating stock report: " + e.getMessage());
        }
    }

    // Display all items in the inventory
    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("No items in the inventory.");
        } else {
            System.out.println("Inventory Items:");
            System.out.println("------------------------------------------------------------");
            System.out.printf("%-10s %-20s %-15s %-10s %-10s\n", "Item ID", "Item Name", "Supplier", "Stock", "Reorder");
            System.out.println("------------------------------------------------------------");
            for (Item item : items) {
                System.out.printf("%-10s %-20s %-15s %-10d %-10d\n",
                        item.getItemID(),
                        item.getName(),
                        item.getSupplierID(),
                        item.getStockLevel(),
                        item.getReorderLevel());
            }
        }
    }

    // Edit an item's stock or reorder level
    public void editItem(String itemID, int newStockLevel, int newReorderLevel) {
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                item.setStockLevel(newStockLevel);
                item.setReorderLevel(newReorderLevel);
                System.out.println("Item updated: " + itemID);
                return;
            }
        }
        System.out.println("Item not found: " + itemID);
    }

    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Example usage
        String filePath = "items.txt";
        String reportPath = "stock_report.txt";

        // Load items from file
        manager.loadItems(filePath);

        // Display items
        manager.displayItems();

        // Edit an item
        manager.editItem("ITM-001", 300, 60);

        // Save updated items to file
        manager.saveItems(filePath);

        // Generate stock report
        manager.generateStockReport(reportPath);
    }
}
