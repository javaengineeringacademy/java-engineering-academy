"""
Exception Chaining in Python
Demonstrates raise...from and exception chaining
"""

# ============================================
# Basic Exception Chaining
# ============================================

def divide_numbers(a: float, b: float) -> float:
    """Divide two numbers with error handling."""
    try:
        return a / b
    except ZeroDivisionError as e:
        # Chain the original exception
        raise ValueError(f"Cannot divide {a} by {b}") from e

def convert_to_int(value: str) -> int:
    """Convert string to integer with chaining."""
    try:
        return int(value)
    except ValueError as e:
        raise TypeError(f"Cannot convert '{value}' to integer") from e

# ============================================
# Implicit Exception Chaining
# ============================================

def process_data(data: dict) -> str:
    """Process data with implicit chaining."""
    try:
        # This will raise KeyError
        return data["nonexistent"]["value"]
    except KeyError:
        # Implicitly chains with the original KeyError
        raise RuntimeError("Failed to access nested data")

# ============================================
# Exception Chaining with Custom Exceptions
# ============================================

class DatabaseError(Exception):
    """Database operation error."""
    pass

class ServiceError(Exception):
    """Service layer error."""
    pass

class APIError(Exception):
    """API layer error."""
    pass

def database_operation() -> None:
    """Simulate database operation that fails."""
    try:
        # Simulate database error
        raise ConnectionError("Database connection refused")
    except ConnectionError as e:
        raise DatabaseError("Failed to execute query") from e

def service_operation() -> None:
    """Simulate service operation that fails."""
    try:
        database_operation()
    except DatabaseError as e:
        raise ServiceError("Service unavailable") from e

def api_operation() -> None:
    """Simulate API operation that fails."""
    try:
        service_operation()
    except ServiceError as e:
        raise APIError("API request failed") from e

# ============================================
# Suppressing Exception Chaining
# ============================================

def suppress_chain() -> None:
    """Suppress exception chaining with None."""
    try:
        raise ValueError("Original error")
    except ValueError:
        # Use None to suppress chaining
        raise RuntimeError("New error") from None

# ============================================
# Exception Group (Python 3.11+)
# ============================================

def validate_multiple(values: list) -> None:
    """Validate multiple values, collecting all errors."""
    errors = []
    for i, value in enumerate(values):
        if not isinstance(value, int):
            errors.append(ValueError(f"Item {i}: not an integer"))
        elif value < 0:
            errors.append(ValueError(f"Item {i}: negative value"))
    
    if errors:
        # Create exception group (Python 3.11+)
        raise ExceptionGroup("Validation failed", errors)

# ============================================
# Custom Exception with Traceback
# ============================================

import traceback
import sys

class DetailedError(Exception):
    """Error with detailed traceback information."""
    
    def __init__(self, message: str) -> None:
        super().__init__(message)
        self.original_traceback = traceback.format_exc()
    
    def get_original_error(self) -> str:
        """Get the original error traceback."""
        return self.original_traceback

def detailed_error_example() -> None:
    """Demonstrate detailed error information."""
    try:
        try:
            result = 1 / 0
        except ZeroDivisionError as e:
            raise DetailedError("Calculation failed") from e
    except DetailedError as e:
        print(f"Error: {e}")
        print(f"Original traceback:\n{e.get_original_error()}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Basic chaining
    print("=== Basic Exception Chaining ===")
    try:
        result = divide_numbers(10, 0)
    except ValueError as e:
        print(f"Error: {e}")
        print(f"Original cause: {e.__cause__}")
    
    # Type conversion
    print("\n=== Type Conversion ===")
    try:
        convert_to_int("abc")
    except TypeError as e:
        print(f"Error: {e}")
        print(f"Original cause: {e.__cause__}")
    
    # Implicit chaining
    print("\n=== Implicit Chaining ===")
    try:
        process_data({"key": "value"})
    except RuntimeError as e:
        print(f"Error: {e}")
        print(f"Original cause: {e.__cause__}")
    
    # Custom exception chain
    print("\n=== Custom Exception Chain ===")
    try:
        api_operation()
    except APIError as e:
        print(f"Error: {e}")
        print(f"Cause: {e.__cause__}")
        print(f"Inner cause: {e.__cause__.__cause__}")
    
    # Suppress chaining
    print("\n=== Suppress Chaining ===")
    try:
        suppress_chain()
    except RuntimeError as e:
        print(f"Error: {e}")
        print(f"Cause (suppressed): {e.__cause__}")
    
    # Exception groups
    print("\n=== Exception Groups ===")
    try:
        validate_multiple([1, "two", -3, "four"])
    except ExceptionGroup as e:
        print(f"Error group: {e}")
        for err in e.exceptions:
            print(f"  - {err}")
    
    # Detailed error
    print("\n=== Detailed Error ===")
    detailed_error_example()
