package academy.javaengineering.generics.internals;

public class GenericMethodsInternals {

    static <T> T firstElement(T[] array) {
        return array.length > 0 ? array[0] : null;
    }

    static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static <T, R> R convert(T input, java.util.function.Function<T, R> mapper) {
        return mapper.apply(input);
    }

    public static void main(String[] args) {
        System.out.println("=== Generic Methods Internals ===\n");

        // 1. Type Inference in Methods
        System.out.println("--- Type Inference ---");
        String first = firstElement(new String[]{"Java", "Python"});
        System.out.println("firstElement(String[]) infers T=String");
        System.out.println("Result: " + first);

        // 2. Bounded Type Parameters
        System.out.println("\n--- Bounded Types ---");
        Integer maxInt = max(10, 20);
        String maxStr = max("Apple", "Banana");
        System.out.println("max(10,20) = " + maxInt);
        System.out.println("max(Apple,Banana) = " + maxStr);

        // 3. Method-Level Generics vs Class-Level
        System.out.println("\n--- Method vs Class ---");
        System.out.println("Class-level: T declared in class declaration");
        System.out.println("Method-level: T declared in method signature");
        System.out.println("Method-level: independent per call");

        // 4. Generic Method with Multiple Params
        System.out.println("\n--- Multiple Type Params ---");
        Integer length = convert("Hello", String::length);
        System.out.println("convert(String, Function<String,Integer>)");
        System.out.println("Result: " + length);

        // 5. Varargs Generics
        System.out.println("\n--- Varargs ---");
        System.out.println("Safe: T... args creates T[] internally");
        System.out.println("Heap pollution risk with varargs + generics");
    }
}
