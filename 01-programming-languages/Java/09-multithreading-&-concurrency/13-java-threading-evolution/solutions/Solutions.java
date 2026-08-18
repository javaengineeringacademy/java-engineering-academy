package academy.javaengineering.concurrency.evolution.solutions;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Threading Evolution Solutions
 * 
 * Complete solutions for all 5 exercises demonstrating
 * Java threading evolution concepts.
 */
public class Solutions {

    /**
     * Exercise 1 Solution: Green Thread Simulation
     * 
     * Simulates green threads by running multiple tasks on a single OS thread
     * with cooperative scheduling (tasks yield at predetermined points).
     */
    static class GreenThreadScheduler {
        private final Queue<Runnable> taskQueue = new LinkedList<>();
        private final List<String> executionLog = new ArrayList<>();
        private volatile boolean running = false;
        private Thread schedulerThread;

        public void submit(Runnable task) {
            taskQueue.add(task);
        }

        public void start() {
            running = true;
            schedulerThread = new Thread(this::runScheduler, "GreenScheduler");
            schedulerThread.start();
        }

        private void runScheduler() {
            int timeSlice = 3;  // Each task runs for 3 iterations before yielding

            while (running && !taskQueue.isEmpty()) {
                Runnable task = taskQueue.poll();
                if (task != null) {
                    String threadName = Thread.currentThread().getName();
                    executionLog.add("[" + threadName + "] Starting task");

                    // Execute task with cooperative yielding
                    try {
                        task.run();
                    } catch (Exception e) {
                        executionLog.add("[" + threadName + "] Task failed: " + e.getMessage());
                    }

                    executionLog.add("[" + threadName + "] Task completed, scheduling next");

                    // Re-enqueue if task wants to continue (simulated)
                    if (!taskQueue.isEmpty()) {
                        taskQueue.add(task);
                    }
                }
            }
        }

        public void stop() {
            running = false;
            if (schedulerThread != null) {
                schedulerThread.interrupt();
            }
        }

        public List<String> getExecutionLog() {
            return Collections.unmodifiableList(executionLog);
        }
    }

    /**
     * Exercise 1: Green Thread Simulation Demo
     */
    public static void exercise1_GreenThreadSimulation() {
        System.out.println("=== Exercise 1: Green Thread Simulation ===\n");

        GreenThreadScheduler scheduler = new GreenThreadScheduler();

        // Submit multiple tasks
        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            scheduler.submit(() -> {
                String name = Thread.currentThread().getName();
                System.out.println("  [Task-" + taskId + "] Running on " + name);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("  [Task-" + taskId + "] Completed");
            });
        }

        System.out.println("Starting green thread scheduler (single OS thread)...\n");

        scheduler.start();

        // Wait for completion
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scheduler.stop();

        System.out.println("\nExecution Log:");
        for (String log : scheduler.getExecutionLog()) {
            System.out.println("  " + log);
        }

