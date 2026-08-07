# Logging Quiz

## Question 1 (Multiple Choice)
In Python's `logging` module, what is the correct order of log levels from most to least severe?

- A) DEBUG → INFO → WARNING → ERROR → CRITICAL
- B) CRITICAL → ERROR → WARNING → INFO → DEBUG
- C) WARNING → ERROR → CRITICAL → INFO → DEBUG
- D) INFO → DEBUG → WARNING → ERROR → CRITICAL

**Answer: B**
**Explanation:** Log levels from most to least severe: CRITICAL (50), ERROR (40), WARNING (30), INFO (20), DEBUG (10). The root logger's default level is WARNING, meaning DEBUG and INFO messages are silently discarded unless you explicitly lower the level. Understanding severity ordering is essential for configuring filters and deciding what gets logged in production vs development.

---

## Question 2 (Multiple Choice)
Why should you use `logger = logging.getLogger(__name__)` instead of `logging.getLogger("myapp")` at the module level?

- A) `"myapp"` is not a valid logger name
- B) `__name__` creates a hierarchical logger that mirrors the module structure, enabling fine-grained filtering by module path
- C) `__name__` is faster than hardcoded strings
- D) It doesn't matter — both approaches are equivalent

**Answer: B**
**Explanation:** Using `__name__` creates a logger named `"package.module"` that mirrors Python's module hierarchy. This lets you configure logging for specific modules: `logging.getLogger("myapp.views").setLevel(DEBUG)` while keeping `"myapp.models"` at WARNING. Hardcoded names lose this hierarchy. It's also a Python anti-pattern to use `"root"` or overly generic names — hierarchical loggers are the recommended approach.

---

## Question 3 (Multiple Choice)
What is the purpose of a `Formatter` in Python logging?

- A) It filters log messages based on severity
- B) It converts LogRecord objects into human-readable strings (or structured data) for output
- C) It routes log messages to different destinations (file, console, network)
- D) It manages log rotation and file cleanup

**Answer: B**
**Explanation:** Formatters control the output format of log messages. They take a `LogRecord` and produce a string: `logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')`. Handlers route messages, Formatters shape them. You can use structured formatters (JSON) for machine-readable logs. Each handler can have its own formatter, so the same message can be formatted differently for console (human-readable) vs file (JSON).

---

## Question 4 (Multiple Choice)
Which logging handler should you use when you want log files to automatically rotate after reaching a certain size, keeping only the last N files?

- A) `StreamHandler` — writes to stdout/stderr
- B) `RotatingFileHandler` — rotates files based on size with backup count
- C) `FileHandler` — writes to a single file indefinitely
- D) `SocketHandler` — sends logs over the network

**Answer: B**
**Explanation:** `RotatingFileHandler` rotates log files when they reach `maxBytes`, keeping up to `backupCount` backups. It prevents log files from growing unbounded. `FileHandler` writes to one file forever (dangerous in production). `StreamHandler` is for console output. For time-based rotation (daily, hourly), use `TimedRotatingFileHandler`. For network logging, `SocketHandler` or `SysLogHandler` are appropriate.

---

## Question 5 (Code Output)
What is the output of this code?

```python
import logging

logger = logging.getLogger("myapp")
logger.setLevel(logging.DEBUG)

handler = logging.StreamHandler()
handler.setLevel(logging.WARNING)
formatter = logging.Formatter('%(levelname)s: %(message)s')
handler.setFormatter(formatter)
logger.addHandler(handler)

logger.debug("This is debug")
logger.info("This is info")
logger.warning("This is warning")
logger.error("This is error")
```

**Answer:**
```
WARNING: This is warning
ERROR: This is error
```
**Explanation:** The logger is set to DEBUG (captures everything), but the handler is set to WARNING. Logging works like a funnel: the logger decides if a message passes the severity threshold (DEBUG passes all), then the handler applies its own filter. Since the handler's level is WARNING, only WARNING and above messages appear on the console. DEBUG and INFO are captured by the logger but filtered out by the handler before output.

---

## Question 6 (Code Output)
What is the output of this code?

```python
import logging

logger = logging.getLogger("app.db")
parent = logging.getLogger("app")
parent.setLevel(logging.WARNING)

child = logging.getLogger("app.db")
print(child.level, child.isEnabledFor(logging.WARNING), child.isEnabledFor(logging.DEBUG))
```

