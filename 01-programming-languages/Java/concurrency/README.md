# Java Concurrency

## Table of Contents

1. [Thread Creation](#thread-creation)
2. [Synchronization](#synchronization)
3. [Locks](#locks)
4. [Atomic Variables](#atomic-variables)
5. [Executor Service](#executor-service)
6. [CompletableFuture](#completablefuture)
7. [Virtual Threads](#virtual-threads)
8. [StructuredTaskScope](#structuredtaskscope)
9. [Concurrent Collections](#concurrent-collections)

---

## Thread Creation

### Creating Threads

```java
public class ThreadCreation {
    
    // Method 1: Extending Thread class
    static class MyThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    
    // Method 2: Implementing Runnable interface
    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    
    // Method 3: Lambda expression (Java 8+)
    static Runnable lambdaRunnable = () -> {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    };
    
    // Method 4: Callable (returns result)
    static class MyCallable implements java.util.concurrent.Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
                Thread.sleep(50);
            }
            return sum;
        }
    }
    
    public static void main(String[] args) throws Exception {
        // Method 1: Extending Thread
        System.out.println("=== Extending Thread ===");
        MyThread thread1 = new MyThread();
        thread1.start();
        
        // Method 2: Implementing Runnable
        System.out.println("\n=== Implementing Runnable ===");
        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();
        
        // Method 3: Lambda expression
        System.out.println("\n=== Lambda Expression ===");
        Thread thread3 = new Thread(lambdaRunnable);
        thread3.start();
        
        // Method 4: Callable with FutureTask
        System.out.println("\n=== Callable ===");
        java.util.concurrent.FutureTask<Integer> futureTask = 
            new java.util.concurrent.FutureTask<>(new MyCallable());
        Thread thread4 = new Thread(futureTask);
        thread4.start();
        
        // Wait for results
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        
        Integer result = futureTask.get();
        System.out.println("Callable result: " + result);
        
        // Thread methods
        System.out.println("\n=== Thread Methods ===");
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread: " + currentThread.getName());
        System.out.println("Thread ID: " + currentThread.getId());
        System.out.println("Thread priority: " + currentThread.getPriority());
        System.out.println("Thread state: " + currentThread.getState());
        System.out.println("Thread group: " + currentThread.getThreadGroup().getName());
        
        // Thread states
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        System.out.println("Before start: " + thread.getState()); // NEW
        thread.start();
        System.out.println("After start: " + thread.getState());  // RUNNABLE
        Thread.sleep(100);
        System.out.println("During sleep: " + thread.getState()); // TIMED_WAITING
        thread.join();
        System.out.println("After join: " + thread.getState());   // TERMINATED
    }
}
```

### Thread Scheduling

```java
public class ThreadScheduling {
    
    public static void main(String[] args) throws InterruptedException {
        // Thread priorities
        Thread highPriority = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("High priority: " + i);
                Thread.yield();
            }
        });
        
        Thread lowPriority = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Low priority: " + i);
                Thread.yield();
            }
        });
        
        highPriority.setPriority(Thread.MAX_PRIORITY);  // 10
        lowPriority.setPriority(Thread.MIN_PRIORITY);   // 1
        
        highPriority.start();
        lowPriority.start();
        
        highPriority.join();
        lowPriority.join();
        
        // Daemon threads
        Thread daemonThread = new Thread(() -> {
            int count = 0;
            while (true) {
                System.out.println("Daemon thread: " + count++);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        daemonThread.setDaemon(true);  // Must be set before start
        daemonThread.start();
        
        Thread.sleep(500);  // Daemon thread runs in background
        
        System.out.println("Main thread finished");
        // Daemon thread is automatically terminated when all non-daemon threads finish
    }
}
```

### Thread Groups

```java
public class ThreadGroups {
    
    public static void main(String[] args) throws InterruptedException {
        // Create thread group
        ThreadGroup group = new ThreadGroup("WorkerGroup");
        
        // Create threads in group
        Thread thread1 = new Thread(group, () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread 1: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "Worker1");
        
        Thread thread2 = new Thread(group, () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread 2: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "Worker2");
        
        // Set thread properties
        group.setMaxPriority(8);
        thread1.setPriority(5);
        thread2.setPriority(7);
        
        // Start threads
        thread1.start();
        thread2.start();
        
        // Group operations
        System.out.println("Active count: " + group.activeCount());
        System.out.println("Group name: " + group.getName());
        
        // List all threads in group
        group.list();
        
        // Interrupt all threads in group
        Thread.sleep(300);
        group.interrupt();
        
        // Wait for all threads to finish
        thread1.join();
        thread2.join();
        
        System.out.println("All threads finished");
    }
}
```

---

## Synchronization

### Synchronized Methods and Blocks

```java
public class SynchronizationExample {
    
    // Shared resource
    private int count = 0;
    private String message = "";
    
    // Synchronized method
    public synchronized void increment() {
        count++;
    }
    
    // Synchronized block
    public void decrement() {
        synchronized (this) {
            count--;
        }
    }
    
    // Synchronized on specific object
    public void updateMessage(String newMessage) {
        synchronized (message) {
            message = newMessage;
        }
    }
    
    // Static synchronized method
    private static int staticCount = 0;
    
    public static synchronized void incrementStatic() {
        staticCount++;
    }
    
    // Read-write synchronization
    private int balance = 1000;
    private final Object readLock = new Object();
    private final Object writeLock = new Object();
    
    public int readBalance() {
        synchronized (readLock) {
            return balance;
        }
    }
    
    public void writeBalance(int amount) {
        synchronized (writeLock) {
            balance += amount;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        SynchronizationExample example = new SynchronizationExample();
        
        // Create multiple threads
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    example.increment();
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("Final count: " + example.count);
        // Should be 10000 (10 threads * 1000 increments)
    }
}
```

### Wait/Notify

```java
public class WaitNotifyExample {
    
    private static final Object lock = new Object();
    private static boolean hasData = false;
    private static String data = "";
    
    // Producer
    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    // Wait while data is available
                    while (hasData) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    
                    // Produce data
                    data = "Data " + i;
                    hasData = true;
                    System.out.println("Produced: " + data);
                    
                    // Notify consumer
                    lock.notify();
                }
            }
        }
    }
    
    // Consumer
    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    // Wait while data is not available
                    while (!hasData) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    
                    // Consume data
                    System.out.println("Consumed: " + data);
                    hasData = false;
                    data = "";
                    
                    // Notify producer
                    lock.notify();
                }
            }
        }
    }
    
    // Wait/NotifyAll example
    private static final Object monitor = new Object();
    private static int readyCount = 0;
    private static final int REQUIRED_COUNT = 3;
    
    static class Worker implements Runnable {
        private final int id;
        
        Worker(int id) {
            this.id = id;
        }
        
        @Override
        public void run() {
            try {
                // Simulate work
                Thread.sleep(100 * id);
                
                synchronized (monitor) {
                    readyCount++;
                    System.out.println("Worker " + id + " ready (" + readyCount + "/" + REQUIRED_COUNT + ")");
                    
                    if (readyCount < REQUIRED_COUNT) {
                        monitor.wait();
                    } else {
                        // Last worker wakes everyone up
                        System.out.println("All workers ready, starting...");
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Producer-Consumer
        System.out.println("=== Producer-Consumer ===");
        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        
        // Wait/NotifyAll
        System.out.println("\n=== Wait/NotifyAll ===");
        Thread[] workers = new Thread[REQUIRED_COUNT];
        for (int i = 0; i < REQUIRED_COUNT; i++) {
            workers[i] = new Thread(new Worker(i + 1));
            workers[i].start();
        }
        
        for (Thread worker : workers) {
            worker.join();
        }
    }
}
```

### Volatile Keyword

```java
public class VolatileExample {
    
    // Without volatile: may not see updates from other threads
    // With volatile: guarantees visibility of writes to all threads
    private volatile boolean running = true;
    private volatile int counter = 0;
    
    // Volatile ensures visibility but not atomicity
    // Use synchronized or atomic variables for compound operations
    
    public void run() {
        while (running) {
            counter++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Counter: " + counter);
    }
    
    public void stop() {
        running = false;
    }
    
    // Double-checked locking with volatile
    private static volatile VolatileExample instance;
    
    public static VolatileExample getInstance() {
        if (instance == null) {  // First check (no synchronization)
            synchronized (VolatileExample.class) {
                if (instance == null) {  // Second check (with synchronization)
                    instance = new VolatileExample();
                }
            }
        }
        return instance;
    }
    
    // Volatile for flags
    private static volatile boolean flag = false;
    
    public static void main(String[] args) throws InterruptedException {
        VolatileExample example = new VolatileExample();
        
        Thread thread = new Thread(example::run);
        thread.start();
        
        Thread.sleep(500);
        example.stop();
        
        thread.join();
        System.out.println("Thread stopped");
        
        // Flag example
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(100);
                flag = true;  // Volatile write
                System.out.println("Flag set to true");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        Thread reader = new Thread(() -> {
            while (!flag) {  // Volatile read
                Thread.yield();
            }
            System.out.println("Flag detected as true");
        });
        
        writer.start();
        reader.start();
        
        writer.join();
        reader.join();
    }
}
```

---

## Locks

### ReentrantLock

```java
import java.util.concurrent.locks.*;

public class ReentrantLockExample {
    
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    private final int[] buffer = new int[10];
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    
    // Producer
    public void put(int value) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                notFull.await();  // Wait until buffer is not full
            }
            
            buffer[putIndex] = value;
            putIndex = (putIndex + 1) % buffer.length;
            count++;
            
            System.out.println("Put: " + value + " (count: " + count + ")");
            notEmpty.signal();  // Signal that buffer is not empty
        } finally {
            lock.unlock();
        }
    }
    
    // Consumer
    public int take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();  // Wait until buffer is not empty
            }
            
            int value = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % buffer.length;
            count--;
            
            System.out.println("Take: " + value + " (count: " + count + ")");
            notFull.signal();  // Signal that buffer is not full
            
            return value;
        } finally {
            lock.unlock();
        }
    }
    
    // TryLock example
    private final ReentrantLock tryLock = new ReentrantLock();
    
    public void tryLockExample() {
        if (tryLock.tryLock()) {
            try {
                System.out.println("Lock acquired");
                // Critical section
            } finally {
                tryLock.unlock();
            }
        } else {
            System.out.println("Could not acquire lock");
        }
    }
    
    // TryLock with timeout
    public void tryLockWithTimeout() throws InterruptedException {
        if (tryLock.tryLock(1, java.util.concurrent.TimeUnit.SECONDS)) {
            try {
                System.out.println("Lock acquired with timeout");
                // Critical section
            } finally {
                tryLock.unlock();
            }
        } else {
            System.out.println("Timeout waiting for lock");
        }
    }
    
    // Lock interruptibly
    public void lockInterruptibly() throws InterruptedException {
        tryLock.lockInterruptibly();
        try {
            System.out.println("Lock acquired interruptibly");
            // Critical section
        } finally {
            tryLock.unlock();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        ReentrantLockExample example = new ReentrantLockExample();
        
        // Producer-Consumer with locks
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    example.put(i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    example.take();
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        
        System.out.println("Producer-Consumer completed");
    }
}
```

### ReadWriteLock

```java
import java.util.concurrent.locks.*;
import java.util.*;

public class ReadWriteLockExample {
    
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    
    private final Map<String, Integer> cache = new HashMap<>();
    
    // Multiple readers can read simultaneously
    public Integer read(String key) {
        readLock.lock();
        try {
            System.out.println("Reading " + key + " by " + Thread.currentThread().getName());
            Thread.sleep(100);  // Simulate read operation
            return cache.get(key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            readLock.unlock();
        }
    }
    
    // Only one writer can write, and no readers during write
    public void write(String key, Integer value) throws InterruptedException {
        writeLock.lock();
        try {
            System.out.println("Writing " + key + " by " + Thread.currentThread().getName());
            Thread.sleep(200);  // Simulate write operation
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
    
    // StampedLock (Java 8+)
    private final StampedLock stampedLock = new StampedLock();
    private final Map<String, Integer> stampedCache = new HashMap<>();
    
    public Integer readWithStamp(String key) {
        long stamp = stampedLock.tryOptimisticRead();  // Try optimistic read
        Integer value = stampedCache.get(key);
        
        if (!stampedLock.validate(stamp)) {
            // Fallback to pessimistic read
            stamp = stampedLock.readLock();
            try {
                value = stampedCache.get(key);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        
        return value;
    }
    
    public void writeWithStamp(String key, Integer value) {
        long stamp = stampedLock.writeLock();
        try {
            stampedCache.put(key, value);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
    
    // Convert to read lock
    public Integer readWithConvert(String key) {
        long stamp = stampedLock.writeLock();
        try {
            // Convert to read lock
            stamp = stampedLock.tryConvertToReadLock(stamp);
            Integer value = stampedCache.get(key);
            return value;
        } finally {
            stampedLock.unlock(stamp);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockExample example = new ReadWriteLockExample();
        
        // Multiple readers and writers
        Thread[] readers = new Thread[5];
        Thread[] writers = new Thread[2];
        
        for (int i = 0; i < readers.length; i++) {
            final int readerId = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    example.write("key" + readerId, readerId * 10 + j);
                }
            });
        }
        
        for (int i = 0; i < writers.length; i++) {
            final int writerId = i;
            writers[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    try {
                        example.write("key" + writerId, writerId * 10 + j);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Start all threads
        for (Thread reader : readers) {
            reader.start();
        }
        for (Thread writer : writers) {
            writer.start();
        }
        
        // Wait for completion
        for (Thread reader : readers) {
            reader.join();
        }
        for (Thread writer : writers) {
            writer.join();
        }
        
        System.out.println("All operations completed");
    }
}
```

---

## Atomic Variables

### AtomicInteger, AtomicLong, AtomicBoolean

```java
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

public class AtomicVariablesExample {
    
    // Atomic counter
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);
    
    // Atomic long for large counts
    private static final AtomicLong atomicLongCounter = new AtomicLong(0);
    
    // Atomic boolean for flags
    private static final AtomicBoolean atomicFlag = new AtomicBoolean(false);
    
    // Atomic reference
    private static final AtomicReference<String> atomicReference = new AtomicReference<>("initial");
    
    // Atomic stamp reference (for ABA problem)
    private static final AtomicStampedReference<String> atomicStampedReference = 
        new AtomicStampedReference<>("initial", 0);
    
    public static void main(String[] args) throws InterruptedException {
        // AtomicInteger operations
        System.out.println("=== AtomicInteger ===");
        
        atomicCounter.set(0);
        System.out.println("Set to 0: " + atomicCounter.get());
        
        atomicCounter.incrementAndGet();
        System.out.println("Increment and get: " + atomicCounter.get());
        
        atomicCounter.getAndIncrement();
        System.out.println("Get and increment: " + atomicCounter.get());
        
        atomicCounter.addAndGet(10);
        System.out.println("Add 10 and get: " + atomicCounter.get());
        
        atomicCounter.compareAndSet(11, 100);
        System.out.println("Compare and set (11->100): " + atomicCounter.get());
        
        // AtomicLong operations
        System.out.println("\n=== AtomicLong ===");
        
        atomicLongCounter.set(0L);
        atomicLongCounter.accumulateAndGet(10L, Long::sum);
        System.out.println("Accumulate 10: " + atomicLongCounter.get());
        
        // AtomicBoolean operations
        System.out.println("\n=== AtomicBoolean ===");
        
        atomicFlag.set(false);
        System.out.println("Set to false: " + atomicFlag.get());
        
        atomicFlag.compareAndSet(false, true);
        System.out.println("Compare and set (false->true): " + atomicFlag.get());
        
        // AtomicReference operations
        System.out.println("\n=== AtomicReference ===");
        
        atomicReference.set("initial");
        System.out.println("Set to initial: " + atomicReference.get());
        
        atomicReference.compareAndSet("initial", "updated");
        System.out.println("Compare and set: " + atomicReference.get());
        
        // AtomicStampedReference (for ABA problem)
        System.out.println("\n=== AtomicStampedReference ===");
        
        int[] stampHolder = new int[1];
        String value = atomicStampedReference.get(stampHolder);
        int stamp = stampHolder[0];
        System.out.println("Initial: " + value + " (stamp: " + stamp + ")");
        
        atomicStampedReference.compareAndSet("initial", "updated", stamp, stamp + 1);
        value = atomicStampedReference.get(stampHolder);
        stamp = stampHolder[0];
        System.out.println("Updated: " + value + " (stamp: " + stamp + ")");
        
        // Multi-threaded atomic operations
        System.out.println("\n=== Multi-threaded Atomic Operations ===");
        
        AtomicInteger multiThreadCounter = new AtomicInteger(0);
        int threadCount = 10;
        int incrementsPerThread = 1000;
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    multiThreadCounter.incrementAndGet();
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("Final count: " + multiThreadCounter.get());
        System.out.println("Expected: " + (threadCount * incrementsPerThread));
        
        // Atomic arrays
        System.out.println("\n=== Atomic Integer Array ===");
        
        AtomicIntegerArray atomicArray = new AtomicIntegerArray(10);
        
        Thread[] arrayThreads = new Thread[5];
        for (int i = 0; i < arrayThreads.length; i++) {
            final int index = i;
            arrayThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicArray.incrementAndGet(index % 10);
                }
            });
            arrayThreads[i].start();
        }
        
        for (Thread thread : arrayThreads) {
            thread.join();
        }
        
        System.out.println("Array values:");
        for (int i = 0; i < 10; i++) {
            System.out.print(atomicArray.get(i) + " ");
        }
        System.out.println();
    }
}
```

---

## Executor Service

### ThreadPoolExecutor

```java
import java.util.concurrent.*;
import java.util.*;

public class ThreadPoolExecutorExample {
    
    public static void main(String[] args) throws InterruptedException {
        // Creating ThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                              // core pool size
            4,                              // maximum pool size
            60L,                            // keep alive time
            TimeUnit.SECONDS,              // time unit
            new LinkedBlockingQueue<>(10),  // work queue
            new ThreadPoolExecutor.CallerRunsPolicy()  // rejection policy
        );
        
        // Submit tasks
        for (int i = 0; i < 15; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " running on " + 
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Monitor executor
        System.out.println("Pool size: " + executor.getPoolSize());
        System.out.println("Active count: " + executor.getActiveCount());
        System.out.println("Completed tasks: " + executor.getCompletedTaskCount());
        System.out.println("Queue size: " + executor.getQueue().size());
        
        // Shutdown executor
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Executor shutdown: " + executor.isShutdown());
        System.out.println("Executor terminated: " + executor.isTerminated());
    }
}
```

### Executors Factory Methods

```java
import java.util.concurrent.*;
import java.util.*;

public class ExecutorsExample {
    
    public static void main(String[] args) throws InterruptedException {
        // Fixed thread pool
        ExecutorService fixedPool = Executors.newFixedThreadPool(4);
        
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            fixedPool.submit(() -> {
                System.out.println("Fixed pool task " + taskId + " on " + 
                    Thread.currentThread().getName());
            });
        }
        
        fixedPool.shutdown();
        fixedPool.awaitTermination(5, TimeUnit.SECONDS);
        
        // Cached thread pool
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            cachedPool.submit(() -> {
                System.out.println("Cached pool task " + taskId + " on " + 
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        cachedPool.shutdown();
        cachedPool.awaitTermination(5, TimeUnit.SECONDS);
        
        // Single thread executor
        ExecutorService singleThread = Executors.newSingleThreadExecutor();
        
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            singleThread.submit(() -> {
                System.out.println("Single thread task " + taskId + " on " + 
                    Thread.currentThread().getName());
            });
        }
        
        singleThread.shutdown();
        singleThread.awaitTermination(5, TimeUnit.SECONDS);
        
        // Scheduled thread pool
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);
        
        // Schedule with delay
        scheduledPool.schedule(() -> {
            System.out.println("Scheduled task (delayed)");
        }, 1, TimeUnit.SECONDS);
        
        // Schedule at fixed rate
        scheduledPool.scheduleAtFixedRate(() -> {
            System.out.println("Scheduled task (fixed rate): " + System.currentTimeMillis());
        }, 0, 500, TimeUnit.MILLISECONDS);
        
        // Schedule with fixed delay
        scheduledPool.scheduleWithFixedDelay(() -> {
            System.out.println("Scheduled task (fixed delay): " + System.currentTimeMillis());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        
        // Let scheduled tasks run
        Thread.sleep(3000);
        
        scheduledPool.shutdown();
        scheduledPool.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("All executors completed");
    }
}
```

### Future and Callable

```java
import java.util.concurrent.*;
import java.util.*;

public class FutureCallableExample {
    
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // Submit Callable and get Future
        Callable<Integer> callable = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
            return sum;
        };
        
        Future<Integer> future = executor.submit(callable);
        
        // Check if task is done
        System.out.println("Is done: " + future.isDone());
        
        // Get result (blocks until complete)
        Integer result = future.get();
        System.out.println("Result: " + result);
        
        // Submit multiple tasks
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            futures.add(executor.submit(() -> {
                Thread.sleep(100 * taskId);
                return "Task " + taskId + " completed";
            }));
        }
        
        // Get results in order
        for (Future<String> f : futures) {
            System.out.println(f.get());
        }
        
        // Submit tasks and process results
        List<Callable<Integer>> callables = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            callables.add(() -> {
                Thread.sleep(50);
                return taskId * taskId;
            });
        }
        
        // invokeAny - returns first completed result
        Integer anyResult = executor.invokeAny(callables);
        System.out.println("Any result: " + anyResult);
        
        // invokeAll - returns all results
        List<Future<Integer>> allFutures = executor.invokeAll(callables);
        List<Integer> allResults = new ArrayList<>();
        for (Future<Integer> f : allFutures) {
            allResults.add(f.get());
        }
        System.out.println("All results: " + allResults);
        
        // Future with timeout
        Future<Integer> timeoutFuture = executor.submit(() -> {
            Thread.sleep(1000);
            return 42;
        });
        
        try {
            Integer timeoutResult = timeoutFuture.get(100, TimeUnit.MILLISECONDS);
            System.out.println("Timeout result: " + timeoutResult);
        } catch (TimeoutException e) {
            System.out.println("Task timed out");
            timeoutFuture.cancel(true);  // Cancel the task
        }
        
        // FutureTask
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            return "FutureTask completed";
        });
        
        new Thread(futureTask).start();
        System.out.println("FutureTask: " + futureTask.get());
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
```

---

## CompletableFuture

### Basic CompletableFuture

```java
import java.util.concurrent.*;
import java.util.*;

public class CompletableFutureBasic {
    
    public static void main(String[] args) throws Exception {
        // Creating CompletableFuture
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "Hello, World!";
        });
        
        System.out.println("Result: " + future.get());
        
        // Chaining operations
        CompletableFuture<Integer> lengthFuture = CompletableFuture
            .supplyAsync(() -> "Hello, World!")
            .thenApply(String::length)
            .thenApply(length -> length * 2);
        
        System.out.println("Length * 2: " + lengthFuture.get());
        
        // Accept result without returning
        CompletableFuture<Void> acceptFuture = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenAccept(System.out::println);
        
        acceptFuture.get();
        
        // Accept both input and result
        CompletableFuture<Void> runFuture = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenRun(() -> System.out.println("Completed"));
        
        runFuture.get();
        
        // Combine two CompletableFutures
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");
        
        CompletableFuture<String> combined = future1.thenCombine(future2, 
            (s1, s2) -> s1 + " " + s2);
        
        System.out.println("Combined: " + combined.get());
        
        // Combine with BiFunction
        CompletableFuture<Integer> intFuture1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> intFuture2 = CompletableFuture.supplyAsync(() -> 20);
        
        CompletableFuture<Integer> sumFuture = intFuture1.thenCombine(intFuture2, 
            Integer::sum);
        
        System.out.println("Sum: " + sumFuture.get());
        
        // ThenCompose (flatMap equivalent)
        CompletableFuture<String> composedFuture = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
        
        System.out.println("Composed: " + composedFuture.get());
    }
}
```

### Handling Exceptions

```java
import java.util.concurrent.*;
import java.util.*;

public class CompletableFutureExceptionHandling {
    
    public static void main(String[] args) throws Exception {
        // Exception in supplyAsync
        CompletableFuture<String> failedFuture = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Something went wrong");
        });
        
        // exceptionally - provide fallback
        CompletableFuture<String> withFallback = failedFuture.exceptionally(ex -> {
            System.out.println("Exception: " + ex.getMessage());
            return "Fallback value";
        });
        
        System.out.println("With fallback: " + withFallback.get());
        
        // handle - process result or exception
        CompletableFuture<String> handledFuture = failedFuture.handle((result, ex) -> {
            if (ex != null) {
                return "Error: " + ex.getMessage();
            }
            return "Success: " + result;
        });
        
        System.out.println("Handled: " + handledFuture.get());
        
        // exceptionallyCompose - compose on exception
        CompletableFuture<String> composedException = failedFuture.exceptionallyCompose(ex -> {
            return CompletableFuture.supplyAsync(() -> "Recovered from: " + ex.getMessage());
        });
        
        System.out.println("Composed exception: " + composedException.get());
        
        // Exception in chain
        CompletableFuture<String> chainException = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenApply(s -> {
                throw new RuntimeException("Chain error");
            })
            .exceptionally(ex -> "Chain fallback");
        
        System.out.println("Chain exception: " + chainException.get());
        
        // Timeout
        CompletableFuture<String> slowFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Slow result";
        });
        
        CompletableFuture<String> withTimeout = slowFuture.orTimeout(1, TimeUnit.SECONDS);
        
        try {
            System.out.println("With timeout: " + withTimeout.get());
        } catch (ExecutionException e) {
            System.out.println("Timeout occurred: " + e.getCause().getMessage());
        }
        
        // Complete on timeout
        CompletableFuture<String> completeOnTimeout = slowFuture
            .completeOnTimeout("Default value", 1, TimeUnit.SECONDS);
        
        System.out.println("Complete on timeout: " + completeOnTimeout.get());
    }
}
```

### Advanced CompletableFuture Patterns

```java
import java.util.concurrent.*;
import java.util.*;
import java.util.stream.*;

public class CompletableFutureAdvanced {
    
    // Simulating async service
    static class UserService {
        public CompletableFuture<String> getUserById(int id) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "User" + id;
            });
        }
    }
    
    static class OrderService {
        public CompletableFuture<List<String>> getOrdersByUserId(String userId) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return List.of("Order1", "Order2", "Order3");
            });
        }
    }
    
    static class NotificationService {
        public CompletableFuture<Void> sendNotification(String user, List<String> orders) {
            return CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Sent notification to " + user + " for " + orders.size() + " orders");
            });
        }
    }
    
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();
        
        // Pattern 1: Sequential composition
        System.out.println("=== Sequential Composition ===");
        
        CompletableFuture<String> sequentialChain = CompletableFuture
            .supplyAsync(() -> 1)
            .thenApply(id -> "Processing " + id)
            .thenApply(result -> result + " - Completed");
        
        System.out.println("Sequential: " + sequentialChain.get());
        
        // Pattern 2: Parallel composition
        System.out.println("\n=== Parallel Composition ===");
        
        CompletableFuture<String> parallel1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Result 1";
        });
        
        CompletableFuture<String> parallel2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Result 2";
        });
        
        CompletableFuture<String> parallel3 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Result 3";
        });
        
        CompletableFuture<Void> allOf = CompletableFuture.allOf(parallel1, parallel2, parallel3);
        
        allOf.thenRun(() -> {
            try {
                System.out.println("Parallel 1: " + parallel1.get());
                System.out.println("Parallel 2: " + parallel2.get());
                System.out.println("Parallel 3: " + parallel3.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).get();
        
        // Pattern 3: AnyOf
        System.out.println("\n=== AnyOf ===");
        
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(parallel1, parallel2, parallel3);
        System.out.println("AnyOf result: " + anyOf.get());
        
        // Pattern 4: Real-world scenario
        System.out.println("\n=== Real-world Scenario ===");
        
        CompletableFuture<Void> workflow = userService.getUserById(1)
            .thenCompose(user -> orderService.getOrdersByUserId(user))
            .thenCompose(orders -> notificationService.sendNotification("User1", orders));
        
        workflow.get();
        
        // Pattern 5: Multiple independent operations
        System.out.println("\n=== Multiple Independent Operations ===");
        
        List<CompletableFuture<String>> independentOperations = IntStream.range(0, 5)
            .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                try { Thread.sleep(100 * (5 - i)); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return "Result " + i;
            }))
            .collect(Collectors.toList());
        
        CompletableFuture<Void> allIndependent = CompletableFuture.allOf(
            independentOperations.toArray(new CompletableFuture[0])
        );
        
        allIndependent.thenRun(() -> {
            independentOperations.forEach(cf -> {
                try {
                    System.out.println(cf.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).get();
        
        // Pattern 6: Exception handling in complex workflow
        System.out.println("\n=== Exception Handling ===");
        
        CompletableFuture<String> complexWorkflow = CompletableFuture
            .supplyAsync(() -> {
                throw new RuntimeException("Step 1 failed");
            })
            .exceptionally(ex -> {
                System.out.println("Recovered from: " + ex.getMessage());
                return "Recovered value";
            })
            .thenApply(value -> value + " - Step 2")
            .thenApply(value -> value + " - Step 3");
        
        System.out.println("Complex workflow: " + complexWorkflow.get());
    }
}
```

---

## Virtual Threads

### Basic Virtual Threads

```java
import java.util.concurrent.*;
import java.util.stream.*;

public class VirtualThreadsExample {
    
    public static void main(String[] args) throws Exception {
        // Creating virtual threads
        Thread virtualThread = Thread.ofVirtual().name("my-virtual-thread").start(() -> {
            System.out.println("Virtual thread: " + Thread.currentThread().getName());
            System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
        });
        
        virtualThread.join();
        
        // Virtual thread factory
        ThreadFactory factory = Thread.ofVirtual().name("vt-", 0).factory();
        ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);
        
        // Submit tasks
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " on " + 
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Virtual thread executor
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10).forEach(i -> {
                virtualExecutor.submit(() -> {
                    System.out.println("Virtual task " + i + " on " + 
                        Thread.currentThread().getName());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }
        
        // Virtual threads with CompletableFuture
        System.out.println("\n=== Virtual Threads with CompletableFuture ===");
        
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return "Hello from virtual thread: " + Thread.currentThread().getName();
            }, virtualExecutor);
            
            System.out.println("Result: " + future.get());
        }
        
        // Virtual threads with structured concurrency (preview)
        System.out.println("\n=== Virtual Threads Comparison ===");
        
        // Platform threads
        long start = System.nanoTime();
        try (ExecutorService platformExecutor = Executors.newFixedThreadPool(100)) {
            for (int i = 0; i < 1000; i++) {
                platformExecutor.submit(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
        long platformTime = System.nanoTime() - start;
        
        // Virtual threads
        start = System.nanoTime();
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1000; i++) {
                virtualExecutor.submit(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
        long virtualTime = System.nanoTime() - start;
        
        System.out.println("Platform threads time: " + platformTime / 1_000_000 + " ms");
        System.out.println("Virtual threads time: " + virtualTime / 1_000_000 + " ms");
    }
}
```

### Virtual Threads Best Practices

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

public class VirtualThreadsBestPractices {
    
    // Good: Virtual threads for I/O-bound tasks
    public static CompletableFuture<String> fetchDataAsync(String url) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate HTTP request
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Data from " + url;
        });
    }
    
    // Bad: Virtual threads for CPU-bound tasks
    public static long computeSum(long n) {
        long sum = 0;
        for (long i = 0; i < n; i++) {
            sum += i;
        }
        return sum;
    }
    
    // Virtual threads with synchronization
    private static final Object lock = new Object();
    private static int sharedCounter = 0;
    
    public static void incrementWithSync() {
        synchronized (lock) {
            sharedCounter++;
        }
    }
    
    // Virtual threads with thread-local (use ScopedValue instead)
    private static final ThreadLocal<Integer> threadLocalCounter = 
        ThreadLocal.withInitial(() -> 0);
    
    public static void incrementThreadLocal() {
        threadLocalCounter.set(threadLocalCounter.get() + 1);
    }
    
    public static void main(String[] args) throws Exception {
        // Pattern 1: I/O-bound workload
        System.out.println("=== I/O-bound Workload ===");
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> futures = IntStream.range(0, 100)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    return fetchDataAsync("http://example.com/" + i).join();
                }, executor))
                .collect(Collectors.toList());
            
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    futures.forEach(cf -> {
                        try {
                            System.out.println(cf.get());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                })
                .get();
        }
        
        // Pattern 2: Avoid pinning
        System.out.println("\n=== Avoid Pinning ===");
        
        // Bad: synchronized pinning
        Thread.startVirtualThread(() -> {
            synchronized (lock) {
                // This pins the virtual thread to its carrier thread
                sharedCounter++;
            }
        });
        
        // Good: ReentrantLock instead of synchronized
        java.util.concurrent.locks.ReentrantLock reentrantLock = 
            new java.util.concurrent.locks.ReentrantLock();
        
        Thread.startVirtualThread(() -> {
            reentrantLock.lock();
            try {
                sharedCounter++;
            } finally {
                reentrantLock.unlock();
            }
        });
        
        // Pattern 3: ScopedValue (Java 21+)
        System.out.println("\n=== ScopedValue ===");
        
        ScopedValue<String> userContext = ScopedValue.newInstance();
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            ScopedValue.where(userContext, "Alice").run(() -> {
                System.out.println("User in scope: " + userContext.get());
            });
        }
        
        // Pattern 4: Thread confinement with virtual threads
        System.out.println("\n=== Thread Confinement ===");
        
        // Use thread-local or ScopedValue instead of sharing state
        AtomicLong virtualThreadCounter = new AtomicLong(0);
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1000; i++) {
                executor.submit(() -> {
                    virtualThreadCounter.incrementAndGet();
                });
            }
        }
        
        System.out.println("Counter: " + virtualThreadCounter.get());
        
        // Pattern 5: Virtual threads with blocking operations
        System.out.println("\n=== Blocking Operations ===");
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        // Blocking I/O operation
                        System.out.println("Task " + taskId + " waiting...");
                        Thread.sleep(100);
                        System.out.println("Task " + taskId + " completed");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
        
        System.out.println("All tasks completed");
    }
}
```

---

## StructuredTaskScope

### Basic StructuredTaskScope

```java
import java.util.concurrent.*;

