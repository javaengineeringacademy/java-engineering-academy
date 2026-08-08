"""
Module 02 - OOP: Classes Solutions
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Basic Class - Solution
# =============================================================================
class Car:
    """A class representing a car."""

    vehicle_count = 0

    def __init__(self, make, model, year, mileage=0):
        self.make = make
        self.model = model
        self.year = year
        self.mileage = mileage
        Car.vehicle_count += 1

    def drive(self, miles):
        self.mileage += miles

    def get_info(self):
        return f"{self.year} {self.make} {self.model} - {self.mileage} miles"

car1 = Car("Toyota", "Camry", 2023)
car2 = Car("Honda", "Civic", 2022, 5000)
car1.drive(100)
car2.drive(200)
print(car1.get_info())   # "2023 Toyota Camry - 100 miles"
print(car2.get_info())   # "2022 Honda Civic - 2500 miles"
print(f"Total cars: {Car.vehicle_count}")  # 2


# =============================================================================
# Exercise 2: Class Methods and Static Methods - Solution
# =============================================================================
class Product:
    """A class representing a product with price tracking."""

    all_products = []
    tax_rate = 0.08

    def __init__(self, name, price):
        self.name = name
        self.price = price
        Product.all_products.append(self)

    @classmethod
    def set_tax_rate(cls, rate):
        cls.tax_rate = rate

    @classmethod
    def get_all_products(cls):
        return cls.all_products

    @staticmethod
    def calculate_tax(price, rate):
        return price * rate

    def price_with_tax(self):
        return self.price * (1 + self.tax_rate)

p1 = Product("Laptop", 999.99)
p2 = Product("Mouse", 29.99)
Product.set_tax_rate(0.10)
print(p1.price_with_tax())   # 1099.989
print(Product.calculate_tax(100, 0.08))  # 8.0
print(len(Product.get_all_products()))   # 2


# =============================================================================
# Exercise 3: Properties - Solution
# =============================================================================
class BankAccount:
    """A class representing a bank account with validation."""

    def __init__(self, owner, balance=0):
        self._owner = owner
        self._balance = balance

    @property
    def balance(self):
        return self._balance

    @balance.setter
    def balance(self, value):
        if value < 0:
            raise ValueError("Balance cannot be negative")
        self._balance = value

    @property
    def owner(self):
        return self._owner

account = BankAccount("Alice", 1000)
print(account.balance)     # 1000
account.balance = 500
print(account.balance)     # 500
try:
    account.balance = -100
except ValueError as e:
    print(e)               # "Balance cannot be negative"
print(account.owner)       # "Alice"


# =============================================================================
# Exercise 4: String Representation - Solution
# =============================================================================
class Employee:
    """An employee class with proper string representations."""

    def __init__(self, name, department, salary):
        self.name = name
        self.department = department
        self.salary = salary

    def __repr__(self):
        return f"Employee('{self.name}', '{self.department}', {self.salary})"

    def __str__(self):
        return f"{self.name} ({self.department})"

    def __eq__(self, other):
        if not isinstance(other, Employee):
            return NotImplemented
        return self.name == other.name and self.department == other.department

emp1 = Employee("John Smith", "Engineering", 85000)
emp2 = Employee("John Smith", "Engineering", 85000)
emp3 = Employee("Jane Doe", "Marketing", 75000)
print(repr(emp1))  # "Employee('John Smith', 'Engineering', 85000)"
print(str(emp1))   # "John Smith (Engineering)"
print(emp1 == emp2)  # True
print(emp1 == emp3)  # False


# =============================================================================
# Exercise 5: Composition - Solution
# =============================================================================
class Engine:
    """Represents a car engine."""

    def __init__(self, horsepower, fuel_type):
        self.horsepower = horsepower
        self.fuel_type = fuel_type

    def __str__(self):
        return f"{self.horsepower}hp {self.fuel_type}"

class Wheel:
    """Represents a car wheel."""

    def __init__(self, size, brand):
        self.size = size
        self.brand = brand

    def __str__(self):
        return f"{self.brand} {self.size}\""

class Vehicle:
    """A vehicle composed of engine and wheels."""

    def __init__(self, make, model, engine, wheels):
        self.make = make
        self.model = model
        self.engine = engine
        self.wheels = wheels

    def __str__(self):
        return f"{self.make} {self.model} - {self.engine}"

    def start(self):
        return f"{self.make} {self.model} starts with a roar!"

engine = Engine(200, "Gasoline")
wheels = [Wheel(17, "Michelin") for _ in range(4)]
car = Vehicle("Toyota", "Camry", engine, wheels)
print(car)         # "Toyota Camry - 200hp Gasoline"
print(car.start()) # "Toyota Camry starts with a roar!"
