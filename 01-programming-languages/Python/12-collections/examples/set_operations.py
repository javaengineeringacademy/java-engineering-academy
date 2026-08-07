"""
Set Operations in Python
Demonstrates set operations and advanced techniques
"""

# ============================================
# Basic Set Operations
# ============================================

def basic_operations() -> None:
    """Demonstrate basic set operations."""
    print("=== Basic Operations ===")
    
    # Create sets
    set1 = {1, 2, 3, 4, 5}
    set2 = {4, 5, 6, 7, 8}
    
    print(f"  Set 1: {set1}")
    print(f"  Set 2: {set2}")
    
    # Add elements
    set1.add(6)
    print(f"  After add(6): {set1}")
    
    # Remove elements
    set1.discard(10)  # No error if not exists
    set1.remove(6)    # Raises KeyError if not exists
    print(f"  After remove operations: {set1}")

# ============================================
# Set Theoretic Operations
# ============================================

def set_theory_operations() -> None:
    """Demonstrate set theory operations."""
    print("\n=== Set Theory Operations ===")
    
    A = {1, 2, 3, 4, 5}
    B = {4, 5, 6, 7, 8}
    
    print(f"  A: {A}")
    print(f"  B: {B}")
    
    # Union
    print(f"  A | B (union): {A | B}")
    print(f"  A.union(B): {A.union(B)}")
    
    # Intersection
    print(f"  A & B (intersection): {A & B}")
    print(f"  A.intersection(B): {A.intersection(B)}")
    
    # Difference
    print(f"  A - B (difference): {A - B}")
    print(f"  B - A: {B - A}")
    
    # Symmetric Difference
    print(f"  A ^ B (symmetric diff): {A ^ B}")
    print(f"  A.symmetric_difference(B): {A.symmetric_difference(B)}")

# ============================================
# Subset and Superset
# ============================================

def subset_operations() -> None:
    """Demonstrate subset and superset operations."""
    print("\n=== Subset/Superset ===")
    
    A = {1, 2, 3}
    B = {1, 2, 3, 4, 5}
    C = {1, 2, 3}
    
    print(f"  A: {A}")
    print(f"  B: {B}")
    print(f"  C: {C}")
    
    # Subset
    print(f"  A <= B (A is subset of B): {A <= B}")
    print(f"  A.issubset(B): {A.issubset(B)}")
    
    # Proper subset
    print(f"  A < B (proper subset): {A < B}")
    
    # Superset
    print(f"  B >= A (B is superset of A): {B >= A}")
    print(f"  B.issuperset(A): {B.issuperset(A)}")
    
    # Proper superset
    print(f"  B > A (proper superset): {B > A}")
    
    # Equality
    print(f"  A == C: {A == C}")

# ============================================
# Set Comprehensions
# ============================================

def set_comprehensions() -> None:
    """Demonstrate set comprehensions."""
    print("\n=== Set Comprehensions ===")
    
    # Basic comprehension
    squares = {x**2 for x in range(10)}
    print(f"  Squares: {squares}")
    
    # Conditional
    even_squares = {x**2 for x in range(10) if x % 2 == 0}
    print(f"  Even squares: {even_squares}")
    
    # From string
    unique_chars = {char.lower() for char in "Hello World"}
    print(f"  Unique chars: {unique_chars}")
    
    # Remove duplicates from list
    numbers = [1, 2, 2, 3, 3, 3, 4, 4, 4, 4]
    unique = list(set(numbers))
    print(f"  Unique numbers: {unique}")

# ============================================
# Frozen Sets
# ============================================

def frozen_sets() -> None:
    """Demonstrate frozen sets."""
    print("\n=== Frozen Sets ===")
    
    # Regular set
    regular_set = {1, 2, 3}
    print(f"  Regular set: {regular_set}")
    
    # Frozen set (immutable)
    frozen_set = frozenset([4, 5, 6])
    print(f"  Frozen set: {frozen_set}")
    
    # Frozen sets can be used as dict keys
    locations = {
        frozenset([40.7128, -74.0060]): "New York",
        frozenset([34.0522, -118.2437]): "Los Angeles"
    }
    print(f"  Locations with frozenset keys: {locations}")
    
    # Frozen sets as set elements
    set_of_sets = {frozenset([1, 2]), frozenset([3, 4])}
    print(f"  Set of frozensets: {set_of_sets}")

# ============================================
# Practical Applications
# ============================================

def practical_applications() -> None:
    """Demonstrate practical set applications."""
    print("\n=== Practical Applications ===")
    
    # Find common elements
    students_math = {"Alice", "Bob", "Charlie", "Diana"}
    students_science = {"Bob", "Diana", "Eve", "Frank"}
    
    both_subjects = students_math & students_science
    print(f"  Students in both subjects: {both_subjects}")
    
    only_math = students_math - students_science
    print(f"  Only in math: {only_math}")
    
    # Permission checking
    admin_permissions = {"read", "write", "delete", "execute"}
    user_permissions = {"read", "write"}
    
    required = {"read", "write", "execute"}
    has_permission = required.issubset(user_permissions)
    print(f"\n  Admin permissions: {admin_permissions}")
    print(f"  User permissions: {user_permissions}")
    print(f"  User has required permissions: {has_permission}")
    
    # Remove duplicates while preserving order
    def remove_duplicates_ordered(sequence):
        seen = set()
        result = []
        for item in sequence:
            if item not in seen:
                seen.add(item)
                result.append(item)
        return result
    
    data = [1, 2, 2, 3, 3, 3, 4, 4, 4, 4]
    print(f"\n  Original: {data}")
    print(f"  Without duplicates (ordered): {remove_duplicates_ordered(data)}")
    
    # Fast membership testing
    large_set = set(range(1000000))
    print(f"\n  999999 in large_set: {999999 in large_set}")
    print(f"  1000000 in large_set: {1000000 in large_set}")

# ============================================
# Set Algorithms
# ============================================

def set_algorithms() -> None:
    """Demonstrate set-based algorithms."""
    print("\n=== Set Algorithms ===")
    
    # Cartesian product
    colors = {"red", "blue"}
    sizes = {"S", "M", "L"}
    
    products = {(color, size) for color in colors for size in sizes}
    print(f"  Cartesian product: {products}")
    
    # Power set
    def power_set(s):
        result = [set()]
        for elem in s:
            result.extend([subset | {elem} for subset in result])
        return result
    
    small_set = {1, 2, 3}
    p_set = power_set(small_set)
    print(f"  Power set of {small_set}: {p_set}")
    
    # Partition
    def partition(s, predicate):
        true_set = {x for x in s if predicate(x)}
        false_set = s - true_set
        return true_set, false_set
    
    numbers = set(range(10))
    evens, odds = partition(numbers, lambda x: x % 2 == 0)
    print(f"\n  Evens: {evens}")
    print(f"  Odds: {odds}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    basic_operations()
    set_theory_operations()
    subset_operations()
    set_comprehensions()
    frozen_sets()
    practical_applications()
    set_algorithms()
