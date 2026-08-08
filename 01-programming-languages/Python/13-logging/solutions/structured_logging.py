"""
Module 13 - Logging: Structured Logging Solutions
Complete solutions with explanations
"""

import logging
import json
from datetime import datetime


# =============================================================================
# Exercise 1: JSON Formatter - SOLUTION
# =============================================================================

class JSONFormatter(logging.Formatter):
    """
    Custom formatter that outputs JSON.
    
    JSON logging is useful for log aggregation systems like ELK stack.
    """
    def format(self, record):
        log_data = {
            'timestamp': datetime.utcnow().isoformat(),
            'level': record.levelname,
            'logger': record.name,
            'message': record.getMessage(),
            'module': record.module,
            'function': record.funcName,
            'line': record.lineno,
        }
        
        # Add extra fields if present
        if hasattr(record, 'extra_data'):
            log_data['extra'] = record.extra_data
        
        # Add exception info if present
        if record.exc_info:
            log_data['exception'] = self.formatException(record.exc_info)
        
        return json.dumps(log_data)


# =============================================================================
# Exercise 2: Context Logger - SOLUTION
# =============================================================================

class ContextLogger:
    """
    Logger that adds context to all messages.
    
    Context is useful for adding request IDs, user IDs, etc.
    """
    def __init__(self, logger, context=None):
        self.logger = logger
        self.context = context or {}
    
    def add_context(self, key, value):
        """Add key-value to context."""
        self.context[key] = value
    
    def remove_context(self, key):
        """Remove key from context."""
        if key in self.context:
            del self.context[key]
    
    def _log_with_context(self, level, message, **kwargs):
        """Log message with merged context."""
        extra = {**self.context, **kwargs}
        self.logger.log(level, message, extra=extra)
    
    def debug(self, message, **kwargs):
        self._log_with_context(logging.DEBUG, message, **kwargs)
    
    def info(self, message, **kwargs):
        self._log_with_context(logging.INFO, message, **kwargs)
    
    def warning(self, message, **kwargs):
        self._log_with_context(logging.WARNING, message, **kwargs)
    
    def error(self, message, **kwargs):
        self._log_with_context(logging.ERROR, message, **kwargs)
    
    def critical(self, message, **kwargs):
        self._log_with_context(logging.CRITICAL, message, **kwargs)


# =============================================================================
# Exercise 3: Request Logger - SOLUTION
# =============================================================================

class RequestLogger:
    """
    Logger for tracking HTTP requests.
    
    Useful for monitoring API performance and debugging.
    """
    def __init__(self, logger):
        self.logger = logger
    
    def log_request(self, method, url, request_id=None):
        """Log incoming request."""
        extra = {
            'request_method': method,
            'request_url': url,
        }
        if request_id:
            extra['request_id'] = request_id
        
        self.logger.info(f"Incoming request: {method} {url}", extra=extra)
    
    def log_response(self, method, url, status_code, duration, request_id=None):
        """Log response with timing."""
        extra = {
            'request_method': method,
            'request_url': url,
            'response_code': status_code,
            'duration_ms': round(duration * 1000, 2),
        }
        if request_id:
            extra['request_id'] = request_id
        
        level = logging.INFO if status_code < 400 else logging.WARNING
        self.logger.log(level, f"Response: {status_code} ({duration:.3f}s)", extra=extra)


# =============================================================================
# Exercise 4: Metrics Logger - SOLUTION
# =============================================================================

