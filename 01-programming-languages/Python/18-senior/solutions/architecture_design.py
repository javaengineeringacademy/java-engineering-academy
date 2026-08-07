"""
Module 18: Senior - Architecture Design Solutions
Practice architectural design patterns and principles.
"""

from abc import ABC, abstractmethod
from typing import Any, Dict, List, Optional
from dataclasses import dataclass
from enum import Enum


class ArchitecturePattern(Enum):
    """Common architecture patterns."""
    MVC = "Model-View-Controller"
    MVP = "Model-View-Presenter"
    MVVM = "Model-View-ViewModel"
    CLEAN = "Clean Architecture"
    HEXAGONAL = "Hexagonal Architecture"
    MICROSERVICES = "Microservices"


@dataclass
class Component:
    """Represents a software component."""
    name: str
    description: str
    dependencies: List[str]
    version: str = "1.0.0"


class Repository(ABC):
    """Repository pattern for data access."""

    @abstractmethod
    def get(self, id: str) -> Optional[Any]:
        pass

    @abstractmethod
    def save(self, entity: Any) -> bool:
        pass

    @abstractmethod
    def delete(self, id: str) -> bool:
        pass

    @abstractmethod
    def list_all(self) -> List[Any]:
        pass


class InMemoryRepository(Repository):
    """In-memory implementation of Repository."""

    def __init__(self):
        self._storage: Dict[str, Any] = {}

    def get(self, id: str) -> Optional[Any]:
        return self._storage.get(id)

    def save(self, entity: Any) -> bool:
        self._storage[entity.id] = entity
        return True

    def delete(self, id: str) -> bool:
        if id in self._storage:
            del self._storage[id]
            return True
        return False

    def list_all(self) -> List[Any]:
        return list(self._storage.values())


class Service(ABC):
    """Base service class."""

    @abstractmethod
    def execute(self, *args, **kwargs) -> Any:
        pass


class UserService(Service):
    """User service implementation."""

    def __init__(self, repository: Repository):
        self.repository = repository

    def execute(self, action: str, **kwargs) -> Any:
        if action == "create":
            return self.create_user(kwargs.get("name"))
        elif action == "get":
            return self.get_user(kwargs.get("id"))
        elif action == "list":
            return self.list_users()
        return None

    def create_user(self, name: str) -> Any:
        user = User(id=str(len(self.repository.list_all()) + 1), name=name)
        self.repository.save(user)
        return user

    def get_user(self, id: str) -> Optional[Any]:
        return self.repository.get(id)

    def list_users(self) -> List[Any]:
        return self.repository.list_all()


@dataclass
class User:
    """User entity."""
    id: str
    name: str


class Event:
    """Event for event-driven architecture."""

    def __init__(self, event_type: str, data: Any):
        self.event_type = event_type
        self.data = data
        self.timestamp = None


class EventHandler(ABC):
    """Base event handler."""

    @abstractmethod
    def handle(self, event: Event) -> None:
        pass


class EventBus:
    """Event bus for pub/sub pattern."""

    def __init__(self):
        self._handlers: Dict[str, List[EventHandler]] = {}

    def subscribe(self, event_type: str, handler: EventHandler):
        if event_type not in self._handlers:
            self._handlers[event_type] = []
        self._handlers[event_type].append(handler)

    def publish(self, event: Event):
        if event.event_type in self._handlers:
            for handler in self._handlers[event.event_type]:
                handler.handle(event)


class Mediator:
    """Mediator pattern for component communication."""

    def __init__(self):
        self._components: Dict[str, Any] = {}

    def register(self, name: str, component: Any):
        self._components[name] = component

    def notify(self, sender: str, event: str, data: Any = None):
        for name, component in self._components.items():
            if name != sender and hasattr(component, f'on_{event}'):
                getattr(component, f'on_{event}')(data)


class Facade:
    """Facade pattern for simplified interface."""

    def __init__(self):
        self._subsystems: Dict[str, Any] = {}

    def add_subsystem(self, name: str, subsystem: Any):
        self._subsystems[name] = subsystem

    def operation(self, *args, **kwargs) -> Any:
        results = {}
        for name, subsystem in self._subsystems.items():
            if hasattr(subsystem, 'execute'):
                results[name] = subsystem.execute(*args, **kwargs)
        return results


if __name__ == "__main__":
    print("Testing Architecture Design Solutions...")

    # Test Repository pattern
    repo = InMemoryRepository()
    user = User(id="1", name="Alice")
    repo.save(user)
    assert repo.get("1") is not None
    assert len(repo.list_all()) == 1
    assert repo.delete("1") is True
    assert repo.get("1") is None
    print("✓ Exercise 1: Repository pattern works")

    # Test Service pattern
    repo = InMemoryRepository()
    service = UserService(repo)
    user = service.execute("create", name="Bob")
    assert user.name == "Bob"
    retrieved = service.execute("get", id=user.id)
    assert retrieved.name == "Bob"
    print("✓ Exercise 2: Service pattern works")

    # Test Event-driven architecture
    class UserCreatedHandler(EventHandler):
        def __init__(self):
            self.events = []

        def handle(self, event: Event):
            self.events.append(event)

    bus = EventBus()
    handler = UserCreatedHandler()
    bus.subscribe("user.created", handler)
    event = Event("user.created", {"user_id": "1"})
    bus.publish(event)
    assert len(handler.events) == 1
    print("✓ Exercise 3: Event-driven architecture works")

    # Test Mediator
    class ComponentA:
        def on_event(self, data):
            self.received = data

    class ComponentB:
        def on_event(self, data):
            self.received = data

    mediator = Mediator()
    comp_a = ComponentA()
    comp_b = ComponentB()
    mediator.register("A", comp_a)
    mediator.register("B", comp_b)
    mediator.notify("A", "event", "test_data")
    assert hasattr(comp_b, 'received')
    print("✓ Exercise 4: Mediator pattern works")

    # Test Facade
    class SubsystemA:
        def execute(self, *args, **kwargs):
            return "A"

    class SubsystemB:
        def execute(self, *args, **kwargs):
            return "B"

    facade = Facade()
    facade.add_subsystem("A", SubsystemA())
    facade.add_subsystem("B", SubsystemB())
    result = facade.operation()
    assert result == {"A": "A", "B": "B"}
    print("✓ Exercise 5: Facade pattern works")

    print("All Architecture Design solutions passed!")
