package SalesManager;

public class Supplier {
    private final int supplierID;
    private final String name;

    public Supplier(int supplierID, String name) {
        this.supplierID = supplierID;
        this.name = name;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Supplier ID: " + supplierID + ", Name: " + name;
    }
}
