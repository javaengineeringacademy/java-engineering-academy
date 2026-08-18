package academy.javaengineering.concurrency.sync.deeprdive;

/**
 * Synchronized Instance Method Example.
 *
 * Demonstrates that a synchronized instance method locks on `this`.
 * Multiple threads calling the same method on the SAME object will
 * serialize — only one thread executes at a time.
 *
 * Multiple threads calling on DIFFERENT objects run independently.
 */
public class SyncMethodExample {

    private int count = 0;

    /**
     * Synchronized instance method — locks on `this`.
     * Only one thread per object can execute this at a time.
     */
    public synchronized void increment() {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] entering increment(), count before: " + count);
        try {
            Thread.sleep(100); // simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        count++;
        System.out.println("[" + threadName + "] exiting increment(), count after: " + count);
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        // --- Scenario 1: Same object, multiple threads ---
        System.out.println("=== Scenario 1: Same Object ===");
        SyncMethodExample shared = new SyncMethodExample();

        Thread t1 = new Thread(shared::increment, "Thread-A");
        Thread t2 = new Thread(shared::increment, "Thread-B");
        Thread t3 = new Thread(shared::increment, "Thread-C");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Final count (same object): " + shared.getCount());
        System.out.println("Expected: 3, No race condition.\n");

        // --- Scenario 2: Different objects, multiple threads ---
        System.out.println("=== Scenario 2: Different Objects ===");
        SyncMethodExample obj1 = new SyncMethodExample();
        SyncMethodExample obj2 = new SyncMethodExample();

        Thread t4 = new Thread(obj1::increment, "Thread-D");
        Thread t5 = new Thread(obj2::increment, "Thread-E");

        t4.start();
        t5.start();

        t4.join();
        t5.join();

        System.out.println("obj1 count: " + obj1.getCount());
        System.out.println("obj2 count: " + obj2.getCount());
        System.out.println("Different objects have independent locks — both increment independently.");
    }
}
