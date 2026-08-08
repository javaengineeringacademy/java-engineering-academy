package academy.javaengineering.collections.queue.examples;

import java.util.*;

public class QueueExamples {
    public static void main(String[] args) {
        System.out.println("=== Queue Examples ===\n");

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        System.out.println("Queue: " + queue);
        System.out.println("Poll: " + queue.poll());
        System.out.println("Peek: " + queue.peek());

        System.out.println("\n--- PriorityQueue ---");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.addAll(Arrays.asList(5, 1, 3));
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");
        System.out.println();
    }
}
