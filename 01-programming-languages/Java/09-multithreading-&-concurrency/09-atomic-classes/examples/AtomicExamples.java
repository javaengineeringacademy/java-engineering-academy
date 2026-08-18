package academy.javaengineering.concurrency.atomic.examples;

import java.util.concurrent.atomic.*;

public class AtomicExamples {
    public static void main(String[] args) throws InterruptedException {
        // AtomicInteger
        AtomicInteger counter = new AtomicInteger(0);
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) counter.incrementAndGet();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("AtomicInteger: " + counter.get());

        // AtomicReference
        AtomicReference<String> ref = new AtomicReference<>("initial");
        Thread updater = new Thread(() -> {
            ref.compareAndSet("initial", "updated");
        });
        updater.start();
        updater.join();
        System.out.println("AtomicReference: " + ref.get());

        // LongAdder
        java.util.concurrent.atomic.LongAdder adder = new java.util.concurrent.atomic.LongAdder();
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) adder.increment();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("LongAdder: " + adder.sum());
    }
}
