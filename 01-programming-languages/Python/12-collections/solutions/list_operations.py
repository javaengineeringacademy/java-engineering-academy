"""
Module 12: Collections - List Operations Solutions
Practice advanced list operations and techniques.
"""


def flatten(nested_list):
    """Flatten arbitrarily nested lists."""
    result = []
    for item in nested_list:
        if isinstance(item, list):
            result.extend(flatten(item))
        else:
            result.append(item)
    return result


def chunk_list(lst, chunk_size):
    """Split list into chunks of specified size."""
    return [lst[i:i + chunk_size] for i in range(0, len(lst), chunk_size)]


def interleave(*lists):
    """Interleave elements from multiple lists."""
    result = []
    max_len = max(len(lst) for lst in lists) if lists else 0
    for i in range(max_len):
        for lst in lists:
            if i < len(lst):
                result.append(lst[i])
    return result


def sliding_window(lst, window_size):
    """Return all windows of given size."""
    if len(lst) < window_size:
        return []
    return [lst[i:i + window_size] for i in range(len(lst) - window_size + 1)]


def deduplicate_ordered(lst):
    """Remove duplicates while preserving first occurrence order."""
    seen = set()
    result = []
    for item in lst:
        if item not in seen:
            seen.add(item)
            result.append(item)
    return result


if __name__ == "__main__":
    print("Testing List Operations Solutions...")

    assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
    assert flatten([[[[1]]]]) == [1]
    assert flatten([]) == []
    assert flatten([1, 2, 3]) == [1, 2, 3]
    print("✓ Exercise 1 passed: flattening works")

    assert chunk_list([1, 2, 3, 4, 5], 2) == [[1, 2], [3, 4], [5]]
    assert chunk_list([1, 2, 3], 1) == [[1], [2], [3]]
    assert chunk_list([], 3) == []
    assert chunk_list([1, 2], 5) == [[1, 2]]
    print("✓ Exercise 2 passed: chunking works")

    assert interleave([1, 3], [2, 4]) == [1, 2, 3, 4]
    assert interleave([1], [2], [3]) == [1, 2, 3]
    assert interleave([1, 4], [2, 5], [3, 6]) == [1, 2, 3, 4, 5, 6]
    print("✓ Exercise 3 passed: interleaving works")

    assert sliding_window([1, 2, 3, 4, 5], 3) == [[1, 2, 3], [2, 3, 4], [3, 4, 5]]
    assert sliding_window([1, 2], 3) == []
    assert sliding_window([1, 2, 3], 1) == [[1], [2], [3]]
    print("✓ Exercise 4 passed: sliding window works")

    assert deduplicate_ordered([3, 1, 2, 1, 3, 4]) == [3, 1, 2, 4]
    assert deduplicate_ordered([1, 1, 1]) == [1]
    assert deduplicate_ordered([]) == []
    assert deduplicate_ordered([1, 2, 3]) == [1, 2, 3]
    print("✓ Exercise 5 passed: order-preserving deduplication works")

    print("All List Operations solutions passed!")
