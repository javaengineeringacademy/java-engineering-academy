package academy.javaengineering.concurrency.collections.internals;

import java.util.concurrent.*;

public class ConcurrentHashMapInternals {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // Atomic operations
        map.put("a", 1);
        map.putIfAbsent("a", 99); // won't replace
        System.out.println("putIfAbsent: " + map.get("a"));

        map.compute("a", (k, v) -> v + 10);
        System.out.println("compute: " + map.get("a"));

        map.merge("b", 1, Integer::sum);
        map.merge("b", 5, Integer::sum);
        System.out.println("merge: " + map.get("b"));

        // Concurrent modification
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    map.merge("counter", 1, Integer::sum);
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Concurrent counter: " + map.get("counter"));
    }
}
