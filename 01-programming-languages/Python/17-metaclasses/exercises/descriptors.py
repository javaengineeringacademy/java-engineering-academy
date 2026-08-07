"""
Module 17: Metaclasses - Descriptors Exercises
==============================================
Practice creating and using descriptors.
"""

# =============================================================================
# Exercise 1: Basic Descriptor (★☆☆☆☆)
# =============================================================================
# TODO: Create basic property descriptor

class Property:
    """Simple property descriptor."""
    # TODO: Implement __get__, __set__, __delete__
    pass

# Test Cases
class Person:
    name = Property()
    
    def __init__(self, name):
        self.name = name

def test_basic_descriptor():
    p = Person("Alice")
    assert p.name == "Alice"
    p.name = "Bob"
    assert p.name == "Bob"
    print("✓ Exercise 1 passed: descriptor get/set works")

# =============================================================================
# Exercise 2: Validated Attribute (★★☆☆☆)
# =============================================================================
# TODO: Create descriptor that validates values

class PositiveInt:
    """Descriptor that only allows positive integers."""
    # TODO: Validate value is positive integer
    pass

# Test Tests
class Account:
    balance = PositiveInt()
    
    def __init__(self, balance):
        self.balance = balance

def test_validated_descriptor():
    acc = Account(100)
    assert acc.balance == 100
    
    acc.balance = 200
    assert acc.balance == 200
    
    try:
        acc.balance = -50
        assert False, "Should have raised ValueError"
    except ValueError:
        pass
    
    print("✓ Exercise 2 passed: validation works")

# =============================================================================
# Exercise 3: Cached Property (★★★☆☆)
# =============================================================================
# TODO: Create cached property descriptor

class CachedProperty:
    """Property that caches computed value."""
    # TODO: Compute on first access, cache for subsequent
    pass

# Test Cases
class DataProcessor:
    def __init__(self, data):
        self.data = data
    
    @CachedProperty
    def processed(self):
        # Expensive computation
        return sorted(self.data, reverse=True)

def test_cached_property():
    processor = DataProcessor([3, 1, 4, 1, 5, 9, 2, 6])
    
    result1 = processor.processed
    result2 = processor.processed
    
    assert result1 is result2  # Same object, cached
    assert result1 == [9, 6, 5, 4, 3, 2, 1, 1]
    print("✓ Exercise 3 passed: property cached correctly")

# =============================================================================
# Exercise 4: Type Enforced Descriptor (★★★★☆)
# =============================================================================
# TODO: Create descriptor that enforces type

class Typed:
    """Descriptor that enforces type on assignment."""
    # TODO: Check type on __set__
    pass

# Test Cases
class StrictClass:
    name: str = Typed(str)
    age: int = Typed(int)
    score: float = Typed(float)

def test_type_enforcement():
    obj = StrictClass()
    obj.name = "Alice"
    obj.age = 30
    obj.score = 95.5
    
    assert obj.name == "Alice"
    
    try:
        obj.name = 123
        assert False, "Should have raised TypeError"
    except TypeError:
        pass
    
    print("✓ Exercise 4 passed: type enforcement works")

# =============================================================================
# Exercise 5: Observer Descriptor (★★★★★)
# =============================================================================
# TODO: Create descriptor that notifies on changes

class Observable:
    """Descriptor that notifies observers on change."""
    # TODO: Support add_observer, remove_observer
    # TODO: Notify on set/delete
    pass

# Test Classes
class UserModel:
    username = Observable()
    email = Observable()
    
    def __init__(self):
        self._observers = {}

def test_observer_descriptor():
    user = UserModel()
    changes = []
    
    def on_change(attr, old, new):
        changes.append((attr, old, new))
    
    user.observe(on_change)
    user.username = "alice"
    user.username = "bob"
    user.email = "bob@example.com"
    
    assert len(changes) == 3
    assert changes[0] == ("username", None, "alice")
    assert changes[1] == ("username", "alice", "bob")
    print(f"✓ Exercise 5 passed: observed {len(changes)} changes")

if __name__ == "__main__":
    print("Running Descriptors Exercises...")
    print("=" * 50)
    test_basic_descriptor()
    test_validated_descriptor()
    test_cached_property()
    test_type_enforcement()
    test_observer_descriptor()
    print("=" * 50)
    print("All tests passed!")
