package academy.javaengineering.elasticsearch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Elasticsearch Tests")
class ElasticsearchTest {

    @Test
    @DisplayName("ProductDocument should be created correctly")
    void testProductDocument() {
        var doc = new ProductDocument("1", "Laptop", "High-performance", 999.99, "Electronics");
        
        assertEquals("1", doc.getId());
        assertEquals("Laptop", doc.getName());
        assertEquals("High-performance", doc.getDescription());
        assertEquals(999.99, doc.getPrice(), 0.01);
        assertEquals("Electronics", doc.getCategory());
    }

    @Test
    @DisplayName("ProductDocument should have default constructor")
    void testDefaultConstructor() {
        var doc = new ProductDocument();
        assertNotNull(doc);
    }

    @Test
    @DisplayName("ProductDocument should allow setters")
    void testSetters() {
        var doc = new ProductDocument();
        doc.setId("2");
        doc.setName("Phone");
        doc.setPrice(699.99);
        
        assertEquals("2", doc.getId());
        assertEquals("Phone", doc.getName());
        assertEquals(699.99, doc.getPrice(), 0.01);
    }
}
