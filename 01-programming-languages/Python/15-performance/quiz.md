# Performance Quiz

## Question 1 (Multiple Choice)
What is the first thing you should do before optimizing any Python code?

- A) Rewrite it in C for speed
- B) Profile it to identify actual bottlenecks — don't guess
- C) Add caching everywhere
- D) Use NumPy for all operations

**Answer: B**
**Explanation:** Premature optimization is the root of all evil (Knuth). Profile first with `cProfile`, `line_profiler`, or `py-spy` to find where time is actually spent. Often, 90% of execution time is in 10% of the code. Optimizing code that isn't the bottleneck wastes effort. `python -m cProfile -s cumtime script.py` shows cumulative time per function. `line_profiler` shows line-by-line timing. Profile → measure → optimize → verify.

---

## Question 2 (Multiple Choice)
Which caching strategy is most effective for a function with expensive computation that takes the same arguments repeatedly?

- A) Global variable cache — manually check and store results
- B) `functools.lru_cache` — automatic memoization with LRU eviction
- C) `functools.cache` — unlimited cache (same as `lru_cache(maxsize=None)`)
- D) Both B and C work, but C can cause memory issues with many unique arguments

**Answer: D**
**Explanation:** `lru_cache` is the standard memoization decorator. It caches results and evicts the least recently used when `maxsize` is exceeded. `cache` (Python 3.9+) is equivalent to `lru_cache(maxsize=None)` — unlimited cache. Both work, but unlimited cache can consume all memory if the function is called with millions of unique arguments. `lru_cache(maxsize=128)` is the safe default. Use `cache` only when argument space is bounded. Both require hashable arguments.

---

## Question 3 (Multiple Choice)
What is the time complexity difference between concatenating strings with `+=` in a loop versus using `str.join()`?

- A) They are the same — both O(n)
- B) `+=` is O(n²) because each concatenation creates a new string; `join()` is O(n) — builds the entire string in one pass
- C) `+=` is O(n) because Python optimizes it; `join()` is O(n²)
- D) Both are O(n²) — string immutability makes all concatenation expensive

**Answer: B**
**Explanation:** Strings are immutable in Python. `"s" += "x"` creates a new string each time, copying all previous characters. In a loop, this means copying 1+2+3+...+n characters = O(n²). `join()` pre-calculates the total size, allocates once, and copies each string exactly once = O(n). For 10,000 iterations, this can be 100x faster. Always use `"".join(list_of_strings)` for building strings in loops.

---

## Question 4 (Multiple Choice)
In an async Python application using `asyncio`, what is the most common performance bottleneck?

- A) The event loop itself is slow
- B) Accidentally blocking the event loop with synchronous I/O or CPU-bound work
- C) Too many coroutines running concurrently
- D) `async/await` syntax overhead

**Answer: B**
**Explanation:** `asyncio` runs on a single thread. Any blocking call (sync I/O, `time.sleep()`, CPU-heavy computation) freezes the entire event loop, making all coroutines wait. Use `aiohttp` instead of `requests`, `aiofiles` instead of `open()`, and `loop.run_in_executor()` for CPU-bound work. The event loop overhead is negligible. The real bottleneck is always blocking calls disguised as async — a common mistake when migrating synchronous code.

---

## Question 5 (Code Output)
What is the output of this benchmark?

```python
import timeit

setup = """
def slow():
    result = []
    for i in range(1000):
        result.append(i)
    return result

def fast():
    return list(range(1000))
"""

print("List comp:", timeit.timeit("list(range(1000))", number=10000))
print("Append loop:", timeit.timeit("slow()", setup=setup, number=10000))
print("List():", timeit.timeit("fast()", setup=setup, number=10000))
```

**Answer:** List comprehension is fastest, append loop is slowest, `list(range())` is in between.
**Explanation:** List comprehensions are optimized at the C level in CPython — they avoid the overhead of repeated `list.append()` method lookups and function calls. `list(range(1000))` is also C-optimized but involves creating the range object first. The append loop has Python-level overhead for each iteration. Expected ordering: `list(range())` ≈ list comprehension < `append` loop. Always benchmark with `timeit` rather than assuming — micro-benchmarks can surprise you.

---

## Question 6 (Code Output)
What is the output of this code?

```python
import functools
import time

@functools.lru_cache(maxsize=128)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)

start = time.time()
result = fibonacci(100)
elapsed = time.time() - start
print(f"fib(100) = {result}")
print(f"Time: {elapsed:.4f}s")
print(f"Cache info: {fibonacci.cache_info()}")
```

