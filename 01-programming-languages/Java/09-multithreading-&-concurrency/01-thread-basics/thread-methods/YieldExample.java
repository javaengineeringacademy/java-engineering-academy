package academy.javaengineering.concurrency.basics.methods;

/**
 * yield() behavior - Hint to scheduler
 */
public class YieldExample {

    public static void main(String[] args) {
        System.out.println("=== Yield Example ===\n");

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread-1: " + i);
                if (i == 2) {
                    System.out.println("Thread-1: yield() called");
                    Thread.yield(); // Hint to scheduler
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread-2: " + i);
                if (i == 2) {
                    System.out.println("Thread-2: yield() called");
                    Thread.yield(); // Hint to scheduler
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== Key Points ===");
        System.out.println("yield() is a HINT, not a guarantee");
        System.out.println("Scheduler may ignore it");
        System.out.println("Thread returns to RUNNABLE state (not blocked)");
    }
}
