package academy.javaengineering.generics.best-practices.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * Best Practices Demo - Working examples of generic best practices.
 */
public class BestPracticesDemo {

    public static void main(String[] args) {
        System.out.println("=== Best Practices Demo ===\n");

        // 1. Use bounded types properly
        System.out.println("1. Bounded Types:");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        System.out.println("  Sum: " + sum(numbers));

        // 2. Prefer List over array
        System.out.println("\n2. List vs Array:");
        List<String> safeList = new ArrayList<>();
        safeList.add("Hello");
        safeList.add("World");
        System.out.println("  Safe list: " + safeList);

        // 3. Use @SafeVarargs
        System.out.println("\n3. Safe Varargs:");
        List<String> result = makeList("A", "B", "C");
        System.out.println("  Created list: " + result);

        // 4. Document type parameters
        System.out.println("\n4. Documentation:");
        System.out.println("  See DocumentedCache class for example");

        // 5. Type witnesses when needed
        System.out.println("\n5. Type Witnesses:");
        Object obj = BestPracticesDemo.<String>identity("Hello");
        System.out.println("  Explicit type: " + obj);
    }

    public static <T extends Number> double sum(List<T> list) {
        double total = 0;
        for (T num : list) {
            total += num.doubleValue();
        }
        return total;
    }

    @SafeVarargs
    public static <T> List<T> makeList(T... items) {
        List<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    public static <T> T identity(T value) {
        return value;
    }
}
