package academy.javaengineering.collections.list.internals;

import java.util.*;
import java.util.concurrent.*;

public class ListInternals {

    public static void main(String[] args) {
        System.out.println("=== List Interface Internals ===\n");

        // 1. ArrayList internal array
        System.out.println("--- ArrayList Internal Array ---");
        ArrayList<String> arrayList = new ArrayList<>();
        System.out.println("Initial capacity: 10");
        System.out.println("Growth factor: 1.5x (oldCapacity + oldCapacity >> 1)");
        for (int i = 0; i < 15; i++) {
            arrayList.add("item" + i);
            System.out.print(arrayList.size() + " ");
        }
        System.out.println("\nFinal size: " + arrayList.size());

        // 2. LinkedList Node structure
        System.out.println("\n--- LinkedList Node Structure ---");
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");
        System.out.println("Node contains: item, next, prev");
        System.out.println("First -> Second -> Third");

        // 3. CopyOnWriteArrayList behavior
        System.out.println("\n--- CopyOnWriteArrayList ---");
        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
        cowList.add("Original");
        Iterator<String> it = cowList.iterator();
        cowList.add("Modified");
        System.out.println("Iterator sees: " + it.next());
        System.out.println("List contains: " + cowList);

        // 4. Vector vs ArrayList synchronization
        System.out.println("\n--- Vector Synchronization ---");
        Vector<String> vector = new Vector<>();
        vector.add("Thread-safe");
        System.out.println("Vector methods are synchronized");
        System.out.println("ArrayList is not synchronized (faster)");

        // 5. Stack LIFO behavior
        System.out.println("\n--- Stack LIFO ---");
        Stack<String> stack = new Stack<>();
        stack.push("Bottom");
        stack.push("Middle");
        stack.push("Top");
        System.out.println("Pop: " + stack.pop());
        System.out.println("Peek: " + stack.peek());
        System.out.println("Size: " + stack.size());

        // 6. subList() view behavior
        System.out.println("\n--- subList() View ---");
        List<String> original = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        List<String> sub = original.subList(1, 3);
        System.out.println("Original: " + original);
        System.out.println("SubList: " + sub);
        sub.set(0, "X");
        System.out.println("After modifying sub: " + original);
    }
}
