package academy.javaengineering.text.memory;

import java.util.regex.*;

public class RegexMemory {

    public static void main(String[] args) {
        System.out.println("=== Regex Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Pattern Object Size
        System.out.println("--- Pattern Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Pattern pattern = Pattern.compile("\\d+");
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Pattern: " + (after - before) + " bytes");
        System.out.println("Compiled pattern: reusable");

        // 2. Matcher Memory
        System.out.println("\n--- Matcher Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Matcher matcher = pattern.matcher("Hello 123 World 456");
        long after2 = rt.totalMemory() - rt.freeMemory();
        System.out.println("Matcher: " + (after2 - before) + " bytes");
        System.out.println("Matcher: per-match instance");

        // 3. Pattern Compilation Cost
        System.out.println("\n--- Pattern Compilation ---");
        System.out.println("Compile once, reuse many times");
        System.out.println("Compilation: ~100-1000 cycles");
        System.out.println("Matching: ~10-100 cycles");
    }
}
