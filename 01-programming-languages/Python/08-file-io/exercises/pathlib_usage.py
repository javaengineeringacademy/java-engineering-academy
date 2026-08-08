"""
Module 08 - File I/O: Pathlib Exercises
Difficulty: Beginner to Intermediate
"""

from pathlib import Path

# =============================================================================
# Exercise 1: Basic Path Operations (Difficulty: Beginner)
# =============================================================================
# Use Path objects for file operations.

# TODO: Create and manipulate paths
def create_path_components():
    """Create paths using different methods."""
    pass

def get_path_info(path_str):
    """Get information about a path."""
    pass

# Test cases
# path = Path("documents/reports/quarterly.txt")
# print(path.name)      # Expected: "quarterly.txt"
# print(path.stem)      # Expected: "quarterly"
# print(path.suffix)    # Expected: ".txt"
# print(path.parent)    # Expected: "documents/reports"


# =============================================================================
# Exercise 2: Path Navigation (Difficulty: Beginner)
# =============================================================================
# Navigate directory structure.

# TODO: Implement path navigation
def list_python_files(directory):
    """List all Python files in directory."""
    pass

def find_files_by_extension(directory, extension):
    """Find files by extension."""
    pass

def get_relative_path(file_path, base_path):
    """Get relative path from base."""
    pass

# Test cases
# py_files = list_python_files(".")
# print(py_files)


# =============================================================================
# Exercise 3: Path Manipulation (Difficulty: Intermediate)
# =============================================================================
# Manipulate paths.

# TODO: Implement path manipulation
def change_extension(path_str, new_ext):
    """Change file extension."""
    pass

def add_suffix(path_str, suffix):
    """Add suffix to filename."""
    pass

def create_unique_path(path_str):
    """Create unique path by adding number if exists."""
    pass

# Test cases
# print(change_extension("file.txt", ".md"))  # Expected: "file.md"
# print(add_suffix("file.txt", "_backup"))  # Expected: "file_backup.txt"


# =============================================================================
# Exercise 4: File Operations with Pathlib (Difficulty: Intermediate)
# =============================================================================
# Use pathlib for file operations.

# TODO: Implement file operations
def read_with_pathlib(filename):
    """Read file using pathlib."""
    pass

def write_with_pathlib(filename, content):
    """Write file using pathlib."""
    pass

def ensure_directory(path_str):
    """Ensure directory exists, create if not."""
    pass

# Test cases
# ensure_directory("output/new_dir")
# write_with_pathlib("output/new_dir/test.txt", "Hello")
# content = read_with_pathlib("output/new_dir/test.txt")
# print(content)


# =============================================================================
# Exercise 5: Path Globbing (Difficulty: Intermediate)
# =============================================================================
# Use glob patterns with pathlib.

# TODO: Implement glob operations
def find_python_files(directory):
    """Find all .py files recursively."""
    pass

def find_large_files(directory, min_size_mb=1):
    """Find files larger than specified size."""
    pass

def find_recent_files(directory, days=7):
    """Find recently modified files."""
    pass

# Test cases
# py_files = find_python_files(".")
# print(py_files)
