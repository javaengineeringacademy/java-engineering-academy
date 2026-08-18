package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.concurrent.TimeUnit;

public class LockSolutions {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: ReentrantLock basics
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: ReentrantLock Basics ===");
        ReentrantLock lock = new ReentrantLock();
        int[] counter = {0};

        Runnable task = () -> {
            lock.lock();
            try {
                for (int i = 0; i < 1000; i++) {
                    counter[0]++;
                }
                System.out.println(Thread.currentThread().getName() + " finished, counter: " + counter[0]);
            } finally {
                lock.unlock();
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Final counter: " + counter[0]);
    }

    /**
     * Exercise 2: tryLock() non-blocking
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: tryLock() ===");
        ReentrantLock lock = new ReentrantLock();

        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Holder: acquired lock, sleeping 2 seconds");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread tryer = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            boolean acquired = lock.tryLock();
            if (acquired) {
                try {
                    System.out.println("Tryer: acquired lock!");
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("Tryer: Could not acquire lock");
            }
        });

        holder.start();
        tryer.start();
        holder.join();
        tryer.join();
    }

    /**
     * Exercise 3: tryLock with timeout
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: tryLock with Timeout ===");
        ReentrantLock lock = new ReentrantLock();

        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Holder: acquired lock, sleeping 3 seconds");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread tryer = new Thread(() -> {
            try {
                Thread.sleep(500);
                System.out.println("Tryer: attempting to acquire lock with 1 second timeout...");
                boolean acquired = lock.tryLock(1, TimeUnit.SECONDS);
                if (acquired) {
                    try {
                        System.out.println("Tryer: acquired lock!");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Tryer: Lock acquisition timed out");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        holder.start();
        tryer.start();
        holder.join();
        tryer.join();
    }

    /**
     * Exercise 4: ReadWriteLock
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: ReadWriteLock ===");
        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        int[] sharedData = {0};

        Runnable reader = () -> {
            for (int i = 0; i < 3; i++) {
                rwLock.readLock().lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " read: " + sharedData[0]);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.readLock().unlock();
                }
            }
        };

        Runnable writer = () -> {
            for (int i = 1; i <= 3; i++) {
                rwLock.writeLock().lock();
                try {
                    sharedData[0] = i;
                    System.out.println("Writer wrote: " + i);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.writeLock().unlock();
                }
            }
        };

        Thread[] readers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            readers[i] = new Thread(reader, "Reader-" + i);
        }
        Thread writerThread = new Thread(writer, "Writer");

        for (Thread r : readers) r.start();
        writerThread.start();
        for (Thread r : readers) r.join();
        writerThread.join();
    }

    /**
     * Exercise 5: StampedLock
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: StampedLock ===");
        StampedLock lock = new StampedLock();
        int[] data = {0};

        Runnable reader = () -> {
            long stamp = lock.tryOptimisticRead();
            int current = data[0];
            if (!lock.validate(stamp)) {
                stamp = lock.readLock();
                try {
                    current = data[0];
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            System.out.println(Thread.currentThread().getName() + " read: " + current);
        };

        Runnable writer = () -> {
            long stamp = lock.writeLock();
            try {
                data[0]++;
                System.out.println("Writer wrote: " + data[0]);
            } finally {
                lock.unlockWrite(stamp);
            }
        };

        Thread t1 = new Thread(reader, "Reader-1");
        Thread t2 = new Thread(writer, "Writer");
        Thread t3 = new Thread(reader, "Reader-2");

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
