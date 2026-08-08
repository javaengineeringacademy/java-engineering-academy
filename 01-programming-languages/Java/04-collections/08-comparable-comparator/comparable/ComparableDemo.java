import java.util.*;

/**
 * Demonstrates Comparable interface for natural ordering.
 *
 * <p>The Comparable interface defines the natural ordering of objects.
 * Classes implementing Comparable can be sorted using Collections.sort()
 * or Arrays.sort() without a separate Comparator.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Natural ordering via compareTo() method</li>
 *   <li>Returns negative, zero, or positive integer</li>
 *   <li>Used by TreeSet, TreeMap, Collections.sort()</li>
 *   <li>compareTo must be consistent with equals</li>
 *   <li>Transitivity and symmetry requirements</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ComparableDemo {

    public static void main(String[] args) {
        demonstrateComparableImplementation();
        demonstrateStringComparison();
        demonstrateSortingWithComparable();
        demonstrateCustomComparable();
    }

    /**
     * Demonstrates implementing Comparable in a custom class.
     */
    private static void demonstrateComparableImplementation() {
        System.out.println("=== Comparable Implementation ===");

        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 30));
        people.add(new Person("Bob", 25));
        people.add(new Person("Charlie", 35));

        System.out.println("Before sorting: " + people);
        Collections.sort(people);
        System.out.println("After sorting: " + people);
        System.out.println();
    }

    /**
     * Demonstrates String's Comparable implementation.
     */
    private static void demonstrateStringComparison() {
        System.out.println("=== String Comparison ===");

        String a = "Apple";
        String b = "Banana";
        String c = "apple";

        System.out.println("\"Apple\".compareTo(\"Banana\"): " + a.compareTo(b));
        System.out.println("\"Banana\".compareTo(\"Apple\"): " + b.compareTo(a));
        System.out.println("\"Apple\".compareTo(\"Apple\"): " + a.compareTo("Apple"));
        System.out.println("\"Apple\".compareTo(\"apple\"): " + a.compareTo(c));

        // Case-insensitive comparison
        System.out.println("Case-insensitive: " + a.compareToIgnoreCase(c));
        System.out.println();
    }

    /**
     * Demonstrates sorting with Comparable.
     */
    private static void demonstrateSortingWithComparable() {
        System.out.println("=== Sorting with Comparable ===");

        // Integer (natural ordering)
        List<Integer> numbers = new ArrayList<>(List.of(5, 2, 8, 1, 9, 3));
        Collections.sort(numbers);
        System.out.println("Sorted integers: " + numbers);

        // TreeSet uses Comparable
        TreeSet<String> treeSet = new TreeSet<>(List.of("Banana", "Apple", "Cherry"));
        System.out.println("TreeSet (sorted): " + treeSet);

        // TreeMap uses Comparable keys
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(3, "Charlie");
        map.put(1, "Alice");
        map.put(2, "Bob");
        System.out.println("TreeMap (sorted): " + map);
        System.out.println();
    }

    /**
     * Demonstrates custom Comparable with multiple fields.
     */
    private static void demonstrateCustomComparable() {
        System.out.println("=== Custom Comparable ===");

        List<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 3.8));
        students.add(new Student("Bob", 3.5));
        students.add(new Student("Charlie", 3.8));
        students.add(new Student("Diana", 3.9));

        System.out.println("Before sorting: " + students);
        Collections.sort(students);
        System.out.println("After sorting (GPA desc, name asc): " + students);
    }

    /**
     * Person class implementing Comparable by name.
     */
    static class Person implements Comparable<Person> {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public int compareTo(Person other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }

    /**
     * Student class with multi-field comparison.
     */
    static class Student implements Comparable<Student> {
        String name;
        double gpa;

        Student(String name, double gpa) {
            this.name = name;
            this.gpa = gpa;
        }

        @Override
        public int compareTo(Student other) {
            // Sort by GPA descending
            int gpaCompare = Double.compare(other.gpa, this.gpa);
            if (gpaCompare != 0) return gpaCompare;
            // Then by name ascending
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + "(" + gpa + ")";
        }
    }
}
