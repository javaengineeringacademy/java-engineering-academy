"""
Module 02 - OOP: Magic Methods Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: String Representation - Solution
# =============================================================================
class Money:
    """A class representing money with proper formatting."""

    CURRENCY_SYMBOLS = {"USD": "$", "EUR": "€", "GBP": "£"}

    def __init__(self, amount, currency="USD"):
        self.amount = amount
        self.currency = currency

    def __repr__(self):
        return f"Money({self.amount}, '{self.currency}')"

    def __str__(self):
        symbol = self.CURRENCY_SYMBOLS.get(self.currency, self.currency)
        return f"{symbol}{self.amount:,.2f}"

    def __format__(self, format_spec):
        if format_spec in self.CURRENCY_SYMBOLS:
            symbol = self.CURRENCY_SYMBOLS[format_spec]
            return f"{symbol}{self.amount:,.2f}"
        symbol = self.CURRENCY_SYMBOLS.get(self.currency, self.currency)
        return f"{symbol}{self.amount:{format_spec}}"

price = Money(1234.56)
print(repr(price))         # "Money(1234.56, 'USD')"
print(str(price))          # "$1,234.56"
print(f"{price:.0f}")      # "$1,235"
print(f"{price:EUR}")      # "€1,234.56"


# =============================================================================
# Exercise 2: Comparison Methods - Solution
# =============================================================================
class Student:
    """A student class with comparison support."""

    def __init__(self, name, grade):
        self.name = name
        self.grade = grade

    def __eq__(self, other):
        return self.grade == other.grade

    def __lt__(self, other):
        return self.grade < other.grade

    def __le__(self, other):
        return self.grade <= other.grade

    def __gt__(self, other):
        return self.grade > other.grade

    def __ge__(self, other):
        return self.grade >= other.grade

    def __ne__(self, other):
        return self.grade != other.grade

s1 = Student("Alice", 95)
s2 = Student("Bob", 87)
s3 = Student("Charlie", 95)
print(s1 == s3)  # True
print(s1 > s2)   # True
print(s1 < s2)   # False
students = [s2, s1, s3]
print(sorted(students, key=lambda s: s.name))


# =============================================================================
# Exercise 3: Container Methods - Solution
# =============================================================================
class Library:
    """A library of books with container protocol."""

    def __init__(self):
        self._books = []

    def append(self, book):
        self._books.append(book)

    def __len__(self):
        return len(self._books)

    def __getitem__(self, index):
        return self._books[index]

    def __setitem__(self, index, value):
        self._books[index] = value

    def __delitem__(self, index):
        del self._books[index]

    def __contains__(self, item):
        return item in self._books

    def __iter__(self):
        return iter(self._books)

library = Library()
library.append("Python 101")
library.append("Data Structures")
library.append("Algorithms")
print(len(library))           # 3
print(library[0])             # "Python 101"
print("Python 101" in library) # True
del library[1]
print(len(library))           # 2
for book in library:
    print(book)


# =============================================================================
# Exercise 4: Arithmetic Operations - Solution
# =============================================================================
class Vector:
    """A 2D vector with arithmetic operations."""

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)

    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y)

    def __mul__(self, scalar):
        return Vector(self.x * scalar, self.y * scalar)

    def __rmul__(self, scalar):
        return self.__mul__(scalar)

    def __abs__(self):
        return (self.x ** 2 + self.y ** 2) ** 0.5

    def __neg__(self):
        return Vector(-self.x, -self.y)

    def __bool__(self):
        return self.x != 0 or self.y != 0

    def __str__(self):
        return f"Vector({self.x}, {self.y})"

v1 = Vector(3, 4)
v2 = Vector(1, 2)
v3 = v1 + v2
print(v3)        # "Vector(4, 6)"
v4 = v1 * 2
print(v4)        # "Vector(6, 8)"
print(abs(v1))   # 5.0
print(-v1)       # "Vector(-3, -4)"
print(bool(Vector(0, 0)))  # False


# =============================================================================
# Exercise 5: Callable Objects - Solution
# =============================================================================
class Multiplier:
    """A callable multiplier object."""

    def __init__(self, factor):
        self.factor = factor

    def __call__(self, x):
        return x * self.factor

class Pipeline:
    """A pipeline of callable functions."""

    def __init__(self):
        self.functions = []

    def add(self, func):
        self.functions.append(func)

    def __call__(self, x):
        result = x
        for func in self.functions:
            result = func(result)
        return result

double = Multiplier(2)
triple = Multiplier(3)
print(double(5))     # 10
print(triple(5))     # 15

pipeline = Pipeline()
pipeline.add(lambda x: x + 1)
pipeline.add(lambda x: x * 2)
pipeline.add(lambda x: x - 3)
print(pipeline(5))   # 9 (5+1=6, 6*2=12, 12-3=9)
