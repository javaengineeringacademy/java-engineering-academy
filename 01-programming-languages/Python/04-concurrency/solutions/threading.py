"""
Module 04 - Concurrency: Threading Solutions
Difficulty: Intermediate
"""

import threading
import time
from queue import Queue
from concurrent.futures import ThreadPoolExecutor

# =============================================================================
# Exercise 1: Basic Threading - Solution
# =============================================================================
def print_numbers(name, delay):
    """Print numbers with delay in a thread."""
    for i in range(5):
        time.sleep(delay)
        print(f"{name}: {i}")

def run_threads():
    """Create 3 threads that print numbers concurrently."""
    threads = []
    for i in range(3):
        t = threading.Thread(target=print_numbers, args=(f"Thread-{i}", 0.5))
        threads.append(t)
        t.start()
    for t in threads:
        t.join()

run_threads()


# =============================================================================
# Exercise 2: Thread Synchronization - Solution
# =============================================================================
class ThreadSafeCounter:
    """A counter that is safe to use from multiple threads."""

    def __init__(self):
        self._value = 0
        self._lock = threading.Lock()

    def increment(self):
        with self._lock:
            self._value += 1

    def decrement(self):
        with self._lock:
            self._value -= 1

    def get_value(self):
        with self._lock:
            return self._value

counter = ThreadSafeCounter()
def increment_counter(n):
    for _ in range(n):
        counter.increment()

threads = [threading.Thread(target=increment_counter, args=(1000,)) for _ in range(10)]
for t in threads:
    t.start()
for t in threads:
    t.join()
print(counter.get_value())  # 10000


# =============================================================================
# Exercise 3: Producer-Consumer Pattern - Solution
# =============================================================================
def producer(queue, items):
    """Produce items and put them in queue."""
    for item in items:
        queue.put(item)
        print(f"Produced: {item}")
        time.sleep(0.1)
    queue.put(None)  # Signal end

def consumer(queue):
    """Consume items from queue."""
    while True:
        item = queue.get()
        if item is None:
            break
        print(f"Consumed: {item}")
        queue.task_done()

q = Queue(maxsize=5)
producer_thread = threading.Thread(target=producer, args=(q, range(10)))
consumer_thread = threading.Thread(target=consumer, args=(q,))
producer_thread.start()
consumer_thread.start()
producer_thread.join()
consumer_thread.join()


# =============================================================================
# Exercise 4: Thread Pools - Solution
# =============================================================================
def process_item(item):
    """Process a single item (simulated work)."""
    time.sleep(0.1)
    return item * item

def parallel_process(items, max_workers=4):
    """Process items in parallel using thread pool."""
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        results = list(executor.map(process_item, items))
    return results

items = list(range(20))
results = parallel_process(items)
print(results)


# =============================================================================
# Exercise 5: Thread Communication - Solution
# =============================================================================
def sender(event):
    """Send signal using event."""
    time.sleep(1)
    print("Sender: Sending signal")
    event.set()

def receiver(event):
    """Wait for signal using event."""
    print("Receiver: Waiting for signal")
    event.wait()
    print("Receiver: Signal received!")

class SharedBuffer:
    """Thread-safe buffer using conditions."""

    def __init__(self, capacity):
        self.buffer = []
        self.capacity = capacity
        self.condition = threading.Condition()

    def put(self, item):
        with self.condition:
            while len(self.buffer) >= self.capacity:
                self.condition.wait()
            self.buffer.append(item)
            self.condition.notify()

    def get(self):
        with self.condition:
            while not self.buffer:
                self.condition.wait()
            item = self.buffer.pop(0)
            self.condition.notify()
            return item

event = threading.Event()
sender_thread = threading.Thread(target=sender, args=(event,))
receiver_thread = threading.Thread(target=receiver, args=(event,))
receiver_thread.start()
sender_thread.start()
sender_thread.join()
receiver_thread.join()
