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
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String code = parts[0];
                    String name = parts[1];
                    String supplier = parts[2];
                    int stockLevel = Integer.parseInt(parts[3]);
                    items.add(new Item(code, name, supplier, stockLevel));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }
        return items;
    }

    public static void saveItems(String filePath, List<Item> items) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Item Code,Item Name,Supplier,Stock Level\n");
            for (Item item : items) {
                bw.write(item.getCode() + "," + item.getName() + "," + item.getSupplier() + "," + item.getStockLevel() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving items: " + e.getMessage());
        }
    }
}
