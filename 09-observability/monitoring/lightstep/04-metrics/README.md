# Metrics with LightStep

## Overview

LightStep collects and analyzes metrics alongside traces, providing a comprehensive view of application performance.

---

## Metric Types

### Counter

A monotonically increasing value:

```java
import io.opentelemetry.api.metrics.LongCounter;

LongCounter counter = meter.counterBuilder("http.requests")
    .setDescription("Total HTTP requests")
    .build();

// Increment by 1
counter.add(1);

// Increment with attributes
counter.add(1, Attributes.of(
    AttributeKey.stringKey("method"), "GET",
    AttributeKey.stringKey("path"), "/api/users"
));
```

### Gauge

A value that can go up and down:

```java
import io.opentelemetry.api.metrics.LongGauge;

LongGauge gauge = meter.gaugeBuilder("system.memory.usage")
    .setDescription("Current memory usage")
    .ofLongs()
    .build();

gauge.set(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
```

### Histogram

A distribution of values:

```java
import io.opentelemetry.api.metrics.DoubleHistogram;

DoubleHistogram histogram = meter.histogramBuilder("http.duration")
    .setDescription("HTTP request duration")
    .setUnit("ms")
    .ofDouble()
    .build();

histogram.record(125.5, Attributes.of(
    AttributeKey.stringKey("method"), "GET",
    AttributeKey.stringKey("path"), "/api/users"
));
```

---

## Standard Metrics

### HTTP Metrics

```java
// Request count
meter.counterBuilder("http.server.requests")
    .setDescription("Total HTTP requests")
    .build();

// Request duration
meter.histogramBuilder("http.server.duration")
    .setDescription("HTTP request duration")
    .setUnit("ms")
    .build();

// Request size
meter.histogramBuilder("http.server.request.size")
    .setDescription("HTTP request size")
    .setUnit("bytes")
    .build();

// Response size
meter.histogramBuilder("http.server.response.size")
    .setDescription("HTTP response size")
    .setUnit("bytes")
    .build();
```

### Database Metrics

```java
// Connection pool
meter.gaugeBuilder("db.connections.active")
    .setDescription("Active database connections")
    .ofLongs()
    .build();

// Query duration
meter.histogramBuilder("db.query.duration")
    .setDescription("Database query duration")
    .setUnit("ms")
    .build();

// Query count
meter.counterBuilder("db.queries")
    .setDescription("Total database queries")
    .build();
```

---

## Custom Metrics

### Business Metrics

```java
// Order metrics
LongCounter orderCounter = meter.counterBuilder("orders.total")
    .setDescription("Total orders placed")
    .build();

DoubleHistogram orderValue = meter.histogramBuilder("orders.value")
    .setDescription("Order value distribution")
    .setUnit("USD")
    .ofDouble()
    .build();

// User metrics
LongCounter activeUsers = meter.counterBuilder("users.active")
    .setDescription("Active users")
    .build();
```

### System Metrics

```java
// Memory usage
LongGauge memoryUsage = meter.gaugeBuilder("jvm.memory.used")
    .setDescription("JVM memory usage")
    .ofLongs()
    .build();

// Thread count
LongGauge threadCount = meter.gaugeBuilder("jvm.threads.count")
    .setDescription("JVM thread count")
    .ofLongs()
    .build();

// GC metrics
LongCounter gcCount = meter.counterBuilder("jvm.gc.collections")
    .setDescription("GC collection count")
    .build();
```

---

## Metric Attributes

### Standard Attributes

```java
// HTTP attributes
Attributes httpAttributes = Attributes.of(
    AttributeKey.stringKey("http.method"), "GET",
    AttributeKey.stringKey("http.url"), "/api/users",
    AttributeKey.longKey("http.status_code"), 200
);

// Database attributes
Attributes dbAttributes = Attributes.of(
    AttributeKey.stringKey("db.system"), "postgresql",
    AttributeKey.stringKey("db.statement"), "SELECT * FROM users"
);

// Custom attributes
Attributes customAttributes = Attributes.of(
    AttributeKey.stringKey("user.tier"), "premium",
    AttributeKey.stringKey("user.region"), "us-east-1"
);
```

