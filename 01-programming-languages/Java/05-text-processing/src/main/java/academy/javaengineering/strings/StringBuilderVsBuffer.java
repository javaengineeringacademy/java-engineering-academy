package academy.javaengineering.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringBuilderVsBuffer {

    private static final int CONCAT_ITERATIONS = 100000;

    public static void main(String[] args) {
        demonstrateStringBuilderCreation();
        demonstrateStringBufferCreation();
        demonstrateStringBuilderOperations();
        demonstrateStringBufferOperations();
        demonstrateThreadSafety();
        demonstratePerformanceComparison();
        demonstrateMemoryUsage();
        demonstrateStringConcatVsBuilder();
        demonstrateLoopConcatenation();
        demonstrateMethodChaining();
        demonstrateSubstringOperations();
        demonstrateCapacityVsLength();
        demonstrateWhenToUseWhich();
    }

    private static void demonstrateStringBuilderCreation() {
        System.out.println("=== StringBuilder Creation ===");

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder("Initial Value");
        StringBuilder sb3 = new StringBuilder(100);
        StringBuilder sb4 = new StringBuilder("Hello", 0, 5);

        System.out.println("Default: " + sb1);
        System.out.println("With String: " + sb2);
        System.out.println("With Capacity: " + sb3);
        System.out.println("With Substring: " + sb4);
    }

    private static void demonstrateStringBufferCreation() {
        System.out.println("\n=== StringBuffer Creation ===");

        StringBuffer sbf1 = new StringBuffer();
        StringBuffer sbf2 = new StringBuffer("Initial Value");
        StringBuffer sbf3 = new StringBuffer(100);
        StringBuffer sbf4 = new StringBuffer("Hello", 0, 5);

        System.out.println("Default: " + sbf1);
        System.out.println("With String: " + sbf2);
        System.out.println("With Capacity: " + sbf3);
        System.out.println("With Substring: " + sbf4);
    }

    private static void demonstrateStringBuilderOperations() {
        System.out.println("\n=== StringBuilder Operations ===");

        StringBuilder sb = new StringBuilder("Hello");

        sb.append(" World");
        System.out.println("After append: " + sb);

        sb.insert(5, ",");
        System.out.println("After insert: " + sb);

        sb.delete(5, 6);
        System.out.println("After delete: " + sb);

        sb.replace(6, 11, "Java");
        System.out.println("After replace: " + sb);

        sb.reverse();
        System.out.println("After reverse: " + sb);

        sb.deleteCharAt(0);
        System.out.println("After deleteCharAt: " + sb);

        sb.append("ABC");
        System.out.println("Current: " + sb);
        System.out.println("Char at 2: " + sb.charAt(2));
        System.out.println("Index of 'A': " + sb.indexOf("A"));
        System.out.println("Last index of 'C': " + sb.lastIndexOf("C"));
    }

    private static void demonstrateStringBufferOperations() {
        System.out.println("\n=== StringBuffer Operations ===");

        StringBuffer sbf = new StringBuffer("Hello");

        sbf.append(" World");
        System.out.println("After append: " + sbf);

        sbf.insert(5, ",");
        System.out.println("After insert: " + sbf);

        sbf.delete(5, 6);
        System.out.println("After delete: " + sbf);

        sbf.replace(6, 11, "Java");
        System.out.println("After replace: " + sbf);

        sbf.reverse();
        System.out.println("After reverse: " + sbf);

        sbf.deleteCharAt(0);
        System.out.println("After deleteCharAt: " + sbf);

        sbf.append("ABC");
        System.out.println("Current: " + sbf);
        System.out.println("Char at 2: " + sbf.charAt(2));
        System.out.println("Index of 'A': " + sbf.indexOf("A"));
        System.out.println("Last index of 'C': " + sbf.lastIndexOf("C"));
    }

    private static void demonstrateThreadSafety() {
        System.out.println("\n=== Thread Safety Demonstration ===");

        StringBuilder sbNotThreadSafe = new StringBuilder();
        StringBuffer sbfThreadSafe = new StringBuffer();

        int threadCount = 10;
        int appendCount = 1000;
        CountDownLatch latch1 = new CountDownLatch(threadCount);
        CountDownLatch latch2 = new CountDownLatch(threadCount);

        ExecutorService executor1 = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor1.submit(() -> {
                for (int j = 0; j < appendCount; j++) {
                    sbNotThreadSafe.append("A");
                }
                latch1.countDown();
            });
        }
        try {
            latch1.await();
            executor1.shutdown();
            executor1.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ExecutorService executor2 = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor2.submit(() -> {
                for (int j = 0; j < appendCount; j++) {
                    sbfThreadSafe.append("B");
                }
                latch2.countDown();
            });
        }
        try {
            latch2.await();
            executor2.shutdown();
            executor2.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int expectedLength = threadCount * appendCount;
        System.out.println("StringBuilder (NOT synchronized):");
        System.out.println("  Expected length: " + expectedLength);
        System.out.println("  Actual length: " + sbNotThreadSafe.length());
        System.out.println("  Data corruption possible: " + (sbNotThreadSafe.length() != expectedLength));

        System.out.println("StringBuffer (synchronized):");
        System.out.println("  Expected length: " + expectedLength);
        System.out.println("  Actual length: " + sbfThreadSafe.length());
        System.out.println("  Data corruption possible: " + (sbfThreadSafe.length() != expectedLength));
    }

    private static void demonstratePerformanceComparison() {
        System.out.println("\n=== Performance Comparison ===");

        System.out.println("Running " + CONCAT_ITERATIONS + " concatenations...");

        long start = System.currentTimeMillis();
        String stringConcat = "";
        for (int i = 0; i < CONCAT_ITERATIONS; i++) {
            stringConcat += "a";
        }
        long stringTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CONCAT_ITERATIONS; i++) {
            sb.append("a");
        }
        String builderResult = sb.toString();
        long builderTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < CONCAT_ITERATIONS; i++) {
            sbf.append("a");
        }
        String bufferResult = sbf.toString();
        long bufferTime = System.currentTimeMillis() - start;

        System.out.println("String concat: " + stringTime + " ms");
        System.out.println("StringBuilder: " + builderTime + " ms");
        System.out.println("StringBuffer: " + bufferTime + " ms");
        System.out.println("StringBuilder is " + (stringTime / Math.max(builderTime, 1)) + "x faster than String");
        System.out.println("StringBuffer is " + (stringTime / Math.max(bufferTime, 1)) + "x faster than String");
    }

    private static void demonstrateMemoryUsage() {
        System.out.println("\n=== Memory Usage Comparison ===");

        Runtime runtime = Runtime.getRuntime();

        runtime.gc();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("Hello World ");
        }

        runtime.gc();
        long afterBuilderMemory = runtime.totalMemory() - runtime.freeMemory();

        runtime.gc();
        long beforeBufferMemory = runtime.totalMemory() - runtime.freeMemory();

        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < 10000; i++) {
            sbf.append("Hello World ");
        }

        runtime.gc();
        long afterBufferMemory = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("StringBuilder memory used: ~" + ((afterBuilderMemory - beforeMemory) / 1024) + " KB");
        System.out.println("StringBuffer memory used: ~" + ((afterBufferMemory - beforeBufferMemory) / 1024) + " KB");
    }

    private static void demonstrateStringConcatVsBuilder() {
        System.out.println("\n=== String Concatenation vs StringBuilder vs StringBuffer ===");

        List<String> words = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            words.add("word" + i);
        }

        long start = System.currentTimeMillis();
        String result1 = "";
        for (String word : words) {
            result1 += word + " ";
        }
        long concatTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word).append(" ");
        }
        String result2 = sb.toString();
        long builderTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (String word : words) {
            sbf.append(word).append(" ");
        }
        String result3 = sbf.toString();
        long bufferTime = System.currentTimeMillis() - start;

        System.out.println("String concat time: " + concatTime + " ms");
        System.out.println("StringBuilder time: " + builderTime + " ms");
        System.out.println("StringBuffer time: " + bufferTime + " ms");
        System.out.println("Results equal: " + (result1.equals(result2) && result2.equals(result3)));
    }

    private static void demonstrateLoopConcatenation() {
        System.out.println("\n=== StringBuilder in Loops vs String Concatenation ===");

        int iterations = 10000;

        long start = System.currentTimeMillis();
        String concatResult = "";
        for (int i = 0; i < iterations; i++) {
            concatResult = concatResult + "a";
        }
        long concatTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        String builderResult = sb.toString();
        long builderTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbf.append("a");
        }
        String bufferResult = sbf.toString();
        long bufferTime = System.currentTimeMillis() - start;

        System.out.println("String in loop: " + concatTime + " ms (creates " + iterations + " objects)");
        System.out.println("StringBuilder in loop: " + builderTime + " ms (mutates single object)");
        System.out.println("StringBuffer in loop: " + bufferTime + " ms (mutates single object, synchronized)");
        System.out.println("Results equal: " + (concatResult.equals(builderResult) && builderResult.equals(bufferResult)));
    }

    private static void demonstrateMethodChaining() {
        System.out.println("\n=== Method Chaining ===");

        String result = new StringBuilder()
                .append("Hello")
                .append(" ")
                .append("World")
                .insert(5, ",")
                .replace(6, 11, "Java")
                .delete(0, 5)
                .append("!")
                .toString();
        System.out.println("StringBuilder chain: " + result);

        String result2 = new StringBuffer()
                .append("Hello")
                .append(" ")
                .append("World")
                .insert(5, ",")
                .replace(6, 11, "Java")
                .delete(0, 5)
                .append("!")
                .toString();
        System.out.println("StringBuffer chain: " + result2);
    }

    private static void demonstrateSubstringOperations() {
        System.out.println("\n=== Substring Operations ===");

        StringBuilder sb = new StringBuilder("Hello World Java Programming");
        System.out.println("Original: " + sb);
        System.out.println("Substring 0,5: " + sb.substring(0, 5));
        System.out.println("Substring 6: " + sb.substring(6));
        System.out.println("Substring 6,11: " + sb.substring(6, 11));

        StringBuffer sbf = new StringBuffer("Hello World Java Programming");
        System.out.println("\nOriginal: " + sbf);
        System.out.println("Substring 0,5: " + sbf.substring(0, 5));
        System.out.println("Substring 6: " + sbf.substring(6));
        System.out.println("Substring 6,11: " + sbf.substring(6, 11));
    }

    private static void demonstrateCapacityVsLength() {
        System.out.println("\n=== Capacity vs Length ===");

        StringBuilder sb = new StringBuilder(100);
        System.out.println("StringBuilder - Initial capacity: " + sb.capacity());
        System.out.println("StringBuilder - Initial length: " + sb.length());

        sb.append("Hello");
        System.out.println("StringBuilder - After append 'Hello':");
        System.out.println("  Capacity: " + sb.capacity());
        System.out.println("  Length: " + sb.length());

        sb.append(" World Java Programming");
        System.out.println("StringBuilder - After append ' World Java Programming':");
        System.out.println("  Capacity: " + sb.capacity());
        System.out.println("  Length: " + sb.length());

        sb.ensureCapacity(200);
        System.out.println("StringBuilder - After ensureCapacity(200):");
        System.out.println("  Capacity: " + sb.capacity());
        System.out.println("  Length: " + sb.length());

        sb.trimToSize();
        System.out.println("StringBuilder - After trimToSize():");
        System.out.println("  Capacity: " + sb.capacity());
        System.out.println("  Length: " + sb.length());

        StringBuffer sbf = new StringBuffer(100);
        System.out.println("\nStringBuffer - Initial capacity: " + sbf.capacity());
        System.out.println("StringBuffer - Initial length: " + sbf.length());

        sbf.append("Hello");
        System.out.println("StringBuffer - After append 'Hello':");
        System.out.println("  Capacity: " + sbf.capacity());
        System.out.println("  Length: " + sbf.length());

        sbf.ensureCapacity(200);
        System.out.println("StringBuffer - After ensureCapacity(200):");
        System.out.println("  Capacity: " + sbf.capacity());
        System.out.println("  Length: " + sbf.length());

        sbf.trimToSize();
        System.out.println("StringBuffer - After trimToSize():");
        System.out.println("  Capacity: " + sbf.capacity());
        System.out.println("  Length: " + sbf.length());
    }

    private static void demonstrateWhenToUseWhich() {
        System.out.println("\n=== When to Use Which ===");

        System.out.println("Use StringBuilder when:");
        System.out.println("  - Single-threaded applications");
        System.out.println("  - Building strings in loops");
        System.out.println("  - Performance is critical");
        System.out.println("  - No concurrent access expected");

        System.out.println("\nUse StringBuffer when:");
        System.out.println("  - Multi-threaded applications");
        System.out.println("  - Shared mutable string buffer");
        System.out.println("  - Thread safety is required");
        System.out.println("  - Can tolerate slight performance overhead");

        System.out.println("\nUse String when:");
        System.out.println("  - String is immutable and rarely changes");
        System.out.println("  - Using string literals (pool optimization)");
        System.out.println("  - Thread safety inherently provided by immutability");
        System.out.println("  - HashMap keys or other hash-based collections");
    }
}
