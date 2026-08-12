package genericconstraints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Generic Constraints Demo - Complete Guide
 * 
 * Covers Comparable, Serializable, Cloneable constraints,
 * custom type bounds, and practical constraint patterns.
 */
public class GenericConstraintsDemo {

    // ==========================================
    // SECTION 1: Comparable Constraints
    // ==========================================
    static class ComparableConstraints {

        // Sortable container with Comparable bound
        static class SortableList<T extends Comparable<T>> {
            private final List<T> items = new ArrayList<>();

            public void add(T item) { items.add(item); }

            public List<T> getSorted() {
                List<T> sorted = new ArrayList<>(items);
                sorted.sort(Comparator.naturalOrder());
                return sorted;
            }

            public T min() {
                if (items.isEmpty()) throw new IllegalStateException("Empty list");
                T min = items.get(0);
                for (T item : items) {
                    if (item.compareTo(min) < 0) min = item;
                }
                return min;
            }

            public T max() {
                if (items.isEmpty()) throw new IllegalStateException("Empty list");
                T max = items.get(0);
                for (T item : items) {
                    if (item.compareTo(max) > 0) max = item;
                }
                return max;
            }
        }

        // Range checker with Comparable
        static class Range<T extends Comparable<T>> {
            private final T min;
            private final T max;

            public Range(T min, T max) {
                if (min.compareTo(max) > 0) {
                    throw new IllegalArgumentException("min must be <= max");
                }
                this.min = min;
                this.max = max;
            }

            public boolean contains(T value) {
                return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
            }

            public boolean isWithin(T value, Range<T> other) {
                return this.min.compareTo(other.min) >= 0 && this.max.compareTo(other.max) <= 0;
            }

            @Override
            public String toString() {
                return "[" + min + ", " + max + "]";
            }
        }

        static void demonstrateComparable() {
            System.out.println("=== Comparable Constraints ===\n");

            // SortableList with different comparable types
            SortableList<String> strings = new SortableList<>();
            strings.add("banana");
            strings.add("apple");
            strings.add("cherry");
            System.out.println("Sorted strings: " + strings.getSorted());
            System.out.println("Min: " + strings.min());
            System.out.println("Max: " + strings.max());

            SortableList<Integer> numbers = new SortableList<>();
            numbers.add(5);
            numbers.add(2);
            numbers.add(8);
            numbers.add(1);
            System.out.println("\nSorted numbers: " + numbers.getSorted());
            System.out.println("Min: " + numbers.min());
            System.out.println("Max: " + numbers.max());

            // Range operations
            System.out.println("\n--- Range Operations ---");
            Range<Integer> intRange = new Range<>(1, 100);
            System.out.println("Range: " + intRange);
            System.out.println("50 in range? " + intRange.contains(50));
            System.out.println("150 in range? " + intRange.contains(150));

            Range<String> strRange = new Range<>("apple", "cherry");
            System.out.println("\nString range: " + strRange);
            System.out.println("banana in range? " + strRange.contains("banana"));
            System.out.println("date in range? " + strRange.contains("date"));
        }

        // Custom Comparable implementation
        static class Student implements Comparable<Student> {
            private final String name;
            private final double gpa;

            public Student(String name, double gpa) {
                this.name = name;
                this.gpa = gpa;
            }

            @Override
            public int compareTo(Student other) {
                return Double.compare(this.gpa, other.gpa);
            }

            @Override
            public String toString() {
                return name + "(" + gpa + ")";
            }
        }

        static void demonstrateCustomComparable() {
            System.out.println("\n--- Custom Comparable ---");
            SortableList<Student> students = new SortableList<>();
            students.add(new Student("Alice", 3.8));
            students.add(new Student("Bob", 3.5));
            students.add(new Student("Charlie", 3.9));
            System.out.println("Students sorted by GPA: " + students.getSorted());
            System.out.println("Highest GPA: " + students.max());
        }
    }

    // ==========================================
    // SECTION 2: Serializable Constraints
    // ==========================================
    static class SerializableConstraints {

