"""
Module 12: Collections - List Operations Exercises
==================================================
Practice advanced list operations and techniques.
"""

# =============================================================================
# Exercise 1: List Flattener (★☆☆☆☆)
# =============================================================================
# TODO: Flatten nested lists

def flatten(nested_list):
    """Flatten arbitrarily nested lists.
    
    Input: [1, [2, 3], [4, [5, 6]]]
    Output: [1, 2, 3, 4, 5, 6]
    """
    # TODO: Implement recursive flattening
    pass

# Test Cases
def test_flatten():
    assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
    assert flatten([[[[1]]]]) == [1]
    assert flatten([]) == []
    assert flatten([1, 2, 3]) == [1, 2, 3]
    print("✓ Exercise 1 passed: flattening works")

# =============================================================================
# Exercise 2: List Chunker (★★☆☆☆)
# =============================================================================
# TODO: Split list into chunks

def chunk_list(lst, chunk_size):
    """Split list into chunks of specified size.
    
    chunk_list([1,2,3,4,5], 2) => [[1,2], [3,4], [5]]
    """
    # TODO: Implement chunking
    pass

# Test Cases
def test_chunking():
    assert chunk_list([1, 2, 3, 4, 5], 2) == [[1, 2], [3, 4], [5]]
    assert chunk_list([1, 2, 3], 1) == [[1], [2], [3]]
    assert chunk_list([], 3) == []
    assert chunk_list([1, 2], 5) == [[1, 2]]
    print("✓ Exercise 2 passed: chunking works")

# =============================================================================
# Exercise 3: Interleave Lists (★★★☆☆)
# =============================================================================
# TODO: Interleave multiple lists

def interleave(*lists):
    """Interleave elements from multiple lists.
    
    interleave([1,3], [2,4]) => [1, 2, 3, 4]
    """
    # TODO: Implement interleaving
    pass

# Test Cases
def test_interleave():
    assert interleave([1, 3], [2, 4]) == [1, 2, 3, 4]
    assert interleave([1], [2], [3]) == [1, 2, 3]
    assert interleave([1, 4], [2, 5], [3, 6]) == [1, 2, 3, 4, 5, 6]
    print("✓ Exercise 3 passed: interleaving works")

# =============================================================================
# Exercise 4: List Window Slider (★★★★☆)
# =============================================================================
# TODO: Create sliding window over list

def sliding_window(lst, window_size):
    """Return all windows of given size.
    
    sliding_window([1,2,3,4,5], 3) => [[1,2,3], [2,3,4], [3,4,5]]
    """
    # TODO: Implement sliding window
    pass

# Test Cases
def test_sliding_window():
    assert sliding_window([1, 2, 3, 4, 5], 3) == [[1, 2, 3], [2, 3, 4], [3, 4, 5]]
    assert sliding_window([1, 2], 3) == []
    assert sliding_window([1, 2, 3], 1) == [[1], [2], [3]]
    print("✓ Exercise 4 passed: sliding window works")

# =============================================================================
# Exercise 5: List Deduplicator with Order (★★★★★)
# =============================================================================
# TODO: Remove duplicates while preserving order

def deduplicate_ordered(lst):
    """Remove duplicates while preserving first occurrence order.
    
    deduplicate_ordered([3, 1, 2, 1, 3, 4]) => [3, 1, 2, 4]
    """
    # TODO: Implement order-preserving deduplication
    pass

# Test Cases
def test_deduplication():
    assert deduplicate_ordered([3, 1, 2, 1, 3, 4]) == [3, 1, 2, 4]
    assert deduplicate_ordered([1, 1, 1]) == [1]
    assert deduplicate_ordered([]) == []
    assert deduplicate_ordered([1, 2, 3]) == [1, 2, 3]
    print("✓ Exercise 5 passed: order-preserving deduplication works")

if __name__ == "__main__":
    print("Running List Operations Exercises...")
    print("=" * 50)
    test_flatten()
    test_chunking()
    test_interleave()
    test_sliding_window()
    test_deduplication()
    print("=" * 50)
    print("All tests passed!")
