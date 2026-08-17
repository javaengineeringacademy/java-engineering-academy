package academy.javaengineering.concurrency.introduction;

/**
 * IntroductionMemory - Demonstrates thread memory concepts.
 * Shows shared heap vs private stack, and memory visibility issues.
 */
public class IntroductionMemory {

    private static int sharedCounter = 0;
    private static volatile int volatileCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Shared Heap vs Private Stack ===");
        sharedHeapDemo();

        System.out.println("\n=== Memory Visibility Problem ===");
        memoryVisibilityProblem();

        System.out.println("\n=== Volatile Visibility Fix ===");
        volatileFixDemo();

        System.out.println("\n=== Thread-Local Storage ===");
        threadLocalDemo();
    }

    static void sharedHeapDemo() throws InterruptedException {
        int[] sharedArray = {0};

        Thread t1 = new Thread(() -> {
            sharedArray[0] = 100;
            System.out.println("  Thread 1 set sharedArray[0] = 100");
        });

        Thread t2 = new Thread(() -> {
            System.out.println("  Thread 2 reads sharedArray[0] = " + sharedArray[0]);
        });

        t1.start();
        t1.join();
        t2.start();
        t2.join();

        System.out.println("  Main reads sharedArray[0] = " + sharedArray[0]);
        System.out.println("  All threads share the same heap object");
    }

    static void memoryVisibilityProblem() throws InterruptedException {
        sharedCounter = 0;
        boolean[] stop = {false};

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                sharedCounter = i; // Without volatile, may not be visible
            }
            stop[0] = true;
        });

        Thread reader = new Thread(() -> {
            int lastValue = 0;
            while (!stop[0]) {
                if (sharedCounter != lastValue) {
                    lastValue = sharedCounter;
                }
            }
            System.out.println("  Reader saw final value (may be stale): " + lastValue);
        });

        writer.start();
        reader.start();
        writer.join(2000);
        reader.join(2000);
        System.out.println("  Actual final value: " + sharedCounter);
    }

    static void volatileFixDemo() throws InterruptedException {
        volatileCounter = 0;
        volatile boolean[] stop = {false};

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                volatileCounter = i;
            }
            stop[0] = true;
        });

        Thread reader = new Thread(() -> {
            int lastValue = 0;
            while (!stop[0]) {
                if (volatileCounter != lastValue) {
                    lastValue = volatileCounter;
                }
            }
            System.out.println("  Reader saw final value: " + lastValue);
        });

        writer.start();
        reader.start();
        writer.join(2000);
        reader.join(2000);
        System.out.println("  Actual final value: " + volatileCounter);
    }

    static void threadLocalDemo() throws InterruptedException {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        Thread t1 = new Thread(() -> {
            threadLocal.set("Data from Thread 1");
            System.out.println("  Thread 1: " + threadLocal.get());
        });

        Thread t2 = new Thread(() -> {
            threadLocal.set("Data from Thread 2");
            System.out.println("  Thread 2: " + threadLocal.get());
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  Main thread: " + threadLocal.get());
        threadLocal.remove(); // Clean up
    }
}
