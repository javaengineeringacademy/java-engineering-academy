"""
Module 12 - Collections: Dictionary Operations Exercises
Difficulty: ⭐⭐ (Intermediate)
Topic: Advanced dictionary operations
"""


# =============================================================================
# Exercise 1: Dictionary Comprehension (⭐⭐)
# =============================================================================

def exercise_1_dict_comprehension():
    """
    Create dictionaries using comprehensions.
    
    TODO:
    1. Create dict mapping numbers to their squares
    2. Create dict from two lists (keys and values)
    3. Filter dict by value
    """
    numbers = [1, 2, 3, 4, 5]
    keys = ['a', 'b', 'c', 'd']
    values = [10, 20, 30, 40]
    data = {'a': 1, 'b': 2, 'c': 3, 'd': 4, 'e': 5}
    
    squares = {}
    combined = {}
    filtered = {}
    
    # TODO: Implement dictionary comprehensions
    pass


# =============================================================================
# Exercise 2: Dictionary Merging (⭐⭐⭐)
# =============================================================================

def exercise_2_dict_merging():
    """
    Merge dictionaries using different methods.
    
    TODO:
    1. Merge using | operator (Python 3.9+)
    2. Merge using {**d1, **d2}
    3. Handle conflicts (second dict wins)
    """
    dict1 = {'a': 1, 'b': 2, 'c': 3}
    dict2 = {'b': 20, 'c': 30, 'd': 40}
    
    merged_operator = {}
    merged_unpack = {}
    merged_update = {}
    
    # TODO: Implement merging methods
    pass


# =============================================================================
# Exercise 3: Default Dictionary (⭐⭐⭐)
# =============================================================================

def exercise_3_default_dict():
    """
    Use defaultdict for groupby operations.
    
    TODO:
    1. Group words by first letter
    2. Count occurrences
    3. Build adjacency list
    """
    from collections import defaultdict
    
    words = ['apple', 'banana', 'cherry', 'avocado', 'blueberry', 'cantaloupe']
    
    grouped = defaultdict(list)
    counts = defaultdict(int)
    adjacency = defaultdict(list)
    
    edges = [('A', 'B'), ('A', 'C'), ('B', 'C'), ('C', 'D')]
    
    # TODO: Implement operations
    pass


# =============================================================================
# Exercise 4: Dictionary Sorting (⭐⭐⭐)
# =============================================================================

def exercise_4_dict_sorting():
    """
    Sort dictionaries by various criteria.
    
    TODO:
    1. Sort by key
    2. Sort by value
    3. Sort by multiple criteria
    """
    data = {
        'Charlie': 3,
        'Alice': 1,
        'Bob': 2,
        'Diana': 4,
        'Eve': 2
    }
    
    sorted_by_key = {}
    sorted_by_value = {}
    sorted_by_value_then_key = {}
    
    # TODO: Implement sorting operations
    pass


# =============================================================================
# Exercise 5: Advanced Dict Operations (⭐⭐⭐⭐)
# =============================================================================

def exercise_5_advanced_dict():
    """
    Perform advanced dictionary operations.
    
    TODO:
    1. Deep merge two nested dicts
    2. Flatten nested dict
    3. Invert dict (swap keys and values)
    """
    def deep_merge(d1, d2):
        # TODO: Deep merge two dicts
        pass
    
    def flatten_dict(d, parent_key='', sep='.'):
        # TODO: Flatten nested dict
        pass
    
    def invert_dict(d):
        # TODO: Invert dict (swap keys/values)
        pass
    
    return deep_merge, flatten_dict, invert_dict


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 12 - Dictionary Operations Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Dictionary Comprehension")
    try:
        result = exercise_1_dict_comprehension()
        print(f"  Squares: {result['squares']}")
        print(f"  Combined: {result['combined']}")
        print(f"  Filtered: {result['filtered']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Dictionary Merging")
    try:
        result = exercise_2_dict_merging()
        print(f"  Operator: {result['merged_operator']}")
        print(f"  Unpack: {result['merged_unpack']}")
        print(f"  Update: {result['merged_update']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Default Dictionary")
    try:
        result = exercise_3_default_dict()
        print(f"  Grouped: {dict(result['grouped'])}")
        print(f"  Counts: {dict(result['counts'])}")
        print(f"  Adjacency: {dict(result['adjacency'])}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Dictionary Sorting")
    try:
        result = exercise_4_dict_sorting()
        print(f"  By key: {result['sorted_by_key']}")
        print(f"  By value: {result['sorted_by_value']}")
        print(f"  By value+key: {result['sorted_by_value_then_key']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced Dict Operations")
    try:
        deep_merge, flatten_dict, invert_dict = exercise_5_advanced_dict()
        
        d1 = {'a': 1, 'b': {'c': 2, 'd': 3}}
        d2 = {'b': {'d': 4, 'e': 5}, 'f': 6}
        merged = deep_merge(d1, d2)
        print(f"  Deep merged: {merged}")
        
        nested = {'a': {'b': 1, 'c': 2}, 'd': 3}
        flat = flatten_dict(nested)
        print(f"  Flattened: {flat}")
        
        simple = {'a': 1, 'b': 2, 'c': 3}
        inverted = invert_dict(simple)
        print(f"  Inverted: {inverted}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
