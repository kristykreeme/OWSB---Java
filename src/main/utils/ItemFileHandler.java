package utils;

import models.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemFileHandler {

    // Reads items from a file and returns a list of Item objects
    public static List<Item> readItemsFromFile(String filePath) throws IOException {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String supplier = parts[2].trim();
                    int stock = Integer.parseInt(parts[3].trim());
                    int reorderLevel = Integer.parseInt(parts[4].trim());
                    items.add(new Item(id, name, supplier, stock, reorderLevel));
                }
            }
        }
        return items;
    }

    // Writes a list of Item objects to a file
    public static void writeItemsToFile(String filePath, List<Item> items) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Item ID,Item Name,Supplier ID,Stock Level,Reorder Level\n"); // Write header
            for (Item item : items) {
                bw.write(item.getItemID() + "," +
                        item.getName() + "," +
                        item.getSupplierID() + "," +
                        item.getStockLevel() + "," +
                        item.getReorderLevel() + "\n");
            }
        }
    }
}