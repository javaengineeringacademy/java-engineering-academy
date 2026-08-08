"""
Module 17 - Metaclasses: Descriptors Solutions
Complete solutions with explanations
"""


# =============================================================================
# Exercise 1: Basic Descriptor - SOLUTION
# =============================================================================

class BasicDescriptor:
    """
    Implement a basic descriptor with __get__, __set__, __delete__.
    """
    def __init__(self, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        """Return value from instance's __dict__."""
        if obj is None:
            return self
        return obj.__dict__.get(self.name)
    
    def __set__(self, obj, value):
        """Set value in instance's __dict__."""
        obj.__dict__[self.name] = value
    
    def __delete__(self, obj):
        """Delete value from instance's __dict__."""
        if self.name in obj.__dict__:
            del obj.__dict__[self.name]


# =============================================================================
# Exercise 2: Validated Descriptor - SOLUTION
# =============================================================================

class Validated:
    """
    Descriptor that validates values on assignment.
    """
    def __init__(self, validator):
        self.validator = validator
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(f'validated_{self.name}')
    
    def __set__(self, obj, value):
        """Validate and set value."""
        if not self.validator(value):
            raise ValueError(f"Invalid value for {self.name}: {value}")
        obj.__dict__[f'validated_{self.name}'] = value


# =============================================================================
# Exercise 3: Computed Property - SOLUTION
# =============================================================================

class ComputedProperty:
    """
    Descriptor that computes value on access and caches it.
    """
    def __init__(self, func):
        self.func = func
        self.attr_name = None
    
    def __set_name__(self, owner, name):
        self.attr_name = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        
        # Check if cached value exists
        cache_attr = f'_cached_{self.attr_name}'
        if hasattr(obj, cache_attr):
            return getattr(obj, cache_attr)
        
        # Compute and cache
        value = self.func(obj)
        setattr(obj, cache_attr, value)
        return value


# =============================================================================
# Exercise 4: Type Checked Descriptor - SOLUTION
# =============================================================================

class TypeChecked:
    """
    Descriptor that enforces type checking.
    """
    def __init__(self, expected_type, convert=False):
        self.expected_type = expected_type
        self.convert = convert
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(f'typed_{self.name}')
    
    def __set__(self, obj, value):
        """Check type and set value."""
        if value is not None and not isinstance(value, self.expected_type):
            if self.convert:
                try:
                    value = self.expected_type(value)
                except (ValueError, TypeError):
                    raise TypeError(
                        f"Cannot convert {value} to {self.expected_type.__name__}"
                    )
            else:
                raise TypeError(
                    f"{self.name} must be {self.expected_type.__name__}, "
                    f"got {type(value).__name__}"
                )
        obj.__dict__[f'typed_{self.name}'] = value


# =============================================================================
# Exercise 5: Observer Descriptor - SOLUTION
# =============================================================================

class Observable:
    """
    Descriptor that notifies observers on change.
    """
    def __init__(self):
        self.observers = []
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(f'observable_{self.name}')
    
    def __set__(self, obj, value):
        """Set value and notify observers."""
        old_value = obj.__dict__.get(f'observable_{self.name}')
        obj.__dict__[f'observable_{self.name}'] = value
        
        if old_value != value:
            for observer in self.observers:
                observer(value, old_value)
    
    def add_observer(self, observer):
        """Add observer callback."""
        if observer not in self.observers:
            self.observers.append(observer)
    
    def remove_observer(self, observer):
        """Remove observer callback."""
        if observer in self.observers:
            self.observers.remove(observer)


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 17 - Descriptors Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Descriptor")
    class MyClass:
        value = BasicDescriptor('value')
    
    obj = MyClass()
    obj.value = 42
    assert obj.value == 42
    
    del obj.value
    assert obj.value is None
    print(f"  Value after set: 42, after delete: None")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Validated Descriptor")
    class Person:
        age = Validated(lambda x: isinstance(x, int) and 0 <= x <= 150)
    
    person = Person()
    person.age = 25
    assert person.age == 25
    
    try:
        person.age = -5
        print("  ✗ Should have raised ValueError")
    except ValueError:
        print("  ✓ Validation works")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Computed Property")
    class Circle:
        def __init__(self, radius):
            self._radius = radius
        
        area = ComputedProperty(lambda self: 3.14159 * self._radius ** 2)
    
    circle = Circle(5)
    area1 = circle.area
    area2 = circle.area  # Should use cached value
    assert area1 == area2
    assert abs(area1 - 78.54) < 0.01
    print(f"  Area: {circle.area:.2f}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Type Checked Descriptor")
    class Config:
        name = TypeChecked(str)
        count = TypeChecked(int)
    
    config = Config()
    config.name = "test"
    config.count = 42
    
    assert config.name == "test"
    assert config.count == 42
    
    # Test conversion
    config.count = "100"  # Should convert to int
    assert config.count == 100
    
    try:
        config.name = 123
        print("  ✗ Should have raised TypeError")
    except TypeError:
        print("  ✓ Type checking works")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Observer Descriptor")
    class Model:
        value = Observable()
    
    model = Model()
    changes = []
    model.value.add_observer(lambda v, old: changes.append((old, v)))
    
    model.value = 10
    model.value = 20
    model.value = 20  # No change
    
    assert changes == [(None, 10), (10, 20)]
    print(f"  Changes: {changes}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
