"""
Decorator Pattern in Python
Demonstrates the Decorator pattern for adding behavior dynamically
"""

from typing import Any, Callable
from abc import ABC, abstractmethod

# ============================================
# Component Interface
# ============================================

class Component(ABC):
    """Abstract component interface."""
    
    @abstractmethod
    def operation(self) -> str:
        """Perform operation."""
        pass
    
    @abstractmethod
    def cost(self) -> float:
        """Get cost."""
        pass

# ============================================
# Concrete Component
# ============================================

class Coffee(Component):
    """Basic coffee."""
    
    def __init__(self) -> None:
        self.description = "Coffee"
    
    def operation(self) -> str:
        return self.description
    
    def cost(self) -> float:
        return 2.00

class Tea(Component):
    """Basic tea."""
    
    def __init__(self) -> None:
        self.description = "Tea"
    
    def operation(self) -> str:
        return self.description
    
    def cost(self) -> float:
        return 1.50

# ============================================
# Base Decorator
# ============================================

class CondimentDecorator(Component):
    """Base condiment decorator."""
    
    def __init__(self, component: Component) -> None:
        self._component = component
    
    @abstractmethod
    def operation(self) -> str:
        pass
    
    @abstractmethod
    def cost(self) -> float:
        pass

# ============================================
# Concrete Decorators
# ============================================

class Milk(CondimentDecorator):
    """Milk decorator."""
    
    def operation(self) -> str:
        return f"{self._component.operation()} + Milk"
    
    def cost(self) -> float:
        return self._component.cost() + 0.50

class Sugar(CondimentDecorator):
    """Sugar decorator."""
    
    def operation(self) -> str:
        return f"{self._component.operation()} + Sugar"
    
    def cost(self) -> float:
        return self._component.cost() + 0.25

class WhipCream(CondimentDecorator):
    """Whipped cream decorator."""
    
    def operation(self) -> str:
        return f"{self._component.operation()} + Whip Cream"
    
    def cost(self) -> float:
        return self._component.cost() + 0.75

class Caramel(CondimentDecorator):
    """Caramel decorator."""
    
    def operation(self) -> str:
        return f"{self._component.operation()} + Caramel"
    
    def cost(self) -> float:
        return self._component.cost() + 0.60

# ============================================
# Practical Example: Text Processing
# ============================================

class TextProcessor(ABC):
    """Abstract text processor."""
    
    @abstractmethod
    def process(self, text: str) -> str:
        """Process text."""
        pass

class PlainText(TextProcessor):
    """Plain text processor."""
    
    def process(self, text: str) -> str:
        return text

class TextDecorator(TextProcessor):
    """Base text decorator."""
    
    def __init__(self, processor: TextProcessor) -> None:
        self._processor = processor
    
    def process(self, text: str) -> str:
        return self._processor.process(text)

class UpperCaseDecorator(TextDecorator):
    """Convert to uppercase."""
    
    def process(self, text: str) -> str:
        return self._processor.process(text).upper()

class TrimDecorator(TextDecorator):
    """Trim whitespace."""
    
    def process(self, text: str) -> str:
        return self._processor.process(text).strip()

class ReplaceDecorator(TextDecorator):
    """Replace characters."""
    
    def __init__(self, processor: TextProcessor, old: str, new: str) -> None:
        super().__init__(processor)
        self.old = old
        self.new = new
    
    def process(self, text: str) -> str:
        return self._processor.process(text).replace(self.old, self.new)

# ============================================
# Practical Example: HTTP Middleware
# ============================================

class HTTPHandler(ABC):
    """Abstract HTTP handler."""
    
    @abstractmethod
    def handle(self, request: dict) -> dict:
        """Handle HTTP request."""
        pass

class BaseHTTPHandler(HTTPHandler):
    """Base HTTP handler."""
    
    def handle(self, request: dict) -> dict:
        return {"status": 200, "body": "OK"}

class HTTPMiddleware(HTTPHandler):
    """Base HTTP middleware."""
    
    def __init__(self, handler: HTTPHandler) -> None:
        self._handler = handler
    
    def handle(self, request: dict) -> dict:
        return self._handler.handle(request)

class AuthenticationMiddleware(HTTPMiddleware):
    """Authentication middleware."""
    
    def handle(self, request: dict) -> dict:
        token = request.get("token")
        if not token:
            return {"status": 401, "body": "Unauthorized"}
        return self._handler.handle(request)

class LoggingMiddleware(HTTPMiddleware):
    """Logging middleware."""
    
    def handle(self, request: dict) -> dict:
        print(f"  [LOG] Request: {request.get('method', 'GET')} {request.get('path', '/')}")
        response = self._handler.handle(request)
        print(f"  [LOG] Response: {response['status']}")
        return response

class RateLimitMiddleware(HTTPMiddleware):
    """Rate limiting middleware."""
    
    def __init__(self, handler: HTTPHandler, max_requests: int = 100) -> None:
        super().__init__(handler)
        self.max_requests = max_requests
        self.request_count = 0
    
    def handle(self, request: dict) -> dict:
        self.request_count += 1
        if self.request_count > self.max_requests:
            return {"status": 429, "body": "Too Many Requests"}
        return self._handler.handle(request)

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Coffee Shop ===")
    coffee = Coffee()
    print(f"  {coffee.operation()}: ${coffee.cost():.2f}")
    
    # Add milk
    coffee_with_milk = Milk(coffee)
    print(f"  {coffee_with_milk.operation()}: ${coffee_with_milk.cost():.2f}")
    
    # Add more condiments
    fancy_coffee = WhipCream(Caramel(Sugar(coffee)))
    print(f"  {fancy_coffee.operation()}: ${fancy_coffee.cost():.2f}")
    
    print("\n=== Text Processing ===")
    text = "  Hello, World!  "
    
    processor = PlainText()
    print(f"  Plain: '{processor.process(text)}'")
    
    processor = UpperCaseDecorator(PlainText())
    print(f"  Upper: '{processor.process(text)}'")
    
    processor = TrimDecorator(UpperCaseDecorator(PlainText()))
    print(f"  Trim+Upper: '{processor.process(text)}'")
    
    processor = ReplaceDecorator(TrimDecorator(UpperCaseDecorator(PlainText())), "O", "0")
    print(f"  Replace: '{processor.process(text)}'")
    
    print("\n=== HTTP Middleware ===")
    handler = BaseHTTPHandler()
    
    # Add middleware
    handler = LoggingMiddleware(handler)
    handler = AuthenticationMiddleware(handler)
    handler = RateLimitMiddleware(handler, max_requests=2)
    
    # Test requests
    request1 = {"method": "GET", "path": "/api/users", "token": "abc123"}
    request2 = {"method": "POST", "path": "/api/users", "token": "abc123"}
    request3 = {"method": "GET", "path": "/api/users"}  # No token
    
    print("Request 1:")
    response1 = handler.handle(request1)
    print(f"  Response: {response1}")
    
    print("Request 2:")
    response2 = handler.handle(request2)
    print(f"  Response: {response2}")
    
    print("Request 3 (no token):")
    response3 = handler.handle(request3)
    print(f"  Response: {response3}")
