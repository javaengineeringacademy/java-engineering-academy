# Strategy Pattern in Python

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. Python's first-class functions make this pattern particularly elegant.

## When to Use

- Multiple algorithms for a specific task
- Switching algorithms at runtime
- Avoiding conditional statements for algorithm selection
- Sorting, validation, pricing, or compression strategies

## Python Implementation

### Function-Based Strategy
```python
from typing import Callable, List

def bubble_sort(data: List) -> List:
    arr = data.copy()
    n = len(arr)
    for i in range(n):
        for j in range(0, n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    return arr

def quick_sort(data: List) -> List:
    if len(data) <= 1:
        return data
    pivot = data[len(data) // 2]
    left = [x for x in data if x < pivot]
    middle = [x for x in data if x == pivot]
    right = [x for x in data if x > pivot]
    return quick_sort(left) + middle + quick_sort(right)

class Sorter:
    def __init__(self, strategy: Callable = None):
        self._strategy = strategy or bubble_sort

    @property
    def strategy(self):
        return self._strategy

    @strategy.setter
    def strategy(self, strategy: Callable):
        self._strategy = strategy

    def sort(self, data: List) -> List:
        return self._strategy(data)
```

### Class-Based Strategy
```python
from abc import ABC, abstractmethod

class PricingStrategy(ABC):
    @abstractmethod
    def calculate_price(self, base_price: float) -> float:
        pass

class RegularPricing(PricingStrategy):
    def calculate_price(self, base_price: float) -> float:
        return base_price

class PremiumPricing(PricingStrategy):
    def calculate_price(self, base_price: float) -> float:
        return base_price * 0.85

class Product:
    def __init__(self, name: str, base_price: float, pricing: PricingStrategy):
        self.name = name
        self.base_price = base_price
        self.pricing = pricing

    def final_price(self):
        return self.pricing.calculate_price(self.base_price)
```

## Pythonic Alternative

Use functions directly as strategies:
```python
strategies = {
    "fast": lambda x: sorted(x),
    "memory": lambda x: (x.sort(), x)[-1]
}

def process(data, strategy="fast"):
    return strategies[strategy](data)
```

## Real-World Example

```python
class NotificationService:
    def __init__(self, strategy=None):
        self._strategy = strategy or self._email_strategy

    def notify(self, recipient: str, message: str):
        return self._strategy(recipient, message)

    @staticmethod
    def _email_strategy(recipient, message):
        print(f"Email to {recipient}: {message}")
        return True

    @staticmethod
    def _sms_strategy(recipient, message):
        print(f"SMS to {recipient}: {message}")
        return True
```

## Best Practices

1. Use functions as strategies when stateless
2. Use classes for strategies with state
3. Keep strategy interface consistent
4. Document expected strategy behavior
5. Consider default strategy fallback

## Interview Questions

1. How does Strategy differ from polymorphism?
2. When would you use function vs class strategies?
3. How do you handle strategy selection at runtime?
4. What are the performance implications of Strategy?
5. How would you add a new strategy without modifying existing code?

## References

- *Design Patterns* - GoF, Chapter 5
- *Fluent Python* - Luciano Ramalho
- Python `typing.Protocol` documentation
