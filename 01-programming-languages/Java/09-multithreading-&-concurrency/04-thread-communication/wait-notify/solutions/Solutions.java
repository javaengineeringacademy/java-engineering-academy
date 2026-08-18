package academy.javaengineering.concurrency.communication.waitnotify.solutions;

/**
 * Complete solutions for wait/notify exercises.
 */
public class Solutions {

    /**
     * Exercise 1: Simple Signal
     */
    public static void exercise1() {
        System.out.println("=== Exercise 1: Simple Signal (Solution) ===");
        Object lock = new Object();
        boolean[] flag = {false};

        Thread a = new Thread(() -> {
            synchronized (lock) {
                while (!flag[0]) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("Received");
            }
        }, "ThreadA");

        Thread b = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            synchronized (lock) {
                flag[0] = true;
                lock.notify();
            }
        }, "ThreadB");

        a.start();
        b.start();
        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    /**
     * Exercise 2: Ping-Pong
     */
    public static void exercise2() {
        System.out.println("=== Exercise 2: Ping-Pong (Solution) ===");
        Object lock = new Object();
        boolean[] isPingTurn = {true};

        Thread ping = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (!isPingTurn[0]) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.println("Ping");
                    isPingTurn[0] = false;
                    lock.notifyAll();
                }
            }
        }, "Ping");

        Thread pong = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (isPingTurn[0]) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.println("Pong");
                    isPingTurn[0] = true;
                    lock.notifyAll();
                }
            }
        }, "Pong");

        ping.start();
        pong.start();
        try {
            ping.join();
            pong.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    /**
     * Exercise 3: Countdown Latch
     */
    public static void exercise3() {
        System.out.println("=== Exercise 3: Countdown Latch (Solution) ===");
        Object lock = new Object();
        int[] count = {3};

        for (int i = 0; i < 3; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    Thread.sleep(200 + id * 200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (lock) {
                    count[0]--;
                    System.out.println("Worker " + id + " counted down. Remaining: " + count[0]);
                    lock.notifyAll();
                }
            }, "Worker-" + i).start();
        }

        Thread main = new Thread(() -> {
            synchronized (lock) {
                while (count[0] > 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("All done!");
            }
        }, "MainThread");

        main.start();
        try {
            main.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    /**
     * Exercise 4: Single-Slot Producer-Consumer
     */
    public static void exercise4() {
        System.out.println("=== Exercise 4: Single-Slot Producer-Consumer (Solution) ===");
        Object lock = new Object();
        int[] buffer = {0};
        boolean[] hasItem = {false};

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                synchronized (lock) {
                    while (hasItem[0]) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    buffer[0] = i;
                    hasItem[0] = true;
                    System.out.println("Produced: " + i);
                    lock.notifyAll();
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (!hasItem[0]) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    int item = buffer[0];
                    hasItem[0] = false;
                    System.out.println("Consumed: " + item);
                    lock.notifyAll();
                }
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    /**
     * Exercise 5: Graceful Thread Termination
     */
    public static void exercise5() {
        System.out.println("=== Exercise 5: Graceful Thread Termination (Solution) ===");
        Object lock = new Object();
        boolean[] terminate = {false};

        Thread worker = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    while (!terminate[0]) {
                        try {
                            lock.wait(300);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                        if (!terminate[0]) {
                            System.out.println("Working...");
                        }
                    }
                    System.out.println("Worker terminating gracefully");
                    return;
                }
            }
        }, "Worker");

        worker.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronized (lock) {
            terminate[0] = true;
            lock.notify();
        }

        try {
            worker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
        System.out.println("All exercises completed!");
    }
}
