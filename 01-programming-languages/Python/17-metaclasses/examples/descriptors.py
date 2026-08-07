"""
Descriptors in Python
Demonstrates descriptor protocol and usage
"""

from typing import Any, Optional

# ============================================
# Basic Descriptor
# ============================================

class BasicDescriptor:
    """Basic descriptor implementation."""
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)
    
    def __set__(self, obj, value):
        obj.__dict__[self.name] = value

class MyClass:
    """Class using basic descriptor."""
    x = BasicDescriptor()
    y = BasicDescriptor()

# ============================================
# Validated Descriptor
# ============================================

class Validated:
    """Descriptor that validates values."""
    
    def __init__(self, min_value=None, max_value=None):
        self.min_value = min_value
        self.max_value = max_value
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)
    
    def __set__(self, obj, value):
        if self.min_value is not None and value < self.min_value:
            raise ValueError(f"{self.name} must be >= {self.min_value}")
        if self.max_value is not None and value > self.max_value:
            raise ValueError(f"{self.name} must be <= {self.max_value}")
        obj.__dict__[self.name] = value

class Temperature:
    """Temperature class with validated values."""
    celsius = Validated(min_value=-273.15)
    fahrenheit = Validated(min_value=-459.67)

# ============================================
# Property-like Descriptor
# ============================================

class Property:
    """Property-like descriptor."""
    
    def __init__(self, fget=None, fset=None):
        self.fget = fget
        self.fset = fset
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        if self.fget:
            return self.fget(obj)
        raise AttributeError("unreadable attribute")
    
    def __set__(self, obj, value):
        if self.fset:
            self.fset(obj, value)
        else:
            raise AttributeError("can't set attribute")

class Circle:
    """Circle with property-like descriptor."""
    
    def __init__(self, radius):
        self._radius = radius
    
    @Property
    def radius(self):
        return self._radius
    
    @radius.setter
    def radius(self, value):
        if value < 0:
            raise ValueError("Radius cannot be negative")
        self._radius = value

# ============================================
# Cached Descriptor
# ============================================

class Cached:
    """Descriptor that caches computed values."""
    
    def __init__(self, func):
        self.func = func
        self.attrname = None
    
    def __set_name__(self, owner, name):
        self.attrname = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        
        # Check cache
        try:
            value = obj.__dict__[self.attrname]
        except KeyError:
            # Compute and cache
            value = self.func(obj)
            obj.__dict__[self.attrname] = value
        
        return value

class DataProcessor:
    """Data processor with cached computation."""
    
    def __init__(self, data):
        self.data = data
    
    @Cached
    def processed_data(self):
        """Expensive computation."""
        print("  Computing...")
        return [x ** 2 for x in self.data]

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Descriptor ===")
    obj = MyClass()
    obj.x = 10
    obj.y = 20
    print(f"  x: {obj.x}, y: {obj.y}")
    
    print("\n=== Validated Descriptor ===")
    temp = Temperature()
    temp.celsius = 25
    print(f"  Celsius: {temp.celsius}")
    
    try:
        temp.celsius = -300
    except ValueError as e:
        print(f"  Error: {e}")
    
    print("\n=== Cached Descriptor ===")
    processor = DataProcessor([1, 2, 3, 4, 5])
    
    print("  First access:")
    print(f"  Data: {processor.processed_data}")
    
    print("  Second access (cached):")
    print(f"  Data: {processor.processed_data}")
