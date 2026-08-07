"""
Module 13: Logging - Logging Setup Exercises
============================================
Practice configuring Python's logging module.
"""

import logging
import tempfile
import os

# =============================================================================
# Exercise 1: Basic Logger Setup (★☆☆☆☆)
# =============================================================================
# TODO: Configure logger with file and console handlers

def setup_logger(name, log_file, level=logging.DEBUG):
    """Configure and return a logger with file and console handlers."""
    # TODO: Create logger, add handlers, set formatters
    pass

# Test Cases
def test_basic_logger():
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.log') as f:
        temp_path = f.name
    
    try:
        logger = setup_logger("test_logger", temp_path)
        logger.info("Test message")
        
        with open(temp_path) as f:
            content = f.read()
        assert "Test message" in content
        print("✓ Exercise 1 passed: logger writes to file")
    finally:
        os.unlink(temp_path)

# =============================================================================
# Exercise 2: Log Rotation (★★☆☆☆)
# =============================================================================
# TODO: Configure rotating file handler

def setup_rotating_logger(name, log_file, max_bytes=1024, backup_count=3):
    """Configure logger with rotating file handler."""
    # TODO: Use RotatingFileHandler
    pass

# Test Tests
def test_rotating_logger():
    with tempfile.TemporaryDirectory() as tmpdir:
        log_file = os.path.join(tmpdir, "app.log")
        logger = setup_rotating_logger("rotating", log_file, max_bytes=100, backup_count=2)
        
        # Write enough to trigger rotation
        for i in range(50):
            logger.info(f"Message {i}" * 10)
        
        # Should have created backup files
        files = os.listdir(tmpdir)
        assert len(files) > 1
        print(f"✓ Exercise 2 passed: rotation created {len(files)} files")

# =============================================================================
# Exercise 3: Custom Formatter (★★★☆☆)
# =============================================================================
# TODO: Create custom log formatter

class CustomFormatter(logging.Formatter):
    """Custom formatter with color and timestamp."""
    # TODO: Override format method
    pass

# Test Cases
def test_custom_formatter():
    formatter = CustomFormatter()
    record = logging.LogRecord(
        name="test", level=logging.INFO, pathname="", lineno=0,
        msg="Hello %s", args=("world",), exc_info=None
    )
    
    formatted = formatter.format(record)
    assert "Hello world" in formatted
    assert "INFO" in formatted
    print("✓ Exercise 3 passed: custom formatter works")

# =============================================================================
# Exercise 4: Logger Factory (★★★★☆)
# =============================================================================
# TODO: Create factory for production-ready loggers

class LoggerFactory:
    """Factory for creating configured loggers."""
    # TODO: Support different environments (dev, prod, test)
    # TODO: Support structured logging
    pass

# Test Cases
def test_logger_factory():
    factory = LoggerFactory()
    
    dev_logger = factory.create("dev_logger", environment="development")
    prod_logger = factory.create("prod_logger", environment="production")
    
    dev_logger.info("Development message")
    prod_logger.info("Production message")
    
    print("✓ Exercise 4 passed: logger factory creates configured loggers")

# =============================================================================
# Exercise 5: Logging Context Manager (★★★★★)
# =============================================================================
# TODO: Create context manager for logging with context

class LoggingContext:
    """Context manager that adds context to log messages."""
    # TODO: Add context fields to all log messages within block
    pass

# Test Cases
def test_logging_context():
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.log') as f:
        temp_path = f.name
    
    try:
        logger = setup_logger("context_test", temp_path)
        
        with LoggingContext(logger, request_id="123", user="alice"):
            logger.info("Processing request")
            logger.info("Request complete")
        
        with open(temp_path) as f:
            content = f.read()
        assert "123" in content
        assert "alice" in content
        print("✓ Exercise 5 passed: logging context adds fields")
    finally:
        os.unlink(temp_path)

if __name__ == "__main__":
    print("Running Logging Setup Exercises...")
    print("=" * 50)
    test_basic_logger()
    test_rotating_logger()
    test_custom_formatter()
    test_logger_factory()
    test_logging_context()
    print("=" * 50)
    print("All tests passed!")
