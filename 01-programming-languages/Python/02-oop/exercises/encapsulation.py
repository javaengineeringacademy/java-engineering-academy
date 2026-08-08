"""
Module 02 - OOP: Encapsulation Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Private Attributes (Difficulty: Beginner)
# =============================================================================
# Implement proper encapsulation with name mangling.

# TODO: Implement the BankAccount class
class BankAccount:
    """Bank account with private balance and transactions."""

    def __init__(self, owner, initial_balance=0):
        pass

    @property
    def balance(self):
        """Get current balance."""
        pass

    def deposit(self, amount):
        """Deposit money (must be positive)."""
        pass

    def withdraw(self, amount):
        """Withdraw money (must be positive and <= balance)."""
        pass

    def get_transaction_history(self):
        """Return copy of transaction history."""
        pass

# Test cases
# account = BankAccount("Alice", 1000)
# print(account.balance)           # Expected: 1000
# account.deposit(500)
# print(account.balance)           # Expected: 1500
# account.withdraw(200)
# print(account.balance)           # Expected: 1300
# print(account.get_transaction_history())  # Expected: [(deposit, 500), (withdrawal, 200)]
# try:
#     account.withdraw(2000)       # Should raise ValueError
# except ValueError as e:
#     print(e)


# =============================================================================
# Exercise 2: Property Decorators (Difficulty: Intermediate)
# =============================================================================
# Use properties to control access.

# TODO: Implement the Person class
class Person:
    """Person with validated attributes."""

    def __init__(self, name, age, email):
        pass

    @property
    def name(self):
        pass

    @name.setter
    def name(self, value):
        pass

    @property
    def age(self):
        pass

    @age.setter
    def age(self, value):
        pass

    @property
    def email(self):
        pass

    @email.setter
    def email(self, value):
        pass

# Test cases
# person = Person("John", 30, "john@example.com")
# print(person.name)    # Expected: "John"
# person.name = "Jane"
# print(person.name)    # Expected: "Jane"
# try:
#     person.age = -5    # Should raise ValueError
# except ValueError as e:
#     print(e)
# try:
#     person.email = "invalid"  # Should raise ValueError
# except ValueError as e:
#     print(e)


# =============================================================================
# Exercise 3: Encapsulation Patterns (Difficulty: Intermediate)
# =============================================================================
# Implement common encapsulation patterns.

# TODO: Implement the validated class
class ValidatedAttribute:
    """Descriptor for validated attributes."""

    def __init__(self, min_value=None, max_value=None):
        pass

    def __set_name__(self, owner, name):
        pass

    def __get__(self, obj, objtype=None):
        pass

    def __set__(self, obj, value):
        pass

class Temperature:
    """Temperature with validated Celsius value."""

    celsius = ValidatedAttribute(min_value=-273.15)

    def __init__(self, celsius):
        pass

    @property
    def fahrenheit(self):
        pass

# Test cases
# temp = Temperature(25)
# print(temp.celsius)      # Expected: 25
# print(temp.fahrenheit)   # Expected: 77.0
# try:
#     temp.celsius = -300   # Should raise ValueError
# except ValueError as e:
#     print(e)


# =============================================================================
# Exercise 4: Information Hiding (Difficulty: Intermediate)
# =============================================================================
# Properly hide implementation details.

# TODO: Implement the Queue class
class Queue:
    """Queue implementation hiding internal details."""

    def __init__(self, max_size=None):
        pass

    def enqueue(self, item):
        """Add item to queue."""
        pass

    def dequeue(self):
        """Remove and return item from queue."""
        pass

    def peek(self):
        """Return front item without removing."""
        pass

    def is_empty(self):
        """Check if queue is empty."""
        pass

    def size(self):
        """Return queue size."""
        pass

    def __str__(self):
        pass

# Test cases
# q = Queue(3)
# q.enqueue("A")
# q.enqueue("B")
# q.enqueue("C")
# print(q)            # Expected: "Queue: A -> B -> C"
# print(q.dequeue())  # Expected: "A"
# print(q.peek())     # Expected: "B"
# print(q.size())     # Expected: 2
# try:
#     q.enqueue("D")   # Should work (size < 3 after dequeue)
#     q.enqueue("E")   # Should raise QueueFull
# except Exception as e:
#     print(e)


# =============================================================================
# Exercise 5: Context Manager for Encapsulation (Difficulty: Advanced)
# =============================================================================
# Use context managers to enforce encapsulation.

# TODO: Implement the locked resource
class LockedResource:
    """Context manager that locks access to a resource."""

    def __init__(self, resource):
        pass

    def __enter__(self):
        """Acquire lock and return resource."""
        pass

    def __exit__(self, exc_type, exc_val, exc_tb):
        """Release lock."""
        pass

# TODO: Use the locked resource
class SharedCounter:
    """A thread-safe counter."""

    def __init__(self):
        self.count = 0

    def increment(self):
        self.count += 1

    def get_count(self):
        return self.count

# Test cases
# counter = SharedCounter()
# with LockedResource(counter) as c:
#     c.increment()
#     c.increment()
#     c.increment()
# print(counter.get_count())  # Expected: 3
