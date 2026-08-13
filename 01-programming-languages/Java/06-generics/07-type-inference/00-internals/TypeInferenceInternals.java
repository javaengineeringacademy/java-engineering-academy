package academy.javaengineering.generics.internals;

import java.util.*;
import java.util.stream.*;

public class TypeInferenceInternals {

    static <T> List<T> ofList(T... items) {
        return Arrays.asList(items);
    }

    static <T extends Comparable<T>> Optional<T> max(List<T> list) {
        return list.stream().max(Comparator.naturalOrder());
    }

    public static void main(String[] args) {
        System.out.println("=== Type Inference Internals ===\n");

        // 1. Local Variable Type Inference (var)
        System.out.println("--- var (Java 10+) ---");
        var list = new ArrayList<String>();
        var map = Map.of("key", "value");
        System.out.println("var: compiler infers from initializer");
        System.out.println("Not available for fields, parameters");

        // 2. Diamond Operator
        System.out.println("\n--- Diamond <> ---");
        List<String> names = new ArrayList<>();
        Map<String, Integer> scores = new HashMap<>();
        System.out.println("Java 7+: new ArrayList<>()");
        System.out.println("Type inferred from declaration");

        // 3. Method Argument Inference
        System.out.println("\n--- Method Argument ---");
        List<String> result = ofList("A", "B", "C");
        System.out.println("ofList(\"A\",\"B\",\"C\") infers T=String");

        // 4. Target Type
        System.out.println("\n--- Target Type ---");
        Optional<Integer> maxVal = max(Arrays.asList(1, 2, 3));
        System.out.println("Target type: Optional<Integer>");
        System.out.println("Infers T=Integer from context");

        // 5. Inference Limits
        System.out.println("\n--- Inference Limits ---");
        System.out.println("Cannot infer from assignment alone");
        System.out.println("var x = null; // ERROR");
        System.out.println("Must have initializer");
    }
}
