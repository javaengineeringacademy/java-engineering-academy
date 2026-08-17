package academy.javaengineering.collections.queue.priorityqueue.examples;

import java.util.*;

public class PriorityQueueExample {
    public static void main(String[] args) {
        System.out.println("=== PriorityQueue Examples ===\n");

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(3);
        pq.add(2);
        pq.add(4);
        System.out.println("Head (min): " + pq.peek());
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");
        System.out.println("\n");

        PriorityQueue<String> pqLen = new PriorityQueue<>(Comparator.comparingInt(String::length));
        pqLen.add("Java");
        pqLen.add("Hi");
        pqLen.add("Python");
        while (!pqLen.isEmpty()) System.out.print(pqLen.poll() + " ");
        System.out.println();
    }
}
