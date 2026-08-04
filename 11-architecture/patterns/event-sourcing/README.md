# Event Sourcing

## Overview

Event Sourcing is an architectural pattern where state changes are stored as an immutable sequence of events rather than just the current state. Instead of storing "what is," you store "what happened." This provides a complete audit trail, enables temporal queries, and allows rebuilding state at any point in time.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Architecture](#architecture)
- [Implementation](#implementation)
- [Event Store](#event-store)
- [Aggregates](#aggregates)
- [Projections](#projections)
- [Benefits](#benefits)
- [Trade-offs](#trade-offs)
- [Best Practices](#best-practices)

## Core Concepts

### Event

An event is an immutable fact that something happened in the system.

```python
from dataclasses import dataclass
from datetime import datetime
from uuid import UUID, uuid4

@dataclass(frozen=True)
class Event:
    """Base event class - immutable"""
    event_id: UUID
    aggregate_id: UUID
    timestamp: datetime
    version: int
    event_type: str
    data: dict

@dataclass(frozen=True)
class OrderCreated(Event):
    """Order creation event"""
    customer_id: UUID = None
    items: list = None

@dataclass(frozen=True)
class OrderItemAdded(Event):
    """Item added to order"""
    product_id: UUID = None
    quantity: int = 0
    price: float = 0.0

@dataclass(frozen=True)
class OrderConfirmed(Event):
    """Order confirmed"""
    confirmed_by: str = ""
    confirmed_at: datetime = None

@dataclass(frozen=True)
class OrderShipped(Event):
    """Order shipped"""
    tracking_number: str = ""
    carrier: str = ""
```

### Event Store

Event Store is the persistence layer for events.

```python
class EventStore:
    def __init__(self):
        self.events = {}  # aggregate_id -> [events]
    
    def append(self, aggregate_id: UUID, events: list):
        """Append events to store"""
        if aggregate_id not in self.events:
            self.events[aggregate_id] = []
        
        for event in events:
            event.version = len(self.events[aggregate_id]) + 1
            self.events[aggregate_id].append(event)
    
    def get_events(self, aggregate_id: UUID) -> list:
        """Get all events for aggregate"""
        return self.events.get(aggregate_id, [])
    
    def get_events_from(self, aggregate_id: UUID, 
                        version: int) -> list:
        """Get events from specific version"""
        events = self.events.get(aggregate_id, [])
        return [e for e in events if e.version >= version]
```

### Aggregate

Aggregates are rebuilt from events.

```python
class OrderAggregate:
    def __init__(self, order_id: UUID):
        self.id = order_id
        self.version = 0
        self.customer_id = None
        self.items = []
        self.status = None
        self.total = 0.0
        self.uncommitted_events = []
    
    def apply(self, event: Event):
        """Apply event to update state"""
        handler = getattr(self, f'_apply_{event.event_type}', None)
        if handler:
            handler(event)
            self.version = event.version
    
    def _apply_OrderCreated(self, event):
        self.customer_id = event.data['customer_id']
        self.status = 'created'
    
    def _apply_OrderItemAdded(self, event):
        self.items.append({
            'product_id': event.data['product_id'],
            'quantity': event.data['quantity'],
            'price': event.data['price']
        })
        self._recalculate_total()
    
    def _apply_OrderConfirmed(self, event):
        self.status = 'confirmed'
    
    def _apply_OrderShipped(self, event):
        self.status = 'shipped'
    
    def _recalculate_total(self):
        self.total = sum(
            item['quantity'] * item['price']
            for item in self.items
        )
```

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│              EVENT SOURCING ARCHITECTURE                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Commands ──→ Command Handler ──→ Aggregate                 │
│                                         │                   │
│                                         ▼                   │
│                                   Event Store               │
│                                         │                   │
│                    ┌────────────────────┼────────────────┐  │
│                    ▼                    ▼                ▼  │
│              Projection 1        Projection 2    Projection 3│
│              (Read Model)        (Read Model)    (Analytics) │
│                    │                    │                │  │
│                    ▼                    ▼                ▼  │
│              Query Handler       Query Handler    Data Lake │
│                    │                    │                │  │
│                    ▼                    ▼                ▼  │
│                  API                  API              BI    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Event Flow

```
1. Client sends Command
2. Command Handler loads Aggregate
3. Aggregate validates business rules
4. Aggregate produces Event(s)
5. Events saved to Event Store
6. Events published to subscribers
7. Projections update read models
8. Query handlers return data from projections
```

## Implementation

### Command and Handler

```python
from abc import ABC, abstractmethod

class Command:
    pass

class CreateOrderCommand(Command):
    def __init__(self, customer_id: UUID, items: list):
        self.customer_id = customer_id
        self.items = items

class AddItemCommand(Command):
    def __init__(self, order_id: UUID, product_id: UUID, 
                 quantity: int, price: float):
        self.order_id = order_id
        self.product_id = product_id
        self.quantity = quantity
        self.price = price

class CommandHandler:
    def __init__(self, event_store: EventStore):
        self._event_store = event_store
    
    def handle_create_order(self, command: CreateOrderCommand):
        order_id = uuid4()
        event = OrderCreated(
            event_id=uuid4(),
            aggregate_id=order_id,
            timestamp=datetime.now(),
            version=1,
            event_type='OrderCreated',
            data={
                'customer_id': str(command.customer_id),
                'items': command.items
            }
        )
        self._event_store.append(order_id, [event])
        return order_id
    
    def handle_add_item(self, command: AddItemCommand):
        events = self._event_store.get_events(command.order_id)
        order = OrderAggregate(command.order_id)
        for event in events:
            order.apply(event)
        
        event = OrderItemAdded(
            event_id=uuid4(),
            aggregate_id=command.order_id,
            timestamp=datetime.now(),
            version=order.version + 1,
            event_type='OrderItemAdded',
            data={
                'product_id': str(command.product_id),
                'quantity': command.quantity,
                'price': command.price
            }
        )
        self._event_store.append(command.order_id, [event])
```

### Rebuilding State

```python
def rebuild_aggregate(aggregate_id: UUID, 
                      event_store: EventStore) -> OrderAggregate:
    """Rebuild aggregate from events"""
    order = OrderAggregate(aggregate_id)
    events = event_store.get_events(aggregate_id)
    
    for event in events:
        order.apply(event)
    
    return order

# Usage
order = rebuild_aggregate(order_id, event_store)
print(f"Order status: {order.status}")
print(f"Order total: {order.total}")
```

## Event Store

### Event Schema

```python
event_schema = {
    'event_id': 'UUID (primary key)',
    'aggregate_id': 'UUID (indexed)',
    'aggregate_type': 'String (indexed)',
    'event_type': 'String (indexed)',
    'version': 'Integer (per aggregate)',
    'timestamp': 'Timestamp (indexed)',
    'data': 'JSON (event payload)',
    'metadata': 'JSON (correlation, causation)'
}
```

### Database Implementation

```sql
-- Event store table
CREATE TABLE events (
    event_id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    version INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    data JSONB NOT NULL,
    metadata JSONB,
    UNIQUE(aggregate_id, version)
);

-- Indexes
CREATE INDEX idx_events_aggregate ON events(aggregate_id);
CREATE INDEX idx_events_type ON events(event_type);
CREATE INDEX idx_events_timestamp ON events(timestamp);

-- Snapshot table
CREATE TABLE snapshots (
    aggregate_id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100),
    version INTEGER,
    state JSONB,
    timestamp TIMESTAMP
);
```

### Snapshots

```python
class SnapshotStore:
    def __init__(self, snapshot_interval=100):
        self.snapshots = {}
        self.snapshot_interval = snapshot_interval
    
    def save_snapshot(self, aggregate: OrderAggregate):
        """Save aggregate snapshot"""
        self.snapshots[aggregate.id] = {
            'version': aggregate.version,
            'state': self._serialize(aggregate),
            'timestamp': datetime.now()
        }
    
    def load_snapshot(self, aggregate_id: UUID) -> dict:
        """Load aggregate snapshot"""
        return self.snapshots.get(aggregate_id)

def rebuild_with_snapshot(aggregate_id: UUID, 
                         event_store: EventStore,
                         snapshot_store: SnapshotStore) -> OrderAggregate:
    """Rebuild using snapshot + events"""
    # Load snapshot
    snapshot = snapshot_store.load_snapshot(aggregate_id)
    
    if snapshot:
        # Rebuild from snapshot
        order = deserialize_aggregate(snapshot['state'])
        # Load events after snapshot
        events = event_store.get_events_from(
            aggregate_id, snapshot['version'] + 1
        )
    else:
        # Rebuild from all events
        order = OrderAggregate(aggregate_id)
        events = event_store.get_events(aggregate_id)
    
    # Apply remaining events
    for event in events:
        order.apply(event)
    
    return order
```

## Aggregates

### Designing Aggregates

```python
class OrderAggregate:
    def __init__(self, order_id: UUID):
        self.id = order_id
        self.version = 0
        self._customer_id = None
        self._items = []
        self._status = None
        self._events = []
    
    # Business Methods
    def create(self, customer_id: UUID, items: list):
        """Create order - business logic"""
        if self._status is not None:
            raise ValueError("Order already exists")
        
        self._record_event(OrderCreated(
            event_id=uuid4(),
            aggregate_id=self.id,
            timestamp=datetime.now(),
            version=self.version + 1,
            event_type='OrderCreated',
            data={'customer_id': str(customer_id), 'items': items}
        ))
    
    def add_item(self, product_id: UUID, quantity: int, price: float):
        """Add item - enforces business rules"""
        if self._status != 'created':
            raise ValueError("Can only add items to created orders")
        
        if len(self._items) >= 100:
            raise ValueError("Order cannot have more than 100 items")
        
        self._record_event(OrderItemAdded(
            event_id=uuid4(),
            aggregate_id=self.id,
            timestamp=datetime.now(),
            version=self.version + 1,
            event_type='OrderItemAdded',
            data={
                'product_id': str(product_id),
                'quantity': quantity,
                'price': price
            }
        ))
    
    def confirm(self):
        """Confirm order"""
        if self._status != 'created':
            raise ValueError("Only created orders can be confirmed")
        
        if not self._items:
            raise ValueError("Cannot confirm empty order")
        
        self._record_event(OrderConfirmed(
            event_id=uuid4(),
            aggregate_id=self.id,
            timestamp=datetime.now(),
            version=self.version + 1,
            event_type='OrderConfirmed',
            data={'confirmed_at': datetime.now().isoformat()}
        ))
    
    def _record_event(self, event: Event):
        """Record new event"""
        self._events.append(event)
        self.apply(event)
    
    def get_uncommitted_events(self) -> list:
        """Get events not yet persisted"""
        return self._events
```

### Aggregate Boundaries

```
┌─────────────────────────────────────────────────────────────┐
│                    AGGREGATE BOUNDARIES                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Order Aggregate          │  Customer Aggregate            │
│  ┌───────────────────┐   │  ┌───────────────────┐         │
│  │ Order (Root)      │   │  │ Customer (Root)   │         │
│  │   ├── OrderItem 1 │   │  │   ├── Profile     │         │
│  │   ├── OrderItem 2 │   │  │   ├── Preferences │         │
│  │   └── OrderItem 3 │   │  │   └── Addresses   │         │
│  └───────────────────┘   │  └───────────────────┘         │
│          │               │          │                      │
│          └─── References by ID only ─┘                     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Projections

### Read Model

```python
class OrderProjection:
    def __init__(self, read_store):
        self._read_store = read_store
    
    def handle(self, event: Event):
        """Update read model based on event"""
        handler = getattr(self, f'_handle_{event.event_type}', None)
        if handler:
            handler(event)
    
    def _handle_OrderCreated(self, event):
        self._read_store.upsert('orders', {
            'id': str(event.aggregate_id),
            'customer_id': event.data['customer_id'],
            'status': 'created',
            'total': 0,
            'item_count': 0,
            'created_at': event.timestamp.isoformat()
        })
    
    def _handle_OrderItemAdded(self, event):
        order = self._read_store.find('orders', event.aggregate_id)
        order['item_count'] += 1
        order['total'] += event.data['quantity'] * event.data['price']
        self._read_store.upsert('orders', order)
    
    def _handle_OrderConfirmed(self, event):
        order = self._read_store.find('orders', event.aggregate_id)
        order['status'] = 'confirmed'
        order['confirmed_at'] = event.timestamp.isoformat()
        self._read_store.upsert('orders', order)

# Multiple projections for different queries
class OrderSummaryProjection:
    """Optimized for order list queries"""
    pass

class OrderDetailProjection:
    """Optimized for order detail queries"""
    pass

class OrderAnalyticsProjection:
    """Optimized for analytics queries"""
    pass
```

## Benefits

### Complete Audit Trail

```python
# Every change is recorded
events = event_store.get_events(order_id)
for event in events:
    print(f"{event.timestamp}: {event.event_type}")
    print(f"  Data: {event.data}")
```

### Temporal Queries

```python
def get_order_at_time(order_id: UUID, timestamp: datetime):
    """Get order state at specific point in time"""
    events = event_store.get_events(order_id)
    relevant_events = [
        e for e in events 
        if e.timestamp <= timestamp
    ]
    
    order = OrderAggregate(order_id)
    for event in relevant_events:
        order.apply(event)
    
    return order
```

### Event Replay

```python
# Replay events to rebuild state
order = OrderAggregate(order_id)
events = event_store.get_events(order_id)

for event in events:
    order.apply(event)

# Or rebuild projections
for event in all_events:
    projection.handle(event)
```

### Debugging

```python
# Debug by replaying events
def debug_order(order_id):
    events = event_store.get_events(order_id)
    order = OrderAggregate(order_id)
    
    for event in events:
        print(f"Applying {event.event_type}")
        order.apply(event)
        print(f"  State: {order.__dict__}")
```

## Trade-offs

### Pros

- Complete history of all changes
- Temporal queries possible
- Event replay for debugging
- Natural fit for event-driven architectures
- Enables CQRS naturally

### Cons

- Increased complexity
- Event schema evolution challenging
- Eventually consistent projections
- Larger storage requirements
- Learning curve

## Best Practices

### 1. Use Immutable Events

```python
@dataclass(frozen=True)
class OrderCreated:
    order_id: UUID
    customer_id: UUID
    timestamp: datetime
```

### 2. Include Metadata

```python
event = OrderCreated(
    order_id=order_id,
    customer_id=customer_id,
    timestamp=datetime.now(),
    metadata={
        'correlation_id': correlation_id,
        'causation_id': command_id,
        'user_id': user_id,
        'ip_address': ip_address
    }
)
```

### 3. Version Events

```python
@dataclass(frozen=True)
class OrderCreated:
    schema_version: int = 1  # For schema evolution
    order_id: UUID = None
    customer_id: UUID = None
```

### 4. Use Snapshots

```python
# Save snapshots periodically
if event_count % 100 == 0:
    snapshot_store.save_snapshot(aggregate)
```

### 5. Keep Events Small

```python
# Event should contain what changed, not full state
# Good
event = OrderItemAdded(
    product_id=product_id,
    quantity=1,
    price=29.99
)

# Bad - too much data
event = OrderUpdated(
    full_order=order.to_dict()  # Large payload
)
```

## Further Reading

- [Implementing Domain-Driven Design - Vaughn Vernon](https://www.amazon.com/Implementing-Domain-Driven-Design-Vaughn-Vernon/dp/0321834574)
- [Event Sourcing on Wikipedia](https://en.wikipedia.org/wiki/Event_sourcing)
- [Martin Fowler on Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html)
