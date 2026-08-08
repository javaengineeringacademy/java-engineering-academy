"""
Module 01 - Fundamentals: Strings Exercises
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: String Basics (Difficulty: Beginner)
# =============================================================================
# Practice basic string operations.

# TODO: String manipulation
def manipulate_string(s):
    """Return a dictionary with string statistics."""
    pass

# TODO: Reverse words in sentence
def reverse_words(sentence):
    """Reverse the order of words in a sentence."""
    pass

# Test cases
# stats = manipulate_string("Hello, World!")
# print(stats)
# # Expected: {'length': 13, 'upper': 'HELLO, WORLD!', 'lower': 'hello, world!',
# #            'alpha_only': 'HelloWorld', 'words': ['Hello', 'World']}
# print(reverse_words("Hello World"))  # Expected: "World Hello"
# print(reverse_words("Python is great"))  # Expected: "great is Python"


# =============================================================================
# Exercise 2: String Formatting (Difficulty: Beginner)
# =============================================================================
# Practice different string formatting methods.

# TODO: Format a price
def format_price(amount, currency="USD"):
    """Format price with currency symbol and 2 decimal places."""
    pass

# TODO: Create a table
def create_table(headers, rows):
    """Create a formatted ASCII table."""
    pass

# Test cases
# print(format_price(1234.5))       # Expected: "$1,234.50"
# print(format_price(1234.5, "EUR"))  # Expected: "€1,234.50"
# table = create_table(["Name", "Age", "City"],
#                       [["Alice", 30, "NYC"], ["Bob", 25, "SF"]])
# print(table)
# # Expected:
# # Name    | Age | City
# # --------|-----|-----
# # Alice   | 30  | NYC
# # Bob     | 25  | SF


# =============================================================================
# Exercise 3: String Methods (Difficulty: Beginner)
# =============================================================================
# Use string methods for text processing.

# TODO: Validate email (simple)
def is_valid_email(email):
    """Check if email has basic valid format (contains @ and .)."""
    pass

# TODO: Extract information
def extract_info(text):
    """Extract emails and phone numbers from text."""
    pass

# Test cases
# print(is_valid_email("user@example.com"))  # Expected: True
# print(is_valid_email("invalid"))           # Expected: False
# text = "Contact me at test@email.com or call 555-1234"
# info = extract_info(text)
# print(info)  # Expected: {'emails': ['test@email.com'], 'phones': ['555-1234']}


# =============================================================================
# Exercise 4: String Slicing (Difficulty: Beginner)
# =============================================================================
# Practice string slicing operations.

# TODO: Get initials
def get_initials(full_name):
    """Return initials from full name."""
    pass

# TODO: Mask sensitive data
def mask_email(email):
    """Mask email: user@domain.com -> u***r@domain.com"""
    pass

# Test cases
# print(get_initials("John Doe"))          # Expected: "J.D."
# print(get_initials("Jane Marie Smith"))  # Expected: "J.M.S."
# print(mask_email("john.doe@example.com"))  # Expected: "j***e@example.com"
# print(mask_email("a@b.com"))             # Expected: "a@b.com"


# =============================================================================
# Exercise 5: String Encoding (Difficulty: Intermediate)
# =============================================================================
# Work with string encoding and decoding.

# TODO: Caesar cipher
def caesar_cipher(text, shift):
    """Encrypt text using Caesar cipher."""
    pass

# TODO: Simple hash
def simple_hash(text):
    """Create a simple hash of a string."""
    pass

# Test cases
# encrypted = caesar_cipher("Hello", 3)
# print(encrypted)  # Expected: "Khoor"
# decrypted = caesar_cipher("Khoor", -3)
# print(decrypted)  # Expected: "Hello"
# print(simple_hash("password"))  # Expected: a consistent hash value
