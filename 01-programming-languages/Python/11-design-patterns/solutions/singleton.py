"""
Module 11: Design Patterns - Singleton Solutions
Practice implementing the Singleton design pattern.
"""

import threading


class DatabaseConnection:
    """Singleton database connection."""
    _instance = None
    _lock = threading.Lock()

    def __new__(cls):
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance


class ThreadSafeSingleton:
    """Thread-safe singleton using lock."""
    _instance = None
    _lock = threading.Lock()

    def __new__(cls):
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance


class ResettableSingleton:
    """Singleton that can be reset."""
    _instance = None
    _lock = threading.Lock()

    def __new__(cls):
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance

    @classmethod
    def _reset(cls):
        with cls._lock:
            cls._instance = None


class SingletonRegistry:
    """Registry ensuring one instance per key."""
    _instances = {}
    _lock = threading.Lock()

    def get_or_create(self, key, cls):
        if key not in self._instances:
            with self._lock:
                if key not in self._instances:
                    self._instances[key] = cls()
        return self._instances[key]


class SingletonMeta(type):
    """Metaclass that creates singleton classes."""
    _instances = {}
    _lock = threading.Lock()

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            with cls._lock:
                if cls not in cls._instances:
                    instance = super().__call__(*args, **kwargs)
                    cls._instances[cls] = instance
        return cls._instances[cls]


if __name__ == "__main__":
    print("Testing Singleton Solutions...")

    # Test basic singleton
    db1 = DatabaseConnection()
    db2 = DatabaseConnection()
    assert db1 is db2
    print("✓ Exercise 1 passed: singleton pattern works")

    # Test thread-safe singleton
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

    # Test resettable singleton
    s1 = ResettableSingleton()
    s2 = ResettableSingleton()
    assert s1 is s2

    ResettableSingleton._reset()
    s3 = ResettableSingleton()
    assert s1 is not s3
    print("✓ Exercise 3 passed: singleton reset works")

    # Test singleton registry
    class ServiceA:
        pass

    class ServiceB:
        pass

    registry = SingletonRegistry()

    svc1 = registry.get_or_create("primary", ServiceA)
    svc2 = registry.get_or_create("primary", ServiceA)
    svc3 = registry.get_or_create("secondary", ServiceB)

    assert svc1 is svc2
    assert svc1 is not svc3
    print("✓ Exercise 4 passed: registry enforces singletons per key")

    # Test metaclass singleton
    class AppConfig(metaclass=SingletonMeta):
        def __init__(self):
            self.settings = {}

    config1 = AppConfig()
    config2 = AppConfig()
    assert config1 is config2
    config1.settings["debug"] = True
    assert config2.settings["debug"] is True
    print("✓ Exercise 5 passed: metaclass singleton works")

    print("All Singleton solutions passed!")