public class StructuredTaskScopeBasic {
    
    public static void main(String[] args) throws Exception {
        // StructuredTaskScope with ShutdownOnFailure
        System.out.println("=== ShutdownOnFailure ===");
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Submit tasks
            Subtask<String> task1 = scope.fork(() -> {
                Thread.sleep(100);
                return "Result 1";
            });
            
            Subtask<String> task2 = scope.fork(() -> {
                Thread.sleep(150);
                return "Result 2";
            });
            
            Subtask<String> task3 = scope.fork(() -> {
                Thread.sleep(50);
                return "Result 3";
            });
            
            // Wait for all tasks to complete
            scope.join();
            
            // Check for exceptions
            scope.throwIfFailed();
            
            // Get results
            System.out.println("Task 1: " + task1.get());
            System.out.println("Task 2: " + task2.get());
            System.out.println("Task 3: " + task3.get());
        }
        
        // StructuredTaskScope with ShutdownOnSuccess
        System.out.println("\n=== ShutdownOnSuccess ===");
        
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            // Submit tasks
            scope.fork(() -> {
                Thread.sleep(200);
                return "Slow result";
            });
            
            scope.fork(() -> {
                Thread.sleep(100);
                return "Fast result";
            });
            
            scope.fork(() -> {
                Thread.sleep(150);
                return "Medium result";
            });
            
