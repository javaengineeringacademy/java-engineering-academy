package academy.javaengineering.knowledgeatoms.garbagecollection;

import java.lang.ref.*;

public class GarbageCollectionInternals {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Garbage Collection Internals ===\n");

        // 1. Generational model
        System.out.println("--- Generational GC Model ---");
        System.out.println("Heap divided into generations:");
        System.out.println("  Young Generation (1/3 of heap)");
        System.out.println("    - Eden: new objects allocated here");
        System.out.println("    - Survivor S0/S0: objects surviving minor GC");
        System.out.println("  Old Generation (2/3 of heap)");
        System.out.println("    - Long-lived objects promoted here");
        System.out.println("  Metaspace (off-heap)");
        System.out.println("    - Class metadata, method info");

        // 2. Reference types
        System.out.println("\n--- Reference Types ---");
        demonstrateReferenceTypes();

        // 3. GC triggers
        System.out.println("\n--- GC Triggers ---");
        System.out.println("1. Eden space fills up -> Minor GC");
        System.out.println("2. Old generation fills up -> Major/Full GC");
        System.out.println("3. System.gc() -> requests GC (not guaranteed)");
        System.out.println("4. Metaspace fills up -> Metaspace GC");

        // 4. Object lifecycle
        System.out.println("\n--- Object Lifecycle ---");
        System.out.println("1. Allocation in Eden (or TLAB)");
        System.out.println("2. Survives Minor GC -> moved to Survivor");
        System.out.println("3. Survives multiple Minor GCs -> promoted to Old Gen");
        System.out.println("4. Old Gen fills up -> Major GC");
        System.out.println("5. No more references -> eligible for collection");

        // 5. TLAB (Thread-Local Allocation Buffer)
        System.out.println("\n--- TLAB ---");
        System.out.println("Each thread gets a private allocation buffer in Eden");
        System.out.println("Eliminates contention for small object allocations");
        System.out.println("TLAB size: auto-tuned based on allocation rate");
    }

    private static void demonstrateReferenceTypes() throws InterruptedException {
        // Strong reference
        Object strong = new Object();
        System.out.println("Strong: prevents GC as long as reachable");

        // Soft reference — collected before OOM
        SoftReference<Object> soft = new SoftReference<>(new Object());
        System.out.println("Soft: collected when memory is low (before OOM)");
        System.out.println("  Referent alive: " + (soft.get() != null));

        // Weak reference — collected on next GC
        WeakReference<Object> weak = new WeakReference<>(new Object());
        System.out.println("Weak: collected on next GC cycle");
        System.out.println("  Referent alive: " + (weak.get() != null));
        System.gc();
        Thread.sleep(100);
        System.out.println("  After GC, alive: " + (weak.get() != null));

        // Phantom reference — enqueued after finalization
        PhantomReference<Object> phantom = new PhantomReference<>(new Object(), null);
        System.out.println("Phantom: enqueued after finalizer, used for cleanup");
        System.out.println("  get() always returns null: " + (phantom.get() == null));
    }
}
