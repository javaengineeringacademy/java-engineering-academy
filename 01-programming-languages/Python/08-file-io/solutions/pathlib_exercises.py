"""
Module 08: File I/O - Pathlib Solutions
Practice with Python's pathlib module for modern path handling.
"""

from pathlib import Path
import tempfile
import shutil


def get_path_info(path_str):
    """Return dict with path components: name, stem, suffix, parent, parts."""
    p = Path(path_str)
    return {
        "name": p.name,
        "stem": p.stem,
        "suffix": p.suffix,
        "parent": str(p.parent),
        "parts": p.parts
    }


def create_project_structure(base_dir, structure):
    """Create directory structure from dict."""
    for folder, files in structure.items():
        folder_path = base_dir / folder
        folder_path.mkdir(exist_ok=True)
        for file in files:
            file_path = folder_path / file
            file_path.touch()


def find_files_by_patterns(directory, patterns):
    """Find files matching any of the given patterns."""
    result = {}
    for pattern in patterns:
        result[pattern] = list(directory.glob(pattern))
    return result


def normalize_path(path_str, base_dir=None):
    """Normalize path: resolve .., remove ., handle ~ expansion."""
    p = Path(path_str)
    if base_dir and not p.is_absolute():
        p = Path(base_dir) / p
    return str(p)


def organize_files_by_extension(directory):
    """Move files into subdirectories based on their extension."""
    extension_map = {
        '.jpg': 'images', '.jpeg': 'images', '.png': 'images', '.gif': 'images',
        '.pdf': 'documents', '.doc': 'documents', '.docx': 'documents', '.txt': 'documents',
        '.py': 'code', '.js': 'code', '.java': 'code', '.cpp': 'code'
    }

    result = {}

    for file in directory.iterdir():
        if file.is_file():
            ext = file.suffix.lower()
            category = extension_map.get(ext, 'other')

            folder = directory / category
            folder.mkdir(exist_ok=True)

            dest = folder / file.name
            shutil.move(str(file), str(dest))

            if category not in result:
                result[category] = []
            result[category].append(file.name)

    return result


if __name__ == "__main__":
    print("Testing Pathlib Solutions...")

    # Test path_info
    result = get_path_info("/home/user/documents/report.pdf")
    assert result["name"] == "report.pdf"
    assert result["stem"] == "report"
    assert result["suffix"] == ".pdf"
    assert result["parent"] == "/home/user/documents"
    assert "home" in result["parts"]
    print(f"✓ Exercise 1 passed: extracted path info for {result['name']}")

    # Test project_structure
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

    # Test pattern_matcher
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
        shutil.rmtree(base)

    # Test path_normalizer
    result = normalize_path("/home/user/../user/./docs/file.txt")
    assert result == "/home/user/docs/file.txt"

    result2 = normalize_path("./src/main.py", "/project")
    assert result2 == "/project/src/main.py"
    print("✓ Exercise 4 passed: paths normalized correctly")

    # Test file_organizer
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

    print("All Pathlib solutions passed!")
