"""
Module 12: Collections - Set Operations Solutions
Practice set operations in Python.
"""


def set_union(*sets):
    """Compute union of multiple sets."""
    result = set()
    for s in sets:
        result |= s
    return result


def set_intersection(*sets):
    """Compute intersection of multiple sets."""
    if not sets:
        return set()
    result = sets[0].copy()
    for s in sets[1:]:
        result &= s
    return result


def set_difference(set1, set2):
    """Compute difference of two sets."""
    return set1 - set2


def symmetric_difference(set1, set2):
    """Compute symmetric difference of two sets."""
    return set1 ^ set2


def powerset(s):
    """Compute powerset of a set."""
    result = [set()]
    for elem in s:
        result.extend([subset | {elem} for subset in result])
    return result


def find_duplicates(lst):
    """Find duplicate elements in a list using sets."""
    seen = set()
    duplicates = set()
    for item in lst:
        if item in seen:
            duplicates.add(item)
        seen.add(item)
    return duplicates


def are_disjoint(set1, set2):
    """Check if two sets are disjoint."""
    return set1.isdisjoint(set2)


def subset_of(set1, set2):
    """Check if set1 is a subset of set2."""
    return set1.issubset(set2)


def superset_of(set1, set2):
    """Check if set1 is a superset of set2."""
    return set1.issuperset(set2)


def cartesian_product(set1, set2):
    """Compute Cartesian product of two sets."""
    return {(a, b) for a in set1 for b in set2}


if __name__ == "__main__":
    print("Testing Set Operations Solutions...")

    assert set_union({1, 2}, {3, 4}, {5, 6}) == {1, 2, 3, 4, 5, 6}
    assert set_union({1, 2}, {2, 3}) == {1, 2, 3}
    print("✓ Exercise 1 passed: union works")

    assert set_intersection({1, 2, 3}, {2, 3, 4}, {3, 4, 5}) == {3}
    assert set_intersection({1, 2}, {3, 4}) == set()
    print("✓ Exercise 2 passed: intersection works")

    assert set_difference({1, 2, 3, 4}, {2, 4}) == {1, 3}
    assert set_difference({1, 2}, {3, 4}) == {1, 2}
    print("✓ Exercise 3 passed: difference works")

    assert symmetric_difference({1, 2, 3}, {3, 4, 5}) == {1, 2, 4, 5}
    assert symmetric_difference({1, 2}, {1, 2}) == set()
    print("✓ Exercise 4 passed: symmetric difference works")

    ps = powerset({1, 2})
    assert len(ps) == 4
    assert set() in ps
    assert {1, 2} in ps
    print("✓ Exercise 5 passed: powerset works")

    assert find_duplicates([1, 2, 3, 2, 4, 3]) == {2, 3}
    assert find_duplicates([1, 2, 3]) == set()
    print("✓ Exercise 6 passed: duplicates works")

    assert are_disjoint({1, 2}, {3, 4}) == True
    assert are_disjoint({1, 2}, {2, 3}) == False
    print("✓ Exercise 7 passed: disjoint works")

    assert subset_of({1, 2}, {1, 2, 3}) == True
    assert subset_of({1, 4}, {1, 2, 3}) == False
    print("✓ Exercise 8 passed: subset works")

    assert superset_of({1, 2, 3}, {1, 2}) == True
    assert superset_of({1, 2}, {1, 2, 3}) == False
    print("✓ Exercise 9 passed: superset works")

    result = cartesian_product({1, 2}, {3, 4})
    assert (1, 3) in result
    assert (2, 4) in result
    assert len(result) == 4
    print("✓ Exercise 10 passed: cartesian product works")

    print("All Set Operations solutions passed!")
