package multithreading;

/**
 * ThreadBasics - Thread creation, start, join
 *
 * Covers:
 * - Thread lifecycle basics
 * - Starting threads
 * - Joining threads
 * - Thread naming and priorities
 */
public class ThreadBasics {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Creating Threads ===");
        threadCreation();

        System.out.println("\n=== Thread Join ===");
        threadJoin();

        System.out.println("\n=== Thread Naming ===");
        threadNaming();

        System.out.println("\n=== Thread Priority ===");
        threadPriority();
    }

    static void threadCreation() throws InterruptedException {
        // Method 1: Extending Thread class
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread 1 (extends Thread): " + Thread.currentThread().getName());
            }
        };

        // Method 2: Implementing Runnable
        Runnable runnable = () -> {
            System.out.println("Thread 2 (Runnable): " + Thread.currentThread().getName());
        };
        Thread thread2 = new Thread(runnable);

        // Method 3: Using lambda (Java 8+)
        Thread thread3 = new Thread(() -> {
            System.out.println("Thread 3 (Lambda): " + Thread.currentThread().getName());
        });

        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();

        // Wait for all to complete
        thread1.join();
        thread2.join();
        thread3.join();
    }

    static void threadJoin() throws InterruptedException {
        Thread producer = new Thread(() -> {
            try {
                System.out.println("Producer: Working...");
                Thread.sleep(1000);
                System.out.println("Producer: Done!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                producer.join(); // Wait for producer to finish
                System.out.println("Consumer: Processing after producer completes");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        consumer.join();
        System.out.println("Main thread: Both threads completed");
    }

    static void threadNaming() {
        Thread named = new Thread(() -> {
            System.out.println("Named thread: " + Thread.currentThread().getName());
            System.out.println("Thread ID: " + Thread.currentThread().getId());
        }, "MyWorkerThread");

        named.start();

        try {
            named.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Main thread info
        System.out.println("Main thread: " + Thread.currentThread().getName());
        System.out.println("Default thread group: " +
            Thread.currentThread().getThreadGroup().getName());
    }

    static void threadPriority() throws InterruptedException {
        Thread highPriority = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("High priority (" +
                    Thread.currentThread().getPriority() + "): " + i);
            }
        }, "HighPriority");

        Thread lowPriority = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Low priority (" +
                    Thread.currentThread().getPriority() + "): " + i);
            }
        }, "LowPriority");

        highPriority.setPriority(Thread.MAX_PRIORITY); // 10
        lowPriority.setPriority(Thread.MIN_PRIORITY);  // 1

        highPriority.start();
        lowPriority.start();

        highPriority.join();
        lowPriority.join();

        System.out.println("Note: Priority is a hint, not a guarantee");
    }
}