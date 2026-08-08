"""
Module 13 - Logging: Structured Logging Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Structured and JSON logging
"""

import logging
import json
from datetime import datetime


# =============================================================================
# Exercise 1: JSON Formatter (⭐⭐⭐)
# =============================================================================

class JSONFormatter(logging.Formatter):
    """
    Custom formatter that outputs JSON.
    
    TODO:
    1. Override format() method
    2. Convert log record to JSON
    3. Include timestamp, level, message, and extra fields
    """
    def format(self, record):
        # TODO: Create JSON formatted string
        pass


# =============================================================================
# Exercise 2: Context Logger (⭐⭐⭐⭐)
# =============================================================================

class ContextLogger:
    """
    Logger that adds context to all messages.
    
    TODO:
    1. Accept context dict in constructor
    2. Merge context with each log message
    3. Support adding/removing context
    """
    def __init__(self, logger, context=None):
        self.logger = logger
        self.context = context or {}
    
    def add_context(self, key, value):
        # TODO: Add key-value to context
        pass
    
    def remove_context(self, key):
        # TODO: Remove key from context
        pass
    
    def info(self, message, **kwargs):
        # TODO: Log with context
        pass
    
    def error(self, message, **kwargs):
        # TODO: Log error with context
        pass


# =============================================================================
# Exercise 3: Request Logger (⭐⭐⭐⭐)
# =============================================================================

class RequestLogger:
    """
    Logger for tracking HTTP requests.
    
    TODO:
    1. Log request method, URL, status code
    2. Log response time
    3. Support request ID tracking
    """
    def __init__(self, logger):
        self.logger = logger
    
    def log_request(self, method, url, request_id=None):
        # TODO: Log incoming request
        pass
    
    def log_response(self, method, url, status_code, duration, request_id=None):
        # TODO: Log response with timing
        pass


# =============================================================================
# Exercise 4: Metrics Logger (⭐⭐⭐⭐)
# =============================================================================

class MetricsLogger:
    """
    Logger for application metrics.
    
    TODO:
    1. Log counter metrics
    2. Log gauge metrics
    3. Log histogram metrics
    """
    def __init__(self, logger):
        self.logger = logger
    
    def counter(self, name, value=1, tags=None):
        # TODO: Log counter metric
        pass
    
    def gauge(self, name, value, tags=None):
        # TODO: Log gauge metric
        pass
    
    def histogram(self, name, value, tags=None):
        # TODO: Log histogram metric
        pass


# =============================================================================
# Exercise 5: Audit Logger (⭐⭐⭐⭐⭐)
# =============================================================================

class AuditLogger:
    """
    Logger for security audit events.
    
    TODO:
    1. Log user actions
    2. Include timestamp, user, action, resource
    3. Support success/failure status
    """
    def __init__(self, logger):
        self.logger = logger
    
    def log_action(self, user, action, resource, success=True, details=None):
        # TODO: Log audit event
        pass
    
    def log_login(self, user, success=True, ip_address=None):
        # TODO: Log login attempt
        pass
    
    def log_access(self, user, resource, granted=True):
        # TODO: Log access attempt
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 13 - Structured Logging Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: JSON Formatter")
    try:
        formatter = JSONFormatter()
        assert formatter is not None, "Formatter should be created"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Context Logger")
    try:
        logger = logging.getLogger('test_context')
        context_logger = ContextLogger(logger, {'app': 'test'})
        context_logger.add_context('user', 'testuser')
        assert 'user' in context_logger.context
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Request Logger")
    try:
        logger = logging.getLogger('test_request')
        request_logger = RequestLogger(logger)
        assert request_logger.logger is not None
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Metrics Logger")
    try:
        logger = logging.getLogger('test_metrics')
        metrics_logger = MetricsLogger(logger)
        assert metrics_logger.logger is not None
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Audit Logger")
    try:
        logger = logging.getLogger('test_audit')
        audit_logger = AuditLogger(logger)
        assert audit_logger.logger is not None
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
