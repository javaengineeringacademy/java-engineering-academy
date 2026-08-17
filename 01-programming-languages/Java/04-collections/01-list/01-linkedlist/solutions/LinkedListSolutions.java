package list.linkedlist.solutions;

import java.util.*;

public class LinkedListSolutions {

    public static <T> void reverse(LinkedList<T> list) {
        LinkedList<T> reversed = new LinkedList<>();
        for (T item : list) {
            reversed.addFirst(item);
        }
        list.clear();
        list.addAll(reversed);
    }

    public static <T> T findMiddle(LinkedList<T> list) {
        if (list.isEmpty()) return null;
        ListIterator<T> slow = list.listIterator();
        ListIterator<T> fast = list.listIterator();
        while (fast.hasNext() && fast.next() != null && fast.hasNext() && fast.next() != null) {
            slow.next();
        }
        return slow.next();
    }

    public static boolean hasCycle(LinkedList<Integer> list) {
        Set<Integer> seen = new HashSet<>();
        for (Integer node : list) {
            if (!seen.add(node)) {
                return true;
            }
        }
        return false;
    }

    public static <T> LinkedList<T> mergeAlternate(LinkedList<T> list1, LinkedList<T> list2) {
        LinkedList<T> merged = new LinkedList<>();
        Iterator<T> it1 = list1.iterator();
        Iterator<T> it2 = list2.iterator();
        while (it1.hasNext() || it2.hasNext()) {
            if (it1.hasNext()) merged.add(it1.next());
            if (it2.hasNext()) merged.add(it2.next());
        }
        return merged;
    }

    public static <T> void removeAllOccurrences(LinkedList<T> list, T value) {
        list.removeFirstOccurrence(value);
        while (list.contains(value)) {
            list.removeFirstOccurrence(value);
        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("Original: " + list);
        reverse(list);
        System.out.println("Reversed: " + list);

        LinkedList<String> mid = new LinkedList<>(Arrays.asList("A", "B", "C", "D", "E"));
        System.out.println("Middle: " + findMiddle(mid));

        System.out.println("Merge alternate: " + mergeAlternate(
            new LinkedList<>(Arrays.asList(1, 3, 5)),
            new LinkedList<>(Arrays.asList(2, 4, 6))
        ));

        LinkedList<Integer> removeList = new LinkedList<>(Arrays.asList(1, 2, 3, 2, 4, 2, 5));
        removeAllOccurrences(removeList, 2);
        System.out.println("After remove all 2: " + removeList);
    }
}
