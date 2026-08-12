package academy.javaengineering.generics.type-inference.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Type Inference Solutions - Complete implementations for all exercises.
 */
public class TypeInferenceSolutions {

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    static class Pair<A, B> {
        private final A first;
        private final B second;
        public Pair(A first, B second) { this.first = first; this.second = second; }
        public A getFirst() { return first; }
        public B getSecond() { return second; }
    }

    // Exercise 1: Diamond Operator
    public static void exercise1() {
        System.out.println("Exercise 1: Diamond Operator");

        // Explicit type arguments (Java 5+)
        Box<String> explicit = new Box<>("Explicit");
        System.out.println("Explicit: " + explicit.getValue());

        // Diamond operator (Java 7+) - compiler infers type
        Box<String> inferred = new Box<>("Inferred");
        System.out.println("Inferred: " + inferred.getValue());

        // Both are equivalent - diamond is syntactic sugar
        System.out.println("Both work the same way!");
    }

    // Exercise 2: Method Type Inference
    public static <T> Box<T> makeBox(T value) {
        return new Box<>(value);
    }

    public static <A, B> Pair<A, B> makePair(A first, B second) {
        return new Pair<>(first, second);
    }

    public static void exercise2() {
        System.out.println("\nExercise 2: Method Type Inference");

        // Types inferred from arguments
        Box<String> stringBox = makeBox("Hello");
        System.out.println("Box<String>: " + stringBox.getValue());

        Box<Integer> intBox = makeBox(42);
        System.out.println("Box<Integer>: " + intBox.getValue());

        // Types inferred from arguments
        Pair<Integer, String> pair = makePair(1, "One");
        System.out.println("Pair<Integer, String>: " + pair.getFirst() + ", " + pair.getSecond());

        // Explicit type arguments (rarely needed)
        Pair<Double, Boolean> explicitPair = TypeInferenceSolutions.<Double, Boolean>makePair(3.14, true);
        System.out.println("Explicit Pair: " + explicitPair.getFirst() + ", " + explicitPair.getSecond());
    }

    // Exercise 3: Target Type Inference
    public static void exercise3() {
        System.out.println("\nExercise 3: Target Type Inference");

        // Target type affects inference
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        System.out.println("List<String>: " + strings);

        List<Number> numbers = new ArrayList<>();
        numbers.add(42);
        numbers.add(3.14);
        System.out.println("List<Number>: " + numbers);

        // Inference from target type
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        System.out.println("List<Integer>: " + ints);
    }

    // Exercise 4: Multiple Parameter Inference
    public static <T> List<T> merge(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }

    public static void exercise4() {
        System.out.println("\nExercise 4: Multiple Parameter Inference");

        List<Integer> list1 = List.of(1, 2, 3);
        List<Integer> list2 = List.of(4, 5, 6);

        // Type inferred as Integer from both arguments
        List<Integer> merged = merge(list1, list2);
        System.out.println("Merged: " + merged);

        List<String> s1 = List.of("A", "B");
        List<String> s2 = List.of("C", "D");
        List<String> mergedStrings = merge(s1, s2);
        System.out.println("Merged strings: " + mergedStrings);
    }

    // Exercise 5: Bounded Type Inference
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Bounded Type Inference");

        // int result = max(1, 2);  // DOES NOT WORK: primitive types
        // Must use wrapper classes:
        Integer maxInt = max(1, 2);
        System.out.println("Max integer: " + maxInt);

        String maxStr = max("apple", "banana");
        System.out.println("Max string: " + maxStr);

        // Works with any Comparable type
        Double maxDouble = max(3.14, 2.71);
        System.out.println("Max double: " + maxDouble);
    }

    public static void main(String[] args) {
        System.out.println("=== Type Inference Solutions ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
