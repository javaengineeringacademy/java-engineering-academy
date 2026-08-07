"""
Python OOP - Classes Exercises
Complete each exercise by implementing the required class.
Run the test cases to verify your solution.
"""

from abc import ABC, abstractmethod


# Exercise 1: Bank Account (Easy)
# Create a BankAccount class with proper encapsulation

class BankAccount:
    """
    A bank account with deposit, withdraw, and transfer functionality.
    
    Requirements:
    - Private balance attribute
    - Proper validation for negative amounts
    - Transaction history
    """
    
    def __init__(self, owner, initial_balance=0):
        """Initialize account with owner name and optional balance."""
        # TODO: Implement this method
        pass
    
    def deposit(self, amount):
        """Deposit money to account. Raise ValueError for negative amounts."""
        # TODO: Implement this method
        pass
    
    def withdraw(self, amount):
        """Withdraw money from account. Raise ValueError for insufficient funds."""
        # TODO: Implement this method
        pass
    
    def get_balance(self):
        """Return current balance."""
        # TODO: Implement this method
        pass
    
    def transfer(self, other_account, amount):
        """Transfer money to another account."""
        # TODO: Implement this method
        pass
    
    def get_history(self):
        """Return list of transactions."""
        # TODO: Implement this method
        pass


# Exercise 2: Vector Class (Medium)
# Implement a 2D vector with mathematical operations

class Vector:
    """
    A 2D vector class with mathematical operations.
    
    Requirements:
    - Support addition, subtraction, magnitude
    - Support dot product and normalization
    - Proper string representation
    """
    
    def __init__(self, x, y):
        """Initialize vector with x and y components."""
        # TODO: Implement this method
        pass
    
    def __repr__(self):
        """Return string representation like Vector(1, 2)"""
        # TODO: Implement this method
        pass
    
    def __add__(self, other):
        """Add two vectors."""
        # TODO: Implement this method
        pass
    
    def __sub__(self, other):
        """Subtract two vectors."""
        # TODO: Implement this method
        pass
    
    def __eq__(self, other):
        """Check if two vectors are equal."""
        # TODO: Implement this method
        pass
    
    def magnitude(self):
        """Calculate the magnitude (length) of the vector."""
        # TODO: Implement this method
        pass
    
    def dot(self, other):
        """Calculate dot product with another vector."""
        # TODO: Implement this method
        pass
    
    def normalize(self):
        """Return a normalized (unit) vector."""
        # TODO: Implement this method
        pass


# Exercise 3: Stack Implementation (Medium)
# Create a Stack class with LIFO behavior

class Stack:
    """
    A stack data structure with LIFO (Last In, First Out) behavior.
    
    Requirements:
    - push, pop, peek operations
    - Proper exception handling for empty stack
    - Support len() and bool()
    """
    
    def __init__(self):
        """Initialize an empty stack."""
        # TODO: Implement this method
        pass
    
    def push(self, item):
        """Add item to top of stack."""
        # TODO: Implement this method
        pass
    
    def pop(self):
        """Remove and return top item. Raise IndexError if empty."""
        # TODO: Implement this method
        pass
    
    def peek(self):
        """Return top item without removing. Raise IndexError if empty."""
        # TODO: Implement this method
        pass
    
    def is_empty(self):
        """Check if stack is empty."""
        # TODO: Implement this method
        pass
    
    def __len__(self):
        """Return number of items in stack."""
        # TODO: Implement this method
        pass
    
    def __contains__(self, item):
        """Check if item is in stack."""
        # TODO: Implement this method
        pass


# Exercise 4: Shopping Cart (Medium)
# Build a ShoppingCart class with item management

class Item:
    """Represents an item in the cart."""
    
    def __init__(self, name, price, quantity=1):
        # TODO: Implement this method
        pass
    
    def __repr__(self):
        # TODO: Implement this method
        pass


class ShoppingCart:
    """
    A shopping cart that manages items and calculates totals.
    
    Requirements:
    - Add/remove items
    - Calculate subtotal, tax, and total
    - Apply discounts
    """
    
    def __init__(self, tax_rate=0.08):
        """Initialize empty cart with tax rate."""
        # TODO: Implement this method
        pass
    
    def add_item(self, name, price, quantity=1):
        """Add item to cart or increment quantity if exists."""
        # TODO: Implement this method
        pass
    
    def remove_item(self, name):
        """Remove item from cart. Raise KeyError if not found."""
        # TODO: Implement this method
        pass
    
    def get_subtotal(self):
        """Calculate sum of all items before tax."""
        # TODO: Implement this method
        pass
    
    def get_tax(self):
        """Calculate tax amount."""
        # TODO: Implement this method
        pass
    
    def get_total(self):
        """Calculate total with tax."""
        # TODO: Implement this method
        pass
    
    def apply_discount(self, percentage):
        """Apply percentage discount (e.g., 10 for 10%)."""
        # TODO: Implement this method
        pass
    
    def __len__(self):
        """Return total number of items in cart."""
        # TODO: Implement this method
        pass


# Exercise 5: Singleton Pattern (Hard)
# Implement the Singleton design pattern

class SingletonMeta(type):
    """
    A metaclass that creates Singleton classes.
    
    Usage:
        class Database(metaclass=SingletonMeta):
            pass
    """
    _instances = {}
    
    def __call__(cls, *args, **kwargs):
        # TODO: Implement singleton behavior
        pass


class Database(metaclass=SingletonMeta):
    """Example database class using Singleton pattern."""
    
    def __init__(self, connection_string=""):
        self.connection_string = connection_string
        self.connected = False
    
    def connect(self):
        self.connected = True
        return f"Connected to {self.connection_string}"


# ==================== TEST CASES ====================

def test_exercises():
    print("Testing Exercise 1: Bank Account")
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
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 2: Vector Class")
    v1 = Vector(1, 2)
    v2 = Vector(3, 4)
    assert repr(v1) == "Vector(1, 2)"
    assert (v1 + v2) == Vector(4, 6)
    assert (v2 - v1) == Vector(2, 2)
    assert abs(v1.magnitude() - 2.236) < 0.01
    assert v1.dot(v2) == 11
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 3: Stack")
    stack = Stack()
    assert stack.is_empty() == True
    stack.push(1)
    stack.push(2)
    stack.push(3)
    assert len(stack) == 3
    assert stack.peek() == 3
    assert stack.pop() == 3
    assert 2 in stack
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 4: Shopping Cart")
    cart = ShoppingCart(tax_rate=0.10)
    cart.add_item("Apple", 1.00, 3)
    cart.add_item("Banana", 0.50, 2)
    assert cart.get_subtotal() == 4.00
    assert cart.get_tax() == 0.40
    assert cart.get_total() == 4.40
    assert len(cart) == 5
    cart.remove_item("Banana")
    assert cart.get_subtotal() == 3.00
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 5: Singleton")
    db1 = Database("localhost:5432")
    db2 = Database("different_host")
    assert db1 is db2
    assert db1.connection_string == "localhost:5432"
    print("  ✓ All tests passed!\n")

    print("All OOP class exercises passed!")


if __name__ == "__main__":
    test_exercises()
