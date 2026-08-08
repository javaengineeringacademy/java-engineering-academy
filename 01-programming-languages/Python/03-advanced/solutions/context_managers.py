"""
Module 03 - Advanced: Context Managers Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Context Manager - Solution
# =============================================================================
class SafeFile:
    """Context manager for safe file operations."""

    def __init__(self, filename, mode):
        self.filename = filename
        self.mode = mode
        self.file = None

    def __enter__(self):
        self.file = open(self.filename, self.mode)
        return self.file

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.file:
            self.file.close()
        return False  # Don't suppress exceptions

with SafeFile("test.txt", "w") as f:
    f.write("Hello, Context Manager!")
with SafeFile("test.txt", "r") as f:
    print(f.read())  # "Hello, Context Manager!"


# =============================================================================
# Exercise 2: Context Manager with Exception Handling - Solution
# =============================================================================
class DatabaseConnection:
    """Context manager for database connections."""

    def __init__(self, connection_string):
        self.connection_string = connection_string
        self.connection = None

    def __enter__(self):
        print(f"Connecting to {self.connection_string}")
        self.connection = {"connected": True, "queries": []}
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        if exc_type:
            print(f"Error occurred: {exc_val}")
            print("Rolling back transaction")
        else:
            print("Committing transaction")
        self.connection = None
        print("Connection closed")
        return False

    def execute(self, query):
        if self.connection:
            self.connection["queries"].append(query)
            return f"Executed: {query}"
        raise Exception("Not connected")

with DatabaseConnection("sqlite:///:memory:") as db:
    db.execute("CREATE TABLE users (id INT, name TEXT)")
    db.execute("INSERT INTO users VALUES (1, 'Alice')")
    result = db.execute("SELECT * FROM users")
    print(result)


# =============================================================================
# Exercise 3: Contextlib Usage - Solution
# =============================================================================
from contextlib import contextmanager, suppress, redirect_stdout
import os
import tempfile
import io

@contextmanager
def temporary_directory():
    """Create and clean up a temporary directory."""
    temp_dir = tempfile.mkdtemp()
    try:
        yield temp_dir
    finally:
        import shutil
        shutil.rmtree(temp_dir, ignore_errors=True)

def capture_output(func, *args, **kwargs):
    """Capture function's stdout output."""
    captured = io.StringIO()
    with redirect_stdout(captured):
        result = func(*args, **kwargs)
    return captured.getvalue(), result

with temporary_directory() as temp_dir:
    print(f"Working in: {temp_dir}")

def my_function():
    print("This is captured")
    return 42

output, result = capture_output(my_function)
print(f"Output: {output}")   # "This is captured\n"
print(f"Result: {result}")   # 42


# =============================================================================
# Exercise 4: Nested Context Managers - Solution
# =============================================================================
class Transaction:
    """Context manager for database transactions."""

    def __init__(self, connection):
        self.connection = connection
        self.queries = []

    def __enter__(self):
        print("Beginning transaction")
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        if exc_type:
            print(f"Rolling back due to: {exc_val}")
        else:
            print("Committing transaction")
        return False

    def execute(self, query):
        self.queries.append(query)
        return f"Queued: {query}"

class ReadWriteLock:
    """Context manager for read-write locks."""

    def __init__(self):
        self.readers = 0
        self.writer_active = False

    @contextmanager
    def read_lock(self):
        self.readers += 1
        try:
            yield
        finally:
            self.readers -= 1

    @contextmanager
    def write_lock(self):
        self.writer_active = True
        try:
            yield
        finally:
            self.writer_active = False

db = DatabaseConnection("sqlite:///:memory:")
with Transaction(db) as tx:
    tx.execute("INSERT INTO users VALUES (1, 'Alice')")
    tx.execute("INSERT INTO users VALUES (2, 'Bob')")


# =============================================================================
# Exercise 5: Asynchronous Context Manager - Solution
# =============================================================================
import asyncio

class AsyncDatabase:
    """Async context manager for database operations."""

    def __init__(self, connection_string):
        self.connection_string = connection_string
        self.connection = None

    async def __aenter__(self):
        print(f"Async connecting to {self.connection_string}")
        self.connection = {"connected": True}
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        if exc_type:
            print(f"Async error: {exc_val}")
        self.connection = None
        print("Async connection closed")
        return False

    async def execute(self, query):
        await asyncio.sleep(0.1)  # Simulate async operation
        return f"Executed: {query}"

async def main():
    async with AsyncDatabase("sqlite:///:memory:") as db:
        await db.execute("CREATE TABLE users (id INT, name TEXT)")
        await db.execute("INSERT INTO users VALUES (1, 'Alice')")
        result = await db.execute("SELECT * FROM users")
        print(result)

asyncio.run(main())
