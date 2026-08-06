"""Magic/dunder methods for customizing class behavior."""

# ── String Representations ───────────────────────────────────────────
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        """Human-readable: print(), str()"""
        return f"({self.x}, {self.y})"

    def __repr__(self):
        """Developer: repr(), debugger, REPL"""
        return f"Point(x={self.x}, y={self.y})"

p = Point(3, 4)
print(str(p))    # (3, 4)
print(repr(p))   # Point(x=3, y=4)

# ── Comparison Methods ───────────────────────────────────────────────
class Money:
    def __init__(self, amount, currency="USD"):
        self.amount = amount
        self.currency = currency

    def __eq__(self, other):   # ==
        return self.amount == other.amount

    def __lt__(self, other):   # <
        return self.amount < other.amount

    def __le__(self, other):   # <=
        return self.amount <= other.amount

    def __hash__(self):        # hash() — required if __eq__ defined
        return hash((self.amount, self.currency))

    def __repr__(self):
        return f"Money({self.amount}, '{self.currency}')"

m1 = Money(100)
m2 = Money(200)
print(m1 < m2)    # True
print(m1 == Money(100))  # True

# Can use @functools.total_ordering to auto-generate all comparison methods
from functools import total_ordering

@total_ordering
class Temperature:
    def __init__(self, celsius):
        self.celsius = celsius

    def __eq__(self, other):
        return self.celsius == other.celsius

    def __lt__(self, other):
        return self.celsius < other.celsius

# ── Arithmetic Methods ───────────────────────────────────────────────
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):       # v1 + v2
        return Vector(self.x + other.x, self.y + other.y)

    def __sub__(self, other):       # v1 - v2
        return Vector(self.x - other.x, self.y - other.y)

    def __mul__(self, scalar):      # v * 3
        return Vector(self.x * scalar, self.y * scalar)

    def __rmul__(self, scalar):     # 3 * v
        return self.__mul__(scalar)

    def __abs__(self):              # abs(v)
        return (self.x ** 2 + self.y ** 2) ** 0.5

    def __bool__(self):             # bool(v)
        return self.x != 0 or self.y != 0

    def __repr__(self):
        return f"Vector({self.x}, {self.y})"

v1 = Vector(1, 2)
v2 = Vector(3, 4)
print(v1 + v2)     # Vector(4, 6)
print(v1 - v2)     # Vector(-2, -2)
print(v1 * 3)      # Vector(3, 6)
print(3 * v1)      # Vector(3, 6)
print(abs(v1))      # 2.236

# ── Container Methods ────────────────────────────────────────────────
class Inventory:
    def __init__(self):
        self._items = {}

    def __len__(self):              # len()
        return len(self._items)

    def __getitem__(self, key):     # inv[key]
        return self._items[key]

    def __setitem__(self, key, val):  # inv[key] = val
        self._items[key] = val

    def __delitem__(self, key):     # del inv[key]
        del self._items[key]

    def __contains__(self, key):    # key in inv
        return key in self._items

    def __iter__(self):             # for key in inv
        return iter(self._items)

inv = Inventory()
inv["apple"] = 5
inv["banana"] = 3
print(len(inv))         # 2
print("apple" in inv)   # True
for item in inv:
    print(item)         # apple, banana

# ── Context Manager ──────────────────────────────────────────────────
class Timer:
    def __enter__(self):
        import time
        self.start = time.time()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        import time
        self.elapsed = time.time() - self.start
        print(f"Elapsed: {self.elapsed:.4f}s")
        return False  # Don't suppress exceptions

with Timer() as t:
    sum(range(1000000))

# ── Callable ─────────────────────────────────────────────────────────
class Adder:
    def __init__(self, n):
        self.n = n

    def __call__(self, x):  # Makes instance callable like a function
        return self.n + x

add5 = Adder(5)
print(add5(3))   # 8
print(callable(add5))  # True
