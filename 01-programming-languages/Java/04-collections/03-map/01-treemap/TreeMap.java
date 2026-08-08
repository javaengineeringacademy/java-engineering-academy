package academy.javaengineering.collections.map.treemap;

/**
 * TreeMap - Red-Black tree based sorted Map.
 *
 * Internal: Red-Black self-balancing BST (binary search tree)
 * Nodes colored red or black to maintain balance invariants
 * All operations O(log n) guaranteed
 *
 * Complexity: put O(log n), get O(log n), remove O(log n), containsKey O(log n)
 * Thread-safe: NO
 *
 * Keys in sorted order (natural ordering or Comparator).
 * No null keys allowed (if Comparator not provided).
 * Supports navigation: firstKey, lastKey, headMap, tailMap, subMap.
 * Preferred when you need sorted key iteration or range operations.
 */
public class TreeMap<K, V> extends java.util.AbstractMap<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    static class Node<K, V> implements java.util.Map.Entry<K, V> {
        K key;
        V value;
        Node<K, V> left, right, parent;
        boolean color = RED;

        Node(K key, V value, Node<K, V> parent) {
            this.key = key; this.value = value; this.parent = parent;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
        public V setValue(V value) { V old = this.value; this.value = value; return old; }
    }

    private Node<K, V> root;
    private int size = 0;
    private final java.util.Comparator<? super K> comparator;

    public TreeMap() { this.comparator = null; }
    public TreeMap(java.util.Comparator<? super K> comparator) { this.comparator = comparator; }

    /** Compares two keys. Returns <0, 0, >0 */
    @SuppressWarnings("unchecked")
    private int compare(K k1, K k2) {
        if (comparator != null) return comparator.compare(k1, k2);
        return ((Comparable<? super K>) k1).compareTo(k2);
    }

    /** Adds key-value pair. O(log n) */
    public V put(K key, V value) {
        Node<K, V> parent = null;
        Node<K, V> current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            parent = current;
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else { V old = current.value; current.value = value; return old; }
        }
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (parent == null) root = newNode;
        else if (compare(key, parent.key) < 0) parent.left = newNode;
        else parent.right = newNode;
        size++;
        fixAfterInsert(newNode);
        return null;
    }

    /** Returns value for key. O(log n) */
    public V get(Object key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    Node<K, V> getNode(Object key) {
        Node<K, V> current = root;
        while (current != null) {
            @SuppressWarnings("unchecked")
            int cmp = compare((K) key, current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else return current;
        }
        return null;
    }

    public boolean containsKey(Object key) { return getNode(key) != null; }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public K firstKey() {
        Node<K, V> x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    public K lastKey() {
        Node<K, V> x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    public java.util.Set<K> keySet() { return new java.util.TreeSet<>(this); }

    /** Left rotate around x. O(1) */
    private void rotateLeft(Node<K, V> x) {
        Node<K, V> y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    /** Right rotate around x. O(1) */
    private void rotateRight(Node<K, V> x) {
        Node<K, V> y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    /** Fix Red-Black properties after insertion. O(log n) rotations */
    private void fixAfterInsert(Node<K, V> x) {
        while (x != root && x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                Node<K, V> uncle = x.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    x.parent.color = BLACK; uncle.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) { x = x.parent; rotateLeft(x); }
                    x.parent.color = BLACK; x.parent.parent.color = RED;
                    rotateRight(x.parent.parent);
                }
            } else {
                Node<K, V> uncle = x.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    x.parent.color = BLACK; uncle.color = BLACK;
                    x.parent.parent.color = RED; x = x.parent.parent;
                } else {
                    if (x == x.parent.left) { x = x.parent; rotateRight(x); }
                    x.parent.color = BLACK; x.parent.parent.color = RED;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }
}
