package academy.javaengineering.text.memory;

public class StringBuilderMemory {

    public static void main(String[] args) {
        System.out.println("=== StringBuilder Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. StringBuilder Capacity
        System.out.println("--- StringBuilder Capacity ---");
        StringBuilder sb = new StringBuilder();
        System.out.println("Initial capacity: " + sb.capacity());
        System.out.println("Default: 16 characters");
        System.out.println("Grows by: capacity * 2 + 2");

        // 2. StringBuilder vs String Concatenation
        System.out.println("\n--- StringBuilder vs Concat ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        StringBuilder sbResult = new StringBuilder();
        for (int i = 0; i < 1000; i++) sbResult.append("a");
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("StringBuilder: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        String strResult = "";
        for (int i = 0; i < 1000; i++) strResult += "a";
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("String concat: " + (after - before) + " bytes");

        // 3. Memory Efficiency
        System.out.println("\n--- Memory Efficiency ---");
        System.out.println("StringBuilder: reuses buffer");
        System.out.println("String: creates new object each time");
        System.out.println("StringBuilder: 10x more efficient");
    }
}
