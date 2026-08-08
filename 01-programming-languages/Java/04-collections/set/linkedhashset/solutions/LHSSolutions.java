package set.linkedhashset.solutions;

import java.util.*;

public class LHSSolutions {

    public static <T> LinkedHashSet<T> removeDuplicatesPreserveOrder(List<T> list) {
        return new LinkedHashSet<>(list);
    }

    public static <T> Map<String, T> firstAndLast(LinkedHashSet<T> set) {
        Map<String, T> result = new HashMap<>();
        Iterator<T> it = set.iterator();
        if (it.hasNext()) result.put("first", it.next());
        T last = null;
        while (it.hasNext()) last = it.next();
        if (last != null) result.put("last", last);
        return result;
    }

    public static LinkedHashSet<Character> charsInOrder(String str) {
        LinkedHashSet<Character> chars = new LinkedHashSet<>();
        for (char c : str.toCharArray()) {
            chars.add(c);
        }
        return chars;
    }

    public static <T> LinkedHashSet<T> differencePreserveOrder(LinkedHashSet<T> set1, LinkedHashSet<T> set2) {
        LinkedHashSet<T> result = new LinkedHashSet<>();
        for (T item : set1) {
            if (!set2.contains(item)) result.add(item);
        }
        return result;
    }

    public static <T> LinkedHashSet<T> rotate(LinkedHashSet<T> set, int k) {
        List<T> list = new ArrayList<>(set);
        k = k % list.size();
        if (k < 0) k += list.size();
        List<T> rotated = new ArrayList<>(list.subList(k, list.size()));
        rotated.addAll(list.subList(0, k));
        return new LinkedHashSet<>(rotated);
    }

    public static void main(String[] args) {
        System.out.println("Remove dupes: " + removeDuplicatesPreserveOrder(Arrays.asList("A", "B", "A", "C")));
        System.out.println("First/Last: " + firstAndLast(new LinkedHashSet<>(Arrays.asList("X", "Y", "Z"))));
        System.out.println("Chars in order: " + charsInOrder("programming"));

        LinkedHashSet<Integer> s1 = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4));
        LinkedHashSet<Integer> s2 = new LinkedHashSet<>(Arrays.asList(2, 3, 5));
        System.out.println("Difference: " + differencePreserveOrder(s1, s2));

        LinkedHashSet<String> rotateSet = new LinkedHashSet<>(Arrays.asList("A", "B", "C", "D", "E"));
        System.out.println("Rotate 2: " + rotate(rotateSet, 2));
    }
}
