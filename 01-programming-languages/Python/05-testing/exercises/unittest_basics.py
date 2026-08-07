"""
Python Testing - Unittest Exercises
Complete each exercise by implementing the test class.
Run with: python -m pytest unittest_basics.py -v
"""

import unittest
from unittest.mock import Mock, patch, MagicMock
import math


# Functions to test

def add(a, b):
    """Add two numbers."""
    return a + b

def divide(a, b):
    """Divide two numbers. Raises ValueError for division by zero."""
    if b == 0:
        raise ValueError("Cannot divide by zero")
    return a / b

def is_palindrome(text):
    """Check if text is a palindrome."""
    cleaned = text.lower().replace(" ", "")
    return cleaned == cleaned[::-1]

def factorial(n):
    """Calculate factorial. Raises ValueError for negative numbers."""
    if n < 0:
        raise ValueError("Negative numbers not allowed")
    if n == 0:
        return 1
    return n * factorial(n - 1)

class StringFormatter:
    """A class with string formatting methods."""
    
    def __init__(self, text):
        self.text = text
    
    def capitalize_words(self):
        """Capitalize first letter of each word."""
        return " ".join(word.capitalize() for word in self.text.split())
    
    def truncate(self, max_length=100):
        """Truncate text to max_length with ellipsis."""
        if len(self.text) <= max_length:
            return self.text
        return self.text[:max_length-3] + "..."
    
    def word_count(self):
        """Count words in text."""
        return len(self.text.split())


class Database:
    """Simulated database class."""
    
    def __init__(self):
        self.data = {}
    
    def connect(self):
        """Connect to database."""
        return True
    
    def insert(self, key, value):
        """Insert key-value pair."""
        self.data[key] = value
    
    def get(self, key):
        """Get value by key. Raises KeyError if not found."""
        if key not in self.data:
            raise KeyError(f"Key '{key}' not found")
        return self.data[key]
    
    def delete(self, key):
        """Delete key. Raises KeyError if not found."""
        if key not in self.data:
            raise KeyError(f"Key '{key}' not found")
        del self.data[key]


# ==================== EXERCISE TEST CASES ====================

# Exercise 1: Calculator Tests (Easy)
class TestCalculator(unittest.TestCase):
    """
    Test basic calculator operations.
    
    Requirements:
    - Test add function
    - Test divide function
    - Test edge cases (zero, negative)
    """
    
    def test_add_positive_numbers(self):
        """Test adding positive numbers."""
        # TODO: Implement test
        pass
    
    def test_add_negative_numbers(self):
        """Test adding negative numbers."""
        # TODO: Implement test
        pass
    
    def test_divide_normal(self):
        """Test normal division."""
        # TODO: Implement test
        pass
    
    def test_divide_by_zero(self):
        """Test division by zero raises ValueError."""
        # TODO: Implement test
        pass
    
    def test_divide_negative(self):
        """Test division with negative numbers."""
        # TODO: Implement test
        pass


# Exercise 2: String Formatter Tests (Medium)
class TestStringFormatter(unittest.TestCase):
    """
    Test string formatting operations.
    
    Requirements:
    - Test capitalize_words with various inputs
    - Test truncate with edge cases
    - Test word_count
    """
    
    def setUp(self):
        """Set up test fixtures."""
        # TODO: Initialize StringFormatter instances
        pass
    
    def test_capitalize_words_normal(self):
        """Test capitalizing normal sentence."""
        # TODO: Implement test
        pass
    
    def test_capitalize_words_already_capitalized(self):
        """Test with already capitalized words."""
        # TODO: Implement test
        pass
    
    def test_truncate_short_text(self):
        """Test truncating text shorter than max_length."""
        # TODO: Implement test
        pass
    
    def test_truncate_long_text(self):
        """Test truncating text longer than max_length."""
        # TODO: Implement test
        pass
    
    def test_word_count_empty(self):
        """Test word count on empty string."""
        # TODO: Implement test
        pass
    
    def test_word_count_normal(self):
        """Test word count on normal text."""
        # TODO: Implement test
        pass


# Exercise 3: Mock Database (Medium)
class TestMockDatabase(unittest.TestCase):
    """
    Test database operations with mocking.
    
    Requirements:
    - Test basic CRUD operations
    - Mock external dependencies
    - Verify method calls
    """
    
    def setUp(self):
        """Set up test fixtures."""
        # TODO: Initialize database
        pass
    
    def test_insert_and_get(self):
        """Test inserting and retrieving data."""
        # TODO: Implement test
        pass
    
    def test_get_nonexistent_key(self):
        """Test getting nonexistent key raises KeyError."""
        # TODO: Implement test
        pass
    
    def test_delete_key(self):
        """Test deleting a key."""
        # TODO: Implement test
        pass
    
    def test_delete_nonexistent_key(self):
        """Test deleting nonexistent key raises KeyError."""
        # TODO: Implement test
        pass
    
    @patch('builtins.print')
    def test_connect_logs_message(self, mock_print):
        """Test that connect prints a message."""
        # TODO: Implement test
        pass


# Exercise 4: Exception Testing (Medium)
class TestExceptionHandling(unittest.TestCase):
    """
    Test exception handling.
    
    Requirements:
    - Test that correct exceptions are raised
    - Test exception messages
    - Test exception chaining
    """
    
    def test_factorial_negative_raises_valueerror(self):
        """Test factorial of negative raises ValueError."""
        # TODO: Implement test
        pass
    
    def test_factorial_zero_returns_one(self):
        """Test factorial of zero returns 1."""
        # TODO: Implement test
        pass
    
    def test_factorial_positive(self):
        """Test factorial of positive numbers."""
        # TODO: Implement test
        pass
    
    def test_divide_by_zero_message(self):
        """Test divide by zero has correct message."""
        # TODO: Implement test
        pass
    
    def test_multiple_exception_types(self):
        """Test handling different exception types."""
        # TODO: Implement test
        pass


# Exercise 5: Test Fixtures (Medium)
class TestWithFixtures(unittest.TestCase):
    """
    Test using setUp and tearDown.
    
    Requirements:
    - Proper setup and teardown
    - Test isolation
    - Resource cleanup
    """
    
    @classmethod
    def setUpClass(cls):
        """Set up class-level fixtures."""
        # TODO: Implement class-level setup
        pass
    
    @classmethod
    def tearDownClass(cls):
        """Clean up class-level fixtures."""
        # TODO: Implement class-level teardown
        pass
    
    def setUp(self):
        """Set up test fixtures."""
        # TODO: Implement per-test setup
        pass
    
    def tearDown(self):
        """Clean up test fixtures."""
        # TODO: Implement per-test cleanup
        pass
    
    def test_database_operations(self):
        """Test database operations with fixtures."""
        # TODO: Implement test
        pass
    
    def test_isolation(self):
        """Verify test isolation (setUp called each time)."""
        # TODO: Implement test
        pass


if __name__ == "__main__":
    unittest.main()
