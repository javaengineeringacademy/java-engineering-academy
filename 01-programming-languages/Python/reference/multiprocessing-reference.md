# Python Multiprocessing Reference

## What is multiprocessing?

The multiprocessing module provides tools for creating and managing processes. Unlike threading, processes have their own memory space, making them suitable for CPU-bound tasks.

## Why does multiprocessing matter?

Understanding multiprocessing helps you:
- Utilize multiple CPU cores
- Avoid GIL limitations
- Perform CPU-bound tasks in parallel
- Isolate processes for reliability

---

## 1. Basic Process

```python
import multiprocessing

def worker():
    print("Worker process")

# Create and start process
p = multiprocessing.Process(target=worker)
p.start()

# Wait for process to finish
p.join()
```

---

## 2. Process with Arguments

```python
import multiprocessing

def worker(name, delay):
    import time
    time.sleep(delay)
    print(f"Worker {name} done")

# Create processes with arguments
processes = []
for i in range(3):
    p = multiprocessing.Process(target=worker, args=(f"Process-{i}", i))
    processes.append(p)
    p.start()

# Wait for all processes
for p in processes:
    p.join()
```

---

## 3. Process Pool

```python
import multiprocessing

def square(x):
    return x ** 2

# Create pool
with multiprocessing.Pool(4) as pool:
    results = pool.map(square, [1, 2, 3, 4, 5])
    print(results)  # [1, 4, 9, 16, 25]

# Map with multiple arguments
def power(base, exponent):
    return base ** exponent

with multiprocessing.Pool(4) as pool:
    results = pool.starmap(power, [(2, 3), (3, 4), (4, 5)])
    print(results)  # [8, 81, 1024]
```

---

## 4. Queue

```python
import multiprocessing

def producer(queue):
    for i in range(5):
        queue.put(i)
        print(f"Produced {i}")
    queue.put(None)  # Signal completion

def consumer(queue):
    while True:
        item = queue.get()
        if item is None:
            break
        print(f"Consumed {item}")

# Create queue
queue = multiprocessing.Queue()

# Create processes
p1 = multiprocessing.Process(target=producer, args=(queue,))
p2 = multiprocessing.Process(target=consumer, args=(queue,))

p1.start()
p2.start()

p1.join()
p2.join()
```

---

## 5. Pipe

```python
import multiprocessing

def sender(conn):
    conn.send("Hello from sender")
    conn.close()

def receiver(conn):
    msg = conn.recv()
    print(f"Received: {msg}")
    conn.close()

# Create pipe
parent_conn, child_conn = multiprocessing.Pipe()

# Create processes
p1 = multiprocessing.Process(target=sender, args=(parent_conn,))
p2 = multiprocessing.Process(target=receiver, args=(child_conn,))

p1.start()
p2.start()

p1.join()
p2.join()
```

---

## 6. Shared Memory

```python
import multiprocessing

def worker(shared_value):
    shared_value.value += 1

# Create shared value
shared_value = multiprocessing.Value('i', 0)  # 'i' for integer

# Create processes
processes = []
for _ in range(10):
    p = multiprocessing.Process(target=worker, args=(shared_value,))
    processes.append(p)
    p.start()

# Wait for all
for p in processes:
    p.join()

print(shared_value.value)  # 10
```

---

## 7. Manager

```python
import multiprocessing

def worker(shared_dict, key, value):
    shared_dict[key] = value

# Create manager
with multiprocessing.Manager() as manager:
    shared_dict = manager.dict()
    
    # Create processes
    processes = []
    for i in range(5):
        p = multiprocessing.Process(target=worker, args=(shared_dict, f"key-{i}", i))
        processes.append(p)
        p.start()
    
    # Wait for all
    for p in processes:
        p.join()
    
    print(dict(shared_dict))
```

---

## 8. Lock

```python
import multiprocessing

counter = 0
lock = multiprocessing.Lock()

def increment():
    global counter
    with lock:
        counter += 1

# Create processes
processes = []
for _ in range(10):
    p = multiprocessing.Process(target=increment)
    processes.append(p)
    p.start()

# Wait for all
for p in processes:
    p.join()

print(counter)  # 10
```

---

## One-Minute Revision Table

| Class | Description | Example |
|-------|-------------|---------|
| **Process** | Create process | `multiprocessing.Process(target=func)` |
| **Pool** | Process pool | `multiprocessing.Pool(4)` |
| **Queue** | Inter-process queue | `multiprocessing.Queue()` |
| **Pipe** | Two-way connection | `multiprocessing.Pipe()` |
| **Value** | Shared value | `multiprocessing.Value('i', 0)` |
| **Array** | Shared array | `multiprocessing.Array('i', 10)` |
| **Manager** | Shared objects | `multiprocessing.Manager()` |
| **Lock** | Mutual exclusion | `multiprocessing.Lock()` |

---

## Common Mistakes

### 1. Forgetting to Join Processes

```python
# WRONG
p = multiprocessing.Process(target=worker)
p.start()
# Process may still be running

# RIGHT
p = multiprocessing.Process(target=worker)
p.start()
p.join()  # Wait for process to finish
```

### 2. Not Using `if __name__ == '__main__'`

```python
# WRONG (may cause issues on Windows)
p = multiprocessing.Process(target=worker)
p.start()

# RIGHT
if __name__ == '__main__':
    p = multiprocessing.Process(target=worker)
    p.start()
    p.join()
```

### 3. Pickling Errors

```python
# WRONG (cannot pickle local function)
def main():
    def worker():
        print("Worker")
    p = multiprocessing.Process(target=worker)
    p.start()

# RIGHT (use global function)
def worker():
    print("Worker")

def main():
    p = multiprocessing.Process(target=worker)
    p.start()
    p.join()

if __name__ == '__main__':
    main()
```

---

## Production Notes

1. **Use multiprocessing for CPU-bound tasks** - Avoids GIL
2. **Use Pool for parallel maps** - Convenient for batch processing
3. **Use Queue for inter-process communication** - Safe and easy
4. **Use Pipe for two-way communication** - More efficient than Queue
5. **Use Value/Array for shared data** - Simple shared memory
6. **Use Manager for shared objects** - More flexible
7. **Use Lock for shared state** - Prevent race conditions
8. **Always use `if __name__ == '__main__'`** - Prevents issues on Windows
9. **Handle process errors** - Check exit codes
10. **Consider ProcessPoolExecutor** - From concurrent.futures

---

## Further Reading

- Python documentation on multiprocessing module
- Python documentation on concurrent.futures
- Python documentation on multiprocessing.shared_memory
