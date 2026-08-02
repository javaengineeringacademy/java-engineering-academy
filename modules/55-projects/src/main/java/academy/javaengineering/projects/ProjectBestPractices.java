package academy.javaengineering.projects;

/**
 * Demonstrates project best practices.
 */
public class ProjectBestPractices {

    public static java.util.List<String> getCodingStandards() {
        return java.util.List.of(
            "Follow Google Java Style Guide",
            "Use meaningful variable names",
            "Write self-documenting code",
            "Keep methods short and focused",
            "Avoid magic numbers"
        );
    }

    public static java.util.List<String> getTestingStrategies() {
        return java.util.List.of(
            "Write unit tests for business logic",
            "Use integration tests for API endpoints",
            "Implement end-to-end tests for critical paths",
            "Mock external dependencies",
            "Aim for 80%+ code coverage"
        );
    }

    public static java.util.List<String> getGitWorkflow() {
        return java.util.List.of(
            "Use feature branches",
            "Write meaningful commit messages",
            "Review code before merging",
            "Keep main branch always deployable",
            "Use semantic versioning"
        );
    }
}
