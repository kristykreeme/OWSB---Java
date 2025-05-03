package FinanceManager;

import models.Payment;
import utils.FileHandler;
import java.util.Date;

public class FinanceManager {

    // Approve a Purchase Order (PO)
    public void approvePO(String poID, String financeManagerID) {
        PO po = FileHandler.findPOByID(poID);
        if (po != null && po.getStatus().equals("Pending")) {
            po.setStatus("Approved");
            po.setApprovedBy(financeManagerID);
            FileHandler.updatePO(po);
            System.out.println("PO " + poID + " approved by " + financeManagerID);
        } else {
            System.out.println("PO not found or already approved.");
        }
    }

    // Process a Payment for an Approved PO
    public void processPayment(String poID, double amount) {
        PO po = FileHandler.findPOByID(poID);
        if (po != null && po.getStatus().equals("Approved")) {
            Payment payment = new Payment();
            payment.setPaymentID("PAY" + System.currentTimeMillis());
            payment.setAmount(amount);
            payment.setDate(new Date());
            payment.setPayStatus("Paid");
            payment.setLinkedPO(poID);

            FileHandler.savePayment(payment);
            System.out.println("Payment processed for PO " + poID);
        } else {
            System.out.println("PO not approved or not found.");
        }
    }

}
