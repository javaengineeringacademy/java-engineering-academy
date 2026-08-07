"""
Structured Logging in Python
Demonstrates structured logging techniques for better observability
"""

import logging
import json
import time
from typing import Dict, Any
from datetime import datetime

# ============================================
# JSON Formatter
# ============================================

class JSONFormatter(logging.Formatter):
    """Custom JSON formatter for structured logging."""
    
    def format(self, record: logging.LogRecord) -> str:
        """Format log record as JSON."""
        log_data = {
            "timestamp": datetime.fromtimestamp(record.created).isoformat(),
            "level": record.levelname,
            "logger": record.name,
            "message": record.getMessage(),
            "module": record.module,
            "function": record.funcName,
            "line": record.lineno,
        }
        
        # Add exception info if present
        if record.exc_info:
            log_data["exception"] = {
                "type": record.exc_info[0].__name__,
                "message": str(record.exc_info[1]),
                "traceback": self.formatException(record.exc_info)
            }
        
        # Add extra fields
        if hasattr(record, 'extra_data'):
            log_data["extra"] = record.extra_data
        
        return json.dumps(log_data)

# ============================================
# Context Logger
# ============================================

class ContextLogger:
    """Logger with context information."""
    
    def __init__(self, name: str) -> None:
        self.logger = logging.getLogger(name)
        self.context: Dict[str, Any] = {}
    
    def set_context(self, **kwargs) -> None:
        """Set context for all subsequent log messages."""
        self.context.update(kwargs)
    
    def clear_context(self) -> None:
        """Clear all context."""
        self.context.clear()
    
    def _log(self, level: int, message: str, **kwargs) -> None:
        """Log with context."""
        extra = {**self.context, **kwargs}
        
        # Create log record
        record = self.logger.makeRecord(
            self.logger.name, level, "(context)", 0, message, (), None
        )
        record.extra_data = extra
        
        self.logger.handle(record)
    
    def info(self, message: str, **kwargs) -> None:
        """Log info level."""
        self._log(logging.INFO, message, **kwargs)
    
    def error(self, message: str, **kwargs) -> None:
        """Log error level."""
        self._log(logging.ERROR, message, **kwargs)
    
    def warning(self, message: str, **kwargs) -> None:
        """Log warning level."""
        self._log(logging.WARNING, message, **kwargs)
    
    def debug(self, message: str, **kwargs) -> None:
        """Log debug level."""
        self._log(logging.DEBUG, message, **kwargs)

# ============================================
# Performance Logger
# ============================================

class PerformanceLogger:
    """Logger for performance metrics."""
    
    def __init__(self, name: str) -> None:
        self.logger = logging.getLogger(name)
        self.timers: Dict[str, float] = {}
    
    def start_timer(self, operation: str) -> None:
        """Start a timer for an operation."""
        self.timers[operation] = time.time()
    
    def end_timer(self, operation: str) -> float:
        """End a timer and log the duration."""
        if operation not in self.timers:
            return 0.0
        
        duration = time.time() - self.timers[operation]
        del self.timers[operation]
        
        self.logger.info(
            f"Performance: {operation}",
            extra={"extra_data": {"operation": operation, "duration_ms": duration * 1000}}
        )
        
        return duration

# ============================================
# Request Logger
# ============================================

class RequestLogger:
    """Logger for HTTP requests."""
    
    def __init__(self, name: str) -> None:
        self.logger = logging.getLogger(name)
    
    def log_request(self, method: str, path: str, status: int, duration: float) -> None:
        """Log an HTTP request."""
        level = logging.INFO if status < 400 else logging.WARNING
        
        self.logger.log(
            level,
            f"{method} {path} - {status}",
            extra={
                "extra_data": {
                    "http_method": method,
                    "path": path,
                    "status_code": status,
                    "duration_ms": duration * 1000,
                    "level": "INFO" if status < 400 else "WARNING"
                }
            }
        )

# ============================================
# Audit Logger
# ============================================

class AuditLogger:
    """Logger for audit trail."""
    
    def __init__(self, name: str) -> None:
        self.logger = logging.getLogger(name)
    
    def log_action(self, user: str, action: str, resource: str, result: str) -> None:
        """Log an auditable action."""
        self.logger.info(
            f"User {user} performed {action} on {resource}",
            extra={
                "extra_data": {
                    "user": user,
                    "action": action,
                    "resource": resource,
                    "result": result,
                    "audit": True
                }
            }
        )

# ============================================
# Setup Structured Logging
# ============================================

def setup_structured_logging() -> None:
    """Configure structured logging."""
    logger = logging.getLogger()
    logger.setLevel(logging.DEBUG)
    
    # Console handler with JSON format
    handler = logging.StreamHandler()
    handler.setLevel(logging.DEBUG)
    handler.setFormatter(JSONFormatter())
    
    logger.addHandler(handler)

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    setup_structured_logging()
    
    print("=== JSON Structured Logging ===")
    logger = logging.getLogger("demo")
    logger.info("Application started", extra={"extra_data": {"version": "1.0.0"}})
    logger.warning("Low memory", extra={"extra_data": {"memory_mb": 512}})
    
    print("\n=== Context Logger ===")
    context_logger = ContextLogger("context_demo")
    context_logger.set_context(user_id="123", session_id="abc-456")
    
    context_logger.info("User logged in")
    context_logger.info("Profile updated")
    context_logger.clear_context()
    
    print("\n=== Performance Logger ===")
    perf_logger = PerformanceLogger("perf_demo")
    
    perf_logger.start_timer("database_query")
    time.sleep(0.1)  # Simulate work
    perf_logger.end_timer("database_query")
    
    perf_logger.start_timer("api_call")
    time.sleep(0.05)  # Simulate work
    perf_logger.end_timer("api_call")
    
    print("\n=== Request Logger ===")
    req_logger = RequestLogger("request_demo")
    
    req_logger.log_request("GET", "/api/users", 200, 0.025)
    req_logger.log_request("POST", "/api/users", 201, 0.150)
    req_logger.log_request("GET", "/api/users/999", 404, 0.010)
    
    print("\n=== Audit Logger ===")
    audit_logger = AuditLogger("audit_demo")
    
    audit_logger.log_action("admin", "create", "user:123", "success")
    audit_logger.log_action("user456", "read", "document:456", "success")
    audit_logger.log_action("user789", "delete", "document:789", "denied")
