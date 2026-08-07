"""
__slots__ in Python
Demonstrates __slots__ for memory optimization
"""

import sys
import time

# ============================================
# Basic __slots__
# ============================================

def basic_slots() -> None:
    """Demonstrate basic __slots__ usage."""
    print("=== Basic __slots__ ===")
    
    class RegularClass:
        def __init__(self, x: int, y: int) -> None:
            self.x = x
            self.y = y
    
    class SlottedClass:
        __slots__ = ['x', 'y']
        
        def __init__(self, x: int, y: int) -> None:
            self.x = x
            self.y = y
    
    # Create instances
    regular = RegularClass(1, 2)
    slotted = SlottedClass(1, 2)
    
    # Compare memory usage
    print(f"  Regular class size: {sys.getsizeof(regular)} bytes")
    print(f"  Slotted class size: {sys.getsizeof(slotted)} bytes")
    
    # Compare dict usage
    print(f"  Regular has __dict__: {hasattr(regular, '__dict__')}")
    print(f"  Slotted has __dict__: {hasattr(slotted, '__dict__')}")

# ============================================
# Memory Comparison
# ============================================

def memory_comparison() -> None:
    """Compare memory usage with many instances."""
    print("\n=== Memory Comparison ===")
    
    class RegularPoint:
        def __init__(self, x: float, y: float) -> None:
            self.x = x
            self.y = y
    
    class SlottedPoint:
        __slots__ = ['x', 'y']
        
        def __init__(self, x: float, y: float) -> None:
            self.x = x
            self.y = y
    
    # Create many instances
    n = 100000
    
    regular_points = [RegularPoint(i, i) for i in range(n)]
    slotted_points = [SlottedPoint(i, i) for i in range(n)]
    
    # Estimate memory usage
    regular_size = sys.getsizeof(regular_points) + sum(sys.getsizeof(p) for p in regular_points)
    slotted_size = sys.getsizeof(slotted_points) + sum(sys.getsizeof(p) for p in slotted_points)
    
    print(f"  {n} Regular points: ~{regular_size:,} bytes")
    print(f"  {n} Slotted points: ~{slotted_size:,} bytes")
    print(f"  Savings: ~{regular_size - slotted_size:,} bytes ({(1 - slotted_size/regular_size) * 100:.1f}%)")
    
    # Cleanup
    del regular_points
    del slotted_points

# ============================================
# __slots__ with Inheritance
# ============================================

def slots_inheritance() -> None:
    """Demonstrate __slots__ with inheritance."""
    print("\n=== __slots__ with Inheritance ===")
    
    class Base:
        __slots__ = ['a']
        
        def __init__(self, a: int) -> None:
            self.a = a
    
    class Child(Base):
        __slots__ = ['b']
        
        def __init__(self, a: int, b: int) -> None:
            super().__init__(a)
            self.b = b
    
    # Create instance
    obj = Child(1, 2)
    print(f"  a: {obj.a}, b: {obj.b}")
    
    # Check attributes
    print(f"  Has __dict__: {hasattr(obj, '__dict__')}")
    print(f"  Has 'a': {hasattr(obj, 'a')}")
    print(f"  Has 'b': {hasattr(obj, 'b')}")

# ============================================
# __slots__ with Properties
# ============================================

def slots_with_properties() -> None:
    """Demonstrate __slots__ with properties."""
    print("\n=== __slots__ with Properties ===")
    
    class Person:
        __slots__ = ['_name', '_age']
        
        def __init__(self, name: str, age: int) -> None:
            self._name = name
            self._age = age
        
        @property
        def name(self) -> str:
            return self._name
        
        @property
        def age(self) -> int:
            return self._age
        
        @age.setter
        def age(self, value: int) -> None:
            if value < 0:
                raise ValueError("Age cannot be negative")
            self._age = value
    
    # Create instance
    person = Person("Alice", 30)
    print(f"  Name: {person.name}")
    print(f"  Age: {person.age}")
    
    # Modify via property
    person.age = 31
    print(f"  Updated age: {person.age}")

# ============================================
# Performance Test
# ============================================

def performance_test() -> None:
    """Compare performance of regular vs slotted classes."""
    print("\n=== Performance Test ===")
    
    class RegularClass:
        def __init__(self, value: int) -> None:
            self.value = value
    
    class SlottedClass:
        __slots__ = ['value']
        
        def __init__(self, value: int) -> None:
            self.value = value
    
    n = 1000000
    
    # Test creation time
    start = time.time()
    regular = [RegularClass(i) for i in range(n)]
    regular_time = time.time() - start
    
    start = time.time()
    slotted = [SlottedClass(i) for i in range(n)]
    slotted_time = time.time() - start
    
    print(f"  Creation time:")
    print(f"    Regular: {regular_time:.4f}s")
    print(f"    Slotted: {slotted_time:.4f}s")
    
    # Test access time
    start = time.time()
    for obj in regular:
        _ = obj.value
    regular_access = time.time() - start
    
    start = time.time()
    for obj in slotted:
        _ = obj.value
    slotted_access = time.time() - start
    
    print(f"  Access time:")
    print(f"    Regular: {regular_access:.4f}s")
    print(f"    Slotted: {slotted_access:.4f}s")
    
    # Cleanup
    del regular
    del slotted

# ============================================
# Restrictions
# ============================================

def restrictions() -> None:
    """Demonstrate __slots__ restrictions."""
    print("\n=== Restrictions ===")
    
    class SlottedClass:
        __slots__ = ['x']
        
        def __init__(self, x: int) -> None:
            self.x = x
    
    obj = SlottedClass(10)
    
    # Cannot add new attributes
    try:
        obj.y = 20
    except AttributeError as e:
        print(f"  Cannot add attribute: {e}")
    
    # Cannot use __dict__
    try:
        _ = obj.__dict__
    except AttributeError as e:
        print(f"  No __dict__: {e}")
    
    # Cannot use default __repr__
    try:
        print(f"  Default repr: {repr(obj)}")
    except Exception as e:
        print(f"  Repr error: {e}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    basic_slots()
    memory_comparison()
    slots_inheritance()
    slots_with_properties()
    performance_test()
    restrictions()
