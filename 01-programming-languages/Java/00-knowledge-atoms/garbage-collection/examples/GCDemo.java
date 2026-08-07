/**
 * Basic Garbage Collection Demo
 * Demonstrates fundamental GC concepts: object allocation, eligibility, and System.gc()
 */
public class GCDemo {

    private static class MyObject {
        private int id;

        public MyObject(int id) {
            this.id = id;
            System.out.println("MyObject " + id + " created");
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("MyObject " + id + " finalized (GC'd)");
            super.finalize();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== GC Demo ===\n");

        // 1. Object becomes eligible after losing all references
        System.out.println("--- Demo 1: Object Eligibility ---");
        MyObject obj1 = new MyObject(1);
        obj1 = null; // Object(1) is now eligible for GC

        // 2. Objects in a method become eligible after method returns
        System.out.println("\n--- Demo 2: Method Scope ---");
        createAndDiscard();

        // 3. Request garbage collection (not guaranteed)
        System.out.println("\n--- Demo 3: Requesting GC ---");
        System.out.println("Free memory before GC: " + getFreeMemory() + " MB");
        System.gc();
        System.out.println("GC requested. Free memory after: " + getFreeMemory() + " MB");

        // 4. Finalizer may run eventually (non-deterministic)
        System.out.println("\n--- Demo 4: Finalization ---");
        for (int i = 2; i <= 5; i++) {
            new MyObject(i);
        }

        System.out.println("\n--- Forcing GC to finalize objects ---");
        System.gc();
        try {
            Thread.sleep(100); // Give finalizer thread time to run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== End of GC Demo ===");
        System.out.println("Note: Finalization is deprecated since Java 9. Use Cleaner or try-with-resources.");
    }

    private static void createAndDiscard() {
        MyObject obj = new MyObject(100);
        obj = null; // eligible for GC after this method returns
        System.out.println("MyObject(100) reference discarded inside method");
    }

    private static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / (1024 * 1024);
    }
}

/*
Expected Output (approximate):
=== GC Demo ===

--- Demo 1: Object Eligibility ---
MyObject 1 created

--- Demo 2: Method Scope ---
MyObject 100 created
MyObject(100) reference discarded inside method

--- Demo 3: Requesting GC ---
Free memory before GC: X MB
GC requested. Free memory after: Y MB

--- Demo 4: Finalization ---
MyObject 2 created
MyObject 3 created
MyObject 4 created
MyObject 5 created

--- Forcing GC to finalize objects ---
MyObject 1 finalized (GC'd)
MyObject 100 finalized (GC'd)
MyObject 2 finalized (GC'd)
...

=== End of GC Demo ===
Note: Finalization is deprecated since Java 9. Use Cleaner or try-with-resources.
*/
