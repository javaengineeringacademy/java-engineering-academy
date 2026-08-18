package academy.javaengineering.concurrency.locks.memory;

import java.util.concurrent.locks.*;

public class LockMemory {
    public static void main(String[] args) {
        System.out.println("Lock Memory Comparison");
        System.out.println("======================");

        Runtime rt = Runtime.getRuntime();
        long before = rt.freeMemory();

        ReentrantLock rLock = new ReentrantLock();
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        StampedLock sLock = new StampedLock();

        long after = rt.freeMemory();
        System.out.println("ReentrantLock: ~" + (before - after) + " bytes");

        before = rt.freeMemory();
        ReadWriteLock rw2 = new ReentrantReadWriteLock();
        after = rt.freeMemory();
        System.out.println("ReadWriteLock: ~" + (before - after) + " bytes (2x ReentrantLock)");

        System.out.println("\nMemory per waiting thread:");
        System.out.println("  - Condition queue: ~48 bytes per waiter");
        System.out.println("  - Lock queue: ~32 bytes per waiter");
        System.out.println("  - Sync queue (AQS): ~16 bytes per waiter");
    }
}
