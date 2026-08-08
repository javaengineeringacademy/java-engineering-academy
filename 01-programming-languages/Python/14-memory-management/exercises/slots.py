"""
Module 14 - Memory Management: Slots Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: __slots__ for memory optimization
"""


# =============================================================================
# Exercise 1: Basic Slots (⭐⭐⭐)
# =============================================================================

class RegularClass:
    """Class without __slots__."""
    def __init__(self, x, y):
        self.x = x
        self.y = y


class SlottedClass:
    """TODO: Class with __slots__."""
    # TODO: Define __slots__
    
    def __init__(self, x, y):
        self.x = x
        self.y = y


def exercise_1_basic_slots():
    """
    Compare memory usage with and without __slots__.
    
    TODO:
    1. Create instances of both classes
    2. Compare memory usage using sys.getsizeof()
    3. Return size comparison
    """
    import sys
    
    sizes = {}
    
    # TODO: Measure and compare sizes
    
    return sizes


# =============================================================================
# Exercise 2: Slots with Inheritance (⭐⭐⭐⭐)
# =============================================================================

class BaseSlotted:
    """TODO: Base class with __slots__."""
    # TODO: Define __slots__
    
    def __init__(self, id):
        self.id = id


class ChildSlotted(BaseSlotted):
    """TODO: Child class with __slots__."""
    # TODO: Define __slots__
    
    def __init__(self, id, name):
        super().__init__(id)
        self.name = name


def exercise_2_slots_inheritance():
    """
    Understand slots with inheritance.
    
    TODO:
    1. Create child instance
    2. Check available attributes
    3. Verify no dynamic attribute creation
    """
    result = {}
    
    # TODO: Test inheritance with slots
    
    return result


# =============================================================================
# Exercise 3: Slots with Properties (⭐⭐⭐⭐)
# =============================================================================

class PropertySlotted:
    """TODO: Class with slots and properties."""
    # TODO: Define __slots__
    
    def __init__(self, value):
        self._value = value
    
    @property
    def value(self):
        # TODO: Return value
        pass
    
    @value.setter
    def value(self, new_value):
        # TODO: Set value with validation
        pass


def exercise_3_slots_properties():
    """
    Use slots with properties.
    
    TODO:
    1. Create instance
    2. Use property getter/setter
    3. Verify validation works
    """
    result = {}
    
    # TODO: Test slots with properties
    
    return result


# =============================================================================
# Exercise 4: Slots and Pickle (⭐⭐⭐⭐)
# =============================================================================

import pickle

class PickleSlotted:
    """TODO: Class with slots that supports pickling."""
    # TODO: Define __slots__
    
    def __init__(self, data):
        self.data = data
    
    # TODO: Implement __getstate__ and __setstate__


def exercise_4_slots_pickle():
    """
    Make slotted classes picklable.
    
    TODO:
    1. Create instance
    2. Pickle and unpickle
    3. Verify data preserved
    """
    result = {}
    
    # TODO: Test pickling with slots
    
    return result


# =============================================================================
# Exercise 5: Memory Benchmark (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_memory_benchmark():
    """
    Benchmark memory usage of different class designs.
    
    TODO:
    1. Create classes with different slot configurations
    2. Create many instances
    3. Measure total memory usage
    4. Return comparison results
    """
    import sys
    
    results = {}
    
    # TODO: Benchmark different designs
    
    return results


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 14 - Slots Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Slots")
    try:
        result = exercise_1_basic_slots()
        assert isinstance(result, dict)
        print(f"  Sizes: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Slots with Inheritance")
    try:
        result = exercise_2_slots_inheritance()
        assert isinstance(result, dict)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Slots with Properties")
    try:
        result = exercise_3_slots_properties()
        assert isinstance(result, dict)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Slots and Pickle")
    try:
        result = exercise_4_slots_pickle()
        assert isinstance(result, dict)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Memory Benchmark")
    try:
        result = exercise_5_memory_benchmark()
        assert isinstance(result, dict)
        print(f"  Results: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
