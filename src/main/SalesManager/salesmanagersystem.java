package salesmanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main system class for the Sales Manager functionality of Omega Wholesale Sdn Bhd (OWSB)
 */
public class SalesManagerSystem {
    private final List<Item> items = new ArrayList<>();
    private final List<Supplier> suppliers = new ArrayList<>();
    private final List<PurchaseRequest> purchaseRequests = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private int prCounter = 1;

    public static void main(String[] args) {
        new SalesManagerSystem().run();
    }

    public void run() {
        System.out.println("Welcome to OWSB Sales Manager System");
        System.out.println("===================================");

        boolean running = true;
        while (running) {
            try {
                displayMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> addNewItem();
                    case "2" -> addNewSupplier();
                    case "3" -> enterDailySale();
                    case "4" -> raisePurchaseRequest();
                    case "5" -> viewAllPRs();
                    case "6" -> viewAllItems();
                    case "7" -> viewAllSuppliers();
                    case "8" -> {
                        running = false;
                        System.out.println("Thank you for using OWSB Sales Manager System.");
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Add New Item");
        System.out.println("2. Add New Supplier");
        System.out.println("3. Enter Daily Sale");
        System.out.println("4. Raise Purchase Request");
        System.out.println("5. View All Purchase Requests");
        System.out.println("6. View All Items");
        System.out.println("7. View All Suppliers");
        System.out.println("8. Exit");
        System.out.print("Choose an option (1-8): ");
    }

    private void addNewItem() {
        try {
            System.out.println("\n=== Add New Item ===");
            System.out.print("Enter Item ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            if (items.stream().anyMatch(item -> item.getItemID() == id)) {
                System.out.println("Error: Item ID already exists!");
                return;
            }

            System.out.print("Enter Item Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());
            if (qty < 0) {
                System.out.println("Error: Quantity cannot be negative!");
                return;
            }

            System.out.print("Enter Price (RM): ");
            double price = Double.parseDouble(scanner.nextLine());
            if (price < 0) {
                System.out.println("Error: Price cannot be negative!");
                return;
            }

            items.add(new Item(id, name, qty, price));
            System.out.println("Item added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers!");
        }
    }

    private void addNewSupplier() {
        try {
            System.out.println("\n=== Add New Supplier ===");
            System.out.print("Enter Supplier ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            if (suppliers.stream().anyMatch(supplier -> supplier.getSupplierID() == id)) {
                System.out.println("Error: Supplier ID already exists!");
                return;
            }

            System.out.print("Enter Supplier Name: ");
            String name = scanner.nextLine();

            if (name.trim().isEmpty()) {
                System.out.println("Error: Supplier name cannot be empty!");
                return;
            }

            suppliers.add(new Supplier(id, name));
            System.out.println("Supplier added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid Supplier ID!");
        }
    }

    private void enterDailySale() {
        try {
            System.out.println("\n=== Enter Daily Sale ===");
            if (items.isEmpty()) {
                System.out.println("No items available in the system!");
                return;
            }

            System.out.print("Enter Item ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Quantity Sold: ");
            int qty = Integer.parseInt(scanner.nextLine());

            if (qty <= 0) {
                System.out.println("Error: Quantity must be positive!");
                return;
            }

            boolean found = false;
            for (Item item : items) {
                if (item.getItemID() == id) {
                    found = true;
                    if (item.getQuantity() >= qty) {
                        item.reduceQuantity(qty);
                        System.out.println("Sale recorded successfully!");
                        System.out.println("Remaining stock for " + item.getName() + ": " + item.getQuantity());

                        if (item.getQuantity() < 10) {
                            System.out.println("WARNING: Low stock! Consider raising a Purchase Request.");
                        }
                    } else {
                        System.out.println("Error: Not enough stock! Available: " + item.getQuantity());
                    }
                    break;
                }
            }

            if (!found) {
                System.out.println("Error: Item not found!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers!");
        }
    }

    private void raisePurchaseRequest() {
        try {
            System.out.println("\n=== Raise Purchase Request ===");
            if (items.isEmpty()) {
                System.out.println("No items available in the system!");
                return;
            }

            System.out.print("Enter Item ID: ");
            int itemID = Integer.parseInt(scanner.nextLine());

            boolean itemExists = items.stream().anyMatch(item -> item.getItemID() == itemID);
            if (!itemExists) {
                System.out.println("Error: Item not found!");
                return;
            }

            System.out.print("Enter Quantity Needed: ");
            int qty = Integer.parseInt(scanner.nextLine());

            if (qty <= 0) {
                System.out.println("Error: Quantity must be positive!");
                return;
            }

            purchaseRequests.add(new PurchaseRequest(prCounter++, itemID, qty, LocalDate.now()));
            System.out.println("Purchase Request raised successfully!");
            System.out.println("PR ID: " + (prCounter - 1));
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers!");
        }
    }

    private void viewAllPRs() {
        System.out.println("\n=== All Purchase Requests ===");
        if (purchaseRequests.isEmpty()) {
            System.out.println("No Purchase Requests found.");
        } else {
            purchaseRequests.forEach(pr -> {
                System.out.println(pr);
                items.stream()
                        .filter(item -> item.getItemID() == pr.getItemID())
                        .findFirst()
                        .ifPresent(item -> System.out.println("Item Name: " + item.getName()));
                System.out.println("----------------");
            });
        }
    }

    private void viewAllItems() {
        System.out.println("\n=== All Items ===");
        if (items.isEmpty()) {
            System.out.println("No items found.");
        } else {
            items.forEach(item -> {
                System.out.println(item);
                System.out.println("----------------");
            });
        }
    }

    private void viewAllSuppliers() {
        System.out.println("\n=== All Suppliers ===");
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
        } else {
            suppliers.forEach(supplier -> {
                System.out.println(supplier);
                System.out.println("----------------");
            });
        }
    }
}
