package FinanceManager;

public class PurchaseRequisition {
    private String prID;
    private String items; // item1:qty;item2:qty
    private String requestDate;
    private String requiredDate;

    public PurchaseRequisition(String prID, String items, String requestDate, String requiredDate) {
        this.prID = prID;
        this.items = items;
        this.requestDate = requestDate;
        this.requiredDate = requiredDate;
    }

    public String getPrID() {
        return prID;
    }

    public String getItems() {
        return items;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public static PurchaseRequisition fromFileString(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length != 4) return null;
        return new PurchaseRequisition(parts[0], parts[1], parts[2], parts[3]);
    }

    public String toString() {
        return "PR ID: " + prID + "\nItems: " + items + "\nRequest Date: " + requestDate + "\nRequired Date: " + requiredDate + "\n";
    }
}
