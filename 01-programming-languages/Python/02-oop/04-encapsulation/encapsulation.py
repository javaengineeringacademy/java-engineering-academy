"""Encapsulation, private attributes, and @property."""

# ── Name Mangling ────────────────────────────────────────────────────
# Python doesn't have true private — uses conventions
# _prefix: "internal use" (convention only)
# __prefix: name mangling (prevents accidental override)

class BankAccount:
    def __init__(self, owner, balance=0):
        self.owner = owner          # Public
        self._balance = balance     # Protected (convention)
        self.__pin = "1234"         # Private (name mangled)

    def deposit(self, amount):
        if amount <= 0:
            raise ValueError("Amount must be positive")
        self._balance += amount

    def withdraw(self, amount):
        if amount > self._balance:
            raise ValueError("Insufficient funds")
        self._balance -= amount

    def get_balance(self):
        return self._balance

    def check_pin(self, pin):
        return pin == self.__pin

account = BankAccount("Alice", 1000)
print(account.owner)          # OK — public
print(account._balance)       # Works but discouraged
# print(account.__pin)        # AttributeError
print(account._BankAccount__pin)  # Works via name mangling

account.deposit(500)
print(account.get_balance())  # 1500

# ── Property Decorator ──────────────────────────────────────────────
class Temperature:
    def __init__(self, celsius=0):
        self._celsius = celsius

    @property
    def celsius(self):
        return self._celsius

    @celsius.setter
    def celsius(self, value):
        if value < -273.15:
            raise ValueError("Temperature below absolute zero")
        self._celsius = value

    @property
    def fahrenheit(self):
        return self._celsius * 9/5 + 32

    @fahrenheit.setter
    def fahrenheit(self, value):
        self.celsius = (value - 32) * 5/9

temp = Temperature(100)
print(temp.fahrenheit)      # 212.0
temp.fahrenheit = 32
print(temp.celsius)         # 0.0

# ── Slots (Memory Optimization) ─────────────────────────────────────
class Point:
    __slots__ = ('x', 'y')  # Prevents __dict__, saves memory

    def __init__(self, x, y):
        self.x = x
        self.y = y

p = Point(1, 2)
# p.z = 3  # AttributeError — no new attributes allowed

# ── Encapsulation Patterns ──────────────────────────────────────────
class Config:
    """Read-only properties with validation."""

    def __init__(self, **kwargs):
        self._data = kwargs

    def __getattr__(self, name):
        try:
            return self._data[name]
        except KeyError:
            raise AttributeError(f"Config has no attribute '{name}'")

    def __setattr__(self, name, value):
        if name.startswith('_'):
            super().__setattr__(name, value)
        else:
            raise AttributeError("Config is read-only")

config = Config(debug=True, db="postgres://...")
print(config.debug)     # True
# config.debug = False  # AttributeError — read-only

# ── Descriptor Protocol ─────────────────────────────────────────────
class Validated:
    def __init__(self, min_val=None, max_val=None):
        self.min_val = min_val
        self.max_val = max_val

    def __set_name__(self, owner, name):
        self.name = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)

    def __set__(self, obj, value):
        if self.min_val is not None and value < self.min_val:
            raise ValueError(f"{self.name} must be >= {self.min_val}")
        if self.max_val is not None and value > self.max_val:
            raise ValueError(f"{self.name} must be <= {self.max_val}")
        obj.__dict__[self.name] = value

class Player:
    health = Validated(min_val=0, max_val=100)
    level = Validated(min_val=1, max_val=100)

    def __init__(self, health, level):
        self.health = health
        self.level = level

player = Player(50, 10)
print(player.health)  # 50
# player.health = -5   # ValueError
