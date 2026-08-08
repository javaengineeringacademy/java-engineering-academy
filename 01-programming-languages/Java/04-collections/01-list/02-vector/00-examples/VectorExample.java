package list.vector.examples;

import java.util.*;

public class VectorExample {

    public static void main(String[] args) {
        example1_BasicVectorOperations();
        example2_SynchronizedAccess();
        example3_VectorCapacity();
        example4_VectorEnumeration();
        example5_VectorVsArrayList();
    }

    static void example1_BasicVectorOperations() {
        System.out.println("=== Example 1: Basic Vector Operations ===");
        Vector<String> vector = new Vector<>();
        vector.add("Java");
        vector.add("Python");
        vector.add("C++");
        System.out.println("Vector: " + vector);
        System.out.println("Element at 1: " + vector.get(1));
        System.out.println("Size: " + vector.size());
    }

    static void example2_SynchronizedAccess() {
        System.out.println("\n=== Example 2: Synchronized Access ===");
        Vector<Integer> vector = new Vector<>(Arrays.asList(1, 2, 3, 4, 5));
        synchronized (vector) {
            for (Integer num : vector) {
                System.out.print(num + " ");
            }
        }
        System.out.println();
    }

    static void example3_VectorCapacity() {
        System.out.println("\n=== Example 3: Vector Capacity ===");
        Vector<Integer> vector = new Vector<>(5);
        System.out.println("Initial capacity: " + vector.capacity());
        for (int i = 0; i < 20; i++) {
            vector.add(i);
        }
        System.out.println("Capacity after 20 adds: " + vector.capacity());
        System.out.println("Size: " + vector.size());
    }

    static void example4_VectorEnumeration() {
        System.out.println("\n=== Example 4: Vector with Enumeration ===");
        Vector<String> vector = new Vector<>(Arrays.asList("A", "B", "C", "D"));
        Enumeration<String> enumeration = vector.elements();
        System.out.print("Enumeration: ");
        while (enumeration.hasMoreElements()) {
            System.out.print(enumeration.nextElement() + " ");
        }
        System.out.println();
    }

    static void example5_VectorVsArrayList() {
        System.out.println("\n=== Example 5: Vector vs ArrayList ===");
        long startVec = System.nanoTime();
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < 10000; i++) vector.add(i);
        long endVec = System.nanoTime();

        long startArr = System.nanoTime();
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) arrayList.add(i);
        long endArr = System.nanoTime();

        System.out.println("Vector add 10k: " + (endVec - startVec) + " ns");
        System.out.println("ArrayList add 10k: " + (endArr - startArr) + " ns");
    }
}
