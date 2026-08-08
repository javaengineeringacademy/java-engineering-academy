# Logging in Python

> **If you can't see it, you can't fix it.**

## Why Logging Matters

Every production application needs visibility into its behavior — what's happening, when, and why. Python's `logging` module provides a flexible framework for emitting log messages with context like timestamps, thread IDs, and request traces. Without proper logging, you'd debug production issues blind, relying on guesswork instead of evidence.

Without logging, you'd have no audit trail, no monitoring data, and no way to understand your application's behavior in production. That's why logging exists — it provides the observability foundation for debugging, monitoring, compliance, and understanding how your application behaves under real-world conditions.

## What You'll Learn

By the end of this module, you'll be able to:

- Configure Python's logging module with handlers, formatters, and filters
- Use log levels appropriately to control verbosity
- Implement structured logging for machine-readable, searchable logs
- Set up logging for production environments with rotation and aggregation
- Integrate logging with monitoring and alerting systems

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Any production application, debugging, auditing | `print()` for quick prototypes |
| When NOT to use | Don't log sensitive data; don't over-log | Use appropriate log levels |
| Alternatives | structlog for structured logging, loguru for simplicity | Basic logging for simple scripts |
| Production Examples | Web services, microservices, data pipelines | Quick scripts, prototypes |
| Common Mistakes | Logging sensitive data, using `print()`, not rotating logs | Use structured logging; rotate files |

## When

| Scenario | Level | What to Log |
|----------|-------|-------------|
| Function entry/exit | DEBUG | Parameters, return values (dev only) |
| Business events | INFO | Orders placed, users registered, payments processed |
| Unexpected conditions | WARNING | Degraded service, retrying, fallback used |
| Errors needing attention | ERROR | Failed operations, exceptions with full trace |
| Critical failures | CRITICAL | System down, data loss, security breach |
| Performance metrics | INFO | Request duration, query time, memory usage |
| Security events | WARNING/ERROR | Auth failures, permission denied, suspicious input |

## How

### Basic Configuration

```python
import logging

# Simple setup — good enough for scripts
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.StreamHandler(),
        logging.FileHandler('app.log')
    ]
)

logger = logging.getLogger(__name__)
logger.info("Application started")
```

### Logger Hierarchy

```python
# Create loggers with hierarchy (dots indicate nesting)
app_logger = logging.getLogger('myapp')
db_logger = logging.getLogger('myapp.database')
api_logger = logging.getLogger('myapp.api')

# Setting level propagates to children
app_logger.setLevel(logging.INFO)

# Disable propagation (prevents duplicate logs)
db_logger.propagate = False

# Independent handler per logger
db_handler = logging.FileHandler('database.log')
db_logger.addHandler(db_handler)
```

### Structured Logging

```python
import logging
import json
from datetime import datetime

class StructuredFormatter(logging.Formatter):
    """JSON-structured log formatter for production."""

    def format(self, record):
        log_data = {
            "timestamp": datetime.utcnow().isoformat(),
            "level": record.levelname,
            "logger": record.name,
            "message": record.getMessage(),
            "module": record.module,
            "function": record.funcName,
            "line": record.lineno,
        }

        # Add exception info if present
        if record.exc_info and record.exc_info[0]:
            log_data["exception"] = self.formatException(record.exc_info)

        # Add extra fields
        for key in ['request_id', 'user_id', 'trace_id']:
            if hasattr(record, key):
                log_data[key] = getattr(record, key)

        return json.dumps(log_data)

# Setup
handler = logging.StreamHandler()
handler.setFormatter(StructuredFormatter())
logger = logging.getLogger('myapp')
logger.addHandler(handler)
logger.setLevel(logging.INFO)

# Usage
logger.info("Payment processed", extra={"user_id": "u123", "amount": 99.99})
```

### Handlers

