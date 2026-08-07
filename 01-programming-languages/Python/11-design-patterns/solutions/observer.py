"""
Module 11: Design Patterns - Observer Solutions
Practice implementing the Observer design pattern.
"""

from abc import ABC, abstractmethod
from typing import List, Dict, Any


class Observer(ABC):
    """Abstract base class for observers."""

    @abstractmethod
    def update(self, subject, event_type: str, data: Any) -> None:
        """Called when the subject notifies observers."""
        pass


class Subject:
    """Subject that maintains a list of observers."""

    def __init__(self):
        self._observers: Dict[str, List[Observer]] = {}

    def attach(self, observer: Observer, event_type: str = "default") -> None:
        """Attach an observer to a specific event type."""
        if event_type not in self._observers:
            self._observers[event_type] = []
        if observer not in self._observers[event_type]:
            self._observers[event_type].append(observer)

    def detach(self, observer: Observer, event_type: str = "default") -> None:
        """Detach an observer from a specific event type."""
        if event_type in self._observers:
            self._observers[event_type].remove(observer)

    def notify(self, event_type: str, data: Any = None) -> None:
        """Notify all observers of a specific event type."""
        if event_type in self._observers:
            for observer in self._observers[event_type]:
                observer.update(self, event_type, data)


class EventEmitter:
    """Event emitter for simple event handling."""

    def __init__(self):
        self._handlers: Dict[str, List[callable]] = {}

    def on(self, event: str, handler: callable) -> None:
        """Register a handler for an event."""
        if event not in self._handlers:
            self._handlers[event] = []
        self._handlers[event].append(handler)

    def off(self, event: str, handler: callable) -> None:
        """Remove a handler for an event."""
        if event in self._handlers:
            self._handlers[event].remove(handler)

    def emit(self, event: str, *args, **kwargs) -> None:
        """Emit an event to all registered handlers."""
        if event in self._handlers:
            for handler in self._handlers[event]:
                handler(*args, **kwargs)


class Event:
    """Event object with metadata."""

    def __init__(self, event_type: str, source: Any, data: Any = None):
        self.event_type = event_type
        self.source = source
        self.data = data
        self.timestamp = None

    def __repr__(self):
        return f"Event({self.event_type}, source={self.source})"


class EventBus:
    """Central event bus for decoupled communication."""

    def __init__(self):
        self._subscribers: Dict[str, List[callable]] = {}

    def subscribe(self, event_type: str, callback: callable) -> None:
        """Subscribe to an event type."""
        if event_type not in self._subscribers:
            self._subscribers[event_type] = []
        self._subscribers[event_type].append(callback)

    def unsubscribe(self, event_type: str, callback: callable) -> None:
        """Unsubscribe from an event type."""
        if event_type in self._subscribers:
            self._subscribers[event_type].remove(callback)

    def publish(self, event: Event) -> None:
        """Publish an event to all subscribers."""
        if event.event_type in self._subscribers:
            for callback in self._subscribers[event.event_type]:
                callback(event)


if __name__ == "__main__":
    print("Testing Observer Solutions...")

    # Test Observer pattern
    class Logger(Observer):
        def __init__(self):
            self.logs = []

        def update(self, subject, event_type, data):
            self.logs.append(f"{event_type}: {data}")

    class Storage(Observer):
        def __init__(self):
            self.storage = []

        def update(self, subject, event_type, data):
            self.storage.append(data)

    subject = Subject()
    logger = Logger()
    storage = Storage()

    subject.attach(logger, "data")
    subject.attach(storage, "data")

    subject.notify("data", {"value": 42})
    assert len(logger.logs) == 1
    assert len(storage.storage) == 1
    print("✓ Observer pattern works")

    # Test EventEmitter
    emitter = EventEmitter()
    received = []

    def on_data(data):
        received.append(data)

    emitter.on("data", on_data)
    emitter.emit("data", {"key": "value"})
    assert len(received) == 1
    print("✓ EventEmitter works")

    # Test EventBus
    bus = EventBus()
    events = []

    def handle_event(event):
        events.append(event)

    bus.subscribe("user.created", handle_event)
    event = Event("user.created", "system", {"user_id": 123})
    bus.publish(event)
    assert len(events) == 1
    print("✓ EventBus works")

    print("All Observer solutions passed!")
