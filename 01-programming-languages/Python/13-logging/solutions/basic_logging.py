"""
Module 13 - Logging: Basic Logging Solutions
Complete solutions with explanations
"""

import logging


# =============================================================================
# Exercise 1: Basic Logger Setup - SOLUTION
# =============================================================================

def exercise_1_basic_logger():
    """
    Set up a basic logger with default settings.
    """
    # Create a logger
    logger = logging.getLogger('basic_logger')
    
    # Set level to INFO
    logger.setLevel(logging.INFO)
    
    # Create a handler (console)
    handler = logging.StreamHandler()
    handler.setLevel(logging.INFO)
    
    # Create formatter
    formatter = logging.Formatter('%(levelname)s: %(message)s')
    handler.setFormatter(formatter)
    
    # Add handler to logger
    logger.addHandler(handler)
    
    # Log messages at different levels
    logger.debug("This is a debug message")  # Won't show (below INFO)
    logger.info("This is an info message")
    logger.warning("This is a warning message")
    logger.error("This is an error message")
    logger.critical("This is a critical message")
    
    return logger


# =============================================================================
# Exercise 2: Logger with File Handler - SOLUTION
# =============================================================================

def exercise_2_file_handler():
    """
    Set up a logger that writes to a file.
    """
    # Create logger
    logger = logging.getLogger('file_logger')
    logger.setLevel(logging.DEBUG)
    
    # Create FileHandler
    file_handler = logging.FileHandler('app.log')
    file_handler.setLevel(logging.DEBUG)
    
    # Create formatter
    formatter = logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    file_handler.setFormatter(formatter)
    
    # Add handler to logger
    logger.addHandler(file_handler)
    
    # Test logging
    logger.info("This will be written to app.log")
    
    return logger


# =============================================================================
# Exercise 3: Logger Levels - SOLUTION
# =============================================================================

def exercise_3_logger_levels():
    """
    Understand and use different logging levels.
    """
    messages = []
    
    # Create logger with custom handler that captures messages
    logger = logging.getLogger('level_logger')
    logger.setLevel(logging.DEBUG)
    
    # Create a handler that captures messages
    class CaptureHandler(logging.Handler):
        def emit(self, record):
            messages.append(record.getMessage())
    
    handler = CaptureHandler()
    handler.setLevel(logging.DEBUG)
    logger.addHandler(handler)
    
    # Log messages at each level
    logger.debug("Debug level message")
    logger.info("Info level message")
    logger.warning("Warning level message")
    logger.error("Error level message")
    logger.critical("Critical level message")
    
    return messages


# =============================================================================
# Exercise 4: Logger Format - SOLUTION
# =============================================================================

def exercise_4_logger_format():
    """
    Create custom log formats.
    """
    formatters = []
    
    # Formatter with timestamp, level, and message
    formatter1 = logging.Formatter(
        '%(asctime)s - %(levelname)s - %(message)s'
    )
    formatters.append(formatter1)
    
    # Formatter with module name
    formatter2 = logging.Formatter(
        '%(asctime)s - %(name)s - %(module)s:%(lineno)d - %(levelname)s - %(message)s'
    )
    formatters.append(formatter2)
    
    # Formatter with user info (using extra)
    formatter3 = logging.Formatter(
        '%(asctime)s - %(levelname)s - %(user)s - %(message)s'
    )
    formatters.append(formatter3)
    
    return formatters


# =============================================================================
# Exercise 5: Multiple Handlers - SOLUTION
# =============================================================================

def exercise_5_multiple_handlers():
    """
    Configure logger with multiple handlers.
    """
    # Create logger
    logger = logging.getLogger('multi_handler_logger')
    logger.setLevel(logging.DEBUG)
    
    # Console handler with simple format
    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.INFO)
    console_formatter = logging.Formatter('%(levelname)s: %(message)s')
    console_handler.setFormatter(console_formatter)
    
    # File handler with detailed format
    file_handler = logging.FileHandler('detailed.log')
    file_handler.setLevel(logging.DEBUG)
    file_formatter = logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    file_handler.setFormatter(file_formatter)
    
    # Add both handlers
    logger.addHandler(console_handler)
    logger.addHandler(file_handler)
    
    # Test logging
    logger.info("This goes to both console and file")
    logger.debug("This only goes to file")
    
    return logger


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 13 - Basic Logging Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Logger Setup")
    logger = exercise_1_basic_logger()
    assert logger is not None, "Logger should be created"
    assert logger.level == logging.INFO, "Level should be INFO"
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Logger with File Handler")
    logger = exercise_2_file_handler()
    assert logger is not None, "Logger should be created"
    assert len(logger.handlers) > 0, "Should have handlers"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Logger Levels")
    messages = exercise_3_logger_levels()
    assert isinstance(messages, list), "Should return list"
    assert len(messages) == 5, "Should have 5 messages"
    assert "Debug" in messages[0]
    assert "Critical" in messages[4]
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Logger Format")
    formatters = exercise_4_logger_format()
    assert isinstance(formatters, list), "Should return list"
    assert len(formatters) >= 2, "Should have at least 2 formatters"
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Multiple Handlers")
    logger = exercise_5_multiple_handlers()
    assert logger is not None, "Logger should be created"
    assert len(logger.handlers) >= 2, "Should have at least 2 handlers"
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
