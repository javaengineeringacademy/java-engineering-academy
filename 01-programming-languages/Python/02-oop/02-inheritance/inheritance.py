"""Single, multiple inheritance, and MRO."""

# ── Single Inheritance ───────────────────────────────────────────────
class Animal:
    def __init__(self, name, sound):
        self.name = name
        self.sound = sound

    def speak(self):
        return f"{self.name} says {self.sound}!"

    def __repr__(self):
        return f"Animal('{self.name}', '{self.sound}')"

class Dog(Animal):
    def __init__(self, name, breed):
        super().__init__(name, sound="Woof")  # Call parent __init__
        self.breed = breed

    def fetch(self, item):
        return f"{self.name} fetches the {item}!"

class Cat(Animal):
    def __init__(self, name, indoor=True):
        super().__init__(name, sound="Meow")
        self.indoor = indoor

    def speak(self):  # Override parent method
        return f"{self.name} purrs!"

dog = Dog("Rex", "German Shepherd")
print(dog.speak())      # Rex says Woof! (inherited)
print(dog.fetch("ball"))  # Rex fetches the ball!
print(dog.breed)        # German Shepherd

cat = Cat("Whiskers")
print(cat.speak())      # Whiskers purrs! (overridden)

# ── Multiple Inheritance ─────────────────────────────────────────────
class Swimmer:
    def swim(self):
        return f"{self.name} is swimming"

class Flyer:
    def fly(self):
        return f"{self.name} is flying"

class Duck(Animal, Swimmer, Flyer):
    def __init__(self, name):
        super().__init__(name, sound="Quack")

donald = Duck("Donald")
print(donald.speak())    # Donald says Quack!
print(donald.swim())    # Donald is swimming
print(donald.fly())     # Donald is flying

# ── Method Resolution Order (MRO) ───────────────────────────────────
# Python uses C3 linearization to determine method lookup order
print(Duck.__mro__)
# (Duck, Animal, Swimmer, Flyer, object)

# super() follows MRO — calls next class in chain
class A:
    def greet(self):
        return "A"

class B(A):
    def greet(self):
        return "B" + super().greet()

class C(A):
    def greet(self):
        return "C" + super().greet()

class D(B, C):
    def greet(self):
        return "D" + super().greet()

print(D().greet())  # DCBA
print(D.__mro__)    # D, B, C, A, object

# ── isinstance and issubclass ────────────────────────────────────────
print(isinstance(dog, Dog))         # True
print(isinstance(dog, Animal))      # True
print(isinstance(dog, (Dog, Cat)))  # True (tuple check)

print(issubclass(Dog, Animal))      # True
print(issubclass(Dog, object))      # True

# ── Composition Over Inheritance ────────────────────────────────────
class Engine:
    def start(self):
        return "Engine started"

class Car:
    def __init__(self, engine):
        self.engine = engine  # Composition — has-a relationship

    def start(self):
        return self.engine.start()

car = Car(Engine())
print(car.start())  # Engine started

# ── Abstract Base Class for Interface ───────────────────────────────
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self):
        pass

    @abstractmethod
    def perimeter(self):
        pass

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius

    def area(self):
        import math
        return math.pi * self.radius ** 2

    def perimeter(self):
        import math
        return 2 * math.pi * self.radius
