import java.util.Comparator;

/**
 * Utility class providing multiple Comparator instances for Student objects.
 * Demonstrates various Comparator patterns including method chaining and reversal.
 *
 * Comparators are used when you need custom sorting orders different from
 * the natural ordering defined by Comparable.
 */
public class StudentComparator {

    // Private constructor to prevent instantiation
    private StudentComparator() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Compares students by name (case-insensitive).
     * Equivalent to Comparable's natural ordering but as a Comparator.
     */
    public static Comparator<Student> byName() {
        return Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Compares students by age in ascending order.
     */
    public static Comparator<Student> byAge() {
        return Comparator.comparingInt(Student::getAge);
    }

    /**
     * Compares students by GPA in descending order (highest first).
     * Uses reversed() to invert the natural ordering.
     */
    public static Comparator<Student> byGPA() {
        return Comparator.comparingDouble(Student::getGpa).reversed();
    }

    /**
     * Compares students by grade alphabetically.
     */
    public static Comparator<Student> byGrade() {
        return Comparator.comparing(Student::getGrade);
    }

    /**
     * Chained comparator: first by name, then by age for students with same name.
     * Demonstrates the thenComparing() method for multi-level sorting.
     */
    public static Comparator<Student> byNameThenAge() {
        return Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER)
                .thenComparingInt(Student::getAge);
    }

    /**
     * Complex chained comparator: by GPA descending, then by name ascending.
     */
    public static Comparator<Student> byGPAThenName() {
        return Comparator.comparingDouble(Student::getGpa).reversed()
                .thenComparing(Student::getName, String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Comparator with null handling: places null values at the end.
     */
    public static Comparator<Student> byNameNullsLast() {
        return Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER,
                Comparator.nullsLast(Comparator.naturalOrder()));
    }

    /**
     * Comparator with null handling: places null values at the beginning.
     */
    public static Comparator<Student> byNameNullsFirst() {
        return Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER,
                Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    /**
     * Reversed comparator: sorts by name in descending order.
     */
    public static Comparator<Student> byNameReversed() {
        return byName().reversed();
    }

    /**
     * Combines multiple comparators with null handling.
     * Sorts by age, then GPA, with null-safe comparisons.
     */
    public static Comparator<Student> byAgeThenGPA() {
        return Comparator.comparingInt(Student::getAge)
                .thenComparingDouble(Student::getGpa);
    }

    /**
     * Demonstrates Comparator chaining with reversed comparators.
     * Sorts by grade ascending, then by name descending.
     */
    public static Comparator<Student> byGradeThenNameReversed() {
        return Comparator.comparing(Student::getGrade)
                .thenComparing(Student::getName, String.CASE_INSENSITIVE_ORDER.reversed());
    }
}