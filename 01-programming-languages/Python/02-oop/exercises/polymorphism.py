"""
Module 02 - OOP: Polymorphism Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Method Polymorphism (Difficulty: Intermediate)
# =============================================================================
# Implement polymorphism through method overriding.

# TODO: Implement the animal classes
class Animal:
    """Base animal class."""

    def __init__(self, name):
        self.name = name

    def speak(self):
        raise NotImplementedError("Subclass must implement this")

    def move(self):
        raise NotImplementedError("Subclass must implement this")

class Dog(Animal):
    """Dog implementation."""

    def speak(self):
        pass

    def move(self):
        pass

class Fish(Animal):
    """Fish implementation."""

    def speak(self):
        pass

    def move(self):
        pass

class Bird(Animal):
    """Bird implementation."""

    def speak(self):
        pass

    def move(self):
        pass

# Test cases
# animals = [Dog("Rex"), Fish("Nemo"), Bird("Tweety")]
# for animal in animals:
#     print(f"{animal.name}: {animal.speak()} | {animal.move()}")
# # Expected:
# # Rex: Woof! | Runs on land
# # Nemo: Blub! | Swims in water
# # Tweety: Tweet! | Flies in sky


# =============================================================================
# Exercise 2: Duck Typing (Difficulty: Intermediate)
# =============================================================================
# Use duck typing for polymorphism.

# TODO: Implement the printable classes
class PDFDocument:
    """PDF document class."""

    def __init__(self, content):
        self.content = content

    def render(self):
        pass

class HTMLDocument:
    """HTML document class."""

    def __init__(self, content):
        self.content = content

    def render(self):
        pass

class TextDocument:
    """Plain text document class."""

    def __init__(self, content):
        self.content = content

    def render(self):
        pass

# TODO: Function that works with any printable object
def print_document(doc):
    """Print any document that has a render method."""
    pass

# Test cases
# docs = [
#     PDFDocument("PDF content"),
#     HTMLDocument("<p>HTML content</p>"),
#     TextDocument("Plain text")
# ]
# for doc in docs:
#     print_document(doc)
# # Expected: Renders each document type appropriately


# =============================================================================
# Exercise 3: Operator Overloading (Difficulty: Intermediate)
# =============================================================================
# Overload operators for custom classes.

# TODO: Implement the Vector class
class Vector:
    """A 2D vector class with operator overloading."""

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        """Add two vectors."""
        pass

    def __sub__(self, other):
        """Subtract two vectors."""
        pass

    def __mul__(self, scalar):
        """Multiply vector by scalar."""
        pass

    def __eq__(self, other):
        """Check equality."""
        pass

    def __str__(self):
        pass

# Test cases
# v1 = Vector(1, 2)
# v2 = Vector(3, 4)
# v3 = v1 + v2
# print(v3)          # Expected: "Vector(4, 6)"
# v4 = v2 - v1
# print(v4)          # Expected: "Vector(2, 2)"
# v5 = v1 * 3
# print(v5)          # Expected: "Vector(3, 6)"
# print(v1 == v2)    # Expected: False


# =============================================================================
# Exercise 4: Factory Pattern (Difficulty: Intermediate)
# =============================================================================
# Use polymorphism with factory pattern.

# TODO: Implement the factory
class Notification:
    """Base notification class."""

    def __init__(self, message):
        self.message = message

    def send(self):
        raise NotImplementedError

class EmailNotification(Notification):
    """Email notification."""

    def __init__(self, message, recipient):
        super().__init__(message)
        self.recipient = recipient

    def send(self):
        pass

class SMSNotification(Notification):
    """SMS notification."""

    def __init__(self, message, phone):
        super().__init__(message)
        self.phone = phone

    def send(self):
        pass

class PushNotification(Notification):
    """Push notification."""

    def __init__(self, message, device_id):
        super().__init__(message)
        self.device_id = device_id

    def send(self):
        pass

# TODO: Create notification factory
def create_notification(notification_type, message, **kwargs):
    """Factory function to create notifications."""
    pass

# Test cases
# email = create_notification("email", "Hello!", recipient="user@example.com")
# sms = create_notification("sms", "Hello!", phone="555-1234")
# push = create_notification("push", "Hello!", device_id="device123")
# print(email.send())  # Expected: "Email to user@example.com: Hello!"
# print(sms.send())    # Expected: "SMS to 555-1234: Hello!"
# print(push.send())   # Expected: "Push to device123: Hello!"


# =============================================================================
# Exercise 5: Strategy Pattern (Difficulty: Advanced)
# =============================================================================
# Use polymorphism for strategy pattern.

# TODO: Implement the payment strategies
class PaymentStrategy:
    """Base payment strategy."""

    def pay(self, amount):
        raise NotImplementedError

class CreditCardPayment(PaymentStrategy):
    """Credit card payment."""

    def __init__(self, card_number):
        self.card_number = card_number

    def pay(self, amount):
        pass

class PayPalPayment(PaymentStrategy):
    """PayPal payment."""

    def __init__(self, email):
        self.email = email

    def pay(self, amount):
        pass

class CryptoPayment(PaymentStrategy):
    """Cryptocurrency payment."""

    def __init__(self, wallet_address):
        self.wallet_address = wallet_address

    def pay(self, amount):
        pass

# TODO: Shopping cart that uses payment strategies
class ShoppingCart:
    """Shopping cart with pluggable payment."""

    def __init__(self):
        self.items = []

    def add_item(self, name, price):
        pass

    def total(self):
        pass

    def checkout(self, payment_strategy):
        pass

# Test cases
# cart = ShoppingCart()
# cart.add_item("Laptop", 999.99)
# cart.add_item("Mouse", 29.99)
# print(f"Total: ${cart.total():.2f}")
#
# credit_card = CreditCardPayment("1234-5678-9012-3456")
# paypal = PayPalPayment("user@example.com")
# crypto = CryptoPayment("0x1234...")
#
# print(cart.checkout(credit_card))
# print(cart.checkout(paypal))
# print(cart.checkout(crypto))
