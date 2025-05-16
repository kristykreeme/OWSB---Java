package FinanceManager;

import java.io.*;
import java.util.*;

public class FinanceManager {
    static Scanner sc = new Scanner(System.in);

    static class PurchaseOrder {
        String poID;
        String status;
        String supplierID;

        PurchaseOrder(String poID, String status, String supplierID) {
            this.poID = poID;
            this.status = status;
            this.supplierID = supplierID;
        }

        public String toString() {
            return poID + "," + status + "," + supplierID;
        }

        public static PurchaseOrder fromString(String line) {
            String[] parts = line.split(",");
            return new PurchaseOrder(parts[0], parts[1], parts[2]);
        }
    }

    static class Payment {
        String paymentID;
        double amount;
        String date;
        String payStatus;
        String linkedPO;

        Payment(String paymentID, double amount, String date, String payStatus, String linkedPO) {
            this.paymentID = paymentID;
            this.amount = amount;
            this.date = date;
            this.payStatus = payStatus;
            this.linkedPO = linkedPO;
        }

        public String toString() {
            return paymentID + "," + amount + "," + date + "," + payStatus + "," + linkedPO;
        }

        public static Payment fromString(String line) {
            String[] parts = line.split(",");
            return new Payment(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3], parts[4]);
        }
    }

    static ArrayList<PurchaseOrder> loadPOs(String filename) {
        ArrayList<PurchaseOrder> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(PurchaseOrder.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading PO file.");
        }
        return list;
    }

    static void savePOs(ArrayList<PurchaseOrder> list, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (PurchaseOrder po : list) {
                pw.println(po);
            }
        } catch (IOException e) {
            System.out.println("Error saving PO file.");
        }
    }

    static void viewPendingPOs() {
        ArrayList<PurchaseOrder> list = loadPOs("PurchaseOrder.txt");
        for (PurchaseOrder po : list) {
            if (po.status.equalsIgnoreCase("Pending")) {
                System.out.println("PO ID: " + po.poID + " | Supplier: " + po.supplierID + " | Status: " + po.status);
            }
        }
    }

    static void approveRejectPO() {
        ArrayList<PurchaseOrder> list = loadPOs("PurchaseOrder.txt");
        viewPendingPOs();
        System.out.print("Enter PO ID to update: ");
        String id = sc.nextLine();
        for (PurchaseOrder po : list) {
            if (po.poID.equals(id) && po.status.equalsIgnoreCase("Pending")) {
                System.out.print("Approve (A) or Reject (R)? ");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("A")) {
                    po.status = "Approved";
                } else if (choice.equalsIgnoreCase("R")) {
                    po.status = "Rejected";
                }
                break;
            }
        }
        savePOs(list, "PurchaseOrder.txt");
        System.out.println("PO updated.");
    }

    static void viewApprovedPOs() {
        ArrayList<PurchaseOrder> list = loadPOs("PurchaseOrder.txt");
        for (PurchaseOrder po : list) {
            if (po.status.equalsIgnoreCase("Approved")) {
                System.out.println("PO ID: " + po.poID + " | Supplier: " + po.supplierID + " | Status: " + po.status);
            }
        }
    }

    static void processPayment() {
        System.out.print("Enter PO ID to pay: ");
        String poID = sc.nextLine();
        System.out.print("Enter Payment ID: ");
        String payID = sc.nextLine();
        System.out.print("Enter Amount: ");
        double amt = Double.parseDouble(sc.nextLine());
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        Payment payment = new Payment(payID, amt, date, "Paid", poID);

        try (PrintWriter pw = new PrintWriter(new FileWriter("Payments.txt", true))) {
            pw.println(payment);
        } catch (IOException e) {
            System.out.println("Error saving payment.");
        }
        System.out.println("Payment recorded.");
    }

    static void viewPayments() {
        try (BufferedReader br = new BufferedReader(new FileReader("Payments.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Payment p = Payment.fromString(line);
                System.out.println("Payment ID: " + p.paymentID + " | Amount: " + p.amount + " | PO: " + p.linkedPO + " | Status: " + p.payStatus);
            }
        } catch (IOException e) {
            System.out.println("Error loading payments.");
        }
    }

    static void viewPRs() {
        try (BufferedReader br = new BufferedReader(new FileReader("PurchaseRequests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("PR: " + line);
            }
        } catch (IOException e) {
            System.out.println("Error loading PRs.");
        }
    }

    static void generateReport() {
        System.out.println("Generating financial report...");

        // Run these from Python/another tool if chart libraries not allowed
        System.out.println("Bar Chart: Refer to 'item_sales_chart.png'");
        System.out.println("Pie Chart: Refer to 'po_approval_status_chart.png'");
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== Finance Manager ====");
            System.out.println("1. View Pending PO");
            System.out.println("2. Approve/Reject PO");
            System.out.println("3. View Approved PO");
            System.out.println("4. Process Payment");
            System.out.println("5. View Payments");
            System.out.println("6. View PRs");
            System.out.println("7. Generate Financial Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": viewPendingPOs(); break;
                case "2": approveRejectPO(); break;
                case "3": viewApprovedPOs(); break;
                case "4": processPayment(); break;
                case "5": viewPayments(); break;
                case "6": viewPRs(); break;
                case "7": generateReport(); break;
                case "0": System.exit(0);
                default: System.out.println("Invalid option.");
            }
        }
    }
}
