"""
Module 11: Design Patterns - Observer Exercises
===============================================
Practice implementing the Observer design pattern.
"""

# =============================================================================
# Exercise 1: Basic Observer (★☆☆☆☆)
# =============================================================================
# TODO: Implement simple observer pattern

class EventEmitter:
    """Simple event emitter with subscribe/emit."""
    # TODO: Implement on, off, and emit methods
    pass

# Test Cases
def test_basic_observer():
    emitter = EventEmitter()
    results = []
    
    def handler(data):
        results.append(data)
    
    emitter.on("data", handler)
    emitter.emit("data", "hello")
    emitter.emit("data", "world")
    
    assert results == ["hello", "world"]
    print(f"✓ Exercise 1 passed: received {len(results)} events")

# =============================================================================
# Exercise 2: Multi-Event Observer (★★☆☆☆)
# =============================================================================
# TODO: Support multiple event types

class EventBus:
    """Event bus supporting multiple named events."""
    # TODO: Implement subscribe, unsubscribe, publish
    pass

# Test Cases
def test_event_bus():
    bus = EventBus()
    results = {"click": [], "hover": []}
    
    def on_click(pos):
        results["click"].append(pos)
    
    def on_hover(elem):
        results["hover"].append(elem)
    
    bus.subscribe("click", on_click)
    bus.subscribe("hover", on_hover)
    
    bus.publish("click", (100, 200))
    bus.publish("hover", "button")
    bus.publish("click", (150, 250))
    
    assert len(results["click"]) == 2
    assert len(results["hover"]) == 1
    print("✓ Exercise 2 passed: multi-event working")

# =============================================================================
# Exercise 3: Priority Observer (★★★☆☆)
# =============================================================================
# TODO: Execute observers in priority order

class PriorityEventEmitter:
    """Event emitter with priority-based execution order."""
    # TODO: Support priority parameter in subscribe
    pass

# Test Cases
def test_priority_observer():
    emitter = PriorityEventEmitter()
    order = []
    
    def low_priority(data):
        order.append("low")
    
    def high_priority(data):
        order.append("high")
    
    def medium_priority(data):
        order.append("medium")
    
    emitter.on("event", low_priority, priority=1)
    emitter.on("event", high_priority, priority=10)
    emitter.on("event", medium_priority, priority=5)
    
    emitter.emit("event", None)
    
    assert order == ["high", "medium", "low"]
    print(f"✓ Exercise 3 passed: execution order is {order}")

# =============================================================================
# Exercise 4: Once Observer (★★★★☆)
# =============================================================================
# TODO: Support one-time observers that auto-remove

class OnceEmitter:
    """Event emitter supporting one-time listeners."""
    # TODO: Implement once method
    pass

# Test Cases
def test_once_observer():
    emitter = OnceEmitter()
    call_count = 0
    
    def handler(data):
        nonlocal call_count
        call_count += 1
    
    emitter.once("event", handler)
    emitter.emit("event", "first")
    emitter.emit("event", "second")
    emitter.emit("event", "third")
    
    assert call_count == 1
    print(f"✓ Exercise 4 passed: once handler called {call_count} time(s)")

# =============================================================================
# Exercise 5: Observable Property (★★★★★)
# =============================================================================
# TODO: Create property that notifies on change

class Observable:
    """Class with observable properties."""
    # TODO: Use descriptor or metaclass to make properties observable
    pass

# Test Cases
class User(Observable):
    def __init__(self):
        self.name = ""
        self.email = ""

def test_observable_property():
    user = User()
    changes = []
    
    def on_change(attr, old_val, new_val):
        changes.append((attr, old_val, new_val))
    
    user.observe(on_change)
    user.name = "Alice"
    user.name = "Bob"
    user.email = "bob@example.com"
    
    assert len(changes) == 3
    assert changes[0] == ("name", "", "Alice")
    print(f"✓ Exercise 5 passed: observed {len(changes)} changes")

if __name__ == "__main__":
    print("Running Observer Pattern Exercises...")
    print("=" * 50)
    test_basic_observer()
    test_event_bus()
    test_priority_observer()
    test_once_observer()
    test_observable_property()
    print("=" * 50)
    print("All tests passed!")
