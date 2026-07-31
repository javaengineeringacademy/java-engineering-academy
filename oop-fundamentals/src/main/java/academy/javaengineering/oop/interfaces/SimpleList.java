package academy.javaengineering.oop.interfaces;

/**
 * SimpleList - Concrete implementation of Sortable interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SimpleList implements Sortable {

    private final java.util.List<Integer> list = new java.util.ArrayList<>();

    @Override
    public void add(int value) {
        list.add(value);
    }

    @Override
    public java.util.List<Integer> getAll() {
        return new java.util.ArrayList<>(list);
    }

    @Override
    public String toString() {
        return "SimpleList{values=" + list + "}";
    }
}