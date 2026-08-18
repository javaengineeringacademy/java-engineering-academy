package academy.javaengineering.concurrency.communication.internals;

public class WaitNotifyInternals {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        StringBuilder sequence = new StringBuilder();

        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                sequence.append("WAITER: acquired lock, waiting\n");
                try {
                    lock.wait();
                    sequence.append("WAITER: notified, resuming\n");
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        Thread notifier = new Thread(() -> {
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            synchronized (lock) {
                sequence.append("NOTIFIER: acquired lock, calling notify\n");
                lock.notify();
                sequence.append("NOTIFIER: notified, releasing lock\n");
            }
        });

        waiter.start();
        notifier.start();
        waiter.join();
        notifier.join();

        System.out.println(sequence.toString());
        System.out.println("Key points:");
        System.out.println("- wait() releases the lock");
        System.out.println("- notify() does NOT release the lock immediately");
        System.out.println("- Waiter must reacquire lock after notify");
    }
}
