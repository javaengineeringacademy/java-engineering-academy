package academy.javaengineering.reflection.fieldaccess.practices;

import java.lang.reflect.Field;
import java.util.*;

public class Exercise3_DeepFieldComparison {

    public static List<String> findDifferences(Object a, Object b) throws Exception {
        List<String> diffs = new ArrayList<>();
        if (a.getClass() != b.getClass()) {
            diffs.add("Different classes");
            return diffs;
        }
        for (Field field : a.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object valA = field.get(a);
            Object valB = field.get(b);
            if (!Objects.equals(valA, valB)) {
                diffs.add(field.getName());
            }
        }
        return diffs;
    }

    static class Person {
        private String name;
        private int age;
        Person(String name, int age) { this.name = name; this.age = age; }
    }

    public static void main(String[] args) throws Exception {
        Person a = new Person("Alice", 30);
        Person b = new Person("Bob", 30);
        Person c = new Person("Alice", 30);
        System.out.println("a vs b: " + findDifferences(a, b));
        System.out.println("a vs c: " + findDifferences(a, c));
    }
}
