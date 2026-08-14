package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Anonymous Class Patterns ===\n");

        // WHY: Anonymous classes provide inline implementation of interface/abstract class
        // INTERNAL: Compiler generates synthetic class file (ClassName$1.class)
        // ENGINEERING: Use for one-off implementations, mostly replaced by lambdas

        // Anonymous class implementing Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("  Running anonymous task");
            }
        };
        task.run();

        // Anonymous class extending abstract class
        AbstractLogger logger = new AbstractLogger() {
            @Override
            protected void write(String msg) {
                System.out.println("  [ANON] " + msg);
            }
        };
        logger.log("Hello");

        // TRADE-OFF: Anonymous class vs lambda
        // Anonymous class: can implement multiple interfaces, have state, final local vars
        // Lambda: concise, single abstract method only, effectively final vars
        // Java 8+: prefer lambdas for SAM interfaces
    }
}

abstract class AbstractLogger {
    public final void log(String msg) { write(msg); }
    protected abstract void write(String msg);
}
