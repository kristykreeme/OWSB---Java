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
}