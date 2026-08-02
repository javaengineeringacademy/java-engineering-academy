package academy.javaengineering.graphql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GraphQL Tests")
class GraphQLTest {

    @Test
    @DisplayName("GraphQLTypes should create product record")
    void testProductType() {
        var product = new GraphQLTypes.ProductType(1L, "Laptop", "Description", 999.99, 10);
        
        assertEquals(1L, product.id());
        assertEquals("Laptop", product.name());
        assertEquals(999.99, product.price(), 0.01);
        assertEquals(10, product.stock());
    }

    @Test
    @DisplayName("GraphQLDataFetchers should return products")
    void testDataFetchers() {
        var fetchers = new GraphQLDataFetchers();
        var products = fetchers.getAllProducts().get(null);
        
        assertNotNull(products);
        assertEquals(3, products.size());
    }

    @Test
    @DisplayName("GraphQLSchemaBuilder should build GraphQL instance")
    void testSchemaBuilder() {
        var builder = new GraphQLSchemaBuilder();
        var graphQL = builder.buildGraphQL();
        
        assertNotNull(graphQL);
    }
}
