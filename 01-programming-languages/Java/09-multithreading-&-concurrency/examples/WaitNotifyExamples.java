package academy.javaengineering.concurrency.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WaitNotifyExamples {

    private static final List<Integer> sharedList = new ArrayList<>();
    private static final Object listLock = new Object();
    private static final int MAX_SIZE = 5;

    public static void main(String[] args) throws InterruptedException {
        example1_WaitNotifyAll();
        example2_BasicProducerConsumer();
        example3_MultipleProducersConsumers();
        example4_WaitWithTimeout();
        example5_SpuriousWakeup();
    }

    // Example 1: wait(), notify(), notifyAll() differences
    static void example1_WaitNotifyAll() throws InterruptedException {
        System.out.println("=== Example 1: wait/notify/notifyAll ===");

        Object monitor = new Object();
        List<Thread> waiters = new ArrayList<>();

        // Create multiple waiters
        for (int i = 0; i < 3; i++) {
            final int id = i;
            Thread waiter = new Thread(() -> {
                synchronized (monitor) {
                    try {
                        System.out.println("Waiter-" + id + " waiting...");
                        monitor.wait();
                        System.out.println("Waiter-" + id + " notified!");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }, "Waiter-" + id);
            waiters.add(waiter);
        }

        for (Thread t : waiters) t.start();
        TimeUnit.MILLISECONDS.sleep(300);

        // notify() wakes only ONE thread
        synchronized (monitor) {
            System.out.println("Calling notify() - only one waiter will wake");
            monitor.notify();
        }

        TimeUnit.MILLISECONDS.sleep(300);

        // notifyAll() wakes ALL waiting threads
        synchronized (monitor) {
            System.out.println("Calling notifyAll() - all remaining waiters will wake");
            monitor.notifyAll();
        }

        for (Thread t : waiters) t.join();

        System.out.println();
    }

    // Example 2: Basic producer-consumer with wait/notify
    static void example2_BasicProducerConsumer() throws InterruptedException {
        System.out.println("=== Example 2: Basic Producer-Consumer ===");

        SharedBuffer buffer = new SharedBuffer(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.put(i);
                    System.out.println("Produced: " + i);
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int item = buffer.get();
                    System.out.println("Consumed: " + item);
                    TimeUnit.MILLISECONDS.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println();
    }

    // Example 3: Multiple producers and consumers
    static void example3_MultipleProducersConsumers() throws InterruptedException {
        System.out.println("=== Example 3: Multiple Producers & Consumers ===");

        SharedBuffer buffer = new SharedBuffer(3);
        int itemsPerProducer = 5;
        int numProducers = 2;
        int numConsumers = 2;

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();

        for (int p = 0; p < numProducers; p++) {
            final int producerId = p;
            Thread producer = new Thread(() -> {
                try {
                    for (int i = 1; i <= itemsPerProducer; i++) {
                        buffer.put(producerId * 100 + i);
                        System.out.println("Producer-" + producerId + " put: " + (producerId * 100 + i));
                        TimeUnit.MILLISECONDS.sleep(50);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer-" + producerId);
            producers.add(producer);
        }

        int totalItems = numProducers * itemsPerProducer;
        for (int c = 0; c < numConsumers; c++) {
            final int consumerId = c;
            final int itemsPerConsumer = totalItems / numConsumers;
            Thread consumer = new Thread(() -> {
                try {
                    for (int i = 0; i < itemsPerConsumer; i++) {
                        int item = buffer.get();
                        System.out.println("Consumer-" + consumerId + " got: " + item);
                        TimeUnit.MILLISECONDS.sleep(80);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer-" + consumerId);
            consumers.add(consumer);
        }

        for (Thread t : producers) t.start();
        for (Thread t : consumers) t.start();

        for (Thread t : producers) t.join();
        for (Thread t : consumers) t.join();

        System.out.println();
    }

    // Example 4: wait() with timeout
    static void example4_WaitWithTimeout() throws InterruptedException {
        System.out.println("=== Example 4: wait() with Timeout ===");

        Object monitor = new Object();

        Thread waiter = new Thread(() -> {
            synchronized (monitor) {
                try {
                    System.out.println("Waiting for 1 second...");
                    monitor.wait(1000); // Wait at most 1 second
                    System.out.println("Wait completed (timed out or notified)");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "TimedWaiter");

        waiter.start();
        waiter.join();

        // Different scenario: notification before timeout
        Object monitor2 = new Object();
        Thread waiter2 = new Thread(() -> {
            synchronized (monitor2) {
                try {
                    System.out.println("\nWaiting for 5 seconds (will be notified early)...");
                    monitor2.wait(5000);
                    System.out.println("Wait completed (was notified)");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "EarlyNotifyWaiter");

        Thread notifier2 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (monitor2) {
                System.out.println("Notifier: sending early notification");
                monitor2.notify();
            }
        }, "EarlyNotifier");

        waiter2.start();
        notifier2.start();
        waiter2.join();
        notifier2.join();

        System.out.println();
    }

    // Example 5: Handling spurious wakeups
    static void example5_SpuriousWakeup() throws InterruptedException {
        System.out.println("=== Example 5: Spurious Wakeup Handling ===");

        // Always use while loop with condition check to handle spurious wakeups
        Object monitor = new Object();
        final boolean[] dataReady = {false};

        Thread consumer = new Thread(() -> {
            synchronized (monitor) {
                // CORRECT: while loop to handle spurious wakeups
                while (!dataReady[0]) {
                    try {
                        System.out.println("Consumer: waiting for data...");
                        monitor.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("Consumer: data received!");
            }
        }, "SafeConsumer");

        consumer.start();
        TimeUnit.MILLISECONDS.sleep(500);

        // Simulate spurious wakeup by calling notify before data is ready
        synchronized (monitor) {
            System.out.println("Simulating spurious wakeup...");
            monitor.notify(); // Data not ready yet, but thread wakes
        }

        TimeUnit.MILLISECONDS.sleep(100);

        // Actually set data ready
        synchronized (monitor) {
            dataReady[0] = true;
            System.out.println("Setting data ready and notifying...");
            monitor.notify();
        }

        consumer.join();
        System.out.println();
    }

    // Shared bounded buffer implementation
    static class SharedBuffer {
        private final List<Integer> buffer = new ArrayList<>();
        private final int capacity;

        public SharedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void put(int item) throws InterruptedException {
            while (buffer.size() >= capacity) {
                System.out.println("Buffer full, producer waiting...");
                wait();
            }
            buffer.add(item);
            notifyAll(); // Wake up consumers
        }

        public synchronized int get() throws InterruptedException {
            while (buffer.isEmpty()) {
                System.out.println("Buffer empty, consumer waiting...");
                wait();
            }
            int item = buffer.remove(0);
            notifyAll(); // Wake up producers
            return item;
        }
    }
}
