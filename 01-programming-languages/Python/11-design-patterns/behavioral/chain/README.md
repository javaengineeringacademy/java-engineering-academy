# Chain of Responsibility Pattern in Python

The Chain of Responsibility pattern passes requests along a chain of handlers, where each handler decides either to process the request or pass it to the next handler. Python's function composition makes this pattern flexible.

## When to Use

- Multiple objects may handle a request, and handler determined at runtime
- Wanting to issue a request to one of several objects without specifying receiver
- Handler set should be specified dynamically
- Logging, authentication, validation pipelines
- Event bubbling in GUI frameworks

## Python Implementation

### Class-Based Chain
```python
from abc import ABC, abstractmethod
from typing import Optional

class Handler(ABC):
    def __init__(self):
        self._next: Optional[Handler] = None

    def set_next(self, handler: "Handler") -> "Handler":
        self._next = handler
        return handler

    @abstractmethod
    def handle(self, request: dict) -> Optional[str]:
        pass

    def pass_to_next(self, request: dict) -> Optional[str]:
        if self._next:
            return self._next.handle(request)
        return None

class AuthHandler(Handler):
    def handle(self, request: dict) -> Optional[str]:
        if not request.get("token"):
            return "Auth failed: No token"
        print("Auth passed")
        return self.pass_to_next(request)

class ValidationHandler(Handler):
    def handle(self, request: dict) -> Optional[str]:
        if not request.get("data"):
            return "Validation failed: No data"
        print("Validation passed")
        return self.pass_to_next(request)

class ProcessingHandler(Handler):
    def handle(self, request: dict) -> Optional[str]:
        print("Processing complete")
        return "Success"

# Usage
auth = AuthHandler()
validation = ValidationHandler()
processing = ProcessingHandler()

auth.set_next(validation).set_next(processing)

result = auth.handle({"token": "abc", "data": "test"})
print(result)  # Success
```

### Function-Based Chain
```python
from typing import Callable, Any

def create_chain(*handlers: Callable) -> Callable:
    def chain(request):
        for handler in handlers:
            result = handler(request)
            if result is not None:
                return result
        return None
    return chain

def auth_check(request):
    if not request.get("token"):
        return "Auth failed"
    return None

def validate_data(request):
    if not request.get("data"):
        return "Validation failed"
    return None

def process(request):
    return "Success"

pipeline = create_chain(auth_check, validate_data, process)
print(pipeline({"token": "abc", "data": "test"}))  # Success
```

### Middleware Pattern
```python
class Middleware:
    def __init__(self):
        self._stack = []

    def add(self, middleware: Callable):
        self._stack.append(middleware)
        return self

    def execute(self, request):
        for middleware in reversed(self._stack):
            request = middleware(request)
        return request

# Usage
middleware = Middleware()
middleware.add(lambda req: {**req, "authenticated": True})
middleware.add(lambda req: {**req, "validated": True})

result = middleware.execute({"data": "test"})
print(result)  # {'data': 'test', 'authenticated': True, 'validated': True}
```

## Pythonic Alternative

Use function composition or middleware stacks:
```python
def pipeline(*funcs):
    def wrapper(data):
        for func in funcs:
            data = func(data)
        return data
    return wrapper

process = pipeline(
    lambda x: x.strip(),
    lambda x: x.upper(),
    lambda x: f"[{x}]"
)

print(process("  hello  "))  # [HELLO]
```

## Real-World Example

```python
class RateLimiter:
    def __init__(self, limit: int):
        self.limit = limit
        self._counts = {}
        self._next = None

    def set_next(self, handler):
        self._next = handler
        return handler

    def handle(self, request):
        client = request.get("client", "unknown")
        self._counts[client] = self._counts.get(client, 0) + 1

        if self._counts[client] > self.limit:
            return {"status": 429, "error": "Rate limit exceeded"}

        if self._next:
            return self._next.handle(request)
        return {"status": 200}
```

## Best Practices

1. Keep handlers focused on single responsibility
2. Ensure chain termination
3. Consider order of handlers carefully
4. Use logging to debug chain flow
5. Document handler precedence

## Interview Questions

1. How does Chain of Responsibility differ from Decorator?
2. When would you use chain over direct dispatch?
3. How do you handle chain termination?
4. How would you make chain handlers asynchronous?
5. What are performance implications of long chains?

## References

- *Design Patterns* - GoF, Chapter 5
- WSGI middleware documentation
- *Python Cookbook* - Alex Martelli
