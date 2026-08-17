package list.arraylist.solutions;

import java.util.*;

public class ArrayListSolutions {

    public static List<int[]> findPairsWithSum(ArrayList<Integer> list, int target) {
        List<int[]> pairs = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        for (int num : list) {
            int complement = target - num;
            if (seen.contains(complement)) {
                pairs.add(new int[]{complement, num});
            }
            seen.add(num);
        }
        return pairs;
    }

    public static List<String> compress(ArrayList<String> list) {
        if (list.isEmpty()) return new ArrayList<>();
        List<String> compressed = new ArrayList<>();
        String current = list.get(0);
        int count = 1;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).equals(current)) {
                count++;
            } else {
                compressed.add(count > 1 ? current + "(" + count + ")" : current);
                current = list.get(i);
                count = 1;
            }
        }
        compressed.add(count > 1 ? current + "(" + count + ")" : current);
        return compressed;
    }

    public static <T> ArrayList<T> interweave(ArrayList<T> list1, ArrayList<T> list2) {
        ArrayList<T> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            result.add(list1.get(i++));
            result.add(list2.get(j++));
        }
        while (i < list1.size()) result.add(list1.get(i++));
        while (j < list2.size()) result.add(list2.get(j++));
        return result;
    }

    public static <T> ArrayList<T> keepOnlyUnique(ArrayList<T> list) {
        Map<T, Integer> countMap = new LinkedHashMap<>();
        for (T item : list) {
            countMap.merge(item, 1, Integer::sum);
        }
        ArrayList<T> result = new ArrayList<>();
        for (Map.Entry<T, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == 1) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public static <T> void cyclicShift(ArrayList<T> list, int k) {
        if (list.isEmpty()) return;
        k = k % list.size();
        if (k < 0) k += list.size();
        List<T> shifted = new ArrayList<>(list.subList(list.size() - k, list.size()));
        shifted.addAll(list.subList(0, list.size() - k));
        list.clear();
        list.addAll(shifted);
    }

    public static void main(String[] args) {
        System.out.println("Pairs with sum 8: " + findPairsWithSum(new ArrayList<>(Arrays.asList(1, 3, 5, 7, 2, 6)), 8));
        System.out.println("Compress: " + compress(new ArrayList<>(Arrays.asList("a", "a", "b", "c", "c", "c"))));
        System.out.println("Interweave: " + interweave(new ArrayList<>(Arrays.asList(1, 3, 5)), new ArrayList<>(Arrays.asList(2, 4, 6))));
        System.out.println("Keep unique: " + keepOnlyUnique(new ArrayList<>(Arrays.asList(1, 2, 2, 3, 3, 4))));
        ArrayList<String> shift = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        cyclicShift(shift, 2);
        System.out.println("Cyclic shift 2: " + shift);
    }
}
