"""
Module 07 - Functional Programming: Partial Solutions
Difficulty: Beginner to Intermediate
"""

from functools import partial

# =============================================================================
# Exercise 1: Basic partial() - Solution
# =============================================================================
def power(base, exponent):
    return base ** exponent

square = partial(power, exponent=2)
cube = partial(power, exponent=3)

def format_price(currency, amount):
    return f"{currency}{amount:.2f}"

usd_format = partial(format_price, "$")
eur_format = partial(format_price, "€")

print(square(5))      # 25
print(cube(3))        # 27
print(usd_format(19.99))  # "$19.99"
print(eur_format(19.99))  # "€19.99"


# =============================================================================
# Exercise 2: partial with map/filter - Solution
# =============================================================================
def multiply(x, y):
    return x * y

double = partial(multiply, 2)
triple = partial(multiply, 3)

numbers = [1, 2, 3, 4, 5]
doubled = map(double, numbers)
tripled = map(triple, numbers)

print(list(doubled))  # [2, 4, 6, 8, 10]
print(list(tripled))  # [3, 6, 9, 12, 15]


# =============================================================================
# Exercise 3: partial for callbacks - Solution
# =============================================================================
def handle_event(event_type, data):
    print(f"Handling {event_type}: {data}")

handle_click = partial(handle_event, "click")
handle_submit = partial(handle_event, "submit")
handle_keypress = partial(handle_event, "keypress")

handle_click({"x": 100, "y": 200})  # Handling click: {'x': 100, 'y': 200}
handle_submit({"form": "login"})     # Handling submit: {'form': 'login'}
handle_keypress({"key": "enter"})    # Handling keypress: {'key': 'enter'}


# =============================================================================
# Exercise 4: partial with class methods - Solution
# =============================================================================
class Logger:
    def __init__(self, prefix):
        self.prefix = prefix

    def log(self, level, message):
        print(f"[{self.prefix}] {level}: {message}")

logger = Logger("APP")
log_info = partial(logger.log, "INFO")
log_error = partial(logger.log, "ERROR")
log_debug = partial(logger.log, "DEBUG")

log_info("Application started")      # [APP] INFO: Application started
log_error("Something went wrong")    # [APP] ERROR: Something went wrong
log_debug("Debugging information")   # [APP] DEBUG: Debugging information


# =============================================================================
# Exercise 5: partial for data processing - Solution
# =============================================================================
def validate(field, value):
    return value is not None and len(str(value)) > 0

def transform(field, value, transform_func):
    return transform_func(value)

validate_name = partial(validate, "name")
validate_email = partial(validate, "email")

capitalize_name = partial(transform, "name", transform_func=str.capitalize)
lowercase_email = partial(transform, "email", transform_func=str.lower)

print(validate_name("Alice"))    # True
print(validate_name(""))         # False
print(capitalize_name("alice"))  # "Alice"
print(lowercase_email("TEST@EXAMPLE.COM"))  # "test@example.com"
