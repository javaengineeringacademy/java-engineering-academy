package academy.javaengineering.concurrency.sync.memory;

public class SynchronizationMemory {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Synchronization Memory Model");
        System.out.println("============================");

        // Without volatile — possible visibility issue
        class Counter {
            int count = 0;
            volatile boolean running = true;
        }

        Counter c = new Counter();
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) c.count++;
            c.running = false;
        });

        Thread reader = new Thread(() -> {
            while (c.running) Thread.yield();
            System.out.println("Without sync - count: " + c.count + " (may not be 1000)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        // With synchronized
        class SyncCounter {
            int count = 0;
            volatile boolean running = true;
            synchronized void increment() { count++; }
        }

        SyncCounter sc = new SyncCounter();
        Thread writer2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) sc.increment();
            sc.running = false;
        });

        Thread reader2 = new Thread(() -> {
            while (sc.running) Thread.yield();
            System.out.println("With sync - count: " + sc.count + " (always 1000)");
        });

        writer2.start();
        reader2.start();
        writer2.join();
        reader2.join();
    }
}
