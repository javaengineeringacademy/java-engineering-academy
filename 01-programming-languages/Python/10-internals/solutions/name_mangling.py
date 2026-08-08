"""
Module 10 - Python Internals: Name Mangling Solutions
Complete solutions with explanations
"""

import time


# =============================================================================
# Exercise 1: Basic Name Mangling - SOLUTION
# =============================================================================

class Exercise1Class:
    """
    Demonstrate basic name mangling with double underscore prefix.
    
    Name mangling converts __private_var to _ClassName__private_var
    to avoid naming conflicts in subclasses.
    """
    def __init__(self):
        # Initialize __private_var - this gets mangled to _Exercise1Class__private_var
        self.__private_var = "Hello, Private World!"
    
    def get_private(self):
        # Access __private_var within the class - no mangling needed here
        return self.__private_var
    
    def set_private(self, value):
        # Set __private_var within the class
        self.__private_var = value


# =============================================================================
# Exercise 2: Name Mangling in Inheritance - SOLUTION
# =============================================================================

class ParentClass:
    def __init__(self):
        # __private gets mangled to _ParentClass__private
        self.__private = "parent_value"
    
    def get_parent_private(self):
        # Access parent's mangled name
        return self.__private


class ChildClass(ParentClass):
    """
    Demonstrate how name mangling works in inheritance.
    
    Each class has its own mangled namespace, so parent and child
    __private variables don't conflict.
    """
    def __init__(self):
        super().__init__()
        # __private gets mangled to _ChildClass__private
        # This is different from _ParentClass__private
        self.__private = "child_value"
    
    def get_child_private(self):
        # Access child's mangled name
        return self.__private
    
    def get_both_private(self):
        # Access both parent and child's mangled names
        parent_val = self._ParentClass__private  # Direct mangled access
        child_val = self._ChildClass__private    # Direct mangled access
        return (parent_val, child_val)


# =============================================================================
# Exercise 3: Name Mangling with Properties - SOLUTION
# =============================================================================

class PropertyClass:
    """
    Use name mangling with Python properties.
    
    The @property decorator works with mangled names by handling
    the mangling transparently within the class methods.
    """
    def __init__(self, initial_value):
        # Initialize __value using the setter for validation
        self.value = initial_value
    
    @property
    def value(self):
        # Return __value (accessed as _PropertyClass__value)
        return self.__value
    
    @value.setter
    def value(self, new_value):
        # Validate and set __value
        if not isinstance(new_value, (int, float)):
            raise TypeError("Value must be a number")
        if new_value < 0:
            raise ValueError("Value must be positive")
        self.__value = new_value
    
    def increment(self, amount=1):
        # Increment __value with validation
        if not isinstance(amount, (int, float)):
            raise TypeError("Amount must be a number")
        if amount < 0:
            raise ValueError("Amount must be positive")
        self.__value += amount
    
    def __repr__(self):
        return f"PropertyClass(value={self.__value})"


# =============================================================================
# Exercise 4: Name Mangling in Decorators - SOLUTION
# =============================================================================

def private_decorator(cls):
    """
    Create a decorator that adds name-mangled private attributes.
    
    This decorator adds __instance_id and __creation_time to the class.
    The attributes are mangled with the class name.
    """
    # Store original __init__
    original_init = cls.__init__
    
    def new_init(self, *args, **kwargs):
        # Call original __init__
        original_init(self, *args, **kwargs)
        
        # Add mangled attributes
        # These become _ClassName__instance_id and _ClassName__creation_time
        self.__instance_id = id(self)
        self.__creation_time = time.time()
    
    # Replace __init__
    cls.__init__ = new_init
    
    # Add getter methods
    def get_instance_id(self):
        return self.__instance_id
    
    def get_creation_time(self):
        return self.__creation_time
    
    cls.get_instance_id = get_instance_id
    cls.get_creation_time = get_creation_time
    
    return cls


# =============================================================================
# Exercise 5: Name Mangling and Serialization - SOLUTION
# =============================================================================

class SerializableClass:
    """
    Handle name mangling when serializing/deserializing objects.
    
    Key insight: When accessing mangled attributes from methods,
    Python handles the mangling automatically. But when working
    with external data (like dicts), we need to be explicit.
    """
    def __init__(self, data, metadata):
        # Initialize __data and __metadata
        # These get mangled to _SerializableClass__data etc.
        self.__data = data
        self.__metadata = metadata
    
    def to_dict(self):
        """
        Convert to dictionary, handling mangled names.
        
        We need to access the mangled names directly when creating
        the dictionary representation.
        """
        return {
            'data': self.__data,  # Python handles mangling here
            'metadata': self.__metadata,  # Python handles mangling here
            '_class_name': self.__class__.__name__
        }
    
    @classmethod
    def from_dict(cls, d):
        """
        Create instance from dictionary.
        
        We pass the raw values to __init__, which handles mangling.
        """
        return cls(
            data=d['data'],
            metadata=d['metadata']
        )
    
    def __repr__(self):
        return f"SerializableClass(data={self.__data}, metadata={self.__metadata})"


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 10 - Name Mangling Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Name Mangling")
    obj = Exercise1Class()
    # Access mangled name directly from outside the class
    obj._Exercise1Class__private_var = "test_value"
    result = obj.get_private()
    print(f"  Private value: {result}")
    assert result == "test_value", "Should access mangled variable"
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Name Mangling in Inheritance")
    child = ChildClass()
    child._ChildClass__private = "child"
    result = child.get_both_private()
    print(f"  Both values: {result}")
    assert isinstance(result, tuple), "Should return tuple"
    assert len(result) == 2, "Should have 2 values"
    assert result[0] == "parent_value", "Parent value should be preserved"
    assert result[1] == "child", "Child value should be set"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Name Mangling with Properties")
    obj = PropertyClass(10)
    print(f"  Initial value: {obj.value}")
    assert obj.value == 10, "Initial value should be 10"
    
    obj.value = 20
    print(f"  After set: {obj.value}")
    assert obj.value == 20, "Value should be 20 after set"
    
    obj.increment(5)
    print(f"  After increment: {obj.value}")
    assert obj.value == 25, "Value should be 25 after increment"
    
    # Test validation
    try:
        obj.value = -5
        print("  ✗ Should have raised ValueError")
    except ValueError:
        print("  ✓ Validation works")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Name Mangling in Decorators")
    @private_decorator
    class TestClass:
        pass
    
    obj = TestClass()
    assert hasattr(obj, '_TestClass__instance_id'), "Should have instance_id"
    assert hasattr(obj, '_TestClass__creation_time'), "Should have creation_time"
    print(f"  Instance ID: {obj.get_instance_id()}")
    print(f"  Creation time: {obj.get_creation_time()}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Name Mangling and Serialization")
    original = SerializableClass("important_data", {"key": "value"})
    print(f"  Original: {original}")
    
    d = original.to_dict()
    print(f"  Serialized: {d}")
    
    restored = SerializableClass.from_dict(d)
    print(f"  Restored: {restored}")
    
    assert original._SerializableClass__data == restored._SerializableClass__data
    assert original._SerializableClass__metadata == restored._SerializableClass__metadata
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
