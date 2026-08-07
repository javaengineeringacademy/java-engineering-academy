"""
Pathlib Usage in Python
Demonstrates modern path handling with pathlib
"""

from pathlib import Path
import os

# ============================================
# Creating Path Objects
# ============================================

def create_paths() -> None:
    """Create various path objects."""
    # From string
    p1 = Path("/Users/pooja/Documents")
    
    # Using / operator
    p2 = Path("Users") / "pooja" / "Documents"
    
    # Current directory
    p3 = Path.cwd()
    
    # Home directory
    p4 = Path.home()
    
    print(f"String path: {p1}")
    print(f"Using / operator: {p2}")
    print(f"Current dir: {p3}")
    print(f"Home dir: {p4}")

# ============================================
# Path Properties
# ============================================

def show_path_properties(path: Path) -> None:
    """Display various properties of a path."""
    print(f"Path: {path}")
    print(f"  Name: {path.name}")
    print(f"  Stem: {path.stem}")
    print(f"  Suffix: {path.suffix}")
    print(f"  Parent: {path.parent}")
    print(f"  Parts: {path.parts}")
    print(f"  Absolute: {path.is_absolute()}")

# ============================================
# Path Operations
# ============================================

def path_operations() -> None:
    """Demonstrate path manipulation operations."""
    base = Path("/Users/pooja")
    
    # Join paths
    docs = base / "Documents"
    project = docs / "MyProject"
    
    # Resolve path
    current = Path(".")
    resolved = current.resolve()
    
    # Relative path
    target = Path("/Users/pooja/Documents/file.txt")
    try:
        relative = target.relative_to("/Users/pooja")
        print(f"Relative: {relative}")
    except ValueError as e:
        print(f"Cannot make relative: {e}")
    
    # Change suffix
    file = Path("document.txt")
    new_file = file.with_suffix(".md")
    print(f"Original: {file}")
    print(f"With new suffix: {new_file}")
    
    # Change name
    new_name = file.with_name("readme.txt")
    print(f"With new name: {new_name}")

# ============================================
# File Operations with Pathlib
# ============================================

def file_operations() -> None:
    """Create, read, and manipulate files using pathlib."""
    # Create test directory
    test_dir = Path("test_pathlib")
    test_dir.mkdir(exist_ok=True)
    
    # Create files
    file1 = test_dir / "file1.txt"
    file2 = test_dir / "file2.txt"
    file3 = test_dir / "file3.py"
    
    file1.write_text("Hello from file1!")
    file2.write_text("Hello from file2!")
    file3.write_text("print('Hello from Python file!')")
    
    # Read files
    print(f"file1 content: {file1.read_text()}")
    
    # List directory
    print(f"\nFiles in {test_dir}:")
    for item in sorted(test_dir.iterdir()):
        print(f"  {item.name}")
    
    # Find files by pattern
    print(f"\nText files: {[f.name for f in test_dir.glob('*.txt')]}")
    print(f"Python files: {[f.name for f in test_dir.glob('*.py')]}")
    
    # Get file info
    for file in [file1, file2, file3]:
        stat = file.stat()
        print(f"\n{file.name}:")
        print(f"  Size: {stat.st_size} bytes")
        print(f"  Created: {stat.st_ctime}")
    
    # Cleanup
    for file in test_dir.iterdir():
        file.unlink()
    test_dir.rmdir()

# ============================================
# Working with Glob Patterns
# ============================================

def glob_patterns() -> None:
    """Demonstrate glob pattern matching."""
    # Create test structure
    base = Path("test_glob")
    (base / "src").mkdir(parents=True, exist_ok=True)
    (base / "tests").mkdir(exist_ok=True)
    
    # Create files
    (base / "src" / "main.py").write_text("# main")
    (base / "src" / "utils.py").write_text("# utils")
    (base / "tests" / "test_main.py").write_text("# tests")
    (base / "README.md").write_text("# README")
    
    # Glob patterns
    print("All Python files:")
    for p in base.rglob("*.py"):
        print(f"  {p.relative_to(base)}")
    
    print("\nAll files:")
    for p in base.rglob("*"):
        if p.is_file():
            print(f"  {p.relative_to(base)}")
    
    # Cleanup
    for p in base.rglob("*"):
        if p.is_file():
            p.unlink()
    for p in sorted(base.rglob("*"), reverse=True):
        if p.is_dir():
            p.rmdir()

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Creating Paths ===")
    create_paths()
    
    print("\n=== Path Properties ===")
    show_path_properties(Path(__file__))
    
    print("\n=== Path Operations ===")
    path_operations()
    
    print("\n=== File Operations ===")
    file_operations()
    
    print("\n=== Glob Patterns ===")
    glob_patterns()
