package academy.javaengineering.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Map;

/**
 * Demonstrates GraphQL data fetchers.
 */
public class GraphQLDataFetchers {

    public DataFetcher<List<GraphQLTypes.ProductType>> getAllProducts() {
        return environment -> List.of(
            new GraphQLTypes.ProductType(1L, "Laptop", "High-performance laptop", 999.99, 10),
            new GraphQLTypes.ProductType(2L, "Phone", "Smartphone", 699.99, 25),
            new GraphQLTypes.ProductType(3L, "Tablet", "10-inch tablet", 499.99, 15)
        );
    }

    public DataFetcher<GraphQLTypes.ProductType> getProductById() {
        return environment -> {
            Long id = environment.getArgument("id");
            return new GraphQLTypes.ProductType(id, "Product " + id, "Description", 99.99, 5);
        };
    }

    public DataFetcher<List<GraphQLTypes.OrderType>> getCustomerOrders() {
        return environment -> {
            GraphQLTypes.CustomerType customer = environment.getSource();
            return List.of(
                new GraphQLTypes.OrderType(1L, "ORD-001", List.of(), 150.00, "CONFIRMED"),
                new GraphQLTypes.OrderType(2L, "ORD-002", List.of(), 250.00, "SHIPPED")
            );
        };
    }
}
