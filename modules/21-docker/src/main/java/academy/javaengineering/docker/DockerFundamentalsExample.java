package academy.javaengineering.docker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DockerFundamentalsExample {

    public static void main(String[] args) {
        System.out.println("=== Docker Fundamentals Examples ===\n");
        
        demonstrateDockerCommands();
        demonstrateDockerfileSyntax();
        demonstrateContainerLifecycle();
        demonstrateImageManagement();
    }

    public static void demonstrateDockerCommands() {
        System.out.println("--- Docker CLI Commands ---");
        
        String[] commands = {
            "docker build -t myapp:latest .",
            "docker run -d -p 8080:8080 --name myapp myapp:latest",
            "docker ps",
            "docker ps -a",
            "docker logs myapp",
            "docker exec -it myapp /bin/bash",
            "docker stop myapp",
            "docker rm myapp",
            "docker images",
            "docker rmi myapp:latest",
            "docker system prune -a"
        };
        
        for (String command : commands) {
            System.out.println("  $ " + command);
        }
        System.out.println();
    }

    public static void demonstrateDockerfileSyntax() {
        System.out.println("--- Dockerfile Instructions ---");
        
        Map<String, String> instructions = Map.of(
            "FROM", "Base image - must be first instruction",
            "RUN", "Execute commands during build",
            "COPY", "Copy files from build context to image",
            "ADD", "Copy with URL/tar support",
            "CMD", "Default command (overridable)",
            "ENTRYPOINT", "Fixed executable (not overridable)",
            "ENV", "Set environment variables",
            "EXPOSE", "Document container port",
            "WORKDIR", "Set working directory",
            "USER", "Set user for subsequent instructions",
            "HEALTHCHECK", "Define health check command"
        );
        
        instructions.forEach((instruction, description) ->
            System.out.printf("  %-12s - %s%n", instruction, description)
        );
        System.out.println();
    }

    public static void demonstrateContainerLifecycle() {
        System.out.println("--- Container Lifecycle ---");
        
        String[] lifecycle = {
            "Created    - Container exists but not started",
            "Running    - Container is executing",
            "Paused     - Container processes frozen",
            "Stopped    - Container stopped gracefully",
            "Restarting - Container is restarting",
            "Removed    - Container deleted from system"
        };
        
        for (String state : lifecycle) {
            System.out.println("  " + state);
        }
        System.out.println();
    }

    public static void demonstrateImageManagement() {
        System.out.println("--- Docker Image Layers ---");
        
        String[] layers = {
            "Layer 1: Base image (FROM eclipse-temurin:21-jre)",
            "Layer 2: System packages (RUN apt-get install...)",
            "Layer 3: Application files (COPY target/app.jar)",
            "Layer 4: Configuration (ENV, EXPOSE, CMD)"
        };
        
        for (String layer : layers) {
            System.out.println("  " + layer);
        }
        System.out.println();
    }

    public static void generateDockerfile(Path outputPath) throws IOException {
        String dockerfile = """
                FROM eclipse-temurin:21-jre-jammy
                RUN groupadd -r app && useradd -r -g app app
                WORKDIR /app
                COPY target/*.jar app.jar
                RUN chown app:app app.jar
                USER app
                EXPOSE 8080
                HEALTHCHECK --interval=30s --timeout=3s \\
                  CMD curl -f http://localhost:8080/actuator/health || exit 1
                ENTRYPOINT ["java", \\
                  "-XX:+UseContainerSupport", \\
                  "-XX:MaxRAMPercentage=75.0", \\
                  "-jar", "app.jar"]
                """;
        
        Files.writeString(outputPath, dockerfile);
        System.out.println("Dockerfile generated: " + outputPath);
    }
}
