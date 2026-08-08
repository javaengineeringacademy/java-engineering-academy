"""
Module 09 - Exception Handling: Custom Exceptions Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Custom Exceptions - Solution
# =============================================================================
class InsufficientFundsError(Exception):
    """Exception raised when account has insufficient funds."""

    def __init__(self, balance, amount):
        self.balance = balance
        self.amount = amount
        super().__init__(f"Insufficient funds: have {balance}, tried to withdraw {amount}")

class InvalidAgeError(Exception):
    """Exception raised for invalid age."""

    def __init__(self, age):
        self.age = age
        super().__init__(f"Invalid age: {age}")

class ValidationError(Exception):
    """Exception raised for validation errors."""

    def __init__(self, field, message):
        self.field = field
        self.message = message
        super().__init__(f"Validation error for {field}: {message}")

class BankAccount:
    """Bank account with custom exceptions."""

    def __init__(self, balance=0):
        self.balance = balance

    def withdraw(self, amount):
        if amount > self.balance:
            raise InsufficientFundsError(self.balance, amount)
        self.balance -= amount
        return self.balance

class User:
    """User with age validation."""

    def __init__(self, name, age):
        if age < 0 or age > 150:
            raise InvalidAgeError(age)
        self.name = name
        self.age = age

account = BankAccount(100)
try:
    account.withdraw(150)
except InsufficientFundsError as e:
    print(e)  # "Insufficient funds: have 100, tried to withdraw 150"

try:
    user = User("Alice", -5)
except InvalidAgeError as e:
    print(e)  # "Invalid age: -5"


# =============================================================================
# Exercise 2: Exception Hierarchy - Solution
# =============================================================================
class AppError(Exception):
    """Base application error."""
    pass

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

def execute_query(query):
    """Execute query with proper error handling."""
    if "DROP" in query.upper():
        raise QueryError(f"Dangerous query: {query}")
    return f"Executed: {query}"

def connect_to_database(connection_string):
    """Connect to database with error handling."""
    if "invalid" in connection_string.lower():
        raise ConnectionError(f"Cannot connect to: {connection_string}")
    return f"Connected to {connection_string}"

try:
    connect_to_database("invalid://connection")
except ConnectionError as e:
    print(f"Connection failed: {e}")

try:
    execute_query("DROP TABLE users")
except QueryError as e:
    print(f"Query failed: {e}")


# =============================================================================
# Exercise 3: Exception Chaining - Solution
# =============================================================================
class ServiceError(Exception):
    """Service layer error."""
    pass

class RepositoryError(Exception):
    """Repository layer error."""
    pass

def fetch_data_from_repository(url):
    """Fetch data, may raise RepositoryError."""
    if "invalid" in url:
        raise RepositoryError(f"Cannot fetch from {url}")
    return {"id": 1, "name": "Alice"}

def process_data(data):
    """Process data, may raise ServiceError."""
    if not data:
        raise ServiceError("No data to process")
    return f"Processed: {data}"

def get_user_data(user_id):
    """High-level function that chains exceptions."""
    try:
        data = fetch_data_from_repository(f"/users/{user_id}")
        return process_data(data)
    except RepositoryError as e:
        raise ServiceError("Failed to get user data") from e

try:
    get_user_data(123)
except ServiceError as e:
    print(f"Service error: {e}")
    print(f"Original cause: {e.__cause__}")


# =============================================================================
# Exercise 4: Exception Groups - Solution
# =============================================================================
class ValidationErrors(Exception):
    """Group of validation errors."""

    def __init__(self, errors):
        self.errors = errors
        super().__init__(f"Validation failed with {len(errors)} errors")

def validate_user_data(data):
    """Validate user data, collect all errors."""
    errors = []
    if not data.get("name"):
        errors.append("Name is required")
    if not data.get("email") or "@" not in data.get("email", ""):
        errors.append("Valid email is required")
    age = data.get("age")
    if age is None or age < 0 or age > 150:
        errors.append("Age must be between 0 and 150")
    if errors:
        raise ValidationErrors(errors)
    return True

try:
    validate_user_data({"name": "", "email": "invalid", "age": -1})
except ValidationErrors as e:
    for error in e.errors:
        print(f"  - {error}")


# =============================================================================
# Exercise 5: Exception Safety - Solution
# =============================================================================
class ExceptionSafeTransaction:
    """Transaction that handles exceptions properly."""

    def __init__(self, connection):
        self.connection = connection
        self.queries = []

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        if exc_type:
            print(f"Rolling back due to: {exc_val}")
            return False
        print("Committing transaction")
        return False

    def execute(self, query):
        self.queries.append(query)
        return f"Queued: {query}"

def retry_with_backoff(func, max_retries=3, base_delay=1):
    """Retry function with exponential backoff."""
    import time
    last_exception = None
    for attempt in range(max_retries):
        try:
            return func()
        except Exception as e:
            last_exception = e
            if attempt < max_retries - 1:
                delay = base_delay * (2 ** attempt)
                time.sleep(delay)
    raise last_exception

with ExceptionSafeTransaction(None) as tx:
    tx.execute("INSERT INTO users VALUES (1, 'Alice')")
    tx.execute("INSERT INTO users VALUES (2, 'Bob')")
