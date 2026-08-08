"""
Module 11 - Design Patterns: Decorator Pattern Solutions
Complete solutions with explanations
"""

import time
from abc import ABC, abstractmethod


# =============================================================================
# Exercise 1: Basic Component Decorator - SOLUTION
# =============================================================================

class Component(ABC):
    """Base component interface."""
    
    @abstractmethod
    def operation(self):
        pass


class ConcreteComponent(Component):
    """Concrete component that does the base work."""
    
    def operation(self):
        return "BaseComponent"


class Decorator(Component):
    """Base decorator that wraps a component."""
    
    def __init__(self, component):
        self._component = component
    
    def operation(self):
        """Delegate to wrapped component."""
        return self._component.operation()


class ConcreteDecoratorA(Decorator):
    """Decorator that adds behavior A."""
    
    def operation(self):
        """Add behavior before and after."""
        result = self._component.operation()
        return f"[A({result})]"


class ConcreteDecoratorB(Decorator):
    """Decorator that adds behavior B."""
    
    def operation(self):
        """Add behavior before and after."""
        result = self._component.operation()
        return f"{{B({result})}}"


# =============================================================================
# Exercise 2: Text Formatting Decorator - SOLUTION
# =============================================================================

class TextComponent:
    """Base text component."""
    
    def __init__(self, text=""):
        self.text = text
    
    def render(self):
        return self.text


class TextDecorator(TextComponent):
    """Base text decorator."""
    
    def __init__(self, component):
        self._component = component
    
    def render(self):
        return self._component.render()


class BoldDecorator(TextDecorator):
    """Makes text bold using ** markers."""
    
    def render(self):
        return f"**{self._component.render()}**"


class ItalicDecorator(TextDecorator):
    """Makes text italic using // markers."""
    
    def render(self):
        return f"//{self._component.render()}//"


class UnderlineDecorator(TextDecorator):
    """Underlines text using __ markers."""
    
    def render(self):
        return f"__{self._component.render()}__"


# =============================================================================
# Exercise 3: Coffee Shop Decorator - SOLUTION
# =============================================================================

class Coffee(ABC):
    """Base coffee class."""
    
    @abstractmethod
    def cost(self):
        pass
    
    @abstractmethod
    def description(self):
        pass


class Espresso(Coffee):
    """Espresso coffee - base implementation."""
    
    def cost(self):
        return 3.00
    
    def description(self):
        return "Espresso"


class CoffeeDecorator(Coffee):
    """Base coffee decorator."""
    
    def __init__(self, coffee):
        self._coffee = coffee
    
    def cost(self):
        return self._coffee.cost()
    
    def description(self):
        return self._coffee.description()


class MilkDecorator(CoffeeDecorator):
    """Adds milk to coffee - $0.50."""
    
    def cost(self):
        return self._coffee.cost() + 0.50
    
    def description(self):
        return f"{self._coffee.description()}, Milk"


class SugarDecorator(CoffeeDecorator):
    """Adds sugar to coffee - $0.25."""
    
    def cost(self):
        return self._coffee.cost() + 0.25
    
    def description(self):
        return f"{self._coffee.description()}, Sugar"


class WhipCreamDecorator(CoffeeDecorator):
    """Adds whip cream to coffee - $0.75."""
    
    def cost(self):
        return self._coffee.cost() + 0.75
    
    def description(self):
        return f"{self._coffee.description()}, Whip Cream"


# =============================================================================
# Exercise 4: Caching Decorator - SOLUTION
# =============================================================================

import time

class DataSource(ABC):
    """Base data source interface."""
    
    @abstractmethod
    def fetch(self, key):
        pass


class DatabaseSource(DataSource):
    """Simulates slow database fetch."""
    
    def fetch(self, key):
        """Simulate slow database fetch."""
        time.sleep(0.01)  # Simulate delay
        return f"data_for_{key}"


class CachingDecorator(DataSource):
    """Adds caching to any data source."""
    
    def __init__(self, source):
        self._source = source
        self._cache = {}
    
    def fetch(self, key):
        """Check cache first, then fetch and cache."""
        if key not in self._cache:
            self._cache[key] = self._source.fetch(key)
        return self._cache[key]
    
    def invalidate(self, key=None):
        """Clear cache or specific key."""
        if key is None:
            self._cache.clear()
        elif key in self._cache:
            del self._cache[key]


