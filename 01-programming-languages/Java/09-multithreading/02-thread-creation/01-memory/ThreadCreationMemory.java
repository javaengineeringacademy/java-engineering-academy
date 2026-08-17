package academy.javaengineering.concurrency.threadcreation;

/**
 * ThreadCreationMemory - Demonstrates memory aspects of thread creation.
 */
public class ThreadCreationMemory {

    private static int sharedCounter = 0;
    private static final ThreadLocal<Integer> threadLocalCounter =
        ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Memory Cost ===");
        threadMemoryCost();

        System.out.println("\n=== Closure Capture ===");
        closureCapture();

        System.out.println("\n=== Shared vs Thread-Local ===");
        sharedVsThreadLocal();

        System.out.println("\n=== Thread Object on Heap ===");
        threadObjectOnHeap();
    }

    static void threadMemoryCost() throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("  Memory before creating threads: " + before / 1024 + " KB");

        int numThreads = 100;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int id = i;
            threads[i] = new Thread(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { return; }
            }, "Thread-" + id);
            threads[i].start();
        }

        long after = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("  Memory after creating " + numThreads + " threads: " +
            after / 1024 + " KB");
        System.out.println("  Approximate per-thread cost: " +
            ((after - before) / numThreads / 1024) + " KB");

        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    static void closureCapture() throws InterruptedException {
        int outerVar = 100; // Effectively final for capture
        String name = "captured"; // Captured by lambda

        Thread t = new Thread(() -> {
            // These variables are captured (copied) from the enclosing scope
            System.out.println("  Captured outerVar: " + outerVar);
            System.out.println("  Captured name: " + name);
        });

        t.start();
        t.join();
    }

    static void sharedVsThreadLocal() throws InterruptedException {
        sharedCounter = 0;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedCounter++;
                int local = threadLocalCounter.get() + 1;
                threadLocalCounter.set(local);
            }
            System.out.println("  Thread 1 - shared: " + sharedCounter +
                ", thread-local: " + threadLocalCounter.get());
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedCounter++;
                int local = threadLocalCounter.get() + 1;
                threadLocalCounter.set(local);
            }
            System.out.println("  Thread 2 - shared: " + sharedCounter +
                ", thread-local: " + threadLocalCounter.get());
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  Main - shared: " + sharedCounter +
            ", thread-local: " + threadLocalCounter.get());

        threadLocalCounter.remove(); // Clean up
    }

    static void threadObjectOnHeap() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("  Thread object is on heap: " +
                Thread.currentThread().getName());
        }, "HeapThread");

        System.out.println("  Thread is a regular Java object: " + (t instanceof Object));
        System.out.println("  Thread class: " + t.getClass().getName());

        t.start();
        t.join();
    }
}
