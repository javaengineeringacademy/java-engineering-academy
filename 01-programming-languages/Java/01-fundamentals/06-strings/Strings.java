package academy.javaengineering.fundamentals;

/**
 * Strings in Java
 *
 * This file covers:
 * - String creation (literal vs new)
 * - String methods (length, charAt, substring, indexOf, contains, replace)
 * - String comparison (== vs equals)
 * - StringBuilder vs StringBuffer
 * - String formatting
 * - String immutability explanation
 * - String pool concept
 */
public class Strings {

    public static void main(String[] args) {

        // =========================================================
        // 1. STRING CREATION
        // =========================================================
        System.out.println("=== String Creation ===");

        // Method 1: String literal (preferred - uses String pool)
        String literal = "Hello, World!";

        // Method 2: Using new keyword (creates new object on heap)
        String newString = new String("Hello, World!");

        // Method 3: From char array
        char[] chars = {'J', 'a', 'v', 'a'};
        String fromChars = new String(chars);

        // Method 4: From byte array
        byte[] bytes = {72, 101, 108, 108, 111}; // ASCII for "Hello"
        String fromBytes = new String(bytes);

        System.out.println("String literal:   " + literal);
        System.out.println("Using new:        " + newString);
        System.out.println("From char array:  " + fromChars);
        System.out.println("From byte array:  " + fromBytes);

        // =========================================================
        // 2. STRING POOL (INTERNING)
        // =========================================================
        System.out.println("\n=== String Pool ===");

        // String literals are stored in a special memory area called String Pool
        // Two literals with same content share the same object
        String pool1 = "Java";
        String pool2 = "Java";
        String pool3 = new String("Java");

        System.out.println("pool1 == pool2: " + (pool1 == pool2));       // true  (same pool object)
        System.out.println("pool1 == pool3: " + (pool1 == pool3));       // false (new creates separate object)
        System.out.println("pool1.equals(pool3): " + pool1.equals(pool3)); // true  (same content)

        // intern() method returns the canonical representation
        String interned = pool3.intern();
        System.out.println("pool1 == interned: " + (pool1 == interned)); // true

        // =========================================================
        // 3. STRING IMMUTABILITY
        // =========================================================
        System.out.println("\n=== String Immutability ===");

        // Strings in Java are IMMUTABLE - once created, they cannot be changed
        String original = "Hello";
        String modified = original.concat(", World!");

        System.out.println("Original:  " + original);   // "Hello" - unchanged
        System.out.println("Modified:  " + modified);    // "Hello, World!"
        System.out.println("Same object? " + (original == modified)); // false

        // Every "modification" creates a new String object
        String str = "ABC";
        str = str + "DEF";  // Creates new String "ABCDEF", reassigns reference
        System.out.println("After concatenation: " + str);

        // Why immutable?
        // 1. Security: Strings used in class loading, networking, file paths
        // 2. Thread safety: Multiple threads can safely share String objects
        // 3. Hash caching: Hash code can be cached since content doesn't change
        // 4. String pool: Immutability allows safe sharing in the pool

        // =========================================================
        // 4. STRING METHODS
        // =========================================================
        System.out.println("\n=== String Methods ===");

        String text = "Hello, Java World!";

        // length() - returns number of characters
        System.out.println("String: \"" + text + "\"");
        System.out.println("length()          : " + text.length()); // 18

        // charAt() - returns character at specified index
        System.out.println("charAt(0)         : " + text.charAt(0));  // 'H'
        System.out.println("charAt(7)         : " + text.charAt(7));  // 'J'

        // substring() - returns portion of string
        System.out.println("substring(7)      : " + text.substring(7));      // "Java World!"
        System.out.println("substring(7, 11)  : " + text.substring(7, 11));  // "Java"

        // indexOf() - returns index of first occurrence
        System.out.println("indexOf('a')      : " + text.indexOf('a'));      // 3
        System.out.println("indexOf('Java')   : " + text.indexOf("Java"));   // 7
        System.out.println("indexOf('xyz')    : " + text.indexOf("xyz"));    // -1

        // lastIndexOf() - returns index of last occurrence
        System.out.println("lastIndexOf('l')  : " + text.lastIndexOf('l'));  // 16

        // contains() - checks if string contains substring
        System.out.println("contains('Java')  : " + text.contains("Java"));  // true
        System.out.println("contains('Python'): " + text.contains("Python")); // false

        // replace() - returns new string with replacements
        System.out.println("replace('l','L')  : " + text.replace('l', 'L'));       // "HeLLo, Java WorLd!"
        System.out.println("replace(\"Java\",\"Python\"): " + text.replace("Java", "Python"));

        // replaceAll() - uses regex
        System.out.println("replaceAll(\"[aeiou]\",\"*\"): " + text.replaceAll("[aeiou]", "*"));

        // =========================================================
        // 5. CASE METHODS
        // =========================================================
        System.out.println("\n=== Case Methods ===");

        String mixed = "Hello, World!";

        System.out.println("toUpperCase()    : " + mixed.toUpperCase());   // "HELLO, WORLD!"
        System.out.println("toLowerCase()    : " + mixed.toLowerCase());   // "hello, world!"
        System.out.println("toCharArray()    : " + java.util.Arrays.toString(mixed.toCharArray()));

        // =========================================================
        // 6. TRIMMING AND CHECKING
        // =========================================================
        System.out.println("\n=== Trimming and Checking ===");

        String padded = "   Hello, World!   ";
        System.out.println("Original:    \"" + padded + "\"");
        System.out.println("trim():      \"" + padded.trim() + "\"");       // Removes leading/trailing spaces
        System.out.println("strip():     \"" + padded.strip() + "\"");      // Java 11+ (Unicode aware)
        System.out.println("isEmpty()    : " + padded.isEmpty());            // false
        System.out.println("isBlank()    : " + padded.isBlank());            // false (Java 11+)
        System.out.println("startsWith(\"   \"): " + padded.startsWith("   ")); // true
        System.out.println("endsWith(\"!   \"): " + padded.endsWith("!   "));   // true

        // =========================================================
        // 7. SPLIT AND JOIN
        // =========================================================
        System.out.println("\n=== Split and Join ===");

        String csv = "apple,banana,cherry,date,elderberry";
        String[] fruits = csv.split(",");
        System.out.println("Split by comma: " + java.util.Arrays.toString(fruits));

        // Join array into string
        String joined = String.join(" | ", fruits);
        System.out.println("Join with |: " + joined);

        // Split with limit
        String data = "one-two-three-four-five";
        String[] parts = data.split("-", 3); // Split into max 3 parts
        System.out.println("Split with limit: " + java.util.Arrays.toString(parts));

        // =========================================================
        // 8. STRING COMPARISON
        // =========================================================
        System.out.println("\n=== String Comparison ===");

        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = "hello";
        String s4 = new String("Hello");

        // == compares references (memory address)
        System.out.println("== comparison:");
        System.out.println("s1 == s2  : " + (s1 == s2));   // true (same pool object)
        System.out.println("s1 == s4  : " + (s1 == s4));   // false (different objects)

        // equals() compares content
        System.out.println("\n.equals() comparison:");
        System.out.println("s1.equals(s2)   : " + s1.equals(s2));       // true
        System.out.println("s1.equals(s4)   : " + s1.equals(s4));       // true
        System.out.println("s1.equals(s3)   : " + s1.equals(s3));       // false

        // equalsIgnoreCase() ignores case
        System.out.println("\n.equalsIgnoreCase() comparison:");
        System.out.println("s1.equalsIgnoreCase(s3): " + s1.equalsIgnoreCase(s3)); // true

        // compareTo() - lexicographic comparison
        System.out.println("\n.compareTo() results:");
        System.out.println("s1.compareTo(s2)    : " + s1.compareTo(s2));     // 0 (equal)
        System.out.println("s1.compareTo(s3)    : " + s1.compareTo(s3));     // negative (before)
        System.out.println("s3.compareTo(s1)    : " + s3.compareTo(s1));     // positive (after)
        System.out.println("s1.compareToIgnoreCase(s3): " + s1.compareToIgnoreCase(s3)); // 0

        // =========================================================
        // 9. STRING BUILDER AND STRING BUFFER
        // =========================================================
        System.out.println("\n=== StringBuilder vs StringBuffer ===");

        // StringBuilder - mutable, NOT thread-safe, faster
        // Use when: single-threaded string manipulation
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(", World!");
        sb.insert(5, " Beautiful");
        sb.replace(7, 14, "Java");
        sb.delete(12, 17);
        sb.reverse();
        System.out.println("StringBuilder operations:");
        System.out.println("  After append: " + "Hello, World!");
        System.out.println("  Final result (reversed): " + sb);

        // Performance comparison
        System.out.println("\nPerformance test (10000 concatenations):");

        // Bad: String concatenation in loop (creates many objects)
        long start = System.nanoTime();
        String strResult = "";
        for (int i = 0; i < 10000; i++) {
            strResult += "a";
        }
        long stringTime = System.nanoTime() - start;
        System.out.println("  String += : " + stringTime + " ns");

        // Good: StringBuilder (modifies same object)
        start = System.nanoTime();
        StringBuilder sbResult = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sbResult.append("a");
        }
        String sbFinal = sbResult.toString();
        long sbTime = System.nanoTime() - start;
        System.out.println("  StringBuilder: " + sbTime + " ns");
        System.out.println("  StringBuilder is ~" + (stringTime / sbTime) + "x faster");

