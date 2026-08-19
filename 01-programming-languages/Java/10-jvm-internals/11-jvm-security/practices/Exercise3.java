package academy.javaengineering.jvm.security;

/**
 * Exercise 3: Bytecode Verification Analysis
 *
 * Task: Analyze bytecode verification behavior and understand
 * what makes bytecode fail verification.
 */
public class Exercise3 {

    public static void main(String[] args) {
        System.out.println("=== Bytecode Verification Analysis ===\n");

        // TODO: Use javap -v to examine bytecode
        // TODO: Identify verification-relevant structures
        // TODO: Understand stack map frames

        System.out.println("Run: javap -v Exercise3.class");
        System.out.println("Look for:");
        System.out.println("- StackMapTable attribute");
        System.out.println("- Constant pool entries");
        System.out.println("- Method descriptors");
        System.out.println("- Access flags");
    }
}
