package academy.javaengineering.collections.queue.deque.examples;

import java.util.*;

public class DequeExample {
    public static void main(String[] args) {
        System.out.println("=== ArrayDeque Examples ===\n");

        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addFirst("First");
        deque.addLast("Last");
        deque.add("Middle");
        System.out.println("Deque: " + deque);
        System.out.println("peekFirst: " + deque.peekFirst());
        System.out.println("peekLast: " + deque.peekLast());

        System.out.println("\nAs Stack:");
        deque.push("Pushed");
        System.out.println("pop: " + deque.pop());

        System.out.println("\nAs Queue:");
        deque.offer("Offered");
        System.out.println("poll: " + deque.poll());
    }
}
