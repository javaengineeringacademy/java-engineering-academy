"""
Module 12 - Collections: Set Operations Exercises
Difficulty: ⭐⭐ (Intermediate)
Topic: Set operations and frozenset
"""


# =============================================================================
# Exercise 1: Basic Set Operations (⭐⭐)
# =============================================================================

def exercise_1_basic_sets():
    """
    Perform basic set operations.
    
    TODO:
    1. Create sets from lists (remove duplicates)
    2. Find common elements (intersection)
    3. Find all unique elements (union)
    """
    list1 = [1, 2, 3, 4, 5, 5, 6]
    list2 = [4, 5, 6, 7, 8, 8, 9]
    
    set1 = set()
    set2 = set()
    common = set()
    all_unique = set()
    
    # TODO: Implement set operations
    pass


# =============================================================================
# Exercise 2: Set Comprehension (⭐⭐)
# =============================================================================

def exercise_2_set_comprehension():
    """
    Use set comprehensions for various operations.
    
    TODO:
    1. Create set of squares
    2. Create set of word lengths
    3. Remove vowels from string
    """
    numbers = range(1, 11)
    words = ['hello', 'world', 'python', 'programming']
    text = "Hello World"
    
    squares = set()
    lengths = set()
    no_vowels = set()
    
    # TODO: Implement set comprehensions
    pass


# =============================================================================
# Exercise 3: Set Theory Operations (⭐⭐⭐)
# =============================================================================

def exercise_3_set_theory():
    """
    Perform set theory operations.
    
    TODO:
    1. Find difference (A - B)
    2. Find symmetric difference (A △ B)
    3. Check if subset/superset
    """
    A = {1, 2, 3, 4, 5}
    B = {4, 5, 6, 7, 8}
    
    difference = set()
    symmetric_diff = set()
    is_subset = False
    is_superset = False
    
    # TODO: Implement set theory operations
    pass


# =============================================================================
# Exercise 4: Frozenset Operations (⭐⭐⭐)
# =============================================================================

def exercise_4_frozenset():
    """
    Work with frozensets (immutable sets).
    
    TODO:
    1. Create frozenset
    2. Use frozenset as dict key
    3. Create set of frozensets
    """
    data = [1, 2, 3, 4, 5]
    
    fs = frozenset()
    dict_with_fs = {}
    set_of_fs = set()
    
    # TODO: Implement frozenset operations
    pass


# =============================================================================
# Exercise 5: Advanced Set Operations (⭐⭐⭐⭐)
# =============================================================================

def exercise_5_advanced_sets():
    """
    Perform advanced set operations.
    
    TODO:
    1. Find all subsets of a set
    2. Check if sets can form a partition
    3. Find cartesian product
    """
    def find_subsets(s):
        # TODO: Find all subsets
        pass
    
    def is_partition(sets, universe):
        # TODO: Check if sets form a partition
        pass
    
    def cartesian_product(set1, set2):
        # TODO: Find cartesian product
        pass
    
    return find_subsets, is_partition, cartesian_product


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 12 - Set Operations Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Set Operations")
    try:
        result = exercise_1_basic_sets()
        print(f"  Set1: {result['set1']}")
        print(f"  Set2: {result['set2']}")
        print(f"  Common: {result['common']}")
        print(f"  All unique: {result['all_unique']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Set Comprehension")
    try:
        result = exercise_2_set_comprehension()
        print(f"  Squares: {result['squares']}")
        print(f"  Lengths: {result['lengths']}")
        print(f"  No vowels: {result['no_vowels']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Set Theory Operations")
    try:
        result = exercise_3_set_theory()
        print(f"  Difference: {result['difference']}")
        print(f"  Symmetric diff: {result['symmetric_diff']}")
        print(f"  Is subset: {result['is_subset']}")
        print(f"  Is superset: {result['is_superset']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Frozenset Operations")
    try:
        result = exercise_4_frozenset()
        print(f"  Frozenset: {result['fs']}")
        print(f"  Dict with fs: {result['dict_with_fs']}")
        print(f"  Set of fs: {result['set_of_fs']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced Set Operations")
    try:
        find_subsets, is_partition, cartesian_product = exercise_5_advanced_sets()
        
        subsets = find_subsets({1, 2, 3})
        print(f"  Subsets: {subsets}")
        
        partition = is_partition([{1, 2}, {3, 4}], {1, 2, 3, 4})
        print(f"  Is partition: {partition}")
        
        product = cartesian_product({1, 2}, {'a', 'b'})
        print(f"  Cartesian product: {product}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
