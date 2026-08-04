# Serverless Architecture

Serverless architecture lets you build and run applications without managing infrastructure. Functions execute on demand, scaling automatically.

## Table of Contents

1. [Concepts](#concepts)
2. [Function as a Service](#function-as-a-service)
3. [Backend as a Service](#backend-as-a-service)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Serverless?

No server management. Pay per execution. Auto-scaling. Event-driven functions.

```
Event ──▶ Function ──▶ Response
  │          │
  │     (auto-scaled)
  │          │
  └──── Cloud Provider
```

### Benefits

- **No Infrastructure** - provider manages servers
- **Auto-Scaling** - scales with demand
- **Pay Per Use** - only pay for execution
- **Fast Deployment** - deploy functions quickly

---

## Function as a Service

### AWS Lambda Example

```java
// Simple Lambda function
public class OrderFunction implements RequestHandler<APIGatewayProxyRequestEvent,
                                                       APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent input, Context context) {

        String body = input.getBody();
        OrderRequest request = JsonUtil.fromJson(body, OrderRequest.class);

        // Process order
        Order order = orderService.createOrder(request);

        return new APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withBody(JsonUtil.toJson(order));
    }
}

// SQS-triggered function
public class InventoryFunction implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage message : event.getRecords()) {
            InventoryUpdate update = JsonUtil.fromJson(message.getBody(),
                InventoryUpdate.class);
            inventoryService.updateStock(update.productId(), update.quantity());
        }
        return null;
    }
}
```

### Serverless with Spring Cloud

```java
@SpringBootApplication
public class ServerlessApplication {

    @Bean
    public Consumer<S3Event> processUpload() {
        return event -> {
            for (S3Event.S3EventNotificationRecord record : event.getRecords()) {
                String key = record.getS3().getObject().getKey();
                processFile(key);
            }
        };
    }

    @Bean
    public Function<OrderRequest, OrderResponse> createOrder() {
        return request -> {
            Order order = orderService.createOrder(request);
            return new OrderResponse(order.getId(), "Created");
        };
    }
}
```

---

## Backend as a Service

### Firebase/Supabase Style

```java
// Use BaaS for common functionality
// Authentication, database, storage - all managed

// Client-side code
public class OrderClient {
    private final FirebaseClient firebase;

    public void createOrder(OrderRequest request) {
        // Firebase handles auth, database, real-time sync
        firebase.getCollection("orders").add(request);
    }

    public void listenForOrders(Consumer<Order> callback) {
        firebase.getCollection("orders")
            .onSnapshot(snapshot -> {
                snapshot.getDocumentChanges().forEach(change -> {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                        callback.accept(change.getDocument().toObject(Order.class));
                    }
                });
            });
    }
}
```

---

## Best Practices

### Do

```java
// 1. Keep functions small and focused
public class ValidateOrderFunction implements RequestHandler<Request, Response> {
    @Override
    public Response handleRequest(Request input, Context context) {
        // Only validation logic
        ValidationResult result = validator.validate(input);
        return new Response(result.isValid(), result.getErrors());
    }
}

// 2. Use managed services for common needs
// Auth → Cognito/Auth0
// Database → DynamoDB/RDS Serverless
// Queue → SQS/EventBridge
```

### Don't

```java
// 1. Don't have long-running functions
// Serverless has time limits (15 min AWS)

// 2. Don't store state in functions
// Functions are stateless

// 3. Don't overuse for everything
// Consider containers for complex workloads
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Serverless** | No server management |
| **FaaS** | Function as a Service |
| **BaaS** | Backend as a Service |
| **Event-Driven** | Functions triggered by events |
| **Auto-Scaling** | Scales with demand |
| **Pay Per Use** | Cost-effective for variable load |
| **Stateless** | No state between invocations |
| **Cold Start** | Latency on first invocation |
| **Use Cases** | APIs, data processing, automation |
