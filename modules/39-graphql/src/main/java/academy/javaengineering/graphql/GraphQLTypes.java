package academy.javaengineering.graphql;

import java.util.List;

/**
 * Demonstrates GraphQL schema types.
 */
public class GraphQLTypes {

    public record ProductType(
        Long id,
        String name,
        String description,
        double price,
        int stock
    ) {}

    public record OrderType(
        Long id,
        String orderNumber,
        List<OrderItemType> items,
        double totalAmount,
        String status
    ) {}

    public record OrderItemType(
        Long id,
        ProductType product,
        int quantity,
        double price
    ) {}

    public record CustomerType(
        Long id,
        String name,
        String email,
        List<OrderType> orders
    ) {}
}
