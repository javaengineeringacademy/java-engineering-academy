"""
Module 08: File I/O - File Operations Exercises
==============================================
Practice with Python's built-in file operations.
"""

# =============================================================================
# Exercise 1: Read and Count Words (★☆☆☆☆)
# =============================================================================
# TODO: Complete the function to read a file and return word count

def count_words_in_file(filepath):
    """Read a file and return the number of words."""
    # TODO: Open the file, read contents, count words
    pass

# Test Cases
def test_count_words():
    import tempfile
    import os
    
    # Create temp file
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.txt') as f:
        f.write("Hello world\nThis is a test\nPython is great")
        temp_path = f.name
    
    try:
        result = count_words_in_file(temp_path)
        assert result == 7, f"Expected 7, got {result}"
        print(f"✓ Exercise 1 passed: {result} words")
    finally:
        os.unlink(temp_path)

# =============================================================================
# Exercise 2: Copy File with Progress (★★☆☆☆)
# =============================================================================
# TODO: Implement file copy with size tracking

def copy_file_progress(src, dst, chunk_size=1024):
    """Copy a file in chunks and return total bytes copied."""
    # TODO: Implement chunked file copy
    pass

# Test Cases
def test_copy_file():
    import tempfile
    import os
    
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

# =============================================================================
# Exercise 3: Log File Parser (★★★☆☆)
# =============================================================================
# TODO: Parse a log file and extract timestamps and messages

def parse_log_file(filepath):
    """Parse log file and return list of (timestamp, level, message) tuples."""
    # TODO: Implement log parsing
    pass

# Test Cases
def test_log_parser():
    import tempfile
    import os
    
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

# =============================================================================
# Exercise 4: File Line Number Adder (★★★☆☆)
# =============================================================================
# TODO: Read a file and return content with line numbers prepended

def add_line_numbers(filepath):
    """Read file and return string with numbered lines."""
    # TODO: Implement line numbering
    pass

# Test Cases
def test_line_numbers():
    import tempfile
    import os
    
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

# =============================================================================
# Exercise 5: Binary File Stats (★★★★☆)
# =============================================================================
# TODO: Read a binary file and compute byte frequency distribution

def byte_frequency(filepath):
    """Read binary file and return dict of byte values to counts."""
    # TODO: Implement byte frequency analysis
    pass

# Test Cases
def test_byte_frequency():
    import tempfile
    import os
    
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

if __name__ == "__main__":
    print("Running File Operations Exercises...")
    print("=" * 50)
    test_count_words()
    test_copy_file()
    test_log_parser()
    test_line_numbers()
    test_byte_frequency()
    print("=" * 50)
    print("All tests passed!")
