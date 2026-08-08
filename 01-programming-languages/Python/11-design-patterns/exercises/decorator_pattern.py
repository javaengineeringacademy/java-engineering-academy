"""
Module 11 - Design Patterns: Decorator Pattern Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Decorator pattern implementation (not Python decorators)
"""


# =============================================================================
# Exercise 1: Basic Component Decorator (⭐⭐⭐)
# =============================================================================

class Component:
    """Base component interface."""
    def operation(self):
        pass


class ConcreteComponent(Component):
    """TODO: Implement concrete component."""
    def operation(self):
        # TODO: Return base operation
        pass


class Decorator(Component):
    """Base decorator class."""
    def __init__(self, component):
        self._component = component
    
    def operation(self):
        # TODO: Delegate to wrapped component
        pass


class ConcreteDecoratorA(Decorator):
    """TODO: Add behavior A."""
    def operation(self):
        # TODO: Add behavior before/after
        pass


class ConcreteDecoratorB(Decorator):
    """TODO: Add behavior B."""
    def operation(self):
        # TODO: Add behavior before/after
        pass


# =============================================================================
# Exercise 2: Text Formatting Decorator (⭐⭐⭐⭐)
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
    """TODO: Make text bold."""
    def render(self):
        # TODO: Wrap text in bold markers
        pass


class ItalicDecorator(TextDecorator):
    """TODO: Make text italic."""
    def render(self):
        # TODO: Wrap text in italic markers
        pass


class UnderlineDecorator(TextDecorator):
    """TODO: Underline text."""
    def render(self):
        # TODO: Wrap text in underline markers
        pass


# =============================================================================
# Exercise 3: Coffee Shop Decorator (⭐⭐⭐⭐)
# =============================================================================

class Coffee:
    """Base coffee class."""
    def cost(self):
        pass
    
    def description(self):
        pass


class Espresso(Coffee):
    """TODO: Implement Espresso."""
    def cost(self):
        # TODO: Return espresso cost
        pass
    
    def description(self):
        # TODO: Return espresso description
        pass


class CoffeeDecorator(Coffee):
    """Base coffee decorator."""
    def __init__(self, coffee):
        self._coffee = coffee
    
    def cost(self):
        return self._coffee.cost()
    
    def description(self):
        return self._coffee.description()


class MilkDecorator(CoffeeDecorator):
    """TODO: Add milk."""
    def cost(self):
        # TODO: Add milk cost
        pass
    
    def description(self):
        # TODO: Add milk to description
        pass


class SugarDecorator(CoffeeDecorator):
    """TODO: Add sugar."""
    def cost(self):
        # TODO: Add sugar cost
        pass
    
    def description(self):
        # TODO: Add sugar to description
        pass


class WhipCreamDecorator(CoffeeDecorator):
    """TODO: Add whip cream."""
    def cost(self):
        # TODO: Add whip cream cost
        pass
    
    def description(self):
        # TODO: Add whip cream to description
        pass


# =============================================================================
# Exercise 4: Caching Decorator (⭐⭐⭐⭐)
# =============================================================================

class DataSource:
    """Base data source interface."""
    def fetch(self, key):
        pass


class DatabaseSource(DataSource):
    """TODO: Simulate database fetch."""
    def fetch(self, key):
        # TODO: Simulate slow database fetch
        pass


class CachingDecorator(DataSource):
    """TODO: Add caching to data source."""
    def __init__(self, source):
        self._source = source
        self._cache = {}
    
    def fetch(self, key):
        # TODO: Check cache first, then fetch and cache
        pass
    
    def invalidate(self, key=None):
        # TODO: Clear cache or specific key
        pass


# =============================================================================
# Exercise 5: Logging Decorator (⭐⭐⭐⭐⭐)
# =============================================================================

class Service:
    """Base service class."""
    def execute(self, *args, **kwargs):
        pass


class ConcreteService(Service):
    """TODO: Implement service."""
    def execute(self, *args, **kwargs):
        # TODO: Return execution result
        pass


class LoggingDecorator(Service):
    """TODO: Add logging to service."""
    def __init__(self, service):
        self._service = service
        self._logs = []
    
    def execute(self, *args, **kwargs):
        # TODO: Log before and after execution
        pass
    
    def get_logs(self):
        # TODO: Return execution logs
        pass


class TimingDecorator(Service):
    """TODO: Add timing to service."""
    def __init__(self, service):
        self._service = service
        self._timings = []
    
    def execute(self, *args, **kwargs):
        # TODO: Time execution and store
        pass
    
    def get_timings(self):
        # TODO: Return timing data
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Decorator Pattern Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Component Decorator")
    try:
        component = ConcreteComponent()
        decorated = ConcreteDecoratorA(ConcreteDecoratorB(component))
        result = decorated.operation()
        assert "B" in result and "A" in result, "Should include both decorators"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Text Formatting Decorator")
    try:
        text = ItalicDecorator(BoldDecorator(TextComponent("Hello")))
        result = text.render()
        assert "**" in result and "//" in result, "Should have bold and italic"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Coffee Shop Decorator")
    try:
        coffee = MilkDecorator(SugarDecorator(Espresso()))
        assert coffee.cost() > 0, "Should have positive cost"
        assert "milk" in coffee.description().lower(), "Should include milk"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Caching Decorator")
    try:
        source = CachingDecorator(DatabaseSource())
        result1 = source.fetch("key1")
        result2 = source.fetch("key1")  # Should be cached
        assert result1 == result2, "Should return same result"
        assert len(source._cache) == 1, "Should have 1 cached item"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Logging Decorator")
    try:
        service = TimingDecorator(LoggingDecorator(ConcreteService()))
        service.execute("test")
        assert len(service._timings) == 1, "Should have timing"
        assert len(service._service._logs) == 2, "Should have log entries"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
