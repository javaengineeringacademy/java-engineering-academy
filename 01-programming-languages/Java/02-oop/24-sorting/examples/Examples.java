package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Object Sorting Patterns ===\n");

        // WHY: Objects need comparison for sorting. Java provides Comparable + Comparator
        // INTERNAL: Arrays.sort uses dual-pivot quicksort for primitives, TimSort for objects
        // ENGINEERING: Use Comparator for external sorting, Comparable for natural ordering

        java.util.List<Product> products = new java.util.ArrayList<>();
        products.add(new Product("Laptop", 999.99, 4.5));
        products.add(new Product("Phone", 699.99, 4.8));
        products.add(new Product("Tablet", 299.99, 4.2));
        products.add(new Product("Watch", 199.99, 4.6));

        // Natural ordering (Comparable)
        java.util.Collections.sort(products);
        System.out.println("By name: " + products);

        // Custom ordering (Comparator)
        products.sort(java.util.Comparator.comparingDouble(Product::getPrice).reversed());
        System.out.println("By price desc: " + products);

        products.sort(java.util.Comparator.comparingDouble(Product::getRating));
        System.out.println("By rating asc: " + products);

        // TRADE-OFF: Comparable vs Comparator
        // Comparable: single natural ordering, modifies class
        // Comparator: multiple orderings, external to class
    }
}

class Product implements Comparable<Product> {
    private final String name;
    private final double price;
    private final double rating;

    Product(String name, double price, double rating) {
        this.name = name; this.price = price; this.rating = rating;
    }

    public double getPrice() { return price; }
    public double getRating() { return rating; }

    @Override
    public int compareTo(Product other) {
        return this.name.compareTo(other.name); // Natural: alphabetical
    }

    @Override
    public String toString() {
        return name + "($" + price + ", " + rating + ")";
    }
}
