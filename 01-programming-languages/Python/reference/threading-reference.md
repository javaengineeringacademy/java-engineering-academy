# Python Threading Reference

## What is threading?

The threading module provides tools for working with threads. Threads are lightweight processes that share the same memory space, making them useful for I/O-bound tasks.

## Why does threading matter?

Understanding threading helps you:
- Perform concurrent I/O operations
- Keep applications responsive
- Handle multiple connections
- Implement background tasks

---

## 1. Basic Thread

```python
import threading

def worker():
    print("Worker thread")

# Create and start thread
t = threading.Thread(target=worker)
t.start()

# Wait for thread to finish
t.join()
```

---

## 2. Thread with Arguments

```python
import threading

def worker(name, delay):
    import time
    time.sleep(delay)
    print(f"Worker {name} done")

# Create threads with arguments
threads = []
for i in range(3):
    t = threading.Thread(target=worker, args=(f"Thread-{i}", i))
    threads.append(t)
    t.start()

# Wait for all threads
for t in threads:
    t.join()
```

---

## 3. Thread Subclassing

```python
import threading
import time

class MyThread(threading.Thread):
    def __init__(self, name):
        super().__init__()
        self.name = name
    
    def run(self):
        print(f"Thread {self.name} starting")
        time.sleep(2)
        print(f"Thread {self.name} finished")

# Create and start
t = MyThread("Worker")
t.start()
t.join()
```

---

## 4. Lock

```python
import threading

counter = 0
lock = threading.Lock()

def increment():
    global counter
    with lock:
        counter += 1

# Create threads
threads = []
for _ in range(10):
    t = threading.Thread(target=increment)
    threads.append(t)
    t.start()

# Wait for all
for t in threads:
    t.join()

print(counter)  # 10
```

---

## 5. RLock

```python
import threading

rlock = threading.RLock()

def outer():
    with rlock:
        print("Outer")
        inner()

def inner():
    with rlock:  # Same thread can acquire again
        print("Inner")

# This works (would deadlock with Lock)
outer()
```

---

## 6. Event

```python
import threading
import time

event = threading.Event()

def waiter():
    print("Waiting for event")
    event.wait()
    print("Event received")

def setter():
    time.sleep(2)
    print("Setting event")
    event.set()

# Create threads
t1 = threading.Thread(target=waiter)
t2 = threading.Thread(target=setter)

t1.start()
t2.start()

t1.join()
t2.join()
```

---

## 7. Condition

```python
import threading
import time

condition = threading.Condition()
items = []

def producer():
    with condition:
        items.append("item")
        print("Produced item")
        condition.notify()

def consumer():
    with condition:
        while not items:
            condition.wait()
        item = items.pop()
        print(f"Consumed {item}")

# Create threads
t1 = threading.Thread(target=producer)
t2 = threading.Thread(target=consumer)

t1.start()
t2.start()

t1.join()
t2.join()
```

---

## 8. Semaphore

```python
import threading
import time

semaphore = threading.Semaphore(2)  # Allow 2 concurrent

def worker(name):
    with semaphore:
        print(f"{name} started")
        time.sleep(2)
        print(f"{name} finished")

# Create threads
threads = []
for i in range(5):
    t = threading.Thread(target=worker, args=(f"Thread-{i}",))
    threads.append(t)
    t.start()

# Wait for all
for t in threads:
    t.join()
```

---

## 9. Timer

```python
import threading
import time

def delayed():
    print("Delayed function")

# Create timer
timer = threading.Timer(2, delayed)
timer.start()

# Cancel if needed
# timer.cancel()
```

---

## 10. Barrier

```python
import threading
import time

barrier = threading.Barrier(3)

def worker(name):
    print(f"{name} ready")
    barrier.wait()
    print(f"{name} passed barrier")

# Create threads
threads = []
for i in range(3):
    t = threading.Thread(target=worker, args=(f"Thread-{i}",))
    threads.append(t)
    t.start()

# Wait for all
for t in threads:
    t.join()
```

---

## One-Minute Revision Table

| Class | Description | Example |
|-------|-------------|---------|
| **Thread** | Create thread | `threading.Thread(target=func)` |
| **Lock** | Mutual exclusion | `threading.Lock()` |
| **RLock** | Reentrant lock | `threading.RLock()` |
| **Event** | Simple communication | `threading.Event()` |
| **Condition** | Complex communication | `threading.Condition()` |
| **Semaphore** | Limit concurrency | `threading.Semaphore(2)` |
| **Timer** | Delayed execution | `threading.Timer(delay, func)` |
| **Barrier** | Synchronize threads | `threading.Barrier(n)` |

---

## Common Mistakes

### 1. Forgetting to Join

```python
# WRONG
t = threading.Thread(target=worker)
t.start()
# Thread may still be running

# RIGHT
t = threading.Thread(target=worker)
t.start()
t.join()  # Wait for thread to finish
```

### 2. Race Conditions

```python
# WRONG
counter = 0
def increment():
    global counter
    counter += 1  # Not thread-safe

# RIGHT
counter = 0
lock = threading.Lock()
def increment():
    global counter
    with lock:
        counter += 1
```

### 3. Deadlock

```python
# WRONG
lock1 = threading.Lock()
lock2 = threading.Lock()

def thread1():
    with lock1:
        with lock2:  # May deadlock
            pass

def thread2():
    with lock2:
        with lock1:  # May deadlock
            pass

# RIGHT (consistent ordering)
def thread1():
    with lock1:
        with lock2:
            pass

def thread2():
    with lock1:
        with lock2:
            pass
```

---

## Production Notes

1. **Use threading for I/O-bound tasks** - Use multiprocessing for CPU-bound
2. **Use locks for shared state** - Prevent race conditions
3. **Use RLock for nested locking** - When same thread needs multiple locks
4. **Use Event for simple signaling** - Between threads
5. **Use Condition for complex signaling** - With shared state
6. **Use Semaphore to limit concurrency** - Connection pools, etc.
7. **Use Timer for delayed execution** - Schedulers
8. **Use Barrier for synchronization** - Wait for all threads
9. **Always join threads** - Ensure they complete
10. **Be careful with GIL** - Python's Global Interpreter Lock

---

## Further Reading

- Python documentation on threading module
- Python documentation on concurrent.futures
- Python GIL documentation
