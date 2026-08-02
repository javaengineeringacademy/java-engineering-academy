package academy.javaengineering.jvm;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Garbage Collection - GC Algorithms, G1, ZGC, Tuning.
 */
public class GarbageCollectionDemo {

    public static class MemoryHog {
        private byte[] data;

        public MemoryHog(int size) {
            data = new byte[size];
        }
    }

    public static class FinalizeExample {
        private static int counter = 0;

        public FinalizeExample() { counter++; }

        @Override
        protected void finalize() throws Throwable {
            counter--;
            System.out.println("Finalize called. Counter: " + counter);
        }
    }

    public static class ReferenceDemo {
        public void demonstrateSoftReference() {
            SoftReference<byte[]> softRef = new SoftReference<>(new byte[1024 * 1024]);
            System.out.println("Soft reference: " + softRef.get());
        }

        public void demonstrateWeakReference() {
            WeakReference<Object> weakRef = new WeakReference<>(new Object());
            System.out.println("Weak reference: " + weakRef.get());
        }
    }

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

        ReferenceDemo refDemo = new ReferenceDemo();
        refDemo.demonstrateSoftReference();
        refDemo.demonstrateWeakReference();

        hogs.clear();
        System.out.println("Cleared references, triggering GC...");
        System.gc();
    }
}
