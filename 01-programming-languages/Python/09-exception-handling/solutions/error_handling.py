"""
Module 09 - Exception Handling: Error Handling Solutions
Difficulty: Intermediate
"""

import logging

# =============================================================================
# Exercise 1: Try/Except Patterns - Solution
# =============================================================================
def safe_divide(a, b):
    """Divide with error handling."""
    try:
        return a / b
    except ZeroDivisionError:
        return None
    except TypeError:
        return None

def safe_get(dictionary, key, default=None):
    """Get from dictionary with error handling."""
    try:
        return dictionary[key]
    except KeyError:
        return default

def safe_int(value):
    """Convert to int with error handling."""
    try:
        return int(value)
    except (ValueError, TypeError):
        return None

print(safe_divide(10, 2))    # 5.0
print(safe_divide(10, 0))    # None
print(safe_get({"a": 1}, "b", 0))  # 0
print(safe_int("123"))       # 123
print(safe_int("abc"))       # None


# =============================================================================
# Exercise 2: Exception Handling Patterns - Solution
# =============================================================================
import time

def handle_with_retry(func, max_attempts=3):
    """Retry pattern."""
    for attempt in range(max_attempts):
        try:
            return func()
        except Exception:
            if attempt == max_attempts - 1:
                return None
            time.sleep(0.1)

def handle_with_fallback(primary_func, fallback_func):
    """Fallback pattern."""
    try:
        return primary_func()
    except Exception:
        return fallback_func()

def handle_with_default(func, default_value):
    """Default value pattern."""
    try:
        return func()
    except Exception:
        return default_value

result = handle_with_retry(lambda: 1/0, max_attempts=3)
print(result)  # None

result = handle_with_fallback(
    lambda: 1/0,
    lambda: "fallback value"
)
print(result)  # "fallback value"


# =============================================================================
# Exercise 3: Logging Errors - Solution
# =============================================================================
def setup_error_logger():
    """Set up error logger."""
    logging.basicConfig(
        level=logging.ERROR,
        format='%(asctime)s - %(levelname)s - %(message)s'
    )
    return logging.getLogger(__name__)

def log_and_raise(error_type, message):
    """Log error and raise exception."""
    logger = setup_error_logger()
    logger.error(message)
    raise error_type(message)

def process_with_logging(data):
    """Process data with error logging."""
    logger = setup_error_logger()
    try:
        return sum(data)
    except Exception as e:
        logger.error(f"Error processing data: {e}")
        raise

try:
    log_and_raise(ValueError, "Invalid input")
except ValueError as e:
    print(e)


# =============================================================================
# Exercise 4: Exception Translation - Solution
# =============================================================================
class ExternalAPIError(Exception):
    """External API error."""
    pass

class InternalServiceError(Exception):
    """Internal service error."""
    pass

def call_external_api(endpoint):
    """Call external API (may raise ExternalAPIError)."""
    if "invalid" in endpoint:
        raise ExternalAPIError(f"API error for {endpoint}")
    return {"status": "ok"}

def translate_api_error(endpoint):
    """Translate external API error to internal error."""
    try:
        return call_external_api(endpoint)
    except ExternalAPIError as e:
        raise InternalServiceError(f"Service unavailable: {e}") from e

try:
    translate_api_error("/invalid/endpoint")
except InternalServiceError as e:
    print(e)


# =============================================================================
# Exercise 5: Resource Cleanup - Solution
# =============================================================================
class DatabaseConnection:
    """Database connection with cleanup."""

    def __init__(self):
        self.connected = False

    def connect(self):
        self.connected = True
        print("Connected to database")

    def disconnect(self):
        self.connected = False
        print("Disconnected from database")

    def __enter__(self):
        self.connect()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.disconnect()
        return False

def execute_with_cleanup(func):
    """Execute function with proper cleanup."""
    conn = DatabaseConnection()
    try:
        conn.connect()
        return func(conn)
    finally:
        conn.disconnect()

with DatabaseConnection() as conn:
    # Do work
    pass
