"""
Module 02 - OOP: Inheritance Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Inheritance - Solution
# =============================================================================
class Animal:
    """Base class for all animals."""

    def __init__(self, name, species, sound):
        self.name = name
        self.species = species
        self.sound = sound

    def speak(self):
        return f"{self.name} says {self.sound}!"

    def __str__(self):
        return f"{self.name} the {self.species}"

class Dog(Animal):
    """Dog class inheriting from Animal."""

    def __init__(self, name, breed):
        super().__init__(name, "Dog", "Woof")
        self.breed = breed

    def fetch(self, item):
        return f"{self.name} fetches the {item}"

class Cat(Animal):
    """Cat class inheriting from Animal."""

    def __init__(self, name, indoor=True):
        super().__init__(name, "Cat", "Meow")
        self.indoor = indoor

    def purr(self):
        return f"{self.name} purrs contentedly"

dog = Dog("Rex", "German Shepherd")
cat = Cat("Whiskers")
print(dog.speak())     # "Rex says Woof!"
print(cat.speak())     # "Whiskers says Meow!"
print(dog.fetch("ball"))  # "Rex fetches the ball!"
print(cat.purr())      # "Whiskers purrs contentedly"


# =============================================================================
# Exercise 2: Method Overriding - Solution
# =============================================================================
import math

class Shape:
    """Base shape class."""

    def __init__(self, color="red"):
        self.color = color

    def area(self):
        return 0

    def perimeter(self):
        return 0

    def describe(self):
        return f"A {self.color} {self.__class__.__name__.lower()}"

class Circle(Shape):
    """Circle shape."""

    def __init__(self, radius, color="red"):
        super().__init__(color)
        self.radius = radius

    def area(self):
        return math.pi * self.radius ** 2

    def perimeter(self):
        return 2 * math.pi * self.radius

class Rectangle(Shape):
    """Rectangle shape."""

    def __init__(self, width, height, color="red"):
        super().__init__(color)
        self.width = width
        self.height = height

    def area(self):
        return self.width * self.height

    def perimeter(self):
        return 2 * (self.width + self.height)

circle = Circle(5)
rect = Rectangle(4, 6)
print(f"Circle area: {circle.area():.2f}")      # 78.54
print(f"Rectangle area: {rect.area()}")         # 24
print(circle.describe())                         # "A red circle"


# =============================================================================
# Exercise 3: Multiple Inheritance - Solution
# =============================================================================
class Flyer:
    """Mixin for flying ability."""

    def __init__(self):
        self.can_fly = True

    def fly(self):
        return f"{self.name} flies with wings!"

class Swimmer:
    """Mixin for swimming ability."""

    def __init__(self):
        self.can_swim = True

    def swim(self):
        return f"{self.name} swims gracefully!"

class Duck(Flyer, Swimmer):
    """Duck can both fly and swim."""

    def __init__(self, name):
        Flyer.__init__(self)
        Swimmer.__init__(self)
        self.name = name

duck = Duck("Donald")
print(duck.fly())    # "Donald flies with wings!"
print(duck.swim())   # "Donald swims gracefully!"
print(Duck.__mro__)  # Shows method resolution order


# =============================================================================
# Exercise 4: super() Usage - Solution
# =============================================================================
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

tesla = TeslaModel3(2023, "red")
print(tesla.get_info())  # "2023 Tesla Model 3 (75kWh) [red]"


# =============================================================================
# Exercise 5: Abstract Base Classes - Solution
# =============================================================================
from abc import ABC, abstractmethod

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
        return "Connected to MySQL"

    def query(self, sql):
        return f"MySQL executing: {sql}"

    def close(self):
        return "MySQL connection closed"

class PostgreSQLDatabase(Database):
    """PostgreSQL implementation."""

    def connect(self):
        return "Connected to PostgreSQL"

    def query(self, sql):
        return f"PostgreSQL executing: {sql}"

    def close(self):
        return "PostgreSQL connection closed"

mysql = MySQLDatabase()
postgres = PostgreSQLDatabase()
print(mysql.connect())     # "Connected to MySQL"
print(postgres.connect())  # "Connected to PostgreSQL"
try:
    db = Database()
except TypeError as e:
    print(e)  # Can't instantiate abstract class
