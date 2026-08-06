package academy.javaengineering.senior.jvm;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Memory Leak Demo - Common leak patterns and prevention.
 *
 * Detection tools:
 *   jhat, VisualVM, Eclipse MAT, jmap -dump:format=b,file=heap.bin <pid>
 *   -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof
 */
public class MemoryLeakDemo {

    // =====================================================
    // PATTERN 1: Static Collections Growing Unbounded
    // =====================================================
    // Static map holds references forever. Objects never GC'd.
    private static final Map<String, Object> CACHE = new HashMap<>();

    public static void staticCollectionLeak() {
        System.out.println("=== Pattern 1: Static Collection Leak ===");
        for (int i = 0; i < 100000; i++) {
            CACHE.put("key-" + i, new byte[1024]); // 1KB each = 100MB leaked
        }
        System.out.println("Leaked: " + CACHE.size() + " entries in static map");
        // FIX: Use WeakHashMap, or bounded cache (Caffeine/Guava)
    }

    // =====================================================
    // PATTERN 2: Unclosed Resources (Streams, Connections)
    // =====================================================
    // Resources not closed hold native memory and file descriptors.
    public static void unclosedResourceLeak() {
        System.out.println("\n=== Pattern 2: Unclosed Resource Leak ===");
        // BAD: Stream never closed on exception
        // InputStream is = new FileInputStream("data.txt");
        // is.read();
        // is.close(); // If read() throws, close() never executes

        // GOOD: Try-with-resources
        // try (InputStream is = new FileInputStream("data.txt")) {
        //     is.read();
        // } // Auto-closed even on exception

        System.out.println("Always use try-with-resources for AutoCloseable");
        System.out.println("Resources: streams, connections, channels, locks");
    }

    // =====================================================
    // PATTERN 3: Inner Classes Holding Outer References
    // =====================================================
    // Non-static inner class holds implicit reference to outer.
    // Prevents GC of outer class even after outer is logically done.
    public class InnerClassLeak {
        // BAD: Non-static inner class
        // public void doWork() {
        //     Runnable r = new Runnable() {  // holds ref to Outer
        //         public void run() { /* ... */ }
        //     };
        // }
    }

    // GOOD: Static nested class
    public static class StaticNestedWork {
        public void doWork() {
            Runnable r = new Runnable() {
                public void run() { /* no outer reference */ }
            };
        }
    }

    // =====================================================
    // PATTERN 4: ThreadLocal Without Cleanup
    // =====================================================
    // ThreadLocal values live as long as the thread.
    // In thread pools, threads are reused → values never cleaned.
    private static final ThreadLocal<byte[]> THREAD_LOCAL = new ThreadLocal<>();

    public static void threadLocalLeak() {
        System.out.println("\n=== Pattern 4: ThreadLocal Leak ===");
        THREAD_LOCAL.set(new byte[1024 * 1024]); // 1MB per thread
        // BAD: Never calling remove() → leaks in thread pools
        // GOOD: Always remove in finally block
        try {
            // work with thread-local data
        } finally {
            THREAD_LOCAL.remove(); // CRITICAL for thread pools
        }
        System.out.println("Always call ThreadLocal.remove() in finally blocks");
    }

    // =====================================================
    // PATTERN 5: Listener/Callback Not Removed
    // =====================================================
    // Registered listeners accumulate over time.
    private static final List<Object> LISTENERS = new ArrayList<>();

    public static void listenerLeak() {
        System.out.println("\n=== Pattern 5: Listener Leak ===");
        for (int i = 0; i < 10000; i++) {
            LISTENERS.add(new Object()); // Listeners never removed
        }
        System.out.println("Registered: " + LISTENERS.size() + " listeners");
        // FIX: Use WeakReference for listeners, or explicitly unregister
    }

    // =====================================================
    // REFERENCE TYPES: WeakReference & SoftReference
    // =====================================================
    public static void referenceTypesDemo() {
        System.out.println("\n=== Reference Types ===");

        // WeakReference: GC'd at next GC cycle, regardless of memory
        Object strong = new Object();
        WeakReference<Object> weak = new WeakReference<>(strong);
        System.out.println("Weak ref before GC: " + weak.get());
        strong = null; // Remove strong reference
        System.gc();
        System.out.println("Weak ref after GC:  " + weak.get()); // null

        // SoftReference: GC'd only when memory is low
        Object strong2 = new Object();
        SoftReference<Object> soft = new SoftReference<>(strong2);
        System.out.println("Soft ref before GC: " + soft.get());
        strong2 = null;
        System.gc(); // Soft ref survives unless memory pressure
        System.out.println("Soft ref after GC:  " + soft.get()); // likely non-null
        System.out.println("SoftRef: good for caches, WeakRef: map keys");
    }

    // =====================================================
    // GOOD PATTERN: WeakHashMap for Caches
    // =====================================================
    private static final Map<Object, Object> WEAK_CACHE = new WeakHashMap<>();

    public static void weakHashMapDemo() {
        System.out.println("\n=== WeakHashMap for Caching ===");
        Object key = new Object();
        WEAK_CACHE.put(key, new byte[1024]);
        System.out.println("Cache size before nulling key: " + WEAK_CACHE.size());
        key = null;
        System.gc();
        // Entry eligible for GC when key has no strong refs
        System.out.println("Cache size after GC:  " + WEAK_CACHE.size());
    }

    public static void main(String[] args) {
        staticCollectionLeak();
        unclosedResourceLeak();
        threadLocalLeak();
        listenerLeak();
        referenceTypesDemo();
        weakHashMapDemo();

        System.out.println("\n=== Detection Commands ===");
        System.out.println("jmap -dump:format=b,file=heap.bin <pid>");
        System.out.println("jhat heap.bin                          (browse heap)");
        System.out.println("jstat -gcutil <pid> 1000               (GC stats)");
        System.out.println("-XX:+HeapDumpOnOutOfMemoryError        (auto dump)");
    }
}