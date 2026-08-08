package academy.javaengineering.collections;

import java.util.*;
import java.util.stream.*;

public class Solutions {
    
    public static void exercise1() {
        List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve"));
        System.out.println(names);
    }
    
    public static void exercise2() {
        List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve"));
        names.removeIf(n -> n.startsWith("D"));
        System.out.println(names);
    }
    
    public static void exercise3() {
        Map<String, Integer> grades = new HashMap<>();
        grades.put("Alice", 95);
        grades.put("Bob", 87);
        grades.put("Charlie", 92);
        System.out.println(grades);
    }
    
    public static void exercise4() {
        Map<String, Integer> grades = new HashMap<>();
        grades.put("Alice", 95);
        grades.put("Bob", 87);
        grades.put("Charlie", 92);
        String topStudent = grades.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("None");
        System.out.println(topStudent + "=" + grades.get(topStudent));
    }
    
    public static void exercise5() {
        Set<String> sorted = new TreeSet<>(Comparator.comparingInt(String::length));
        sorted.addAll(Arrays.asList("I", "am", "Java", "is", "great"));
        System.out.println(sorted);
    }
    
    public static void exercise6() {
        List<Integer> numbers = Arrays.asList(1, 12, 3, 15, 20, 25, 7);
        Predicate<Integer> greaterThan10 = n -> n > 10;
        List<Integer> filtered = numbers.stream()
            .filter(greaterThan10)
            .collect(Collectors.toList());
        System.out.println(filtered);
    }
    
    public static void exercise7() {
        List<String> words = Arrays.asList("hello", "hi", "welcome", "ok");
        Function<String, Integer> toLength = String::length;
        List<Integer> lengths = words.stream()
            .map(toLength)
            .collect(Collectors.toList());
        System.out.println(lengths);
    }
    
    public static void exercise8() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        int product = numbers.stream()
            .reduce(1, (a, b) -> a * b);
        System.out.println(product);
    }
    
    public static void exercise9() {
        List<String> employees = Arrays.asList("Alice", "Bob", "Charlie", "David");
        Map<String, List<String>> byDept = employees.stream()
            .collect(Collectors.groupingBy(e -> 
                e.equals("Alice") || e.equals("Bob") ? "Engineering" : "HR"));
        System.out.println(byDept);
    }
    
    public static void exercise10() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        map.put("E", 5);
        map.forEach((k, v) -> System.out.println(k + "=" + v));
    }
    
    public static void main(String[] args) {
        exercise1(); exercise2(); exercise3(); exercise4();
        exercise5(); exercise6(); exercise7(); exercise8();
        exercise9(); exercise10();
    }
}