"""
Module 02: OOP - Classes Solutions
Practice class implementation in Python.
"""

from abc import ABC, abstractmethod
import math


class BankAccount:
    """A bank account with deposit, withdraw, and transfer functionality."""

    def __init__(self, owner, initial_balance=0):
        self._owner = owner
        self._balance = initial_balance
        self._history = []

    def deposit(self, amount):
        """Deposit money to account."""
        if amount < 0:
            raise ValueError("Deposit amount must be positive")
        self._balance += amount
        self._history.append(f"Deposited ${amount:.2f}")

    def withdraw(self, amount):
        """Withdraw money from account."""
        if amount < 0:
            raise ValueError("Withdrawal amount must be positive")
        if amount > self._balance:
            raise ValueError("Insufficient funds")
        self._balance -= amount
        self._history.append(f"Withdrew ${amount:.2f}")

    def get_balance(self):
        """Return current balance."""
        return self._balance

    def transfer(self, other_account, amount):
        """Transfer money to another account."""
        self.withdraw(amount)
        other_account.deposit(amount)
        self._history.append(f"Transferred ${amount:.2f} to {other_account._owner}")

    def get_history(self):
        """Return list of transactions."""
        return self._history.copy()


class Vector:
    """A 2D vector class with mathematical operations."""

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return f"Vector({self.x}, {self.y})"

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)

    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def magnitude(self):
        return math.sqrt(self.x**2 + self.y**2)

    def dot(self, other):
        return self.x * other.x + self.y * other.y

    def normalize(self):
        mag = self.magnitude()
        if mag == 0:
            return Vector(0, 0)
        return Vector(self.x / mag, self.y / mag)


class Stack:
    """A stack data structure with LIFO behavior."""

    def __init__(self):
        self._items = []

    def push(self, item):
        """Add item to top of stack."""
        self._items.append(item)

    def pop(self):
        """Remove and return top item."""
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self._items.pop()

    def peek(self):
        """Return top item without removing."""
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self._items[-1]

    def is_empty(self):
        """Check if stack is empty."""
        return len(self._items) == 0

    def __len__(self):
        return len(self._items)

    def __contains__(self, item):
        return item in self._items


class Item:
    """Represents an item in the cart."""

    def __init__(self, name, price, quantity=1):
        self.name = name
        self.price = price
        self.quantity = quantity

    def __repr__(self):
        return f"Item({self.name}, ${self.price:.2f}, qty={self.quantity})"


class ShoppingCart:
    """A shopping cart that manages items and calculates totals."""

    def __init__(self, tax_rate=0.08):
        self._items = {}
        self._tax_rate = tax_rate

    def add_item(self, name, price, quantity=1):
        """Add item to cart or increment quantity if exists."""
        if name in self._items:
            self._items[name].quantity += quantity
        else:
            self._items[name] = Item(name, price, quantity)

    def remove_item(self, name):
        """Remove item from cart."""
        if name not in self._items:
            raise KeyError(f"Item '{name}' not found in cart")
        del self._items[name]

    def get_subtotal(self):
        """Calculate sum of all items before tax."""
        return sum(item.price * item.quantity for item in self._items.values())

    def get_tax(self):
        """Calculate tax amount."""
        return self.get_subtotal() * self._tax_rate

    def get_total(self):
        """Calculate total with tax."""
        return self.get_subtotal() + self.get_tax()

    def apply_discount(self, percentage):
        """Apply percentage discount."""
        discount = self.get_subtotal() * (percentage / 100)
        return self.get_total() - discount

    def __len__(self):
        """Return total number of items in cart."""
        return sum(item.quantity for item in self._items.values())


class SingletonMeta(type):
    """A metaclass that creates Singleton classes."""
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            instance = super().__call__(*args, **kwargs)
            cls._instances[cls] = instance
        return cls._instances[cls]


class Database(metaclass=SingletonMeta):
    """Example database class using Singleton pattern."""

    def __init__(self, connection_string=""):
        self.connection_string = connection_string
        self.connected = False

    def connect(self):
        self.connected = True
        return f"Connected to {self.connection_string}"


if __name__ == "__main__":
    print("Testing Classes Solutions...")

    # Test BankAccount
    acc1 = BankAccount("Alice", 1000)
    acc2 = BankAccount("Bob", 500)
    assert acc1.get_balance() == 1000
    acc1.deposit(500)
    assert acc1.get_balance() == 1500
    acc1.withdraw(200)
    assert acc1.get_balance() == 1300
    acc1.transfer(acc2, 300)
    assert acc1.get_balance() == 1000
    assert acc2.get_balance() == 800

    # Test Vector
    v1 = Vector(1, 2)
    v2 = Vector(3, 4)
    assert repr(v1) == "Vector(1, 2)"
    assert (v1 + v2) == Vector(4, 6)
    assert (v2 - v1) == Vector(2, 2)
    assert abs(v1.magnitude() - 2.236) < 0.01
    assert v1.dot(v2) == 11

    # Test Stack
    stack = Stack()
    assert stack.is_empty() == True
    stack.push(1)
    stack.push(2)
    stack.push(3)
    assert len(stack) == 3
    assert stack.peek() == 3
    assert stack.pop() == 3
    assert 2 in stack

    # Test ShoppingCart
    cart = ShoppingCart(tax_rate=0.10)
    cart.add_item("Apple", 1.00, 3)
    cart.add_item("Banana", 0.50, 2)
    assert cart.get_subtotal() == 4.00
    assert cart.get_tax() == 0.40
    assert cart.get_total() == 4.40
    assert len(cart) == 5
    cart.remove_item("Banana")
    assert cart.get_subtotal() == 3.00

    # Test Singleton
    db1 = Database("localhost:5432")
    db2 = Database("different_host")
    assert db1 is db2
    assert db1.connection_string == "localhost:5432"

    print("All Classes solutions passed!")
