package set.hashset.solutions;

import java.util.*;
import java.util.function.Predicate;

public class HashSetSolutions {

    public static <T> HashSet<T> commonElements(HashSet<T> set1, HashSet<T> set2) {
        HashSet<T> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    public static HashSet<Character> uniqueChars(String str) {
        HashSet<Character> chars = new HashSet<>();
        for (char c : str.toCharArray()) {
            chars.add(c);
        }
        return chars;
    }

    public static HashSet<Integer> largestCommonSubset(HashSet<Integer> set1, HashSet<Integer> set2) {
        HashSet<Integer> result = new HashSet<>();
        for (Integer num : set1) {
            if (set2.contains(num)) result.add(num);
        }
        return result;
    }

    public static <T> Map<Boolean, HashSet<T>> partition(HashSet<T> set, Predicate<T> predicate) {
        Map<Boolean, HashSet<T>> result = new HashMap<>();
        result.put(true, new HashSet<>());
        result.put(false, new HashSet<>());
        for (T item : set) {
            result.get(predicate.test(item)).add(item);
        }
        return result;
    }

    public static HashSet<Integer> randomSet(int count, int min, int max) {
        Random random = new Random();
        HashSet<Integer> set = new HashSet<>();
        while (set.size() < count) {
            set.add(random.nextInt(max - min + 1) + min);
        }
        return set;
    }

    public static void main(String[] args) {
        HashSet<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        HashSet<Integer> s2 = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));
        System.out.println("Common: " + commonElements(s1, s2));
        System.out.println("Unique chars: " + uniqueChars("hello world"));
        System.out.println("Largest common: " + largestCommonSubset(s1, s2));
        System.out.println("Partition: " + partition(s1, n -> n % 2 == 0));
        System.out.println("Random set: " + randomSet(5, 1, 20));
    }
}
