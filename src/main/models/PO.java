package models;

public class PO {
    private String poID;
    private String prID;
    private String itemID;
    private int quantity;
    private String supplierID;
    private String purchaseManagerID;

    public PO(String poID, String prID, String itemID, int quantity, String supplierID, String purchaseManagerID) {
        this.poID = poID;
        this.prID = prID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.supplierID = supplierID;
        this.purchaseManagerID = purchaseManagerID;
    }

    public String getPoID() { return poID; }
    public String getPrID() { return prID; }
    public String getItemID() { return itemID; }
    public int getQuantity() { return quantity; }
    public String getSupplierID() { return supplierID; }
    public String getPurchaseManagerID() { return purchaseManagerID; }

    public String toFileString() {
        return poID + "," + prID + "," + itemID + "," + quantity + "," + supplierID + "," + purchaseManagerID;
    }

    public static PO fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 6) {
            try {
                String poID = parts[0];
                String prID = parts[1];
                String itemID = parts[2];
                int quantity = Integer.parseInt(parts[3]);
                String supplierID = parts[4];
                String pmID = parts[5];
                return new PO(poID, prID, itemID, quantity, supplierID, pmID);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
