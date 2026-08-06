package war.stories;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

/**
 * WAR STORY: Production Deadlock - The Silent Service Freeze
 * 
 * Scenario: An order processing service became completely unresponsive.
 * All 200 threads were blocked, but no errors appeared in logs.
 * The service appeared healthy (HTTP 200 on health checks) but processed zero requests.
 * 
 * Investigation Process:
 * 1. Take thread dump: jstack <pid> or kill -3 <pid>
 * 2. Look for threads in BLOCKED state
 * 3. Identify lock ownership and wait chains
 * 4. Find circular dependencies (A waits for B, B waits for A)
 * 
 * Root Cause: Two methods acquired locks in opposite orders:
 * - processOrder() locked orderLock then inventoryLock
 * - updateInventory() locked inventoryLock then orderLock
 * When called concurrently, they deadlocked.
 */
public class DeadlockStory {

    // BUGGY VERSION: Deadlock-prone lock ordering
    static class OrderProcessorBuggy {
        private final Object orderLock = new Object();
        private final Object inventoryLock = new Object();
        private int orderCount = 0;
        private int inventoryCount = 1000;

        // Method 1: Locks orderLock first, then inventoryLock
        public void processOrder(int quantity) {
            synchronized (orderLock) {
                System.out.println(Thread.currentThread().getName() + 
                    ": Acquired orderLock, waiting for inventoryLock...");
                
                // Simulate some work
                simulateWork(10);
                
                synchronized (inventoryLock) {
                    System.out.println(Thread.currentThread().getName() + 
                        ": Acquired inventoryLock, processing order");
                    orderCount++;
                    inventoryCount -= quantity;
                }
            }
        }

        // Method 2: Locks inventoryLock first, then orderLock - DEADLOCK!
        public void updateInventory(int quantity) {
            synchronized (inventoryLock) {
                System.out.println(Thread.currentThread().getName() + 
                    ": Acquired inventoryLock, waiting for orderLock...");
                
                // Simulate some work
                simulateWork(10);
                
                synchronized (orderLock) {
                    System.out.println(Thread.currentThread().getName() + 
                        ": Acquired orderLock, updating inventory");
                    inventoryCount += quantity;
                }
            }
        }
    }

    // FIXED VERSION: Consistent lock ordering
    static class OrderProcessorFixed {
        private final Object orderLock = new Object();
        private final Object inventoryLock = new Object();
        private int orderCount = 0;
        private int inventoryCount = 1000;

        // Both methods now lock in same order: orderLock then inventoryLock
        public void processOrder(int quantity) {
            synchronized (orderLock) {
                synchronized (inventoryLock) {
                    System.out.println(Thread.currentThread().getName() + 
                        ": Processing order (consistent lock order)");
                    orderCount++;
                    inventoryCount -= quantity;
                }
            }
        }

        public void updateInventory(int quantity) {
            // Acquire locks in SAME order as processOrder
            synchronized (orderLock) {
                synchronized (inventoryLock) {
                    System.out.println(Thread.currentThread().getName() + 
                        ": Updating inventory (consistent lock order)");
                    inventoryCount += quantity;
                }
            }
        }
    }

    // BETTER VERSION: Use tryLock with timeout to prevent deadlock
    static class OrderProcessorBetter {
        private final ReentrantLock orderLock = new ReentrantLock();
        private final ReentrantLock inventoryLock = new ReentrantLock();
        private final Condition orderCondition = orderLock.newCondition();
        private int orderCount = 0;
        private int inventoryCount = 1000;

        public boolean processOrder(int quantity) throws InterruptedException {
            // Try to acquire both locks with timeout
            if (!orderLock.tryLock(1, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + 
                    ": Could not acquire orderLock, backing off");
                return false;
            }
            try {
                if (!inventoryLock.tryLock(1, TimeUnit.SECONDS)) {
                    System.out.println(Thread.currentThread().getName() + 
                        ": Could not acquire inventoryLock, backing off");
                    return false;
                }
                try {
                    System.out.println(Thread.currentThread().getName() + 
                        ": Processing order with tryLock");
                    orderCount++;
                    inventoryCount -= quantity;
                    return true;
                } finally {
                    inventoryLock.unlock();
                }
            } finally {
                orderLock.unlock();
            }
        }

