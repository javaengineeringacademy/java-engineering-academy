package academy.javaengineering.oop.equalshashcode;

/**
 * EqualsHashCodeDemo - Demonstrates proper equals() and hashCode() contract.
 * 
 * <p><b>The Contract:</b>
 * <ul>
 *   <li>If {@code a.equals(b)} is true, then {@code a.hashCode() == b.hashCode()}</li>
 *   <li>If {@code a.hashCode() == b.hashCode()}, {@code a.equals(b)} may be false (hash collision)</li>
 *   <li>{@code equals()} must be: reflexive, symmetric, transitive, consistent</li>
 *   <li>{@code equals(null)} must return false</li>
 * </ul>
 * 
 * <p><b>Why it matters:</b>
 * <ul>
 *   <li>HashMap, HashSet, Hashtable use hashCode() to find bucket</li>
 *   <li>Then use equals() to find exact match within bucket</li>
 *   <li>Broken contract = broken collections!</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class EqualsHashCodeDemo {

    private EqualsHashCodeDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Equals & HashCode Demo ===\n");

        // Proper implementation
        System.out.println("--- Proper Implementation ---");
        Employee emp1 = new Employee(1001, "Alice", "Engineering");
        Employee emp2 = new Employee(1001, "Alice", "Engineering");
        Employee emp3 = new Employee(1002, "Bob", "Marketing");

        System.out.println("emp1: " + emp1);
        System.out.println("emp2: " + emp2);
        System.out.println("emp3: " + emp3);

        System.out.println("\nemp1.equals(emp2): " + emp1.equals(emp2)); // true
        System.out.println("emp1.equals(emp3): " + emp1.equals(emp3)); // false
        System.out.println("emp1.hashCode() == emp2.hashCode(): " +
            (emp1.hashCode() == emp2.hashCode())); // true

        // Using in collections
        System.out.println("\n--- Using in Collections ---");
        java.util.Set<Employee> employeeSet = new java.util.HashSet<>();
        employeeSet.add(emp1);
        employeeSet.add(emp2); // Won't be added (duplicate)
        employeeSet.add(emp3);
        System.out.println("Set size: " + employeeSet.size()); // 2

        // Broken implementation example
        System.out.println("\n--- Broken Implementation (BAD!) ---");
        BadEmployee bad1 = new BadEmployee(1001, "Alice");
        BadEmployee bad2 = new BadEmployee(1001, "Alice");

        System.out.println("bad1.equals(bad2): " + bad1.equals(bad2)); // true
        System.out.println("bad1.hashCode() == bad2.hashCode(): " +
            (bad1.hashCode() == bad2.hashCode())); // FALSE! Contract broken

        java.util.Set<BadEmployee> badSet = new java.util.HashSet<>();
        badSet.add(bad1);
        badSet.add(bad2); // Added! (different hashCode)
        System.out.println("Bad Set size: " + badSet.size()); // 2 (should be 1!)

        // Using Objects utility
        System.out.println("\n--- Using java.util.Objects ---");
        Point p1 = new Point(10, 20);
        Point p2 = new Point(10, 20);
        Point p3 = new Point(30, 40);

        System.out.println("p1: " + p1);
        System.out.println("p2: " + p2);
        System.out.println("p1.equals(p2): " + p1.equals(p2)); // true
        System.out.println("p1.equals(p3): " + p1.equals(p3)); // false

        // Pattern matching with records (Java 16+)
        System.out.println("\n--- Records Auto-Generate equals/hashCode ---");
        var rec1 = new RecordPerson("Charlie", 25);
        var rec2 = new RecordPerson("Charlie", 25);
        System.out.println("rec1.equals(rec2): " + rec1.equals(rec2)); // true
        System.out.println("rec1.hashCode() == rec2.hashCode(): " +
            (rec1.hashCode() == rec2.hashCode())); // true
    }
}