"""
Module 09 - Exception Handling: Custom Exceptions Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Custom Exceptions (Difficulty: Beginner)
# =============================================================================
# Create custom exception classes.

# TODO: Create custom exceptions
class InsufficientFundsError(Exception):
    """Exception raised when account has insufficient funds."""
    pass

class InvalidAgeError(Exception):
    """Exception raised for invalid age."""
    pass

class ValidationError(Exception):
    """Exception raised for validation errors."""
    pass

# TODO: Implement classes that use custom exceptions
class BankAccount:
    """Bank account with custom exceptions."""

    def __init__(self, balance=0):
        pass

    def withdraw(self, amount):
        """Withdraw money, raise InsufficientFundsError if insufficient."""
        pass

class User:
    """User with age validation."""

    def __init__(self, name, age):
        pass

# Test cases
# account = BankAccount(100)
# try:
#     account.withdraw(150)
# except InsufficientFundsError as e:
#     print(e)  # Expected: "Insufficient funds: have 100, tried to withdraw 150"
#
# try:
#     user = User("Alice", -5)
# except InvalidAgeError as e:
#     print(e)  # Expected: "Invalid age: -5"


# =============================================================================
# Exercise 2: Exception Hierarchy (Difficulty: Intermediate)
# =============================================================================
# Create exception hierarchy.

# TODO: Create base exception
class AppError(Exception):
    """Base application error."""
    pass

# TODO: Create derived exceptions
class DatabaseError(AppError):
    """Database-related errors."""
    pass

class ConnectionError(DatabaseError):
    """Database connection errors."""
    pass

class QueryError(DatabaseError):
    """Database query errors."""
    pass

class APIError(AppError):
    """API-related errors."""
    pass

# TODO: Implement error handling
def execute_query(query):
    """Execute query with proper error handling."""
    pass

def connect_to_database(connection_string):
    """Connect to database with error handling."""
    pass

# Test cases
# try:
#     connect_to_database("invalid://connection")
# except ConnectionError as e:
#     print(f"Connection failed: {e}")
#
# try:
#     execute_query("INVALID SQL")
# except QueryError as e:
#     print(f"Query failed: {e}")


# =============================================================================
# Exercise 3: Exception Chaining (Difficulty: Intermediate)
# =============================================================================
# Chain exceptions properly.

# TODO: Implement exception chaining
class ServiceError(Exception):
    """Service layer error."""
    pass

class RepositoryError(Exception):
    """Repository layer error."""
    pass

def fetch_data_from_repository(url):
    """Fetch data, may raise RepositoryError."""
    pass

def process_data(data):
    """Process data, may raise ServiceError."""
    pass

def get_user_data(user_id):
    """High-level function that chains exceptions."""
    pass

# Test cases
# try:
#     get_user_data(123)
# except ServiceError as e:
#     print(f"Service error: {e}")
#     print(f"Original cause: {e.__cause__}")


# =============================================================================
# Exercise 4: Exception Groups (Difficulty: Advanced)
# =============================================================================
# Work with multiple exceptions.

# TODO: Implement exception groups
class ValidationErrors(Exception):
    """Group of validation errors."""

    def __init__(self, errors):
        self.errors = errors
        super().__init__(f"Validation failed with {len(errors)} errors")

def validate_user_data(data):
    """Validate user data, collect all errors."""
    pass

# Test cases
# try:
#     validate_user_data({"name": "", "email": "invalid", "age": -1})
# except ValidationErrors as e:
#     for error in e.errors:
#         print(f"  - {error}")


# =============================================================================
# Exercise 5: Exception Safety (Difficulty: Advanced)
# =============================================================================
# Implement exception-safe patterns.

# TODO: Implement context manager with exception handling
class ExceptionSafeTransaction:
    """Transaction that handles exceptions properly."""

    def __init__(self, connection):
        pass

    def __enter__(self):
        pass

    def __exit__(self, exc_type, exc_val, exc_tb):
        pass

    def execute(self, query):
        pass

# TODO: Implement retry with backoff
def retry_with_backoff(func, max_retries=3, base_delay=1):
    """Retry function with exponential backoff."""
    pass

# Test cases
# with ExceptionSafeTransaction(db) as tx:
#     tx.execute("INSERT INTO users VALUES (1, 'Alice')")
#     tx.execute("INSERT INTO users VALUES (2, 'Bob')")
# # Commits if no exceptions, rolls back otherwise
