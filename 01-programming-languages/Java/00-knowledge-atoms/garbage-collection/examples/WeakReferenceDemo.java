import java.lang.ref.*;

/**
 * WeakReference Demo
 * Demonstrates WeakReference, SoftReference, and PhantomReference usage
 */
public class WeakReferenceDemo {

    private static class Data {
        private String value;

        public Data(String value) {
            this.value = value;
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("  Data(\"" + value + "\") finalized");
            super.finalize();
        }

        @Override
        public String toString() {
            return "Data(\"" + value + "\")";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== WeakReference Demo ===\n");

        // 1. WeakReference - collected on next GC, no matter memory pressure
        System.out.println("--- Demo 1: WeakReference ---");
        WeakReference<Data> weakRef = new WeakReference<>(new Data("weak-data"));
        System.out.println("Before GC: weakRef.get() = " + weakRef.get());
        System.out.println("isEnqueued: " + weakRef.isEnqueued());

        System.gc();
        Thread.sleep(100);
        System.out.println("After GC:  weakRef.get() = " + weakRef.get());
        System.out.println("isEnqueued: " + weakRef.isEnqueued());

        // 2. SoftReference - collected only when memory is low
        System.out.println("\n--- Demo 2: SoftReference ---");
        SoftReference<Data> softRef = new SoftReference<>(new Data("soft-data"));
        System.out.println("Before GC: softRef.get() = " + softRef.get());

        System.gc();
        Thread.sleep(100);
        System.out.println("After GC:  softRef.get() = " + softRef.get());
        System.out.println("Soft references survive GC unless memory is low");

        // 3. PhantomReference - enqueued after finalization, can't get object
        System.out.println("\n--- Demo 3: PhantomReference ---");
        ReferenceQueue<Data> phantomQueue = new ReferenceQueue<>();
        Data phantomData = new Data("phantom-data");
        PhantomReference<Data> phantomRef = new PhantomReference<>(phantomData, phantomQueue);
        phantomData = null; // Allow GC

        System.out.println("Before GC: phantomRef.get() = " + phantomRef.get());
        System.gc();
        Thread.sleep(100);
        Reference<? extends Data> polled = phantomQueue.poll();
        System.out.println("After GC: phantomRef.get() = " + phantomRef.get());
        System.out.println("Phantom reference enqueued: " + (polled != null));

        // 4. WeakHashMap - entries removed when key is GC'd
        System.out.println("\n--- Demo 4: WeakHashMap ---");
        WeakHashMap<Data, String> map = new WeakHashMap<>();
        Data key = new Data("map-key");
        map.put(key, "value");
        System.out.println("Before GC: map.size() = " + map.size());

        key = null; // Allow key to be GC'd
        System.gc();
        Thread.sleep(100);
        System.out.println("After GC:  map.size() = " + map.size());
        System.out.println("Entry automatically removed when key was GC'd");

        // 5. Use case: Cache implementation
        System.out.println("\n--- Demo 5: Simple Cache with WeakReference ---");
        SimpleCache cache = new SimpleCache();
        cache.put("key1", new Data("cached-value1"));
        System.out.println("Before GC: cache.get(\"key1\") = " + cache.get("key1"));

        System.gc();
        Thread.sleep(100);
        System.out.println("After GC:  cache.get(\"key1\") = " + cache.get("key1"));
        System.out.println("Cache entry automatically cleared by GC");

        System.out.println("\n=== End of WeakReference Demo ===");
    }

    static class SimpleCache {
        private final WeakHashMap<String, WeakReference<Data>> cache = new WeakHashMap<>();

        public void put(String key, Data value) {
            cache.put(key, new WeakReference<>(value));
        }

        public Data get(String key) {
            WeakReference<Data> ref = cache.get(key);
            return ref != null ? ref.get() : null;
        }
    }
}

/*
Expected Output (approximate):
=== WeakReference Demo ===

--- Demo 1: WeakReference ---
Before GC: weakRef.get() = Data("weak-data")
isEnqueued: false
After GC:  weakRef.get() = null
isEnqueued: true
  Data("weak-data") finalized

--- Demo 2: SoftReference ---
Before GC: softRef.get() = Data("soft-data")
After GC:  softRef.get() = Data("soft-data")
Soft references survive GC unless memory is low

--- Demo 3: PhantomReference ---
Before GC: phantomRef.get() = null
After GC: phantomRef.get() = null
Phantom reference enqueued: true
  Data("phantom-data") finalized

--- Demo 4: WeakHashMap ---
Before GC: map.size() = 1
After GC:  map.size() = 0
Entry automatically removed when key was GC'd
  Data("map-key") finalized

--- Demo 5: Simple Cache with WeakReference ---
Before GC: cache.get("key1") = Data("cached-value1")
After GC:  cache.get("key1") = null
Cache entry automatically cleared by GC
  Data("cached-value1") finalized

=== End of WeakReference Demo ===
*/
