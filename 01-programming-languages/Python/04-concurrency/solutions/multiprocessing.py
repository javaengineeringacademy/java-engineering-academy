"""
Module 04 - Concurrency: Multiprocessing Solutions
Difficulty: Intermediate
"""

import multiprocessing
import time
from multiprocessing import Pool

# =============================================================================
# Exercise 1: Basic Multiprocessing - Solution
# =============================================================================
def worker(name, delay):
    """Worker function that simulates work."""
    for i in range(3):
        time.sleep(delay)
        print(f"{name}: {i}")

def run_processes():
    """Create 3 processes that run concurrently."""
    processes = []
    for i in range(3):
        p = multiprocessing.Process(target=worker, args=(f"Process-{i}", 0.5))
        processes.append(p)
        p.start()
    for p in processes:
        p.join()

if __name__ == "__main__":
    run_processes()


# =============================================================================
# Exercise 2: Process Pool - Solution
# =============================================================================
def cpu_task(n):
    """CPU-intensive task."""
    total = sum(i * i for i in range(n))
    return total

def parallel_compute(numbers, pool_size=4):
    """Compute in parallel using process pool."""
    with Pool(pool_size) as pool:
        results = pool.map(cpu_task, numbers)
    return results

if __name__ == "__main__":
    numbers = list(range(10))
    results = parallel_compute(numbers)
    print(results)


# =============================================================================
# Exercise 3: Shared State - Solution
# =============================================================================
def shared_counter_worker(counter, lock, n):
    """Worker that increments shared counter."""
    for _ in range(n):
        with lock:
            counter.value += 1

def array_worker(arr, index, value):
    """Worker that modifies shared array."""
    arr[index] = value

if __name__ == "__main__":
    counter = multiprocessing.Value('i', 0)
    lock = multiprocessing.Lock()
    processes = [
        multiprocessing.Process(target=shared_counter_worker, args=(counter, lock, 1000))
        for _ in range(4)
    ]
    for p in processes:
        p.start()
    for p in processes:
        p.join()
    print(f"Counter: {counter.value}")  # 4000


# =============================================================================
# Exercise 4: Inter-Process Communication - Solution
# =============================================================================
def mp_producer(queue, items):
    """Producer for multiprocessing."""
    for item in items:
        queue.put(item)
        print(f"Produced: {item}")
    queue.put(None)

def mp_consumer(queue):
    """Consumer for multiprocessing."""
    while True:
        item = queue.get()
        if item is None:
            break
        print(f"Consumed: {item}")

def pipe_sender(conn):
    """Send data through pipe."""
    for i in range(5):
        conn.send(f"Message {i}")
    conn.send(None)
    conn.close()

def pipe_receiver(conn):
    """Receive data from pipe."""
    while True:
        msg = conn.recv()
        if msg is None:
            break
        print(f"Received: {msg}")

if __name__ == "__main__":
    q = multiprocessing.Queue()
    producer = multiprocessing.Process(target=mp_producer, args=(q, range(10)))
    consumer = multiprocessing.Process(target=mp_consumer, args=(q,))
    producer.start()
    consumer.start()
    producer.join()
    consumer.join()


# =============================================================================
# Exercise 5: Process Pool with Callbacks - Solution
# =============================================================================
def compute_square(n):
    """Compute square of n."""
    time.sleep(0.1)
    return n * n

def log_result(result):
    """Callback to log result."""
    print(f"Result: {result}")

def log_error(error):
    """Callback for errors."""
    print(f"Error: {error}")

if __name__ == "__main__":
    with Pool(4) as pool:
        for n in range(10):
            pool.apply_async(compute_square, args=(n,), callback=log_result, error_callback=log_error)
        pool.close()
        pool.join()
