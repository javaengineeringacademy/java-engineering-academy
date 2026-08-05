# Aggregate Root Pattern

## Overview

An Aggregate Root is the single entry point to a cluster of associated domain objects treated as a unit for data changes. The Aggregate Root owns and controls all members within its boundary, enforcing invariants and ensuring consistency within the cluster.

In domain-driven design, aggregates define transactional consistency boundaries. All modifications to objects within an aggregate must go through the root, which validates and coordinates changes.

## When to Use

- Domain objects form natural clusters with lifecycle dependencies
- Multiple objects must be saved or deleted together for consistency
- Invariant rules span across multiple related entities
- Transaction boundaries need to align with business operations
- Complex domain logic requires clear ownership and control

## Implementation

### TypeScript

```typescript
class Order {
  private _items: OrderItem[] = [];
  private _total: Money;

  constructor(
    public readonly id: string,
    public readonly customerId: string
  ) {
    this._total = new Money(0, 'USD');
  }

  get items(): ReadonlyArray<OrderItem> {
    return [...this._items];
  }

  get total(): Money {
    return this._total;
  }

  addItem(productId: string, price: Money, quantity: number): void {
    if (quantity <= 0) throw new Error('Quantity must be positive');
    const existing = this._items.find(i => i.productId === productId);
    if (existing) {
      existing.increaseQuantity(quantity);
    } else {
      this._items.push(new OrderItem(productId, price, quantity));
    }
    this.recalculateTotal();
  }

  removeItem(productId: string): void {
    this._items = this._items.filter(i => i.productId !== productId);
    this.recalculateTotal();
  }

  private recalculateTotal(): void {
    this._total = this._items.reduce(
      (sum, item) => sum.add(item.subtotal),
      new Money(0, 'USD')
    );
  }
}

class OrderRepository {
  async save(order: Order): Promise<void> {
    await this.db.transaction(async (tx) => {
      await tx.query('UPDATE orders SET total = $1 WHERE id = $2',
        [order.total.amount, order.id]);
      await tx.query('DELETE FROM order_items WHERE order_id = $1', [order.id]);
      for (const item of order.items) {
        await tx.query(
          'INSERT INTO order_items (order_id, product_id, price, quantity) VALUES ($1, $2, $3, $4)',
          [order.id, item.productId, item.price.amount, item.quantity]
        );
      }
    });
  }
}
```

### Java

```java
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String id;
    private String customerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(String productId, Money price, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        items.add(new OrderItem(productId, price, quantity));
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    public Money getTotal() {
        return items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(Money.ZERO, Money::add);
    }
}
```

### Python

```python
from dataclasses import dataclass, field
from typing import List

@dataclass
class Order:
    id: str
    customer_id: str
    _items: List['OrderItem'] = field(default_factory=list, init=False)

    @property
    def items(self) -> List['OrderItem']:
        return self._items.copy()

    def add_item(self, product_id: str, price: Money, quantity: int):
        if quantity <= 0:
            raise ValueError('Quantity must be positive')
        existing = next((i for i in self._items if i.product_id == product_id), None)
        if existing:
            existing.increase_quantity(quantity)
        else:
            self._items.append(OrderItem(product_id, price, quantity))

    def remove_item(self, product_id: str):
        self._items = [i for i in self._items if i.product_id != product_id]

    def get_total(self) -> Money:
        total = Money(0, 'USD')
        for item in self._items:
            total = total.add(item.subtotal)
        return total
```

### C\#

```csharp
public class Order {
    public string Id { get; }
    public string CustomerId { get; }
    private readonly List<OrderItem> _items = new();

    public IReadOnlyList<OrderItem> Items => _items.AsReadOnly();

    public Order(string id, string customerId) {
        Id = id;
        CustomerId = customerId;
    }

    public void AddItem(string productId, Money price, int quantity) {
        if (quantity <= 0) throw new ArgumentException("Quantity must be positive");
        var existing = _items.FirstOrDefault(i => i.ProductId == productId);
        if (existing != null) {
            existing.IncreaseQuantity(quantity);
        } else {
            _items.Add(new OrderItem(productId, price, quantity));
        }
    }

    public Money Total => _items.Aggregate(
        Money.Zero,
        (sum, item) => sum.Add(item.Subtotal)
    );
}
```

## Best Practices

- Keep aggregates small to minimize lock contention and improve performance
- Reference other aggregates by identity, not by direct object reference
- Use idempotent commands where possible for aggregate operations
- Ensure aggregate roots have globally unique identifiers
- Persist entire aggregates as a unit within a single transaction
- Avoid bidirectional references between aggregates

## Interview Questions

1. What defines the boundary of an aggregate?
2. How do aggregates handle references to other aggregates?
3. What is the relationship between aggregates and transaction boundaries?
4. When should you split a large aggregate into smaller ones?
5. How do aggregates relate to eventual consistency in distributed systems?

## References

- Evans, Eric. *Domain-Driven Design*, Aggregates chapter
- Vernon, Vaughn. *Implementing Domain-Driven Design*, chapter on Aggregates
- Richardson, Chris. *Microservices Patterns*, chapter on Saga pattern
- Vaughn Vernon. *Designing Aggregate Boundaries*