**Answer:**
```
fib(100) = 354224848179261915075
Time: ~0.0000s
CacheInfo(hits=98, misses=101, maxsize=128, currsize=101)
```
**Explanation:** Without caching, `fib(100)` makes ~2^100 recursive calls (exponential). With `lru_cache`, each value is computed once and cached. The 100th call hits the cache 98 times (for `fib(98)` through `fib(2)`), misses only when computing new values (101 total unique calls). The result is instant instead of taking billions of years. This demonstrates how caching transforms exponential algorithms into linear ones.

---

## Question 7 (Bug Finding)
Find the performance bug in this code:

```python
def find_duplicates(data):
    duplicates = []
    for item in data:
        if data.count(item) > 1 and item not in duplicates:
            duplicates.append(item)
    return duplicates

result = find_duplicates([1, 2, 3, 2, 4, 3, 5, 1, 6])
print(result)
```

**Bug:** `data.count(item)` is O(n) and called inside an O(n) loop, making the overall algorithm O(n²). The `item not in duplicates` check is another O(n) operation. For a list of 10,000 elements, this performs ~100M operations. It works correctly but scales terribly.
**Fix:** Use a `Counter` or `set` for O(n) performance:
```python
from collections import Counter

def find_duplicates(data):
    counts = Counter(data)
    return [item for item, count in counts.items() if count > 1]
```
This is O(n) — one pass to count, one pass to filter.

---

## Question 8 (Bug Finding)
Find the performance issue in this code:

```python
import json
import time

def process_large_json(filepath):
    with open(filepath) as f:
        data = json.load(f)  # Load entire file into memory
    
    results = []
    for item in data:
        if item["status"] == "active":
            results.append({"id": item["id"], "name": item["name"]})
    
    return results

start = time.time()
results = process_large_json("huge_file.json")
print(f"Processed {len(results)} items in {time.time() - start:.2f}s")
```

**Bug:** `json.load()` reads the entire file into memory at once. For a 2GB JSON file, this requires ~4-8GB of RAM (Python objects are larger than raw JSON). If the file is larger than available RAM, the process crashes or swaps heavily, causing extreme slowness. The processing logic is fine, but the loading strategy is the bottleneck.
**Fix:** Stream the JSON incrementally:
```python
import ijson  # or json with manual streaming

def process_large_json(filepath):
    results = []
    with open(filepath, 'rb') as f:
        for item in ijson.items(f, 'item'):
            if item["status"] == "active":
                results.append({"id": item["id"], "name": item["name"]})
    return results
```
This processes one item at a time with constant memory usage.

---

## Question 9 (Scenario)
Your Python web API has p99 latency of 2 seconds under load. Profiling shows most time is spent in a function that queries a database, processes results, and returns JSON. The database query takes 100ms, processing takes 800ms, and serialization takes 100ms. What should you optimize first?

- A) The database query — add more indexes
- B) The processing step — it takes 80% of the time, optimize the algorithm or parallelize
- C) The serialization step — switch to a faster JSON library
- D) Add caching at all three levels

**Answer: B**
**Explanation:** Follow the data: 800ms / 1000ms = 80% of time is in processing. Optimizing the database query from 100ms to 50ms saves 50ms (5% improvement). Optimizing processing from 800ms to 200ms saves 600ms (60% improvement). Profile to understand *why* processing is slow — is it a bad algorithm? Too many iterations? Can it be vectorized with NumPy? Can it be parallelized? Always optimize the largest portion first, then move to the next.

---

## Question 10 (Architecture Decision)
You need to design a Python data pipeline that processes 100GB of data daily. Each record requires validation, transformation, and enrichment (API call). The pipeline must handle failures gracefully and be monitorable. How should you architect this?

- A) Single Python script that loads all data, processes it, and writes results
- B) Celery task queue with worker pools — each step is a task, failures are retried with exponential backoff, results are tracked in Redis/PostgreSQL
- C) Apache Spark — overkill for 100GB
- D) Multithreaded script with `threading` module

**Answer: B**
**Explanation:** Celery provides distributed task processing with built-in retry logic, worker scaling, result backends, and monitoring (Flower). Each pipeline step (validate, transform, enrich) is a Celery task. Failures are isolated — one bad record doesn't crash the pipeline. Workers can scale horizontally. Spark is designed for terabyte+ scale and adds significant operational complexity. A single script has no fault isolation. `threading` doesn't help with I/O-bound work as well as async/Celery, and provides no retry/monitoring. For 100GB/day, Celery with Redis as the broker is the sweet spot.

---
