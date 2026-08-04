package academy.javaengineering.fundamentals;

import java.util.Locale;

/**
 * Demonstrates Java String basics: creation, immutability, common methods,
 * StringBuilder, and string formatting.
 *
 * <p>Strings in Java are immutable objects. Once created, their content
 * cannot be changed. Every modification creates a new String object.
 * StringBuilder is used for mutable string operations.</p>
 */
public class StringBasics {

    public static void main(String[] args) {
        System.out.println("=== String Basics Demo ===\n");

        demoStringCreation();
        demoImmutability();
        demoCommonMethods();
        demoSubstringOperations();
        demoSearchMethods();
        demoReplaceAndSplit();
        demoStringBuilder();
        demoStringFormatting();
        demoStringComparison();
        demoStringConversion();
    }

    // --- String Creation ---

    /**
     * Demonstrates different ways to create Strings.
     */
    public static void demoStringCreation() {
        System.out.println("--- String Creation ---");

        // String literal (stored in string pool)
        String literal1 = "Hello";
        String literal2 = "Hello";
        System.out.println("Literal 1: " + literal1);
        System.out.println("Literal 2: " + literal2);
        System.out.println("Same reference? " + (literal1 == literal2) + " (string pool)");

        // new String (creates new object on heap)
        String newStr1 = new String("Hello");
        String newStr2 = new String("Hello");
        System.out.println("\nnew String 1: " + newStr1);
        System.out.println("new String 2: " + newStr2);
        System.out.println("Same reference? " + (newStr1 == newStr2) + " (different objects)");
        System.out.println("Value equal? " + newStr1.equals(newStr2));

        // From char array
        char[] chars = {'J', 'a', 'v', 'a'};
        String fromChars = new String(chars);
        System.out.println("\nFrom char array: " + fromChars);

        // From byte array
        byte[] bytes = {72, 101, 108, 108, 111};
        String fromBytes = new String(bytes);
        System.out.println("From byte array: " + fromBytes);

        // Empty and blank strings
        String empty = "";
        String blank = "   ";
        String alsoEmpty = new String();
        System.out.println("\nEmpty string: \"" + empty + "\" (length: " + empty.length() + ")");
        System.out.println("Blank string: \"" + blank + "\" (length: " + blank.length() + ")");
        System.out.println("new String(): \"" + alsoEmpty + "\" (length: " + alsoEmpty.length() + ")");
        System.out.println();
    }

    // --- Immutability ---

