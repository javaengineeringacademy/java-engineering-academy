"""Classes, __init__, self, methods, and instance vs class attributes."""

# ── Basic Class ──────────────────────────────────────────────────────
class Dog:
    """A simple Dog class."""

    # Class attribute (shared by all instances)
    species = "Canis familiaris"

    def __init__(self, name, age):
        # Instance attributes (unique to each instance)
        self.name = name
        self.age = age

    # Instance method (has access to self)
    def bark(self):
        return f"{self.name} says Woof!"

    def __repr__(self):
        return f"Dog('{self.name}', {self.age})"

# Creating instances
buddy = Dog("Buddy", 3)
max_dog = Dog("Max", 5)

print(buddy.bark())       # Buddy says Woof!
print(buddy.species)      # Canis familiaris
print(repr(buddy))        # Dog('Buddy', 3)

# ── Class vs Instance Attributes ────────────────────────────────────
class Counter:
    count = 0  # Class attribute

    def __init__(self):
        Counter.count += 1  # Modify class attribute
        self.id = Counter.count  # Instance attribute

c1 = Counter()
c2 = Counter()
print(Counter.count)  # 2
print(c1.id)          # 1

# ── Methods ──────────────────────────────────────────────────────────
class Rectangle:
    def __init__(self, width, height):
        self.width = width
        self.height = height

    # Instance method
    def area(self):
        return self.width * self.height

    # Class method (receives class as first arg)
    @classmethod
    def from_dimensions(cls, size):
        return cls(size, size)

    # Static method (no access to class or instance)
    @staticmethod
    def is_valid(width, height):
        return width > 0 and height > 0

rect = Rectangle(5, 10)
print(rect.area())              # 50
square = Rectangle.from_dimensions(7)  # Rectangle(7, 7)
print(Rectangle.is_valid(5, -1))      # False

# ── Property Decorator ──────────────────────────────────────────────
class Circle:
    def __init__(self, radius):
        self._radius = radius  # "private" attribute

    @property
    def radius(self):
        """Getter — accessed like an attribute."""
        return self._radius

    @radius.setter
    def radius(self, value):
        """Setter — validates before setting."""
        if value < 0:
            raise ValueError("Radius cannot be negative")
        self._radius = value

    @property
    def area(self):
        """Computed property — no setter."""
        import math
        return math.pi * self._radius ** 2

c = Circle(5)
print(c.radius)      # 5
print(c.area)        # 78.54
c.radius = 10        # Valid
# c.radius = -1      # ValueError

# ── __str__ and __repr__ ────────────────────────────────────────────
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        """Human-readable string."""
        return f"({self.x}, {self.y})"

    def __repr__(self):
        """Developer representation."""
        return f"Point(x={self.x}, y={self.y})"

p = Point(3, 4)
print(str(p))    # (3, 4)
print(repr(p))   # Point(x=3, y=4)

# ── Dataclass (Python 3.7+) ─────────────────────────────────────────
from dataclasses import dataclass, field

@dataclass
class User:
    name: str
    age: int
    email: str = ""
    roles: list = field(default_factory=list)

    @property
    def is_admin(self):
        return "admin" in self.roles

user = User("Alice", 30, "alice@example.com", ["admin"])
print(user)  # User(name='Alice', age=30, email='alice@example.com', roles=['admin'])
print(user.is_admin)  # True