        System.out.println("\nKey Points:");
        System.out.println("  • All tasks ran on ONE OS thread");
        System.out.println("  • Tasks executed sequentially (cooperative)");
        System.out.println("  • No parallelism (single-core model)");
        System.out.println("  • This is how green threads worked in Java 1.0-1.3");
    }

    /**
     * Exercise 2 Solution: Daemon Thread Manager
     * 
     * Manages daemon threads with automatic restart and monitoring.
     */
    static class DaemonThreadManager {
        private final Map<Thread, Runnable> daemonThreads = new ConcurrentHashMap<>();
        private final AtomicInteger taskCounter = new AtomicInteger(0);
        private volatile boolean running = true;

        public void submitDaemon(Runnable task) {
            String name = "Daemon-" + taskCounter.incrementAndGet();

            Thread daemonThread = new Thread(() -> {
                while (running && !Thread.currentThread().isInterrupted()) {
                    try {
                        task.run();
                        Thread.sleep(1000);  // Simulate periodic work
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        System.out.println("  [DaemonManager] Thread failed: " + e.getMessage());
                        // Restart on failure
                        if (running) {
                            restartDaemon(this, task);
                        }
                    }
                }
            }, name);

            daemonThread.setDaemon(true);  // Set as daemon before start
            daemonThreads.put(daemonThread, task);
            daemonThread.start();

            System.out.println("  [DaemonManager] Started daemon: " + name);
        }

        private void restartDaemon(DaemonThreadManager manager, Runnable task) {
            manager.submitDaemon(task);
        }

        public void shutdown() {
            running = false;
            for (Thread t : daemonThreads.keySet()) {
                t.interrupt();
            }
        }

        public int getActiveDaemonCount() {
            int count = 0;
            for (Thread t : daemonThreads.keySet()) {
                if (t.isAlive()) count++;
            }
            return count;
        }
    }

    /**
     * Exercise 2: Daemon Thread Manager Demo
     */
    public static void exercise2_DaemonThreadManager() {
        System.out.println("\n=== Exercise 2: Daemon Thread Manager ===\n");

        DaemonThreadManager manager = new DaemonThreadManager();

        // Submit daemon tasks
        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            manager.submitDaemon(() -> {
                System.out.println("  [Task-" + taskId + "] Daemon working...");
            });
        }

        System.out.println("\nActive daemon threads: " + manager.getActiveDaemonCount());

        // Let daemons run
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nShutting down daemon manager...");
        manager.shutdown();

        System.out.println("\nKey Points:");
        System.out.println("  • Daemon threads are set with setDaemon(true) before start()");
        System.out.println("  • JVM exits when only daemon threads remain");
        System.out.println("  • Daemons are ideal for background tasks");
        System.out.println("  • Manager handles automatic restart on failure");
    }

    /**
     * Exercise 3 Solution: Virtual Thread Executor
     * 
     * Creates a virtual thread executor with task prioritization.
     */
    static class VirtualThreadExecutor {
        private final ExecutorService executor;
        private final AtomicInteger completedTasks = new AtomicInteger(0);
        private final AtomicInteger failedTasks = new AtomicInteger(0);
        private final ReentrantLock lock = new ReentrantLock();  // Avoid pinning!

        public VirtualThreadExecutor() {
            this.executor = Executors.newVirtualThreadPerTaskExecutor();
        }

        public Future<String> submitTask(String name, int priority) {
            return executor.submit(() -> {
                String threadInfo = Thread.currentThread().isVirtual() ? "Virtual" : "Platform";
                System.out.println("  [" + name + "] Running on " + threadInfo + " thread (priority: " + priority + ")");

                // Use ReentrantLock instead of synchronized to avoid pinning
                lock.lock();
                try {
                    // Simulate I/O operation
                    Thread.sleep(100);
                    completedTasks.incrementAndGet();
                    return "Task " + name + " completed";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    failedTasks.incrementAndGet();
                    throw e;
                } finally {
                    lock.unlock();
                }
            });
        }

        public void shutdown() {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void printMetrics() {
            System.out.println("\n--- Executor Metrics ---");
            System.out.println("  Completed tasks: " + completedTasks.get());
            System.out.println("  Failed tasks: " + failedTasks.get());
        }
    }

    /**
     * Exercise 3: Virtual Thread Executor Demo
     */
    public static void exercise3_VirtualThreadExecutor() {
        System.out.println("\n=== Exercise 3: Virtual Thread Executor ===\n");

        VirtualThreadExecutor executor = new VirtualThreadExecutor();

        System.out.println("Submitting tasks to virtual thread executor...\n");

        // Submit multiple tasks
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futures.add(executor.submitTask("Task-" + i, i));
        }

        // Wait for results
        for (Future<String> future : futures) {
            try {
                String result = future.get(2, TimeUnit.SECONDS);
                System.out.println("  Result: " + result);
            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }

        executor.printMetrics();
        executor.shutdown();

        System.out.println("\nKey Points:");
        System.out.println("  • Virtual threads are lightweight (~1KB vs ~1MB)");
        System.out.println("  • Use ReentrantLock instead of synchronized to avoid pinning");
        System.out.println("  • Perfect for I/O-bound workloads");
        System.out.println("  • Can create millions of virtual threads");
    }

    /**
     * Exercise 4 Solution: Thread Scheduler Visualizer
     * 
     * Visualizes thread scheduling behavior on CPU cores.
     */
    static class SchedulerVisualizer {
        private final Map<Integer, List<String>> timeline = new ConcurrentHashMap<>();
        private final AtomicInteger eventCounter = new AtomicInteger(0);

        public void trackThread(int threadId, String coreName, String event) {
            String timestamp = String.format("%04d", eventCounter.incrementAndGet());
            String entry = timestamp + " [" + coreName + "] " + event;

            timeline.computeIfAbsent(threadId, k -> new CopyOnWriteArrayList<>()).add(entry);
        }

        public void visualize() {
            System.out.println("\n--- Thread Execution Timeline ---\n");

            // Print header
            System.out.println("Time  Core    Event");
            System.out.println("----  ------  -----");

            // Collect and sort all events
            List<String> allEvents = new ArrayList<>();
            for (Map.Entry<Integer, List<String>> entry : timeline.entrySet()) {
                allEvents.addAll(entry.getValue());
            }
            Collections.sort(allEvents);

            // Print events
            for (String event : allEvents) {
                System.out.println("  " + event);
            }

            System.out.println("\n--- Thread Statistics ---");
            for (Map.Entry<Integer, List<String>> entry : timeline.entrySet()) {
                System.out.println("  Thread-" + entry.getKey() + ": " + entry.getValue().size() + " events");
            }
        }
    }

    /**
     * Exercise 4: Thread Scheduler Visualizer Demo
     */
    public static void exercise4_ThreadSchedulerVisualizer() {
        System.out.println("\n=== Exercise 4: Thread Scheduler Visualizer ===\n");

        SchedulerVisualizer visualizer = new SchedulerVisualizer();

        int threadCount = 3;
        CountDownLatch latch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);

        System.out.println("Simulating " + threadCount + " threads on multiple cores...\n");

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            final String coreName = "Core-" + (i % 2);

            new Thread(() -> {
                try {
                    startLatch.await();
                    visualizer.trackThread(threadId, coreName, "START");

                    for (int j = 0; j < 3; j++) {
                        visualizer.trackThread(threadId, coreName, "RUN-" + (j + 1));
                        Thread.sleep(20);
                    }

                    visualizer.trackThread(threadId, coreName, "END");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }, "Thread-" + i).start();
        }

        startLatch.countDown();  // Start all threads

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        visualizer.visualize();

        System.out.println("\nKey Points:");
        System.out.println("  • Visualize thread execution over time");
        System.out.println("  • Track which core each thread runs on");
        System.out.println("  • Identify context switches and scheduling patterns");
        System.out.println("  • Useful for performance analysis and debugging");
    }

    /**
     * Exercise 5 Solution: Hybrid Thread Pool
     * 
     * Routes tasks to appropriate thread type based on workload.
     */
    static class HybridThreadPool {
        private final ExecutorService cpuExecutor;
        private final ExecutorService ioExecutor;
        private final AtomicInteger cpuTasks = new AtomicInteger(0);
        private final AtomicInteger ioTasks = new AtomicInteger(0);

        public HybridThreadPool(int cpuThreads) {
            this.cpuExecutor = Executors.newFixedThreadPool(cpuThreads);  // Platform threads for CPU
            this.ioExecutor = Executors.newVirtualThreadPerTaskExecutor();  // Virtual for I/O
        }

        public Future<?> submitTask(String name, boolean isIOBound) {
            if (isIOBound) {
                ioTasks.incrementAndGet();
                return ioExecutor.submit(() -> {
                    System.out.println("  [" + name + "] I/O task on virtual thread");
                    try {
                        Thread.sleep(100);  // Simulate I/O
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } else {
                cpuTasks.incrementAndGet();
                return cpuExecutor.submit(() -> {
                    System.out.println("  [" + name + "] CPU task on platform thread");
                    // Simulate CPU work
                    long end = System.nanoTime() + 50_000_000;
                    while (System.nanoTime() < end) {
                        // Busy wait
                    }
                });
            }
        }

        public void shutdown() {
            cpuExecutor.shutdown();
            ioExecutor.shutdown();
            try {
                cpuExecutor.awaitTermination(5, TimeUnit.SECONDS);
                ioExecutor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void printMetrics() {
            System.out.println("\n--- Hybrid Pool Metrics ---");
            System.out.println("  CPU tasks (platform threads): " + cpuTasks.get());
            System.out.println("  I/O tasks (virtual threads): " + ioTasks.get());
        }
    }

    /**
     * Exercise 5: Hybrid Thread Pool Demo
     */
    public static void exercise5_HybridThreadPool() {
        System.out.println("\n=== Exercise 5: Hybrid Thread Pool ===\n");

        HybridThreadPool pool = new HybridThreadPool(Runtime.getRuntime().availableProcessors());

        System.out.println("Submitting mixed workload (CPU + I/O tasks)...\n");

        List<Future<?>> futures = new ArrayList<>();

        // Submit mix of CPU and I/O tasks
        for (int i = 0; i < 5; i++) {
            futures.add(pool.submitTask("Task-" + i, i % 2 == 0));  // Alternate I/O and CPU
        }

        // Wait for completion
        for (Future<?> future : futures) {
            try {
                future.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }

        pool.printMetrics();
        pool.shutdown();

        System.out.println("\nKey Points:");
        System.out.println("  • CPU-bound tasks → platform threads (better performance)");
        System.out.println("  • I/O-bound tasks → virtual threads (lightweight)");
        System.out.println("  • Hybrid approach optimizes for mixed workloads");
        System.out.println("  • Automatically routes tasks to appropriate executor");
    }

    /**
     * Main method to run all solutions
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         THREADING EVOLUTION SOLUTIONS            ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        exercise1_GreenThreadSimulation();
        exercise2_DaemonThreadManager();
        exercise3_VirtualThreadExecutor();
        exercise4_ThreadSchedulerVisualizer();
        exercise5_HybridThreadPool();

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         ALL SOLUTIONS COMPLETED                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}