class MetricsLogger:
    """
    Logger for application metrics.
    
    Metrics are useful for monitoring application health and performance.
    """
    def __init__(self, logger):
        self.logger = logger
    
    def counter(self, name, value=1, tags=None):
        """Log counter metric."""
        metric = {
            'type': 'counter',
            'name': name,
            'value': value,
            'tags': tags or {},
            'timestamp': datetime.utcnow().isoformat(),
        }
        self.logger.info(f"METRIC: {name} +{value}", extra={'metric': metric})
    
    def gauge(self, name, value, tags=None):
        """Log gauge metric."""
        metric = {
            'type': 'gauge',
            'name': name,
            'value': value,
            'tags': tags or {},
            'timestamp': datetime.utcnow().isoformat(),
        }
        self.logger.info(f"METRIC: {name} = {value}", extra={'metric': metric})
    
    def histogram(self, name, value, tags=None):
        """Log histogram metric."""
        metric = {
            'type': 'histogram',
            'name': name,
            'value': value,
            'tags': tags or {},
            'timestamp': datetime.utcnow().isoformat(),
        }
        self.logger.info(f"METRIC: {name} {value}", extra={'metric': metric})


# =============================================================================
# Exercise 5: Audit Logger - SOLUTION
# =============================================================================

class AuditLogger:
    """
    Logger for security audit events.
    
    Audit logs are critical for security and compliance.
    """
    def __init__(self, logger):
        self.logger = logger
    
    def log_action(self, user, action, resource, success=True, details=None):
        """Log audit event."""
        audit_data = {
            'event_type': 'action',
            'user': user,
            'action': action,
            'resource': resource,
            'success': success,
            'timestamp': datetime.utcnow().isoformat(),
        }
        if details:
            audit_data['details'] = details
        
        level = logging.INFO if success else logging.WARNING
        self.logger.log(level, f"AUDIT: {user} {action} {resource}", extra=audit_data)
    
    def log_login(self, user, success=True, ip_address=None):
        """Log login attempt."""
        audit_data = {
            'event_type': 'login',
            'user': user,
            'success': success,
            'ip_address': ip_address,
            'timestamp': datetime.utcnow().isoformat(),
        }
        
        level = logging.INFO if success else logging.WARNING
        status = "successful" if success else "failed"
        self.logger.log(level, f"AUDIT: Login {status} for {user}", extra=audit_data)
    
    def log_access(self, user, resource, granted=True):
        """Log access attempt."""
        audit_data = {
            'event_type': 'access',
            'user': user,
            'resource': resource,
            'granted': granted,
            'timestamp': datetime.utcnow().isoformat(),
        }
        
        level = logging.INFO if granted else logging.WARNING
        status = "granted" if granted else "denied"
        self.logger.log(level, f"AUDIT: Access {status} for {user} to {resource}", extra=audit_data)


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 13 - Structured Logging Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: JSON Formatter")
    formatter = JSONFormatter()
    record = logging.LogRecord(
        name='test', level=logging.INFO, pathname='', lineno=0,
        msg='Test message', args=(), exc_info=None
    )
    output = formatter.format(record)
    data = json.loads(output)
    assert data['level'] == 'INFO'
    assert data['message'] == 'Test message'
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Context Logger")
    logger = logging.getLogger('test_context')
    context_logger = ContextLogger(logger, {'app': 'test'})
    context_logger.add_context('user', 'testuser')
    assert 'user' in context_logger.context
    context_logger.remove_context('user')
    assert 'user' not in context_logger.context
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Request Logger")
    logger = logging.getLogger('test_request')
    request_logger = RequestLogger(logger)
    request_logger.log_request('GET', '/api/users', 'req-123')
    request_logger.log_response('GET', '/api/users', 200, 0.05, 'req-123')
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Metrics Logger")
    logger = logging.getLogger('test_metrics')
    metrics_logger = MetricsLogger(logger)
    metrics_logger.counter('requests', 1, {'method': 'GET'})
    metrics_logger.gauge('queue_size', 42)
    metrics_logger.histogram('response_time', 0.15)
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Audit Logger")
    logger = logging.getLogger('test_audit')
    audit_logger = AuditLogger(logger)
    audit_logger.log_action('admin', 'DELETE', '/api/users/123')
    audit_logger.log_login('user1', success=True, ip_address='192.168.1.1')
    audit_logger.log_access('user1', '/admin', granted=False)
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
