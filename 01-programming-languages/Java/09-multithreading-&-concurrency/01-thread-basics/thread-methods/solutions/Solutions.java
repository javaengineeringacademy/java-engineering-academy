package academy.javaengineering.concurrency.basics.methods;

/**
 * Solutions - Complete solutions for all exercises
 */
public class Solutions {

    /**
     * EXERCISE 1 SOLUTION: start() vs run()
     */
    public static void exercise1() {
        System.out.println("=== EXERCISE 1 SOLUTION: start() vs run() ===");

        Thread t = new Thread(() -> {
            System.out.println("New thread: " + Thread.currentThread().getName());
        });

        System.out.println("Main thread: " + Thread.currentThread().getName());
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Both threads have different names");
        System.out.println();
    }

    /**
     * EXERCISE 2 SOLUTION: join() with Multiple Threads
     */
    public static void exercise2() {
        System.out.println("=== EXERCISE 2 SOLUTION: join() with Multiple Threads ===");

        Thread t1 = new Thread(() -> {
            try {
                System.out.println("Thread 1 sleeping 1s...");
                Thread.sleep(1000);
                System.out.println("Thread 1 done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                System.out.println("Thread 2 sleeping 2s...");
                Thread.sleep(2000);
                System.out.println("Thread 2 done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                System.out.println("Thread 3 sleeping 3s...");
                Thread.sleep(3000);
                System.out.println("Thread 3 done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All done!");
        System.out.println();
    }

    /**
     * EXERCISE 3 SOLUTION: Proper Interrupt Handling
     */
    public static void exercise3() {
        System.out.println("=== EXERCISE 3 SOLUTION: Proper Interrupt Handling ===");

        Thread worker = new Thread(() -> {
            int count = 0;
            while (!Thread.currentThread().isInterrupted()) {
                count++;
                if (count % 5000000 == 0) {
                    System.out.println("Working... " + count);
                }
            }
            System.out.println("Stopping gracefully. Work done: " + count);
        });

        worker.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Interrupting worker...");
        worker.interrupt();

        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Worker stopped");
        System.out.println();
    }

    /**
     * EXERCISE 4 SOLUTION: interrupted() vs isInterrupted()
     */
    public static void exercise4() {
        System.out.println("=== EXERCISE 4 SOLUTION: interrupted() vs isInterrupted() ===");

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Caught interrupt");
            }
        });

        t.start();
        t.interrupt();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Demonstrate the difference
        Thread.currentThread().interrupt(); // Set flag on current thread

        System.out.println("isInterrupted(): " + Thread.currentThread().isInterrupted());
        System.out.println("isInterrupted() again: " + Thread.currentThread().isInterrupted());
        System.out.println("interrupted(): " + Thread.interrupted());
        System.out.println("interrupted() again: " + Thread.interrupted());
        System.out.println();
    }

    /**
     * EXERCISE 5 SOLUTION: Daemon Thread
     */
    public static void exercise5() {
        System.out.println("=== EXERCISE 5 SOLUTION: Daemon Thread ===");

        Thread daemon = new Thread(() -> {
            int count = 0;
            while (true) {
                System.out.println("Background work: " + count);
                count++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        daemon.setDaemon(true); // Must be before start()
        daemon.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main thread exiting...");
        System.out.println("Daemon will be stopped automatically");
        System.out.println();
    }

    public static void main(String[] args) {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
