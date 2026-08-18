# LightStep Examples

## Overview

This directory contains comprehensive examples of LightStep integration with Java applications.

---

## Example 1: Basic Spring Boot Application

### Project Structure

```
basic-spring-boot/
├── src/main/java/
│   └── com/example/
│       ├── Application.java
│       ├── LightStepConfig.java
│       ├── UserService.java
│       └── UserController.java
├── src/main/resources/
│   └── application.yml
└── pom.xml
```

### Application.java

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### LightStepConfig.java

```java
package com.example;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LightStepConfig {
    
    @Value("${lightstep.access-token}")
    private String accessToken;
    
    @Value("${lightstep.service.name}")
    private String serviceName;
    
    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault().merge(
            Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), serviceName
            ))
        );
        
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
            .setEndpoint("ingest.lightstep.com:443")
            .addHeader("lightstep-access-token", accessToken)
            .build();
        
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
            .setResource(resource)
            .build();
        
        OtlpGrpcMetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
            .setEndpoint("ingest.lightstep.com:443")
            .addHeader("lightstep-access-token", accessToken)
            .build();
        
        SdkMeterProvider meterProvider = SdkMeterProvider.builder()
            .registerMetricReader(PeriodicMetricReader.builder(metricExporter).build())
            .setResource(resource)
            .build();
        
        return OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .setMeterProvider(meterProvider)
            .buildAndRegisterGlobal();
    }
    
    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("user-service");
    }
    
    @Bean
    public Meter meter(OpenTelemetry openTelemetry) {
        return openTelemetry.getMeter("user-service");
    }
}
```

### UserService.java

```java
package com.example;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import org.springframework.stereotype.Service;

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
            // Simulate database call
            User user = new User(userId, "John Doe", "john@example.com");
            
            userCounter.add(1, Attributes.of(
                AttributeKey.stringKey("user.tier"), "free"
            ));
            
            span.setAttribute("user.found", true);
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
    
    public User createUser(CreateUserRequest request) {
        Span span = tracer.spanBuilder("create-user")
            .setAttribute("user.email", request.getEmail())
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            User user = new User(
                java.util.UUID.randomUUID().toString(),
                request.getName(),
                request.getEmail()
            );
            
            userCounter.add(1, Attributes.of(
                AttributeKey.stringKey("user.tier"), "free"
            ));
            
            span.setAttribute("user.id", user.getId());
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

### UserController.java

```java
package com.example;

import org.springframework.web.bind.annotation.*;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    private final Tracer tracer;
    
    public UserController(UserService userService, Tracer tracer) {
        this.userService = userService;
        this.tracer = tracer;
    }
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        Span span = tracer.spanBuilder("HTTP GET /api/users/{id}")
            .setAttribute("http.method", "GET")
            .setAttribute("http.url", "/api/users/" + id)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            return userService.getUser(id);
        } finally {
            span.end();
        }
    }
    
    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        Span span = tracer.spanBuilder("HTTP POST /api/users")
            .setAttribute("http.method", "POST")
            .setAttribute("http.url", "/api/users")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            return userService.createUser(request);
        } finally {
            span.end();
        }
    }
}
```

### application.yml

```yaml
server:
  port: 8080

lightstep:
  access-token: ${LIGHTSTEP_ACCESS_TOKEN}
  service-name: user-service

logging:
  level:
    io.opentelemetry: DEBUG
