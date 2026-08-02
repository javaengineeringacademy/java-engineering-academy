package academy.javaengineering.devops;

import java.util.Map;

/**
 * Demonstrates DevOps pipeline configuration.
 */
public class DevOpsPipeline {

    public record PipelineStage(
        String name,
        String status,
        long durationMs,
        String logs
    ) {}

    public record PipelineConfig(
        String name,
        String repository,
        String branch,
        Map<String, String> environment
    ) {}

    public record BuildResult(
        boolean success,
        String buildId,
        PipelineStage[] stages,
        long totalDurationMs
    ) {}
}
