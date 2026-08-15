package academy.javaengineering.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * Exercises: Streams (filter, map, reduce, collect)
 *
 * Complete the TODO sections below.
 */
public class StreamExercises {

    // TODO 1: Given a list of orders, find the total revenue
    // Order has: id (int), product (String), amount (double), quantity (int)
    public static class Order {
        private final int id;
        private final String product;
        private final double amount;
        private final int quantity;

        public Order(int id, String product, double amount, int quantity) {
            this.id = id;
            this.product = product;
            this.amount = amount;
            this.quantity = quantity;
        }

        public int getId() { return id; }
        public String getProduct() { return product; }
        public double getAmount() { return amount; }
        public int getQuantity() { return quantity; }
        public double getTotal() { return amount * quantity; }
    }

    public double calculateTotalRevenue(List<Order> orders) {
        // TODO: implement using stream - sum of (amount * quantity)
        return 0.0;
    }

    // TODO 2: Find the top N most expensive products
    public List<String> topNProducts(List<Order> orders, int n) {
        // TODO: implement using stream - distinct products sorted by max price descending
        return List.of();
    }

    // TODO 3: Group orders by product
    public Map<String, List<Order>> groupByProduct(List<Order> orders) {
        // TODO: implement using stream Collectors.groupingBy
        return Map.of();
    }

    // TODO 4: Find orders within a price range
    public List<Order> ordersInRange(List<Order> orders, double minTotal, double maxTotal) {
        // TODO: implement using stream filter
        return List.of();
    }

    // TODO 5: Calculate average order value
    public OptionalDouble averageOrderValue(List<Order> orders) {
        // TODO: implement using stream
        return OptionalDouble.empty();
    }

    // TODO 6: Find the most popular product (by quantity sold)
    public Optional<String> mostPopularProduct(List<Order> orders) {
        // TODO: implement using stream
        return Optional.empty();
    }

    // TODO 7: Partition orders into high-value (total >= 100) and low-value
    public Map<Boolean, List<Order>> partitionByValue(List<Order> orders) {
        // TODO: implement using stream Collectors.partitioningBy
        return Map.of();
    }

    // TODO 8: Create a summary string of all orders
    // Format: "Order #1: Widget - $49.99 x 2 = $99.98"
    // Return as a single String with newlines between orders
    public String orderSummary(List<Order> orders) {
        // TODO: implement using stream map and Collectors.joining
        return "";
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        StreamExercises exercises = new StreamExercises();
        int passed = 0;
        int total = 0;

        List<Order> orders = List.of(
            new Order(1, "Widget", 49.99, 2),
            new Order(2, "Gadget", 99.99, 1),
            new Order(3, "Widget", 49.99, 3),
            new Order(4, "Doohickey", 24.99, 5),
            new Order(5, "Gadget", 99.99, 2),
            new Order(6, "Thingamajig", 149.99, 1)
        );

        System.out.println("=== StreamExercises Tests ===\n");

        // Test 1
        total++;
        double revenue = exercises.calculateTotalRevenue(orders);
        // Widget: 49.99*2 + 49.99*3 = 249.95
        // Gadget: 99.99*1 + 99.99*2 = 299.97
        // Doohickey: 24.99*5 = 124.95
        // Thingamajig: 149.99*1 = 149.99
        // Total: 824.86
        if (Math.abs(revenue - 824.86) < 0.1) {
            System.out.println("Test 1 PASSED: calculateTotalRevenue");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: calculateTotalRevenue - expected ~824.86, got " + revenue);
        }

        // Test 2
        total++;
        List<String> top2 = exercises.topNProducts(orders, 2);
        if (top2.size() == 2 && "Thingamajig".equals(top2.get(0)) && "Gadget".equals(top2.get(1))) {
            System.out.println("Test 2 PASSED: topNProducts");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: topNProducts - got " + top2);
        }

        // Test 3
        total++;
        Map<String, List<Order>> grouped = exercises.groupByProduct(orders);
        if (grouped.get("Widget").size() == 2 && grouped.get("Gadget").size() == 2) {
            System.out.println("Test 3 PASSED: groupByProduct");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: groupByProduct - got " + grouped.keySet());
        }

        // Test 4
        total++;
        List<Order> range = exercises.ordersInRange(orders, 200, 300);
        if (range.size() == 2) {
            System.out.println("Test 4 PASSED: ordersInRange");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: ordersInRange - expected 2, got " + range.size());
        }

        // Test 5
        total++;
        OptionalDouble avg = exercises.averageOrderValue(orders);
        if (avg.isPresent() && Math.abs(avg.getAsDouble() - 137.48) < 0.1) {
            System.out.println("Test 5 PASSED: averageOrderValue");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: averageOrderValue - got " + avg);
        }

        // Test 6
        total++;
        Optional<String> popular = exercises.mostPopularProduct(orders);
        if (popular.isPresent() && "Widget".equals(popular.get())) {
            System.out.println("Test 6 PASSED: mostPopularProduct");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: mostPopularProduct - got " + popular);
        }

        // Test 7
        total++;
        Map<Boolean, List<Order>> partitioned = exercises.partitionByValue(orders);
        // High value (>=100): Widget*2=249.95, Gadget*1=99.99(No!), Gadget*2=199.98, Doohickey*5=124.95, Thingamajig=149.99
        // Let's recount: total >= 100
        // Order 1: 99.99 < 100 -> false
        // Order 2: 99.99 < 100 -> false
        // Order 3: 149.97 >= 100 -> true
        // Order 4: 124.95 >= 100 -> true
        // Order 5: 199.98 >= 100 -> true
        // Order 6: 149.99 >= 100 -> true
        if (partitioned.get(true).size() == 4 && partitioned.get(false).size() == 2) {
            System.out.println("Test 7 PASSED: partitionByValue");
            passed++;
        } else {
            System.out.println("Test 7 FAILED: partitionByValue");
        }

        // Test 8
        total++;
        String summary = exercises.orderSummary(orders);
        if (summary.contains("Order #1") && summary.contains("Widget") && summary.contains("$49.99")) {
            System.out.println("Test 8 PASSED: orderSummary");
            passed++;
        } else {
            System.out.println("Test 8 FAILED: orderSummary");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
