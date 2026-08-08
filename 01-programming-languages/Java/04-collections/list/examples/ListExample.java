package list.examples;

import java.util.*;

public class ListExample {

    public static void main(String[] args) {
        example1_BasicListOperations();
        example2_IndexBasedAccess();
        example3_ListIteration();
        example4_ListSubList();
        example5_ListSorting();
    }

    static void example1_BasicListOperations() {
        System.out.println("=== Example 1: Basic List Operations ===");
        List<String> list = new ArrayList<>();
        list.add("Java");
        list.add("Python");
        list.add("C++");
        list.add("JavaScript");
        System.out.println("List: " + list);
        System.out.println("Size: " + list.size());
    }

    static void example2_IndexBasedAccess() {
        System.out.println("\n=== Example 2: Index-Based Access ===");
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        System.out.println("Element at index 0: " + list.get(0));
        System.out.println("Element at index 2: " + list.get(2));
        list.set(1, "X");
        System.out.println("After set(1, X): " + list);
        System.out.println("Index of C: " + list.indexOf("C"));
    }

    static void example3_ListIteration() {
        System.out.println("\n=== Example 3: List Iteration ===");
        List<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));
        System.out.print("For-each: ");
        for (Integer num : list) {
            System.out.print(num + " ");
        }
        System.out.println();
        System.out.print("ListIterator: ");
        ListIterator<Integer> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            System.out.print(listIterator.previous() + " ");
        }
        System.out.println();
    }

    static void example4_ListSubList() {
        System.out.println("\n=== Example 4: SubList ===");
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        List<String> subList = list.subList(1, 4);
        System.out.println("Original: " + list);
        System.out.println("SubList(1,4): " + subList);
    }

    static void example5_ListSorting() {
        System.out.println("\n=== Example 5: List Sorting ===");
        List<String> list = new ArrayList<>(Arrays.asList("Banana", "Apple", "Cherry", "Date"));
        System.out.println("Before sort: " + list);
        Collections.sort(list);
        System.out.println("After sort: " + list);
        Collections.sort(list, Comparator.reverseOrder());
        System.out.println("Reverse sort: " + list);
    }
}
