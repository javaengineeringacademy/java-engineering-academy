package academy.javaengineering.collections.map.weakhashmap;

import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;

/**
 * WeakHashMap - Hash table with weak reference keys.
 *
 * Internal: Entry<K,V> extends WeakReference<Object> + HashMap structure
 * Keys: WeakReference - eligible for GC when no strong references remain
 * Entries automatically removed by GC when key is collected
 *
 * Complexity: put O(1), get O(1), remove O(1) amortized
 * Thread-safe: NO
 *
 * Best for: Caches where entries should be garbage collected when no longer referenced.
 * Not suitable for keys with short lifetimes (may be collected before use).
 * Entries with null key or value are kept (value acts as strong reference).
 */
public class WeakHashMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Entry<K, V> extends WeakReference<Object> implements java.util.Map.Entry<K, V> {
        V value;
        int hash;
        Entry<K, V> next;

        Entry(Object key, V value, int hash, ReferenceQueue<Object> queue, Entry<K, V> next) {
            super(key, queue);
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        public K getKey() { return (K) get(); }
        public V getValue() { return value; }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    private ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private Entry<K, V>[] table;
    private int size;
    private float loadFactor;
    private int threshold;

    public WeakHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public WeakHashMap(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
        table = new Entry[initialCapacity];
    }

    /** Puts key-value pair. Key is held via weak reference. O(1) */
    public V put(K key, V value) {
        processQueue();
        int hash = hash(key);
        int i = hash & (table.length - 1);
        for (Entry<K, V> e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && key.equals(e.get())) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }
        if (size >= threshold) rehash();
        i = (hash & (table.length - 1));
        Entry<K, V> newEntry = new Entry<>(key, value, hash, queue, table[i]);
        table[i] = newEntry;
        size++;
        return null;
    }

    /** Returns value for key. O(1) */
    public V get(Object key) {
        processQueue();
        int hash = hash(key);
        int i = hash & (table.length - 1);
        for (Entry<K, V> e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && key.equals(e.get())) return e.value;
        }
        return null;
    }

    /** Removes entries whose keys have been garbage collected */
    private void processQueue() {
        Entry<K, V> ref;
        while ((ref = (Entry<K, V>) queue.poll()) != null) {
            int i = ref.hash & (table.length - 1);
            Entry<K, V> prev = null, curr = table[i];
            while (curr != null) {
                if (curr == ref) {
                    if (prev != null) prev.next = curr.next;
                    else table[i] = curr.next;
                    size--;
                    break;
                }
                prev = curr;
                curr = curr.next;
            }
        }
    }

    public int size() { processQueue(); return size; }
    public boolean isEmpty() { return size() == 0; }

    private int hash(Object key) {
        int h = key.hashCode();
        return (h ^ (h >>> 16)) & 0x7FFFFFFF;
    }

    private void rehash() {
        int oldCapacity = table.length;
        Entry<K, V>[] oldTable = table;
        int newCapacity = oldCapacity << 1;
        table = new Entry[newCapacity];
        threshold = (int) (newCapacity * loadFactor);
        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> e = oldTable[i];
            while (e != null) {
                Entry<K, V> next = e.next;
                int j = e.hash & (newCapacity - 1);
                e.next = table[j];
                table[j] = e;
                e = next;
            }
        }
    }
}
