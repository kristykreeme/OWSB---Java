package models;

public class Item {
    private String itemID;
    private String name;
    private String supplierID;
    private int stockLevel;

    public Item(String itemID, String name, String supplierID, int stockLevel) {
        this.itemID = itemID;
        this.name = name;
        this.supplierID = supplierID;
        this.stockLevel = stockLevel;
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
}
