"""
Module 08 - File I/O: Text Files Exercises
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Reading Text Files (Difficulty: Beginner)
# =============================================================================
# Read and process text files.

# TODO: Implement file reading
def read_file(filename):
    """Read entire file content."""
    pass

def read_lines(filename):
    """Read file line by line."""
    pass

def read_with_context(filename, num_lines=3):
    """Read file with context (first N and last N lines)."""
    pass

# Test cases
# content = read_file("sample.txt")
# print(content)
#
# lines = read_lines("sample.txt")
# for line in lines:
#     print(line.strip())


# =============================================================================
# Exercise 2: Writing Text Files (Difficulty: Beginner)
# =============================================================================
# Write content to text files.

# TODO: Implement file writing
def write_file(filename, content):
    """Write content to file."""
    pass

def append_to_file(filename, content):
    """Append content to file."""
    pass

def write_lines(filename, lines):
    """Write list of lines to file."""
    pass

# Test cases
# write_file("output.txt", "Hello, World!")
# append_to_file("output.txt", "\nSecond line")
# write_lines("lines.txt", ["Line 1", "Line 2", "Line 3"])


# =============================================================================
# Exercise 3: File Processing (Difficulty: Intermediate)
# =============================================================================
# Process text files for common tasks.

# TODO: Count word frequency
def word_frequency(filename):
    """Count word frequency in file."""
    pass

# TODO: Find longest lines
def find_longest_lines(filename, n=3):
    """Find N longest lines in file."""
    pass

# TODO: Search and replace
def search_replace(filename, search, replace):
    """Search and replace text in file."""
    pass

# Test cases
# freq = word_frequency("sample.txt")
# print(freq)
# longest = find_longest_lines("sample.txt")
# print(longest)


# =============================================================================
# Exercise 4: CSV Processing (Difficulty: Intermediate)
# =============================================================================
# Process CSV files.

# TODO: Read CSV
def read_csv(filename):
    """Read CSV file and return list of dictionaries."""
    pass

# TODO: Write CSV
def write_csv(filename, headers, rows):
    """Write data to CSV file."""
    pass

# TODO: Process CSV data
def process_csv(filename, filter_func=None, transform_func=None):
    """Read, filter, and transform CSV data."""
    pass

# Test cases
# data = read_csv("data.csv")
# print(data)


# =============================================================================
# Exercise 5: Log File Processing (Difficulty: Intermediate)
# =============================================================================
# Process log files.

# TODO: Parse log file
def parse_log_line(line):
    """Parse a log line into components."""
    pass

# TODO: Analyze logs
def analyze_logs(filename):
    """Analyze log file for errors, warnings, etc."""
    pass

# TODO: Filter logs
def filter_logs(filename, level="ERROR"):
    """Filter log entries by level."""
    pass

# Test cases
# analysis = analyze_logs("app.log")
# print(analysis)
# errors = filter_logs("app.log", "ERROR")
# print(errors)
