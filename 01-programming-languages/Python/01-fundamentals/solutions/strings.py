"""
Module 01 - Fundamentals: Strings Solutions
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: String Basics - Solution
# =============================================================================
def manipulate_string(s):
    """Return a dictionary with string statistics."""
    import re
    return {
        'length': len(s),
        'upper': s.upper(),
        'lower': s.lower(),
        'alpha_only': ''.join(c for c in s if c.isalpha()),
        'words': re.findall(r'\b\w+\b', s)
    }

def reverse_words(sentence):
    """Reverse the order of words in a sentence."""
    return ' '.join(sentence.split()[::-1])

stats = manipulate_string("Hello, World!")
print(stats)
print(reverse_words("Hello World"))  # "World Hello"
print(reverse_words("Python is great"))  # "great is Python"


# =============================================================================
# Exercise 2: String Formatting - Solution
# =============================================================================
def format_price(amount, currency="USD"):
    """Format price with currency symbol and 2 decimal places."""
    symbols = {"USD": "$", "EUR": "€", "GBP": "£"}
    symbol = symbols.get(currency, currency)
    formatted = f"{amount:,.2f}"
    return f"{symbol}{formatted}"

def create_table(headers, rows):
    """Create a formatted ASCII table."""
    col_widths = [len(h) for h in headers]
    for row in rows:
        for i, cell in enumerate(row):
            col_widths[i] = max(col_widths[i], len(str(cell)))

    header_line = " | ".join(h.ljust(col_widths[i]) for i, h in enumerate(headers))
    separator = "-|-".join("-" * w for w in col_widths)

    lines = [header_line, separator]
    for row in rows:
        line = " | ".join(str(cell).ljust(col_widths[i]) for i, cell in enumerate(row))
        lines.append(line)
    return "\n".join(lines)

print(format_price(1234.5))       # "$1,234.50"
print(format_price(1234.5, "EUR"))  # "€1,234.50"
table = create_table(["Name", "Age", "City"],
                      [["Alice", 30, "NYC"], ["Bob", 25, "SF"]])
print(table)


# =============================================================================
# Exercise 3: String Methods - Solution
# =============================================================================
def is_valid_email(email):
    """Check if email has basic valid format."""
    return '@' in email and '.' in email.split('@')[-1]

def extract_info(text):
    """Extract emails and phone numbers from text."""
    import re
    emails = re.findall(r'[\w.+-]+@[\w-]+\.[\w.]+', text)
    phones = re.findall(r'\d{3}[-.]?\d{4}', text)
    return {'emails': emails, 'phones': phones}

print(is_valid_email("user@example.com"))  # True
print(is_valid_email("invalid"))           # False
text = "Contact me at test@email.com or call 555-1234"
info = extract_info(text)
print(info)  # {'emails': ['test@email.com'], 'phones': ['555-1234']}


# =============================================================================
# Exercise 4: String Slicing - Solution
# =============================================================================
def get_initials(full_name):
    """Return initials from full name."""
    names = full_name.split()
    return '.'.join(name[0].upper() for name in names) + '.'

def mask_email(email):
    """Mask email: user@domain.com -> u***r@domain.com"""
    user, domain = email.split('@')
    if len(user) <= 2:
        return email
    return f"{user[0]}***{user[-1]}@{domain}"

print(get_initials("John Doe"))          # "J.D."
print(get_initials("Jane Marie Smith"))  # "J.M.S."
print(mask_email("john.doe@example.com"))  # "j***e@example.com"
print(mask_email("a@b.com"))             # "a@b.com"


# =============================================================================
# Exercise 5: String Encoding - Solution
# =============================================================================
def caesar_cipher(text, shift):
    """Encrypt text using Caesar cipher."""
    result = []
    for char in text:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            shifted = (ord(char) - base + shift) % 26 + base
            result.append(chr(shifted))
        else:
            result.append(char)
    return ''.join(result)

def simple_hash(text):
    """Create a simple hash of a string."""
    hash_value = 0
    for i, char in enumerate(text):
        hash_value += ord(char) * (i + 1)
    return hash_value

encrypted = caesar_cipher("Hello", 3)
print(encrypted)  # "Khoor"
decrypted = caesar_cipher("Khoor", -3)
print(decrypted)  # "Hello"
print(simple_hash("password"))