            // Wait for first successful result
            scope.join();
            
            // Get first successful result
            System.out.println("First successful: " + scope.get());
        }
        
        // StructuredTaskScope with custom result
        System.out.println("\n=== Custom Result ===");
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<Integer> task1 = scope.fork(() -> {
                Thread.sleep(100);
                return 10;
            });
            
            Subtask<Integer> task2 = scope.fork(() -> {
                Thread.sleep(150);
                return 20;
            });
            
            scope.join();
            scope.throwIfFailed();
            
            // Process results
            int sum = task1.get() + task2.get();
            System.out.println("Sum: " + sum);
        }
    }
}
```

### Real-world StructuredTaskScope

```java
import java.util.concurrent.*;
import java.util.*;

public class StructuredTaskScopeRealWorld {
    
    // Simulated services
    static class UserService {
        public String getUser(int id) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "User" + id;
        }
    }
    
    static class OrderService {
        public List<String> getOrders(String userId) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return List.of("Order1", "Order2");
        }
    }
    
    static class NotificationService {
        public void sendNotification(String user, List<String> orders) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Sent notification to " + user);
        }
    }
    
    // Parallel execution with structured concurrency
    public static void processUserOrders(int userId) throws Exception {
        UserService userService = new UserService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();
        
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Parallel fetch of user and initial data
            Subtask<String> userTask = scope.fork(() -> userService.getUser(userId));
            Subtask<List<String>> initialOrdersTask = scope.fork(() -> 
                orderService.getOrders("User" + userId));
            
            scope.join();
            scope.throwIfFailed();
            
            String user = userTask.get();
            List<String> initialOrders = initialOrdersTask.get();
            
            // Second phase: parallel operations
            try (var innerScope = new StructuredTaskScope.ShutdownOnFailure()) {
                innerScope.fork(() -> {
                    notificationService.sendNotification(user, initialOrders);
                    return null;
                });
                
                innerScope.fork(() -> {
                    // Additional processing
                    return processAdditionalData(user);
                });
                
                innerScope.join();
                innerScope.throwIfFailed();
            }
            
            System.out.println("Processed user: " + user);
        }
    }
    
    private static String processAdditionalData(String user) {
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Additional data for " + user;
    }
    
    // Timeout example
    public static void processWithTimeout(int userId) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> task = scope.fork(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "Result";
            });
            
            // Set timeout
            scope.joinUntil(java.time.Instant.now().plusSeconds(2));
            
            if (task.state() == Subtask.State.SUCCESS) {
                System.out.println("Result: " + task.get());
            } else if (task.state() == Subtask.State.FAILED) {
                System.out.println("Task failed: " + task.exception().getMessage());
            } else {
                System.out.println("Task incomplete");
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Processing User Orders ===");
        processUserOrders(1);
        
        System.out.println("\n=== Processing with Timeout ===");
        processWithTimeout(1);
    }
}
```

---

## Concurrent Collections

### ConcurrentHashMap Advanced

```java
import java.util.concurrent.*;
import java.util.*;
import java.util.stream.*;

