"""
Module 00: Knowledge Atoms - Duck Typing Exercises (Solutions)
Practice duck typing concepts in Python.
"""


# Exercise 1: Duck Typing Basics (⭐)

class Duck:
    """A duck that can fly."""
    def fly(self):
        return "Flying like a duck!"


class Sparrow:
    """A sparrow that can fly."""
    def fly(self):
        return "Flying like a sparrow!"


class Penguin:
    """A penguin that cannot fly."""
    def fly(self):
        return "Penguins cannot fly!"


def make_it_fly(bird):
    """Takes any bird-like object and makes it fly."""
    return bird.fly()


# Exercise 2: Iterable Objects (⭐⭐)

class CountDown:
    """An iterable that counts down from a given number to 0."""

    def __init__(self, start):
        self.start = start
        self.current = start

    def __iter__(self):
        return self

    def __next__(self):
        if self.current < 0:
            raise StopIteration
        value = self.current
        self.current -= 1
        return value


# Exercise 3: Callable Objects (⭐⭐)

class Multiplier:
    """A callable class that multiplies its argument by a fixed value."""

    def __init__(self, factor):
        self.factor = factor

    def __call__(self, value):
        return value * self.factor


# Exercise 4: Context Manager Protocol (⭐⭐⭐)

class FileManager:
    """A context manager that simulates file operations."""

    def __init__(self, filename, mode='r'):
        self.filename = filename
        self.mode = mode
        self.content = None

    def __enter__(self):
        print(f"Opening {self.filename}")
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        print(f"Closing {self.filename}")
        return False

    def read(self):
        return f"Content of {self.filename}"


# Exercise 5: Advanced Duck Typing (⭐⭐⭐)

class Dog:
    """A dog that barks."""
    def speak(self):
        return "Woof!"


class Cat:
    """A cat that meows."""
    def speak(self):
        return "Meow!"


class Cow:
    """A cow that moos loudly."""
    def speak(self):
        return "Moooooo!"


def quack_all(animals):
    """Call speak() on all animals in the list."""
    return [animal.speak() for animal in animals]


def get_loudest(animals):
    """Return the animal with the longest speak() result."""
    if not animals:
        return None
    return max(animals, key=lambda a: len(a.speak()))


# Test cases
if __name__ == "__main__":
    print("Testing Module 00 Solutions...")

    # Test Exercise 1
    duck = Duck()
    sparrow = Sparrow()
    penguin = Penguin()
    assert make_it_fly(duck) == "Flying like a duck!"
    assert make_it_fly(sparrow) == "Flying like a sparrow!"
    assert make_it_fly(penguin) == "Penguins cannot fly!"
    print("✓ Exercise 1 passed")

    # Test Exercise 2
    countdown = CountDown(5)
    result = list(countdown)
    assert result == [5, 4, 3, 2, 1, 0]
    print("✓ Exercise 2 passed")

    # Test Exercise 3
    double = Multiplier(2)
    triple = Multiplier(3)
    assert double(5) == 10
    assert triple(5) == 15
    print("✓ Exercise 3 passed")

    # Test Exercise 4
    with FileManager("test.txt") as f:
        content = f.read()
    assert content == "Content of test.txt"
    print("✓ Exercise 4 passed")

    # Test Exercise 5
    animals = [Dog(), Cat(), Cow()]
    result = quack_all(animals)
    assert result == ["Woof!", "Meow!", "Moooooo!"]
    loudest = get_loudest(animals)
    assert isinstance(loudest, Cow)
    assert get_loudest([]) is None
    print("✓ Exercise 5 passed")

    print("\nAll Module 00 solutions passed!")