### Attribute Best Practices

```java
// Good - low cardinality
AttributeKey.stringKey("http.method")  // GET, POST, PUT
AttributeKey.longKey("http.status_code")  // 200, 404, 500
AttributeKey.stringKey("user.tier")  // free, premium, enterprise

// Bad - high cardinality
AttributeKey.stringKey("http.url")  // /api/users/12345
AttributeKey.stringKey("user.id")  // unique per user
AttributeKey.stringKey("request.id")  // unique per request
```

---

## Metric Collection Patterns

### Request Metrics

```java
public Response handleRequest(Request request) {
    long startTime = System.currentTimeMillis();
    
    try {
        Response response = processRequest(request);
        
        // Record success metrics
        requestCounter.add(1, Attributes.of(
            AttributeKey.stringKey("status"), "success",
            AttributeKey.stringKey("method"), request.getMethod()
        ));
        
        durationHistogram.record(
            System.currentTimeMillis() - startTime,
            Attributes.of(
                AttributeKey.stringKey("method"), request.getMethod(),
                AttributeKey.stringKey("status"), "success"
            )
        );
        
        return response;
    } catch (Exception e) {
        // Record error metrics
        requestCounter.add(1, Attributes.of(
            AttributeKey.stringKey("status"), "error",
            AttributeKey.stringKey("method"), request.getMethod()
        ));
        
        errorCounter.add(1, Attributes.of(
            AttributeKey.stringKey("error.type"), e.getClass().getSimpleName()
        ));
        
        throw e;
    }
}
```

### Business Metrics

```java
public Order createOrder(OrderRequest request) {
    Order order = orderService.create(request);
    
    // Record order metrics
    orderCounter.add(1, Attributes.of(
        AttributeKey.stringKey("order.type"), order.getType(),
        AttributeKey.stringKey("user.tier"), order.getUser().getTier()
    ));
    
    orderValueHistogram.record(order.getTotal(), Attributes.of(
        AttributeKey.stringKey("order.type"), order.getType(),
        AttributeKey.stringKey("currency"), order.getCurrency()
    ));
    
    return order;
}
```

---

## Metric Aggregation

### Rate Calculation

```java
// Calculate requests per second
double requestsPerSecond = requestCounter.sum() / timeWindowSeconds;

// Calculate error rate
double errorRate = errorCounter.sum() / requestCounter.sum();
```

### Percentile Calculation

```java
// P50, P95, P99 latency
// LightStep automatically calculates these from histograms
```

---

## Alerting on Metrics

### Error Rate Alert

```yaml
alert:
  name: high-error-rate
  condition: |
    rate(http.server.requests{status="error"}[5m]) / 
    rate(http.server.requests[5m]) > 0.05
  severity: critical
  message: "Error rate exceeds 5%"
```

### Latency Alert

```yaml
alert:
  name: high-latency
  condition: |
    histogram_quantile(0.99, rate(http.server.duration_bucket[5m])) > 1000
  severity: warning
  message: "P99 latency exceeds 1000ms"
```

---

## Dashboard Metrics

### Request Overview

```
Request Rate: 1000 req/s
Error Rate: 0.5%
P50 Latency: 50ms
P95 Latency: 150ms
P99 Latency: 300ms
```

### Resource Utilization

```
CPU Usage: 45%
Memory Usage: 65%
Thread Count: 200
Active Connections: 50
```

---

## Best Practices

1. **Use Standard Metrics**: Follow OpenTelemetry conventions
2. **Keep Attributes Low Cardinality**: Avoid high-cardinality attributes
3. **Use Histograms for Latency**: Better than gauges for distributions
4. **Monitor Business Metrics**: Track KPIs alongside technical metrics
5. **Set Up Alerts**: Proactive monitoring with alerts

---

## Next Steps

- [Sampling](../05-sampling/) - Sampling strategies
- [Context Propagation](../06-context-propagation/) - Cross-service context
- [Alerting & Dashboards](../08-alerting-dashboards/) - Monitoring setup
