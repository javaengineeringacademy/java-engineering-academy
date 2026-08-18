# LightStep Setup & Configuration

## Installation Methods

### 1. Java Agent (Auto-instrumentation)

#### Download Agent

```bash
# Latest version
curl -L https://github.com/lightstep/opentelemetry-java/releases/latest/download/lightstep-opentelemetry-java-agent.jar -o lightstep-agent.jar

# Specific version
curl -L https://github.com/lightstep/opentelemetry-java/releases/download/v1.0.0/lightstep-opentelemetry-java-agent.jar -o lightstep-agent.jar
```

#### Run with Agent

```bash
# Basic usage
java -javaagent:lightstep-agent.jar \
     -Dlightstep.access.token=YOUR_TOKEN \
     -jar my-application.jar

# With additional options
java -javaagent:lightstep-agent.jar \
     -Dlightstep.access.token=YOUR_TOKEN \
     -Dlightstep.service.name=my-service \
     -Dlightstep.collector.host=ingest.lightstep.com \
     -Dlightstep.collector.port=443 \
     -jar my-application.jar
```

---

## Environment Variables

### Core Configuration

```bash
# Required
export LIGHTSTEP_ACCESS_TOKEN="your-access-token"
export LIGHTSTEP_SERVICE_NAME="my-service"

# Optional - defaults shown
export LIGHTSTEP_COLLECTOR_HOST="ingest.lightstep.com"
export LIGHTSTEP_COLLECTOR_PORT=443
export LIGHTSTEP_COLLECTOR_PROTOCOL="grpc"
```

### Tracing Configuration

```bash
# Enable/disable tracing
export LIGHTSTEP_TRACES_ENABLED=true

# Sampling rate (0.0 to 1.0)
export LIGHTSTEP_TRACE_SAMPLE_RATE=0.1

# Maximum buffered spans
export LIGHTSTEP_MAX_BUFFERED_SPANS=1000

# Export interval in milliseconds
export LIGHTSTEP_EXPORT_INTERVAL_MS=10000
```

### Metrics Configuration

```bash
# Enable/disable metrics
export LIGHTSTEP_METRICS_ENABLED=true

# Metrics export interval
export LIGHTSTEP_METRICS_EXPORT_INTERVAL=60000

# Custom metric prefix
export LIGHTSTEP_METRIC_PREFIX="myapp"
```

---

## System Properties

### Java System Properties

```bash
# Core
-Dlightstep.access.token=my-token
-Dlightstep.service.name=my-service
-Dlightstep.collector.host=ingest.lightstep.com
-Dlightstep.collector.port=443

# Tracing
-Dlightstep.traces.enabled=true
-Dlightstep.trace.sample.rate=0.1

# Metrics
-Dlightstep.metrics.enabled=true
-Dlightstep.metrics.export.interval=60000
```

---

## Configuration Files

### application.properties (Spring Boot)

```properties
# LightStep configuration
lightstep.access-token=${LIGHTSTEP_ACCESS_TOKEN}
lightstep.service-name=my-service
lightstep.collector.host=ingest.lightstep.com
lightstep.collector.port=443

# Tracing
lightstep.traces.enabled=true
lightstep.trace.sample-rate=0.1

# Metrics
lightstep.metrics.enabled=true
lightstep.metrics.export-interval=60000
```

### application.yml (Spring Boot)

```yaml
lightstep:
  access-token: ${LIGHTSTEP_ACCESS_TOKEN}
  service-name: my-service
  collector:
    host: ingest.lightstep.com
    port: 443
  traces:
    enabled: true
    sample-rate: 0.1
  metrics:
    enabled: true
    export-interval: 60000
```

---

## Maven Configuration

### Dependencies

