"""
Module 04: Concurrency - Threading Solutions
Practice threading concepts in Python.
"""

import threading
import time
import queue
from concurrent.futures import ThreadPoolExecutor


class ThreadSafeCounter:
    """A thread-safe counter using locks."""

    def __init__(self, initial=0):
        self._value = initial
        self._lock = threading.Lock()

    def increment(self, amount=1):
        """Increment counter by amount. Thread-safe."""
        with self._lock:
            self._value += amount

    def decrement(self, amount=1):
        """Decrement counter by amount. Thread-safe."""
        with self._lock:
            self._value -= amount

    def get_value(self):
        """Get current counter value. Thread-safe."""
        with self._lock:
            return self._value


def thread_safe_operation(shared_list, operation, value):
    """Perform thread-safe operation on shared list."""
    lock = threading.Lock()
    with lock:
        if operation == "append":
            shared_list.append(value)
        elif operation == "remove":
            if value in shared_list:
                shared_list.remove(value)


class ProducerConsumer:
    """Producer-Consumer pattern using queues."""

    def __init__(self, max_size=10):
        self.queue = queue.Queue(maxsize=max_size)
        self.results = []
        self.lock = threading.Lock()

    def producer(self, producer_id, items):
        """Produce items."""
        for item in items:
            self.queue.put(f"P{producer_id}-{item}")
            time.sleep(0.001)

    def consumer(self, consumer_id):
        """Consume items until None is received."""
        while True:
            item = self.queue.get()
            if item is None:
                self.queue.task_done()
                break
            with self.lock:
                self.results.append(item)
            self.queue.task_done()

    def run(self, num_producers, num_consumers, items_per_producer):
        """Run producer-consumer simulation."""
        self.results = []

        # Start consumers
        consumer_threads = []
        for i in range(num_consumers):
            t = threading.Thread(target=self.consumer, args=(i,))
            t.start()
            consumer_threads.append(t)

        # Start producers
        producer_threads = []
        for i in range(num_producers):
            items = list(range(items_per_producer))
            t = threading.Thread(target=self.producer, args=(i, items))
            t.start()
            producer_threads.append(t)

        # Wait for producers to finish
        for t in producer_threads:
            t.join()

        # Wait for queue to be processed
        self.queue.join()

        # Signal consumers to stop
        for _ in range(num_consumers):
            self.queue.put(None)

        # Wait for consumers to finish
        for t in consumer_threads:
            t.join()

        return self.results


class ThreadPool:
    """Simple thread pool implementation."""

    def __init__(self, num_threads):
        self.num_threads = num_threads
        self.tasks = queue.Queue()
        self.workers = []
        self.results = {}
        self.lock = threading.Lock()
        self.task_counter = 0
        self.completed = threading.Event()

        for _ in range(num_threads):
            worker = threading.Thread(target=self._worker)
            worker.daemon = True
            worker.start()
            self.workers.append(worker)

    def _worker(self):
        while True:
            task_id, func, args, kwargs = self.tasks.get()
            if task_id is None:
                break
            try:
                result = func(*args, **kwargs)
                with self.lock:
                    self.results[task_id] = result
            except Exception as e:
                with self.lock:
                    self.results[task_id] = e
            finally:
                self.tasks.task_done()

    def submit(self, func, *args, **kwargs):
        """Submit a task to the pool."""
        with self.lock:
            task_id = self.task_counter
            self.task_counter += 1
        self.tasks.put((task_id, func, args, kwargs))
        return task_id

    def wait_completion(self):
        """Wait for all tasks to complete."""
        self.tasks.join()

    def shutdown(self):
        """Shutdown the thread pool."""
        for _ in range(self.num_threads):
            self.tasks.put((None, None, None, None))
        for worker in self.workers:
            worker.join()


class ReadWriteLock:
    """Reader-Writer lock allowing multiple readers or single writer."""

    def __init__(self):
        self.read_ready = threading.Condition(threading.Lock())
        self.readers = 0
        self.writer_active = False

    def acquire_read(self):
        """Acquire read lock."""
        with self.read_ready:
            while self.writer_active:
                self.read_ready.wait()
            self.readers += 1

    def release_read(self):
        """Release read lock."""
        with self.read_ready:
            self.readers -= 1
            if self.readers == 0:
                self.read_ready.notify_all()

    def acquire_write(self):
        """Acquire write lock."""
        with self.read_ready:
            while self.writer_active or self.readers > 0:
                self.read_ready.wait()
            self.writer_active = True

    def release_write(self):
        """Release write lock."""
        with self.read_ready:
            self.writer_active = False
            self.read_ready.notify_all()


def parallel_map(func, items, num_workers=None):
    """Apply function to items in parallel."""
    if num_workers is None:
        num_workers = min(len(items), 4)

    results = [None] * len(items)

    def worker(index, item):
        results[index] = func(item)

    with ThreadPoolExecutor(max_workers=num_workers) as executor:
        futures = []
        for i, item in enumerate(items):
            futures.append(executor.submit(worker, i, item))
        for future in futures:
            future.result()

    return results


def parallel_filter(func, items, num_workers=None):
    """Filter items in parallel."""
    if num_workers is None:
        num_workers = min(len(items), 4)

    results = []

    def worker(item):
        if func(item):
            return item
        return None

    with ThreadPoolExecutor(max_workers=num_workers) as executor:
        futures = [executor.submit(worker, item) for item in items]
        for future in futures:
            result = future.result()
            if result is not None:
                results.append(result)

    return results


if __name__ == "__main__":
    print("Testing Threading Solutions...")

    # Test ThreadSafeCounter
    counter = ThreadSafeCounter(0)

    def increment_counter(n):
        for _ in range(n):
            counter.increment()

    threads = [threading.Thread(target=increment_counter, args=(1000,)) for _ in range(10)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()

    assert counter.get_value() == 10000

    # Test Producer-Consumer
    pc = ProducerConsumer(max_size=5)
    result = pc.run(num_producers=2, num_consumers=2, items_per_producer=5)
    assert len(result) == 10

    # Test ThreadPool
    results = []
    pool = ThreadPool(4)

    def square(n):
        return n * n

    for i in range(8):
        pool.submit(lambda x: results.append(square(x)), i)

    pool.wait_completion()
    assert sorted(results) == [0, 1, 4, 9, 16, 25, 36, 49]
    pool.shutdown()

    # Test Parallel Map
    def slow_square(x):
        time.sleep(0.01)
        return x * x

    start = time.time()
    results = parallel_map(slow_square, range(10), num_workers=4)
    parallel_time = time.time() - start

    assert results == [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]

    print("All Threading solutions passed!")
