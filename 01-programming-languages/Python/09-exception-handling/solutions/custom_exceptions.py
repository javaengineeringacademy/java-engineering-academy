"""
Module 09: Exception Handling - Custom Exceptions Solutions
Practice creating and using custom exception classes.
"""


class ValidationError(Exception):
    """Custom exception for validation errors."""
    def __init__(self, field_name, message):
        super().__init__(f"{field_name}: {message}")
        self.field = field_name
        self.message = message


class BankingError(Exception):
    """Base exception for banking errors."""
    pass


class InsufficientFundsError(BankingError):
    """Raised when account has insufficient funds."""
    def __init__(self, balance, amount):
        super().__init__(f"Insufficient funds: balance={balance}, requested={amount}")
        self.balance = balance
        self.amount = amount


class AccountLockedError(BankingError):
    """Raised when account is locked."""
    def __init__(self, lock_reason):
        super().__init__(f"Account locked: {lock_reason}")
        self.lock_reason = lock_reason


class APIError(Exception):
    """Exception with API request context."""
    def __init__(self, status_code, url, response_body):
        from datetime import datetime
        super().__init__(f"API Error {status_code}: {url}")
        self.status_code = status_code
        self.url = url
        self.response_body = response_body
        self.timestamp = datetime.now()


class CompositeError(Exception):
    """Collects multiple validation errors."""
    def __init__(self, errors):
        self.errors = errors
        super().__init__(self._format_errors())

    def _format_errors(self):
        return "; ".join(str(e) for e in self.errors)

    def __str__(self):
        return self._format_errors()


def retry_on_exception(max_retries=3, exceptions=(Exception,)):
    """Decorator that retries function on specified exceptions."""
    def decorator(func):
        def wrapper(*args, **kwargs):
            last_exception = None
            for attempt in range(max_retries):
                try:
                    return func(*args, **kwargs)
                except exceptions as e:
                    last_exception = e
                    if attempt < max_retries - 1:
                        continue
            raise last_exception
        return wrapper
    return decorator


if __name__ == "__main__":
    print("Testing Custom Exceptions Solutions...")

    # Test ValidationError
    try:
        raise ValidationError("email", "invalid format")
    except ValidationError as e:
        assert e.field == "email"
        assert e.message == "invalid format"
        print(f"✓ Exercise 1 passed: caught {e.field}: {e.message}")

    # Test banking exceptions
    try:
        raise InsufficientFundsError(100, 500)
    except InsufficientFundsError as e:
        assert e.balance == 100
        assert e.amount == 500
        print(f"✓ Exercise 2a passed: insufficient funds detected")

    try:
        raise InsufficientFundsError(100, 500)
    except BankingError:
        print(f"✓ Exercise 2b passed: hierarchy works correctly")

    # Test APIError
    try:
        raise APIError(
            status_code=404,
            url="https://api.example.com/users",
            response_body='{"error": "not found"}'
        )
    except APIError as e:
        assert e.status_code == 404
        assert e.url == "https://api.example.com/users"
        assert hasattr(e.timestamp, 'timestamp') or hasattr(e.timestamp, 'now')
        print(f"✓ Exercise 3 passed: API error with status {e.status_code}")

    # Test CompositeError
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

    # Test retry decorator
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

    print("All Custom Exceptions solutions passed!")
