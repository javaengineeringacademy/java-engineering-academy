package com.javaacademy.sprint1.methods;

/**
 * OverloadingDemo - Demonstrates method overloading (same name, different parameters).
 *
 * <p><b>Method Overloading Rules:</b>
 * <ul>
 *   <li>Same method name</li>
 *   <li>Different parameter list (number, type, or order)</li>
 *   <li>Return type can be different</li>
 *   <li>Access modifier can be different</li>
 *   <li>Throws clause can be different</li>
 * </ul>
 *
 * <p><b>NOT Overloading:</b> Only changing return type - compile error!
 *
 * <p><b>Real-world analogy:</b> Like a Swiss Army knife -
 * same tool name "cut" but different blades for paper, wire, wood.
 *
 * <p><b>Best Practice:</b> Overload for same logical operation with different inputs.
 * Avoid overloading just to have same name - use clear names instead.
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class OverloadingDemo {

    private OverloadingDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Method Overloading ===\n");

        Calculator calc = new Calculator();

        // Overloaded by number of parameters
        System.out.println("--- By Number of Parameters ---");
        System.out.println("add(10, 20) = " + calc.add(10, 20));
        System.out.println("add(10, 20, 30) = " + calc.add(10, 20, 30));

        // Overloaded by parameter types
        System.out.println("\n--- By Parameter Types ---");
        System.out.println("add(10, 20) = " + calc.add(10, 20));         // int
        System.out.println("add(10.5, 20.3) = " + calc.add(10.5, 20.3)); // double
        System.out.println("add('A', 'B') = " + calc.add('A', 'B'));     // char

        // Overloaded by parameter order
        System.out.println("\n--- By Parameter Order ---");
        System.out.println("print(10, \"Hi\") = ");
        calc.print(10, "Hi");
        System.out.println("print(\"Hi\", 10) = ");
        calc.print("Hi", 10);

        // Varargs overloading
        System.out.println("\n--- Varargs Overloading ---");
        System.out.println("sum() = " + calc.sum());
        System.out.println("sum(1) = " + calc.sum(1));
        System.out.println("sum(1, 2, 3) = " + calc.sum(1, 2, 3));
        System.out.println("sum(new int[]{1,2,3}) = " + calc.sum(new int[]{1, 2, 3}));

        // Constructor overloading
        System.out.println("\n--- Constructor Overloading ---");
        Person p1 = new Person();
        Person p2 = new Person("Alice");
        Person p3 = new Person("Bob", 30);
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);

        // Ambiguity example (commented - would cause compile error)
        // System.out.println(calc.add(10, 20L)); // Ambiguous: int,int or int,long?

        // Expected output demonstrates all overloading scenarios
    }
}

/**
 * Calculator with overloaded methods.
 */
class Calculator {

    // Two int parameters
    int add(int a, int b) {
        return a + b;
    }

    // Three int parameters
    int add(int a, int b, int c) {
        return a + b + c;
    }

    // Two double parameters
    double add(double a, double b) {
        return a + b;
    }

    // Two char parameters (promoted to int)
    int add(char a, char b) {
        return a + b;
    }

    // Different parameter order
    void print(int num, String text) {
        System.out.println("Number: " + num + ", Text: " + text);
    }

    void print(String text, int num) {
        System.out.println("Text: " + text + ", Number: " + num);
    }

    // Varargs (variable arguments)
    int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) total += n;
        return total;
    }
}

/**
 * Person with overloaded constructors.
 */
class Person {
    String name;
    int age;

    // No-arg constructor
    Person() {
        this.name = "Unknown";
        this.age = 0;
    }

    // One parameter
    Person(String name) {
        this.name = name;
        this.age = 0;
    }

    // Two parameters
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}