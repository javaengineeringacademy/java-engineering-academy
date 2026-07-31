package academy.javaengineering.oop.interfaces;

/**
 * Sortable - Interface demonstrating default methods.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Sortable {

    void add(int value);

    java.util.List<Integer> getAll();

    default void sort() {
        java.util.List<Integer> list = getAll();
        java.util.Collections.sort(list);
        System.out.println("  Sorted: " + list);
    }

    default int size() {
        return getAll().size();
    }

    default boolean isEmpty() {
        return getAll().isEmpty();
    }
}