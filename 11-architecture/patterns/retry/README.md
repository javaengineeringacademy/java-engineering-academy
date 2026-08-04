# Retry Pattern

## Overview

The Retry pattern automatically retries failed operations with configurable backoff strategies. It handles transient failures by waiting and retrying, improving system resilience.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Backoff Strategies](#backoff-strategies)
- [Implementation](#implementation)
- [Configuration](#configuration)
- [Benefits](#benefits)
- [Best Practices](#best-practices)

## Core Concepts

```
+--------------------------------------------------+
|            RETRY PATTERN                          |
+--------------------------------------------------+
|  Request --> Failure --> Wait --> Retry           |
|                              |                    |
|                              +--> Success         |
|                              |                    |
|                              +--> Failure --> ... |
+--------------------------------------------------+
```

| Strategy | Description |
|----------|-------------|
| Fixed Delay | Constant wait between retries |
| Exponential Backoff | Doubles wait each retry |
| Linear Backoff | Increases wait linearly |
| Random Jitter | Adds randomness to prevent thundering herd |

## Backoff Strategies

### Fixed Delay

```python
import time

def retry_fixed_delay(func, max_attempts=3, delay=1):
    for attempt in range(max_attempts):
        try:
            return func()
        except Exception as e:
            if attempt == max_attempts - 1:
                raise
            time.sleep(delay)
```

### Exponential Backoff

```python
import time
import random

def retry_exponential_backoff(func, max_attempts=3, base_delay=1, max_delay=60):
    for attempt in range(max_attempts):
        try:
            return func()
        except Exception as e:
            if attempt == max_attempts - 1:
                raise
            delay = min(base_delay * (2 ** attempt), max_delay)
            jitter = delay * 0.1 * random.random()
            time.sleep(delay + jitter)
```

### Linear Backoff

```python
def retry_linear_backoff(func, max_attempts=3, base_delay=1):
    for attempt in range(max_attempts):
        try:
            return func()
        except Exception as e:
            if attempt == max_attempts - 1:
                raise
            time.sleep(base_delay * (attempt + 1))
```

## Implementation

### Basic Retry Decorator

```python
import time
import random
from functools import wraps

def retry(max_attempts=3, base_delay=1, max_delay=60, 
          exceptions=(Exception,)):
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            last_exception = None
            for attempt in range(max_attempts):
                try:
                    return func(*args, **kwargs)
                except exceptions as e:
                    last_exception = e
                    if attempt < max_attempts - 1:
                        delay = min(base_delay * (2 ** attempt), max_delay)
                        jitter = delay * 0.1 * random.random()
                        time.sleep(delay + jitter)
            raise last_exception
        return wrapper
    return decorator

# Usage
@retry(max_attempts=3, base_delay=1, exceptions=(ConnectionError, TimeoutError))
def call_external_api():
    response = requests.get('https://api.example.com/data', timeout=10)
    response.raise_for_status()
    return response.json()
```

### Retry with Callbacks

```python
class RetryHandler:
    def __init__(self, max_attempts=3, base_delay=1):
        self.max_attempts = max_attempts
        self.base_delay = base_delay
        self.on_retry = None
        self.on_failure = None

    def execute(self, func, *args, **kwargs):
        last_exception = None
        for attempt in range(self.max_attempts):
            try:
                return func(*args, **kwargs)
            except Exception as e:
                last_exception = e
                if attempt < self.max_attempts - 1:
                    delay = self.base_delay * (2 ** attempt)
                    if self.on_retry:
                        self.on_retry(attempt + 1, delay, e)
                    time.sleep(delay)
        
        if self.on_failure:
            self.on_failure(last_exception)
        raise last_exception
```

### Retry with Circuit Breaker

```python
class ResilientClient:
    def __init__(self, circuit_breaker, retry_config):
        self.circuit_breaker = circuit_breaker
        self.retry_config = retry_config

    def call(self, func, *args, **kwargs):
        @retry(**self.retry_config)
        @self.circuit_breaker.wrap
        def _call():
            return func(*args, **kwargs)
        
        return _call()
```

## Configuration

```python
retry_config = {
    'max_attempts': 3,           # Maximum retry attempts
    'base_delay': 1,             # Initial delay in seconds
    'max_delay': 60,             # Maximum delay in seconds
    'exponential_base': 2,       # Backoff multiplier
    'jitter': True,              # Add randomness
    'retryable_exceptions': (    # Which exceptions to retry
        ConnectionError,
        TimeoutError,
        ServiceUnavailableError
    )
}
```

### Retryable vs Non-Retryable

```python
# Retryable errors (transient)
RETRYABLE_EXCEPTIONS = (
    ConnectionError,
    TimeoutError,
    ServiceUnavailableError,
    TooManyRequestsError
)

# Non-retryable errors (permanent)
NON_RETRYABLE_EXCEPTIONS = (
    BadRequestError,
    UnauthorizedError,
    ForbiddenError,
    NotFoundError
)
```

## Benefits

1. **Improved reliability**: Handles transient failures automatically
2. **Better user experience**: Users see fewer errors
3. **Resilience**: Systems recover from temporary issues
4. **Flexibility**: Configurable strategies for different scenarios
5. **Observability**: Can monitor retry patterns

## Best Practices

### 1. Use Exponential Backoff

```python
@retry(max_attempts=3, base_delay=1)
def call_api():
    # Exponential backoff: 1s, 2s, 4s
    pass
```

### 2. Add Jitter

```python
import random

def calculate_delay(attempt, base_delay, max_delay):
    delay = min(base_delay * (2 ** attempt), max_delay)
    jitter = delay * 0.1 * random.random()
    return delay + jitter
```

### 3. Set Maximum Delay

```python
@retry(max_attempts=5, base_delay=1, max_delay=30)
def call_service():
    # Delays: 1s, 2s, 4s, 8s, 16s (capped at 30s)
    pass
```

### 4. Log Retries

```python
import logging

logger = logging.getLogger(__name__)

@retry(max_attempts=3)
def call_api():
    try:
        return requests.get('https://api.example.com')
    except Exception as e:
        logger.warning(f'Retry attempt failed: {e}')
        raise
```

### 5. Use Idempotent Operations

```python
# Ensure retried operations are idempotent
@retry(max_attempts=3)
def create_payment(payment_id, amount):
    # payment_id ensures idempotency
    return payment_gateway.charge(payment_id, amount)
```

### 6. Handle Rate Limiting

```python
def retry_with_rate_limit(func, max_attempts=5):
    for attempt in range(max_attempts):
        try:
            return func()
        except RateLimitError as e:
            if attempt < max_attempts - 1:
                # Use Retry-After header if available
                retry_after = int(e.response.headers.get('Retry-After', 60))
                time.sleep(retry_after)
            else:
                raise
```

## Further Reading

- [Exponential Backoff and Jitter - AWS](https://aws.amazon.com/blogs/architecture/exponential-backoff-and-jitter/)
- [Release It! - Michael Nygard](https://pragprog.com/titles/mnee2/release-it-second-edition/)
- [Java Retry Patterns](https://github.com/spring-projects/spring-retry)
