package academy.javaengineering.concurrency.atomic.internals;

import java.util.concurrent.atomic.*;

public class CASInternals {
    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger(0);

        // CAS operation
        int expected = 0;
        int update = 1;
        boolean success = counter.compareAndSet(expected, update);
        System.out.println("CAS(0→1): " + success + ", value: " + counter.get());

        // Failed CAS
        success = counter.compareAndSet(0, 2);
        System.out.println("CAS(0→2): " + success + ", value: " + counter.get());

        // updateAndGet
        counter.updateAndGet(v -> v * 10 + 5);
        System.out.println("After updateAndGet: " + counter.get());

        // getAndUpdate
        int old = counter.getAndIncrement();
        System.out.println("getAndIncrement: old=" + old + ", new=" + counter.get());
    }
}
