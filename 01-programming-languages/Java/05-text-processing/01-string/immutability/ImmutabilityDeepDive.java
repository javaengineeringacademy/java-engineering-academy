import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * String Immutability Deep Dive
 *
 * Complete coverage of why and how String is immutable in Java.
 *
 * Key concepts:
 * - String is a final class with final byte[] value (Java 9+)
 * - Immutability provides thread safety, security, caching, and pool safety
 * - Trade-offs include memory waste from concatenation
 * - Immutable classes can be created with final fields and no setters
 */
public class ImmutabilityDeepDive {

    public static void main(String[] args) {

        // =====================================================================
        // 1. HOW STRING IS MADE IMMUTABLE
        // =====================================================================
        System.out.println("=== 1. HOW STRING IS MADE IMMUTABLE ===");

        // String class declaration:
        // public final class String { ... }
        // - final class: cannot be extended/subclassed

        // Internal storage (Java 9+):
        // private final byte[] value;
        // private final byte coder; // LATIN1 or UTF16

        // Before Java 9:
        // private final char[] value;

        // The 'final' keyword on the array means:
        // - The reference cannot be reassigned
        // - The array contents CAN technically be modified via reflection
        // - But String provides no way to modify contents through its API

        String original = "Hello";
        String modified = original.concat(" World");

        System.out.println("original: " + original); // "Hello" - unchanged
        System.out.println("modified: " + modified); // "Hello World" - new object
        System.out.println("original == modified: " + (original == modified)); // false

        System.out.println();

        // =====================================================================
        // 2. WHAT WOULD HAPPEN IF STRING WAS MUTABLE?
        // =====================================================================
        System.out.println("=== 2. MUTABILITY DANGER SCENARIOS ===");

        // Scenario 1: Security breach
        // If String were mutable, modifying a password string would affect
        // all references to that password, including in authentication systems
        String password = "secret123";
        String username = "admin";
        // If mutable: password.replace('s', 'x') would change all "secret123" references

        // Scenario 2: Thread safety issues
        // Multiple threads reading the same String could see partial writes
        // Without immutability, every string operation would need synchronization

        // Scenario 3: Hash code instability
        // If String content could change, cached hash codes would become invalid
        // This would break HashMap, HashSet, and other hash-based collections

        // Scenario 4: String pool corruption
        // If a pooled string could be modified, all references would be affected
        // This would cause unpredictable behavior across the entire application

        System.out.println("Immutability prevents all these scenarios");

        System.out.println();

        // =====================================================================
        // 3. BENEFIT: THREAD SAFETY
        // =====================================================================
        System.out.println("=== 3. THREAD SAFETY ===");

        // Immutable objects are inherently thread-safe
        // No synchronization needed for read access
        // Safe to publish across threads without volatile or synchronization

        final String sharedString = "Thread Safe";

        // Multiple threads can safely read the same String
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Thread 1: " + sharedString);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Thread 2: " + sharedString);
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Both threads read the same value safely");

        System.out.println();

        // =====================================================================
        // 4. BENEFIT: SECURITY
        // =====================================================================
        System.out.println("=== 4. SECURITY ===");

        // Java uses Strings for:
        // - Class loading (class names, package names)
        // - Network connections (hostnames, URLs)
        // - File paths
        // - Database URLs and credentials

        // If Strings were mutable, an attacker could:
        // 1. Load a malicious class by changing class name strings
        // 2. Redirect network connections to malicious servers
        // 3. Access unauthorized files by modifying file paths

        String className = "java.lang.String";
        String filePath = "/etc/passwd";
        String dbUrl = "jdbc:mysql://localhost:3306/mydb";

        // These Strings cannot be modified, ensuring security
        System.out.println("Class name immutable: " + className);
        System.out.println("File path immutable: " + filePath);
        System.out.println("DB URL immutable: " + dbUrl);

        System.out.println();

