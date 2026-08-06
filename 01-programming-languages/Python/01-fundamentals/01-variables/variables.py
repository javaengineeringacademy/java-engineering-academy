"""Variables, types, and type conversion in Python."""

# ── Variable Assignment ──────────────────────────────────────────────
# Python is dynamically typed: no type declaration needed.
name = "Alice"           # str
age = 30                 # int
height = 5.7             # float
is_active = True         # bool
nothing = None           # NoneType

# Multiple assignment
x, y, z = 1, 2, 3
a = b = c = 0            # same value to all

# Unpacking
coordinates = (10, 20, 30)
cx, cy, cz = coordinates

# ── Built-in Types ───────────────────────────────────────────────────
# int, float, complex, bool, str, bytes, list, tuple, dict, set, frozenset

print(f"int: {type(age)}")
print(f"float: {type(height)}")
print(f"str: {type(name)}")
print(f"bool: {type(is_active)}")
print(f"list: {type([1, 2, 3])}")
print(f"dict: {type({'key': 'value'})}")

# ── Type Conversion ──────────────────────────────────────────────────
num_str = "42"
num_int = int(num_str)        # str → int
num_float = float(num_str)    # str → float
back_to_str = str(num_int)    # int → str

# Bool conversions
bool(0)        # False
bool("")       # False
bool([])       # False
bool(None)     # False
bool(1)        # True
bool("hello")  # True

# ── Type Checking ────────────────────────────────────────────────────
value = 42
print(isinstance(value, int))        # True — preferred way
print(type(value) == int)            # True — less flexible

# ── Variable Naming Conventions ──────────────────────────────────────
user_name = "alice"        # snake_case (PEP 8)
MAX_SIZE = 100             # UPPER_SNAKE for constants
_private = "underscore"    # convention for "internal" use
__mangled = "double"       # name mangling in classes
__dunder__ = "special"     # reserved for Python magic methods

# ── Scope and LEGB Rule ─────────────────────────────────────────────
global_var = "global"

def demonstrate_scope():
    enclosing_var = "enclosing"

    def inner():
        local_var = "local"
        print(global_var)      # Global
        print(enclosing_var)   # Enclosing
        print(local_var)       # Local

    inner()

demonstrate_scope()

# ── Key Concepts ─────────────────────────────────────────────────────
# - Variables are references (labels), not boxes holding values.
# - Assignment never copies the object; it creates a new reference.
# - Immutable types: int, float, str, tuple, frozenset, bytes.
# - Mutable types: list, dict, set, bytearray.
# - `id(obj)` returns the memory address; `is` compares identity.
