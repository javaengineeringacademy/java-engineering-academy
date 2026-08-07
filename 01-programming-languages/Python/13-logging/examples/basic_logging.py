"""
Basic Logging in Python
Demonstrates fundamental logging concepts and configurations
"""

import logging
import sys

# ============================================
# Basic Logging Setup
# ============================================

def basic_setup() -> None:
    """Basic logging configuration."""
    print("=== Basic Logging Setup ===")
    
    # Configure logging
    logging.basicConfig(
        level=logging.DEBUG,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    
    # Create logger
    logger = logging.getLogger(__name__)
    
    # Log messages at different levels
    logger.debug("This is a debug message")
    logger.info("This is an info message")
    logger.warning("This is a warning message")
    logger.error("This is an error message")
    logger.critical("This is a critical message")

# ============================================
# Log Levels
# ============================================

def log_levels() -> None:
    """Demonstrate different log levels."""
    print("\n=== Log Levels ===")
    
    logger = logging.getLogger("levels_demo")
    logger.setLevel(logging.DEBUG)
    
    # Add handler
    handler = logging.StreamHandler(sys.stdout)
    handler.setLevel(logging.DEBUG)
    formatter = logging.Formatter('%(levelname)s: %(message)s')
    handler.setFormatter(formatter)
    logger.addHandler(handler)
    
    # Log at each level
    logger.debug("DEBUG: Detailed information for debugging")
    logger.info("INFO: Confirmation that things are working")
    logger.warning("WARNING: Something unexpected happened")
    logger.error("ERROR: Something failed")
    logger.critical("CRITICAL: Program may crash")

# ============================================
# Logger Configuration
# ============================================

def logger_configuration() -> None:
    """Demonstrate logger configuration options."""
    print("\n=== Logger Configuration ===")
    
    # Create logger with specific name
    app_logger = logging.getLogger("myapp")
    app_logger.setLevel(logging.INFO)
    
    # Create console handler
    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.INFO)
    
    # Create file handler
    file_handler = logging.FileHandler("app.log")
    file_handler.setLevel(logging.DEBUG)
    
    # Create formatter
    formatter = logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    
    # Add formatter to handlers
    console_handler.setFormatter(formatter)
    file_handler.setFormatter(formatter)
    
    # Add handlers to logger
    app_logger.addHandler(console_handler)
    app_logger.addHandler(file_handler)
    
    # Log messages
    app_logger.info("Application started")
    app_logger.debug("Debugging application")
    app_logger.warning("Something might be wrong")
    app_logger.error("Something failed")
    
    # Clean up
    app_logger.removeHandler(console_handler)
    app_logger.removeHandler(file_handler)
    console_handler.close()
    file_handler.close()

# ============================================
# Logger Hierarchy
# ============================================

def logger_hierarchy() -> None:
    """Demonstrate logger hierarchy."""
    print("\n=== Logger Hierarchy ===")
    
    # Create parent logger
    parent_logger = logging.getLogger("parent")
    parent_logger.setLevel(logging.DEBUG)
    
    # Create child logger
    child_logger = logging.getLogger("parent.child")
    
    # Add handler to parent
    handler = logging.StreamHandler()
    handler.setLevel(logging.DEBUG)
    formatter = logging.Formatter('%(name)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)
    parent_logger.addHandler(handler)
    
    # Log from child (will use parent's handler)
    child_logger.info("Message from child logger")
    
    # Clean up
    parent_logger.removeHandler(handler)
    handler.close()

# ============================================
# Exception Logging
# ============================================

def exception_logging() -> None:
    """Demonstrate exception logging."""
    print("\n=== Exception Logging ===")
    
    logger = logging.getLogger("exception_demo")
    logger.setLevel(logging.DEBUG)
    
    handler = logging.StreamHandler()
    handler.setLevel(logging.DEBUG)
    formatter = logging.Formatter('%(levelname)s - %(message)s')
    handler.setFormatter(formatter)
    logger.addHandler(handler)
    
    # Log exception
    try:
        result = 1 / 0
    except ZeroDivisionError:
        logger.exception("An error occurred:")
    
    # Log with exc_info
    try:
        data = {"key": "value"}
        value = data["nonexistent"]
    except KeyError:
        logger.error("Key not found", exc_info=True)
    
    # Clean up
    logger.removeHandler(handler)
    handler.close()

# ============================================
# Custom Log Format
# ============================================

def custom_format() -> None:
    """Demonstrate custom log formats."""
    print("\n=== Custom Log Format ===")
    
    # JSON-like format
    json_format = '{"time":"%(asctime)s","level":"%(levelname)s","message":"%(message)s"}'
    
    # Detailed format
    detailed_format = """
=====================================
Time: %(asctime)s
Level: %(levelname)s
Module: %(module)s
Function: %(funcName)s
Line: %(lineno)d
Message: %(message)s
=====================================
"""
    
    # Simple format
    simple_format = '%(levelname)s: %(message)s'
    
    logger = logging.getLogger("format_demo")
    logger.setLevel(logging.DEBUG)
    
    handler = logging.StreamHandler()
    handler.setLevel(logging.DEBUG)
    handler.setFormatter(logging.Formatter(simple_format))
    logger.addHandler(handler)
    
    logger.info("Simple format message")
    
    # Clean up
    logger.removeHandler(handler)
    handler.close()

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    basic_setup()
    log_levels()
    logger_configuration()
    logger_hierarchy()
    exception_logging()
    custom_format()
