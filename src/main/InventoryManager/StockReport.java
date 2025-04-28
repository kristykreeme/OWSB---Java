package InventoryManager;

import models.Item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StockReport {

    // Generates a stock report and saves it to a file
    public static void generate(String filePath, List<Item> items) throws IOException {
        int totalItems = items.size();
        int lowStockCount = 0;
        int totalStock = 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Stock Report\n");
            bw.write("===============================\n");
            bw.write("Total Items: " + totalItems + "\n");
            bw.write("===============================\n\n");

            bw.write("Low Stock Items:\n");
            bw.write("Item ID,Item Name,Supplier ID,Stock Level,Reorder Level\n");

            // Process each item and classify it
            for (Item item : items) {
                totalStock += item.getStockLevel();
                if (item.getStockLevel() < item.getReorderLevel()) {
                    lowStockCount++;
                    bw.write(item.getItemID() + "," +
                            item.getName() + "," +
                            item.getSupplierID() + "," +
                            item.getStockLevel() + "," +
                            item.getReorderLevel() + "\n");
                }
            }

            // Append summary
            bw.write("\n===============================\n");
            bw.write("Summary\n");
            bw.write("===============================\n");
            bw.write("Total Stock: " + totalStock + " units\n");
            bw.write("Low Stock Items: " + lowStockCount + "\n");
        }
    }
}