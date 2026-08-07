# Module 08: File I/O

> "File I/O is where your code meets the real world. Do it right, or lose data."

---

## open() and File Modes

```python
# Basic open
f = open("data.txt", "r")    # Read
f = open("data.txt", "w")    # Write (overwrites!)
f = open("data.txt", "a")    # Append
f = open("data.txt", "r+")   # Read + Write
f = open("data.txt", "rb")   # Read binary
f = open("data.txt", "wb")   # Write binary

# ALWAYS close when done manually (don't do this)
f = open("data.txt", "r")
content = f.read()
f.close()  # Easy to forget on errors!
```

**Mode Reference:**

| Mode | Description | Creates File? | Truncates? |
|------|-------------|---------------|------------|
| `r` | Read only | No | No |
| `w` | Write only | Yes | Yes |
| `a` | Append | Yes | No |
| `r+` | Read + Write | No | No |
| `w+` | Write + Read | Yes | Yes |
| `a+` | Append + Read | Yes | No |
| `rb/wb/ab` | Binary mode | Same as above | Same as above |

**Warning:** `w` mode **silently destroys** existing content. This is the #1 file I/O bug.

---

## Context Managers (with statement)

```python
# ALWAYS use context managers — they guarantee cleanup
with open("data.txt", "r") as f:
    content = f.read()
# File is automatically closed, even on exceptions

# Read line by line (memory efficient)
with open("large_file.txt", "r") as f:
    for line in f:
        process(line)

# Read/write with binary
with open("image.png", "rb") as f:
    data = f.read()

# Write binary
with open("output.png", "wb") as f:
    f.write(image_bytes)
```

### Custom Context Manager

```python
from contextlib import contextmanager

@contextmanager
def managed_resource(name: str):
    """Custom context manager for resource management."""
    print(f"Acquiring {name}")
    resource = {"name": name, "status": "active"}
    try:
        yield resource
    except Exception as e:
        print(f"Error with {name}: {e}")
        resource["status"] = "error"
    finally:
        print(f"Releasing {name}")
        resource["status"] = "released"

with managed_resource("database") as db:
    print(f"Using {db['name']}")
# Acquiring database
# Using database
# Releasing database
```

### Async Context Manager

```python
import aiofiles

async def read_file(path: str) -> str:
    async with aiofiles.open(path, "r") as f:
        return await f.read()
```

---

## pathlib (Modern File Handling)

`pathlib` is the modern, object-oriented way to handle file paths.

```python
from pathlib import Path

# Create path objects
p = Path("data/file.txt")
p = Path.home() / "Documents" / "data.txt"
p = Path("/usr/local/bin/python")

# Path properties
print(p.name)      # "python"
print(p.stem)      # "python" (without extension)
print(p.suffix)    # "" (extension)
print(p.parent)    # "/usr/local/bin"
print(p.exists())  # True/False
print(p.is_file()) # True/False
print(p.is_dir())  # True/False

# Read/write operations
content = p.read_text()              # Read entire file
p.write_text("hello world")         # Write string
data = p.read_bytes()               # Read as bytes
p.write_bytes(b"binary data")       # Write bytes

# Directory operations
p.mkdir(parents=True, exist_ok=True)  # Create dirs recursively
p.rmdir()                              # Remove empty dir
list(p.glob("*.py"))                  # Find Python files
list(p.rglob("*.txt"))                # Recursive search

# Rename/move
p.rename("new_name.txt")
p.unlink()  # Delete file
```

### Path Operations

```python
from pathlib import Path

# Join paths (use / operator)
data_dir = Path("data")
file_path = data_dir / "input" / "file.txt"
# PosixPath('data/input/file.txt')

# Relative paths
root = Path("/usr/local")
relative = root.relative_to("/usr")
# PosixPath('local')

# Suffix changes
p = Path("file.txt")
new_p = p.with_suffix(".csv")
# PosixPath('file.csv')

# Parent traversal
p = Path("/a/b/c/d.txt")
p.parent      # PosixPath('/a/b/c')
p.parent.parent  # PosixPath('/a/b')
```

---

## Reading/Writing Files

### Reading

