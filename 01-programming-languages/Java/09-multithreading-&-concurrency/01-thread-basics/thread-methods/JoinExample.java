package academy.javaengineering.concurrency.basics.methods;

/**
 * join() usage - Wait for thread to complete
 */
public class JoinExample {

    public static void main(String[] args) {
        System.out.println("=== Join Example ===\n");

        // Basic join()
        System.out.println("--- Basic join() ---");
        basicJoin();

        // join(timeout)
        System.out.println("\n--- join(timeout) ---");
        joinWithTimeout();

        // Multiple threads join order
        System.out.println("\n--- Multiple Threads Join Order ---");
        multipleThreadsJoin();
    }

    static void basicJoin() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Thread completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        System.out.println("Main thread waiting for thread to complete...");
        try {
            thread.join(); // Wait indefinitely
            System.out.println("Thread completed, main continues");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void joinWithTimeout() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000); // Sleep 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        try {
            System.out.println("Waiting up to 1 second...");
            thread.join(1000); // Wait max 1 second
            System.out.println("Timeout reached or thread completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void multipleThreadsJoin() {
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Thread-1 completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(500);
                System.out.println("Thread-2 completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join(); // Wait for thread1
            thread2.join(); // Wait for thread2
            System.out.println("Both threads completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
