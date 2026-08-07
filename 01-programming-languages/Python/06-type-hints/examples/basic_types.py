"""
Basic Type Hints in Python
Demonstrates fundamental type annotation features
"""

from typing import List, Dict, Tuple, Optional, Union, Any

# ============================================
# Simple Type Annotations
# ============================================

def greet(name: str) -> str:
    """Function with parameter and return type hints."""
    return f"Hello, {name}!"

def add(a: int, b: int) -> int:
    """Integer addition with type hints."""
    return a + b

def is_active(active: bool = True) -> bool:
    """Boolean parameter with default value."""
    return active

# ============================================
# Complex Types
# ============================================

def process_items(items: List[str]) -> List[str]:
    """List type hint for both parameter and return."""
    return [item.upper() for item in items]

def get_user_info(user_id: int) -> Dict[str, Any]:
    """Dictionary type hint with Any for flexible values."""
    return {
        "id": user_id,
        "name": "Alice",
        "active": True
    }

def get_coordinates() -> Tuple[float, float, float]:
    """Tuple type hint with specific types."""
    return (10.5, 20.3, 30.1)

def find_item(items: List[str], target: str) -> Optional[str]:
    """Optional type - can return None or str."""
    for item in items:
        if item == target:
            return item
    return None

# ============================================
# Union Types
# ============================================

def parse_value(value: Union[int, str]) -> int:
    """Union type - accepts multiple types."""
    if isinstance(value, str):
        return int(value)
    return value

# ============================================
# Variable Annotations
# ============================================

# Python 3.6+ variable annotations
name: str = "Python"
version: float = 3.11
is_awesome: bool = True
numbers: List[int] = [1, 2, 3, 4, 5]
metadata: Dict[str, str] = {"key": "value"}

# ============================================
# Default Values and Type Hints
# ============================================

def create_profile(
    name: str,
    age: int,
    email: Optional[str] = None,
    active: bool = True
) -> Dict[str, Any]:
    """Function with mixed type hints and defaults."""
    profile = {
        "name": name,
        "age": age,
        "active": active
    }
    if email:
        profile["email"] = email
    return profile

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Simple types
    print(greet("World"))  # Hello, World!
    print(add(5, 3))       # 8
    print(is_active())     # True

    # Complex types
    items = ["hello", "world", "python"]
    print(process_items(items))  # ['HELLO', 'WORLD', 'PYTHON']

    user = get_user_info(1)
    print(user)  # {'id': 1, 'name': 'Alice', 'active': True}

    coords = get_coordinates()
    print(coords)  # (10.5, 20.3, 30.1)

    result = find_item(items, "python")
    print(result)  # python

    # Union types
    print(parse_value("42"))  # 42
    print(parse_value(42))    # 42

    # Variables
    print(f"{name} {version}")  # Python 3.11

    # Profile
    profile = create_profile("Bob", 25, "bob@example.com")
    print(profile)  # {'name': 'Bob', 'age': 25, 'active': True, 'email': 'bob@example.com'}
