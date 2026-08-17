package academy.javaengineering.fundamentals.projectstructure;

/**
 * Demonstrates project structure internals in Java.
 */
public class ProjectStructureInternals {

    public static void main(String[] args) {
        System.out.println("=== Project Structure Internals Demo ===\n");

        // 1. Package access levels
        System.out.println("--- Package Access Levels ---");
        System.out.println("private: Only within the same class");
        System.out.println("package-private (default): Within the same package");
        System.out.println("protected: Subclasses + same package");
        System.out.println("public: Anywhere");

        // 2. Class loading
        System.out.println("\n--- Class Loading ---");
        System.out.println("Bootstrap classloader: JDK core classes");
        System.out.println("Platform classloader: JavaFX, etc.");
        System.out.println("Application classloader: Your code + dependencies");

        // 3. Resource loading
        System.out.println("\n--- Resource Loading ---");
        System.out.println("getClass().getResource('/file.txt')");
        System.out.println("getClass().getResourceAsStream('/config.properties')");

        // 4. Package structure example
        System.out.println("\n--- Package Structure ---");
        System.out.println("com.company.project.module.feature");
        System.out.println("  └── feature/");
        System.out.println("        ├── controller/");
        System.out.println("        ├── service/");
        System.out.println("        ├── repository/");
        System.out.println("        └── model/");

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
