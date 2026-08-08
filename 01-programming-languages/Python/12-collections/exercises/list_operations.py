"""
Module 12 - Collections: List Operations Exercises
Difficulty: ⭐⭐ (Intermediate)
Topic: Advanced list operations and list comprehensions
"""


# =============================================================================
# Exercise 1: List Comprehension Basics (⭐⭐)
# =============================================================================

def exercise_1_list_comprehension():
    """
    Create list comprehensions for various operations.
    
    TODO:
    1. Create a list of squares from 1-10
    2. Create a list of even numbers from 1-20
    3. Create a list of tuples (n, n²) for n in 1-5
    """
    squares = []
    evens = []
    n_squared = []
    
    # TODO: Implement list comprehensions
    pass


# =============================================================================
# Exercise 2: Nested List Comprehension (⭐⭐⭐)
# =============================================================================

def exercise_2_nested_comprehension():
    """
    Work with nested lists and flatten them.
    
    TODO:
    1. Flatten a 2D list into 1D
    2. Create a 5x5 identity matrix
    3. Transpose a matrix
    """
    matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
    
    flattened = []
    identity = []
    transposed = []
    
    # TODO: Implement operations
    pass


# =============================================================================
# Exercise 3: List Slicing (⭐⭐)
# =============================================================================

def exercise_3_list_slicing():
    """
    Use list slicing for various operations.
    
    TODO:
    1. Reverse a list using slicing
    2. Get every other element
    3. Get middle third of a list
    """
    data = list(range(1, 16))  # [1, 2, ..., 15]
    
    reversed_list = []
    every_other = []
    middle_third = []
    
    # TODO: Implement slicing operations
    pass


# =============================================================================
# Exercise 4: List Filtering and Mapping (⭐⭐⭐)
# =============================================================================

def exercise_4_filter_map():
    """
    Use filter and map functions with lists.
    
    TODO:
    1. Filter even numbers using filter()
    2. Square numbers using map()
    3. Combine filter and map
    """
    numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    
    evens = []
    squared = []
    even_squared = []
    
    # TODO: Implement filter and map operations
    pass


# =============================================================================
# Exercise 5: Advanced List Operations (⭐⭐⭐⭐)
# =============================================================================

def exercise_5_advanced_list():
    """
    Perform advanced list operations.
    
    TODO:
    1. Find all pairs that sum to a target
    2. Rotate a list by n positions
    3. Find longest consecutive sequence
    """
    def find_pairs(lst, target):
        # TODO: Find all pairs that sum to target
        pass
    
    def rotate_list(lst, n):
        # TODO: Rotate list by n positions
        pass
    
    def longest_consecutive(lst):
        # TODO: Find longest consecutive sequence
        pass
    
    return find_pairs, rotate_list, longest_consecutive


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 12 - List Operations Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: List Comprehension Basics")
    try:
        result = exercise_1_list_comprehension()
        print(f"  Squares: {result['squares']}")
        print(f"  Evens: {result['evens']}")
        print(f"  N²: {result['n_squared']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Nested List Comprehension")
    try:
        result = exercise_2_nested_comprehension()
        print(f"  Flattened: {result['flattened']}")
        print(f"  Identity: {result['identity']}")
        print(f"  Transposed: {result['transposed']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: List Slicing")
    try:
        result = exercise_3_list_slicing()
        print(f"  Reversed: {result['reversed_list']}")
        print(f"  Every other: {result['every_other']}")
        print(f"  Middle third: {result['middle_third']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: List Filtering and Mapping")
    try:
        result = exercise_4_filter_map()
        print(f"  Evens: {result['evens']}")
        print(f"  Squared: {result['squared']}")
        print(f"  Even²: {result['even_squared']}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced List Operations")
    try:
        find_pairs, rotate_list, longest_consecutive = exercise_5_advanced_list()
        pairs = find_pairs([1, 2, 3, 4, 5], 5)
        print(f"  Pairs summing to 5: {pairs}")
        rotated = rotate_list([1, 2, 3, 4, 5], 2)
        print(f"  Rotated: {rotated}")
        longest = longest_consecutive([1, 3, 4, 5, 6, 7, 8])
        print(f"  Longest consecutive: {longest}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
