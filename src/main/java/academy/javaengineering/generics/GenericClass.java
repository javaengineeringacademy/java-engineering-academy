package academy.javaengineering.generics;

import java.util.Objects;

/**
 * Topic 02: Generic Classes.
 *
 * <p>This class demonstrates generic class design with
 * multiple type parameters, bounds, and real-world patterns.</p>
 *
 * @param <T> the type of the contained value
 */
public class GenericClass<T> {

    private T content;

    public GenericClass() {
    }

    public GenericClass(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GenericClass<?> other)) return false;
        return Objects.equals(content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "GenericClass[" + content + "]";
    }

    /**
     * Generic Pair class with multiple type parameters.
     */
    public static class Pair<A, B> {
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
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Pair<?, ?> other)) return false;
            return Objects.equals(first, other.first) &&
                   Objects.equals(second, other.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }

        public <C> Pair<C, B> withFirst(C newFirst) {
            return new Pair<>(newFirst, second);
        }

        public <C> Pair<A, C> withSecond(C newSecond) {
            return new Pair<>(first, newSecond);
        }
    }

    /**
     * Binary tree with bounded type parameter.
     */
    public static class BinaryTree<T extends Comparable<T>> {

        private static class Node<T> {
            T data;
            Node<T> left;
            Node<T> right;

            Node(T data) {
                this.data = data;
            }
        }

        private Node<T> root;
        private int size;

        public void insert(T value) {
            Objects.requireNonNull(value);
            root = insertRecursive(root, value);
            size++;
        }

        private Node<T> insertRecursive(Node<T> current, T value) {
            if (current == null) {
                return new Node<>(value);
            }
            int compare = value.compareTo(current.data);
            if (compare < 0) {
                current.left = insertRecursive(current.left, value);
            } else if (compare > 0) {
                current.right = insertRecursive(current.right, value);
            }
            return current;
        }

        public boolean contains(T value) {
            return containsRecursive(root, value);
        }

        private boolean containsRecursive(Node<T> current, T value) {
            if (current == null) {
                return false;
            }
            int compare = value.compareTo(current.data);
            if (compare == 0) {
                return true;
            } else if (compare < 0) {
                return containsRecursive(current.left, value);
            } else {
                return containsRecursive(current.right, value);
            }
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }

    /**
     * Demonstrates generic class usage.
     */
    public static void main(String[] args) {
        // Basic generic class
        GenericClass<String> stringBox = new GenericClass<>("Hello");
        System.out.println(stringBox);

        // Pair with multiple type parameters
        Pair<String, Integer> nameAge = new Pair<>("Alice", 30);
        Pair<Integer, Boolean> idActive = new Pair<>(1001, true);
        System.out.println(nameAge);
        System.out.println(idActive);

        // Modified pairs
        Pair<String, Integer> updatedName = nameAge.withFirst("Bob");
        System.out.println(updatedName);

        // Binary tree with bounded type
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);

        System.out.println("Tree size: " + tree.size());
        System.out.println("Contains 30: " + tree.contains(30));
        System.out.println("Contains 25: " + tree.contains(25));
    }
}