public class ConcurrentHashMapAdvanced {
    
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        // Parallel bulk operations
        for (int i = 0; i < 100; i++) {
            map.put("key" + i, i);
        }
        
        // parallelForEachThreshold - controls parallel execution
        map.parallelForEach(10, (key, value) -> {
            System.out.println(Thread.currentThread().getName() + ": " + key + " = " + value);
        });
        
        // searchParallel
        Optional<String> result = map.search(10, (key, value) -> {
            if (value == 50) {
                return key;
            }
            return null;
        });
        
        System.out.println("Found: " + result.orElse("not found"));
        
        // reduceValues
        int maxValue = map.reduceValues(10, Integer::max);
        System.out.println("Max value: " + maxValue);
        
        // reduceEntries
        Map.Entry<String, Integer> maxEntry = map.reduceEntries(10, 
            (entry1, entry2) -> entry1.getValue() > entry2.getValue() ? entry1 : entry2);
        
        System.out.println("Max entry: " + maxEntry);
        
        // computeIfAbsent atomicity
        map.computeIfAbsent("key50", key -> {
            System.out.println("Computing value for " + key);
            return 100;
        });
        
        // merge atomicity
        map.merge("key50", 10, Integer::sum);
        System.out.println("After merge: " + map.get("key50"));
        
