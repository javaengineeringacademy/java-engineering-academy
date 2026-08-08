"""
Module 11 - Design Patterns: Factory Solutions
Complete solutions with explanations
"""

from abc import ABC, abstractmethod


# =============================================================================
# Exercise 1: Simple Factory - SOLUTION
# =============================================================================

class Animal(ABC):
    """Base class for animals."""
    
    @abstractmethod
    def speak(self):
        pass


class Dog(Animal):
    """Dog implementation."""
    
    def speak(self):
        return "Woof!"


class Cat(Animal):
    """Cat implementation."""
    
    def speak(self):
        return "Meow!"


class AnimalFactory:
    """Factory that creates animals based on type."""
    
    _animals = {
        'dog': Dog,
        'cat': Cat,
    }
    
    @classmethod
    def create(cls, animal_type):
        """Create and return appropriate animal."""
        animal_class = cls._animals.get(animal_type.lower())
        if animal_class is None:
            raise ValueError(f"Unknown animal type: {animal_type}")
        return animal_class()


# =============================================================================
# Exercise 2: Factory Method - SOLUTION
# =============================================================================

class Transport(ABC):
    """Base class for transport."""
    
    @abstractmethod
    def deliver(self):
        pass


class Truck(Transport):
    """Truck implementation."""
    
    def deliver(self):
        return "Delivering by land in a truck"


class Ship(Transport):
    """Ship implementation."""
    
    def deliver(self):
        return "Delivering by sea in a ship"


class Logistics(ABC):
    """Creator class with factory method."""
    
    @abstractmethod
    def create_transport(self):
        """Factory method - to be overridden by subclasses."""
        pass
    
    def plan_delivery(self):
        """Uses the factory method to get a transport."""
        transport = self.create_transport()
        return transport.deliver()


class RoadLogistics(Logistics):
    """Creates trucks for road delivery."""
    
    def create_transport(self):
        return Truck()


class SeaLogistics(Logistics):
    """Creates ships for sea delivery."""
    
    def create_transport(self):
        return Ship()


# =============================================================================
# Exercise 3: Abstract Factory - SOLUTION
# =============================================================================

class Button(ABC):
    """Base class for buttons."""
    
    @abstractmethod
    def render(self):
        pass


class Checkbox(ABC):
    """Base class for checkboxes."""
    
    @abstractmethod
    def render(self):
        pass


class WindowsButton(Button):
    """Windows button implementation."""
    
    def render(self):
        return "[Windows Button]"


class WindowsCheckbox(Checkbox):
    """Windows checkbox implementation."""
    
    def render(self):
        return "[Windows Checkbox]"


class MacButton(Button):
    """Mac button implementation."""
    
    def render(self):
        return "(Mac Button)"


class MacCheckbox(Checkbox):
    """Mac checkbox implementation."""
    
    def render(self):
        return "(Mac Checkbox)"


class GUIFactory(ABC):
    """Abstract factory interface."""
    
    @abstractmethod
    def create_button(self):
        pass
    
    @abstractmethod
    def create_checkbox(self):
        pass


class WindowsFactory(GUIFactory):
    """Creates Windows UI elements."""
    
    def create_button(self):
        return WindowsButton()
    
    def create_checkbox(self):
        return WindowsCheckbox()


class MacFactory(GUIFactory):
    """Creates Mac UI elements."""
    
    def create_button(self):
        return MacButton()
    
    def create_checkbox(self):
        return MacCheckbox()


class Application:
    """Application that uses abstract factory."""
    
    def __init__(self, factory):
        self.factory = factory
    
    def create_ui(self):
        """Create UI elements using the factory."""
        button = self.factory.create_button()
        checkbox = self.factory.create_checkbox()
        return button.render(), checkbox.render()


# =============================================================================
# Exercise 4: Factory with Registration - SOLUTION
# =============================================================================

class Database(ABC):
    """Base class for databases."""
    
    @abstractmethod
    def connect(self):
        pass
    
    @abstractmethod
    def query(self, sql):
        pass


class MySQLDatabase(Database):
    """MySQL implementation."""
    
    def connect(self):
        return "Connected to MySQL"
    
    def query(self, sql):
        return f"MySQL executed: {sql}"


