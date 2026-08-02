package academy.javaengineering.bestpractices;

/**
 * Demonstrates design best practices.
 */
public class DesignBestPractices {

    public static java.util.List<String> getDesignPrinciples() {
        return java.util.List.of(
            "Single Responsibility Principle",
            "Open/Closed Principle",
            "Liskov Substitution Principle",
            "Interface Segregation Principle",
            "Dependency Inversion Principle",
            "DRY (Don't Repeat Yourself)",
            "KISS (Keep It Simple, Stupid)",
            "YAGNI (You Aren't Gonna Need It)"
        );
    }

    public static java.util.List<String> getNamingConventions() {
        return java.util.List.of(
            "Classes: PascalCase (UserService)",
            "Methods: camelCase (getUserById)",
            "Variables: camelCase (userName)",
            "Constants: UPPER_SNAKE_CASE (MAX_SIZE)",
            "Packages: lowercase (com.example.myapp)"
        );
    }
}
