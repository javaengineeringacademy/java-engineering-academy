"""
Module 01: Fundamentals - Collections Solutions
Practice collection operations in Python.
"""


def matrix_transpose(matrix):
    """Transpose a matrix without using zip()."""
    if not matrix:
        return []

    rows = len(matrix)
    cols = len(matrix[0])
    result = []

    for j in range(cols):
        new_row = []
        for i in range(rows):
            new_row.append(matrix[i][j])
        result.append(new_row)

    return result


def flatten_deep(nested_list):
    """Flatten arbitrarily nested lists."""
    result = []
    for item in nested_list:
        if isinstance(item, list):
            result.extend(flatten_deep(item))
        else:
            result.append(item)
    return result


def chunk_list(lst, chunk_size):
    """Split list into chunks of specified size."""
    return [lst[i:i + chunk_size] for i in range(0, len(lst), chunk_size)]


def interleave_lists(*lists):
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


if __name__ == "__main__":
    print("Testing Collections Solutions...")
    assert matrix_transpose([[1, 2, 3], [4, 5, 6]]) == [[1, 4], [2, 5], [3, 6]]
    assert flatten_deep([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
    assert flatten_deep([[[[1]]]]) == [1]
    assert chunk_list([1, 2, 3, 4, 5], 2) == [[1, 2], [3, 4], [5]]
    assert interleave_lists([1, 3], [2, 4]) == [1, 2, 3, 4]
    assert sliding_window([1, 2, 3, 4, 5], 3) == [[1, 2, 3], [2, 3, 4], [3, 4, 5]]
    print("All Collections solutions passed!")
