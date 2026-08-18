package academy.javaengineering.concurrency.memorymodel.solutions;

public class MemoryModelSolutions {
    static class Singleton {
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

    public static void main(String[] args) throws InterruptedException {
        // Solution 1: Volatile visibility
        class Flag {
            volatile boolean running = true;
        }
        Flag flag = new Flag();
        Thread t = new Thread(() -> {
            while (flag.running) Thread.yield();
            System.out.println("Worker stopped");
        });
        t.start();
        Thread.sleep(100);
        flag.running = false;
        t.join();
        System.out.println("Volatile flag works: worker stopped gracefully");
    }
}
