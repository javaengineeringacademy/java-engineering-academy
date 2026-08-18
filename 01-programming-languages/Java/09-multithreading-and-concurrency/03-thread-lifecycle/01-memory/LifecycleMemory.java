package academy.javaengineering.concurrency.lifecycle.memory;

public class LifecycleMemory {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Thread State Transitions and Memory");
        System.out.println("===================================");

        Object lock = new Object();
        Thread t = new Thread(() -> {
            synchronized (lock) {
                try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        System.out.println("1. NEW state: Thread object allocated, no OS thread yet");
        System.out.println("   Memory: ~100 bytes for Java object");

        t.start();
        Thread.sleep(50);
        System.out.println("2. WAITING state: OS thread allocated, parked");
        System.out.println("   Memory: ~512KB-1MB stack + kernel structures");

        synchronized (lock) { lock.notify(); }
        t.join();
        System.out.println("3. TERMINATED: OS thread resources released");
        System.out.println("   Memory: stack freed, thread object becomes garbage");
    }
}