        // Search and transform
        List<String> highValueKeys = map.reduceEntries(10,
            (entry, list) -> {
                if (entry.getValue() > 90) {
                    list.add(entry.getKey());
                }
                return list;
            },
            ArrayList::new
        );
        
        System.out.println("High value keys: " + highValueKeys);
    }
}
```

### ConcurrentLinkedQueue and Deque

```java
import java.util.concurrent.*;
import java.util.*;

public class ConcurrentQueueExample {
    
    public static void main(String[] args) throws InterruptedException {
        // ConcurrentLinkedQueue (unbounded, non-blocking)
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        
        // Producer
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.offer("Item " + i);
                System.out.println("Produced: Item " + i);
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
                String item;
                while ((item = queue.poll()) == null) {
                    Thread.yield();
                }
                System.out.println("Consumed: " + item);
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
        
        // ConcurrentLinkedDeque
        ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();
        
        // Multiple producers
        for (int i = 0; i < 5; i++) {
            final int producerId = i;
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    deque.offerFirst("Producer" + producerId + "-Item" + j);
                    deque.offerLast("Producer" + producerId + "-Item" + j);
                }
            }).start();
        }
        
        Thread.sleep(500);
        
        System.out.println("Deque size: " + deque.size());
        
        // Process from both ends
        while (!deque.isEmpty()) {
            String item = deque.pollFirst();
            if (item != null) {
                System.out.println("Processed: " + item);
            }
        }
    }
}
```

### BlockingQueue Patterns

```java
import java.util.concurrent.*;
import java.util.*;

