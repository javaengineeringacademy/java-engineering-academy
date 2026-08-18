package academy.javaengineering.concurrency.lifecycle.solutions;

public class LifecycleSolutions {
    public static void main(String[] args) throws InterruptedException {
        // Solution 1: All 6 states
        Object lock = new Object();
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(100);
                synchronized (lock) { lock.wait(); }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        System.out.println("1. NEW: " + t.getState());
        t.start();
        Thread.sleep(50);
        System.out.println("2. TIMED_WAITING (sleep): " + t.getState());

        Thread.sleep(100);
        System.out.println("3. WAITING (wait): " + t.getState());

        synchronized (lock) { lock.notify(); }
        t.join();
        System.out.println("4. TERMINATED: " + t.getState());
        System.out.println("5. RUNNABLE: main thread is " + Thread.currentThread().getState());
    }
}
