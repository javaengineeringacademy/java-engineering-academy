"""
Module 13: Logging - Structured Logging Solutions
Practice structured logging patterns.
"""

import json
import logging
from datetime import datetime
from typing import Any, Dict


class StructuredFormatter(logging.Formatter):
    """Formatter that outputs JSON structured logs."""

    def format(self, record):
        log_entry = {
            "timestamp": datetime.now().isoformat(),
            "level": record.levelname,
            "logger": record.name,
            "message": record.getMessage(),
            "module": record.module,
            "function": record.funcName,
            "line": record.lineno
        }

        if hasattr(record, 'extra_data'):
            log_entry["data"] = record.extra_data

        if record.exc_info:
            log_entry["exception"] = self.formatException(record.exc_info)

        return json.dumps(log_entry)


class ContextFilter(logging.Filter):
    """Filter that adds context to log records."""

    def __init__(self, **context):
        super().__init__()
        self.context = context

    def filter(self, record):
        for key, value in self.context.items():
            setattr(record, key, value)
        return True


class JSONLogger:
    """Logger that outputs structured JSON."""

    def __init__(self, name: str):
        self.logger = logging.getLogger(name)
        self.logger.setLevel(logging.DEBUG)

        # Console handler
        handler = logging.StreamHandler()
        handler.setFormatter(StructuredFormatter())
        self.logger.addHandler(handler)

    def info(self, message: str, **data):
        extra = {"extra_data": data} if data else {}
        self.logger.info(message, extra=extra)

    def error(self, message: str, **data):
        extra = {"extra_data": data} if data else {}
        self.logger.error(message, extra=extra)

    def warning(self, message: str, **data):
        extra = {"extra_data": data} if data else {}
        self.logger.warning(message, extra=extra)

    def debug(self, message: str, **data):
        extra = {"extra_data": data} if data else {}
        self.logger.debug(message, extra=extra)


class RequestLogger:
    """Logger that tracks request context."""

    def __init__(self, logger: logging.Logger):
        self.logger = logger
        self.context: Dict[str, Any] = {}

    def set_context(self, **context):
        """Set context for all subsequent log messages."""
        self.context.update(context)

    def clear_context(self):
        """Clear all context."""
        self.context.clear()

    def log(self, level: str, message: str, **data):
        """Log a message with context."""
        combined_data = {**self.context, **data}
        extra = {"extra_data": combined_data} if combined_data else {}

        log_func = getattr(self.logger, level.lower())
        log_func(message, extra=extra)


class AuditLogger:
    """Logger for audit trails."""

    def __init__(self, name: str):
        self.logger = logging.getLogger(name)
        self.logger.setLevel(logging.INFO)

        handler = logging.StreamHandler()
        handler.setFormatter(StructuredFormatter())
        self.logger.addHandler(handler)

    def audit(self, action: str, user: str, resource: str, **details):
        """Log an audit event."""
        audit_data = {
            "action": action,
            "user": user,
            "resource": resource,
            "timestamp": datetime.now().isoformat(),
            **details
        }
        self.logger.info(f"Audit: {action}", extra={"extra_data": audit_data})


if __name__ == "__main__":
    print("Testing Structured Logging Solutions...")

    # Test JSON Logger
    json_logger = JSONLogger("test_json")
    json_logger.info("Test message", user="alice", action="login")
    print("✓ Exercise 1 passed: JSON logger works")

    # Test Context Filter
    logger = logging.getLogger("context_test")
    logger.setLevel(logging.DEBUG)
    handler = logging.StreamHandler()
    handler.setFormatter(StructuredFormatter())
    logger.addHandler(handler)

    context_filter = ContextFilter(request_id="123", session="abc")
    logger.addFilter(context_filter)
    logger.info("Message with context")
    print("✓ Exercise 2 passed: context filter works")

    # Test Request Logger
    base_logger = logging.getLogger("request_test")
    base_logger.setLevel(logging.DEBUG)
    handler = logging.StreamHandler()
    handler.setFormatter(StructuredFormatter())
    base_logger.addHandler(handler)

    request_logger = RequestLogger(base_logger)
    request_logger.set_context(user="bob", request_id="456")
    request_logger.log("INFO", "Processing request")
    request_logger.clear_context()
    print("✓ Exercise 3 passed: request logger works")

    # Test Audit Logger
    audit_logger = AuditLogger("audit")
    audit_logger.audit("login", "alice", "auth_service", ip="192.168.1.1")
    audit_logger.audit("read", "bob", "database", table="users")
    print("✓ Exercise 4 passed: audit logger works")

    print("All Structured Logging solutions passed!")