        // Serializable wrapper
        static class SerializableBox<T extends Serializable> implements Serializable {
            private static final long serialVersionUID = 1L;
            private final T value;

            public SerializableBox(T value) {
                this.value = value;
            }

            public T getValue() { return value; }

            @Override
            public String toString() {
                return "Box[" + value + "]";
            }
        }

        // Serializable collection
        static class SerializableList<T extends Serializable> implements Serializable {
            private static final long serialVersionUID = 1L;
            private final List<T> items = new ArrayList<>();

            public void add(T item) { items.add(item); }
            public List<T> getItems() { return new ArrayList<>(items); }

            @Override
            public String toString() {
                return "SerializableList" + items;
            }
        }

        // Method requiring Serializable
        static <T extends Serializable> byte[] serializeObject(T obj) throws java.io.IOException {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            return baos.toByteArray();
        }

        static void demonstrateSerializable() {
            System.out.println("\n=== Serializable Constraints ===\n");

            SerializableBox<String> stringBox = new SerializableBox<>("Hello");
            SerializableBox<Integer> intBox = new SerializableBox<>(42);
            System.out.println("String box: " + stringBox);
            System.out.println("Integer box: " + intBox);

            // Cannot create SerializableBox with non-Serializable
            // SerializableBox<Object> objBox = new SerializableBox<>(new Object()); // COMPILE ERROR

            SerializableList<String> list = new SerializableList<>();
            list.add("first");
            list.add("second");
            System.out.println("Serializable list: " + list);

            // Demonstrate serialization
            try {
                byte[] data = serializeObject(stringBox);
                System.out.println("Serialized to " + data.length + " bytes");
            } catch (java.io.IOException e) {
                System.out.println("Serialization error: " + e.getMessage());
            }
        }
    }

    // ==========================================
    // SECTION 3: Cloneable Constraints
    // ==========================================
    static class CloneableConstraints {

        // Cloneable wrapper
        static class CloneableBox<T extends Cloneable> {
            private final T value;

            public CloneableBox(T value) {
                this.value = value;
            }

            @SuppressWarnings("unchecked")
            public T cloneValue() {
                try {
                    // Use reflection to call clone()
                    java.lang.reflect.Method cloneMethod = value.getClass().getMethod("clone");
                    return (T) cloneMethod.invoke(value);
                } catch (Exception e) {
                    throw new RuntimeException("Clone failed", e);
                }
            }

            public T getValue() { return value; }
        }

        // Deep copy utility with Cloneable bound
        @SuppressWarnings("unchecked")
        static <T extends Cloneable> List<T> deepCopy(List<T> original) {
            List<T> copy = new ArrayList<>();
            for (T item : original) {
                try {
                    java.lang.reflect.Method cloneMethod = item.getClass().getMethod("clone");
                    copy.add((T) cloneMethod.invoke(item));
                } catch (Exception e) {
                    throw new RuntimeException("Deep copy failed", e);
                }
            }
            return copy;
        }

        static class CopyableString implements Cloneable {
            private final String value;

            public CopyableString(String value) {
                this.value = value;
            }

            @Override
            public CopyableString clone() {
                return new CopyableString(value);
            }

            @Override
            public String toString() {
                return value;
            }
        }

        static void demonstrateCloneable() {
            System.out.println("\n=== Cloneable Constraints ===\n");

            CopyableString original = new CopyableString("Hello");
            CloneableBox<CopyableString> box = new CloneableBox<>(original);
            CopyableString cloned = box.cloneValue();
            System.out.println("Original: " + original);
            System.out.println("Cloned: " + cloned);
            System.out.println("Same reference? " + (original == cloned));

            // Deep copy list
            List<CopyableString> originals = Arrays.asList(
                    new CopyableString("A"),
                    new CopyableString("B"),
                    new CopyableString("C")
            );
            List<CopyableString> copies = deepCopy(originals);
            System.out.println("\nOriginals: " + originals);
            System.out.println("Copies: " + copies);
            System.out.println("Same references? " + (originals.get(0) == copies.get(0)));
        }
    }

    // ==========================================
    // SECTION 4: Custom Type Bounds
    // ==========================================
    static class CustomBounds {

