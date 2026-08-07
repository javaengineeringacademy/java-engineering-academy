"""
Custom Log Handlers in Python
Demonstrates creating custom log handlers
"""

import logging
import logging.handlers
import json
import sqlite3
import smtplib
from email.mime.text import MIMEText
from typing import Dict, Any
from datetime import datetime

# ============================================
# Rotating File Handler
# ============================================

def rotating_file_handler() -> None:
    """Demonstrate rotating file handler."""
    print("=== Rotating File Handler ===")
    
    logger = logging.getLogger("rotating_demo")
    logger.setLevel(logging.DEBUG)
    
    # Create rotating file handler
    handler = logging.handlers.RotatingFileHandler(
        "rotating.log",
        maxBytes=1024 * 1024,  # 1MB
        backupCount=5
    )
    handler.setLevel(logging.DEBUG)
    
    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)
    
    logger.addHandler(handler)
    
    # Log some messages
    for i in range(10):
        logger.info(f"Log message {i}")
    
    # Clean up
    logger.removeHandler(handler)
    handler.close()

# ============================================
# Timed Rotating File Handler
# ============================================

def timed_rotating_handler() -> None:
    """Demonstrate timed rotating file handler."""
    print("\n=== Timed Rotating Handler ===")
    
    logger = logging.getLogger("timed_demo")
    logger.setLevel(logging.DEBUG)
    
    # Create timed rotating handler
    handler = logging.handlers.TimedRotatingFileHandler(
        "timed.log",
        when="midnight",
        interval=1,
        backupCount=7
    )
    handler.setLevel(logging.DEBUG)
    
    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)
    
    logger.addHandler(handler)
    
    # Log messages
    logger.info("Timed rotating handler test")
    
    # Clean up
    logger.removeHandler(handler)
    handler.close()

# ============================================
# SQLite Handler
# ============================================

class SQLiteHandler(logging.Handler):
    """Custom handler that logs to SQLite database."""
    
    def __init__(self, db_path: str = "logs.db") -> None:
        super().__init__()
        self.db_path = db_path
        self._init_db()
    
    def _init_db(self) -> None:
        """Initialize database table."""
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp TEXT,
                level TEXT,
                logger TEXT,
                message TEXT,
                module TEXT,
                function TEXT,
                line INTEGER
            )
        """)
        conn.commit()
        conn.close()
    
    def emit(self, record: logging.LogRecord) -> None:
        """Write log record to database."""
        try:
            conn = sqlite3.connect(self.db_path)
            cursor = conn.cursor()
            cursor.execute("""
                INSERT INTO logs (timestamp, level, logger, message, module, function, line)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """, (
                datetime.fromtimestamp(record.created).isoformat(),
                record.levelname,
                record.name,
                record.getMessage(),
                record.module,
                record.funcName,
                record.lineno
            ))
            conn.commit()
            conn.close()
        except Exception as e:
            self.handleError(record)

# ============================================
# SMTP Handler
# ============================================

class EmailHandler(logging.Handler):
    """Custom handler that sends emails."""
    
    def __init__(
        self,
        smtp_server: str,
        smtp_port: int,
        sender: str,
        recipients: list,
        subject: str = "Log Alert"
    ) -> None:
        super().__init__()
        self.smtp_server = smtp_server
        self.smtp_port = smtp_port
        self.sender = sender
        self.recipients = recipients
        self.subject = subject
    
    def emit(self, record: logging.LogRecord) -> None:
        """Send email for log record."""
        try:
            # Format message
            message = self.format(record)
            
            # Create email
            msg = MIMEText(message)
            msg['Subject'] = f"{self.subject} - {record.levelname}"
            msg['From'] = self.sender
            msg['To'] = ', '.join(self.recipients)
            
            # Note: In production, you would actually send the email
            # with smtplib.SMTP(self.smtp_server, self.smtp_port)
            print(f"  [EMAIL] To: {msg['To']}")
            print(f"  [EMAIL] Subject: {msg['Subject']}")
            print(f"  [EMAIL] Body: {message[:100]}...")
            
        except Exception as e:
            self.handleError(record)

# ============================================
# HTTP Handler
# ============================================

class HTTPHandler(logging.Handler):
    """Custom handler that sends logs to HTTP endpoint."""
    
    def __init__(self, url: str, method: str = "POST") -> None:
        super().__init__()
        self.url = url
        self.method = method
    
    def emit(self, record: logging.LogRecord) -> None:
        """Send log record to HTTP endpoint."""
        try:
            # Format as JSON
            log_data = {
                "timestamp": datetime.fromtimestamp(record.created).isoformat(),
                "level": record.levelname,
                "logger": record.name,
                "message": record.getMessage(),
                "module": record.module,
                "function": record.funcName,
                "line": record.lineno
            }
            
            # Note: In production, you would use requests library
            # response = requests.request(self.method, self.url, json=log_data)
            print(f"  [HTTP] {self.method} {self.url}")
            print(f"  [HTTP] Data: {json.dumps(log_data)[:100]}...")
            
        except Exception as e:
            self.handleError(record)

# ============================================
# Queue Handler
# ============================================

class QueueHandler(logging.Handler):
    """Custom handler that puts logs in a queue."""
    
    def __init__(self, queue) -> None:
        super().__init__()
        self.queue = queue
    
    def emit(self, record: logging.LogRecord) -> None:
        """Put log record in queue."""
        try:
            # Format record
            log_entry = self.format(record)
            self.queue.put(log_entry)
        except Exception as e:
            self.handleError(record)

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    rotating_file_handler()
    timed_rotating_handler()
    
    print("\n=== SQLite Handler ===")
    logger = logging.getLogger("sqlite_demo")
    logger.setLevel(logging.DEBUG)
    
    sqlite_handler = SQLiteHandler("test_logs.db")
    sqlite_handler.setLevel(logging.DEBUG)
    formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
    sqlite_handler.setFormatter(formatter)
    
    logger.addHandler(sqlite_handler)
    logger.info("SQLite handler test message")
    logger.warning("SQLite handler warning")
    
    # Clean up
    logger.removeHandler(sqlite_handler)
    sqlite_handler.close()
    
    print("\n=== Email Handler ===")
    email_logger = logging.getLogger("email_demo")
    email_logger.setLevel(logging.ERROR)
    
    email_handler = EmailHandler(
        smtp_server="smtp.example.com",
        smtp_port=587,
        sender="alerts@example.com",
        recipients=["admin@example.com"],
        subject="Error Alert"
    )
    email_handler.setLevel(logging.ERROR)
    email_handler.setFormatter(formatter)
    
    email_logger.addHandler(email_handler)
    email_logger.error("Critical error occurred")
    
    # Clean up
    email_logger.removeHandler(email_handler)
    
    print("\n=== HTTP Handler ===")
    http_logger = logging.getLogger("http_demo")
    http_logger.setLevel(logging.INFO)
    
    http_handler = HTTPHandler("https://logs.example.com/ingest")
    http_handler.setLevel(logging.INFO)
    
    http_logger.addHandler(http_handler)
    http_logger.info("HTTP handler test message")
    
    # Clean up
    http_logger.removeHandler(http_handler)
