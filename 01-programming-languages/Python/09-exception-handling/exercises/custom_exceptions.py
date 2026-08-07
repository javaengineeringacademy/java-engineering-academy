"""
Module 09: Exception Handling - Custom Exceptions Exercises
==========================================================
Practice creating and using custom exception classes.
"""

# =============================================================================
# Exercise 1: Basic Custom Exception (★☆☆☆☆)
# =============================================================================
# TODO: Create a custom exception with a message attribute

class ValidationError(Exception):
    """Custom exception for validation errors."""
    # TODO: Add __init__ that accepts field_name and message
    pass

# Test Cases
def test_validation_error():
    try:
        # TODO: Raise ValidationError with field="email" and message="invalid format"
        raise ValidationError("email", "invalid format")
    except ValidationError as e:
        assert e.field == "email"
        assert e.message == "invalid format"
        print(f"✓ Exercise 1 passed: caught {e.field}: {e.message}")

# =============================================================================
# Exercise 2: Exception Hierarchy (★★☆☆☆)
# =============================================================================
# TODO: Create a hierarchy of exceptions for a banking system

class BankingError(Exception):
    """Base exception for banking errors."""
    pass

class InsufficientFundsError(BankingError):
    """Raised when account has insufficient funds."""
    # TODO: Add balance and amount attributes
    pass

class AccountLockedError(BankingError):
    """Raised when account is locked."""
    # TODO: Add lock_reason attribute
    pass

# Test Cases
def test_banking_exceptions():
    # Test InsufficientFundsError
    try:
        raise InsufficientFundsError(100, 500)
    except InsufficientFundsError as e:
        assert e.balance == 100
        assert e.amount == 500
        print(f"✓ Exercise 2a passed: insufficient funds detected")
    
    # Test hierarchy
    try:
        raise InsufficientFundsError(100, 500)
    except BankingError:
        print(f"✓ Exercise 2b passed: hierarchy works correctly")

# =============================================================================
# Exercise 3: Exception with Context (★★★☆☆)
# =============================================================================
# TODO: Create exception that captures full context

class APIError(Exception):
    """Exception with API request context."""
    # TODO: Add status_code, url, response_body, timestamp attributes
    pass

# Test Cases
def test_api_error():
    from datetime import datetime
    
    try:
        raise APIError(
            status_code=404,
            url="https://api.example.com/users",
            response_body='{"error": "not found"}'
        )
    except APIError as e:
        assert e.status_code == 404
        assert e.url == "https://api.example.com/users"
        assert isinstance(e.timestamp, datetime)
        print(f"✓ Exercise 3 passed: API error with status {e.status_code}")

# =============================================================================
# Exercise 4: Exception Aggregator (★★★★☆)
# =============================================================================
# TODO: Create exception that collects multiple errors

class CompositeError(Exception):
    """Collects multiple validation errors."""
    # TODO: Add errors list attribute
    # TODO: Override __str__ to show all errors
    pass

# Test Cases
def test_composite_error():
    errors = [
        ValidationError("name", "required"),
        ValidationError("email", "invalid"),
        ValidationError("age", "must be positive")
    ]
    
    try:
        raise CompositeError(errors)
    except CompositeError as e:
        assert len(e.errors) == 3
        error_str = str(e)
        assert "name" in error_str
        assert "email" in error_str
        print(f"✓ Exercise 4 passed: aggregated {len(e.errors)} errors")

# =============================================================================
# Exercise 5: Retry Decorator with Exceptions (★★★★★)
# =============================================================================
# TODO: Create decorator that retries on specific exceptions

def retry_on_exception(max_retries=3, exceptions=(Exception,)):
    """Decorator that retries function on specified exceptions."""
    # TODO: Implement retry logic
    pass

# Test Cases
def test_retry_decorator():
    call_count = 0
    
    @retry_on_exception(max_retries=3, exceptions=(ValueError,))
    def flaky_function():
        nonlocal call_count
        call_count += 1
        if call_count < 3:
            raise ValueError("Temporary failure")
        return "success"
    
    result = flaky_function()
    assert result == "success"
    assert call_count == 3
    print(f"✓ Exercise 5 passed: function succeeded after {call_count} retries")

if __name__ == "__main__":
    print("Running Custom Exceptions Exercises...")
    print("=" * 50)
    test_validation_error()
    test_banking_exceptions()
    test_api_error()
    test_composite_error()
    test_retry_decorator()
    print("=" * 50)
    print("All tests passed!")
