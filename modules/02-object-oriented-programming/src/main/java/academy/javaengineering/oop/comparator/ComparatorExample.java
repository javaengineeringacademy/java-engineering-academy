package academy.javaengineering.oop.comparator;

import java.util.*;

/**
 * Demonstrates Comparator interface - custom ordering.
 * Comparator provides multiple ways to sort objects.
 * More flexible than Comparable.
 */
public class ComparatorExample {

    static class Product {
        private String name;
        private double price;
        private int rating;

        public Product(String name, double price, int rating) {
            this.name = name;
            this.price = price;
            this.rating = rating;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getRating() { return rating; }

        @Override
        public String toString() {
            return name + " ($" + price + ", Rating: " + rating + "/5)";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Comparator Interface ===");
        
        List<Product> products = new ArrayList<>(Arrays.asList(
            new Product("Laptop", 999.99, 4),
            new Product("Phone", 699.99, 5),
            new Product("Tablet", 499.99, 4),
            new Product("Headphones", 149.99, 3),
            new Product("Monitor", 349.99, 4)
        ));

        // 1. Anonymous class Comparator
        System.out.println("\n--- Sort by Price (Ascending) - Anonymous Class ---");
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return Double.compare(p1.getPrice(), p2.getPrice());
            }
        });
        products.forEach(System.out::println);

        // 2. Lambda Comparator
        System.out.println("\n--- Sort by Price (Descending) - Lambda ---");
        products.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        products.forEach(System.out::println);

        // 3. Comparator.comparing() - Method Reference style
        System.out.println("\n--- Sort by Rating (Descending) - Comparator.comparing ---");
        products.sort(Comparator.comparingInt(Product::getRating).reversed());
        products.forEach(System.out::println);

        // 4. Comparator.comparing() with thenComparing
        System.out.println("\n--- Sort by Rating Desc, then Price Asc ---");
        products.sort(
            Comparator.comparingInt(Product::getRating).reversed()
                      .thenComparingDouble(Product::getPrice)
        );
        products.forEach(System.out::println);

        // 5. Multiple criteria
        System.out.println("\n--- Sort by Name (Alphabetical) ---");
        products.sort(Comparator.comparing(Product::getName));
        products.forEach(System.out::println);
    }
}
