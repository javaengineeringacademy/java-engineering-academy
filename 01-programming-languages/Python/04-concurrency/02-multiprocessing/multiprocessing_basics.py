"""Multiprocessing: Process, Pool, shared state."""

import multiprocessing
import time
from multiprocessing import Pool, Process, Value, Array, Queue

# ── Basic Process ────────────────────────────────────────────────────
def worker(name, delay):
    print(f"Process {name} starting (PID: {multiprocessing.current_process().pid})")
    time.sleep(delay)
    print(f"Process {name} finished")

if __name__ == "__main__":
    processes = []
    for i in range(3):
        p = Process(target=worker, args=(f"P{i}", 1))
        processes.append(p)
        p.start()

    for p in processes:
        p.join()

    print("All processes done")

# ── Pool — Map/Parallel Execution ───────────────────────────────────
def square(x):
    time.sleep(0.5)  # Simulate work
    return x ** 2

if __name__ == "__main__":
    with Pool(processes=4) as pool:
        results = pool.map(square, range(10))
        print(f"Map: {results}")

        # map_async — non-blocking
        async_result = pool.map_async(square, range(10))
        print(f"Async: {async_result.get()}")

        # starmap — multiple arguments
        def add(a, b):
            return a + b
        results = pool.starmap(add, [(1, 2), (3, 4), (5, 6)])
        print(f"Starmap: {results}")  # [3, 7, 11]

# ── Shared State ─────────────────────────────────────────────────────
def increment_counter(counter, n):
    for _ in range(n):
        with counter.get_lock():
            counter.value += 1

def increment_array(arr):
    for i in range(len(arr)):
        arr[i] += 1

if __name__ == "__main__":
    # Shared value
    counter = Value('i', 0)  # 'i' = int, initial value 0
    processes = [
        Process(target=increment_counter, args=(counter, 100000))
        for _ in range(4)
    ]
    for p in processes:
        p.start()
    for p in processes:
        p.join()
    print(f"Counter: {counter.value}")  # 400000

    # Shared array
    arr = Array('i', [1, 2, 3, 4, 5])
    processes = [
        Process(target=increment_array, args=(arr,))
        for _ in range(3)
    ]
    for p in processes:
        p.start()
    for p in processes:
        p.join()
    print(f"Array: {list(arr)}")

# ── Queue (Process-Safe) ────────────────────────────────────────────
def producer(q):
    for i in range(5):
        q.put(f"Item {i}")
    q.put(None)

def consumer(q):
    while True:
        item = q.get()
        if item is None:
            break
        print(f"Consumed: {item}")

if __name__ == "__main__":
    q = Queue()
    p1 = Process(target=producer, args=(q,))
    p2 = Process(target=consumer, args=(q,))
    p1.start()
    p2.start()
    p1.join()
    p2.join()

# ── ProcessPoolExecutor ─────────────────────────────────────────────
from concurrent.futures import ProcessPoolExecutor, as_completed

def heavy_computation(n):
    return sum(i**2 for i in range(n))

if __name__ == "__main__":
    with ProcessPoolExecutor(max_workers=4) as executor:
        futures = {
            executor.submit(heavy_computation, n): n
            for n in [10**6, 10**7, 10**8]
        }
        for future in as_completed(futures):
            n = futures[future]
            result = future.result()
            print(f"Computation({n}): {result}")

# ── Inter-Process Communication ──────────────────────────────────────
from multiprocessing import Pipe

def sender(conn):
    conn.send({"message": "Hello from child"})
    conn.close()

def receiver(conn):
    data = conn.recv()
    print(f"Received: {data}")

if __name__ == "__main__":
    parent_conn, child_conn = Pipe()
    p1 = Process(target=sender, args=(child_conn,))
    p2 = Process(target=receiver, args=(parent_conn,))
    p1.start()
    p2.start()
    p1.join()
    p2.join()
