# LightStep Practices

## Overview

This directory contains hands-on exercises to help you learn LightStep integration with Java applications.

---

## Practice 1: Basic Tracing

### Objective
Implement basic distributed tracing in a Spring Boot application.

### Instructions

1. **Create a new Spring Boot project**
   - Add OpenTelemetry dependencies
   - Configure LightStep exporter

2. **Implement tracing in a service**
   - Create spans for business operations
   - Add attributes to spans
   - Handle errors properly

3. **Verify traces in LightStep dashboard**

### Starter Code

```java
package com.example;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Scope;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    private final Tracer tracer;
    
    public ProductService(Tracer tracer) {
        this.tracer = tracer;
    }
    
    public Product getProduct(String productId) {
        // TODO: Create span
        // TODO: Add attributes
        // TODO: Process request
        // TODO: Handle errors
        // TODO: End span
        return null;
    }
}
```

### Expected Output

- Traces visible in LightStep dashboard
- Spans with correct attributes
- Error handling with status codes

---

## Practice 2: Context Propagation

### Objective
Implement context propagation across service boundaries.

### Instructions

1. **Create two Spring Boot services**
   - Service A: API Gateway
   - Service B: Backend Service

2. **Implement HTTP client in Service A**
   - Inject trace context into headers
   - Make request to Service B

3. **Implement HTTP server in Service B**
   - Extract trace context from headers
   - Create child spans

4. **Verify distributed traces**

### Starter Code

```java
// Service A - HTTP Client
package com.example.gateway;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderClient {
    
    private final Tracer tracer;
    private final RestTemplate restTemplate;
    
    public OrderClient(Tracer tracer, RestTemplate restTemplate) {
        this.tracer = tracer;
        this.restTemplate = restTemplate;
    }
    
    public Order getOrder(String orderId) {
        Span span = tracer.spanBuilder("HTTP GET /api/orders/{id}")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // TODO: Inject context into headers
            // TODO: Make HTTP request
            // TODO: Return response
            return null;
        } finally {
            span.end();
        }
    }
}
```

```java
// Service B - HTTP Server
package com.example.orders;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Component;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final Tracer tracer;
    
    public OrderController(Tracer tracer) {
        this.tracer = tracer;
    }
    
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) {
        // TODO: Extract context from headers
        // TODO: Create child span
        // TODO: Process request
        return null;
    }
}
```

### Expected Output

- Single trace spanning both services
- Parent-child span relationships
- Correct trace context propagation

---

## Practice 3: Metrics Collection

### Objective
Implement custom metrics collection in a Spring Boot application.

### Instructions

1. **Create metrics for business operations**
   - Counter for request count
   - Histogram for latency
   - Gauge for active users

2. **Implement metrics in a service**
   - Record request metrics
   - Record business metrics
   - Add attributes to metrics

3. **Verify metrics in LightStep dashboard**

### Starter Code

```java
package com.example;

import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongGauge;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final Meter meter;
    private final LongCounter userCounter;
    private final DoubleHistogram latencyHistogram;
    private final LongGauge activeUsersGauge;
    
    public UserService(Meter meter) {
        this.meter = meter;
        
        // TODO: Initialize metrics
        this.userCounter = null;
        this.latencyHistogram = null;
        this.activeUsersGauge = null;
    }
    
    public User getUser(String userId) {
        long startTime = System.currentTimeMillis();
        
        try {
            // TODO: Process request
            // TODO: Record metrics
            return null;
        } finally {
            // TODO: Record latency
        }
    }
    
    public void userLoggedIn() {
        // TODO: Increment active users
    }
    
    public void userLoggedOut() {
        // TODO: Decrement active users
    }
}
```

### Expected Output

- Metrics visible in LightStep dashboard
- Correct attribute values
- Proper metric aggregation

---

## Practice 4: Sampling Strategies

### Objective
Implement different sampling strategies for different environments.

### Instructions

1. **Implement probability sampling**
   - Sample 10% of traces in production
   - Sample 100% of traces in development

2. **Implement parent-based sampling**
   - Ensure consistent sampling across services

3. **Implement custom sampling**
   - Always sample error traces
   - Always sample critical paths

### Starter Code

```java
package com.example.config;

import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.TraceIdRatioBasedSampler;
import io.opentelemetry.sdk.trace.samplers.ParentBasedSampler;
import io.opentelemetry.sdk.trace.samplers.AlwaysOnSampler;
import io.opentelemetry.sdk.trace.samplers.AlwaysOffSampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import io.opentelemetry.sdk.common.Codec;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.trace.data.SpanData;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CustomSampler implements Sampler {
    
    @Override
    public SamplingResult shouldSample(
        Codec parentContext,
        String traceId,
        String name,
        SpanKind spanKind,
        Attributes attributes,
        List<SpanData> parentLinks) {
        
        // TODO: Implement sampling logic
        // 1. Always sample errors
        // 2. Always sample critical paths
        // 3. Sample 10% of other traces
        
        return SamplingResult.create(SamplingDecision.DROP);
    }
    
    @Override
    public String getDescription() {
        return "CustomSampler";
    }
}
```

