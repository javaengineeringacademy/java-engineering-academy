package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicSolutions {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: AtomicInteger basics
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: AtomicInteger Basics ===");
        AtomicInteger counter = new AtomicInteger(0);

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.incrementAndGet();
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        System.out.println("Final value: " + counter.get());
        System.out.println("After incrementAndGet: " + counter.incrementAndGet());
        System.out.println("After addAndGet(5): " + counter.addAndGet(5));
    }

    /**
     * Exercise 2: AtomicInteger compareAndSet
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: AtomicInteger compareAndSet ===");
        AtomicInteger value = new AtomicInteger(10);

        boolean updated = value.compareAndSet(10, 20);
        System.out.println("CAS(10 -> 20) succeeded: " + updated + ", value: " + value.get());

        updated = value.compareAndSet(10, 30);
        System.out.println("CAS(10 -> 30) succeeded: " + updated + ", value: " + value.get());

        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                int current;
                do {
                    current = value.get();
                } while (!value.compareAndSet(current, current + 1));
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("After concurrent CAS: " + value.get());
    }

    /**
     * Exercise 3: AtomicReference
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: AtomicReference ===");
        AtomicReference<String> ref = new AtomicReference<>("initial");

        Thread updater = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            String old = ref.getAndSet("updated");
            System.out.println("Updated from: " + old + " to: " + ref.get());
        });

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Read: " + ref.get());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        updater.start();
        reader.start();
        updater.join();
        reader.join();
    }

    /**
     * Exercise 4: AtomicBoolean
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: AtomicBoolean ===");
        AtomicBoolean flag = new AtomicBoolean(false);

        Thread setter = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            flag.set(true);
            System.out.println("Flag set to true");
        });

        Thread checker = new Thread(() -> {
            while (!flag.get()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Checker detected flag is true!");
        });

        setter.start();
        checker.start();
        setter.join();
        checker.join();
    }

    /**
     * Exercise 5: AtomicLong accumulator
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: AtomicLong Accumulator ===");
        AtomicLong accumulator = new AtomicLong(0);

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final long increment = (i + 1) * 10;
            threads[i] = new Thread(() -> {
                accumulator.getAndUpdate(value -> value + increment);
                System.out.println(Thread.currentThread().getName() + " added " + increment);
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        System.out.println("Final accumulated value: " + accumulator.get());
        System.out.println("Expected: " + (10 + 20 + 30 + 40 + 50 + 60 + 70 + 80 + 90 + 100));
    }
}
