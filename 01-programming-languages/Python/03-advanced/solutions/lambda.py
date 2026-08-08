"""
Module 03 - Advanced: Lambda Solutions
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Lambda Basics - Solution
# =============================================================================
square = lambda x: x ** 2
add = lambda x, y: x + y
is_even = lambda x: x % 2 == 0
absolute = lambda x: abs(x)
power = lambda x, y: x ** y

print(square(5))      # 25
print(add(3, 4))      # 7
print(is_even(4))     # True
print(absolute(-5))   # 5
print(power(2, 3))    # 8


# =============================================================================
# Exercise 2: Lambda with map() - Solution
# =============================================================================
numbers = [1, 2, 3, 4, 5]
doubled = map(lambda x: x * 2, numbers)
cubed = map(lambda x: x ** 3, numbers)
to_strings = map(lambda x: str(x), numbers)

print(list(doubled))      # [2, 4, 6, 8, 10]
print(list(cubed))        # [1, 8, 27, 64, 125]
print(list(to_strings))   # ['1', '2', '3', '4', '5']


# =============================================================================
# Exercise 3: Lambda with filter() - Solution
# =============================================================================
numbers = range(1, 21)
evens = filter(lambda x: x % 2 == 0, numbers)
greater_than_10 = filter(lambda x: x > 10, numbers)
multiples_of_3 = filter(lambda x: x % 3 == 0, numbers)

print(list(evens))           # [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
print(list(greater_than_10)) # [11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
print(list(multiples_of_3))  # [3, 6, 9, 12, 15, 18]


# =============================================================================
# Exercise 4: Lambda with sorted() - Solution
# =============================================================================
students = [
    {"name": "Alice", "grade": 88},
    {"name": "Bob", "grade": 95},
    {"name": "Charlie", "grade": 82},
    {"name": "Diana", "grade": 95}
]

by_grade_asc = sorted(students, key=lambda x: x["grade"])
by_grade_desc = sorted(students, key=lambda x: x["grade"], reverse=True)
by_name = sorted(students, key=lambda x: x["name"])

print(by_grade_asc)
print(by_name)


# =============================================================================
# Exercise 5: Lambda in Functional Patterns - Solution
# =============================================================================
def compose(f, g):
    """Compose two functions: (f ∘ g)(x) = f(g(x))"""
    return lambda *args, **kwargs: f(g(*args, **kwargs))

def curry(func):
    """Curry a function using closure."""
    import inspect
    params = inspect.signature(func).parameters

    def curried(*args):
        if len(args) >= len(params):
            return func(*args)
        return lambda *more_args: curried(*args, *more_args)

    return curried

add_one = lambda x: x + 1
double = lambda x: x * 2
add_one_then_double = compose(double, add_one)
print(add_one_then_double(5))  # 12 (5+1=6, 6*2=12)

add = lambda x, y: x + y
add5 = curry(add)(5)
print(add5(3))  # 8
