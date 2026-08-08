package list.copyonwritearraylist.solutions;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class COWSolutions {

    public static class ThreadSafeCounter {
        private final CopyOnWriteArrayList<Long> timestamps = new CopyOnWriteArrayList<>();

        public void increment() {
            timestamps.add(System.nanoTime());
        }

        public int getCount() {
            return timestamps.size();
        }
    }

    public static <T> CopyOnWriteArrayList<T> safeFilter(CopyOnWriteArrayList<T> list, Predicate<T> predicate) {
        CopyOnWriteArrayList<T> result = new CopyOnWriteArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T> void atomicReplace(CopyOnWriteArrayList<T> list, T oldValue, T newValue) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(oldValue)) {
                list.set(i, newValue);
            }
        }
    }

    public static <T> List<T> takeSnapshot(CopyOnWriteArrayList<T> list) {
        return new ArrayList<>(list);
    }

    public static <T> boolean addIfAbsent(CopyOnWriteArrayList<T> list, T element) {
        if (!list.contains(element)) {
            list.addIfAbsent(element);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        ThreadSafeCounter counter = new ThreadSafeCounter();
        counter.increment();
        counter.increment();
        counter.increment();
        System.out.println("Counter: " + counter.getCount());

        CopyOnWriteArrayList<Integer> nums = new CopyOnWriteArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("Filter > 3: " + safeFilter(nums, n -> n > 3));

        CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C", "B"));
        atomicReplace(strings, "B", "X");
        System.out.println("Atomic replace B->X: " + strings);

        System.out.println("Snapshot: " + takeSnapshot(nums));

        CopyOnWriteArrayList<String> unique = new CopyOnWriteArrayList<>(Arrays.asList("A", "B"));
        System.out.println("Add absent C: " + addIfAbsent(unique, "C"));
        System.out.println("Add absent B: " + addIfAbsent(unique, "B"));
        System.out.println("Final: " + unique);
    }
}
