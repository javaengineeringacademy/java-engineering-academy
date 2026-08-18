package academy.javaengineering.concurrency.examples;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class LockExamples {

    public static void main(String[] args) throws InterruptedException {
        example1_ReentrantLock();
        example2_LockWithCondition();
        example3_TryLock();
        example4_ReadWriteLock();
        example5_StampedLock();
    }

    // Example 1: ReentrantLock basics
    static void example1_ReentrantLock() throws InterruptedException {
        System.out.println("=== Example 1: ReentrantLock Basics ===");

        ReentrantLock lock = new ReentrantLock();
        int[] counter = {0};

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                lock.lock();
                try {
                    counter[0]++;
                } finally {
                    lock.unlock(); // Always unlock in finally
                }
            }
        };

        Thread t1 = new Thread(task, "RL-1");
        Thread t2 = new Thread(task, "RL-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("ReentrantLock counter: " + counter[0]);

        // ReentrantLock is reentrant - same thread can lock multiple times
        ReentrantLock reentrant = new ReentrantLock();
        reentrant.lock();
        System.out.println("Hold count after first lock: " + reentrant.getHoldCount());
        reentrant.lock();
        System.out.println("Hold count after second lock: " + reentrant.getHoldCount());
        reentrant.unlock();
        System.out.println("Hold count after first unlock: " + reentrant.getHoldCount());
        reentrant.unlock();
        System.out.println("Hold count after second unlock: " + reentrant.getHoldCount());

        System.out.println();
    }

    // Example 2: ReentrantLock with Condition
    static void example2_LockWithCondition() throws InterruptedException {
        System.out.println("=== Example 2: Lock with Condition ===");

        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    buffer.put(i);
                    System.out.println("Produced: " + i);
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "LockProducer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 8; i++) {
                    int item = buffer.get();
                    System.out.println("Consumed: " + item);
                    TimeUnit.MILLISECONDS.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "LockConsumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println();
    }

    // Example 3: tryLock() and lockInterruptibly()
    static void example3_TryLock() throws InterruptedException {
        System.out.println("=== Example 3: tryLock() ===");

        ReentrantLock lock = new ReentrantLock();

        // tryLock() - non-blocking attempt
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread-1 holds lock, doing work...");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }, "TryLock-1");

        t1.start();
        TimeUnit.MILLISECONDS.sleep(100);

        // Try to acquire lock with timeout
        Thread t2 = new Thread(() -> {
            try {
                System.out.println("Thread-2 attempting tryLock with 500ms timeout...");
                boolean acquired = lock.tryLock(500, TimeUnit.MILLISECONDS);
                if (acquired) {
                    try {
                        System.out.println("Thread-2 acquired lock!");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Thread-2 could not acquire lock within timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "TryLock-2");

        t2.start();
        t1.join();
        t2.join();

        // lockInterruptibly() example
        System.out.println("\nlockInterruptibly() example:");
        ReentrantLock lock2 = new ReentrantLock();

        Thread holder = new Thread(() -> {
            lock2.lock();
            try {
                System.out.println("Holder thread has the lock");
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock2.unlock();
            }
        }, "LockHolder");

        Thread interruptible = new Thread(() -> {
            try {
                System.out.println("Interruptible thread trying to acquire lock...");
                lock2.lockInterruptibly(); // Can be interrupted while waiting
                System.out.println("Interruptible thread acquired lock");
                lock2.unlock();
            } catch (InterruptedException e) {
                System.out.println("Interruptible thread was interrupted while waiting!");
            }
        }, "InterruptibleThread");

        holder.start();
        TimeUnit.MILLISECONDS.sleep(100);
        interruptible.start();

        TimeUnit.MILLISECONDS.sleep(300);
        interruptible.interrupt(); // Interrupt the waiting thread

        holder.join();
        interruptible.join();

        System.out.println();
    }

    // Example 4: ReadWriteLock
    static void example4_ReadWriteLock() throws InterruptedException {
        System.out.println("=== Example 4: ReadWriteLock ===");

        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        int[] sharedData = {0, 0, 0};

        // Multiple readers can read simultaneously
        Runnable reader = () -> {
            for (int i = 0; i < 5; i++) {
                rwLock.readLock().lock();
                try {
                    System.out.println(Thread.currentThread().getName() +
                            " reading: [" + sharedData[0] + ", " + sharedData[1] + ", " + sharedData[2] + "]");
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.readLock().unlock();
                }
            }
        };

        // Writers need exclusive access
        Runnable writer = () -> {
            for (int i = 0; i < 5; i++) {
                rwLock.writeLock().lock();
                try {
                    sharedData[0] = i;
                    sharedData[1] = i * 2;
                    sharedData[2] = i * 3;
                    System.out.println(Thread.currentThread().getName() +
                            " writing: [" + sharedData[0] + ", " + sharedData[1] + ", " + sharedData[2] + "]");
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.writeLock().unlock();
                }
            }
        };

        Thread[] readers = new Thread[3];
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Thread(reader, "Reader-" + i);
        }

        Thread writerThread = new Thread(writer, "Writer");

        for (Thread t : readers) t.start();
        writerThread.start();

        for (Thread t : readers) t.join();
        writerThread.join();

        System.out.println();
    }

    // Example 5: StampedLock
    static void example5_StampedLock() throws InterruptedException {
        System.out.println("=== Example 5: StampedLock ===");

        StampedLock stampedLock = new StampedLock();
        double[] point = {0.0, 0.0};

        // Optimistic read - doesn't acquire lock
        Runnable optimisticReader = () -> {
            for (int i = 0; i < 5; i++) {
                long stamp = stampedLock.tryOptimisticRead();
                double x = point[0];
                double y = point[1];
                if (!stampedLock.validate(stamp)) {
                    // Fallback to read lock
                    stamp = stampedLock.readLock();
                    try {
                        x = point[0];
                        y = point[1];
                    } finally {
                        stampedLock.unlockRead(stamp);
                    }
                }
                System.out.println(Thread.currentThread().getName() +
                        " optimistic read: (" + x + ", " + y + ")");
                try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        };

        // Write lock
        Runnable writer = () -> {
            for (int i = 1; i <= 5; i++) {
                long stamp = stampedLock.writeLock();
                try {
                    point[0] = i * 1.0;
                    point[1] = i * 2.0;
                    System.out.println(Thread.currentThread().getName() +
                            " wrote: (" + point[0] + ", " + point[1] + ")");
                } finally {
                    stampedLock.unlockWrite(stamp);
                }
                try { TimeUnit.MILLISECONDS.sleep(40); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        };

        Thread[] readers = new Thread[2];
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Thread(optimisticReader, "OptimisticReader-" + i);
        }
        Thread writerThread = new Thread(writer, "StampedWriter");

        for (Thread t : readers) t.start();
        writerThread.start();

        for (Thread t : readers) t.join();
        writerThread.join();

        System.out.println();
    }

    // Bounded buffer using Lock and Condition
    static class BoundedBuffer<T> {
        private final java.util.LinkedList<T> buffer = new java.util.LinkedList<>();
        private final int capacity;
        private final Lock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();

        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public void put(T item) throws InterruptedException {
            lock.lock();
            try {
                while (buffer.size() >= capacity) {
                    notFull.await();
                }
                buffer.addLast(item);
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        public T get() throws InterruptedException {
            lock.lock();
            try {
                while (buffer.isEmpty()) {
                    notEmpty.await();
                }
                T item = buffer.removeFirst();
                notFull.signal();
                return item;
            } finally {
                lock.unlock();
            }
        }
    }
}
