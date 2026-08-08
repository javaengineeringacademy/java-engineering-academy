"""
Module 12 - Collections: Dictionary Operations Solutions
Complete solutions with explanations
"""

from collections import defaultdict


# =============================================================================
# Exercise 1: Dictionary Comprehension - SOLUTION
# =============================================================================

def exercise_1_dict_comprehension():
    """
    Create dictionaries using comprehensions.
    """
    numbers = [1, 2, 3, 4, 5]
    keys = ['a', 'b', 'c', 'd']
    values = [10, 20, 30, 40]
    data = {'a': 1, 'b': 2, 'c': 3, 'd': 4, 'e': 5}
    
    # Create dict mapping numbers to their squares
    squares = {x: x**2 for x in numbers}
    
    # Create dict from two lists (keys and values)
    combined = {k: v for k, v in zip(keys, values)}
    
    # Filter dict by value (keep items where value > 2)
    filtered = {k: v for k, v in data.items() if v > 2}
    
    return {
        'squares': squares,
        'combined': combined,
        'filtered': filtered
    }


# =============================================================================
# Exercise 2: Dictionary Merging - SOLUTION
# =============================================================================

def exercise_2_dict_merging():
    """
    Merge dictionaries using different methods.
    """
    dict1 = {'a': 1, 'b': 2, 'c': 3}
    dict2 = {'b': 20, 'c': 30, 'd': 40}
    
    # Merge using | operator (Python 3.9+)
    merged_operator = dict1 | dict2
    
    # Merge using {**d1, **d2}
    merged_unpack = {**dict1, **dict2}
    
    # Merge using update() - modifies dict1 in place
    merged_update = dict1.copy()
    merged_update.update(dict2)
    
    return {
        'merged_operator': merged_operator,
        'merged_unpack': merged_unpack,
        'merged_update': merged_update
    }


# =============================================================================
# Exercise 3: Default Dictionary - SOLUTION
# =============================================================================

def exercise_3_default_dict():
    """
    Use defaultdict for groupby operations.
    """
    from collections import defaultdict
    
    words = ['apple', 'banana', 'cherry', 'avocado', 'blueberry', 'cantaloupe']
    
    # Group words by first letter
    grouped = defaultdict(list)
    for word in words:
        grouped[word[0]].append(word)
    
    # Count occurrences
    counts = defaultdict(int)
    text = "hello world hello python world hello"
    for word in text.split():
        counts[word] += 1
    
    # Build adjacency list
    adjacency = defaultdict(list)
    edges = [('A', 'B'), ('A', 'C'), ('B', 'C'), ('C', 'D')]
    for start, end in edges:
        adjacency[start].append(end)
        adjacency[end].append(start)
    
    return {
        'grouped': grouped,
        'counts': counts,
        'adjacency': adjacency
    }


# =============================================================================
# Exercise 4: Dictionary Sorting - SOLUTION
# =============================================================================

def exercise_4_dict_sorting():
    """
    Sort dictionaries by various criteria.
    """
    data = {
        'Charlie': 3,
        'Alice': 1,
        'Bob': 2,
        'Diana': 4,
        'Eve': 2
    }
    
    # Sort by key
    sorted_by_key = dict(sorted(data.items()))
    
    # Sort by value
    sorted_by_value = dict(sorted(data.items(), key=lambda x: x[1]))
    
    # Sort by multiple criteria (value ascending, then key ascending)
    sorted_by_value_then_key = dict(sorted(data.items(), key=lambda x: (x[1], x[0])))
    
    return {
        'sorted_by_key': sorted_by_key,
        'sorted_by_value': sorted_by_value,
        'sorted_by_value_then_key': sorted_by_value_then_key
    }


# =============================================================================
# Exercise 5: Advanced Dict Operations - SOLUTION
# =============================================================================

def exercise_5_advanced_dict():
    """
    Perform advanced dictionary operations.
    """
    def deep_merge(d1, d2):
        """Deep merge two nested dicts."""
        result = d1.copy()
        for key, value in d2.items():
            if key in result and isinstance(result[key], dict) and isinstance(value, dict):
                result[key] = deep_merge(result[key], value)
            else:
                result[key] = value
        return result
    
    def flatten_dict(d, parent_key='', sep='.'):
        """Flatten nested dict."""
        items = []
        for k, v in d.items():
            new_key = f"{parent_key}{sep}{k}" if parent_key else k
            if isinstance(v, dict):
                items.extend(flatten_dict(v, new_key, sep).items())
            else:
                items.append((new_key, v))
        return dict(items)
    
    def invert_dict(d):
        """Invert dict (swap keys and values)."""
        return {v: k for k, v in d.items()}
    
    return deep_merge, flatten_dict, invert_dict


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 12 - Dictionary Operations Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Dictionary Comprehension")
    result = exercise_1_dict_comprehension()
    assert result['squares'] == {1: 1, 2: 4, 3: 9, 4: 16, 5: 25}
    assert result['combined'] == {'a': 10, 'b': 20, 'c': 30, 'd': 40}
    assert result['filtered'] == {'c': 3, 'd': 4, 'e': 5}
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Dictionary Merging")
    result = exercise_2_dict_merging()
    expected = {'a': 1, 'b': 20, 'c': 30, 'd': 40}
    assert result['merged_operator'] == expected
    assert result['merged_unpack'] == expected
    assert result['merged_update'] == expected
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Default Dictionary")
    result = exercise_3_default_dict()
    assert 'a' in result['grouped']
    assert 'apple' in result['grouped']['a']
    assert result['counts']['hello'] == 3
    assert 'B' in result['adjacency']['A']
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Dictionary Sorting")
    result = exercise_4_dict_sorting()
    assert list(result['sorted_by_key'].keys()) == ['Alice', 'Bob', 'Charlie', 'Diana', 'Eve']
    assert list(result['sorted_by_value'].values()) == [1, 2, 2, 3, 4]
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced Dict Operations")
    deep_merge, flatten_dict, invert_dict = exercise_5_advanced_dict()
    
    d1 = {'a': 1, 'b': {'c': 2, 'd': 3}}
    d2 = {'b': {'d': 4, 'e': 5}, 'f': 6}
    merged = deep_merge(d1, d2)
    assert merged == {'a': 1, 'b': {'c': 2, 'd': 4, 'e': 5}, 'f': 6}
    
    nested = {'a': {'b': 1, 'c': 2}, 'd': 3}
    flat = flatten_dict(nested)
    assert flat == {'a.b': 1, 'a.c': 2, 'd': 3}
    
    simple = {'a': 1, 'b': 2, 'c': 3}
    inverted = invert_dict(simple)
    assert inverted == {1: 'a', 2: 'b', 3: 'c'}
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
