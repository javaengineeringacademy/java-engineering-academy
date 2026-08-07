"""
Class Decorators in Python
Demonstrates class decorator patterns and usage
"""

from typing import Any, Callable, Type

# ============================================
# Basic Class Decorator
# ============================================

def basic_decorator(cls):
    """Basic class decorator."""
    print(f"  Decorating class: {cls.__name__}")
    return cls

@basic_decorator
class MyClass:
    """Class with basic decorator."""
    pass

# ============================================
# Adding Methods
# ============================================

def add_repr(cls):
    """Decorator that adds __repr__ method."""
    def __repr__(self):
        attrs = ', '.join(f'{k}={v!r}' for k, v in self.__dict__.items())
        return f'{cls.__name__}({attrs})'
    
    cls.__repr__ = __repr__
    return cls

@add_repr
class User:
    """User class with auto __repr__."""
    def __init__(self, name, age):
        self.name = name
        self.age = age

# ============================================
# Validation Decorator
# ============================================

def validate_init(cls):
    """Decorator that validates __init__ arguments."""
    original_init = cls.__init__
    
    def new_init(self, *args, **kwargs):
        print(f"  Validating arguments for {cls.__name__}")
        original_init(self, *args, **kwargs)
    
    cls.__init__ = new_init
    return cls

@validate_init
class Product:
    """Product with validated initialization."""
    def __init__(self, name, price):
        self.name = name
        self.price = price

# ============================================
# Registry Decorator
# ============================================

def register_class(cls):
    """Decorator that registers class in global registry."""
    if not hasattr(register_class, 'registry'):
        register_class.registry = {}
    
    register_class.registry[cls.__name__] = cls
    return cls

@register_class
class PluginA:
    """Plugin A."""
    pass

@register_class
class PluginB:
    """Plugin B."""
    pass

# ============================================
# Singleton Decorator
# ============================================

def singleton(cls):
    """Decorator that implements singleton pattern."""
    instances = {}
    
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    
    return get_instance

@singleton
class Database:
    """Singleton database class."""
    def __init__(self):
        self.connection = "connected"

# ============================================
# Timing Decorator
# ============================================

def timing_decorator(cls):
    """Decorator that adds timing to methods."""
    import time
    
    for attr in cls.__dict__:
        if callable(getattr(cls, attr)):
            original = getattr(cls, attr)
            
            def timed_method(self, *args, **kwargs):
                start = time.time()
                result = original(self, *args, **kwargs)
                end = time.time()
                print(f"  {original.__name__}: {end - start:.4f}s")
                return result
            
            setattr(cls, attr, timed_method)
    
    return cls

@timing_decorator
class DataProcessor:
    """Data processor with timed methods."""
    def process(self, data):
        return [x ** 2 for x in data]

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Decorator ===")
    obj = MyClass()
    
    print("\n=== Add Repr ===")
    user = User("Alice", 30)
    print(f"  User: {user}")
    
    print("\n=== Validate Init ===")
    product = Product("Widget", 9.99)
    
    print("\n=== Registry ===")
    print(f"  Registry: {list(register_class.registry.keys())}")
    
    print("\n=== Singleton ===")
    db1 = Database()
    db2 = Database()
    print(f"  Same instance: {db1 is db2}")
    
    print("\n=== Timing ===")
    processor = DataProcessor()
    processor.process(range(1000000))
