# CQRS for Data

## Overview

Command Query Responsibility Segregation (CQRS) separates read and write operations into different models. Commands change state and return void or success indicators, while queries return data without side effects. This separation allows independent optimization of read and write paths.

CQRS acknowledges that read and write workloads have fundamentally different characteristics. Read models can be denormalized for query efficiency while write models maintain domain integrity.

## When to Use

- Read and write performance requirements differ significantly
- Complex domain logic exists on the write side but reads are simple
- Different storage technologies suit reads vs writes
- Audit logging and event sourcing align with the command side
- Scaling reads independently from writes is needed

## Implementation

### TypeScript

```typescript
class CreateOrderCommand {
  constructor(
    public readonly customerId: string,
    public readonly items: Array<{ productId: string; quantity: number }>
  ) {}
}

class OrderSummaryQuery {
  constructor(public readonly customerId: string) {}
}

class CommandHandler {
  constructor(private orderRepository: OrderRepository) {}

  async handle(command: CreateOrderCommand): Promise<string> {
    const order = new Order(command.customerId);
    command.items.forEach(item => order.addItem(item.productId, item.quantity));
    await this.orderRepository.save(order);
    return order.id;
  }
}

class QueryHandler {
  constructor(private readDb: ReadDatabase) {}

  async handle(query: OrderSummaryQuery): Promise<OrderSummary[]> {
    return this.readDb.query(
      'SELECT * FROM order_summaries WHERE customer_id = $1',
      [query.customerId]
    );
  }
}
```

### Java

```java
// Command side
public class CreateOrderCommand {
    private final String customerId;
    private final List<OrderItemRequest> items;
}

public class OrderCommandHandler {
    private final OrderRepository repository;

    public String handle(CreateOrderCommand command) {
        Order order = Order.create(command.getCustomerId(), command.getItems());
        repository.save(order);
        return order.getId();
    }
}

// Query side
public class OrderQueryService {
    private final JdbcTemplate readJdbcTemplate;

    public List<OrderSummaryDto> getOrdersForCustomer(String customerId) {
        return readJdbcTemplate.query(
            "SELECT * FROM order_summaries WHERE customer_id = ?",
            new OrderSummaryRowMapper(), customerId
        );
    }
}
```

### Python

```python
from abc import ABC, abstractmethod

class Command:
    pass

class Query:
    pass

class CreateOrderCommand(Command):
    def __init__(self, customer_id: str, items: list):
        self.customer_id = customer_id
        self.items = items

class OrderSummaryQuery(Query):
    def __init__(self, customer_id: str):
        self.customer_id = customer_id

class CommandHandler(ABC):
    @abstractmethod
    def handle(self, command: Command):
        pass

class CreateOrderHandler(CommandHandler):
    def __init__(self, repository):
        self.repository = repository

    def handle(self, command: CreateOrderCommand):
        order = Order(command.customer_id)
        for item in command.items:
            order.add_item(item['product_id'], item['quantity'])
        self.repository.save(order)
        return order.id

class QueryHandler(ABC):
    @abstractmethod
    def handle(self, query: Query):
        pass

class OrderQueryHandler(QueryHandler):
    def __init__(self, read_db):
        self.read_db = read_db

    def handle(self, query: OrderSummaryQuery):
        return self.read_db.execute(
            'SELECT * FROM order_summaries WHERE customer_id = %s',
            (query.customer_id,)
        ).fetchall()
```

### C\#

```csharp
// Command
public record CreateOrderCommand(string CustomerId, List<OrderItemRequest> Items);

public class OrderCommandHandler {
    private readonly IOrderRepository _repository;

    public async Task<string> HandleAsync(CreateOrderCommand command) {
        var order = Order.Create(command.CustomerId, command.Items);
        await _repository.SaveAsync(order);
        return order.Id;
    }
}

// Query
public record OrderSummaryQuery(string CustomerId);

public class OrderQueryHandler {
    private readonly IDbConnection _readDb;

    public async Task<List<OrderSummaryDto>> HandleAsync(OrderSummaryQuery query) {
        return (await _readDb.QueryAsync<OrderSummaryDto>(
            "SELECT * FROM order_summaries WHERE customer_id = @CustomerId",
            query)).ToList();
    }
}
```

## Best Practices

- Use separate databases for reads and writes only when necessary
- Synchronize read models asynchronously from write events
- Keep the write model focused on domain invariants
- Denormalize read models for query performance
- Consider eventual consistency carefully in user-facing features
- Use event sourcing with the command side for full audit trails

## Interview Questions

1. What is the difference between CQRS and simple layered architecture?
2. How do you handle eventual consistency between read and write models?
3. When does CQRS add unnecessary complexity?
4. How does CQRS relate to event sourcing?
5. What are the scaling benefits of separating read and write models?

## References

- Young, Greg. *CQRS Documents*
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- Richardson, Chris. *Microservices Patterns*
- Microsoft. *CQRS Pattern*
