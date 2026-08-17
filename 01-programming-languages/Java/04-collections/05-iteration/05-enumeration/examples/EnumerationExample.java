package academy.javaengineering.collections.iteration.enumeration;

import java.util.*;

public class EnumerationExample {
    public static void main(String[] args) {
        Vector<String> v = new Vector<>(Arrays.asList("A", "B", "C"));
        Enumeration<String> en = v.elements();
        while (en.hasMoreElements()) System.out.println(en.nextElement());
    }
}
