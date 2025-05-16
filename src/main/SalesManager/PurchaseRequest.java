package salesmanager;

import java.time.LocalDate;

public class PurchaseRequest {
    private int prID;
    private int itemID;
    private int quantity;
    private LocalDate date;

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
        return "PR ID: " + prID + ", Item ID: " + itemID + ", Quantity: " + quantity + ", Date: " + date;
    }
}