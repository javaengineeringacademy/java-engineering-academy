"""
Module 11 - Design Patterns: Singleton Solutions
Complete solutions with explanations
"""

import threading


# =============================================================================
# Exercise 1: Basic Singleton - SOLUTION
# =============================================================================

class SingletonBasic:
    """
    Basic Singleton using __new__.
    
    The __new__ method is called before __init__ and is responsible
    for creating the instance. By overriding it, we can control
    instance creation.
    """
    _instance = None
    
    def __new__(cls, *args, **kwargs):
        # If no instance exists, create one
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance
    
    def __init__(self, value=None):
        # Only initialize if value is provided (first creation)
        if value is not None:
            self.value = value


# =============================================================================
# Exercise 2: Thread-Safe Singleton - SOLUTION
# =============================================================================

class ThreadSafeSingleton:
    """
    Thread-safe Singleton using double-checked locking.
    
    The lock ensures that only one thread can create the instance.
    The double-check avoids acquiring the lock after the instance exists.
    """
    _instance = None
    _lock = threading.Lock()
    
    def __new__(cls, *args, **kwargs):
        # First check (no lock) - fast path
        if cls._instance is None:
            # Acquire lock only when instance doesn't exist
            with cls._lock:
                # Second check (with lock) - safe path
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance
    
    def __init__(self, value=None):
        if value is not None:
            self.value = value


# =============================================================================
# Exercise 3: Singleton with Decoration - SOLUTION
# =============================================================================

def singleton(cls):
    """
    Singleton decorator that wraps a class.
    
    The decorator maintains a dictionary of instances by class.
    This allows multiple different singleton classes.
    """
    instances = {}
    
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    
    return get_instance


# =============================================================================
# Exercise 4: Singleton Registry - SOLUTION
# =============================================================================

class SingletonRegistry:
    """
    Registry that manages multiple singleton instances.
    
    This is useful when you need multiple different singletons
    (e.g., database connections for different services).
    """
    _registry = {}
    
    @classmethod
    def get_or_create(cls, name, instance_class, *args, **kwargs):
        """Get existing instance or create new one."""
        if name not in cls._registry:
            cls._registry[name] = instance_class(*args, **kwargs)
        return cls._registry[name]
    
    @classmethod
    def get_instance(cls, name):
        """Get instance by name, None if not found."""
        return cls._registry.get(name)
    
    @classmethod
    def list_instances(cls):
        """Return list of all registered singleton instances."""
        return list(cls._registry.keys())


# =============================================================================
# Exercise 5: Singleton with Reset - SOLUTION
# =============================================================================

class ResettableSingleton:
    """
    Singleton that can be reset (useful for testing).
    
    The reset classmethod allows deleting the current instance,
    which is useful when you need to test with fresh instances.
    """
    _instance = None
    
    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance
    
    def __init__(self, value=None):
        if value is not None:
            self.value = value
    
    @classmethod
    def reset(cls):
        """Reset the singleton instance (for testing)."""
        cls._instance = None


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Singleton Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Singleton")
    s1 = SingletonBasic("first")
    s2 = SingletonBasic("second")
    assert s1 is s2, "Should be same instance"
    assert s1.value == "first", "Value should be 'first'"
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Thread-Safe Singleton")
    s1 = ThreadSafeSingleton("first")
    s2 = ThreadSafeSingleton("second")
    assert s1 is s2, "Should be same instance"
    assert s1.value == "first", "Value should be 'first'"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Singleton with Decoration")
    @singleton
    class MyClass:
        def __init__(self, value):
            self.value = value
    
    o1 = MyClass("first")
    o2 = MyClass("second")
    assert o1 is o2, "Should be same instance"
    assert o1.value == "first", "Value should be 'first'"
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Singleton Registry")
    class ServiceA:
        pass
    class ServiceB:
        pass
    
    SingletonRegistry.get_or_create("service_a", ServiceA)
    SingletonRegistry.get_or_create("service_b", ServiceB)
    
    instances = SingletonRegistry.list_instances()
    assert len(instances) == 2, "Should have 2 instances"
    assert "service_a" in instances
    assert "service_b" in instances
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Singleton with Reset")
    s1 = ResettableSingleton("first")
    s1_id = id(s1)
    ResettableSingleton.reset()
    s2 = ResettableSingleton("second")
    assert s1_id != id(s2), "Should be different instances after reset"
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
