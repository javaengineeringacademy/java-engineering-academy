"""
Module 06 - Type Hints: Basic Types Solutions
Difficulty: Beginner to Intermediate
"""

from typing import List, Dict, Tuple, Optional, Union, Any, Set

# =============================================================================
# Exercise 1: Function Type Hints - Solution
# =============================================================================
def greet(name: str) -> str:
    """Return a greeting message."""
    return f"Hello, {name}!"

def add_numbers(a: int, b: int) -> int:
    """Add two numbers and return result."""
    return a + b

def get_user_info(user_id: int) -> Optional[Dict[str, str]]:
    """Get user information by ID."""
    users = {1: {"name": "Alice", "email": "alice@example.com"},
             2: {"name": "Bob", "email": "bob@example.com"}}
    return users.get(user_id)

print(greet("Alice"))           # "Hello, Alice!"
print(add_numbers(5, 3))        # 8
print(get_user_info(1))         # {'name': 'Alice', 'email': 'alice@example.com'}


# =============================================================================
# Exercise 2: Collection Types - Solution
# =============================================================================
def process_items(items: List[int]) -> List[int]:
    """Process a list of items and return filtered results."""
    return [item for item in items if item > 0]

def merge_dicts(dict1: Dict[str, Any], dict2: Dict[str, Any]) -> Dict[str, Any]:
    """Merge two dictionaries."""
    return {**dict1, **dict2}

def unique_elements(lst: List[Any]) -> List[Any]:
    """Return unique elements from list."""
    return list(set(lst))

print(process_items([-1, 2, -3, 4]))  # [2, 4]
print(merge_dicts({"a": 1}, {"b": 2}))  # {"a": 1, "b": 2}
print(unique_elements([1, 2, 2, 3, 3]))  # [1, 2, 3]


# =============================================================================
# Exercise 3: Optional and Union Types - Solution
# =============================================================================
def find_user(user_id: int) -> Optional[str]:
    """Find user by ID, return None if not found."""
    users = {1: "Alice", 2: "Bob"}
    return users.get(user_id)

def process_value(value: Union[int, str]) -> Union[int, str]:
    """Process value that can be int or string."""
    if isinstance(value, int):
        return value * 2
    return value.upper()

def safe_divide(a: float, b: float) -> Optional[float]:
    """Divide a by b, return None if b is zero."""
    if b == 0:
        return None
    return a / b

print(find_user(1))      # "Alice"
print(find_user(999))    # None
print(process_value(5))  # 10
print(process_value("hello"))  # "HELLO"
print(safe_divide(10, 2))  # 5.0
print(safe_divide(10, 0))  # None


# =============================================================================
# Exercise 4: Return Type Hints - Solution
# =============================================================================
def get_statistics(numbers: List[float]) -> Dict[str, Union[float, int]]:
    """Calculate statistics for a list of numbers."""
    return {
        "mean": sum(numbers) / len(numbers),
        "min": min(numbers),
        "max": max(numbers),
        "count": len(numbers)
    }

def split_and_join(text: str, delimiter: str = " ") -> str:
    """Split text and join with different delimiter."""
    return delimiter.join(text.split())

def create_matrix(rows: int, cols: int, fill_value: Any = 0) -> List[List[Any]]:
    """Create a matrix filled with fill_value."""
    return [[fill_value for _ in range(cols)] for _ in range(rows)]

stats = get_statistics([1, 2, 3, 4, 5])
print(stats)  # {'mean': 3.0, 'min': 1, 'max': 5, 'count': 5}
print(split_and_join("hello world", "-"))  # "hello-world"
print(create_matrix(2, 3, 1))  # [[1, 1, 1], [1, 1, 1]]


# =============================================================================
# Exercise 5: Type Aliases - Solution
# =============================================================================
# Create type aliases
UserID = int
UserDict = Dict[str, Any]
Matrix = List[List[float]]

def get_user(user_id: UserID) -> Optional[UserDict]:
    """Get user by ID."""
    users = {
        1: {"name": "Alice", "email": "alice@example.com"},
        2: {"name": "Bob", "email": "bob@example.com"}
    }
    return users.get(user_id)

def process_matrix(matrix: Matrix) -> float:
    """Process a matrix and return sum of all elements."""
    return sum(sum(row) for row in matrix)

user = get_user(1)
print(user)  # {'name': 'Alice', 'email': 'alice@example.com'}
matrix = [[1, 2, 3], [4, 5, 6]]
print(process_matrix(matrix))  # 21
