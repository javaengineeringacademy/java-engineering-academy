"""
Module 08: File I/O - Pathlib Exercises
=======================================
Practice with Python's pathlib module for modern path handling.
"""

from pathlib import Path

# =============================================================================
# Exercise 1: Path Information Extractor (★☆☆☆☆)
# =============================================================================
# TODO: Complete the function to extract path components

def get_path_info(path_str):
    """Return dict with path components: name, stem, suffix, parent, parts."""
    # TODO: Use Path to extract all components
    pass

# Test Cases
def test_path_info():
    result = get_path_info("/home/user/documents/report.pdf")
    assert result["name"] == "report.pdf"
    assert result["stem"] == "report"
    assert result["suffix"] == ".pdf"
    assert result["parent"] == "/home/user/documents"
    assert "home" in result["parts"]
    print(f"✓ Exercise 1 passed: extracted path info for {result['name']}")

# =============================================================================
# Exercise 2: Directory Tree Builder (★★☆☆☆)
# =============================================================================
# TODO: Create a directory tree structure using pathlib

def create_project_structure(base_dir, structure):
    """Create directory structure from dict.
    
    structure = {
        "src": ["__init__.py", "main.py"],
        "tests": ["test_main.py"],
        "docs": []
    }
    """
    # TODO: Create directories and files
    pass

# Test Cases
def test_project_structure():
    import tempfile
    import shutil
    
    base = Path(tempfile.mkdtemp())
    structure = {
        "src": ["__init__.py", "main.py"],
        "tests": ["test_main.py"],
        "docs": []
    }
    
    try:
        create_project_structure(base, structure)
        assert (base / "src" / "main.py").exists()
        assert (base / "tests" / "test_main.py").exists()
        assert (base / "docs").is_dir()
        print("✓ Exercise 2 passed: project structure created")
    finally:
        shutil.rmtree(base)

# =============================================================================
# Exercise 3: File Pattern Matcher (★★★☆☆)
# =============================================================================
# TODO: Find files matching multiple patterns

def find_files_by_patterns(directory, patterns):
    """Find files matching any of the given patterns.
    
    Returns dict mapping pattern to list of matching Paths.
    """
    # TODO: Implement glob-based file finding
    pass

# Test Cases
def test_pattern_matcher():
    import tempfile
    
    base = Path(tempfile.mkdtemp())
    (base / "test1.py").touch()
    (base / "test2.py").touch()
    (base / "data.csv").touch()
    (base / "readme.md").touch()
    
    try:
        result = find_files_by_patterns(base, ["*.py", "*.csv"])
        assert len(result["*.py"]) == 2
        assert len(result["*.csv"]) == 1
        print(f"✓ Exercise 3 passed: found files for {len(result)} patterns")
    finally:
        import shutil
        shutil.rmtree(base)

# =============================================================================
# Exercise 4: Path Normalizer (★★★☆☆)
# =============================================================================
# TODO: Normalize messy paths and resolve relative references

def normalize_path(path_str, base_dir=None):
    """Normalize path: resolve .., remove ., handle ~ expansion.
    
    If base_dir provided, resolve relative paths against it.
    """
    # TODO: Implement path normalization
    pass

# Test Cases
def test_path_normalizer():
    result = normalize_path("/home/user/../user/./docs/file.txt")
    assert result == "/home/user/docs/file.txt"
    
    result2 = normalize_path("./src/main.py", "/project")
    assert result2 == "/project/src/main.py"
    print("✓ Exercise 4 passed: paths normalized correctly")

# =============================================================================
# Exercise 5: File Organizer (★★★★☆)
# =============================================================================
# TODO: Organize files by extension into subdirectories

def organize_files_by_extension(directory):
    """Move files into subdirectories based on their extension.
    
    Creates folders like: images/, documents/, code/, other/
    Returns dict mapping category to list of moved file names.
    """
    # TODO: Implement file organization
    pass

# Test Cases
def test_file_organizer():
    import tempfile
    import shutil
    
    base = Path(tempfile.mkdtemp())
    (base / "photo.jpg").touch()
    (base / "doc.pdf").touch()
    (base / "script.py").touch()
    (base / "unknown.xyz").touch()
    
    try:
        result = organize_files_by_extension(base)
        assert "images" in result
        assert "documents" in result
        assert "code" in result
        assert len(result["images"]) == 1
        print(f"✓ Exercise 5 passed: organized into {len(result)} categories")
    finally:
        shutil.rmtree(base)

if __name__ == "__main__":
    print("Running Pathlib Exercises...")
    print("=" * 50)
    test_path_info()
    test_project_structure()
    test_pattern_matcher()
    test_path_normalizer()
    test_file_organizer()
    print("=" * 50)
    print("All tests passed!")
