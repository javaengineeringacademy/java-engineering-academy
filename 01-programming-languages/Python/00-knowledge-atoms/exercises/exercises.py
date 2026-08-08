"""
Module 00 - Knowledge Atoms: Exercises
Topics: Duck Typing, GIL, Garbage Collection, Data Model
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Duck Typing (Difficulty: Beginner)
# =============================================================================
# Create a function that accepts any object with a .quack() method and .feathers()
# method, regardless of its class.

# TODO: Implement the function below
def animal_sound(animal):
    """Call animal.quack() and return the result.
    This should work with ANY object that has a quack method.
    """
    pass


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
# print(animal_sound(Duck()))      # Expected: "Quack!"
# print(animal_sound(Person()))    # Expected: "I'm quacking like a duck!"
# print(animal_sound(Rock()))      # Expected: "..."


# =============================================================================
# Exercise 2: GIL Awareness (Difficulty: Intermediate)
# =============================================================================
# The Global Interpreter Lock (GIL) prevents true parallel execution of threads.
# Create two functions: one CPU-bound and one I/O-bound, and explain when to use
# threading vs multiprocessing.

# TODO: Implement a CPU-bound function
def cpu_bound_task(n):
    """Calculate sum of squares from 1 to n.
    This is CPU-bound and would NOT benefit from threading due to GIL.
    """
    pass

# TODO: Implement an I/O-bound function
def io_bound_task(filename):
    """Read a file and return its contents.
    This IS suitable for threading despite the GIL.
    """
    pass


# Test cases
# Uncomment to test:
# result = cpu_bound_task(1000000)
# print(f"CPU-bound result: {result}")

# with open("test.txt", "w") as f:
#     f.write("Hello, GIL!")
# content = io_bound_task("test.txt")
# print(f"I/O-bound result: {content}")


# =============================================================================
# Exercise 3: Garbage Collection (Difficulty: Intermediate)
# =============================================================================
# Understand how Python's garbage collector works with reference counting and
# circular references.

# TODO: Implement a class that tracks reference counts
class TrackedObject:
    """A class that prints when it's created and destroyed.
    Use weak references or __del__ to track destruction.
    """
    instance_count = 0

    def __init__(self, name):
        pass

    def __del__(self):
        pass


# TODO: Create a circular reference
def create_circular_reference():
    """Create two objects that reference each other (circular reference).
    This tests Python's garbage collector for cycles.
    """
    pass


# Test cases
# Uncomment to test:
# obj1 = TrackedObject("first")
# obj2 = TrackedObject("second")
# print(f"Active instances: {TrackedObject.instance_count}")
# del obj1
# del obj2
# print(f"Active instances after deletion: {TrackedObject.instance_count}")


# =============================================================================
# Exercise 4: Data Model - __len__ and __getitem__ (Difficulty: Beginner)
# =============================================================================
# Implement a class that uses Python's data model to behave like a sequence.

# TODO: Implement the class below
class Portfolio:
    """A collection of stocks that supports len() and indexing.
    Should support:
    - len(portfolio) to get count
    - portfolio[i] to get stock at index
    - 'AAPL' in portfolio to check membership
    """

    def __init__(self, stocks=None):
        pass

    def __len__(self):
        pass

    def __getitem__(self, index):
        pass


# Test cases
# Uncomment to test:
# p = Portfolio(["AAPL", "GOOGL", "MSFT"])
# print(len(p))           # Expected: 3
# print(p[0])             # Expected: "AAPL"
# print("AAPL" in p)      # Expected: True
# print("TSLA" in p)      # Expected: False


# =============================================================================
# Exercise 5: Data Model - __repr__ and __str__ (Difficulty: Beginner)
# =============================================================================
# Implement proper string representations for a class.

# TODO: Implement the class below
class Point:
    """A 2D point with proper string representations.
    - repr(point) should return "Point(x, y)" for debugging
    - str(point) should return "(x, y)" for user display
    - Should support equality comparison
    """

    def __init__(self, x, y):
        pass

    def __repr__(self):
        pass

    def __str__(self):
        pass

    def __eq__(self, other):
        pass


# Test cases
# Uncomment to test:
# p1 = Point(3, 4)
# p2 = Point(3, 4)
# p3 = Point(5, 6)
# print(repr(p1))        # Expected: "Point(3, 4)"
# print(str(p1))         # Expected: "(3, 4)"
# print(p1 == p2)        # Expected: True
# print(p1 == p3)        # Expected: False
