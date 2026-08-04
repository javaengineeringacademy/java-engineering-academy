# Apache Camel with Quarkus

## Overview

Apache Camel on Quarkus provides fast startup, low memory footprint, and native compilation for cloud-native integration applications.

## Table of Contents

1. [Camel Quarkus Overview](#camel-quarkus-overview)
2. [Getting Started](#getting-started)
3. [Native Compilation](#native-compilation)
4. [Extensions](#extensions)
5. [Configuration](#configuration)
6. [Health and Metrics](#health-and-metrics)
7. [Deployment](#deployment)

## Camel Quarkus Overview

### Benefits

- Fast startup (< 1 second JVM, < 0.1 second native)
- Low memory consumption
- Native compilation with GraalVM
- Cloud-native ready
- Quarkus extensions for Camel components

### Architecture

```
┌─────────────────────────────────────────┐
│              Quarkus App                │
├─────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────────┐  │
│  │ Camel Route │  │ Quarkus CDI     │  │
│  └─────────────┘  └─────────────────┘  │
│  ┌─────────────┐  ┌─────────────────┐  │
│  │ Camel Ext   │  │ Quarkus Ext     │  │
│  └─────────────┘  └─────────────────┘  │
└─────────────────────────────────────────┘
```

## Getting Started

### Create Project

```bash
mvn quarkus:create \
  -DprojectGroupId=com.example \
  -DprojectArtifactId=camel-quarkus-app \
  -DcamelQuarkusVersion=3.6.0
```

### Maven Dependencies

```xml
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-core</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-timer</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-log</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-direct</artifactId>
</dependency>
```

### Define Route

```java
import org.apache.camel.builder.RouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:hello?period=5000")
            .log("Hello from Camel on Quarkus!")
            .setBody(constant("Hello World"))
            .to("log:out");
    }
}
```

### Run Application

```bash
# JVM mode
./mvnw quarkus:dev

# Native mode
./mvnw package -Pnative
./target/*-runner
```

## Native Compilation

### Build Native Image

```bash
# Install GraalVM
sdk install java 21.0.2-graal

# Build native
./mvnw package -Pnative

# Build with container support
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

### Native Configuration

```properties
# application.properties
quarkus.native.enabled=true
quarkus.native.resources.includes=*
quarkus.native.resources.excludes=META-INF/README*

# GraalVM options
quarkus.native.additional-build-args=--no-fallback,-H:+ReportExceptionStackTraces
```

### Native Limitations

- Dynamic class loading not supported
- Reflection requires configuration
- Resources must be included explicitly

## Extensions

### Available Extensions

| Extension | Description |
|-----------|-------------|
| camel-quarkus-core | Core Camel |
| camel-quarkus-timer | Timer component |
| camel-quarkus-log | Logging |
| camel-quarkus-direct | Direct invocation |
| camel-quarkus-http | HTTP client |
| camel-quarkus-jackson | JSON processing |
| camel-quarkus-jaxb | XML processing |
| camel-quarkus-microprofile-health | Health checks |

### Adding Extensions

```bash
# Add extension
./mvnw quarkus:add-extension -Dextensions="camel-http,camel-jackson"

# List extensions
./mvnw quarkus:list-extensions
```

## Configuration

### Application Properties

```properties
# application.properties
camel.component.timer.delay=1000
camel.component.file.directory=/data/input

# Quarkus settings
quarkus.http.port=8080
quarkus.log.level=INFO
```

### Environment Variables

```bash
export QUARKUS_HTTP_PORT=9090
export CAMEL_COMPONENT_TIMER_DELAY=2000
```

## Health and Metrics

### Health Checks

```java
import org.apache.camel.health.HealthCheck;
import org.apache.camel.health.HealthCheckRegistry;

@ApplicationScoped
public class RouteHealthCheck implements HealthCheck {
    @Override
    public boolean isUp() {
        return true;
    }
    
    @Override
    public String getComponentName() {
        return "my-route";
    }
}
```

### Expose Health Endpoint

```properties
# Health endpoint
quarkus.smallrye-health.enabled=true
quarkus.smallrye-health.ui.enabled=true

# Access: http://localhost:8080/q/health
```

### Metrics

```properties
# Metrics
quarkus.micrometer.enabled=true
quarkus.micrometer.export.prometheus.enabled=true

# Access: http://localhost:8080/q/metrics
```

## Deployment

### Container Image

```dockerfile
FROM quay.io/quarkus/quarkus-microprofile:latest
COPY target/*-runner /application
EXPOSE 8080
CMD ["/application", "-Dquarkus.http.host=0.0.0.0"]
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: camel-quarkus-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: camel-quarkus
  template:
    metadata:
      labels:
        app: camel-quarkus
    spec:
      containers:
      - name: app
        image: camel-quarkus-app:1.0
        ports:
        - containerPort: 8080
        readinessProbe:
          httpGet:
            path: /q/health/ready
            port: 8080
        livenessProbe:
          httpGet:
            path: /q/health/live
            port: 8080
```

## Best Practices

1. **Use CDI**: Leverage Quarkus CDI for dependency injection
2. **Native ready**: Design for native compilation
3. **Health checks**: Include health endpoints
4. **Configuration**: Use application.properties
5. **Testing**: Test with Quarkus Test
6. **Monitoring**: Enable metrics and tracing
7. **Containerization**: Build container images

## References

- [Camel Quarkus](https://camel.apache.org/camel-quarkus/)
- [Quarkus Extensions](https://quarkus.io/extensions/)
