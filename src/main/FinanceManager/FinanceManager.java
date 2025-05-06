//package FinanceManager;

//import models.Payment;
//import utils.FileHandler;
//import java.util.Date;
//
//public class FinanceManager {
//
//    // Approve a Purchase Order (PO)
//    public void approvePO(String poID, String financeManagerID) {
//        PO po = FileHandler.findPOByID(poID);
//        if (po != null && po.getStatus().equals("Pending")) {
//            po.setStatus("Approved");
//            po.setApprovedBy(financeManagerID);
//            FileHandler.updatePO(po);
//            System.out.println("PO " + poID + " approved by " + financeManagerID);
//        } else {
//            System.out.println("PO not found or already approved.");
//        }
//    }
//
//    // Process a Payment for an Approved PO
//    public void processPayment(String poID, double amount) {
//        PO po = FileHandler.findPOByID(poID);
//        if (po != null && po.getStatus().equals("Approved")) {
//            Payment payment = new Payment();
//            payment.setPaymentID("PAY" + System.currentTimeMillis());
//            payment.setAmount(amount);
//            payment.setDate(new Date());
//            payment.setPayStatus("Paid");
//            payment.setLinkedPO(poID);
//
//            FileHandler.savePayment(payment);
//            System.out.println("Payment processed for PO " + poID);
//        } else {
//            System.out.println("PO not approved or not found.");
//        }
//    }
//
//}
import java.util.*;
import java.time.*;

public class FinanceManagerSystem {
    private List<PurchaseOrder> pendingPOs;
    private List<PurchaseOrder> approvedPOs;
    private List<Payment> payments;
    private Scanner scanner;

    public FinanceManagerSystem() {
        this.pendingPOs = new ArrayList<>();
        this.approvedPOs = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Sample POs
        PurchaseOrder po1 = new PurchaseOrder(101, "Pending", 1, 1001);
        po1.addItem(new POItem(1, 10, 25.99));
        po1.addItem(new POItem(2, 5, 49.99));
        pendingPOs.add(po1);

        PurchaseOrder po2 = new PurchaseOrder(102, "Pending", 2, 1002);
        po2.addItem(new POItem(3, 20, 12.50));
        pendingPOs.add(po2);
    }

    public void run() {
        System.out.println("Finance Manager System");
        System.out.println("----------------------");

        boolean running = true;
        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. View Pending POs");
            System.out.println("2. Approve/Reject PO");
            System.out.println("3. Process Payment");
            System.out.println("4. View Approved POs");
            System.out.println("5. View Payment History");
            System.out.println("6. Generate Financial Report");
            System.out.println("7. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewPendingPOs();
                    break;
                case 2:
                    approveRejectPO();
                    break;
                case 3:
                    processPayment();
                    break;
                case 4:
                    viewApprovedPOs();
                    break;
                case 5:
                    viewPaymentHistory();
                    break;
                case 6:
                    generateFinancialReport();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        System.out.println("System exited. Goodbye!");
        scanner.close();
    }

    // Other methods will be implemented here
    // [Previous methods shown in earlier snippets would go here]

    public static void main(String[] args) {
        FinanceManagerSystem system = new FinanceManagerSystem();
        system.run();
    }
}