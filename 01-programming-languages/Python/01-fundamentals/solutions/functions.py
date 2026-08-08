"""
Module 01 - Fundamentals: Functions Solutions
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Basic Functions - Solution
# =============================================================================
def greet(name, greeting="Hello"):
    """Return a greeting string."""
    return f"{greeting}, {name}!"

def calculate_sum(*args):
    """Return the sum of all arguments."""
    return sum(args)

def create_profile(**kwargs):
    """Return a dictionary of profile information."""
    return kwargs

print(greet("Alice"))              # "Hello, Alice!"
print(greet("Bob", "Hi"))          # "Hi, Bob!"
print(calculate_sum(1, 2, 3, 4))   # 10
profile = create_profile(name="John", age=30, city="NYC")
print(profile)                     # {'name': 'John', 'age': 30, 'city': 'NYC'}


# =============================================================================
# Exercise 2: Return Values - Solution
# =============================================================================
def get_stats(numbers):
    """Return min, max, and average of a list."""
    return min(numbers), max(numbers), sum(numbers) / len(numbers)

def analyze_text(text):
    """Return word count, character count, and average word length."""
    words = text.split()
    word_count = len(words)
    char_count = len(text)
    avg_length = sum(len(word) for word in words) / word_count if word_count > 0 else 0
    return {'words': word_count, 'chars': char_count, 'avg_length': avg_length}

min_val, max_val, avg = get_stats([3, 1, 4, 1, 5, 9, 2, 6])
print(f"Min: {min_val}, Max: {max_val}, Avg: {avg}")

stats = analyze_text("Hello World")
print(stats)


# =============================================================================
# Exercise 3: Lambda Functions - Solution
# =============================================================================
def sort_by_second(tuples_list):
    """Sort list of tuples by second element using lambda."""
    return sorted(tuples_list, key=lambda x: x[1])

def filter_evens(numbers):
    """Return only even numbers using filter and lambda."""
    return list(filter(lambda x: x % 2 == 0, numbers))

data = [(1, 3), (2, 1), (3, 2)]
print(sort_by_second(data))  # [(2, 1), (3, 2), (1, 3)]
print(filter_evens([1, 2, 3, 4, 5, 6]))  # [2, 4, 6]


# =============================================================================
# Exercise 4: Recursion - Solution
# =============================================================================
def factorial(n):
    """Calculate n! recursively."""
    if n <= 1:
        return 1
    return n * factorial(n - 1)

def flatten(nested_list):
    """Flatten a nested list of arbitrary depth."""
    result = []
    for item in nested_list:
        if isinstance(item, list):
            result.extend(flatten(item))
        else:
            result.append(item)
    return result

print(factorial(5))   # 120
print(factorial(0))   # 1
print(flatten([1, [2, 3], [4, [5, 6]]]))  # [1, 2, 3, 4, 5, 6]


# =============================================================================
# Exercise 5: Higher-Order Functions - Solution
# =============================================================================
def repeat(times):
    """Return a decorator that repeats a function call 'times' times."""
    def decorator(func):
        def wrapper(*args, **kwargs):
            results = []
            for _ in range(times):
                results.append(func(*args, **kwargs))
            return results
        return wrapper
    return decorator

def my_map(func, iterable):
    """Apply func to each element in iterable."""
    return [func(item) for item in iterable]

@repeat(3)
def say_hello():
    return "Hello!"

print(say_hello())  # ["Hello!", "Hello!", "Hello!"]

result = my_map(lambda x: x * 2, [1, 2, 3])
print(result)  # [2, 4, 6]
