package FinanceManager;
import java.util.*;

public class PurchaseOrder {
    private String poID;
    private String items; // item1:qty;item2:qty
    private String status; // Pending, Approved, Rejected
    private String supplier;

    public PurchaseOrder(String poID, String items, String status, String supplier) {
        this.poID = poID;
        this.items = items;
        this.status = status;
        this.supplier = supplier;
    }

    public String getPoID() {
        return poID;
    }

    public String getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        return poID + "," + items + "," + status + "," + supplier;
    }

    public static PurchaseOrder fromFileString(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length != 4) return null;
        return new PurchaseOrder(parts[0], parts[1], parts[2], parts[3]);
    }
}
