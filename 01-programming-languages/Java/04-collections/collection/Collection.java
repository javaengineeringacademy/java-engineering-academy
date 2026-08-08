package academy.javaengineering.collections.collection;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection - Root interface (excluding Iterable) for all collections.
 * 
 * Defines the contract for storing and manipulating groups of objects.
 * Provides core operations: add, remove, contains, size, iterator.
 * 
 * Time Complexity:
 *   add(E)           - O(1) to O(n) depending on implementation
 *   remove(Object)   - O(1) to O(n) depending on implementation
 *   contains(Object) - O(1) to O(n) depending on implementation
 *   size()           - O(1)
 *   isEmpty()        - O(1)
 *   iterator()       - O(1)
 * 
 * Relationship:
 *   Iterable<E> ← Collection<E>
 *   Collection<E> ← List<E>, Set<E>, Queue<E>, Deque<E>
 * 
 * Key Implementations:
 *   ArrayList, LinkedList, HashSet, TreeSet, PriorityQueue, ArrayDeque
 * 
 * Thread Safety: NOT thread-safe by default.
 * Use Collections.synchronizedCollection() for basic thread safety.
 * 
 * @see List
 * @see Set
 * @see Queue
 * @see Collections#synchronizedCollection(Collection)
 */
public class CollectionDemo {
    
    /**
     * Demonstrates Collection interface operations.
     */
    public static void main(String[] args) {
        // Create a Collection implementation
        Collection<String> fruits = new ArrayList<>();
        
        // Adding elements
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Date");
        
        System.out.println("Collection: " + fruits);
        System.out.println("Size: " + fruits.size());
        System.out.println("Contains Apple: " + fruits.contains("Apple"));
        System.out.println("Is empty: " + fruits.isEmpty());
        
        // Removing elements
        boolean removed = fruits.remove("Banana");
        System.out.println("Removed Banana: " + removed);
        System.out.println("After removal: " + fruits);
        
        // Bulk operations
        Collection<String> moreFruits = List.of("Elderberry", "Fig");
        fruits.addAll(moreFruits);
        System.out.println("After addAll: " + fruits);
        
        // Check containsAll
        boolean containsAll = fruits.containsAll(List.of("Apple", "Cherry"));
        System.out.println("Contains all [Apple, Cherry]: " + containsAll);
        
        // Remove if
        fruits.removeIf(f -> f.startsWith("E"));
        System.out.println("After removeIf startsWith E: " + fruits);
        
        // Iterate using for-each (uses Iterable.iterator())
        System.out.println("Iterating:");
        for (String fruit : fruits) {
            System.out.println("  " + fruit);
        }
        
        // Convert to array
        Object[] array = fruits.toArray();
        System.out.println("Array length: " + array.length);
        
        // Clear collection
        fruits.clear();
        System.out.println("After clear: " + fruits);
        System.out.println("Is empty: " + fruits.isEmpty());
    }
}