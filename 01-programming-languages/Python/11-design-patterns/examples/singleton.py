"""
Singleton Pattern in Python
Demonstrates various implementations of the Singleton pattern
"""

from typing import Optional, Dict, Any
import threading

# ============================================
# Basic Singleton using __new__
# ============================================

class SingletonBasic:
    """Basic singleton using __new__ method."""
    
    _instance: Optional['SingletonBasic'] = None
    
    def __new__(cls) -> 'SingletonBasic':
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance
    
    def __init__(self) -> None:
        self.value: Optional[str] = None
    
    def set_value(self, value: str) -> None:
        """Set a value."""
        self.value = value
    
    def get_value(self) -> Optional[str]:
        """Get the value."""
        return self.value

# ============================================
# Thread-Safe Singleton
# ============================================

class SingletonThreadSafe:
    """Thread-safe singleton implementation."""
    
    _instance: Optional['SingletonThreadSafe'] = None
    _lock: threading.Lock = threading.Lock()
    
    def __new__(cls) -> 'SingletonThreadSafe':
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance
    
    def __init__(self) -> None:
        self.config: Dict[str, Any] = {}
    
    def set_config(self, key: str, value: Any) -> None:
        """Set configuration."""
        self.config[key] = value
    
    def get_config(self, key: str) -> Optional[Any]:
        """Get configuration."""
        return self.config.get(key)

# ============================================
# Singleton using Decorator
# ============================================

def singleton(cls):
    """Singleton decorator."""
    instances: Dict[type, Any] = {}
    
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    
    return get_instance

@singleton
class DatabaseConnection:
    """Database connection using singleton decorator."""
    
    def __init__(self) -> None:
        self.connected = False
        self.connection_id: Optional[int] = None
    
    def connect(self) -> None:
        """Establish connection."""
        if not self.connected:
            self.connection_id = id(self)
            self.connected = True
    
    def disconnect(self) -> None:
        """Close connection."""
        self.connected = False
        self.connection_id = None

# ============================================
# Singleton with Lazy Initialization
# ============================================

class SingletonLazy:
    """Lazy initialization singleton."""
    
    _instance: Optional['SingletonLazy'] = None
    _initialized: bool = False
    
    def __new__(cls) -> 'SingletonLazy':
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance
    
    def __init__(self) -> None:
        if not SingletonLazy._initialized:
            self.data: Dict[str, Any] = {}
            SingletonLazy._initialized = True
    
    def set_data(self, key: str, value: Any) -> None:
        """Set data (only initializes once)."""
        self.data[key] = value
    
    def get_data(self, key: str) -> Optional[Any]:
        """Get data."""
        return self.data.get(key)

# ============================================
# Practical Example: Logger
# ============================================

@singleton
class Logger:
    """Logger singleton."""
    
    def __init__(self) -> None:
        self.logs: list = []
    
    def log(self, message: str, level: str = "INFO") -> None:
        """Add a log message."""
        self.logs.append({"level": level, "message": message})
    
    def get_logs(self) -> list:
        """Get all logs."""
        return self.logs.copy()
    
    def clear_logs(self) -> None:
        """Clear all logs."""
        self.logs.clear()

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Singleton ===")
    s1 = SingletonBasic()
    s2 = SingletonBasic()
    print(f"  s1 is s2: {s1 is s2}")  # True
    
    s1.set_value("Hello")
    print(f"  s1 value: {s1.get_value()}")
    print(f"  s2 value: {s2.get_value()}")  # Same value
    
    print("\n=== Thread-Safe Singleton ===")
    ts1 = SingletonThreadSafe()
    ts2 = SingletonThreadSafe()
    print(f"  ts1 is ts2: {ts1 is ts2}")  # True
    
    ts1.set_config("debug", True)
    print(f"  ts1 config: {ts1.config}")
    print(f"  ts2 config: {ts2.config}")  # Same config
    
    print("\n=== Singleton Decorator ===")
    db1 = DatabaseConnection()
    db2 = DatabaseConnection()
    print(f"  db1 is db2: {db1 is db2}")  # True
    
    db1.connect()
    print(f"  db1 connected: {db1.connected}")
    print(f"  db2 connected: {db2.connected}")  # Same connection
    
    print("\n=== Lazy Singleton ===")
    lazy1 = SingletonLazy()
    lazy2 = SingletonLazy()
    print(f"  lazy1 is lazy2: {lazy1 is lazy2}")  # True
    
    lazy1.set_data("key", "value")
    print(f"  lazy1 data: {lazy1.data}")
    print(f"  lazy2 data: {lazy2.data}")  # Same data
    
    print("\n=== Logger Singleton ===")
    logger1 = Logger()
    logger2 = Logger()
    print(f"  logger1 is logger2: {logger1 is logger2}")  # True
    
    logger1.log("Application started")
    logger2.log("User logged in")
    print(f"  Total logs: {len(logger1.get_logs())}")
