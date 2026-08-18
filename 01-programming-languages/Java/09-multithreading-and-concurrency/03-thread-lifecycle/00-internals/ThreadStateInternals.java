package academy.javaengineering.concurrency.lifecycle.internals;

public class ThreadStateInternals {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Thread t = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("Thread state during wait: " + Thread.currentThread().getState());
                    lock.wait(2000);
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        System.out.println("NEW: " + t.getState());
        t.start();
        Thread.sleep(100);
        System.out.println("WAITING: " + t.getState());
        t.join();
        System.out.println("TERMINATED: " + t.getState());

        // Demonstrate BLOCKED state
        Thread blocker = new Thread(() -> {
            synchronized (lock) {
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
        Thread waiter = new Thread(() -> {
            synchronized (lock) { System.out.println("Got lock"); }
        });

        blocker.start();
        Thread.sleep(50);
        waiter.start();
        Thread.sleep(50);
        System.out.println("BLOCKED: " + waiter.getState());

        blocker.join();
        waiter.join();
    }
}
