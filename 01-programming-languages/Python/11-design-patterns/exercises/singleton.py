"""
Module 11 - Design Patterns: Singleton Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Singleton pattern implementation
"""


# =============================================================================
# Exercise 1: Basic Singleton (⭐⭐⭐)
# =============================================================================

class SingletonBasic:
    """
    Implement a basic Singleton using __new__.
    
    TODO:
    1. Override __new__ to ensure only one instance exists
    2. Store the instance in a class variable
    3. Return existing instance if it exists
    """
    _instance = None
    
    def __new__(cls, *args, **kwargs):
        # TODO: Implement singleton pattern
        pass
    
    def __init__(self, value=None):
        # TODO: Initialize only once
        pass


# =============================================================================
# Exercise 2: Thread-Safe Singleton (⭐⭐⭐⭐)
# =============================================================================

import threading

class ThreadSafeSingleton:
    """
    Implement a thread-safe Singleton using a lock.
    
    TODO:
    1. Add a class-level lock
    2. Use the lock in __new__ to ensure thread safety
    3. Handle double-checked locking pattern
    """
    _instance = None
    _lock = threading.Lock()
    
    def __new__(cls, *args, **kwargs):
        # TODO: Implement thread-safe singleton
        pass
    
    def __init__(self, value=None):
        # TODO: Initialize only once
        pass


# =============================================================================
# Exercise 3: Singleton with Decoration (⭐⭐⭐)
# =============================================================================

def singleton(cls):
    """
    Implement a singleton decorator.
    
    TODO:
    1. Create a decorator that wraps the class
    2. Store the instance in the decorator
    3. Return existing instance if it exists
    """
    # TODO: Implement singleton decorator
    pass


# =============================================================================
# Exercise 4: Singleton Registry (⭐⭐⭐⭐)
# =============================================================================

class SingletonRegistry:
    """
    Implement a registry that manages multiple singleton instances.
    
    TODO:
    1. Create a registry dictionary
    2. Implement get_or_create method
    3. Implement get_instance method
    4. Implement list_instances method
    """
    _registry = {}
    
    @classmethod
    def get_or_create(cls, name, instance_class, *args, **kwargs):
        # TODO: Get existing or create new singleton
        pass
    
    @classmethod
    def get_instance(cls, name):
        # TODO: Get instance by name
        pass
    
    @classmethod
    def list_instances(cls):
        # TODO: Return list of all registered singletons
        pass


# =============================================================================
# Exercise 5: Singleton with Reset (⭐⭐⭐⭐⭐)
# =============================================================================

class ResettableSingleton:
    """
    Implement a Singleton that can be reset (useful for testing).
    
    TODO:
    1. Implement basic singleton pattern
    2. Add a class method to reset/delete the instance
    3. Ensure reset works correctly
    """
    _instance = None
    
    def __new__(cls, *args, **kwargs):
        # TODO: Implement singleton pattern
        pass
    
    def __init__(self, value=None):
        # TODO: Initialize only once
        pass
    
    @classmethod
    def reset(cls):
        # TODO: Reset the singleton instance
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Singleton Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Singleton")
    try:
        s1 = SingletonBasic("first")
        s2 = SingletonBasic("second")
        assert s1 is s2, "Should be same instance"
        assert s1.value == "first", "Value should be 'first'"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Thread-Safe Singleton")
    try:
        s1 = ThreadSafeSingleton("first")
        s2 = ThreadSafeSingleton("second")
        assert s1 is s2, "Should be same instance"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Singleton with Decoration")
    try:
        @singleton
        class MyClass:
            def __init__(self, value):
                self.value = value
        
        o1 = MyClass("first")
        o2 = MyClass("second")
        assert o1 is o2, "Should be same instance"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Singleton Registry")
    try:
        class ServiceA:
            pass
        class ServiceB:
            pass
        
        SingletonRegistry.get_or_create("service_a", ServiceA)
        SingletonRegistry.get_or_create("service_b", ServiceB)
        
        instances = SingletonRegistry.list_instances()
        assert len(instances) == 2, "Should have 2 instances"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Singleton with Reset")
    try:
        s1 = ResettableSingleton("first")
        s1_id = id(s1)
        ResettableSingleton.reset()
        s2 = ResettableSingleton("second")
        assert s1_id != id(s2), "Should be different instances after reset"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
