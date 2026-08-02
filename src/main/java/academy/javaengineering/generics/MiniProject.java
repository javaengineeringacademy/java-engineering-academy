package academy.javaengineering.generics;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Topic 09: Mini Project - Type-Safe Collection Framework.
 *
 * <p>A simplified, type-safe collection framework demonstrating
 * all generic concepts learned in this module.</p>
 */
public final class MiniProject {

    private MiniProject() {
    }

    /**
     * SimpleList interface - core collection operations.
     */
    public interface SimpleList<E> extends Iterable<E> {
        boolean add(E element);
        E get(int index);
        E set(int index, E element);
        E remove(int index);
        int size();
        boolean isEmpty();
        boolean contains(E element);
        void clear();
    }

    /**
     * SimpleIterator interface.
     */
    public interface SimpleIterator<E> {
        boolean hasNext();
        E next();
    }

    /**
     * ArrayList implementation.
     */
    public static class ArrayList<E> implements SimpleList<E> {
        private static final int DEFAULT_CAPACITY = 10;
        private Object[] elements;
        private int size;

        public ArrayList() {
            elements = new Object[DEFAULT_CAPACITY];
        }

        public ArrayList(int initialCapacity) {
            if (initialCapacity < 0) {
                throw new IllegalArgumentException("Capacity: " + initialCapacity);
            }
            elements = new Object[initialCapacity];
        }

        @Override
        public boolean add(E element) {
            ensureCapacity();
            elements[size++] = element;
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E get(int index) {
            checkIndex(index);
            return (E) elements[index];
        }

        @Override
        @SuppressWarnings("unchecked")
        public E set(int index, E element) {
            checkIndex(index);
            E old = (E) elements[index];
            elements[index] = element;
            return old;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E remove(int index) {
            checkIndex(index);
            E old = (E) elements[index];
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(elements, index + 1, elements, index, numMoved);
            }
            elements[--size] = null;
            return old;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean contains(E element) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(elements[i], element)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void clear() {
            for (int i = 0; i < size; i++) {
                elements[i] = null;
            }
            size = 0;
        }

        @Override
        public Iterator<E> iterator() {
            return new ArrayIterator();
        }

        private void ensureCapacity() {
            if (size == elements.length) {
                elements = java.util.Arrays.copyOf(elements, elements.length * 2);
            }
        }

        private void checkIndex(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException(
                        "Index: " + index + ", Size: " + size);
            }
        }

        private class ArrayIterator implements Iterator<E> {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) elements[cursor++];
            }
        }
    }

    /**
     * LinkedList implementation.
     */
    public static class LinkedList<E> implements SimpleList<E> {
        private Node<E> first;
        private Node<E> last;
        private int size;

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

        @Override
        public boolean add(E element) {
            final Node<E> l = last;
            final Node<E> newNode = new Node<>(l, element, null);
            last = newNode;
            if (l == null) {
                first = newNode;
            } else {
                l.next = newNode;
            }
            size++;
            return true;
        }

        @Override
        public E get(int index) {
            checkIndex(index);
            return node(index).item;
        }

        @Override
        public E set(int index, E element) {
            checkIndex(index);
            Node<E> x = node(index);
            E oldVal = x.item;
            x.item = element;
            return oldVal;
        }

        @Override
        public E remove(int index) {
            checkIndex(index);
            return unlink(node(index));
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean contains(E element) {
            for (E x : this) {
                if (Objects.equals(x, element)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void clear() {
            for (Node<E> x = first; x != null; ) {
                Node<E> next = x.next;
                x.item = null;
                x.next = null;
                x.prev = null;
                x = next;
            }
            first = last = null;
            size = 0;
        }

        @Override
        public Iterator<E> iterator() {
            return new ListIterator();
        }

        private Node<E> node(int index) {
            Node<E> x;
            if (index < (size >> 1)) {
                x = first;
                for (int i = 0; i < index; i++) {
                    x = x.next;
                }
            } else {
                x = last;
                for (int i = size - 1; i > index; i--) {
                    x = x.prev;
                }
            }
            return x;
        }

        private E unlink(Node<E> x) {
            final E element = x.item;
            final Node<E> next = x.next;
            final Node<E> prev = x.prev;

            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.item = null;
            size--;
            return element;
        }

        private void checkIndex(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException(
                        "Index: " + index + ", Size: " + size);
            }
        }

        private class ListIterator implements Iterator<E> {
            private Node<E> next;
            private int nextIndex;

            ListIterator() {
                next = (size > 0) ? first : null;
            }

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E item = next.item;
                next = next.next;
                nextIndex++;
                return item;
            }
        }
    }

    /**
     * Collections utility class.
     */
    public static final class Collections {

        private Collections() {
        }

        @SafeVarargs
        public static <T> boolean addAll(SimpleList<? super T> dest, T... elements) {
            for (T element : elements) {
                dest.add(element);
            }
            return elements.length > 0;
        }

        public static <T> void swap(SimpleList<T> list, int i, int j) {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }

        public static <T extends Comparable<T>> T max(SimpleList<? extends T> list) {
            T max = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                T current = list.get(i);
                if (current.compareTo(max) > 0) {
                    max = current;
                }
            }
            return max;
        }

        public static <T> SimpleList<T> filter(
                SimpleList<? extends T> source,
                Predicate<? super T> predicate) {
            SimpleList<T> result = new ArrayList<>();
            for (T item : source) {
                if (predicate.test(item)) {
                    result.add(item);
                }
            }
            return result;
        }

        public static <T, R> SimpleList<R> map(
                SimpleList<? extends T> source,
                Function<? super T, ? extends R> mapper) {
            SimpleList<R> result = new ArrayList<>();
            for (T item : source) {
                result.add(mapper.apply(item));
            }
            return result;
        }

        public static <T extends Comparable<T>> void sort(SimpleList<T> list) {
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = 0; j < list.size() - i - 1; j++) {
                    if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                        swap(list, j, j + 1);
                    }
                }
            }
        }

        public static <T> String toString(SimpleList<T> list) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(list.get(i));
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /**
     * Demonstrates the collection framework.
     */
    public static void main(String[] args) {
        System.out.println("=== ArrayList Demo ===");
        SimpleList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, "Alice", "Bob", "Charlie");
        System.out.println("ArrayList: " + Collections.toString(arrayList));

        System.out.println("\n=== LinkedList Demo ===");
        SimpleList<Integer> linkedList = new LinkedList<>();
        Collections.addAll(linkedList, 5, 3, 1, 4, 2);
        System.out.println("Before sort: " + Collections.toString(linkedList));

        Collections.sort(linkedList);
        System.out.println("After sort: " + Collections.toString(linkedList));

        System.out.println("\n=== Utility Methods ===");
        SimpleList<Integer> evens = Collections.filter(linkedList, n -> n % 2 == 0);
        System.out.println("Evens: " + Collections.toString(evens));

        SimpleList<String> strings = Collections.map(linkedList, n -> "Num: " + n);
        System.out.println("Mapped: " + Collections.toString(strings));

        System.out.println("Max: " + Collections.max(linkedList));

        System.out.println("\n=== Iterator Demo ===");
        for (String name : arrayList) {
            System.out.println("  " + name);
        }
    }
}
