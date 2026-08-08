"""
Module 07 - Functional Programming: Partial Exercises
Difficulty: Beginner to Intermediate
"""

from functools import partial

# =============================================================================
# Exercise 1: Basic partial() (Difficulty: Beginner)
# =============================================================================
# Use partial() to create specialized functions.

# TODO: Create partial functions
def power(base, exponent):
    return base ** exponent

# Create square and cube functions using partial
square = None
cube = None

# TODO: Create partial for formatting
def format_price(currency, amount):
    return f"{currency}{amount:.2f}"

usd_format = None
eur_format = None

# Test cases
# print(square(5))      # Expected: 25
# print(cube(3))        # Expected: 27
# print(usd_format(19.99))  # Expected: "$19.99"
# print(eur_format(19.99))  # Expected: "€19.99"


# =============================================================================
# Exercise 2: partial with map/filter (Difficulty: Beginner)
# =============================================================================
# Use partial with map and filter.

# TODO: Use partial with map
def multiply(x, y):
    return x * y

double = partial(multiply, 2)
triple = partial(multiply, 3)

numbers = [1, 2, 3, 4, 5]
doubled = None
tripled = None

# Test cases
# print(list(doubled))  # Expected: [2, 4, 6, 8, 10]
# print(list(tripled))  # Expected: [3, 6, 9, 12, 15]


# =============================================================================
# Exercise 3: partial for callbacks (Difficulty: Intermediate)
# =============================================================================
# Use partial for callback functions.

# TODO: Create event handlers
def handle_event(event_type, data):
    print(f"Handling {event_type}: {data}")

# Create specific handlers using partial
handle_click = None
handle_submit = None
handle_keypress = None

# Test cases
# handle_click({"x": 100, "y": 200})
# handle_submit({"form": "login"})
# handle_keypress({"key": "enter"})


# =============================================================================
# Exercise 4: partial with class methods (Difficulty: Intermediate)
# =============================================================================
# Use partial to create specialized class methods.

# TODO: Implement Logger class
class Logger:
    def __init__(self, prefix):
        self.prefix = prefix

    def log(self, level, message):
        print(f"[{self.prefix}] {level}: {message}")

# Create specialized loggers
logger = Logger("APP")
log_info = None
log_error = None
log_debug = None

# Test cases
# log_info("Application started")
# log_error("Something went wrong")
# log_debug("Debugging information")


# =============================================================================
# Exercise 5: partial for data processing (Difficulty: Intermediate)
# =============================================================================
# Use partial for data processing pipelines.

# TODO: Create processing functions
def validate(field, value):
    return value is not None and len(str(value)) > 0

def transform(field, value, transform_func):
    return transform_func(value)

# Create field-specific validators
validate_name = None
validate_email = None

# Create field-specific transformers
capitalize_name = None
lowercase_email = None

# Test cases
# print(validate_name("Alice"))    # Expected: True
# print(validate_name(""))         # Expected: False
# print(capitalize_name("alice"))  # Expected: "Alice"
# print(lowercase_email("TEST@EXAMPLE.COM"))  # Expected: "test@example.com"
