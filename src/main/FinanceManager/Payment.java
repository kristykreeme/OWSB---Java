package FinanceManager;

public class Payment {
    private String paymentID;
    private String poID;
    private double amount;
    private String date;
    private String status;

    public Payment(String paymentID, String poID, double amount, String date, String status) {
        this.paymentID = paymentID;
        this.poID = poID;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getPoID() {
        return poID;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String toFileString() {
        return paymentID + "," + poID + "," + amount + "," + date + "," + status;
    }

    public static Payment fromFileString(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length != 5) return null;
        return new Payment(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3], parts[4]);
    }
}
