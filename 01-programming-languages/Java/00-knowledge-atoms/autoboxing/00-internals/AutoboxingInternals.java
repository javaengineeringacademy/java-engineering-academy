package academy.javaengineering.knowledgeatoms.autoboxing;

public class AutoboxingInternals {

    public static void main(String[] args) {
        System.out.println("=== Autoboxing Internals ===\n");

        // 1. Autoboxing translates to valueOf()
        System.out.println("--- Autoboxing = valueOf() calls ---");
        Integer a = 42;           // compiler generates: Integer.valueOf(42)
        int b = a;                // compiler generates: a.intValue()
        System.out.println("Autoboxed Integer: " + a);
        System.out.println("Unboxed int: " + b);

        // 2. Integer Cache internals
        System.out.println("\n--- Integer Cache Behavior ---");
        Integer cacheLow1 = -128;
        Integer cacheLow2 = -128;
        Integer cacheHigh1 = 128;
        Integer cacheHigh2 = 128;

        System.out.println("Within cache (-128 to 127):");
        System.out.println("  -128 == -128  -> " + (cacheLow1 == cacheLow2) + "  (same object)");
        System.out.println("Outside cache:");
        System.out.println("  128 == 128    -> " + (cacheHigh1 == cacheHigh2) + "  (different objects)");
        System.out.println("  128 .equals(128) -> " + cacheHigh1.equals(cacheHigh2) + "  (value equality)");

        // 3. Cache configuration
        System.out.println("\n--- Cache Configuration ---");
        System.out.println("Default cache range: -128 to 127");
        System.out.println("Configure upper bound: -XX:AutoBoxCacheMax=<N>");
        System.out.println("Note: Lower bound is always -128 (hardcoded)");

        // 4. Autoboxing in arithmetic causes repeated conversions
        System.out.println("\n--- Autoboxing Overhead in Loops ---");
        long startWrapper = System.nanoTime();
        Long sumW = 0L;
        for (int i = 0; i < 1_000_000; i++) {
            sumW += i;  // unbox sumW, add, autobox result
        }
        long wrapperTime = System.nanoTime() - startWrapper;

        long startPrimitive = System.nanoTime();
        long sumP = 0L;
        for (int i = 0; i < 1_000_000; i++) {
            sumP += i;  // primitive add, no boxing
        }
        long primitiveTime = System.nanoTime() - startPrimitive;

        System.out.println("Wrapper loop:  " + wrapperTime + " ns");
        System.out.println("Primitive loop: " + primitiveTime + " ns");
        System.out.println("Ratio: " + String.format("%.1f", (double) wrapperTime / primitiveTime) + "x slower with wrappers");

        // 5. Bytecode perspective
        System.out.println("\n--- Bytecode Transformation ---");
        System.out.println("Source:   Integer x = 10;");
        System.out.println("Bytecode: invokestatic Integer.valueOf(int) -> Integer");
        System.out.println("");
        System.out.println("Source:   int y = x;");
        System.out.println("Bytecode: invokevirtual Integer.intValue() -> int");

        // 6. All wrapper caches
        System.out.println("\n--- All Wrapper Class Caches ---");
        System.out.println("Byte:     all values (-128 to 127)");
        System.out.println("Short:    -128 to 127");
        System.out.println("Integer:  -128 to 127 (configurable upper)");
        System.out.println("Long:     -128 to 127");
        System.out.println("Character: 0 to 127");
        System.out.println("Boolean:  TRUE and FALSE only");
        System.out.println("Float:    no cache");
        System.out.println("Double:   no cache");
    }
}
