package academy.javaengineering.cicd;

/**
 * Demonstrates CI/CD pipeline configuration.
 */
public class PipelineConfig {

    public record PipelineStage(
        String name,
        String command,
        int timeoutMinutes
    ) {}

    public static java.util.List<PipelineStage> getJavaPipelineStages() {
        return java.util.List.of(
            new PipelineStage("Checkout", "git checkout $BRANCH", 5),
            new PipelineStage("Build", "mvn clean compile", 15),
            new PipelineStage("Test", "mvn test", 30),
            new PipelineStage("Package", "mvn package -DskipTests", 10),
            new PipelineStage("Docker Build", "docker build -t app:$BUILD_NUMBER .", 15),
            new PipelineStage("Deploy", "kubectl apply -f k8s/", 10)
        );
    }

    public record QualityGate(
        String name,
        double threshold,
        boolean mandatory
    ) {}

    public static java.util.List<QualityGate> getQualityGates() {
        return java.util.List.of(
            new QualityGate("Code Coverage", 80.0, true),
            new QualityGate("Duplicate Code", 3.0, true),
            new QualityGate("Code Smells", 0.0, false),
            new QualityGate("Technical Debt", 5.0, false)
        );
    }
}
