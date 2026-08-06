import java.util.HashMap;
import java.util.Map;

/**
 * String Pool Deep Dive
 *
 * Complete coverage of Java String pool (string table) mechanics.
 *
 * Key concepts:
 * - Pool lives on heap (since Java 7; was in PermGen/Metaspace before)
 * - Literal strings are automatically interned
 * - new String() bypasses the pool
 * - intern() explicitly adds to pool and returns pool reference
 * - GC can remove pool entries when unreferenced
 */
public class StringPoolDeepDive {

    public static void main(String[] args) {

        // =====================================================================
        // 1. BASIC POOL MECHANICS: LITERALS vs new String()
        // =====================================================================
        System.out.println("=== 1. BASIC POOL MECHANICS ===");

        // Literals are automatically placed in the String pool
        String literal1 = "hello";
        String literal2 = "hello";

        // new String() creates a NEW object on the heap, NOT in the pool
        String newString1 = new String("hello");
        String newString2 = new String("hello");

        // Two literals with same content → same reference (pool lookup)
        System.out.println("literal1 == literal2: " + (literal1 == literal2)); // true

        // Two new String() with same content → different references
        System.out.println("newString1 == newString2: " + (newString1 == newString2)); // false

        // Literal vs new String() → different references
        System.out.println("literal1 == newString1: " + (literal1 == newString1)); // false

        // But .equals() compares content, not reference
        System.out.println("literal1.equals(newString1): " + literal1.equals(newString1)); // true

        System.out.println();

        // =====================================================================
        // 2. STRING.INTERN() - EXPLICITLY ADD TO POOL
        // =====================================================================
        System.out.println("=== 2. STRING.INTERN() ===");

        String notInPool = new String("world");
        String inPool = notInPool.intern(); // Adds "world" to pool, returns pool ref

        String literalWorld = "world";

        // intern() returns the pool reference, so it matches the literal
        System.out.println("inPool == literalWorld: " + (inPool == literalWorld)); // true
        // Original new String() is still separate
        System.out.println("notInPool == inPool: " + (notInPool == inPool)); // false

        // Calling intern() on a string already in the pool returns the same reference
        String alreadyInPool = "test";
        String sameRef = alreadyInPool.intern();
        System.out.println("alreadyInPool == sameRef: " + (alreadyInPool == sameRef)); // true

        System.out.println();

        // =====================================================================
        // 3. POOL MEMORY EFFICIENCY
        // =====================================================================
        System.out.println("=== 3. POOL MEMORY EFFICIENCY ===");

        // Without pool: 1000 String objects for "DATABASE_HOST" would use ~48KB
        // With pool: 1 reference + 1 String object = ~80 bytes
        // This is why databases, config keys, and enums benefit greatly

        String dbHostKey = "DATABASE_HOST";
        String configKey = "DATABASE_HOST";

        // Both references point to the same pool entry → memory saved
        System.out.println("dbHostKey == configKey: " + (dbHostKey == configKey)); // true

        // In real applications, this pattern is common:
        // - Database column names (SELECT id, name, email FROM users)
        // - Enum values (Status.ACTIVE.toString() == "ACTIVE")
        // - Configuration keys ("server.port", "spring.datasource.url")

        System.out.println();

        // =====================================================================
        // 4. POOL LOOKUP PERFORMANCE - O(1)
        // =====================================================================
        System.out.println("=== 4. POOL LOOKUP PERFORMANCE ===");

        // String pool uses a hash table internally
        // intern() performs a lookup by hash code → O(1) average case
        // This is why intern() is fast even with millions of strings

        long startTime = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            String temp = new String("test_" + (i % 100));
            temp.intern(); // O(1) lookup
        }
        long endTime = System.nanoTime();
        System.out.println("100K intern() calls: " + (endTime - startTime) / 1_000_000 + "ms");

        System.out.println();

        // =====================================================================
        // 5. GC BEHAVIOR - POOL ENTRIES CAN BE COLLECTED
        // =====================================================================
        System.out.println("=== 5. GC BEHAVIOR ===");

        // Since Java 7, String pool is on the heap
        // Pool entries are eligible for GC when no references exist
        // This means the pool can shrink over time

        for (int i = 0; i < 10; i++) {
            String temp = new String("temporary_" + i);
            temp.intern();
            // After this loop, if no references remain, GC can collect these
        }
        System.gc();
        System.out.println("GC invoked - pool entries without references are eligible");

        System.out.println();

        // =====================================================================
        // 6. CONCATENATION AND POOL INTERACTION
        // =====================================================================
        System.out.println("=== 6. CONCATENATION AND POOL ===");

        String a = "hello";
        String b = "hello";
        String c = a + b; // Compile-time constant? No, a is not a constant
        String d = "helo" + "lo"; // Both are compile-time constants → constant folding
        String e = "hell" + "o";

        // c is NOT in pool because a+b requires runtime computation
        System.out.println("c == \"hellohello\": " + (c == "hellohello")); // false

        // d and e are constant-folded at compile time
        System.out.println("d == e: " + (d == e)); // true
        System.out.println("d == \"hello\": " + (d == "hello")); // true

