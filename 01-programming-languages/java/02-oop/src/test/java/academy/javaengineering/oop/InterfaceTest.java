package academy.javaengineering.oop;

import academy.javaengineering.oop.interfaces.InterfaceExample.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Interface Tests")
class InterfaceTest {

    @Test
    @DisplayName("Valid product passes validation")
    void validProduct() {
        Product product = new Product(1, "Widget", 29.99, "Electronics", "admin");
        assertTrue(product.isValid());
        assertTrue(product.validate().isEmpty());
    }

    @Test
    @DisplayName("Invalid product fails validation")
    void invalidProduct() {
        Product product = new Product(1, "", -5.0, "", "admin");
        assertFalse(product.isValid());
        assertFalse(product.validate().isEmpty());
    }

    @Test
    @DisplayName("Product implements Auditable interface")
    void productAuditable() {
        Product product = new Product(1, "Widget", 29.99, "Electronics", "admin");
        String audit = product.auditSummary();
        assertTrue(audit.contains("Product"));
        assertTrue(audit.contains("1"));
        assertTrue(audit.contains("admin"));
    }

    @Test
    @DisplayName("Product is Persisted based on ID")
    void productPersisted() {
        Product persisted = new Product(1, "A", 10.0, "X", "admin");
        Product notPersisted = new Product(0, "B", 20.0, "Y", "admin");

        assertTrue(persisted.isPersisted());
        assertFalse(notPersisted.isPersisted());
    }

    @Test
    @DisplayName("Product catalog filter works")
    void catalogFilter() {
        ProductCatalog catalog = new ProductCatalog();
        catalog.add(new Product(1, "Laptop", 999.99, "Electronics", "admin"));
        catalog.add(new Product(2, "Mouse", 29.99, "Electronics", "admin"));
        catalog.add(new Product(3, "Desk", 199.99, "Furniture", "admin"));

        List<Product> electronics = catalog.filter(p -> "Electronics".equals(p.getCategory()));
        assertEquals(2, electronics.size());

        List<Product> expensive = catalog.filter(p -> p.getPrice() > 100);
        assertEquals(2, expensive.size());
    }

    @Test
    @DisplayName("Functional interface transformation")
    void functionalTransform() {
        ProductCatalog catalog = new ProductCatalog();
        catalog.add(new Product(1, "Widget", 10.0, "X", "admin"));

        List<String> names = catalog.map(p -> p.getName().toUpperCase());
        assertEquals(List.of("WIDGET"), names);

        List<Double> prices = catalog.map(Product::getPrice);
        assertEquals(List.of(10.0), prices);
    }

    @Test
    @DisplayName("Filter composition with and/negate")
    void filterComposition() {
        ProductCatalog catalog = new ProductCatalog();
        catalog.add(new Product(1, "Laptop", 999.99, "Electronics", "admin"));
        catalog.add(new Product(2, "Mouse", 29.99, "Electronics", "admin"));
        catalog.add(new Product(3, "Desk", 199.99, "Furniture", "admin"));

        ProductFilter electronics = p -> "Electronics".equals(p.getCategory());
        ProductFilter expensive = p -> p.getPrice() > 100;

        List<Product> expensiveElectronics = catalog.filter(expensive.and(electronics));
        assertEquals(1, expensiveElectronics.size());
        assertEquals("Laptop", expensiveElectronics.get(0).getName());

        List<Product> cheap = catalog.filter(expensive.negate());
        assertEquals(1, cheap.size());
        assertEquals("Mouse", cheap.get(0).getName());
    }

    @Test
    @DisplayName("Order entity type override")
    void orderEntityType() {
        Order order = new Order(1001, "Alice", "system");
        assertEquals("Order", order.getEntityType());
        assertTrue(order.isPersisted());
        assertTrue(order.auditSummary().contains("Order"));
    }
}
