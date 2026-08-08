"""
Module 10 - Python Internals: Name Mangling Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Understanding Python's name mangling mechanism
"""


# =============================================================================
# Exercise 1: Basic Name Mangling (⭐⭐⭐)
# =============================================================================

class Exercise1Class:
    """
    Demonstrate basic name mangling with double underscore prefix.
    
    TODO:
    1. Create an instance variable __private_var
    2. Create a method that accesses __private_var
    3. Show how to access the mangled name from outside the class
    """
    def __init__(self):
        # TODO: Initialize __private_var
        pass
    
    def get_private(self):
        # TODO: Return __private_var
        pass


# =============================================================================
# Exercise 2: Name Mangling in Inheritance (⭐⭐⭐⭐)
# =============================================================================

class ParentClass:
    def __init__(self):
        self.__private = "parent"
    
    def get_parent_private(self):
        return self.__private


class ChildClass(ParentClass):
    """
    Demonstrate how name mangling works in inheritance.
    
    TODO:
    1. Create __private in ChildClass (should not conflict with parent)
    2. Override get_parent_private to return child's __private
    3. Create method to access both parent and child private vars
    """
    def __init__(self):
        super().__init__()
        # TODO: Initialize child's __private
    
    def get_child_private(self):
        # TODO: Return child's __private
        pass
    
    def get_both_private(self):
        # TODO: Return both parent and child private vars as tuple
        pass


# =============================================================================
# Exercise 3: Name Mangling with Properties (⭐⭐⭐)
# =============================================================================

class PropertyClass:
    """
    Use name mangling with Python properties.
    
    TODO:
    1. Create __value with property getter and setter
    2. Add validation in setter (value must be positive)
    3. Add a method to increment the value
    """
    def __init__(self, initial_value):
        # TODO: Initialize __value using setter
        pass
    
    @property
    def value(self):
        # TODO: Return __value
        pass
    
    @value.setter
    def value(self, new_value):
        # TODO: Validate and set __value
        pass
    
    def increment(self, amount=1):
        # TODO: Increment __value
        pass


# =============================================================================
# Exercise 4: Name Mangling in Decorators (⭐⭐⭐⭐)
# =============================================================================

def private_decorator(cls):
    """
    Create a decorator that adds name-mangled private attributes.
    
    TODO:
    1. Add __instance_id attribute to the class
    2. Add __creation_time attribute
    3. Add methods to access these attributes
    """
    # TODO: Implement decorator
    pass


# =============================================================================
# Exercise 5: Name Mangling and Serialization (⭐⭐⭐⭐⭐)
# =============================================================================

class SerializableClass:
    """
    Handle name mangling when serializing/deserializing objects.
    
    TODO:
    1. Create __data and __metadata attributes
    2. Implement to_dict() method that handles mangled names
    3. Implement from_dict() class method
    4. Ensure round-trip serialization works
    """
    def __init__(self, data, metadata):
        # TODO: Initialize __data and __metadata
        pass
    
    def to_dict(self):
        # TODO: Convert to dictionary, handling mangled names
        pass
    
    @classmethod
    def from_dict(cls, d):
        # TODO: Create instance from dictionary
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 10 - Name Mangling Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Name Mangling")
    try:
        obj = Exercise1Class()
        # Access mangled name directly
        obj._Exercise1Class__private_var = "test_value"
        result = obj.get_private()
        print(f"  Private value: {result}")
        assert result == "test_value", "Should access mangled variable"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Name Mangling in Inheritance")
    try:
        child = ChildClass()
        child._ChildClass__private = "child"
        result = child.get_both_private()
        print(f"  Both values: {result}")
        assert isinstance(result, tuple), "Should return tuple"
        assert len(result) == 2, "Should have 2 values"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Name Mangling with Properties")
    try:
        obj = PropertyClass(10)
        print(f"  Initial value: {obj.value}")
        obj.value = 20
        print(f"  After set: {obj.value}")
        obj.increment(5)
        print(f"  After increment: {obj.value}")
        assert obj.value == 25, "Should be 25 after operations"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Name Mangling in Decorators")
    try:
        @private_decorator
        class TestClass:
            pass
        
        obj = TestClass()
        assert hasattr(obj, '_TestClass__instance_id'), "Should have instance_id"
        assert hasattr(obj, '_TestClass__creation_time'), "Should have creation_time"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Name Mangling and Serialization")
    try:
        obj = SerializableClass("data", {"key": "value"})
        d = obj.to_dict()
        restored = SerializableClass.from_dict(d)
        print(f"  Original data: {obj._SerializableClass__data}")
        print(f"  Restored data: {restored._SerializableClass__data}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
