package academy.javaengineering.text.memory;

public class StringMemory {

    public static void main(String[] args) {
        System.out.println("=== String Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. String Object Size
        System.out.println("--- String Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        String s1 = "Hello";
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("String literal: " + (after - before) + " bytes");
        System.out.println("Stored in string pool");

        // 2. New String Object
        System.out.println("\n--- New String Object ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        String s2 = new String("Hello");
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("new String(): " + (after - before) + " bytes");
        System.out.println("Creates separate object");

        // 3. String Concatenation
        System.out.println("\n--- String Concatenation ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        String result = "";
        for (int i = 0; i < 1000; i++) result += "a";
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Concatenation: " + (after - before) + " bytes");
        System.out.println("Creates new String each time");

        // 4. StringBuilder Alternative
        System.out.println("\n--- StringBuilder ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) sb.append("a");
        String result2 = sb.toString();
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("StringBuilder: " + (after - before) + " bytes");
        System.out.println("Much more efficient");
    }
}
