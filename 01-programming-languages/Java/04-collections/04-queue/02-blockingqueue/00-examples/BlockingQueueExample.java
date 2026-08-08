package academy.javaengineering.collections.queue.blockingqueue.examples;

import java.util.concurrent.*;

public class BlockingQueueExample {
    public static void main(String[] args) {
        System.out.println("=== BlockingQueue Examples ===\n");

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        try {
            queue.put("A");
            queue.put("B");
            queue.put("C");
            System.out.println("Queue: " + queue);
            System.out.println("Take: " + queue.take());
            System.out.println("Poll: " + queue.poll());
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
