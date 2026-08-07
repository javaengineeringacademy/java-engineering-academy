# Python Logging Reference

## What is logging?

The logging module provides a flexible framework for emitting log messages from Python programs. It's essential for debugging, monitoring, and auditing applications.

## Why does logging matter?

Understanding logging helps you:
- Debug applications effectively
- Monitor application behavior
- Track errors and issues
- Maintain audit trails

---

## 1. Basic Logging

```python
import logging

# Basic configuration
logging.basicConfig(level=logging.INFO)

# Log messages
logging.debug('Debug message')
logging.info('Info message')
logging.warning('Warning message')
logging.error('Error message')
logging.critical('Critical message')
```

---

## 2. Log Levels

```python
import logging

# Log levels (in order of severity)
# DEBUG: Detailed information, typically of interest only when diagnosing problems
# INFO: Confirmation that things are working as expected
# WARNING: An indication that something unexpected happened
# ERROR: Due to a more serious problem, the software has not been able to perform a function
# CRITICAL: A serious error, indicating that the program itself may be unable to continue running

logging.debug('Debug')      # 10
logging.info('Info')        # 20
logging.warning('Warning')  # 30
logging.error('Error')      # 40
logging.critical('Critical')  # 50
```

---

## 3. Logger Objects

```python
import logging

# Create logger
logger = logging.getLogger('my_app')
logger.setLevel(logging.DEBUG)

# Create handler
handler = logging.StreamHandler()
handler.setLevel(logging.INFO)

# Create formatter
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)

# Add handler to logger
logger.addHandler(handler)

# Use logger
logger.debug('Debug message')
logger.info('Info message')
logger.warning('Warning message')
```

---

## 4. Handlers

```python
import logging

# StreamHandler (console)
console_handler = logging.StreamHandler()

# FileHandler
file_handler = logging.FileHandler('app.log')

# RotatingFileHandler
from logging.handlers import RotatingFileHandler
rotating_handler = RotatingFileHandler('app.log', maxBytes=1024*1024, backupCount=5)

# TimedRotatingFileHandler
from logging.handlers import TimedRotatingFileHandler
timed_handler = TimedRotatingFileHandler('app.log', when='midnight', interval=1, backupCount=30)

# SysLogHandler
from logging.handlers import SysLogHandler
syslog_handler = SysLogHandler(address='/dev/log')

# SMTPHandler
from logging.handlers import SMTPHandler
smtp_handler = SMTPHandler(
    mailhost='smtp.example.com',
    fromaddr='from@example.com',
    toaddrs=['to@example.com'],
    subject='Application Error'
)
```

---

## 5. Formatters

```python
import logging

# Basic formatter
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

# With date format
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                              datefmt='%Y-%m-%d %H:%M:%S')

# Common format attributes
# %(name)s - Logger name
# %(levelname)s - Log level
# %(asctime)s - Timestamp
# %(message)s - Log message
# %(filename)s - Filename
# %(lineno)d - Line number
# %(funcName)s - Function name
# %(thread)d - Thread ID
# %(process)d - Process ID
```

---

## 6. Configuration

```python
import logging
import logging.config

# Dictionary configuration
config = {
    'version': 1,
    'disable_existing_loggers': False,
    'formatters': {
        'simple': {
            'format': '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
        },
    },
    'handlers': {
        'console': {
            'class': 'logging.StreamHandler',
            'level': 'INFO',
            'formatter': 'simple',
            'stream': 'ext://sys.stdout',
        },
        'file': {
            'class': 'logging.FileHandler',
            'level': 'DEBUG',
            'formatter': 'simple',
            'filename': 'app.log',
        },
    },
    'root': {
        'level': 'INFO',
        'handlers': ['console', 'file'],
    },
}

logging.config.dictConfig(config)
```

---

## 7. Exceptions

```python
import logging

try:
    1 / 0
except Exception as e:
    logging.error('An error occurred', exc_info=True)
    # or
    logging.exception('An error occurred')
```

---

## One-Minute Revision Table

| Component | Description | Example |
|-----------|-------------|---------|
| **Logger** | Interface for logging | `logging.getLogger('name')` |
| **Handler** | Send log to destination | `logging.StreamHandler()` |
| **Formatter** | Format log messages | `logging.Formatter('format')` |
| **Filter** | Filter log messages | `logger.addFilter(filter)` |
| **Level** | Log severity | `logging.INFO` |

---

## Common Mistakes

### 1. Not Configuring Logging

```python
# WRONG
import logging
logging.info('Info message')  # No output

# RIGHT
import logging
logging.basicConfig(level=logging.INFO)
logging.info('Info message')
```

### 2. Using print Instead of logging

```python
# WRONG
print('Starting application')

# RIGHT
logging.info('Starting application')
```

### 3. Not Using Logger Hierarchy

```python
# WRONG
logger1 = logging.getLogger('app')
logger2 = logging.getLogger('app.module')

# RIGHT (use hierarchy)
logger = logging.getLogger('app')
module_logger = logging.getLogger('app.module')
```

---

## Production Notes

1. **Use appropriate log levels** - Don't log everything at INFO
2. **Use structured logging** - JSON format for machine parsing
3. **Use log rotation** - Prevent disk space issues
4. **Use logger hierarchy** - Organize logs by module
5. **Don't log sensitive data** - Passwords, tokens, etc.
6. **Use exception logging** - Include stack traces
7. **Use context for logs** - Request ID, user ID, etc.
8. **Test logging configuration** - Ensure logs are captured
9. **Use logging.config.dictConfig** - For complex configurations
10. **Monitor log output** - Use tools like ELK stack

---

## Further Reading

- Python documentation on logging module
- Python logging HOWTO
- logging.config documentation
