"""File I/O, context managers, and path operations."""

# ── Writing Files ────────────────────────────────────────────────────
# 'w' mode — overwrites existing file
with open("output.txt", "w") as f:
    f.write("Hello, World!\n")
    f.write("Second line\n")

# 'a' mode — appends to file
with open("output.txt", "a") as f:
    f.write("Appended line\n")

# Write multiple lines
lines = ["Line 1\n", "Line 2\n", "Line 3\n"]
with open("output.txt", "w") as f:
    f.writelines(lines)

# ── Reading Files ────────────────────────────────────────────────────
# Read entire file
with open("output.txt", "r") as f:
    content = f.read()

# Read line by line
with open("output.txt", "r") as f:
    for line in f:
        print(line.strip())

# Read all lines into list
with open("output.txt", "r") as f:
    lines = f.readlines()

# ── File Modes ───────────────────────────────────────────────────────
# 'r'  — read (default)
# 'w'  — write (truncate)
# 'a'  — append
# 'x'  — exclusive create (fails if exists)
# 'b'  — binary mode (rb, wb)
# '+'  — read and write (r+, w+, a+)

# ── Binary Files ─────────────────────────────────────────────────────
with open("data.bin", "wb") as f:
    f.write(b"\x00\x01\x02\x03")

with open("data.bin", "rb") as f:
    data = f.read()
    print(data)  # b'\x00\x01\x02\x03'

# ── Path Operations (pathlib) ───────────────────────────────────────
from pathlib import Path

# Create paths
p = Path("folder") / "subfolder" / "file.txt"
home = Path.home()
cwd = Path.cwd()

# Path properties
print(p.name)      # "file.txt"
print(p.stem)      # "file"
print(p.suffix)    # ".txt"
print(p.parent)    # Path("folder/subfolder")

# Read/write shortcuts
p = Path("test.txt")
p.write_text("Hello from pathlib\n")
content = p.read_text()

# ── File/Directory Operations ────────────────────────────────────────
import os

# Check existence
print(os.path.exists("output.txt"))
print(os.path.isfile("output.txt"))
print(os.path.isdir("folder"))

# List directory
for item in os.listdir("."):
    print(item)

# Create/remove directories
os.makedirs("dir1/dir2", exist_ok=True)
os.rmdir("dir1/dir2")

# Using pathlib
p = Path("new_dir")
p.mkdir(parents=True, exist_ok=True)

# ── Temporary Files ──────────────────────────────────────────────────
import tempfile

# Temporary file
with tempfile.NamedTemporaryFile(mode="w", delete=False) as f:
    f.write("temp data")
    temp_path = f.name

# Temporary directory
with tempfile.TemporaryDirectory() as tmpdir:
    tmpdir_path = tmpdir  # Auto-cleaned up

# ── Context Manager Pattern ──────────────────────────────────────────
class ManagedFile:
    def __init__(self, filename, mode):
        self.filename = filename
        self.mode = mode

    def __enter__(self):
        self.file = open(self.filename, self.mode)
        return self.file

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.file:
            self.file.close()

# Usage
with ManagedFile("test.txt", "w") as f:
    f.write("Managed file content")

# ── CSV Reading ──────────────────────────────────────────────────────
import csv

# Write CSV
with open("data.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow(["Name", "Age"])
    writer.writerow(["Alice", 30])
    writer.writerow(["Bob", 25])

# Read CSV
with open("data.csv", "r") as f:
    reader = csv.reader(f)
    for row in reader:
        print(row)

# ── JSON Reading/Writing ────────────────────────────────────────────
import json

data = {"name": "Alice", "scores": [95, 87, 92]}

# Write JSON
with open("data.json", "w") as f:
    json.dump(data, f, indent=2)

# Read JSON
with open("data.json", "r") as f:
    loaded = json.load(f)

# ── Cleaning Up ──────────────────────────────────────────────────────
import os
for f in ["output.txt", "data.bin", "test.txt", "data.csv", "data.json", "temp_data"]:
    if os.path.exists(f):
        os.remove(f)
if os.path.exists("new_dir"):
    os.rmdir("new_dir")
