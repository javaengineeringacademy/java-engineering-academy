package academy.javaengineering.concurrency.locks.internals;

import java.util.concurrent.locks.*;

public class LockInternals {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(true); // fair lock
        StringBuilder log = new StringBuilder();

        Runnable task = (Runnable) () -> {
            String name = Thread.currentThread().getName();
            log.append(name + ": attempting to acquire lock\n");
            lock.lock();
            try {
                log.append(name + ": acquired lock (hold count: " + lock.getHoldCount() + ")\n");
                log.append(name + ": is held by current thread: " + lock.isHeldByCurrentThread() + "\n");
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            } finally {
                log.append(name + ": releasing lock\n");
                lock.unlock();
            }
        };

        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(log.toString());
        System.out.println("Fair lock ensures FIFO ordering: " + lock.hasQueuedThreads());
    }
}
