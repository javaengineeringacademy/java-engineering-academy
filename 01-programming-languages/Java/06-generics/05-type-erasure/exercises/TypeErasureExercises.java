package academy.javaengineering.generics.type-erasure.exercises;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Type Erasure Exercises
 * Understand how type erasure works in Java generics.
 */
public class TypeErasureExercises {

    static class Box<T> {
        private T value;

        public Box(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    static class StringBox extends Box<String> {
        public StringBox(String value) {
            super(value);
        }
    }

    // Exercise 1: Demonstrate that generic type info is erased at runtime
    // TODO: Show that Box<String> and Box<Integer> are the same class
    public static void exercise1() {
        System.out.println("Exercise 1: Type Erasure at Runtime");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);

        // TODO: Print the class of both boxes and compare
        // Expected: Both should print the same class

    }

    // Exercise 2: Show that you cannot use instanceof with generic types
    // TODO: Demonstrate the compile-time error (commented out)
    public static void exercise2() {
        System.out.println("\nExercise 2: instanceof with Generics");
        Box<String> box = new Box<>("Test");

        // TODO: The following line causes a compile error:
        // if (box instanceof Box<String>) { }

        // TODO: Show what you can do instead
        // Hint: Use raw type or class check

    }

    // Exercise 3: Demonstrate bridge methods
    // TODO: Show bridge method in StringBox using reflection
    public static void exercise3() {
        System.out.println("\nExercise 3: Bridge Methods");
        StringBox stringBox = new StringBox("Hello");

        // TODO: Use reflection to show all methods in StringBox
        // Include declared methods and inherited methods
        // Look for bridge methods

    }

    // Exercise 4: Show array creation with generics
    // TODO: Demonstrate why you cannot create generic arrays
    public static void exercise4() {
        System.out.println("\nExercise 4: Generic Arrays");

        // TODO: The following line causes a compile error:
        // Box<String>[] boxes = new Box<String>[10];

        // TODO: Show what happens with raw types (unsafe)
        // Box[] rawBoxes = new Box[10];
        // rawBoxes[0] = new Box<Integer>(42);
        // rawBoxes[1] = new Box<String>("Hello");

        // TODO: Show the correct approach using List

    }

    // Exercise 5: Demonstrate type erasure with bounded types
    // TODO: Show that bounded type parameters are replaced by their bounds
    static <T extends Comparable<T>> void sort(List<T> list) {
        // TODO: Show that T is replaced by Comparable at runtime

    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Bounded Type Erasure");
        // TODO: Use reflection to show the actual parameter type of sort method

    }

    public static void main(String[] args) {
        System.out.println("=== Type Erasure Exercises ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
