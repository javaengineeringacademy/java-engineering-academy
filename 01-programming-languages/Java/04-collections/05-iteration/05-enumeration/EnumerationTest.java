package academy.javaengineering.collections.iteration.enumeration;

import java.util.*;

public class EnumerationTest {
    public static void main(String[] args) {
        Vector<Integer> v = new Vector<>(Arrays.asList(1, 2, 3));
        int sum = 0;
        Enumeration<Integer> en = v.elements();
        while (en.hasMoreElements()) sum += en.nextElement();
        assert sum == 6 : "Sum should be 6";
        System.out.println("EnumerationTest passed");
    }
}
