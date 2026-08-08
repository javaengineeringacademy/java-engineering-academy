"""
Module 11 - Design Patterns: Observer Solutions
Complete solutions with explanations
"""

import asyncio
from typing import List, Callable, Any


# =============================================================================
# Exercise 1: Basic Observer - SOLUTION
# =============================================================================

class Subject:
    """
    Subject that manages observers and notifies them of state changes.
    
    The Subject maintains a list of observers and calls their update()
    method when state changes.
    """
    def __init__(self):
        self._observers = []
        self._state = None
    
    def attach(self, observer):
        """Register an observer."""
        if observer not in self._observers:
            self._observers.append(observer)
    
    def detach(self, observer):
        """Unregister an observer."""
        if observer in self._observers:
            self._observers.remove(observer)
    
    def notify(self):
        """Notify all observers of state change."""
        for observer in self._observers:
            observer.update(self)
    
    @property
    def state(self):
        return self._state
    
    @state.setter
    def state(self, value):
        """Update state and notify observers."""
        self._state = value
        self.notify()


class Observer:
    """Observer interface with update method."""
    def update(self, subject):
        pass


# =============================================================================
# Exercise 2: Event System - SOLUTION
# =============================================================================

class EventEmitter:
    """
    Event system that supports multiple event types.
    
    Handlers are stored in a dictionary keyed by event type.
    When an event is emitted, all registered handlers are called.
    """
    def __init__(self):
        self._handlers = {}
    
    def on(self, event_type, handler):
        """Register a handler for an event type."""
        if event_type not in self._handlers:
            self._handlers[event_type] = []
        if handler not in self._handlers[event_type]:
            self._handlers[event_type].append(handler)
    
    def off(self, event_type, handler):
        """Unregister a handler from an event type."""
        if event_type in self._handlers:
            if handler in self._handlers[event_type]:
                self._handlers[event_type].remove(handler)
    
    def emit(self, event_type, *args, **kwargs):
        """Emit an event and call all registered handlers."""
        if event_type in self._handlers:
            for handler in self._handlers[event_type]:
                handler(*args, **kwargs)


# =============================================================================
# Exercise 3: Observable Property - SOLUTION
# =============================================================================

class ObservableProperty:
    """
    A property descriptor that notifies listeners when the value changes.
    
    This implements the descriptor protocol (__get__ and __set__) to
    create properties that automatically notify observers.
    """
    def __init__(self, name, default=None):
        self.name = name
        self.default = default
        self._listeners = []
    
    def add_listener(self, callback):
        """Add a callback to be called when value changes."""
        if callback not in self._listeners:
            self._listeners.append(callback)
    
    def remove_listener(self, callback):
        """Remove a callback from listeners."""
        if callback in self._listeners:
            self._listeners.remove(callback)
    
    def __get__(self, obj, objtype=None):
        """Return the value (or the descriptor if accessed from class)."""
        if obj is None:
            return self
        return getattr(obj, f'_obs_{self.name}', self.default)
    
    def __set__(self, obj, value):
        """Set the value and notify listeners if changed."""
        old_value = self.__get__(obj)
        if old_value != value:
            setattr(obj, f'_obs_{self.name}', value)
            for listener in self._listeners:
                listener(old_value, value)


# =============================================================================
# Exercise 4: Observer with Priority - SOLUTION
# =============================================================================

class PrioritySubject:
    """
    Subject that supports observer priority.
    
    Observers are stored with their priority values. When notifying,
    observers are called in priority order (higher priority first).
    """
    def __init__(self):
        self._observers = {}  # observer -> priority
        self._state = None
    
    def attach(self, observer, priority=0):
        """Add observer with priority."""
        self._observers[observer] = priority
    
    def detach(self, observer):
        """Remove observer."""
        if observer in self._observers:
            del self._observers[observer]
    
    def update_priority(self, observer, priority):
        """Update observer's priority."""
        if observer in self._observers:
            self._observers[observer] = priority
    
    def notify(self):
        """Notify observers in priority order (highest first)."""
        # Sort observers by priority (descending)
        sorted_observers = sorted(
            self._observers.items(),
            key=lambda x: x[1],
            reverse=True
        )
        for observer, _ in sorted_observers:
            observer.update(self)
    
    @property
    def state(self):
        return self._state
    
    @state.setter
    def state(self, value):
        self._state = value
        self.notify()


# =============================================================================
# Exercise 5: Async Observer - SOLUTION
# =============================================================================

class AsyncSubject:
    """
    Subject that notifies observers asynchronously.
    
    This is useful when observer callbacks involve I/O operations
    or other async work.
    """
    def __init__(self):
        self._observers = []
        self._state = None
    
    async def attach(self, observer):
        """Add observer asynchronously."""
        if observer not in self._observers:
            self._observers.append(observer)
    
    async def detach(self, observer):
        """Remove observer asynchronously."""
        if observer in self._observers:
            self._observers.remove(observer)
    
    async def notify(self):
        """Notify all observers asynchronously."""
        tasks = [observer.update(self) for observer in self._observers]
        await asyncio.gather(*tasks)
    
    @property
    def state(self):
        return self._state
    
    @state.setter
    def state(self, value):
        self._state = value


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Observer Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Observer")
    subject = Subject()
    results = []
    
    class ConcreteObserver(Observer):
        def __init__(self, name):
            self.name = name
        def update(self, subject):
            results.append(self.name)
    
    obs1 = ConcreteObserver("obs1")
    obs2 = ConcreteObserver("obs2")
    subject.attach(obs1)
    subject.attach(obs2)
    subject.state = "new"
    assert len(results) == 2, "Both observers should be notified"
    assert "obs1" in results and "obs2" in results
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Event System")
    emitter = EventEmitter()
    results = []
    handler = lambda x: results.append(x)
    emitter.on("click", handler)
    emitter.emit("click", "clicked")
    assert "clicked" in results, "Handler should be called"
    emitter.off("click", handler)
    emitter.emit("click", "removed")
    assert len(results) == 1, "Handler should be removed"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Observable Property")
    class MyClass:
        name = ObservableProperty("name", "default")
    
    obj = MyClass()
    changes = []
    obj.name.add_listener(lambda o, n: changes.append((o, n)))
    obj.name = "new_value"
    assert len(changes) == 1, "Should have one change"
    assert changes[0] == ("default", "new_value"), "Should track old and new"
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Observer with Priority")
    subject = PrioritySubject()
    results = []
    
    class PriorityObserver:
        def __init__(self, name):
            self.name = name
        def update(self, subject):
            results.append(self.name)
    
    obs_high = PriorityObserver("high")
    obs_low = PriorityObserver("low")
    subject.attach(obs_low, priority=1)
    subject.attach(obs_high, priority=10)
    subject.notify()
    assert results == ["high", "low"], "Should notify in priority order"
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Async Observer")
    async def test_async():
        subject = AsyncSubject()
        results = []
        
        class AsyncObserver:
            def __init__(self, name):
                self.name = name
            async def update(self, subject):
                results.append(self.name)
        
        obs1 = AsyncObserver("obs1")
        await subject.attach(obs1)
        subject._state = "new"
        await subject.notify()
        return len(results) == 1
    
    assert asyncio.run(test_async()), "Async observer should work"
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
