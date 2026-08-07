"""
Custom Exceptions in Python
Demonstrates creating and using custom exception classes
"""

# ============================================
# Basic Custom Exception
# ============================================

class AppError(Exception):
    """Base exception for application errors."""
    pass

class ValidationError(AppError):
    """Raised when validation fails."""
    pass

class DatabaseError(AppError):
    """Raised when database operation fails."""
    pass

# ============================================
# Custom Exception with Details
# ============================================

class InsufficientFundsError(Exception):
    """Raised when account has insufficient funds."""
    
    def __init__(self, balance: float, amount: float) -> None:
        self.balance = balance
        self.amount = amount
        self.deficit = amount - balance
        super().__init__(
            f"Insufficient funds: balance ${balance:.2f}, "
            f"requested ${amount:.2f}, deficit ${self.deficit:.2f}"
        )

class UserNotFoundError(Exception):
    """Raised when user is not found."""
    
    def __init__(self, user_id: int) -> None:
        self.user_id = user_id
        super().__init__(f"User with ID {user_id} not found")

# ============================================
# Exception Hierarchy
# ============================================

class ServiceError(Exception):
    """Base class for service errors."""
    pass

class APIError(ServiceError):
    """API-related errors."""
    pass

class AuthenticationError(APIError):
    """Authentication failed."""
    pass

class RateLimitError(APIError):
    """Rate limit exceeded."""
    pass

class NetworkError(ServiceError):
    """Network-related errors."""
    pass

# ============================================
# Custom Exception with Additional Context
# ============================================

class ValidationErrorWithContext(Exception):
    """Validation error with field details."""
    
    def __init__(self, errors: dict) -> None:
        self.errors = errors
        message = "Validation failed: " + "; ".join(
            f"{field}: {msg}" for field, msg in errors.items()
        )
        super().__init__(message)

# ============================================
# Using Custom Exceptions
# ============================================

def withdraw(balance: float, amount: float) -> float:
    """Withdraw money from account."""
    if amount > balance:
        raise InsufficientFundsError(balance, amount)
    return balance - amount

def find_user(users: dict, user_id: int) -> dict:
    """Find user by ID."""
    if user_id not in users:
        raise UserNotFoundError(user_id)
    return users[user_id]

def validate_email(email: str) -> None:
    """Validate email format."""
    if "@" not in email:
        raise ValidationErrorWithContext({"email": "Invalid format"})

def validate_age(age: int) -> None:
    """Validate age."""
    errors = {}
    if not isinstance(age, int):
        errors["age"] = "Must be integer"
    elif age < 0 or age > 150:
        errors["age"] = "Must be between 0 and 150"
    
    if errors:
        raise ValidationErrorWithContext(errors)

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Test InsufficientFundsError
    print("=== Testing InsufficientFundsError ===")
    try:
        balance = 100.0
        amount = 150.0
        print(f"Withdrawing ${amount} from balance ${balance}")
        new_balance = withdraw(balance, amount)
        print(f"New balance: ${new_balance}")
    except InsufficientFundsError as e:
        print(f"Error: {e}")
        print(f"  Balance: ${e.balance}")
        print(f"  Requested: ${e.amount}")
        print(f"  Deficit: ${e.deficit}")
    
    # Test UserNotFoundError
    print("\n=== Testing UserNotFoundError ===")
    users = {1: {"name": "Alice"}, 2: {"name": "Bob"}}
    try:
        user = find_user(users, 3)
        print(f"Found user: {user}")
    except UserNotFoundError as e:
        print(f"Error: {e}")
        print(f"  User ID: {e.user_id}")
    
    # Test ValidationErrorWithContext
    print("\n=== Testing ValidationErrorWithContext ===")
    try:
        validate_email("invalid-email")
    except ValidationErrorWithContext as e:
        print(f"Error: {e}")
        print(f"  Errors: {e.errors}")
    
    try:
        validate_age(-5)
    except ValidationErrorWithContext as e:
        print(f"Error: {e}")
        print(f"  Errors: {e.errors}")
    
    # Test exception hierarchy
    print("\n=== Testing Exception Hierarchy ===")
    try:
        raise RateLimitError("API rate limit exceeded")
    except APIError as e:
        print(f"Caught APIError: {e}")
    except ServiceError as e:
        print(f"Caught ServiceError: {e}")
    
    try:
        raise NetworkError("Connection timeout")
    except ServiceError as e:
        print(f"Caught ServiceError: {e}")
