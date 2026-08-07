# Module 08: File I/O — Quiz

## Multiple Choice Questions

### Q1. What is the default encoding when opening a file in text mode?
A) UTF-8  
B) ASCII  
C) Platform-dependent (system default)  
D) Latin-1  

**Answer: C**  
**Explanation:** In Python 3, `open()` in text mode uses the platform's default encoding unless you specify `encoding=`. On most modern systems this is UTF-8, but on Windows it could be cp1252. Always specify `encoding='utf-8'` for portability.

---

### Q2. What happens if you call `f.read()` twice on the same file handle?
A) It reads the file again from the beginning  
B) It returns an empty string  
C) It raises an error  
D) It appends the content  

**Answer: B**  
**Explanation:** After the first `read()`, the file pointer is at the end. The second `read()` finds nothing left and returns an empty string. Use `f.seek(0)` to reset the pointer.

---

### Q3. Which is the recommended way to open a file?
A) `f = open("data.txt")`  
B) `with open("data.txt") as f:`  
C) `f = file("data.txt")`  
D) `os.open("data.txt")`  

**Answer: B**  
**Explanation:** The `with` statement ensures the file is closed automatically, even if an exception occurs. Option A requires manual `f.close()` and risks resource leaks.

---

### Q4. What is the difference between `pathlib.Path` and `os.path`?
A) `pathlib` is faster  
B) `pathlib` returns string paths, `os.path` returns Path objects  
C) `pathlib` uses object-oriented paths, `os.path` uses string manipulation  
D) They are identical  

**Answer: C**  
**Explanation:** `pathlib.Path` represents paths as objects with methods like `.stem`, `.suffix`, `.parent`. `os.path` uses string functions like `os.path.basename()`. `pathlib` is more modern and readable.

---

### Q5. What does `open("file.txt", "rb")` return?
A) A text file handle  
B) A binary file handle  
C) A buffered file handle  
D) A raw file handle  

**Answer: B**  
**Explanation:** The `"rb"` mode opens the file in binary read mode. The `b` flag means binary — no encoding/decoding is applied, and data is returned as `bytes` instead of `str`.

---

## Code Output Questions

### Q6. What does this code print?
```python
with open("test.txt", "w") as f:
    f.write("Hello\nWorld\n")

with open("test.txt", "r") as f:
    lines = f.readlines()
    print(len(lines))
```

A) 1  
B) 2  
C) 3  
D) Error  

**Answer: B**  
**Explanation:** `readlines()` returns a list of lines including the newline characters. The file has two lines: `"Hello\n"` and `"World\n"`, so `len(lines)` is 2.

---

### Q7. What does this code print?
```python
from pathlib import Path

p = Path("/home/user/docs/report.txt")
print(p.suffix, p.stem, p.parent.name)
```

A) `.txt report docs`  
B) `txt report docs`  
C) `.txt report user`  
D) Error  

**Answer: A**  
**Explanation:** `p.suffix` returns `".txt"` (with dot), `p.stem` returns `"report"` (filename without extension), `p.parent.name` returns `"docs"` (parent directory name).

---

## Bug Finding Questions

### Q8. Find the bug in this code:
```python
def read_config(path):
    f = open(path)
    data = f.read()
    return data
```

A) `open()` needs `"r"` mode  
B) File is never closed — resource leak  
C) `read()` is wrong — should use `readlines()`  
D) No bug — this works fine  

**Answer: B**  
**Explanation:** The file handle `f` is never closed. Use `with open(path) as f:` to ensure proper cleanup. This is a classic resource leak.

---

### Q9. Find the bug in this code:
```python
def write_data(filename, lines):
    with open(filename, "w") as f:
        for line in lines:
            f.write(line)
```

A) `"w"` mode should be `"a"`  
B) Missing newline characters between lines  
C) `lines` should be a string  
D) No bug  

**Answer: B**  
**Explanation:** If `lines` is a list of strings without trailing newlines, the output will have all lines concatenated. Add `f.write(line + "\n")` or ensure each line ends with `"\n"`.

---

## Scenario Questions

### Q10. You need to process a 10GB log file on a machine with 2GB RAM. Which approach works?
A) `f.read()` and process all at once  
B) Read line by line with `for line in f:`  
C) Load into a list with `f.readlines()`  
D) Use `json.load()` to parse it  

**Answer: B**  
**Explanation:** Reading line by line uses constant memory regardless of file size. Options A and C load the entire file into memory. Option D is for JSON files and would also load everything.

---

## Architecture Questions

### Q11. You're building a file upload service. Which pattern prevents concurrent writes to the same file?
A) Use `"w"` mode  
B) Use file locking with `fcntl.flock()`  
C) Use `tempfile.NamedTemporaryFile()` and atomic rename  
D) Both B and C  

**Answer: D**  
**Explanation:** File locking prevents concurrent access. Atomic rename (write to temp, then rename) ensures readers never see partial writes. Production systems often use both.

---

### Q12. Design decision: When should you use `pathlib` vs `os.path` vs `open()`?
A) Always use `pathlib` — it's newest  
B) `pathlib` for complex path manipulation, `os.path` for simple operations, `open()` for file I/O  
C) They're interchangeable  
D) Use `os.path` for cross-platform, `pathlib` for same-platform  

**Answer: B**  
**Explanation:** Each tool has a sweet spot. `pathlib` excels at building and querying paths. `os.path` is fine for simple joins. `open()` is for actual I/O. Mixing them appropriately leads to cleaner code.
