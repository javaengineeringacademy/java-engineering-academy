import java.util.*;

/**
 * Object Identity in Java
 * Understanding == vs equals() vs Objects.equals() and identity semantics.
 */
public class ObjectIdentity {

    public static void main(String[] args) {
        System.out.println("=== Object Identity in Java ===\n");

        equalityBasics();
        objectsEqualsDemo();
        identityHashCodeDemo();
        identityInCollections();
        stringPoolIdentity();
        whenIdentityMatters();
    }

    // --- == vs equals() ---
    static void equalityBasics() {
        System.out.println("--- == vs equals() ---");

        String s1 = new String("hello");
        String s2 = new String("hello");
        String s3 = s1;

        // == compares references (identity)
        System.out.println("s1 == s2: " + (s1 == s2));           // false
        System.out.println("s1 == s3: " + (s1 == s3));           // true

        // equals() compares content
        System.out.println("s1.equals(s2): " + s1.equals(s2));   // true
        System.out.println("s1.equals(s3): " + s1.equals(s3));   // true

        // Primitive == compares values directly
        int a = 5, b = 5;
        System.out.println("a == b: " + (a == b));               // true

        System.out.println();
    }

    // --- Objects.equals() null-safe comparison ---
    static void objectsEqualsDemo() {
        System.out.println("--- Objects.equals() null-safe ---");

        String s1 = null;
        String s2 = "hello";
        String s3 = null;

        // Objects.equals() handles null safely
        System.out.println("Objects.equals(null, \"hello\"): " +
            Objects.equals(s1, s2));  // false
        System.out.println("Objects.equals(null, null): " +
            Objects.equals(s1, s3));  // true

        // Equivalent null check without Objects.equals
        boolean safeEquals = (s1 == null) ? (s2 == null) : s1.equals(s2);
        System.out.println("Manual null-safe: " + safeEquals);

        System.out.println();
    }

    // --- Identity hashCode vs content hashCode ---
    static void identityHashCodeDemo() {
        System.out.println("--- hashCode Identity vs Content ---");

        String s1 = new String("hello");
        String s2 = new String("hello");

        System.out.println("s1 content hashCode: " + s1.hashCode());
        System.out.println("s2 content hashCode: " + s2.hashCode());
        System.out.println("Same content hash: " +
            (s1.hashCode() == s2.hashCode()));  // true

        // System.identityHashCode() - unique per object lifetime
        System.out.println("s1 identity hashCode: " +
            System.identityHashCode(s1));
        System.out.println("s2 identity hashCode: " +
            System.identityHashCode(s2));
        System.out.println("Different identity hash: " +
            (System.identityHashCode(s1) != System.identityHashCode(s2)));

        // Custom class with identity-based hashCode
        Object obj1 = new Object();
        Object obj2 = new Object();
        System.out.println("obj1 identity: " + System.identityHashCode(obj1));
        System.out.println("obj2 identity: " + System.identityHashCode(obj2));

        System.out.println();
    }

    // --- Identity matters in collections ---
    static void identityInCollections() {
        System.out.println("--- Identity in Collections ---");

        // HashSet uses equals() and hashCode()
        Set<String> set = new HashSet<>();
        set.add(new String("hello"));
        set.add(new String("hello"));
        System.out.println("HashSet size (content equals): " + set.size()); // 1

        // IdentityHashMap uses == for keys
        IdentityHashMap<String, String> identityMap = new IdentityHashMap<>();
        identityMap.put(new String("key1"), "val1");
        identityMap.put(new String("key1"), "val2");
        System.out.println("IdentityHashMap size: " + identityMap.size()); // 2

        // Regular HashMap uses equals()
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(new String("key1"), "val1");
        hashMap.put(new String("key1"), "val2");
        System.out.println("HashMap size (content equals): " + hashMap.size()); // 1

        System.out.println();
    }

    // --- String pool identity ---
    static void stringPoolIdentity() {
        System.out.println("--- String Pool Identity ---");

        String a = "hello";
        String b = "hello";
        String c = new String("hello");

        // String literals share same object from pool
        System.out.println("a == b (pool): " + (a == b));         // true
        System.out.println("a == c (new): " + (a == c));         // false

        // Intern returns pool reference
        String d = c.intern();
        System.out.println("a == d (intern): " + (a == d));     // true

        // Integer cache: -128 to 127
        Integer x = 127;
        Integer y = 127;
        System.out.println("127 == 127: " + (x == y));           // true

        Integer m = 128;
        Integer n = 128;
        System.out.println("128 == 128: " + (m == n));          // false

        System.out.println();
    }

    // --- When identity matters ---
    static void whenIdentityMatters() {
        System.out.println("--- When Identity Matters ---");

        // 1. Lock objects - must use same instance
        Object lock1 = new Object();
        Object lock2 = new Object();
        // synchronized(lock1) and synchronized(lock2) are independent!

        // 2. Identity-based caches
        IdentityHashMap<Object, Long> creationTime = new IdentityHashMap<>();
        Object obj = new Object();
        creationTime.put(obj, System.nanoTime());

        // 3. Sentinel objects
        Object SENTINEL = new Object();
        Object userObj = SENTINEL;
        if (userObj == SENTINEL) {
            System.out.println("Detected sentinel object by identity");
        }

        // 4. Enum singletons
        Thread.State state = Thread.State.RUNNABLE;
        Thread.State state2 = Thread.State.RUNNABLE;
        System.out.println("Enum identity: " + (state == state2)); // true

        // 5. Default methods in collections
        List<String> list = new ArrayList<>(Arrays.asList("a", "b"));
        System.out.println("list identity hashCode: " +
            System.identityHashCode(list));

        System.out.println("\n=== Complete ===");
    }
}
