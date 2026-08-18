package academy.javaengineering.concurrency.communication.examples;

import java.util.concurrent.*;

public class ThreadCommunicationExamples {
    public static void main(String[] args) throws InterruptedException {
        // Example 1: Producer-Consumer with wait/notify
        System.out.println("=== Producer-Consumer ===");
        Object lock = new Object();
        java.util.LinkedList<Integer> buffer = new java.util.LinkedList<>();
        int capacity = 5;

        Runnable producer = () -> {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (buffer.size() == capacity) {
                        try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                    buffer.add(i);
                    System.out.println("Produced: " + i + " (size: " + buffer.size() + ")");
                    lock.notifyAll();
                }
            }
        };

        Runnable consumer = () -> {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (buffer.isEmpty()) {
                        try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                    int item = buffer.remove();
                    System.out.println("  Consumed: " + item + " (size: " + buffer.size() + ")");
                    lock.notifyAll();
                }
            }
        };

        Thread p = new Thread(producer, "Producer");
        Thread c = new Thread(consumer, "Consumer");
        p.start();
        c.start();
        p.join();
        c.join();

        // Example 2: Thread.join for ordering
        System.out.println("\n=== Thread.join Ordering ===");
        Thread last = new Thread(() -> System.out.println("Third"));
        Thread second = new Thread(() -> {
            try { last.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("Second");
        });
        Thread first = new Thread(() -> {
            try { second.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("First");
        });
        first.start();
        second.start();
        last.start();
        first.join();

        // Example 3: Graceful shutdown with interrupt
        System.out.println("\n=== Interrupt Handling ===");
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Working...");
                try { Thread.sleep(100); } catch (InterruptedException e) {
                    System.out.println("Interrupted! Cleaning up...");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Worker stopped gracefully");
        });
        worker.start();
        Thread.sleep(350);
        worker.interrupt();
        worker.join();
    }
}
