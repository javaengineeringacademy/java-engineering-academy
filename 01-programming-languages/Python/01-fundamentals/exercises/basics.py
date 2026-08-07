"""
Python Fundamentals - Basic Exercises
Complete each exercise by implementing the required function.
Run the test cases to verify your solution.
"""

# Exercise 1: Temperature Converter (Easy)
# Implement functions to convert between Celsius and Fahrenheit
# Formula: F = (C * 9/5) + 32

def celsius_to_fahrenheit(celsius):
    """Convert Celsius to Fahrenheit."""
    # TODO: Implement this function
    pass

def fahrenheit_to_celsius(fahrenheit):
    """Convert Fahrenheit to Celsius."""
    # TODO: Implement this function
    pass


# Exercise 2: List Manipulation (Easy)
# Implement functions to manipulate lists

def reverse_list(lst):
    """Return a new list with elements in reverse order."""
    # TODO: Implement this function without using [::-1]
    pass

def flatten_list(nested_list):
    """Flatten a nested list into a single list."""
    # TODO: Implement this function
    # Example: [[1,2], [3,4], [5]] -> [1, 2, 3, 4, 5]
    pass

def filter_even_numbers(lst):
    """Return a new list containing only even numbers."""
    # TODO: Implement this function
    pass


# Exercise 3: String Palindrome Checker (Easy)
# Check if a string reads the same forwards and backwards

def is_palindrome(text):
    """
    Check if text is a palindrome (case-insensitive, ignoring spaces).
    Example: "racecar" -> True, "Race Car" -> True
    """
    # TODO: Implement this function
    pass


# Exercise 4: Dictionary Merge (Medium)
# Merge two dictionaries, handling conflicts

def merge_dictionaries(dict1, dict2, conflict_strategy="keep_both"):
    """
    Merge two dictionaries.
    conflict_strategy: 
        "keep_both" - create list of values for conflicting keys
        "first" - keep value from first dict
        "second" - keep value from second dict
        "add" - add numeric values
    """
    # TODO: Implement this function
    pass


# Exercise 5: FizzBuzz Extended (Medium)
# Classic FizzBuzz with customizable rules

def fizzbuzz_extended(n, rules=None):
    """
    Return a list of strings from 1 to n.
    Default rules: multiples of 3 -> "Fizz", multiples of 5 -> "Buzz"
    multiples of both -> "FizzBuzz"
    
    rules parameter allows custom mappings, e.g., {6: "Fizz", 10: "Buzz"}
    """
    # TODO: Implement this function
    pass


# ==================== TEST CASES ====================

def test_exercises():
    print("Testing Exercise 1: Temperature Converter")
    assert celsius_to_fahrenheit(0) == 32.0
    assert celsius_to_fahrenheit(100) == 212.0
    assert fahrenheit_to_celsius(32) == 0.0
    assert fahrenheit_to_celsius(212) == 100.0
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 2: List Manipulation")
    assert reverse_list([1, 2, 3]) == [3, 2, 1]
    assert flatten_list([[1, 2], [3, 4], [5]]) == [1, 2, 3, 4, 5]
    assert filter_even_numbers([1, 2, 3, 4, 5, 6]) == [2, 4, 6]
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 3: Palindrome Checker")
    assert is_palindrome("racecar") == True
    assert is_palindrome("Race Car") == True
    assert is_palindrome("hello") == False
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 4: Dictionary Merge")
    result1 = merge_dictionaries({"a": 1, "b": 2}, {"b": 3, "c": 4})
    assert result1 == {"a": 1, "b": [2, 3], "c": 4}
    result2 = merge_dictionaries({"a": 1}, {"a": 2}, "add")
    assert result2 == {"a": 3}
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 5: FizzBuzz Extended")
    result = fizzbuzz_extended(5)
    assert result == ["1", "2", "Fizz", "4", "Buzz"]
    result = fizzbuzz_extended(15)[-1]
    assert result == "FizzBuzz"
    print("  ✓ All tests passed!\n")

    print("All basic exercises passed!")


if __name__ == "__main__":
    test_exercises()
