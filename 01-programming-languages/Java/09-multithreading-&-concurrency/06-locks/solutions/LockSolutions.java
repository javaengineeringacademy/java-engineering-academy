package academy.javaengineering.concurrency.locks.solutions;

import java.util.concurrent.locks.*;
import java.util.concurrent.TimeUnit;

public class LockSolutions {
    public static void main(String[] args) throws InterruptedException {
        // Solution 1: Try-lock deadlock avoidance
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        Runnable safeTask = () -> {
            while (true) {
                boolean gotLock1 = false;
                boolean gotLock2 = false;
                try {
                    gotLock1 = lock1.tryLock(100, TimeUnit.MILLISECONDS);
                    gotLock2 = lock2.tryLock(100, TimeUnit.MILLISECONDS);
                    if (gotLock1 && gotLock2) {
                        System.out.println(Thread.currentThread().getName() + ": got both locks");
                        break;
                    }
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                finally {
                    if (gotLock2) lock2.unlock();
                    if (gotLock1) lock1.unlock();
                }
            }
        };

        Thread t1 = new Thread(safeTask, "T1");
        Thread t2 = new Thread(safeTask, "T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("No deadlock - try-lock with backoff");

        // Solution 2: ReadWriteLock cache
        System.out.println("\nReadWriteLock Cache:");
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        java.util.Map<String, String> cache = new java.util.HashMap<>();

        Runnable readTask = () -> {
            rwLock.readLock().lock();
            try {
                String val = cache.getOrDefault("key", "not found");
                System.out.println("Read: " + val);
            } finally { rwLock.readLock().unlock(); }
        };

        Runnable writeTask = () -> {
            rwLock.writeLock().lock();
            try {
                cache.put("key", "value-" + System.currentTimeMillis());
                System.out.println("Written: " + cache.get("key"));
            } finally { rwLock.writeLock().unlock(); }
        };

        new Thread(writeTask).start();
        Thread.sleep(10);
        new Thread(readTask).start();
        new Thread(readTask).start();
        Thread.sleep(100);
    }
}
