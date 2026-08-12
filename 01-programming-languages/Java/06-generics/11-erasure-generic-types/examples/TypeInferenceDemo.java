package typeinference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Type Inference Demo - Complete Guide
 * 
 * Covers diamond operator, target type inference, method argument inference,
 * and how Java 7-10+ improved type inference.
 */
public class TypeInferenceDemo {

    // ==========================================
    // SECTION 1: Diamond Operator (Java 7+)
    // ==========================================
    static class DiamondOperator {

        // Before diamond operator (Java 6)
        static void preDiamond() {
            System.out.println("=== Pre-Diamond (Java 6) ===\n");
            // Had to specify type on both sides
            List<String> oldStyle = new ArrayList<String>();
            oldStyle.add("hello");
            System.out.println("  List<String>: " + oldStyle);
        }

        // With diamond operator (Java 7+)
        static void withDiamond() {
            System.out.println("\n=== Diamond Operator (Java 7+) ===\n");
            // Type inferred from right side
            List<String> strings = new ArrayList<>();
            strings.add("hello");
            System.out.println("  List<String>: " + strings);

            // Works with complex types
            java.util.Map<String, List<Integer>> map = new java.util.HashMap<>();
            map.put("numbers", new ArrayList<>());
            System.out.println("  Map<String, List<Integer>>: " + map);
        }

        // Diamond with anonymous classes
        static void diamondWithAnonymous() {
            System.out.println("\n=== Diamond with Anonymous Classes ===\n");

            // Java 7: Cannot use diamond with anonymous classes
            // List<String> list = new ArrayList<>() {}; // COMPILE ERROR in Java 7

            // Java 9+: Can use diamond with anonymous classes
            List<String> list = new ArrayList<>() {
                @Override
                public boolean add(String s) {
                    System.out.println("    Adding: " + s);
                    return super.add(s);
                }
            };
            list.add("test");

            // Factory method with diamond
            Supplier<List<String>> supplier = ArrayList::new;
            List<String> newList = supplier.get();
            System.out.println("  Created via supplier: " + newList);
        }

        // Diamond operator limitations
        static void diamondLimitations() {
            System.out.println("\n=== Diamond Operator Limitations ===\n");

            // Works well
            List<Integer> ints = new ArrayList<>();
            java.util.Map<String, Integer> map = new java.util.HashMap<>();

            // Edge case: ambiguous types
            // List<> list = new ArrayList<>(); // COMPILE ERROR - cannot infer

            // Workaround with explicit type
            List<Object> objectList = new ArrayList<>();
            System.out.println("  Explicit type needed for ambiguous cases");
        }

        static void demonstrateAll() {
            preDiamond();
            withDiamond();
            diamondWithAnonymous();
            diamondLimitations();
        }
    }

    // ==========================================
    // SECTION 2: Target Type Inference (Java 8+)
    // ==========================================
    static class TargetTypeInference {

        // Lambda target type inference
        static void lambdaTargetType() {
            System.out.println("=== Lambda Target Type Inference ===\n");

            // Target type from variable
            java.util.function.Function<String, Integer> lengthFunc = s -> s.length();
            System.out.println("  Function from lambda: " + lengthFunc.apply("hello"));

            // Target type from method parameter
            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
            names.sort((a, b) -> a.compareTo(b)); // Target type inferred from context
            System.out.println("  Sorted: " + names);

            // Target type in method call
            processFunction(s -> s.toUpperCase(), "hello");
        }

        static <T, R> void processFunction(java.util.function.Function<T, R> func, T input) {
            System.out.println("  Function result: " + func.apply(input));
        }

        // Inferred type with generics
        static <T> List<T> createList(T... items) {
            List<T> list = new ArrayList<>();
            for (T item : items) {
                list.add(item);
            }
            return list;
        }

        static void targetInferenceWithGenerics() {
            System.out.println("\n=== Target Inference with Generics ===\n");

            // Type inferred from target
            List<String> strings = createList("a", "b", "c");
            System.out.println("  Inferred List<String>: " + strings);

            List<Integer> integers = createList(1, 2, 3);
            System.out.println("  Inferred List<Integer>: " + integers);

            // Multiple levels of inference
            List<List<String>> nested = createList(
                    createList("a", "b"),
                    createList("c", "d")
            );
            System.out.println("  Nested inference: " + nested);
        }

        // Method chaining inference
        static void chainingInference() {
            System.out.println("\n=== Chaining Inference ===\n");

            List<String> result = createList("hello", "world", "java")
                    .stream()
                    .map(String::toUpperCase)
                    .map(s -> s + "!")
                    .collect(java.util.stream.Collectors.toList());
            System.out.println("  Chained: " + result);
        }

