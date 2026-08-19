package academy.javaengineering.patterns.enterprise.specification;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tests for the Specification pattern.
 */
public class SpecificationTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Specification Pattern Tests ===\n");

        testPriceSpecification();
        testCategorySpecification();
        testAndSpecification();
        testOrSpecification();
        testNotSpecification();
        testDefaultMethods();
        testComplexComposition();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static Product laptop = new Product("Laptop", 999.99, "Electronics");
    private static Product book = new Product("Book", 14.99, "Books");
    private static Product headphones = new Product("Headphones", 49.99, "Electronics");

    private static void testPriceSpecification() {
        Specification<Product> under100 = new PriceSpecification(100);
        assertTest("Price <= 100: headphones", under100.isSatisfiedBy(headphones));
        assertTest("Price <= 100: book", under100.isSatisfiedBy(book));
        assertTest("Price <= 100: laptop false", !under100.isSatisfiedBy(laptop));
    }

    private static void testCategorySpecification() {
        Specification<Product> electronics = new CategorySpecification("Electronics");
        assertTest("Category Electronics: laptop", electronics.isSatisfiedBy(laptop));
        assertTest("Category Electronics: headphones", electronics.isSatisfiedBy(headphones));
        assertTest("Category Electronics: book false", !electronics.isSatisfiedBy(book));
    }

    private static void testAndSpecification() {
        Specification<Product> under100 = new PriceSpecification(100);
        Specification<Product> electronics = new CategorySpecification("Electronics");
        Specification<Product> spec = new AndSpecification<>(under100, electronics);
        assertTest("AND: headphones true", spec.isSatisfiedBy(headphones));
        assertTest("AND: laptop false (price)", !spec.isSatisfiedBy(laptop));
        assertTest("AND: book false (category)", !spec.isSatisfiedBy(book));
    }

    private static void testOrSpecification() {
        Specification<Product> books = new CategorySpecification("Books");
        Specification<Product> expensive = new PriceSpecification(100);
        Specification<Product> spec = new OrSpecification<>(books, expensive);
        assertTest("OR: book true", spec.isSatisfiedBy(book));
        assertTest("OR: laptop true", spec.isSatisfiedBy(laptop));
        assertTest("OR: headphones false", !spec.isSatisfiedBy(headphones));
    }

    private static void testNotSpecification() {
        Specification<Product> electronics = new CategorySpecification("Electronics");
        Specification<Product> notElectronics = new NotSpecification<>(electronics);
        assertTest("NOT: book true", notElectronics.isSatisfiedBy(book));
        assertTest("NOT: laptop false", !notElectronics.isSatisfiedBy(laptop));
    }

    private static void testDefaultMethods() {
        Specification<Product> electronics = new CategorySpecification("Electronics");
        Specification<Product> cheap = new PriceSpecification(100);

        Specification<Product> andSpec = electronics.and(cheap);
        assertTest("Default AND: headphones", andSpec.isSatisfiedBy(headphones));

        Specification<Product> orSpec = electronics.or(cheap);
        assertTest("Default OR: book", orSpec.isSatisfiedBy(book));

        Specification<Product> notSpec = electronics.not();
        assertTest("Default NOT: book", notSpec.isSatisfiedBy(book));
    }

    private static void testComplexComposition() {
        Specification<Product> electronics = new CategorySpecification("Electronics");
        Specification<Product> cheap = new PriceSpecification(100);
        Specification<Product> books = new CategorySpecification("Books");

        // (Electronics AND cheap) OR Books
        Specification<Product> complex = electronics.and(cheap).or(books);
        assertTest("Complex: headphones", complex.isSatisfiedBy(headphones));
        assertTest("Complex: book", complex.isSatisfiedBy(book));
        assertTest("Complex: laptop false", !complex.isSatisfiedBy(laptop));
    }

    private static void assertTest(String name, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + name);
            passed++;
        } else {
            System.out.println("  FAIL: " + name);
            failed++;
        }
    }
}
