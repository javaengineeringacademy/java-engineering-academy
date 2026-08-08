package list.linkedlist.examples;

import java.util.*;

public class LinkedListExample {

    public static void main(String[] args) {
        example1_BasicOperations();
        example2_QueueAndDequeMethods();
        example3_FirstLastAccess();
        example4_LinkedListPerformance();
        example5_LinkedListAsDeque();
    }

    static void example1_BasicOperations() {
        System.out.println("=== Example 1: Basic LinkedList Operations ===");
        LinkedList<String> list = new LinkedList<>();
        list.add("Java");
        list.add("Python");
        list.add("C++");
        System.out.println("LinkedList: " + list);
        list.addFirst("JavaScript");
        list.addLast("Go");
        System.out.println("After addFirst/addLast: " + list);
    }

    static void example2_QueueAndDequeMethods() {
        System.out.println("\n=== Example 2: Queue and Deque Methods ===");
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(10);
        queue.offer(20);
        queue.offer(30);
        System.out.println("Queue: " + queue);
        System.out.println("Peek: " + queue.peek());
        System.out.println("Poll: " + queue.poll());
        System.out.println("After poll: " + queue);
    }

    static void example3_FirstLastAccess() {
        System.out.println("\n=== Example 3: First and Last Access ===");
        LinkedList<String> list = new LinkedList<>(Arrays.asList("A", "B", "C", "D"));
        System.out.println("First: " + list.getFirst());
        System.out.println("Last: " + list.getLast());
        list.removeFirst();
        list.removeLast();
        System.out.println("After removeFirst/removeLast: " + list);
    }

    static void example4_LinkedListPerformance() {
        System.out.println("\n=== Example 4: Performance Characteristics ===");
        LinkedList<Integer> list = new LinkedList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            list.addFirst(i);
        }
        long end = System.nanoTime();
        System.out.println("Add 10000 elements at beginning: " + (end - start) + " ns");
        System.out.println("Size: " + list.size());
    }

    static void example5_LinkedListAsDeque() {
        System.out.println("\n=== Example 5: LinkedList as Deque ===");
        Deque<String> stack = new LinkedList<>();
        stack.push("Bottom");
        stack.push("Middle");
        stack.push("Top");
        System.out.println("Stack: " + stack);
        System.out.println("Pop: " + stack.pop());
        System.out.println("After pop: " + stack);
        Deque<String> queue = new LinkedList<>();
        queue.addLast("First");
        queue.addLast("Second");
        queue.addLast("Third");
        System.out.println("Deque as queue: " + queue);
    }
}
