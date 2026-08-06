package academy.javaengineering.strings;

public class StringBuilderDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateInsertAndDelete();
        demonstrateReverseAndReplace();
        demonstrateCapacity();
        demonstratePerformance();
        demonstrateChaining();
    }

    private static void demonstrateBasicOperations() {
        System.out.println("=== Basic Operations ===");

        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("Basic: " + sb.toString());

        StringBuilder sb2 = new StringBuilder("Initial");
        System.out.println("With initial value: " + sb2);

        StringBuilder sb3 = new StringBuilder(50);
        System.out.println("With capacity 50: " + sb3);
        System.out.println("Initial capacity: " + sb3.capacity());
    }

    private static void demonstrateInsertAndDelete() {
        System.out.println("\n=== Insert and Delete ===");

        StringBuilder sb = new StringBuilder("Hello World");
        System.out.println("Original: " + sb);

        sb.insert(5, ",");
        System.out.println("After insert(5, ','): " + sb);

        sb.insert(6, " Beautiful");
        System.out.println("After insert(6, ' Beautiful'): " + sb);

        sb.delete(5, 15);
        System.out.println("After delete(5, 15): " + sb);

        sb.deleteCharAt(0);
        System.out.println("After deleteCharAt(0): " + sb);
    }

    private static void demonstrateReverseAndReplace() {
        System.out.println("\n=== Reverse and Replace ===");

        StringBuilder sb = new StringBuilder("Hello");
        System.out.println("Original: " + sb);
        System.out.println("Reversed: " + sb.reverse());

        StringBuilder sb2 = new StringBuilder("Hello World");
        System.out.println("\nOriginal: " + sb2);
        sb2.replace(6, 11, "Java");
        System.out.println("After replace(6, 11, 'Java'): " + sb2);

        StringBuilder sb3 = new StringBuilder("Hello World");
        System.out.println("\nOriginal: " + sb3);
        System.out.println("Index of 'World': " + sb3.indexOf("World"));
        System.out.println("Index of 'Java': " + sb3.indexOf("Java"));
    }

    private static void demonstrateCapacity() {
        System.out.println("\n=== Capacity Management ===");

        StringBuilder sb = new StringBuilder();
        System.out.println("Initial capacity: " + sb.capacity());
        System.out.println("Initial length: " + sb.length());

        for (int i = 0; i < 20; i++) {
            sb.append("a");
            if (sb.length() > sb.capacity() - 10) {
                System.out.println("Length: " + sb.length() + ", Capacity: " + sb.capacity());
            }
        }

        StringBuilder sb2 = new StringBuilder(100);
        System.out.println("\nPre-allocated capacity: " + sb2.capacity());
        System.out.append("Pre-allocated length: ").println(sb2.length());
    }

    private static void demonstratePerformance() {
        System.out.println("\n=== Performance Comparison ===");

        int iterations = 100000;

        long start = System.currentTimeMillis();
        String concat = "";
        for (int i = 0; i < iterations; i++) {
            concat += "a";
        }
        long concatTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        String builderResult = sb.toString();
        long builderTime = System.currentTimeMillis() - start;

        System.out.println("Iterations: " + iterations);
        System.out.println("String concat: " + concatTime + "ms");
        System.out.println("StringBuilder: " + builderTime + "ms");
        System.out.println("Speedup: " + (concatTime / Math.max(builderTime, 1)) + "x");
    }

    private static void demonstrateChaining() {
        System.out.println("\n=== Method Chaining ===");

        String result = new StringBuilder()
                .append("Java")
                .append(" ")
                .append("Programming")
                .append(" ")
                .append("Language")
                .toString();
        System.out.println("Chained: " + result);

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ")
          .append("John")
          .append(", Age: ")
          .append(30)
          .append(", Email: ")
          .append("john@example.com");
        System.out.println("Builder result: " + sb.toString());
    }
}
