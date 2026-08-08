"""
Module 03 - Advanced: Context Managers Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Context Manager (Difficulty: Beginner)
# =============================================================================
# Implement a context manager using __enter__ and __exit__.

# TODO: Implement the file context manager
class SafeFile:
    """Context manager for safe file operations."""

    def __init__(self, filename, mode):
        pass

    def __enter__(self):
        pass

    def __exit__(self, exc_type, exc_val, exc_tb):
        pass

# Test cases
# with SafeFile("test.txt", "w") as f:
#     f.write("Hello, Context Manager!")
# # File is automatically closed
# with SafeFile("test.txt", "r") as f:
#     print(f.read())  # Expected: "Hello, Context Manager!"


# =============================================================================
# Exercise 2: Context Manager with Exception Handling (Difficulty: Intermediate)
# =============================================================================
# Handle exceptions in context managers.

# TODO: Implement the database context manager
class DatabaseConnection:
    """Context manager for database connections."""

    def __init__(self, connection_string):
        pass

    def __enter__(self):
        pass

    def __exit__(self, exc_type, exc_val, exc_tb):
        pass

    def execute(self, query):
        pass

# Test cases
# with DatabaseConnection("sqlite:///:memory:") as db:
#     db.execute("CREATE TABLE users (id INT, name TEXT)")
#     db.execute("INSERT INTO users VALUES (1, 'Alice')")
#     result = db.execute("SELECT * FROM users")
#     print(result)
# # Connection is closed automatically


# =============================================================================
# Exercise 3: Contextlib Usage (Difficulty: Intermediate)
# =============================================================================
# Use contextlib for simpler context managers.

from contextlib import contextmanager, suppress, redirect_stdout
import io

# TODO: Implement using @contextmanager
@contextmanager
def temporary_directory():
    """Create and clean up a temporary directory."""
    pass

# TODO: Implement redirect stdout
def capture_output(func, *args, **kwargs):
    """Capture function's stdout output."""
    pass

# Test cases
# with temporary_directory() as temp_dir:
#     print(f"Working in: {temp_dir}")
#     # Do work in temp_dir
# # Directory is cleaned up automatically
#
# def my_function():
#     print("This is captured")
#     return 42
#
# output, result = capture_output(my_function)
# print(f"Output: {output}")   # Expected: "This is captured"
# print(f"Result: {result}")   # Expected: 42


# =============================================================================
# Exercise 4: Nested Context Managers (Difficulty: Intermediate)
# =============================================================================
# Use nested context managers.

# TODO: Implement transaction context
class Transaction:
    """Context manager for database transactions."""

    def __init__(self, connection):
        pass

    def __enter__(self):
        pass

    def __exit__(self, exc_type, exc_val, exc_tb):
        pass

# TODO: Implement lock context
class ReadWriteLock:
    """Context manager for read-write locks."""

    def read_lock(self):
        pass

    def write_lock(self):
        pass

# Test cases
# db = DatabaseConnection("sqlite:///:memory:")
# with Transaction(db) as tx:
#     tx.execute("INSERT INTO users VALUES (1, 'Alice')")
#     tx.execute("INSERT INTO users VALUES (2, 'Bob')")
#     # Transaction commits automatically


# =============================================================================
# Exercise 5: Asynchronous Context Manager (Difficulty: Advanced)
# =============================================================================
# Implement async context managers.

import asyncio

# TODO: Implement async context manager
class AsyncDatabase:
    """Async context manager for database operations."""

    def __init__(self, connection_string):
        pass

    async def __aenter__(self):
        pass

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        pass

    async def execute(self, query):
        pass

# TODO: Implement async generator context
@contextmanager
async def async_file_operations(filename):
    """Async context manager for file operations."""
    pass

# Test cases
# async def main():
#     async with AsyncDatabase("sqlite:///:memory:") as db:
#         await db.execute("CREATE TABLE users (id INT, name TEXT)")
#         await db.execute("INSERT INTO users VALUES (1, 'Alice')")
#         result = await db.execute("SELECT * FROM users")
#         print(result)
#
# asyncio.run(main())
