package academy.javaengineering.collections.map.linkedhashmap;

import java.util.HashMap;

/**
 * LinkedHashMap - Hash table + doubly-linked list maintaining iteration order.
 *
 * Internal: HashMap + doubly-linked list (accessOrder or insertionOrder)
 * Order: Insertion order (default) or access order (accessOrder=true)
 *
 * Complexity: put O(1), get O(1), remove O(1), iteration O(n)
 * Thread-safe: NO
 *
 * Best for: When you need Map behavior with predictable iteration order.
 * Can create LRU cache with accessOrder=true + removeEldestEntry().
 * Slightly slower than HashMap due to linked list maintenance overhead.
 */
public class LinkedHashMap<K, V> extends HashMap<K, V> {

    transient Entry<K, V> head;
    transient Entry<K, V> tail;
    final boolean accessOrder;

    static class Entry<K, V> extends HashMap.Node<K, V> {
        Entry<K, V> before, after;

        Entry(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
        }
    }

    public LinkedHashMap() { super(); accessOrder = false; }

    public LinkedHashMap(boolean accessOrder) { super(); this.accessOrder = accessOrder; }

    /** Adds entry at end of linked list. O(1) */
    void afterNodeInsertion(boolean evict) {
        if (evict) {
            Entry<K, V> eldest = head;
            if (eldest != null && removeEldestEntry(eldest)) {
                removeNode(eldest.hash, eldest.key, null, false, true);
            }
        }
    }

    /** Override to implement LRU cache: return true when size > max */
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return false;
    }

    /** Moves accessed entry to end of list. O(1) */
    void afterNodeAccess(Node<K, V> e) {
        if (accessOrder && e != null) {
            Entry<K, V> last = tail;
            if (e == last) return;
            Entry<K, V> p = (Entry<K, V>) e, b = p.before, a = p.after;
            p.after = null;
            if (b == null) head = a; else b.after = a;
            if (a != null) a.before = b; else last = b;
            p.before = last;
            if (last == null) head = p; else last.after = p;
            tail = p;
        }
    }

    public V get(Object key) {
        V val = super.get(key);
        if (val != null && accessOrder) {
            Node<K, V> e = getNode(hash(key), key);
            if (e != null) afterNodeAccess(e);
        }
        return val;
    }

    /** Returns keys in order. O(n) */
    public java.util.Set<K> keySet() {
        return new java.util.LinkedHashSet<K>() {
            public java.util.Iterator<K> iterator() {
                return new java.util.Iterator<K>() {
                    Entry<K, V> current = head;
                    public boolean hasNext() { return current != null; }
                    public K next() { K k = current.key; current = current.after; return k; }
                };
            }
            public int size() { return LinkedHashMap.this.size(); }
        };
    }
}