```python
from pathlib import Path

# Read entire file
content = Path("data.txt").read_text()

# Read line by line (memory efficient)
with open("large_file.txt", "r") as f:
    for line in f:
        process(line.strip())

# Read all lines into list
with open("data.txt", "r") as f:
    lines = f.readlines()  # Includes newline characters

# Read with encoding
with open("data.txt", "r", encoding="utf-8") as f:
    content = f.read()

# Read binary
with open("image.png", "rb") as f:
    data = f.read()
    # Process binary data
```

### Writing

```python
from pathlib import Path

# Write string (overwrites!)
Path("output.txt").write_text("Hello, World!")

# Write with encoding
with open("output.txt", "w", encoding="utf-8") as f:
    f.write("Line 1\n")
    f.write("Line 2\n")
    f.writelines(["Line 3\n", "Line 4\n"])

# Append mode
with open("log.txt", "a") as f:
    f.write("New log entry\n")

# Write binary
with open("output.bin", "wb") as f:
    f.write(b"\x00\x01\x02")

# Atomic write pattern (safe for production)
import tempfile
from pathlib import Path

def atomic_write(path: Path, content: str) -> None:
    """Write atomically — no partial writes on crash."""
    with tempfile.NamedTemporaryFile(
        mode="w", dir=path.parent, delete=False
    ) as tmp:
        tmp.write(content)
        tmp_path = Path(tmp.name)
    tmp_path.rename(path)

atomic_write(Path("data.json"), '{"key": "value"}')
```

---

## CSV, JSON, YAML Handling

### CSV

```python
import csv
from pathlib import Path

# Read CSV
with open("data.csv", "r", newline="") as f:
    reader = csv.DictReader(f)
    for row in reader:
        print(row["name"], row["age"])

# Write CSV
with open("output.csv", "w", newline="") as f:
    writer = csv.DictWriter(f, fieldnames=["name", "age"])
    writer.writeheader()
    writer.writerow({"name": "Alice", "age": 30})

# With pandas (recommended for complex CSV)
import pandas as pd
df = pd.read_csv("data.csv")
df.to_csv("output.csv", index=False)
```

### JSON

```python
import json
from pathlib import Path

# Read JSON
data = json.loads(Path("config.json").read_text())

# Write JSON
config = {"host": "localhost", "port": 8080}
Path("config.json").write_text(json.dumps(config, indent=2))

# Pretty print
print(json.dumps(data, indent=2, sort_keys=True))

# Handle errors
try:
    data = json.loads(invalid_json)
except json.JSONDecodeError as e:
    print(f"Invalid JSON: {e}")
```

### YAML

```python
import yaml
from pathlib import Path

# Read YAML
with open("config.yaml") as f:
    config = yaml.safe_load(f)

# Write YAML
config = {"database": {"host": "localhost", "port": 5432}}
with open("config.yaml", "w") as f:
    yaml.dump(config, f, default_flow_style=False)

# Multi-document YAML
with open("documents.yaml") as f:
    docs = list(yaml.safe_load_all(f))
```

---

## Async File I/O

```python
import asyncio
import aiofiles

async def async_read(path: str) -> str:
    async with aiofiles.open(path, "r") as f:
        return await f.read()

async def async_write(path: str, content: str) -> None:
    async with aiofiles.open(path, "w") as f:
        await f.write(content)

async def process_files(paths: list[str]) -> list[str]:
    """Read multiple files concurrently."""
    tasks = [async_read(path) for path in paths]
    return await asyncio.gather(*tasks)

# Run it
results = asyncio.run(process_files(["file1.txt", "file2.txt"]))
```

### When to Use Async I/O

- Reading many small files (network or disk)
- Web servers handling file uploads/downloads
- Background file processing
- Any I/O-bound operation that can be parallelized

### When NOT to Use Async I/O

- Single file operations (overhead not worth it)
- CPU-bound processing after reading
- Simple scripts (synchronous is clearer)

---

## When to Use Which Approach

