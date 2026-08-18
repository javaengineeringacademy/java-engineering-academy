package academy.javaengineering.concurrency.collections.solutions;

import java.util.concurrent.*;

public class ConcurrentCollectionsSolutions {
    public static void main(String[] args) throws Exception {
        // Solution 1: Thread-safe counter with merge
        ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counters.computeIfAbsent("total", k -> new AtomicInteger(0)).incrementAndGet();
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Counter: " + counters.get("total").get());

        // Solution 2: Producer-consumer with BlockingQueue
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("  Consumed: " + queue.take());
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