```python
# Rotating file handler — prevents disk exhaustion
from logging.handlers import RotatingFileHandler, TimedRotatingFileHandler

# Size-based rotation
rotating = RotatingFileHandler(
    'app.log',
    maxBytes=10_000_000,  # 10MB
    backupCount=5
)

# Time-based rotation
timed = TimedRotatingFileHandler(
    'app.log',
    when='midnight',
    interval=1,
    backupCount=30
)

# Syslog handler — for centralized logging
syslog = logging.handlers.SysLogHandler(
    address='/dev/log',
    facility=logging.handlers.SysLogHandler.LOG_USER
)
```

### Filters

```python
class RequestIDFilter(logging.Filter):
    """Inject request ID into all log records."""
    def __init__(self, request_id: str):
        super().__init__()
        self.request_id = request_id

    def filter(self, record):
        record.request_id = self.request_id
        return True

class LevelFilter(logging.Filter):
    """Allow only specific log levels."""
    def __init__(self, levels: list):
        super().__init__()
        self.levels = set(levels)

    def filter(self, record):
        return record.levelno in self.levels

# Usage
logger.addFilter(RequestIDFilter("req-abc-123"))
logger.addFilter(LevelFilter([logging.WARNING, logging.ERROR]))
```

### Performance-Aware Logging

```python
import time

class PerformanceFilter(logging.Filter):
    """Add elapsed time since last log for performance tracking."""
    def __init__(self):
        super().__init__()
        self.last_time = time.monotonic()

    def filter(self, record):
        now = time.monotonic()
        record.elapsed_ms = (now - self.last_time) * 1000
        self.last_time = now
        return True

# Lazy evaluation — don't format string if level not enabled
logger.debug("Processing %s items: %s", count, expensive_computation())
# ^ This still evaluates expensive_computation()!

# Better: use isEnabledFor
if logger.isEnabledFor(logging.DEBUG):
    logger.debug("Processing %s items: %s", count, expensive_computation())
```

### Async-Safe Logging

```python
import asyncio
import logging

# Use QueueHandler for async applications
from logging.handlers import QueueHandler, QueueListener
import queue

# Create queue and handler
log_queue = queue.Queue(-1)
queue_handler = QueueHandler(log_queue)

# Set up handler that does the actual writing
stream_handler = logging.StreamHandler()
stream_handler.setFormatter(logging.Formatter('%(message)s'))

# Listener processes logs in background
listener = QueueListener(log_queue, stream_handler)
listener.start()

# Attach to logger
async_logger = logging.getLogger('async_app')
async_logger.addHandler(queue_handler)
```

## Production Incidents

### Incident 1: Log Injection Attack

**Problem:** Logs contained malicious content that broke log parsing
**Cause:** User input logged directly without sanitization
**Impact:** Log aggregation system crashed; monitoring blind for 2 hours
**Detection:** Log parser errors; monitoring gaps
**Solution:**
```python
# BAD: Direct user input in logs
logger.info(f"User {username} performed action")

# GOOD: Sanitize user input
import re
def sanitize_log(msg):
    return re.sub(r'[\n\r\t]', '_', msg)

logger.info("User %s performed action", sanitize_log(username))
```
**Prevention:** Sanitize user input before logging; use structured logging; validate log format

### Incident 2: Log Flooding Causing Disk Exhaustion

**Problem:** Disk filled up in 10 minutes; service crashed
**Cause:** Debug logging enabled in production; high-traffic endpoint logged every request
**Impact:** Service outage; required manual disk cleanup
**Detection:** Disk space alerts; service crash
**Solution:**
```python
# BAD: Debug logging in production
logger.setLevel(logging.DEBUG)
logger.debug(f"Processing request: {request}")

# GOOD: Environment-based log level
import os
log_level = os.getenv('LOG_LEVEL', 'INFO')
logger.setLevel(getattr(logging, log_level))
```
**Prevention:** Set log level based on environment; use log rotation; monitor disk usage

### Incident 3: Exception Logging Without Context

**Problem:** Error alerts showed "NoneType object has no attribute"
**Cause:** `logger.exception()` called outside `except` block; no traceback
**Impact:** 30-minute debugging to find actual error location
**Detection:** Error monitoring showed generic messages
**Solution:**
```python
# BAD: logger.exception() outside except block
try:
    process_data()
except:
    logger.exception("Error occurred")  # No traceback!

# GOOD: Use exc_info=True
try:
    process_data()
except Exception as e:
    logger.error("Error processing data: %s", e, exc_info=True)
```
**Prevention:** Use `logger.exception()` only in `except` blocks; use `exc_info=True` elsewhere; include context