        public boolean updateInventory(int quantity) throws InterruptedException {
            // CRITICAL: Must acquire locks in SAME ORDER as processOrder
            if (!orderLock.tryLock(1, TimeUnit.SECONDS)) {
                return false;
            }
            try {
                if (!inventoryLock.tryLock(1, TimeUnit.SECONDS)) {
                    return false;
                }
                try {
                    System.out.println(Thread.currentThread().getName() + 
                        ": Updating inventory with tryLock");
                    inventoryCount += quantity;
                    return true;
                } finally {
                    inventoryLock.unlock();
                }
            } finally {
                orderLock.unlock();
            }
        }
    }

    // PRODUCTION VERSION: Use StampedLock for better concurrency
    static class OrderProcessorProduction {
        private final StampedLock lock = new StampedLock();
        private int orderCount = 0;
        private int inventoryCount = 1000;

        public void processOrder(int quantity) {
            long stamp = lock.writeLock();
            try {
                System.out.println(Thread.currentThread().getName() + 
                    ": Processing order with StampedLock");
                orderCount++;
                inventoryCount -= quantity;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        public boolean tryProcessOrder(int quantity) throws InterruptedException {
            long stamp = lock.tryWriteLock(1, TimeUnit.SECONDS);
            if (stamp == 0) {
                System.out.println(Thread.currentThread().getName() + 
                    ": Could not acquire write lock, backing off");
                return false;
            }
            try {
                orderCount++;
                inventoryCount -= quantity;
                return true;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        public int readInventory() {
            long stamp = lock.tryOptimisticRead();
            int currentInventory = inventoryCount;
            if (!lock.validate(stamp)) {
                // Fallback to read lock
                stamp = lock.readLock();
                try {
                    currentInventory = inventoryCount;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            return currentInventory;
        }
    }

    private static void simulateWork(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Deadlock War Story ===\n");
        
        // Demonstrate the deadlock scenario
        System.out.println("--- Demonstrating Deadlock (Buggy Version) ---");
        System.out.println("NOTE: In production, this would freeze the entire service!\n");
        
        OrderProcessorBuggy buggy = new OrderProcessorBuggy();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                buggy.processOrder(10);
                simulateWork(5);
            }
        }, "OrderThread");
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                buggy.updateInventory(10);
                simulateWork(5);
            }
        }, "InventoryThread");
        
        // Uncomment to see actual deadlock (will hang!)
        // t1.start();
        // t2.start();
        
        System.out.println("Deadlock would occur if both threads run simultaneously.");
        System.out.println("Thread 1: orderLock -> inventoryLock");
        System.out.println("Thread 2: inventoryLock -> orderLock");
        System.out.println("If timing is wrong, both wait forever!\n");
        
        // Show the fix
        System.out.println("--- Demonstrating Fixed Version (Consistent Lock Order) ---");
        OrderProcessorFixed fixed = new OrderProcessorFixed();
        
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                fixed.processOrder(10);
                simulateWork(5);
            }
        }, "OrderThread-Fixed");
        
        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                fixed.updateInventory(10);
                simulateWork(5);
            }
        }, "InventoryThread-Fixed");
        
        t3.start();
        t4.start();
        
        try {
            t3.join(5000);
            t4.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Fixed version completes without deadlock!\n");
        
        // Print investigation checklist
        printInvestigationChecklist();
    }

    private static void printInvestigationChecklist() {
        System.out.println("=== Deadlock Investigation Checklist ===");
        System.out.println("1. Take thread dump:");
        System.out.println("   jstack <pid> or kill -3 <pid>");
        System.out.println("   jcmd <pid> Thread.print");
        System.out.println("\n2. Look for BLOCKED threads:");
        System.out.println("   - Threads waiting to enter synchronized block");
        System.out.println("   - Check 'held by' and 'waiting to lock' in dump");
        System.out.println("\n3. Identify lock chains:");
        System.out.println("   Thread A: holds Lock1, waiting for Lock2");
        System.out.println("   Thread B: holds Lock2, waiting for Lock1");
        System.out.println("   -> Circular dependency = deadlock");
        System.out.println("\n4. Prevention strategies:");
        System.out.println("   - Always acquire locks in consistent order");
        System.out.println("   - Use tryLock with timeout instead of synchronized");
        System.out.println("   - Minimize lock scope and duration");
        System.out.println("   - Consider lock-free alternatives (ConcurrentHashMap)");
        System.out.println("   - Use StampedLock for read-heavy workloads");
        System.out.println("\n5. Monitoring:");
        System.out.println("   - Enable JMX for thread monitoring");
        System.out.println("   - Alert on threads in BLOCKED state > 10 seconds");
        System.out.println("   - Regular thread dump analysis in production");
    }
}