```

---

## Example 2: Microservices Architecture

### Service A (API Gateway)

```java
package com.gateway;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private final Tracer tracer;
    private final RestTemplate restTemplate;
    
    public OrderService(Tracer tracer, RestTemplate restTemplate) {
        this.tracer = tracer;
        this.restTemplate = restTemplate;
    }
    
    public Order createOrder(CreateOrderRequest request) {
        Span span = tracer.spanBuilder("create-order")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Call inventory service
            InventoryResponse inventory = callInventoryService(request);
            
            // Call payment service
            PaymentResponse payment = callPaymentService(request);
            
            // Create order
            Order order = new Order(inventory, payment);
            
            span.setAttribute("order.id", order.getId());
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
            
            return order;
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
    
    private InventoryResponse callInventoryService(CreateOrderRequest request) {
        Span span = tracer.spanBuilder("HTTP POST /api/inventory/check")
            .setAttribute("http.method", "POST")
            .setAttribute("http.url", "http://inventory-service/api/inventory/check")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Inject context into headers
            java.util.Map<String, String> headers = new java.util.HashMap<>();
            W3CTraceContextPropagator.getInstance()
                .inject(Context.current(), headers, java.util.Map::put);
            
            org.springframework.http.HttpEntity<CreateOrderRequest> entity =
                new org.springframework.http.HttpEntity<>(request, 
                    new org.springframework.http.HttpHeaders() {{
                        headers.forEach(this::add);
                    }});
            
            InventoryResponse response = restTemplate.postForObject(
                "http://inventory-service/api/inventory/check",
                entity,
                InventoryResponse.class
            );
            
            span.setAttribute("inventory.available", response.isAvailable());
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
            
            return response;
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
    
    private PaymentResponse callPaymentService(CreateOrderRequest request) {
        Span span = tracer.spanBuilder("HTTP POST /api/payment/process")
            .setAttribute("http.method", "POST")
            .setAttribute("http.url", "http://payment-service/api/payment/process")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Similar to inventory service call
            PaymentResponse response = restTemplate.postForObject(
                "http://payment-service/api/payment/process",
                request,
                PaymentResponse.class
            );
            
            span.setAttribute("payment.status", response.getStatus());
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
            
            return response;
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

---

## Example 3: Kafka Integration

### Producer

```java
package com.example.kafka;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    
    public OrderEventProducer(Tracer tracer, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.tracer = tracer;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void sendOrderEvent(OrderEvent event) {
        Span span = tracer.spanBuilder("KAFKA PUBLISH order-events")
            .setAttribute("messaging.system", "kafka")
            .setAttribute("messaging.destination", "order-events")
            .setAttribute("messaging.operation", "publish")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            // Inject context into message headers
            java.util.Map<String, byte[]> headers = new java.util.HashMap<>();
            W3CTraceContextPropagator.getInstance()
                .inject(Context.current(), headers, 
                    (carrier, key, value) -> 
                        carrier.put(key, value.getBytes()));
            
            org.springframework.kafka.support.ProducerRecord<String, OrderEvent> record =
                new org.springframework.kafka.support.ProducerRecord<>(
                    "order-events", event.getOrderId(), event);
            
            // Add trace headers
            headers.forEach((key, value) -> 
                record.headers().add(key, value));
            
            kafkaTemplate.send(record);
            
            span.setAttribute("messaging.kafka.partition", record.partition());
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

### Consumer

```java
package com.example.kafka;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {
    
    private final Tracer tracer;
    private final OrderService orderService;
    
    public OrderEventConsumer(Tracer tracer, OrderService orderService) {
        this.tracer = tracer;
        this.orderService = orderService;
    }
    
    @KafkaListener(topics = "order-events")
    public void consumeOrderEvent(OrderEvent event) {
        // Extract context from message headers
        java.util.Map<String, String> headers = new java.util.HashMap<>();
        // Note: You would extract headers from the ConsumerRecord
        
        Context extractedContext = W3CTraceContextPropagator.getInstance()
            .extract(Context.current(), headers, java.util.Map::get);
        
        Span span = tracer.spanBuilder("KAFKA CONSUME order-events")
            .setParent(extractedContext)
            .setAttribute("messaging.system", "kafka")
            .setAttribute("messaging.destination", "order-events")
            .setAttribute("messaging.operation", "consume")
            .setAttribute("messaging.kafka.partition", 0)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            orderService.processOrderEvent(event);
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

---

## Example 4: Database Integration

### JDBC with Tracing

```java
package com.example.database;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.api.common.AttributeKey;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    
    private final Tracer tracer;
    private final JdbcTemplate jdbcTemplate;
    
    public UserRepository(Tracer tracer, JdbcTemplate jdbcTemplate) {
        this.tracer = tracer;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public User findById(String userId) {
        Span span = tracer.spanBuilder("DB SELECT users")
            .setAttribute("db.system", "postgresql")
            .setAttribute("db.statement", "SELECT * FROM users WHERE id = ?")
            .setAttribute("db.user", "app_user")
            .setAttribute("user.id", userId)
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            User user = jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new Object[]{userId},
                new UserRowMapper()
            );
            
            span.setAttribute("user.found", user != null);
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
            
            return user;
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
    
    public User save(User user) {
        Span span = tracer.spanBuilder("DB INSERT users")
            .setAttribute("db.system", "postgresql")
            .setAttribute("db.statement", "INSERT INTO users ...")
            .setAttribute("db.user", "app_user")
            .startSpan();
        
        try (Scope scope = span.makeCurrent()) {
            jdbcTemplate.update(
                "INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
                user.getId(), user.getName(), user.getEmail()
            );
            
            span.setAttribute("user.id", user.getId());
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
            
            return user;
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

---

## Example 5: Async Processing

### CompletableFuture with Context

```java
package com.example.async;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Scope;
import io.opentelemetry.context.Context;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    
    private final Tracer tracer;
    
    public AsyncService(Tracer tracer) {
        this.tracer = tracer;
    }
    
    public CompletableFuture<Result> processAsync(Request request) {
        Span span = tracer.spanBuilder("process-async")
            .startSpan();
        
        // Capture context for async processing
        Context context = Context.current().with(span);
        
        return CompletableFuture.supplyAsync(() -> {
            // Restore context in async thread
            try (Scope scope = context.makeCurrent()) {
                Result result = doProcessing(request);
                span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
                return result;
            } catch (Exception e) {
                span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
                span.recordException(e);
                throw new java.util.concurrent.CompletionException(e);
            } finally {
                span.end();
            }
        });
    }
    
    private Result doProcessing(Request request) {
        // Simulate long-running processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return new Result("processed");
    }
}
```

---

## Running Examples

### Prerequisites

1. LightStep account and access token
2. Java 17+
3. Maven or Gradle

### Setup

```bash
# Set environment variables
export LIGHTSTEP_ACCESS_TOKEN=your-access-token

# Run example
cd examples/basic-spring-boot
mvn spring-boot:run
```

### Verify

```bash
# Test endpoint
curl http://localhost:8080/api/users/123

# Check LightStep dashboard for traces
```

---

## Next Steps

- [Practices](../practices/) - Hands-on exercises
- [Solutions](../solutions/) - Complete solutions
