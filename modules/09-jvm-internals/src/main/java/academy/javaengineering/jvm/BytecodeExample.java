package academy.javaengineering.jvm;

/**
 * Bytecode - Java Bytecode, Compilation, Class File Format.
 */
public class BytecodeExample {

    public static class BytecodeGenerator {
        public int add(int a, int b) {
            return a + b;
        }

        public String concatenate(String a, String b) {
            return a + b;
        }

        public int factorial(int n) {
            if (n <= 1) return 1;
            return n * factorial(n - 1);
        }
    }

    public static class BytecodeAnalyzer {
        public void analyze() {
            try {
                Class<?> clazz = BytecodeGenerator.class;
                System.out.println("Class: " + clazz.getName());
                System.out.println("Package: " + clazz.getPackageName());
                System.out.println("Methods: " + java.util.Arrays.toString(
                    Arrays.stream(clazz.getDeclaredMethods())
                        .map(m -> m.getName())
                        .toArray()
                ));
            } catch (Exception e) {
                System.out.println("Error analyzing bytecode: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Bytecode Demo ===");

        BytecodeGenerator generator = new BytecodeGenerator();
        System.out.println("Add: " + generator.add(5, 3));
        System.out.println("Concatenate: " + generator.concatenate("Hello", " World"));
        System.out.println("Factorial: " + generator.factorial(5));

        BytecodeAnalyzer analyzer = new BytecodeAnalyzer();
        analyzer.analyze();

        System.out.println("\n=== Bytecode Instructions ===");
        System.out.println("iconst_0: Push 0 onto stack");
        System.out.println("iload_1: Load int from local variable 1");
        System.out.println("iadd: Add two integers");
        System.out.println("ireturn: Return integer");
    }
}
