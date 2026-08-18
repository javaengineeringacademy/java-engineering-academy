package academy.javaengineering.concurrency.collections.examples;

import java.util.concurrent.*;
import java.util.*;

public class ConcurrentCollectionsExamples {
    public static void main(String[] args) throws Exception {
        // ConcurrentHashMap
        ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
        scores.put("Alice", 100);
        scores.put("Bob", 85);
        scores.putIfAbsent("Charlie", 90);
        scores.computeIfPresent("Alice", (k, v) -> v + 10);
        System.out.println("Scores: " + scores);

        // BlockingQueue (producer-consumer)
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put("Item-" + i);
                    System.out.println("Produced: Item-" + i);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String item = queue.take();
                    System.out.println("  Consumed: " + item);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // CopyOnWriteArrayList
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        for (String s : list) {
            list.add("C"); // safe during iteration
            System.out.println("Read: " + s);
        }
        System.out.println("List: " + list);
    }
}
