# Bulkhead Pattern

## Overview

Isolate resources to prevent failures from spreading.

## Types

- **Thread Pool**: Separate pools per service
- **Semaphore**: Limit concurrent calls
- **Partition**: Separate infrastructure

## Implementation

```python
from concurrent.futures import ThreadPoolExecutor

class Bulkhead:
    def __init__(self, max_concurrent):
        self.executor = ThreadPoolExecutor(max_concurrent)
        self.semaphore = threading.Semaphore(max_concurrent)
    
    def call(self, func, *args, **kwargs):
        if not self.semaphore.acquire(blocking=False):
            raise BulkheadFullError()
        try:
            future = self.executor.submit(func, *args, **kwargs)
            return future.result()
        finally:
            self.semaphore.release()

# Usage
payment_bulkhead = Bulkhead(max_concurrent=10)
order_bulkhead = Bulkhead(max_concurrent=20)
```
