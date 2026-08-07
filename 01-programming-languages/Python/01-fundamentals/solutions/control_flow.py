"""
Module 01: Fundamentals - Control Flow Solutions
Practice control flow structures in Python.
"""


def sieve_of_eratosthenes(limit):
    """Generate all prime numbers up to limit using Sieve of Eratosthenes."""
    if limit < 2:
        return []

    is_prime = [True] * (limit + 1)
    is_prime[0] = is_prime[1] = False

    for i in range(2, int(limit**0.5) + 1):
        if is_prime[i]:
            for j in range(i*i, limit + 1, i):
                is_prime[j] = False

    return [i for i, prime in enumerate(is_prime) if prime]


def is_prime(n):
    """Check if a number is prime."""
    if n < 2:
        return False
    if n < 4:
        return True
    if n % 2 == 0 or n % 3 == 0:
        return False
    i = 5
    while i * i <= n:
        if n % i == 0 or n % (i + 2) == 0:
            return False
        i += 6
    return True


def transpose_matrix(matrix):
    """Transpose a matrix (2D list)."""
    if not matrix or not matrix[0]:
        return []

    rows = len(matrix)
    cols = len(matrix[0])
    return [[matrix[j][i] for j in range(rows)] for i in range(cols)]


def matrix_dimensions(matrix):
    """Return (rows, columns) of a matrix."""
    if not matrix:
        return (0, 0)
    return (len(matrix), len(matrix[0]) if matrix[0] else 0)


def fibonacci_memo(n, memo=None):
    """Calculate nth Fibonacci number with memoization."""
    if memo is None:
        memo = {}

    if n in memo:
        return memo[n]

    if n < 2:
        return n

    memo[n] = fibonacci_memo(n - 1, memo) + fibonacci_memo(n - 2, memo)
    return memo[n]


def fibonacci_generator(limit):
    """Generate Fibonacci numbers up to limit."""
    a, b = 0, 1
    while a <= limit:
        yield a
        a, b = b, a + b


if __name__ == "__main__":
    print("Testing Control Flow Solutions...")
    assert sieve_of_eratosthenes(30) == [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
    assert is_prime(2) == True
    assert is_prime(4) == False
    assert is_prime(17) == True
    assert transpose_matrix([[1, 2, 3], [4, 5, 6]]) == [[1, 4], [2, 5], [3, 6]]
    assert matrix_dimensions([[1, 2, 3], [4, 5, 6]]) == (2, 3)
    assert fibonacci_memo(10) == 55
    assert fibonacci_memo(0) == 0
    assert fibonacci_memo(1) == 1
    gen = list(fibonacci_generator(20))
    assert gen == [0, 1, 1, 2, 3, 5, 8, 13]
    print("All Control Flow solutions passed!")
