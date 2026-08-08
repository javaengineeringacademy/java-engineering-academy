"""
Module 14 - Memory Management: Slots Solutions
Complete solutions with explanations
"""

import sys
import pickle


# =============================================================================
# Exercise 1: Basic Slots - SOLUTION
# =============================================================================

class RegularClass:
    """Class without __slots__ - uses __dict__."""
    def __init__(self, x, y):
        self.x = x
        self.y = y


class SlottedClass:
    """Class with __slots__ - more memory efficient."""
    __slots__ = ('x', 'y')
    
    def __init__(self, x, y):
        self.x = x
        self.y = y


def exercise_1_basic_slots():
    """
    Compare memory usage with and without __slots__.
    """
    # Create instances
    regular = RegularClass(1, 2)
    slotted = SlottedClass(1, 2)
    
    # Measure memory
    regular_size = sys.getsizeof(regular)
    slotted_size = sys.getsizeof(slotted)
    
    # Regular class has __dict__
    has_dict = hasattr(regular, '__dict__')
    slotted_has_dict = hasattr(slotted, '__dict__')
    
    sizes = {
        'regular_size': regular_size,
        'slotted_size': slotted_size,
        'savings': regular_size - slotted_size,
        'regular_has_dict': has_dict,
        'slotted_has_dict': slotted_has_dict,
    }
    
    return sizes


# =============================================================================
# Exercise 2: Slots with Inheritance - SOLUTION
# =============================================================================

class BaseSlotted:
    """Base class with __slots__."""
    __slots__ = ('id',)
    
    def __init__(self, id):
        self.id = id


class ChildSlotted(BaseSlotted):
    """Child class with __slots__."""
    __slots__ = ('name',)
    
    def __init__(self, id, name):
        super().__init__(id)
        self.name = name


def exercise_2_slots_inheritance():
    """
    Understand slots with inheritance.
    """
    child = ChildSlotted(1, "test")
    
    result = {
        'has_id': hasattr(child, 'id'),
        'has_name': hasattr(child, 'name'),
        'can_set_new': False,
    }
    
    # Try to set new attribute
    try:
        child.new_attr = "new"
        result['can_set_new'] = True
    except AttributeError:
        result['can_set_new'] = False
    
    return result


# =============================================================================
# Exercise 3: Slots with Properties - SOLUTION
# =============================================================================

class PropertySlotted:
    """Class with slots and properties."""
    __slots__ = ('_value',)
    
    def __init__(self, value):
        self._value = value
    
    @property
    def value(self):
        return self._value
    
    @value.setter
    def value(self, new_value):
        if not isinstance(new_value, (int, float)):
            raise TypeError("Value must be a number")
        if new_value < 0:
            raise ValueError("Value must be positive")
        self._value = new_value


def exercise_3_slots_properties():
    """
    Use slots with properties.
    """
    obj = PropertySlotted(10)
    
    result = {
        'initial_value': obj.value,
        'validation_works': False,
    }
    
    # Test setter
    obj.value = 20
    result['after_set'] = obj.value
    
    # Test validation
    try:
        obj.value = -5
    except ValueError:
        result['validation_works'] = True
    
    return result


# =============================================================================
# Exercise 4: Slots and Pickle - SOLUTION
# =============================================================================

class PickleSlotted:
    """Class with slots that supports pickling."""
    __slots__ = ('data',)
    
    def __init__(self, data):
        self.data = data
    
    def __getstate__(self):
        """Return state for pickling."""
        return {slot: getattr(self, slot) for slot in self.__slots__}
    
    def __setstate__(self, state):
        """Restore state from pickling."""
        for slot, value in state.items():
            setattr(self, slot, value)


def exercise_4_slots_pickle():
    """
    Make slotted classes picklable.
    """
    # Create instance
    original = PickleSlotted([1, 2, 3])
    
    # Pickle and unpickle
    pickled = pickle.dumps(original)
    restored = pickle.loads(pickled)
    
    result = {
        'original_data': original.data,
        'restored_data': restored.data,
        'data_matches': original.data == restored.data,
    }
    
    return result


# =============================================================================
# Exercise 5: Memory Benchmark - SOLUTION
# =============================================================================

def exercise_5_memory_benchmark():
    """
    Benchmark memory usage of different class designs.
    """
    class Regular:
        def __init__(self, x, y):
            self.x = x
            self.y = y
    
    class WithSlots:
        __slots__ = ('x', 'y')
        def __init__(self, x, y):
            self.x = x
            self.y = y
    
    # Create many instances
    n = 10000
    
    # Regular class instances
    regular_instances = [Regular(i, i*2) for i in range(n)]
    regular_total = sum(sys.getsizeof(obj) for obj in regular_instances)
    
    # Slotted class instances
    slotted_instances = [WithSlots(i, i*2) for i in range(n)]
    slotted_total = sum(sys.getsizeof(obj) for obj in slotted_instances)
    
    results = {
        'instances': n,
        'regular_total': regular_total,
        'slotted_total': slotted_total,
        'savings': regular_total - slotted_total,
        'savings_percent': round((regular_total - slotted_total) / regular_total * 100, 2),
        'regular_per_instance': regular_total / n,
        'slotted_per_instance': slotted_total / n,
    }
    
    return results


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 14 - Slots Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Slots")
    result = exercise_1_basic_slots()
    assert isinstance(result, dict)
    assert result['regular_has_dict'] == True
    assert result['slotted_has_dict'] == False
    print(f"  Sizes: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Slots with Inheritance")
    result = exercise_2_slots_inheritance()
    assert isinstance(result, dict)
    assert result['has_id'] == True
    assert result['has_name'] == True
    assert result['can_set_new'] == False
    print(f"  Result: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Slots with Properties")
    result = exercise_3_slots_properties()
    assert isinstance(result, dict)
    assert result['initial_value'] == 10
    assert result['after_set'] == 20
    assert result['validation_works'] == True
    print(f"  Result: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Slots and Pickle")
    result = exercise_4_slots_pickle()
    assert isinstance(result, dict)
    assert result['data_matches'] == True
    print(f"  Result: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Memory Benchmark")
    result = exercise_5_memory_benchmark()
    assert isinstance(result, dict)
    assert result['savings'] > 0
    print(f"  Results: {result}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
