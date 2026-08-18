package academy.javaengineering.concurrency.evolution;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.Subtask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Virtual Thread Examples (Java 21+)
 * 
 * Demonstrates:
 * - Creating virtual threads
 * - Virtual thread behavior
 * - Pinning problem and solutions
 * - Structured concurrency
 * - Scoped values
 */
public class VirtualThreadExample {

    private static final AtomicInteger completedTasks = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("=== VIRTUAL THREAD EXAMPLES (Java 21+) ===\n");

        demonstrateVirtualThreadCreation();
        demonstrateVirtualThreadBehavior();
        demonstratePinningProblem();
        demonstrateStructuredConcurrency();
        demonstrateScopedValues();
    }

    /**
     * Shows different ways to create virtual threads
     */
    private static void demonstrateVirtualThreadCreation() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         VIRTUAL THREAD CREATION                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // Method 1: Thread.ofVirtual()
        System.out.println("Method 1: Thread.ofVirtual()");
        Thread vt1 = Thread.ofVirtual()
            .name("virtual-thread-1")
            .start(() -> {
                System.out.println("  [VT1] Running in virtual thread");
                System.out.println("  [VT1] Is virtual: " + Thread.currentThread().isVirtual());
            });

        // Method 2: Thread.Builder
        System.out.println("\nMethod 2: Thread.Builder");
        Thread vt2 = Thread.ofVirtual()
            .name("worker-", 0)
            .start(() -> {
                System.out.println("  [VT2] Running in virtual thread");
                System.out.println("  [VT2] Thread name: " + Thread.currentThread().getName());
            });

        // Method 3: Thread.startVirtualThread()
        System.out.println("\nMethod 3: Thread.startVirtualThread()");
        Thread vt3 = Thread.startVirtualThread(() -> {
            System.out.println("  [VT3] Running in virtual thread");
            System.out.println("  [VT3] Is virtual: " + Thread.currentThread().isVirtual());
        });

        // Method 4: try-with-resources
        System.out.println("\nMethod 4: try-with-resources");
        try (var vt4 = Thread.ofVirtual().name("auto-close").start(() -> {
            System.out.println("  [VT4] Running in virtual thread");
            System.out.println("  [VT4] Will auto-close when scope exits");
        })) {
            vt4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Method 5: ExecutorService with virtual threads
        System.out.println("\nMethod 5: ExecutorService (recommended for bulk)");
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> future = executor.submit(() -> {
                return "Result from virtual thread";
            });

            System.out.println("  [Executor] " + future.getNow("pending"));
        } catch (Exception e) {
            System.out.println("  [Executor] Error: " + e.getMessage());
        }

        // Wait for all threads
        try {
            vt1.join();
            vt2.join();
            vt3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- Key Points ---");
        System.out.println("• Virtual threads are managed by JVM, not OS");
        System.out.println("• Millions can be created (vs thousands for platform)");
        System.out.println("• Same API as regular threads");
        System.out.println("• Use ExecutorService for bulk operations");
    }

    /**
     * Demonstrates virtual thread behavior and benefits
     */
    private static void demonstrateVirtualThreadBehavior() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         VIRTUAL THREAD BEHAVIOR                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        int taskCount = 1000;
        System.out.println("Creating " + taskCount + " virtual threads for I/O-bound work...\n");

        long startTime = System.nanoTime();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, taskCount).forEach(i -> {
                executor.submit(() -> {
                    try {
                        // Simulate I/O operation
                        Thread.sleep(Duration.ofMillis(10));
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;

        System.out.println("  Completed " + completedTasks.get() + " tasks");
        System.out.println("  Total time: " + duration + "ms");
        System.out.println("  Average per task: " + (duration * 1000.0 / taskCount / 1000) + "ms\n");

        System.out.println("Virtual Thread Benefits:");
        System.out.println("  ✓ Lightweight (~1KB vs ~1MB for platform threads)");
        System.out.println("  ✓ Blocking doesn't waste carrier threads");
        System.out.println("  ✓ Millions of threads possible");
        System.out.println("  ✓ Perfect for I/O-bound workloads");

        // Show memory comparison
        System.out.println("\n--- Memory Comparison ---");
        System.out.println("  Platform thread: ~1MB stack space");
        System.out.println("  Virtual thread: ~1KB (1000x less)");
        System.out.println("  10,000 platform threads: ~10GB");
        System.out.println("  10,000 virtual threads: ~10MB");
    }

    /**
     * Demonstrates the pinning problem and solutions
     */
    private static void demonstratePinningProblem() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         PINNING PROBLEM & SOLUTIONS              ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Pinning occurs when virtual thread holds:");
        System.out.println("  1. Native (JNI) method");
        System.out.println("  2. synchronized block during blocking I/O\n");

        // Demonstrate pinning with synchronized
        System.out.println("--- Example: synchronized causes pinning ---\n");

        Object lock = new Object();
        int threadCount = 10;

        long startTime = System.nanoTime();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, threadCount).forEach(i -> {
                executor.submit(() -> {
                    synchronized (lock) {  // This PINs the virtual thread!
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("  Time with synchronized: " + duration + "ms (pinned)");

        // Demonstrate solution with ReentrantLock
        System.out.println("\n--- Solution: Use ReentrantLock ---\n");

        ReentrantLock reentrantLock = new ReentrantLock();
        startTime = System.nanoTime();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, threadCount).forEach(i -> {
                executor.submit(() -> {
                    reentrantLock.lock();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        reentrantLock.unlock();
                    }
                });
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1_000_000;
        System.out.println("  Time with ReentrantLock: " + duration + "ms (not pinned)\n");

        System.out.println("Key Takeaways:");
        System.out.println("  • synchronized pins virtual threads during blocking");
        System.out.println("  • ReentrantLock allows virtual thread to unmount");
        System.out.println("  • Use -Djdk.tracePinnedThreads=full to detect pinning");
        System.out.println("  • Migrate to ReentrantLock for virtual thread code");
    }

    /**
     * Demonstrates structured concurrency
     */
    private static void demonstrateStructuredConcurrency() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         STRUCTURED CONCURRENCY                    ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("Structured concurrency ensures child tasks complete");
        System.out.println("before parent scope exits.\n");

        System.out.println("--- Example: Fetching user, order, and receipt ---\n");

        long startTime = System.nanoTime();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> user = scope.fork(() -> {
                Thread.sleep(50);
                return "User: John Doe";
            });

            Subtask<String> order = scope.fork(() -> {
                Thread.sleep(30);
                return "Order: #12345";
            });

            Subtask<String> receipt = scope.fork(() -> {
                Thread.sleep(40);
                return "Receipt: PDF generated";
            });

            scope.join();            // Wait for all
            scope.throwIfFailed();   // Propagate errors

            // All results available here
            System.out.println("  [Result] " + user.get());
            System.out.println("  [Result] " + order.get());
            System.out.println("  [Result] " + receipt.get());

        } catch (Exception e) {
            System.out.println("  [Error] " + e.getMessage());
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("\n  Total time: " + duration + "ms (parallel execution)");
        System.out.println("  (Would be ~120ms if sequential: 50+30+40)");

        System.out.println("\n--- ShutdownOnFailure Behavior ---");
        System.out.println("  • If ANY child task fails, all others are cancelled");
        System.out.println("  • Parent receives the first failure");
        System.out.println("  • Guarantees cleanup on scope exit");
    }

    /**
     * Demonstrates scoped values (replacement for ThreadLocal)
     */
    private static void demonstrateScopedValues() {
        System.out.println("\n\n╔══════════════════════════════════════════════════╗");
        System.out.println("║         SCOPED VALUES (ThreadLocal Alternative)  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("ScopedValue provides structured, auto-cleaned thread-local storage.\n");

        // Simulating scoped value usage
        System.out.println("--- Scoped Value Benefits ---\n");

        System.out.println("  ThreadLocal Problems:");
        System.out.println("    • Memory leaks if not cleaned up");
        System.out.println("    • Thread pool reuse causes stale values");
        System.out.println("    • Complex lifecycle management");
        System.out.println("    • Not compatible with virtual threads\n");

        System.out.println("  ScopedValue Solutions:");
        System.out.println("    • Automatically cleaned when scope exits");
        System.out.println("    • No memory leaks");
        System.out.println("    • Works perfectly with virtual threads");
        System.out.println("    • Read-only by default (safer)\n");

        System.out.println("--- Code Comparison ---\n");

        System.out.println("  // ThreadLocal (old way)");
        System.out.println("  private static final ThreadLocal<User> currentUser = new ThreadLocal<>();");
        System.out.println("  currentUser.set(user);  // Must remember to clean up!");
        System.out.println("  try {");
        System.out.println("      handleRequest();");
        System.out.println("  } finally {");
        System.out.println("      currentUser.remove();  // Easy to forget!");
        System.out.println("  }\n");

        System.out.println("  // ScopedValue (new way)");
        System.out.println("  private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();");
        System.out.println("  ScopedValue.where(CURRENT_USER, user).run(() -> {");
        System.out.println("      handleRequest();  // CURRENT_USER accessible here");
        System.out.println("  });  // Automatically cleaned up!\n");

        System.out.println("Key Points:");
        System.out.println("  • ScopedValue is read-only (no set() method)");
        System.out.println("  • Values are scoped to the run() block");
        System.out.println("  • Perfect for virtual thread architectures");
        System.out.println("  • Available as preview in Java 21, stable in later versions");
    }
}
