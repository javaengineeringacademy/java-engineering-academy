package academy.javaengineering.concurrency.solutions;

public class ThreadBasicsSolutions {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Create a thread using the Runnable interface
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: Runnable Interface ===");
        Runnable task = () -> System.out.println("Hello from Runnable");
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Exercise 2: Create a thread by extending the Thread class
     */
    static void exercise2() {
        System.out.println("=== Exercise 2: Extending Thread Class ===");
        class MyThread extends Thread {
            @Override
            public void run() {
                System.out.println("Hello from Extended Thread");
            }
        }
        MyThread thread = new MyThread();
        thread.start();
    }

    /**
     * Exercise 3: Create a lambda-based thread
     */
    static void exercise3() {
        System.out.println("=== Exercise 3: Lambda Thread ===");
        Thread thread = new Thread(() -> System.out.println("Hello from Lambda"));
        thread.start();
    }

    /**
     * Exercise 4: Multiple threads running concurrently
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Multiple Threads ===");
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int threadNum = i + 1;
            threads[i] = new Thread(() -> System.out.println("Thread " + threadNum + " is running"));
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("All threads completed");
    }

    /**
     * Exercise 5: Anonymous inner class thread
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Anonymous Inner Class ===");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from Anonymous Inner Class");
            }
        });
        thread.start();
    }
}
