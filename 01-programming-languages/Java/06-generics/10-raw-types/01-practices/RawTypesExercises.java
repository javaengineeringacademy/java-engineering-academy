package academy.javaengineering.generics.raw-types.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw Types Exercises
 * Understand raw types and why they should be avoided.
 */
public class RawTypesExercises {

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
        public void setValue(T value) { this.value = value; }
    }

    // Exercise 1: Show raw type usage and warnings
    // TODO: Demonstrate raw type vs parameterized type
    public static void exercise1() {
        System.out.println("Exercise 1: Raw Types vs Parameterized Types");

        // Raw type (generates warning)
        @SuppressWarnings("rawtypes")
        Box rawBox = new Box("Hello");
        Object rawValue = rawBox.getValue();  // Returns Object
        System.out.println("Raw box value: " + rawValue);

        // Parameterized type (no warning)
        Box<String> typedBox = new Box<>("World");
        String typedValue = typedBox.getValue();  // Returns String
        System.out.println("Typed box value: " + typedValue);

        // TODO: Show assignment compatibility issues

    }

    // Exercise 2: Raw type bypasses type checking
    // TODO: Show how raw types allow unsafe operations
    public static void exercise2() {
        System.out.println("\nExercise 2: Unsafe Raw Type Operations");

        @SuppressWarnings("rawtypes")
        List rawList = new ArrayList();
        rawList.add("String");
        rawList.add(42);  // No compile error with raw type!
        rawList.add(3.14);

        System.out.println("Raw list contains mixed types:");
        for (Object item : rawList) {
            System.out.println("  " + item.getClass().getSimpleName() + ": " + item);
        }

        // TODO: Show what happens when you try to cast

    }

    // Exercise 3: Raw type in method parameters
    // TODO: Show issues with raw type parameters
    public static void processRaw(@SuppressWarnings("rawtypes") List list) {
        // Cannot safely cast elements
        // String first = (String) list.get(0);  // May throw ClassCastException
    }

    public static void processTyped(List<String> list) {
        // Safe to use
        String first = list.get(0);
        System.out.println("First element: " + first);
    }

    public static void exercise3() {
        System.out.println("\nExercise 3: Raw Type in Methods");
        // TODO: Show compilation warnings

    }

    // Exercise 4: Raw type vs wildcard
    // TODO: Compare raw type and unbounded wildcard
    public static void exercise4() {
        System.out.println("\nExercise 4: Raw Type vs Wildcard");

        // Raw type
        @SuppressWarnings("rawtypes")
        Box rawBox = new Box("Hello");

        // Unbounded wildcard
        Box<?> wildcardBox = new Box<>("World");

        // Both can hold any Box, but wildcard is type-safe
        // rawBox.setValue(42);  // No error, but unsafe
        // wildcardBox.setValue("New");  // Compile error, safe

        System.out.println("Raw type: bypasses type checking");
        System.out.println("Wildcard: maintains type safety");
    }

    // Exercise 5: When raw types are necessary
    // TODO: Show legitimate uses of raw types
    public static void exercise5() {
        System.out.println("\nExercise 5: Necessary Raw Types");

        // Legacy code compatibility
        @SuppressWarnings("rawtypes")
        Class rawClass = String.class;
        System.out.println("Raw Class: " + rawClass);

        // Reflection with unknown types
        try {
            @SuppressWarnings("rawtypes")
            Box rawBox = (Box) rawClass.newInstance();
            System.out.println("Created raw instance: " + rawBox.getClass());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Raw Types Exercises ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