        static void demonstrateAll() {
            lambdaTargetType();
            targetInferenceWithGenerics();
            chainingInference();
        }
    }

    // ==========================================
    // SECTION 3: Method Argument Inference (Java 8+)
    // ==========================================
    static class MethodArgumentInference {

        // Type inferred from method arguments
        static <T> T identity(T t) { return t; }

        static <T extends Comparable<T>> T max(T a, T b) {
            return a.compareTo(b) >= 0 ? a : b;
        }

        static <T> List<T> listOf(T... items) {
            return Arrays.asList(items);
        }

        static void demonstrateMethodArgumentInference() {
            System.out.println("=== Method Argument Inference ===\n");

            // Type inferred from argument
            String s = identity("hello");
            Integer i = identity(42);
            System.out.println("  identity(\"hello\") -> " + s.getClass().getSimpleName());
            System.out.println("  identity(42) -> " + i.getClass().getSimpleName());

            // max inference
            Integer maxInt = max(3, 5);
            String maxStr = max("apple", "banana");
            System.out.println("  max(3, 5) -> " + maxInt);
            System.out.println("  max(\"apple\", \"banana\") -> " + maxStr);

            // listOf inference
            List<String> strings = listOf("a", "b", "c");
            List<Integer> numbers = listOf(1, 2, 3);
            System.out.println("  listOf strings: " + strings);
            System.out.println("  listOf numbers: " + numbers);
        }

        // Generic method with complex inference
        static <K, V> java.util.Map<K, V> mapOf(K key, V value) {
            java.util.Map<K, V> map = new java.util.HashMap<>();
            map.put(key, value);
            return map;
        }

        static <T> T first(List<T> list) {
            return list.isEmpty() ? null : list.get(0);
        }

        static void complexInference() {
            System.out.println("\n=== Complex Inference ===\n");

            // Multiple type parameters inferred
            java.util.Map<String, Integer> map = mapOf("age", 30);
            System.out.println("  mapOf(\"age\", 30): " + map);

            java.util.Map<Integer, List<String>> complex = mapOf(1, Arrays.asList("a", "b"));
            System.out.println("  mapOf(1, List): " + complex);

            // Inference with nested generics
            List<Integer> nums = listOf(10, 20, 30);
            Integer first = first(nums);
            System.out.println("  first(List<Integer>): " + first);
        }

        static void demonstrateAll() {
            demonstrateMethodArgumentInference();
            complexInference();
        }
    }

    // ==========================================
    // SECTION 4: Java 8+ Type Inference Enhancements
    // ==========================================
    static class Java8Enhancements {

        // Lambda parameter type inference
        static void lambdaParameterInference() {
            System.out.println("=== Java 8: Lambda Parameter Inference ===\n");

            List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

            // Explicit types (Java 7 style)
            names.stream()
                    .filter((String s) -> s.length() > 3)
                    .forEach(System.out::println);

            // Inferred types (Java 8+)
            names.stream()
                    .filter(s -> s.length() > 3)
                    .forEach(System.out::println);

            // Multiple parameters
            java.util.Map<String, Integer> map = new java.util.HashMap<>();
            map.forEach((key, value) -> System.out.println("  " + key + "=" + value));
        }

        // Method reference inference
        static void methodReferenceInference() {
            System.out.println("\n=== Method Reference Inference ===\n");

            List<String> words = Arrays.asList("hello", "world", "java");

            // Constructor reference
            java.util.stream.Stream<List<String>> stream =
                    words.stream()
                            .map(ArrayList::new);

            // Array constructor reference
            java.util.stream.Stream<int[]> arrays =
                    words.stream()
                            .map(String::length)
                            .map(int[]::new);

            // Bound method reference
            String::toUpperCase.getClass(); // Type inferred
            System.out.println("  Method references: type inferred from context");
        }

        // Effectively final variables in lambdas (Java 8+)
        static void effectivelyFinal() {
            System.out.println("\n=== Effectively Final Variables ===\n");

            String prefix = "Hello, "; // effectively final (not reassigned)
            List<String> names = Arrays.asList("Alice", "Bob");

            // Can use effectively final variables in lambdas
            names.forEach(name -> System.out.println("  " + prefix + name));

            // This would NOT work:
            // int counter = 0;
            // names.forEach(name -> counter++); // COMPILE ERROR - not effectively final
        }

        static void demonstrateAll() {
            lambdaParameterInference();
            methodReferenceInference();
            effectivelyFinal();
        }
    }

