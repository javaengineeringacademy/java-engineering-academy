import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * Demonstrates Enumeration (legacy) iteration patterns.
 */
public class EnumerationDemo {

    public static void main(String[] args) {
        basicEnumeration();
        vectorIteration();
        hashtableIteration();
        enumerationToIterator();
    }

    static void basicEnumeration() {
        System.out.println("=== Basic Enumeration ===");
        Vector<String> names = new Vector<>(List.of("Alice", "Bob", "Charlie"));
        Enumeration<String> e = names.elements();

        while (e.hasMoreElements()) {
            String name = e.nextElement();
            System.out.println("Name: " + name);
        }
        System.out.println();
    }

    static void vectorIteration() {
        System.out.println("=== Vector Iteration ===");
        Vector<Integer> numbers = new Vector<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);

        Enumeration<Integer> e = numbers.elements();
        int sum = 0;
        while (e.hasMoreElements()) {
            sum += e.nextElement();
        }
        System.out.println("Sum: " + sum);
        System.out.println();
    }

    static void hashtableIteration() {
        System.out.println("=== Hashtable Iteration ===");
        Hashtable<String, Integer> ages = new Hashtable<>();
        ages.put("Alice", 30);
        ages.put("Bob", 25);

        System.out.println("Keys:");
        Enumeration<String> keys = ages.keys();
        while (keys.hasMoreElements()) {
            System.out.println("  " + keys.nextElement());
        }

        System.out.println("Values:");
        Enumeration<Integer> values = ages.elements();
        while (values.hasMoreElements()) {
            System.out.println("  " + values.nextElement());
        }
        System.out.println();
    }

    static void enumerationToIterator() {
        System.out.println("=== Convert Enumeration to Iterator ===");
        Vector<String> vector = new Vector<>(List.of("X", "Y", "Z"));

        // Method 1: Collections.list()
        List<String> list = Collections.list(vector.elements());
        for (String s : list) {
            System.out.println("From iterator: " + s);
        }

        // Method 2: Manual conversion
        Enumeration<String> e = vector.elements();
        java.util.Iterator<String> it = new java.util.Iterator<String>() {
            public boolean hasNext() { return e.hasMoreElements(); }
            public String next() { return e.nextElement(); }
            public void remove() { throw new UnsupportedOperationException(); }
        };

        while (it.hasNext()) {
            System.out.println("Manual: " + it.next());
        }
        System.out.println();
    }
}
