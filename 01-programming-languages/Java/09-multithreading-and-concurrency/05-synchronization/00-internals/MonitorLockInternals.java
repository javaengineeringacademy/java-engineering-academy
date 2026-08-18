package academy.javaengineering.concurrency.sync.internals;

public class MonitorLockInternals {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        StringBuilder log = new StringBuilder();

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                log.append("T1: acquired lock\n");
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                log.append("T1: releasing lock\n");
            }
        });

        Thread t2 = new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            log.append("T2: trying to acquire lock...\n");
            synchronized (lock) {
                log.append("T2: acquired lock\n");
                log.append("T2: releasing lock\n");
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(log.toString());
        System.out.println("Monitor lock ensures only one thread holds the lock at a time");
        System.out.println("T2 had to wait until T1 released the lock");
    }
}
