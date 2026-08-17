package academy.javaengineering.concurrency.introduction;

/**
 * Solutions - Complete solutions for Introduction to Multithreading exercises.
 */
public class Solutions {

    /**
     * Solution 1: Concurrent Countdown
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: Concurrent Countdown");
        System.out.println("==================================");

        Runnable countdown = () -> {
            String name = Thread.currentThread().getName();
            for (int i = 5; i >= 1; i--) {
                System.out.println("  " + name + ": " + i);
                try { Thread.sleep(200); } catch (InterruptedException e) { return; }
            }
        };

        Thread t1 = new Thread(countdown, "Thread-1");
        Thread t2 = new Thread(countdown, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("  Done");
        System.out.println();
    }

    /**
     * Solution 2: Thread-Safe Counter
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: Thread-Safe Counter");
        System.out.println("===============================");

        final Object lock = new Object();
        int[] counter = {0};
        int numThreads = 10;
        int incrementsPerThread = 1000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    synchronized (lock) {
                        counter[0]++;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        int expected = numThreads * incrementsPerThread;
        System.out.println("  Expected: " + expected);
        System.out.println("  Actual: " + counter[0]);
        System.out.println("  Thread-safe: " + (counter[0] == expected));
        System.out.println();
    }

    /**
     * Solution 3: Bounded Producer-Consumer
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: Bounded Producer-Consumer");
        System.out.println("======================================");

        final int BUFFER_SIZE = 5;
        final int ITEMS = 20;
        final Object[] buffer = new Object[BUFFER_SIZE];
        final Object lock = new Object();
        final int[] count = {0};

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= ITEMS; i++) {
                synchronized (lock) {
                    while (count[0] == BUFFER_SIZE) {
                        try { lock.wait(); } catch (InterruptedException e) { return; }
                    }
                    buffer[count[0]++] = i;
                    System.out.println("  Produced: " + i + " (buffer: " + count[0] + ")");
                    lock.notify();
                }
                try { Thread.sleep(50); } catch (InterruptedException e) { return; }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            int consumed = 0;
            while (consumed < ITEMS) {
                synchronized (lock) {
                    while (count[0] == 0) {
                        try { lock.wait(); } catch (InterruptedException e) { return; }
                    }
                    int value = (int) buffer[--count[0]];
                    consumed++;
                    System.out.println("  Consumed: " + value + " (buffer: " + count[0] + ")");
                    lock.notify();
                }
                try { Thread.sleep(100); } catch (InterruptedException e) { return; }
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("  All items produced and consumed.");
        System.out.println();
    }

    /**
     * Solution 4: Graceful Shutdown
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Graceful Shutdown");
        System.out.println("=============================");

        Thread worker = new Thread(() -> {
            int iteration = 0;
            while (!Thread.currentThread().isInterrupted()) {
                iteration++;
                System.out.println("  Worker: iteration " + iteration);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("  Worker: caught interruption");
                    Thread.currentThread().interrupt(); // Restore flag
                    break;
                }
            }
            System.out.println("  Worker: Shutting down gracefully...");
        }, "Worker");

        worker.start();
        Thread.sleep(500);
        System.out.println("  Main: interrupting worker...");
        worker.interrupt();
        worker.join();
        System.out.println("  Main: worker thread finished");
        System.out.println();
    }

    /**
     * Solution 5: Join with Timeout
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Join with Timeout");
        System.out.println("=============================");

        Thread slowThread = new Thread(() -> {
            try {
                System.out.println("  SlowThread: starting work (3s)...");
                Thread.sleep(3000);
                System.out.println("  SlowThread: work completed");
            } catch (InterruptedException e) {
                System.out.println("  SlowThread: interrupted");
            }
        }, "SlowThread");

        slowThread.start();

        System.out.println("  Main: waiting 1s...");
        slowThread.join(1000);

        if (slowThread.isAlive()) {
            System.out.println("  Main: Still working...");
        }

        System.out.println("  Main: waiting for actual completion...");
        slowThread.join();
        System.out.println("  Main: all done");
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
