package models;

public class PR {
    private String prID;
    private String itemID;
    private int quantity;
    private String requiredDate;
    private String raisedBy;

    public PR(String prID, String itemID, int quantity, String requiredDate, String raisedBy) {
        this.prID = prID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.requiredDate = requiredDate;
        this.raisedBy = raisedBy;
    }

    public String getPrID() { return prID; }
    public String getItemID() { return itemID; }
    public int getQuantity() { return quantity; }
    public String getRequiredDate() { return requiredDate; }
    public String getRaisedBy() { return raisedBy; }

    public static PR fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
            try {
                String prID = parts[0];
                String itemID = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                String date = parts[3];
                String raisedBy = parts[4];
                return new PR(prID, itemID, quantity, date, raisedBy);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String toFileString() {
        return prID + "," + itemID + "," + quantity + "," + requiredDate + "," + raisedBy;
    }
}