    // ==========================================
    // SECTION 5: Java 9+ Type Inference Enhancements
    // ==========================================
    static class Java9Enhancements {

        // Diamond operator with anonymous classes (Java 9)
        static void diamondWithAnonymous() {
            System.out.println("=== Java 9: Diamond with Anonymous Classes ===\n");

            // Java 9 allows diamond with anonymous classes
            List<String> list = new ArrayList<>() {
                @Override
                public String toString() {
                    return "CustomList" + super.toString();
                }
            };
            list.add("test");
            System.out.println("  " + list);
        }

        // Effectively local variables (var) (Java 10+)
        static void localVariableInference() {
            System.out.println("\n=== Java 10: var Keyword ===\n");

            // var infers type from initializer
            var list = new ArrayList<String>();
            list.add("hello");
            System.out.println("  var list -> " + list.getClass().getSimpleName());

            var map = new java.util.HashMap<String, Integer>();
            map.put("key", 42);
            System.out.println("  var map -> " + map.getClass().getSimpleName());

            // Can't use var without initializer
            // var x; // COMPILE ERROR

            // Can't use var for null
            // var y = null; // COMPILE ERROR

            // var works with complex types
            var result = list.stream()
                    .map(String::toUpperCase)
                    .collect(java.util.stream.Collectors.toList());
            System.out.println("  var stream result -> " + result.getClass().getSimpleName());
        }

        // Java 10 var limitations
        static void varLimitations() {
            System.out.println("\n=== var Limitations ===\n");

            var list = new ArrayList<Integer>();

            // These don't work with var:
            // var x = null; // ERROR
            // var y; // ERROR
            // var z = {1, 2, 3}; // ERROR
            // var a = (x) -> x + 1; // ERROR (ambiguous lambda)

            System.out.println("  var cannot be used for:");
            System.out.println("    - null assignments");
            System.out.println("    - Uninitialized variables");
            System.out.println("    - Array initializers");
            System.out.println("    - Ambiguous lambdas");
            System.out.println("    - Method return types");
            System.out.println("    - Fields/parameters");
        }

        static void demonstrateAll() {
            diamondWithAnonymous();
            localVariableInference();
            varLimitations();
        }
    }

    // ==========================================
    // SECTION 6: Practical Examples
    // ==========================================
    static class PracticalExamples {

        // Type inference in builders
        static class Builder<T> {
            private T value;

            public Builder<T> with(T value) {
                this.value = value;
                return this;
            }

            public T build() {
                return value;
            }
        }

        // Generic factory with inference
        static <T> Builder<T> builder() {
            return new Builder<>();
        }

        // Stream operations with inference
        static void streamInference() {
            System.out.println("\n=== Stream Type Inference ===\n");

            List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

            // Complex stream chain - all types inferred
            List<String> result = names.stream()
                    .filter(name -> name.length() > 3)
                    .map(String::toUpperCase)
                    .sorted()
                    .collect(java.util.stream.Collectors.toList());

            System.out.println("  Filtered, mapped, sorted: " + result);

            // Reducing with inference
            String joined = names.stream()
                    .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
            System.out.println("  Reduced: " + joined);
        }

        // Type inference with generics and lambdas
        static <T extends Comparable<T>> List<T> filterAndSort(
                List<T> list,
                java.util.function.Predicate<T> predicate) {

            List<T> result = new ArrayList<>();
            for (T item : list) {
                if (predicate.test(item)) {
                    result.add(item);
                }
            }
            result.sort(Comparator.naturalOrder());
            return result;
        }

        static void genericWithLambdas() {
            System.out.println("\n=== Generics + Lambdas Inference ===\n");

            List<Integer> numbers = Arrays.asList(5, 3, 8, 1, 9, 2, 7);

            // Types inferred from context
            List<Integer> filtered = filterAndSort(numbers, n -> n > 4);
            System.out.println("  Filtered (>4) and sorted: " + filtered);

            List<String> words = Arrays.asList("banana", "apple", "cherry", "date");
            List<String> filteredWords = filterAndSort(words, w -> w.length() > 5);
            System.out.println("  Words >5 chars sorted: " + filteredWords);
        }

        static void demonstrateAll() {
            streamInference();
            genericWithLambdas();
        }
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║    TYPE INFERENCE DEMO                  ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        DiamondOperator.demonstrateAll();
        TargetTypeInference.demonstrateAll();
        MethodArgumentInference.demonstrateAll();
        Java8Enhancements.demonstrateAll();
        Java9Enhancements.demonstrateAll();
        PracticalExamples.demonstrateAll();

        System.out.println("\nAll type inference demos complete!");
    }
}
