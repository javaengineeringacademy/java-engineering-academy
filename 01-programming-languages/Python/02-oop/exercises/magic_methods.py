"""
Module 02 - OOP: Magic Methods Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: String Representation (Difficulty: Beginner)
# =============================================================================
# Implement __repr__, __str__, and __format__.

# TODO: Implement the Money class
class Money:
    """A class representing money with proper formatting."""

    def __init__(self, amount, currency="USD"):
        pass

    def __repr__(self):
        pass

    def __str__(self):
        pass

    def __format__(self, format_spec):
        pass

# Test cases
# price = Money(1234.56)
# print(repr(price))         # Expected: "Money(1234.56, 'USD')"
# print(str(price))          # Expected: "$1,234.56"
# print(f"{price:.0f}")      # Expected: "$1,235"
# print(f"{price:EUR}")      # Expected: "€1,234.56"


# =============================================================================
# Exercise 2: Comparison Methods (Difficulty: Intermediate)
# =============================================================================
# Implement comparison magic methods.

# TODO: Implement the Student class
class Student:
    """A student class with comparison support."""

    def __init__(self, name, grade):
        pass

    def __eq__(self, other):
        pass

    def __lt__(self, other):
        pass

    def __le__(self, other):
        pass

    def __gt__(self, other):
        pass

    def __ge__(self, other):
        pass

    def __ne__(self, other):
        pass

# Test cases
# s1 = Student("Alice", 95)
# s2 = Student("Bob", 87)
# s3 = Student("Charlie", 95)
# print(s1 == s3)  # Expected: True (same grade)
# print(s1 > s2)   # Expected: True
# print(s1 < s2)   # Expected: False
# students = [s2, s1, s3]
# print(sorted(students, key=lambda s: s.name))  # Sorted by name


# =============================================================================
# Exercise 3: Container Methods (Difficulty: Intermediate)
# =============================================================================
# Implement container protocol methods.

# TODO: Implement the Library class
class Library:
    """A library of books with container protocol."""

    def __init__(self):
        pass

    def __len__(self):
        pass

    def __getitem__(self, index):
        pass

    def __setitem__(self, index, value):
        pass

    def __delitem__(self, index):
        pass

    def __contains__(self, item):
        pass

    def __iter__(self):
        pass

# Test cases
# library = Library()
# library.append("Python 101")
# library.append("Data Structures")
# library.append("Algorithms")
# print(len(library))           # Expected: 3
# print(library[0])             # Expected: "Python 101"
# print("Python 101" in library) # Expected: True
# del library[1]
# print(len(library))           # Expected: 2
# for book in library:
#     print(book)


# =============================================================================
# Exercise 4: Arithmetic Operations (Difficulty: Intermediate)
# =============================================================================
# Implement arithmetic magic methods.

# TODO: Implement the Vector class
class Vector:
    """A 2D vector with arithmetic operations."""

    def __init__(self, x, y):
        pass

    def __add__(self, other):
        pass

    def __sub__(self, other):
        pass

    def __mul__(self, scalar):
        pass

    def __rmul__(self, scalar):
        pass

    def __abs__(self):
        pass

    def __neg__(self):
        pass

    def __bool__(self):
        pass

# Test cases
# v1 = Vector(3, 4)
# v2 = Vector(1, 2)
# v3 = v1 + v2
# print(v3)        # Expected: "Vector(4, 6)"
# v4 = v1 * 2
# print(v4)        # Expected: "Vector(6, 8)"
# print(abs(v1))   # Expected: 5.0 (magnitude)
# print(-v1)       # Expected: "Vector(-3, -4)"
# print(bool(Vector(0, 0)))  # Expected: False


# =============================================================================
# Exercise 5: Callable Objects (Difficulty: Advanced)
# =============================================================================
# Make objects callable.

# TODO: Implement the Multiplier class
class Multiplier:
    """A callable multiplier object."""

    def __init__(self, factor):
        pass

    def __call__(self, x):
        pass

# TODO: Implement the Pipeline class
class Pipeline:
    """A pipeline of callable functions."""

    def __init__(self):
        pass

    def add(self, func):
        pass

    def __call__(self, x):
        pass

# Test cases
# double = Multiplier(2)
# triple = Multiplier(3)
# print(double(5))     # Expected: 10
# print(triple(5))     # Expected: 15
#
# pipeline = Pipeline()
# pipeline.add(lambda x: x + 1)
# pipeline.add(lambda x: x * 2)
# pipeline.add(lambda x: x - 3)
# print(pipeline(5))   # Expected: 9 (5+1=6, 6*2=12, 12-3=9)