        // StringBuffer - mutable, thread-safe (synchronized), slower
        // Use when: multi-threaded string manipulation
        StringBuffer buffer = new StringBuffer("Thread-safe");
        buffer.append(" string");
        System.out.println("\nStringBuffer: " + buffer);

        // =========================================================
        // 10. STRING FORMATTING
        // =========================================================
        System.out.println("\n=== String Formatting ===");

        // String.format() - like printf but returns a string
        String name = "Alice";
        int age = 30;
        double gpa = 3.85;

        String formatted = String.format("Name: %s, Age: %d, GPA: %.2f", name, age, gpa);
        System.out.println("Formatted: " + formatted);

        // Format specifiers
        System.out.println("\nFormat specifiers:");
        System.out.println("  %s  - string:       " + String.format("%s", "Hello"));
        System.out.println("  %d  - integer:      " + String.format("%d", 42));
        System.out.println("  %f  - float:        " + String.format("%f", 3.14));
        System.out.println("  %.2f - 2 decimals:  " + String.format("%.2f", 3.14159));
        System.out.println("  %10s - right align: " + String.format("%10s", "Hi"));
        System.out.println("  %-10s - left align: " + String.format("%-10s", "Hi"));
        System.out.println("  %05d - zero padded: " + String.format("%05d", 42));

