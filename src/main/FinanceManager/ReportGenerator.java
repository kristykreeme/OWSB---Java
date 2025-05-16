package FinanceManager;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {

    public static void generatePOStatusChart(List<PurchaseOrder> poList) {
        int approved = 0, rejected = 0, pending = 0;

        for (PurchaseOrder po : poList) {
            switch (po.getStatus().toLowerCase()) {
                case "approved": approved++; break;
                case "rejected": rejected++; break;
                default: pending++; break;
            }
        }

        System.out.println("\n📊 PO Approval Status:");
        System.out.println("Approved: " + approved);
        System.out.println("Rejected: " + rejected);
        System.out.println("Pending : " + pending);
    }

    public static void generateSalesBarChart(Map<String, Integer> salesData) {
        System.out.println("\n📈 Item Sales Bar Chart:");
        for (String item : salesData.keySet()) {
            int count = salesData.get(item);
            System.out.printf("%-15s | %s (%d)%n", item, "*".repeat(count), count);
        }
    }

    public static Map<String, Integer> loadSalesData(String filename) {
        List<String> lines = FileHandler.readFile(filename);
        Map<String, Integer> sales = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(",", 3); // e.g., itemName,quantitySold,date
            if (parts.length < 2) continue;
            String item = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            sales.put(item, sales.getOrDefault(item, 0) + quantity);
        }

        return sales;
    }
}
