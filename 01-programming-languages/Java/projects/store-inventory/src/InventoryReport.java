import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * InventoryReport generates and holds inventory analytics.
 * Provides insights into stock levels, values, and movements.
 */
public class InventoryReport {
    private final LocalDateTime reportDate;
    private final int totalProducts;
    private final int totalStockValue;
    private final List<Product> lowStockItems;
    private final List<StockMovement> recentMovements;

    public InventoryReport(LocalDateTime reportDate, int totalProducts,
                           int totalStockValue, List<Product> lowStockItems,
                           List<StockMovement> recentMovements) {
        this.reportDate = reportDate;
        this.totalProducts = totalProducts;
        this.totalStockValue = totalStockValue;
        this.lowStockItems = new ArrayList<>(lowStockItems);
        this.recentMovements = new ArrayList<>(recentMovements);
    }

    // Getters
    public LocalDateTime getReportDate() { return reportDate; }
    public int getTotalProducts() { return totalProducts; }
    public int getTotalStockValue() { return totalStockValue; }
    public List<Product> getLowStockItems() { return new ArrayList<>(lowStockItems); }
    public List<StockMovement> getRecentMovements() { return new ArrayList<>(recentMovements); }

    /**
     * Generate formatted report string
     */
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Inventory Report ===\n");
        sb.append("Date: ").append(reportDate).append("\n");
        sb.append("Total Products: ").append(totalProducts).append("\n");
        sb.append("Total Stock Value: $").append(totalStockValue).append("\n");
        sb.append("Low Stock Items: ").append(lowStockItems.size()).append("\n");
        
        if (!lowStockItems.isEmpty()) {
            sb.append("\nLow Stock Alert:\n");
            for (Product p : lowStockItems) {
                sb.append("  - ").append(p.getName())
                  .append(": ").append(p.getQuantity()).append(" units\n");
            }
        }
        
        sb.append("\nRecent Movements: ").append(recentMovements.size()).append("\n");
        return sb.toString();
    }
}