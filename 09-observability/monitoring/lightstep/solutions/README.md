# LightStep Solutions

## Overview

This directory contains complete solutions for the practices and challenges.

---

## Solution 1: Basic Tracing

### ProductService.java

```java
package com.example;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    private final Tracer tracer;
    
    public ProductService(Tracer tracer) {
        this.tracer = tracer;
    }
    
    public Product getProduct(String productId) {
        // Create span
        Span span = tracer.spanBuilder("get-product")
            .setAttribute("product.id", productId)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Process request
            Product product = new Product(productId, "Sample Product", 99.99);
            
            // Add attributes
            span.setAttribute("product.name", product.getName());
            span.setAttribute("product.price", product.getPrice());
            span.setStatus(StatusCode.OK);
            
            return product;
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
    
    public Product createProduct(CreateProductRequest request) {
        Span span = tracer.spanBuilder("create-product")
            .setAttribute("product.name", request.getName())
            .setAttribute("product.price", request.getPrice())
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            Product product = new Product(
                java.util.UUID.randomUUID().toString(),
                request.getName(),
                request.getPrice()
            );
            
            span.setAttribute("product.id", product.getId());
            span.setStatus(StatusCode.OK);
            
            return product;
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

## Solution 2: Context Propagation

### Service A - OrderClient.java

```java
package com.example.gateway;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

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
            .setAttribute("http.method", "GET")
            .setAttribute("http.url", "/api/orders/" + orderId)
            .setAttribute("order.id", orderId)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Inject context into headers
            Map<String, String> headers = new HashMap<>();
            W3CTraceContextPropagator.getInstance()
                .inject(Context.current(), headers, Map::put);
            
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::add);
            
            HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
            
            ResponseEntity<Order> response = restTemplate.exchange(
                "http://order-service/api/orders/" + orderId,
                HttpMethod.GET,
                entity,
                Order.class
            );
            
            span.setAttribute("http.status_code", response.getStatusCodeValue());
            span.setStatus(StatusCode.OK);
            
            return response.getBody();
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

### Service B - OrderController.java

```java
package com.example.orders;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final Tracer tracer;
    private final OrderService orderService;
    
    public OrderController(Tracer tracer, OrderService orderService) {
        this.tracer = tracer;
        this.orderService = orderService;
    }
    
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) {
        // Extract context from headers
        // Note: In real implementation, you would get headers from HttpServletRequest
        Context extractedContext = Context.current();
        
        Span span = tracer.spanBuilder("HTTP GET /api/orders/{id}")
            .setParent(extractedContext)
            .setAttribute("http.method", "GET")
            .setAttribute("http.url", "/api/orders/" + id)
            .setAttribute("order.id", id)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            Order order = orderService.getOrder(id);
            
            span.setAttribute("order.status", order.getStatus());
            span.setStatus(StatusCode.OK);
            
            return order;
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

## Solution 3: Metrics Collection

### UserService.java

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
        
        // Initialize metrics
        this.userCounter = meter.counterBuilder("users.total")
            .setDescription("Total users processed")
            .build();
        
        this.latencyHistogram = meter.histogramBuilder("users.latency")
            .setDescription("User operation latency")
            .setUnit("ms")
            .ofDouble()
            .build();
        
        this.activeUsersGauge = meter.gaugeBuilder("users.active")
            .setDescription("Active users")
            .ofLongs()
            .build();
    }
    
    public User getUser(String userId) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Process request
            User user = new User(userId, "John Doe", "john@example.com");
            
            // Record metrics
            userCounter.add(1, Attributes.of(
                AttributeKey.stringKey("operation"), "get"
            ));
            
            return user;
        } finally {
            // Record latency
            double latency = System.currentTimeMillis() - startTime;
            latencyHistogram.record(latency, Attributes.of(
                AttributeKey.stringKey("operation"), "get"
            ));
        }
    }
    
    public void userLoggedIn() {
        activeUsersGauge.set(
            activeUsersGauge.get() + 1,
            Attributes.of()
        );
    }
    
    public void userLoggedOut() {
        activeUsersGauge.set(
            Math.max(0, activeUsersGauge.get() - 1),
            Attributes.of()
        );
    }
}
```

---

## Solution 4: Sampling Strategies

### CustomSampler.java

