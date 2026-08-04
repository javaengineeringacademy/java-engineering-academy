# Strategic DDD

## Overview

Identify bounded contexts and their relationships.

## Bounded Context

A logical boundary within which a particular domain model applies.

```python
# Sales Context
class Order:
    def __init__(self, customer_id, items):
        pass

# Shipping Context  
class Shipment:
    def __init__(self, order_id, destination):
        pass
```

## Context Map

Visual representation of context relationships.

## Patterns

- Shared Kernel
- Customer-Supplier
- Anti-Corruption Layer
- Open Host Service
- Published Language
