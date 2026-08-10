package academy.javaengineering.collections.sorting.comparable.solutions;

import java.util.*;

public class ComparableSolutions {
    public static List<Student> sortByGPA(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((a, b) -> Double.compare(b.gpa, a.gpa));
        return sorted;
    }
    
    public static <T extends Comparable<T>> T findMin(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        T min = list.get(0);
        for (T item : list) {
            if (item.compareTo(min) < 0) min = item;
        }
        return min;
    }
    
    public static <T extends Comparable<T>> boolean isSorted(List<T> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).compareTo(list.get(i)) > 0) return false;
        }
        return true;
    }
    
    public static void sortPersons(Person[] persons) {
        Arrays.sort(persons, (a, b) -> {
            int ageCompare = Integer.compare(a.age, b.age);
            if (ageCompare != 0) return ageCompare;
            return a.name.compareTo(b.name);
        });
    }
    
    public static <T extends Comparable<T>> T kthSmallest(List<T> list, int k) {
        List<T> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        return sorted.get(k - 1);
    }
    
    static class Student {
        String name;
        double gpa;
        Student(String name, double gpa) { this.name = name; this.gpa = gpa; }
    }
    
    static class Person {
        String name;
        int age;
        Person(String name, int age) { this.name = name; this.age = age; }
    }
}
