"""
Module 13: Logging - Logging Setup Solutions
Practice configuring Python's logging module.
"""

import logging
import tempfile
import os
from logging.handlers import RotatingFileHandler


def setup_logger(name, log_file, level=logging.DEBUG):
    """Configure and return a logger with file and console handlers."""
    logger = logging.getLogger(name)
    logger.setLevel(level)

    # File handler
    file_handler = logging.FileHandler(log_file)
    file_handler.setLevel(level)

    # Console handler
    console_handler = logging.StreamHandler()
    console_handler.setLevel(level)

    # Formatter
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    file_handler.setFormatter(formatter)
    console_handler.setFormatter(formatter)

    # Add handlers
    logger.addHandler(file_handler)
    logger.addHandler(console_handler)

    return logger


def setup_rotating_logger(name, log_file, max_bytes=1024, backup_count=3):
    """Configure logger with rotating file handler."""
    logger = logging.getLogger(name)
    logger.setLevel(logging.DEBUG)

    # Rotating file handler
    handler = RotatingFileHandler(
        log_file,
        maxBytes=max_bytes,
        backupCount=backup_count
    )
    handler.setLevel(logging.DEBUG)

    # Formatter
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)

    logger.addHandler(handler)
    return logger


class CustomFormatter(logging.Formatter):
    """Custom formatter with color and timestamp."""

    def format(self, record):
        # Add timestamp and level
        timestamp = self.formatTime(record)
        level = record.levelname
        message = record.getMessage()

        return f"{timestamp} [{level}] {message}"


class LoggerFactory:
    """Factory for creating configured loggers."""

    def __init__(self):
        self._loggers = {}

    def create(self, name, environment="development", log_file=None):
        """Create a configured logger."""
        if name in self._loggers:
            return self._loggers[name]

        logger = logging.getLogger(name)

        if environment == "development":
            logger.setLevel(logging.DEBUG)
        elif environment == "production":
            logger.setLevel(logging.WARNING)
        else:
            logger.setLevel(logging.INFO)

        # Console handler
        console_handler = logging.StreamHandler()
        console_handler.setLevel(logger.level)

        # Formatter
        formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
        console_handler.setFormatter(formatter)
        logger.addHandler(console_handler)

        # File handler if specified
        if log_file:
            file_handler = logging.FileHandler(log_file)
            file_handler.setLevel(logger.level)
            file_handler.setFormatter(formatter)
            logger.addHandler(file_handler)

        self._loggers[name] = logger
        return logger


class LoggingContext:
    """Context manager that adds context to log messages."""

    def __init__(self, logger, **context):
        self.logger = logger
        self.context = context
        self.old_extra = {}

    def __enter__(self):
        # Store old extra and set new context
        for key, value in self.context.items():
            self.old_extra[key] = getattr(self.logger, key, None)
            setattr(self.logger, key, value)
        return self.logger

    def __exit__(self, exc_type, exc_val, exc_tb):
        # Restore old extra
        for key in self.context:
            if key in self.old_extra:
                if self.old_extra[key] is not None:
                    setattr(self.logger, key, self.old_extra[key])
                else:
                    delattr(self.logger, key)
        return False


if __name__ == "__main__":
    print("Testing Logging Setup Solutions...")

    # Test basic logger
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

    # Test rotating logger
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

    # Test custom formatter
    formatter = CustomFormatter()
    record = logging.LogRecord(
        name="test", level=logging.INFO, pathname="", lineno=0,
        msg="Hello %s", args=("world",), exc_info=None
    )

    formatted = formatter.format(record)
    assert "Hello world" in formatted
    assert "INFO" in formatted
    print("✓ Exercise 3 passed: custom formatter works")

    # Test logger factory
    factory = LoggerFactory()

    dev_logger = factory.create("dev_logger", environment="development")
    prod_logger = factory.create("prod_logger", environment="production")

    dev_logger.info("Development message")
    prod_logger.info("Production message")

    print("✓ Exercise 4 passed: logger factory creates configured loggers")

    # Test logging context
    with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.log') as f:
        temp_path = f.name

    try:
        logger = setup_logger("context_test", temp_path)

        with LoggingContext(logger, request_id="123", user="alice"):
            logger.info("Processing request")
            logger.info("Request complete")

        print("✓ Exercise 5 passed: logging context adds fields")
    finally:
        os.unlink(temp_path)

    print("All Logging Setup solutions passed!")
