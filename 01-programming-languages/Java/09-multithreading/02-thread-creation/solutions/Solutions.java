package academy.javaengineering.concurrency.threadcreation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solutions - Complete solutions for Thread Creation exercises.
 */
public class Solutions {

    /**
     * Solution 1: Three Creation Methods
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: Three Creation Methods");
        System.out.println("===================================");

        // Extending Thread
        Thread t1 = new Thread() {
            @Override
            public void run() {
                System.out.println("  Thread created using extends Thread: " + getName());
            }
        };
        t1.setName("Subclass-1");

        Thread t2 = new Thread() {
            @Override
            public void run() {
                System.out.println("  Thread created using extends Thread: " + getName());
            }
        };
        t2.setName("Subclass-2");

        // Implementing Runnable
        Runnable task = () -> {
            System.out.println("  Thread created using implements Runnable: " +
                Thread.currentThread().getName());
        };
        Thread t3 = new Thread(task, "Runnable-1");
        Thread t4 = new Thread(task, "Runnable-2");

        // Lambda
        Thread t5 = new Thread(() -> {
            System.out.println("  Thread created using lambda: " +
                Thread.currentThread().getName());
        }, "Lambda-1");

        t1.start(); t2.start(); t3.start(); t4.start(); t5.start();
        t1.join(); t2.join(); t3.join(); t4.join(); t5.join();

        System.out.println();
    }

    /**
     * Solution 2: Factorial with Callable
     */
    static void exercise2() {
        System.out.println("Exercise 2: Factorial with Callable");
        System.out.println("====================================");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int n = 1; n <= 10; n++) {
            final int number = n;
            Callable<Long> factorialTask = () -> {
                long result = 1;
                for (int i = 2; i <= number; i++) {
                    result *= i;
                }
                return result;
            };

            Future<Long> future = executor.submit(factorialTask);
            try {
                System.out.println("  " + number + "! = " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        System.out.println();
    }

    /**
     * Solution 3: Custom Thread Factory
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: Custom Thread Factory");
        System.out.println("=================================");

        ThreadFactory factory = new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Pool-" + count.incrementAndGet());
            }
        };

        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = factory.newThread(() -> {
                System.out.println("  " + Thread.currentThread().getName() + " executing");
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println();
    }

    /**
     * Solution 4: CompletableFuture Chain
     */
    static void exercise4() {
        System.out.println("Exercise 4: CompletableFuture Chain");
        System.out.println("====================================");

        java.util.concurrent.CompletableFuture
            .supplyAsync(() -> {
                int random = (int) (Math.random() * 100) + 1;
                System.out.println("  Generated: " + random);
                return random;
            })
            .thenApply(n -> {
                int squared = n * n;
                System.out.println("  Squared: " + squared);
                return squared;
            })
            .thenApply(n -> {
                String parity = (n % 2 == 0) ? "even" : "odd";
                System.out.println("  " + n + " is " + parity);
                return parity;
            })
            .thenAccept(result -> {
                System.out.println("  Final result: " + result);
            })
            .join();

        System.out.println();
    }

    /**
     * Solution 5: Daemon Heartbeat
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: Daemon Heartbeat");
        System.out.println("============================");

        Thread daemon = new Thread(() -> {
            int beat = 0;
            while (true) {
                beat++;
                System.out.println("  Heartbeat #" + beat + " at " +
                    System.currentTimeMillis());
                try { Thread.sleep(500); } catch (InterruptedException e) { return; }
            }
        }, "HeartbeatDaemon");

        daemon.setDaemon(true);
        daemon.start();

        Thread.sleep(2000);
        System.out.println("  Main done");

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
