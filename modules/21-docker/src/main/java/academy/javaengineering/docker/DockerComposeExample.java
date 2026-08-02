package academy.javaengineering.docker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class DockerComposeExample {

    public static void main(String[] args) {
        System.out.println("=== Docker Compose Examples ===\n");
        
        demonstrateBasicCompose();
        demonstrateServiceDependencies();
        demonstrateNetworkConfiguration();
        demonstrateVolumeManagement();
        demonstrateEnvironmentVariables();
    }

    public static void demonstrateBasicCompose() {
        System.out.println("--- Basic Compose Structure ---");
        
        String composeYaml = """
                version: '3.8'
                services:
                  web:
                    image: nginx:alpine
                    ports:
                      - "80:80"
                  app:
                    build: .
                    ports:
                      - "8080:8080"
                """;
        
        System.out.println(composeYaml);
    }

    public static void demonstrateServiceDependencies() {
        System.out.println("--- Service Dependencies ---");
        
        String dependencies = """
                services:
                  app:
                    depends_on:
                      db:
                        condition: service_healthy
                  db:
                    image: postgres:15
                    healthcheck:
                      test: ["CMD-SHELL", "pg_isready -U postgres"]
                      interval: 5s
                      timeout: 5s
                      retries: 5
                """;
        
        System.out.println(dependencies);
    }

    public static void demonstrateNetworkConfiguration() {
        System.out.println("--- Network Configuration ---");
        
        Map<String, String> networks = Map.of(
            "frontend", "User-facing services (nginx, app)",
            "backend", "Internal services (app, db)",
            "data", "Database and cache only"
        );
        
        networks.forEach((name, purpose) ->
            System.out.printf("  %-12s - %s%n", name, purpose)
        );
        System.out.println();
    }

    public static void demonstrateVolumeManagement() {
        System.out.println("--- Volume Types ---");
        
        Map<String, String> volumeTypes = Map.of(
            "Named Volume", "Docker-managed, persistent",
            "Bind Mount", "Host path, development use",
            "Tmpfs", "Memory-only, temporary data"
        );
        
        volumeTypes.forEach((type, description) ->
            System.out.printf("  %-15s - %s%n", type, description)
        );
        System.out.println();
    }

    public static void demonstrateEnvironmentVariables() {
        System.out.println("--- Environment Configuration ---");
        
        String envConfig = """
                services:
                  app:
                    environment:
                      - SPRING_PROFILES_ACTIVE=docker
                      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
                    env_file:
                      - .env
                      - .env.docker
                """;
        
        System.out.println(envConfig);
    }

    public static String generateFullStackCompose() {
        return """
                version: '3.8'
                
                services:
                  gateway:
                    image: traefik:v2.10
                    command:
                      - "--providers.docker=true"
                      - "--providers.docker.exposedbydefault=false"
                    ports:
                      - "80:80"
                      - "8080:8080"
                    volumes:
                      - /var/run/docker.sock:/var/run/docker.sock:ro
                    networks:
                      - web
                
                  app:
                    build: .
                    environment:
                      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery:8761/eureka
                    depends_on:
                      discovery:
                        condition: service_healthy
                    networks:
                      - web
                      - backend
                
                  discovery:
                    build: ./service-discovery
                    healthcheck:
                      test: ["CMD", "curl", "-f", "http://localhost:8761/actuator/health"]
                      interval: 10s
                      timeout: 5s
                      retries: 5
                    networks:
                      - backend
                
                  db:
                    image: postgres:15-alpine
                    environment:
                      POSTGRES_DB: mydb
                      POSTGRES_PASSWORD: secret
                    volumes:
                      - pgdata:/var/lib/postgresql/data
                    networks:
                      - backend
                
                  redis:
                    image: redis:7-alpine
                    networks:
                      - backend
                
                networks:
                  web:
                  backend:
                
                volumes:
                  pgdata:
                """;
    }
}
