package com.javaacademy.sprint1.strings;

/**
 * StringBasics - Demonstrates String fundamentals in Java.
 * 
 * <p><b>String is:</b>
 * <ul>
 *   <li><b>Immutable</b> - cannot be changed after creation</li>
 *   <li><b>Final</b> - cannot be subclassed</li>
 *   <li><b>Interned</b> - string literals shared in String Pool</li>
 *   <li><b>Object</b> - reference type, not primitive</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> 
 * - String = printed book (immutable, can't change pages)
 * - StringBuilder = draft manuscript (mutable, edit freely)
 * - String Pool = library catalog (shared copies of same book)
 * 
 * <p><b>Immutability Benefits:</b>
 * <ul>
 *   <li>Thread-safe (no synchronization needed)</li>
 *   <li>Security (parameters can't be modified)</li>
 *   <li>Caching (hashcode, substring optimization)</li>
 *   <li>String Pool sharing</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class StringBasics {

    private StringBasics() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== String Basics ===\n");

        // Creation methods
        System.out.println("--- Creation ---");
        String literal = "Hello";              // String pool
        String anotherLiteral = "Hello";       // Same pool reference!
        String newString = new String("Hello"); // Heap, new object
        String fromChars = new String(new char[]{'H', 'e', 'l', 'l', 'o'});
        String fromBytes = new String(new byte[]{72, 101, 108, 108, 111}); // ASCII

        System.out.println("literal == anotherLiteral: " + (literal == anotherLiteral)); // true (pool)
        System.out.println("literal == newString: " + (literal == newString));           // false (different objects)
        System.out.println("literal.equals(newString): " + literal.equals(newString));   // true (content)

        // String Pool
        System.out.println("\n--- String Pool ---");
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        String s4 = s3.intern(); // Forces pool lookup/insert
        
        System.out.println("s1 == s2: " + (s1 == s2)); // true
        System.out.println("s1 == s3: " + (s1 == s3)); // false
        System.out.println("s1 == s4: " + (s1 == s4)); // true (interned)

        // Length and indexing
        System.out.println("\n--- Length & Indexing ---");
        String text = "Hello";
        System.out.println("Length: " + text.length());           // 5
        System.out.println("Char at 0: " + text.charAt(0));       // H
        System.out.println("Char at 4: " + text.charAt(4));       // o
        // text.charAt(5) // StringIndexOutOfBoundsException!

        // Substring (Java 7+ shares backing array, Java 7u6+ copies)
        System.out.println("\n--- Substring ---");
        String str = "JavaEngineering";
        System.out.println("substring(4): " + str.substring(4));        // Engineering
        System.out.println("substring(0,4): " + str.substring(0, 4));   // Java

        // Searching
        System.out.println("\n--- Searching ---");
        String haystack = "Hello World Hello";
        System.out.println("indexOf('Hello'): " + haystack.indexOf("Hello"));      // 0
        System.out.println("lastIndexOf('Hello'): " + haystack.lastIndexOf("Hello")); // 12
        System.out.println("indexOf('Java'): " + haystack.indexOf("Java"));        // -1 (not found)
        System.out.println("contains('World'): " + haystack.contains("World"));     // true
        System.out.println("startsWith('Hello'): " + haystack.startsWith("Hello")); // true
        System.out.println("endsWith('Hello'): " + haystack.endsWith("Hello"));     // true

        // Comparison
        System.out.println("\n--- Comparison ---");
        String a = "apple";
        String b = "banana";
        String c = "apple";
        System.out.println("a.equals(b): " + a.equals(b));           // false
        System.out.println("a.equals(c): " + a.equals(c));           // true
        System.out.println("a.equalsIgnoreCase('APPLE'): " + a.equalsIgnoreCase("APPLE")); // true
        System.out.println("a.compareTo(b): " + a.compareTo(b));     // negative (a < b)
        System.out.println("a.compareTo(c): " + a.compareTo(c));     // 0 (equal)
        System.out.println("'b'.compareTo('a'): " + "b".compareTo("a")); // positive

        // Transformation
        System.out.println("\n--- Transformation ---");
        String messy = "  Hello World  ";
        System.out.println("trim(): '" + messy.trim() + "'");                    // "Hello World"
        System.out.println("toUpperCase(): " + messy.trim().toUpperCase());      // "HELLO WORLD"
        System.out.println("toLowerCase(): " + messy.trim().toLowerCase());      // "hello world"
        System.out.println("replace('l', 'L'): " + messy.trim().replace('l', 'L')); // "HeLLo WorLd"
        System.out.println("replaceAll('\\\\s+', '-'): " + messy.trim().replaceAll("\\s+", "-")); // "Hello-World"

        // Splitting
        System.out.println("\n--- Splitting ---");
        String csv = "apple,banana,orange";
        String[] fruits = csv.split(",");
        System.out.println("split(','): " + java.util.Arrays.toString(fruits));
        
        String spaced = "apple  banana   orange";
        String[] words = spaced.split("\\s+"); // Regex: one or more whitespace
        System.out.println("split('\\\\s+'): " + java.util.Arrays.toString(words));

        // Empty/Null checks
        System.out.println("\n--- Empty/Null ---");
        System.out.println("\"\".isEmpty(): " + "".isEmpty());       // true
        System.out.println("\" \".isEmpty(): " + " ".isEmpty());     // false (has space)
        System.out.println("\" \".isBlank(): " + " ".isBlank());     // true (Java 11+)
        System.out.println("\"hi\".isBlank(): " + "hi".isBlank());   // false
        
        String nullStr = null;
        // nullStr.isEmpty() // NPE!
        System.out.println("Objects.equals(null, \"\"): " + java.util.Objects.equals(nullStr, "")); // Safe

        // Expected output demonstrates all String basics
    }
}