# Tactical DDD

## Overview

Tactical patterns for implementing domain models within bounded contexts.

## Patterns

### Entity

```python
class Order:
    def __init__(self, order_id):
        self.id = order_id  # Identity
    
    def __eq__(self, other):
        return self.id == other.id
```

### Value Object

```python
class Money:
    def __init__(self, amount, currency):
        self.amount = amount
        self.currency = currency
    
    def __eq__(self, other):
        return self.amount == other.amount and self.currency == other.currency
```

### Aggregate

```python
class Order:
    def __init__(self, id):
        self.id = id
        self.items = []
    
    def add_item(self, item):
        self.items.append(item)
        # Enforce invariants
        if len(self.items) > 100:
            raise ValueError("Too many items")
```

### Domain Event

```python
class OrderCreated:
    def __init__(self, order_id, items):
        self.order_id = order_id
        self.items = items
        self.timestamp = datetime.now()
```
