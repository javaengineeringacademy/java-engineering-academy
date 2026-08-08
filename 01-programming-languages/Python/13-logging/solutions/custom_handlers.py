"""
Module 13 - Logging: Custom Handlers Solutions
Complete solutions with explanations
"""

import logging
import logging.handlers
import json
import smtplib
from datetime import datetime
from email.mime.text import MIMEText


# =============================================================================
# Exercise 1: Rotating File Handler - SOLUTION
# =============================================================================

def exercise_1_rotating_handler():
    """
    Set up a rotating file handler.
    
    RotatingFileHandler automatically rotates log files when they reach
    a certain size, preventing disk space issues.
    """
    logger = logging.getLogger('rotating_logger')
    logger.setLevel(logging.DEBUG)
    
    # Create RotatingFileHandler
    # maxBytes: Maximum size of each log file (1MB)
    # backupCount: Number of backup files to keep
    handler = logging.handlers.RotatingFileHandler(
        'rotating.log',
        maxBytes=1024 * 1024,  # 1MB
        backupCount=5
    )
    handler.setLevel(logging.DEBUG)
    
    # Create formatter
    formatter = logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    handler.setFormatter(formatter)
    
    logger.addHandler(handler)
    
    return logger, handler


# =============================================================================
# Exercise 2: Timed Rotating Handler - SOLUTION
# =============================================================================

