package academy.javaengineering.concurrency.communication.memory;

public class CommunicationMemory {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Thread Communication Memory Overhead");
        System.out.println("====================================");

        Object lock = new Object();
        long before = Runtime.getRuntime().freeMemory();

        synchronized (lock) {
            Thread t = new Thread(() -> {
                synchronized (lock) {
                    try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            });
            t.start();
            Thread.sleep(50);

            long during = Runtime.getRuntime().freeMemory();
            System.out.println("Memory used by waiting thread: ~" + (before - during) + " bytes");

            lock.notify();
            t.join();
        }

        System.out.println("\nEach wait/notify adds:");
        System.out.println("  - Entry in object's wait set");
        System.out.println("  - Thread state saved/restored");
        System.out.println("  - Monitor ownership transferred");
    }
}
