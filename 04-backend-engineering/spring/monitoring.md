# Spring Monitoring

## Spring Boot Actuator

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Configuration

```properties
# Enable all endpoints
management.endpoints.web.exposure.include=*

# Enable specific endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus

# Disable specific endpoints
management.endpoints.web.exposure.exclude=env,beans

# Base path
management.endpoints.web.base-path=/actuator

# Health details
management.endpoint.health.show-details=always
```

### Common Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health |
| `/actuator/info` | Application info |
| `/actuator/metrics` | Application metrics |
| `/actuator/prometheus` | Prometheus metrics |
| `/actuator/env` | Environment properties |
| `/actuator/beans` | Application beans |
| `/actuator/configprops` | Configuration properties |
| `/actuator/mappings` | Request mappings |
| `/actuator/loggers` | Logger configuration |
| `/actuator/threaddump` | Thread dump |

## Micrometer

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### Custom Metrics

```java
@Component
public class CustomMetrics {
    
    private final Counter orderCounter;
    private final Timer orderTimer;
    private final Gauge queueGauge;
    
    public CustomMetrics(MeterRegistry meterRegistry) {
        this.orderCounter = Counter.builder("orders.created")
            .description("Number of orders created")
            .tag("type", "online")
            .register(meterRegistry);
        
        this.orderTimer = Timer.builder("orders.processing.time")
            .description("Time to process orders")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);
        
        this.queueGauge = Gauge.builder("orders.queue.size", this::getQueueSize)
            .description("Current queue size")
            .register(meterRegistry);
    }
    
    public void recordOrder() {
        orderCounter.increment();
    }
    
    public void recordProcessingTime(long duration) {
        orderTimer.record(duration, TimeUnit.MILLISECONDS);
    }
    
    private double getQueueSize() {
        return orderQueue.size();
    }
}
```

### Timer Usage

```java
@Service
public class OrderService {
    
    private final MeterRegistry meterRegistry;
    
    @Timed(value = "order.service.create", description = "Time to create order")
    public Order createOrder(OrderRequest request) {
        // Create order logic
        return order;
    }
    
    public Order processOrder(OrderRequest request) {
        return Timer.sample(meterRegistry, "order.processing", () -> {
            // Process order
            return processOrderInternal(request);
        });
    }
}
```

## Prometheus

### Configuration

```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'spring-boot-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
    scrape_interval: 10s
```

### Useful Queries

```promql
# Request rate
rate(http_server_requests_seconds_count[5m])

# Response time
histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))

# Error rate
rate(http_server_requests_seconds_count{status=~"5.."}[5m])

# JVM memory
jvm_memory_used_bytes{area="heap"}

# GC pause time
rate(jvm_gc_pause_seconds_sum[5m])

# Thread count
jvm_threads_live_threads
```

## Grafana Dashboard

### Import Spring Boot Dashboard

1. Open Grafana
2. Go to Dashboards > Import
3. Import dashboard ID: 12900 (Spring Boot Statistics)
4. Select Prometheus data source

### Key Dashboard Panels

- Request Rate
- Response Time (p50, p95, p99)
- Error Rate
- JVM Memory Usage
- GC Activity
- Thread Count
- HTTP Status Codes

## Custom Health Indicators

```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final DataSource dataSource;
    
    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            return Health.up()
                .withDetail("database", "accessible")
                .withDetail("connection", conn.isValid(5))
                .build();
        } catch (SQLException e) {
            return Health.down()
                .withDetail("database", e.getMessage())
                .build();
        }
    }
}

@Component
public class RedisHealthIndicator implements HealthIndicator {
    
    private final RedisConnectionFactory connectionFactory;
    
    @Override
    public Health health() {
        try {
            RedisConnection connection = connectionFactory.getConnection();
            String pong = connection.ping();
            connection.close();
            return Health.up()
                .withDetail("redis", pong)
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("redis", e.getMessage())
                .build();
        }
    }
}
```

## Info Contributors

```java
@Component
public class CustomInfoContributor implements InfoContributor {
    
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("appVersion", getAppVersion())
            .withDetail("buildTime", getBuildTime())
            .withDetail("javaVersion", System.getProperty("java.version"));
    }
}
```

## Metrics Export

### Export to Multiple Systems

```java
@Configuration
public class MetricsConfig {
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCustomizer() {
        return registry -> {
            registry.config()
                .commonTags("application", "my-spring-app")
                .commonTags("environment", "production");
        };
    }
}
```

### Export to Datadog

```properties
management.datadog.metrics.export.enabled=true
management.datadog.metrics.export.api-key=your-api-key
management.datadog.metrics.export.step=1m
```

### Export to New Relic

```properties
management.newrelic.metrics.export.enabled=true
management.newrelic.metrics.export.api-key=your-api-key
management.newrelic.metrics.export.account-id=your-account-id
```

## Logging

### Structured Logging

```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.pattern.json={"timestamp":"%d{yyyy-MM-dd HH:mm:ss}","level":"%level","logger":"%logger","thread":"%thread","message":"%msg"}
```

### Log Levels

```properties
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=DEBUG
```

## Monitoring Checklist

1. Enable Actuator endpoints
2. Configure Prometheus metrics
3. Set up Grafana dashboards
4. Create custom health indicators
5. Monitor JVM metrics
6. Track application-specific metrics
7. Set up alerting rules
8. Monitor log levels
9. Track HTTP request metrics
10. Monitor database connections
