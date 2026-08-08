package collection.solutions;

import java.util.*;

public class CollectionSolutions {

    public static Collection<Integer> removeGreaterThan(Collection<Integer> collection, int threshold) {
        Iterator<Integer> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() > threshold) {
                iterator.remove();
            }
        }
        return collection;
    }

    public static <T> Collection<T> intersection(Collection<T> col1, Collection<T> col2) {
        Collection<T> result = new ArrayList<>(col1);
        result.retainAll(col2);
        return result;
    }

    public static <T> Map<T, Integer> countOccurrences(Collection<T> collection) {
        Map<T, Integer> countMap = new HashMap<>();
        for (T element : collection) {
            countMap.merge(element, 1, Integer::sum);
        }
        return countMap;
    }

    public static <T> boolean isSubset(Collection<T> subset, Collection<T> superset) {
        return superset.containsAll(subset);
    }

    public static <T> Collection<T> mergeUnique(Collection<T> col1, Collection<T> col2) {
        Set<T> result = new LinkedHashSet<>(col1);
        result.addAll(col2);
        return result;
    }

    public static void main(String[] args) {
        Collection<Integer> nums = new ArrayList<>(Arrays.asList(1, 5, 3, 8, 2, 9));
        System.out.println("Before removeGreaterThan: " + nums);
        removeGreaterThan(nums, 5);
        System.out.println("After removeGreaterThan(5): " + nums);

        Collection<Integer> c1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Collection<Integer> c2 = new ArrayList<>(Arrays.asList(3, 4, 5, 6));
        System.out.println("Intersection: " + intersection(c1, c2));

        Collection<String> words = Arrays.asList("a", "b", "a", "c", "b", "a");
        System.out.println("Count occurrences: " + countOccurrences(words));

        Collection<Integer> sub = Arrays.asList(1, 2);
        Collection<Integer> sup = Arrays.asList(1, 2, 3, 4);
        System.out.println("Is subset: " + isSubset(sub, sup));

        System.out.println("Merge unique: " + mergeUnique(c1, c2));
    }
}
