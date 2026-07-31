package academy.javaengineering.oop.polymorphism;

/**
 * Calculator - Demonstrates compile-time polymorphism (method overloading).
 * 
 * <p><b>Method Overloading</b> allows multiple methods with the same name
 * but different parameter lists (type, number, or order).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Calculator {

    // Overloaded add methods - same name, different parameters
    public int add(int a, int b) {
        System.out.println("  Using add(int, int)");
        return a + b;
    }

    public double add(double a, double b) {
        System.out.println("  Using add(double, double)");
        return a + b;
    }

    public int add(int a, int b, int c) {
        System.out.println("  Using add(int, int, int)");
        return a + b + c;
    }

    public String add(String a, String b) {
        System.out.println("  Using add(String, String)");
        return a + b;
    }

    // Note: Return type alone is NOT enough for overloading
    // public double add(int a, int b) {} // WON'T COMPILE - same parameters

    // Overloaded subtract
    public int subtract(int a, int b) {
        return a - b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    // Overloaded multiply
    public int multiply(int a, int b) {
        return a * b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public int multiply(int a, int b, int c) {
        return a * b * c;
    }
}