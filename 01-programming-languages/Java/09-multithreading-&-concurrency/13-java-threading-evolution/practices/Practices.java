package academy.javaengineering.concurrency.evolution.practices;

/**
 * Threading Evolution Practices
 * 
 * Complete these exercises to master Java threading evolution concepts.
 * Each exercise focuses on a specific aspect of thread scheduling and evolution.
 */
public class Practices {

    /**
     * Exercise 1: Green Thread Simulation
     * 
     * Create a simple green thread simulator that:
     * - Accepts multiple Runnable tasks
     * - Executes them sequentially on a single thread
     * - Tracks execution order and timing
     * - Demonstrates cooperative scheduling (tasks yield at points)
     * 
     * Requirements:
     * - Use a single OS thread to run multiple "virtual" tasks
     * - Implement a simple scheduler that rotates through tasks
     * - Each task should yield after a certain number of iterations
     * - Print execution trace showing context switches
     */
    public static void exercise1_GreenThreadSimulation() {
        System.out.println("=== Exercise 1: Green Thread Simulation ===\n");

        // TODO: Implement green thread simulator
        // 1. Create a class GreenThreadScheduler
        // 2. Add method submit(Runnable task)
        // 3. Add method start() to begin execution
        // 4. Implement round-robin scheduling
        // 5. Track and print context switches

        System.out.println("TODO: Implement green thread simulation");
        System.out.println("Hint: Use a single thread with a queue of tasks");
        System.out.println("Each task runs for a time slice then yields\n");
    }

    /**
     * Exercise 2: Daemon Thread Manager
     * 
     * Create a daemon thread manager that:
     * - Manages a pool of daemon threads for background tasks
     * - Automatically restarts failed daemon threads
     * - Provides monitoring of daemon thread health
     * - Gracefully shuts down all daemon threads
     * 
     * Requirements:
     * - Use setDaemon(true) before start()
     * - Implement thread monitoring
     * - Handle thread failures gracefully
     * - Provide shutdown hook for cleanup
     */
    public static void exercise2_DaemonThreadManager() {
        System.out.println("=== Exercise 2: Daemon Thread Manager ===\n");

        // TODO: Implement daemon thread manager
        // 1. Create a class DaemonThreadManager
        // 2. Add method submitDaemon(Runnable task)
        // 3. Monitor daemon thread health
        // 4. Restart failed threads automatically
        // 5. Implement graceful shutdown

        System.out.println("TODO: Implement daemon thread manager");
        System.out.println("Hint: Use Thread.setDaemon(true) and monitor thread states");
        System.out.println("Remember: JVM exits when only daemon threads remain\n");
    }

    /**
     * Exercise 3: Virtual Thread Executor
     * 
     * Create a virtual thread executor that:
     * - Creates and manages virtual threads (Java 21+)
     * - Handles both I/O-bound and CPU-bound tasks
     * - Implements task prioritization
     * - Provides metrics on thread usage
     * 
     * Requirements:
     * - Use Executors.newVirtualThreadPerTaskExecutor()
     * - Handle pinning issues (use ReentrantLock instead of synchronized)
     * - Implement task queue with priorities
     * - Collect execution metrics
     */
    public static void exercise3_VirtualThreadExecutor() {
        System.out.println("=== Exercise 3: Virtual Thread Executor ===\n");

        // TODO: Implement virtual thread executor
        // 1. Create a class VirtualThreadExecutor
        // 2. Use ExecutorService with virtual threads
        // 3. Implement task prioritization
        // 4. Avoid pinning (use ReentrantLock)
        // 5. Collect and report metrics

        System.out.println("TODO: Implement virtual thread executor");
        System.out.println("Hint: Use Executors.newVirtualThreadPerTaskExecutor()");
        System.out.println("Remember: Avoid synchronized blocks with blocking I/O\n");
    }

    /**
     * Exercise 4: Thread Scheduler Visualizer
     * 
     * Create a scheduler visualizer that:
     * - Shows how threads are scheduled on CPU cores
     * - Visualizes time slicing behavior
     * - Demonstrates priority scheduling
     * - Shows context switch overhead
     * 
     * Requirements:
     * - Create ASCII visualization of thread execution
     * - Show thread states over time
     * - Demonstrate preemption behavior
     * - Measure and display scheduling metrics
     */
    public static void exercise4_ThreadSchedulerVisualizer() {
        System.out.println("=== Exercise 4: Thread Scheduler Visualizer ===\n");

        // TODO: Implement scheduler visualizer
        // 1. Create a class SchedulerVisualizer
        // 2. Track thread execution on cores
        // 3. Create timeline visualization
        // 4. Show context switches
        // 5. Display scheduling statistics

        System.out.println("TODO: Implement scheduler visualizer");
        System.out.println("Hint: Track thread start/end times and CPU core assignments");
        System.out.println("Create ASCII art showing thread execution over time\n");
    }

    /**
     * Exercise 5: Hybrid Thread Pool
     * 
     * Create a hybrid thread pool that:
     * - Uses platform threads for CPU-bound tasks
     * - Uses virtual threads for I/O-bound tasks
     * - Automatically routes tasks to appropriate thread type
     * - Provides unified API for task submission
     * 
     * Requirements:
     * - Detect task type (CPU-bound vs I/O-bound)
     * - Route to appropriate executor
     * - Handle mixed workloads efficiently
     * - Provide performance comparison metrics
     */
    public static void exercise5_HybridThreadPool() {
        System.out.println("=== Exercise 5: Hybrid Thread Pool ===\n");

        // TODO: Implement hybrid thread pool
        // 1. Create a class HybridThreadPool
        // 2. Maintain both platform and virtual thread executors
        // 3. Implement task routing logic
        // 4. Collect metrics for comparison
        // 5. Provide unified submission API

        System.out.println("TODO: Implement hybrid thread pool");
        System.out.println("Hint: Use Executors.newFixedThreadPool() for CPU-bound");
        System.out.println("Use Executors.newVirtualThreadPerTaskExecutor() for I/O-bound\n");
    }

    /**
     * Main method to run all exercises
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         THREADING EVOLUTION PRACTICES            ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        exercise1_GreenThreadSimulation();
        exercise2_DaemonThreadManager();
        exercise3_VirtualThreadExecutor();
        exercise4_ThreadSchedulerVisualizer();
        exercise5_HybridThreadPool();

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         COMPLETED ALL EXERCISES                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}
