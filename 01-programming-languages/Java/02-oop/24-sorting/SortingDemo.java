import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive demonstration of Java sorting mechanisms.
 * Covers Comparable, Comparator, lambda expressions, method references,
 * null handling, stream sorting, and common patterns.
 */
public class SortingDemo {

    public static void main(String[] args) {
        System.out.println("=== Java Sorting Comprehensive Demo ===\n");

        sortingWithComparable();
        sortingWithComparator();
        sortingWithLambda();
        sortingWithMethodReference();
        sortingDescending();
        sortingWithNulls();
        sortingCustomObjects();
        sortingMapsByValue();
        sortingStream();
    }

    /**
     * Sorting with Comparable - natural ordering.
     * Uses the compareTo() method defined in the Student class.
     */
    static void sortingWithComparable() {
        System.out.println("--- 1. Sorting with Comparable (Natural Ordering) ---");

        List<Student> students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7)
        ));

        // Using Collections.sort() - uses natural ordering (Comparable)
        System.out.println("Before sort: " + students);
        Collections.sort(students);
        System.out.println("After Collections.sort(): " + students);

        // Using Arrays.sort()
        Student[] studentArray = {
                new Student("Eve", 19, "A", 3.6),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Charlie", 22, "A", 3.8)
        };
        System.out.println("Array before sort: " + Arrays.toString(studentArray));
        Arrays.sort(studentArray);
        System.out.println("Array after sort: " + Arrays.toString(studentArray));
        System.out.println();
    }

    /**
     * Sorting with Comparator - custom ordering.
     * Provides a Comparator to define custom sort order.
     */
    static void sortingWithComparator() {
        System.out.println("--- 2. Sorting with Comparator ---");

        List<Student> students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7)
        ));

        // Sort by GPA using Comparator.comparingDouble()
        students.sort(Comparator.comparingDouble(Student::getGpa));
        System.out.println("Sorted by GPA (ascending): " + students);

        // Sort by age using Comparator.comparingInt()
        students.sort(Comparator.comparingInt(Student::getAge));
        System.out.println("Sorted by age (ascending): " + students);

        // Sort by grade
        students.sort(Comparator.comparing(Student::getGrade));
        System.out.println("Sorted by grade: " + students);
        System.out.println();
    }

    /**
     * Sorting with lambda expressions.
     * Lambda comparators provide inline custom sorting logic.
     */
    static void sortingWithLambda() {
        System.out.println("--- 3. Sorting with Lambda Expressions ---");

        List<Student> students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7)
        ));

        // Sort by name length (shortest first)
        students.sort((s1, s2) -> Integer.compare(s1.getName().length(), s2.getName().length()));
        System.out.println("Sorted by name length: " + students);

        // Sort by GPA descending using lambda
        students.sort((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()));
        System.out.println("Sorted by GPA descending (lambda): " + students);

        // Lambda with multiple fields
        students.sort((s1, s2) -> {
            int gradeCompare = s1.getGrade().compareTo(s2.getGrade());
            if (gradeCompare != 0) return gradeCompare;
            return s1.getName().compareTo(s2.getName());
        });
        System.out.println("Sorted by grade then name: " + students);
        System.out.println();
    }

    /**
     * Sorting with method references.
     * Clean syntax using method references for simple property comparisons.
     */
    static void sortingWithMethodReference() {
        System.out.println("--- 4. Sorting with Method References ---");

        List<Student> students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7)
        ));

        // Using method reference with Comparator.comparing()
        students.sort(Comparator.comparing(Student::getName));
        System.out.println("Sorted by name (method reference): " + students);

        // Using Student::compareTo (method reference to instance method)
        students.sort(Student::compareTo);
        System.out.println("Sorted using Student::compareTo: " + students);
        System.out.println();
    }

    /**
     * Sorting in descending order.
     * Multiple approaches for reverse sorting.
     */
    static void sortingDescending() {
        System.out.println("--- 5. Sorting in Descending Order ---");

        List<Student> students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7)
        ));

        // Using Collections.reverseOrder()
        Collections.sort(students, Collections.reverseOrder());
        System.out.println("Reverse natural order: " + students);

        // Using Comparator.reversed()
        students.sort(Comparator.comparing(Student::getName).reversed());
        System.out.println("Name reversed: " + students);

        // Using Comparator.comparing with reversed()
        students.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        System.out.println("GPA descending: " + students);
        System.out.println();
    }

    /**
     * Handling null values in sorting.
     * Comparator.nullsFirst() and Comparator.nullsLast() handle null elements.
     */
    static void sortingWithNulls() {
        System.out.println("--- 6. Sorting with Null Handling ---");

        List<Student> students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                null,
                new Student("Alice", 20, "A+", 3.9),
                null,
                new Student("Bob", 21, "B+", 3.5)
        ));

        // nullsFirst: null values appear before non-null
        students.sort(Comparator.nullsFirst(Comparator.comparing(Student::getName)));
        System.out.println("Nulls first: " + students);

        // nullsLast: null values appear after non-null
        students.sort(Comparator.nullsLast(Comparator.comparing(Student::getName)));
        System.out.println("Nulls last: " + students);

        // Combining null handling with multiple fields
        students.sort(Comparator.nullsLast(
                Comparator.comparing(Student::getName)
                        .thenComparingInt(Student::getAge)
        ));
        System.out.println("Nulls last with chaining: " + students);
        System.out.println();
    }

    /**
     * Complete sorting example with custom objects.
     * Demonstrates practical sorting scenarios.
     */
    static void sortingCustomObjects() {
        System.out.println("--- 7. Sorting Custom Objects (Complete Example) ---");

        List<Student> students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7),
                new Student("Eve", 19, "A-", 3.6),
                new Student("Frank", 21, "A", 3.85)
        ));

        // Sort by multiple criteria using StudentComparator
        System.out.println("Original: " + students);

        students.sort(StudentComparator.byName());
        System.out.println("By name: " + students);

        students.sort(StudentComparator.byGPA());
        System.out.println("By GPA (highest first): " + students);

        students.sort(StudentComparator.byAge());
        System.out.println("By age: " + students);

        students.sort(StudentComparator.byNameThenAge());
        System.out.println("By name then age: " + students);

        students.sort(StudentComparator.byGPAThenName());
        System.out.println("By GPA then name: " + students);

        students.sort(StudentComparator.byGradeThenNameReversed());
        System.out.println("By grade then name reversed: " + students);
        System.out.println();
    }

    /**
     * Sorting Map entries by value.
     * Common pattern for sorting a Map's entries.
     */
    static void sortingMapsByValue() {
        System.out.println("--- 8. Sorting Map Entries by Value ---");

        Map<String, Double> studentGPAs = new LinkedHashMap<>();
        studentGPAs.put("Alice", 3.9);
        studentGPAs.put("Bob", 3.5);
        studentGPAs.put("Charlie", 3.8);
        studentGPAs.put("Diana", 3.7);
        studentGPAs.put("Eve", 3.6);

        System.out.println("Original map: " + studentGPAs);

        // Sort by value (GPA) ascending
        Map<String, Double> sortedAscending = studentGPAs.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        System.out.println("Sorted by GPA ascending: " + sortedAscending);

        // Sort by value (GPA) descending
        Map<String, Double> sortedDescending = studentGPAs.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        System.out.println("Sorted by GPA descending: " + sortedDescending);

        // Sort by key
        Map<String, Double> sortedByKey = new TreeMap<>(studentGPAs);
        System.out.println("Sorted by key (TreeMap): " + sortedByKey);
        System.out.println();
    }

    /**
     * Sorting with Java Streams.
     * Demonstrates stream-based sorting patterns.
     */
    static void sortingStream() {
        System.out.println("--- 9. Sorting with Streams ---");

        List<Student> students = List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7),
                new Student("Eve", 19, "A-", 3.6)
        );

        // Sort and collect
        List<Student> sortedByName = students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
        System.out.println("Stream sorted by name: " + sortedByName);

        // Sort by GPA descending
        List<Student> sortedByGPA = students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .collect(Collectors.toList());
        System.out.println("Stream sorted by GPA desc: " + sortedByGPA);

        // Sort and limit (top 3 by GPA)
        List<Student> top3 = students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("Top 3 by GPA: " + top3);

        // Sort and map to names only
        List<String> namesSorted = students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("Names sorted: " + namesSorted);

        // Parallel sorting
        List<Student> parallelSorted = students.parallelStream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
        System.out.println("Parallel sorted: " + parallelSorted);
        System.out.println();

        // Performance considerations
        System.out.println("=== Performance Considerations ===");
        System.out.println("1. For small lists (< 1000): Arrays.sort() is fastest");
        System.out.println("2. For large lists: Parallel streams can improve performance");
        System.out.println("3. Avoid creating new Comparator objects in loops");
        System.out.println("4. Use primitive streams (IntStream, DoubleStream) for better performance");
        System.out.println("5. Cache Comparator instances when used repeatedly");
    }
}