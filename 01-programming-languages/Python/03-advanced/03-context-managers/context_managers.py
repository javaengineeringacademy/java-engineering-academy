"""Context managers, with statement, and contextlib."""

# ── Basic Context Manager (class-based) ──────────────────────────────
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
        if exc_type:
            print(f"Exception occurred: {exc_val}")
        return False  # Don't suppress exceptions

with ManagedFile("test.txt", "w") as f:
    f.write("Hello, World!")

# ── contextlib.contextmanager ────────────────────────────────────────
from contextlib import contextmanager

@contextmanager
def managed_file(filename, mode):
    """Function-based context manager — cleaner syntax."""
    f = open(filename, mode)
    try:
        yield f
    finally:
        f.close()

with managed_file("test.txt", "w") as f:
    f.write("Using contextlib")

# ── Timer Context Manager ────────────────────────────────────────────
import time

@contextmanager
def timer(label=""):
    start = time.time()
    try:
        yield
    finally:
        elapsed = time.time() - start
        print(f"{label} Elapsed: {elapsed:.4f}s")

with timer("Sort"):
    sorted(range(1000000))

# ── Suppressing Exceptions ──────────────────────────────────────────
from contextlib import suppress

with suppress(FileNotFoundError):
    os.remove("nonexistent.txt")  # No error raised

# ── Redirect stdout ─────────────────────────────────────────────────
from contextlib import redirect_stdout
import io

f = io.StringIO()
with redirect_stdout(f):
    print("This goes to StringIO")

output = f.getvalue()
print(f"Captured: {output.strip()}")

# ── ExitStack — Dynamic Context Managers ────────────────────────────
from contextlib import ExitStack

def process_files(filenames):
    with ExitStack() as stack:
        files = [stack.enter_context(open(fn)) for fn in filenames]
        # All files auto-closed when block exits
        return [f.read() for f in files]

# ── Async Context Manager ───────────────────────────────────────────
import asyncio
from contextlib import asynccontextmanager

@asynccontextmanager
async def async_timer(label=""):
    start = time.time()
    try:
        yield
    finally:
        elapsed = time.time() - start
        print(f"{label} Async elapsed: {elapsed:.4f}s")

async def main():
    with open("test.txt", "w") as f:
        f.write("test content")
    print("Done")

asyncio.run(main())

# ── Practical Examples ──────────────────────────────────────────────
@contextmanager
def change_directory(path):
    """Temporarily change working directory."""
    import os
    old_dir = os.getcwd()
    os.chdir(path)
    try:
        yield
    finally:
        os.chdir(old_dir)

@contextmanager
def timer():
    """Measure execution time."""
    start = time.perf_counter()
    yield
    elapsed = time.perf_counter() - start
    print(f"Elapsed: {elapsed:.6f}s")

# ── Cleanup ──────────────────────────────────────────────────────────
import os
if os.path.exists("test.txt"):
    os.remove("test.txt")
