"""
Module 17 - Metaclasses: Type Creation Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Creating types dynamically
"""


# =============================================================================
# Exercise 1: Basic Type Creation (⭐⭐⭐⭐)
# =============================================================================

def exercise_1_basic_type():
    """
    Create a class using type() function.
    
    TODO:
    1. Create a class dynamically using type()
    2. Add methods to the dynamic class
    3. Create instances
    """
    # TODO: Create class using type()
    # DynamicClass = type('DynamicClass', (object,), {'attr': 'value'})
    
    pass


# =============================================================================
# Exercise 2: Custom Metaclass (⭐⭐⭐⭐)
# =============================================================================

class MetaRegistry(type):
    """
    Custom metaclass that registers all created classes.
    
    TODO:
    1. Override __new__ to register classes
    2. Store classes in registry
    3. Provide get_class method
    """
    _registry = {}
    
    def __new__(mcs, name, bases, namespace):
        # TODO: Create class and register it
        pass
    
    @classmethod
    def get_class(mcs, name):
        # TODO: Get class by name
        pass


# =============================================================================
# Exercise 3: Auto-Documentation (⭐⭐⭐⭐)
# =============================================================================

class DocMeta(type):
    """
    Metaclass that adds docstrings automatically.
    
    TODO:
    1. Generate docstrings from method signatures
    2. Add class documentation
    """
    def __new__(mcs, name, bases, namespace):
        # TODO: Add automatic documentation
        pass


# =============================================================================
# Exercise 4: Attribute Validation (⭐⭐⭐⭐⭐)
# =============================================================================

class ValidatedMeta(type):
    """
    Metaclass that validates attribute types.
    
    TODO:
    1. Define type annotations in class
    2. Validate attribute types on assignment
    3. Raise error for invalid types
    """
    def __new__(mcs, name, bases, namespace):
        # TODO: Set up type validation
        pass


# =============================================================================
# Exercise 5: Singleton Metaclass (⭐⭐⭐⭐⭐)
# =============================================================================

class SingletonMeta(type):
    """
    Metaclass that implements Singleton pattern.
    
    TODO:
    1. Override __call__ to control instance creation
    2. Store instance reference
    3. Return existing instance if exists
    """
    _instances = {}
    
    def __call__(cls, *args, **kwargs):
        # TODO: Implement singleton pattern
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 17 - Type Creation Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Type Creation")
    try:
        result = exercise_1_basic_type()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Custom Metaclass")
    try:
        class MyClass(metaclass=MetaRegistry):
            pass
        result = MetaRegistry.get_class('MyClass')
        print(f"  Registered: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Auto-Documentation")
    try:
        class MyDoc(metaclass=DocMeta):
            def my_method(self, x):
                pass
        print(f"  Docstring: {MyDoc.__doc__}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Attribute Validation")
    try:
        class MyValidated(metaclass=ValidatedMeta):
            name: str
            age: int
        obj = MyValidated()
        print(f"  Created: {obj}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Singleton Metaclass")
    try:
        class MySingleton(metaclass=SingletonMeta):
            pass
        s1 = MySingleton()
        s2 = MySingleton()
        assert s1 is s2
        print(f"  Same instance: {s1 is s2}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
