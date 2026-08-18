package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ConcurrentCollectionsSolutions {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: ConcurrentHashMap putIfAbsent
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: ConcurrentHashMap putIfAbsent ===");
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        String key = "existing";
        map.put(key, 100);

        Integer existing = map.putIfAbsent(key, 200);
        System.out.println("putIfAbsent on existing key: " + existing);

        Integer newVal = map.putIfAbsent("new", 300);
        System.out.println("putIfAbsent on new key: " + newVal);
        System.out.println("Map: " + map);
    }

    /**
     * Exercise 2: ConcurrentHashMap compute
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: ConcurrentHashMap compute ===");
        ConcurrentHashMap<String, Integer> wordCount = new ConcurrentHashMap<>();

        String[] words = {"hello", "world", "hello", "java", "hello", "world"};

        for (String word : words) {
            wordCount.compute(word, (key, count) -> count == null ? 1 : count + 1);
        }

        wordCount.computeIfAbsent("new", key -> 0);

        System.out.println("Word counts: " + wordCount);
    }

    /**
     * Exercise 3: ConcurrentLinkedQueue
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: ConcurrentLinkedQueue ===");
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                queue.offer("Item-" + i);
                System.out.println("Produced: Item-" + i);
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String item;
            while ((item = queue.poll()) != null) {
                System.out.println("Consumed: " + item);
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    /**
     * Exercise 4: BlockingQueue put/take
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: BlockingQueue put/take ===");
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    queue.put(i);
                    System.out.println("Put: " + i + ", size: " + queue.size());
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int item = queue.take();
                    System.out.println("Took: " + item);
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    /**
     * Exercise 5: ArrayBlockingQueue bounded
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: ArrayBlockingQueue ===");
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    queue.put(i);
                    System.out.println("Put: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 8; i++) {
                    int item = queue.take();
                    System.out.println("Took: " + item);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
