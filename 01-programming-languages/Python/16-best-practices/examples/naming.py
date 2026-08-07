"""
Naming Conventions in Python
Demonstrates PEP 8 naming conventions
"""

# ============================================
# Module Naming
# ============================================

# Module names: lowercase, underscores
# my_module.py, utility_functions.py

# ============================================
# Class Naming
# ============================================

class MyClass:
    """Class names: CamelCase."""
    pass

class UserProfile:
    """Multi-word class: CamelCase."""
    pass

class HTMLParser:
    """Acronyms: uppercase for short, lowercase for long."""
    pass

# ============================================
# Function Naming
# ============================================

def my_function():
    """Function names: lowercase_with_underscores."""
    pass

def get_user_data():
    """Getters: get_ prefix."""
    pass

def set_user_data():
    """Setters: set_ prefix."""
    pass

def is_valid():
    """Booleans: is_ prefix."""
    pass

def has_permission():
    """Booleans: has_ prefix."""
    pass

# ============================================
# Variable Naming
# ============================================

# Variables: lowercase_with_underscores
user_name = "Alice"
max_retry_count = 3
is_active = True

# Constants: UPPER_CASE
MAX_RETRY_COUNT = 3
DEFAULT_TIMEOUT = 30
API_BASE_URL = "https://api.example.com"

# Private: single underscore prefix
_private_var = "hidden"
_internal_count = 0

# Name mangling: double underscore prefix
__mangled_var = "truly private"

# ============================================
# Parameter Naming
# ============================================

def function_with_params(
    required_param,
    optional_param=None,
    *args,
    **kwargs
):
    """Parameter naming conventions."""
    pass

# ============================================
# Special Names
# ============================================

class SpecialNames:
    """Special method names."""
    
    def __init__(self):
        """Constructor."""
        pass
    
    def __str__(self):
        """String representation."""
        return "SpecialNames"
    
    def __repr__(self):
        """Developer representation."""
        return "SpecialNames()"
    
    def __len__(self):
        """Length."""
        return 0
    
    def __getitem__(self, key):
        """Get item."""
        return None

# ============================================
# Naming Examples
# ============================================

def calculate_total_price(quantity, price_per_unit, tax_rate=0.1):
    """Calculate total price with tax."""
    subtotal = quantity * price_per_unit
    tax = subtotal * tax_rate
    return subtotal + tax

def process_user_data(user_data, validate=True):
    """Process user data."""
    if validate:
        if not user_data.get("email"):
            raise ValueError("Email is required")
    return user_data

def is_even(number):
    """Check if number is even."""
    return number % 2 == 0

def has_even_number(numbers):
    """Check if list has even number."""
    return any(is_even(n) for n in numbers)

# ============================================
# Bad Naming Examples (Don't Do This)
# ============================================

# Bad: Single letter variables (except loops)
# x = 10  # What is x?

# Bad: Unclear names
# def proc(d):  # What does proc do? What is d?

# Good: Clear names
def process_data(data):
    """Clear function name and parameter."""
    return [item for item in data if item is not None]

# Bad: Mixing conventions
# userName = "Alice"  # camelCase
# USER_NAME = "Alice"  # Used for constants

# Good: Consistent convention
user_name = "Alice"  # lowercase_with_underscores

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Usage examples
    total = calculate_total_price(5, 10.0)
    print(f"Total price: ${total}")
    
    user = {"name": "Alice", "email": "alice@example.com"}
    processed = process_user_data(user)
    print(f"Processed user: {processed}")
    
    print(f"Is 4 even? {is_even(4)}")
    print(f"Has even in [1, 3, 5]? {has_even_number([1, 3, 5])}")
    print(f"Has even in [1, 2, 3]? {has_even_number([1, 2, 3])}")
