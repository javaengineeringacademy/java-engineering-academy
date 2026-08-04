# Retry Pattern

## Retry Patterns, Exponential Backoff, and Jitter

---

## Table of Contents

- [Overview](#overview)
- [Retry Concepts](#retry-concepts)
- [Exponential Backoff](#exponential-backoff)
- [Jitter](#jitter)
- [Implementation](#implementation)
- [Best Practices](#best-practices)

---

## Overview

Retry patterns handle transient failures by retrying operations. This guide covers retry strategies, exponential backoff, and jitter for reliable message processing.

### Key Concepts

- **Retry**: Attempt operation again after failure
- **Backoff**: Increase delay between retries
- **Jitter**: Add randomness to prevent thundering herd
- **Max Retries**: Limit retry attempts

---

## Retry Concepts

### Retry Concept

```
Retry Pattern:
┌─────────────────────────────────────────────────────────────┐
│                    Retry Flow                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Attempt operation                                       │
│     └── Success → Complete                                 │
│                                                              │
│  2. Failure detected                                         │
│     └── Check retry count                                  │
│                                                              │
│  3. Retry allowed                                            │
│     └── Wait (backoff + jitter)                            │
│     └── Attempt again                                       │
│                                                              │
│  4. Max retries reached                                      │
│     └── Fail permanently                                   │
│     └── Send to dead letter queue                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Retry Strategies

| Strategy | Description |
|----------|-------------|
| Fixed Interval | Same delay between retries |
| Linear Backoff | Increase delay linearly |
| Exponential Backoff | Double delay each retry |
| Exponential + Jitter | Backoff with randomness |

---

## Exponential Backoff

### Exponential Backoff Concept

```
Exponential Backoff:
Delay doubles with each retry

Retry 1: 1 second
Retry 2: 2 seconds
Retry 3: 4 seconds
Retry 4: 8 seconds
Retry 5: 16 seconds
Retry 6: 32 seconds

Formula: delay = initialDelay * 2^retryCount
```

### Exponential Backoff Implementation

```java
public class ExponentialBackoffRetry {
    
    private final int maxRetries;
    private final long initialDelay;
    private final long maxDelay;
    
    public ExponentialBackoffRetry(int maxRetries, long initialDelay, long maxDelay) {
        this.maxRetries = maxRetries;
        this.initialDelay = initialDelay;
        this.maxDelay = maxDelay;
    }
    
    public <T> T execute(Supplier<T> operation) {
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                return operation.get();
            } catch (Exception e) {
                retryCount++;
                
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Max retries exceeded", e);
                }
                
                long delay = calculateDelay(retryCount);
                sleep(delay);
            }
        }
        
        throw new RuntimeException("Max retries exceeded");
    }
    
    private long calculateDelay(int retryCount) {
        long delay = initialDelay * (1L << retryCount);
        return Math.min(delay, maxDelay);
    }
    
    private void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", e);
        }
    }
}
```

---

## Jitter

### Jitter Concept

```
Jitter:
Add randomness to prevent thundering herd

Without Jitter:
All clients retry at same time → Spike

With Jitter:
Clients retry at random times → Smooth

Retry 1: 1s ± 0.5s (0.5s - 1.5s)
Retry 2: 2s ± 1s (1s - 3s)
Retry 3: 4s ± 2s (2s - 6s)
Retry 4: 8s ± 4s (4s - 12s)
```

### Jitter Implementation

```java
public class ExponentialBackoffWithJitter {
    
    private final int maxRetries;
    private final long initialDelay;
    private final long maxDelay;
    private final Random random;
    
    public ExponentialBackoffWithJitter(int maxRetries, long initialDelay, long maxDelay) {
        this.maxRetries = maxRetries;
        this.initialDelay = initialDelay;
        this.maxDelay = maxDelay;
        this.random = new Random();
    }
    
    public <T> T execute(Supplier<T> operation) {
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                return operation.get();
            } catch (Exception e) {
                retryCount++;
                
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Max retries exceeded", e);
                }
                
                long delay = calculateDelayWithJitter(retryCount);
                sleep(delay);
            }
        }
        
        throw new RuntimeException("Max retries exceeded");
    }
    
    private long calculateDelayWithJitter(int retryCount) {
        long baseDelay = initialDelay * (1L << retryCount);
        long maxJitter = baseDelay / 2;
        long jitter = random.nextLong() % maxJitter;
        return Math.min(baseDelay + jitter, maxDelay);
    }
    
    private void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", e);
        }
    }
}
```

### Jitter Types

| Type | Description |
|------|-------------|
| Full Jitter | Random between 0 and delay |
| Equal Jitter | Random between delay/2 and delay |
| Decorrelated | Random based on previous delay |

---

## Implementation

### Kafka Retry Example

```java
public class KafkaRetryConsumer {
    
