"""
Module 12: Collections - Dict Operations Exercises
==================================================
Practice advanced dictionary operations and techniques.
"""

# =============================================================================
# Exercise 1: Dict Merge (★☆☆☆☆)
# =============================================================================
# TODO: Merge multiple dictionaries with precedence

def merge_dicts(*dicts, precedence="last"):
    """Merge multiple dicts. precedence='last' means later dicts win."""
    # TODO: Implement dict merging
    pass

# Test Cases
def test_dict_merge():
    d1 = {"a": 1, "b": 2}
    d2 = {"b": 3, "c": 4}
    d3 = {"c": 5, "d": 6}
    
    result = merge_dicts(d1, d2, d3)
    assert result == {"a": 1, "b": 3, "c": 5, "d": 6}
    
    result_first = merge_dicts(d1, d2, d3, precedence="first")
    assert result_first["b"] == 2
    print("✓ Exercise 1 passed: dict merging works")

# =============================================================================
# Exercise 2: Dict Inversion (★★☆☆☆)
# =============================================================================
# TODO: Invert dict keys and values

def invert_dict(d):
    """Invert dict: values become keys, keys become values.
    
    invert_dict({"a": 1, "b": 2}) => {1: "a", 2: "b"}
    """
    # TODO: Implement dict inversion
    pass

# Test Cases
def test_invert_dict():
    assert invert_dict({"a": 1, "b": 2}) == {1: "a", 2: "b"}
    assert invert_dict({"x": "hello"}) == {"hello": "x"}
    assert invert_dict({}) == {}
    print("✓ Exercise 2 passed: dict inversion works")

# =============================================================================
# Exercise 3: Nested Dict Access (★★★☆☆)
# =============================================================================
# TODO: Access nested dict with dot notation

def get_nested(d, path, default=None):
    """Get value from nested dict using dot-separated path.
    
    get_nested({"a": {"b": {"c": 1}}}, "a.b.c") => 1
    """
    # TODO: Implement nested access
    pass

# Test Cases
def test_nested_access():
    data = {"user": {"profile": {"name": "Alice", "age": 30}}}
    
    assert get_nested(data, "user.profile.name") == "Alice"
    assert get_nested(data, "user.profile.age") == 30
    assert get_nested(data, "user.address.city", "Unknown") == "Unknown"
    print("✓ Exercise 3 passed: nested access works")

# =============================================================================
# Exercise 4: Dict Grouping (★★★★☆)
# =============================================================================
# TODO: Group list of dicts by key

def group_by(items, key):
    """Group list of dicts by specified key.
    
    group_by([{"type": "a", "val": 1}, {"type": "b", "val": 2}, {"type": "a", "val": 3}], "type")
    => {"a": [{"type": "a", "val": 1}, {"type": "a", "val": 3}], "b": [...]}
    """
    # TODO: Implement grouping
    pass

# Test Cases
def test_grouping():
    items = [
        {"type": "fruit", "name": "apple"},
        {"type": "veggie", "name": "carrot"},
        {"type": "fruit", "name": "banana"},
        {"type": "veggie", "name": "spinach"}
    ]
    
    result = group_by(items, "type")
    assert len(result["fruit"]) == 2
    assert len(result["veggie"]) == 2
    print("✓ Exercise 4 passed: grouping works")

# =============================================================================
# Exercise 5: Dict Diff (★★★★★)
# =============================================================================
# TODO: Compute difference between two dicts

def dict_diff(d1, d2):
    """Compute diff between two dicts.
    
    Returns dict with keys: 'added', 'removed', 'changed', 'unchanged'
    """
    # TODO: Implement dict diffing
    pass

# Test Cases
def test_dict_diff():
    d1 = {"a": 1, "b": 2, "c": 3}
    d2 = {"b": 2, "c": 4, "d": 5}
    
    result = dict_diff(d1, d2)
    assert result["added"] == {"d": 5}
    assert result["removed"] == {"a": 1}
    assert result["changed"] == {"c": (3, 4)}
    assert result["unchanged"] == {"b": 2}
    print("✓ Exercise 5 passed: dict diff works")

if __name__ == "__main__":
    print("Running Dict Operations Exercises...")
    print("=" * 50)
    test_dict_merge()
    test_invert_dict()
    test_nested_access()
    test_grouping()
    test_dict_diff()
    print("=" * 50)
    print("All tests passed!")
