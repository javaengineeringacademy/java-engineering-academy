package persistence;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import Product;
import Category;
import StockMovement;

/**
 * FileInventoryRepository implements InventoryRepository using file storage.
 * Uses simple text-based format for persistence.
 */
public class FileInventoryRepository implements InventoryRepository {
    private static final String DATA_DIR = "data";
    private static final String PRODUCTS_FILE = DATA_DIR + "/products.txt";
    private static final String CATEGORIES_FILE = DATA_DIR + "/categories.txt";
    private static final String MOVEMENTS_FILE = DATA_DIR + "/movements.txt";

    public FileInventoryRepository() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory", e);
        }
    }

    @Override
    public void saveProduct(Product product) {
        List<Product> products = findAllProducts();
        products.add(product);
        saveProductsToFile(products);
    }

    @Override
    public Optional<Product> findProductById(String id) {
        return findAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Product> findAllProducts() {
        return readProductsFromFile();
    }

    @Override
    public void updateProduct(Product product) {
        List<Product> products = findAllProducts();
        products.removeIf(p -> p.getId().equals(product.getId()));
        products.add(product);
        saveProductsToFile(products);
    }

    @Override
    public void deleteProduct(String id) {
        List<Product> products = findAllProducts();
        products.removeIf(p -> p.getId().equals(id));
        saveProductsToFile(products);
    }

    @Override
    public void saveCategory(Category category) {
        List<Category> categories = findAllCategories();
        categories.add(category);
        saveCategoriesToFile(categories);
    }

    @Override
    public Optional<Category> findCategoryById(String id) {
        return findAllCategories().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Category> findAllCategories() {
        return readCategoriesFromFile();
    }

    @Override
    public void deleteCategory(String id) {
        List<Category> categories = findAllCategories();
        categories.removeIf(c -> c.getId().equals(id));
        saveCategoriesToFile(categories);
    }

    @Override
    public void saveStockMovement(StockMovement movement) {
        List<StockMovement> movements = findAllMovements();
        movements.add(movement);
        saveMovementsToFile(movements);
    }

    @Override
    public List<StockMovement> findMovementsByProductId(String productId) {
        return findAllMovements().stream()
                .filter(m -> m.getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    @Override
    public List<StockMovement> findAllMovements() {
        return readMovementsFromFile();
    }

    @Override
    public List<Product> findProductsByCategory(String categoryId) {
        return findAllProducts().stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findLowStockProducts(int threshold) {
        return findAllProducts().stream()
                .filter(p -> p.getQuantity() <= threshold)
                .collect(Collectors.toList());
    }

    @Override
    public int calculateTotalStockValue() {
        return findAllProducts().stream()
                .mapToInt(p -> (int) (p.getPrice() * p.getQuantity()))
                .sum();
    }

    // File I/O methods (simplified for demonstration)
    private List<Product> readProductsFromFile() {
        // Implementation would read from file and parse
        return new ArrayList<>();
    }

    private void saveProductsToFile(List<Product> products) {
        // Implementation would write to file
    }

    private List<Category> readCategoriesFromFile() {
        return new ArrayList<>();
    }

    private void saveCategoriesToFile(List<Category> categories) {
        // Implementation would write to file
    }

    private List<StockMovement> readMovementsFromFile() {
        return new ArrayList<>();
    }

    private void saveMovementsToFile(List<StockMovement> movements) {
        // Implementation would write to file
    }
}