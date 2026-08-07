import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import persistence.InventoryRepository;
import persistence.FileInventoryRepository;

/**
 * InventoryManager orchestrates business logic for inventory operations.
 * Coordinates between entities and repository for complete functionality.
 */
public class InventoryManager {
    private final InventoryRepository repository;
    private static final int LOW_STOCK_THRESHOLD = 10;

    public InventoryManager() {
        this.repository = new FileInventoryRepository();
    }

    public InventoryManager(InventoryRepository repository) {
        this.repository = repository;
    }

    // Product management
    public Product addProduct(String name, String description, double price,
                              int quantity, String categoryId) {
        String id = UUID.randomUUID().toString();
        Product product = new Product.Builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .categoryId(categoryId)
                .build();
        
        repository.saveProduct(product);
        return product;
    }

    public Optional<Product> getProduct(String id) {
        return repository.findProductById(id);
    }

    public List<Product> getAllProducts() {
        return repository.findAllProducts();
    }

    public boolean updateProduct(String id, String name, String description,
                                 double price, int quantity) {
        Optional<Product> existing = repository.findProductById(id);
        if (existing.isPresent()) {
            Product old = existing.get();
            Product updated = new Product.Builder()
                    .id(id)
                    .name(name != null ? name : old.getName())
                    .description(description != null ? description : old.getDescription())
                    .price(price > 0 ? price : old.getPrice())
                    .quantity(quantity >= 0 ? quantity : old.getQuantity())
                    .categoryId(old.getCategoryId())
                    .build();
            repository.updateProduct(updated);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(String id) {
        Optional<Product> product = repository.findProductById(id);
        if (product.isPresent()) {
            repository.deleteProduct(id);
            return true;
        }
        return false;
    }

    // Stock movement processing
    public boolean processStockIn(String productId, int quantity, String reason) {
        Optional<Product> productOpt = repository.findProductById(productId);
        if (productOpt.isPresent() && quantity > 0) {
            Product product = productOpt.get();
            int newQuantity = product.getQuantity() + quantity;
            updateProductQuantity(productId, newQuantity);
            
            String movementId = UUID.randomUUID().toString();
            StockMovement movement = new StockMovement(
                    movementId, productId, StockMovement.MovementType.IN,
                    quantity, reason);
            repository.saveStockMovement(movement);
            return true;
        }
        return false;
    }

    public boolean processStockOut(String productId, int quantity, String reason) {
        Optional<Product> productOpt = repository.findProductById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getQuantity() >= quantity) {
                int newQuantity = product.getQuantity() - quantity;
                updateProductQuantity(productId, newQuantity);
                
                String movementId = UUID.randomUUID().toString();
                StockMovement movement = new StockMovement(
                        movementId, productId, StockMovement.MovementType.OUT,
                        quantity, reason);
                repository.saveStockMovement(movement);
                return true;
            }
        }
        return false;
    }

    private void updateProductQuantity(String productId, int newQuantity) {
        Optional<Product> productOpt = repository.findProductById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            Product updated = new Product.Builder()
                    .id(productId)
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(newQuantity)
                    .categoryId(product.getCategoryId())
                    .build();
            repository.updateProduct(updated);
        }
    }

    // Reporting
    public InventoryReport generateReport() {
        List<Product> allProducts = repository.findAllProducts();
        List<Product> lowStock = repository.findLowStockProducts(LOW_STOCK_THRESHOLD);
        int totalValue = repository.calculateTotalStockValue();
        List<StockMovement> recentMovements = repository.findAllMovements();
        
        return new InventoryReport(
                LocalDateTime.now(),
                allProducts.size(),
                totalValue,
                lowStock,
                recentMovements
        );
    }

    public List<Product> getLowStockProducts() {
        return repository.findLowStockProducts(LOW_STOCK_THRESHOLD);
    }

    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        
        // Add sample category
        Category category = new Category("cat1", "Electronics", "Electronic devices");
        
        // Add sample products
        Product laptop = manager.addProduct("Laptop", "High-performance laptop", 
                                           999.99, 5, "cat1");
        Product phone = manager.addProduct("Smartphone", "Latest smartphone", 
                                          699.99, 15, "cat1");
        
        // Process stock movements
        manager.processStockIn(laptop.getId(), 10, "Initial stock");
        manager.processStockOut(phone.getId(), 3, "Customer purchase");
        
        // Generate report
        InventoryReport report = manager.generateReport();
        System.out.println(report.generateReport());
    }
}