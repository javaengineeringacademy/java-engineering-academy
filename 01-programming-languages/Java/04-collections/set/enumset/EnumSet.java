package academy.javaengineering.collections.set.enumset;

/**
 * EnumSet - Specialized Set for enum types. Extremely efficient.
 *
 * Internal: Bit vector (long or long[]) - one bit per enum constant
 * Values stored as ordinal bits, not objects
 *
 * Complexity: add O(1), remove O(1), contains O(1), iteration O(n) (very fast)
 * Thread-safe: NO (but all operations are atomic/single-instruction)
 *
 * All basic operations (add, contains, remove) execute in constant time.
 * Iteration is very fast - linear in number of elements, not capacity.
 * Cannot contain null (throws NullPointerException).
 * Cannot create EnumSet with parameterized types (use allOf/range/of).
 */
public abstract class EnumSet<E extends Enum<E>> extends java.util.AbstractSet<E> {

    Class<E> elementType;
    Enum<?>[] universe;

    /** Returns EnumSet with all constants of specified enum type */
    public static <E extends Enum<E>> EnumSet<E> allOf(Class<E> elementType) {
        EnumSet<E> result = noneOf(elementType);
        for (E e : elementType.getEnumConstants()) result.add(e);
        return result;
    }

    /** Returns empty EnumSet for specified enum type */
    public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType) {
        Enum<?>[] universe = elementType.getEnumConstants();
        if (universe == null) throw new ClassCastException(elementType + " is not an enum type");
        return new RegularEnumSet<>(elementType, universe);
    }

    /** Returns EnumSet with specified elements */
    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> of(E first, E... rest) {
        EnumSet<E> result = noneOf(first.getDeclaringClass());
        result.add(first);
        for (E e : rest) result.add(e);
        return result;
    }

    /** Returns EnumSet with range [from, to] inclusive */
    public static <E extends Enum<E>> EnumSet<E> range(E from, E to) {
        if (from.ordinal() > to.ordinal()) throw new IllegalArgumentException(from + " > " + to);
        EnumSet<E> result = noneOf(from.getDeclaringClass());
        for (E e : from.getDeclaringClass().getEnumConstants()) {
            if (e.ordinal() >= from.ordinal() && e.ordinal() <= to.ordinal()) result.add(e);
        }
        return result;
    }

    /** Bit-vector based implementation */
    private static class RegularEnumSet<E extends Enum<E>> extends EnumSet<E> {
        private long elements;

        RegularEnumSet(Class<E> elementType, Enum<?>[] universe) {
            this.elementType = elementType;
            this.universe = universe;
        }

        public boolean add(E e) {
            int ordinal = e.ordinal();
            long oldElements = elements;
            elements |= (1L << ordinal);
            return elements != oldElements;
        }

        public boolean remove(Object o) {
            int ordinal = ((Enum<?>) o).ordinal();
            long oldElements = elements;
            elements &= ~(1L << ordinal);
            return elements != oldElements;
        }

        public boolean contains(Object o) {
            int ordinal = ((Enum<?>) o).ordinal();
            return (elements & (1L << ordinal)) != 0;
        }

        public int size() { return Long.bitCount(elements); }
        public boolean isEmpty() { return elements == 0; }
        public void clear() { elements = 0; }
    }
}
