package academy.javaengineering.concurrency.threadbasics.internals;

public class ThreadClassInternals {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("Thread name: " + Thread.currentThread().getName());
            System.out.println("Thread ID: " + Thread.currentThread().getId());
            System.out.println("Priority: " + Thread.currentThread().getPriority());
            System.out.println("Is alive: " + Thread.currentThread().isAlive());
        }, "MyCustomThread");

        System.out.println("Before start:");
        System.out.println("  Name: " + t.getName());
        System.out.println("  State: " + t.getState());
        System.out.println("  Is alive: " + t.isAlive());

        t.start();
        t.join();

        System.out.println("After join:");
        System.out.println("  State: " + t.getState());
        System.out.println("  Is alive: " + t.isAlive());

        // Thread priority demonstration
        Thread high = new Thread(() -> { for (int i = 0; i < 3; i++) System.out.println("HIGH: " + i); });
        Thread low = new Thread(() -> { for (int i = 0; i < 3; i++) System.out.println("LOW: " + i); });
        high.setPriority(Thread.MAX_PRIORITY);
        low.setPriority(Thread.MIN_PRIORITY);
        high.start();
        low.start();
        high.join();
        low.join();

        // Daemon thread
        Thread daemon = new Thread(() -> {
            while (true) {
                System.out.println("Daemon running...");
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
            }
        });
        daemon.setDaemon(true);
        daemon.start();
        Thread.sleep(1100);
        System.out.println("Main thread exiting (daemon will stop)");
    }
}
