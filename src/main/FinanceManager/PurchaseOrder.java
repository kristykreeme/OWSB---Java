package FinanceManager;

import java.util.*;

public class PurchaseOrder {
    private int poID;
    private String status;
    private int supplierID;
    private int prID;
    private List<POItem> items;
    private LocalDate approvalDate;
    private String approvedBy;

    public PurchaseOrder(int poID, String status, int supplierID, int prID) {
        this.poID = poID;
        this.status = status;
        this.supplierID = supplierID;
        this.prID = prID;
        this.items = new ArrayList<>();
    }

    public void addItem(POItem item) {
        items.add(item);
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
    }

    // Getters and setters
    public int getPoID() { return poID; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getSupplierID() { return supplierID; }
    public int getPrID() { return prID; }
    public List<POItem> getItems() { return items; }
    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
}