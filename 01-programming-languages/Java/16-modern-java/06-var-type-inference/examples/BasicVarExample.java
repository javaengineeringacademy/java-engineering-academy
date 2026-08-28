package academy.javaengineering.modern.vartype;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Basic var type inference examples.
 */
public class BasicVarExample {

    public static void main(String[] args) {
        // Basic types
        var x = 10;           // int
        var name = "Hello";   // String
        var pi = 3.14;        // double
        var active = true;    // boolean

        System.out.println("Basic types:");
        System.out.println("x: " + x + " (" + ((Object) x).getClass().getSimpleName() + ")");
        System.out.println("name: " + name + " (" + ((Object) name).getClass().getSimpleName() + ")");
        System.out.println("pi: " + pi + " (" + ((Object) pi).getClass().getSimpleName() + ")");
        System.out.println("active: " + active + " (" + ((Object) active).getClass().getSimpleName() + ")");

        // Collections
        var list = List.of(1, 2, 3, 4, 5);
        var map = Map.of("a", 1, "b", 2, "c", 3);

        System.out.println("\nCollections:");
        System.out.println("list: " + list);
        System.out.println("map: " + map);

        // Streams
        var stream = list.stream().filter(i -> i > 2);
        var filteredList = stream.collect(Collectors.toList());
        System.out.println("\nFiltered list: " + filteredList);

        // For-each loop
        System.out.println("\nFor-each with var:");
        for (var entry : map.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        // Complex generic types
        var complexMap = new java.util.HashMap<String, List<Integer>>();
        complexMap.put("numbers", List.of(1, 2, 3));
        System.out.println("\nComplex map: " + complexMap);

        // Method calls
        var result = computeValue();
        System.out.println("\nComputed value: " + result);

        // Arrays
        var array = new int[]{1, 2, 3, 4, 5};
        System.out.println("\nArray: " + java.util.Arrays.toString(array));
    }

    static int computeValue() {
        return 42;
    }
}
