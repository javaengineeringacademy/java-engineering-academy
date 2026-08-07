"""
Module 01: Fundamentals - Variables Solutions
Practice variable operations and manipulation.
"""


def celsius_to_fahrenheit(celsius):
    """Convert Celsius to Fahrenheit."""
    return (celsius * 9/5) + 32


def fahrenheit_to_celsius(fahrenheit):
    """Convert Fahrenheit to Celsius."""
    return (fahrenheit - 32) * 5/9


def reverse_list(lst):
    """Return a new list with elements in reverse order."""
    result = []
    for i in range(len(lst) - 1, -1, -1):
        result.append(lst[i])
    return result


def flatten_list(nested_list):
    """Flatten a nested list into a single list."""
    result = []
    for item in nested_list:
        if isinstance(item, list):
            result.extend(flatten_list(item))
        else:
            result.append(item)
    return result


def filter_even_numbers(lst):
    """Return a new list containing only even numbers."""
    return [x for x in lst if x % 2 == 0]


def is_palindrome(text):
    """Check if text is a palindrome (case-insensitive, ignoring spaces)."""
    cleaned = text.lower().replace(" ", "")
    return cleaned == cleaned[::-1]


if __name__ == "__main__":
    print("Testing Variables Solutions...")
    assert celsius_to_fahrenheit(0) == 32.0
    assert celsius_to_fahrenheit(100) == 212.0
    assert fahrenheit_to_celsius(32) == 0.0
    assert fahrenheit_to_celsius(212) == 100.0
    assert reverse_list([1, 2, 3]) == [3, 2, 1]
    assert flatten_list([[1, 2], [3, 4], [5]]) == [1, 2, 3, 4, 5]
    assert filter_even_numbers([1, 2, 3, 4, 5, 6]) == [2, 4, 6]
    assert is_palindrome("racecar") == True
    assert is_palindrome("Race Car") == True
    print("All Variables solutions passed!")
