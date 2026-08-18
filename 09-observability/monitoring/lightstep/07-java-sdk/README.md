# Java SDK Integration

## Overview

LightStep provides comprehensive Java SDK support through OpenTelemetry, enabling distributed tracing and metrics collection.

---

## Dependencies

### Maven

```xml
<properties>
    <opentelemetry.version>1.0.0</opentelemetry.version>
</properties>

<dependencies>
    <!-- OpenTelemetry API -->
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-api</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    
    <!-- OTLP gRPC Exporter -->
    <dependency>
        <groupId>io.opentelemetry.exporter</groupId>
        <artifactId>opentelemetry-exporter-otlp</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    
    <!-- SDK -->
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-sdk</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    
    <!-- Semantic Conventions -->
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-semconv</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
dependencies {
    implementation 'io.opentelemetry:opentelemetry-api:1.0.0'
    implementation 'io.opentelemetry.exporter:opentelemetry-exporter-otlp:1.0.0'
    implementation 'io.opentelemetry:opentelemetry-sdk:1.0.0'
    implementation 'io.opentelemetry:opentelemetry-semconv:1.0.0'
}
```

---

## SDK Initialization

### Basic Setup

```java
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;

public class LightStepConfig {
    
    public static OpenTelemetry initialize(String serviceName, String accessToken) {
        // Create resource
        Resource resource = Resource.getDefault().merge(
            Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), serviceName,
                AttributeKey.stringKey("service.version"), "1.0.0")
            )
        );
        
        // Create span exporter
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
            .setEndpoint("ingest.lightstep.com:443")
            .addHeader("lightstep-access-token", accessToken)
            .build();
        
        // Create tracer provider
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
            .setResource(resource)
            .build();
        
        // Create metric exporter
        OtlpGrpcMetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
            .setEndpoint("ingest.lightstep.com:443")
            .addHeader("lightstep-access-token", accessToken)
            .build();
        
        // Create meter provider
        SdkMeterProvider meterProvider = SdkMeterProvider.builder()
            .registerMetricReader(PeriodicMetricReader.builder(metricExporter).build())
            .setResource(resource)
            .build();
        
        // Build OpenTelemetry instance
        return OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .setMeterProvider(meterProvider)
            .buildAndRegisterGlobal();
    }
}
```

---

## Tracer Usage

### Creating Spans

```java
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;

public class UserService {
    
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("user-service");
    
    public User getUser(String userId) {
        // Create span
        Span span = tracer.spanBuilder("GET /api/users/{id}")
            .setAttribute("http.method", "GET")
            .setAttribute("http.url", "/api/users/" + userId)
            .setAttribute("user.id", userId)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Business logic
            User user = userRepository.findById(userId);
            
            span.setAttribute("user.found", user != null);
            span.setStatus(StatusCode.OK);
            
            return user;
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

### Nested Spans

```java
public Order createOrder(OrderRequest request) {
    Span parentSpan = tracer.spanBuilder("create-order").startSpan();
    
    try (Scope scope = parentSpan.makeCurrent()) {
        // Child span for validation
        Span validationSpan = tracer.spanBuilder("validate-order")
            .setAttribute("order.items", request.getItems().size())
            .startSpan();
        
        try (Scope validationScope = validationSpan.makeCurrent()) {
            validateOrder(request);
            validationSpan.setStatus(StatusCode.OK);
        } finally {
            validationSpan.end();
        }
        
        // Child span for processing
        Span processingSpan = tracer.spanBuilder("process-order")
            .startSpan();
        
        try (Scope processingScope = processingSpan.makeCurrent()) {
            Order order = processOrder(request);
            processingSpan.setAttribute("order.id", order.getId());
            processingSpan.setStatus(StatusCode.OK);
            return order;
        } finally {
            processingSpan.end();
        }
    } finally {
        parentSpan.end();
    }
}
```

---

## Meter Usage

### Creating Metrics

```java
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;

public class OrderService {
    
    private final Meter meter = GlobalOpenTelemetry.getMeter("order-service");
    
    private final LongCounter orderCounter = meter.counterBuilder("orders.total")
        .setDescription("Total orders placed")
        .build();
    
    private final DoubleHistogram orderValue = meter.histogramBuilder("orders.value")
        .setDescription("Order value distribution")
        .setUnit("USD")
        .ofDouble()
        .build();
    
    public Order createOrder(OrderRequest request) {
        Order order = processOrder(request);
        
        // Record metrics
        orderCounter.add(1, Attributes.of(
            AttributeKey.stringKey("order.type"), order.getType(),
            AttributeKey.stringKey("user.tier"), order.getUser().getTier()
        ));
        
        orderValue.record(order.getTotal(), Attributes.of(
            AttributeKey.stringKey("order.type"), order.getType(),
            AttributeKey.stringKey("currency"), order.getCurrency()
        ));
        
        return order;
    }
}
```

---

## Spring Boot Integration

### Auto-configuration

```java
@Configuration
public class LightStepConfig {
    
    @Bean
    public OpenTelemetry openTelemetry(
        @Value("${lightstep.access-token}") String accessToken,
        @Value("${lightstep.service.name}") String serviceName) {
        
        return LightStepConfig.initialize(serviceName, accessToken);
    }
    
    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("my-service");
    }
    
    @Bean
    public Meter meter(OpenTelemetry openTelemetry) {
        return openTelemetry.getMeter("my-service");
    }
}
```

### Service Usage

```java
@Service
public class UserService {
    
