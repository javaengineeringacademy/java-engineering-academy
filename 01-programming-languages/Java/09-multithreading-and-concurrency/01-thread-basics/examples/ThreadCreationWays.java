package academy.javaengineering.concurrency.threadbasics.examples;

public class ThreadCreationWays {
    public static void main(String[] args) throws InterruptedException {
        // Way 1: Extend Thread
        Thread t1 = new Thread() {
            @Override
            public void run() {
                System.out.println("1. Extending Thread: " + Thread.currentThread().getName());
            }
        };

        // Way 2: Implement Runnable
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("2. Implementing Runnable: " + Thread.currentThread().getName());
            }
        };
        Thread t2 = new Thread(runnable);

        // Way 3: Lambda Runnable
        Thread t3 = new Thread(() -> {
            System.out.println("3. Lambda Runnable: " + Thread.currentThread().getName());
        });

        // Way 4: Method reference
        Thread t4 = new Thread(ThreadCreationWays::printMessage);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("All threads completed");
    }

    static void printMessage() {
        System.out.println("4. Method reference: " + Thread.currentThread().getName());
    }
}
