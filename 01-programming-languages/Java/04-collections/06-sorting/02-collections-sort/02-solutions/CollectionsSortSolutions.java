package academy.javaengineering.collections.sorting.collectionssort.solutions;

import java.util.*;

public class CollectionsSortSolutions {
    public static List<String> sortReverseAlphabetical(List<String> strings) {
        List<String> sorted = new ArrayList<>(strings);
        Collections.sort(sorted, Comparator.reverseOrder());
        return sorted;
    }
    
    public static List<Integer> sortByAbsoluteValue(List<Integer> numbers) {
        List<Integer> sorted = new ArrayList<>(numbers);
        Collections.sort(sorted, Comparator.comparingInt(n -> Math.abs(n)));
        return sorted;
    }
    
    public static <T> List<T> sortWithCustomComparator(List<T> list, Comparator<T> comparator) {
        List<T> sorted = new ArrayList<>(list);
        Collections.sort(sorted, comparator);
        return sorted;
    }
    
    public static List<String> sortByLengthThenAlphabetical(List<String> strings) {
        List<String> sorted = new ArrayList<>(strings);
        Collections.sort(sorted, Comparator.comparingInt(String::length)
                .thenComparing(Comparator.naturalOrder()));
        return sorted;
    }
    
    public static List<Integer> sortDescending(List<Integer> numbers) {
        List<Integer> sorted = new ArrayList<>(numbers);
        Collections.sort(sorted, Comparator.reverseOrder());
        return sorted;
    }
}
