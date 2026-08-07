"""
Python Concurrency - Threading Exercises
Complete each exercise by implementing the required function/class.
Run the test cases to verify your solution.
"""

import threading
import time
import queue
from concurrent.futures import ThreadPoolExecutor


# Exercise 1: Thread-Safe Counter (Easy)
# Implement thread-safe operations

class ThreadSafeCounter:
    """
    A thread-safe counter using locks.
    
    Requirements:
    - Thread-safe increment
    - Thread-safe decrement
    - Thread-safe get value
    """
    
    def __init__(self, initial=0):
        # TODO: Implement this method with a lock
        pass
    
    def increment(self, amount=1):
        """Increment counter by amount. Thread-safe."""
        # TODO: Implement this method
        pass
    
    def decrement(self, amount=1):
        """Decrement counter by amount. Thread-safe."""
        # TODO: Implement this method
        pass
    
    def get_value(self):
        """Get current counter value. Thread-safe."""
        # TODO: Implement this method
        pass


def thread_safe_operation(shared_list, operation, value):
    """
    Perform thread-safe operation on shared list.
    
    Args:
        shared_list: List to modify
        operation: "append" or "remove"
        value: Value to add/remove
    """
    # TODO: Implement this function using locks
    pass


# Exercise 2: Producer-Consumer (Medium)
# Implement producer-consumer pattern

class ProducerConsumer:
    """
    Producer-Consumer pattern using queues.
    
    Requirements:
    - Producers add items to queue
    - Consumers remove items from queue
    - Graceful shutdown
    """
    
    def __init__(self, max_size=10):
        # TODO: Implement this method
        pass
    
    def producer(self, producer_id, items):
        """
        Produce items.
        
        Args:
            producer_id: ID of producer
            items: List of items to produce
        """
        # TODO: Implement this method
        pass
    
    def consumer(self, consumer_id):
        """
        Consume items until None is received.
        
        Args:
            consumer_id: ID of consumer
        """
        # TODO: Implement this method
        pass
    
    def run(self, num_producers, num_consumers, items_per_producer):
        """
        Run producer-consumer simulation.
        
        Returns:
            List of consumed items in order
        """
        # TODO: Implement this method
        pass


# Exercise 3: Thread Pool (Medium)
# Implement simple thread pool

class ThreadPool:
    """
    Simple thread pool implementation.
    
    Requirements:
    - Fixed number of worker threads
    - Submit tasks
    - Wait for all tasks to complete
    """
    
    def __init__(self, num_threads):
        # TODO: Implement this method
        pass
    
    def submit(self, func, *args, **kwargs):
        """
        Submit a task to the pool.
        
        Returns:
            Task ID for tracking
        """
        # TODO: Implement this method
        pass
    
    def wait_completion(self):
        """Wait for all tasks to complete."""
        # TODO: Implement this method
        pass
    
    def shutdown(self):
        """Shutdown the thread pool."""
        # TODO: Implement this method
        pass


# Exercise 4: Reader-Writer Lock (Hard)
# Implement reader-writer synchronization

class ReadWriteLock:
    """
    Reader-Writer lock allowing multiple readers or single writer.
    
    Requirements:
    - Multiple readers can read simultaneously
    - Only one writer at a time
    - No readers while writing
    """
    
    def __init__(self):
        # TODO: Implement this method
        pass
    
    def acquire_read(self):
        """Acquire read lock."""
        # TODO: Implement this method
        pass
    
    def release_read(self):
        """Release read lock."""
        # TODO: Implement this method
        pass
    
    def acquire_write(self):
        """Acquire write lock."""
        # TODO: Implement this method
        pass
    
    def release_write(self):
        """Release write lock."""
        # TODO: Implement this method
        pass


# Exercise 5: Parallel Map (Medium)
# Parallel execution of function

def parallel_map(func, items, num_workers=None):
    """
    Apply function to items in parallel.
    
    Args:
        func: Function to apply
        items: List of items
        num_workers: Number of worker threads
    
    Returns:
        List of results in original order
    """
    # TODO: Implement this function using ThreadPoolExecutor
    pass


def parallel_filter(func, items, num_workers=None):
    """
    Filter items in parallel.
    
    Args:
        func: Filter function (returns bool)
        items: List of items
        num_workers: Number of worker threads
    
    Returns:
        List of items where func returns True
    """
    # TODO: Implement this function
    pass


# ==================== TEST CASES ====================

def test_exercises():
    print("Testing Exercise 1: Thread-Safe Counter")
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
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 2: Producer-Consumer")
    pc = ProducerConsumer(max_size=5)
    result = pc.run(num_producers=2, num_consumers=2, items_per_producer=5)
    assert len(result) == 10
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 3: Thread Pool")
    results = []
    pool = ThreadPool(4)
    
    def square(n):
        return n * n
    
    for i in range(8):
        pool.submit(lambda x: results.append(square(x)), i)
    
    pool.wait_completion()
    assert sorted(results) == [0, 1, 4, 9, 16, 25, 36, 49]
    pool.shutdown()
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 4: Reader-Writer Lock")
    rw_lock = ReadWriteLock()
    data = {"value": 0}
    readers_count = [0]
    
    def reader():
        rw_lock.acquire_read()
        readers_count[0] += 1
        _ = data["value"]
        readers_count[0] -= 1
        rw_lock.release_read()
    
    def writer():
        rw_lock.acquire_write()
        data["value"] += 1
        rw_lock.release_write()
    
    threads = [threading.Thread(target=reader) for _ in range(5)]
    threads += [threading.Thread(target=writer) for _ in range(3)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()
    
    assert data["value"] == 3
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 5: Parallel Map")
    def slow_square(x):
        time.sleep(0.01)
        return x * x
    
    start = time.time()
    results = parallel_map(slow_square, range(10), num_workers=4)
    parallel_time = time.time() - start
    
    assert results == [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
    assert parallel_time < 0.2  # Should be faster than serial
    print("  ✓ All tests passed!\n")

    print("All threading exercises passed!")


if __name__ == "__main__":
    test_exercises()
