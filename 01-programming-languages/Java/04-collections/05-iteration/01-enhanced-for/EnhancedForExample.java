package academy.javaengineering.collections.iteration.enhanced;

import java.util.*;

public class EnhancedForExample {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Java", "Python", "C++");
        for (String lang : list) {
            System.out.println(lang);
        }
        Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));
        for (int n : set) {
            System.out.println(n);
        }
    }
}
