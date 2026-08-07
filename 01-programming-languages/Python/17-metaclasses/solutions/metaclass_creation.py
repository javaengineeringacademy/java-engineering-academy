"""
Module 17: Metaclasses - Metaclass Creation Solutions
Practice creating metaclasses in Python.
"""

from typing import Any, Dict, List


class SingletonMeta(type):
    """Metaclass that creates singleton classes."""
    _instances: Dict[type, Any] = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            instance = super().__call__(*args, **kwargs)
            cls._instances[cls] = instance
        return cls._instances[cls]


class ValidatedMeta(type):
    """Metaclass that validates class attributes."""

    def __new__(mcs, name, bases, namespace):
        # Check for required attributes
        if bases:  # Don't check base classes themselves
            for attr_name in ['required_attr']:
                if attr_name not in namespace:
                    raise AttributeError(f"Class {name} must define '{attr_name}'")

        return super().__new__(mcs, name, bases, namespace)


class Auto_repr_Meta(type):
    """Metaclass that automatically adds __repr__ method."""

    def __new__(mcs, name, bases, namespace):
        if '__repr__' not in namespace:
            def __repr__(self):
                attrs = ', '.join(f"{k}={v!r}" for k, v in self.__dict__.items())
                return f"{name}({attrs})"
            namespace['__repr__'] = __repr__

        return super().__new__(mcs, name, bases, namespace)


class RegistryMeta(type):
    """Metaclass that maintains a registry of classes."""

    _registry: Dict[str, type] = {}

    def __init__(cls, name, bases, namespace):
        super().__init__(name, bases, namespace)
        if bases:  # Don't register base classes
            RegistryMeta._registry[name] = cls

    @classmethod
    def get_registry(mcs):
        """Get all registered classes."""
        return mcs._registry.copy()

    @classmethod
    def get_class(mcs, name):
        """Get a class by name."""
        return mcs._registry.get(name)


class HookMeta(type):
    """Metaclass with lifecycle hooks."""

    def __new__(mcs, name, bases, namespace):
        print(f"Creating class: {name}")
        return super().__new__(mcs, name, bases, namespace)

    def __init__(cls, name, bases, namespace):
        print(f"Initializing class: {name}")
        super().__init__(name, bases, namespace)


class AbstractMeta(type):
    """Metaclass that enforces abstract methods."""

    def __new__(mcs, name, bases, namespace):
        # Get abstract methods from all bases
        abstract_methods = set()
        for base in bases:
            if hasattr(base, '__abstractmethods__'):
                abstract_methods.update(base.__abstractmethods__)

        # Check if all abstract methods are implemented
        for method_name in abstract_methods:
            if method_name not in namespace:
                raise TypeError(f"Can't instantiate abstract class {name} "
                              f"without abstract method {method_name}")

        return super().__new__(mcs, name, bases, namespace)


class TypeCheckedMeta(type):
    """Metaclass that enforces type annotations."""

    def __new__(mcs, name, bases, namespace):
        # Check that annotated methods have return types
        for attr_name, attr_value in namespace.items():
            if callable(attr_value) and hasattr(attr_value, '__annotations__'):
                if 'return' not in attr_value.__annotations__:
                    print(f"Warning: {name}.{attr_name} missing return type annotation")

        return super().__new__(mcs, name, bases, namespace)


if __name__ == "__main__":
    print("Testing Metaclass Creation Solutions...")

    # Test Singleton
    class Database(metaclass=SingletonMeta):
        def __init__(self):
            self.connection = "connected"

    db1 = Database()
    db2 = Database()
    assert db1 is db2
    print("✓ Exercise 1: Singleton metaclass works")

    # Test Validated
    try:
        class InvalidClass(metaclass=ValidatedMeta):
            pass
        print("✗ Should have raised AttributeError")
    except AttributeError:
        print("✓ Exercise 2: Validated metaclass works")

    # Test Auto_repr
    class Point(metaclass=Auto_repr_Meta):
        def __init__(self, x, y):
            self.x = x
            self.y = y

    p = Point(1, 2)
    assert "Point" in repr(p)
    assert "x=1" in repr(p)
    print("✓ Exercise 3: Auto repr metaclass works")

    # Test Registry
    class Plugin(metaclass=RegistryMeta):
        pass

    class PluginA(Plugin):
        pass

    class PluginB(Plugin):
        pass

    registry = RegistryMeta.get_registry()
    assert "PluginA" in registry
    assert "PluginB" in registry
    print("✓ Exercise 4: Registry metaclass works")

    # Test Hook
    class TrackedClass(metaclass=HookMeta):
        pass
    print("✓ Exercise 5: Hook metaclass works")

    # Test TypeChecked
    class DocumentedClass(metaclass=TypeCheckedMeta):
        def method(self) -> int:
            return 42
    print("✓ Exercise 6: TypeChecked metaclass works")

    print("All Metaclass Creation solutions passed!")
