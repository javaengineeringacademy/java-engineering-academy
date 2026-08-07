# Exception Handling in Python

> **Mastering errors is mastering the craft.**

## Why Exception Handling Matters

Every production application needs to handle unexpected situations gracefully — network timeouts, invalid user input, missing files, database failures. Exception handling is Python's mechanism for managing these runtime errors without crashing. Without it, you'd write brittle code that fails unpredictably and leaves no trace of what went wrong.

Without proper exception handling, you'd resort to return codes and sentinel values that make error handling optional and easy to forget. That's why exceptions exist — they provide a structured way to capture error context, propagate it up the call stack, and handle it at the appropriate level, turning potential crashes into manageable, loggable, recoverable events.

## What You'll Learn

By the end of this module, you'll be able to:

- Use try/except/else/finally blocks effectively
- Design custom exception hierarchies for your domain
- Chain exceptions to preserve root cause information
- Handle exceptions at the right abstraction level
- Write exception-safe code that doesn't leak resources

## When

| Scenario | Exception Strategy |
|----------|-------------------|
| File operations | `try/except` around I/O, catch `FileNotFoundError`, `PermissionError` |
| Network calls | Retry with exponential backoff, catch `ConnectionError`, `Timeout` |
| Database operations | Catch `IntegrityError`, `OperationalError`, rollback transactions |
| User input validation | Raise `ValueError` with descriptive message |
| API development | Global exception handler, map exceptions to HTTP status codes |
| Library development | Define custom exception hierarchy, never catch broad `Exception` |
| Configuration loading | Fail fast with clear error on invalid config |

## How

### try/except/else/finally

```python
def load_config(path: str) -> dict:
    """Load and validate configuration from file."""
    try:
        with open(path, 'r') as f:
            data = json.load(f)
    except FileNotFoundError:
        logger.warning(f"Config not found at {path}, using defaults")
        return get_default_config()
    except json.JSONDecodeError as e:
        raise ConfigError(f"Invalid JSON in {path}: {e}") from e
    else:
        validate_config(data)
        return data
    finally:
        log_config_attempt(path)
```

### Custom Exception Classes

```python
class AppError(Exception):
    """Base exception for all application errors."""
    def __init__(self, message: str, code: str = None, context: dict = None):
        super().__init__(message)
        self.code = code
        self.context = context or {}

class ValidationError(AppError):
    """Raised when input validation fails."""
    def __init__(self, field: str, message: str, value: any = None):
        super().__init__(
            f"Validation failed for '{field}': {message}",
            code="VALIDATION_ERROR",
            context={"field": field, "value": value}
        )
        self.field = field
        self.value = value

class NotFoundError(AppError):
    """Raised when a requested resource is not found."""
    def __init__(self, resource_type: str, resource_id: str):
        super().__init__(
            f"{resource_type} with id '{resource_id}' not found",
            code="NOT_FOUND",
            context={"resource_type": resource_type, "resource_id": resource_id}
        )
```

### Exception Chaining

```python
def process_payment(order_id: str, amount: float):
    """Process payment with proper exception chaining."""
    try:
        charge = stripe.Charge.create(amount=amount, currency="usd")
    except stripe.CardError as e:
        # Chain: original exception preserved as __cause__
        raise PaymentFailed(order_id, "Card declined") from e
    except stripe.APIConnectionError as e:
        # Chain: original exception preserved as __context__
        raise PaymentFailed(order_id, "Payment service unreachable") from e

# Using 'raise ... from None' to suppress context
def sanitize_error(user_input: str):
    try:
        return json.loads(user_input)
    except json.JSONDecodeError:
        # Don't expose internal error details to user
        raise ValueError("Invalid input format") from None
```

### Context Variables

```python
from contextvars import ContextVar
import uuid

# Context variables for request-scoped data
request_id: ContextVar[str] = ContextVar('request_id', default='')
user_id: ContextVar[str] = ContextVar('user_id', default='')

def create_request_context():
    """Set up context variables for a new request."""
    request_id.set(str(uuid.uuid4()))

def log_with_context(message: str):
    """Log with automatic context injection."""
    logger.info(f"[{request_id.get()}] [{user_id.get()}] {message}")
```

