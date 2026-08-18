package academy.javaengineering.concurrency.threadbasics.solutions;

public class ThreadBasicsSolutions {
    public static void main(String[] args) throws InterruptedException {
        // Solution 1: Named thread printing
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Hello from " + Thread.currentThread().getName());
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        }, "GreetThread");
        t1.start();

        // Solution 2: Range sum
        int[][] ranges = {{1, 100}, {101, 200}, {201, 300}};
        Thread[] sumThreads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int start = ranges[i][0], end = ranges[i][1];
            sumThreads[i] = new Thread(() -> {
                long sum = 0;
                for (int j = start; j <= end; j++) sum += j;
                System.out.println("Sum " + start + "-" + end + " = " + sum);
            });
            sumThreads[i].start();
        }

        // Solution 3: Daemon memory monitor
        Thread monitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Runtime rt = Runtime.getRuntime();
                System.out.printf("Memory: %dMB used / %dMB max%n",
                    (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024,
                    rt.maxMemory() / 1024 / 1024);
                try { Thread.sleep(2000); } catch (InterruptedException e) { break; }
            }
        });
        monitor.setDaemon(true);
        monitor.start();

        // Wait for sum threads
        for (Thread t : sumThreads) t.join();
        t1.join();

        System.out.println("All solutions completed");
    }
}
