package academy.javaengineering.concurrency.examples;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConcurrentCollectionsExamples {

    public static void main(String[] args) throws InterruptedException {
        example1_ConcurrentHashMap();
        example2_ConcurrentLinkedQueue();
        example3_BlockingQueue();
        example4_CopyOnWriteArrayList();
        example5_ConcurrentHashMapAdvanced();
    }

    // Example 1: ConcurrentHashMap basics
    static void example1_ConcurrentHashMap() throws InterruptedException {
        System.out.println("=== Example 1: ConcurrentHashMap Basics ===");

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // Thread-safe put
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                map.put("key-" + i, i);
                System.out.println("T1 put: key-" + i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 5; i < 10; i++) {
                map.put("key-" + i, i);
                System.out.println("T2 put: key-" + i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Map size: " + map.size());
        System.out.println("Map contents: " + map);

        // Atomic operations
        map.putIfAbsent("key-0", 999); // Won't replace existing
        System.out.println("After putIfAbsent key-0: " + map.get("key-0"));

        map.compute("key-0", (k, v) -> v + 100);
        System.out.println("After compute key-0: " + map.get("key-0"));

        map.merge("key-0", 10, Integer::sum);
        System.out.println("After merge key-0: " + map.get("key-0"));

        System.out.println();
    }

    // Example 2: ConcurrentLinkedQueue
    static void example2_ConcurrentLinkedQueue() throws InterruptedException {
        System.out.println("=== Example 2: ConcurrentLinkedQueue ===");

        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        // Non-blocking concurrent queue
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.offer("item-" + i);
                System.out.println("Offered: item-" + i);
                try { TimeUnit.MILLISECONDS.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                String item = queue.poll();
                while (item == null) {
                    try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    item = queue.poll();
                }
                System.out.println("Polled: " + item);
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("Queue empty? " + queue.isEmpty());

        System.out.println();
    }

    // Example 3: BlockingQueue (producer-consumer)
    static void example3_BlockingQueue() throws InterruptedException {
        System.out.println("=== Example 3: BlockingQueue ===");

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(5);

        // put() blocks when full, take() blocks when empty
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    String item = "Item-" + i;
                    blockingQueue.put(item); // Blocks if queue is full
                    System.out.println("Put: " + item + " [size=" + blockingQueue.size() + "]");
                    TimeUnit.MILLISECONDS.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "BlockingProducer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 8; i++) {
                    String item = blockingQueue.take(); // Blocks if queue is empty
                    System.out.println("Took: " + item + " [size=" + blockingQueue.size() + "]");
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "BlockingConsumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // poll() with timeout
        System.out.println("\npoll() with timeout:");
        String polled = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
        System.out.println("Polled (should be null): " + polled);

        System.out.println();
    }

    // Example 4: CopyOnWriteArrayList
    static void example4_CopyOnWriteArrayList() throws InterruptedException {
        System.out.println("=== Example 4: CopyOnWriteArrayList ===");

        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();

        // Safe for concurrent iteration and modification
        cowList.add("A");
        cowList.add("B");
        cowList.add("C");

        // Iteration creates a snapshot - no ConcurrentModificationException
        Thread writer = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
                cowList.add("D");
                System.out.println("Added D");
                TimeUnit.MILLISECONDS.sleep(50);
                cowList.add("E");
                System.out.println("Added E");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Iteration " + i + ": " + cowList);
                try { TimeUnit.MILLISECONDS.sleep(80); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        System.out.println("Final list: " + cowList);
        System.out.println("Note: CopyOnWriteArrayList is best for read-heavy, write-rare scenarios");

        System.out.println();
    }

    // Example 5: ConcurrentHashMap advanced operations
    static void example5_ConcurrentHashMapAdvanced() throws InterruptedException {
        System.out.println("=== Example 5: ConcurrentHashMap Advanced ===");

        ConcurrentHashMap<String, Integer> wordCount = new ConcurrentHashMap<>();

        // Parallel word counting
        String[] texts = {
                "hello world hello java",
                "java is great world",
                "hello concurrency is great"
        };

        Thread[] counters = new Thread[texts.length];
        for (int i = 0; i < texts.length; i++) {
            final String text = texts[i];
            counters[i] = new Thread(() -> {
                for (String word : text.split(" ")) {
                    wordCount.merge(word, 1, Integer::sum); // Atomic merge
                }
            });
        }

        for (Thread t : counters) t.start();
        for (Thread t : counters) t.join();

        System.out.println("Word counts: " + wordCount);

        // forEach on ConcurrentHashMap
        System.out.println("\nWord count details:");
        wordCount.forEach(1, (word, count) ->
                System.out.println("  " + word + ": " + count));

        // Search
        String mostFrequent = wordCount.reduce(1,
                (k1, v1, k2, v2) -> v1 > v2 ? k1 : k2);
        System.out.println("Most frequent word: " + mostFrequent);

        System.out.println();
    }
}
