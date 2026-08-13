package academy.javaengineering.text.internals;

import java.nio.charset.*;
import java.util.*;

public class CharsetInternals {

    public static void main(String[] args) {
        System.out.println("=== Charset Internals ===\n");

        // 1. Available Charsets
        System.out.println("--- Available Charsets ---");
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        System.out.println("UTF-8: " + charsets.containsKey("UTF-8"));
        System.out.println("ISO-8859-1: " + charsets.containsKey("ISO-8859-1"));
        System.out.println("US-ASCII: " + charsets.containsKey("US-ASCII"));

        // 2. Charset Conversion
        System.out.println("\n--- Charset Conversion ---");
        String text = "Hello, World!";
        byte[] utf8 = text.getBytes(StandardCharsets.UTF_8);
        byte[] latin1 = text.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println("UTF-8 bytes: " + utf8.length);
        System.out.println("ISO-8859-1 bytes: " + latin1.length);

        // 3. Default Charset
        System.out.println("\n--- Default Charset ---");
        System.out.println("Default: " + Charset.defaultCharset());
        System.out.println("Use explicit charset for portability");
    }
}
