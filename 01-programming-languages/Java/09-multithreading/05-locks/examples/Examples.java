package academy.javaengineering.concurrency.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * Examples - Runnable examples demonstrating lock concepts.
 */
public class Examples {

    /**
     * Example 1: ReentrantLock Basics
     * Demonstrates lock, unlock, tryLock, and timed lock.
     */
    static class Example1_ReentrantLock {
        private static final ReentrantLock lock = new ReentrantLock();
        private static int counter = 0;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 1: ReentrantLock Basics");
            System.out.println("================================");

            // Basic lock/unlock
            Thread t1 = new Thread(() -> {
                lock.lock();
                try {
                    counter++;
                    System.out.println("  T1: counter = " + counter);
                } finally {
                    lock.unlock();
                }
            });

            // tryLock
            Thread t2 = new Thread(() -> {
                boolean acquired = lock.tryLock();
                if (acquired) {
                    try {
                        counter += 10;
                        System.out.println("  T2: acquired via tryLock, counter = " + counter);
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("  T2: could not acquire lock");
                }
            });

            // Timed lock
            Thread t3 = new Thread(() -> {
                try {
                    boolean acquired = lock.tryLock(1, java.util.concurrent.TimeUnit.SECONDS);
                    if (acquired) {
                        try {
                            counter += 100;
                            System.out.println("  T3: acquired via timed lock, counter = " + counter);
                        } finally {
                            lock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            t1.start(); t1.join();
            t2.start(); t2.join();
            t3.start(); t3.join();

            System.out.println("  Final counter: " + counter);
            System.out.println();
        }
    }

    /**
     * Example 2: ReadWriteLock
     * Demonstrates concurrent reads and exclusive writes.
     */
    static class Example2_ReadWriteLock {
        private static final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private static int data = 0;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 2: ReadWriteLock");
            System.out.println("=========================");

            // Multiple readers
            Thread[] readers = new Thread[5];
            for (int i = 0; i < 5; i++) {
                final int id = i;
                readers[i] = new Thread(() -> {
                    rwLock.readLock().lock();
                    try {
                        System.out.println("  Reader " + id + " read: " + data);
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        rwLock.readLock().unlock();
                    }
                });
            }

            // Writer
            Thread writer = new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    rwLock.writeLock().lock();
                    try {
                        data++;
                        System.out.println("  Writer wrote: " + data);
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

            System.out.println("  Final data: " + data);
            System.out.println();
        }
    }

    /**
     * Example 3: StampedLock with Optimistic Read
     * Demonstrates optimistic reading for maximum performance.
     */
    static class Example3_StampedLock {
        private static final StampedLock sl = new StampedLock();
        private static int x = 0;
        private static int y = 0;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 3: StampedLock Optimistic Read");
            System.out.println("=======================================");

            // Reader with optimistic read
            Thread reader = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    long stamp = sl.tryOptimisticRead();
                    int currentX = x;
                    int currentY = y;

                    if (!sl.validate(stamp)) {
                        // Fallback to read lock
                        stamp = sl.readLock();
                        try {
                            currentX = x;
                            currentY = y;
                        } finally {
                            sl.unlockRead(stamp);
                        }
                    }

                    System.out.println("  Read: x=" + currentX + ", y=" + currentY);
                    try { Thread.sleep(10); } catch (InterruptedException e) { return; }
                }
            });

            // Writer
            Thread writer = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    long stamp = sl.writeLock();
                    try {
                        x++;
                        y = x * 2;
                        System.out.println("  Wrote: x=" + x + ", y=" + y);
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
    }

    /**
     * Example 4: Condition Variables
     * Demonstrates using Conditions for producer-consumer.
     */
    static class Example4_Conditions {
        private static final ReentrantLock lock = new ReentrantLock();
        private static final Condition notEmpty = lock.newCondition();
        private static final Condition notFull = lock.newCondition();
        private static final int BUFFER_SIZE = 5;
        private static final int[] buffer = new int[BUFFER_SIZE];
        private static int count = 0;

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 4: Condition Variables");
            System.out.println("==============================");

            Thread producer = new Thread(() -> {
                for (int i = 1; i <= 10; i++) {
                    lock.lock();
                    try {
                        while (count == BUFFER_SIZE) {
                            notFull.await();
                        }
                        buffer[count++] = i;
                        System.out.println("  Produced: " + i);
                        notEmpty.signal();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                    try { Thread.sleep(50); } catch (InterruptedException e) { return; }
                }
            });

            Thread consumer = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    lock.lock();
                    try {
                        while (count == 0) {
                            notEmpty.await();
                        }
                        int value = buffer[--count];
                        System.out.println("  Consumed: " + value);
                        notFull.signal();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                    try { Thread.sleep(100); } catch (InterruptedException e) { return; }
                }
            });

            producer.start();
            consumer.start();
            producer.join();
            consumer.join();
            System.out.println();
        }
    }

    /**
     * Example 5: tryLock for Deadlock Avoidance
     * Demonstrates using tryLock to avoid deadlock.
     */
    static class Example5_TryLockDeadlock {
        private static final ReentrantLock lockA = new ReentrantLock();
        private static final ReentrantLock lockB = new ReentrantLock();

        public static void main(String[] args) throws InterruptedException {
            System.out.println("Example 5: tryLock Deadlock Avoidance");
            System.out.println("======================================");

            Thread t1 = new Thread(() -> {
                while (true) {
                    if (lockA.tryLock()) {
                        try {
                            if (lockB.tryLock()) {
                                try {
                                    System.out.println("  T1: acquired both locks");
                                    break;
                                } finally {
                                    lockB.unlock();
                                }
                            }
                        } finally {
                            lockA.unlock();
                        }
                    }
                    Thread.yield();
                }
            });

            Thread t2 = new Thread(() -> {
                while (true) {
                    if (lockB.tryLock()) {
                        try {
                            if (lockA.tryLock()) {
                                try {
                                    System.out.println("  T2: acquired both locks");
                                    break;
                                } finally {
                                    lockA.unlock();
                                }
                            }
                        } finally {
                            lockB.unlock();
                        }
                    }
                    Thread.yield();
                }
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println("  Both threads completed without deadlock");
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Example1_ReentrantLock.main(args);
        Example2_ReadWriteLock.main(args);
        Example3_StampedLock.main(args);
        Example4_Conditions.main(args);
        Example5_TryLockDeadlock.main(args);
    }
}
