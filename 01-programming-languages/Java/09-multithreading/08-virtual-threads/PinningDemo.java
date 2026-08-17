import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Virtual Thread Pinning Demo (Java 21)
 *
 * Virtual threads can become "pinned" to carrier (platform) threads when:
 * 1. Executing a synchronized block/method
 * 2. Executing a native method (JNI)
 *
 * When pinned, the virtual thread occupies a carrier thread, reducing
 * concurrency. Use ReentrantLock instead of synchronized to avoid pinning.
 *
 * Expected output:
 * === Thread Pinning with synchronized ===
 * synchronized: pinned ~30 carrier threads (bad for concurrency)
 *
 * === Thread Pinning with ReentrantLock ===
 * ReentrantLock: no pinning, all virtual threads run smoothly
 *
 * === Pinning Detection ===
 * Enable -Djdk.tracePinnedThreads=short to detect pinning
 */
public class PinningDemo {

    private static final AtomicInteger pinCount = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        synchronizedPinning();
        reentrantLockNoPinning();
        pinningDetection();
    }

    // =========================================================
    // 1. SYNCHRONIZED PINNING (BAD)
    // =========================================================
    static void synchronizedPinning() throws Exception {
        System.out.println("=== Thread Pinning with synchronized ===\n");

        // --- Before Java 21: synchronized was fine with platform threads ---
        // synchronized (lock) {
        //     // Only one platform thread at a time - no issue
        // }

        // --- With Java 21: synchronized PINNING with virtual threads ---
        // When a virtual thread enters a synchronized block, it "pins"
        // to the carrier thread, blocking it even during I/O or sleep

        Object lock = new Object();
        int taskCount = 100;
        long sleepMs = 50;

        Instant start = java.time.Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    synchronized (lock) { // PINNING! Virtual thread stuck on carrier
                        try {
                            Thread.sleep(sleepMs); // This blocks the carrier thread
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
            }
        }

        Duration elapsed = Duration.between(start, java.time.Instant.now());
        System.out.println("With synchronized + sleep(" + sleepMs + "ms):");
        System.out.println("  Time: " + elapsed.toMillis() + "ms");
        System.out.println("  Expected: ~" + (taskCount * sleepMs) + "ms if serial (pinning causes this)");
        System.out.println("  The sleep inside synchronized pins the virtual thread!\n");
    }

    // =========================================================
    // 2. REENTRANT LOCK - NO PINNING (GOOD)
    // =========================================================
    static void reentrantLockNoPinning() throws Exception {
        System.out.println("=== Thread Pinning with ReentrantLock ===\n");

        // --- Before Java 21: ReentrantLock vs synchronized ---
        // ReentrantLock was generally preferred for complex locking
        // Now it's also preferred for virtual thread compatibility

        // --- With Java 21: ReentrantLock does NOT pin virtual threads ---
        java.util.concurrent.locks.ReentrantLock lock = new java.util.concurrent.locks.ReentrantLock();
        int taskCount = 100;
        long sleepMs = 50;

        Instant start = java.time.Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    lock.lock(); // No pinning! Virtual thread can unmount
                    try {
                        Thread.sleep(sleepMs); // Virtual thread yields carrier during sleep
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                });
            }
        }

        Duration elapsed = Duration.between(start, java.time.Instant.now());
        System.out.println("With ReentrantLock + sleep(" + sleepMs + "ms):");
        System.out.println("  Time: " + elapsed.toMillis() + "ms");
        System.out.println("  Expected: ~" + sleepMs + "ms (all run concurrently)");
        System.out.println("  ReentrantLock does NOT pin virtual threads!\n");
    }

    // =========================================================
    // 3. PINNING DETECTION
    // =========================================================
    static void pinningDetection() {
        System.out.println("=== Pinning Detection ===\n");

        System.out.println("To detect pinning in your application:");
        System.out.println("");
        System.out.println("1. JVM flag (prints stack traces of pinned threads):");
        System.out.println("   java -Djdk.tracePinnedThreads=short YourApp");
        System.out.println("   java -Djdk.tracePinnedThreads=full YourApp");
        System.out.println("");
        System.out.println("2. Common pinning causes:");
        System.out.println("   - synchronized blocks/methods");
        System.out.println("   - Native methods (JNI)");
        System.out.println("   - Certain JDK internals (e.g., some FileChannel ops)");
        System.out.println("");
        System.out.println("3. Fixes for pinning:");
        System.out.println("   - Replace synchronized with ReentrantLock");
        System.out.println("   - Use java.nio (non-blocking I/O) instead of blocking I/O");
        System.out.println("   - Avoid native methods in virtual thread hot paths");
        System.out.println("");
        System.out.println("4. Quick reference:");
        System.out.println("   synchronized (lock) { ... }  // BAD - pins");
        System.out.println("   lock.lock();                  // GOOD - no pin");
        System.out.println("   try { ... } finally { lock.unlock(); }");
        System.out.println();
    }
}
