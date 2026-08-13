package academy.javaengineering.generics.internals;

public class BoundedTypeInternals {

    interface Printable { void print(); }
    interface Drawable { void draw(); }

    static class Document implements Printable, Drawable {
        private String name;
        public Document(String name) { this.name = name; }
        public void print() { System.out.println("Printing: " + name); }
        public void draw() { System.out.println("Drawing: " + name); }
    }

    static <T extends Comparable<T> & Printable> void process(T item) {
        System.out.println("Processing: " + item);
        item.print();
    }

    public static void main(String[] args) {
        System.out.println("=== Bounded Type Parameters Internals ===\n");

        // 1. Upper Bounded (extends)
        System.out.println("--- Upper Bounded <T extends Number> ---");
        System.out.println("T must be Number or subclass");
        System.out.println("Allows: Integer, Double, Long, etc.");
        System.out.println("Forbids: String, Object, etc.");

        // 2. Multiple Bounds
        System.out.println("\n--- Multiple Bounds <T extends A & B> ---");
        Document doc = new Document("report.pdf");
        process(doc);
        System.out.println("T must implement BOTH interfaces");

        // 3. Class First Rule
        System.out.println("\n--- Class First Rule ---");
        System.out.println("<T extends Comparable<T>>");
        System.out.println("<T extends Comparable<T> & Serializable>");
        System.out.println("Class must come first, then interfaces");

        // 4. Wildcards vs Bounded
        System.out.println("\n--- Wildcards vs Bounded ---");
        System.out.println("<? extends Number> - wildcard");
        System.out.println("<T extends Number> - type parameter");
        System.out.println("Type parameter allows usage in method body");

        // 5. Recursive Bounds
        System.out.println("\n--- Recursive Bounds ---");
        System.out.println("<T extends Comparable<T>>");
        System.out.println("T must be comparable to itself");
        System.out.println("Used in sorting, searching algorithms");
    }
}
