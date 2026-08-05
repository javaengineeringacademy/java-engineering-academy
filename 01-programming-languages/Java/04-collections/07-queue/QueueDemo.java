import java.util.*;

/**
 * Demonstrates Queue interface operations for FIFO processing.
 * Covers ArrayDeque, LinkedList, and PriorityQueue for queue operations.
 */
public class QueueDemo {

    public static void main(String[] args) throws InterruptedException {
        demonstrateBasicOperations();
        demonstratePriorityQueue();
        demonstrateBlockingQueue();
    }

    /**
     * Demonstrates basic Queue operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== Basic Queue Operations ===");

        // Create queue (ArrayDeque is preferred)
        Queue<String> queue = new ArrayDeque<>();

        // Add elements
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");

        System.out.println("Queue: " + queue);
        System.out.println("Size: " + queue.size());

        // Peek at head
        System.out.println("Peek: " + queue.peek());

        // Process queue
        System.out.println("\nProcessing:");
        while (!queue.isEmpty()) {
            System.out.println("  Serving: " + queue.poll());
        }

        System.out.println("Queue empty: " + queue.isEmpty());
        System.out.println();
    }

    /**
     * Demonstrates PriorityQueue for priority-based processing.
     */
    private static void demonstratePriorityQueue() {
        System.out.println("=== PriorityQueue ===");

        // Min-heap
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(2);
        minHeap.offer(8);
        minHeap.offer(1);

        System.out.println("Min-heap poll order:");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();

        // Max-heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(2);
        maxHeap.offer(8);
        maxHeap.offer(1);

        System.out.println("Max-heap poll order:");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates BlockingQueue for producer-consumer pattern.
     */
    private static void demonstrateBlockingQueue() throws InterruptedException {
        System.out.println("=== BlockingQueue (Producer-Consumer) ===");

        BlockingQueue<String> buffer = new ArrayBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    buffer.put("Item" + i);
                    System.out.println("Produced: Item" + i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String item = buffer.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
