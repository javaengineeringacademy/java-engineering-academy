# Java Production

> JVM production configuration, GC tuning, heap settings, and container JVM deployment.

## Production JVM Settings

### Container Deployment

```bash
# Dockerfile JVM configuration
FROM eclipse-temurin:21-jre-jammy

ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/tmp/heapdump.hprof \
    -XX:ActiveProcessorCount=4 \
    -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
```

### Production JVM Flags

```bash
# Memory
-Xms4g -Xmx4g                    # Fixed heap (avoid resize)
-XX:MaxMetaspaceSize=256m         # Limit metaspace
-XX:MaxDirectMemorySize=1g        # Limit direct buffers
-XX:+AlwaysPreTouch               # Pre-touch all pages

# GC (G1 recommended for most workloads)
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m
-XX:InitiatingHeapOccupancyPercent=45
-XX:G1ReservePercent=10
-XX:+ParallelRefProcEnabled

# JIT Compilation
-XX:+TieredCompilation
-XX:+AggressiveOpts
-XX:ReservedCodeCacheSize=256m

# Diagnostics
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/log/app/heapdump.hprof
-XX:+PrintFlagsFinal
-Xlog:gc*:file=/var/log/app/gc.log:time,uptime,level,tags:filecount=5,filesize=50m

# Security
-Djava.security.egd=file:/dev/./urandom
-Djdk.tls.acknowledgeCloseNotify=true
```

## GC Tuning by Workload

### Latency-Critical (API Gateway)

```bash
# ZGC for ultra-low latency
-XX:+UseZGC
-XX:+ZGenerational
-XX:SoftMaxHeapSize=4g
-XX:MaxHeapSize=8g

# OR G1 with aggressive tuning
-XX:+UseG1GC
-XX:MaxGCPauseMillis=50
-XX:G1NewSizePercent=30
-XX:G1MaxNewSizePercent=50
-XX:G1HeapRegionSize=8m
```

### Throughput (Batch Processing)

```bash
# Parallel GC for maximum throughput
-XX:+UseParallelGC
-XX:ParallelGCThreads=8
-XX:MaxGCPauseMillis=1000
-XX:GCTimeRatio=19

# OR G1 with larger heap
-XX:+UseG1GC
-XX:MaxGCPauseMillis=500
-XX:G1HeapRegionSize=32m
```

### Memory-Intensive (Caching)

```bash
# Large heap with G1
-XX:+UseG1GC
-XX:MaxHeapSize=32g
-XX:G1HeapRegionSize=32m
-XX:InitiatingHeapOccupancyPercent=35
-XX:G1ReservePercent=15

# ZGC for large heaps
-XX:+UseZGC
-XX:+ZGenerational
-XX:MaxHeapSize=64g
```

## Production Checklist

### JVM Configuration

- [ ] Heap size set (fixed min/max)
- [ ] GC algorithm selected for workload
- [ ] GC logging enabled
- [ ] Heap dump on OOM enabled
- [ ] JFR enabled for production monitoring
- [ ] Container support enabled

### Application Configuration

- [ ] Connection pool sized (database, HTTP)
- [ ] Thread pool configured
- [ ] Timeouts set (HTTP, database, cache)
- [ ] Retry logic implemented
- [ ] Circuit breakers configured
- [ ] Graceful shutdown enabled

### Monitoring

- [ ] Metrics endpoint exposed (Prometheus)
- [ ] Health checks configured
- [ ] JMX monitoring enabled
- [ ] Alerting rules defined
- [ ] Log aggregation configured
- [ ] Distributed tracing enabled

### Security

- [ ] HTTPS enforced
- [ ] Secrets not in code
- [ ] Dependency vulnerabilities scanned
- [ ] Security headers configured
- [ ] Rate limiting enabled

## Graceful Shutdown

```java
// Spring Boot graceful shutdown
server:
  shutdown: graceful

spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s

// Custom shutdown hook
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    logger.info("Shutting down...");
    executorService.shutdown();
    try {
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    } catch (InterruptedException e) {
        executorService.shutdownNow();
    }
    database.close();
    logger.info("Shutdown complete");
}));
```

## Docker Production

### Optimized Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre-jammy AS runtime

RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app
COPY --chown=appuser:appuser target/app.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+HeapDumpOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

USER appuser
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    deploy:
      resources:
        limits:
          memory: 4G
          cpus: '2'
        reservations:
          memory: 2G
          cpus: '1'
    environment:
      - JAVA_OPTS=-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0
      - SPRING_PROFILES_ACTIVE=prod
    ports:
      - "8080:8080"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

## Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-app
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: java-app
        image: myapp:1.0.0
        resources:
          requests:
            memory: "2Gi"
            cpu: "1000m"
          limits:
            memory: "4Gi"
            cpu: "2000m"
        env:
        - name: JAVA_OPTS
          value: "-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 5
```

## References

- [JVM Tuning Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/)
- [Container Java](https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html)
- [Spring Boot Production](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html)

---
**Prerequisites:** [Java performance](performance.md) | [Java configuration](configuration.md)
**Related:** [Java monitoring](monitoring.md) | [Java security](security.md)
**Next:** [Java scaling](scaling.md)