# =============================================================================
# Exercise 5: Logging Decorator - SOLUTION
# =============================================================================

import logging

class Service(ABC):
    """Base service class."""
    
    @abstractmethod
    def execute(self, *args, **kwargs):
        pass


class ConcreteService(Service):
    """Concrete service implementation."""
    
    def execute(self, *args, **kwargs):
        return f"Executed with {args}, {kwargs}"


class LoggingDecorator(Service):
    """Adds logging to any service."""
    
    def __init__(self, service):
        self._service = service
        self._logs = []
    
    def execute(self, *args, **kwargs):
        """Log before and after execution."""
        self._logs.append(f"Calling with {args}, {kwargs}")
        result = self._service.execute(*args, **kwargs)
        self._logs.append(f"Returned: {result}")
        return result
    
    def get_logs(self):
        """Return execution logs."""
        return self._logs.copy()


class TimingDecorator(Service):
    """Adds timing to any service."""
    
    def __init__(self, service):
        self._service = service
        self._timings = []
    
    def execute(self, *args, **kwargs):
        """Time execution and store result."""
        start = time.time()
        result = self._service.execute(*args, **kwargs)
        duration = time.time() - start
        self._timings.append(duration)
        return result
    
    def get_timings(self):
        """Return timing data."""
        return self._timings.copy()


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Decorator Pattern Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Component Decorator")
    component = ConcreteComponent()
    assert component.operation() == "BaseComponent"
    
    decorated_a = ConcreteDecoratorA(component)
    assert decorated_a.operation() == "[A(BaseComponent)]"
    
    decorated_b = ConcreteDecoratorB(component)
    assert decorated_b.operation() == "{B(BaseComponent)}"
    
    # Stack decorators
    stacked = ConcreteDecoratorA(ConcreteDecoratorB(component))
    assert stacked.operation() == "[A({B(BaseComponent)})]"
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Text Formatting Decorator")
    text = TextComponent("Hello")
    assert text.render() == "Hello"
    
    bold = BoldDecorator(text)
    assert bold.render() == "**Hello**"
    
    italic = ItalicDecorator(text)
    assert italic.render() == "//Hello//"
    
    # Stack decorators
    combined = ItalicDecorator(BoldDecorator(TextComponent("World")))
    assert combined.render() == "//**World**//"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Coffee Shop Decorator")
    espresso = Espresso()
    assert espresso.cost() == 3.00
    assert espresso.description() == "Espresso"
    
    latte = MilkDecorator(espresso)
    assert latte.cost() == 3.50
    assert "Milk" in latte.description()
    
    mocha = WhipCreamDecorator(SugarDecorator(MilkDecorator(espresso)))
    assert mocha.cost() == 4.50  # 3.00 + 0.50 + 0.25 + 0.75
    assert "Milk" in mocha.description()
    assert "Sugar" in mocha.description()
    assert "Whip Cream" in mocha.description()
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Caching Decorator")
    source = CachingDecorator(DatabaseSource())
    
    # First fetch - should call database
    result1 = source.fetch("key1")
    assert result1 == "data_for_key1"
    assert len(source._cache) == 1
    
    # Second fetch - should use cache
    result2 = source.fetch("key1")
    assert result1 == result2
    assert len(source._cache) == 1
    
    # Different key - should call database
    result3 = source.fetch("key2")
    assert len(source._cache) == 2
    
    # Invalidate
    source.invalidate("key1")
    assert len(source._cache) == 1
    
    source.invalidate()
    assert len(source._cache) == 0
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Logging Decorator")
    service = ConcreteService()
    
    # Add logging
    logged_service = LoggingDecorator(service)
    result = logged_service.execute("test", key="value")
    assert result == "Executed with ('test',), {'key': 'value'}"
    assert len(logged_service.get_logs()) == 2
    
    # Add timing
    timed_service = TimingDecorator(logged_service)
    result = timed_service.execute("test")
    assert len(timed_service.get_timings()) == 1
    assert timed_service.get_timings()[0] >= 0
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
