package academy.javaengineering.concurrency.threadcreation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Examples - Runnable examples demonstrating thread creation patterns.
 */
public class Examples {

    /**
     * Example 1: Three Ways to Create Threads
     * Compares extending Thread, implementing Runnable, and using lambda.
     */
    static class Example1_ThreeWaysToCreate {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 1: Three Ways to Create Threads");
            System.out.println("========================================");

            // Method 1: Extending Thread
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    System.out.println("  [extends Thread] " + Thread.currentThread().getName());
                }
            };
            t1.setName("SubclassThread");

            // Method 2: Implementing Runnable
            Runnable runnable = () -> {
                System.out.println("  [Runnable] " + Thread.currentThread().getName());
            };
            Thread t2 = new Thread(runnable, "RunnableThread");

            // Method 3: Lambda
            Thread t3 = new Thread(() -> {
                System.out.println("  [Lambda] " + Thread.currentThread().getName());
            }, "LambdaThread");

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();

            System.out.println("  All threads completed.");
            System.out.println();
        }
    }

    /**
     * Example 2: Callable with Future for Return Values
     * Demonstrates getting results from threads using Callable.
     */
    static class Example2_CallableWithFuture {
        public static void main(String[] args) {
            System.out.println("Example 2: Callable with Future");
            System.out.println("===============================");

            ExecutorService executor = Executors.newFixedThreadPool(3);

            Callable<Integer> sumTask = () -> {
                int sum = 0;
                for (int i = 1; i <= 100; i++) {
                    sum += i;
                }
                System.out.println("  Computed sum on: " + Thread.currentThread().getName());
                return sum;
            };

            Callable<String> greetingTask = () -> {
                Thread.sleep(100);
                return "Hello from " + Thread.currentThread().getName();
            };

            Callable<Double> randomTask = () -> {
                return Math.random() * 100;
            };

            Future<Integer> sumFuture = executor.submit(sumTask);
            Future<String> greetingFuture = executor.submit(greetingTask);
            Future<Double> randomFuture = executor.submit(randomTask);

            try {
                System.out.println("  Sum: " + sumFuture.get());
                System.out.println("  Greeting: " + greetingFuture.get());
                System.out.println("  Random: " + String.format("%.2f", randomFuture.get()));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            executor.shutdown();
            System.out.println();
        }
    }

    /**
     * Example 3: Thread Factory Pattern
     * Demonstrates creating threads with a factory for consistent naming and config.
     */
    static class Example3_ThreadFactory {
        static class NamedThreadFactory implements java.util.concurrent.ThreadFactory {
            private final String prefix;
            private int count = 0;

            NamedThreadFactory(String prefix) {
                this.prefix = prefix;
            }

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, prefix + "-" + (++count));
            }
        }

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 3: Thread Factory Pattern");
            System.out.println("=================================");

            NamedThreadFactory factory = new NamedThreadFactory("Worker");

            Thread[] threads = new Thread[5];
            for (int i = 0; i < 5; i++) {
                threads[i] = factory.newThread(() -> {
                    System.out.println("  " + Thread.currentThread().getName() + " running");
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            System.out.println("  All factory-created threads completed.");
            System.out.println();
        }
    }

    /**
     * Example 4: Virtual Threads (Java 21+)
     * Demonstrates creating millions of lightweight virtual threads.
     */
    static class Example4_VirtualThreads {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 4: Virtual Threads (Java 21+)");
            System.out.println("=======================================");

            long start = System.currentTimeMillis();

            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < 10000; i++) {
                    final int taskId = i;
                    executor.submit(() -> {
                        if (taskId % 2500 == 0) {
                            System.out.println("  Virtual task " + taskId + " on " +
                                Thread.currentThread().getName());
                        }
                        try { Thread.sleep(10); } catch (InterruptedException e) { return; }
                    });
                }
            }

            long elapsed = System.currentTimeMillis() - start;
            System.out.println("  10,000 virtual threads completed in " + elapsed + "ms");
            System.out.println();
        }
    }

    /**
     * Example 5: CompletableFuture Composition
     * Demonstrates composing async operations without blocking.
     */
    static class Example5_CompletableFuture {
        public static void main(String[] args) {
            System.out.println("Example 5: CompletableFuture Composition");
            System.out.println("=========================================");

            java.util.concurrent.CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("  Step 1: Fetching user ID");
                    return 42;
                })
                .thenApply(userId -> {
                    System.out.println("  Step 2: Fetching user for ID " + userId);
                    return "Alice";
                })
                .thenApply(userName -> {
                    System.out.println("  Step 3: Processing for " + userName);
                    return "Welcome, " + userName + "!";
                })
                .thenAccept(message -> {
                    System.out.println("  Step 4: " + message);
                })
                .join();

            System.out.println("  Pipeline completed.");
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Example1_ThreeWaysToCreate.main(args);
        Example2_CallableWithFuture.main(args);
        Example3_ThreadFactory.main(args);
        Example4_VirtualThreads.main(args);
        Example5_CompletableFuture.main(args);
    }
}
