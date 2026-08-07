# Logging in Python

> **If you can't see it, you can't fix it.**

## What

Python's `logging` module provides a flexible framework for emitting log messages from your application. It's built on four core components: Loggers (where logs originate), Handlers (where logs go), Formatters (how logs look), and Filters (what gets through).

Structured logging goes beyond plain text — it embeds machine-readable data in your logs, making them searchable, analyzable, and actionable at scale.

## Why

- **Print statements die in production.** Logging persists, rotates, and ships to central systems.
- **Debugging requires context.** Logs carry timestamps, thread IDs, request traces — print doesn't.
- **Monitoring depends on logs.** Alerting, metrics extraction, and dashboards all start with proper logging.
- **Log levels are a feature.** Debug in dev, info in prod, error for alerts — one framework, multiple views.
- **Compliance often mandates logging.** Audit trails, access logs, error logs — regulations require them.

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