        // printf for direct output
        System.out.printf("printf: Name=%s, Age=%d, GPA=%.2f%n", name, age, gpa);

        // =========================================================
        // 11. ESCAPE CHARACTERS
        // =========================================================
        System.out.println("\n=== Escape Characters ===");

        System.out.println("Tab:\tHello");
        System.out.println("Newline:\nHello");
        System.out.println("Backslash: \\");
        System.out.println("Quote: \"Hello\"");
        System.out.println("Unicode: \u00A9 2024");
        System.out.println("Null char: \0");

        // =========================================================
        // 12. PRACTICAL EXAMPLES
        // =========================================================
        System.out.println("\n=== Practical Examples ===");

        // Check if string is palindrome
        String palindrome = "racecar";
        String reversed = new StringBuilder(palindrome).reverse().toString();
        boolean isPalindrome = palindrome.equals(reversed);
        System.out.println("\"" + palindrome + "\" is palindrome: " + isPalindrome);

        // Count vowels
        String sentence = "Hello World";
        int vowelCount = 0;
        for (char c : sentence.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) != -1) {
                vowelCount++;
            }
        }
        System.out.println("\"" + sentence + "\" has " + vowelCount + " vowels");

        // Capitalize first letter
        String word = "hello";
        String capitalized = word.substring(0, 1).toUpperCase() + word.substring(1);
        System.out.println("Capitalize: \"" + word + "\" -> \"" + capitalized + "\"");

        // Truncate with ellipsis
        String longString = "This is a very long string that needs to be truncated";
        int maxLength = 20;
        String truncated = (longString.length() > maxLength)
            ? longString.substring(0, maxLength) + "..."
            : longString;
        System.out.println("Truncated: \"" + truncated + "\"");

        // Check if string contains only digits
        String numberStr = "12345";
        boolean isNumeric = numberStr.matches("\\d+");
        System.out.println("\"" + numberStr + "\" is numeric: " + isNumeric);

        System.out.println("\n=== Strings Demo Complete ===");
    }
}
