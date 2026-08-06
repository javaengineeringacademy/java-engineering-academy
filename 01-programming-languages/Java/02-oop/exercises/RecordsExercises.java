package academy.javaengineering.exercises;

import java.util.List;
import java.util.Objects;

/**
 * Exercises: Records (Java 16+)
 *
 * Complete the TODO sections below.
 */
public class RecordsExercises {

    // TODO 1: Create a Point record
    // Fields: x (double), y (double)
    // Add a compact constructor that normalizes:
    // - Round x and y to 2 decimal places
    // Add a method: double distanceTo(Point other) - calculates Euclidean distance
    // Add a method: Point translate(double dx, double dy) - returns new Point

    // TODO 2: Create a Product record
    // Fields: id (String), name (String), price (double), quantity (int)
    // Add a compact constructor that validates:
    // - price must be >= 0, throw IllegalArgumentException otherwise
    // - quantity must be >= 0, throw IllegalArgumentException otherwise
    // Add a method: double getTotalValue() returns price * quantity
    // Override toString to return "name (id): $price x quantity"

    // TODO 3: Create an Address record
    // Fields: street (String), city (String), state (String), zipCode (String)
    // Add a compact constructor that:
    // - Trims whitespace from all fields
    // - Converts state to uppercase
    // Add a method: String toSingleLine() returns "street, city, state zipCode"

    // TODO 4: Create a complex record with another record
    // Record: EmployeeRecord with fields: name (String), department (String), salary (double)
    // Record: DepartmentSummary with fields: departmentName (String), employees (List<EmployeeRecord>), headCount (int)
    // Add a static factory method in DepartmentSummary:
    // - static DepartmentSummary fromEmployees(String deptName, EmployeeRecord... employees)
    // - This method should create the summary with headCount = employees.length

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        System.out.println("=== RecordsExercises Tests ===\n");

        // Test 1: Point Record
        total++;
        try {
            // Uncomment after implementing Point record
            // Point p1 = new Point(1.123, 2.456);
            // Point p2 = new Point(4.0, 6.0);
            // double dist = p1.distanceTo(p2);
            // Point translated = p1.translate(2.0, 3.0);
            // if (Math.abs(p1.x() - 1.12) < 0.01
            //     && Math.abs(p1.y() - 2.46) < 0.01
            //     && dist > 0
            //     && Math.abs(translated.x() - 3.12) < 0.01) {
            //     System.out.println("Test 1 PASSED: Point record");
            //     passed++;
            // } else {
            //     System.out.println("Test 1 FAILED: Point record");
            // }
            System.out.println("Test 1 SKIPPED: Point record - implement record");
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: Point record - " + e.getMessage());
        }

        // Test 2: Product Record
        total++;
        try {
            // Uncomment after implementing Product record
            // Product p = new Product("P001", "Widget", 9.99, 100);
            // if (Math.abs(p.getTotalValue() - 999.0) < 0.01
            //     && p.toString().contains("Widget")
            //     && p.toString().contains("P001")) {
            //     System.out.println("Test 2 PASSED: Product record");
            //     passed++;
            // } else {
            //     System.out.println("Test 2 FAILED: Product record");
            // }
            System.out.println("Test 2 SKIPPED: Product record - implement record");
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: Product record - " + e.getMessage());
        }

        // Test 3: Address Record
        total++;
        try {
            // Uncomment after implementing Address record
            // Address addr = new Address("  123 Main St  ", "  Springfield  ", "il", "62701");
            // if ("123 Main St".equals(addr.street())
            //     && "Springfield".equals(addr.city())
            //     && "IL".equals(addr.state())
            //     && addr.toSingleLine().equals("123 Main St, Springfield, IL 62701")) {
            //     System.out.println("Test 3 PASSED: Address record");
            //     passed++;
            // } else {
            //     System.out.println("Test 3 FAILED: Address record");
            // }
            System.out.println("Test 3 SKIPPED: Address record - implement record");
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: Address record - " + e.getMessage());
        }

        // Test 4: Complex Records
        total++;
        try {
            // Uncomment after implementing records
            // EmployeeRecord emp1 = new EmployeeRecord("Alice", "Engineering", 85000);
            // EmployeeRecord emp2 = new EmployeeRecord("Bob", "Engineering", 75000);
            // DepartmentSummary summary = DepartmentSummary.fromEmployees("Engineering", emp1, emp2);
            // if ("Engineering".equals(summary.departmentName())
            //     && summary.headCount() == 2
            //     && summary.employees().size() == 2) {
            //     System.out.println("Test 4 PASSED: Complex records");
            //     passed++;
            // } else {
            //     System.out.println("Test 4 FAILED: Complex records");
            // }
            System.out.println("Test 4 SKIPPED: Complex records - implement records");
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: Complex records - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
        System.out.println("Note: Uncomment the test code above after implementing the records.");
    }
}
