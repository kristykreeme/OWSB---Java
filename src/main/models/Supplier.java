package models;

public class Supplier {
    private String supplierID;
    private String supplierName;

    public Supplier(String supplierID, String supplierName) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String toFileString() {
        return supplierID + "," + supplierName;
    }

    public static Supplier fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 2) {
            return new Supplier(parts[0], parts[1]);
        }
        return null;
    }
}
