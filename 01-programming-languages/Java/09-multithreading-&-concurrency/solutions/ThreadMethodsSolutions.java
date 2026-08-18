package academy.javaengineering.concurrency.solutions;

public class ThreadMethodsSolutions {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Thread start() vs run()
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: start() vs run() ===");
        Thread thread = new Thread(() -> System.out.println("In thread: " + Thread.currentThread().getName()));

        System.out.println("Calling run() directly:");
        thread.run();  // Runs in current thread
        System.out.println("After run(): " + Thread.currentThread().getName());

        System.out.println("\nCalling start():");
        thread.start();  // Creates new thread
        System.out.println("After start(): " + Thread.currentThread().getName());
    }

    /**
     * Exercise 2: Thread.join()
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Thread join() ===");
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Thread completed");
        });
        thread.start();
        System.out.println("Waiting for thread to finish...");
        thread.join();
        System.out.println("Thread finished, continuing main");
    }

    /**
     * Exercise 3: Thread.sleep()
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Thread sleep() ===");
        Thread thread = new Thread(() -> {
            System.out.println("Start");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("End");
        });
        System.out.println("Waiting...");
        thread.start();
        thread.join();
    }

    /**
     * Exercise 4: Thread.interrupt()
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Thread interrupt() ===");
        Thread thread = new Thread(() -> {
            int count = 0;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Count: " + count++);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted!");
                    break;
                }
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        thread.join();
    }

    /**
     * Exercise 5: Thread.yield()
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Thread yield() ===");
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                Thread.yield();
            }
        };
        new Thread(task, "Thread-A").start();
        new Thread(task, "Thread-B").start();
    }
}
