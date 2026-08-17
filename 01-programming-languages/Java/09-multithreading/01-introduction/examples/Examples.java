package academy.javaengineering.concurrency.introduction;

import java.util.ArrayList;
import java.util.List;

/**
 * Examples - Runnable examples demonstrating multithreading introduction concepts.
 * Each example is self-contained with a main() method.
 */
public class Examples {

    /**
     * Example 1: Basic Thread Creation and Join
     * Demonstrates creating threads, starting them, and waiting for completion.
     */
    static class Example1_BasicThreadCreation {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 1: Basic Thread Creation and Join");
            System.out.println("=========================================");

            Thread thread1 = new Thread(() -> {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("  Thread-1: step " + i);
                    try { Thread.sleep(200); } catch (InterruptedException e) { return; }
                }
            }, "Worker-1");

            Thread thread2 = new Thread(() -> {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("  Thread-2: step " + i);
                    try { Thread.sleep(300); } catch (InterruptedException e) { return; }
                }
            }, "Worker-2");

            System.out.println("Starting threads...");
            thread1.start();
            thread2.start();

            System.out.println("Waiting for threads to finish...");
            thread1.join();
            thread2.join();

            System.out.println("Both threads completed. Main thread continues.");
            System.out.println();
        }
    }

    /**
     * Example 2: Producer-Consumer with Shared List
     * Demonstrates basic thread communication and synchronization.
     */
    static class Example2_ProducerConsumer {
        private static final List<Integer> buffer = new ArrayList<>();
        private static final Object lock = new Object();
        private static final int ITEMS_TO_PRODUCE = 10;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 2: Producer-Consumer Pattern");
            System.out.println("====================================");

            Thread producer = new Thread(() -> {
                for (int i = 1; i <= ITEMS_TO_PRODUCE; i++) {
                    synchronized (lock) {
                        buffer.add(i);
                        System.out.println("  Produced: " + i + " (buffer size: " + buffer.size() + ")");
                        lock.notify();
                    }
                    try { Thread.sleep(100); } catch (InterruptedException e) { return; }
                }
            }, "Producer");

            Thread consumer = new Thread(() -> {
                int consumed = 0;
                while (consumed < ITEMS_TO_PRODUCE) {
                    synchronized (lock) {
                        while (buffer.isEmpty()) {
                            try { lock.wait(); } catch (InterruptedException e) { return; }
                        }
                        int value = buffer.remove(0);
                        consumed++;
                        System.out.println("  Consumed: " + value + " (buffer size: " + buffer.size() + ")");
                    }
                }
            }, "Consumer");

            producer.start();
            consumer.start();

            producer.join();
            consumer.join();

            System.out.println("All items produced and consumed.");
            System.out.println();
        }
    }

    /**
     * Example 3: Thread Naming and Priority
     * Demonstrates setting thread names, priorities, and observing behavior.
     */
    static class Example3_ThreadNamingPriority {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 3: Thread Naming and Priority");
            System.out.println("======================================");

            Runnable task = () -> {
                String name = Thread.currentThread().getName();
                int priority = Thread.currentThread().getPriority();
                for (int i = 1; i <= 3; i++) {
                    System.out.println("  [" + name + " p=" + priority + "] work " + i);
                    Thread.yield();
                }
            };

            Thread high = new Thread(task, "HighPri");
            Thread normal = new Thread(task, "NormalPri");
            Thread low = new Thread(task, "LowPri");

            high.setPriority(Thread.MAX_PRIORITY);
            normal.setPriority(Thread.NORM_PRIORITY);
            low.setPriority(Thread.MIN_PRIORITY);

            low.start();
            normal.start();
            high.start();

            low.join();
            normal.join();
            high.join();

            System.out.println("Note: Priority is a hint, not a guarantee of execution order.");
            System.out.println();
        }
    }

    /**
     * Example 4: Daemon Thread Behavior
     * Demonstrates daemon vs user threads and JVM shutdown behavior.
     */
    static class Example4_DaemonThreads {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 4: Daemon Thread Behavior");
            System.out.println("==================================");

            Thread daemon = new Thread(() -> {
                int count = 0;
                while (true) {
                    count++;
                    System.out.println("  Daemon heartbeat: " + count);
                    try { Thread.sleep(100); } catch (InterruptedException e) { return; }
                }
            }, "HeartbeatDaemon");

            daemon.setDaemon(true);
            daemon.start();

            Thread.sleep(350);

            System.out.println("  Main thread finishing (daemon will be stopped by JVM)");
            System.out.println();
        }
    }

    /**
     * Example 5: Interrupt Handling
     * Demonstrates graceful thread shutdown using interrupt mechanism.
     */
    static class Example5_InterruptHandling {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 5: Interrupt Handling");
            System.out.println("==============================");

            Thread worker = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("  Worker: doing work...");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("  Worker: interrupted, cleaning up...");
                        Thread.currentThread().interrupt(); // Restore interrupt flag
                        break;
                    }
                }
                System.out.println("  Worker: gracefully shut down");
            }, "Worker");

            worker.start();
            Thread.sleep(350);

            System.out.println("  Main: sending interrupt signal");
            worker.interrupt();
            worker.join();

            System.out.println("  Main: worker thread finished");
            System.out.println();
        }
    }

    /**
     * Example 6: Join with Timeout
     * Demonstrates using join with a timeout limit.
     */
    static class Example6_JoinWithTimeout {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 6: Join with Timeout");
            System.out.println("============================");

            Thread slowThread = new Thread(() -> {
                try {
                    System.out.println("  Slow thread: starting (will take 2s)");
                    Thread.sleep(2000);
                    System.out.println("  Slow thread: done");
                } catch (InterruptedException e) {
                    System.out.println("  Slow thread: interrupted");
                }
            }, "SlowThread");

            slowThread.start();

            System.out.println("  Main: waiting up to 500ms for slow thread...");
            slowThread.join(500);

            if (slowThread.isAlive()) {
                System.out.println("  Main: slow thread still running after 500ms");
                System.out.println("  Main: continuing with other work");
            } else {
                System.out.println("  Main: slow thread completed");
            }

            slowThread.join(); // Wait for actual completion
            System.out.println("  Main: all done");
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Example1_BasicThreadCreation.main(args);
        Example2_ProducerConsumer.main(args);
        Example3_ThreadNamingPriority.main(args);
        Example4_DaemonThreads.main(args);
        Example5_InterruptHandling.main(args);
        Example6_JoinWithTimeout.main(args);
    }
}
