"""
Module 12: Collections - Set Operations Exercises
=================================================
Practice advanced set operations and techniques.
"""

# =============================================================================
# Exercise 1: Set Algebra (★☆☆☆☆)
# =============================================================================
# TODO: Implement set operations on lists

def set_algebra(list1, list2, operation):
    """Perform set operation on two lists.
    
    Operations: 'union', 'intersection', 'difference', 'symmetric_difference'
    """
    # TODO: Convert to sets and perform operation
    pass

# Test Cases
def test_set_algebra():
    a = [1, 2, 3, 4]
    b = [3, 4, 5, 6]
    
    assert set_algebra(a, b, "union") == {1, 2, 3, 4, 5, 6}
    assert set_algebra(a, b, "intersection") == {3, 4}
    assert set_algebra(a, b, "difference") == {1, 2}
    assert set_algebra(a, b, "symmetric_difference") == {1, 2, 5, 6}
    print("✓ Exercise 1 passed: set algebra works")

# =============================================================================
# Exercise 2: Power Set (★★☆☆☆)
# =============================================================================
# TODO: Generate all subsets of a set

def power_set(s):
    """Return all subsets of a set as a list of sets."""
    # TODO: Implement power set generation
    pass

# Test Cases
def test_power_set():
    result = power_set({1, 2})
    assert len(result) == 4
    assert set() in result
    assert {1} in result
    assert {2} in result
    assert {1, 2} in result
    
    result3 = power_set({1, 2, 3})
    assert len(result3) == 8
    print("✓ Exercise 2 passed: power set generation works")

# =============================================================================
# Exercise 3: Venn Diagram Calculator (★★★☆☆)
# =============================================================================
# TODO: Compute Venn diagram regions for two sets

def venn_regions(set1, set2):
    """Return dict with Venn diagram regions:
    
    - only_a: elements only in set1
    - only_b: elements only in set2
    - both: elements in both
    - neither: elements in universe not in either
    """
    # TODO: Compute all four regions
    pass

# Test Cases
def test_venn():
    a = {1, 2, 3}
    b = {3, 4, 5}
    universe = range(1, 7)
    
    result = venn_regions(a, b, universe)
    assert result["only_a"] == {1, 2}
    assert result["only_b"] == {4, 5}
    assert result["both"] == {3}
    assert result["neither"] == {6}
    print("✓ Exercise 3 passed: Venn regions computed")

# =============================================================================
# Exercise 4: Set Partitioning (★★★★☆)
# =============================================================================
# TODO: Partition set into equal-sized groups

def partition_set(s, group_size):
    """Partition set into groups of specified size.
    
    Last group may be smaller.
    """
    # TODO: Implement partitioning
    pass

# Test Cases
def test_partitioning():
    result = partition_set({1, 2, 3, 4, 5, 6}, 2)
    assert len(result) == 3
    assert all(len(g) <= 2 for g in result)
    assert set.union(*result) == {1, 2, 3, 4, 5, 6}
    
    result3 = partition_set({1, 2, 3, 4, 5}, 3)
    assert len(result3) == 2
    print("✓ Exercise 4 passed: set partitioning works")

# =============================================================================
# Exercise 5: Multi-Set Operations (★★★★★)
# =============================================================================
# TODO: Implement multiset (bag) operations

class MultiSet:
    """Multiset (bag) implementation using dict."""
    # TODO: Implement add, remove, count
    # TODO: Implement union, intersection for multisets
    pass

# Test Tests
def test_multiset():
    ms1 = MultiSet()
    ms1.add(1)
    ms1.add(1)
    ms1.add(2)
    
    ms2 = MultiSet()
    ms2.add(1)
    ms2.add(2)
    ms2.add(2)
    
    assert ms1.count(1) == 2
    assert ms1.count(3) == 0
    
    union = ms1.union(ms2)
    assert union.count(1) == 2  # max
    assert union.count(2) == 2  # max
    
    intersection = ms1.intersection(ms2)
    assert intersection.count(1) == 1  # min
    assert intersection.count(2) == 1  # min
    
    print("✓ Exercise 5 passed: multiset operations work")

if __name__ == "__main__":
    print("Running Set Operations Exercises...")
    print("=" * 50)
    test_set_algebra()
    test_power_set()
    test_venn()
    test_partitioning()
    test_multiset()
    print("=" * 50)
    print("All tests passed!")
