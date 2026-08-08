"""
Module 17 - Metaclasses: Class Decorators Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Decorators that modify classes
"""


# =============================================================================
# Exercise 1: Basic Class Decorator (⭐⭐⭐)
# =============================================================================

def add_repr(cls):
    """
    Class decorator that adds __repr__ method.
    
    TODO:
    1. Create __repr__ method
    2. Add to class
    3. Return modified class
    """
    # TODO: Add __repr__ to class
    pass


# =============================================================================
# Exercise 2: Class Registration (⭐⭐⭐⭐)
# =============================================================================

class Registry:
    """Registry for decorated classes."""
    _classes = {}
    
    @classmethod
    def register(cls, name):
        """
        Decorator that registers a class.
        
        TODO:
        1. Create decorator function
        2. Register class in registry
        3. Return original class
        """
        def decorator(cls_to_register):
            # TODO: Register the class
            pass
        return decorator
    
    @classmethod
    def get(cls, name):
        """Get registered class by name."""
        return cls._classes.get(name)


# =============================================================================
# Exercise 3: Method Addition (⭐⭐⭐⭐)
# =============================================================================

def add_methods(methods_dict):
    """
    Class decorator that adds multiple methods.
    
    TODO:
    1. Accept dict of methods
    2. Add each method to class
    3. Return modified class
    """
    def decorator(cls):
        # TODO: Add methods to class
        pass
    return decorator


# =============================================================================
# Exercise 4: Subclass Tracking (⭐⭐⭐⭐)
# =============================================================================

def track_subclasses(cls):
    """
    Class decorator that tracks subclasses.
    
    TODO:
    1. Maintain list of subclasses
    2. Update when new subclass created
    3. Add class method to get subclasses
    """
    # TODO: Implement subclass tracking
    pass


# =============================================================================
# Exercise 5: Singleton Decorator (⭐⭐⭐⭐⭐)
# =============================================================================

def singleton(cls):
    """
    Class decorator that implements Singleton pattern.
    
    TODO:
    1. Store instance
    2. Return same instance on subsequent calls
    3. Support class reset
    """
    # TODO: Implement singleton pattern
    pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 17 - Class Decorators Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Class Decorator")
    try:
        @add_repr
        class MyClass:
            def __init__(self, value):
                self.value = value
        
        obj = MyClass(42)
        print(f"  Repr: {repr(obj)}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Class Registration")
    try:
        @Registry.register('my_class')
        class MyClass:
            pass
        
        result = Registry.get('my_class')
        print(f"  Registered: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Method Addition")
    try:
        @add_methods({
            'greet': lambda self: f"Hello, {self.name}!",
            'farewell': lambda self: f"Goodbye, {self.name}!"
        })
        class Person:
            def __init__(self, name):
                self.name = name
        
        obj = Person("Alice")
        print(f"  Greet: {obj.greet()}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Subclass Tracking")
    try:
        @track_subclasses
        class Base:
            pass
        
        class Child1(Base):
            pass
        
        class Child2(Base):
            pass
        
        print(f"  Subclasses: {Base.__subclasses__()}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Singleton Decorator")
    try:
        @singleton
        class MyClass:
            def __init__(self):
                self.value = 42
        
        s1 = MyClass()
        s2 = MyClass()
        assert s1 is s2
        print(f"  Same instance: {s1 is s2}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
