package models;

public class Item {
    private String itemID;
    private String name;
    private String supplierID;
    private int stockLevel;
    private int reorderLevel;

    public Item(String itemID, String name, String supplierID, int stockLevel, int reorderLevel) {
        this.itemID = itemID;
        this.name = name;
        this.supplierID = supplierID;
        this.stockLevel = stockLevel;
        this.reorderLevel = reorderLevel;
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String toFileString() {
        return itemID + "," + name + "," + supplierID + "," + stockLevel + "," + reorderLevel;
    }

    // Load from file string format
    public static Item fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            try {
                String itemID = parts[0];
                String name = parts[1];
                String supplierID = parts[2];
                int stockLevel = Integer.parseInt(parts[3]);
                int reorderLevel = Integer.parseInt(parts[4]);

                return new Item(itemID, name, supplierID, stockLevel, reorderLevel);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in line: " + line);
                e.printStackTrace();  // You can comment this out later if you want cleaner output
            }
        } else {
            System.out.println("Invalid item line: " + line);
        }
        return null; // Return null if line is invalid
    }
}
