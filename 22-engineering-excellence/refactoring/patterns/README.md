# Refactoring Patterns

## Catalog of Patterns

### Extract Class
Split class into two based on responsibilities

### Inline Class
Merge class into another

### Move Method/Field
Move between classes

### Replace Conditional with Polymorphism
Use inheritance for variations

### Introduce Parameter Object
Group related parameters

### Preserve Whole Object
Pass object instead of individual values

### Replace Magic Number with Named Constant

### Decompose Conditional

### Consolidate Duplicate Conditional Fragments

## Example

```python
# Before
def calculate_discount(order):
    if order.total > 1000:
        return order.total * 0.1
    elif order.total > 500:
        return order.total * 0.05
    return 0

# After
class DiscountCalculator:
    def calculate(self, order):
        for rule in self.rules:
            if rule.applies(order):
                return rule.calculate(order)
        return 0
```
