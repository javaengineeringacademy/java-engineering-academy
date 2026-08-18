package academy.javaengineering.concurrency.sync.solutions;

public class SynchronizationSolutions {
    public static void main(String[] args) throws InterruptedException {
        // Solution 1: Thread-safe counter
        class Counter {
            private int count = 0;
            public synchronized void increment() { count++; }
            public synchronized int getCount() { return count; }
        }

        Counter counter = new Counter();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) counter.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Counter: " + counter.getCount());

        // Solution 2: Deadlock and fix
        Object lockA = new Object();
        Object lockB = new Object();

        // FIXED version with lock ordering
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("Thread 1: holding lockA");
                synchronized (lockB) {
                    System.out.println("Thread 1: holding lockA + lockB");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockA) {  // Same order as t1
                System.out.println("Thread 2: holding lockA");
                synchronized (lockB) {
                    System.out.println("Thread 2: holding lockA + lockB");
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("No deadlock - lock ordering enforced");
    }
}
