# Pulsar Functions

## Pulsar Functions, Processing at the Edge, and Stream Processing

---

## Table of Contents

- [Overview](#overview)
- [Pulsar Functions Architecture](#pulsar-functions-architecture)
- [Function Types](#function-types)
- [Function Development](#function-development)
- [Function Deployment](#function-deployment)
- [Processing Patterns](#processing-patterns)
- [Best Practices](#best-practices)

---

## Overview

Pulsar Functions are lightweight stream processing functions that can process messages without deploying a full stream processing framework. They enable processing at the edge, close to the data source.

### Key Features

- **Lightweight**: No cluster required
- **Native Integration**: Built into Pulsar
- **Multiple Languages**: Java, Python, Go
- **Processing at Edge**: Close to data source
- **Stateful Processing**: Built-in state storage
- **Exactly-Once**: Transactional processing

### When to Use Pulsar Functions

- Simple message transformations
- Filtering and routing
- Aggregation and counting
- Enrichment and validation
- Light stream processing

---

## Pulsar Functions Architecture

### Function Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Pulsar Functions                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐            │
│  │  Source  │────▶│ Function │────▶│   Sink   │            │
│  │  Topic   │     │          │     │  Topic   │            │
│  └──────────┘     └──────────┘     └──────────┘            │
│                         │                                    │
│                         │ State                              │
│                         ▼                                    │
│                  ┌──────────────┐                           │
│                  │ State Store  │                           │
│                  └──────────────┘                           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Function Components

| Component | Responsibility |
|-----------|---------------|
| Source Topic | Input messages |
| Function | Processing logic |
| Sink Topic | Output messages |
| State Store | Stateful processing |

---

## Function Types

### Java Function

```java
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class OrderProcessor implements Function<String, String> {
    
    @Override
    public String process(String input, Context context) {
        // Process order
        Order order = Order.parse(input);
        
        // Validate order
        if (order.getAmount() > 0) {
            // Enrich order
            order.setProcessedAt(System.currentTimeMillis());
            return order.toJson();
        }
        
        return null; // Filter invalid orders
    }
}
```

### Python Function

```python
from pulsar import Function

class OrderProcessor(Function):
    def process(self, input, context):
        # Process order
        order = json.loads(input)
        
        # Validate order
        if order['amount'] > 0:
            # Enrich order
            order['processed_at'] = int(time.time() * 1000)
            return json.dumps(order)
        
        return None  # Filter invalid orders
```

### Go Function

```go
package main

import (
    "context"
    "encoding/json"
    "github.com/apache/pulsar/pulsar-function-go/pf"
)

type OrderProcessor struct{}

func (o *OrderProcessor) Process(ctx context.Context, input []byte) ([]byte, error) {
    // Process order
    var order map[string]interface{}
    json.Unmarshal(input, &order)
    
    // Validate order
    if order["amount"].(float64) > 0 {
        // Enrich order
        order["processed_at"] = time.Now().UnixMilli()
        return json.Marshal(order)
    }
    
    return nil, nil // Filter invalid orders
}

func main() {
    pf.Start(OrderProcessor{})
}
```

---

## Function Development

### Function Interface

```java
public interface Function<I, O> {
    
    // Process single message
    O process(I input, Context context) throws Exception;
    
    // Initialize function (optional)
    default void open(Context context) throws Exception {}
    
    // Cleanup function (optional)
    default void close() {}
}
```

### Function Context

```java
public interface Context {
    
    // Function metadata
    String getFunctionId();
    String getFunctionVersion();
    String getOutputTopic();
    
    // State management
    Optional<byte[]> getState(String key);
    void putState(String key, byte[] value);
    void deleteState(String key);
    
    // Metrics
    void recordMetric(String metricName, double value);
    
    // Logging
    void getLogger().info(String message);
    
    // User config
    Optional<String> getUserConfig(String key);
}
```

### Function Configuration

```java
// Function configuration
FunctionConfig config = FunctionConfig.builder()
    .name("order-processor")
    .className("com.example.OrderProcessor")
    .sourceTopic("persistent://tenant/namespace/orders-input")
    .sinkTopic("persistent://tenant/namespace/orders-output")
    .processingGuarantee(ProcessingGuarantee.AT_LEAST_ONCE)
    .build();
```

---

## Function Deployment

### Deploy via CLI

```bash
# Deploy Java function
pulsar-admin functions create \
  --jar target/order-processor.jar \
  --classname com.example.OrderProcessor \
  --name order-processor \
  --tenant my-tenant \
  --namespace my-namespace \
  --input-topic persistent://my-tenant/my-namespace/orders-input \
  --output-topic persistent://my-tenant/my-namespace/orders-output

# Deploy Python function
pulsar-admin functions create \
  --py order_processor.py \
  --classname order_processor.OrderProcessor \
  --name order-processor \
  --tenant my-tenant \
  --namespace my-namespace \
  --input-topic persistent://my-tenant/my-namespace/orders-input \
  --output-topic persistent://my-tenant/my-namespace/orders-output

# Deploy Go function
pulsar-admin functions create \
  --go order-processor \
  --name order-processor \
  --tenant my-tenant \
  --namespace my-namespace \
  --input-topic persistent://my-tenant/my-namespace/orders-input \
  --output-topic persistent://my-tenant/my-namespace/orders-output
```

### Deploy via API

```java
// Create function
Functions functions = client.newFunctions();

FunctionConfig config = FunctionConfig.builder()
    .name("order-processor")
    .className("com.example.OrderProcessor")
    .sourceTopic("persistent://tenant/namespace/orders-input")
    .sinkTopic("persistent://tenant/namespace/orders-output")
    .build();

functions.create(config, new File("target/order-processor.jar"));
```

### Function Management

```bash
# List functions
pulsar-admin functions list \
  --tenant my-tenant \
  --namespace my-namespace

# Get function status
pulsar-admin functions status \
  --tenant my-tenant \
  --namespace my-namespace \
  --name order-processor

# Get function stats
pulsar-admin functions stats \
  --tenant my-tenant \
  --namespace my-namespace \
  --name order-processor

# Stop function
pulsar-admin functions stop \
  --tenant my-tenant \
  --namespace my-namespace \
  --name order-processor

# Start function
pulsar-admin functions start \
  --tenant my-tenant \
  --namespace my-namespace \
  --name order-processor

# Delete function
pulsar-admin functions delete \
  --tenant my-tenant \
  --namespace my-namespace \
  --name order-processor
```

---

## Processing Patterns

### Transformation

```java
public class OrderTransformer implements Function<String, String> {
    
    @Override
    public String process(String input, Context context) {
        // Transform order
        Order order = Order.parse(input);
        order.setProcessedAt(System.currentTimeMillis());
        order.setStatus("PROCESSED");
        return order.toJson();
    }
}
```

### Filtering

```java
public class OrderFilter implements Function<String, String> {
    
    @Override
    public String process(String input, Context context) {
        Order order = Order.parse(input);
        
        // Filter invalid orders
        if (order.getAmount() <= 0) {
            return null; // Don't forward
        }
        
        return input; // Forward valid orders
    }
}
```

### Enrichment

```java
public class OrderEnricher implements Function<String, String> {
    
    private Map<String, String> userCache;
    
    @Override
    public void open(Context context) {
        userCache = new HashMap<>();
    }
    
    @Override
    public String process(String input, Context context) {
        Order order = Order.parse(input);
        
        // Enrich with user data
        String userId = order.getUserId();
        if (!userCache.containsKey(userId)) {
            String userData = fetchUserData(userId);
            userCache.put(userId, userData);
        }
        
        order.setUserData(userCache.get(userId));
        return order.toJson();
    }
}
```

### Aggregation

```java
public class OrderAggregator implements Function<String, Long> {
    
    private Map<String, Double> totalByUser;
    
    @Override
    public void open(Context context) {
        totalByUser = new HashMap<>();
    }
    
    @Override
    public Long process(String input, Context context) {
        Order order = Order.parse(input);
        String userId = order.getUserId();
        
        // Aggregate by user
        totalByUser.merge(userId, order.getAmount(), Double::sum);
        
        // Return running total
        return totalByUser.get(userId).longValue();
    }
}
```

---

## Best Practices

### Function Design

1. **Keep functions small** - Single responsibility
2. **Handle errors gracefully** - Don't throw exceptions
3. **Use state wisely** - Minimize state size
4. **Test functions locally** - Use local run mode

### Performance

1. **Use batching** - Process messages in batches
2. **Tune resources** - Set appropriate memory/CPU
3. **Use async processing** - When possible
4. **Monitor function metrics** - Track throughput

### Reliability

1. **Use at-least-once** - For reliability
2. **Handle duplicate messages** - Implement idempotency
3. **Use dead letter topics** - Capture failed messages
4. **Test failure scenarios** - Verify recovery

### Operations

1. **Version functions** - Use semantic versioning
2. **Monitor function health** - Track status
3. **Use namespaces** - Organize functions logically
4. **Document functions** - Maintain documentation

---

## Further Reading

- [Pulsar Functions](https://pulsar.apache.org/docs/functions-overview/)
- [Pulsar Functions API](https://pulsar.apache.org/docs/functions-api/)
- [Pulsar Functions Examples](https://github.com/apache/pulsar/tree/master/pulsar-functions/java-examples)
