package academy.javaengineering.concurrency.solutions;

public class WaitNotifySolutions {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Basic wait/notify
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Basic wait/notify ===");
        Object monitor = new Object();

        Thread waiter = new Thread(() -> {
            synchronized (monitor) {
                try {
                    System.out.println("Thread A: Waiting...");
                    monitor.wait();
                    System.out.println("Thread A: Woken up!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread notifier = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (monitor) {
                System.out.println("Thread B: Notifying...");
                monitor.notify();
            }
        });

        waiter.start();
        notifier.start();
        waiter.join();
        notifier.join();
    }

    /**
     * Exercise 2: Producer-Consumer with wait/notify
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Producer-Consumer ===");
        class SharedBuffer {
            private int item;
            private boolean hasItem = false;

            synchronized void produce(int value) throws InterruptedException {
                while (hasItem) {
                    wait();
                }
                item = value;
                hasItem = true;
                System.out.println("Produced: " + value);
                notify();
            }

            synchronized int consume() throws InterruptedException {
                while (!hasItem) {
                    wait();
                }
                hasItem = false;
                System.out.println("Consumed: " + item);
                notify();
                return item;
            }
        }

        SharedBuffer buffer = new SharedBuffer();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.produce(i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    buffer.consume();
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    /**
     * Exercise 3: notifyAll() vs notify()
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: notifyAll() Demo ===");
        Object monitor = new Object();

        Runnable waiter = () -> {
            synchronized (monitor) {
                try {
                    System.out.println(Thread.currentThread().getName() + " waiting");
                    monitor.wait();
                    System.out.println(Thread.currentThread().getName() + " woke up");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread t1 = new Thread(waiter, "Waiter-1");
        Thread t2 = new Thread(waiter, "Waiter-2");
        Thread t3 = new Thread(waiter, "Waiter-3");

        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(500);

        synchronized (monitor) {
            System.out.println("Notifier: calling notifyAll()");
            monitor.notifyAll();
        }

        t1.join();
        t2.join();
        t3.join();
    }

    /**
     * Exercise 4: Spurious wakeup prevention
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Spurious Wakeup Prevention ===");
        class ConditionWaiter {
            private boolean condition = false;

            synchronized void waitForCondition() throws InterruptedException {
                while (!condition) {  // Always use while loop, not if!
                    System.out.println("Waiting for condition...");
                    wait();
                }
                System.out.println("Condition met!");
            }

            synchronized void setCondition() {
                condition = true;
                notifyAll();
            }
        }

        ConditionWaiter waiter = new ConditionWaiter();

        Thread waitingThread = new Thread(() -> {
            try {
                waiter.waitForCondition();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        waitingThread.start();
        Thread.sleep(1000);
        waiter.setCondition();
        waitingThread.join();
    }

    /**
     * Exercise 5: Bounded buffer producer-consumer
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Bounded Buffer ===");
        class BoundedBuffer {
            private final int[] buffer;
            private int count = 0;
            private int putIndex = 0;
            private int getIndex = 0;

            BoundedBuffer(int size) {
                buffer = new int[size];
            }

            synchronized void put(int item) throws InterruptedException {
                while (count == buffer.length) {
                    System.out.println("Buffer full, producer waiting...");
                    wait();
                }
                buffer[putIndex] = item;
                putIndex = (putIndex + 1) % buffer.length;
                count++;
                System.out.println("Put: " + item + ", count: " + count);
                notifyAll();
            }

            synchronized int take() throws InterruptedException {
                while (count == 0) {
                    System.out.println("Buffer empty, consumer waiting...");
                    wait();
                }
                int item = buffer[getIndex];
                getIndex = (getIndex + 1) % buffer.length;
                count--;
                System.out.println("Take: " + item + ", count: " + count);
                notifyAll();
                return item;
            }
        }

        BoundedBuffer buffer = new BoundedBuffer(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.put(i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.take();
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
    }
}
