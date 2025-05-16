package salesmanager;

public class Item {
    private int itemID;
    private String name;
    private int quantity;
    private double price;

    public Item(int itemID, String name, int quantity, double price) {
        this.itemID = itemID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void reduceQuantity(int qty) {
        this.quantity -= qty;
    }

    @Override
    public String toString() {
        return "Item ID: " + itemID + ", Name: " + name + ", Quantity: " + quantity + ", Price: RM " + String.format("%.2f", price);
    }
}