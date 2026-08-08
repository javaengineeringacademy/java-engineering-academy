package academy.javaengineering.collections.list.stack;

/**
 * Stack - LIFO (Last-In-First-Out) stack. Legacy class.
 *
 * Internal: Extends Vector (synchronized dynamic array)
 * Growth: 2x (inherited from Vector)
 *
 * Complexity: push O(1)*, pop O(1), peek O(1), search O(n)
 * Thread-safe: YES (inherited from Vector - all methods synchronized)
 *
 * Legacy: Prefer ArrayDeque as a stack (push/pop/peek).
 * Stack extends Vector, so it has index-based methods that break LIFO semantics.
 */
public class Stack<E> extends java.util.Vector<E> {

    public Stack() { super(); }

    /** Pushes element onto top of stack. Returns the element. O(1) */
    public E push(E item) {
        addElement(item);
        return item;
    }

    /** Removes and returns top element. O(1) */
    public synchronized E pop() {
        int len = size();
        if (len == 0) throw new java.util.EmptyStackException();
        return remove(len - 1);
    }

    /** Returns top element without removing. O(1) */
    public synchronized E peek() {
        int len = size();
        if (len == 0) throw new java.util.EmptyStackException();
        return get(len - 1);
    }

    /** Returns true if empty. O(1) */
    public boolean empty() { return isEmpty(); }

    /** Returns 1-based position from top, or -1 if not found. O(n) */
    public synchronized int search(Object o) {
        int i = lastIndexOf(o);
        if (i >= 0) return size() - i;
        return -1;
    }
}