    private final Tracer tracer;
    private final Meter meter;
    private final LongCounter userCounter;
    
    public UserService(Tracer tracer, Meter meter) {
        this.tracer = tracer;
        this.meter = meter;
        
        this.userCounter = meter.counterBuilder("users.total")
            .setDescription("Total users")
            .build();
    }
    
    public User getUser(String userId) {
        Span span = tracer.spanBuilder("get-user")
            .setAttribute("user.id", userId)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            User user = userRepository.findById(userId);
            userCounter.add(1);
            return user;
        } finally {
            span.end();
        }
    }
}
```

---

## Agent Auto-instrumentation

### Setup

```bash
# Download agent
curl -L https://github.com/lightstep/opentelemetry-java/releases/latest/download/lightstep-opentelemetry-java-agent.jar -o lightstep-agent.jar

# Run with agent
java -javaagent:lightstep-agent.jar \
     -Dlightstep.access.token=YOUR_TOKEN \
     -Dlightstep.service.name=my-service \
     -jar my-application.jar
```

### Agent Configuration

```bash
# Environment variables
export LIGHTSTEP_ACCESS_TOKEN=your-token
export LIGHTSTEP_SERVICE_NAME=my-service
export LIGHTSTEP_COLLECTOR_HOST=ingest.lightstep.com
export LIGHTSTEP_COLLECTOR_PORT=443

# System properties
-Dlightstep.access.token=your-token
-Dlightstep.service.name=my-service
-Dlightstep.collector.host=ingest.lightstep.com
-Dlightstep.collector.port=443
```

---

## Custom Instrumentation

### HTTP Client

```java
@Component
public class TracingHttpClient {
    
    private final Tracer tracer;
    private final HttpClient httpClient;
    
    public TracingHttpClient(Tracer tracer) {
        this.tracer = tracer;
        this.httpClient = HttpClient.newHttpClient();
    }
    
    public String get(String url) {
        Span span = tracer.spanBuilder("HTTP GET")
            .setAttribute("http.method", "GET")
            .setAttribute("http.url", url)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Inject context into headers
            Map<String, String> headers = new HashMap<>();
            W3CTraceContextPropagator.getInstance()
                .inject(Context.current(), headers, Map::put);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(headers.entrySet().stream()
                    .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
                    .toArray(String[]::new))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString());
            
            span.setAttribute("http.status_code", response.statusCode());
            span.setStatus(StatusCode.OK);
            
            return response.body();
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw new RuntimeException(e);
        } finally {
            span.end();
        }
    }
}
```

### Database

```java
@Component
public class TracingUserRepository {
    
    private final Tracer tracer;
    private final JdbcTemplate jdbcTemplate;
    
    public User findById(String userId) {
        Span span = tracer.spanBuilder("DB SELECT users")
            .setAttribute("db.system", "postgresql")
            .setAttribute("db.statement", "SELECT * FROM users WHERE id = ?")
            .setAttribute("user.id", userId)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            User user = jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new Object[]{userId},
                new UserRowMapper()
            );
            
            span.setAttribute("user.found", user != null);
            span.setStatus(StatusCode.OK);
            
            return user;
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

---

## Best Practices

### Span Naming

```java
// Good - consistent, low cardinality
tracer.spanBuilder("HTTP GET /api/users/{id}")
tracer.spanBuilder("DB SELECT users")
tracer.spanBuilder("KAFKA PUBLISH user-events")

// Bad - high cardinality
tracer.spanBuilder("GET /api/users/12345")
tracer.spanBuilder("SELECT * FROM users WHERE id = 12345")
```

### Attributes

```java
// Good - useful for filtering
span.setAttribute("http.method", "GET");
span.setAttribute("http.status_code", 200);
span.setAttribute("user.id", userId);

// Bad - high cardinality
span.setAttribute("http.url", "/api/users/" + userId);
span.setAttribute("request.id", UUID.randomUUID().toString());
```

### Error Handling

```java
try {
    // Your code
} catch (BusinessException e) {
    span.setStatus(StatusCode.ERROR, "Business logic failed");
    span.recordException(e);
    throw e;
} catch (Exception e) {
    span.setStatus(StatusCode.ERROR, "Unexpected error");
    span.recordException(e);
    throw e;
} finally {
    span.end();
}
```

---

## Performance Optimization

### Batch Processing

```java
BatchSpanProcessor processor = BatchSpanProcessor.builder(exporter)
    .setScheduleDelay(1000)  // Export every 1 second
    .setMaxQueueSize(1024)   // Max spans in queue
    .setMaxExportBatchSize(512)  // Spans per batch
    .build();
```

### Sampling

```java
// Sample 10% of traces
TraceIdRatioBasedSampler sampler = TraceIdRatioBasedSampler.create(0.1);

// Use parent-based sampling for consistency
ParentBasedSampler parentBasedSampler = ParentBasedSampler.builder()
    .setRoot(TraceIdRatioBasedSampler.create(0.1))
    .build();
```

---

## Troubleshooting

### No Traces Appearing

- Check access token
- Verify network connectivity
- Check sampling rate
- Verify agent is loaded

### High Latency

- Increase batch size
- Reduce export frequency
- Use sampling
- Check network latency

### Memory Issues

- Reduce queue size
- Decrease export frequency
- Use sampling
- Monitor JVM metrics

---

## Next Steps

- [Alerting & Dashboards](../08-alerting-dashboards/) - Monitoring setup
- [Examples](../examples/) - Code examples
- [Practices](../practices/) - Hands-on exercises
