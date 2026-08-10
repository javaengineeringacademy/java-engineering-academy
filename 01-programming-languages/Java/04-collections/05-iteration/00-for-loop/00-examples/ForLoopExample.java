package academy.javaengineering.collections.iteration.forloop;

import java.util.*;

public class ForLoopExample {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Python", "C++");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ": " + list.get(i));
        }
        System.out.println("\nReverse:");
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.println(i + ": " + list.get(i));
        }
    }
}
