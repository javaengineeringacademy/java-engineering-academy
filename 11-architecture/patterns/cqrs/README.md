# CQRS (Command Query Responsibility Segregation)

## Overview

CQRS is a pattern that separates read and write operations into different models. Commands change state (write), while Queries read state (read). This separation allows independent optimization, scaling, and evolution of read and write concerns.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Architecture](#architecture)
- [Implementation](#implementation)
- [Read and Write Models](#read-and-write-models)
- [Synchronization](#synchronization)
- [Benefits](#benefits)
- [Trade-offs](#trade-offs)
- [Best Practices](#best-practices)

## Core Concepts

```
┌─────────────────────────────────────────────────────────────┐
│                    CQRS ARCHITECTURE                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Commands ──→ Write Model ──→ Event Store                   │
│                 │                       │                   │
│                 ▼                       ▼                   │
│           Write Database          Event Bus                 │
│                                         │                   │
│                                         ▼                   │
│                                   Read Model                │
│                                         │                   │
│  Queries ──→ Read Model ──→ Read Database                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Key Principles

| Concept | Description |
|---------|-------------|
| Command | Intent to change state |
| Query | Request for data |
| Write Model | Domain logic, business rules |
| Read Model | Optimized for queries |
| Separation | Read and write are independent |

## Architecture

### Separate Models

```python
# Write Model - Domain-focused
class Order:
    def __init__(self, id, customer_id):
        self.id = id
        self.customer_id = customer_id
        self.items = []
        self.status = 'created'
    
    def add_item(self, product_id, quantity, price):
        """Business logic"""
        if self.status != 'created':
            raise ValueError("Cannot add items to non-created order")
        self.items.append({
            'product_id': product_id,
            'quantity': quantity,
            'price': price
        })

# Read Model - Query-focused
class OrderReadModel:
    def __init__(self, id, customer_name, status, total, item_count):
        self.id = id
        self.customer_name = customer_name
        self.status = status
        self.total = total
        self.item_count = item_count
```

### Separate Databases

```
┌─────────────────────────────────────────────────────────────┐
│                    SEPARATE DATABASES                        │
├─────────────────────────────────────────────────────────────┤
│  Write Database (OLTP)          Read Database (OLAP)        │
│  ┌─────────────────┐           ┌─────────────────┐         │
│  │ Normalized       │           │ Denormalized    │         │
│  │ 3NF/BCNF        │           │ Star/Snowflake  │         │
│  │ Optimized for   │           │ Optimized for   │         │
│  │ writes          │           │ reads           │         │
│  └─────────────────┘           └─────────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

## Implementation

### Command Side

```python
from abc import ABC, abstractmethod
from datetime import datetime
from uuid import UUID, uuid4

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
    def __init__(self, repository, event_store):
        self._repository = repository
        self._event_store = event_store
    
    def handle_create_order(self, command: CreateOrderCommand):
        # Create aggregate
        order = Order(
            id=uuid4(),
            customer_id=command.customer_id
        )
        
        # Add initial items
        for item in command.items:
            order.add_item(item['product_id'], 
                          item['quantity'], 
                          item['price'])
        
        # Save
        self._repository.save(order)
        
        # Publish events
        for event in order.get_uncommitted_events():
            self._event_store.append(event)
        
        return order.id
    
    def handle_add_item(self, command: AddItemCommand):
        # Load aggregate
        order = self._repository.find(command.order_id)
        
        # Execute business logic
        order.add_item(
            command.product_id,
            command.quantity,
            command.price
        )
        
        # Save
        self._repository.save(order)
        
        # Publish events
        for event in order.get_uncommitted_events():
            self._event_store.append(event)
```

### Query Side

```python
class Query:
    pass

class GetOrderQuery(Query):
    def __init__(self, order_id: UUID):
        self.order_id = order_id

class GetOrdersByCustomerQuery(Query):
    def __init__(self, customer_id: UUID):
        self.customer_id = customer_id

class QueryHandler:
    def __init__(self, read_store):
        self._read_store = read_store
    
    def handle_get_order(self, query: GetOrderQuery):
        return self._read_store.find('orders', query.order_id)
    
    def handle_get_customer_orders(self, query: GetOrdersByCustomerQuery):
        return self._read_store.query(
            'orders',
            {'customer_id': query.customer_id}
        )
```

### Event Handler / Projection

```python
class OrderProjection:
    def __init__(self, read_store):
        self._read_store = read_store
    
    def handle(self, event):
        """Update read model"""
        if event.event_type == 'OrderCreated':
            self._read_store.upsert('orders', {
                'id': str(event.aggregate_id),
                'customer_id': event.data['customer_id'],
                'status': 'created',
                'total': 0,
                'item_count': 0,
                'created_at': event.timestamp.isoformat()
            })
        
        elif event.event_type == 'OrderItemAdded':
            order = self._read_store.find('orders', event.aggregate_id)
            order['item_count'] += 1
            order['total'] += event.data['quantity'] * event.data['price']
            self._read_store.upsert('orders', order)
        
        elif event.event_type == 'OrderConfirmed':
            order = self._read_store.find('orders', event.aggregate_id)
            order['status'] = 'confirmed'
            order['confirmed_at'] = event.timestamp.isoformat()
            self._read_store.upsert('orders', order)
```

## Read and Write Models

### Write Model (Domain)

```python
class Order:
    """Write model - rich domain logic"""
    
    def __init__(self, id, customer_id):
        self.id = id
        self.customer_id = customer_id
        self.items = []
        self.status = 'created'
        self._events = []
    
    def add_item(self, product_id, quantity, price):
        """Business rule validation"""
        if self.status != 'created':
            raise InvalidOrderStateError("Cannot add items")
        
        if quantity <= 0:
            raise ValueError("Quantity must be positive")
        
        self.items.append({
            'product_id': product_id,
            'quantity': quantity,
            'price': price
        })
        
        self._record_event(OrderItemAdded(
            product_id=product_id,
            quantity=quantity,
            price=price
        ))
    
    def confirm(self):
        """Business rule validation"""
        if not self.items:
            raise ValueError("Cannot confirm empty order")
        
        self.status = 'confirmed'
        self._record_event(OrderConfirmed())
    
    def calculate_total(self):
        return sum(
            item['quantity'] * item['price']
            for item in self.items
        )
```

### Read Model (Denormalized)

```python
class OrderReadModel:
    """Read model - optimized for queries"""
    
    def __init__(self):
        self.id = None
        self.customer_name = None
        self.customer_email = None
        self.status = None
        self.total = 0
        self.item_count = 0
        self.items = []
        self.created_at = None
        self.confirmed_at = None
        self.shipped_at = None
    
    def to_dict(self):
        return {
            'id': self.id,
            'customer_name': self.customer_name,
            'customer_email': self.customer_email,
            'status': self.status,
            'total': self.total,
            'item_count': self.item_count,
            'items': self.items,
            'created_at': self.created_at,
            'confirmed_at': self.confirmed_at,
            'shipped_at': self.shipped_at
        }
```

### Multiple Read Models

```python
# Different projections for different use cases
class OrderListProjection:
    """Optimized for order list view"""
    def project(self, event):
        # Minimal data for list view
        pass

class OrderDetailProjection:
    """Optimized for order detail view"""
    def project(self, event):
        # Full order data
        pass

class OrderSummaryProjection:
    """Optimized for dashboard"""
    def project(self, event):
        # Aggregated data
        pass

class OrderAnalyticsProjection:
    """Optimized for analytics"""
    def project(self, event):
        # Denormalized for analytics queries
        pass
```

## Synchronization

### Event-Driven Synchronization

```python
class EventBus:
    def __init__(self):
        self._handlers = {}
    
    def subscribe(self, event_type, handler):
        if event_type not in self._handlers:
            self._handlers[event_type] = []
        self._handlers[event_type].append(handler)
    
    def publish(self, event):
        handlers = self._handlers.get(event.event_type, [])
        for handler in handlers:
            handler(event)

# Setup
event_bus = EventBus()
event_bus.subscribe('OrderCreated', order_projection.handle)
event_bus.subscribe('OrderItemAdded', order_projection.handle)
event_bus.subscribe('OrderConfirmed', order_projection.handle)
```

### Consistency Patterns

```python
# Strong consistency (same database)
class SynchronizedWrite:
    def handle(self, command):
        with transaction():
            # Write to write model
            order = self.write_model.save(command)
            
            # Update read model in same transaction
            self.read_model.update(order)
            
            # Both updated atomically

# Eventual consistency (different databases)
class EventualConsistency:
    def handle(self, command):
        # Write to write model
        order = self.write_model.save(command)
        
        # Publish event
        self.event_bus.publish(OrderCreated(order))
        
        # Read model updated asynchronously
```

## Benefits

### Independent Scaling

```python
# Scale read and write independently
class ReadDatabase:
    """Can have multiple read replicas"""
    def __init__(self, replicas):
        self.replicas = replicas
    
    def read(self, query):
        # Load balance across replicas
        replica = self.get_least_loaded_replica()
        return replica.query(query)

class WriteDatabase:
    """Single primary for consistency"""
    def write(self, data):
        return self.primary.insert(data)
```

### Optimized Models

```python
# Write model - normalized, transactional
class WriteModel:
    def save(self, order):
        # Normalized tables
        self.db.insert('orders', order.header)
        for item in order.items:
            self.db.insert('order_items', item)

# Read model - denormalized, query-optimized
class ReadModel:
    def find(self, order_id):
        # Single query for all data
        return self.db.query("""
            SELECT * FROM order_summary
            WHERE id = %s
        """, order_id)
```

### Independent Evolution

```python
# Write model can evolve independently
class WriteModelV2:
    def save(self, order):
        # New write model with additional validation
        pass

# Read model can evolve independently
class ReadModelV2:
    def find(self, order_id):
        # New read model with additional fields
        pass
```

## Trade-offs

### Pros

- Independent scaling of reads and writes
- Optimized models for each operation
- Easier to maintain and evolve
- Natural fit for event-driven architectures
- Better performance for complex queries

### Cons

- Increased complexity
- Eventual consistency
- More infrastructure
- Learning curve
- Synchronization challenges

## Best Practices

### 1. Start Simple

```python
# Start with single model, add CQRS when needed
class Order:
    """Simple model first"""
    def add_item(self, product_id, quantity):
        self.items.append(item)
    
    def get_summary(self):
        """Query method on same model"""
        return {
            'total': self.calculate_total(),
            'items': len(self.items)
        }
```

### 2. Use Events for Synchronization

```python
# Events keep read and write models in sync
class OrderService:
    def create_order(self, command):
        order = Order.create(command)
        self.repository.save(order)
        
        # Publish event for read model
        self.event_bus.publish(OrderCreatedEvent(order))
```

### 3. Handle Eventual Consistency

```python
# Provide consistency tokens
class OrderAPI:
    def create_order(self, request):
        order_id = self.command_handler.handle(request)
        
        # Return consistency token
        return {
            'order_id': order_id,
            'version': 1,
            'consistency_token': str(uuid4())
        }
    
    def get_order(self, order_id, consistency_token=None):
        if consistency_token:
            # Wait for consistency
            self.wait_for_version(order_id, consistency_token)
        
        return self.query_handler.handle(GetOrderQuery(order_id))
```

### 4. Separate Concerns Clearly

```python
# Write side: Business logic only
class Order:
    def confirm(self):
        if not self.items:
            raise ValueError("Empty order")
        self.status = 'confirmed'

# Read side: Query logic only
class OrderRepository:
    def find(self, order_id):
        return self.db.query("SELECT * FROM orders WHERE id = %s", order_id)
```

## Further Reading

- [CQRS by Greg Young](https://cqrs.files.wordpress.com/2010/11/cqrs_documents.pdf)
- [Martin Fowler on CQRS](https://martinfowler.com/bliki/CQRS.html)
- [Event Sourcing and CQRS](https://www.eventstore.com/event-sourcing-and-cqrs)
