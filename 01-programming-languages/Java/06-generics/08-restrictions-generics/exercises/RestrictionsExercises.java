package academy.javaengineering.generics.restrictions-generics.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Restrictions on Generics Exercises
 * Understand what you cannot do with Java generics.
 */
public class RestrictionsExercises {

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    // Exercise 1: Cannot use primitive types as type arguments
    // TODO: Show what works and what doesn't
    public static void exercise1() {
        System.out.println("Exercise 1: Primitive Type Restrictions");
        // TODO: These cause compile errors:
        // Box<int> intBox = new Box<>(42);
        // Box<double> doubleBox = new Box<>(3.14);

        // TODO: Use wrapper classes instead:
        // Box<Integer> integerBox = new Box<>(42);
        // Box<Double> doubleBox = new Box<>(3.14);

    }

    // Exercise 2: Cannot create generic instances
    // TODO: Show why you cannot use new T()
    public static <T> T createInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        // TODO: You cannot do: return new T();
        // Instead, use Class object
        return clazz.newInstance();
    }

    public static void exercise2() {
        System.out.println("\nExercise 2: Cannot Create Instances");
        // TODO: Explain why new T() doesn't work
        // Because T is erased at runtime, JVM doesn't know which constructor to call

    }

    // Exercise 3: Cannot create generic arrays
    // TODO: Show array creation restrictions
    public static void exercise3() {
        System.out.println("\nExercise 3: Cannot Create Generic Arrays");
        // TODO: These cause compile errors:
        // Box<String>[] boxes = new Box<String>[10];
        // T[] array = new T[10];

        // TODO: Workarounds:
        @SuppressWarnings("unchecked")
        Box<String>[] boxes = (Box<String>[]) new Box[10];
        System.out.println("Used raw type and cast (unsafe)");

        List<Box<String>> list = new ArrayList<>();
        System.out.println("Use List instead (safe)");
    }

    // Exercise 4: Cannot use instanceof with parameterized types
    // TODO: Show instanceof restrictions
    public static void exercise4() {
        System.out.println("\nExercise 4: Cannot Use instanceof");
        Box<String> box = new Box<>("Hello");

        // TODO: This causes a compile error:
        // if (box instanceof Box<String>) { }

        // TODO: What you can do:
        if (box instanceof Box) {
            System.out.println("box is a Box (raw type check)");
        }

    }

    // Exercise 5: Cannot use static context with type parameters
    // TODO: Show static member restrictions
    static class GenericContainer<T> {
        // TODO: This causes a compile error:
        // private static T value;

        // TODO: What you can do:
        private static Object staticValue;
        private T instanceValue;
    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Cannot Use Static Context");
        // TODO: Explain why static members cannot use type parameters
        // Because type parameters are per-instance, but static members are per-class

    }

    public static void main(String[] args) {
        System.out.println("=== Restrictions on Generics Exercises ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
