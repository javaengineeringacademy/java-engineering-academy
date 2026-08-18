package academy.javaengineering.concurrency.memorymodel.internals;

public class HappensBeforeInternals {
    private static int x = 0;
    private static boolean ready = false;

    public static void main(String[] args) throws InterruptedException {
        // Without volatile — may never see ready = true
        Thread writer = new Thread(() -> {
            x = 42;
            ready = true; // not volatile
        });
        Thread reader = new Thread(() -> {
            while (!ready) { /* busy wait */ }
            System.out.println("x = " + x); // may print 0!
        });

        writer.start();
        reader.start();
        writer.join();
        reader.interrupt();

        // With volatile — guaranteed visibility
        class Shared {
            volatile boolean flag = false;
            int data = 0;
        }
        Shared s = new Shared();

        Thread w = new Thread(() -> {
            s.data = 42;
            s.flag = true;
        });
        Thread r = new Thread(() -> {
            while (!s.flag) Thread.yield();
            System.out.println("data = " + s.data); // always 42
        });
        w.start();
        r.start();
        w.join();
        r.join();
    }
}
