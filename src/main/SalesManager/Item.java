package SalesManager;

public class Item {
    private final int itemID;
    private final String name;
    private int quantity;
    private final double price;

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

    public void reduceQuantity(int amount) {
        if (amount > 0 && quantity >= amount) {
            quantity -= amount;
        }
    }

    @Override
    public String toString() {
        return "Item ID: " + itemID + ", Name: " + name + ", Quantity: " + quantity + ", Price: " + price;
    }
}
