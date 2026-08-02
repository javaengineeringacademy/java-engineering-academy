package academy.javaengineering.functional.project;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Functional Data Pipeline Engine - Mini Project
 *
 * <p>This class demonstrates a complete functional data processing system
 * using all the concepts learned in the Functional Programming module.</p>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class FunctionalPipelineApp {

    private FunctionalPipelineApp() {
        // Application class - no instantiation
    }

    // ==================== Data Models ====================

    public record Customer(String id, String name, String email, String city) {}

    public record Product(String id, String name, String category, BigDecimal price) {}

    public record OrderItem(String productId, int quantity, BigDecimal unitPrice) {
        public BigDecimal lineTotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public record Order(
        String id,
        String customerId,
        List<OrderItem> items,
        LocalDateTime createdAt,
        OrderStatus status
    ) {
        public BigDecimal totalAmount() {
            return items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    public record OrderSummary(
        String orderId,
        String customerName,
        BigDecimal totalAmount,
        int itemCount,
        String status
    ) {}

    // ==================== Functional Interfaces ====================

    @FunctionalInterface
    public interface Transformer<T, R> {
        R transform(T input);

        default <V> Transformer<T, R> andThen(Transformer<R, V> after) {
            return input -> after.transform(this.transform(input));
        }

        static <T> Transformer<T, T> identity() {
            return input -> input;
        }
    }

    @FunctionalInterface
    public interface Validator<T> {
        boolean validate(T input);

        default Validator<T> and(Validator<T> other) {
            return input -> this.validate(input) && other.validate(input);
        }

        default Validator<T> or(Validator<T> other) {
            return input -> this.validate(input) || other.validate(input);
        }

        default Validator<T> negate() {
            return input -> !this.validate(input);
        }
    }

    // ==================== Pipeline Builder ====================

    public static class PipelineBuilder<I, O> {
        private final Transformer<I, O> transformer;

        private PipelineBuilder(Transformer<I, O> transformer) {
            this.transformer = transformer;
        }

        public static <T> PipelineBuilder<T, T> create() {
            return new PipelineBuilder<>(Transformer.identity());
        }

        public <R> PipelineBuilder<I, R> addStep(Transformer<O, R> step) {
            return new PipelineBuilder<>(transformer.andThen(step));
        }

        public PipelineBuilder<I, O> addFilter(Predicate<O> predicate) {
            return new PipelineBuilder<>(input -> {
                O result = transformer.transform(input);
                if (!predicate.test(result)) {
                    throw new IllegalArgumentException("Filter predicate not satisfied");
                }
                return result;
            });
        }

        public O apply(I input) {
            return transformer.transform(input);
        }
    }

    // ==================== Custom Collectors ====================

    public static <T, K> java.util.stream.Collector<T, ?, java.util.Map<K, Long>> countingBy(
            Function<T, K> classifier) {
        return java.util.stream.Collector.of(
            java.util.HashMap::new,
            (map, item) -> map.merge(classifier.apply(item), 1L, Long::sum),
            (map1, map2) -> {
                map2.forEach((k, v) -> map1.merge(k, v, Long::sum));
                return map1;
            }
        );
    }

    // ==================== Analytics Service ====================

    public static class AnalyticsService {

        public java.util.Map<String, BigDecimal> calculateRevenueByCustomer(
                List<Order> orders) {
            return orders.stream()
                .filter(o -> o.status() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                    Order::customerId,
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        Order::totalAmount,
                        BigDecimal::add
                    )
                ));
        }

        public java.util.Map<String, Long> countByStatus(List<Order> orders) {
            return orders.stream()
                .collect(countingBy(o -> o.status().name()));
        }

        public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
            return orders.stream()
                .max(java.util.Comparator.comparing(Order::totalAmount));
        }

        public BigDecimal calculateAverageOrderValue(List<Order> orders) {
            return orders.stream()
                .mapToDouble(o -> o.totalAmount().doubleValue())
                .average()
                .orElse(0.0);
        }

        public List<Order> getRecentOrders(List<Order> orders, int days) {
            LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
            return orders.stream()
                .filter(o -> o.createdAt().isAfter(cutoff))
                .sorted(java.util.Comparator.comparing(Order::createdAt).reversed())
                .toList();
        }
    }

    // ==================== Main Application ====================

    public static void main(String[] args) {
        System.out.println("=== Functional Data Pipeline Engine ===\n");

        // Create sample data
        List<Customer> customers = createCustomers();
        List<Product> products = createProducts();
        List<Order> orders = createOrders();

        // Initialize services
        AnalyticsService analyticsService = new AnalyticsService();

        // 1. Build order summary pipeline
        System.out.println("1. Order Summaries:");
        Transformer<Order, OrderSummary> toSummary = createOrderSummaryTransformer(customers);
        
        List<OrderSummary> summaries = orders.stream()
            .map(toSummary::transform)
            .sorted(java.util.Comparator.comparing(OrderSummary::totalAmount).reversed())
            .toList();
        
        summaries.forEach(s -> System.out.println("  " + s));

        // 2. Revenue by customer
        System.out.println("\n2. Revenue by Customer:");
        java.util.Map<String, BigDecimal> revenueByCustomer = analyticsService
            .calculateRevenueByCustomer(orders);
        revenueByCustomer.forEach((id, amount) -> 
            System.out.printf("  Customer %s: $%s%n", id, amount));

        // 3. Order count by status
        System.out.println("\n3. Order Count by Status:");
        java.util.Map<String, Long> countByStatus = analyticsService.countByStatus(orders);
        countByStatus.forEach((status, count) -> 
            System.out.printf("  %s: %d%n", status, count));

        // 4. Most expensive order
        System.out.println("\n4. Most Expensive Order:");
        analyticsService.findMostExpensiveOrder(orders)
            .ifPresent(order -> 
                System.out.printf("  Order %s: $%s%n", order.id(), order.totalAmount()));

        // 5. Average order value
        System.out.println("\n5. Average Order Value:");
        BigDecimal avgValue = analyticsService.calculateAverageOrderValue(orders);
        System.out.printf("  $%s%n", avgValue);

        // 6. Recent orders
        System.out.println("\n6. Recent Orders (last 7 days):");
        List<Order> recentOrders = analyticsService.getRecentOrders(orders, 7);
        recentOrders.forEach(o -> System.out.println("  " + o.id()));

        // 7. Pipeline builder example
        System.out.println("\n7. Pipeline Builder Example:");
        PipelineBuilder<String, String> textPipeline = PipelineBuilder.<String>create()
            .addStep(String::trim)
            .addStep(String::toLowerCase)
            .addStep(s -> s.replaceAll("[^a-z0-9\\s]", ""))
            .addStep(s -> s.replaceAll("\\s+", "_"));
        
        System.out.println("  Input: '  Hello, World!  '");
        System.out.println("  Output: " + textPipeline.apply("  Hello, World!  "));

        System.out.println("\n=== Project Complete ===");
    }

    // ==================== Helper Methods ====================

    private static Transformer<Order, OrderSummary> createOrderSummaryTransformer(
            List<Customer> customers) {
        return order -> {
            Optional<Customer> customer = customers.stream()
                .filter(c -> c.id().equals(order.customerId()))
                .findFirst();

            return new OrderSummary(
                order.id(),
                customer.map(Customer::name).orElse("Unknown"),
                order.totalAmount(),
                order.items().size(),
                order.status().name()
            );
        };
    }

    private static List<Customer> createCustomers() {
        return List.of(
            new Customer("C001", "Alice", "alice@example.com", "New York"),
            new Customer("C002", "Bob", "bob@example.com", "London"),
            new Customer("C003", "Charlie", "charlie@example.com", "New York")
        );
    }

    private static List<Product> createProducts() {
        return List.of(
            new Product("P001", "Laptop", "Electronics", new BigDecimal("999.99")),
            new Product("P002", "Phone", "Electronics", new BigDecimal("699.99")),
            new Product("P003", "Desk", "Furniture", new BigDecimal("299.99")),
            new Product("P004", "Chair", "Furniture", new BigDecimal("149.99"))
        );
    }

    private static List<Order> createOrders() {
        return List.of(
            new Order("O001", "C001",
                List.of(new OrderItem("P001", 1, new BigDecimal("999.99"))),
                LocalDateTime.now().minusDays(5), OrderStatus.DELIVERED),
            new Order("O002", "C002",
                List.of(new OrderItem("P002", 2, new BigDecimal("699.99"))),
                LocalDateTime.now().minusDays(3), OrderStatus.DELIVERED),
            new Order("O003", "C001",
                List.of(new OrderItem("P003", 1, new BigDecimal("299.99"))),
                LocalDateTime.now().minusDays(1), OrderStatus.PENDING),
            new Order("O004", "C003",
                List.of(new OrderItem("P004", 3, new BigDecimal("149.99"))),
                LocalDateTime.now().minusDays(2), OrderStatus.PROCESSING),
            new Order("O005", "C002",
                List.of(new OrderItem("P001", 1, new BigDecimal("999.99"))),
                LocalDateTime.now().minusDays(10), OrderStatus.CANCELLED)
        );
    }
}
