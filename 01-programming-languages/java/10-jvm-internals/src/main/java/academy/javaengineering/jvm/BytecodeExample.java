package academy.javaengineering.jvm;

/**
 * Demonstrates Java bytecode concepts including compilation and class file format.
 *
 * <p>This class shows how Java source code is compiled to bytecode and explains
 * key bytecode instructions used by the JVM for execution.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Java bytecode instruction set</li>
 *   <li>Stack-based execution model</li>
 *   <li>Class file format structure</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class BytecodeExample {

    /**
     * Sample class demonstrating methods that generate different bytecode patterns.
     */
    public static class BytecodeGenerator {
        /**
         * Adds two integers.
         *
         * @param a the first operand
         * @param b the second operand
         * @return the sum
         */
        public int add(int a, int b) { return a + b; }

        /**
         * Concatenates two strings.
         *
         * @param a the first string
         * @param b the second string
         * @return the concatenated string
         */
        public String concatenate(String a, String b) { return a + b; }

        /**
         * Computes factorial recursively.
         *
         * @param n the input number
         * @return the factorial value
         */
        public int factorial(int n) {
            if (n <= 1) return 1;
            return n * factorial(n - 1);
        }
    }

    /**
     * Demonstrates bytecode concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Bytecode Demo ===");
        BytecodeGenerator generator = new BytecodeGenerator();
        System.out.println("Add: " + generator.add(5, 3));
        System.out.println("Concatenate: " + generator.concatenate("Hello", " World"));
        System.out.println("Factorial: " + generator.factorial(5));
        System.out.println("Bytecode Instructions:");
        System.out.println("  iconst_0: Push 0 onto stack");
        System.out.println("  iload_1: Load int from local variable 1");
        System.out.println("  iadd: Add two integers");
        System.out.println("  ireturn: Return integer");
    }
}
