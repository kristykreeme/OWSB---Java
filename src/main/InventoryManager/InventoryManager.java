package InventoryManager;

import models.Item;
// import models.PurchaseOrder; ( EDIT WHEN THERE IS PURCHACE ORDER CLASS !!! )
import InventoryManager.StockReport;
import utils.ItemFileHandler;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Item> items;
    private StockReport stockReport;

    public InventoryManager() {
        this.items = new ArrayList<>();
        this.stockReport = new StockReport();
    }

    public void viewItems() {
        System.out.println("Item Code\tItem Name\tSupplier\tStock Level");
        for (Item item : items) {
            String stockDisplay = item.getStockLevel() < 60 ? "Low Stock" : String.valueOf(item.getStockLevel());
            System.out.println(item.getCode() + "\t\t" + item.getName() + "\t\t" + item.getSupplier() + "\t\t" + stockDisplay);
        }
    }

    public void updateStock(String itemCode, int quantity) {
        for (Item item : items) {
            if (item.getCode().equals(itemCode)) {
                item.setStockLevel(item.getStockLevel() + quantity);
                System.out.println("Stock updated for item: " + item.getName() + ". New stock level: " + item.getStockLevel());
                return;
            }
        }
        System.out.println("Item not found.");
    }

   // public void updateStockFromPOs(List<PurchaseOrder> approvedPOs) {
       // for (PurchaseOrder po : approvedPOs) {
          //  String itemCode = po.getItemCode(); // Ensure your PO class has getItemCode()
          //  int quantityReceived = po.getQuantity();
          //  updateStock(itemCode, quantityReceived);
       // }
  //  }

    public void generateStockReport(String filePath) {
        stockReport.generateStockReport(items, filePath);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void loadItemsFromFile(String filePath) {
        this.items = ItemFileHandler.loadItems(filePath);
    }

    public void saveItemsToFile(String filePath) {
        ItemFileHandler.saveItems(filePath, this.items);
    }
}
