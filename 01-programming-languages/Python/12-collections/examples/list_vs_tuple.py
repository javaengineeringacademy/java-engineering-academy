"""
Lists vs Tuples in Python
Demonstrates differences between lists and tuples
"""

import sys
import time

# ============================================
# Basic Differences
# ============================================

def basic_differences() -> None:
    """Show basic differences between lists and tuples."""
    print("=== Basic Differences ===")
    
    # Creation
    my_list = [1, 2, 3, 4, 5]
    my_tuple = (1, 2, 3, 4, 5)
    
    print(f"List: {my_list} (type: {type(my_list).__name__})")
    print(f"Tuple: {my_tuple} (type: {type(my_tuple).__name__})")
    
    # Mutability
    print("\nMutability:")
    my_list[0] = 10
    print(f"  List after modification: {my_list}")
    
    try:
        my_tuple[0] = 10
    except TypeError as e:
        print(f"  Tuple modification error: {e}")

# ============================================
# Memory Usage
# ============================================

def memory_comparison() -> None:
    """Compare memory usage."""
    print("\n=== Memory Usage ===")
    
    data_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    data_tuple = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
    print(f"  List size: {sys.getsizeof(data_list)} bytes")
    print(f"  Tuple size: {sys.getsizeof(data_tuple)} bytes")
    print(f"  Tuple is {sys.getsizeof(data_list) - sys.getsizeof(data_tuple)} bytes smaller")

# ============================================
# Performance Comparison
# ============================================

def performance_comparison() -> None:
    """Compare creation and access performance."""
    print("\n=== Performance ===")
    
    n = 1000000
    
    # Creation time
    start = time.time()
    my_list = [i for i in range(n)]
    list_time = time.time() - start
    
    start = time.time()
    my_tuple = tuple(range(n))
    tuple_time = time.time() - start
    
    print(f"  Creation time:")
    print(f"    List: {list_time:.4f}s")
    print(f"    Tuple: {tuple_time:.4f}s")
    
    # Access time
    start = time.time()
    for _ in range(1000000):
        _ = my_list[500000]
    list_access = time.time() - start
    
    start = time.time()
    for _ in range(1000000):
        _ = my_tuple[500000]
    tuple_access = time.time() - start
    
    print(f"  Access time:")
    print(f"    List: {list_access:.4f}s")
    print(f"    Tuple: {tuple_access:.4f}s")

# ============================================
# Use Cases
# ============================================

def use_cases() -> None:
    """Demonstrate when to use each."""
    print("\n=== Use Cases ===")
    
    # Lists: dynamic data
    shopping_cart = ["apple", "banana", "cherry"]
    shopping_cart.append("date")
    shopping_cart.remove("banana")
    print(f"Shopping cart (list): {shopping_cart}")
    
    # Tuples: fixed data
    coordinates = (40.7128, -74.0060)  # NYC
    rgb_color = (255, 128, 0)
    print(f"Coordinates (tuple): {coordinates}")
    print(f"RGB color (tuple): {rgb_color}")
    
    # Tuples as dictionary keys
    locations = {
        (40.7128, -74.0060): "New York",
        (34.0522, -118.2437): "Los Angeles"
    }
    print(f"Locations (tuple keys): {locations}")
    
    # Lists as dictionary values
    students = {
        "Alice": [95, 87, 92],
        "Bob": [88, 76, 90]
    }
    print(f"Students (list values): {students}")

# ============================================
# Tuple Unpacking
# ============================================

def tuple_unpacking() -> None:
    """Demonstrate tuple unpacking."""
    print("\n=== Tuple Unpacking ===")
    
    # Basic unpacking
    point = (3, 4, 5)
    x, y, z = point
    print(f"  x={x}, y={y}, z={z}")
    
    # Swap using tuples
    a, b = 1, 2
    print(f"  Before swap: a={a}, b={b}")
    a, b = b, a
    print(f"  After swap: a={a}, b={b}")
    
    # Return multiple values
    def get_min_max(data):
        return min(data), max(data)
    
    min_val, max_val = get_min_max([5, 2, 8, 1, 9])
    print(f"  Min: {min_val}, Max: {max_val}")
    
    # Star unpacking
    first, *middle, last = (1, 2, 3, 4, 5)
    print(f"  First: {first}, Middle: {middle}, Last: {last}")

# ============================================
# Named Tuples
# ============================================

def named_tuples() -> None:
    """Demonstrate named tuples."""
    from collections import namedtuple
    
    print("\n=== Named Tuples ===")
    
    # Create named tuple
    Point = namedtuple('Point', ['x', 'y', 'z'])
    point = Point(1, 2, 3)
    
    print(f"  Point: {point}")
    print(f"  x: {point.x}, y: {point.y}, z: {point.z}")
    print(f"  As tuple: {tuple(point)}")
    
    # Employee example
    Employee = namedtuple('Employee', ['name', 'department', 'salary'])
    emp = Employee("Alice", "Engineering", 100000)
    print(f"  Employee: {emp}")
    print(f"  Department: {emp.department}")

# ============================================
# List Operations
# ============================================

def list_operations() -> None:
    """Demonstrate list-specific operations."""
    print("\n=== List Operations ===")
    
    # List methods
    fruits = ["apple", "banana"]
    fruits.append("cherry")
    fruits.extend(["date", "elderberry"])
    fruits.insert(1, "avocado")
    
    print(f"  After operations: {fruits}")
    
    # List comprehension
    squares = [x**2 for x in range(10)]
    print(f"  Squares: {squares}")
    
    # Filtering
    evens = [x for x in range(10) if x % 2 == 0]
    print(f"  Evens: {evens}")
    
    # Sorting
    numbers = [3, 1, 4, 1, 5, 9, 2, 6]
    numbers.sort()
    print(f"  Sorted: {numbers}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    basic_differences()
    memory_comparison()
    performance_comparison()
    use_cases()
    tuple_unpacking()
    named_tuples()
    list_operations()
