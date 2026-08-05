package academy.javaengineering.springdata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spring Data JPA Tests")
class SpringDataTest {

    @Test
    @DisplayName("Product should be created with correct properties")
    void testProductCreation() {
        Product product = new Product("Laptop", 999.99, 10);
        
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice(), 0.01);
        assertEquals(10, product.getQuantity());
    }

    @Test
    @DisplayName("Product should have default constructor")
    void testDefaultConstructor() {
        Product product = new Product();
        assertNotNull(product);
    }

    @Test
    @DisplayName("Product should allow setting properties")
    void testSetters() {
        Product product = new Product();
        product.setName("Phone");
        product.setPrice(699.99);
        product.setQuantity(5);
        
        assertEquals("Phone", product.getName());
        assertEquals(699.99, product.getPrice(), 0.01);
        assertEquals(5, product.getQuantity());
    }
}
