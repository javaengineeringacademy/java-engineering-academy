package academy.javaengineering.concurrency.framework.callable.solutions;

import java.util.concurrent.*;

public class CallableSolutions {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Solution 1: Factorial
        Callable<Long> factorial = () -> {
            long result = 1;
            for (int i = 2; i <= 10; i++) result *= i;
            return result;
        };
        System.out.println("10! = " + executor.submit(factorial).get());

        // Solution 2: Timeout
        Callable<String> slow = () -> { Thread.sleep(5000); return "done"; };
        Future<String> f = executor.submit(slow);
        try {
            f.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Timed out");
            f.cancel(true);
        }

        executor.shutdown();
    }
}
