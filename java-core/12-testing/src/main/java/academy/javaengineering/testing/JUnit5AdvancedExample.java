package academy.javaengineering.testing;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 5 Advanced - Parameterized Tests, Nested Tests, Extensions.
 */
public class JUnit5AdvancedExample {

    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, Stranger!";
        }
        return "Hello, " + name.trim() + "!";
    }

    public int[] sort(int[] array) {
        if (array == null || array.length <= 1) return array;
        int[] sorted = array.clone();
        for (int i = 0; i < sorted.length - 1; i++) {
            for (int j = 0; j < sorted.length - i - 1; j++) {
                if (sorted[j] > sorted[j + 1]) {
                    int temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        return sorted;
    }

    public String repeat(String text, int times) {
        if (text == null || times < 0) return "";
        return text.repeat(times);
    }

    public static void main(String[] args) {
        JUnit5AdvancedExample example = new JUnit5AdvancedExample();
        System.out.println("Greet: " + example.greet("Java"));
        System.out.println("Sort: " + java.util.Arrays.toString(example.sort(new int[]{3, 1, 4, 1, 5})));
        System.out.println("Repeat: " + example.repeat("Ha", 3));
    }
}
