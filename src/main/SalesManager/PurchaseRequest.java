package SalesManager;

import java.time.LocalDate;

public class PurchaseRequest {
    private final int prID;
    private final int itemID;
    private final int quantity;
    private final LocalDate date;

    public PurchaseRequest(int prID, int itemID, int quantity, LocalDate date) {
        this.prID = prID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.date = date;
    }

    public int getPrID() {
        return prID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "PR ID: " + prID +
                ", Item ID: " + itemID +
                ", Quantity: " + quantity +
                ", Date: " + date;
    }
}
