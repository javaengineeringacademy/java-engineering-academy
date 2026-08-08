"""
Module 12 - Collections: List Operations Solutions
Complete solutions with explanations
"""


# =============================================================================
# Exercise 1: List Comprehension Basics - SOLUTION
# =============================================================================

def exercise_1_list_comprehension():
    """
    Create list comprehensions for various operations.
    """
    # List of squares from 1-10
    squares = [x**2 for x in range(1, 11)]
    
    # List of even numbers from 1-20
    evens = [x for x in range(1, 21) if x % 2 == 0]
    
    # List of tuples (n, n²) for n in 1-5
    n_squared = [(n, n**2) for n in range(1, 6)]
    
    return {
        'squares': squares,
        'evens': evens,
        'n_squared': n_squared
    }


# =============================================================================
# Exercise 2: Nested List Comprehension - SOLUTION
# =============================================================================

def exercise_2_nested_comprehension():
    """
    Work with nested lists and flatten them.
    """
    matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
    
    # Flatten 2D list into 1D
    flattened = [num for row in matrix for num in row]
    
    # Create 5x5 identity matrix
    identity = [[1 if i == j else 0 for j in range(5)] for i in range(5)]
    
    # Transpose a matrix
    transposed = [[row[i] for row in matrix] for i in range(len(matrix[0]))]
    
    return {
        'flattened': flattened,
        'identity': identity,
        'transposed': transposed
    }


# =============================================================================
# Exercise 3: List Slicing - SOLUTION
# =============================================================================

def exercise_3_list_slicing():
    """
    Use list slicing for various operations.
    """
    data = list(range(1, 16))  # [1, 2, ..., 15]
    
    # Reverse a list using slicing
    reversed_list = data[::-1]
    
    # Get every other element
    every_other = data[::2]
    
    # Get middle third of a list (indices 5-9 for 15 elements)
    middle_third = data[5:10]
    
    return {
        'reversed_list': reversed_list,
        'every_other': every_other,
        'middle_third': middle_third
    }


# =============================================================================
# Exercise 4: List Filtering and Mapping - SOLUTION
# =============================================================================

def exercise_4_filter_map():
    """
    Use filter and map functions with lists.
    """
    numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    
    # Filter even numbers using filter()
    evens = list(filter(lambda x: x % 2 == 0, numbers))
    
    # Square numbers using map()
    squared = list(map(lambda x: x**2, numbers))
    
    # Combine filter and map
    even_squared = list(map(lambda x: x**2, filter(lambda x: x % 2 == 0, numbers)))
    
    return {
        'evens': evens,
        'squared': squared,
        'even_squared': even_squared
    }


# =============================================================================
# Exercise 5: Advanced List Operations - SOLUTION
# =============================================================================

def exercise_5_advanced_list():
    """
    Perform advanced list operations.
    """
    def find_pairs(lst, target):
        """Find all pairs that sum to target."""
        pairs = []
        seen = set()
        for num in lst:
            complement = target - num
            if complement in seen:
                pairs.append((complement, num))
            seen.add(num)
        return pairs
    
    def rotate_list(lst, n):
        """Rotate list by n positions."""
        if not lst:
            return lst
        n = n % len(lst)
        return lst[n:] + lst[:n]
    
    def longest_consecutive(lst):
        """Find longest consecutive sequence."""
        if not lst:
            return 0
        
        num_set = set(lst)
        longest = 0
        
        for num in num_set:
            if num - 1 not in num_set:  # Start of sequence
                current = num
                streak = 1
                
                while current + 1 in num_set:
                    current += 1
                    streak += 1
                
                longest = max(longest, streak)
        
        return longest
    
    return find_pairs, rotate_list, longest_consecutive


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 12 - List Operations Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: List Comprehension Basics")
    result = exercise_1_list_comprehension()
    assert result['squares'] == [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
    assert result['evens'] == [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
    assert result['n_squared'] == [(1, 1), (2, 4), (3, 9), (4, 16), (5, 25)]
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Nested List Comprehension")
    result = exercise_2_nested_comprehension()
    assert result['flattened'] == [1, 2, 3, 4, 5, 6, 7, 8, 9]
    assert len(result['identity']) == 5
    assert result['identity'][0][0] == 1
    assert result['identity'][0][1] == 0
    assert result['transposed'] == [[1, 4, 7], [2, 5, 8], [3, 6, 9]]
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: List Slicing")
    result = exercise_3_list_slicing()
    assert result['reversed_list'] == list(range(15, 0, -1))
    assert result['every_other'] == [1, 3, 5, 7, 9, 11, 13, 15]
    assert result['middle_third'] == [6, 7, 8, 9, 10]
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: List Filtering and Mapping")
    result = exercise_4_filter_map()
    assert result['evens'] == [2, 4, 6, 8, 10]
    assert result['squared'] == [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
    assert result['even_squared'] == [4, 16, 36, 64, 100]
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced List Operations")
    find_pairs, rotate_list, longest_consecutive = exercise_5_advanced_list()
    
    pairs = find_pairs([1, 2, 3, 4, 5], 5)
    assert (2, 3) in pairs or (3, 2) in pairs
    
    rotated = rotate_list([1, 2, 3, 4, 5], 2)
    assert rotated == [3, 4, 5, 1, 2]
    
    longest = longest_consecutive([1, 3, 4, 5, 6, 7, 8])
    assert longest == 6  # [3, 4, 5, 6, 7, 8]
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
