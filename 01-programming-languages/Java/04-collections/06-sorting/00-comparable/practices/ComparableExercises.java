package academy.javaengineering.collections.sorting.comparable.exercises;

import java.util.*;

public class ComparableExercises {
    // TODO: Implement a method that sorts a list of Student objects by their GPA (descending)
    public static List<Student> sortByGPA(List<Student> students) { return null; }
    
    // TODO: Implement a method that finds the minimum element in a list of Comparable objects
    public static <T extends Comparable<T>> T findMin(List<T> list) { return null; }
    
    // TODO: Implement a method that checks if a list is sorted in ascending order
    public static <T extends Comparable<T>> boolean isSorted(List<T> list) { return false; }
    
    // TODO: Implement a method that sorts an array of Person objects by age, then by name
    public static void sortPersons(Person[] persons) { }
    
    // TODO: Implement a method that returns the kth smallest element from a list of Comparable objects
    public static <T extends Comparable<T>> T kthSmallest(List<T> list, int k) { return null; }
    
    // Helper classes for exercises
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
