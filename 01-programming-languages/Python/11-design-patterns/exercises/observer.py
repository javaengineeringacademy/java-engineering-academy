"""
Module 11 - Design Patterns: Observer Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Observer pattern implementation
"""


# =============================================================================
# Exercise 1: Basic Observer (⭐⭐⭐)
# =============================================================================

class Subject:
    """
    Implement a basic Subject that manages observers.
    
    TODO:
    1. Add attach() method to register observers
    2. Add detach() method to remove observers
    3. Add notify() method to inform all observers
    """
    def __init__(self):
        self._observers = []
        self._state = None
    
    def attach(self, observer):
        # TODO: Add observer to list
        pass
    
    def detach(self, observer):
        # TODO: Remove observer from list
        pass
    
    def notify(self):
        # TODO: Notify all observers
        pass
    
    @property
    def state(self):
        return self._state
    
    @state.setter
    def state(self, value):
        # TODO: Update state and notify observers
        pass


class Observer:
    """Observer interface."""
    def update(self, subject):
        pass


# =============================================================================
# Exercise 2: Event System (⭐⭐⭐⭐)
# =============================================================================

class EventEmitter:
    """
    Implement an event system with multiple event types.
    
    TODO:
    1. Implement on() to subscribe to events
    2. Implement off() to unsubscribe
    3. Implement emit() to trigger events
    4. Support multiple event types
    """
    def __init__(self):
        self._handlers = {}
    
    def on(self, event_type, handler):
        # TODO: Register handler for event type
        pass
    
    def off(self, event_type, handler):
        # TODO: Unregister handler
        pass
    
    def emit(self, event_type, *args, **kwargs):
        # TODO: Call all handlers for event type
        pass


# =============================================================================
# Exercise 3: Observable Property (⭐⭐⭐⭐)
# =============================================================================

class ObservableProperty:
    """
    Implement a property that notifies when changed.
    
    TODO:
    1. Create a descriptor or property that tracks changes
    2. Notify listeners when value changes
    3. Support old_value and new_value in notification
    """
    def __init__(self, name, default=None):
        self.name = name
        self.default = default
        self._listeners = []
    
    def add_listener(self, callback):
        # TODO: Add callback to listeners
        pass
    
    def remove_listener(self, callback):
        # TODO: Remove callback from listeners
        pass
    
    def __get__(self, obj, objtype=None):
        # TODO: Return value
        pass
    
    def __set__(self, obj, value):
        # TODO: Set value and notify if changed
        pass


# =============================================================================
# Exercise 4: Observer with Priority (⭐⭐⭐⭐)
# =============================================================================

class PrioritySubject:
    """
    Implement a Subject that supports observer priority.
    
    TODO:
    1. Support priority when attaching observers
    2. Notify observers in priority order (higher first)
    3. Allow updating observer priority
    """
    def __init__(self):
        self._observers = {}  # observer -> priority
        self._state = None
    
    def attach(self, observer, priority=0):
        # TODO: Add observer with priority
        pass
    
    def detach(self, observer):
        # TODO: Remove observer
        pass
    
    def update_priority(self, observer, priority):
        # TODO: Update observer's priority
        pass
    
    def notify(self):
        # TODO: Notify observers in priority order
        pass


# =============================================================================
# Exercise 5: Async Observer (⭐⭐⭐⭐⭐)
# =============================================================================

import asyncio

class AsyncSubject:
    """
    Implement an async Subject that notifies observers asynchronously.
    
    TODO:
    1. Support async observer callbacks
    2. Implement async attach and detach
    3. Implement async notify that awaits all observers
    """
    def __init__(self):
        self._observers = []
        self._state = None
    
    async def attach(self, observer):
        # TODO: Add observer
        pass
    
    async def detach(self, observer):
        # TODO: Remove observer
        pass
    
    async def notify(self):
        # TODO: Await all observer callbacks
        pass
    
    @property
    def state(self):
        return self._state
    
    @state.setter
    def state(self, value):
        # TODO: Update state (consider async notification)
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 11 - Observer Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Observer")
    try:
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
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Event System")
    try:
        emitter = EventEmitter()
        results = []
        emitter.on("click", lambda x: results.append(x))
        emitter.emit("click", "clicked")
        assert "clicked" in results, "Handler should be called"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Observable Property")
    try:
        class MyClass:
            name = ObservableProperty("name", "default")
        
        obj = MyClass()
        changes = []
        obj.name.add_listener(lambda o, n: changes.append((o, n)))
        obj.name = "new_value"
        assert len(changes) == 1, "Should have one change"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Observer with Priority")
    try:
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
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Async Observer")
    try:
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
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
