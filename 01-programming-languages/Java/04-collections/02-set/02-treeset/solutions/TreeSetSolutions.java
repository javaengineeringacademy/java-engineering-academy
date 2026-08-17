package set.treeset.solutions;

import java.util.*;

public class TreeSetSolutions {

    public static Integer findClosest(TreeSet<Integer> set, int target) {
        Integer floor = set.floor(target);
        Integer ceiling = set.ceiling(target);
        if (floor == null) return ceiling;
        if (ceiling == null) return floor;
        return (target - floor <= ceiling - target) ? floor : ceiling;
    }

    public static TreeSet<Integer> range(TreeSet<Integer> set, int min, int max) {
        return new TreeSet<>(set.subSet(min, true, max, true));
    }

    public static TreeSet<String> sortByLength(String[] strings) {
        TreeSet<String> sorted = new TreeSet<>(Comparator.comparingInt(String::length)
            .thenComparing(Comparator.naturalOrder()));
        Collections.addAll(sorted, strings);
        return sorted;
    }

    public static Integer findMedian(TreeSet<Integer> set) {
        List<Integer> list = new ArrayList<>(set);
        int mid = list.size() / 2;
        return list.get(mid);
    }

    public static List<Integer> kSmallest(TreeSet<Integer> set, int k) {
        return new ArrayList<>(set).subList(0, Math.min(k, set.size()));
    }

    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(10, 20, 30, 40, 50));
        System.out.println("Closest to 25: " + findClosest(set, 25));
        System.out.println("Range [20,40]: " + range(set, 20, 40));
        System.out.println("Sort by length: " + sortByLength(new String[]{"Banana", "Fig", "Apple", "Cherry"}));
        System.out.println("Median: " + findMedian(set));
        System.out.println("3 smallest: " + kSmallest(set, 3));
    }
}
