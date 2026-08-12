package academy.javaengineering.generics.generic-types.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Types Solutions - Complete implementations for all exercises.
 */
public class GenericTypesSolutions {

    // Exercise 1: Generic Pair class
    static class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "Pair[" + first + ", " + second + "]";
        }
    }

    // Exercise 2: Generic Box class
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

        public boolean isPresent() {
            return value != null;
        }

        @Override
        public String toString() {
            return isPresent() ? "Box[" + value + "]" : "Box[empty]";
        }
    }

    // Exercise 3: Generic Stack class
    static class Stack<E> {
        private final List<E> elements = new ArrayList<>();

        public void push(E element) {
            elements.add(element);
        }

        public E pop() {
            if (elements.isEmpty()) {
                throw new RuntimeException("Stack is empty");
            }
            return elements.remove(elements.size() - 1);
        }

        public E peek() {
            if (elements.isEmpty()) {
                throw new RuntimeException("Stack is empty");
            }
            return elements.get(elements.size() - 1);
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }

        public int size() {
            return elements.size();
        }
    }

    // Exercise 4: makePair utility method
    public static <A, B> Pair<A, B> makePair(A first, B second) {
        return new Pair<>(first, second);
    }

    // Exercise 5: Container interface and StringContainer implementation
    interface Container<T> {
        void set(T value);
        T get();
    }

    static class StringContainer implements Container<String> {
        private String value;

        @Override
        public void set(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Generic Types Solutions ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: Pair");
        Pair<String, Integer> pair = new Pair<>("Hello", 42);
        System.out.println("Pair: " + pair);
        System.out.println("First: " + pair.getFirst());
        System.out.println("Second: " + pair.getSecond());

        // Test Exercise 2
        System.out.println("\nExercise 2: Box");
        Box<String> box = new Box<>("Generics");
        System.out.println("Box value: " + box.getValue());
        System.out.println("Box present: " + box.isPresent());
        Box<Object> emptyBox = new Box<>(null);
        System.out.println("Empty box: " + emptyBox);
        System.out.println("Empty box present: " + emptyBox.isPresent());
        emptyBox.setValue("Now present");
        System.out.println("After setValue: " + emptyBox);

        // Test Exercise 3
        System.out.println("\nExercise 3: Stack");
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Size: " + stack.size());
        System.out.println("Peek: " + stack.peek());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Empty: " + stack.isEmpty());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Empty: " + stack.isEmpty());

        // Test Exercise 4
        System.out.println("\nExercise 4: makePair");
        Pair<Double, Boolean> pair2 = makePair(3.14, true);
        System.out.println("Pair: " + pair2);

        // Test Exercise 5
        System.out.println("\nExercise 5: Container");
        Container<String> container = new StringContainer();
        container.set("Test");
        System.out.println("Container value: " + container.get());
    }
}
