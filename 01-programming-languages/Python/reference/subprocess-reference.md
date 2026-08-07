# Python subprocess Reference

## What is subprocess?

The subprocess module allows you to spawn new processes, connect to their input/output/error pipes, and obtain their return codes. It's the recommended way to run external commands.

## Why does subprocess matter?

Understanding subprocess helps you:
- Run system commands from Python
- Capture command output
- Handle process errors
- Write more secure code

---

## 1. Basic Command

```python
import subprocess

# Run command
result = subprocess.run(['ls', '-la'], capture_output=True, text=True)
print(result.stdout)
print(result.returncode)
```

---

## 2. run() Function

```python
import subprocess

# Basic run
result = subprocess.run(['echo', 'Hello'], capture_output=True, text=True)
print(result.stdout)  # Hello

# With shell=True
result = subprocess.run('echo Hello', shell=True, capture_output=True, text=True)
print(result.stdout)  # Hello

# With timeout
try:
    result = subprocess.run(['sleep', '10'], timeout=5)
except subprocess.TimeoutExpired:
    print("Command timed out")

# With check
try:
    result = subprocess.run(['false'], check=True)
except subprocess.CalledProcessError as e:
    print(f"Command failed with return code {e.returncode}")
```

---

## 3. Popen

```python
import subprocess

# Basic Popen
process = subprocess.Popen(['ls', '-la'], stdout=subprocess.PIPE, text=True)
stdout, stderr = process.communicate()
print(stdout)

# With stdin
process = subprocess.Popen(['cat'], stdin=subprocess.PIPE, stdout=subprocess.PIPE, text=True)
stdout, stderr = process.communicate(input='Hello')
print(stdout)  # Hello

# Non-blocking
process = subprocess.Popen(['sleep', '10'])
print(process.poll())  # None (still running)
process.wait()  # Wait for completion
```

---

## 4. check_output

```python
import subprocess

# Basic check_output
output = subprocess.check_output(['echo', 'Hello'], text=True)
print(output)  # Hello

# With shell=True
output = subprocess.check_output('echo Hello', shell=True, text=True)
print(output)  # Hello

# With error handling
try:
    output = subprocess.check_output(['false'], stderr=subprocess.STDOUT)
except subprocess.CalledProcessError as e:
    print(f"Command failed: {e.output}")
```

---

## 5. Piping

```python
import subprocess

# Pipe output to another command
p1 = subprocess.Popen(['ls', '-la'], stdout=subprocess.PIPE)
p2 = subprocess.Popen(['grep', 'py'], stdin=p1.stdout, stdout=subprocess.PIPE, text=True)
p1.stdout.close()

output, _ = p2.communicate()
print(output)
```

---

## 6. Environment Variables

```python
import subprocess
import os

# Pass environment variables
env = os.environ.copy()
env['MY_VAR'] = 'my_value'

result = subprocess.run(['printenv', 'MY_VAR'], capture_output=True, text=True, env=env)
print(result.stdout)  # my_value
```

---

## 7. Working Directory

```python
import subprocess

# Run in different directory
result = subprocess.run(['pwd'], capture_output=True, text=True, cwd='/tmp')
print(result.stdout)  # /tmp
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **run** | Run command | `subprocess.run(['ls'])` |
| **Popen** | Create process | `subprocess.Popen(['ls'])` |
| **check_output** | Get output | `subprocess.check_output(['ls'])` |
| **check_call** | Run and check | `subprocess.check_call(['ls'])` |
| **call** | Run and return code | `subprocess.call(['ls'])` |

---

## Common Mistakes

### 1. Using shell=True with User Input

```python
# WRONG (security risk)
user_input = "file.txt; rm -rf /"
subprocess.run(f"cat {user_input}", shell=True)

# RIGHT
subprocess.run(["cat", user_input])
```

### 2. Not Capturing Output

```python
# WRONG
result = subprocess.run(['ls'])
print(result.stdout)  # None

# RIGHT
result = subprocess.run(['ls'], capture_output=True)
print(result.stdout.decode())
```

### 3. Not Handling Errors

```python
# WRONG
result = subprocess.run(['false'])

# RIGHT
result = subprocess.run(['false'], check=True)
# or
try:
    result = subprocess.run(['false'], check=True)
except subprocess.CalledProcessError as e:
    print(f"Command failed: {e.returncode}")
```

---

## Production Notes

1. **Use `run()` for simple commands** - Most common use case
2. **Use `Popen` for complex scenarios** - More control
3. **Avoid `shell=True`** - Security risk with user input
4. **Use `capture_output=True`** - To get stdout and stderr
5. **Use `text=True`** - For string output instead of bytes
6. **Use `check=True`** - To raise exception on error
7. **Use `timeout`** - Prevent hanging commands
8. **Use `cwd`** - To set working directory
9. **Use `env`** - To pass environment variables
10. **Always handle errors** - Check returncode or use CalledProcessError

---

## Further Reading

- Python documentation on subprocess module
- subprocess documentation
- Python documentation on os.system
