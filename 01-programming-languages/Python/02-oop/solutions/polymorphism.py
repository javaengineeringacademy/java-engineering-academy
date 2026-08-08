"""
Module 02 - OOP: Polymorphism Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Method Polymorphism - Solution
# =============================================================================
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
        return "Woof!"

    def move(self):
        return "Runs on land"

class Fish(Animal):
    """Fish implementation."""

    def speak(self):
        return "Blub!"

    def move(self):
        return "Swims in water"

class Bird(Animal):
    """Bird implementation."""

    def speak(self):
        return "Tweet!"

    def move(self):
        return "Flies in sky"

animals = [Dog("Rex"), Fish("Nemo"), Bird("Tweety")]
for animal in animals:
    print(f"{animal.name}: {animal.speak()} | {animal.move()}")
# Rex: Woof! | Runs on land
# Nemo: Blub! | Swims in water
# Tweety: Tweet! | Flies in sky


# =============================================================================
# Exercise 2: Duck Typing - Solution
# =============================================================================
class PDFDocument:
    """PDF document class."""

    def __init__(self, content):
        self.content = content

    def render(self):
        return f"Rendering PDF: {self.content}"

class HTMLDocument:
    """HTML document class."""

    def __init__(self, content):
        self.content = content

    def render(self):
        return f"Rendering HTML: {self.content}"

class TextDocument:
    """Plain text document class."""

    def __init__(self, content):
        self.content = content

    def render(self):
        return f"Rendering Text: {self.content}"

def print_document(doc):
    """Print any document that has a render method (duck typing)."""
    print(doc.render())

docs = [
    PDFDocument("PDF content"),
    HTMLDocument("<p>HTML content</p>"),
    TextDocument("Plain text")
]
for doc in docs:
    print_document(doc)


# =============================================================================
# Exercise 3: Operator Overloading - Solution
# =============================================================================
class Vector:
    """A 2D vector class with operator overloading."""

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)

    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y)

    def __mul__(self, scalar):
        return Vector(self.x * scalar, self.y * scalar)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __str__(self):
        return f"Vector({self.x}, {self.y})"

v1 = Vector(1, 2)
v2 = Vector(3, 4)
v3 = v1 + v2
print(v3)          # "Vector(4, 6)"
v4 = v2 - v1
print(v4)          # "Vector(2, 2)"
v5 = v1 * 3
print(v5)          # "Vector(3, 6)"
print(v1 == v2)    # False


# =============================================================================
# Exercise 4: Factory Pattern - Solution
# =============================================================================
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
        return f"Email to {self.recipient}: {self.message}"

class SMSNotification(Notification):
    """SMS notification."""

    def __init__(self, message, phone):
        super().__init__(message)
        self.phone = phone

    def send(self):
        return f"SMS to {self.phone}: {self.message}"

class PushNotification(Notification):
    """Push notification."""

    def __init__(self, message, device_id):
        super().__init__(message)
        self.device_id = device_id

    def send(self):
        return f"Push to {self.device_id}: {self.message}"

def create_notification(notification_type, message, **kwargs):
    """Factory function to create notifications."""
    notifications = {
        "email": EmailNotification,
        "sms": SMSNotification,
        "push": PushNotification
    }
    if notification_type not in notifications:
        raise ValueError(f"Unknown notification type: {notification_type}")
    return notifications[notification_type](message, **kwargs)

email = create_notification("email", "Hello!", recipient="user@example.com")
sms = create_notification("sms", "Hello!", phone="555-1234")
push = create_notification("push", "Hello!", device_id="device123")
print(email.send())  # "Email to user@example.com: Hello!"
print(sms.send())    # "SMS to 555-1234: Hello!"
print(push.send())   # "Push to device123: Hello!"


# =============================================================================
# Exercise 5: Strategy Pattern - Solution
# =============================================================================
class PaymentStrategy:
    """Base payment strategy."""

    def pay(self, amount):
        raise NotImplementedError

class CreditCardPayment(PaymentStrategy):
    """Credit card payment."""

    def __init__(self, card_number):
        self.card_number = card_number

    def pay(self, amount):
        return f"Paid ${amount:.2f} with Credit Card ending in {self.card_number[-4:]}"

class PayPalPayment(PaymentStrategy):
    """PayPal payment."""

    def __init__(self, email):
        self.email = email

    def pay(self, amount):
        return f"Paid ${amount:.2f} via PayPal ({self.email})"

class CryptoPayment(PaymentStrategy):
    """Cryptocurrency payment."""

    def __init__(self, wallet_address):
        self.wallet_address = wallet_address

    def pay(self, amount):
        return f"Paid ${amount:.2f} via Crypto to {self.wallet_address[:10]}..."

class ShoppingCart:
    """Shopping cart with pluggable payment."""

    def __init__(self):
        self.items = []

    def add_item(self, name, price):
        self.items.append({"name": name, "price": price})

    def total(self):
        return sum(item["price"] for item in self.items)

    def checkout(self, payment_strategy):
        return payment_strategy.pay(self.total())

cart = ShoppingCart()
cart.add_item("Laptop", 999.99)
cart.add_item("Mouse", 29.99)
print(f"Total: ${cart.total():.2f}")

credit_card = CreditCardPayment("1234-5678-9012-3456")
paypal = PayPalPayment("user@example.com")
crypto = CryptoPayment("0x1234567890abcdef")

print(cart.checkout(credit_card))
print(cart.checkout(paypal))
print(cart.checkout(crypto))
