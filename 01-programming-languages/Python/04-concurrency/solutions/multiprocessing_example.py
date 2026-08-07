"""
Module 04: Concurrency - Multiprocessing Solutions
Practice multiprocessing concepts in Python.
"""

import multiprocessing
import os
import time
from multiprocessing import Pool, Value, Array


def simple_task(x):
    """Simple task that returns square of input."""
    return x * x


def cpu_intensive_task(n):
    """CPU-intensive task for testing parallel execution."""
    total = 0
    for i in range(n):
        total += i * i
    return total


def process_with_shared_memory(shared_array, index, value):
    """Process that modifies shared memory."""
    shared_array[index] = value


def worker_function(data):
    """Worker function for multiprocessing pool."""
    return {
        'pid': os.getpid(),
        'input': data,
        'result': data * 2
    }


def map_reduce_example(data):
    """Simple map-reduce example using multiprocessing."""
    with Pool(processes=4) as pool:
        # Map phase
        squared = pool.map(simple_task, data)

        # Reduce phase
        total = sum(squared)

    return total


class ProcessSafeCounter:
    """Process-safe counter using multiprocessing.Value."""

    def __init__(self, initial=0):
        self._value = Value('i', initial)
        self._lock = multiprocessing.Lock()

    def increment(self, amount=1):
        """Increment counter by amount. Process-safe."""
        with self._lock:
            self._value.value += amount

    def decrement(self, amount=1):
        """Decrement counter by amount. Process-safe."""
        with self._lock:
            self._value.value -= amount

    def get_value(self):
        """Get current counter value. Process-safe."""
        with self._lock:
            return self._value.value


class ProcessPool:
    """Simple process pool implementation."""

    def __init__(self, num_processes=None):
        self.num_processes = num_processes or multiprocessing.cpu_count()
        self.pool = None
        self.results = {}

    def start(self):
        """Start the process pool."""
        self.pool = Pool(processes=self.num_processes)

    def submit(self, func, *args, **kwargs):
        """Submit a task to the pool."""
        if self.pool is None:
            self.start()

        task_id = len(self.results)
        result = self.pool.apply_async(func, args, kwargs)
        self.results[task_id] = result
        return task_id

    def get_result(self, task_id, timeout=None):
        """Get result of a task."""
        return self.results[task_id].get(timeout=timeout)

    def shutdown(self):
        """Shutdown the process pool."""
        if self.pool:
            self.pool.close()
            self.pool.join()
            self.pool = None


def producer_consumer_multiprocessing():
    """Producer-Consumer pattern using multiprocessing queues."""
    import queue

    def producer(queue, producer_id, items):
        for item in items:
            queue.put(f"P{producer_id}-{item}")
            time.sleep(0.001)
        queue.put(None)

    def consumer(queue, results, lock):
        while True:
            item = queue.get()
            if item is None:
                break
            with lock:
                results.append(item)

    manager = multiprocessing.Manager()
    results = manager.list()
    q = manager.Queue()
    lock = manager.Lock()

    producers = []
    for i in range(2):
        p = multiprocessing.Process(
            target=producer,
            args=(q, i, range(5))
        )
        p.start()
        producers.append(p)

    consumers = []
    for i in range(2):
        c = multiprocessing.Process(
            target=consumer,
            args=(q, results, lock)
        )
        c.start()
        consumers.append(c)

    for p in producers:
        p.join()

    for _ in range(2):
        q.put(None)

    for c in consumers:
        c.join()

    return list(results)


if __name__ == "__main__":
    print("Testing Multiprocessing Solutions...")

    # Test simple task
    with Pool(processes=4) as pool:
        results = pool.map(simple_task, range(10))
    assert results == [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]

    # Test CPU-intensive task
    with Pool(processes=4) as pool:
        results = pool.map(cpu_intensive_task, [100, 200, 300])
    assert len(results) == 3

    # Test Map-Reduce
    data = [1, 2, 3, 4, 5]
    result = map_reduce_example(data)
    assert result == 55

    # Test ProcessSafeCounter
    counter = ProcessSafeCounter(0)

    def increment_counter(n):
        for _ in range(n):
            counter.increment()

    processes = []
    for _ in range(10):
        p = multiprocessing.Process(target=increment_counter, args=(1000,))
        processes.append(p)
        p.start()

    for p in processes:
        p.join()

    assert counter.get_value() == 10000

    # Test ProcessPool
    pool = ProcessPool(4)
    pool.start()

    task_ids = []
    for i in range(8):
        task_id = pool.submit(simple_task, i)
        task_ids.append(task_id)

    results = []
    for task_id in task_ids:
        results.append(pool.get_result(task_id))

    assert sorted(results) == [0, 1, 4, 9, 16, 25, 36, 49]
    pool.shutdown()

    print("All Multiprocessing solutions passed!")
