"""
Text File Operations in Python
Demonstrates reading, writing, and manipulating text files
"""

import os
import tempfile

# ============================================
# Writing Text Files
# ============================================

def write_simple_file() -> None:
    """Write text to a file using write()."""
    with open("example.txt", "w") as f:
        f.write("Hello, World!\n")
        f.write("This is a text file.\n")
        f.write("It has multiple lines.\n")

def write_lines() -> None:
    """Write multiple lines using writelines()."""
    lines = [
        "Line 1\n",
        "Line 2\n",
        "Line 3\n",
        "Line 4\n"
    ]
    with open("lines.txt", "w") as f:
        f.writelines(lines)

def write_with_formatting() -> None:
    """Write formatted content to a file."""
    data = [
        ("Alice", 25, "alice@example.com"),
        ("Bob", 30, "bob@example.com"),
        ("Charlie", 35, "charlie@example.com")
    ]
    
    with open("users.txt", "w") as f:
        f.write("Name,Age,Email\n")
        for name, age, email in data:
            f.write(f"{name},{age},{email}\n")

# ============================================
# Reading Text Files
# ============================================

def read_entire_file() -> str:
    """Read entire file content."""
    with open("example.txt", "r") as f:
        return f.read()

def read_line_by_line() -> list:
    """Read file line by line."""
    lines = []
    with open("lines.txt", "r") as f:
        for line in f:
            lines.append(line.strip())
    return lines

def read_all_lines() -> list:
    """Read all lines into a list."""
    with open("lines.txt", "r") as f:
        return [line.strip() for line in f.readlines()]

# ============================================
# File Appending
# ============================================

def append_to_file() -> None:
    """Append content to existing file."""
    with open("example.txt", "a") as f:
        f.write("This line was appended.\n")

# ============================================
# File Context Manager
# ============================================

def process_file_content(filename: str) -> str:
    """Process file content with automatic cleanup."""
    with open(filename, "r") as f:
        content = f.read()
        # File is automatically closed even if error occurs
        return content.upper()

# ============================================
# File Information
# ============================================

def get_file_info(filename: str) -> dict:
    """Get information about a file."""
    stat = os.stat(filename)
    return {
        "size": stat.st_size,
        "exists": os.path.exists(filename),
        "is_file": os.path.isfile(filename),
        "readable": os.access(filename, os.R_OK),
        "writable": os.access(filename, os.W_OK)
    }

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Create files
    write_simple_file()
    write_lines()
    write_with_formatting()
    
    # Read files
    print("=== Reading Files ===")
    content = read_entire_file()
    print(f"File content:\n{content}")
    
    print("Line by line:")
    lines = read_line_by_line()
    for i, line in enumerate(lines, 1):
        print(f"  {i}: {line}")
    
    print("\nAll lines (list):")
    all_lines = read_all_lines()
    print(f"  {all_lines}")
    
    # Append
    append_to_file()
    print("\n=== After Appending ===")
    print(read_entire_file())
    
    # Process content
    print("\n=== Processed Content ===")
    upper_content = process_file_content("example.txt")
    print(upper_content[:100] + "...")
    
    # File info
    print("\n=== File Info ===")
    info = get_file_info("example.txt")
    for key, value in info.items():
        print(f"  {key}: {value}")
    
    # Cleanup
    for file in ["example.txt", "lines.txt", "users.txt"]:
        if os.path.exists(file):
            os.remove(file)
            print(f"\nCleaned up: {file}")
