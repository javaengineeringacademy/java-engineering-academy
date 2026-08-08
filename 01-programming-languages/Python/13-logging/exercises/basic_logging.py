"""
Module 13 - Logging: Basic Logging Exercises
Difficulty: ⭐⭐ (Intermediate)
Topic: Python logging module basics
"""

import logging


# =============================================================================
# Exercise 1: Basic Logger Setup (⭐⭐)
# =============================================================================

def exercise_1_basic_logger():
    """
    Set up a basic logger with default settings.
    
    TODO:
    1. Create a logger using logging.getLogger()
    2. Set level to INFO
    3. Log messages at different levels
    """
    logger = None
    
    # TODO: Create and configure logger
    # TODO: Log messages at DEBUG, INFO, WARNING, ERROR, CRITICAL levels
    
    return logger


# =============================================================================
# Exercise 2: Logger with File Handler (⭐⭐⭐)
# =============================================================================

def exercise_2_file_handler():
    """
    Set up a logger that writes to a file.
    
    TODO:
    1. Create logger
    2. Create FileHandler
    3. Create formatter
    4. Add handler to logger
    """
    logger = None
    
    # TODO: Set up file handler with formatter
    
    return logger


# =============================================================================
# Exercise 3: Logger Levels (⭐⭐)
# =============================================================================

def exercise_3_logger_levels():
    """
    Understand and use different logging levels.
    
    TODO:
    1. Set logger level to DEBUG
    2. Log messages at each level
    3. Return list of logged messages
    """
    messages = []
    
    # TODO: Configure logger and capture messages
    
    return messages


# =============================================================================
# Exercise 4: Logger Format (⭐⭐⭐)
# =============================================================================

def exercise_4_logger_format():
    """
    Create custom log formats.
    
    TODO:
    1. Create formatter with timestamp, level, and message
    2. Create formatter with module name
    3. Apply formats to handlers
    """
    formatters = []
    
    # TODO: Create different formatters
    
    return formatters


# =============================================================================
# Exercise 5: Multiple Handlers (⭐⭐⭐⭐)
# =============================================================================

def exercise_5_multiple_handlers():
    """
    Configure logger with multiple handlers.
    
    TODO:
    1. Create logger
    2. Add console handler (StreamHandler)
    3. Add file handler (FileHandler)
    4. Add different formatters to each handler
    """
    logger = None
    
    # TODO: Set up multiple handlers
    
    return logger


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 13 - Basic Logging Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Logger Setup")
    try:
        logger = exercise_1_basic_logger()
        assert logger is not None, "Logger should be created"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Logger with File Handler")
    try:
        logger = exercise_2_file_handler()
        assert logger is not None, "Logger should be created"
        assert len(logger.handlers) > 0, "Should have handlers"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Logger Levels")
    try:
        messages = exercise_3_logger_levels()
        assert isinstance(messages, list), "Should return list"
        print(f"  Messages logged: {len(messages)}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Logger Format")
    try:
        formatters = exercise_4_logger_format()
        assert isinstance(formatters, list), "Should return list"
        assert len(formatters) >= 2, "Should have at least 2 formatters"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Multiple Handlers")
    try:
        logger = exercise_5_multiple_handlers()
        assert logger is not None, "Logger should be created"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
