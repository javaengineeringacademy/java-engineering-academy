"""
Module 11 - Design Patterns: Factory Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Factory pattern implementation
"""


# =============================================================================
# Exercise 1: Simple Factory (⭐⭐⭐)
# =============================================================================

class Animal:
    """Base class for animals."""
    def speak(self):
        pass


class Dog(Animal):
    """TODO: Implement Dog."""
    def speak(self):
        # TODO: Return bark sound
        pass


class Cat(Animal):
    """TODO: Implement Cat."""
    def speak(self):
        # TODO: Return meow sound
        pass


class AnimalFactory:
    """Factory that creates animals."""
    @staticmethod
    def create(animal_type):
        # TODO: Create and return appropriate animal
        pass


# =============================================================================
# Exercise 2: Factory Method (⭐⭐⭐⭐)
# =============================================================================

class Transport:
    """Base class for transport."""
    def deliver(self):
        pass


class Truck(Transport):
    """TODO: Implement Truck."""
    def deliver(self):
        # TODO: Return truck delivery description
        pass


class Ship(Transport):
    """TODO: Implement Ship."""
    def deliver(self):
        # TODO: Return ship delivery description
        pass


class Logistics:
    """Creator class with factory method."""
    def create_transport(self):
        """Factory method - to be overridden."""
        pass
    
    def plan_delivery(self):
        transport = self.create_transport()
        return transport.deliver()


class RoadLogistics(Logistics):
    """TODO: Create trucks."""
    def create_transport(self):
        # TODO: Create and return Truck
        pass


class SeaLogistics(Logistics):
    """TODO: Create ships."""
    def create_transport(self):
        # TODO: Create and return Ship
        pass


# =============================================================================
# Exercise 3: Abstract Factory (⭐⭐⭐⭐)
# =============================================================================

class Button:
    """Base class for buttons."""
    def render(self):
        pass


class Checkbox:
    """Base class for checkboxes."""
    def render(self):
        pass


class WindowsButton(Button):
    """TODO: Windows button."""
    def render(self):
        # TODO: Return Windows button rendering
        pass


class WindowsCheckbox(Checkbox):
    """TODO: Windows checkbox."""
    def render(self):
        # TODO: Return Windows checkbox rendering
        pass


class MacButton(Button):
    """TODO: Mac button."""
    def render(self):
        # TODO: Return Mac button rendering
        pass


class MacCheckbox(Checkbox):
    """TODO: Mac checkbox."""
    def render(self):
        # TODO: Return Mac checkbox rendering
        pass


class GUIFactory:
    """Abstract factory interface."""
    def create_button(self):
        pass
    
    def create_checkbox(self):
        pass


class WindowsFactory(GUIFactory):
    """TODO: Create Windows UI elements."""
    def create_button(self):
        # TODO: Create WindowsButton
        pass
    
    def create_checkbox(self):
        # TODO: Create WindowsCheckbox
        pass


class MacFactory(GUIFactory):
    """TODO: Create Mac UI elements."""
    def create_button(self):
        # TODO: Create MacButton
        pass
    
    def create_checkbox(self):
        # TODO: Create MacCheckbox
        pass


class Application:
    """Application that uses abstract factory."""
    def __init__(self, factory):
        self.factory = factory
    
    def create_ui(self):
        button = self.factory.create_button()
        checkbox = self.factory.create_checkbox()
        return button.render(), checkbox.render()


# =============================================================================
# Exercise 4: Factory with Registration (⭐⭐⭐⭐)
# =============================================================================

class Database:
    """Base class for databases."""
    def connect(self):
        pass
    
    def query(self, sql):
        pass


class MySQLDatabase(Database):
    """TODO: MySQL implementation."""
    def connect(self):
        # TODO: Connect to MySQL
        pass
    
    def query(self, sql):
        # TODO: Execute MySQL query
        pass


class PostgreSQLDatabase(Database):
    """TODO: PostgreSQL implementation."""
    def connect(self):
        # TODO: Connect to PostgreSQL
        pass
    
    def query(self, sql):
        # TODO: Execute PostgreSQL query
        pass


class DatabaseFactory:
    """Factory with registration mechanism."""
    _databases = {}
    
    @classmethod
    def register(cls, name, db_class):
        # TODO: Register database class
        pass
    
    @classmethod
    def create(cls, name):
        # TODO: Create and return database instance
        pass
    
    @classmethod
    def list_databases(cls):
        # TODO: Return list of registered databases
        pass


# =============================================================================
# Exercise 5: Factory with Configuration (⭐⭐⭐⭐⭐)
# =============================================================================

class Logger:
    """Base class for loggers."""
    def log(self, message):
        pass


class FileLogger(Logger):
    """TODO: File logger."""
    def __init__(self, filepath):
        self.filepath = filepath
    
    def log(self, message):
        # TODO: Write to file
        pass


class ConsoleLogger(Logger):
    """TODO: Console logger."""
    def log(self, message):
        # TODO: Print to console
        pass


class RemoteLogger(Logger):
    """TODO: Remote logger."""
    def __init__(self, endpoint):
        self.endpoint = endpoint
    
    def log(self, message):
        # TODO: Send to remote endpoint
        pass


class LoggerFactory:
    """Factory that creates loggers based on configuration."""
    _config = {}
    
    @classmethod
    def configure(cls, config):
        # TODO: Set factory configuration
        pass
    
    @classmethod
    def create(cls, logger_type):
        # TODO: Create logger based on config
        pass
    
    @classmethod
    def create_from_config(cls):
        # TODO: Create logger from stored config
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Factory Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Simple Factory")
    try:
        dog = AnimalFactory.create("dog")
        cat = AnimalFactory.create("cat")
        assert dog.speak() == "Woof!", "Dog should bark"
        assert cat.speak() == "Meow!", "Cat should meow"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Factory Method")
    try:
        road = RoadLogistics()
        sea = SeaLogistics()
        assert "truck" in road.plan_delivery().lower(), "Road uses trucks"
        assert "ship" in sea.plan_delivery().lower(), "Sea uses ships"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Abstract Factory")
    try:
        win_app = Application(WindowsFactory())
        mac_app = Application(MacFactory())
        win_ui = win_app.create_ui()
        mac_ui = mac_app.create_ui()
        assert "windows" in win_ui[0].lower(), "Windows button"
        assert "mac" in mac_ui[0].lower(), "Mac button"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Factory with Registration")
    try:
        DatabaseFactory.register("mysql", MySQLDatabase)
        DatabaseFactory.register("postgresql", PostgreSQLDatabase)
        db = DatabaseFactory.create("mysql")
        assert isinstance(db, MySQLDatabase), "Should create MySQL"
        databases = DatabaseFactory.list_databases()
        assert len(databases) == 2, "Should have 2 databases"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Factory with Configuration")
    try:
        config = {
            'logger_type': 'console',
            'filepath': 'test.log',
            'endpoint': 'http://localhost:8080'
        }
        LoggerFactory.configure(config)
        logger = LoggerFactory.create_from_config()
        assert isinstance(logger, ConsoleLogger), "Should create console logger"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
