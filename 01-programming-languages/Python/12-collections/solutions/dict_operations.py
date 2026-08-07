"""
Module 12: Collections - Dict Operations Solutions
Practice dictionary operations in Python.
"""


def merge_dictionaries(*dicts, strategy="update"):
    """Merge multiple dictionaries with different strategies."""
    result = {}
    for d in dicts:
        if strategy == "update":
            result.update(d)
        elif strategy == "keep_first":
            for k, v in d.items():
                if k not in result:
                    result[k] = v
        elif strategy == "keep_last":
            for k, v in d.items():
                result[k] = v
        elif strategy == "sum":
            for k, v in d.items():
                result[k] = result.get(k, 0) + v
    return result


def invert_dict(d):
    """Invert dictionary keys and values."""
    return {v: k for k, v in d.items()}


def group_by_key(items, key_func):
    """Group items by a key function."""
    result = {}
    for item in items:
        key = key_func(item)
        if key not in result:
            result[key] = []
        result[key].append(item)
    return result


def nested_get(d, keys, default=None):
    """Get value from nested dictionary using list of keys."""
    current = d
    for key in keys:
        if isinstance(current, dict) and key in current:
            current = current[key]
        else:
            return default
    return current


def nested_set(d, keys, value):
    """Set value in nested dictionary using list of keys."""
    current = d
    for key in keys[:-1]:
        if key not in current:
            current[key] = {}
        current = current[key]
    current[keys[-1]] = value


def flatten_dict(d, parent_key='', sep='.'):
    """Flatten nested dictionary."""
    items = []
    for k, v in d.items():
        new_key = f"{parent_key}{sep}{k}" if parent_key else k
        if isinstance(v, dict):
            items.extend(flatten_dict(v, new_key, sep).items())
        else:
            items.append((new_key, v))
    return dict(items)


def dict_diff(d1, d2):
    """Find differences between two dictionaries."""
    diff = {}
    all_keys = set(d1.keys()) | set(d2.keys())
    for key in all_keys:
        if key not in d1:
            diff[key] = {"added": d2[key]}
        elif key not in d2:
            diff[key] = {"removed": d1[key]}
        elif d1[key] != d2[key]:
            diff[key] = {"old": d1[key], "new": d2[key]}
    return diff


if __name__ == "__main__":
    print("Testing Dict Operations Solutions...")

    d1 = {"a": 1, "b": 2}
    d2 = {"b": 3, "c": 4}
    result = merge_dictionaries(d1, d2, strategy="update")
    assert result == {"a": 1, "b": 3, "c": 4}
    print("✓ Exercise 1 passed: merge works")

    result = invert_dict({"a": 1, "b": 2})
    assert result == {1: "a", 2: "b"}
    print("✓ Exercise 2 passed: invert works")

    items = [("Alice", 85), ("Bob", 90), ("Alice", 92)]
    result = group_by_key(items, lambda x: x[0])
    assert "Alice" in result
    assert len(result["Alice"]) == 2
    print("✓ Exercise 3 passed: group_by works")

    nested = {"a": {"b": {"c": 42}}}
    assert nested_get(nested, ["a", "b", "c"]) == 42
    assert nested_get(nested, ["a", "x"], default="missing") == "missing"
    print("✓ Exercise 4 passed: nested_get works")

    nested = {}
    nested_set(nested, ["a", "b", "c"], 42)
    assert nested == {"a": {"b": {"c": 42}}}
    print("✓ Exercise 5 passed: nested_set works")

    nested = {"a": {"b": 1, "c": 2}, "d": 3}
    result = flatten_dict(nested)
    assert result == {"a.b": 1, "a.c": 2, "d": 3}
    print("✓ Exercise 6 passed: flatten works")

    d1 = {"a": 1, "b": 2, "c": 3}
    d2 = {"b": 4, "c": 3, "d": 5}
    result = dict_diff(d1, d2)
    assert "a" in result
    assert "d" in result
    assert "b" in result
    print("✓ Exercise 7 passed: diff works")

    print("All Dict Operations solutions passed!")
