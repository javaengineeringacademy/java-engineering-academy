package academy.javaengineering.collections.map.hashmap;

/**
 * HashMap - Hash table based Map implementation.
 *
 * Internal: Node<K,V>[] table (array of buckets), each bucket is a linked list
 *           (or tree when bucket size > 8, as of Java 8)
 * Hashing: hash(key) -> bucket index = hash & (n-1)
 * Collision: Chaining (linked list / red-black tree for large buckets)
 * Growth: 2x when load factor threshold reached (0.75 default)
 *
 * Complexity: put O(1), get O(1), remove O(1), containsKey O(1)
 * Thread-safe: NO (use ConcurrentHashMap for concurrent access)
 *
 * Keys must implement hashCode() and equals() properly.
 * Allows one null key and multiple null values.
 * No ordering guarantees.
 */
public class HashMap<K, V> extends java.util.AbstractMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;

    transient Node<K, V>[] table;
    transient int size;
    final float loadFactor;
    int threshold;

    static class Node<K, V> implements java.util.Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash; this.key = key; this.value = value; this.next = next;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public final int hashCode() { return key.hashCode() ^ value.hashCode(); }

        public final boolean equals(Object o) {
            if (o instanceof java.util.Map.Entry) {
                java.util.Map.Entry<?, ?> e = (java.util.Map.Entry<?, ?>) o;
                return key.equals(e.getKey()) && value.equals(e.getValue());
            }
            return false;
        }
    }

    public HashMap() { this.loadFactor = DEFAULT_LOAD_FACTOR; threshold = DEFAULT_INITIAL_CAPACITY; }

    public HashMap(int initialCapacity) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = initialCapacity;
    }

    /** Computes hash: spreads high bits for better distribution. O(1) */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /** Associates key with value. Returns previous value or null. O(1) */
    public V put(K key, V value) {
        int h = hash(key);
        int i = h & (table.length - 1);
        for (Node<K, V> e = table[i]; e != null; e = e.next) {
            if (e.hash == h && (key == e.key || key.equals(e.key))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        table[i] = new Node<>(h, key, value, table[i]);
        if (++size > threshold) resize();
        return null;
    }

    /** Returns value for key, or null. O(1) */
    public V get(Object key) {
        Node<K, V> e = getNode(hash(key), key);
        return e == null ? null : e.value;
    }

    Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab = table;
        int n = tab.length;
        Node<K, V> first = tab[hash & (n - 1)];
        for (Node<K, V> e = first; e != null; e = e.next) {
            if (e.hash == hash && (key == e.key || key.equals(e.key))) return e;
        }
        return null;
    }

    public boolean containsKey(Object key) { return getNode(hash(key), key) != null; }

    /** Removes mapping. Returns value or null. O(1) */
    public V remove(Object key) {
        Node<K, V> e = getNode(hash(key), key);
        if (e == null) return null;
        V oldValue = e.value;
        e.value = null;
        size--;
        return oldValue;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public void clear() { table = new Node[DEFAULT_INITIAL_CAPACITY]; size = 0; threshold = DEFAULT_INITIAL_CAPACITY; }

    /** Doubles capacity and rehashes all entries. O(n) */
    void resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap = oldCap << 1;
        threshold = (int) (newCap * loadFactor);
        Node<K, V>[] newTab = new Node[newCap];
        for (int j = 0; j < oldCap; j++) {
            Node<K, V> e = oldTab[j];
            if (e != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else {
                    Node<K, V> loHead = null, loTail = null, hiHead = null, hiTail = null;
                    Node<K, V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null) loHead = e; else loTail.next = e;
                            loTail = e;
                        } else {
                            if (hiTail == null) hiHead = e; else hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) { loTail.next = null; newTab[j] = loHead; }
                    if (hiTail != null) { hiTail.next = null; newTab[j + oldCap] = hiHead; }
                }
            }
        }
        table = newTab;
    }
}
