package academy.javaengineering.concurrency.introduction.solutions;

public class ThreadSolutions {
    public static void main(String[] args) throws InterruptedException {
        // Solution 1
        Thread counter = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Number: " + i);
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        counter.start();
        counter.join();

        // Solution 2
        Object lock = new Object();
        Thread ping = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    System.out.println("Ping");
                    try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            }
        });
        Thread pong = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    System.out.println("  Pong");
                    lock.notify();
                }
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        ping.start();
        pong.start();
        ping.join();
        pong.join();

        // Solution 3
        Thread daemon = new Thread(() -> {
            while (true) {
                System.out.println("Heartbeat: " + System.currentTimeMillis());
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        });
        daemon.setDaemon(true);
        daemon.start();
        Thread.sleep(3000);
        System.out.println("Main exiting, daemon will stop");
    }
}
