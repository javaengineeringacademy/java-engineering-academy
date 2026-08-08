"""
Module 13 - Logging: Custom Handlers Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Custom logging handlers
"""

import logging
import logging.handlers
import json
import smtplib
from datetime import datetime


# =============================================================================
# Exercise 1: Rotating File Handler (⭐⭐⭐)
# =============================================================================

def exercise_1_rotating_handler():
    """
    Set up a rotating file handler.
    
    TODO:
    1. Create RotatingFileHandler
    2. Set maxBytes and backupCount
    3. Configure rotation behavior
    """
    logger = logging.getLogger('rotating_logger')
    handler = None
    
    # TODO: Create and configure RotatingFileHandler
    
    return logger, handler


# =============================================================================
# Exercise 2: Timed Rotating Handler (⭐⭐⭐)
# =============================================================================

def exercise_2_timed_handler():
    """
    Set up a timed rotating file handler.
    
    TODO:
    1. Create TimedRotatingFileHandler
    2. Set rotation interval (daily, weekly, etc.)
    3. Configure backup count
    """
    logger = logging.getLogger('timed_logger')
    handler = None
    
    # TODO: Create and configure TimedRotatingFileHandler
    
    return logger, handler


# =============================================================================
# Exercise 3: Memory Handler (⭐⭐⭐⭐)
# =============================================================================

class MemoryHandler(logging.Handler):
    """
    Custom handler that stores logs in memory.
    
    TODO:
    1. Store log records in a list
    2. Implement flush() to write stored records
    3. Support capacity limit
    """
    def __init__(self, capacity=1000):
        super().__init__()
        self.capacity = capacity
        self.buffer = []
    
    def emit(self, record):
        # TODO: Store record in buffer
        pass
    
    def flush(self):
        # TODO: Write all buffered records
        pass
    
    def get_records(self):
        # TODO: Return stored records
        pass


# =============================================================================
# Exercise 4: Email Handler (⭐⭐⭐⭐)
# =============================================================================

class EmailHandler(logging.Handler):
    """
    Custom handler that sends logs via email.
    
    TODO:
    1. Configure SMTP settings
    2. Send email on CRITICAL level
    3. Support email batching
    """
    def __init__(self, smtp_host, smtp_port, from_addr, to_addrs):
        super().__init__()
        self.smtp_host = smtp_host
        self.smtp_port = smtp_port
        self.from_addr = from_addr
        self.to_addrs = to_addrs
    
    def emit(self, record):
        # TODO: Send email with log record
        pass


# =============================================================================
# Exercise 5: Database Handler (⭐⭐⭐⭐⭐)
# =============================================================================

class DatabaseHandler(logging.Handler):
    """
    Custom handler that stores logs in a database.
    
    TODO:
    1. Accept database connection
    2. Create log table if not exists
    3. Insert log records
    4. Support querying logs
    """
    def __init__(self, db_connection):
        super().__init__()
        self.db = db_connection
        self._setup_table()
    
    def _setup_table(self):
        # TODO: Create log table
        pass
    
    def emit(self, record):
        # TODO: Insert log record
        pass
    
    def query_logs(self, level=None, limit=100):
        # TODO: Query logs from database
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 13 - Custom Handlers Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Rotating File Handler")
    try:
        logger, handler = exercise_1_rotating_handler()
        assert logger is not None, "Logger should be created"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Timed Rotating Handler")
    try:
        logger, handler = exercise_2_timed_handler()
        assert logger is not None, "Logger should be created"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Memory Handler")
    try:
        handler = MemoryHandler(capacity=10)
        record = logging.LogRecord(
            name='test', level=logging.INFO, pathname='', lineno=0,
            msg='Test message', args=(), exc_info=None
        )
        handler.emit(record)
        records = handler.get_records()
        assert len(records) == 1, "Should have 1 record"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Email Handler")
    try:
        handler = EmailHandler('smtp.example.com', 587, 'from@test.com', ['to@test.com'])
        assert handler.smtp_host == 'smtp.example.com'
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Database Handler")
    try:
        # Mock database
        class MockDB:
            def __init__(self):
                self.tables = {}
                self.records = []
            def execute(self, query):
                pass
            def fetchall(self):
                return []
        
        handler = DatabaseHandler(MockDB())
        assert handler.db is not None
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
