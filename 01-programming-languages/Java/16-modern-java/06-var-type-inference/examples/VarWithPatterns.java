package academy.javaengineering.modern.vartype;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * var with real-world patterns.
 */
public class VarWithPatterns {

    public record Order(String id, String customer, double total, String status) {}
    public record Product(String name, double price, String category) {}

    public static void main(String[] args) {
        // Data processing pipeline
        System.out.println("=== Data Processing Pipeline ===");
        var orders = getOrders();
        var products = getProducts();

        // Filter and transform
        var highValueOrders = orders.stream()
            .filter(o -> o.total() > 100)
            .collect(Collectors.toList());
        System.out.println("High value orders: " + highValueOrders.size());

        // Grouping
        var ordersByStatus = orders.stream()
            .collect(Collectors.groupingBy(Order::status));
        System.out.println("Orders by status: " + ordersByStatus.keySet());

        // Aggregation
        var totalRevenue = orders.stream()
            .mapToDouble(Order::total)
            .sum();
        System.out.println("Total revenue: $" + totalRevenue);

        // Complex transformations
        System.out.println("\n=== Complex Transformations ===");
        var productSummary = products.stream()
            .collect(Collectors.groupingBy(
                Product::category,
                Collectors.summarizingDouble(Product::price)
            ));
        System.out.println("Product summary: " + productSummary);

        // Method chaining with var
        System.out.println("\n=== Method Chaining ===");
        var topProducts = products.stream()
            .sorted((p1, p2) -> Double.compare(p2.price(), p1.price()))
            .limit(3)
            .map(Product::name)
            .collect(Collectors.toList());
        System.out.println("Top 3 products: " + topProducts);

        // var with builder patterns
        System.out.println("\n=== Builder Pattern ===");
        var report = new StringBuilder()
            .append("Sales Report\n")
            .append("============\n")
            .append("Total orders: ").append(orders.size()).append("\n")
            .append("Total revenue: $").append(totalRevenue).append("\n")
            .toString();
        System.out.println(report);
    }

    static List<Order> getOrders() {
        return List.of(
            new Order("001", "Alice", 150.00, "Shipped"),
            new Order("002", "Bob", 75.00, "Pending"),
            new Order("003", "Charlie", 200.00, "Shipped"),
            new Order("004", "Diana", 50.00, "Pending"),
            new Order("005", "Eve", 300.00, "Shipped")
        );
    }

    static List<Product> getProducts() {
        return List.of(
            new Product("Laptop", 999.99, "Electronics"),
            new Product("Phone", 699.99, "Electronics"),
            new Product("Book", 19.99, "Books"),
            new Product("Headphones", 149.99, "Electronics"),
            new Product("Notebook", 9.99, "Books")
        );
    }
}