| Scenario | Recommended | Why |
|----------|-------------|-----|
| Simple config file | `Path.read_text()` + `json.loads()` | Clean, modern |
| Large file processing | `open()` + line iteration | Memory efficient |
| Complex CSV/data | `pandas` | Powerful data manipulation |
| Binary files | `open("rb")` | Direct binary access |
| Many files concurrently | `aiofiles` | Parallel I/O |
| Production writes | Atomic write pattern | Prevents data corruption |
| Path manipulation | `pathlib.Path` | Object-oriented, cross-platform |

---

## Production Checklist

- [ ] **Use context managers** — Never forget to close files
- [ ] **Use pathlib** — Modern, cross-platform path handling
- [ ] **Handle encoding** — Always specify `encoding="utf-8"` for text
- [ ] **Atomic writes** — Use temp files + rename for critical data
- [ ] **Validate file existence** — Check `Path.exists()` before operations
- [ ] **Use appropriate buffer size** — Large files: larger buffer; small files: smaller
- [ ] **Handle binary vs text** — Use `rb`/`wb` for binary, `r`/`w` for text
- [ ] **Exception handling** — Catch `FileNotFoundError`, `PermissionError`, `IsADirectoryError`
- [ ] **Log file operations** — Track opens, closes, errors
- [ ] **Test file cleanup** — Use `tempfile` for test fixtures

```python
# Production pattern
import logging
from pathlib import Path

logger = logging.getLogger(__name__)

def read_config(path: Path) -> dict:
    """Read and parse config file with error handling."""
    if not path.exists():
        raise FileNotFoundError(f"Config not found: {path}")
    if not path.is_file():
        raise IsADirectoryError(f"Expected file: {path}")

    try:
        content = path.read_text(encoding="utf-8")
        return json.loads(content)
    except json.JSONDecodeError as e:
        logger.error(f"Invalid JSON in {path}: {e}")
        raise
```

---

## Maturity Levels

| Level | What It Looks Like | Indicators |
|-------|-------------------|------------|
| **Beginner** | `open()` without context manager | Resource leaks on exceptions |
| **Basic** | `with open()` | Correct cleanup, basic operations |
| **Intermediate** | `pathlib` + context managers | Modern, cross-platform code |
| **Advanced** | Async I/O + atomic writes | Production-ready, concurrent processing |
| **Expert** | Custom context managers + error recovery | Self-healing file operations |

### Progression Path

1. **Start:** Always use `with` statement
2. **Then:** Switch to `pathlib` for all path operations
3. **Then:** Add proper encoding and error handling
4. **Then:** Use async I/O for concurrent file operations
5. **Finally:** Implement atomic writes and self-healing patterns

---

## Common Myths

**Myth: "Text files don't need encoding specified"**
> Reality: The default encoding varies by platform. Always specify `encoding="utf-8"` to ensure portability.

**Myth: "pathlib is slower than os.path"**
> Reality: Negligible difference in most cases. The clarity and safety of pathlib far outweigh any micro-optimization.

**Myth: "Buffering always improves performance"**
> Reality: For small files, default buffering is fine. For large files, explicit buffering helps. For network I/O, buffering can be critical. Measure first.

**Myth: "readlines() is the standard way to read files"**
> Reality: Iterating directly over the file object is more memory efficient — it reads line by line without loading the entire file.

**Myth: "File operations are atomic"**
> Reality: They're NOT. A crash during write can corrupt data. Use atomic write patterns (temp file + rename) for critical data.

---

## One-Minute Revision

- **Always use context managers** — `with open(...)` guarantees cleanup
- **Use pathlib** — `Path("file.txt").read_text()` over `open("file.txt")`
- **Specify encoding** — `encoding="utf-8"` for all text operations
- **File modes matter** — `w` truncates silently, `a` appends, `r+` reads+writes
- **Read large files line by line** — Don't load entire file into memory
- **Atomic writes** — Temp file + rename for data safety
- **CSV:** Use `csv.DictReader` or `pandas` for complex data
- **JSON:** `json.loads()` / `json.dumps()` with `indent=2`
- **YAML:** Always use `yaml.safe_load()` (not `yaml.load()`)
- **Async I/O:** For concurrent file operations, use `aiofiles`
- **Error handling:** Catch `FileNotFoundError`, `PermissionError`, `JSONDecodeError`
