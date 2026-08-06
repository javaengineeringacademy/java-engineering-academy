package multithreading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * LockExamples - ReentrantLock, ReadWriteLock, StampedLock
 *
 * Covers:
 * - ReentrantLock for explicit locking
 * - ReadWriteLock for read-heavy scenarios
 * - StampedLock for optimistic reads
 * - Condition for thread coordination
 */
public class LockExamples {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ReentrantLock ===");
        reentrantLockDemo();

        System.out.println("\n=== ReadWriteLock ===");
        readWriteLockDemo();

        System.out.println("\n=== StampedLock ===");
        stampedLockDemo();

        System.out.println("\n=== Condition ===");
        conditionDemo();
    }

    static void reentrantLockDemo() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        int[] counter = {0};
        int numThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    lock.lock();
                    try {
                        counter[0]++;
                    } finally {
                        lock.unlock();
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("ReentrantLock result: " + counter[0]);
        System.out.println("Expected: " + (numThreads * incrementsPerThread));

        // Try-lock demo
        ReentrantLock tryLock = new ReentrantLock();
        boolean acquired = tryLock.tryLock();
        System.out.println("tryLock acquired: " + acquired);
        if (acquired) {
            tryLock.unlock();
        }
    }

    static void readWriteLockDemo() throws InterruptedException {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        Lock readLock = rwLock.readLock();
        Lock writeLock = rwLock.writeLock();

        int[] data = {0};
        int numReaders = 5;
        int numWriters = 2;

        // Readers
        Thread[] readers = new Thread[numReaders];
        for (int i = 0; i < numReaders; i++) {
            final int readerId = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    readLock.lock();
                    try {
                        System.out.println("Reader " + readerId + " read: " + data[0]);
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        readLock.unlock();
                    }
                }
            });
        }

        // Writers
        Thread[] writers = new Thread[numWriters];
        for (int i = 0; i < numWriters; i++) {
            final int writerId = i;
            writers[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    writeLock.lock();
                    try {
                        data[0]++;
                        System.out.println("Writer " + writerId + " wrote: " + data[0]);
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        writeLock.unlock();
                    }
                }
            });
        }

        for (Thread t : readers) t.start();
        for (Thread t : writers) t.start();

        for (Thread t : readers) t.join();
        for (Thread t : writers) t.join();

        System.out.println("Final value: " + data[0]);
    }

    static void stampedLockDemo() throws InterruptedException {
        StampedLock stampedLock = new StampedLock();
        int[] data = {0};

        // Optimistic read
        Thread reader = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                long stamp = stampedLock.tryOptimisticRead();
                int currentData = data[0];
                if (!stampedLock.validate(stamp)) {
                    // Fallback to read lock
                    stamp = stampedLock.readLock();
                    try {
                        currentData = data[0];
                    } finally {
                        stampedLock.unlockRead(stamp);
                    }
                }
                System.out.println("Optimistic read: " + currentData);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Writer
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                long stamp = stampedLock.writeLock();
                try {
                    data[0]++;
                    System.out.println("Written: " + data[0]);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    stampedLock.unlockWrite(stamp);
                }
            }
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("Final value: " + data[0]);
    }

    static void conditionDemo() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition notEmpty = lock.newCondition();
        Condition notFull = lock.newCondition();

        int[] buffer = new int[5];
        int count = 0;

        // Producer
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (count == buffer.length) {
                        notFull.await();
                    }
                    buffer[count++] = i;
                    System.out.println("Produced: " + i + ", count: " + count);
                    notEmpty.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Consumer
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (count == 0) {
                        notEmpty.await();
                    }
                    int value = buffer[--count];
                    System.out.println("Consumed: " + value + ", count: " + count);
                    notFull.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}