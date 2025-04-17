package InventoryManager;

import java.io.*;
import java.util.*;

public class ItemFileHandler {

    public static List<Item> loadItemsFromFile(String filename) {
        List<Item> itemList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String code = parts[0];
                    String name = parts[1];
                    String supplier = parts[2];
                    int stock = Integer.parseInt(parts[3]);
                    itemList.add(new Item(code, name, supplier, stock));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading items: " + e.getMessage());

        }
        return itemList;
    }
}
