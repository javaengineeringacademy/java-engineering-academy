# Object-Oriented Programming (OOP)

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
  - [Classes and Objects](#classes-and-objects)
  - [Encapsulation](#encapsulation)
  - [Abstraction](#abstraction)
  - [Inheritance](#inheritance)
  - [Polymorphism](#polymorphism)
- [SOLID Principles](#solid-principles)
- [Composition vs Inheritance](#composition-vs-inheritance)
- [Design Patterns](#design-patterns)
- [Anti-Patterns](#anti-patterns)
- [Examples in Different Languages](#examples-in-different-languages)

---

## Overview

Object-Oriented Programming is a programming approach based on the concept of "objects", which can contain data (attributes) and code (methods). OOP models real-world entities as software objects that have state and behavior.

### Key Benefits

- **Modularity**: Code is organized into discrete objects
- **Reusability**: Through inheritance and composition
- **Maintainability**: Changes are localized to specific objects
- **Scalability**: Systems can grow through object composition

### OOP vs Other Approachs

| Feature | OOP | Functional | Procedural |
|---------|-----|------------|------------|
| Unit of computation | Object | Function | Procedure |
| State | Mutable (encapsulated) | Immutable | Mutable (global) |
| Primary mechanism | Message passing | Function composition | Sequential execution |
| Data & behavior | Bundled together | Separate | Separate |

---

## Core Concepts

### Classes and Objects

A **class** is a blueprint for creating objects. An **object** is an instance of a class.

```
┌─────────────────────────────────────┐
│             CLASS                   │
│  ┌─────────────────────────────┐    │
│  │  Attributes (State)         │    │
│  │  - name: String             │    │
│  │  - age: int                 │    │
│  └─────────────────────────────┘    │
│  ┌─────────────────────────────┐    │
│  │  Methods (Behavior)         │    │
│  │  + getName(): String        │    │
│  │  + setAge(age: int): void   │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
           │
           │ instantiates
           ▼
┌─────────────────────────────────────┐
│             OBJECT                  │
│  name = "Alice"                     │
│  age = 30                           │
└─────────────────────────────────────┘
```

#### Python Example

```python
class Animal:
    def __init__(self, name: str, species: str):
        self.name = name
        self.species = species

    def speak(self) -> str:
        return f"{self.name} makes a sound"

# Creating objects
dog = Animal("Buddy", "Dog")
cat = Animal("Whiskers", "Cat")

print(dog.speak())  # Buddy makes a sound
```

#### Java Example

```java
public class Animal {
    private String name;
    private String species;

    public Animal(String name, String species) {
        this.name = name;
        this.species = species;
    }

    public String speak() {
        return name + " makes a sound";
    }

    // Getters and setters
    public String getName() { return name; }
    public String getSpecies() { return species; }
}
```

#### JavaScript Example

```javascript
class Animal {
    constructor(name, species) {
        this.name = name;
        this.species = species;
    }

    speak() {
        return `${this.name} makes a sound`;
    }
}

const dog = new Animal("Buddy", "Dog");
console.log(dog.speak()); // Buddy makes a sound
```

---

### Encapsulation

Encapsulation is the bundling of data with the methods that operate on that data, restricting direct access to some components.

#### Benefits

- **Data protection**: Prevents invalid state
- **Interface control**: Only expose what's necessary
- **Internal flexibility**: Can change implementation without affecting callers

#### Access Modifiers

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | ✓ | ✓ | ✓ | ✓ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `private` | ✓ | ✗ | ✗ | ✗ |
| `default` (Java) | ✓ | ✓ | ✗ | ✗ |

#### Example

```python
class BankAccount:
    def __init__(self, owner: str, balance: float = 0):
        self._owner = owner        # Protected
        self.__balance = balance   # Private (name mangling)

    def deposit(self, amount: float) -> None:
        if amount <= 0:
            raise ValueError("Deposit amount must be positive")
        self.__balance += amount

    def withdraw(self, amount: float) -> None:
        if amount > self.__balance:
            raise ValueError("Insufficient funds")
        self.__balance -= amount

    def get_balance(self) -> float:
        return self.__balance

account = BankAccount("Alice", 1000)
account.deposit(500)
print(account.get_balance())  # 1500
# account.__balance  # AttributeError - can't access directly
```

---

### Abstraction

Abstraction hides complex implementation details and exposes only the essential features.

#### Abstract Classes

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self) -> float:
        pass

    @abstractmethod
    def perimeter(self) -> float:
        pass

    def describe(self) -> str:
        return f"Shape with area {self.area():.2f}"

class Circle(Shape):
    def __init__(self, radius: float):
        self.radius = radius

    def area(self) -> float:
        return 3.14159 * self.radius ** 2

    def perimeter(self) -> float:
        return 2 * 3.14159 * self.radius

class Rectangle(Shape):
    def __init__(self, width: float, height: float):
        self.width = width
        self.height = height

    def area(self) -> float:
        return self.width * self.height

    def perimeter(self) -> float:
        return 2 * (self.width + self.height)

# shape = Shape()  # TypeError: Can't instantiate abstract class
circle = Circle(5)
print(circle.area())  # 78.53975
```

#### Interfaces

```java
// Java Interface
public interface Drawable {
    void draw();
    default void erase() {
        System.out.println("Erasing...");
    }
}

public interface Resizable {
    void resize(double factor);
}

// Multiple interface implementation
public class Circle implements Drawable, Resizable {
    private double radius;

    @Override
    public void draw() {
        System.out.println("Drawing circle with radius " + radius);
    }

    @Override
    public void resize(double factor) {
        radius *= factor;
    }
}
```

---

### Inheritance

Inheritance allows a class (child/subclass) to inherit attributes and methods from another class (parent/superclass).

#### Types of Inheritance

```
Single Inheritance:
    A
    │
    B

Multiple Inheritance:
    A   B
    │   │
    └─┬─┘
      C

Multilevel Inheritance:
    A
    │
    B
    │
    C

Hierarchical Inheritance:
      A
     / \
    B   C
```

#### Example

```python
class Vehicle:
    def __init__(self, make: str, model: str, year: int):
        self.make = make
        self.model = model
        self.year = year
        self._speed = 0

    def accelerate(self, amount: int) -> None:
        self._speed += amount

    def brake(self, amount: int) -> None:
        self._speed = max(0, self._speed - amount)

    def __str__(self) -> str:
        return f"{self.year} {self.make} {self.model}"

class Car(Vehicle):
    def __init__(self, make: str, model: str, year: int, doors: int = 4):
        super().__init__(make, model, year)
        self.doors = doors
        self.trunk_open = False

    def open_trunk(self) -> None:
        self.trunk_open = True

    def close_trunk(self) -> None:
        self.trunk_open = False

class ElectricCar(Car):
    def __init__(self, make: str, model: str, year: int, battery_kwh: float):
        super().__init__(make, model, year)
        self.battery_kwh = battery_kwh
        self._charge = 100

    def charge(self) -> None:
        self._charge = 100
        print("Charging complete!")

    def drive(self, miles: int) -> None:
        consumption = miles * 0.3  # kWh per mile
        if consumption > self._charge:
            print("Not enough charge!")
            return
        self._charge -= consumption
        self.accelerate(60)
```

#### Method Resolution Order (MRO)

```python
class A:
    def method(self):
        print("A.method")

class B(A):
    def method(self):
        print("B.method")

class C(A):
    def method(self):
        print("C.method")

class D(B, C):
    pass

d = D()
d.method()  # B.method (follows MRO: D -> B -> C -> A)
print(D.__mro__)
# (<class 'D'>, <class 'B'>, <class 'C'>, <class 'A'>, <class 'object'>)
```

---

### Polymorphism

Polymorphism allows objects of different types to be treated as objects of a common base type.

#### Compile-time Polymorphism (Method Overloading)

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }
}
```

#### Runtime Polymorphism (Method Overriding)

```python
from math import pi

class Shape:
    def area(self) -> float:
        raise NotImplementedError

class Circle(Shape):
    def __init__(self, radius: float):
        self.radius = radius

    def area(self) -> float:
        return pi * self.radius ** 2

class Square(Shape):
    def __init__(self, side: float):
        self.side = side

    def area(self) -> float:
        return self.side ** 2

class Triangle(Shape):
    def __init__(self, base: float, height: float):
        self.base = base
        self.height = height

    def area(self) -> float:
        return 0.5 * self.base * self.height

# Polymorphic behavior
def print_area(shape: Shape) -> None:
    print(f"Area: {shape.area():.2f}")

shapes = [Circle(5), Square(4), Triangle(3, 6)]
for shape in shapes:
    print_area(shape)
# Area: 78.54
# Area: 16.00
# Area: 9.00
```

#### Duck Typing (Python)

```python
class Duck:
    def quack(self):
        return "Quack!"

class Person:
    def quack(self):
        return "I'm quacking like a duck!"

class Dog:
    def bark(self):
        return "Woof!"

def make_it_quack(animal):
    # Python uses duck typing - if it has quack(), it can quack
    print(animal.quack())

make_it_quack(Duck())    # Quack!
make_it_quack(Person())  # I'm quacking like a duck!
# make_it_quack(Dog())   # AttributeError: 'Dog' object has no attribute 'quack'
```

---

## SOLID Principles

SOLID is a mnemonic for five design principles that make OOP designs more understandable, flexible, and maintainable.

### S - Single Responsibility Principle (SRP)

A class should have only one reason to change.

```python
# BAD - Multiple responsibilities
class Employee:
    def calculate_pay(self):
        pass

    def save_to_database(self):
        pass

    def generate_report(self):
        pass

# GOOD - Single responsibility
class Employee:
    def __init__(self, name: str, salary: float):
        self.name = name
        self.salary = salary

class PayCalculator:
    def calculate_pay(self, employee: Employee) -> float:
        return employee.salary / 12

class EmployeeRepository:
    def save(self, employee: Employee) -> None:
        print(f"Saving {employee.name} to database")

class ReportGenerator:
    def generate(self, employee: Employee) -> str:
        return f"Report for {employee.name}"
```

### O - Open/Closed Principle (OCP)

Software entities should be open for extension but closed for modification.

```python
# BAD - Must modify class to add new discount types
class DiscountCalculator:
    def calculate(self, discount_type: str, amount: float) -> float:
        if discount_type == "percentage":
            return amount * 0.1
        elif discount_type == "fixed":
            return 10.0
        elif discount_type == "bogo":  # Adding new type requires modification
            return amount

# GOOD - Open for extension, closed for modification
from abc import ABC, abstractmethod

class DiscountStrategy(ABC):
    @abstractmethod
    def calculate(self, amount: float) -> float:
        pass

class PercentageDiscount(DiscountStrategy):
    def __init__(self, percentage: float):
        self.percentage = percentage

    def calculate(self, amount: float) -> float:
        return amount * (self.percentage / 100)

class FixedDiscount(DiscountStrategy):
    def __init__(self, fixed_amount: float):
        self.fixed_amount = fixed_amount

    def calculate(self, amount: float) -> float:
        return min(self.fixed_amount, amount)

# Easy to extend without modifying existing code
class BOGODiscount(DiscountStrategy):
    def calculate(self, amount: float) -> float:
        return amount / 2
```

### L - Liskov Substitution Principle (LSP)

Objects of a superclass should be replaceable with objects of its subclasses without affecting correctness.

```python
# BAD - Violates LSP
class Rectangle:
    def __init__(self, width: float, height: float):
        self._width = width
        self._height = height

    @property
    def width(self):
        return self._width

    @width.setter
    def width(self, value):
        self._width = value

    @property
    def height(self):
        return self._height

    @height.setter
    def height(self, value):
        self._height = value

    def area(self):
        return self._width * self._height

class Square(Rectangle):
    @Rectangle.width.setter
    def width(self, value):
        self._width = value
        self._height = value  # Violates LSP - unexpected behavior

# GOOD - Follows LSP
class Shape(ABC):
    @abstractmethod
    def area(self) -> float:
        pass

class Rectangle(Shape):
    def __init__(self, width: float, height: float):
        self.width = width
        self.height = height

    def area(self) -> float:
        return self.width * self.height

class Square(Shape):
    def __init__(self, side: float):
        self.side = side

    def area(self) -> float:
        return self.side ** 2
```

### I - Interface Segregation Principle (ISP)

No client should be forced to depend on interfaces it does not use.

```python
# BAD - Fat interface
class Worker(ABC):
    @abstractmethod
    def work(self):
        pass

    @abstractmethod
    def eat(self):
        pass

    @abstractmethod
    def sleep(self):
        pass

# GOOD - Segregated interfaces
class Workable(ABC):
    @abstractmethod
    def work(self):
        pass

class Eatable(ABC):
    @abstractmethod
    def eat(self):
        pass

class Sleepable(ABC):
    @abstractmethod
    def sleep(self):
        pass

class HumanWorker(Workable, Eatable, Sleepable):
    def work(self):
        print("Human working")

    def eat(self):
        print("Human eating")

    def sleep(self):
        print("Human sleeping")

class RobotWorker(Workable):
    def work(self):
        print("Robot working")
    # Robot doesn't need eat() or sleep()
```

### D - Dependency Inversion Principle (DIP)

High-level modules should not depend on low-level modules. Both should depend on abstractions.

```python
# BAD - High-level depends on low-level
class MySQLDatabase:
    def query(self, sql: str):
        print(f"Executing {sql} on MySQL")

class UserService:
    def __init__(self):
        self.db = MySQLDatabase()  # Tight coupling

    def get_user(self, user_id: int):
        return self.db.query(f"SELECT * FROM users WHERE id = {user_id}")

# GOOD - Both depend on abstraction
from abc import ABC, abstractmethod

class Database(ABC):
    @abstractmethod
    def query(self, sql: str):
        pass

class MySQLDatabase(Database):
    def query(self, sql: str):
        print(f"Executing {sql} on MySQL")

class PostgreSQLDatabase(Database):
    def query(self, sql: str):
        print(f"Executing {sql} on PostgreSQL")

class UserService:
    def __init__(self, database: Database):  # Depends on abstraction
        self.db = database

    def get_user(self, user_id: int):
        return self.db.query(f"SELECT * FROM users WHERE id = {user_id}")

# Easily switch implementations
service = UserService(MySQLDatabase())
service = UserService(PostgreSQLDatabase())
```

---

## Composition vs Inheritance

### When to Use Inheritance

- "is-a" relationship (Dog is an Animal)
- Shared interface/contract
- Hierarchical classification

### When to Use Composition

- "has-a" relationship (Car has an Engine)
- Need for runtime behavior change
- Avoiding deep inheritance hierarchies

### Example Comparison

```python
# INHERITANCE approach
class Animal:
    def __init__(self, name: str):
        self.name = name

    def speak(self):
        raise NotImplementedError

class Dog(Animal):
    def speak(self):
        return "Woof!"

class Cat(Animal):
    def speak(self):
        return "Meow!"

# COMPOSITION approach
class SoundBehavior:
    def make_sound(self):
        raise NotImplementedError

class Bark(SoundBehavior):
    def make_sound(self):
        return "Woof!"

class Meow(SoundBehavior):
    def make_sound(self):
        return "Meow!"

class Silence(SoundBehavior):
    def make_sound(self):
        return "..."

class Animal:
    def __init__(self, name: str, sound_behavior: SoundBehavior):
        self.name = name
        self.sound_behavior = sound_behavior

    def speak(self):
        return self.sound_behavior.make_sound()

    def set_sound(self, behavior: SoundBehavior):
        self.sound_behavior = behavior

# Behavior can change at runtime with composition
dog = Dog("Rex", Bark())
print(dog.speak())  # Woof!
dog.set_sound(Silence())
print(dog.speak())  # ...
```

### Favor Composition Over Inheritance

| Aspect | Inheritance | Composition |
|--------|-------------|-------------|
| Coupling | Tight | Loose |
| Flexibility | Compile-time | Runtime |
| Code reuse | Automatic | Explicit |
| Testability | Harder | Easier |
| Depth | Can become deep | Flat |

---

## Design Patterns

### Creational Patterns

| Pattern | Purpose |
|---------|---------|
| Singleton | Ensure single instance |
| Factory Method | Create objects without specifying class |
| Abstract Factory | Create families of related objects |
| Builder | Construct complex objects step by step |
| Prototype | Create objects by cloning |

### Structural Patterns

| Pattern | Purpose |
|---------|---------|
| Adapter | Interface incompatible classes |
| Bridge | Separate abstraction from implementation |
| Composite | Tree structures of objects |
| Decorator | Add responsibilities dynamically |
| Facade | Simplified interface to complex subsystem |

### Behavioral Patterns

| Pattern | Purpose |
|---------|---------|
| Observer | One-to-many dependency notification |
| Strategy | Interchangeable algorithms |
| Command | Encapsulate requests as objects |
| Iterator | Sequential access without暴露 representation |
| Template Method | Define algorithm skeleton, defer steps |

---

## Anti-Patterns

### God Object

A class that knows too much or does too much.

```python
# BAD - God Object
class Application:
    def __init__(self):
        self.users = []
        self.orders = []
        self.products = []
        self.config = {}
        self.db_connection = None
        self.logger = None

    def create_user(self): pass
    def delete_user(self): pass
    def create_order(self): pass
    def process_payment(self): pass
    def send_email(self): pass
    def log_error(self): pass
    # ... 50 more methods
```

### Spaghetti Code

Unstructured and difficult to maintain code.

```python
# BAD - Spaghetti Code
def process_order(order):
    if order['type'] == 'regular':
        if order['amount'] > 100:
            if order['customer']['vip']:
                discount = 0.2
                if order['items']:
                    for item in order['items']:
                        if item['stock'] > 0:
                            # Deeply nested, hard to follow logic
                            pass
```

---

## Summary

| Concept | Description | Key Benefit |
|---------|-------------|-------------|
| Encapsulation | Hide internal state | Data protection |
| Abstraction | Hide complexity | Simplified interface |
| Inheritance | Share behavior/code | Code reuse |
| Polymorphism | Multiple forms | Flexibility |
| SOLID | Design principles | Maintainability |
| Composition | Build from parts | Loose coupling |