### Exception Hierarchy Design

```
AppError
├── ValidationError
│   ├── SchemaValidationError
│   └── BusinessRuleValidationError
├── NotFoundError
├── AuthenticationError
│   ├── InvalidCredentialsError
│   └── TokenExpiredError
├── AuthorizationError
├── PaymentError
│   ├── PaymentFailedError
│   └── InsufficientFundsError
└── ExternalServiceError
    ├── ServiceUnavailableError
    └── ServiceTimeoutError
```

### Production Exception Handler

```python
from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse

app = FastAPI()

@app.exception_handler(AppError)
async def app_error_handler(request: Request, exc: AppError):
    """Centralized exception handler for API."""
    logger.error(
        f"Application error: {exc.code}",
        extra={
            "code": exc.code,
            "context": exc.context,
            "path": request.url.path,
            "method": request.method
        },
        exc_info=True
    )
    return JSONResponse(
        status_code=exc.status_code,
        content={
            "error": {
                "code": exc.code,
                "message": str(exc),
                "context": exc.context
            }
        }
    )
```

## Production Checklist

- [ ] **Define a base exception class** for your application with code, context, and status
- [ ] **Never catch bare `Exception`** — catch specific exception types only
- [ ] **Always use `raise ... from ...`** to chain exceptions and preserve root cause
- [ ] **Log exceptions with full context** — include request ID, user ID, timestamps
- [ ] **Create an exception hierarchy** — at least 3 levels deep for large applications
- [ ] **Test exception paths** — every `except` block should have a corresponding test
- [ ] **Document exceptions in docstrings** — callers need to know what can be raised

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **Ad-hoc** | Bare `try/except Exception`, silent failures, no custom exceptions |
| 2 | **Aware** | Specific exception catches, basic custom exceptions, logging in except blocks |
| 3 | **Structured** | Exception hierarchy, exception chaining, context variables, centralized handlers |
| 4 | **Proactive** | Exception testing, error budgets, circuit breakers, retry policies |
| 5 | **Resilient** | Self-healing patterns, chaos engineering, exception metrics dashboards |

## Common Myths

### Myth 1: "Catching all exceptions is safer"
**Reality:** Catching broad `Exception` hides bugs. A `TypeError` or `AttributeError` is a programming error, not an expected condition. Catch what you can handle; let the rest propagate.

### Myth 2: "Exceptions are slow, use return codes"
**Reality:** In Python, exceptions are faster than return codes for the common (non-error) path. The try block has zero cost when no exception occurs. Optimize for the happy path.

### Myth 3: "Finally blocks always run"
**Reality:** `finally` blocks do NOT run if the process is killed (SIGKILL, `sys.exit()`, or interpreter crash). They also don't run if the `except` block raises a new exception. Plan for cleanup accordingly.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Basic try/except | `try: ... except Error as e: ...` | Catch specific errors |
| Else clause | `try: ... except: ... else: ...` | Runs if no exception |
| Finally clause | `try: ... finally: ...` | Cleanup code, always runs |
| Raise | `raise ValueError("msg")` | Explicitly raise exception |
| Chain exceptions | `raise NewError() from old_error` | Preserve root cause |
| Suppress context | `raise ValueError() from None` | Hide internal details |
| Custom exception | `class MyError(Exception): ...` | Domain-specific errors |
| Exception groups | `raise ExceptionGroup("msg", [e1, e2])` | Multiple exceptions (3.11+) |
| Except* syntax | `except* ValueError as eg:` | Handle exception groups |

## Related Topics

- [01-basics](../01-basics/) - Python fundamentals
- [10-decorators](../10-decorators/) - Decorator patterns
- [13-logging](../13-logging/) - Structured logging with exceptions
- [15-async](../15-async/) - Exception handling in async code
- [18-senior](../18-senior/) - Production error handling patterns

---

> **Remember:** Exception handling isn't about preventing errors — it's about handling them gracefully when they inevitably occur.
