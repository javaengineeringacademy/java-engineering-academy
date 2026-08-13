package academy.javaengineering.oop.internals;

public class DependencyInternals {

    static class Printer {
        void print(String document) {
            System.out.println("Printing: " + document);
        }
    }

    static class Computer {
        // Dependency: Printer used temporarily
        void printDocument(String doc, Printer printer) {
            printer.print(doc); // Uses printer as dependency
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Dependency Internals ===\n");

        // 1. Dependency Relationship
        System.out.println("--- Dependency ---");
        Computer computer = new Computer();
        Printer printer = new Printer();
        computer.printDocument("Report", printer);
        System.out.println("Computer depends on Printer");

        // 2. Dependency vs Association
        System.out.println("\n--- Dependency vs Association ---");
        System.out.println("Dependency: temporary use (method param)");
        System.out.println("Association: long-term relationship");
        System.out.println("Dependency: weakest relationship");

        // 3. Types of Dependency
        System.out.println("\n--- Types ---");
        System.out.println("1. Method parameter");
        System.out.println("2. Local variable");
        System.out.println("3. Static method call");
        System.out.println("4. Create new instance");
    }
}
