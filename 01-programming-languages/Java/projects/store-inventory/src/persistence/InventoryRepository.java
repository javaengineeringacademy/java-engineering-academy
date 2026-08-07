package persistence;

import java.util.List;
import java.util.Optional;

import Product;
import Category;
import StockMovement;

/**
 * InventoryRepository interface defining data access operations.
 * Follows repository pattern for clean separation of concerns.
 */
public interface InventoryRepository {
    // Product operations
    void saveProduct(Product product);
    Optional<Product> findProductById(String id);
    List<Product> findAllProducts();
    void updateProduct(Product product);
    void deleteProduct(String id);
    
    // Category operations
    void saveCategory(Category category);
    Optional<Category> findCategoryById(String id);
    List<Category> findAllCategories();
    void deleteCategory(String id);
    
    // Stock movement operations
    void saveStockMovement(StockMovement movement);
    List<StockMovement> findMovementsByProductId(String productId);
    List<StockMovement> findAllMovements();
    
    // Query methods
    List<Product> findProductsByCategory(String categoryId);
    List<Product> findLowStockProducts(int threshold);
    int calculateTotalStockValue();
}