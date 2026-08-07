# Python os Module Reference

## What is the os Module?

The os module provides a portable way of using operating system dependent functionality. It allows you to interact with the file system, environment variables, and other OS features.

## Why does the os Module matter?

Understanding the os module helps you:
- Work with files and directories
- Access environment variables
- Run system commands
- Write cross-platform code

---

## 1. File and Directory Operations

### Creating and Removing

```python
import os

# Create directory
os.mkdir('new_dir')

# Create nested directories
os.makedirs('path/to/new_dir', exist_ok=True)

# Remove file
os.remove('file.txt')

# Remove empty directory
os.rmdir('new_dir')

# Remove nested directories
os.removedirs('path/to/new_dir')

# Rename
os.rename('old_name.txt', 'new_name.txt')
```

### Listing Directory Contents

```python
import os

# List directory
contents = os.listdir('.')
print(contents)

# List with details
for entry in os.scandir('.'):
    if entry.is_file():
        print(f"File: {entry.name}")
    elif entry.is_dir():
        print(f"Dir: {entry.name}")
```

---

## 2. Path Operations

```python
import os

# Join paths
path = os.path.join('path', 'to', 'file.txt')
print(path)  # path/to/file.txt

# Get absolute path
abs_path = os.path.abspath('file.txt')

# Get basename and dirname
print(os.path.basename('/path/to/file.txt'))  # file.txt
print(os.path.dirname('/path/to/file.txt'))   # /path/to

# Split path
print(os.path.split('/path/to/file.txt'))  # ('/path/to', 'file.txt')

# Split extension
print(os.path.splitext('file.txt'))  # ('file', '.txt')

# Check if path exists
print(os.path.exists('/path/to/file.txt'))

# Check path type
print(os.path.isfile('/path/to/file.txt'))
print(os.path.isdir('/path/to'))
print(os.path.islink('/path/to/link'))
```

---

## 3. Environment Variables

```python
import os

# Get environment variable
home = os.environ.get('HOME')
print(home)

# Set environment variable
os.environ['MY_VAR'] = 'my_value'

# Get with default
path = os.environ.get('PATH', '/usr/bin')
```

---

## 4. Current Working Directory

```python
import os

# Get current directory
cwd = os.getcwd()
print(cwd)

# Change directory
os.chdir('/path/to/directory')
```

---

## 5. File Permissions

```python
import os

# Check permissions
print(os.access('file.txt', os.R_OK))  # Read
print(os.access('file.txt', os.W_OK))  # Write
print(os.access('file.txt', os.X_OK))  # Execute

# Change permissions
os.chmod('file.txt', 0o755)

# Change owner (requires root)
os.chown('file.txt', uid=1000, gid=1000)
```

---

## 6. Running System Commands

```python
import os

# Run command
exit_code = os.system('ls -la')
print(exit_code)  # 0 if success

# Run command and get output
import subprocess
output = subprocess.check_output(['ls', '-la'])
print(output.decode())
```

---

## 7. Process Information

```python
import os

# Get current process ID
pid = os.getpid()
print(pid)

# Get parent process ID
ppid = os.getppid()
print(ppid)
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **mkdir** | Create directory | `os.mkdir('dir')` |
| **makedirs** | Create nested directories | `os.makedirs('path/to/dir')` |
| **remove** | Remove file | `os.remove('file.txt')` |
| **rmdir** | Remove empty directory | `os.rmdir('dir')` |
| **listdir** | List directory contents | `os.listdir('.')` |
| **scandir** | List directory with details | `os.scandir('.')` |
| **rename** | Rename file/directory | `os.rename('old', 'new')` |
| **getcwd** | Get current directory | `os.getcwd()` |
| **chdir** | Change directory | `os.chdir('path')` |
| **environ** | Environment variables | `os.environ.get('HOME')` |
| **path.join** | Join paths | `os.path.join('path', 'to', 'file')` |
| **path.exists** | Check if path exists | `os.path.exists('path')` |
| **path.isfile** | Check if path is file | `os.path.isfile('path')` |
| **path.isdir** | Check if path is directory | `os.path.isdir('path')` |
| **system** | Run system command | `os.system('ls')` |
| **getpid** | Get process ID | `os.getpid()` |

---

## Common Mistakes

### 1. Not Using `exist_ok`

```python
# WRONG
os.mkdir('dir')  # FileExistsError if exists

# RIGHT
os.mkdir('dir', exist_ok=True)
```

### 2. Not Using `os.path.join`

```python
# WRONG
path = 'path' + '/' + 'to' + '/' + 'file.txt'

# RIGHT
path = os.path.join('path', 'to', 'file.txt')
```

### 3. Not Using `with` for File Operations

```python
# WRONG
f = open('file.txt', 'r')
content = f.read()
f.close()

# RIGHT
with open('file.txt', 'r') as f:
    content = f.read()
```

---

## Production Notes

1. **Use `os.path.join` for paths** - Cross-platform
2. **Use `os.makedirs` with `exist_ok=True`** - Avoid FileExistsError
3. **Use `os.scandir` instead of `os.listdir`** - More efficient
4. **Use `pathlib` instead of `os.path`** - More modern and object-oriented
5. **Use `subprocess` instead of `os.system`** - More control and security
6. **Use `os.environ` for environment variables** - Cross-platform
7. **Use `os.access` for permission checks** - More reliable
8. **Use `os.chmod` for permission changes** - Be careful with security
9. **Use `os.getpid` for process identification** - Useful for logging
10. **Use `os.symlink` for symbolic links** - More flexible than hard links

---

## Further Reading

- Python documentation on os module
- Python documentation on os.path module
- Python documentation on pathlib module
- Python documentation on subprocess module
