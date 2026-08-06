package academy.javaengineering.senior.java21;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualThreadsAdvancedDemo {

    static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {
        structuredTaskScopeDemo();
        scopedValueDemo();
        virtualThreadPinningDetection();
    }

    // ==================== StructuredTaskScope ====================

    static void structuredTaskScopeDemo() throws Exception {
        System.out.println("=== StructuredTaskScope ===\n");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> user = scope.fork(() -> fetchUser(1));
            Subtask<Integer> score = scope.fork(() -> fetchScore(1));
            Subtask<String> prefs = scope.fork(() -> fetchPreferences(1));

            scope.join();

            System.out.println("User: " + user.get());
            System.out.println("Score: " + score.get());
            System.out.println("Prefs: " + prefs.get());
        }

        System.out.println("\n--- ShutdownOnSuccess ---\n");

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            scope.fork(() -> fetchFromSource1(42));
            scope.fork(() -> fetchFromSource2(42));
            scope.fork(() -> fetchFromSource3(42));

            scope.join();

            System.out.println("Winner: " + scope.get());
        }

        System.out.println("\n--- Parallel processing ---\n");

        record ProcessResult(int id, String status, long duration) {}

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            int[] taskIds = {1, 2, 3, 4, 5};

            Subtask<ProcessResult>[] results = new Subtask[taskIds.length];
            for (int i = 0; i < taskIds.length; i++) {
                final int id = taskIds[i];
                results[i] = scope.fork(() -> {
                    long start = System.nanoTime();
                    Thread.sleep(100 + (long) (Math.random() * 200));
                    long duration = (System.nanoTime() - start) / 1_000_000;
                    return new ProcessResult(id, "completed", duration);
                });
            }

            scope.join();

            for (Subtask<ProcessResult> result : results) {
                System.out.println("  Task " + result.get().id() + ": " +
                    result.get().status() + " (" + result.get().duration() + "ms)");
            }
        }
    }

    static String fetchUser(int id) throws InterruptedException {
        Thread.sleep(100);
        return "User#" + id;
    }

    static int fetchScore(int id) throws InterruptedException {
        Thread.sleep(150);
        return 42 + id;
    }

    static String fetchPreferences(int id) throws InterruptedException {
        Thread.sleep(80);
        return "theme=dark;lang=en";
    }

    static String fetchFromSource1(int id) throws InterruptedException {
        Thread.sleep(200);
        return "Source1-data-" + id;
    }

    static String fetchFromSource2(int id) throws InterruptedException {
        Thread.sleep(100);
        return "Source2-data-" + id;
    }

    static String fetchFromSource3(int id) throws InterruptedException {
        Thread.sleep(150);
        return "Source3-data-" + id;
    }

    // ==================== ScopedValue Demo ====================

    static void scopedValueDemo() throws Exception {
        System.out.println("\n=== ScopedValue ===\n");

        try (ScopedValue.ScopedEscape<String> ignored = CURRENT_USER.where("admin")) {
            processRequest();
        }

        try (ScopedValue.ScopedEscape<String> ignored = CURRENT_USER.where("guest")) {
            processRequest();
        }

        System.out.println("\n--- Nested scopes ---\n");

        ScopedValue<String> outer = ScopedValue.newInstance();
        ScopedValue<Integer> inner = ScopedValue.newInstance();

        try (var o = outer.where("OuterValue");
             var i = inner.where(42)) {
            String result = ScopedValue.runWhere(outer, "Override", () ->
                "Outer=" + outer.get() + ", Inner=" + inner.get());
            System.out.println("  " + result);
        }

        System.out.println("\n--- ScopedValue with virtual threads ---\n");

        try (ScopedValue.ScopedEscape<String> ignored = CURRENT_USER.where("request-user")) {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                Future<String> f1 = executor.submit(() -> "Thread sees: " + CURRENT_USER.get());
                Future<String> f2 = executor.submit(() -> "Thread sees: " + CURRENT_USER.get());

                System.out.println("  " + f1.get());
                System.out.println("  " + f2.get());
            }
        }
    }

    static void processRequest() {
        String user = CURRENT_USER.get();
        System.out.println("  Processing request for user: " + user);
        authorize(user);
    }

    static void authorize(String user) {
        System.out.println("  Authorizing user: " + user);
    }

    // ==================== Virtual Thread Pinning Detection ====================

    static void virtualThreadPinningDetection() throws Exception {
        System.out.println("\n=== Virtual Thread Pinning Detection ===\n");

        AtomicLong pinCount = new AtomicLong(0);
        AtomicBoolean pinDetected = new AtomicBoolean(false);

        ReentrantLock lock = new ReentrantLock();

        System.out.println("  Testing synchronized block (may cause pinning):");

        for (int i = 0; i < 100; i++) {
            final int taskNum = i;
            Thread.startVirtualThread(() -> {
                synchronized (lock) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        Thread.sleep(500);

        System.out.println("  Testing Lock-based approach (no pinning):");

        for (int i = 0; i < 100; i++) {
            Thread.startVirtualThread(() -> {
                lock.lock();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            });
        }
        Thread.sleep(500);

        System.out.println("\n--- Pinning workaround patterns ---\n");

        System.out.println("  1. Replace synchronized with ReentrantLock");
        System.out.println("  2. Use Semaphore instead of synchronized");
        System.out.println("  3. Use atomic variables for simple counters");
        System.out.println("  4. Avoid native/JNI calls in virtual threads");
        System.out.println("  5. Use -Djdk.tracePinnedThreads=full to detect pinning");

        System.out.println("\n--- Monitoring virtual threads ---\n");

        Thread vt1 = Thread.ofVirtual().name("monitor-demo-1").start(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread vt2 = Thread.ofVirtual().name("monitor-demo-2").start(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("  Thread " + vt1.getName() + ": virtual=" + vt1.isVirtual());
        System.out.println("  Thread " + vt2.getName() + ": virtual=" + vt2.isVirtual());

        vt1.join();
        vt2.join();

        System.out.println("\n  Virtual thread " + vt1.getName() + " completed");
        System.out.println("  Virtual thread " + vt2.getName() + " completed");
    }
}
