/**
 * Happens-Before Demo
 * Demonstrates all happens-before relationships in the JMM
 */
public class HappensBeforeDemo {

    private static int sharedValue = 0;
    private static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Happens-Before Demo ===\n");

        // Rule 1: Program Order Rule
        System.out.println("--- Rule 1: Program Order (same thread) ---");
        int a = 1;
        int b = a + 1;
        System.out.println("a=" + a + ", b=" + b);
        System.out.println("Within same thread: a=1 happens-before b=a+1");
        System.out.println("b is guaranteed to see a=1\n");

        // Rule 2: Monitor Lock Rule
        System.out.println("--- Rule 2: Monitor Lock Rule ---");
        final Object monitor = new Object();
        final int[] monitorData = {0};

        Thread writer1 = new Thread(() -> {
            synchronized (monitor) {
                monitorData[0] = 42;
                System.out.println("Writer: set monitorData=42");
            } // unlock
        });

        Thread reader1 = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            synchronized (monitor) { // lock
                System.out.println("Reader: sees monitorData=" + monitorData[0]);
                System.out.println("  (guaranteed: unlock happens-before lock on same monitor)");
            }
        });

        writer1.start();
        reader1.start();
        writer1.join();
        reader1.join();
        System.out.println();

        // Rule 3: Volatile Variable Rule
        System.out.println("--- Rule 3: Volatile Variable Rule ---");
        final volatile int[] volatileData = {0};
        final volatile boolean[] volatileFlag = {false};

        Thread writer2 = new Thread(() -> {
            volatileData[0] = 100;   // regular write
            volatileFlag[0] = true;  // volatile write
            System.out.println("Writer: set volatileData=100, volatileFlag=true");
        });

        Thread reader2 = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            if (volatileFlag[0]) { // volatile read
                System.out.println("Reader: sees volatileData=" + volatileData[0]);
                System.out.println("  (guaranteed: volatile write happens-before volatile read)");
            }
        });

        writer2.start();
        reader2.start();
        writer2.join();
        reader2.join();
        System.out.println();

        // Rule 4: Thread Start Rule
        System.out.println("--- Rule 4: Thread Start Rule ---");
        sharedValue = 100;
        System.out.println("Main: set sharedValue=100 before Thread.start()");

        Thread started = new Thread(() -> {
            System.out.println("Started thread: sees sharedValue=" + sharedValue);
            System.out.println("  (guaranteed: Thread.start() happens-before any action in started thread)");
        });
        started.start();
        started.join();
        System.out.println();

        // Rule 5: Thread Termination Rule (join)
        System.out.println("--- Rule 5: Thread Termination Rule (join) ---");
        Thread computed = new Thread(() -> {
            sharedValue = 200;
            System.out.println("Worker thread: set sharedValue=200");
        });
        computed.start();
        computed.join(); // join returns
        System.out.println("Main after join: sees sharedValue=" + sharedValue);
        System.out.println("  (guaranteed: all actions in thread happen-before join returns)");
        System.out.println();

        // Rule 6: Transitivity
        System.out.println("--- Rule 6: Transitivity ---");
        System.out.println("If A happens-before B, and B happens-before C");
        System.out.println("Then A happens-before C");
        System.out.println();
        System.out.println("Example chain:");
        System.out.println("  1. write x=1 (program order)  ->  volatile write flag=true");
        System.out.println("  2. volatile read flag=true (volatile rule)  ->  read x");
        System.out.println("  3. By transitivity: write x=1 happens-before read x");
        System.out.println();

        // Practical example combining all rules
        System.out.println("--- Practical Example: Complete Ordering ---");
        final int[] data1 = {0};
        final int[] data2 = {0};
        final volatile boolean[] ready = {false};

        Thread producer = new Thread(() -> {
            data1[0] = 10;                     // action 1
            data2[0] = 20;                     // action 2
            ready[0] = true;                   // action 3 (volatile write)
            // Actions 1,2 happen-before action 3 (program order)
        });

        Thread consumer = new Thread(() -> {
            while (!ready[0]) { }              // action 4 (volatile read)
            // Actions 4 happens-before 5 (program order)
            // Action 3 happens-before action 4 (volatile rule)
            // By transitivity: actions 1,2 happen-before action 5
            System.out.println("Consumer: data1=" + data1[0] + ", data2=" + data2[0]);
            System.out.println("  (guaranteed: sees 10 and 20 by transitivity)");
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println("\n=== End of Happens-Before Demo ===");
    }
}

/*
Expected Output (approximate):
=== Happens-Before Demo ===

--- Rule 1: Program Order (same thread) ---
a=1, b=2
Within same thread: a=1 happens-before b=a+1
b is guaranteed to see a=1

--- Rule 2: Monitor Lock Rule ---
Writer: set monitorData=42
Reader: sees monitorData=42
  (guaranteed: unlock happens-before lock on same monitor)

--- Rule 3: Volatile Variable Rule ---
Writer: set volatileData=100, volatileFlag=true
Reader: sees volatileData=100
  (guaranteed: volatile write happens-before volatile read)

--- Rule 4: Thread Start Rule ---
Main: set sharedValue=100 before Thread.start()
Started thread: sees sharedValue=100
  (guaranteed: Thread.start() happens-before any action in started thread)

--- Rule 5: Thread Termination Rule (join) ---
Worker thread: set sharedValue=200
Main after join: sees sharedValue=200
  (guaranteed: all actions in thread happen-before join returns)

--- Rule 6: Transitivity ---
If A happens-before B, and B happens-before C
Then A happens-before C

Example chain:
  1. write x=1 (program order)  ->  volatile write flag=true
  2. volatile read flag=true (volatile rule)  ->  read x
  3. By transitivity: write x=1 happens-before read x

--- Practical Example: Complete Ordering ---
Consumer: data1=10, data2=20
  (guaranteed: sees 10 and 20 by transitivity)

=== End of Happens-Before Demo ===
*/
