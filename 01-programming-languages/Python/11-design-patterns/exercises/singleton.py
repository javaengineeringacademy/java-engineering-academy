"""
Module 11: Design Patterns - Singleton Exercises
================================================
Practice implementing the Singleton design pattern.
"""

# =============================================================================
# Exercise 1: Basic Singleton (★☆☆☆☆)
# =============================================================================
# TODO: Implement singleton using __new__

class DatabaseConnection:
    """Singleton database connection."""
    # TODO: Override __new__ to ensure single instance
    pass

# Test Cases
def test_basic_singleton():
    db1 = DatabaseConnection()
    db2 = DatabaseConnection()
    assert db1 is db2
    print("✓ Exercise 1 passed: singleton pattern works")

# =============================================================================
# Exercise 2: Thread-Safe Singleton (★★☆☆☆)
# =============================================================================
# TODO: Make singleton thread-safe

import threading

class ThreadSafeSingleton:
    """Thread-safe singleton using lock."""
    # TODO: Use threading.Lock to ensure thread safety
    pass

# Test Cases
def test_thread_safe_singleton():
    instances = []
    
    def create_instance():
        instances.append(ThreadSafeSingleton())
    
    threads = [threading.Thread(target=create_instance) for _ in range(10)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()
    
    assert all(inst is instances[0] for inst in instances)
    print(f"✓ Exercise 2 passed: all {len(instances)} instances identical")

# =============================================================================
# Exercise 3: Singleton with Reset (★★★☆☆)
# =============================================================================
# TODO: Singleton that can be reset for testing

class ResettableSingleton:
    """Singleton that can be reset."""
    # TODO: Add _reset class method
    pass

# Test Cases
def test_resettable_singleton():
    s1 = ResettableSingleton()
    s2 = ResettableSingleton()
    assert s1 is s2
    
    ResettableSingleton._reset()
    s3 = ResettableSingleton()
    assert s1 is not s3
    print("✓ Exercise 3 passed: singleton reset works")

# =============================================================================
# Exercise 4: Singleton Registry (★★★★☆)
# =============================================================================
# TODO: Registry that enforces singleton per key

class SingletonRegistry:
    """Registry ensuring one instance per key."""
    # TODO: Implement get_or_create method
    pass

# Test Cases
def test_singleton_registry():
    registry = SingletonRegistry()
    
    db1 = registry.get_or_create("primary", DatabaseConnection)
    db2 = registry.get_or_create("primary", DatabaseConnection)
    db3 = registry.get_or_create("secondary", DatabaseConnection)
    
    assert db1 is db2
    assert db1 is not db3
    print("✓ Exercise 4 passed: registry enforces singletons per key")

# =============================================================================
# Exercise 5: Metaclass Singleton (★★★★★)
# =============================================================================
# TODO: Implement singleton using metaclass

class SingletonMeta(type):
    """Metaclass that creates singleton classes."""
    # TODO: Override __call__ to return existing instance
    pass

# Test Cases
class AppConfig(metaclass=SingletonMeta):
    def __init__(self):
        self.settings = {}

def test_metaclass_singleton():
    config1 = AppConfig()
    config2 = AppConfig()
    assert config1 is config2
    config1.settings["debug"] = True
    assert config2.settings["debug"] is True
    print("✓ Exercise 5 passed: metaclass singleton works")

if __name__ == "__main__":
    print("Running Singleton Pattern Exercises...")
    print("=" * 50)
    test_basic_singleton()
    test_thread_safe_singleton()
    test_resettable_singleton()
    test_singleton_registry()
    test_metaclass_singleton()
    print("=" * 50)
    print("All tests passed!")
