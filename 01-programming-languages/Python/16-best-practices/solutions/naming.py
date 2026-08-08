"""
Module 16 - Best Practices: Naming Solutions
Complete solutions with explanations

PEP 8 Naming Conventions:
- Variables: snake_case
- Functions: snake_case
- Classes: PascalCase
- Constants: UPPER_SNAKE_CASE
- Modules: snake_case
- Packages: lowercase (no underscores preferred)
"""


# =============================================================================
# Exercise 1: Variable Naming - SOLUTION
# =============================================================================

def exercise_1_variable_naming():
    """
    Fix variable naming to follow PEP 8.
    """
    # Fixed variable names with descriptive names
    user_age = 10
    item_count = 20
    greeting_message = "hello"
    numbers_list = [1, 2, 3]
    
    # Single letters are acceptable in:
    # - Loop variables: for i in range(10)
    # - List comprehensions: [x for x in data]
    # - Mathematical formulas: y = mx + b
    
    # Example of acceptable single-letter usage
    results = [x * 2 for x in range(10)]
    
    return {
        'user_age': user_age,
        'item_count': item_count,
        'greeting_message': greeting_message,
        'results': results
    }


# =============================================================================
# Exercise 2: Function Naming - SOLUTION
# =============================================================================

def add_numbers(x, y):
    """Add two numbers and return the result."""
    return x + y


def fetch_user_data():
    """Fetch user data from storage."""
    return {"name": "John", "email": "john@example.com"}


def process_data(data):
    """Process input data and return result."""
    return data

# More examples of good function names
def calculate_total_price(quantity, unit_price):
    """Calculate total price from quantity and unit price."""
    return quantity * unit_price


def validate_email_address(email):
    """Validate if email address is properly formatted."""
    import re
    pattern = r'^[\w\.-]+@[\w\.-]+\.\w+$'
    return bool(re.match(pattern, email))


def exercise_2_function_naming():
    """
    Fix function naming to follow PEP 8.
    """
    # Test the functions
    assert add_numbers(2, 3) == 5
    assert "name" in fetch_user_data()
    assert calculate_total_price(5, 10) == 50
    assert validate_email_address("test@example.com") == True
    
    return {
        'add_numbers': add_numbers(2, 3),
        'fetch_user_data': fetch_user_data(),
        'validate_email': validate_email_address("test@example.com")
    }


# =============================================================================
# Exercise 3: Class Naming - SOLUTION
# =============================================================================

class UserManager:
    """Manages user operations."""
    
    def __init__(self):
        self.users = []
    
    def add_user(self, user):
        """Add a user to the manager."""
        self.users.append(user)


class DataProcessor:
    """Processes data transformations."""
    
    def process(self, data):
        """Process the given data."""
        return data


class XmlParserForHtmlContent:
    """Specialized parser for HTML content with XML syntax."""
    
    def parse(self, content):
        """Parse HTML content."""
        return content

# More examples
class ShoppingCart:
    """Represents a shopping cart."""
    pass


class DatabaseConnection:
    """Manages database connections."""
    pass


def exercise_3_class_naming():
    """
    Fix class naming to follow PEP 8.
    """
    # Test the classes
    manager = UserManager()
    processor = DataProcessor()
    parser = XmlParserForHtmlContent()
    
    return {
        'UserManager': type(UserManager).__name__,
        'DataProcessor': type(DataProcessor).__name__,
    }


# =============================================================================
# Exercise 4: Constant Naming - SOLUTION
# =============================================================================

# Constants use UPPER_SNAKE_CASE
MAX_RETRIES = 3
DEFAULT_TIMEOUT = 30
API_KEY = "secret_key_123"

# Grouped constants
class HTTPStatus:
    """HTTP status code constants."""
    OK = 200
    NOT_FOUND = 404
    SERVER_ERROR = 500


class DatabaseConfig:
    """Database configuration constants."""
    HOST = "localhost"
    PORT = 5432
    NAME = "mydb"


# Module-level constants
PI = 3.14159
EULER_NUMBER = 2.71828


def exercise_4_constant_naming():
    """
    Fix constant naming to follow PEP 8.
    """
    return {
        'MAX_RETRIES': MAX_RETRIES,
        'DEFAULT_TIMEOUT': DEFAULT_TIMEOUT,
        'HTTP_OK': HTTPStatus.OK,
        'DB_PORT': DatabaseConfig.PORT,
    }


# =============================================================================
# Exercise 5: Comprehensive Refactoring - SOLUTION
# =============================================================================

class UserManager:
    """Manages user operations and data."""
    
    def __init__(self, name, email):
        """Initialize UserManager with name and email."""
        self.name = name
        self.email = email
        self.is_active = True
    
    def get_user_data(self):
        """Return user data as dictionary."""
        return {
            "name": self.name,
            "email": self.email,
            "is_active": self.is_active
        }
    
    def set_active_status(self, status):
        """Set the active status of the user."""
        self.is_active = status


def calculate_total_price(items):
    """Calculate total price from list of items."""
    total = 0
    for item in items:
        total += item.get("price", 0)
    return total


# Additional examples
def process_user_input(user_input):
    """Process and validate user input."""
    cleaned = user_input.strip()
    return cleaned.lower()


class DataValidator:
    """Validates data against specified rules."""
    
    def __init__(self, rules):
        """Initialize with validation rules."""
        self.rules = rules
    
    def validate(self, data):
        """Validate data against rules."""
        return all(rule(data) for rule in self.rules)


# Constants
MAX_LOGIN_ATTEMPTS = 5
SESSION_TIMEOUT_MINUTES = 30


def exercise_5_comprehensive_refactoring():
    """
    Refactored code following all naming conventions.
    """
    # Test refactored code
    user = UserManager("John", "john@example.com")
    user_data = user.get_user_data()
    
    items = [{"price": 10}, {"price": 20}, {"price": 30}]
    total = calculate_total_price(items)
    
    validator = DataValidator([lambda x: len(x) > 0])
    is_valid = validator.validate("test")
    
    return {
        'user_data': user_data,
        'total_price': total,
        'is_valid': is_valid,
    }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 16 - Naming Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Variable Naming")
    result = exercise_1_variable_naming()
    assert result['user_age'] == 10
    assert len(result['results']) == 10
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Function Naming")
    result = exercise_2_function_naming()
    assert result['add_numbers'] == 5
    assert result['validate_email'] == True
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Class Naming")
    result = exercise_3_class_naming()
    assert 'UserManager' in result
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Constant Naming")
    result = exercise_4_constant_naming()
    assert result['MAX_RETRIES'] == 3
    assert result['HTTP_OK'] == 200
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Comprehensive Refactoring")
    result = exercise_5_comprehensive_refactoring()
    assert result['total_price'] == 60
    assert result['is_valid'] == True
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