    /**
     * Demonstrates that Strings are immutable.
     */
    public static void demoImmutability() {
        System.out.println("--- String Immutability ---");

        String original = "Hello";
        String modified = original.concat(" World");

        System.out.println("Original: " + original);
        System.out.println("Modified: " + modified);
        System.out.println("Original unchanged? " + (original.equals("Hello")));

        // Each operation creates a new String
        String upper = original.toUpperCase();
        String trimmed = "  Hello  ".trim();
        String replaced = original.replace('l', 'L');

        System.out.println("\ntoUpperCase: " + upper);
        System.out.println("Original after toUpperCase: " + original);
        System.out.println("trim: " + trimmed);
        System.out.println("replace: " + replaced);

        // String concatenation in loop (inefficient)
        System.out.println("\nString concatenation creates many objects:");
        String result = "";
        for (int i = 0; i < 5; i++) {
            result += i + " "; // Creates new String each time
        }
        System.out.println("Result: " + result.trim());

        // Better: use StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i).append(" ");
        }
        System.out.println("StringBuilder: " + sb.toString().trim());
        System.out.println();
    }

    // --- Common Methods ---

    /**
     * Demonstrates fundamental String methods.
     */
    public static void demoCommonMethods() {
        System.out.println("--- Common String Methods ---");

        String str = "Hello, World!";

        // length()
        System.out.println("str = \"" + str + "\"");
        System.out.println("length(): " + str.length());

        // charAt()
        System.out.println("charAt(0): " + str.charAt(0));
        System.out.println("charAt(7): " + str.charAt(7));

        // isEmpty() and isBlank()
        System.out.println("isEmpty(): " + str.isEmpty());
        System.out.println("\"\".isEmpty(): " + "".isEmpty());
        System.out.println("\" \".isBlank(): " + " ".isBlank());
        System.out.println("\"\".isBlank(): " + "".isBlank());

        // substring()
        System.out.println("substring(7): " + str.substring(7));
        System.out.println("substring(0, 5): " + str.substring(0, 5));

        // indexOf()
        System.out.println("indexOf('o'): " + str.indexOf('o'));
        System.out.println("indexOf('o', 5): " + str.indexOf('o', 5));
        System.out.println("indexOf(\"World\"): " + str.indexOf("World"));
        System.out.println("indexOf(\"xyz\"): " + str.indexOf("xyz"));

        // lastIndexOf()
        System.out.println("lastIndexOf('l'): " + str.lastIndexOf('l'));

        // contains()
        System.out.println("contains(\"World\"): " + str.contains("World"));
        System.out.println("contains(\"Java\"): " + str.contains("Java"));

        // startsWith() and endsWith()
        System.out.println("startsWith(\"Hello\"): " + str.startsWith("Hello"));
        System.out.println("endsWith(\"!\"): " + str.endsWith("!"));
        System.out.println();
    }

    // --- Substring Operations ---

    /**
     * Demonstrates substring extraction and manipulation.
     */
    public static void demoSubstringOperations() {
        System.out.println("--- Substring Operations ---");

        String email = "user@example.com";

        // Extract username
        int atIndex = email.indexOf('@');
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Domain: " + domain);

        // File path parsing
        String path = "/home/user/documents/file.txt";
        int lastSlash = path.lastIndexOf('/');
        String filename = path.substring(lastSlash + 1);
        String directory = path.substring(0, lastSlash);
        System.out.println("\nPath: " + path);
        System.out.println("Directory: " + directory);
        System.out.println("Filename: " + filename);

        // Extract file extension
        int dotIndex = filename.lastIndexOf('.');
        String name = filename.substring(0, dotIndex);
        String extension = filename.substring(dotIndex + 1);
        System.out.println("Name: " + name + ", Extension: " + extension);

        // Chained substring operations
        String fullName = "John Michael Smith";
        String firstName = fullName.substring(0, fullName.indexOf(' '));
        String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
        System.out.println("\nFull name: " + fullName);
        System.out.println("First: " + firstName + ", Last: " + lastName);
        System.out.println();
    }

    // --- Search Methods ---

    /**
     * Demonstrates searching within strings.
     */
    public static void demoSearchMethods() {
        System.out.println("--- Search Methods ---");

        String text = "the quick brown fox jumps over the lazy dog";

        // indexOf variants
        System.out.println("Text: \"" + text + "\"");
        System.out.println("indexOf(\"the\"): " + text.indexOf("the"));
        System.out.println("indexOf(\"the\", 10): " + text.indexOf("the", 10));
        System.out.println("lastIndexOf(\"the\"): " + text.lastIndexOf("the"));

        // Count occurrences
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf("the", idx)) != -1) {
            count++;
            idx += 3;
        }
        System.out.println("Occurrences of \"the\": " + count);

        // regionMatches
        String s1 = "Hello, World!";
        String s2 = "World";
        System.out.println("\n\"Hello, World!\".regionMatches(7, \"World\", 0, 5): " +
                s1.regionMatches(7, s2, 0, 5));

        // matches (regex)
        String phone = "123-456-7890";
        boolean isPhone = phone.matches("\\d{3}-\\d{3}-\\d{4}");
        System.out.println("\"" + phone + "\" matches phone pattern: " + isPhone);

        // Pattern matching with contains
        System.out.println("\nSearching for words:");
        String[] words = {"the", "cat", "fox", "dog", "bird"};
        for (String word : words) {
            boolean found = text.contains(word);
            System.out.println("  \"" + word + "\": " + found);
        }
        System.out.println();
    }

    // --- Replace and Split ---

    /**
     * Demonstrates string replacement and splitting.
     */
    public static void demoReplaceAndSplit() {
        System.out.println("--- Replace and Split ---");

        // replace
        String original = "Hello, World! Hello, Java!";
        String replaced = original.replace("Hello", "Hi");
        System.out.println("Original: " + original);
        System.out.println("replace(\"Hello\", \"Hi\"): " + replaced);

        // replaceFirst
        String first = original.replaceFirst("Hello", "Hi");
        System.out.println("replaceFirst: " + first);

        // replaceAll with regex
        String withNumbers = "abc123def456ghi789";
        String noNumbers = withNumbers.replaceAll("\\d", "");
        System.out.println("\nreplaceAll(\"\\\\d\", \"\"): " + noNumbers);

        // replaceAll to format
        String messy = "hello   world    java";
        String clean = messy.replaceAll("\\s+", " ");
        System.out.println("Collapse spaces: " + clean);

        // split
        String csv = "apple,banana,cherry,date";
        String[] fruits = csv.split(",");
        System.out.println("\nsplit(\",\"): " + java.util.Arrays.toString(fruits));

        // split with limit
        String sentence = "one two three four five";
        String[] twoWords = sentence.split(" ", 2);
        System.out.println("split(\" \", 2): " + java.util.Arrays.toString(twoWords));

        // split with regex
        String data = "name:age:city";
        String[] parts = data.split(":");
        System.out.println("split(\":\"): " + java.util.Arrays.toString(parts));

        // join (opposite of split)
        String joined = String.join(" | ", fruits);
        System.out.println("\nString.join: " + joined);

        // strip vs trim
        String padded = "  Hello, World!  ";
        System.out.println("\nstrip:   \"" + padded.strip() + "\"");
        System.out.println("trim:    \"" + padded.trim() + "\"");
        System.out.println("stripLeading:  \"" + padded.stripLeading() + "\"");
        System.out.println("stripTrailing: \"" + padded.stripTrailing() + "\"");
        System.out.println();
    }

    // --- StringBuilder ---

    /**
     * Demonstrates StringBuilder for mutable string operations.
     */
    public static void demoStringBuilder() {
        System.out.println("--- StringBuilder ---");

        // Basic operations
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("append: " + sb);

        // Insert
        sb.insert(5, ",");
        System.out.println("insert(5, ','): " + sb);

        // Delete
        sb.delete(5, 6);
        System.out.println("delete(5, 6): " + sb);

        // Replace
        sb.replace(6, 11, "Java");
        System.out.println("replace(6, 11, \"Java\"): " + sb);

        // Reverse
        StringBuilder rev = new StringBuilder("Hello");
        rev.reverse();
        System.out.println("reverse: " + rev);

        // Capacity and length
        StringBuilder large = new StringBuilder(100);
        System.out.println("\nNew StringBuilder(100):");
        System.out.println("  capacity(): " + large.capacity());
        System.out.println("  length(): " + large.length());

        // Performance comparison
        System.out.println("\nPerformance: StringBuilder is much faster for concatenation in loops");

        // Building a string efficiently
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append("  <head><title>Test</title></head>\n");
        html.append("  <body>\n");
        for (int i = 1; i <= 3; i++) {
            html.append("    <p>Paragraph ").append(i).append("</p>\n");
        }
        html.append("  </body>\n");
        html.append("</html>");
        System.out.println("\nHTML built with StringBuilder:\n" + html);

        // toString
        String result = html.toString();
        System.out.println("\ntoString() length: " + result.length());
        System.out.println();
    }

    // --- String Formatting ---

    /**
     * Demonstrates different string formatting methods.
     */
    public static void demoStringFormatting() {
        System.out.println("--- String Formatting ---");

        // String.format
        String name = "Alice";
        int age = 30;
        double gpa = 3.85;

        String formatted = String.format("Name: %s, Age: %d, GPA: %.2f", name, age, gpa);
        System.out.println("String.format: " + formatted);

        // printf
        System.out.printf("printf: Name: %s, Age: %d, GPA: %.2f%n", name, age, gpa);

        // Formatting specifiers
        System.out.println("\nFormat specifiers:");
        System.out.printf("  %%s (string):  %s%n", "hello");
        System.out.printf("  %%d (decimal): %d%n", 42);
        System.out.printf("  %%f (float):   %.3f%n", 3.14159);
        System.out.printf("  %%c (char):    %c%n", 'A');
        System.out.printf("  %%b (boolean): %b%n", true);
        System.out.printf("  %%n (newline): [before]%n[after]");

        // Width and alignment
        System.out.println("\nWidth and alignment:");
        System.out.printf("  Right-aligned: [%10s]%n", "right");
        System.out.printf("  Left-aligned:  [%-10s]%n", "left");
        System.out.printf("  Zero-padded:   [%06d]%n", 42);
        System.out.printf("  Space-padded:  [%6d]%n", 42);

        // Date formatting
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String dateStr = String.format("Date: %tF, Time: %tT", now, now);
        System.out.println("\n" + dateStr);

        // Text block (Java 13+)
        String textBlock = """
                This is a text block.
                    It preserves indentation.
                Line 3 of the text block.
                """;
        System.out.println("\nText block:\n" + textBlock);

        // formatted method (Java 15+)
        String msg = "Hello, %s! You have %d new messages.".formatted("Bob", 5);
        System.out.println("formatted(): " + msg);
        System.out.println();
    }

    // --- String Comparison ---

    /**
     * Demonstrates different ways to compare strings.
     */
    public static void demoStringComparison() {
        System.out.println("--- String Comparison ---");

        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        String s4 = "hello";

        // == vs equals
        System.out.println("s1 = \"Hello\", s2 = \"Hello\", s3 = new String(\"Hello\")");
        System.out.println("s1 == s2:    " + (s1 == s2) + " (same pool reference)");
        System.out.println("s1 == s3:    " + (s1 == s3) + " (different objects)");
        System.out.println("s1.equals(s3): " + s1.equals(s3) + " (value equality)");

        // equalsIgnoreCase
        System.out.println("\ns1.equalsIgnoreCase(s4): " + s1.equalsIgnoreCase(s4));

        // compareTo (lexicographic ordering)
        System.out.println("\ncompareTo:");
        System.out.println("  \"Apple\".compareTo(\"Banana\"): " + "Apple".compareTo("Banana") + " (negative = before)");
        System.out.println("  \"Banana\".compareTo(\"Apple\"): " + "Banana".compareTo("Apple") + " (positive = after)");
        System.out.println("  \"Hello\".compareTo(\"Hello\"): " + "Hello".compareTo("Hello") + " (zero = equal)");

        // compareToIgnoreCase
        System.out.println("\ncompareToIgnoreCase:");
        System.out.println("  \"apple\".compareToIgnoreCase(\"Banana\"): " + "apple".compareToIgnoreCase("Banana"));

        // Sorting strings
        String[] names = {"Charlie", "alice", "Bob", "david"};
        java.util.Arrays.sort(names);
        System.out.println("\nNatural sort: " + java.util.Arrays.toString(names));

        java.util.Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        System.out.println("Case-insensitive: " + java.util.Arrays.toString(names));

        // hashCode consistency
        System.out.println("\nhashCode consistency:");
        System.out.println("  s1.hashCode(): " + s1.hashCode());
        System.out.println("  s3.hashCode(): " + s3.hashCode());
        System.out.println("  Equal strings have equal hashCodes: " + (s1.hashCode() == s3.hashCode()));
        System.out.println();
    }

    // --- String Conversion ---

    /**
     * Demonstrates converting between Strings and other types.
     */
    public static void demoStringConversion() {
        System.out.println("--- String Conversion ---");

        // Number to String
        int intVal = 42;
        double doubleVal = 3.14159;
        boolean boolVal = true;

        System.out.println("int to String:    " + String.valueOf(intVal));
        System.out.println("double to String: " + String.valueOf(doubleVal));
        System.out.println("boolean to String: " + String.valueOf(boolVal));
        System.out.println("Concatenation:    " + "" + intVal); // Implicit conversion

        // String to Number
        String intStr = "123";
        String doubleStr = "3.14159";
        String hexStr = "FF";

        System.out.println("\nString to int:    " + Integer.parseInt(intStr));
        System.out.println("String to double: " + Double.parseDouble(doubleStr));
        System.out.println("Hex to int:       " + Integer.parseInt(hexStr, 16));

        // Character conversions
        char ch = 'A';
        String charStr = Character.toString(ch);
        System.out.println("\nchar to String: " + charStr);
        System.out.println("String to char: " + "Hello".charAt(0));

        // char array conversions
        char[] charArray = "Hello".toCharArray();
        System.out.println("toCharArray: " + java.util.Arrays.toString(charArray));
        String fromCharArray = new String(charArray);
        System.out.println("new String(charArray): " + fromCharArray);

        // bytes conversion
        byte[] bytes = "Hello".getBytes();
        System.out.println("getBytes: " + java.util.Arrays.toString(bytes));
        String fromBytes = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("new String(bytes): " + fromBytes);

        // Case conversions
        String mixed = "Hello, World!";
        System.out.println("\ntoUpperCase: " + mixed.toUpperCase());
        System.out.println("toLowerCase: " + mixed.toLowerCase());
        System.out.println("Locale-aware: " + mixed.toUpperCase(Locale.GERMAN));
        System.out.println();
    }
}
