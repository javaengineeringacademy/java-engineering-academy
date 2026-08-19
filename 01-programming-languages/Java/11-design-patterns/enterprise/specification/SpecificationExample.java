package academy.javaengineering.patterns.enterprise.specification;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates the Specification pattern with product filtering.
 */
public class SpecificationExample {

    public static void main(String[] args) {
        System.out.println("=== Specification Pattern Demo ===\n");

        List<Product> products = Arrays.asList(
                new Product("Laptop", 999.99, "Electronics"),
                new Product("Phone", 699.99, "Electronics"),
                new Product("Book", 14.99, "Books"),
                new Product("Headphones", 49.99, "Electronics"),
                new Product("Novel", 9.99, "Books"),
                new Product("Tablet", 399.99, "Electronics")
        );

        // Individual specifications
        Specification<Product> affordable = new PriceSpecification(100);
        Specification<Product> electronics = new CategorySpecification("Electronics");
        Specification<Product> books = new CategorySpecification("Books");

        // Filter: Electronics under $100
        System.out.println("--- Electronics under $100 ---");
        Specification<Product> cheapElectronics = electronics.and(affordable);
        filterProducts(products, cheapElectronics);

        // Filter: Electronics OR Books (all items)
        System.out.println("\n--- Electronics OR Books ---");
        Specification<Product> allItems = electronics.or(books);
        filterProducts(products, allItems);

        // Filter: NOT Electronics (non-electronics)
        System.out.println("\n--- NOT Electronics ---");
        Specification<Product> notElectronics = electronics.not();
        filterProducts(products, notElectronics);

        // Filter: NOT Electronics AND under $15
        System.out.println("\n--- NOT Electronics AND under $15 ---");
        Specification<Product> cheapNonElectronics = notElectronics.and(affordable);
        filterProducts(products, cheapNonElectronics);

        // Filter: Everything (no filter)
        System.out.println("\n--- Everything (no filter) ---");
        Specification<Product> everything = affordable.or(affordable.not());
        filterProducts(products, everything);
    }

    private static void filterProducts(List<Product> products, Specification<Product> spec) {
        List<Product> filtered = products.stream()
                .filter(spec::isSatisfiedBy)
                .collect(Collectors.toList());
        System.out.println("  Spec: " + spec);
        filtered.forEach(p -> System.out.println("    " + p));
    }
}