        // Final fields ARE compile-time constants
        final String x = "hel";
        final String y = "lo";
        String f = x + y; // Constant folded at compile time
        System.out.println("f == \"hello\": " + (f == "hello")); // true

        System.out.println();

        // =====================================================================
        // 7. COMPACT STRINGS (JAVA 9+) AND POOL
        // =====================================================================
        System.out.println("=== 7. COMPACT STRINGS (JAVA 9+) ===");

        // Java 9 introduced compact strings:
        // - Latin-1 strings use 1 byte per char (byte[] with coder=LATIN1)
        // - Other strings use 2 bytes per char (byte[] with coder=UTF16)
        // - This reduces memory for ASCII-heavy strings in the pool
        // - The pool itself uses hash-based lookup regardless of encoding

        String latin = "Hello World"; // 1 byte per char (LATIN1)
        String unicode = "Hello \u00C0\u00C1\u00C2"; // 2 bytes per char (UTF16)
        System.out.println("Latin-1 string: " + latin);
        System.out.println("Unicode string: " + unicode);

        System.out.println();

        // =====================================================================
        // 8. PRACTICAL REAL-WORLD EXAMPLES
        // =====================================================================
        System.out.println("=== 8. REAL-WORLD EXAMPLES ===");

        // Example 1: Database column names
        // Without pool, each query would create new String objects for column names
        // With pool, repeated column names share the same object
        String[] query1 = {"SELECT", "id", "name", "email", "FROM", "users"};
        String[] query2 = {"SELECT", "id", "name", "email", "FROM", "orders"};
        System.out.println("query1[1] == query2[1]: " + (query1[1] == query2[1])); // true

        // Example 2: Enum values
        enum Status { ACTIVE, INACTIVE, PENDING }
        Status s1 = Status.ACTIVE;
        Status s2 = Status.ACTIVE;
        System.out.println("Status.ACTIVE stored in enum, not pool (enum is not String)");

        // Example 3: Configuration keys
        Map<String, String> config = new HashMap<>();
        config.put("server.port", "8080");
        config.put("server.host", "localhost");
        String portKey = "server.port";
        System.out.println("Config key matches: " + config.containsKey(portKey)); // true

        // Example 4: Method references and constants
        class Constants {
            static final String API_VERSION = "v1";
            static final String BASE_URL = "https://api.example.com";
        }
        String apiRef = "v1";
        System.out.println("Constants.API_VERSION == apiRef: " + (Constants.API_VERSION == apiRef)); // true

        System.out.println();

        // =====================================================================
        // 9. COMMON PITFALLS
        // =====================================================================
        System.out.println("=== 9. COMMON PITFALLS ===");

        // Pitfall 1: Using == instead of .equals()
        String s1 = new String("test");
        String s2 = new String("test");
        System.out.println("s1 == s2: " + (s1 == s2)); // false - WRONG!
        System.out.println("s1.equals(s2): " + s1.equals(s2)); // true - CORRECT

        // Pitfall 2: StringBuilder.toString() is NOT in pool
        StringBuilder sb = new StringBuilder("hello");
        String fromBuilder = sb.toString();
        String literalHello = "hello";
        System.out.println("fromBuilder == literalHello: " + (fromBuilder == literalHello)); // false

        // Pitfall 3: Chars from char array are not in pool
        char[] chars = {'h', 'e', 'l', 'l', 'o'};
        String fromChars = new String(chars);
        System.out.println("fromChars == literalHello: " + (fromChars == literalHello)); // false

        System.out.println();

        // =====================================================================
        // 10. PERFORMANCE BENCHMARK: POOL vs NO POOL
        // =====================================================================
        System.out.println("=== 10. PERFORMANCE BENCHMARK ===");

        // Benchmark: repeated string creation
        int iterations = 10_000_000;
        long start, end;

        // Without pooling (using new String())
        start = System.nanoTime();
        String[] arr = new String[iterations];
        for (int i = 0; i < iterations; i++) {
            arr[i] = new String("test");
        }
        end = System.nanoTime();
        long noPoolTime = end - start;

        // With pooling (using literals)
        start = System.nanoTime();
        String[] arr2 = new String[iterations];
        for (int i = 0; i < iterations; i++) {
            arr2[i] = "test";
        }
        end = System.nanoTime();
        long poolTime = end - start;

        System.out.println("No pool (new String): " + noPoolTime / 1_000_000 + "ms");
        System.out.println("With pool (literal):  " + poolTime / 1_000_000 + "ms");
        System.out.println("Speedup: ~" + (noPoolTime / poolTime) + "x faster");

        System.out.println();
        System.out.println("=== SUMMARY ===");
        System.out.println("1. Literal strings are automatically in the pool");
        System.out.println("2. new String() creates heap objects outside the pool");
        System.out.println("3. intern() explicitly adds to pool, returns pool reference");
        System.out.println("4. Pool is on heap since Java 7, entries can be GC'd");
        System.out.println("5. Use .equals() for content comparison, not ==");
        System.out.println("6. Compact strings (Java 9+) reduce pool memory for ASCII");
    }
}
