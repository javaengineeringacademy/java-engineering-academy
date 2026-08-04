# Context Mapping

## Overview

Defines relationships between bounded contexts.

## Patterns

### Partnership
Two contexts evolve together

### Customer-Supplier
Upstream provides to downstream

### Conformist
Downstream adapts to upstream

### Anti-Corruption Layer
Translation layer between contexts

### Shared Kernel
Shared model between contexts

### Open Host Service
Public API for integration

### Published Language
Shared specification

## Implementation

```python
# ACL Pattern
class SalesToShippingAdapter:
    def translate(self, sales_order):
        return ShippingOrder(
            order_id=sales_order.id,
            items=[self.translate_item(i) for i in sales_order.items]
        )
```
