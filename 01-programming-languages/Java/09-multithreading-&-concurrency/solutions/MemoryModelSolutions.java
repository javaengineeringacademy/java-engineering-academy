package academy.javaengineering.concurrency.solutions;

public class MemoryModelSolutions {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Visibility problem
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Visibility Problem ===");
        class SharedData {
            boolean flag = false;
        }

        SharedData data = new SharedData();

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            data.flag = true;
            System.out.println("Writer: flag set to true");
        });

        Thread reader = new Thread(() -> {
            int count = 0;
            while (!data.flag) {
                count++;
            }
            System.out.println("Reader: detected flag=true after " + count + " iterations");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
    }

    /**
     * Exercise 2: volatile keyword
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: volatile Keyword ===");
        class SharedData {
            volatile boolean flag = false;
        }

        SharedData data = new SharedData();

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            data.flag = true;
            System.out.println("Writer: volatile flag set to true");
        });

        Thread reader = new Thread(() -> {
            int count = 0;
            while (!data.flag) {
                count++;
            }
            System.out.println("Reader: detected volatile flag=true after " + count + " iterations");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
    }

    /**
     * Exercise 3: Happens-before relationship
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Happens-Before ===");
        class SharedData {
            int value = 0;
            boolean ready = false;
        }

        SharedData data = new SharedData();

        Thread writer = new Thread(() -> {
            synchronized (data) {
                data.value = 42;
                data.ready = true;
                System.out.println("Writer: value=42, ready=true (inside synchronized)");
            }
        });

        Thread reader = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (data) {
                System.out.println("Reader: value=" + data.value + ", ready=" + data.ready + " (inside synchronized)");
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
    }

    /**
     * Exercise 4: Instruction reordering
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Instruction Reordering ===");
        class Doubleton {
            static volatile boolean initialized = false;
            static int value;
        }

        Thread initializer = new Thread(() -> {
            Doubleton.value = 42;
            Doubleton.initialized = true;
            System.out.println("Initializer: set value and initialized=true");
        });

        Thread user = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (Doubleton.initialized) {
                System.out.println("User: value=" + Doubleton.value);
            }
        });

        initializer.start();
        user.start();
        initializer.join();
        user.join();
    }

    /**
     * Exercise 5: double-checked locking
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Double-Checked Locking ===");
        class Singleton {
            private static volatile Singleton instance;

            static Singleton getInstance() {
                if (instance == null) {
                    synchronized (Singleton.class) {
                        if (instance == null) {
                            instance = new Singleton();
                            System.out.println("Singleton created");
                        }
                    }
                }
                return instance;
            }
        }

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                Singleton s = Singleton.getInstance();
                System.out.println(Thread.currentThread().getName() + " got instance: " + System.identityHashCode(s));
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
    }
}