**Answer:** `30 True False`
**Explanation:** `child.level` is 30 (WARNING) because Python's logging hierarchy propagates levels. The child logger inherits its effective level from the parent `app` logger set to WARNING (30). `isEnabledFor(WARNING)` is `True` because WARNING >= WARNING. `isEnabledFor(DEBUG)` is `False` because DEBUG (10) < WARNING (30). This demonstrates hierarchical level inheritance — child loggers don't need explicit level configuration if the parent handles it.

---

## Question 7 (Bug Finding)
Find the bug in this logging configuration:

```python
import logging

def setup_logging():
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger("myapp")
    logger.info("Logging configured")

def process_request():
    logger = logging.getLogger("myapp")
    logger.info("Processing request")
    logger.debug("Debug details: %s", {"key": "value"})

setup_logging()
process_request()
```

**Bug:** The `basicConfig` sets the root logger to INFO, and `getLogger("myapp")` inherits this level. The DEBUG message is correctly filtered out. However, the real issue is that `basicConfig` is called in a function, not at module level — it only works if called before any other logging calls. If another module calls `logging.getLogger()` first, `basicConfig` becomes a no-op. In production, this can silently disable all logging configuration.
**Fix:** Call `basicConfig` at module level or use explicit handler configuration:
```python
import logging
logging.basicConfig(level=logging.INFO)
# Now configure specific loggers
```

---

## Question 8 (Bug Finding)
Find the bug in this structured logging approach:

```python
import logging
import json

class JsonFormatter(logging.Formatter):
    def format(self, record):
        log_data = {
            "timestamp": self.formatTime(record),
            "level": record.levelname,
            "message": record.getMessage(),
            "extra": record.__dict__.get("extra_data", {})
        }
        return json.dumps(log_data)

logger = logging.getLogger("app")
handler = logging.StreamHandler()
handler.setFormatter(JsonFormatter())
logger.addHandler(handler)

logger.info("User login", extra={"extra_data": {"user_id": 123}})
logger.info("User logout")  # What happens here?
```

**Bug:** The `extra_data` key may not exist in `record.__dict__` if `extra` wasn't passed. `record.__dict__.get("extra_data", {})` returns an empty dict, which is fine. But the real bug is that `record.getMessage()` is called before the message is formatted — the message string itself isn't yet interpolated with args. This works correctly here, but if you use `record.msg % record.args` instead, you might get inconsistent results. The deeper issue: `extra` dict keys become attributes on the LogRecord, not nested under an "extra" key — so `record.__dict__["user_id"]` would exist, not `record.__dict__["extra_data"]`.
**Fix:** Use `getattr(record, 'extra_data', {})` and understand that `extra` kwargs become top-level LogRecord attributes.

---

## Question 9 (Scenario)
You're building a microservice that logs to both the console (for development) and a centralized logging system (for production). The console should show human-readable messages; the JSON system needs structured data. How should you configure logging?

- A) Use one formatter that outputs JSON everywhere
- B) Add two handlers to the same logger — StreamHandler with a human-readable formatter for console, and a SocketHandler with a JSON formatter for the logging system
- C) Use print statements for console and logging for the remote system
- D) Configure logging differently in development vs production code paths

**Answer: B**
**Explanation:** Python's logging system supports multiple handlers per logger. Each handler gets the same LogRecord but formats it differently. Console gets `%(levelname)s: %(message)s`, remote gets `json.dumps(...)`. This is the standard pattern — one logger, multiple handlers with different formatters. It avoids code duplication and keeps logging configuration centralized. Libraries like `structlog` make structured logging even cleaner.

---

## Question 10 (Architecture Decision)
You're designing a logging architecture for a distributed system with 50+ microservices. Logs must be searchable, correlated across services, and retained for 30 days. How should you architect this?

- A) Each service writes to local files and you SSH to read them
- B) Each service logs to stdout/stderr in structured JSON, collected by a log aggregator (Fluentd/Fluent Bit), shipped to Elasticsearch/OpenSearch, visualized in Kibana/Grafana
- C) Each service sends logs directly to the database
- D) Use print statements and aggregate with grep

**Answer: B**
**Explanation:** This is the standard ELK/EFK stack architecture. Services output structured JSON to stdout (12-factor app principle). A daemon like Fluentd or Fluent Bit collects, enriches (adds trace IDs, service names), and ships logs to Elasticsearch. Kibana provides search, dashboards, and alerting. Retention policies manage storage. This scales to thousands of services and provides centralized, correlated logging without each service needing to know about the logging infrastructure. Correlation IDs (from OpenTelemetry) link requests across services.

---
