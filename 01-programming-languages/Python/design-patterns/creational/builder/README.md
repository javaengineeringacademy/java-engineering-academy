# Builder Pattern in Python

The Builder pattern separates the construction of a complex object from its representation, allowing the same construction process to create different representations. Python's dataclasses and fluent interfaces make this pattern particularly clean.

## When to Use

- Constructing complex objects step by step
- Creating objects with many optional parameters
- Needing different representations of the same object
- Avoiding telescoping constructor anti-pattern
- When construction involves multiple steps or processes

## Python Implementation

### Fluent Interface Builder
```python
from dataclasses import dataclass, field
from typing import Optional

@dataclass
class HttpRequest:
    method: str = "GET"
    url: str = ""
    headers: dict = field(default_factory=dict)
    body: Optional[str] = None
    timeout: int = 30

class HttpRequestBuilder:
    def __init__(self):
        self._request = HttpRequest()
    
    def method(self, method: str) -> "HttpRequestBuilder":
        self._request.method = method
        return self
    
    def url(self, url: str) -> "HttpRequestBuilder":
        self._request.url = url
        return self
    
    def header(self, key: str, value: str) -> "HttpRequestBuilder":
        self._request.headers[key] = value
        return self
    
    def body(self, body: str) -> "HttpRequestBuilder":
        self._request.body = body
        return self
    
    def timeout(self, seconds: int) -> "HttpRequestBuilder":
        self._request.timeout = seconds
        return self
    
    def build(self) -> HttpRequest:
        return self._request

# Usage
request = (HttpRequestBuilder()
    .method("POST")
    .url("https://api.example.com/data")
    .header("Content-Type", "application/json")
    .body('{"key": "value"}')
    .timeout(60)
    .build())
```

### Dataclass with Factory Method
```python
from dataclasses import dataclass, field
from typing import List

@dataclass
class Pizza:
    size: str
    cheese: bool = False
    toppings: List[str] = field(default_factory=list)
    
    @classmethod
    def builder(cls, size: str):
        return PizzaBuilder(cls, size)

class PizzaBuilder:
    def __init__(self, cls, size: str):
        self._pizza = cls(size=size)
    
    def add_cheese(self):
        self._pizza.cheese = True
        return self
    
    def add_topping(self, topping: str):
        self._pizza.toppings.append(topping)
        return self
    
    def build(self):
        return self._pizza

# Usage
pizza = (Pizza.builder("large")
    .add_cheese()
    .add_topping("pepperoni")
    .add_topping("mushrooms")
    .build())
```

## Pythonic Alternative

Use dataclasses with defaults for simple cases:
```python
from dataclasses import dataclass, field

@dataclass
class ServerConfig:
    host: str = "localhost"
    port: int = 8080
    debug: bool = False
    allowed_origins: List[str] = field(default_factory=list)
    database_url: str = "sqlite:///db.sqlite3"
```

## Real-World Example

```python
class QueryBuilder:
    def __init__(self, table: str):
        self._table = table
        self._conditions = []
        self._order_by = None
        self._limit = None
    
    def where(self, condition: str) -> "QueryBuilder":
        self._conditions.append(condition)
        return self
    
    def order_by(self, field: str, desc: bool = False) -> "QueryBuilder":
        self._order_by = f"{field} {'DESC' if desc else 'ASC'}"
        return self
    
    def limit(self, count: int) -> "QueryBuilder":
        self._limit = count
        return self
    
    def build(self) -> str:
        query = f"SELECT * FROM {self._table}"
        if self._conditions:
            query += " WHERE " + " AND ".join(self._conditions)
        if self._order_by:
            query += f" ORDER BY {self._order_by}"
        if self._limit:
            query += f" LIMIT {self._limit}"
        return query
```

## Best Practices

1. Use `@dataclass` to reduce boilerplate
2. Implement fluent interface by returning `self`
3. Validate invariants in `build()` method
4. Make builder state explicit and copyable
5. Consider using NamedTuple for immutable products

## Interview Questions

1. How does Builder differ from Factory Method?
2. What is the telescoping constructor problem?
3. How would you implement a thread-safe Builder?
4. When would you use a director class with Builder?
5. What are the trade-offs of fluent interface builders?

## References

- *Design Patterns* - GoF, Chapter 3
- Python dataclasses documentation
- PEP 557 - Data Classes
- *Fluent Python* - Luciano Ramalho
