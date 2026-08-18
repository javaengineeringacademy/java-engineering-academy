package academy.javaengineering.concurrency.sync.deeprdive;

/**
 * Static Synchronized Method Example.
 *
 * A static synchronized method locks on the Class object (ClassName.class),
 * NOT on any instance. All threads across ALL instances share this single lock.
 *
 * This is a global lock — even different instances cannot run static
 * synchronized methods concurrently.
 */
public class StaticSyncExample {

    private static int globalCount = 0;
    private int instanceCount = 0;

    /**
     * Static synchronized — locks on StaticSyncExample.class.
     * Only one thread across all instances can execute this at a time.
     */
    public static synchronized void staticIncrement(String label) {
        System.out.println("[" + label + "] entering staticIncrement(), globalCount before: " + globalCount);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        globalCount++;
        System.out.println("[" + label + "] exiting staticIncrement(), globalCount after: " + globalCount);
    }

    /**
     * Instance synchronized — locks on `this`.
     * Each instance has its own lock.
     */
    public synchronized void instanceIncrement(String label) {
        System.out.println("[" + label + "] entering instanceIncrement(), instanceCount before: " + instanceCount);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        instanceCount++;
        System.out.println("[" + label + "] exiting instanceIncrement(), instanceCount after: " + instanceCount);
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public static int getGlobalCount() {
        return globalCount;
    }

    public static void main(String[] args) throws InterruptedException {
        // --- Scenario 1: Static sync — different instances, same lock ---
        System.out.println("=== Scenario 1: Static Synchronized — Global Lock ===");
        StaticSyncExample obj1 = new StaticSyncExample();
        StaticSyncExample obj2 = new StaticSyncExample();

        // Even though these are different instances, static sync locks on the Class object
        Thread t1 = new Thread(() -> StaticSyncExample.staticIncrement("Thread-A (obj1)"), "Thread-A");
        Thread t2 = new Thread(() -> StaticSyncExample.staticIncrement("Thread-B (obj2)"), "Thread-B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Global count: " + StaticSyncExample.getGlobalCount());
        System.out.println("Expected: 2. Both threads blocked on same Class lock.\n");

        // --- Scenario 2: Instance sync — different instances, different locks ---
        System.out.println("=== Scenario 2: Instance Synchronized — Per-Object Lock ===");
        Thread t3 = new Thread(() -> obj1.instanceIncrement("Thread-C (obj1)"), "Thread-C");
        Thread t4 = new Thread(() -> obj2.instanceIncrement("Thread-D (obj2)"), "Thread-D");

        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println("obj1 instanceCount: " + obj1.getInstanceCount());
        System.out.println("obj2 instanceCount: " + obj2.getInstanceCount());
        System.out.println("Different instances, different locks — both run concurrently.");

        // --- Scenario 3: Mix — static and instance sync on same object ---
        System.out.println("\n=== Scenario 3: Static vs Instance Sync on Same Object ===");
        StaticSyncExample obj3 = new StaticSyncExample();

        Thread t5 = new Thread(() -> StaticSyncExample.staticIncrement("Thread-E (static)"), "Thread-E");
        Thread t6 = new Thread(() -> obj3.instanceIncrement("Thread-F (instance)"), "Thread-F");

        t5.start();
        t6.start();
        t5.join();
        t6.join();

        System.out.println("Static and instance locks are INDEPENDENT — both can run concurrently.");
    }
}
