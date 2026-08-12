package academy.javaengineering.generics.type-inference.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Type Inference Exercises
 * Practice understanding how Java infers generic types.
 */
public class TypeInferenceExercises {

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

    // Exercise 1: Diamond operator inference
    // TODO: Use diamond operator to infer type arguments
    public static void exercise1() {
        System.out.println("Exercise 1: Diamond Operator");
        // Without diamond operator:
        Box<String> explicit = new Box<>("Explicit");

        // TODO: Use diamond operator
        // Box<String> inferred = new Box<>("Inferred");

        // TODO: Show that both work the same way

    }

    // Exercise 2: Type inference in method calls
    // TODO: Show how types are inferred in method calls
    public static <T> Box<T> makeBox(T value) {
        return new Box<>(value);
    }

    public static <A, B> Pair<A, B> makePair(A first, B second) {
        return new Pair<>(first, second);
    }

    public static void exercise2() {
        System.out.println("\nExercise 2: Method Type Inference");
        // TODO: Call methods without explicit type arguments
        // Box<String> box = makeBox("Hello");
        // Pair<Integer, String> pair = makePair(1, "One");

    }

    // Exercise 3: Target type inference
    // TODO: Show how target type affects inference
    public static void exercise3() {
        System.out.println("\nExercise 3: Target Type Inference");
        // TODO: Assign to different target types
        // List<String> strings = new ArrayList<>();
        // List<Number> numbers = new ArrayList<>();

    }

    // Exercise 4: Generic method with multiple parameters
    // TODO: Create a method where inference uses multiple arguments
    public static <T> List<T> merge(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }

    public static void exercise4() {
        System.out.println("\nExercise 4: Multiple Parameter Inference");
        List<Integer> list1 = List.of(1, 2, 3);
        List<Integer> list2 = List.of(4, 5, 6);
        // TODO: Call merge and show type inference

    }

    // Exercise 5: Inference with bounded types
    // TODO: Show how bounds affect inference
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static void exercise5() {
        System.out.println("\nExercise 5: Bounded Type Inference");
        // TODO: Call max with different types
        // int result = max(1, 2);  // Does this work?

    }

    public static void main(String[] args) {
        System.out.println("=== Type Inference Exercises ===\n");
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
