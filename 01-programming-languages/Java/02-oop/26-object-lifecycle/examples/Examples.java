package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Object Lifecycle ===\n");

        // WHY: Understanding lifecycle helps write efficient, leak-free code
        // INTERNAL: new → constructor → use → unreachable → GC → finalize (deprecated)
        // ENGINEERING: Use try-with-resources, avoid finalizers, use weak references

        System.out.println("Creating objects...");
        ManagedResource r1 = new ManagedResource("File1");
        ManagedResource r2 = new ManagedResource("File2");

        r1.process();
        r2.process();

        System.out.println("\nNullifying references...");
        r1 = null;  // Now eligible for GC
        r2 = null;

        System.out.println("Forcing GC...");
        System.gc();

        // TRADE-OFF: Finalizers vs Cleaner (Java 9+)
        // Finalizers: unreliable, slow, can resurrect objects
        // Cleaner: safer, more predictable, recommended
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        System.out.println("Check stderr for cleanup messages");
    }
}

class ManagedResource {
    private final String name;

    ManagedResource(String name) {
        this.name = name;
        System.out.println("  Created: " + name);
    }

    void process() {
        System.out.println("  Processing: " + name);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        System.out.println("  FINALIZED: " + name); // Don't rely on this!
        super.finalize();
    }
}
