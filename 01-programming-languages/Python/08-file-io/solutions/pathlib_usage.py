"""
Module 08 - File I/O: Pathlib Solutions
Difficulty: Beginner to Intermediate
"""

from pathlib import Path
import os
import time

# =============================================================================
# Exercise 1: Basic Path Operations - Solution
# =============================================================================
def create_path_components():
    """Create paths using different methods."""
    path1 = Path("documents") / "reports" / "quarterly.txt"
    path2 = Path.home() / "documents" / "file.txt"
    path3 = Path.cwd() / "data" / "file.csv"
    return path1, path2, path3

def get_path_info(path_str):
    """Get information about a path."""
    path = Path(path_str)
    return {
        'name': path.name,
        'stem': path.stem,
        'suffix': path.suffix,
        'parent': path.parent,
        'parts': path.parts,
        'absolute': path.is_absolute()
    }

path = Path("documents/reports/quarterly.txt")
print(path.name)      # "quarterly.txt"
print(path.stem)      # "quarterly"
print(path.suffix)    # ".txt"
print(path.parent)    # "documents/reports"


# =============================================================================
# Exercise 2: Path Navigation - Solution
# =============================================================================
def list_python_files(directory):
    """List all Python files in directory."""
    return list(Path(directory).glob("**/*.py"))

def find_files_by_extension(directory, extension):
    """Find files by extension."""
    return list(Path(directory).glob(f"**/*{extension}"))

def get_relative_path(file_path, base_path):
    """Get relative path from base."""
    return Path(file_path).relative_to(base_path)


# =============================================================================
# Exercise 3: Path Manipulation - Solution
# =============================================================================
def change_extension(path_str, new_ext):
    """Change file extension."""
    path = Path(path_str)
    return str(path.with_suffix(new_ext))

def add_suffix(path_str, suffix):
    """Add suffix to filename."""
    path = Path(path_str)
    return str(path.with_stem(path.stem + suffix))

def create_unique_path(path_str):
    """Create unique path by adding number if exists."""
    path = Path(path_str)
    if not path.exists():
        return path_str
    counter = 1
    while True:
        new_path = path.with_stem(f"{path.stem}_{counter}")
        if not new_path.exists():
            return str(new_path)
        counter += 1

print(change_extension("file.txt", ".md"))  # "file.md"
print(add_suffix("file.txt", "_backup"))  # "file_backup.txt"


# =============================================================================
# Exercise 4: File Operations with Pathlib - Solution
# =============================================================================
def read_with_pathlib(filename):
    """Read file using pathlib."""
    return Path(filename).read_text()

def write_with_pathlib(filename, content):
    """Write file using pathlib."""
    Path(filename).write_text(content)

def ensure_directory(path_str):
    """Ensure directory exists, create if not."""
    Path(path_str).mkdir(parents=True, exist_ok=True)

ensure_directory("output/new_dir")
write_with_pathlib("output/new_dir/test.txt", "Hello")
content = read_with_pathlib("output/new_dir/test.txt")
print(content)  # "Hello"


# =============================================================================
# Exercise 5: Path Globbing - Solution
# =============================================================================
def find_python_files(directory):
    """Find all .py files recursively."""
    return list(Path(directory).glob("**/*.py"))

def find_large_files(directory, min_size_mb=1):
    """Find files larger than specified size."""
    large_files = []
    min_size = min_size_mb * 1024 * 1024
    for path in Path(directory).rglob("*"):
        if path.is_file() and path.stat().st_size > min_size:
            large_files.append(path)
    return large_files

def find_recent_files(directory, days=7):
    """Find recently modified files."""
    recent_files = []
    cutoff_time = time.time() - (days * 24 * 3600)
    for path in Path(directory).rglob("*"):
        if path.is_file() and path.stat().st_mtime > cutoff_time:
            recent_files.append(path)
    return recent_files

py_files = find_python_files(".")
print(py_files)
