package academy.javaengineering.jvm.classloading;

/**
 * Exercise 2: Static Initialization Order
 *
 * Task: Predict and verify the order of static initialization when
 * classes have inheritance and circular dependencies.
 */
public class Exercise2 {

    static {
        System.out.println("[INIT] Exercise2 static block");
    }

    public static void main(String[] args) {
        System.out.println("=== Static Initialization Order ===\n");

        // Task 1: Simple inheritance order
        System.out.println("--- Task 1: Parent -> Child Order ---");
        // TODO: Create instance of ChildClass and observe initialization order
        // new ChildClass();

        // Task 2: Interface initialization
        System.out.println("\n--- Task 2: Interface Initialization ---");
        // TODO: Access a constant from an interface (does NOT trigger initialization)
        // TODO: Access a non-constant static field from an interface (DOES trigger initialization)

        // Task 3: Circular dependencies
        System.out.println("\n--- Task 3: Circular Dependencies ---");
        // TODO: Predict what happens when ClassA's static block references ClassB
        // and ClassB's static block references ClassA

        System.out.println("\n[Complete the TODO sections above]");
    }
}

class ParentClass {
    static {
        System.out.println("[INIT] ParentClass static block");
    }
}

class ChildClass extends ParentClass {
    static {
        System.out.println("[INIT] ChildClass static block");
    }
}

interface MyInterface {
    int CONSTANT = 100; // Does NOT trigger interface initialization
    // int nonConstant = computeValue(); // Would trigger initialization
}
