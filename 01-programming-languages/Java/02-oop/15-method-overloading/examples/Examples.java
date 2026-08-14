package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Method Overloading ===\n");

        // WHY: Overloading provides API flexibility with same conceptual operation
        // INTERNAL: Compile-time polymorphism. Compiler resolves via static type, not runtime type
        // ENGINEERING: Keep overloaded methods semantically consistent

        MathUtil math = new MathUtil();
        System.out.println("add(2,3) = " + math.add(2, 3));
        System.out.println("add(2.5,3.5) = " + math.add(2.5, 3.5));
        System.out.println("add(1,2,3) = " + math.add(1, 2, 3));
        System.out.println("add(\"Hello\",\" World\") = " + math.add("Hello", " World"));

        // TRADE-OFF: Overloading vs varargs vs separate methods
        // Overloading: type-safe, clear
        // Varargs: flexible but less safe
        // Separate methods: most explicit but verbose
    }
}

class MathUtil {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public String add(String a, String b) { return a + b; }

    // WARNING: Autoboxing can cause ambiguity
    // add(1, 2) -> int version
    // add(1, null) -> would NOT compile (ambiguous)
}
