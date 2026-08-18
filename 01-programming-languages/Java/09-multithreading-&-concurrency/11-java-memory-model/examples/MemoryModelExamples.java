package academy.javaengineering.concurrency.memorymodel.examples;

public class MemoryModelExamples {
    public static void main(String[] args) throws InterruptedException {
        // Example 1: Double-checked locking (correct with volatile)
        class Singleton {
            private static volatile Singleton instance;
            private Singleton() {}
            static Singleton getInstance() {
                if (instance == null) {
                    synchronized (Singleton.class) {
                        if (instance == null) {
                            instance = new Singleton();
                        }
                    }
                }
                return instance;
            }
        }

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                System.out.println("Singleton: " + Singleton.getInstance().hashCode());
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();

        // Example 2: Safe publication
        class SafePublication {
            final int x; // final guarantees visibility after constructor
            SafePublication(int x) { this.x = x; }
        }

        SafePublication[] refs = new SafePublication[1];
        Thread publisher = new Thread(() -> refs[0] = new SafePublication(42));
        Thread subscriber = new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("Published value: " + refs[0].x);
        });
        publisher.start();
        subscriber.start();
        publisher.join();
        subscriber.join();
    }
}
