import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

/**
 * Unit tests for InventoryManager class.
 * Tests core business logic and operations.
 */
public class InventoryManagerTest {
    private InventoryManager manager;
    private String testProductId;

    @Before
    public void setUp() {
        manager = new InventoryManager();
        Product testProduct = manager.addProduct("Test Product", "Test Description", 
                                                 29.99, 20, "test-category");
        testProductId = testProduct.getId();
    }

    @After
    public void tearDown() {
        manager.deleteProduct(testProductId);
    }

    @Test
    public void testAddProduct() {
        Product product = manager.addProduct("New Product", "Description", 
                                            49.99, 10, "category1");
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("New Product", product.getName());
        assertEquals(49.99, product.getPrice(), 0.001);
        assertEquals(10, product.getQuantity());
    }

    @Test
    public void testGetProduct() {
        Optional<Product> retrieved = manager.getProduct(testProductId);
        assertTrue(retrieved.isPresent());
        assertEquals("Test Product", retrieved.get().getName());
    }

    @Test
    public void testUpdateProduct() {
        boolean updated = manager.updateProduct(testProductId, "Updated Product", 
                                               "Updated Desc", 39.99, 25);
        assertTrue(updated);
        
        Optional<Product> product = manager.getProduct(testProductId);
        assertTrue(product.isPresent());
        assertEquals("Updated Product", product.get().getName());
        assertEquals(25, product.getQuantity());
    }

    @Test
    public void testDeleteProduct() {
        Product tempProduct = manager.addProduct("Temp", "Temp", 10.00, 5, "cat");
        boolean deleted = manager.deleteProduct(tempProduct.getId());
        assertTrue(deleted);
        assertFalse(manager.getProduct(tempProduct.getId()).isPresent());
    }

    @Test
    public void testProcessStockIn() {
        boolean processed = manager.processStockIn(testProductId, 10, "Restocking");
        assertTrue(processed);
        
        Optional<Product> product = manager.getProduct(testProductId);
        assertEquals(30, product.get().getQuantity()); // 20 + 10
    }

    @Test
    public void testProcessStockOut() {
        boolean processed = manager.processStockOut(testProductId, 5, "Sale");
        assertTrue(processed);
        
        Optional<Product> product = manager.getProduct(testProductId);
        assertEquals(15, product.get().getQuantity()); // 20 - 5
    }

    @Test
    public void testProcessStockOutInsufficient() {
        boolean processed = manager.processStockOut(testProductId, 100, "Large order");
        assertFalse(processed);
        
        Optional<Product> product = manager.getProduct(testProductId);
        assertEquals(20, product.get().getQuantity()); // unchanged
    }

    @Test
    public void testGetLowStockProducts() {
        // Add low stock product
        Product lowProduct = manager.addProduct("Low Stock", "Low", 10.00, 3, "cat");
        
        List<Product> lowStock = manager.getLowStockProducts();
        assertFalse(lowStock.isEmpty());
        
        // Cleanup
        manager.deleteProduct(lowProduct.getId());
    }

    @Test
    public void testGenerateReport() {
        InventoryReport report = manager.generateReport();
        assertNotNull(report);
        assertTrue(report.getTotalProducts() > 0);
        assertNotNull(report.getReportDate());
    }
}