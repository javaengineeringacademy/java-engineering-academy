package academy.javaengineering.concurrency.introduction.internals;

public class ThreadInternals {
    public static void main(String[] args) {
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread: " + currentThread.getName());
        System.out.println("Thread ID: " + currentThread.getId());
        System.out.println("Thread state: " + currentThread.getState());
        System.out.println("Thread priority: " + currentThread.getPriority());
        System.out.println("Is daemon: " + currentThread.isDaemon());
        System.out.println("Thread group: " + currentThread.getThreadGroup().getName());

        System.out.println("\nAvailable processors: " + Runtime.getRuntime().availableProcessors());

        Thread t = new Thread(() -> {
            System.out.println("\nChild thread:");
            System.out.println("Name: " + Thread.currentThread().getName());
            System.out.println("ID: " + Thread.currentThread().getId());
            System.out.println("Priority: " + Thread.currentThread().getPriority());
            System.out.println("Is daemon: " + Thread.currentThread().isDaemon());
        });
        t.start();
        try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
