package academy.javaengineering.concurrency.lifecycle;

/**
 * Examples - Runnable examples demonstrating thread lifecycle concepts.
 */
public class Examples {

    /**
     * Example 1: Thread State Machine
     * Demonstrates all 6 thread states and transitions between them.
     */
    static class Example1_ThreadStateMachine {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 1: Thread State Machine");
            System.out.println("================================");

            Object lock = new Object();

            Thread thread = new Thread(() -> {
                // RUNNABLE state
                System.out.println("  State: " + Thread.currentThread().getState());

                synchronized (lock) {
                    try {
                        // TIMED_WAITING state
                        Thread.sleep(200);
                        // WAITING state
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // TERMINATED when run() returns
            }, "StateDemo");

            // NEW state
            System.out.println("  Before start: " + thread.getState());

            thread.start();
            Thread.sleep(50);
            System.out.println("  After start: " + thread.getState());

            synchronized (lock) {
                lock.notify();
            }

            thread.join();
            System.out.println("  After completion: " + thread.getState());
            System.out.println();
        }
    }

    /**
     * Example 2: BLOCKED State Demonstration
     * Shows a thread entering BLOCKED state when waiting for a monitor lock.
     */
    static class Example2_BlockedState {
        private static final Object lock = new Object();

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 2: BLOCKED State");
            System.out.println("========================");

            Thread holder = new Thread(() -> {
                synchronized (lock) {
                    System.out.println("  Holder: acquired lock");
                    try { Thread.sleep(500); } catch (InterruptedException e) { return; }
                }
                System.out.println("  Holder: released lock");
            }, "LockHolder");

            Thread waiter = new Thread(() -> {
                try { Thread.sleep(50); } catch (InterruptedException e) { return; }
                System.out.println("  Waiter state: " + Thread.currentThread().getState());
                synchronized (lock) {
                    System.out.println("  Waiter: acquired lock");
                }
            }, "LockWaiter");

            holder.start();
            waiter.start();

            holder.join();
            waiter.join();
            System.out.println();
        }
    }

    /**
     * Example 3: Producer-Consumer with wait/notify
     * Classic lifecycle coordination pattern.
     */
    static class Example3_ProducerConsumer {
        private static final Object monitor = new Object();
        private static int[] buffer = new int[5];
        private static int count = 0;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 3: Producer-Consumer Lifecycle");
            System.out.println("=======================================");

            Thread producer = new Thread(() -> {
                for (int i = 1; i <= 10; i++) {
                    synchronized (monitor) {
                        while (count == buffer.length) {
                            try { monitor.wait(); } catch (InterruptedException e) { return; }
                        }
                        buffer[count++] = i;
                        System.out.println("  Produced: " + i);
                        monitor.notify();
                    }
                    try { Thread.sleep(50); } catch (InterruptedException e) { return; }
                }
            }, "Producer");

            Thread consumer = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    synchronized (monitor) {
                        while (count == 0) {
                            try { monitor.wait(); } catch (InterruptedException e) { return; }
                        }
                        int value = buffer[--count];
                        System.out.println("  Consumed: " + value);
                        monitor.notify();
                    }
                    try { Thread.sleep(100); } catch (InterruptedException e) { return; }
                }
            }, "Consumer");

            producer.start();
            consumer.start();
            producer.join();
            consumer.join();
            System.out.println();
        }
    }

    /**
     * Example 4: Thread Interruption Lifecycle
     * Shows how interrupts interact with thread states.
     */
    static class Example4_InterruptionLifecycle {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 4: Interruption Lifecycle");
            System.out.println("==================================");

            Thread sleeping = new Thread(() -> {
                try {
                    System.out.println("  Sleeping thread: going to sleep");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("  Sleeping thread: interrupted! Cleaning up...");
                }
                System.out.println("  Sleeping thread: finished");
            }, "SleepingThread");

            Thread waiting = new Thread(() -> {
                synchronized (monitor2) {
                    try {
                        System.out.println("  Waiting thread: waiting...");
                        monitor2.wait();
                    } catch (InterruptedException e) {
                        System.out.println("  Waiting thread: interrupted! Cleaning up...");
                    }
                }
                System.out.println("  Waiting thread: finished");
            }, "WaitingThread");

            sleeping.start();
            waiting.start();
            Thread.sleep(500);

            System.out.println("  Main: interrupting sleeping thread");
            sleeping.interrupt();
            System.out.println("  Main: interrupting waiting thread");
            waiting.interrupt();

            sleeping.join();
            waiting.join();
            System.out.println();
        }

        private static final Object monitor2 = new Object();
    }

    /**
     * Example 5: join() with Timeout Lifecycle
     * Demonstrates timed waiting via join().
     */
    static class Example5_JoinTimeout {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 5: join() Timeout Lifecycle");
            System.out.println("====================================");

            Thread slow = new Thread(() -> {
                try {
                    System.out.println("  Slow: working for 3 seconds...");
                    Thread.sleep(3000);
                    System.out.println("  Slow: done");
                } catch (InterruptedException e) {
                    System.out.println("  Slow: interrupted");
                }
            }, "SlowThread");

            slow.start();
            System.out.println("  Main: waiting 500ms...");

            long start = System.currentTimeMillis();
            slow.join(500);
            long elapsed = System.currentTimeMillis() - start;

            if (slow.isAlive()) {
                System.out.println("  Main: still alive after " + elapsed + "ms");
                System.out.println("  Main: waiting for actual completion...");
            }

            slow.join();
            System.out.println("  Main: slow thread completed");
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Example1_ThreadStateMachine.main(args);
        Example2_BlockedState.main(args);
        Example3_ProducerConsumer.main(args);
        Example4_InterruptionLifecycle.main(args);
        Example5_JoinTimeout.main(args);
    }
}
