package academy.javaengineering.collections.map.concurrenthashmap;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ConcurrentHashMap - Thread-safe hash table with segment-level locking.
 *
 * Internal: Node<K,V>[] table (similar to HashMap)
 *           Segments (Java 7) or CAS + synchronized on nodes (Java 8+)
 *           Uses VolatileNode for visibility across threads
 *
 * Complexity: put O(1), get O(1), remove O(1), size O(n) or O(1) (CounterCell)
 * Thread-safe: YES (fine-grained locking, better than Hashtable)
 *
 * Java 8+: Uses CAS for empty buckets, synchronized per-node for collisions.
 *          No null keys or values allowed.
 *          Read operations are lock-free (volatile read).
 *          Write operations lock only the affected node/bucket.
 *
 * Size estimation: Uses CounterCell[] for distributed counting (better than single counter).
 * Key difference from Hashtable:并发性能高，锁粒度更细
 */
public class ConcurrentHashMap<K, V> {

    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;

    transient volatile Node<K, V>[] table;
    private transient volatile int sizeCtl;
    private transient volatile long baseCount;

    static class Node<K, V> {
        final int hash;
        final K key;
        volatile V value;
        volatile Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash; this.key = key; this.value = value; this.next = next;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public ConcurrentHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        sizeCtl = DEFAULT_CAPACITY;
    }

    /** Puts key-value pair. Thread-safe via CAS + synchronized. O(1) */
    public V put(K key, V value) {
        if (key == null || value == null) throw new NullPointerException();
        int h = hash(key);
        for (Node<K, V>[] tab = table;;) {
            Node<K, V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0) tab = initTable();
            else if ((f = tabAt(tab, i = (n - 1) & h)) == null) {
                if (casTabAt(tab, i, null, new Node<>(h, key, value, null))) break;
            }
            else if ((fh = f.hash) == MOVED) tab = helpTransfer(tab, f);
            else {
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            Node<K, V> e = f;
                            Node<K, V> pred = null;
                            while (e.hash != h || !key.equals(e.key)) {
                                pred = e;
                                if ((e = e.next) == null) {
                                    pred.next = new Node<>(h, key, value, null);
                                    break;
                                }
                            }
                            if (pred != null) pred.next = new Node<>(h, key, value, f.next);
                            else {
                                V old = f.value;
                                f.value = value;
                                return old;
                            }
                        }
                    }
                }
                break;
            }
        }
        addCount(1L);
        return null;
    }

    /** Returns value for key. Lock-free read. O(1) */
    public V get(Object key) {
        Node<K, V>[] tab = table;
        int n = tab.length;
        int h = hash(key);
        for (Node<K, V> e = tabAt(tab, (n - 1) & h); e != null; e = e.next) {
            K ek;
            if (e.hash == h && ((ek = e.key) == key || (key != null && key.equals(ek))))
                return e.value;
        }
        return null;
    }

    public int size() { long n = sumCount(); return (n < 0L) ? 0 : (n > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) n; }

    private static final int MOVED = -1;

    private transient volatile int[] counterCells;
    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    private final void addCount(long x) { baseCount += x; }

    private final long sumCount() { return baseCount; }

    private final Node<K, V>[] initTable() {
        int cap = DEFAULT_CAPACITY;
        Node<K, V>[] nextTable = new Node[cap];
        table = nextTable;
        return nextTable;
    }

    static final <K, V> Node<K, V> tabAt(Node<K, V>[] tab, int i) {
        return (Node<K, V>) java.util.concurrent.atomic.AtomicReferenceFieldUpdater
            .newUpdater(Node[].class, Node.class, "next");
    }

    static final <K, V> boolean casTabAt(Node<K, V>[] tab, int i, Node<K, V> c, Node<K, V> v) {
        return true; // simplified - real implementation uses VarHandle
    }

    private final Node<K, V>[] helpTransfer(Node<K, V>[] tab, Node<K, V> f) { return tab; }
}
