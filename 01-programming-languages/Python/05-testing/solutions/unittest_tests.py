"""
Module 05 - Testing: Unittest Solutions
Difficulty: Intermediate
"""

import unittest
import os
import tempfile
from unittest.mock import Mock, patch, MagicMock

# =============================================================================
# Exercise 1: Basic Test Cases - Solution
# =============================================================================
class Calculator:
    """A simple calculator class."""

    def add(self, a, b):
        return a + b

    def subtract(self, a, b):
        return a - b

    def multiply(self, a, b):
        return a * b

    def divide(self, a, b):
        if b == 0:
            raise ValueError("Cannot divide by zero")
        return a / b

class TestCalculator(unittest.TestCase):
    """Test cases for Calculator class."""

    def setUp(self):
        """Set up test fixtures."""
        self.calc = Calculator()

    def test_add(self):
        """Test addition."""
        self.assertEqual(self.calc.add(2, 3), 5)
        self.assertEqual(self.calc.add(-1, 1), 0)
        self.assertEqual(self.calc.add(0, 0), 0)

    def test_subtract(self):
        """Test subtraction."""
        self.assertEqual(self.calc.subtract(5, 3), 2)
        self.assertEqual(self.calc.subtract(3, 5), -2)

    def test_multiply(self):
        """Test multiplication."""
        self.assertEqual(self.calc.multiply(4, 5), 20)
        self.assertEqual(self.calc.multiply(-2, 3), -6)
        self.assertEqual(self.calc.multiply(0, 100), 0)

    def test_divide(self):
        """Test division."""
        self.assertEqual(self.calc.divide(10, 2), 5)
        self.assertAlmostEqual(self.calc.divide(1, 3), 0.3333, places=4)

    def test_divide_by_zero(self):
        """Test division by zero raises error."""
        with self.assertRaises(ValueError):
            self.calc.divide(10, 0)

if __name__ == '__main__':
    unittest.main()


# =============================================================================
# Exercise 2: Test Fixtures - Solution
# =============================================================================
class FileProcessor:
    """A class that processes files."""

    def __init__(self, filename):
        self.filename = filename
        self.content = None

    def read(self):
        with open(self.filename, 'r') as f:
            self.content = f.read()
        return self.content

    def write(self, content):
        with open(self.filename, 'w') as f:
            f.write(content)
        self.content = content

    def process(self):
        if self.content is None:
            self.read()
        return self.content.upper()

class TestFileProcessor(unittest.TestCase):
    """Test cases for FileProcessor."""

    def setUp(self):
        """Create temporary file for testing."""
        self.temp_file = tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.txt')
        self.temp_file.write("hello world")
        self.temp_file.close()
        self.processor = FileProcessor(self.temp_file.name)

    def tearDown(self):
        """Clean up temporary file."""
        os.unlink(self.temp_file.name)

    def test_read(self):
        """Test file reading."""
        content = self.processor.read()
        self.assertEqual(content, "hello world")

    def test_write(self):
        """Test file writing."""
        self.processor.write("new content")
        self.assertEqual(self.processor.content, "new content")
        with open(self.temp_file.name, 'r') as f:
            self.assertEqual(f.read(), "new content")


# =============================================================================
# Exercise 3: Test Organization - Solution
# =============================================================================
class StringProcessor:
    """String processing utilities."""

    @staticmethod
    def reverse(s):
        return s[::-1]

    @staticmethod
    def capitalize_words(s):
        return ' '.join(word.capitalize() for word in s.split())

    @staticmethod
    def count_vowels(s):
        return sum(1 for c in s.lower() if c in 'aeiou')

class TestStringProcessorReverse(unittest.TestCase):
    """Tests for reverse functionality."""

    def test_reverse_empty(self):
        self.assertEqual(StringProcessor.reverse(""), "")

    def test_reverse_single_char(self):
        self.assertEqual(StringProcessor.reverse("a"), "a")

    def test_reverse_multiple_chars(self):
        self.assertEqual(StringProcessor.reverse("hello"), "olleh")

class TestStringProcessorCapitalize(unittest.TestCase):
    """Tests for capitalize functionality."""

    def test_capitalize_empty(self):
        self.assertEqual(StringProcessor.capitalize_words(""), "")

    def test_capitalize_words(self):
        self.assertEqual(StringProcessor.capitalize_words("hello world"), "Hello World")

def create_test_suite():
    """Create a test suite with all tests."""
    loader = unittest.TestLoader()
    suite = unittest.TestSuite()
    suite.addTests(loader.loadTestsFromTestCase(TestStringProcessorReverse))
    suite.addTests(loader.loadTestsFromTestCase(TestStringProcessorCapitalize))
    return suite


# =============================================================================
# Exercise 4: Mocking - Solution
# =============================================================================
class WeatherService:
    """Service that fetches weather data."""

    def __init__(self, api_client):
        self.api_client = api_client

    def get_temperature(self, city):
        response = self.api_client.get_temperature(city)
        if response is None:
            raise ValueError(f"No data for {city}")
        return response['temperature']

    def get_forecast(self, city, days):
        return self.api_client.get_forecast(city, days)

class TestWeatherService(unittest.TestCase):
    """Test WeatherService with mocked API client."""

    def setUp(self):
        self.mock_api = Mock()
        self.service = WeatherService(self.mock_api)

    def test_get_temperature(self):
        """Test temperature fetching."""
        self.mock_api.get_temperature.return_value = {'temperature': 72}
        temp = self.service.get_temperature("New York")
        self.assertEqual(temp, 72)
        self.mock_api.get_temperature.assert_called_once_with("New York")

    def test_api_failure(self):
        """Test handling of API failure."""
        self.mock_api.get_temperature.return_value = None
        with self.assertRaises(ValueError):
            self.service.get_temperature("Invalid City")


# =============================================================================
# Exercise 5: Parameterized Tests - Solution
# =============================================================================
class Validator:
    """Input validation utilities."""

    @staticmethod
    def is_valid_email(email):
        import re
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))

    @staticmethod
    def is_valid_password(password):
        if len(password) < 8:
            return False
        has_upper = any(c.isupper() for c in password)
        has_lower = any(c.islower() for c in password)
        has_digit = any(c.isdigit() for c in password)
        return has_upper and has_lower and has_digit

class TestValidator(unittest.TestCase):
    """Parameterized tests for Validator."""

    def test_valid_emails(self):
        """Test valid email addresses."""
        valid_emails = [
            "user@example.com",
            "test.email@domain.co.uk",
            "name+tag@example.org"
        ]
        for email in valid_emails:
            with self.subTest(email=email):
                self.assertTrue(Validator.is_valid_email(email))

    def test_invalid_emails(self):
        """Test invalid email addresses."""
        invalid_emails = [
            "invalid",
            "@domain.com",
            "user@",
            "user@.com"
        ]
        for email in invalid_emails:
            with self.subTest(email=email):
                self.assertFalse(Validator.is_valid_email(email))

    def test_password_strength(self):
        """Test password strength validation."""
        # Valid passwords
        self.assertTrue(Validator.is_valid_password("StrongPass1"))
        self.assertTrue(Validator.is_valid_password("MyP4ssw0rd"))

        # Invalid passwords
        self.assertFalse(Validator.is_valid_password("short"))
        self.assertFalse(Validator.is_valid_password("nouppercase1"))
        self.assertFalse(Validator.is_valid_password("NOLOWERCASE1"))
        self.assertFalse(Validator.is_valid_password("NoDigitsHere"))

if __name__ == '__main__':
    unittest.main()
