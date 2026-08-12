package academy.javaengineering.generics.raw-types.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw Types Solutions - Complete implementations for all exercises.
 */
public class RawTypesSolutions {

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
        public void setValue(T value) { this.value = value; }
    }

    // Exercise 1: Raw Types vs Parameterized Types
    public static void exercise1() {
        System.out.println("Exercise 1: Raw Types vs Parameterized Types");

        // Raw type (generates unchecked warning)
        @SuppressWarnings("rawtypes")
        Box rawBox = new Box("Hello");
        Object rawValue = rawBox.getValue();
        System.out.println("Raw box value: " + rawValue + " (type: " + rawValue.getClass() + ")");

        // Parameterized type (no warning)
        Box<String> typedBox = new Box<>("World");
        String typedValue = typedBox.getValue();
        System.out.println("Typed box value: " + typedValue + " (type: " + typedValue.getClass() + ")");

        // Assignment compatibility
        Box<String> stringBox = new Box<>("String");
        Box rawAssign = stringBox;  // OK: raw type can hold any parameterization
        // Box<String> typedAssign = rawBox;  // COMPILE ERROR: cannot assign raw to parameterized

        System.out.println("Raw type: Object getValue()");
        System.out.println("Parameterized: String getValue()");
    }

    // Exercise 2: Unsafe Raw Type Operations
    public static void exercise2() {
        System.out.println("\nExercise 2: Unsafe Raw Type Operations");

        @SuppressWarnings("rawtypes")
        List rawList = new ArrayList();
        rawList.add("String");
        rawList.add(42);
        rawList.add(3.14);

        System.out.println("Raw list contains mixed types:");
        for (Object item : rawList) {
            System.out.println("  " + item.getClass().getSimpleName() + ": " + item);
        }

        // Unsafe casting
        try {
            String first = (String) rawList.get(0);
            System.out.println("Safe cast: " + first);

            String second = (String) rawList.get(1);  // ClassCastException!
            System.out.println("This won't print: " + second);
        } catch (ClassCastException e) {
            System.out.println("ClassCastException: " + e.getMessage());
        }
    }

    // Exercise 3: Raw Type in Methods
    public static void processRaw(@SuppressWarnings("rawtypes") List list) {
        // Raw type: no compile-time type checking
        for (Object item : list) {
            if (item instanceof String) {
                System.out.println("  String: " + item);
            } else if (item instanceof Integer) {
                System.out.println("  Integer: " + item);
            }
        }
    }

    public static void processTyped(List<String> list) {
        // Parameterized type: compile-time type checking
        for (String item : list) {
            System.out.println("  String: " + item);
        }
    }

    public static void exercise3() {
        System.out.println("\nExercise 3: Raw Type in Methods");

        List<String> typedList = List.of("A", "B", "C");
        System.out.println("Typed method:");
        processTyped(typedList);

        @SuppressWarnings("rawtypes")
        List rawList = new ArrayList();
        rawList.add("X");
        rawList.add(42);
        System.out.println("Raw method:");
        processRaw(rawList);
    }

    // Exercise 4: Raw Type vs Wildcard
    public static void exercise4() {
        System.out.println("\nExercise 4: Raw Type vs Wildcard");

        // Raw type
        @SuppressWarnings("rawtypes")
        Box rawBox = new Box("Hello");

        // Unbounded wildcard
        Box<?> wildcardBox = new Box<>("World");

        // Raw type: can modify (unsafe)
        rawBox.setValue(42);
        System.out.println("Raw box now holds: " + rawBox.getValue().getClass());

        // Wildcard: cannot modify (safe)
        // wildcardBox.setValue("New");  // COMPILE ERROR
        System.out.println("Wildcard box: read-only access");

        System.out.println("Raw type: bypasses type checking");
        System.out.println("Wildcard: maintains type safety");
    }

    // Exercise 5: Necessary Raw Types
    public static void exercise5() {
        System.out.println("\nExercise 5: Necessary Raw Types");

        // Legacy code compatibility
        @SuppressWarnings("rawtypes")
        Class rawClass = String.class;
        System.out.println("Raw Class: " + rawClass);

        // Parameterized Class
        Class<String> typedClass = String.class;
        System.out.println("Typed Class: " + typedClass);

        // Reflection with unknown types
        try {
            @SuppressWarnings("rawtypes")
            Box rawBox = (Box) Box.class.newInstance();
            System.out.println("Created raw instance: " + rawBox.getClass());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Raw types necessary for: legacy code, reflection");
    }

    public static void main(String[] args) {
        System.out.println("=== Raw Types Solutions ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
