"""String methods, formatting, and manipulation."""

# ── String Basics ────────────────────────────────────────────────────
s1 = "Hello, World"
s2 = 'Single quotes work too'
s3 = """Triple quotes
span multiple
lines"""

# ── String Operations ────────────────────────────────────────────────
print("Hello" + " " + "World")   # Concatenation
print("Ha" * 3)                  # Repetition: "HaHaHa"
print(len("Hello"))              # Length: 5
print("l" in "Hello")            # Membership: True

# ── Common Methods ───────────────────────────────────────────────────
s = "  Hello, World!  "
print(s.strip())         # "Hello, World!" (remove whitespace)
print(s.lower())         # "  hello, world!  "
print(s.upper())         # "  HELLO, WORLD!  "
print(s.title())         # "  Hello, World!  "
print(s.startswith("  "))  # True
print(s.endswith("!  "))   # True
print(s.find("World"))     # 9 (index, -1 if not found)
print(s.replace("World", "Python"))  # "  Hello, Python!  "
print(s.split(", "))       # ['  Hello', 'World!  ']

# ── Splitting and Joining ────────────────────────────────────────────
csv_line = "Alice,30,Engineer"
parts = csv_line.split(",")        # ['Alice', '30', 'Engineer']
rejoined = " | ".join(parts)       # "Alice | 30 | Engineer"

# Multi-character separator
text = "one::two::three"
parts = text.split("::")           # ['one', 'two', 'three']

# Split from right
"one.two.three".rsplit(".", 1)    # ['one.two', 'three']

# ── String Formatting ────────────────────────────────────────────────
name, age, score = "Alice", 30, 95.5

# f-strings (Python 3.6+) — preferred
print(f"{name} is {age} years old")
print(f"{score:.2f}")              # 95.50 (format float)
print(f"{name:>10}")              # "     Alice" (right-align)
print(f"{'hello':^20}")           # "       hello       " (center)
print(f"{255:08b}")               # 11111111 (binary)
print(f"{1000000:,}")             # 1,000,000 (comma separator)

# .format() method
print("{} is {} years old".format(name, age))
print("{name} is {age} years old".format(name="Alice", age=30))

# % formatting (old style)
print("%s is %d years old" % (name, age))

# ── String Methods Continued ─────────────────────────────────────────
s = "Hello123"
print(s.isalnum())      # True (alphanumeric)
print(s.isalpha())      # False (contains digits)
print(s.isdigit())      # False (contains letters)
print(s.isnumeric())    # False
print(s.istitle())      # True
print(s.isupper())      # False

# ── Encoding ─────────────────────────────────────────────────────────
text = "Hello"
encoded = text.encode("utf-8")    # b'Hello' (bytes)
decoded = encoded.decode("utf-8") # "Hello" (str)

# ── Useful Patterns ──────────────────────────────────────────────────
# Check if string is palindrome
def is_palindrome(s):
    clean = s.lower().replace(" ", "")
    return clean == clean[::-1]

# Reverse a string
reversed_str = "hello"[::-1]  # "olleh"

# Count words
word_count = len("hello world foo".split())

# Title case without small words
def smart_title(s):
    small_words = {"a", "an", "the", "in", "on", "at", "for", "of"}
    words = s.split()
    return " ".join(
        w if w.lower() in small_words and i > 0 else w.title()
        for i, w in enumerate(words)
    )

# Remove all whitespace
no_spaces = "".join("hello world".split())

# Truncate with ellipsis
def truncate(s, max_len):
    return s[:max_len-1] + "..." if len(s) > max_len else s
