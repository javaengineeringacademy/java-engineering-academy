"""
Module 04 - Concurrency: Threading Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Threading (Difficulty: Beginner)
# =============================================================================
# Create and manage threads.

import threading
import time

# TODO: Implement thread function
def print_numbers(name, delay):
    """Print numbers with delay in a thread."""
    pass

# TODO: Create and start threads
def run_threads():
    """Create 3 threads that print numbers concurrently."""
    pass

# Test cases
# run_threads()
# # Expected: Numbers printed concurrently (not sequentially)


# =============================================================================
# Exercise 2: Thread Synchronization (Difficulty: Intermediate)
# =============================================================================
# Use locks for thread-safe operations.

# TODO: Implement thread-safe counter
class ThreadSafeCounter:
    """A counter that is safe to use from multiple threads."""

    def __init__(self):
        pass

    def increment(self):
        pass

    def decrement(self):
        pass

    def get_value(self):
        pass

# Test cases
# counter = ThreadSafeCounter()
# def increment_counter(n):
#     for _ in range(n):
#         counter.increment()
#
# threads = [threading.Thread(target=increment_counter, args=(1000,)) for _ in range(10)]
# for t in threads:
#     t.start()
# for t in threads:
#     t.join()
# print(counter.get_value())  # Expected: 10000 (not less due to race condition)


# =============================================================================
# Exercise 3: Producer-Consumer Pattern (Difficulty: Intermediate)
# =============================================================================
# Implement producer-consumer with queues.

from queue import Queue

# TODO: Implement producer
def producer(queue, items):
    """Produce items and put them in queue."""
    pass

# TODO: Implement consumer
def consumer(queue):
    """Consume items from queue."""
    pass

# Test cases
# q = Queue(maxsize=5)
# producer_thread = threading.Thread(target=producer, args=(q, range(10)))
# consumer_thread = threading.Thread(target=consumer, args=(q,))
# producer_thread.start()
# consumer_thread.start()
# producer_thread.join()
# consumer_thread.join()


# =============================================================================
# Exercise 4: Thread Pools (Difficulty: Intermediate)
# =============================================================================
# Use thread pools for parallel execution.

from concurrent.futures import ThreadPoolExecutor

# TODO: Implement parallel processing
def process_item(item):
    """Process a single item (simulated work)."""
    pass

def parallel_process(items, max_workers=4):
    """Process items in parallel using thread pool."""
    pass

# Test cases
# items = list(range(20))
# results = parallel_process(items)
# print(results)  # Expected: Processed items


# =============================================================================
# Exercise 5: Thread Communication (Difficulty: Advanced)
# =============================================================================
# Communicate between threads using events and conditions.

# TODO: Implement event-based communication
def sender(event):
    """Send signal using event."""
    pass

def receiver(event):
    """Wait for signal using event."""
    pass

# TODO: Implement condition-based communication
class SharedBuffer:
    """Thread-safe buffer using conditions."""

    def __init__(self, capacity):
        pass

    def put(self, item):
        pass

    def get(self):
        pass

# Test cases
# event = threading.Event()
# sender_thread = threading.Thread(target=sender, args=(event,))
# receiver_thread = threading.Thread(target=receiver, args=(event,))
# receiver_thread.start()
# sender_thread.start()
# sender_thread.join()
# receiver_thread.join()