        // =====================================================================
        // 5. BENEFIT: HASH CODE CACHING
        // =====================================================================
        System.out.println("=== 5. HASH CODE CACHING ===");

        // String caches its hash code after first computation
        // This makes HashMap/HashSet operations very fast
        // Hash code is computed once, stored, and reused

        String key = "important_key";
        int hash1 = key.hashCode();
        int hash2 = key.hashCode(); // Returns cached value

        System.out.println("First hashCode(): " + hash1);
        System.out.println("Second hashCode(): " + hash2);
        System.out.println("Same value: " + (hash1 == hash2)); // true

        // This caching is safe because String is immutable
        // The hash code will never change, so it can be computed once

        System.out.println();

        // =====================================================================
        // 6. BENEFIT: STRING POOL SAFETY
        // =====================================================================
        System.out.println("=== 6. STRING POOL SAFETY ===");

        // The String pool allows safe sharing of identical strings
        // This is only possible because Strings cannot be modified

        String pooled1 = "shared";
        String pooled2 = "shared";

        // Both point to the same pool object
        // This is safe because neither can modify the content
        System.out.println("pooled1 == pooled2: " + (pooled1 == pooled2)); // true
        System.out.println("Safe to share because immutable");

        // If Strings were mutable, modifying pooled1 would affect pooled2
        // This would cause bugs that are extremely hard to debug

        System.out.println();

        // =====================================================================
        // 7. BENEFIT: COMPILE-TIME CONSTANTS
        // =====================================================================
        System.out.println("=== 7. COMPILE-TIME CONSTANTS ===");

        // String literals are compile-time constants
        // The compiler can optimize them through constant folding
        // This is only possible because Strings are immutable

        final String FIRST = "Hello";
        final String SECOND = " World";
        String combined = FIRST + SECOND; // Compiler optimizes to "Hello World"

        // The compiler replaces this with a single reference at compile time
        System.out.println("combined: " + combined);
        System.out.println("combined == \"Hello World\": " + (combined == "Hello World")); // true

        System.out.println();

        // =====================================================================
        // 8. COST: MEMORY WASTE FROM CONCATENATION
        // =====================================================================
        System.out.println("=== 8. COST: MEMORY WASTE ===");

        // Each concatenation creates a new String object
        // Old objects become garbage, causing GC pressure

        String result = "";
        for (int i = 0; i < 5; i++) {
            String old = result;
            result = result + i; // Creates new String each iteration
            System.out.println("Iteration " + i + ": old=" + old.hashCode()
                    + " new=" + result.hashCode() + " (different objects)");
        }

