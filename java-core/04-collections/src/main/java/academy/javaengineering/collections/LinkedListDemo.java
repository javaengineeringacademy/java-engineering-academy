package academy.javaengineering.collections;

import java.util.LinkedList;
import java.util.Deque;
import java.util.ListIterator;

/**
 * Demonstrates LinkedList operations as both List and Deque.
 * LinkedList provides O(1) insertions at both ends but O(n) random access.
 */
public class LinkedListDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateDequeOperations();
        demonstrateBidirectionalTraversal();
        demonstrateAdvancedPatterns();
    }

    /**
     * Demonstrates basic LinkedList operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== LinkedList Basic Operations ===");

        // Creation
        LinkedList<String> names = new LinkedList<>();
        LinkedList<String> fromList = new LinkedList<>(List.of("A", "B", "C"));

        // Adding elements
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        names.addFirst("Diana");
        names.addLast("Eve");

        System.out.println("List: " + names);
        System.out.println("Size: " + names.size());
        System.out.println("First: " + names.getFirst());
        System.out.println("Last: " + names.getLast());

        // Removing elements
        names.removeFirst();
        names.removeLast();
        System.out.println("After removals: " + names);

        // Accessing (O(n) - must traverse)
        System.out.println("Element at index 1: " + names.get(1));
        System.out.println();
    }

    /**
     * Demonstrates Deque operations (stack and queue).
     */
    private static void demonstrateDequeOperations() {
        System.out.println("=== Deque Operations ===");

        // As Stack (LIFO)
        Deque<String> stack = new LinkedList<>();
        stack.push("First");
        stack.push("Second");
        stack.push("Third");

        System.out.println("Stack operations:");
        while (!stack.isEmpty()) {
            System.out.println("  Pop: " + stack.pop());
        }

        // As Queue (FIFO)
        Deque<String> queue = new LinkedList<>();
        queue.offer("Customer 1");
        queue.offer("Customer 2");
        queue.offer("Customer 3");

        System.out.println("\nQueue operations:");
        while (!queue.isEmpty()) {
            System.out.println("  Serve: " + queue.poll());
        }

        // Peek operations
        Deque<Integer> deque = new LinkedList<>();
        deque.offer(10);
        deque.offer(20);
        deque.offer(30);

        System.out.println("\nPeek operations:");
        System.out.println("  peekFirst: " + deque.peekFirst());
        System.out.println("  peekLast: " + deque.peekLast());
        System.out.println("  After peek: " + deque);
        System.out.println();
    }

    /**
     * Demonstrates bidirectional traversal.
     */
    private static void demonstrateBidirectionalTraversal() {
        System.out.println("=== Bidirectional Traversal ===");

        LinkedList<Integer> numbers = new LinkedList<>();
        for (int i = 1; i <= 5; i++) {
            numbers.add(i);
        }

        // Forward traversal
        System.out.print("Forward: ");
        ListIterator<Integer> forward = numbers.listIterator();
        while (forward.hasNext()) {
            System.out.print(forward.next() + " ");
        }
        System.out.println();

        // Backward traversal
        System.out.print("Backward: ");
        ListIterator<Integer> backward = numbers.listIterator(numbers.size());
        while (backward.hasPrevious()) {
            System.out.print(backward.previous() + " ");
        }
        System.out.println();

        // Replace elements while iterating
        ListIterator<Integer> replaceIt = numbers.listIterator();
        while (replaceIt.hasNext()) {
            int num = replaceIt.next();
            replaceIt.set(num * 10);
        }
        System.out.println("Doubled: " + numbers);

        // Add elements while iterating
        ListIterator<Integer> addIt = numbers.listIterator();
        while (addIt.hasNext()) {
            int num = addIt.next();
            if (num == 30) {
                addIt.add(25); // Add 25 before 30
            }
        }
        System.out.println("After adding: " + numbers);
        System.out.println();
    }

    /**
     * Demonstrates advanced LinkedList patterns.
     */
    private static void demonstrateAdvancedPatterns() {
        System.out.println("=== Advanced Patterns ===");

        // Pattern 1: LRU Cache
        System.out.println("LRU Cache:");
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("A", 1);
        cache.put("B", 2);
        cache.put("C", 3);
        cache.get("A"); // Moves A to end
        cache.put("D", 4); // Evicts B
        System.out.println("  Cache: " + cache);

        // Pattern 2: Undo/Redo System
        System.out.println("\nUndo/Redo:");
        UndoRedoSystem<String> undoRedo = new UndoRedoSystem<>();
        undoRedo.execute("Action 1");
        undoRedo.execute("Action 2");
        undoRedo.execute("Action 3");
        System.out.println("  Undo: " + undoRedo.undo());
        System.out.println("  Undo: " + undoRedo.undo());
        System.out.println("  Redo: " + undoRedo.redo());

        // Pattern 3: Reverse linked list
        System.out.println("\nReverse:");
        LinkedList<String> list = new LinkedList<>(List.of("A", "B", "C", "D"));
        System.out.println("  Before: " + list);
        reverseLinkedList(list);
        System.out.println("  After: " + list);
    }

    /**
     * Reverses a linked list in place.
     */
    private static <T> void reverseLinkedList(LinkedList<T> list) {
        ListIterator<T> forward = list.listIterator();
        ListIterator<T> backward = list.listIterator(list.size());

        while (forward.nextIndex() < backward.previousIndex()) {
            T forwardElem = forward.next();
            T backwardElem = backward.previous();
            forward.set(backwardElem);
            backward.set(forwardElem);
        }
    }

    /**
     * LRU Cache using LinkedList.
     */
    static class LRUCache<K, V> {
        private final int capacity;
        private final java.util.Map<K, V> cache;
        private final LinkedList<K> accessOrder;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new java.util.HashMap<>();
            this.accessOrder = new LinkedList<>();
        }

        public V get(K key) {
            if (!cache.containsKey(key)) return null;
            accessOrder.remove(key);
            accessOrder.addLast(key);
            return cache.get(key);
        }

        public void put(K key, V value) {
            if (cache.containsKey(key)) {
                accessOrder.remove(key);
            } else if (cache.size() >= capacity) {
                K eldest = accessOrder.removeFirst();
                cache.remove(eldest);
            }
            cache.put(key, value);
            accessOrder.addLast(key);
        }

        @Override
        public String toString() {
            return cache.toString();
        }
    }

    /**
     * Undo/Redo System using LinkedList.
     */
    static class UndoRedoSystem<T> {
        private final LinkedList<T> history = new LinkedList<>();
        private final LinkedList<T> redoStack = new LinkedList<>();

        public void execute(T action) {
            history.addLast(action);
            redoStack.clear();
        }

        public T undo() {
            if (history.isEmpty()) return null;
            T action = history.removeLast();
            redoStack.addLast(action);
            return action;
        }

        public T redo() {
            if (redoStack.isEmpty()) return null;
            T action = redoStack.removeLast();
            history.addLast(action);
            return action;
        }
    }
}
