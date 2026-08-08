package academy.javaengineering.collections.iteration.enhanced;

import java.util.*;

public class EnhancedForTest {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");
        StringBuilder sb = new StringBuilder();
        for (String s : list) sb.append(s);
        assert sb.toString().equals("abc") : "Should concatenate";
        System.out.println("EnhancedForTest passed");
    }
}
