
package InventoryManager;

public class Item {
    private String code;
    private String name;
    private String supplier;
    private int stockLevel;

    public Item(String code, String name, String supplier, int stockLevel) {
        this.code = code;
        this.name = name;
        this.supplier = supplier;
        this.stockLevel = stockLevel;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }
}
