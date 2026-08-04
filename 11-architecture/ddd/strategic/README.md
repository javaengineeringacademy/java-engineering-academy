# Strategic DDD

## Overview

Strategic design identifies bounded contexts and their relationships.

## Key Concepts

- **Bounded Context**: Logical boundary for domain model
- **Ubiquitous Language**: Shared team language
- **Context Map**: Relationships between contexts

## Bounded Contexts

```python
# Sales Context
class Order:
    def __init__(self, customer_id, items):
        self.customer_id = customer_id
        self.items = items

# Shipping Context
class Shipment:
    def __init__(self, order_id, address):
        self.order_id = order_id
        self.address = address
```

## Context Mapping Patterns

- **Shared Kernel**: Shared model between contexts
- **Customer-Supplier**: Upstream/downstream
- **Anti-Corruption Layer**: Translation layer
- **Open Host Service**: Public API
- **Published Language**: Shared specification
