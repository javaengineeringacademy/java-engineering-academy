package academy.javaengineering.bestpractices;

/**
 * Demonstrates Java best practices.
 */
public class JavaBestPractices {

    public static java.util.List<String> getBestPractices() {
        return java.util.List.of(
            "Use meaningful variable and method names",
            "Keep methods short and focused",
            "Use interfaces over implementations",
            "Prefer immutability",
            "Handle exceptions properly",
            "Use try-with-resources",
            "Write unit tests",
            "Follow SOLID principles"
        );
    }

    public record CodeReviewChecklist(
        String category,
        java.util.List<String> items
    ) {}

    public static java.util.List<CodeReviewChecklist> getCodeReviewChecklist() {
        return java.util.List.of(
            new CodeReviewChecklist("Code Quality", java.util.List.of(
                "No code duplication",
                "Meaningful naming",
                "Proper error handling"
            )),
            new CodeReviewChecklist("Performance", java.util.List.of(
                "No memory leaks",
                "Efficient algorithms",
                "Proper caching"
            )),
            new CodeReviewChecklist("Security", java.util.List.of(
                "Input validation",
                "SQL injection prevention",
                "XSS protection"
            ))
        );
    }
}
