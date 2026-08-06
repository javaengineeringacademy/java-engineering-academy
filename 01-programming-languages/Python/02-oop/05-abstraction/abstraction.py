"""Abstract base classes, abstractmethod, and interfaces."""

from abc import ABC, abstractmethod, abstractproperty

# ── Abstract Base Class ──────────────────────────────────────────────
class Vehicle(ABC):
    """Abstract class — cannot be instantiated directly."""

    def __init__(self, make, model, year):
        self.make = make
        self.model = model
        self.year = year

    @abstractmethod
    def start(self):
        """Subclasses must implement this."""
        pass

    @abstractmethod
    def stop(self):
        pass

    @abstractmethod
    def fuel_type(self):
        pass

    # Concrete method — inherited as-is
    def describe(self):
        return f"{self.year} {self.make} {self.model}"

# car = Vehicle("Toyota", "Camry", 2023)  # TypeError

# ── Concrete Implementation ──────────────────────────────────────────
class GasCar(Vehicle):
    def start(self):
        return f"{self.describe()} starts with a roar"

    def stop(self):
        return f"{self.describe()} engine off"

    def fuel_type(self):
        return "Gasoline"

class ElectricCar(Vehicle):
    def __init__(self, make, model, year, battery_kwh):
        super().__init__(make, model, year)
        self.battery_kwh = battery_kwh

    def start(self):
        return f"{self.describe()} powers on silently"

    def stop(self):
        return f"{self.describe()} shuts down"

    def fuel_type(self):
        return "Electric"

    def charge(self):
        return f"Charging {self.battery_kwh}kWh battery"

cars = [GasCar("Toyota", "Camry", 2023), ElectricCar("Tesla", "Model 3", 2024, 75)]

for car in cars:
    print(car.describe())    # Works polymorphically
    print(car.start())
    print(f"Fuel: {car.fuel_type()}")

# ── Abstract Properties ──────────────────────────────────────────────
class Database(ABC):
    @abstractmethod
    def connect(self):
        pass

    @abstractmethod
    def execute(self, query):
        pass

    @property
    @abstractmethod
    def is_connected(self):
        """Abstract property — must be implemented."""
        pass

class PostgreSQL(Database):
    def __init__(self):
        self._connected = False

    def connect(self):
        self._connected = True
        return "Connected to PostgreSQL"

    def execute(self, query):
        return f"Executing: {query}"

    @property
    def is_connected(self):
        return self._connected

# ── Registering Virtual Subclasses ──────────────────────────────────
class Serializer(ABC):
    @abstractmethod
    def serialize(self, data):
        pass

# Register a class that doesn't inherit from Serializer
class JSONSerializer:
    def serialize(self, data):
        import json
        return json.dumps(data)

Serializer.register(JSONSerializer)
print(issubclass(JSONSerializer, Serializer))  # True
print(isinstance(JSONSerializer(), Serializer))  # True

# ── Practical Example: Plugin System ────────────────────────────────
class Plugin(ABC):
    @property
    @abstractmethod
    def name(self):
        pass

    @abstractmethod
    def execute(self, context):
        pass

    def __repr__(self):
        return f"<Plugin: {self.name}>"

class LoggingPlugin(Plugin):
    @property
    def name(self):
        return "logging"

    def execute(self, context):
        return f"Logged: {context}"

class AuthPlugin(Plugin):
    @property
    def name(self):
        return "auth"

    def execute(self, context):
        return f"Authenticated: {context}"

# Plugin registry
plugins: dict[str, Plugin] = {}

def register_plugin(plugin: Plugin):
    plugins[plugin.name] = plugin

register_plugin(LoggingPlugin())
register_plugin(AuthPlugin())

for name, plugin in plugins.items():
    print(f"{plugin}: {plugin.execute('request')}")
