"""
Module 08: File I/O - File Operations Solutions
Practice with Python's built-in file operations.
"""

import tempfile
import os


def count_words_in_file(filepath):
    """Read a file and return the number of words."""
    with open(filepath, 'r') as f:
        content = f.read()
    return len(content.split())


def copy_file_progress(src, dst, chunk_size=1024):
    """Copy a file in chunks and return total bytes copied."""
    total_bytes = 0
    with open(src, 'rb') as source:
        with open(dst, 'wb') as dest:
            while True:
                chunk = source.read(chunk_size)
                if not chunk:
                    break
                dest.write(chunk)
                total_bytes += len(chunk)
    return total_bytes


def parse_log_file(filepath):
    """Parse log file and return list of (timestamp, level, message) tuples."""
    entries = []
    with open(filepath, 'r') as f:
        for line in f:
            line = line.strip()
            if line:
                parts = line.split(' ', 3)
                if len(parts) >= 4:
                    timestamp = f"{parts[0]} {parts[1]}"
                    level = parts[2]
                    message = parts[3]
                    entries.append((timestamp, level, message))
    return entries


def add_line_numbers(filepath):
    """Read file and return string with numbered lines."""
    result = []
    with open(filepath, 'r') as f:
        for i, line in enumerate(f, 1):
            result.append(f"{i}:{line.rstrip()}")
    return '\n'.join(result)


def byte_frequency(filepath):
    """Read binary file and return dict of byte values to counts."""
    freq = {}
    with open(filepath, 'rb') as f:
        while True:
            byte = f.read(1)
            if not byte:
                break
            byte_val = byte[0]
            freq[byte_val] = freq.get(byte_val, 0) + 1
    return freq


if __name__ == "__main__":
    print("Testing File Operations Solutions...")

    # Test count_words
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.txt') as f:
        f.write("Hello world\nThis is a test\nPython is great")
        temp_path = f.name

    try:
        result = count_words_in_file(temp_path)
        assert result == 7, f"Expected 7, got {result}"
        print(f"✓ Exercise 1 passed: {result} words")
    finally:
        os.unlink(temp_path)

    # Test copy_file
    src = tempfile.NamedTemporaryFile(delete=False, suffix='.txt')
    content = b"A" * 5000
    src.write(content)
    src.close()

    dst_path = src.name + ".copy"

    try:
        result = copy_file_progress(src.name, dst_path, chunk_size=1024)
        assert result == 5000, f"Expected 5000, got {result}"
        assert os.path.exists(dst_path), "Copy file not created"
        print(f"✓ Exercise 2 passed: {result} bytes copied")
    finally:
        os.unlink(src.name)
        if os.path.exists(dst_path):
            os.unlink(dst_path)

    # Test log_parser
    log_content = """2024-01-15 10:30:00 INFO Application started
2024-01-15 10:30:05 WARNING Disk space low
2024-01-15 10:30:10 ERROR Connection failed"""

    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.log') as f:
        f.write(log_content)
        temp_path = f.name

    try:
        result = parse_log_file(temp_path)
        assert len(result) == 3, f"Expected 3 entries, got {len(result)}"
        assert result[0][1] == "INFO"
        assert result[1][1] == "WARNING"
        assert result[2][1] == "ERROR"
        print(f"✓ Exercise 3 passed: parsed {len(result)} log entries")
    finally:
        os.unlink(temp_path)

    # Test line_numbers
    content = "alpha\nbeta\ngamma"
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.txt') as f:
        f.write(content)
        temp_path = f.name

    try:
        result = add_line_numbers(temp_path)
        lines = result.strip().split('\n')
        assert len(lines) == 3
        assert lines[0].startswith("1:")
        assert lines[2].startswith("3:")
        print(f"✓ Exercise 4 passed: numbered {len(lines)} lines")
    finally:
        os.unlink(temp_path)

    # Test byte_frequency
    content = b"aaaabbcc"
    with tempfile.NamedTemporaryFile(delete=False, suffix='.bin') as f:
        f.write(content)
        temp_path = f.name

    try:
        result = byte_frequency(temp_path)
        assert result[ord('a')] == 4
        assert result[ord('b')] == 2
        assert result[ord('c')] == 2
        print(f"✓ Exercise 5 passed: {len(result)} unique bytes")
    finally:
        os.unlink(temp_path)

    print("All File Operations solutions passed!")
