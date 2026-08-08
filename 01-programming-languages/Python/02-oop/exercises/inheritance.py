"""
Module 02 - OOP: Inheritance Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Inheritance (Difficulty: Beginner)
# =============================================================================
# Create a class hierarchy with inheritance.

# TODO: Implement the classes
class Animal:
    """Base class for all animals."""

    def __init__(self, name, species, sound):
        pass

    def speak(self):
        pass

    def __str__(self):
        pass

class Dog(Animal):
    """Dog class inheriting from Animal."""

    def __init__(self, name, breed):
        pass

    def fetch(self, item):
        pass

class Cat(Animal):
    """Cat class inheriting from Animal."""

    def __init__(self, name, indoor=True):
        pass

    def purr(self):
        pass

# Test cases
# dog = Dog("Rex", "German Shepherd")
# cat = Cat("Whiskers")
# print(dog.speak())     # Expected: "Rex says Woof!"
# print(cat.speak())     # Expected: "Whiskers says Meow!"
# print(dog.fetch("ball"))  # Expected: "Rex fetches the ball!"
# print(cat.purr())      # Expected: "Whiskers purrs contentedly"


# =============================================================================
# Exercise 2: Method Overriding (Difficulty: Intermediate)
# =============================================================================
# Override parent methods in child classes.

# TODO: Implement the shape classes
class Shape:
    """Base shape class."""

    def __init__(self, color="red"):
        pass

    def area(self):
        """Calculate area (to be overridden)."""
        pass

    def perimeter(self):
        """Calculate perimeter (to be overridden)."""
        pass

    def describe(self):
        pass

class Circle(Shape):
    """Circle shape."""

    def __init__(self, radius, color="red"):
        pass

    def area(self):
        pass

    def perimeter(self):
        pass

class Rectangle(Shape):
    """Rectangle shape."""

    def __init__(self, width, height, color="red"):
        pass

    def area(self):
        pass

    def perimeter(self):
        pass

# Test cases
# circle = Circle(5)
# rect = Rectangle(4, 6)
# print(f"Circle area: {circle.area():.2f}")      # Expected: 78.54
# print(f"Rectangle area: {rect.area()}")         # Expected: 24
# print(circle.describe())                         # Expected: "A red circle"


# =============================================================================
# Exercise 3: Multiple Inheritance (Difficulty: Advanced)
# =============================================================================
# Work with multiple inheritance and MRO.

# TODO: Implement the classes
class Flyer:
    """Mixin for flying ability."""

    def __init__(self):
        self.can_fly = True

    def fly(self):
        pass

class Swimmer:
    """Mixin for swimming ability."""

    def __init__(self):
        self.can_swim = True

    def swim(self):
        pass

class Duck(Flyer, Swimmer):
    """Duck can both fly and swim."""

    def __init__(self, name):
        Flyer.__init__(self)
        Swimmer.__init__(self)
        self.name = name

# Test cases
# duck = Duck("Donald")
# print(duck.fly())    # Expected: "Donald flies with wings!"
# print(duck.swim())   # Expected: "Donald swims gracefully!"
# print(Duck.__mro__)  # Shows method resolution order


# =============================================================================
# Exercise 4: super() Usage (Difficulty: Intermediate)
# =============================================================================
# Use super() to call parent methods.

# TODO: Implement the hierarchy
class Vehicle:
    """Base vehicle class."""

    def __init__(self, make, model, year):
        self.make = make
        self.model = model
        self.year = year

    def get_info(self):
        return f"{self.year} {self.make} {self.model}"

class ElectricVehicle(Vehicle):
    """Electric vehicle with battery."""

    def __init__(self, make, model, year, battery_kwh):
        super().__init__(make, model, year)
        self.battery_kwh = battery_kwh

    def get_info(self):
        base_info = super().get_info()
        return f"{base_info} ({self.battery_kwh}kWh)"

class TeslaModel3(ElectricVehicle):
    """Specific Tesla model."""

    def __init__(self, year, color="white"):
        super().__init__("Tesla", "Model 3", year, 75)
        self.color = color

    def get_info(self):
        base_info = super().get_info()
        return f"{base_info} [{self.color}]"

# Test cases
# tesla = TeslaModel3(2023, "red")
# print(tesla.get_info())  # Expected: "2023 Tesla Model 3 (75kWh) [red]"


# =============================================================================
# Exercise 5: Abstract Base Classes (Difficulty: Intermediate)
# =============================================================================
# Use ABC to create abstract classes.

from abc import ABC, abstractmethod

# TODO: Implement the abstract class and concrete classes
class Database(ABC):
    """Abstract database class."""

    @abstractmethod
    def connect(self):
        pass

    @abstractmethod
    def query(self, sql):
        pass

    @abstractmethod
    def close(self):
        pass

class MySQLDatabase(Database):
    """MySQL implementation."""

    def connect(self):
        pass

    def query(self, sql):
        pass

    def close(self):
        pass

class PostgreSQLDatabase(Database):
    """PostgreSQL implementation."""

    def connect(self):
        pass

    def query(self, sql):
        pass

    def close(self):
        pass

# Test cases
# mysql = MySQLDatabase()
# postgres = PostgreSQLDatabase()
# print(mysql.connect())     # Expected: "Connected to MySQL"
# print(postgres.connect())  # Expected: "Connected to PostgreSQL"
# try:
#     db = Database()  # Should raise TypeError
# except TypeError as e:
#     print(e)