public class BlockingQueuePatterns {
    
    // Bounded buffer (producer-consumer)
    static class BoundedBuffer<T> {
        private final BlockingQueue<T> queue;
        
        BoundedBuffer(int capacity) {
            this.queue = new ArrayBlockingQueue<>(capacity);
        }
        
        public void put(T item) throws InterruptedException {
            queue.put(item);
        }
        
        public T take() throws InterruptedException {
            return queue.take();
        }
        
        public int size() {
            return queue.size();
        }
    }
    
    // Priority blocking queue
    static class PriorityTask implements Comparable<PriorityTask> {
        private final String name;
        private final int priority;
        
        PriorityTask(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
        
        @Override
        public int compareTo(PriorityTask other) {
            return Integer.compare(this.priority, other.priority);
        }
        
        @Override
        public String toString() {
            return name + " (priority: " + priority + ")";
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Bounded buffer example
        System.out.println("=== Bounded Buffer ===");
        
        BoundedBuffer<String> buffer = new BoundedBuffer<>(5);
        
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.put("Item " + i);
                    System.out.println("Produced: Item " + i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    String item = buffer.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        
        // Priority blocking queue
        System.out.println("\n=== Priority Blocking Queue ===");
        
        PriorityBlockingQueue<PriorityTask> priorityQueue = new PriorityBlockingQueue<>();
        
        priorityQueue.offer(new PriorityTask("Low priority", 3));
        priorityQueue.offer(new PriorityTask("High priority", 1));
        priorityQueue.offer(new PriorityTask("Medium priority", 2));
        priorityQueue.offer(new PriorityTask("Very high priority", 0));
        
        while (!priorityQueue.isEmpty()) {
            System.out.println("Processing: " + priorityQueue.poll());
        }
        
        // Delay queue
        System.out.println("\n=== Delay Queue ===");
        
        class DelayedTask implements Delayed {
            private final String name;
            private final long startTime;
            
            DelayedTask(String name, long delayMs) {
                this.name = name;
                this.startTime = System.currentTimeMillis() + delayMs;
            }
            
            @Override
            public long getDelay(java.util.concurrent.TimeUnit unit) {
                long delay = startTime - System.currentTimeMillis();
                return unit.convert(delay, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
            
            @Override
            public int compareTo(Delayed other) {
                return Long.compare(getDelay(java.util.concurrent.TimeUnit.MILLISECONDS),
                    other.getDelay(java.util.concurrent.TimeUnit.MILLISECONDS));
            }
            
            @Override
            public String toString() {
                return name;
            }
        }
        
        DelayQueue<DelayedTask> delayQueue = new DelayQueue<>();
        
        delayQueue.offer(new DelayedTask("Task 1", 200));
        delayQueue.offer(new DelayedTask("Task 2", 100));
        delayQueue.offer(new DelayedTask("Task 3", 300));
        
        while (!delayQueue.isEmpty()) {
            DelayedTask task = delayQueue.take();
            System.out.println("Executed: " + task);
        }
        
        // Linked transfer queue
        System.out.println("\n=== Linked Transfer Queue ===");
        
        LinkedTransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        
        // Producer
        Thread transferProducer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    transferQueue.transfer("Item " + i);
                    System.out.println("Transferred: Item " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer
        Thread transferConsumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    String item = transferQueue.take();
                    System.out.println("Received: " + item);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        transferProducer.start();
        transferConsumer.start();
        
        transferProducer.join();
        transferConsumer.join();
    }
}
```

---

## Summary

Java Concurrency provides:

1. **Thread Creation**: Extending Thread, implementing Runnable/Callable, lambda expressions
2. **Synchronization**: synchronized methods/blocks, wait/notify, volatile
3. **Locks**: ReentrantLock, ReadWriteLock, StampedLock, Conditions
4. **Atomic Variables**: AtomicInteger, AtomicLong, AtomicReference, AtomicStampedReference
5. **Executor Service**: ThreadPoolExecutor, Executors factory methods, Future/Callable
6. **CompletableFuture**: Async composition, exception handling, chaining
7. **Virtual Threads**: Lightweight threads for I/O-bound tasks, structured concurrency
8. **StructuredTaskScope**: Structured concurrency with automatic lifecycle management
9. **Concurrent Collections**: Thread-safe collections for concurrent access

Understanding these concepts is essential for building responsive, scalable, and thread-safe applications.

---

*Next: [Memory Management](../memory-management/README.md)*
