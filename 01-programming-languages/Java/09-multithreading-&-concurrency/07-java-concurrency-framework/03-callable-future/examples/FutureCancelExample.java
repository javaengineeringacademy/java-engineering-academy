package academy.javaengineering.concurrency.framework.callable;

import java.util.concurrent.*;

public class FutureCancelExample {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // --- 1. cancel(true) - interrupt ---
        System.out.println("=== 1. cancel(true) - Interrupt ===");
        Callable<String> interruptibleTask = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("  Task running... iteration " + i);
                Thread.sleep(200);
            }
            return "completed";
        };

        Future<String> future1 = executor.submit(interruptibleTask);
        Thread.sleep(500); // let it start
        boolean cancelled1 = future1.cancel(true); // mayInterruptIfRunning = true
        System.out.println("cancel(true) returned: " + cancelled1);
        System.out.println("isCancelled: " + future1.isCancelled());
        System.out.println("isDone: " + future1.isDone());

        try {
            future1.get();
        } catch (CancellationException e) {
            System.out.println("get() threw CancellationException\n");
        }

        // --- 2. cancel(false) - don't interrupt ---
        System.out.println("=== 2. cancel(false) - No Interrupt ===");
        Callable<String> nonInterruptibleTask = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("  Task running... iteration " + i);
                Thread.sleep(200);
            }
            return "completed";
        };

        Future<String> future2 = executor.submit(nonInterruptibleTask);
        Thread.sleep(500); // let it start
        boolean cancelled2 = future2.cancel(false); // mayInterruptIfRunning = false
        System.out.println("cancel(false) returned: " + cancelled2);
        System.out.println("isCancelled: " + future2.isCancelled());
        System.out.println("isDone: " + future2.isDone());
        System.out.println("Note: task may still complete because interrupt was not sent\n");

        // --- 3. isCancelled() check ---
        System.out.println("=== 3. isCancelled() States ===");
        Callable<String> task = () -> {
            Thread.sleep(2000);
            return "done";
        };

        Future<String> future3a = executor.submit(task);
        System.out.println("Before cancel: isCancelled=" + future3a.isCancelled());
        System.out.println("Before cancel: isDone=" + future3a.isDone());

        future3a.cancel(true);
        System.out.println("After cancel: isCancelled=" + future3a.isCancelled());
        System.out.println("After cancel: isDone=" + future3a.isDone());

        // --- 4. When cancel fails (already completed) ---
        System.out.println("\n=== 4. Cancel After Completion ===");
        Callable<String> fastTask = () -> "fast result";

        Future<String> future4 = executor.submit(fastTask);
        Thread.sleep(500); // let it complete
        System.out.println("isDone: " + future4.isDone());
        System.out.println("Result: " + future4.get());

        boolean cancelled4 = future4.cancel(true); // should return false
        System.out.println("cancel(true) after completion returned: " + cancelled4);
        System.out.println("isCancelled: " + future4.isCancelled());

        // --- 5. Cancel vs interrupt resistance ---
        System.out.println("\n=== 5. Non-Interruptible Task ===");
        Callable<String> nonInterruptible = () -> {
            // This task does not check interrupts
            long sum = 0;
            for (long i = 0; i < 1_000_000_000L; i++) {
                sum += i;
            }
            return "sum=" + sum;
        };

        Future<String> future5 = executor.submit(nonInterruptible);
        Thread.sleep(100);
        boolean cancelled5 = future5.cancel(true);
        System.out.println("cancel(true) on non-interruptible task: " + cancelled5);
        System.out.println("isDone: " + future5.isDone());
        // Task will complete because it doesn't check interrupts

        // --- 6. Summary of cancel states ---
        System.out.println("\n=== 6. Cancel State Summary ===");
        System.out.println("State after cancel():");
        System.out.println("  - If task not started: cancel() returns true, isCancelled()=true, isDone()=true");
        System.out.println("  - If task running + interruptible: cancel(true) returns true, isCancelled()=true");
        System.out.println("  - If task running + not interruptible: cancel(true) returns true, isCancelled()=true");
        System.out.println("  - If task completed: cancel() returns false, isCancelled()=false");

        executor.shutdown();
        executor.awaitTermination(15, TimeUnit.SECONDS);
    }
}
