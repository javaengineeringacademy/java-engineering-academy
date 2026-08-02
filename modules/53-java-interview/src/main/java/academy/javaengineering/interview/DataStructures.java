package academy.javaengineering.interview;

import java.util.*;

/**
 * Demonstrates data structure implementations for interviews.
 */
public class DataStructures {

    static class Stack<T> {
        private final List<T> elements = new ArrayList<>();

        public void push(T item) {
            elements.add(item);
        }

        public T pop() {
            if (elements.isEmpty()) throw new EmptyStackException();
            return elements.remove(elements.size() - 1);
        }

        public T peek() {
            if (elements.isEmpty()) throw new EmptyStackException();
            return elements.get(elements.size() - 1);
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }

        public int size() {
            return elements.size();
        }
    }

    static class Queue<T> {
        private final LinkedList<T> elements = new LinkedList<>();

        public void enqueue(T item) {
            elements.addLast(item);
        }

        public T dequeue() {
            if (elements.isEmpty()) throw new NoSuchElementException();
            return elements.removeFirst();
        }

        public T peek() {
            if (elements.isEmpty()) throw new NoSuchElementException();
            return elements.getFirst();
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }

        public int size() {
            return elements.size();
        }
    }

    static class ListNode<T> {
        T data;
        ListNode<T> next;

        ListNode(T data) {
            this.data = data;
        }
    }
}