        // Custom annotation for validation
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
        @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE)
        @interface Validated {
            String pattern() default "";
        }

        // Custom bounds with annotation check
        static <T extends @Validated(pattern="^[A-Z].*") Object> void processAnnotated(T obj) {
            System.out.println("  Processing: " + obj);
        }

        // Multiple custom bounds
        interface Validatable {
            boolean isValid();
        }

        interface Loggable {
            String toLogString();
        }

        // Class with multiple custom bounds
        static class AuditRecord<T extends Validatable & Loggable & Serializable> {
            private final T record;
            private final List<String> auditLog = new ArrayList<>();

            public AuditRecord(T record) {
                this.record = record;
                auditLog.add("Created audit for: " + record.toLogString());
            }

            public boolean validateAndLog() {
                boolean valid = record.isValid();
                auditLog.add("Validation: " + (valid ? "PASSED" : "FAILED"));
                return valid;
            }

            public List<String> getAuditLog() { return new ArrayList<>(auditLog); }
        }

        // Bounded type with enum constraint
        static <T extends Enum<T>> T parseEnum(Class<T> enumClass, String value) {
            return Enum.valueOf(enumClass, value);
        }

        enum Status { ACTIVE, INACTIVE, PENDING }

        static void demonstrateCustomBounds() {
            System.out.println("\n=== Custom Type Bounds ===\n");

            // Enum constraint
            Status status = parseEnum(Status.class, "ACTIVE");
            System.out.println("Parsed enum: " + status);

            // Multiple custom bounds
            System.out.println("\n--- Multiple Custom Bounds ---");
            // This would need a class implementing Validatable, Loggable, and Serializable
            System.out.println("  Custom bounds enable:");
            System.out.println("    - Compile-time safety");
            System.out.println("    - Multiple interface constraints");
            System.out.println("    - Complex type relationships");
        }

        // Type-safe enum map
        static class EnumMap<K extends Enum<K>, V> {
            private final java.util.Map<K, V> map = new java.util.HashMap<>();
            private final Class<K> keyType;

            public EnumMap(Class<K> keyType) {
                this.keyType = keyType;
            }

            public void put(K key, V value) {
                map.put(key, value);
            }

            public V get(K key) {
                return map.get(key);
            }

            public V getOrDefault(K key, V defaultValue) {
                return map.getOrDefault(key, defaultValue);
            }

            public Class<K> getKeyType() { return keyType; }
        }

