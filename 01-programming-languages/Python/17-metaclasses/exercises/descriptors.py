"""
Module 17 - Metaclasses: Descriptors Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Descriptor protocol implementation
"""


# =============================================================================
# Exercise 1: Basic Descriptor (⭐⭐⭐⭐)
# =============================================================================

class BasicDescriptor:
    """
    Implement a basic descriptor.
    
    TODO:
    1. Implement __get__
    2. Implement __set__
    3. Implement __delete__
    """
    def __init__(self, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        # TODO: Return value
        pass
    
    def __set__(self, obj, value):
        # TODO: Set value
        pass
    
    def __delete__(self, obj):
        # TODO: Delete value
        pass


# =============================================================================
# Exercise 2: Validated Descriptor (⭐⭐⭐⭐)
# =============================================================================

class Validated:
    """
    Descriptor that validates values.
    
    TODO:
    1. Accept validation function in __init__
    2. Validate on __set__
    3. Raise error for invalid values
    """
    def __init__(self, validator):
        self.validator = validator
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        # TODO: Return value
        pass
    
    def __set__(self, obj, value):
        # TODO: Validate and set value
        pass


# =============================================================================
# Exercise 3: Computed Property (⭐⭐⭐⭐)
# =============================================================================

class ComputedProperty:
    """
    Descriptor that computes value on access.
    
    TODO:
    1. Accept compute function
    2. Cache computed value
    3. Recompute when dependencies change
    """
    def __init__(self, func):
        self.func = func
        self.attr_name = None
    
    def __set_name__(self, owner, name):
        self.attr_name = name
    
    def __get__(self, obj, objtype=None):
        # TODO: Compute and cache value
        pass


# =============================================================================
# Exercise 4: Type Checked Descriptor (⭐⭐⭐⭐⭐)
# =============================================================================

class TypeChecked:
    """
    Descriptor that enforces type checking.
    
    TODO:
    1. Accept expected type
    2. Check type on __set__
    3. Support type conversion
    """
    def __init__(self, expected_type, convert=False):
        self.expected_type = expected_type
        self.convert = convert
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        # TODO: Return value
        pass
    
    def __set__(self, obj, value):
        # TODO: Check type and set
        pass


# =============================================================================
# Exercise 5: Observer Descriptor (⭐⭐⭐⭐⭐)
# =============================================================================

class Observable:
    """
    Descriptor that notifies on change.
    
    TODO:
    1. Maintain list of observers
    2. Notify on __set__
    3. Support add/remove observer
    """
    def __init__(self):
        self.observers = []
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        # TODO: Return value
        pass
    
    def __set__(self, obj, value):
        # TODO: Set value and notify observers
        pass
    
    def add_observer(self, observer):
        # TODO: Add observer
        pass
    
    def remove_observer(self, observer):
        # TODO: Remove observer
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 17 - Descriptors Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Descriptor")
    try:
        class MyClass:
            value = BasicDescriptor('value')
        
        obj = MyClass()
        obj.value = 42
        print(f"  Value: {obj.value}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Validated Descriptor")
    try:
        class MyClass:
            age = Validated(lambda x: isinstance(x, int) and x > 0)
        
        obj = MyClass()
        obj.age = 25
        print(f"  Age: {obj.age}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Computed Property")
    try:
        class Circle:
            radius = ComputedProperty(lambda self: self._radius)
        
        obj = Circle()
        print(f"  Computed: {obj}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Type Checked Descriptor")
    try:
        class MyClass:
            name = TypeChecked(str)
        
        obj = MyClass()
        obj.name = "test"
        print(f"  Name: {obj.name}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Observer Descriptor")
    try:
        class MyClass:
            value = Observable()
        
        obj = MyClass()
        changes = []
        obj.value.add_observer(lambda v: changes.append(v))
        obj.value = 42
        print(f"  Changes: {changes}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
