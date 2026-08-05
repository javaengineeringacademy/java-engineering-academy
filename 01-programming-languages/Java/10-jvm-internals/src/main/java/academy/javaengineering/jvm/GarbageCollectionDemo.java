package academy.javaengineering.jvm;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates Java Garbage Collection concepts and reference types.
 *
 * <p>This class provides examples of memory allocation, garbage collection triggers,
 * and different reference types (Soft, Weak) used for memory management.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Heap memory allocation</li>
 *   <li>Garbage collection triggering</li>
 *   <li>Soft and Weak references</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class GarbageCollectionDemo {

    /**
     * Memory-intensive object for demonstrating allocation patterns.
     */
    public static class MemoryHog {
        private byte[] data;
        public MemoryHog(int size) { data = new byte[size]; }
    }

    /**
     * Demonstrates different reference types for memory management.
     */
    public static class ReferenceDemo {
        /**
         * Demonstrates soft reference behavior.
         */
        public void demonstrateSoftReference() {
            SoftReference<byte[]> softRef = new SoftReference<>(new byte[1024 * 1024]);
            System.out.println("Soft reference: " + softRef.get());
        }

        /**
         * Demonstrates weak reference behavior.
         */
        public void demonstrateWeakReference() {
            WeakReference<Object> weakRef = new WeakReference<>(new Object());
            System.out.println("Weak reference: " + weakRef.get());
        }
    }

    /**
     * Demonstrates garbage collection concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Garbage Collection Demo ===");
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Available processors: " + runtime.availableProcessors());
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");

        List<MemoryHog> hogs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            hogs.add(new MemoryHog(1024 * 1024));
            System.out.println("Allocated " + (i + 1) + " MB");
        }

        new ReferenceDemo().demonstrateSoftReference();
        new ReferenceDemo().demonstrateWeakReference();

        hogs.clear();
        System.out.println("Cleared references, triggering GC...");
        System.gc();
    }
}
