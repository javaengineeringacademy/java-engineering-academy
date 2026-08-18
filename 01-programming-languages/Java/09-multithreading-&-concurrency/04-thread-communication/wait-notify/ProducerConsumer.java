package academy.javaengineering.concurrency.communication.waitnotify;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Producer-Consumer pattern with bounded buffer using wait/notify.
 * Multiple producers and consumers share a fixed-size buffer.
 */
public class ProducerConsumer {

    private static final int BUFFER_SIZE = 5;
    private static final int NUM_PRODUCERS = 2;
    private static final int NUM_CONSUMERS = 2;
    private static final int ITEMS_PER_PRODUCER = 5;

    static class BoundedBuffer<T> {
        private final Queue<T> queue = new LinkedList<>();
        private final int capacity;

        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void put(T item) throws InterruptedException {
            while (queue.size() == capacity) {
                System.out.println("[Buffer] Full, producer waiting...");
                wait();
            }
            queue.add(item);
            System.out.println("[Buffer] Added: " + item + " (size=" + queue.size() + ")");
            notifyAll();
        }

        public synchronized T take() throws InterruptedException {
            while (queue.isEmpty()) {
                System.out.println("[Buffer] Empty, consumer waiting...");
                wait();
            }
            T item = queue.poll();
            System.out.println("[Buffer] Removed: " + item + " (size=" + queue.size() + ")");
            notifyAll();
            return item;
        }

        public synchronized int size() {
            return queue.size();
        }
    }

    public static void main(String[] args) {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(BUFFER_SIZE);
        Random random = new Random();
        Thread[] producers = new Thread[NUM_PRODUCERS];
        Thread[] consumers = new Thread[NUM_CONSUMERS];

        // Create producers
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            final int producerId = i;
            producers[i] = new Thread(() -> {
                for (int j = 0; j < ITEMS_PER_PRODUCER; j++) {
                    try {
                        int item = producerId * 100 + j;
                        System.out.println("[Producer-" + producerId + "] Producing: " + item);
                        buffer.put(item);
                        Thread.sleep(random.nextInt(200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("[Producer-" + producerId + "] Finished");
            }, "Producer-" + i);
        }

        // Create consumers
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            final int consumerId = i;
            consumers[i] = new Thread(() -> {
                for (int j = 0; j < (NUM_PRODUCERS * ITEMS_PER_PRODUCER / NUM_CONSUMERS); j++) {
                    try {
                        T item = buffer.take();
                        System.out.println("[Consumer-" + consumerId + "] Consumed: " + item);
                        Thread.sleep(random.nextInt(300));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("[Consumer-" + consumerId + "] Finished");
            }, "Consumer-" + i);
        }

        // Start all threads
        System.out.println("=== Producer-Consumer with Bounded Buffer ===");
        System.out.println("Buffer size: " + BUFFER_SIZE);
        System.out.println("Producers: " + NUM_PRODUCERS + ", Consumers: " + NUM_CONSUMERS);
        System.out.println("Items per producer: " + ITEMS_PER_PRODUCER);
        System.out.println();

        for (Thread t : producers) t.start();
        for (Thread t : consumers) t.start();

        // Wait for completion
        try {
            for (Thread t : producers) t.join();
            for (Thread t : consumers) t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== All producers and consumers finished ===");
    }
}
