import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImmutableCollection {
    public static void main(String[] args) {
        // Example 1: List.of() - Java 9+
        List<String> immutableList = List.of("A", "B", "C");
        System.out.println("Immutable list: " + immutableList);

        try {
            immutableList.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot add to List.of(): " + e.getClass().getSimpleName());
        }

        // Example 2: Map.of() - Java 9+
        var immutableMap = java.util.Map.of("key1", 1, "key2", 2);
        System.out.println("\nImmutable map: " + immutableMap);

        try {
            immutableMap.put("key3", 3);
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot add to Map.of(): " + e.getClass().getSimpleName());
        }

        // Example 3: Collections.unmodifiableList()
        List<String> mutableList = new ArrayList<>(List.of("X", "Y", "Z"));
        List<String> unmodifiableView = Collections.unmodifiableList(mutableList);
        System.out.println("\nUnmodifiable view: " + unmodifiableView);

        // WARNING: The underlying mutable list can still be modified
        mutableList.add("W");
        System.out.println("After modifying original: " + unmodifiableView);

        try {
            unmodifiableView.add("V");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot add via unmodifiable view: " + e.getClass().getSimpleName());
        }

        // Example 4: List.copyOf() - Java 10+ (true immutable copy)
        List<String> source = new ArrayList<>(List.of("1", "2", "3"));
        List<String> trueCopy = List.copyOf(source);
        System.out.println("\nList.copyOf(): " + trueCopy);

        source.add("4");
        System.out.println("Source after modification: " + source);
        System.out.println("Copy unchanged: " + trueCopy);
    }
}
