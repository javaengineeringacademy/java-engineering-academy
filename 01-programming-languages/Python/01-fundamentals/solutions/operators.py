"""
Module 01: Fundamentals - Operators Solutions
Practice operator usage in Python.
"""


def merge_dictionaries(dict1, dict2, conflict_strategy="keep_both"):
    """Merge two dictionaries with conflict handling strategies."""
    result = dict1.copy()

    for key, value in dict2.items():
        if key in result:
            if conflict_strategy == "keep_both":
                result[key] = [result[key], value]
            elif conflict_strategy == "first":
                pass  # Keep first dict's value
            elif conflict_strategy == "second":
                result[key] = value
            elif conflict_strategy == "add":
                if isinstance(result[key], (int, float)) and isinstance(value, (int, float)):
                    result[key] = result[key] + value
        else:
            result[key] = value

    return result


def fizzbuzz_extended(n, rules=None):
    """Return a list of strings from 1 to n with customizable FizzBuzz rules."""
    if rules is None:
        rules = {3: "Fizz", 5: "Buzz"}

    result = []
    for i in range(1, n + 1):
        output = ""
        for divisor, text in rules.items():
            if i % divisor == 0:
                output += text
        result.append(output if output else str(i))

    return result


if __name__ == "__main__":
    print("Testing Operators Solutions...")
    result1 = merge_dictionaries({"a": 1, "b": 2}, {"b": 3, "c": 4})
    assert result1 == {"a": 1, "b": [2, 3], "c": 4}
    result2 = merge_dictionaries({"a": 1}, {"a": 2}, "add")
    assert result2 == {"a": 3}
    result = fizzbuzz_extended(5)
    assert result == ["1", "2", "Fizz", "4", "Buzz"]
    result = fizzbuzz_extended(15)[-1]
    assert result == "FizzBuzz"
    print("All Operators solutions passed!")
