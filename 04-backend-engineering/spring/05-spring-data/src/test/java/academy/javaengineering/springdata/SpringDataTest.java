package academy.javaengineering.springdata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SpringDataTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(productService);
        assertNotNull(productRepository);
    }

    @Test
    void createAndRetrieveProduct() {
        Product saved = productService.createProduct("Laptop", "Gaming laptop", new BigDecimal("999.99"), 10);

        assertNotNull(saved.getId());
        assertEquals("Laptop", saved.getName());
        assertTrue(saved.isInStock());

        Optional<Product> found = productService.getProduct(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Gaming laptop", found.get().getDescription());
    }

    @Test
    void searchByNameWorks() {
        productService.createProduct("Phone", "Smart phone", new BigDecimal("599.99"), 25);
        productService.createProduct("Laptop", "Gaming laptop", new BigDecimal("999.99"), 10);
        productService.createProduct("Tablet", "iPad", new BigDecimal("449.99"), 15);

        List<Product> phones = productService.searchByName("phone");
        assertEquals(1, phones.size());
        assertEquals("Phone", phones.get(0).getName());
    }

    @Test
    void priceRangeFilterWorks() {
        productService.createProduct("Cheap", "Item", new BigDecimal("10.00"), 5);
        productService.createProduct("Mid", "Item", new BigDecimal("50.00"), 5);
        productService.createProduct("Expensive", "Item", new BigDecimal("100.00"), 5);

        List<Product> inRange = productService.getProductsInPriceRange(
            new BigDecimal("20.00"), new BigDecimal("80.00"));

        assertEquals(1, inRange.size());
        assertEquals("Mid", inRange.get(0).getName());
    }

    @Test
    void stockTrackingWorks() {
        Product p1 = productService.createProduct("A", "Desc", new BigDecimal("10.00"), 10);
        Product p2 = productService.createProduct("B", "Desc", new BigDecimal("10.00"), 0);

        assertEquals(1, productService.getInStockCount());
        assertFalse(p2.isInStock());
    }
}