```java
package com.example.config;

import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.TraceIdRatioBasedSampler;
import io.opentelemetry.sdk.trace.samplers.ParentBasedSampler;
import io.opentelemetry.sdk.trace.samplers.AlwaysOnSampler;
import io.opentelemetry.sdk.trace.samplers.AlwaysOffSampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import io.opentelemetry.sdk.trace.samplers.SamplingDecision;
import io.opentelemetry.sdk.common.Codec;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.trace.data.SpanData;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CustomSampler implements Sampler {
    
    private final Sampler fallbackSampler;
    
    public CustomSampler() {
        this.fallbackSampler = TraceIdRatioBasedSampler.create(0.1);
    }
    
    @Override
    public SamplingResult shouldSample(
        Codec parentContext,
        String traceId,
        String name,
        SpanKind spanKind,
        Attributes attributes,
        List<SpanData> parentLinks) {
        
        // Always sample errors
        if (attributes.get(AttributeKey.booleanKey("error")) != null &&
            attributes.get(AttributeKey.booleanKey("error"))) {
            return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
        }
        
        // Always sample critical paths
        if (name.contains("payment") || name.contains("auth") || 
            name.contains("order")) {
            return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
        }
        
        // Sample 10% of other traces
        double random = ThreadLocalRandom.current().nextDouble();
        if (random < 0.1) {
            return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
        }
        
        return SamplingResult.create(SamplingDecision.DROP);
    }
    
    @Override
    public String getDescription() {
        return "CustomSampler";
    }
}
```

### SamplerConfig.java

```java
package com.example.config;

import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.ParentBasedSampler;
import io.opentelemetry.sdk.trace.samplers.TraceIdRatioBasedSampler;
import io.opentelemetry.sdk.trace.samplers.AlwaysOnSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SamplerConfig {
    
    @Bean
    public Sampler sampler(Environment environment) {
        String profile = environment.getActiveProfiles()[0];
        
        switch (profile) {
            case "dev":
                return AlwaysOnSampler.getInstance();
            case "staging":
                return ParentBasedSampler.builder()
                    .setRoot(TraceIdRatioBasedSampler.create(0.5))
                    .build();
            case "prod":
                return new CustomSampler();
            default:
                return ParentBasedSampler.builder()
                    .setRoot(TraceIdRatioBasedSampler.create(0.1))
                    .build();
        }
    }
}
```

---

## Solution 5: Alerting Configuration

### alert-config.yaml

```yaml
alerts:
  - name: high-error-rate
    description: Alert when error rate exceeds threshold
    condition: |
      rate(http.server.requests{status="error"}[5m]) / 
      rate(http.server.requests[5m]) > 0.05
    severity: critical
    message: "Error rate exceeds 5%"
    actions:
      - type: slack
        channel: "#alerts"
        message: |
          High error rate detected
          Service: {{service.name}}
          Error Rate: {{value}}%
          Time: {{timestamp}}

  - name: high-latency
    description: Alert when P99 latency exceeds threshold
    condition: |
      histogram_quantile(0.99, rate(http.server.duration_bucket[5m])) > 1000
    severity: warning
    message: "P99 latency exceeds 1000ms"
    actions:
      - type: slack
        channel: "#performance"
        message: |
          High latency detected
          Service: {{service.name}}
          P99 Latency: {{value}}ms
          Time: {{timestamp}}

  - name: sla-breach
    description: Alert when SLA is breached
    condition: |
      1 - (rate(http.server.requests{status="success"}[5m]) / 
      rate(http.server.requests[5m])) > 0.001
    severity: critical
    message: "SLA breach: Availability below 99.9%"
    actions:
      - type: pagerduty
        severity: critical
        description: "SLA breach detected"
      - type: email
        to: "team@company.com"
        subject: "SLA Breach Alert"
        body: |
          SLA breach detected
          Service: {{service.name}}
          Availability: {{value}}%
          Time: {{timestamp}}
```

---

## Solution 6: Dashboard Creation

### request-overview-dashboard.json

```json
{
  "title": "Request Overview Dashboard",
  "description": "Overview of HTTP request metrics",
  "panels": [
    {
      "title": "Request Rate",
      "type": "timeseries",
      "query": "sum(rate(http.server.requests[5m])) by (service.name)",
      "unit": "req/s",
      "gridPos": {"h": 8, "w": 12, "x": 0, "y": 0}
    },
    {
      "title": "Error Rate",
      "type": "timeseries",
      "query": "sum(rate(http.server.requests{status=\"error\"}[5m])) by (service.name) / sum(rate(http.server.requests[5m])) by (service.name)",
      "unit": "percent",
      "gridPos": {"h": 8, "w": 12, "x": 12, "y": 0}
    },
    {
      "title": "P50 Latency",
      "type": "timeseries",
      "query": "histogram_quantile(0.50, sum(rate(http.server.duration_bucket[5m])) by (le, service.name))",
      "unit": "ms",
      "gridPos": {"h": 8, "w": 12, "x": 0, "y": 8}
    },
    {
      "title": "P95 Latency",
      "type": "timeseries",
      "query": "histogram_quantile(0.95, sum(rate(http.server.duration_bucket[5m])) by (le, service.name))",
      "unit": "ms",
      "gridPos": {"h": 8, "w": 12, "x": 12, "y": 8}
    },
    {
      "title": "P99 Latency",
      "type": "timeseries",
      "query": "histogram_quantile(0.99, sum(rate(http.server.duration_bucket[5m])) by (le, service.name))",
      "unit": "ms",
      "gridPos": {"h": 8, "w": 12, "x": 0, "y": 16}
    },
    {
      "title": "Request Distribution by Service",
      "type": "piechart",
      "query": "sum(rate(http.server.requests[5m])) by (service.name)",
      "gridPos": {"h": 8, "w": 12, "x": 12, "y": 16}
    }
  ]
}
```

