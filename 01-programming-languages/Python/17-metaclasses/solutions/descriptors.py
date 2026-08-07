"""
Module 17: Metaclasses - Descriptors Solutions
Practice implementing descriptors in Python.
"""

from typing import Any, Optional


class Validator:
    """Descriptor that validates attribute values."""

    def __init__(self, min_value=None, max_value=None):
        self.min_value = min_value
        self.max_value = max_value

    def __set_name__(self, owner, name):
        self.name = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)

    def __set__(self, obj, value):
        if self.min_value is not None and value < self.min_value:
            raise ValueError(f"{self.name} must be >= {self.min_value}")
        if self.max_value is not None and value > self.max_value:
            raise ValueError(f"{self.name} must be <= {self.max_value}")
        obj.__dict__[self.name] = value


class Typed:
    """Descriptor that enforces type checking."""

    def __init__(self, expected_type):
        self.expected_type = expected_type

    def __set_name__(self, owner, name):
        self.name = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)

    def __set__(self, obj, value):
        if not isinstance(value, self.expected_type):
            raise TypeError(
                f"{self.name} must be {self.expected_type.__name__}, "
                f"got {type(value).__name__}"
            )
        obj.__dict__[self.name] = value


class CachedProperty:
    """Descriptor that caches computed values."""

    def __init__(self, func):
        self.func = func
        self.attrname = None

    def __set_name__(self, owner, name):
        self.attrname = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self

        # Check cache
        cache_key = f"_cached_{self.attrname}"
        if cache_key in obj.__dict__:
            return obj.__dict__[cache_key]

        # Compute and cache
        value = self.func(obj)
        obj.__dict__[cache_key] = value
        return value


class LazyProperty:
    """Descriptor that computes value only once, then stores it."""

    def __init__(self, func):
        self.func = func
        self.attrname = None

    def __set_name__(self, owner, name):
        self.attrname = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self

        value = self.func(obj)
        # Replace descriptor with computed value
        setattr(obj, self.attrname, value)
        return value


class UnitConverter:
    """Descriptor that automatically converts units."""

    def __init__(self, from_unit, to_unit, conversion_factor):
        self.from_unit = from_unit
        self.to_unit = to_unit
        self.conversion_factor = conversion_factor

    def __set_name__(self, owner, name):
        self.name = name
        self.internal_name = f"_{name}"

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        value = getattr(obj, self.internal_name, 0)
        return value * self.conversion_factor

    def __set__(self, obj, value):
        setattr(obj, self.internal_name, value)


class Observed:
    """Descriptor that notifies on attribute changes."""

    def __init__(self):
        self.observers = []

    def __set_name__(self, owner, name):
        self.name = name
        self.internal_name = f"_{name}"

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return getattr(obj, self.internal_name, None)

    def __set__(self, obj, value):
        old_value = getattr(obj, self.internal_name, None)
        setattr(obj, self.internal_name, value)

        # Notify observers
        for observer in self.observers:
            observer(obj, self.name, old_value, value)

    def add_observer(self, observer):
        """Add an observer function."""
        self.observers.append(observer)


if __name__ == "__main__":
    print("Testing Descriptors Solutions...")

    # Test Validator
    class Student:
        age = Validator(min_value=0, max_value=150)
        grade = Validator(min_value=0, max_value=100)

        def __init__(self, name, age, grade):
            self.name = name
            self.age = age
            self.grade = grade

    student = Student("Alice", 20, 95)
    assert student.age == 20
    assert student.grade == 95

    try:
        student.age = -5
        print("✗ Should have raised ValueError")
    except ValueError:
        print("✓ Exercise 1: Validator descriptor works")

    # Test Typed
    class Config:
        name = Typed(str)
        value = Typed(int)

        def __init__(self, name, value):
            self.name = name
            self.value = value

    config = Config("debug", 1)
    assert config.name == "debug"

    try:
        config.value = "not an int"
        print("✗ Should have raised TypeError")
    except TypeError:
        print("✓ Exercise 2: Typed descriptor works")

    # Test CachedProperty
    class DataProcessor:
        def __init__(self, data):
            self.data = data

        @CachedProperty
        def processed(self):
            print("Processing...")
            return [x * 2 for x in self.data]

    processor = DataProcessor([1, 2, 3])
    result1 = processor.processed  # Prints "Processing..."
    result2 = processor.processed  # Uses cache, no print
    assert result1 == result2
    print("✓ Exercise 3: CachedProperty descriptor works")

    # Test LazyProperty
    class ExpensiveCalculation:
        def __init__(self, value):
            self.value = value

        @LazyProperty
        def result(self):
            print("Computing...")
            return self.value ** 2

    calc = ExpensiveCalculation(10)
    result1 = calc.result  # Prints "Computing..."
    result2 = calc.result  # No print, uses stored value
    assert result1 == result2 == 100
    print("✓ Exercise 4: LazyProperty descriptor works")

    # Test UnitConverter
    class Measurement:
        def __init__(self, meters):
            self.meters = meters
            self.centimeters = meters

        centimeters = UnitConverter("centimeters", "meters", 0.01)

    m = Measurement(100)
    m.centimeters = 100
    assert m.meters == 1.0
    print("✓ Exercise 5: UnitConverter descriptor works")

    # Test Observed
    class Observable:
        name = Observed()

        def __init__(self, name):
            self.name = name

    changes = []
    obs = Observable("test")
    obs.name.add_observer(lambda obj, attr, old, new: changes.append((old, new)))
    obs.name = "new_value"
    assert len(changes) == 1
    assert changes[0] == ("test", "new_value")
    print("✓ Exercise 6: Observed descriptor works")

    print("All Descriptors solutions passed!")
