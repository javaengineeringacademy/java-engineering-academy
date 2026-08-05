# Event Sourcing for Data

## Overview

Event Sourcing stores all changes to application state as a sequence of immutable events rather than storing only the current state. The current state is derived by replaying events from the beginning. Every state mutation is captured as a domain event with sufficient data to reconstruct the change.

This pattern provides a complete audit trail, enables temporal queries, and supports event-driven architectures. State is never modified or deleted; new events are appended to the history.

## When to Use

- Complete audit trail is required for compliance or debugging
- Temporal queries need to reconstruct past states
- Event-driven architecture integrates with other systems
- Complex state transitions need to be tracked
- Debugging production issues requires knowing exactly what changed

## Implementation

### TypeScript

```typescript
interface DomainEvent {
  type: string;
  timestamp: Date;
  data: any;
}

class Order {
  private events: DomainEvent[] = [];

  constructor(private id: string) {}

  static fromEvents(id: string, events: DomainEvent[]): Order {
    const order = new Order(id);
    events.forEach(e => order.apply(e, false));
    return order;
  }

  addItem(productId: string, quantity: number): void {
    this.apply({
      type: 'OrderItemAdded',
      timestamp: new Date(),
      data: { productId, quantity }
    }, true);
  }

  private apply(event: DomainEvent, isNew: boolean): void {
    switch (event.type) {
      case 'OrderItemAdded':
        this.handleItemAdded(event.data);
        break;
    }
    if (isNew) this.events.push(event);
  }

  getEvents(): DomainEvent[] {
    return [...this.events];
  }
}

class EventStore {
  private events: Map<string, DomainEvent[]> = new Map();

  async append(orderId: string, event: DomainEvent): Promise<void> {
    const existing = this.events.get(orderId) || [];
    existing.push(event);
    this.events.set(orderId, existing);
  }

  async getEvents(orderId: string): Promise<DomainEvent[]> {
    return this.events.get(orderId) || [];
  }
}
```

### Java

```java
public interface DomainEvent {
    String getType();
    Instant getTimestamp();
    Map<String, Object> getData();
}

public class Order {
    private String id;
    private List<DomainEvent> events = new ArrayList<>();

    public static Order fromEvents(String id, List<DomainEvent> events) {
        Order order = new Order(id);
        events.forEach(order::apply);
        return order;
    }

    public void addItem(String productId, int quantity) {
        Map<String, Object> data = new HashMap<>();
        data.put("productId", productId);
        data.put("quantity", quantity);
        DomainEvent event = new OrderItemAddedEvent(Instant.now(), data);
        apply(event);
        events.add(event);
    }
}

public class EventStore {
    private final Map<String, List<DomainEvent>> store = new ConcurrentHashMap<>();

    public void append(String aggregateId, DomainEvent event) {
        store.computeIfAbsent(aggregateId, k -> new ArrayList<>()).add(event);
    }

    public List<DomainEvent> getEvents(String aggregateId) {
        return store.getOrDefault(aggregateId, Collections.emptyList());
    }
}
```

### Python

```python
from dataclasses import dataclass, field
from datetime import datetime
from typing import List, Dict, Any
from abc import ABC, abstractmethod

@dataclass
class DomainEvent:
    type: str
    timestamp: datetime
    data: Dict[str, Any]

class Aggregate(ABC):
    def __init__(self):
        self._events: List[DomainEvent] = []

    def apply(self, event: DomainEvent):
        self._when(event)
        self._events.append(event)

    @abstractmethod
    def _when(self, event: DomainEvent):
        pass

    @classmethod
    def from_events(cls, events: List[DomainEvent]):
        aggregate = cls()
        for event in events:
            aggregate._when(event)
        return aggregate

class EventStore:
    def __init__(self):
        self._store: Dict[str, List[DomainEvent]] = {}

    def append(self, aggregate_id: str, event: DomainEvent):
        if aggregate_id not in self._store:
            self._store[aggregate_id] = []
        self._store[aggregate_id].append(event)

    def get_events(self, aggregate_id: str) -> List[DomainEvent]:
        return self._store.get(aggregate_id, [])
```

### C\#

```csharp
public interface IDomainEvent {
    string Type { get; }
    DateTime Timestamp { get; }
    object Data { get; }
}

public class EventStore {
    private readonly Dictionary<string, List<IDomainEvent>> _store = new();

    public void Append(string aggregateId, IDomainEvent @event) {
        if (!_store.ContainsKey(aggregateId))
            _store[aggregateId] = new List<IDomainEvent>();
        _store[aggregateId].Add(@event);
    }

    public IReadOnlyList<IDomainEvent> GetEvents(string aggregateId) =>
        _store.TryGetValue(aggregateId, out var events)
            ? events.AsReadOnly()
            : Array.Empty<IDomainEvent>().AsReadOnly();
}

public class OrderAggregate {
    private readonly List<IDomainEvent> _events = new();

    public static OrderAggregate FromEvents(string id, IEnumerable<IDomainEvent> events) {
        var aggregate = new OrderAggregate();
        foreach (var @event in events) aggregate.Apply(@event);
        return aggregate;
    }
}
```

## Best Practices

- Events should be immutable and contain all data needed to reconstruct state
- Use optimistic concurrency with version numbers on event streams
- Create snapshots for aggregates with long event histories
- Design events around business intentions, not technical changes
- Consider event schema evolution and versioning strategies
- Separate event storage from read model projections

## Interview Questions

1. What are the advantages of Event Sourcing over traditional CRUD?
2. How do you handle event schema changes over time?
3. What is the relationship between snapshots and event replay?
4. How does Event Sourcing affect querying current state?
5. What are the storage and performance implications?

## References

- Young, Greg. *Versioning in an Event Sourced System*
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- Martin Fowler. *Event Sourcing*
- Greg Young. *CQRS and Event Sourcing*
