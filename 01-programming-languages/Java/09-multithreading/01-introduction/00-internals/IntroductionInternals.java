package academy.javaengineering.concurrency.introduction;

/**
 * IntroductionInternals - Demonstrates internal workings of Java threads.
 * Shows thread object creation, start internals, and thread metadata.
 */
public class IntroductionInternals {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Object Internals ===");
        threadObjectInternals();

        System.out.println("\n=== Thread Start Internals ===");
        threadStartInternals();

        System.out.println("\n=== Thread Stack Info ===");
        threadStackInfo();

        System.out.println("\n=== JVM Thread Count ===");
        jvmThreadCount();
    }

    static void threadObjectInternals() {
        Thread t = new Thread(() -> {
            System.out.println("  Thread running: " + Thread.currentThread().getName());
        }, "InternalsDemo");

        System.out.println("  Before start:");
        System.out.println("    Name: " + t.getName());
        System.out.println("    ID: " + t.getId());
        System.out.println("    Priority: " + t.getPriority());
        System.out.println("    Is daemon: " + t.isDaemon());
        System.out.println("    State: " + t.getState());
        System.out.println("    Thread group: " + t.getThreadGroup().getName());
        System.out.println("    Is alive: " + t.isAlive());

        t.start();
        t.join();

        System.out.println("  After completion:");
        System.out.println("    State: " + t.getState());
        System.out.println("    Is alive: " + t.isAlive());
    }

    static void threadStartInternals() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("  Thread executing run() method");
        });

        System.out.println("  Thread status before start: " + getThreadStatus(t));
        t.start();
        System.out.println("  Thread status after start: " + getThreadStatus(t));
        t.join();
        System.out.println("  Thread status after join: " + getThreadStatus(t));
    }

    static int getThreadStatus(Thread t) {
        try {
            var field = Thread.class.getDeclaredField("threadStatus");
            field.setAccessible(true);
            return field.getInt(t);
        } catch (Exception e) {
            return -1;
        }
    }

    static void threadStackInfo() {
        Thread mainThread = Thread.currentThread();
        System.out.println("  Main thread:");
        System.out.println("    Name: " + mainThread.getName());
        System.out.println("    ID: " + mainThread.getId());
        System.out.println("    Priority: " + mainThread.getPriority());
        System.out.println("    Is alive: " + mainThread.isAlive());
        System.out.println("    Is daemon: " + mainThread.isDaemon());
    }

    static void jvmThreadCount() {
        int activeCount = Thread.activeCount();
        System.out.println("  Active threads in current thread group: " + activeCount);

        Thread[] threads = new Thread[activeCount];
        int count = Thread.currentThread().getThreadGroup().enumerate(threads);
        System.out.println("  Enumerated threads: " + count);
        for (int i = 0; i < count; i++) {
            System.out.println("    [" + threads[i].getId() + "] " +
                threads[i].getName() + " - " + threads[i].getState());
        }
    }
}
