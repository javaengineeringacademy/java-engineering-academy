"""
Module 18: Senior - Production Deployment Solutions
Practice production deployment patterns and best practices.
"""

import logging
import time
from typing import Any, Dict, List, Optional
from dataclasses import dataclass
from enum import Enum
from contextlib import contextmanager
from functools import wraps


class Environment(Enum):
    """Deployment environments."""
    DEVELOPMENT = "development"
    TESTING = "testing"
    STAGING = "staging"
    PRODUCTION = "production"


@dataclass
class Config:
    """Application configuration."""
    app_name: str
    version: str
    environment: Environment
    debug: bool = False
    database_url: str = ""
    cache_url: str = ""
    log_level: str = "INFO"


class HealthCheck:
    """Health check for services."""

    def __init__(self):
        self.checks: Dict[str, callable] = {}

    def register(self, name: str, check_func: callable):
        self.checks[name] = check_func

    def check_all(self) -> Dict[str, bool]:
        results = {}
        for name, check_func in self.checks.items():
            try:
                results[name] = check_func()
            except Exception:
                results[name] = False
        return results

    def is_healthy(self) -> bool:
        return all(self.check_all().values())


class CircuitBreaker:
    """Circuit breaker pattern for fault tolerance."""

    def __init__(self, failure_threshold: int = 5, recovery_timeout: int = 60):
        self.failure_threshold = failure_threshold
        self.recovery_timeout = recovery_timeout
        self.failure_count = 0
        self.state = "closed"
        self.last_failure_time = None

    def __call__(self, func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            if self.state == "open":
                if time.time() - self.last_failure_time < self.recovery_timeout:
                    raise Exception("Circuit breaker is open")
                self.state = "half-open"

            try:
                result = func(*args, **kwargs)
                self.failure_count = 0
                self.state = "closed"
                return result
            except Exception as e:
                self.failure_count += 1
                self.last_failure_time = time.time()
                if self.failure_count >= self.failure_threshold:
                    self.state = "open"
                raise

        return wrapper


class RetryPolicy:
    """Retry policy for transient failures."""

    def __init__(self, max_retries: int = 3, delay: float = 1.0, backoff: float = 2.0):
        self.max_retries = max_retries
        self.delay = delay
        self.backoff = backoff

    def __call__(self, func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            last_exception = None
            current_delay = self.delay

            for attempt in range(self.max_retries):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    last_exception = e
                    if attempt < self.max_retries - 1:
                        time.sleep(current_delay)
                        current_delay *= self.backoff

            raise last_exception

        return wrapper


class RateLimiter:
    """Rate limiter for API protection."""

    def __init__(self, max_requests: int, window_seconds: int):
        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.requests: List[float] = []

    def allow(self) -> bool:
        now = time.time()
        # Remove old requests
        self.requests = [r for r in self.requests if now - r < self.window_seconds]

        if len(self.requests) < self.max_requests:
            self.requests.append(now)
            return True
        return False


class Cache:
    """Simple in-memory cache."""

    def __init__(self, ttl: int = 300):
        self.ttl = ttl
        self._cache: Dict[str, Any] = {}
        self._timestamps: Dict[str, float] = {}

    def get(self, key: str) -> Optional[Any]:
        if key in self._cache:
            if time.time() - self._timestamps[key] < self.ttl:
                return self._cache[key]
            else:
                del self._cache[key]
                del self._timestamps[key]
        return None

    def set(self, key: str, value: Any):
        self._cache[key] = value
        self._timestamps[key] = time.time()

    def delete(self, key: str):
        if key in self._cache:
            del self._cache[key]
            del self._timestamps[key]

    def clear(self):
        self._cache.clear()
        self._timestamps.clear()


class Logger:
    """Structured logger for production."""

    def __init__(self, name: str, level: str = "INFO"):
        self.logger = logging.getLogger(name)
        self.logger.setLevel(getattr(logging, level))

        handler = logging.StreamHandler()
        formatter = logging.Formatter(
            '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
        )
        handler.setFormatter(formatter)
        self.logger.addHandler(handler)

    def info(self, message: str, **kwargs):
        self.logger.info(message, extra=kwargs)

    def error(self, message: str, **kwargs):
        self.logger.error(message, extra=kwargs)

    def warning(self, message: str, **kwargs):
        self.logger.warning(message, extra=kwargs)


class MetricsCollector:
    """Collect metrics for monitoring."""

    def __init__(self):
        self.metrics: Dict[str, List[float]] = {}

    def record(self, name: str, value: float):
        if name not in self.metrics:
            self.metrics[name] = []
        self.metrics[name].append(value)

    def get_average(self, name: str) -> float:
        if name in self.metrics and self.metrics[name]:
            return sum(self.metrics[name]) / len(self.metrics[name])
        return 0.0

    def get_count(self, name: str) -> int:
        return len(self.metrics.get(name, []))


@contextmanager
def performance_monitor(metrics: MetricsCollector, operation: str):
    """Context manager for monitoring performance."""
    start = time.time()
    try:
        yield
    finally:
        duration = time.time() - start
        metrics.record(operation, duration)


if __name__ == "__main__":
    print("Testing Production Deployment Solutions...")

    # Test Config
    config = Config(
        app_name="MyApp",
        version="1.0.0",
        environment=Environment.PRODUCTION
    )
    assert config.environment == Environment.PRODUCTION
    print("✓ Exercise 1: Config works")

    # Test HealthCheck
    health = HealthCheck()
    health.register("database", lambda: True)
    health.register("cache", lambda: True)
    assert health.is_healthy() is True
    print("✓ Exercise 2: Health check works")

    # Test CircuitBreaker
    breaker = CircuitBreaker(failure_threshold=2)

    @breaker
    def failing_func():
        raise ValueError("Service unavailable")

    for _ in range(2):
        try:
            failing_func()
        except ValueError:
            pass

    assert breaker.state == "open"
    print("✓ Exercise 3: Circuit breaker works")

    # Test RetryPolicy
    policy = RetryPolicy(max_retries=3, delay=0.01)

    @policy
    def flaky_func():
        return "success"

    result = flaky_func()
    assert result == "success"
    print("✓ Exercise 4: Retry policy works")

    # Test RateLimiter
    limiter = RateLimiter(max_requests=5, window_seconds=1)
    for _ in range(5):
        assert limiter.allow() is True
    assert limiter.allow() is False
    print("✓ Exercise 5: Rate limiter works")

    # Test Cache
    cache = Cache(ttl=1)
    cache.set("key1", "value1")
    assert cache.get("key1") == "value1"
    cache.delete("key1")
    assert cache.get("key1") is None
    print("✓ Exercise 6: Cache works")

    # Test Logger
    logger = Logger("test", "INFO")
    logger.info("Test message")
    print("✓ Exercise 7: Logger works")

    # Test MetricsCollector
    metrics = MetricsCollector()
    metrics.record("response_time", 0.1)
    metrics.record("response_time", 0.2)
    assert metrics.get_average("response_time") == 0.15
    assert metrics.get_count("response_time") == 2
    print("✓ Exercise 8: Metrics collector works")

    # Test performance monitor
    with performance_monitor(metrics, "operation"):
        time.sleep(0.01)
    assert metrics.get_count("operation") > 0
    print("✓ Exercise 9: Performance monitor works")

    print("All Production Deployment solutions passed!")