    private final KafkaConsumer<String, String> consumer;
    private final ExponentialBackoffWithJitter retryPolicy;
    
    public KafkaRetryConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
        this.retryPolicy = new ExponentialBackoffWithJitter(5, 1000, 30000);
    }
    
    public void consume() {
        consumer.subscribe(Arrays.asList("orders"));
        
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            
            for (ConsumerRecord<String, String> record : records) {
                try {
                    retryPolicy.execute(() -> {
                        processMessage(record);
                        return null;
                    });
                    consumer.commitSync();
                } catch (Exception e) {
                    // Send to dead letter queue
                    sendToDLQ(record, e);
                    consumer.commitSync();
                }
            }
        }
    }
    
    private void processMessage(ConsumerRecord<String, String> record) {
        // Process message
        // Throw exception on transient failure
    }
    
    private void sendToDLQ(ConsumerRecord<String, String> record, Exception e) {
        // Send to dead letter queue
    }
}
```

### RabbitMQ Retry Example

```java
public class RabbitMQRetryConsumer {
    
    private final Channel channel;
    private final ExponentialBackoffWithJitter retryPolicy;
    
    public RabbitMQRetryConsumer(Channel channel) {
        this.channel = channel;
        this.retryPolicy = new ExponentialBackoffWithJitter(5, 1000, 30000);
    }
    
    public void consume() throws IOException {
        channel.queueDeclare("orders", true, false, false, null);
        channel.basicQos(1);
        
        channel.basicConsume("orders", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String tag, Envelope envelope, 
                                     AMQP.BasicProperties props, byte[] body) {
                try {
                    retryPolicy.execute(() -> {
                        processMessage(new String(body));
                        return null;
                    });
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    // Send to dead letter queue
                    sendToDLQ(new String(body), e);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }
    
    private void processMessage(String message) {
        // Process message
    }
    
    private void sendToDLQ(String message, Exception e) {
        // Send to dead letter queue
    }
}
```

---

## Best Practices

### Design

1. **Set max retries** - Prevent infinite loops
2. **Use exponential backoff** - Reduce load on failure
3. **Add jitter** - Prevent thundering herd
4. **Use dead letter queue** - Capture failed messages

### Implementation

1. **Implement idempotency** - Handle duplicate retries
2. **Use appropriate retry policy** - Match to failure type
3. **Log retry attempts** - For debugging
4. **Monitor retry rates** - Track failure patterns

### Operations

1. **Monitor retry metrics** - Track retry counts
2. **Alert on high retries** - Detect issues
3. **Analyze failure patterns** - Improve reliability
4. **Document retry policies** - Maintain clarity

---

## Further Reading

- [Retry Pattern](https://www.enterpriseintegrationpatterns.com/patterns/messaging/Retry.html)
- [Exponential Backoff](https://aws.amazon.com/blogs/architecture/exponential-backoff-and-jitter/)
- [Kafka Error Handling](https://kafka.apache.org/documentation/#errorhandling)
