package academy.javaengineering.collections.examples;

import java.util.*;
import java.util.concurrent.*;

public class ListExamples {
    public static void main(String[] args) {
        System.out.println("=== List Examples ===\n");

        // ArrayList basics
        System.out.println("--- ArrayList ---");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("Python");
        arrayList.add("C++");
        arrayList.add(1, "JavaScript");
        System.out.println("List: " + arrayList);
        System.out.println("Get by index: " + arrayList.get(0));
        System.out.println("Contains Java: " + arrayList.contains("Java"));
        System.out.println("Index of Python: " + arrayList.indexOf("Python"));
        System.out.println("Size: " + arrayList.size());

        // ArrayList with initial capacity
        ArrayList<Integer> sized = new ArrayList<>(100);
        for (int i = 0; i < 10; i++) sized.add(i * 10);
        System.out.println("\nSized list: " + sized);

        // LinkedList
        System.out.println("\n--- LinkedList ---");
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.addFirst("First");
        linkedList.addLast("Last");
        linkedList.add("Middle");
        System.out.println("List: " + linkedList);
        System.out.println("First: " + linkedList.getFirst());
        System.out.println("Last: " + linkedList.getLast());

        // CopyOnWriteArrayList
        System.out.println("\n--- CopyOnWriteArrayList ---");
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
        cowList.add("Thread");
        cowList.add("Safe");
        cowList.add("List");
        Iterator<String> it = cowList.iterator();
        cowList.add("New Element"); // Safe during iteration
        System.out.println("COW List: " + cowList);

        // SubList
        System.out.println("\n--- SubList ---");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> subList = numbers.subList(2, 7);
        System.out.println("Original: " + numbers);
        System.out.println("SubList(2,7): " + subList);
    }
}