```xml
<dependencies>
    <!-- OpenTelemetry API -->
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-api</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    <!-- OTLP gRPC Exporter -->
    <dependency>
        <groupId>io.opentelemetry.exporter</groupId>
        <artifactId>opentelemetry-exporter-otlp</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    <!-- SDK -->
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Plugin Configuration

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <jvmArguments>
                    -javaagent:lightstep-agent.jar
                    -Dlightstep.access.token=${LIGHTSTEP_ACCESS_TOKEN}
                </jvmArguments>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## Gradle Configuration

### Dependencies

```groovy
dependencies {
    implementation 'io.opentelemetry:opentelemetry-api:1.0.0'
    implementation 'io.opentelemetry.exporter:opentelemetry-exporter-otlp:1.0.0'
    implementation 'io.opentelemetry:opentelemetry-sdk:1.0.0'
}
```

### Task Configuration

```groovy
task runWithAgent(type: Exec) {
    commandLine 'java', 
        '-javaagent:lightstep-agent.jar',
        '-Dlightstep.access.token=' + System.getenv('LIGHTSTEP_ACCESS_TOKEN'),
        '-jar', 'build/libs/my-application.jar'
}
```

---

## Docker Configuration

### Dockerfile

```dockerfile
FROM openjdk:17-jre-slim

# Download agent
RUN curl -L https://github.com/lightstep/opentelemetry-java/releases/latest/download/lightstep-opentelemetry-java-agent.jar -o /app/lightstep-agent.jar

WORKDIR /app
COPY target/my-application.jar .

# Run with agent
ENTRYPOINT ["java", \
    "-javaagent:/app/lightstep-agent.jar", \
    "-Dlightstep.access.token=${LIGHTSTEP_ACCESS_TOKEN}", \
    "-Dlightstep.service.name=${LIGHTSTEP_SERVICE_NAME}", \
    "-jar", "my-application.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  my-service:
    build: .
    environment:
      - LIGHTSTEP_ACCESS_TOKEN=${LIGHTSTEP_ACCESS_TOKEN}
      - LIGHTSTEP_SERVICE_NAME=my-service
      - LIGHTSTEP_COLLECTOR_HOST=ingest.lightstep.com
    ports:
      - "8080:8080"
```

---

## Kubernetes Configuration

### Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-service
  template:
    metadata:
      labels:
        app: my-service
    spec:
      containers:
      - name: my-service
        image: my-service:latest
        env:
        - name: LIGHTSTEP_ACCESS_TOKEN
          valueFrom:
            secretKeyRef:
              name: lightstep-secrets
              key: access-token
        - name: LIGHTSTEP_SERVICE_NAME
          value: my-service
        - name: LIGHTSTEP_COLLECTOR_HOST
          value: ingest.lightstep.com
```

### Secret

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: lightstep-secrets
type: Opaque
data:
  access-token: <base64-encoded-token>
```

---

## Configuration Validation

### Test Connection

```bash
# Test collector connectivity
curl -v https://ingest.lightstep.com:443

# Verify token
curl -H "Authorization: Bearer ${LIGHTSTEP_ACCESS_TOKEN}" \
     https://api.lightstep.com/api/v2/organizations
```

### Debug Mode

```bash
# Enable debug logging
java -javaagent:lightstep-agent.jar \
     -Dlightstep.access.token=YOUR_TOKEN \
     -Dlightstep.debug=true \
     -jar my-application.jar
```

---

## Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| Connection refused | Check network/firewall settings |
| Authentication failed | Verify access token |
| No traces appearing | Check sampling rate |
| High latency | Increase batch size |

### Debug Commands

```bash
# Check agent logs
java -javaagent:lightstep-agent.jar \
     -Dlightstep.access.token=YOUR_TOKEN \
     -Dlightstep.log.level=DEBUG \
     -jar my-application.jar

# Verify span export
java -jar lightstep-diagnostic.jar
```

---

## Best Practices

1. **Use Environment Variables**: Never hardcode tokens
2. **Enable Sampling**: Reduce costs in development
3. **Set Service Names**: Use descriptive, consistent names
4. **Monitor Agent Health**: Check agent metrics regularly
5. **Use Auto-instrumentation**: When possible, use the agent

---

## Next Steps

- [Tracing](../03-tracing/) - Distributed tracing implementation
- [Metrics](../04-metrics/) - Metrics collection
- [Sampling](../05-sampling/) - Sampling strategies