## Production Checklist

- [ ] **Use structured logging** — JSON format enables machine parsing and searching
- [ ] **Configure log rotation** — prevent disk exhaustion with size or time-based rotation
- [ ] **Set appropriate log levels** — DEBUG in dev, INFO in prod, WARNING+ for monitoring
- [ ] **Include request context** — request ID, user ID, trace ID in every log line
- [ ] **Don't log sensitive data** — no passwords, tokens, PII, credit card numbers
- [ ] **Use lazy evaluation** — check `isEnabledFor` before expensive log formatting
- [ ] **Centralize log collection** — ship logs to ELK, Datadog, CloudWatch, or similar

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **Print Debugging** | `print()` statements, no structure, no levels |
| 2 | **Basic Logging** | `logging.info()`, simple format, console only |
| 3 | **Production Ready** | Structured JSON, handlers, rotation, basic context |
| 4 | **Observable** | Distributed tracing, log correlation, metrics from logs |
| 5 | **Intelligent** | Auto-anomaly detection, log-based alerting, ML-powered insights |

## Common Myths

### Myth 1: "logging is slow, use print"
**Reality:** `logging` is thread-safe, handles rotation, supports levels, and can be async. `print` is a formatting function, not a logging system. The performance difference is negligible for any real application.

### Myth 2: "More logging is better logging"
**Reality:** Excessive logging creates noise, increases storage costs, and makes debugging harder. Log actionable information, not everything. Use DEBUG for verbose development logs.

### Myth 3: "Exception logging handles itself"
**Reality:** `logger.exception()` only logs in `except` blocks. For async code or when re-raising, you need `logger.error(msg, exc_info=True)` explicitly. Also, `exc_info=True` is required to get the traceback.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Get logger | `logging.getLogger(__name__)` | Named logger instance |
| Log levels | `DEBUG < INFO < WARNING < ERROR < CRITICAL` | Severity classification |
| Basic config | `logging.basicConfig(...)` | One-call setup |
| Structured log | JSON formatter with extra fields | Machine-parseable logs |
| Rotating handler | `RotatingFileHandler(maxBytes, backupCount)` | Prevent disk exhaustion |
| Timed rotation | `TimedRotatingFileHandler(when, interval)` | Daily/weekly rotation |
| Filter | `logging.Filter.filter(record)` | Control what gets logged |
| Exception log | `logger.exception(msg)` | Log with traceback |
| Lazy evaluation | `logger.debug(msg, *args)` | Defer string formatting |
| Null handler | `logging.NullHandler()` | Prevent "no handler" warnings in libraries |

## Related Topics

- [09-exception-handling](../09-exception-handling/) - Exception logging patterns
- [11-context-managers](../11-context-managers/) - Log context with contextvars
- [15-async](../15-async/) - Async-safe logging
- [16-testing](../16-testing/) - Testing log output
- [18-senior](../18-senior/) - Observability and monitoring

---

> **Remember:** Logs are your application's voice. Make sure they speak clearly, contextually, and only when necessary.

## Interview Questions

### Q1: What are the logging levels and when to use each?
**Answer:** DEBUG (detailed), INFO (confirmation), WARNING (unexpected), ERROR (problem), CRITICAL (fatal). Use INFO for normal operations, ERROR for failures.

### Q2: What is the difference between logging and print?
**Answer:** print is for development. logging has levels, timestamps, handlers, rotation. Use logging in production.

### Q3: What is a handler in logging?
**Answer:** Handlers direct log records to destinations (file, console, network). Multiple handlers can be attached to one logger.

### Q4: What is structured logging?
**Answer:** Logging in JSON format with consistent fields. Enables log aggregation, search, analytics. Use structlog or python-json-logger.

### Q5: What is log rotation?
**Answer:** Automatically rotating log files when they reach size limit. Prevents disk exhaustion. Use RotatingFileHandler or TimedRotatingFileHandler.
