# TDD Basics: Red-Green-Refactor

## Overview
Test-Driven Development (TDD) writes tests before production code. Cycle: Red-Green-Refactor.

## The TDD Cycle
1. **RED**: Write a failing test
2. **GREEN**: Write minimum code to pass
3. **REFACTOR**: Clean up code
4. Repeat

## Red Phase
```java
@Test
void shouldCalculateTotalPrice() {
    ShoppingCart cart = new ShoppingCart();
    cart.add(new Product("Widget", 10.00), 2);
    cart.add(new Product("Gadget", 25.00), 1);
    assertEquals(45.00, cart.getTotal(), 0.01);
}
```

## Green Phase
```java
public class ShoppingCart {
    private final List<CartItem> items = new ArrayList<>();
    public void add(Product product, int quantity) {
        items.add(new CartItem(product, quantity));
    }
    public double getTotal() {
        return items.stream()
            .mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }
}
```

## Refactor Phase
```java
public class ShoppingCart {
    private final List<CartItem> items = new ArrayList<>();
    public void add(Product product, int quantity) {
        items.add(new CartItem(product, quantity));
    }
    public Money getTotal() {
        return items.stream().map(CartItem::getLineTotal)
            .reduce(Money.ZERO, Money::add);
    }
    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}
```

## Complete Example: User Registration
Step 1: Test -> Step 2: Implement -> Step 3: Add validation -> Step 4: Refactor

## TDD Rules
1. Write test first
2. Write minimal code
3. Refactor frequently
4. Fast, deterministic, independent tests

## Benefits
- Better design with decoupled interfaces
- Living documentation
- Confidence for safe refactoring
- Fewer bugs, faster debugging
