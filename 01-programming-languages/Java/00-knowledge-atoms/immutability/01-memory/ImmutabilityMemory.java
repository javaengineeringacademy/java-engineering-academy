package academy.javaengineering.knowledgeatoms.immutability;

import java.util.*;

public class ImmutabilityMemory {

    public static void main(String[] args) {
        System.out.println("=== Immutability Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Defensive copy overhead
        System.out.println("--- Defensive Copy Overhead ---");
        System.out.println("Every defensive copy doubles the object count:");
        System.out.println("  Constructor: copy from parameter -> heap object");
        System.out.println("  Getter: copy from field -> heap object");
        System.out.println("For Date (8 bytes): copy adds ~16 bytes per access");

        // 2. String vs StringBuilder memory
        System.out.println("\n--- String vs StringBuilder Memory ---");
        rt.gc();
        long beforeStr = rt.totalMemory() - rt.freeMemory();
        String result = "";
        for (int i = 0; i < 1000; i++) {
            result += "a"; // creates new String each time
        }
        long afterStr = rt.totalMemory() - rt.freeMemory();

        rt.gc();
        long beforeSb = rt.totalMemory() - rt.freeMemory();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("a"); // mutates in place
        }
        String sbResult = sb.toString();
        long afterSb = rt.totalMemory() - rt.freeMemory();

        System.out.println("String concatenation: " + (afterStr - beforeStr) / 1024 + " KB");
        System.out.println("StringBuilder:        " + (afterSb - beforeSb) / 1024 + " KB");

        // 3. Immutable collection memory
        System.out.println("\n--- Immutable Collection Memory ---");
        List<Integer> mutableList = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            mutableList.add(i);
        }
        List<Integer> unmodifiableList = Collections.unmodifiableList(mutableList);
        List<Integer> copyList = List.copyOf(mutableList);

        System.out.println("ArrayList size: " + mutableList.size());
        System.out.println("Unmodifiable wrapper: same backing array (no extra memory)");
        System.out.println("List.copyOf(): creates new array (doubles memory)");

        // 4. Immutable object caching benefit
        System.out.println("\n--- Immutable Object Caching ---");
        System.out.println("Immutable objects are safe to cache:");
        System.out.println("  - State never changes, so cached reference is always valid");
        System.out.println("  - No need for defensive copies when reading");
        System.out.println("  - Can be shared across threads without synchronization");
        System.out.println("Example: Integer cache, String pool, Enum instances");

        // 5. Record memory layout
        System.out.println("\n--- Record Memory Layout ---");
        record Point(int x, int y) {}
        Point p = new Point(1, 2);
        System.out.println("Point record: 12 bytes header + 4 bytes x + 4 bytes y = 20 bytes");
        System.out.println("With alignment: 24 bytes");
        System.out.println("No getter/setter method overhead (generated at compile time)");
    }
}
