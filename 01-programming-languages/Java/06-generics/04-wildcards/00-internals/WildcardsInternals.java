package academy.javaengineering.generics.internals;

import java.util.*;

public class WildcardsInternals {

    static double sumOfList(List<? extends Number> list) {
        double sum = 0;
        for (Number num : list) sum += num.doubleValue();
        return sum;
    }

    static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 5; i++) list.add(i);
    }

    public static void main(String[] args) {
        System.out.println("=== Wildcards Internals ===\n");

        // 1. Upper Bounded (? extends T)
        System.out.println("--- Upper Bounded ? extends T ---");
        List<Integer> ints = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        System.out.println("sumOfList(ints) = " + sumOfList(ints));
        System.out.println("sumOfList(doubles) = " + sumOfList(doubles));
        System.out.println("Read-only: can get T, cannot set T");

        // 2. Lower Bounded (? super T)
        System.out.println("\n--- Lower Bounded ? super T ---");
        List<Integer> intList = new ArrayList<>();
        List<Number> numList = new ArrayList<>();
        addNumbers(intList);
        addNumbers(numList);
        System.out.println("Can set Integer, cannot get specific type");

        // 3. PECS Rule
        System.out.println("\n--- PECS Rule ---");
        System.out.println("Producer Extends: ? extends T (read)");
        System.out.println("Consumer Super: ? super T (write)");
        System.out.println("Used in Collections.copy()");

        // 4. Unbounded Wildcard (?)
        System.out.println("\n--- Unbounded ? ---");
        List<?> anyList = Arrays.asList("A", "B", "C");
        System.out.println("? accepts any type");
        System.out.println("Can only read as Object, cannot write");

        // 5. Wildcard Capture
        System.out.println("\n--- Wildcard Capture ---");
        System.out.println("Compiler captures wildcard type");
        System.out.println("Allows safe operations within method");
    }
}
