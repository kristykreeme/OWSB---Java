package utils;

import models.Item;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemFileHandler {

    public static List<Item> loadItems(String filePath) {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String itemID = parts[0];
                    String name = parts[1];
                    String supplierID = parts[2];
                    int stockLevel = Integer.parseInt(parts[3]);

                    items.add(new Item(itemID, name, supplierID, stockLevel));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }
        return items;
    }

    public static void saveItems(String filePath, List<Item> items) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Item ID,Item Name,Supplier ID,Stock Level\n");
            for (Item item : items) {
                bw.write(item.getItemID() + "," + item.getName() + "," + item.getSupplierID() + "," + item.getStockLevel() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving items: " + e.getMessage());
        }
    }
}
