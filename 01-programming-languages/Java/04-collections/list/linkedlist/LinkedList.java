package academy.javaengineering.collections.list.linkedlist;

/**
 * LinkedList - Doubly-linked list implementation of List and Deque.
 *
 * Internal: Node{item, prev, next} <-> Node <-> Node
 * head (first) <-> Node <-> ... <-> Node <-> tail (last)
 *
 * Complexity: get O(n), addFirst/addLast O(1), removeFirst/removeLast O(1)
 * Thread-safe: NO
 *
 * Best for: Frequent add/remove at ends. Worst for: random access.
 */
public class LinkedList<E> implements java.util.List<E>, java.util.Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    transient Node<E> first;
    transient Node<E> last;
    transient int size = 0;

    /** Inserts element at beginning. O(1) */
    public void addFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) last = newNode; else f.prev = newNode;
        size++;
    }

    /** Appends element to end. O(1) */
    public void addLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) first = newNode; else l.next = newNode;
        size++;
    }

    public boolean add(E e) { addLast(e); return true; }

    /** Returns element at index. O(n) - traverses from nearest end */
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E old = x.item;
        x.item = element;
        return old;
    }

    /** Inserts at index. O(n) traversal + O(1) insert */
    public void add(int index, E element) {
        checkPositionIndex(index);
        if (index == size) addLast(element);
        else linkBefore(element, node(index));
    }

    /** Removes at index. O(n) traversal + O(1) remove */
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    public boolean remove(Object o) {
        for (Node<E> x = first; x != null; x = x.next)
            if (o == null ? x.item == null : o.equals(x.item)) { unlink(x); return true; }
        return false;
    }

    public E removeFirst() {
        final Node<E> f = first;
        if (f == null) throw new java.util.NoSuchElementException();
        return unlinkFirst(f);
    }

    public E removeLast() {
        final Node<E> l = last;
        if (l == null) throw new java.util.NoSuchElementException();
        return unlinkLast(l);
    }

    public E peekFirst() { return first == null ? null : first.item; }
    public E peekLast() { return last == null ? null : last.item; }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public boolean contains(Object o) {
        for (Node<E> x = first; x != null; x = x.next)
            if (o == null ? x.item == null : o.equals(x.item)) return true;
        return false;
    }

    public int indexOf(Object o) {
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next, i++)
            if (o == null ? x.item == null : o.equals(x.item)) return i;
        return -1;
    }

    public void clear() {
        for (Node<E> x = first; x != null;) {
            Node<E> next = x.next;
            x.item = null; x.next = null; x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    /** Traverses to node at index from nearest end. O(n) */
    Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private E unlinkFirst(Node<E> f) {
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null; f.next = null;
        first = next;
        if (next == null) last = null; else next.prev = null;
        size--;
        return element;
    }

    private E unlinkLast(Node<E> l) {
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null; l.prev = null;
        last = prev;
        if (prev == null) first = null; else prev.next = null;
        size--;
        return element;
    }

    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;
        if (prev == null) first = next; else prev.next = next;
        if (next == null) last = prev; else next.prev = prev;
        x.item = null; x.next = null; x.prev = null;
        size--;
        return element;
    }

    void linkBefore(E e, Node<E> succ) {
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null) first = newNode; else pred.next = newNode;
        size++;
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkPositionIndex(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    public java.util.Iterator<E> iterator() {
        return new java.util.Iterator<E>() {
            Node<E> current = first;
            public boolean hasNext() { return current != null; }
            public E next() { E val = current.item; current = current.next; return val; }
        };
    }
}
