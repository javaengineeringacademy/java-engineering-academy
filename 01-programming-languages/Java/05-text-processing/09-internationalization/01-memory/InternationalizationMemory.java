package academy.javaengineering.text.memory;

import java.text.*;
import java.util.*;

public class InternationalizationMemory {

    public static void main(String[] args) {
        System.out.println("=== Internationalization Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Locale Object Size
        System.out.println("--- Locale Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Locale us = Locale.US;
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Locale: " + (after - before) + " bytes");
        System.out.println("Cached instances");

        // 2. Format Objects
        System.out.println("\n--- Format Objects ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("NumberFormat: " + (after - before) + " bytes");
        System.out.println("Heavy objects - reuse when possible");

        // 3. Resource Bundles
        System.out.println("\n--- Resource Bundles ---");
        System.out.println("ResourceBundle: cached per Locale");
        System.out.println("Load once, use many times");
        System.out.println("Memory: depends on content size");
    }
}
