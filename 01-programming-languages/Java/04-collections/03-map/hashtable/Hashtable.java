package academy.javaengineering.collections.map.hashtable;

/**
 * Hashtable - Legacy synchronized hash table (thread-safe).
 *
 * Internal: Entry<K,V>[] table (similar to HashMap)
 * Hashing: hash(key) with additional spread (key.hashCode())
 * Growth: 2x + 1 when threshold reached
 *
 * Complexity: put O(1), get O(1), remove O(1), containsKey O(1)
 * Thread-safe: YES (all methods synchronized)
 *
 * Legacy: Prefer ConcurrentHashMap for new code.
 * Does NOT allow null keys or null values.
 * Slower than HashMap due to synchronization overhead.
 * Synchronized at method level - coarse-grained locking.
 */
public class Hashtable<K, V> extends java.util.Dictionary<K, V> implements java.util.Map<K, V> {

    private transient java.util.Hashtable.Entry<K, V>[] table;
    private int count;
    private int threshold;
    private float loadFactor;

    public Hashtable(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
        table = new java.util.Hashtable.Entry[initialCapacity];
    }

    public Hashtable(int initialCapacity) { this(initialCapacity, 0.75f); }
    public Hashtable() { this(11, 0.75f); }

    /** Adds key-value pair. Returns previous value or null. Synchronized. O(1) */
    public synchronized V put(K key, V value) {
        if (value == null) throw new NullPointerException();
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % table.length;
        for (java.util.Hashtable.Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }
        if (count >= threshold) rehash();
        int newIndex = (hash & 0x7FFFFFFF) % table.length;
        java.util.Hashtable.Entry<K, V> newEntry = new java.util.Hashtable.Entry<>(hash, key, value, table[newIndex]);
        table[newIndex] = newEntry;
        count++;
        return null;
    }

    /** Returns value for key. Synchronized. O(1) */
    public synchronized V get(Object key) {
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % table.length;
        for (java.util.Hashtable.Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) return e.value;
        }
        return null;
    }

    public synchronized boolean containsKey(Object key) { return get(key) != null; }
    public synchronized int size() { return count; }
    public synchronized boolean isEmpty() { return count == 0; }

    public synchronized V remove(Object key) {
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % table.length;
        java.util.Hashtable.Entry<K, V> prev = null;
        for (java.util.Hashtable.Entry<K, V> e = table[index]; e != null; prev = e, e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                if (prev != null) prev.next = e.next;
                else table[index] = e.next;
                count--;
                return e.value;
            }
        }
        return null;
    }

    /** Doubles table size and rehashes. O(n) */
    protected void rehash() {
        int oldCapacity = table.length;
        java.util.Hashtable.Entry<K, V>[] oldTable = table;
        int newCapacity = oldCapacity * 2 + 1;
        java.util.Hashtable.Entry<K, V>[] newTable = new java.util.Hashtable.Entry[newCapacity];
        threshold = (int) (newCapacity * loadFactor);
        for (int i = 0; i < oldCapacity; i++) {
            java.util.Hashtable.Entry<K, V> e = oldTable[i];
            while (e != null) {
                java.util.Hashtable.Entry<K, V> next = e.next;
                int newIndex = (e.hash & 0x7FFFFFFF) % newCapacity;
                e.next = newTable[newIndex];
                newTable[newIndex] = e;
                e = next;
            }
        }
        table = newTable;
    }
}
