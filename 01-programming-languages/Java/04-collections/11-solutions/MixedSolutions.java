package academy.javaengineering.collections.solutions;

import java.util.*;
import java.util.stream.*;

public class MixedSolutions {

    // Exercise 31: Most frequent word
    public static String mostFrequentWord(String text) {
        Map<String, Long> freq = Arrays.stream(text.toLowerCase().split("\\s+"))
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        return Collections.max(freq.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // Exercise 32: Group by age range
    public static Map<String, List<String>> groupByAge(List<String> names, List<Integer> ages) {
        Map<String, List<String>> result = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            String range = ages.get(i) < 18 ? "minor" : ages.get(i) < 60 ? "adult" : "senior";
            result.computeIfAbsent(range, k -> new ArrayList<>()).add(names.get(i));
        }
        return result;
    }

    // Exercise 33: Common unique elements
    public static List<Integer> commonUnique(List<Integer> list1, List<Integer> list2) {
        Set<Integer> set1 = new LinkedHashSet<>(list1);
        Set<Integer> set2 = new HashSet<>(list2);
        return set1.stream().filter(set2::contains).collect(Collectors.toList());
    }

    // Exercise 34: Search phone book
    public static List<String> searchPhoneBook(Map<String, String> phoneBook, String query) {
        return phoneBook.entrySet().stream()
            .filter(e -> e.getKey().toLowerCase().contains(query.toLowerCase()))
            .map(e -> e.getKey() + ": " + e.getValue())
            .collect(Collectors.toList());
    }

    // Exercise 35: Flatten nested lists
    public static <T> List<T> flatten(List<List<T>> nested) {
        return nested.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    // Exercise 36: Top N frequent
    public static List<String> topNFrequent(List<String> words, int n) {
        return words.stream()
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(n)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    // Exercise 37: Sliding window
    public static List<List<Integer>> slidingWindow(List<Integer> list, int k) {
        List<List<Integer>> windows = new ArrayList<>();
        for (int i = 0; i <= list.size() - k; i++) {
            windows.add(list.subList(i, i + k));
        }
        return windows;
    }

    // Exercise 38: LRU Cache (implementation in MixedExercises.java)

    // Exercise 39: Merge intervals
    public static List<int[]> mergeIntervals(List<int[]> intervals) {
        if (intervals.isEmpty()) return intervals;
        intervals.sort(Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        int[] current = intervals.get(0);
        for (int i = 1; i < intervals.size(); i++) {
            if (intervals.get(i)[0] <= current[1]) {
                current[1] = Math.max(current[1], intervals.get(i)[1]);
            } else {
                merged.add(current);
                current = intervals.get(i);
            }
        }
        merged.add(current);
        return merged;
    }

    // Exercise 40: Custom joining collector
    public static Collector<String, ?, String> customJoining(String delimiter) {
        return Collector.of(
            StringBuilder::new,
            (sb, s) -> {
                if (sb.length() > 0) sb.append(delimiter);
                sb.append(s);
            },
            (sb1, sb2) -> {
                if (sb2.length() > 0) {
                    if (sb1.length() > 0) sb1.append(delimiter);
                    sb1.append(sb2);
                }
                return sb1;
            },
            StringBuilder::toString
        );
    }
}