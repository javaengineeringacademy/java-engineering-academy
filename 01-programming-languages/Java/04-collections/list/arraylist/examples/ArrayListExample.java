package list.arraylist.examples;

import java.util.*;

public class ArrayListExample {

    public static void main(String[] args) {
        example1_CreationAndInitialization();
        example2_AddRemoveElements();
        example3_CapacityVsSize();
        example4_CloneAndCopy();
        example5_BulkOperations();
    }

    static void example1_CreationAndInitialization() {
        System.out.println("=== Example 1: Creation and Initialization ===");
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("A", "B", "C"));
        ArrayList<String> list3 = new ArrayList<>(100);
        System.out.println("Empty: " + list1);
        System.out.println("From array: " + list2);
        System.out.println("With capacity 100, size: " + list3.size());
    }

    static void example2_AddRemoveElements() {
        System.out.println("\n=== Example 2: Add and Remove Elements ===");
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Java", "Python"));
        list.add("C++");
        list.add(1, "JavaScript");
        System.out.println("After adds: " + list);
        list.remove(0);
        System.out.println("After remove(0): " + list);
        list.remove("C++");
        System.out.println("After remove(C++): " + list);
    }

    static void example3_CapacityVsSize() {
        System.out.println("\n=== Example 3: Capacity vs Size ===");
        ArrayList<Integer> list = new ArrayList<>(5);
        System.out.println("Initial capacity: " + list.size());
        for (int i = 0; i < 10; i++) {
            list.add(i * 10);
        }
        System.out.println("Size after adding 10: " + list.size());
        list.trimToSize();
        System.out.println("After trimToSize: " + list.size());
    }

    static void example4_CloneAndCopy() {
        System.out.println("\n=== Example 4: Clone and Copy ===");
        ArrayList<String> original = new ArrayList<>(Arrays.asList("A", "B", "C"));
        ArrayList<String> cloned = (ArrayList<String>) original.clone();
        ArrayList<String> copied = new ArrayList<>(original);
        cloned.set(0, "X");
        System.out.println("Original: " + original);
        System.out.println("Cloned (modified): " + cloned);
        System.out.println("Copied: " + copied);
    }

    static void example5_BulkOperations() {
        System.out.println("\n=== Example 5: Bulk Operations ===");
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
        list.addAll(Arrays.asList("D", "E", "F"));
        System.out.println("After addAll: " + list);
        System.out.println("SubList(1,4): " + list.subList(1, 4));
        System.out.println("Contains A: " + list.contains("A"));
        System.out.println("Contains Z: " + list.contains("Z"));
    }
}
