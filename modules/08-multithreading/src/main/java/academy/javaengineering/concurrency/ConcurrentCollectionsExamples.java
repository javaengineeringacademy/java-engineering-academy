package academy.javaengineering.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Map;
import java.util.Set;

/**
 * Demonstrates thread-safe concurrent collections.
 * Shows ConcurrentHashMap, CopyOnWriteArrayList, and blocking queues.
 */
public class ConcurrentCollectionsExamples {

    public static void main(String[] args) throws InterruptedException {
        demonstrateConcurrentHashMap();
        demonstrateCopyOnWriteArrayList();
        demonstrateBlockingQueue();
    }

    /**
     * Demonstrates ConcurrentHashMap for thread-safe map operations.
     */
    public static void demonstrateConcurrentHashMap() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    map.compute("key-" + threadNum, (k, v) -> v == null ? 1 : v + 1);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("ConcurrentHashMap size: " + map.size());
        System.out.println("Sample value: " + map.get("key-0"));
        // Expected output: size: 10, sample value: 100
    }

    /**
     * Demonstrates CopyOnWriteArrayList for thread-safe list operations.
     */
    public static void demonstrateCopyOnWriteArrayList() throws InterruptedException {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        Thread[] writers = new Thread[5];
        Thread[] readers = new Thread[5];

        // Writer threads
        for (int i = 0; i < 5; i++) {
            final int writerId = i;
            writers[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    list.add("Writer-" + writerId + "-Item-" + j);
                }
            });
        }

        // Reader threads
        for (int i = 0; i < 5; i++) {
            final int readerId = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println("Reader " + readerId + " sees " + list.size() + " items");
                }
            });
        }

        for (Thread writer : writers) {
            writer.start();
        }
        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread writer : writers) {
            writer.join();
        }
        for (Thread reader : readers) {
            reader.join();
        }

        System.out.println("Final list size: " + list.size());
        // Expected output: Final list size: 50
    }

    /**
     * Demonstrates BlockingQueue for producer-consumer pattern.
     */
    public static void demonstrateBlockingQueue() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(5);
        final int ITEMS_TO_PRODUCE = 10;

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < ITEMS_TO_PRODUCE; i++) {
                    String item = "Item-" + i;
                    queue.put(item);
                    System.out.println("Produced: " + item);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < ITEMS_TO_PRODUCE; i++) {
                    String item = queue.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("BlockingQueue operations completed");
        // Expected output: Items produced and consumed in order
    }
}
