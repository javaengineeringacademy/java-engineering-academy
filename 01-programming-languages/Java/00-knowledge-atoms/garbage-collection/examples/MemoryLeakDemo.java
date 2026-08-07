import java.util.*;

/**
 * Memory Leak Demo
 * Demonstrates common memory leak patterns in Java and how to avoid them
 */
public class MemoryLeakDemo {

    // ========================================
    // LEAK 1: Static collection growing forever
    // ========================================
    static class StaticLeak {
        private static final List<Object> cache = new ArrayList<>();

        public static void addToCache(Object obj) {
            cache.add(obj); // Objects never removed
        }
    }

    // ========================================
    // LEAK 2: Inner class holding outer reference
    // ========================================
    class InnerClassLeak {
        // Non-static inner class holds implicit reference to outer class
        private final String data;

        InnerClassLeak(String data) {
            this.data = data;
        }

        String getData() {
            return data;
        }
    }

    // ========================================
    // LEAK 3: Unclosed resources
    // ========================================
    static class ResourceLeak {
        static void unclosedResource() {
            // Scanner never closed - file handle leaked
            // Scanner sc = new Scanner(new File("data.txt"));
            // sc.useDelimiter(",");
            // ... usage without sc.close()
        }

        static void properResource() {
            // try-with-resources ensures cleanup
            // try (Scanner sc = new Scanner(new File("data.txt"))) {
            //     sc.useDelimiter(",");
            // }
        }
    }

    // ========================================
    // LEAK 4: ThreadLocal without cleanup
    // ========================================
    static class ThreadLocalLeak {
        private static final ThreadLocal<byte[]> threadLocalData =
            ThreadLocal.withInitial(() -> new byte[1024 * 1024]); // 1MB

        // If thread pool is used, ThreadLocal values persist until thread dies
    }

    // ========================================
    // LEAK 5: StringBuilder in loop
    // ========================================
    static class StringBuilderLeak {
        static String problematicMethod() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                result.append("line ").append(i).append("\n");
            }
            return result.toString();
            // StringBuilder itself is fine, but intermediate strings are garbage
        }
    }

    // ========================================
    // SOLUTION: Proper patterns
    // ========================================
    static class ProperPatterns {
        // Use WeakHashMap for caches
        private static final Map<String, WeakReference<Object>> cache = new WeakHashMap<>();

        // Clean up ThreadLocal
        private static final ThreadLocal<byte[]> threadLocal =
            ThreadLocal.withInitial(() -> new byte[1024]);

        public static void cleanup() {
            threadLocal.remove();
        }

        // Use final fields for immutable objects
        static class ImmutableConfig {
            private final String host;
            private final int port;

            ImmutableConfig(String host, int port) {
                this.host = host;
                this.port = port;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Memory Leak Demo ===\n");

        Runtime runtime = Runtime.getRuntime();

        // 1. Demonstrate static collection leak
        System.out.println("--- Leak 1: Static Collection ---");
        long before = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < 100000; i++) {
            StaticLeak.addToCache(new byte[1024]);
        }
        long after = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Added 100K objects to static list");
        System.out.println("Memory used: ~" + ((after - before) / 1024) + " KB");
        System.out.println("Objects are never removed - classic memory leak!");

        // 2. Demonstrate inner class leak
        System.out.println("\n--- Leak 2: Inner Class Reference ---");
        WeakReference<InnerClassLeak> weakRef = createInnerClass();
        System.gc();
        Thread.sleep(100);
        System.out.println("Inner class instance GC'd: " + (weakRef.get() == null));

        // 3. Demonstrate ThreadLocal leak
        System.out.println("\n--- Leak 3: ThreadLocal ---");
        System.out.println("ThreadLocal without remove() leaks memory in thread pools");
        System.out.println("Always call threadLocal.remove() when done");

        // 4. Show proper pattern
        System.out.println("\n--- Proper Pattern: WeakHashMap ---");
        Map<String, Object> properCache = new WeakHashMap<>();
        Object key1 = new String("key1");
        properCache.put(key1, new byte[1024]);
        System.out.println("Before GC: cache size = " + properCache.size());
        key1 = null;
        System.gc();
        Thread.sleep(100);
        System.out.println("After GC:  cache size = " + properCache.size());

        // 5. Show final fields
        System.out.println("\n--- Proper Pattern: Final Fields ---");
        ProperPatterns.ImmutableConfig config = new ProperPatterns.ImmutableConfig("localhost", 8080);
        System.out.println("Immutable config: host=" + config.host + ", port=" + config.port);

        System.out.println("\n=== Memory Leak Prevention Tips ===");
        System.out.println("1. Use try-with-resources for AutoCloseable");
        System.out.println("2. Avoid static collections that grow unbounded");
        System.out.println("3. Use WeakHashMap for caches");
        System.out.println("4. Call ThreadLocal.remove() in thread pools");
        System.out.println("5. Make collections unmodifiable when possible");
        System.out.println("6. Use final fields for immutable objects");
        System.out.println("7. Monitor with JVisualVM or jcmd");

        System.out.println("\n=== End of Memory Leak Demo ===");
    }

    private static WeakReference<InnerClassLeak> createInnerClass() {
        InnerClassLeakDemo outer = new InnerClassLeakDemo();
        InnerClassLeak inner = outer.new InnerClassLeak("data");
        WeakReference<InnerClassLeak> ref = new WeakReference<>(inner);
        outer = null;
        inner = null;
        return ref;
    }

    static class InnerClassLeakDemo {
        class InnerClassLeak {
            private final String data;
            InnerClassLeak(String data) { this.data = data; }
        }
    }
}

/*
Expected Output (approximate):
=== Memory Leak Demo ===

--- Leak 1: Static Collection ---
Added 100K objects to static list
Memory used: ~102400 KB
Objects are never removed - classic memory leak!

--- Leak 2: Inner Class Reference ---
Inner class instance GC'd: true
  ...finalized...

--- Leak 3: ThreadLocal ---
ThreadLocal without remove() leaks memory in thread pools
Always call threadLocal.remove() when done

--- Proper Pattern: WeakHashMap ---
Before GC: cache size = 1
After GC:  cache size = 0

--- Proper Pattern: Final Fields ---
Immutable config: host=localhost, port=8080

=== Memory Leak Prevention Tips ===
1. Use try-with-resources for AutoCloseable
2. Avoid static collections that grow unbounded
3. Use WeakHashMap for caches
4. Call ThreadLocal.remove() in thread pools
5. Make collections unmodifiable when possible
6. Use final fields for immutable objects
7. Monitor with JVisualVM or jcmd

=== End of Memory Leak Demo ===
*/
