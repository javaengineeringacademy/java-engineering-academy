"""
Module 02 - OOP: Encapsulation Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Private Attributes - Solution
# =============================================================================
class BankAccount:
    """Bank account with private balance and transactions."""

    def __init__(self, owner, initial_balance=0):
        self.__owner = owner
        self.__balance = initial_balance
        self.__transactions = []

    @property
    def balance(self):
        return self.__balance

    def deposit(self, amount):
        if amount <= 0:
            raise ValueError("Deposit amount must be positive")
        self.__balance += amount
        self.__transactions.append(("deposit", amount))

    def withdraw(self, amount):
        if amount <= 0:
            raise ValueError("Withdrawal amount must be positive")
        if amount > self.__balance:
            raise ValueError("Insufficient funds")
        self.__balance -= amount
        self.__transactions.append(("withdrawal", amount))

    def get_transaction_history(self):
        return self.__transactions.copy()

account = BankAccount("Alice", 1000)
print(account.balance)           # 1000
account.deposit(500)
print(account.balance)           # 1500
account.withdraw(200)
print(account.balance)           # 1300
print(account.get_transaction_history())  # [('deposit', 500), ('withdrawal', 200)]
try:
    account.withdraw(2000)
except ValueError as e:
    print(e)  # Insufficient funds


# =============================================================================
# Exercise 2: Property Decorators - Solution
# =============================================================================
class Person:
    """Person with validated attributes."""

    def __init__(self, name, age, email):
        self._name = name
        self._age = age
        self._email = email

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, value):
        if not value or not isinstance(value, str):
            raise ValueError("Name must be a non-empty string")
        self._name = value

    @property
    def age(self):
        return self._age

    @age.setter
    def age(self, value):
        if not isinstance(value, int) or value < 0:
            raise ValueError("Age must be a non-negative integer")
        self._age = value

    @property
    def email(self):
        return self._email

    @email.setter
    def email(self, value):
        if '@' not in value or '.' not in value:
            raise ValueError("Invalid email format")
        self._email = value

person = Person("John", 30, "john@example.com")
print(person.name)    # "John"
person.name = "Jane"
print(person.name)    # "Jane"
try:
    person.age = -5
except ValueError as e:
    print(e)  # Age must be a non-negative integer
try:
    person.email = "invalid"
except ValueError as e:
    print(e)  # Invalid email format


# =============================================================================
# Exercise 3: Encapsulation Patterns - Solution
# =============================================================================
class ValidatedAttribute:
    """Descriptor for validated attributes."""

    def __init__(self, min_value=None, max_value=None):
        self.min_value = min_value
        self.max_value = max_value

    def __set_name__(self, owner, name):
        self.name = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return getattr(obj, f'_{self.name}', None)

    def __set__(self, obj, value):
        if self.min_value is not None and value < self.min_value:
            raise ValueError(f"{self.name} must be >= {self.min_value}")
        if self.max_value is not None and value > self.max_value:
            raise ValueError(f"{self.name} must be <= {self.max_value}")
        setattr(obj, f'_{self.name}', value)

class Temperature:
    """Temperature with validated Celsius value."""

    celsius = ValidatedAttribute(min_value=-273.15)

    def __init__(self, celsius):
        self.celsius = celsius

    @property
    def fahrenheit(self):
        return self.celsius * 9/5 + 32

temp = Temperature(25)
print(temp.celsius)      # 25
print(temp.fahrenheit)   # 77.0
try:
    temp.celsius = -300
except ValueError as e:
    print(e)  # celsius must be >= -273.15


# =============================================================================
# Exercise 4: Information Hiding - Solution
# =============================================================================
class Queue:
    """Queue implementation hiding internal details."""

    def __init__(self, max_size=None):
        self._items = []
        self._max_size = max_size

    def enqueue(self, item):
        if self._max_size and len(self._items) >= self._max_size:
            raise Exception("Queue is full")
        self._items.append(item)

    def dequeue(self):
        if self.is_empty():
            raise Exception("Queue is empty")
        return self._items.pop(0)

    def peek(self):
        if self.is_empty():
            raise Exception("Queue is empty")
        return self._items[0]

    def is_empty(self):
        return len(self._items) == 0

    def size(self):
        return len(self._items)

    def __str__(self):
        return "Queue: " + " -> ".join(str(item) for item in self._items)

q = Queue(3)
q.enqueue("A")
q.enqueue("B")
q.enqueue("C")
print(q)            # "Queue: A -> B -> C"
print(q.dequeue())  # "A"
print(q.peek())     # "B"
print(q.size())     # 2
q.enqueue("D")
try:
    q.enqueue("E")
except Exception as e:
    print(e)  # Queue is full


# =============================================================================
# Exercise 5: Context Manager for Encapsulation - Solution
# =============================================================================
import threading

class LockedResource:
    """Context manager that locks access to a resource."""

    def __init__(self, resource):
        self.resource = resource
        self.lock = threading.Lock()

    def __enter__(self):
        self.lock.acquire()
        return self.resource

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.lock.release()
        return False

class SharedCounter:
    """A thread-safe counter."""

    def __init__(self):
        self.count = 0

    def increment(self):
        self.count += 1

    def get_count(self):
        return self.count

counter = SharedCounter()
with LockedResource(counter) as c:
    c.increment()
    c.increment()
    c.increment()
print(counter.get_count())  # 3