        static void demonstrateEnumMap() {
            System.out.println("\n--- Enum Map ---");
            EnumMap<Status, String> statusDescriptions = new EnumMap<>(Status.class);
            statusDescriptions.put(Status.ACTIVE, "Currently active");
            statusDescriptions.put(Status.INACTIVE, "Not active");
            statusDescriptions.put(Status.PENDING, "Awaiting activation");

            for (Status s : Status.values()) {
                System.out.println("  " + s + ": " + statusDescriptions.get(s));
            }
        }
    }

    // ==========================================
    // SECTION 5: Advanced Constraint Patterns
    // ==========================================
    static class AdvancedPatterns {

        // Numeric constraint
        static class NumericBox<T extends Number> {
            private final T value;

            public NumericBox(T value) {
                this.value = value;
            }

            public double doubleValue() { return value.doubleValue(); }
            public int intValue() { return value.intValue(); }
            public long longValue() { return value.longValue(); }

            public <R extends Number> R convert(Function<T, R> converter) {
                return converter.apply(value);
            }

            @Override
            public String toString() {
                return "NumericBox[" + value + "]";
            }
        }

        // Functional interface constraint
        interface ValidFunction<T, R> extends java.util.function.Function<T, R> {
            default R applyOrElse(T input, R defaultValue) {
                try {
                    return apply(input);
                } catch (Exception e) {
                    return defaultValue;
                }
            }
        }

        // Safe processing with function constraint
        static <T, R> R safeProcess(T input, ValidFunction<T, R> func, R defaultValue) {
            return func.applyOrElse(input, defaultValue);
        }

        static void demonstrateAdvanced() {
            System.out.println("\n=== Advanced Constraint Patterns ===\n");

            NumericBox<Integer> intBox = new NumericBox<>(42);
            NumericBox<Double> doubleBox = new NumericBox<>(3.14);
            System.out.println("Integer box: " + intBox + " (double=" + intBox.doubleValue() + ")");
            System.out.println("Double box: " + doubleBox + " (int=" + doubleBox.intValue() + ")");

            // Functional constraint
            ValidFunction<String, Integer> safeLength = s -> s.length();
            System.out.println("\nSafe function: " + safeLength.apply("hello"));
            System.out.println("With default: " + safeLength.applyOrElse(null, -1));
        }

        // Type-safe heterogeneous container
        static class TypeSafeContainer {
            private final java.util.Map<Class<?>, Object> map = new java.util.HashMap<>();

            public <T> void set(Class<T> type, T value) {
                map.put(type, value);
            }

            public <T> T get(Class<T> type) {
                return type.cast(map.get(type));
            }

            public <T> boolean has(Class<T> type) {
                return map.containsKey(type);
            }
        }

        static void demonstrateTypeSafeContainer() {
            System.out.println("\n--- Type-Safe Container ---");
            TypeSafeContainer container = new TypeSafeContainer();
            container.set(String.class, "Hello");
            container.set(Integer.class, 42);
            container.set(Double.class, 3.14);

            System.out.println("String: " + container.get(String.class));
            System.out.println("Integer: " + container.get(Integer.class));
            System.out.println("Has Double? " + container.has(Double.class));
        }

        // Recursive type bound
        static <T extends Comparable<T>> T findMedian(List<T> sorted) {
            if (sorted.isEmpty()) throw new IllegalArgumentException("Empty list");
            return sorted.get(sorted.size() / 2);
        }

        static void demonstrateRecursive() {
            System.out.println("\n--- Recursive Type Bound ---");
            List<Integer> sorted = Arrays.asList(1, 2, 3, 4, 5);
            System.out.println("Median of " + sorted + ": " + findMedian(sorted));

            List<String> words = Arrays.asList("apple", "banana", "cherry");
            System.out.println("Median of " + words + ": " + findMedian(words));
        }
    }

    // ==========================================
    // SECTION 6: Constraints Best Practices
    // ==========================================
    static class BestPractices {

        static void demonstrate() {
            System.out.println("\n=== Constraints Best Practices ===\n");

            System.out.println("1. Keep bounds minimal:");
            System.out.println("   Use only necessary constraints (T extends Comparable, not Number & Comparable & Serializable)");
            System.out.println();

            System.out.println("2. Document constraints clearly:");
            System.out.println("   /** @param <T> must implement Comparable for sorting */");
            System.out.println();

            System.out.println("3. Prefer interface bounds over class bounds:");
            System.out.println("   T extends Comparable (interface) vs T extends Integer (class)");
            System.out.println();

            System.out.println("4. Use wildcards for flexibility:");
            System.out.println("   List<? extends Number> vs List<Number>");
            System.out.println();

            System.out.println("5. Consider type safety vs flexibility trade-offs:");
            System.out.println("   More constraints = more safety, less flexibility");
            System.out.println();

            System.out.println("6. Use @SafeVarargs with generic varargs:");
            System.out.println("   @SafeVarargs static <T> List<T> asList(T... items)");
            System.out.println();

            System.out.println("7. Test with different type arguments:");
            System.out.println("   Ensure your generic code works with various types");
        }
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  GENERIC CONSTRAINTS DEMO               ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        ComparableConstraints.demonstrateComparable();
        ComparableConstraints.demonstrateCustomComparable();

        SerializableConstraints.demonstrateSerializable();

        CloneableConstraints.demonstrateCloneable();

        CustomBounds.demonstrateCustomBounds();
        CustomBounds.demonstrateEnumMap();

        AdvancedPatterns.demonstrateAdvanced();
        AdvancedPatterns.demonstrateTypeSafeContainer();
        AdvancedPatterns.demonstrateRecursive();

        BestPractices.demonstrate();

        System.out.println("\nAll generic constraints demos complete!");
    }
}
