package academy.javaengineering.collections.sorting.comparable;

import java.util.*;

public class ComparableExample implements Comparable<ComparableExample> {
    private int value;
    public ComparableExample(int value) { this.value = value; }
    public int compareTo(ComparableExample other) { return this.value - other.value; }
    public String toString() { return String.valueOf(value); }
    public static void main(String[] args) {
        List<ComparableExample> list = new ArrayList<>();
        list.add(new ComparableExample(5));
        list.add(new ComparableExample(1));
        list.add(new ComparableExample(3));
        Collections.sort(list);
        System.out.println("Sorted: " + list);
    }
}
