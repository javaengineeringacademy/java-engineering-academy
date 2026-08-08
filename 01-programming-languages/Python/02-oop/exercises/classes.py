"""
Module 02 - OOP: Classes Exercises
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Basic Class (Difficulty: Beginner)
# =============================================================================
# Create a class with basic attributes and methods.

# TODO: Implement the Car class
class Car:
    """A class representing a car."""

    # Class variable (shared by all instances)
    vehicle_count = 0

    def __init__(self, make, model, year, mileage=0):
        """Initialize car with make, model, year, and mileage."""
        pass

    def drive(self, miles):
        """Add miles to odometer."""
        pass

    def get_info(self):
        """Return a string with car information."""
        pass

# Test cases
# car1 = Car("Toyota", "Camry", 2023)
# car2 = Car("Honda", "Civic", 2022, 5000)
# car1.drive(100)
# car2.drive(200)
# print(car1.get_info())   # Expected: "2023 Toyota Camry - 100 miles"
# print(car2.get_info())   # Expected: "2022 Honda Civic - 2500 miles"
# print(f"Total cars: {Car.vehicle_count}")  # Expected: 2


# =============================================================================
# Exercise 2: Class Methods and Static Methods (Difficulty: Intermediate)
# =============================================================================
# Implement class methods and static methods.

# TODO: Implement the Product class
class Product:
    """A class representing a product with price tracking."""

    all_products = []
    tax_rate = 0.08

    def __init__(self, name, price):
        pass

    @classmethod
    def set_tax_rate(cls, rate):
        """Set the tax rate for all products."""
        pass

    @classmethod
    def get_all_products(cls):
        """Return list of all products."""
        pass

    @staticmethod
    def calculate_tax(price, rate):
        """Calculate tax amount."""
        pass

    def price_with_tax(self):
        """Return price including tax."""
        pass

# Test cases
# p1 = Product("Laptop", 999.99)
# p2 = Product("Mouse", 29.99)
# Product.set_tax_rate(0.10)
# print(p1.price_with_tax())   # Expected: 1099.989
# print(Product.calculate_tax(100, 0.08))  # Expected: 8.0
# print(len(Product.get_all_products()))   # Expected: 2


# =============================================================================
# Exercise 3: Properties (Difficulty: Intermediate)
# =============================================================================
# Use properties for controlled access.

# TODO: Implement the BankAccount class
class BankAccount:
    """A class representing a bank account with validation."""

    def __init__(self, owner, balance=0):
        pass

    @property
    def balance(self):
        """Get current balance."""
        pass

    @balance.setter
    def balance(self, value):
        """Set balance with validation (cannot be negative)."""
        pass

    @property
    def owner(self):
        """Get owner name (read-only)."""
        pass

# Test cases
# account = BankAccount("Alice", 1000)
# print(account.balance)     # Expected: 1000
# account.balance = 500      # Should work
# print(account.balance)     # Expected: 500
# try:
#     account.balance = -100  # Should raise ValueError
# except ValueError as e:
#     print(e)               # Expected: "Balance cannot be negative"
# print(account.owner)       # Expected: "Alice"


# =============================================================================
# Exercise 4: String Representation (Difficulty: Beginner)
# =============================================================================
# Implement __repr__ and __str__ methods.

# TODO: Implement the Employee class
class Employee:
    """An employee class with proper string representations."""

    def __init__(self, name, department, salary):
        pass

    def __repr__(self):
        """Developer-friendly representation."""
        pass

    def __str__(self):
        """User-friendly representation."""
        pass

    def __eq__(self, other):
        """Two employees are equal if they have the same name and department."""
        pass

# Test cases
# emp1 = Employee("John Smith", "Engineering", 85000)
# emp2 = Employee("John Smith", "Engineering", 85000)
# emp3 = Employee("Jane Doe", "Marketing", 75000)
# print(repr(emp1))  # Expected: "Employee('John Smith', 'Engineering', 85000)"
# print(str(emp1))   # Expected: "John Smith (Engineering)"
# print(emp1 == emp2)  # Expected: True
# print(emp1 == emp3)  # Expected: False


# =============================================================================
# Exercise 5: Composition (Difficulty: Intermediate)
# =============================================================================
# Use composition to build complex objects.

# TODO: Implement the classes below
class Engine:
    """Represents a car engine."""

    def __init__(self, horsepower, fuel_type):
        pass

    def __str__(self):
        pass

class Wheel:
    """Represents a car wheel."""

    def __init__(self, size, brand):
        pass

    def __str__(self):
        pass

class Vehicle:
    """A vehicle composed of engine and wheels."""

    def __init__(self, make, model, engine, wheels):
        pass

    def __str__(self):
        pass

    def start(self):
        pass

# Test cases
# engine = Engine(200, "Gasoline")
# wheels = [Wheel(17, "Michelin") for _ in range(4)]
# car = Vehicle("Toyota", "Camry", engine, wheels)
# print(car)         # Expected: "Toyota Camry - 200hp Gasoline"
# car.start()        # Expected: "Toyota Camry starts with a roar!"
