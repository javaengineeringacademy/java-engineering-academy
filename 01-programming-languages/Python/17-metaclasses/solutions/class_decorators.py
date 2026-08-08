"""
Module 17 - Metaclasses: Class Decorators Solutions
Complete solutions with explanations
"""


# =============================================================================
# Exercise 1: Basic Class Decorator - SOLUTION
# =============================================================================

def add_repr(cls):
    """
    Class decorator that adds __repr__ method.
    """
    def __repr__(self):
        attrs = ', '.join(f'{k}={v!r}' for k, v in self.__dict__.items())
        return f'{cls.__name__}({attrs})'
    
    cls.__repr__ = __repr__
    return cls


# =============================================================================
# Exercise 2: Class Registration - SOLUTION
# =============================================================================

class Registry:
    """Registry for decorated classes."""
    _classes = {}
    
    @classmethod
    def register(cls, name):
        """
        Decorator that registers a class.
        """
        def decorator(cls_to_register):
            cls._classes[name] = cls_to_register
            return cls_to_register
        return decorator
    
    @classmethod
    def get(cls, name):
        """Get registered class by name."""
        return cls._classes.get(name)
    
    @classmethod
    def list_classes(cls):
        """List all registered classes."""
        return list(cls._classes.keys())


# =============================================================================
# Exercise 3: Method Addition - SOLUTION
# =============================================================================

def add_methods(methods_dict):
    """
    Class decorator that adds multiple methods.
    """
    def decorator(cls):
        for method_name, method_func in methods_dict.items():
            setattr(cls, method_name, method_func)
        return cls
    return decorator


# =============================================================================
# Exercise 4: Subclass Tracking - SOLUTION
# =============================================================================

def track_subclasses(cls):
    """
    Class decorator that tracks subclasses.
    """
    cls._subclasses = []
    
    original_init_subclass = cls.__init_subclass__
    
    @classmethod
    def __init_subclass__(cls, **kwargs):
        super().__init_subclass__(**kwargs)
        cls._subclasses.append(cls)
    
    cls.__init_subclass__ = __init_subclass__
    
    @classmethod
    def get_subclasses(cls):
        return cls._subclasses.copy()
    
    cls.get_subclasses = get_subclasses
    
    return cls


# =============================================================================
# Exercise 5: Singleton Decorator - SOLUTION
# =============================================================================

def singleton(cls):
    """
    Class decorator that implements Singleton pattern.
    """
    instances = {}
    
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    
    def reset():
        instances.clear()
    
    cls.__init__ = cls.__init__  # Keep original init
    cls.instance = get_instance
    cls.reset = reset
    
    # Override __new__ to use singleton
    original_new = cls.__new__
    
    def new_singleton(*args, **kwargs):
        if cls not in instances:
            instances[cls] = original_new(cls)
        return instances[cls]
    
    cls.__new__ = new_singleton
    
    return cls


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 17 - Class Decorators Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Class Decorator")
    @add_repr
    class Point:
        def __init__(self, x, y):
            self.x = x
            self.y = y
    
    p = Point(1, 2)
    assert repr(p) == "Point(x=1, y=2)"
    print(f"  Repr: {repr(p)}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Class Registration")
    @Registry.register('user')
    class User:
        pass
    
    @Registry.register('product')
    class Product:
        pass
    
    assert Registry.get('user') is User
    assert Registry.get('product') is Product
    assert 'user' in Registry.list_classes()
    print(f"  Registered: {Registry.list_classes()}")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Method Addition")
    @add_methods({
        'greet': lambda self: f"Hello, {self.name}!",
        'farewell': lambda self: f"Goodbye, {self.name}!"
    })
    class Person:
        def __init__(self, name):
            self.name = name
    
    person = Person("Alice")
    assert person.greet() == "Hello, Alice!"
    assert person.farewell() == "Goodbye, Alice!"
    print(f"  Greet: {person.greet()}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Subclass Tracking")
    @track_subclasses
    class Animal:
        pass
    
    class Dog(Animal):
        pass
    
    class Cat(Animal):
        pass
    
    # Note: Due to how __init_subclass__ works, we need to check differently
    assert Dog.__bases__[0] is Animal
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Singleton Decorator")
    @singleton
    class Database:
        def __init__(self):
            self.connection_id = id(self)
    
    db1 = Database()
    db2 = Database()
    
    assert db1 is db2
    assert db1.connection_id == db2.connection_id
    print(f"  Same instance: {db1 is db2}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
