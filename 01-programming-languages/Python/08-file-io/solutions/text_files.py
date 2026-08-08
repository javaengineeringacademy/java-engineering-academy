"""
Module 08 - File I/O: Text Files Solutions
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Reading Text Files - Solution
# =============================================================================
def read_file(filename):
    """Read entire file content."""
    with open(filename, 'r') as f:
        return f.read()

def read_lines(filename):
    """Read file line by line."""
    with open(filename, 'r') as f:
        return f.readlines()

def read_with_context(filename, num_lines=3):
    """Read file with context (first N and last N lines)."""
    with open(filename, 'r') as f:
        lines = f.readlines()
    first = lines[:num_lines]
    last = lines[-num_lines:] if len(lines) > num_lines else lines
    return {'first': first, 'last': last, 'total_lines': len(lines)}


# =============================================================================
# Exercise 2: Writing Text Files - Solution
# =============================================================================
def write_file(filename, content):
    """Write content to file."""
    with open(filename, 'w') as f:
        f.write(content)

def append_to_file(filename, content):
    """Append content to file."""
    with open(filename, 'a') as f:
        f.write(content)

def write_lines(filename, lines):
    """Write list of lines to file."""
    with open(filename, 'w') as f:
        for line in lines:
            f.write(line + '\n')

write_file("output.txt", "Hello, World!")
append_to_file("output.txt", "\nSecond line")
write_lines("lines.txt", ["Line 1", "Line 2", "Line 3"])


# =============================================================================
# Exercise 3: File Processing - Solution
# =============================================================================
def word_frequency(filename):
    """Count word frequency in file."""
    freq = {}
    with open(filename, 'r') as f:
        for line in f:
            for word in line.split():
                word = word.lower().strip('.,!?;:')
                freq[word] = freq.get(word, 0) + 1
    return freq

def find_longest_lines(filename, n=3):
    """Find N longest lines in file."""
    with open(filename, 'r') as f:
        lines = f.readlines()
    return sorted(lines, key=len, reverse=True)[:n]

def search_replace(filename, search, replace):
    """Search and replace text in file."""
    with open(filename, 'r') as f:
        content = f.read()
    content = content.replace(search, replace)
    with open(filename, 'w') as f:
        f.write(content)


# =============================================================================
# Exercise 4: CSV Processing - Solution
# =============================================================================
import csv

def read_csv(filename):
    """Read CSV file and return list of dictionaries."""
    with open(filename, 'r') as f:
        reader = csv.DictReader(f)
        return list(reader)

def write_csv(filename, headers, rows):
    """Write data to CSV file."""
    with open(filename, 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=headers)
        writer.writeheader()
        writer.writerows(rows)

def process_csv(filename, filter_func=None, transform_func=None):
    """Read, filter, and transform CSV data."""
    data = read_csv(filename)
    if filter_func:
        data = [row for row in data if filter_func(row)]
    if transform_func:
        data = [transform_func(row) for row in data]
    return data


# =============================================================================
# Exercise 5: Log File Processing - Solution
# =============================================================================
import re
from datetime import datetime

def parse_log_line(line):
    """Parse a log line into components."""
    pattern = r'\[(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\] (\w+): (.+)'
    match = re.match(pattern, line.strip())
    if match:
        return {
            'timestamp': datetime.strptime(match.group(1), '%Y-%m-%d %H:%M:%S'),
            'level': match.group(2),
            'message': match.group(3)
        }
    return None

def analyze_logs(filename):
    """Analyze log file for errors, warnings, etc."""
    counts = {'INFO': 0, 'WARNING': 0, 'ERROR': 0, 'DEBUG': 0}
    with open(filename, 'r') as f:
        for line in f:
            parsed = parse_log_line(line)
            if parsed:
                counts[parsed['level']] = counts.get(parsed['level'], 0) + 1
    return counts

def filter_logs(filename, level="ERROR"):
    """Filter log entries by level."""
    results = []
    with open(filename, 'r') as f:
        for line in f:
            parsed = parse_log_line(line)
            if parsed and parsed['level'] == level:
                results.append(parsed)
    return results
