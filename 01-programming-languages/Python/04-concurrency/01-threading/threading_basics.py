"""Threading basics: Thread, Lock, Queue."""

import threading
import time
from queue import Queue

# ── Basic Thread ─────────────────────────────────────────────────────
def worker(name, delay):
    print(f"Worker {name} starting")
    time.sleep(delay)
    print(f"Worker {name} finished")

threads = []
for i in range(3):
    t = threading.Thread(target=worker, args=(f"T{i}", 2))
    threads.append(t)
    t.start()

for t in threads:
    t.join()  # Wait for all threads to complete

print("All threads done")

# ── Thread with Return Value ─────────────────────────────────────────
class ThreadResult:
    def __init__(self):
        self.result = None
        self.error = None

def worker_with_result(result, func, *args):
    try:
        result.result = func(*args)
    except Exception as e:
        result.error = e

def compute(x):
    time.sleep(1)
    return x * x

result = ThreadResult()
t = threading.Thread(target=worker_with_result, args=(result, compute, 5))
t.start()
t.join()

print(f"Result: {result.result}")  # 25

# ── Lock — Mutual Exclusion ──────────────────────────────────────────
counter = 0
lock = threading.Lock()

def increment():
    global counter
    for _ in range(100000):
        with lock:  # Acquire/release automatically
            counter += 1

threads = [threading.Thread(target=increment) for _ in range(5)]
for t in threads:
    t.start()
for t in threads:
    t.join()

print(f"Counter: {counter}")  # Should be 500000

# ── RLock — Reentrant Lock ───────────────────────────────────────────
rlock = threading.RLock()

def recursive_increment(n):
    with rlock:
        if n > 0:
            recursive_increment(n - 1)

# ── Event — Signal Between Threads ───────────────────────────────────
event = threading.Event()

def waiter():
    print("Waiting for event...")
    event.wait()  # Blocks until event is set
    print("Event received!")

def setter():
    time.sleep(2)
    print("Setting event")
    event.set()

threading.Thread(target=waiter).start()
threading.Thread(target=setter).start()

# ── Queue — Thread-Safe Producer/Consumer ────────────────────────────
task_queue = Queue()

def producer(q, num_items):
    for i in range(num_items):
        time.sleep(0.1)
        q.put(f"Item {i}")
        print(f"Produced: Item {i}")
    q.put(None)  # Sentinel value

def consumer(q):
    while True:
        item = q.get()
        if item is None:
            break
        print(f"Consumed: {item}")
        q.task_done()

# Start producer and consumer
threading.Thread(target=producer, args=(task_queue, 5)).start()
threading.Thread(target=consumer, args=(task_queue,)).start()

# ── ThreadPoolExecutor ──────────────────────────────────────────────
from concurrent.futures import ThreadPoolExecutor, as_completed

def fetch_url(url):
    time.sleep(1)  # Simulate network request
    return f"Data from {url}"

urls = ["http://example.com", "http://google.com", "http://github.com"]

with ThreadPoolExecutor(max_workers=3) as executor:
    futures = {executor.submit(fetch_url, url): url for url in urls}
    for future in as_completed(futures):
        url = futures[future]
        result = future.result()
        print(f"{url}: {result}")

# ── GIL — Global Interpreter Lock ───────────────────────────────────
# Only one thread executes Python bytecode at a time
# I/O-bound: threads help (releasing GIL during I/O)
# CPU-bound: use multiprocessing instead

# ── Thread Safety Patterns ──────────────────────────────────────────
class ThreadSafeCounter:
    def __init__(self):
        self._value = 0
        self._lock = threading.Lock()

    @property
    def value(self):
        with self._lock:
            return self._value

    def increment(self):
        with self._lock:
            self._value += 1

# ── Daemon Threads ──────────────────────────────────────────────────
def background_task():
    while True:
        print("Background tick")
        time.sleep(1)

daemon = threading.Thread(target=background_task, daemon=True)
daemon.start()  # Dies when main thread exits