class PostgreSQLDatabase(Database):
    """PostgreSQL implementation."""
    
    def connect(self):
        return "Connected to PostgreSQL"
    
    def query(self, sql):
        return f"PostgreSQL executed: {sql}"


class DatabaseFactory:
    """Factory with registration mechanism."""
    _databases = {}
    
    @classmethod
    def register(cls, name, db_class):
        """Register a database class."""
        cls._databases[name] = db_class
    
    @classmethod
    def create(cls, name):
        """Create and return database instance."""
        if name not in cls._databases:
            raise ValueError(f"Unknown database: {name}")
        return cls._databases[name]()
    
    @classmethod
    def list_databases(cls):
        """Return list of registered databases."""
        return list(cls._databases.keys())


# =============================================================================
# Exercise 5: Factory with Configuration - SOLUTION
# =============================================================================

class Logger(ABC):
    """Base class for loggers."""
    
    @abstractmethod
    def log(self, message):
        pass


class FileLogger(Logger):
    """File logger implementation."""
    
    def __init__(self, filepath):
        self.filepath = filepath
    
    def log(self, message):
        return f"File({self.filepath}): {message}"


class ConsoleLogger(Logger):
    """Console logger implementation."""
    
    def log(self, message):
        return f"Console: {message}"


class RemoteLogger(Logger):
    """Remote logger implementation."""
    
    def __init__(self, endpoint):
        self.endpoint = endpoint
    
    def log(self, message):
        return f"Remote({self.endpoint}): {message}"


class LoggerFactory:
    """Factory that creates loggers based on configuration."""
    _config = {}
    
    @classmethod
    def configure(cls, config):
        """Set factory configuration."""
        cls._config = config.copy()
    
    @classmethod
    def create(cls, logger_type):
        """Create logger based on type."""
        config = cls._config
        
        if logger_type == 'file':
            return FileLogger(config.get('filepath', 'default.log'))
        elif logger_type == 'console':
            return ConsoleLogger()
        elif logger_type == 'remote':
            return RemoteLogger(config.get('endpoint', 'http://localhost'))
        else:
            raise ValueError(f"Unknown logger type: {logger_type}")
    
    @classmethod
    def create_from_config(cls):
        """Create logger from stored config."""
        logger_type = cls._config.get('logger_type', 'console')
        return cls.create(logger_type)


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Factory Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Simple Factory")
    dog = AnimalFactory.create("dog")
    cat = AnimalFactory.create("cat")
    assert dog.speak() == "Woof!", "Dog should bark"
    assert cat.speak() == "Meow!", "Cat should meow"
    
    try:
        AnimalFactory.create("bird")
        assert False, "Should raise error for unknown animal"
    except ValueError:
        pass
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Factory Method")
    road = RoadLogistics()
    sea = SeaLogistics()
    assert "truck" in road.plan_delivery().lower()
    assert "ship" in sea.plan_delivery().lower()
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Abstract Factory")
    win_app = Application(WindowsFactory())
    mac_app = Application(MacFactory())
    
    win_ui = win_app.create_ui()
    mac_ui = mac_app.create_ui()
    
    assert "windows" in win_ui[0].lower()
    assert "windows" in win_ui[1].lower()
    assert "mac" in mac_ui[0].lower()
    assert "mac" in mac_ui[1].lower()
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Factory with Registration")
    DatabaseFactory.register("mysql", MySQLDatabase)
    DatabaseFactory.register("postgresql", PostgreSQLDatabase)
    
    db = DatabaseFactory.create("mysql")
    assert isinstance(db, MySQLDatabase)
    assert "Connected" in db.connect()
    
    databases = DatabaseFactory.list_databases()
    assert len(databases) == 2
    assert "mysql" in databases
    assert "postgresql" in databases
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Factory with Configuration")
    config = {
        'logger_type': 'console',
        'filepath': 'app.log',
        'endpoint': 'http://logs.example.com'
    }
    LoggerFactory.configure(config)
    
    logger = LoggerFactory.create_from_config()
    assert isinstance(logger, ConsoleLogger)
    
    logger = LoggerFactory.create('file')
    assert isinstance(logger, FileLogger)
    assert logger.filepath == 'app.log'
    
    logger = LoggerFactory.create('remote')
    assert isinstance(logger, RemoteLogger)
    assert logger.endpoint == 'http://logs.example.com'
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
