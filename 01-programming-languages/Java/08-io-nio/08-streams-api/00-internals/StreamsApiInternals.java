package academy.javaengineering.io.internals;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

public class StreamsApiInternals {

    public static void main(String[] args) {
        System.out.println("=== Streams API Internals ===\n");

        // 1. Stream Concept
        System.out.println("--- Stream Concept ---");
        System.out.println("Sequence of elements");
        System.out.println("Supports lazy processing");
        System.out.println("Functional-style operations");

        // 2. Stream Sources
        System.out.println("\n--- Stream Sources ---");
        System.out.println("Collection.stream()");
        System.out.println("Arrays.stream()");
        System.out.println("Stream.of()");
        System.out.println("Files.lines()");

        // 3. Stream Operations
        System.out.println("\n--- Stream Operations ---");
        System.out.println("Intermediate: filter, map, sorted");
        System.out.println("Terminal: collect, forEach, reduce");
        System.out.println("Short-circuit: findFirst, limit");
    }
}