def exercise_2_timed_handler():
    """
    Set up a timed rotating file handler.
    
    TimedRotatingFileHandler rotates logs based on time intervals.
    """
    logger = logging.getLogger('timed_logger')
    logger.setLevel(logging.DEBUG)
    
    # Create TimedRotatingFileHandler
    # when: Rotation interval ('S', 'M', 'H', 'D', 'W0'-'W6', 'midnight')
    # interval: Number of intervals between rotations
    # backupCount: Number of backup files to keep
    handler = logging.handlers.TimedRotatingFileHandler(
        'timed.log',
        when='D',  # Daily rotation
        interval=1,
        backupCount=30  # Keep 30 days
    )
    handler.setLevel(logging.DEBUG)
    
    # Create formatter
    formatter = logging.Formatter(
        '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    handler.setFormatter(formatter)
    
    logger.addHandler(handler)
    
    return logger, handler


# =============================================================================
# Exercise 3: Memory Handler - SOLUTION
# =============================================================================

class MemoryHandler(logging.Handler):
    """
    Custom handler that stores logs in memory.
    
    Useful for batch processing or when you need to aggregate logs
    before writing them somewhere.
    """
    def __init__(self, capacity=1000):
        super().__init__()
        self.capacity = capacity
        self.buffer = []
    
    def emit(self, record):
        """Store record in buffer."""
        if len(self.buffer) >= self.capacity:
            # Remove oldest record when at capacity
            self.buffer.pop(0)
        self.buffer.append(record)
    
    def flush(self):
        """Write all buffered records."""
        for record in self.buffer:
            # In a real implementation, you'd write to a file or send somewhere
            print(f"Flushing: {record.getMessage()}")
        self.buffer.clear()
    
    def get_records(self):
        """Return stored records."""
        return self.buffer.copy()


# =============================================================================
# Exercise 4: Email Handler - SOLUTION
# =============================================================================

class EmailHandler(logging.Handler):
    """
    Custom handler that sends logs via email.
    
    Useful for critical alerts that need immediate attention.
    """
    def __init__(self, smtp_host, smtp_port, from_addr, to_addrs):
        super().__init__()
        self.smtp_host = smtp_host
        self.smtp_port = smtp_port
        self.from_addr = from_addr
        self.to_addrs = to_addrs
        self.email_buffer = []
    
    def emit(self, record):
        """Send email with log record."""
        try:
            # Format the message
            message = self.format(record)
            
            # Create email
            msg = MIMEText(message)
            msg['Subject'] = f"[{record.levelname}] {record.name}"
            msg['From'] = self.from_addr
            msg['To'] = ', '.join(self.to_addrs)
            
            # In production, you'd actually send the email:
            # with smtplib.SMTP(self.smtp_host, self.smtp_port) as server:
            #     server.send_message(msg)
            
            # For testing, just store it
            self.email_buffer.append(msg)
            
        except Exception:
            self.handleError(record)
    
    def get_sent_emails(self):
        """Return emails that would have been sent."""
        return self.email_buffer


# =============================================================================
# Exercise 5: Database Handler - SOLUTION
# =============================================================================

class DatabaseHandler(logging.Handler):
    """
    Custom handler that stores logs in a database.
    
    Useful for long-term log storage and querying.
    """
    def __init__(self, db_connection):
        super().__init__()
        self.db = db_connection
        self._setup_table()
    
    def _setup_table(self):
        """Create log table if not exists."""
        create_table_sql = """
        CREATE TABLE IF NOT EXISTS logs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp TIMESTAMP,
            level TEXT,
            logger TEXT,
            message TEXT,
            module TEXT,
            function TEXT,
            line INTEGER
        )
        """
        self.db.execute(create_table_sql)
    
    def emit(self, record):
        """Insert log record."""
        insert_sql = """
        INSERT INTO logs (timestamp, level, logger, message, module, function, line)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """
        values = (
            datetime.fromtimestamp(record.created).isoformat(),
            record.levelname,
            record.name,
            record.getMessage(),
            record.module,
            record.funcName,
            record.lineno
        )
        self.db.execute(insert_sql, values)
    
    def query_logs(self, level=None, limit=100):
        """Query logs from database."""
        if level:
            query = "SELECT * FROM logs WHERE level = ? ORDER BY timestamp DESC LIMIT ?"
            self.db.execute(query, (level, limit))
        else:
            query = "SELECT * FROM logs ORDER BY timestamp DESC LIMIT ?"
            self.db.execute(query, (limit,))
        return self.db.fetchall()


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 13 - Custom Handlers Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Rotating File Handler")
    logger, handler = exercise_1_rotating_handler()
    assert logger is not None, "Logger should be created"
    assert handler.maxBytes == 1024 * 1024
    assert handler.backupCount == 5
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Timed Rotating Handler")
    logger, handler = exercise_2_timed_handler()
    assert logger is not None, "Logger should be created"
    assert handler.when == 'D'
    assert handler.backupCount == 30
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Memory Handler")
    handler = MemoryHandler(capacity=10)
    record = logging.LogRecord(
        name='test', level=logging.INFO, pathname='', lineno=0,
        msg='Test message', args=(), exc_info=None
    )
    handler.emit(record)
    records = handler.get_records()
    assert len(records) == 1, "Should have 1 record"
    assert records[0].getMessage() == 'Test message'
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Email Handler")
    handler = EmailHandler('smtp.example.com', 587, 'from@test.com', ['to@test.com'])
    record = logging.LogRecord(
        name='test', level=logging.CRITICAL, pathname='', lineno=0,
        msg='Critical error!', args=(), exc_info=None
    )
    handler.emit(record)
    emails = handler.get_sent_emails()
    assert len(emails) == 1
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Database Handler")
    # Mock database
    class MockDB:
        def __init__(self):
            self.executed = []
        def execute(self, query, values=None):
            self.executed.append((query, values))
        def fetchall(self):
            return [{'id': 1, 'level': 'INFO', 'message': 'test'}]
    
    db = MockDB()
    handler = DatabaseHandler(db)
    record = logging.LogRecord(
        name='test', level=logging.INFO, pathname='', lineno=0,
        msg='Database log', args=(), exc_info=None
    )
    handler.emit(record)
    assert len(db.executed) == 2  # CREATE TABLE + INSERT
    logs = handler.query_logs(level='INFO')
    assert len(logs) == 1
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