        // Better approach: use StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i); // Modifies internal buffer, no new objects
        }
        String efficientResult = sb.toString();
        System.out.println("Efficient result: " + efficientResult);

        System.out.println();

        // =====================================================================
        // 9. COST: STRINGBUILDER/STRINGBUFFER OVERHEAD
        // =====================================================================
        System.out.println("=== 9. COST: MODIFICATION OVERHEAD ===");

        // For frequent modifications, you must use StringBuilder or StringBuffer
        // This adds complexity and requires awareness of the pattern

        // StringBuilder (not thread-safe, faster)
        StringBuilder sbDemo = new StringBuilder("Hello");
        sbDemo.append(" World");
        sbDemo.insert(5, ",");
        sbDemo.replace(7, 12, "Java");
        System.out.println("StringBuilder: " + sbDemo.toString());

        // StringBuffer (thread-safe, slower due to synchronization)
        StringBuffer buf = new StringBuffer("Hello");
        buf.append(" World");
        buf.insert(5, ",");
        buf.replace(7, 12, "Java");
        System.out.println("StringBuffer: " + buf.toString());

        System.out.println();

        // =====================================================================
        // 10. HOW TO CREATE IMMUTABLE CLASSES
        // =====================================================================
        System.out.println("=== 10. CREATING IMMUTABLE CLASSES ===");

        // Pattern 1: Final class with final fields
        final class Money {
            private final String currency;
            private final double amount;

            public Money(String currency, double amount) {
                this.currency = currency;
                this.amount = amount;
            }

            public String getCurrency() { return currency; }
            public double getAmount() { return amount; }

            // No setters — fields are final and cannot be changed
        }

        Money money = new Money("USD", 100.0);
        System.out.println("Money: " + money.getAmount() + " " + money.getCurrency());

        // Pattern 2: Defensive copies for mutable fields
        final class DateRange {
            private final List<String> dates;

            public DateRange(List<String> dates) {
                // Defensive copy — caller cannot modify our internal list
                this.dates = new ArrayList<>(dates);
            }

            public List<String> getDates() {
                // Return copy, not original — preserves immutability
                return new ArrayList<>(dates);
            }
        }

        List<String> dates = new ArrayList<>();
        dates.add("2024-01-01");
        dates.add("2024-12-31");

        DateRange range = new DateRange(dates);
        System.out.println("Date range: " + range.getDates());

        // Modifying original list doesn't affect DateRange
        dates.add("2025-01-01");
        System.out.println("Original dates modified, DateRange unchanged: " + range.getDates());

        System.out.println();

        // =====================================================================
        // 11. IMMUTABLE COLLECTIONS
        // =====================================================================
        System.out.println("=== 11. IMMUTABLE COLLECTIONS ===");

        // Collections.unmodifiableList creates an unmodifiable view
        // The underlying list can still change, but the view cannot
        List<String> mutableList = new ArrayList<>();
        mutableList.add("one");
        mutableList.add("two");
        mutableList.add("three");

        List<String> unmodifiableList = Collections.unmodifiableList(mutableList);
        System.out.println("Unmodifiable list: " + unmodifiableList);

        // Attempting to modify throws UnsupportedOperationException
        try {
            unmodifiableList.add("four");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list: " + e.getMessage());
        }

        // Note: This is NOT truly immutable — the underlying list can change
        mutableList.add("four"); // This works!
        System.out.println("Underlying list changed: " + unmodifiableList);

        // For truly immutable collections, use List.of() (Java 9+)
        List<String> trulyImmutable = List.of("a", "b", "c");
        try {
            trulyImmutable.add("d");
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of() is truly immutable: " + e.getMessage());
        }

        System.out.println();

        // =====================================================================
        // 12. PRACTICAL EXAMPLES
        // =====================================================================
        System.out.println("=== 12. PRACTICAL EXAMPLES ===");

        // Example 1: String concatenation in loops
        System.out.println("Building string with StringBuilder:");
        StringBuilder csv = new StringBuilder();
        String[] items = {"apple", "banana", "cherry", "date"};
        for (int i = 0; i < items.length; i++) {
            csv.append(items[i]);
            if (i < items.length - 1) {
                csv.append(", ");
            }
        }
        System.out.println("CSV: " + csv.toString());

        // Example 2: Immutability in collections
        System.out.println("\nString keys in HashMap:");
        java.util.Map<String, Integer> wordCount = new java.util.HashMap<>();
        String text = "hello world hello java world hello";
        for (String word : text.split(" ")) {
            wordCount.merge(word, 1, Integer::sum);
        }
        System.out.println("Word counts: " + wordCount);

        // Example 3: Method chaining (only works because of immutability)
        String formatted = "  Hello, World!  "
                .trim()
                .toLowerCase()
                .replace("world", "Java");
        System.out.println("Formatted: " + formatted);

        System.out.println();
        System.out.println("=== SUMMARY ===");
        System.out.println("1. String is immutable via final class + final byte[] value");
        System.out.println("2. Immutability provides: thread safety, security, caching, pool safety");
        System.out.println("3. Costs: memory waste, need StringBuilder for modifications");
        System.out.println("4. Create immutable classes: final class, final fields, no setters");
        System.out.println("5. Use List.of() (Java 9+) for truly immutable collections");
    }
}
