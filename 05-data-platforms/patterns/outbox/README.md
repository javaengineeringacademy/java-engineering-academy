# Outbox Pattern

## Overview

The Outbox Pattern ensures reliable message publishing by writing events to an outbox table in the same database transaction as the business state change. A separate process reads the outbox and publishes messages to the message broker, guaranteeing at-least-once delivery without distributed transactions.

This pattern solves the dual-write problem where updating the database and publishing a message cannot happen atomically across two different systems.

## When to Use

- Database updates and message publishing must be atomic
- Message broker cannot participate in database transactions
- Guaranteed message delivery is required
- Avoiding distributed transactions is a priority
- Event-driven architectures need reliable event publishing

## Implementation

### TypeScript

```typescript
class OrderService {
  constructor(
    private db: DatabaseClient,
    private messageBus: MessageBus
  ) {}

  async createOrder(customerId: string, items: OrderItem[]): Promise<string> {
    return this.db.transaction(async (tx) => {
      const order = new Order(customerId, items);
      await tx.query(
        'INSERT INTO orders (id, customer_id, status) VALUES ($1, $2, $3)',
        [order.id, order.customerId, 'created']
      );

      await tx.query(
        'INSERT INTO outbox (id, aggregate_type, aggregate_id, event_type, payload) VALUES ($1, $2, $3, $4, $5)',
        [uuid(), 'Order', order.id, 'OrderCreated', JSON.stringify({
          orderId: order.id,
          customerId: order.customerId,
          items: order.items
        })]
      );

      return order.id;
    });
  }
}

class OutboxProcessor {
  constructor(private db: DatabaseClient, private messageBus: MessageBus) {}

  async processBatch(batchSize: number = 100): Promise<void> {
    const events = await this.db.query(
      'SELECT * FROM outbox WHERE processed = false ORDER BY created_at LIMIT $1',
      [batchSize]
    );

    for (const event of events) {
      try {
        await this.messageBus.publish(event.event_type, event.payload);
        await this.db.query(
          'UPDATE outbox SET processed = true, processed_at = NOW() WHERE id = $1',
          [event.id]
        );
      } catch (error) {
        console.error(`Failed to publish event ${event.id}:`, error);
      }
    }
  }
}
```

### Java

```java
@Entity
@Table(name = "outbox")
public class OutboxEvent {
    @Id
    private String id;
    private String aggregateType;
    private String aggregateId;
    private String eventType;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private boolean processed;
    private Instant createdAt;
}

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private OutboxRepository outboxRepo;

    @Transactional
    public String createOrder(CreateOrderCommand command) {
        Order order = Order.create(command);
        orderRepo.save(order);

        OutboxEvent event = OutboxEvent.create(
            "Order", order.getId(), "OrderCreated",
            objectMapper.writeValueAsString(new OrderCreatedEvent(order))
        );
        outboxRepo.save(event);

        return order.getId();
    }
}

@Component
public class OutboxProcessor {
    @Scheduled(fixedDelay = 1000)
    public void processPendingEvents() {
        List<OutboxEvent> events = outboxRepo.findUnprocessed(PageRequest.of(0, 100));
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getEventType(), event.getPayload());
                event.setProcessed(true);
                outboxRepo.save(event);
            } catch (Exception e) {
                log.error("Failed to publish event: {}", event.getId(), e);
            }
        }
    }
}
```

### Python

```python
from datetime import datetime
from typing import List
import json

class OutboxEvent:
    def __init__(self, id, aggregate_type, aggregate_id, event_type, payload):
        self.id = id
        self.aggregate_type = aggregate_type
        self.aggregate_id = aggregate_id
        self.event_type = event_type
        self.payload = payload
        self.processed = False
        self.created_at = datetime.utcnow()

class OrderService:
    def __init__(self, session, message_bus):
        self.session = session
        self.message_bus = message_bus

    def create_order(self, customer_id: str, items: list):
        order = Order(customer_id, items)
        self.session.add(order)
        event = OutboxEvent(
            id=str(uuid4()),
            aggregate_type='Order',
            aggregate_id=order.id,
            event_type='OrderCreated',
            payload=json.dumps({'orderId': order.id, 'items': items})
        )
        self.session.add(event)
        self.session.commit()
        return order.id

class OutboxProcessor:
    def __init__(self, session, message_bus):
        self.session = session
        self.message_bus = message_bus

    def process_batch(self, batch_size: int = 100):
        events = self.session.query(OutboxEvent)\
            .filter_by(processed=False)\
            .order_by(OutboxEvent.created_at)\
            .limit(batch_size)\
            .all()

        for event in events:
            try:
                self.message_bus.publish(event.event_type, event.payload)
                event.processed = True
                self.session.commit()
            except Exception as e:
                self.session.rollback()
                print(f'Failed to publish event {event.id}: {e}')
```

### C\#

```csharp
public class OutboxEvent {
    public Guid Id { get; set; }
    public string AggregateType { get; set; }
    public string AggregateId { get; set; }
    public string EventType { get; set; }
    public string Payload { get; set; }
    public bool Processed { get; set; }
    public DateTime CreatedAt { get; set; }
}

public class OrderService {
    private readonly DbContext _context;

    public async Task<Guid> CreateAsync(CreateOrderCommand command) {
        await using var transaction = await _context.Database.BeginTransactionAsync();
        try {
            var order = Order.Create(command);
            _context.Orders.Add(order);

            _context.OutboxEvents.Add(new OutboxEvent {
                Id = Guid.NewGuid(),
                AggregateType = "Order",
                AggregateId = order.Id.ToString(),
                EventType = "OrderCreated",
                Payload = JsonSerializer.Serialize(new { OrderId = order.Id }),
                CreatedAt = DateTime.UtcNow
            });

            await _context.SaveChangesAsync();
            await transaction.CommitAsync();
            return order.Id;
        } catch {
            await transaction.RollbackAsync();
            throw;
        }
    }
}
```

## Best Practices

- Use a transactional outbox within the same database transaction as business data
- Process outbox events frequently to minimize latency
- Implement idempotent message publishing for at-least-once delivery
- Clean up processed outbox entries regularly
- Monitor outbox lag to detect processing delays
- Consider Debezium for CDC-based outbox polling

## Interview Questions

1. What problem does the Outbox Pattern solve?
2. How does the Outbox Pattern differ from distributed transactions?
3. What happens if the outbox processor fails mid-batch?
4. How do you ensure idempotency when publishing outbox events?
5. What are the performance implications of the outbox table?

## References

- Richardson, Chris. *Microservices Patterns*, Chapter 4
- Martin Fowler. *The Outbox Pattern*
- Debezium. *Outbox Event Router*
- Vaughn Vernon. *Implementing Domain-Driven Design*
