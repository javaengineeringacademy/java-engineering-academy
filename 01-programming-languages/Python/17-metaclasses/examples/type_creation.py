"""
Metaclasses in Python
Demonstrates metaclass usage and class creation
"""

# ============================================
# Basic Metaclass
# ============================================

class MyMeta(type):
    """Basic metaclass."""
    
    def __new__(cls, name, bases, namespace):
        print(f"  Creating class: {name}")
        return super().__new__(cls, name, bases, namespace)
    
    def __init__(cls, name, bases, namespace):
        super().__init__(name, bases, namespace)
        print(f"  Initializing class: {name}")

class MyClass(metaclass=MyMeta):
    """Class using metaclass."""
    pass

# ============================================
# Metaclass with Attributes
# ============================================

class ValidationMeta(type):
    """Metaclass that validates class attributes."""
    
    def __new__(cls, name, bases, namespace):
        # Check for required attributes
        if bases:  # Skip base classes
            if 'required_attr' not in namespace:
                raise TypeError(f"Class {name} must define 'required_attr'")
        
        return super().__new__(cls, name, bases, namespace)

class ValidatedClass(metaclass=ValidationMeta):
    """Class that must have required_attr."""
    required_attr = "I'm required"

# ============================================
# Singleton Metaclass
# ============================================

class SingletonMeta(type):
    """Metaclass that implements singleton pattern."""
    
    _instances = {}
    
    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            cls._instances[cls] = super().__call__(*args, **kwargs)
        return cls._instances[cls]

class Singleton(metaclass=SingletonMeta):
    """Singleton class."""
    
    def __init__(self, value=None):
        self.value = value

# ============================================
# Registry Metaclass
# ============================================

class RegistryMeta(type):
    """Metaclass that registers subclasses."""
    
    registry = {}
    
    def __new__(cls, name, bases, namespace):
        new_class = super().__new__(cls, name, bases, namespace)
        
        # Don't register base classes
        if bases:
            cls.registry[name] = new_class
        
        return new_class

class Plugin(metaclass=RegistryMeta):
    """Base plugin class."""
    pass

class PluginA(Plugin):
    """Plugin A."""
    pass

class PluginB(Plugin):
    """Plugin B."""
    pass

# ============================================
# Method Addition Metaclass
# ============================================

class AutoReprMeta(type):
    """Metaclass that adds __repr__ automatically."""
    
    def __new__(cls, name, bases, namespace):
        # Add __repr__ if not defined
        if '__repr__' not in namespace:
            def __repr__(self):
                attrs = ', '.join(f'{k}={v!r}' for k, v in self.__dict__.items())
                return f'{name}({attrs})'
            namespace['__repr__'] = __repr__
        
        return super().__new__(cls, name, bases, namespace)

class AutoRepr(metaclass=AutoReprMeta):
    """Class with automatic __repr__."""
    pass

class User(AutoRepr):
    """User class with auto repr."""
    def __init__(self, name, age):
        self.name = name
        self.age = age

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Metaclass ===")
    obj = MyClass()
    
    print("\n=== Singleton ===")
    s1 = Singleton("first")
    s2 = Singleton("second")
    print(f"  s1 is s2: {s1 is s2}")
    print(f"  s1.value: {s1.value}")
    
    print("\n=== Registry ===")
    print(f"  Registered: {list(RegistryMeta.registry.keys())}")
    
    print("\n=== Auto Repr ===")
    user = User("Alice", 30)
    print(f"  User: {user}")
