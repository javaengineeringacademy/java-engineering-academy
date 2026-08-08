package list.copyonwritearraylist.examples;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class COWExample {

    public static void main(String[] args) throws InterruptedException {
        example1_BasicOperations();
        example2_ThreadSafety();
        example3_IteratorBehavior();
        example4_ConcurrentIteration();
        example5_PerformanceComparison();
    }

    static void example1_BasicOperations() {
        System.out.println("=== Example 1: Basic COW ArrayList Operations ===");
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Java");
        list.add("Python");
        list.add("C++");
        System.out.println("List: " + list);
        list.set(1, "JavaScript");
        System.out.println("After set: " + list);
        list.remove(0);
        System.out.println("After remove: " + list);
    }

    static void example2_ThreadSafety() throws InterruptedException {
        System.out.println("\n=== Example 2: Thread Safety ===");
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                list.add(i);
                System.out.println("Added: " + i);
                try { Thread.sleep(10); } catch (InterruptedException e) { break; }
            }
        });
        Thread reader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Read: " + list);
                try { Thread.sleep(10); } catch (InterruptedException e) { break; }
            }
        });
        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Final list: " + list);
    }

    static void example3_IteratorBehavior() {
        System.out.println("\n=== Example 3: Iterator Snapshot ===");
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C"));
        Iterator<String> iterator = list.iterator();
        list.add("D");
        System.out.print("Iterator sees: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        System.out.println("Actual list: " + list);
    }

    static void example4_ConcurrentIteration() throws InterruptedException {
        System.out.println("\n=== Example 4: Concurrent Iteration ===");
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(Arrays.asList("X", "Y", "Z"));
        Thread modifier = new Thread(() -> {
            try {
                Thread.sleep(50);
                list.add("W");
                System.out.println("Added W during iteration");
            } catch (InterruptedException e) { break; }
        });
        modifier.start();
        for (String s : list) {
            System.out.print(s + " ");
            try { Thread.sleep(20); } catch (InterruptedException e) { break; }
        }
        System.out.println();
        modifier.join();
    }

    static void example5_PerformanceComparison() {
        System.out.println("\n=== Example 5: Performance Characteristics ===");
        CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        int size = 10000;
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) cowList.add(i);
        long cowTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) arrayList.add(i);
        long arrTime = System.nanoTime() - start;

        System.out.println("COW add " + size + ": " + cowTime + " ns");
        System.out.println("ArrayList add " + size + ": " + arrTime + " ns");
        System.out.println("COW is slower for writes but better for concurrent reads.");
    }
}
