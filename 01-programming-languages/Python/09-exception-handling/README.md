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

## Interview Questions

### Q1: What is the difference between `except Exception` and `except`?
**Answer:** `except Exception` catches all exceptions except SystemExit, KeyboardInterrupt. Bare `except` catches everything including SystemExit. Always use `except Exception`.

### Q2: What is exception chaining?
**Answer:** Using `raise NewException() from original_exception` preserves the original traceback. The `__cause__` attribute links exceptions.

### Q3: What is the difference between `finally` and `else`?
**Answer:** `finally` runs always (exception or not). `else` runs only if no exception occurred. Use else for code that shouldn't run on exception.

### Q4: What is a custom exception and when to create one?
**Answer:** Custom exceptions inherit from Exception. Create when you need specific error types for your domain (PaymentError, ValidationError).

### Q5: What is the EAFP principle?
**Answer:** Easier to Ask Forgiveness than Permission. Try the operation and catch exceptions rather than checking conditions first. More Pythonic.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Network calls, file operations, user input, database queries | Simple scripts with known inputs |
| When NOT to use | Don't catch bare `Exception`; don't use exceptions for control flow | Use `if` checks for expected conditions |
| Alternatives | Return codes for simple cases, Result types for functional style | Explicit checks before operations |
| Production Examples | API development, data pipelines, financial systems | Quick scripts, prototypes |
| Common Mistakes | Catching too broad, not chaining exceptions, swallowing errors | Catch specific exceptions; chain with `from` |

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

## Production Incidents

### Incident 1: Catching Too Broad Exception Hiding Bugs

**Problem:** Application silently failed to process payments
**Cause:** `except Exception` caught `TypeError` in payment logic; returned `None`
**Impact:** 200 payments silently dropped; customers charged but not processed
**Detection:** Reconciliation found mismatch between charges and orders
**Solution:**
```python
# BAD: Catches everything
try:
    result = process_payment(order)
except Exception:
    return None  # Hides TypeError!

# GOOD: Catch specific exceptions
try:
    result = process_payment(order)
except PaymentError as e:
    logger.error(f"Payment failed: {e}")
    raise
except ValueError as e:
    logger.error(f"Invalid payment data: {e}")
    raise
```
**Prevention:** Catch specific exceptions; log and re-raise; never silently swallow errors

### Incident 2: Exception Chain Lost in Production

**Problem:** Production error showed generic "Connection failed" without root cause
**Cause:** `raise NewError() from None` suppressed original exception context
**Impact:** 3-hour debugging session to find actual cause (DNS resolution failure)
**Detection:** Customer support tickets about connection errors
**Solution:**
```python
# BAD: Suppresses context
try:
    connect_to_db()
except ConnectionError:
    raise ServiceError("Connection failed") from None

# GOOD: Preserves context
try:
    connect_to_db()
except ConnectionError as e:
    raise ServiceError("Connection failed") from e
```
**Prevention:** Use `raise ... from e` to chain exceptions; log full traceback; include context in error messages

### Incident 3: Finally Block Not Running on SIGTERM

**Problem:** Graceful shutdown didn't flush pending writes
**Cause:** `finally` block doesn't run on SIGTERM; process killed immediately
**Impact:** 1000 log entries lost during deployment
**Detection:** Missing log entries in monitoring
**Solution:**
```python
import signal

def signal_handler(signum, frame):
    flush_pending_writes()
    sys.exit(0)

signal.signal(signal.SIGTERM, signal_handler)

# Don't rely on finally for cleanup
# Use signal handlers for SIGTERM/SIGINT
```
**Prevention:** Register signal handlers for SIGTERM/SIGINT; don't rely on `finally` for critical cleanup; use atexit for final cleanup

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

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| `except Exception` hiding real bug | Remove broad catches; catch specific types | Catch `ValueError`, `KeyError`, etc. specifically; log and re-raise |
| Exception chain lost in production | Use `raise ... from e` always | Chain exceptions with `from`; use `from None` only to hide internal details |
| `finally` not running on SIGTERM | Register `signal.signal(SIGTERM, handler)` | Don't rely on `finally` for critical cleanup; use signal handlers and `atexit` |
| `logger.exception()` outside `except` block | Use `exc_info=True` explicitly | Call `logger.exception()` only inside `except`; use `exc_info=True` elsewhere |
| Exception group handling in asyncio | `except*` syntax (Python 3.11+) | Use `ExceptionGroup` for concurrent errors; handle with `except*` |

## Code Review Checklist

- [ ] No bare `except:` clauses; always catch specific exception types
- [ ] `raise ... from e` used to chain exceptions and preserve root cause
- [ ] Custom exception hierarchy defined with base `AppError` class
- [ ] Every `except` block has a corresponding test
- [ ] Exceptions logged with full context (request ID, user ID, timestamps)
- [ ] `finally` blocks used for resource cleanup, not `__del__`
- [ ] No exceptions used for normal control flow

## Architecture Considerations

Exception handling determines system resilience. A well-designed exception hierarchy maps to domain errors, enabling precise error handling at appropriate abstraction levels. Exception chaining preserves root cause information for debugging. Centralized exception handlers ensure consistent error responses in APIs.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Custom exception hierarchy | Domain-specific error handling | Clear semantics but requires upfront design |
| Exception chaining with `from` | Preserving root cause | Debugging-friendly but adds exception nesting |
| Centralized exception handler | API error responses | Consistent but may hide specific error context |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Exception message leaking internal details | Information disclosure in API responses | Sanitize exception messages; log full details server-side only |
| `except` swallowing security errors | Security vulnerabilities undetected | Never silently catch `PermissionError`, `AuthenticationError` |
| Exception in `finally` masking original error | Data loss or inconsistent state | Keep `finally` blocks simple; log secondary exceptions |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.11+ | `ExceptionGroup` and `except*` | Adopt for concurrent error handling in asyncio |
| Python 3.12+ | Improved exception messages | Upgrade for better debugging; no code changes needed |
| Python 3.13+ | Free-threaded exception handling | Test exception behavior in free-threaded mode |

## Version Validation

| Feature | Python Version | Status |
|---------|---------------|--------|
| Exception chaining (`from`) | 3.0+ | Stable, always use `from` |
| `ExceptionGroup` | 3.11+ | Stable, concurrent error handling |
| `except*` syntax | 3.11+ | Stable, handle exception groups |
| `except Exception` (best practice) | All versions | Stable, catch specific types |
