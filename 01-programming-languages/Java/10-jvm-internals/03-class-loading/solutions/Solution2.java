package academy.javaengineering.jvm.classloading;

/**
 * Solution 2: Static Initialization Order
 *
 * Demonstrates the JVM rules for static initialization order,
 * including inheritance, interfaces, and circular dependencies.
 */
public class Solution2 {

    static {
        System.out.println("[INIT] Solution2 static block");
    }

    public static void main(String[] args) {
        System.out.println("=== Static Initialization Order ===\n");

        // Task 1: Simple inheritance order
        System.out.println("--- Task 1: Parent -> Child Order ---");
        System.out.println("Creating ChildClass (extends ParentClass)...");
        new ChildClassSolution();
        System.out.println("Order: ParentClass static -> ChildClass static -> constructor\n");

        // Task 2: Interface initialization
        System.out.println("--- Task 2: Interface Initialization ---");
        System.out.println("Accessing interface constant (primitive):");
        int val = MyInterfaceSolution.CONSTANT;
        System.out.println("  Value: " + val + " (interface NOT initialized)");

        // Task 3: Circular dependencies
        System.out.println("\n--- Task 3: Circular Dependencies ---");
        System.out.println("ClassA references ClassB, ClassB references ClassA");
        System.out.println("One will see the other's default value:");
        try {
            Class.forName("academy.javaengineering.jvm.classloading.ClassASolution");
            System.out.println("ClassA.aValue = " + ClassASolution.aValue);
            System.out.println("ClassB.bValue = " + ClassBSolution.bValue);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

class ParentClassSolution {
    static {
        System.out.println("[INIT] ParentClassSolution static block");
    }
}

class ChildClassSolution extends ParentClassSolution {
    static {
        System.out.println("[INIT] ChildClassSolution static block");
    }
}

interface MyInterfaceSolution {
    int CONSTANT = 100;
}

class ClassASolution {
    static int aValue = 10;
    static int bRef = ClassBSolution.bValue; // References ClassB
    static {
        System.out.println("[INIT] ClassA static block, aValue=" + aValue);
    }
}

class ClassBSolution {
    static int bValue = 20;
    static int aRef = ClassASolution.aValue; // References ClassA
    static {
        System.out.println("[INIT] ClassB static block, bValue=" + bValue);
    }
}
