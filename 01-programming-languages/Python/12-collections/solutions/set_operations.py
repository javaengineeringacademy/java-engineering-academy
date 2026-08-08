"""
Module 12 - Collections: Set Operations Solutions
Complete solutions with explanations
"""

from itertools import combinations


# =============================================================================
# Exercise 1: Basic Set Operations - SOLUTION
# =============================================================================

def exercise_1_basic_sets():
    """
    Perform basic set operations.
    """
    list1 = [1, 2, 3, 4, 5, 5, 6]
    list2 = [4, 5, 6, 7, 8, 8, 9]
    
    # Create sets from lists (removes duplicates)
    set1 = set(list1)
    set2 = set(list2)
    
    # Find common elements (intersection)
    common = set1 & set2  # or set1.intersection(set2)
    
    # Find all unique elements (union)
    all_unique = set1 | set2  # or set1.union(set2)
    
    return {
        'set1': set1,
        'set2': set2,
        'common': common,
        'all_unique': all_unique
    }


# =============================================================================
# Exercise 2: Set Comprehension - SOLUTION
# =============================================================================

def exercise_2_set_comprehension():
    """
    Use set comprehensions for various operations.
    """
    numbers = range(1, 11)
    words = ['hello', 'world', 'python', 'programming']
    text = "Hello World"
    
    # Create set of squares
    squares = {x**2 for x in numbers}
    
    # Create set of word lengths
    lengths = {len(word) for word in words}
    
    # Remove vowels from string
    vowels = set('aeiouAEIOU')
    no_vowels = {char for char in text if char not in vowels}
    
    return {
        'squares': squares,
        'lengths': lengths,
        'no_vowels': no_vowels
    }


# =============================================================================
# Exercise 3: Set Theory Operations - SOLUTION
# =============================================================================

def exercise_3_set_theory():
    """
    Perform set theory operations.
    """
    A = {1, 2, 3, 4, 5}
    B = {4, 5, 6, 7, 8}
    
    # Find difference (A - B)
    difference = A - B  # or A.difference(B)
    
    # Find symmetric difference (A △ B)
    symmetric_diff = A ^ B  # or A.symmetric_difference(B)
    
    # Check if subset/superset
    C = {1, 2}
    is_subset = C.issubset(A)  # or C <= A
    is_superset = A.issuperset(C)  # or A >= C
    
    return {
        'difference': difference,
        'symmetric_diff': symmetric_diff,
        'is_subset': is_subset,
        'is_superset': is_superset
    }


# =============================================================================
# Exercise 4: Frozenset Operations - SOLUTION
# =============================================================================

def exercise_4_frozenset():
    """
    Work with frozensets (immutable sets).
    """
    data = [1, 2, 3, 4, 5]
    
    # Create frozenset
    fs = frozenset(data)
    
    # Use frozenset as dict key
    dict_with_fs = {fs: "data for this set"}
    
    # Create set of frozensets
    set_of_fs = {frozenset([1, 2]), frozenset([3, 4]), frozenset([5, 6])}
    
    return {
        'fs': fs,
        'dict_with_fs': dict_with_fs,
        'set_of_fs': set_of_fs
    }


# =============================================================================
# Exercise 5: Advanced Set Operations - SOLUTION
# =============================================================================

def exercise_5_advanced_sets():
    """
    Perform advanced set operations.
    """
    def find_subsets(s):
        """Find all subsets of a set."""
        s = list(s)
        subsets = []
        for r in range(len(s) + 1):
            for combo in combinations(s, r):
                subsets.append(set(combo))
        return subsets
    
    def is_partition(sets, universe):
        """Check if sets form a partition of universe."""
        # Check if union equals universe
        union = set()
        for s in sets:
            union |= s
        
        if union != universe:
            return False
        
        # Check if sets are disjoint
        for i, s1 in enumerate(sets):
            for j, s2 in enumerate(sets):
                if i != j and s1 & s2:
                    return False
        
        return True
    
    def cartesian_product(set1, set2):
        """Find cartesian product."""
        return {(a, b) for a in set1 for b in set2}
    
    return find_subsets, is_partition, cartesian_product


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 12 - Set Operations Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Set Operations")
    result = exercise_1_basic_sets()
    assert result['set1'] == {1, 2, 3, 4, 5, 6}
    assert result['set2'] == {4, 5, 6, 7, 8, 9}
    assert result['common'] == {4, 5, 6}
    assert result['all_unique'] == {1, 2, 3, 4, 5, 6, 7, 8, 9}
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Set Comprehension")
    result = exercise_2_set_comprehension()
    assert result['squares'] == {1, 4, 9, 16, 25, 36, 49, 64, 81, 100}
    assert result['lengths'] == {5, 6, 11}
    assert result['no_vowels'] == {'H', 'l', 'l', ' ', 'W', 'r', 'l', 'd'}
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Set Theory Operations")
    result = exercise_3_set_theory()
    assert result['difference'] == {1, 2, 3}
    assert result['symmetric_diff'] == {1, 2, 3, 6, 7, 8}
    assert result['is_subset'] == True
    assert result['is_superset'] == True
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Frozenset Operations")
    result = exercise_4_frozenset()
    assert result['fs'] == frozenset({1, 2, 3, 4, 5})
    assert result['fs'] in result['dict_with_fs']
    assert len(result['set_of_fs']) == 3
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Advanced Set Operations")
    find_subsets, is_partition, cartesian_product = exercise_5_advanced_sets()
    
    subsets = find_subsets({1, 2})
    assert len(subsets) == 4  # {}, {1}, {2}, {1,2}
    
    partition = is_partition([{1, 2}, {3, 4}], {1, 2, 3, 4})
    assert partition == True
    
    not_partition = is_partition([{1, 2}, {2, 3}], {1, 2, 3})
    assert not_partition == False
    
    product = cartesian_product({1, 2}, {'a', 'b'})
    assert product == {(1, 'a'), (1, 'b'), (2, 'a'), (2, 'b')}
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
