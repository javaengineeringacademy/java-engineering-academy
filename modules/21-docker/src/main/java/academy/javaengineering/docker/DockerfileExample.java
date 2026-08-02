package academy.javaengineering.docker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class DockerfileExample {

    public static void main(String[] args) {
        System.out.println("=== Dockerfile Optimization Examples ===\n");
        
        demonstrateLayerOptimization();
        demonstrateMultiStageBuild();
        demonstrateCachingStrategy();
        demonstrateSecurityBestPractices();
    }

    public static void demonstrateLayerOptimization() {
        System.out.println("--- Layer Optimization ---");
        
        System.out.println("Bad approach (3 layers):");
        System.out.println("  RUN apt-get update");
        System.out.println("  RUN apt-get install -y curl");
        System.out.println("  RUN apt-get clean");
        
        System.out.println("\nGood approach (1 layer):");
        System.out.println("  RUN apt-get update && \\");
        System.out.println("      apt-get install -y --no-install-recommends curl && \\");
        System.out.println("      apt-get clean && \\");
        System.out.println("      rm -rf /var/lib/apt/lists/*");
        System.out.println();
    }

    public static void demonstrateMultiStageBuild() {
        System.out.println("--- Multi-Stage Build ---");
        
        System.out.println("Stage 1: Builder");
        System.out.println("  FROM eclipse-temurin:21-jdk AS builder");
        System.out.println("  WORKDIR /app");
        System.out.println("  COPY pom.xml .");
        System.out.println("  RUN mvn dependency:go-offline");
        System.out.println("  COPY src ./src");
        System.out.println("  RUN mvn package -DskipTests");
        
        System.out.println("\nStage 2: Runtime");
        System.out.println("  FROM eclipse-temurin:21-jre");
        System.out.println("  WORKDIR /app");
        System.out.println("  COPY --from=builder /app/target/*.jar app.jar");
        System.out.println("  ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]");
        System.out.println();
    }

    public static void demonstrateCachingStrategy() {
        System.out.println("--- Caching Strategy ---");
        
        Map<String, String> cacheableInstructions = Map.of(
            "RUN apt-get update && apt-get install...", "System packages",
            "COPY pom.xml .", "Dependencies",
            "RUN mvn dependency:go-offline", "Downloaded dependencies",
            "COPY src ./src", "Application source",
            "RUN mvn package", "Build artifacts"
        );
        
        cacheableInstructions.forEach((instruction, purpose) ->
            System.out.printf("  %-40s # %s%n", instruction, purpose)
        );
        System.out.println();
    }

    public static void demonstrateSecurityBestPractices() {
        System.out.println("--- Security Best Practices ---");
        
        String[] practices = {
            "1. Run as non-root user",
            "2. Use minimal base images (Alpine, distroless)",
            "3. Don't store secrets in image layers",
            "4. Use multi-stage builds to exclude build tools",
            "5. Scan images for vulnerabilities",
            "6. Use specific image tags, not 'latest'",
            "7. Set read-only filesystem where possible",
            "8. Use HEALTHCHECK for monitoring"
        };
        
        for (String practice : practices) {
            System.out.println("  " + practice);
        }
        System.out.println();
    }

    public static String generateOptimizedDockerfile() {
        return """
                # Stage 1: Builder
                FROM eclipse-temurin:21-jdk-jammy AS builder
                WORKDIR /workspace
                
                # Cache Maven wrapper
                COPY mvnw ./
                COPY .mvn .mvn
                RUN chmod +x mvnw
                
                # Cache dependencies
                COPY pom.xml ./
                RUN ./mvnw dependency:go-offline -B
                
                # Build application
                COPY src src
                RUN ./mvnw package -DskipTests -B
                
                # Stage 2: Runtime
                FROM eclipse-temurin:21-jre-jammy
                
                # Install minimal utilities
                RUN apt-get update && \\
                    apt-get install -y --no-install-recommends curl && \\
                    apt-get clean && \\
                    rm -rf /var/lib/apt/lists/*
                
                # Create non-root user
                RUN groupadd -r spring && useradd -r -g spring spring
                
                WORKDIR /app
                COPY --from=builder /workspace/target/*.jar app.jar
                RUN chown spring:spring app.jar
                USER spring
                
                EXPOSE 8080
                HEALTHCHECK --interval=30s --timeout=3s --retries=3 \\
                  CMD curl -f http://localhost:8080/actuator/health || exit 1
                
                ENTRYPOINT ["java", \\
                  "-XX:+UseContainerSupport", \\
                  "-XX:MaxRAMPercentage=75.0", \\
                  "-jar", "app.jar"]
                """;
    }
}
