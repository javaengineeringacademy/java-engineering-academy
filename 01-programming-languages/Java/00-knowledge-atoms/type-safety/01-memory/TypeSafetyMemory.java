package academy.javaengineering.knowledgeatoms.typesafety;

import java.util.*;

public class TypeSafetyMemory {

    public static void main(String[] args) {
        System.out.println("=== Type Safety Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Type erasure memory
        System.out.println("--- Type Erasure Memory ---");
        System.out.println("Generic type information is erased at runtime:");
        System.out.println("  List<String> -> List (raw type)");
        System.out.println("  Map<String, Integer> -> Map (raw type)");
        System.out.println("No memory cost for generic type parameters");
        System.out.println("Type checks happen at compile time only");

        // 2. Boxing overhead in generics
        System.out.println("\n--- Boxing Overhead in Generics ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            list.add(i);  // autoboxing: creates Integer object
        }

        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("List<Integer> (100K elements): ~" + (after - before) / 1024 + " KB");
        System.out.println("Each Integer: ~16 bytes (object header + int value)");
        System.out.println("Plus ArrayList reference: ~8 bytes per element");
        System.out.println("Total per element: ~24 bytes");

        // 3. Raw type vs parameterized type
        System.out.println("\n--- Raw Type vs Parameterized Type ---");
        System.out.println("Raw type: List list = new ArrayList();");
        System.out.println("  - No compile-time type checking");
        System.out.println("  - Requires explicit casts at runtime");
        System.out.println("  - Same memory layout as parameterized type");
        System.out.println("");
        System.out.println("Parameterized: List<String> list = new ArrayList<>();");
        System.out.println("  - Compile-time type checking");
        System.out.println("  - Implicit casts inserted by compiler");
        System.out.println("  - Same memory layout (type erased)");

        // 4. instanceof memory
        System.out.println("\n--- instanceof Memory ---");
        System.out.println("instanceof checks class hierarchy at runtime:");
        System.out.println("  - Walks superclass chain");
        System.out.println("  - Checks interface implementations");
        System.out.println("  - Cost: ~1-5 nanoseconds per check");
        System.out.println("  - JIT may optimize to single comparison");

        // 5. Bridge methods
        System.out.println("\n--- Bridge Methods ---");
        System.out.println("Compiler generates bridge methods for type safety:");
        System.out.println("  class StringList extends ArrayList<String> {");
        System.out.println("    // Compiler generates:");
        System.out.println("    // public boolean add(Object o) {");
        System.out.println("    //     return add((String) o); // bridge method");
        System.out.println("    // }");
        System.out.println("  }");
        System.out.println("Bridge methods add ~20 bytes per method to class metadata");
    }
}
