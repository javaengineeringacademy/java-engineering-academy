"""
Module 00: Knowledge Atoms - Duck Typing Exercises
Practice duck typing concepts in Python.
"""


# =============================================================================
# Exercise 1: Duck Typing Basics (⭐)
# =============================================================================
# Implement classes that demonstrate duck typing. Create different bird classes
# that implement a `fly()` method.

class Duck:
    """A duck that can fly."""
    # TODO: Implement fly() method that returns "Flying like a duck!"

    pass


class Sparrow:
    """A sparrow that can fly."""
    # TODO: Implement fly() method that returns "Flying like a sparrow!"

    pass


class Penguin:
    """A penguin that cannot fly."""
    # TODO: Implement fly() method that returns "Penguins cannot fly!"

    pass


def make_it_fly(bird):
    """Takes any bird-like object and makes it fly.
    This demonstrates duck typing - it works with any object that has a fly() method.
    """
    # TODO: Call the fly() method on the bird and return the result
    pass


# Test Exercise 1
def test_exercise_1():
    print("Exercise 1: Duck Typing Basics")
    duck = Duck()
    sparrow = Sparrow()
    penguin = Penguin()

    assert make_it_fly(duck) == "Flying like a duck!", f"Expected 'Flying like a duck!', got {make_it_fly(duck)}"
    assert make_it_fly(sparrow) == "Flying like a sparrow!", f"Expected 'Flying like a sparrow!', got {make_it_fly(sparrow)}"
    assert make_it_fly(penguin) == "Penguins cannot fly!", f"Expected 'Penguins cannot fly!', got {make_it_fly(penguin)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 2: Iterable Objects (⭐⭐)
# =============================================================================
# Create a custom class that implements the iterator protocol

class CountDown:
    """An iterable that counts down from a given number to 0."""

    def __init__(self, start):
        # TODO: Store the start value
        pass

    def __iter__(self):
        # TODO: Return self as the iterator
        pass

    def __next__(self):
        # TODO: Implement the countdown logic
        # Raise StopIteration when countdown reaches 0
        pass


# Test Exercise 2
def test_exercise_2():
    print("\nExercise 2: Iterable Objects")
    countdown = CountDown(5)
    result = list(countdown)
    assert result == [5, 4, 3, 2, 1, 0], f"Expected [5, 4, 3, 2, 1, 0], got {result}"

    # Test with for loop
    result2 = []
    for num in CountDown(3):
        result2.append(num)
    assert result2 == [3, 2, 1, 0], f"Expected [3, 2, 1, 0], got {result2}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 3: Callable Objects (⭐⭐)
# =============================================================================
# Implement a class that makes instances callable like functions

class Multiplier:
    """A callable class that multiplies its argument by a fixed value."""

    def __init__(self, factor):
        # TODO: Store the factor
        pass

    def __call__(self, value):
        # TODO: Return value multiplied by factor
        pass


# Test Exercise 3
def test_exercise_3():
    print("\nExercise 3: Callable Objects")
    double = Multiplier(2)
    triple = Multiplier(3)

    assert double(5) == 10, f"Expected 10, got {double(5)}"
    assert triple(5) == 15, f"Expected 15, got {triple(5)}"
    assert double(0) == 0, f"Expected 0, got {double(0)}"
    assert double(-3) == -6, f"Expected -6, got {double(-3)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 4: Context Manager Protocol (⭐⭐⭐)
# =============================================================================
# Create a custom context manager class

class FileManager:
    """A context manager that simulates file operations."""

    def __init__(self, filename, mode='r'):
        # TODO: Store filename and mode
        # TODO: Initialize content as None
        pass

    def __enter__(self):
        # TODO: Simulate opening a file
        # Print "Opening {filename}"
        # Return self
        pass

    def __exit__(self, exc_type, exc_val, exc_tb):
        # TODO: Simulate closing a file
        # Print "Closing {filename}"
        # Return False to not suppress exceptions
        pass

    def read(self):
        # TODO: Return simulated file content
        # Return "Content of {filename}"
        pass


# Test Exercise 4
def test_exercise_4():
    print("\nExercise 4: Context Manager Protocol")
    import io
    import sys

    # Capture stdout
    old_stdout = sys.stdout
    sys.stdout = buffer = io.StringIO()

    with FileManager("test.txt") as f:
        content = f.read()

    output = buffer.getvalue()
    sys.stdout = old_stdout

    assert "Opening test.txt" in output, f"Expected 'Opening test.txt' in output, got {output}"
    assert "Closing test.txt" in output, f"Expected 'Closing test.txt' in output, got {output}"
    assert content == "Content of test.txt", f"Expected 'Content of test.txt', got {content}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 5: Advanced Duck Typing (⭐⭐⭐)
# =============================================================================
# Implement functions that work with any object that has specific methods

class Dog:
    """A dog that barks."""
    def speak(self):
        return "Woof!"


class Cat:
    """A cat that meows."""
    def speak(self):
        return "Meow!"


class Cow:
    """A cow that moos."""
    def speak(self):
        return "Moo!"


def quack_all(animals):
    """Call speak() on all animals in the list.
    This demonstrates duck typing - it works with any objects that have speak().
    """
    # TODO: Iterate through animals and collect their speak() results
    # Return a list of all speak() results
    pass


def get_loudest(animals):
    """Return the animal with the longest speak() result.
    Handle empty list by returning None.
    """
    # TODO: Find the animal with the longest speak() result
    # Return the animal object, not the string
    pass


# Test Exercise 5
def test_exercise_5():
    print("\nExercise 5: Advanced Duck Typing")
    animals = [Dog(), Cat(), Cow()]

    result = quack_all(animals)
    assert result == ["Woof!", "Meow!", "Moo!"], f"Expected ['Woof!', 'Meow!', 'Moo!'], got {result}"

    loudest = get_loudest(animals)
    assert isinstance(loudest, Cow), f"Expected Cow instance, got {type(loudest)}"
    assert loudest.speak() == "Moo!", f"Expected 'Moo!', got {loudest.speak()}"

    # Test empty list
    assert get_loudest([]) is None, "Expected None for empty list"

    print("  ✓ All tests passed!")


# =============================================================================
# Main
# =============================================================================
if __name__ == "__main__":
    print("=" * 60)
    print("Module 00: Knowledge Atoms - Duck Typing Exercises")
    print("=" * 60)

    test_exercise_1()
    test_exercise_2()
    test_exercise_3()
    test_exercise_4()
    test_exercise_5()

    print("\n" + "=" * 60)
    print("All exercises completed!")
    print("=" * 60)