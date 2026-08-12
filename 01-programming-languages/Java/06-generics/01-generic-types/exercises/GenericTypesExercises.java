package academy.javaengineering.generics.generic-types.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Types Exercises
 * Practice creating and using generic classes and interfaces.
 */
public class GenericTypesExercises {

    // Exercise 1: Create a generic Pair class that holds two values of potentially different types
    // TODO: Implement the Pair class with getFirst(), getSecond(), and toString() methods
    static class Pair<A, B> {
        // TODO: Add fields, constructor, and methods

    }

    // Exercise 2: Create a generic Box class that can hold any type
    // TODO: Implement with getValue(), setValue(), and isPresent() methods
    static class Box<T> {
        // TODO: Add fields, constructor, and methods

    }

    // Exercise 3: Create a generic Stack class with push, pop, peek, and isEmpty methods
    // TODO: Implement using an internal ArrayList
    static class Stack<E> {
        // TODO: Add fields and methods

    }

    // Exercise 4: Create a generic Pair utility method that creates a Pair from two values
    // TODO: Implement the method
    public static <A, B> Pair<A, B> makePair(A first, B second) {
        // TODO: Implement
        return null;
    }

    // Exercise 5: Create a generic Container interface and implement it for different types
    // TODO: Define the interface and implement a StringContainer class
    interface Container<T> {
        // TODO: Define methods
    }

    static class StringContainer implements Container<String> {
        // TODO: Implement the interface

    }

    public static void main(String[] args) {
        System.out.println("=== Generic Types Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Pair");
        Pair<String, Integer> pair = new Pair<>("Hello", 42);
        System.out.println("Pair: " + pair);
        // Expected: Pair[Hello, 42]

        // Test Exercise 2
        System.out.println("\nExercise 2: Box");
        Box<String> box = new Box<>("Generics");
        System.out.println("Box value: " + box.getValue());
        System.out.println("Box present: " + box.isPresent());
        Box<Object> emptyBox = new Box<>(null);
        System.out.println("Empty box present: " + emptyBox.isPresent());

        // Test Exercise 3
        System.out.println("\nExercise 3: Stack");
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Pop: " + stack.pop());
        System.out.println("Peek: " + stack.peek());
        System.out.println("Empty: " + stack.isEmpty());

        // Test Exercise 4
        System.out.println("\nExercise 4: makePair");
        Pair<Double, Boolean> pair2 = makePair(3.14, true);
        System.out.println("Pair: " + pair2);

        // Test Exercise 5
        System.out.println("\nExercise 5: Container");
        Container<String> container = new StringContainer();
        // container.set("Test");
        // System.out.println("Container value: " + container.get());
    }
}
