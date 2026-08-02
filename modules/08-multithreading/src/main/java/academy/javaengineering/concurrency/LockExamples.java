package academy.javaengineering.concurrency;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates ReentrantLock and ReadWriteLock usage.
 * Shows advanced locking mechanisms beyond synchronized.
 */
public class LockExamples {

    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final List<String> sharedList = new ArrayList<>();
    private int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        LockExamples example = new LockExamples();
        example.demonstrateReentrantLock();
        example.demonstrateReadWriteLock();
        example.demonstrateTryLock();
    }

    /**
     * Demonstrates ReentrantLock for thread-safe operations.
     */
    public void demonstrateReentrantLock() throws InterruptedException {
        counter = 0;
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    reentrantLock.lock();
                    try {
                        counter++;
                    } finally {
                        reentrantLock.unlock();
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("ReentrantLock Counter: " + counter);
        // Expected output: ReentrantLock Counter: 10000
    }

    /**
     * Demonstrates ReadWriteLock for read-heavy scenarios.
     */
    public void demonstrateReadWriteLock() throws InterruptedException {
        sharedList.clear();

        // Writer thread
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                readWriteLock.writeLock().lock();
                try {
                    sharedList.add("Item " + i);
                    System.out.println("Writer added: Item " + i);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    readWriteLock.writeLock().unlock();
                }
            }
        });

        // Reader threads
        Thread[] readers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int readerId = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    readWriteLock.readLock().lock();
                    try {
                        System.out.println("Reader " + readerId + " sees " + sharedList.size() + " items");
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        readWriteLock.readLock().unlock();
                    }
                }
            });
        }

        writer.start();
        for (Thread reader : readers) {
            reader.start();
        }

        writer.join();
        for (Thread reader : readers) {
            reader.join();
        }

        System.out.println("Final list size: " + sharedList.size());
        // Expected output: Final list size: 5
    }

    /**
     * Demonstrates tryLock to avoid deadlocks.
     */
    public void demonstrateTryLock() throws InterruptedException {
        final ReentrantLock lock1 = new ReentrantLock();
        final ReentrantLock lock2 = new ReentrantLock();

        Thread thread1 = new Thread(() -> {
            try {
                if (lock1.tryLock(100, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("Thread 1: holding lock1");
                        Thread.sleep(50);
                        if (lock2.tryLock(100, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("Thread 1: holding lock1 and lock2");
                            } finally {
                                lock2.unlock();
                            }
                        } else {
                            System.out.println("Thread 1: could not acquire lock2");
                        }
                    } finally {
                        lock1.unlock();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                if (lock2.tryLock(100, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    try {
                        System.out.println("Thread 2: holding lock2");
                        Thread.sleep(50);
                        if (lock1.tryLock(100, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("Thread 2: holding lock2 and lock1");
                            } finally {
                                lock1.unlock();
                            }
                        } else {
                            System.out.println("Thread 2: could not acquire lock1");
                        }
                    } finally {
                        lock2.unlock();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        // Expected output: One thread will successfully acquire both locks
    }
}
