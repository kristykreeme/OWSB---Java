package InventoryManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import models.Item;

public class StockReport {

    public void generateStockReport(List<Item> items, String filePath)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Item Code,Item Name,Supplier,Stock Level,Status\n");
            for (Item item : items) {
                String status = item.getStockLevel() < 60 ? "Low Stock" : "OK";
                writer.write(item.getItemID() + "," + item.getName() + "," + item.getSupplierID() + "," + item.getStockLevel() + "," + status + "\n");
            }
            System.out.println("Stock report generated successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error while generating report: " + e.getMessage());
        }
    }
}
