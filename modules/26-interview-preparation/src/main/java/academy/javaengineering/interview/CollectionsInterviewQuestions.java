package academy.javaengineering.interview;

import java.util.*;

/**
 * Collections Interview Questions - Collections framework interview prep.
 */
public class CollectionsInterviewQuestions {

    public static classLRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LRU(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<>(list2);
        return list1.stream().filter(set::contains).toList();
    }

    public <T> Map<T, Long> frequencyMap(List<T> list) {
        return list.stream().collect(java.util.stream.Collectors.groupingBy(e -> e, java.util.stream.Collectors.counting()));
    }

    public boolean isAnagram(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        char[] a1 = s1.toCharArray();
        char[] a2 = s2.toCharArray();
        java.util.Arrays.sort(a1);
        java.util.Arrays.sort(a2);
        return java.util.Arrays.equals(a1, a2);
    }

    public static void main(String[] args) {
        CollectionsInterviewQuestions q = new CollectionsInterviewQuestions();
        System.out.println("Anagram: " + q.isAnagram("listen", "silent"));
        System.out.println("Frequency: " + q.frequencyMap(List.of("a", "b", "a", "c")));
    }
}
