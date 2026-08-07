"""
Observer Pattern in Python
Demonstrates the Observer pattern for event-driven programming
"""

from typing import List, Callable, Any, Dict
from abc import ABC, abstractmethod

# ============================================
# Observer Interface
# ============================================

class Observer(ABC):
    """Abstract observer class."""
    
    @abstractmethod
    def update(self, event: str, data: Any) -> None:
        """Update observer with event data."""
        pass

# ============================================
# Subject (Observable)
# ============================================

class EventManager:
    """Event manager for pub/sub pattern."""
    
    def __init__(self) -> None:
        self.listeners: Dict[str, List[Callable]] = {}
    
    def subscribe(self, event: str, callback: Callable) -> None:
        """Subscribe to an event."""
        if event not in self.listeners:
            self.listeners[event] = []
        self.listeners[event].append(callback)
    
    def unsubscribe(self, event: str, callback: Callable) -> None:
        """Unsubscribe from an event."""
        if event in self.listeners:
            self.listeners[event].remove(callback)
    
    def notify(self, event: str, data: Any = None) -> None:
        """Notify all subscribers of an event."""
        if event in self.listeners:
            for callback in self.listeners[event]:
                callback(data)

# ============================================
# Concrete Observer
# ============================================

class EmailNotification(Observer):
    """Email notification observer."""
    
    def __init__(self, email: str) -> None:
        self.email = email
    
    def update(self, event: str, data: Any) -> None:
        """Send email notification."""
        print(f"[EMAIL] To: {self.email} | Event: {event} | Data: {data}")

class SMSNotification(Observer):
    """SMS notification observer."""
    
    def __init__(self, phone: str) -> None:
        self.phone = phone
    
    def update(self, event: str, data: Any) -> None:
        """Send SMS notification."""
        print(f"[SMS] To: {self.phone} | Event: {event} | Data: {data}")

class LogObserver(Observer):
    """Logging observer."""
    
    def __init__(self) -> None:
        self.logs: List[str] = []
    
    def update(self, event: str, data: Any) -> None:
        """Log the event."""
        log_entry = f"{event}: {data}"
        self.logs.append(log_entry)
        print(f"[LOG] {log_entry}")
    
    def get_logs(self) -> List[str]:
        """Get all logs."""
        return self.logs.copy()

# ============================================
# Practical Example: Order System
# ============================================

class OrderSystem:
    """Order system using observer pattern."""
    
    def __init__(self) -> None:
        self.observers: Dict[str, List[Callable]] = {}
        self.orders: List[Dict] = []
    
    def subscribe(self, event: str, callback: Callable) -> None:
        """Subscribe to order events."""
        if event not in self.observers:
            self.observers[event] = []
        self.observers[event].append(callback)
    
    def notify(self, event: str, data: Any) -> None:
        """Notify observers of order event."""
        if event in self.observers:
            for callback in self.observers[event]:
                callback(data)
    
    def place_order(self, order: Dict) -> None:
        """Place a new order."""
        self.orders.append(order)
        self.notify("order_placed", order)
    
    def process_order(self, order_id: int) -> None:
        """Process an order."""
        for order in self.orders:
            if order["id"] == order_id:
                order["status"] = "processing"
                self.notify("order_processing", order)
                break
    
    def ship_order(self, order_id: int) -> None:
        """Ship an order."""
        for order in self.orders:
            if order["id"] == order_id:
                order["status"] = "shipped"
                self.notify("order_shipped", order)
                break

# ============================================
# Example Callbacks
# ============================================

def on_order_placed(order: Dict) -> None:
    """Callback when order is placed."""
    print(f"  Order #{order['id']} placed by {order['customer']}")

def on_order_processing(order: Dict) -> None:
    """Callback when order is processing."""
    print(f"  Order #{order['id']} is now processing")

def on_order_shipped(order: Dict) -> None:
    """Callback when order is shipped."""
    print(f"  Order #{order['id']} has been shipped")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Observer Pattern ===")
    manager = EventManager()
    
    # Create observers
    email_observer = EmailNotification("user@example.com")
    sms_observer = SMSNotification("+1234567890")
    log_observer = LogObserver()
    
    # Subscribe observers
    manager.subscribe("user_registered", email_observer.update)
    manager.subscribe("user_registered", sms_observer.update)
    manager.subscribe("user_registered", log_observer.update)
    
    # Notify observers
    manager.notify("user_registered", {"user_id": 1, "name": "Alice"})
    print()
    
    print("=== Order System ===")
    order_system = OrderSystem()
    
    # Subscribe to order events
    order_system.subscribe("order_placed", on_order_placed)
    order_system.subscribe("order_placed", on_order_placed)
    order_system.subscribe("order_processing", on_order_processing)
    order_system.subscribe("order_shipped", on_order_shipped)
    
    # Place and process orders
    order1 = {"id": 1, "customer": "Alice", "items": ["Widget"], "status": "pending"}
    order_system.place_order(order1)
    
    order_system.process_order(1)
    order_system.ship_order(1)
    
    print("\n=== Unsubscribe Example ===")
    manager.unsubscribe("user_registered", sms_observer.update)
    manager.notify("user_registered", {"user_id": 2, "name": "Bob"})
