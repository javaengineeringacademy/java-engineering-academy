package academy.javaengineering.concurrency;

/**
 * Demonstrates synchronized keyword usage for thread safety.
 * Shows synchronized methods and blocks.
 */
public class SynchronizationExamples {

    private int counter = 0;
    private final Object lockObject = new Object();

    public static void main(String[] args) throws InterruptedException {
        SynchronizationExamples example = new SynchronizationExamples();
        example.demonstrateSynchronizedMethod();
        example.demonstrateSynchronizedBlock();
        example.demonstrateWaitNotify();
    }

    /**
     * Demonstrates synchronized method for thread-safe counter.
     */
    public synchronized void incrementCounter() {
        counter++;
    }

    /**
     * Demonstrates synchronized block for thread-safe counter.
     */
    public void incrementCounterWithBlock() {
        synchronized (lockObject) {
            counter++;
        }
    }

    /**
     * Gets the current counter value.
     */
    public synchronized int getCounter() {
        return counter;
    }

    /**
     * Demonstrates synchronized method usage.
     */
    public void demonstrateSynchronizedMethod() throws InterruptedException {
        counter = 0;
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementCounter();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Synchronized Method Counter: " + counter);
        // Expected output: Synchronized Method Counter: 10000
    }

    /**
     * Demonstrates synchronized block usage.
     */
    public void demonstrateSynchronizedBlock() throws InterruptedException {
        counter = 0;
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementCounterWithBlock();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Synchronized Block Counter: " + counter);
        // Expected output: Synchronized Block Counter: 10000
    }

    /**
     * Demonstrates wait/notify mechanism.
     */
    public void demonstrateWaitNotify() throws InterruptedException {
        final boolean[] flag = {false};
        Object waitLock = new Object();

        Thread producer = new Thread(() -> {
            try {
                synchronized (waitLock) {
                    System.out.println("Producer: producing data...");
                    Thread.sleep(100);
                    flag[0] = true;
                    waitLock.notify();
                    System.out.println("Producer: data produced, notified consumer");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                synchronized (waitLock) {
                    while (!flag[0]) {
                        System.out.println("Consumer: waiting for data...");
                        waitLock.wait();
                    }
                    System.out.println("Consumer: received data");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        Thread.sleep(50);
        producer.start();

        producer.join();
        consumer.join();
        // Expected output:
        // Consumer: waiting for data...
        // Producer: producing data...
        // Producer: data produced, notified consumer
        // Consumer: received data
    }
}
