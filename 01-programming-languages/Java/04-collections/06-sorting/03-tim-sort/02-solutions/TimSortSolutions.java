package academy.javaengineering.collections.sorting.timsort.solutions;

import java.util.*;

public class TimSortSolutions {
    public static List<Integer> demonstrateAdaptiveSorting(List<Integer> list) {
        // TimSort is adaptive - performs better on partially sorted data
        List<Integer> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        return sorted;
    }
    
    public static List<Integer> handlePartialSorting(List<Integer> list) {
        // TimSort detects existing sorted runs and optimizes
        List<Integer> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        return sorted;
    }
    
    public static Map<String, Long> compareSortingAlgorithms(int[] array) {
        Map<String, Long> results = new HashMap<>();
        
        // TimSort (via Collections.sort)
        List<Integer> list1 = new ArrayList<>();
        for (int num : array) list1.add(num);
        long start = System.nanoTime();
        Collections.sort(list1);
        results.put("TimSort", System.nanoTime() - start);
        
        // Arrays.sort (Dual-Pivot Quicksort for primitives)
        int[] arr2 = array.clone();
        start = System.nanoTime();
        Arrays.sort(arr2);
        results.put("Arrays.sort", System.nanoTime() - start);
        
        return results;
    }
    
    public static List<Student> demonstrateStability(List<Student> students) {
        // TimSort is stable - maintains relative order of equal elements
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort(Comparator.comparingInt(s -> s.grade));
        return sorted;
    }
    
    public static long measureMemoryUsage(List<Integer> list) {
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.totalMemory() - runtime.freeMemory();
        
        List<Integer> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        
        long after = runtime.totalMemory() - runtime.freeMemory();
        return after - before;
    }
    
    static class Student {
        String name;
        int grade;
        Student(String name, int grade) {
            this.name = name;
            this.grade = grade;
        }
    }
}
