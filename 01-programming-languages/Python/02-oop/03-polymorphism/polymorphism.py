"""Duck typing, method overriding, and polymorphism."""

# ── Duck Typing ──────────────────────────────────────────────────────
# "If it walks like a duck and quacks like a duck, it's a duck"
# Python doesn't check types — it checks behavior

class Duck:
    def quack(self):
        return "Quack!"

    def walk(self):
        return "Waddle waddle"

class Person:
    def quack(self):
        return "I'm quacking like a duck!"

    def walk(self):
        return "Walking normally"

class Dog:
    def speak(self):
        return "Woof!"

# Any object with quack() and walk() works — no shared base class needed
def duck_test(thing):
    print(thing.quack())
    print(thing.walk())

duck_test(Duck())    # Works
duck_test(Person())  # Works
# duck_test(Dog())   # AttributeError — no quack()

# ── Method Overriding ────────────────────────────────────────────────
class Shape:
    def area(self):
        raise NotImplementedError("Subclasses must implement area()")

    def describe(self):
        return f"Shape with area {self.area()}"

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius

    def area(self):
        import math
        return math.pi * self.radius ** 2

class Rectangle(Shape):
    def __init__(self, width, height):
        self.width = width
        self.height = height

    def area(self):
        return self.width * self.height

# Polymorphic function
def print_area(shape):
    print(f"Area: {shape.area()}")

print_area(Circle(5))        # Area: 78.54
print_area(Rectangle(4, 6))  # Area: 24

# ── Polymorphism with Built-in Functions ─────────────────────────────
# len() works on any object with __len__
print(len([1, 2, 3]))          # 3
print(len("hello"))            # 5
print(len({"a": 1, "b": 2}))  # 2

# ── Operator Overloading ─────────────────────────────────────────────
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)

    def __mul__(self, scalar):
        return Vector(self.x * scalar, self.y * scalar)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __repr__(self):
        return f"Vector({self.x}, {self.y})"

v1 = Vector(1, 2)
v2 = Vector(3, 4)
print(v1 + v2)     # Vector(4, 6)
print(v1 * 3)      # Vector(3, 6)
print(v1 == v2)    # False

# ── Polymorphic Collections ──────────────────────────────────────────
class Notification:
    def send(self, message):
        raise NotImplementedError

class Email(Notification):
    def send(self, message):
        return f"Email: {message}"

class SMS(Notification):
    def send(self, message):
        return f"SMS: {message}"

class Push(Notification):
    def send(self, message):
        return f"Push: {message}"

# Process any notification type uniformly
def broadcast(notifications, message):
    results = []
    for n in notifications:
        results.append(n.send(message))
    return results

notifiers = [Email(), SMS(), Push()]
print(broadcast(notifiers, "Alert!"))
# ['Email: Alert!', 'SMS: Alert!', 'Push: Alert!']
