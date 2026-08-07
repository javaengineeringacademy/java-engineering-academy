"""
Module 17: Metaclasses - Metaclass Creation Exercises
=====================================================
Practice creating and using metaclasses.
"""

# =============================================================================
# Exercise 1: Basic Metaclass (★☆☆☆☆)
# =============================================================================
# TODO: Create simple metaclass that modifies class creation

class UpperCaseMeta(type):
    """Metaclass that converts all methods to uppercase names."""
    # TODO: Override __new__ to rename methods
    pass

# Test Tests
class MyClass(metaclass=UpperCaseMeta):
    def hello(self):
        return "world"

def test_basic_metaclass():
    obj = MyClass()
    assert hasattr(obj, "HELLO")
    assert obj.HELLO() == "world"
    print("✓ Exercise 1 passed: metaclass renames methods")

# =============================================================================
# Exercise 2: Class Registry (★★☆☆☆)
# =============================================================================
# TODO: Auto-register classes using metaclass

class RegistryMeta(type):
    """Metaclass that auto-registers classes."""
    registry = {}
    # TODO: Register classes in __new__
    pass

# Test Cases
class Animal(metaclass=RegistryMeta):
    pass

class Dog(Animal):
    pass

class Cat(Animal):
    pass

def test_class_registry():
    assert "Dog" in RegistryMeta.registry
    assert "Cat" in RegistryMeta.registry
    assert RegistryMeta.registry["Dog"] is Dog
    print(f"✓ Exercise 2 passed: registered {len(RegistryMeta.registry)} classes")

# =============================================================================
# Exercise 3: Attribute Validation (★★★☆☆)
# =============================================================================
# TODO: Validate class attributes using metaclass

class ValidatedMeta(type):
    """Metaclass that validates class attributes."""
    # TODO: Check required attributes exist
    # TODO: Validate attribute types
    pass

# Test Tests
class ValidatedClass(metaclass=ValidatedMeta):
    _required = ["name", "value"]
    name: str = ""
    value: int = 0

def test_attribute_validation():
    try:
        class InvalidClass(metaclass=ValidatedMeta):
            pass
        assert False, "Should have raised TypeError"
    except TypeError as e:
        assert "name" in str(e)
    print("✓ Exercise 3 passed: metaclass validates attributes")

# =============================================================================
# Exercise 4: Singleton Metaclass (★★★★☆)
# =============================================================================
# TODO: Create singleton using metaclass

class SingletonMeta(type):
    """Metaclass that creates singleton classes."""
    # TODO: Override __call__ to return existing instance
    pass

# Test Cases
class Database(metaclass=SingletonMeta):
    def __init__(self):
        self.connections = []

def test_singleton_metaclass():
    db1 = Database()
    db2 = Database()
    assert db1 is db2
    db1.connections.append("conn1")
    assert len(db2.connections) == 1
    print("✓ Exercise 4 passed: metaclass singleton works")

# =============================================================================
# Exercise 5: Auto-API Generator (★★★★★)
# =============================================================================
# TODO: Auto-generate API methods from specification

class APIMeta(type):
    """Metaclass that generates API methods from spec."""
    # TODO: Read _api_spec and generate methods
    # TODO: Add validation, logging, error handling
    pass

# Test Cases
class UserAPI(metaclass=APIMeta):
    _api_spec = {
        "get_user": {"method": "GET", "path": "/users/{id}"},
        "create_user": {"method": "POST", "path": "/users"},
        "delete_user": {"method": "DELETE", "path": "/users/{id}"},
    }
    
    def _make_request(self, method, path, **kwargs):
        return {"method": method, "path": path, **kwargs}

def test_api_generator():
    api = UserAPI()
    
    result = api.get_user(id=123)
    assert result["method"] == "GET"
    assert "/users/123" in result["path"]
    
    result = api.create_user(name="Alice")
    assert result["method"] == "POST"
    
    result = api.delete_user(id=456)
    assert result["method"] == "DELETE"
    
    print("✓ Exercise 5 passed: API methods auto-generated")

if __name__ == "__main__":
    print("Running Metaclass Creation Exercises...")
    print("=" * 50)
    test_basic_metaclass()
    test_class_registry()
    test_attribute_validation()
    test_singleton_metaclass()
    test_api_generator()
    print("=" * 50)
    print("All tests passed!")
