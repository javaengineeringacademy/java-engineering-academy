"""
Partial Functions in Python
Demonstrates functools.partial for function specialization
"""

from functools import partial
from typing import List, Callable, Any

# ============================================
# Basic Partial Application
# ============================================

def power(base: float, exponent: float) -> float:
    """Calculate base raised to exponent."""
    return base ** exponent

square = partial(power, exponent=2)
cube = partial(power, exponent=3)

# ============================================
# Partial with Multiple Arguments
# ============================================

def create_message(prefix: str, name: str, message: str) -> str:
    """Create a formatted message."""
    return f"{prefix} {name}: {message}"

hello_msg = partial(create_message, "Hello!")
goodbye_msg = partial(create_message, "Goodbye!")

# ============================================
# Partial for Callbacks
# ============================================

def log_event(source: str, level: str, message: str) -> None:
    """Log an event with source and level."""
    print(f"[{level}] {source}: {message}")

app_log = partial(log_event, "APP")
db_log = partial(log_event, "DATABASE")
error_handler = partial(log_event, "ERROR", "ERROR")

# ============================================
# Partial with Itertools
# ============================================

from functools import reduce

def multiply(x: int, y: int) -> int:
    """Multiply two numbers."""
    return x * y

double = partial(multiply, 2)
triple = partial(multiply, 3)

# Use with map
numbers = [1, 2, 3, 4, 5]
doubled = list(map(double, numbers))
tripled = list(map(triple, numbers))

# ============================================
# Partial for Configuration
# ============================================

def make_request(method: str, url: str, headers: dict = None) -> dict:
    """Simulate an HTTP request."""
    return {
        "method": method,
        "url": url,
        "headers": headers or {}
    }

get_request = partial(make_request, "GET")
post_request = partial(make_request, "POST")
api_request = partial(make_request, headers={"Authorization": "Bearer token123"})

# ============================================
# Partial for Sorting
# ============================================

def compare_by_key(key: str, item1: dict, item2: dict) -> int:
    """Compare two dictionaries by a specific key."""
    if item1[key] < item2[key]:
        return -1
    elif item1[key] > item2[key]:
        return 1
    return 0

users = [
    {"name": "Alice", "age": 30},
    {"name": "Bob", "age": 25},
    {"name": "Charlie", "age": 35}
]

sort_by_name = partial(compare_by_key, "name")
sort_by_age = partial(compare_by_key, "age")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Partial ===")
    print(f"square(5) = {square(5)}")      # 25
    print(f"cube(5) = {cube(5)}")          # 125
    print(f"square(2.5) = {square(2.5)}")  # 6.25
    
    print("\n=== Partial with Multiple Arguments ===")
    print(hello_msg("Alice"))       # Hello! Alice: 
    print(goodbye_msg("Bob"))       # Goodbye! Bob: 
    print(hello_msg("Charlie", "Welcome!"))  # Hello! Charlie: Welcome!
    
    print("\n=== Partial for Callbacks ===")
    app_log("INFO", "Application started")
    db_log("DEBUG", "Query executed")
    error_handler("Connection failed")
    
    print("\n=== Partial with Itertools ===")
    print(f"Numbers: {numbers}")     # [1, 2, 3, 4, 5]
    print(f"Doubled: {doubled}")     # [2, 4, 6, 8, 10]
    print(f"Tripled: {tripled}")     # [3, 6, 9, 12, 15]
    
    print("\n=== Partial for Configuration ===")
    print(get_request("https://api.example.com"))
    print(post_request("https://api.example.com", {"data": "test"}))
    print(api_request("GET", "https://api.example.com/users"))
    
    print("\n=== Partial for Sorting ===")
    sorted_by_name = sorted(users, key=partial(lambda x: x["name"]))
    sorted_by_age = sorted(users, key=partial(lambda x: x["age"]))
    print(f"By name: {[u['name'] for u in sorted_by_name]}")
    print(f"By age: {[u['age'] for u in sorted_by_age]}")
