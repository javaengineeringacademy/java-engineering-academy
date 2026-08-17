package academy.javaengineering.concurrency.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * Solutions - Complete solutions for Locks exercises.
 */
public class Solutions {

    /**
     * Solution 1: ReentrantLock Counter
     */
    static void exercise1() throws InterruptedException {
        System.out.println("Exercise 1: ReentrantLock Counter");
        System.out.println("=================================");

        final ReentrantLock lock = new ReentrantLock();
        final int[] counter = {0};

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int id = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    lock.lock();
                    try {
                        counter[0]++;
                    } finally {
                        lock.unlock();
                    }
                }
                // Try-lock demo for one thread
                if (id == 0) {
                    boolean acquired = lock.tryLock();
                    System.out.println("  Thread 0 tryLock: " + acquired);
                    if (acquired) lock.unlock();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        System.out.println("  Expected: 1000000");
        System.out.println("  Actual: " + counter[0]);
        System.out.println();
    }

    /**
     * Solution 2: ReadWriteLock Cache
     */
    static void exercise2() throws InterruptedException {
        System.out.println("Exercise 2: ReadWriteLock Cache");
        System.out.println("===============================");

        final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        final int[] cache = {0};

        Thread[] readers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final int id = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    rwLock.readLock().lock();
                    try {
                        System.out.println("  Reader " + id + " read: " + cache[0]);
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        rwLock.readLock().unlock();
                    }
                }
            });
        }

        Thread writer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                rwLock.writeLock().lock();
                try {
                    cache[0] = i * 100;
                    System.out.println("  Writer wrote: " + cache[0]);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.writeLock().unlock();
                }
            }
        });

        for (Thread t : readers) t.start();
        writer.start();

        for (Thread t : readers) t.join();
        writer.join();
        System.out.println();
    }

    /**
     * Solution 3: StampedLock Optimistic Read
     */
    static void exercise3() throws InterruptedException {
        System.out.println("Exercise 3: StampedLock Optimistic Read");
        System.out.println("=======================================");

        final StampedLock sl = new StampedLock();
        final int[] data = {0, 0}; // x, y

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                long stamp = sl.tryOptimisticRead();
                int x = data[0];
                int y = data[1];

                if (!sl.validate(stamp)) {
                    stamp = sl.readLock();
                    try {
                        x = data[0];
                        y = data[1];
                    } finally {
                        sl.unlockRead(stamp);
                    }
                }

                System.out.println("  Read: x=" + x + ", y=" + y);
                try { Thread.sleep(10); } catch (InterruptedException e) { return; }
            }
        });

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                long stamp = sl.writeLock();
                try {
                    data[0]++;
                    data[1] = data[0] * 10;
                    System.out.println("  Wrote: x=" + data[0] + ", y=" + data[1]);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    sl.unlockWrite(stamp);
                }
            }
        });

        reader.start();
        writer.start();
        reader.join();
        writer.join();
        System.out.println();
    }

    /**
     * Solution 4: Condition Bounded Buffer
     */
    static void exercise4() throws InterruptedException {
        System.out.println("Exercise 4: Condition Bounded Buffer");
        System.out.println("=====================================");

        final int BUFFER_SIZE = 5;
        final int ITEMS = 20;
        final ReentrantLock lock = new ReentrantLock();
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();
        final int[] buffer = new int[BUFFER_SIZE];
        final int[] count = {0};

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= ITEMS; i++) {
                lock.lock();
                try {
                    while (count[0] == BUFFER_SIZE) {
                        notFull.await();
                    }
                    buffer[count[0]++] = i;
                    System.out.println("  Produced: " + i);
                    notEmpty.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < ITEMS; i++) {
                lock.lock();
                try {
                    while (count[0] == 0) {
                        notEmpty.await();
                    }
                    int val = buffer[--count[0]];
                    System.out.println("  Consumed: " + val);
                    notFull.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println();
    }

    /**
     * Solution 5: tryLock with Timeout
     */
    static void exercise5() throws InterruptedException {
        System.out.println("Exercise 5: tryLock with Timeout");
        System.out.println("=================================");

        final ReentrantLock lock = new ReentrantLock();

        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  Holder: acquired lock, holding for 2s");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("  Holder: released lock");
            }
        });

        Thread tryer = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { return; }
            try {
                boolean acquired = lock.tryLock(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (acquired) {
                    try {
                        System.out.println("  Tryer: acquired lock");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("  Tryer: Giving up (couldn't acquire within 500ms)");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        holder.start();
        tryer.start();

        holder.join();
        tryer.join();
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
