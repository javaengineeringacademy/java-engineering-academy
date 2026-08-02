package academy.javaengineering.devops;

import java.util.List;

/**
 * Demonstrates CI/CD automation.
 */
public class CICDAutomation {

    public record TestResult(
        String testName,
        boolean passed,
        long durationMs,
        String errorMessage
    ) {}

    public record DeploymentInfo(
        String environment,
        String version,
        String status,
        String deployedBy
    ) {}

    public List<TestResult> runTests(String projectPath) {
        return List.of(
            new TestResult("test1", true, 100, null),
            new TestResult("test2", true, 150, null),
            new TestResult("test3", true, 200, null)
        );
    }

    public boolean buildProject(String projectPath) {
        System.out.println("Building project at: " + projectPath);
        return true;
    }

    public DeploymentInfo deploy(String environment, String version) {
        return new DeploymentInfo(environment, version, "SUCCESS", "system");
    }
}