### Expected Output

- Different sampling rates per environment
- Consistent sampling across services
- Custom sampling for critical paths

---

## Practice 5: Alerting Configuration

### Objective
Configure alerts for common issues.

### Instructions

1. **Create error rate alert**
   - Alert when error rate exceeds 5%

2. **Create latency alert**
   - Alert when P99 latency exceeds 1000ms

3. **Create SLA alert**
   - Alert when availability drops below 99.9%

4. **Test alerts**
   - Trigger each alert
   - Verify notifications

### Starter Code

```yaml
# alert-config.yaml
alerts:
  - name: high-error-rate
    description: Alert when error rate exceeds threshold
    condition: |
      # TODO: Implement condition
    severity: critical
    message: "Error rate exceeds 5%"
    actions:
      - type: slack
        channel: "#alerts"
        message: "High error rate detected: {{value}}%"

  - name: high-latency
    description: Alert when P99 latency exceeds threshold
    condition: |
      # TODO: Implement condition
    severity: warning
    message: "P99 latency exceeds 1000ms"
    actions:
      - type: slack
        channel: "#performance"
        message: "High latency detected: {{value}}ms"

  - name: sla-breach
    description: Alert when SLA is breached
    condition: |
      # TODO: Implement condition
    severity: critical
    message: "SLA breach: Availability below 99.9%"
    actions:
      - type: pagerduty
        severity: critical
```

### Expected Output

- Alerts triggering correctly
- Notifications sent to appropriate channels
- Clear alert messages

---

## Practice 6: Dashboard Creation

### Objective
Create dashboards for monitoring application health.

### Instructions

1. **Create request overview dashboard**
   - Request rate over time
   - Error rate over time
   - Latency percentiles

2. **Create service map dashboard**
   - Service dependencies
   - Service-specific metrics

3. **Create SLA dashboard**
   - Availability percentage
   - Error budget remaining

### Starter Code

```json
{
  "title": "Request Overview Dashboard",
  "panels": [
    {
      "title": "Request Rate",
      "type": "timeseries",
      "query": "TODO: Implement query",
      "unit": "req/s"
    },
    {
      "title": "Error Rate",
      "type": "timeseries",
      "query": "TODO: Implement query",
      "unit": "percent"
    },
    {
      "title": "P50 Latency",
      "type": "timeseries",
      "query": "TODO: Implement query",
      "unit": "ms"
    },
    {
      "title": "P95 Latency",
      "type": "timeseries",
      "query": "TODO: Implement query",
      "unit": "ms"
    },
    {
      "title": "P99 Latency",
      "type": "timeseries",
      "query": "TODO: Implement query",
      "unit": "ms"
    }
  ]
}
```

### Expected Output

- Dashboards displaying correct metrics
- Clear visualization of application health
- Proper time range selection

---

## Challenge Projects

### Challenge 1: E-commerce Tracing

**Objective**: Implement distributed tracing for an e-commerce application.

**Requirements**:
- Trace user requests across all services
- Track order processing
- Monitor payment processing
- Implement error handling

**Services**:
- API Gateway
- User Service
- Product Service
- Order Service
- Payment Service
- Notification Service

### Challenge 2: Real-time Monitoring

**Objective**: Create a real-time monitoring dashboard.

**Requirements**:
- Live request rate
- Real-time error tracking
- Service health status
- Alert notifications

**Features**:
- WebSocket updates
- Auto-refresh
- Interactive charts
- Drill-down capability

### Challenge 3: Performance Optimization

**Objective**: Use LightStep to optimize application performance.

**Requirements**:
- Identify slow operations
- Optimize database queries
- Reduce latency
- Improve throughput

**Steps**:
1. Baseline measurement
2. Identify bottlenecks
3. Implement optimizations
4. Measure improvements

---

## Evaluation Criteria

### For Each Practice

1. **Correctness**: Code works as expected
2. **Completeness**: All requirements met
3. **Code Quality**: Clean, maintainable code
4. **Documentation**: Clear comments and README
5. **Testing**: Proper verification

### For Challenge Projects

1. **Architecture**: Proper service design
2. **Implementation**: Complete functionality
3. **Monitoring**: Comprehensive observability
4. **Performance**: Optimized implementation
5. **Documentation**: Clear setup instructions

---

## Next Steps

- [Solutions](../solutions/) - Complete solutions
- [Examples](../examples/) - More code examples
