# JVM in Kubernetes

Running Java applications in containerized environments.

## Container-Aware JVM

### UseContainerSupport (Default since Java 10)
```bash
# Enabled by default in Java 10+
# Detects container memory and CPU limits

# Verify container detection
java -XX:+PrintFlagsFinal -version | grep -i container
```

### Manual Configuration
```bash
# Disable auto-detection (not recommended)
java -XX:-UseContainerSupport -jar app.jar

# Force specific values
java -XX:ActiveProcessorCount=4 -jar app.jar
```

## Memory Configuration

### Memory Limits
```bash
# Set maximum heap size as percentage of container memory
-XX:MaxRAMPercentage=75.0

# Set minimum heap size
-XX:InitialRAMPercentage=50.0

# Set maximum metaspace size
-XX:MaxMetaspaceSize=256m

# Avoid setting -Xmx directly
# Use percentages for container flexibility
```

### Memory Formulas
```bash
# Container with 1GB memory
-XX:MaxRAMPercentage=75.0  # 750MB heap

# Container with 4GB memory
-XX:MaxRAMPercentage=75.0  # 3GB heap

# Leave room for:
# - Native memory
# - Metaspace
# - Thread stacks
# - Code cache
# - Direct byte buffers
```

### Memory Flags
```bash
# Heap
-XX:MaxRAMPercentage=75.0
-XX:InitialRAMPercentage=50.0

# Metaspace
-XX:MaxMetaspaceSize=256m
-XX:MetaspaceSize=128m

# Native memory
-XX:MaxDirectMemorySize=256m
-XX:ReservedCodeCacheSize=128m
```

## CPU Configuration

### CPU Limits
```bash
# Set number of processors
-XX:ActiveProcessorCount=4

# Use all available CPUs
-XX:ActiveProcessorCount=0

# Check detected CPUs
java -XX:+PrintFlagsFinal -version | grep ActiveProcessorCount
```

### Thread Pool Sizing
```bash
# Common formula
threads = 2 * CPU cores + 1

# For IO-bound applications
threads = 2 * CPU cores

# For CPU-bound applications
threads = CPU cores
```

## GC Selection for Containers

### Recommended GCs
```bash
# Low latency (containers)
-XX:+UseZGC
-XX:+ZGenerational

# Balanced (containers)
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200

# Throughput (containers)
-XX:+UseParallelGC
```

### GC Configuration
```bash
# G1GC
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=4m
-XX:InitiatingHeapOccupancyPercent=45

# ZGC
-XX:+UseZGC
-XX:+ZGenerational
-XX:SoftMaxHeapSize=2g

# Shenandoah
-XX:+UseShenandoahGC
-XX:ShenandoahGCHeuristics=compact
```

### GC Logging
```bash
# Java 11+
-Xlog:gc*:file=/var/log/gc.log:time,uptime,level,tags

# Java 17+
-Xlog:gc*,gc+age=trace,safepoint:file=/var/log/gc.log:time,uptime,level,tags
```

## Jib for Container Images

### Maven Configuration
```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.3.0</version>
    <configuration>
        <from>
            <image>eclipse-temurin:17-jre-alpine</image>
        </from>
        <to>
            <image>gcr.io/my-project/my-app</image>
            <tags>
                <tag>${project.version}</tag>
                <tag>latest</tag>
            </tags>
        </to>
        <container>
            <jvmFlags>
                <jvmFlag>-XX:MaxRAMPercentage=75.0</jvmFlag>
                <jvmFlag>-XX:+UseG1GC</jvmFlag>
                <jvmFlag>-Djava.security.egd=file:/dev/./urandom</jvmFlag>
            </jvmFlags>
            <mainClass>com.example.Main</mainClass>
            <ports>
                <port>8080</port>
            </ports>
            <environment>
                <SPRING_PROFILES_ACTIVE>production</SPRING_PROFILES_ACTIVE>
            </environment>
        </container>
    </configuration>
</plugin>
```

### Gradle Configuration
```groovy
plugins {
    id 'com.google.cloud.tools.jib' version '3.3.0'
}

jib {
    from {
        image = 'eclipse-temurin:17-jre-alpine'
    }
    to {
        image = 'gcr.io/my-project/my-app'
        tags = ['latest', project.version]
    }
    container {
        jvmFlags = ['-XX:MaxRAMPercentage=75.0', '-XX:+UseG1GC']
        mainClass = 'com.example.Main'
        ports = ['8080']
    }
}
```

### Jib Build Commands
```bash
# Build to local Docker daemon
mvn compile jib:dockerBuild

# Build to registry
mvn compile jib:build

# Build with tag
mvn compile jib:build -Djib.to.tags=latest
```

## Health Checks

### Liveness Probe
```yaml
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: app
    livenessProbe:
      httpGet:
        path: /actuator/health/liveness
        port: 8080
      initialDelaySeconds: 30
      periodSeconds: 10
      timeoutSeconds: 5
      failureThreshold: 3
```

### Readiness Probe
```yaml
readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 20
  periodSeconds: 10
  timeoutSeconds: 5
  failureThreshold: 3
```

### Spring Boot Actuator
```java
// Add dependency
// org.springframework.boot:spring-boot-starter-actuator

// Configuration
management.endpoints.web.exposure.include=health,info
management.endpoint.health.probes.enabled=true

// Custom health check
@Component
public class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        if (isHealthy()) {
            return Health.up().withDetail("status", "ok").build();
        }
        return Health.down().withDetail("status", "error").build();
    }
}
```

### JVM Kill Signals
```yaml
# Graceful shutdown
lifecycle:
  preStop:
    exec:
      command: ["sh", "-c", "sleep 15"]
```

```java
// Shutdown hook
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    // Cleanup logic
    context.close();
}));
```

## Container Resource Limits

### Kubernetes Resource Configuration
```yaml
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: app
    resources:
      requests:
        memory: "1Gi"
        cpu: "500m"
      limits:
        memory: "2Gi"
        cpu: "1000m"
```

### JVM Response to Limits
```bash
# Memory limits
- Container limit: 2GB
- JVM MaxRAMPercentage=75%
- Max heap: 1.5GB
- Remaining: 512MB for native memory

# CPU limits
- Container limit: 1 CPU
- JVM ActiveProcessorCount: 1
- G1GC threads: 1
- JIT compiler threads: 1
```

## Best Practices

### JVM Settings
```bash
# Recommended container JVM settings
java \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -Djava.security.egd=file:/dev/./urandom \
  -Dfile.encoding=UTF-8 \
  -jar app.jar
```

### Image Optimization
```bash
# Use JRE instead of JDK
FROM eclipse-temurin:17-jre-alpine

# Use multi-stage builds
FROM maven:3.8-openjdk-17 AS build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
```

### Monitoring
```bash
# Enable JMX
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9010
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false

# Enable JFR
-XX:StartFlightRecording=filename=recording.jfr
```
