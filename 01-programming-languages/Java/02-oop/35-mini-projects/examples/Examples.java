package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Mini Project: Simple Calculator ===\n");

        // Combines: encapsulation, inheritance, polymorphism, interfaces
        Calculator calc = new Calculator();
        System.out.println("2 + 3 = " + calc.compute(new Add(), 2, 3));
        System.out.println("10 - 4 = " + calc.compute(new Subtract(), 10, 4));
        System.out.println("6 * 7 = " + calc.compute(new Multiply(), 6, 7));

        // TRADE-OFF: Strategy pattern allows adding new operations without modifying Calculator
    }
}

class Calculator {
    public double compute(Operation op, double a, double b) {
        return op.apply(a, b);
    }
}

interface Operation {
    double apply(double a, double b);
}

class Add implements Operation {
    @Override public double apply(double a, double b) { return a + b; }
}

class Subtract implements Operation {
    @Override public double apply(double a, double b) { return a - b; }
}

class Multiply implements Operation {
    @Override public double apply(double a, double b) { return a * b; }
}
