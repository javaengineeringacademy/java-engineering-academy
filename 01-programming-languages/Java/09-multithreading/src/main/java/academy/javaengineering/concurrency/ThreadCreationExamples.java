package academy.javaengineering.concurrency;

/**
 * Demonstrates various thread creation patterns in Java 21.
 *
 * <p>This class shows different approaches to creating threads including
 * Runnable, lambda expressions, daemon threads, and Callable with Future.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Thread creation with Runnable interface</li>
 *   <li>Lambda-based thread creation</li>
 *   <li>Daemon threads for background tasks</li>
 *   <li>Callable with Future for result-returning tasks</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ThreadCreationExamples {

    public static void main(String[] args) {
        createThreadWithRunnable();
        createThreadWithLambda();
        createDaemonThread();
        createThreadWithCallable();
        createMultipleThreads();
    }

    /**
     * Creates a thread using the Runnable interface.
     */
    public static void createThreadWithRunnable() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread with Runnable: " + Thread.currentThread().getName());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        }
        // Expected output: Thread with Runnable: Thread-0
    }

    /**
     * Creates a thread using a lambda expression.
     */
    public static void createThreadWithLambda() {
        Thread thread = new Thread(() -> {
            System.out.println("Thread with Lambda: " + Thread.currentThread().getName());
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        }
        // Expected output: Thread with Lambda: Thread-1
    }

    /**
     * Creates a daemon thread that runs in the background.
     */
    public static void createDaemonThread() {
        Thread daemonThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Daemon thread iteration: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Expected output: Daemon thread iteration: 0, 1, 2 (may vary)
    }

    /**
     * Creates a thread using Callable to return a result.
     */
    public static void createThreadWithCallable() {
        java.util.concurrent.Callable<String> callable = () -> {
            return "Result from Callable: " + Thread.currentThread().getName();
        };

        java.util.concurrent.ExecutorService executor =
                java.util.concurrent.Executors.newSingleThreadExecutor();
        java.util.concurrent.Future<String> future = executor.submit(callable);

        try {
            String result = future.get();
            System.out.println(result);
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
        }
        executor.shutdown();
        // Expected output: Result from Callable: pool-1-thread-1
    }

    /**
     * Creates multiple threads to demonstrate concurrent execution.
     */
    public static void createMultipleThreads() {
        for (int i = 0; i < 3; i++) {
            final int threadNum = i;
            Thread thread = new Thread(() -> {
                System.out.println("Thread " + threadNum + " started");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Thread " + threadNum + " finished");
            });
            thread.start();
        }
        // Expected output: Threads start and finish in interleaved order
    }
}
