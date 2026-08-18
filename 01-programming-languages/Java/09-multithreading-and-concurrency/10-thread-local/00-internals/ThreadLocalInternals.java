package academy.javaengineering.concurrency.threadlocal.internals;

public class ThreadLocalInternals {
    private static final ThreadLocal<String> userContext = ThreadLocal.withInitial(() -> "default");

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread: " + userContext.get());

        Thread t1 = new Thread(() -> {
            userContext.set("Alice");
            System.out.println("T1: " + userContext.get());
        });

        Thread t2 = new Thread(() -> {
            userContext.set("Bob");
            System.out.println("T2: " + userContext.get());
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Main thread after: " + userContext.get());

        // InheritableThreadLocal
        ThreadLocal<Integer> inheritable = new InheritableThreadLocal<>();
        inheritable.set(42);

        Thread child = new Thread(() -> {
            System.out.println("Child inherits: " + inheritable.get());
        });
        child.start();
        child.join();
    }
}