### service-map-dashboard.json

```json
{
  "title": "Service Map Dashboard",
  "description": "Service dependencies and health",
  "panels": [
    {
      "title": "Service Dependencies",
      "type": "service-map",
      "query": "service.name != \"\"",
      "gridPos": {"h": 12, "w": 24, "x": 0, "y": 0}
    },
    {
      "title": "Service Request Rate",
      "type": "timeseries",
      "query": "sum by (service.name) (rate(http.server.requests[5m]))",
      "unit": "req/s",
      "gridPos": {"h": 8, "w": 12, "x": 0, "y": 12}
    },
    {
      "title": "Service Error Rate",
      "type": "timeseries",
      "query": "sum by (service.name) (rate(http.server.requests{status=\"error\"}[5m])) / sum by (service.name) (rate(http.server.requests[5m]))",
      "unit": "percent",
      "gridPos": {"h": 8, "w": 12, "x": 12, "y": 12}
    }
  ]
}
```

---

## Challenge Solutions

### Challenge 1: E-commerce Tracing

#### OrderService.java

```java
package com.example.orders;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private final Tracer tracer;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;
    
    public OrderService(Tracer tracer, InventoryClient inventoryClient,
                       PaymentClient paymentClient, NotificationClient notificationClient) {
        this.tracer = tracer;
        this.inventoryClient = inventoryClient;
        this.paymentClient = paymentClient;
        this.notificationClient = notificationClient;
    }
    
    public Order createOrder(CreateOrderRequest request) {
        Span span = tracer.spanBuilder("create-order")
            .setAttribute("order.items", request.getItems().size())
            .setAttribute("order.total", request.getTotal())
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Check inventory
            InventoryResponse inventory = inventoryClient.checkInventory(request);
            if (!inventory.isAvailable()) {
                throw new InsufficientInventoryException("Items not available");
            }
            
            // Process payment
            PaymentResponse payment = paymentClient.processPayment(request);
            if (!payment.isSuccessful()) {
                throw new PaymentFailedException("Payment failed");
            }
            
            // Create order
            Order order = new Order(
                java.util.UUID.randomUUID().toString(),
                request,
                inventory,
                payment
            );
            
            // Send notification
            notificationClient.sendOrderConfirmation(order);
            
            span.setAttribute("order.id", order.getId());
            span.setAttribute("order.status", "created");
            span.setStatus(StatusCode.OK);
            
            return order;
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

#### InventoryClient.java

```java
package com.example.orders;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;

@Component
public class InventoryClient {
    
    private final Tracer tracer;
    private final RestTemplate restTemplate;
    
    public InventoryClient(Tracer tracer, RestTemplate restTemplate) {
        this.tracer = tracer;
        this.restTemplate = restTemplate;
    }
    
    public InventoryResponse checkInventory(CreateOrderRequest request) {
        Span span = tracer.spanBuilder("HTTP POST /api/inventory/check")
            .setAttribute("http.method", "POST")
            .setAttribute("http.url", "http://inventory-service/api/inventory/check")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            Map<String, String> headers = new HashMap<>();
            W3CTraceContextPropagator.getInstance()
                .inject(Context.current(), headers, Map::put);
            
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::add);
            
            HttpEntity<CreateOrderRequest> entity = 
                new HttpEntity<>(request, httpHeaders);
            
            InventoryResponse response = restTemplate.postForObject(
                "http://inventory-service/api/inventory/check",
                entity,
                InventoryResponse.class
            );
            
            span.setAttribute("inventory.available", response.isAvailable());
            span.setStatus(StatusCode.OK);
            
            return response;
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

## Verification Steps

### 1. Run Application

```bash
# Set environment variables
export LIGHTSTEP_ACCESS_TOKEN=your-access-token

# Run application
mvn spring-boot:run
```

### 2. Test Endpoints

```bash
# Test order creation
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"items": [{"productId": "123", "quantity": 2}], "total": 199.98}'

# Test order retrieval
curl http://localhost:8080/api/orders/123
```

### 3. Verify in LightStep

1. Open LightStep dashboard
2. Navigate to Traces
3. Verify trace structure
4. Check span attributes
5. Verify service map

---

## Next Steps

- Review solutions and understand implementation details
- Customize solutions for your specific use case
- Implement additional features as needed
- Share feedback and improvements
