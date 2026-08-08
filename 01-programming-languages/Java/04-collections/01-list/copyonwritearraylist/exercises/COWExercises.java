package list.copyonwritearraylist.exercises;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class COWExercises {

    // TODO 1: Write a thread-safe counter that uses CopyOnWriteArrayList to store
    //         timestamps of increments.
    public static class ThreadSafeCounter {
        private final CopyOnWriteArrayList<Long> timestamps = new CopyOnWriteArrayList<>();

        public void increment() {
            // Your code here
        }

        public int getCount() {
            // Your code here
            return 0;
        }
    }

    // TODO 2: Write a method that safely filters elements from a CopyOnWriteArrayList
    //         based on a predicate while being iterated by another thread.
    public static <T> CopyOnWriteArrayList<T> safeFilter(
            CopyOnWriteArrayList<T> list, java.util.function.Predicate<T> predicate) {
        // Your code here
        return null;
    }

    // TODO 3: Write a method that atomically replaces all occurrences of one value
    //         with another in a CopyOnWriteArrayList.
    public static <T> void atomicReplace(CopyOnWriteArrayList<T> list, T oldValue, T newValue) {
        // Your code here
    }

    // TODO 4: Write a method that returns a snapshot of the list at a given moment.
    public static <T> List<T> takeSnapshot(CopyOnWriteArrayList<T> list) {
        // Your code here
        return null;
    }

    // TODO 5: Write a method that adds elements to a CopyOnWriteArrayList only if
    //         they don't already exist (thread-safe add-if-absent).
    public static <T> boolean addIfAbsent(CopyOnWriteArrayList<T> list, T element) {
        // Your code here
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Run the solutions to verify your answers.");
    }
}
