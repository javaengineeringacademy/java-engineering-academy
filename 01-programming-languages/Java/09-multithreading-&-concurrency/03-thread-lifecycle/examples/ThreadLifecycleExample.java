package academy.javaengineering.concurrency.lifecycle.examples;

public class ThreadLifecycleExample {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        StringBuilder log = new StringBuilder();

        Thread t = new Thread(() -> {
            log.append("START: ").append(Thread.currentThread().getState()).append("\n");
            try {
                Thread.sleep(200);
                log.append("AFTER_SLEEP: ").append(Thread.currentThread().getState()).append("\n");

                synchronized (lock) {
                    log.append("WAITING: ").append(Thread.currentThread().getState()).append("\n");
                    lock.wait(500);
                }
                log.append("AFTER_WAIT: ").append(Thread.currentThread().getState()).append("\n");
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            log.append("END: ").append(Thread.currentThread().getState()).append("\n");
        });

        log.append("BEFORE_START: ").append(t.getState()).append("\n");
        t.start();
        Thread.sleep(50);
        log.append("DURING_SLEEP: ").append(t.getState()).append("\n");
        t.join();

        System.out.println(log.toString());
    }
}
