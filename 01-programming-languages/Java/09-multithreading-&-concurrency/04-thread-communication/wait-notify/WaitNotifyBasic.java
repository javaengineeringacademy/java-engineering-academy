package academy.javaengineering.concurrency.communication.waitnotify;

/**
 * Basic wait/notify example.
 * Thread A waits, Thread B notifies after doing work.
 */
public class WaitNotifyBasic {

    private static final Object lock = new Object();
    private static boolean ready = false;

    public static void main(String[] args) {
        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                System.out.println("[Waiter] Waiting for signal...");
                while (!ready) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("[Waiter] Received signal! Proceeding.");
            }
        }, "Waiter");

        Thread notifier = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            synchronized (lock) {
                System.out.println("[Notifier] Doing work, setting ready=true");
                ready = true;
                lock.notify();
                System.out.println("[Notifier] Notified waiter");
            }
        }, "Notifier");

        waiter.start();
        notifier.start();
    }
}
