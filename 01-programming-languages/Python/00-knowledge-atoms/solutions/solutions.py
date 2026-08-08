"""
Module 00 - Knowledge Atoms: Solutions
Topics: Duck Typing, GIL, Garbage Collection, Data Model
"""

# =============================================================================
# Exercise 1: Duck Typing - Solution
# =============================================================================
# Python uses duck typing: "If it walks like a duck and quacks like a duck,
# it's a duck." We don't check the type, we check the behavior.

def animal_sound(animal):
    """Call animal.quack() and return the result.
    This works with ANY object that has a quack method.
    """
    return animal.quack()


# Test cases
class Duck:
    def quack(self):
        return "Quack!"
    def feathers(self):
        return "White feathers"

class Person:
    def quack(self):
        return "I'm quacking like a duck!"
    def feathers(self):
        return "I use hair gel"

class Rock:
    def quack(self):
        return "..."
    def feathers(self):
        return None

# Uncomment to test:
print(animal_sound(Duck()))      # Expected: "Quack!"
print(animal_sound(Person()))    # Expected: "I'm quacking like a duck!"
print(animal_sound(Rock()))      # Expected: "..."


# =============================================================================
# Exercise 2: GIL Awareness - Solution
# =============================================================================
# The GIL prevents multiple threads from executing Python bytecodes at once.
# - CPU-bound tasks: Use multiprocessing (bypasses GIL)
# - I/O-bound tasks: Use threading (releases GIL during I/O)

import threading

def cpu_bound_task(n):
    """Calculate sum of squares from 1 to n.
    This is CPU-bound and would NOT benefit from threading due to GIL.
    Use multiprocessing instead for true parallelism.
    """
    return sum(i * i for i in range(1, n + 1))

def io_bound_task(filename):
    """Read a file and return its contents.
    This IS suitable for threading because:
    - Thread releases GIL during file I/O
    - Multiple threads can read different files concurrently
    """
    with open(filename, 'r') as f:
        return f.read()


# Test cases
# Uncomment to test:
# result = cpu_bound_task(1000000)
# print(f"CPU-bound result: {result}")

# with open("test.txt", "w") as f:
#     f.write("Hello, GIL!")
# content = io_bound_task("test.txt")
# print(f"I/O-bound result: {content}")


# =============================================================================
# Exercise 3: Garbage Collection - Solution
# =============================================================================
# Python uses reference counting as primary mechanism + cyclic GC for cycles.
# Reference count increases when assigned, decreases when deleted.

class TrackedObject:
    """A class that prints when it's created and destroyed."""

    instance_count = 0

    def __init__(self, name):
        self.name = name
        TrackedObject.instance_count += 1
        print(f"Created {name}. Active: {TrackedObject.instance_count}")

    def __del__(self):
        TrackedObject.instance_count -= 1
        print(f"Destroyed {self.name}. Active: {TrackedObject.instance_count}")


def create_circular_reference():
    """Create two objects that reference each other (circular reference).
    Python's cyclic garbage collector detects and cleans these up.
    """
    class Node:
        def __init__(self, name):
            self.name = name
            self.ref = None

    a = Node("A")
    b = Node("B")
    a.ref = b  # A -> B
    b.ref = a  # B -> A (circular reference!)

    # Both objects have ref count > 0 but are unreachable
    # The cyclic GC will clean them up when we delete a and b
    return a, b


# Test cases
# Uncomment to test:
# obj1 = TrackedObject("first")
# obj2 = TrackedObject("second")
# print(f"Active instances: {TrackedObject.instance_count}")
# del obj1
# del obj2
# print(f"Active instances after deletion: {TrackedObject.instance_count}")


# =============================================================================
# Exercise 4: Data Model - __len__ and __getitem__ - Solution
# =============================================================================
# Python's data model allows objects to implement built-in behavior.
# __len__ enables len(), __getitem__ enables indexing and 'in' operator.

class Portfolio:
    """A collection of stocks that supports len() and indexing."""

    def __init__(self, stocks=None):
        self.stocks = list(stocks) if stocks else []

    def __len__(self):
        """Called by len() function."""
        return len(self.stocks)

    def __getitem__(self, index):
        """Called by indexing operator []. Also enables 'in' operator."""
        return self.stocks[index]


# Test cases
p = Portfolio(["AAPL", "GOOGL", "MSFT"])
print(len(p))           # Expected: 3
print(p[0])             # Expected: "AAPL"
print("AAPL" in p)      # Expected: True
print("TSLA" in p)      # Expected: False


# =============================================================================
# Exercise 5: Data Model - __repr__ and __str__ - Solution
# =============================================================================
# __repr__: Unambiguous representation for developers (debugging)
# __str__: Readable representation for end users

class Point:
    """A 2D point with proper string representations."""

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        """Developer-friendly: Point(3, 4)"""
        return f"Point({self.x}, {self.y})"

    def __str__(self):
        """User-friendly: (3, 4)"""
        return f"({self.x}, {self.y})"

    def __eq__(self, other):
        """Enable == comparison."""
        if not isinstance(other, Point):
            return NotImplemented
        return self.x == other.x and self.y == other.y


# Test cases
p1 = Point(3, 4)
p2 = Point(3, 4)
p3 = Point(5, 6)
print(repr(p1))        # Expected: "Point(3, 4)"
print(str(p1))         # Expected: "(3, 4)"
print(p1 == p2)        # Expected: True
print(p1 == p3)        # Expected: False
