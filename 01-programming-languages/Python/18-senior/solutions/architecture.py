"""
Module 18 - Senior: Architecture Solutions
Complete solutions with explanations
"""

from abc import ABC, abstractmethod
from typing import List, Dict, Any, Optional
from dataclasses import dataclass, field
from enum import Enum


# =============================================================================
# Exercise 1: SOLID Principles - SOLUTION
# =============================================================================

# Single Responsibility: Each class has one responsibility
class UserRepository:
    """Handles user data persistence."""
    
    def __init__(self):
        self._users = {}
    
    def save(self, user):
        self._users[user['id']] = user
    
    def find(self, user_id):
        return self._users.get(user_id)


class EmailService:
    """Handles sending emails."""
    
    def send(self, to, subject, body):
        return f"Email sent to {to}: {subject}"


class Logger:
    """Handles logging."""
    
    def log(self, message):
        return f"LOG: {message}"


# Open/Closed: Open for extension, closed for modification
class NotificationService(ABC):
    @abstractmethod
    def send(self, recipient, message):
        pass

class EmailNotification(NotificationService):
    def send(self, recipient, message):
        return f"Email to {recipient}: {message}"

class SMSNotification(NotificationService):
    def send(self, recipient, message):
        return f"SMS to {recipient}: {message}"


# Dependency Inversion: Depend on abstractions
class UserManager:
    def __init__(self, repository: UserRepository, 
                 email_service: EmailService,
                 logger: Logger):
        self.repository = repository
        self.email_service = email_service
        self.logger = logger
    
    def create_user(self, data):
        self.repository.save(data)
        self.logger.log(f"User created: {data['id']}")
        return data


def exercise_1_solid():
    """Demonstrate SOLID principles."""
    repo = UserRepository()
    email = EmailService()
    logger = Logger()
    
    manager = UserManager(repo, email, logger)
    user = manager.create_user({'id': 1, 'name': 'John'})
    
    # Test notification service (Open/Closed)
    email_notif = EmailNotification()
    sms_notif = SMSNotification()
    
    return {
        'user': user,
        'email_notif': email_notif.send('john@test.com', 'Hello'),
        'sms_notif': sms_notif.send('1234567890', 'Hello'),
    }


# =============================================================================
# Exercise 2: Repository Pattern - SOLUTION
# =============================================================================

@dataclass
class User:
    id: int
    name: str
    email: str


class UserRepository:
    """Concrete repository implementation."""
    
    def __init__(self):
        self._users: Dict[int, User] = {}
    
    def get(self, id) -> Optional[User]:
        return self._users.get(id)
    
    def add(self, user: User):
        self._users[user.id] = user
    
    def update(self, user: User):
        if user.id in self._users:
            self._users[user.id] = user
    
    def delete(self, id):
        if id in self._users:
            del self._users[id]
    
    def get_all(self) -> List[User]:
        return list(self._users.values())


# =============================================================================
# Exercise 3: Domain-Driven Design - SOLUTION
# =============================================================================

class OrderStatus(Enum):
    PENDING = "pending"
    CONFIRMED = "confirmed"
    SHIPPED = "shipped"
    DELIVERED = "delivered"


@dataclass
class OrderItem:
    product_id: str
    quantity: int
    price: float
    
    @property
    def total(self):
        return self.quantity * self.price


class Order:
    """Order aggregate root."""
    
    def __init__(self, order_id: str, customer_id: str):
        self.order_id = order_id
        self.customer_id = customer_id
        self.items: List[OrderItem] = []
        self.status = OrderStatus.PENDING
        self._events = []
    
    def add_item(self, product_id: str, quantity: int, price: float):
        """Add item with validation."""
        if quantity <= 0:
            raise ValueError("Quantity must be positive")
        if price <= 0:
            raise ValueError("Price must be positive")
        
        self.items.append(OrderItem(product_id, quantity, price))
        self._add_event("item_added", {"product_id": product_id})
    
    def calculate_total(self) -> float:
        """Calculate order total."""
        return sum(item.total for item in self.items)
    
    def confirm(self):
        """Confirm order (enforce invariants)."""
        if self.status != OrderStatus.PENDING:
            raise ValueError("Can only confirm pending orders")
        if not self.items:
            raise ValueError("Cannot confirm empty order")
        
        self.status = OrderStatus.CONFIRMED
        self._add_event("order_confirmed", {"total": self.calculate_total()})
    
    def _add_event(self, event_type, data):
        self._events.append({"type": event_type, "data": data})


# =============================================================================
# Exercise 4: CQRS Pattern - SOLUTION
# =============================================================================

class Command:
    pass

class Query:
    pass

@dataclass
class CreateUserCommand(Command):
    name: str
    email: str

@dataclass
class GetUserQuery(Query):
    user_id: int

class InMemoryStore:
    def __init__(self):
        self.users = {}
        self.next_id = 1

class CommandHandler:
    def __init__(self, store: InMemoryStore):
        self.store = store
    
    def handle(self, command: Command):
        if isinstance(command, CreateUserCommand):
            user = User(
                id=self.store.next_id,
                name=command.name,
                email=command.email
            )
            self.store.users[user.id] = user
            self.store.next_id += 1
            return user

class QueryHandler:
    def __init__(self, store: InMemoryStore):
        self.store = store
    
    def handle(self, query: Query):
        if isinstance(query, GetUserQuery):
            return self.store.users.get(query.user_id)


# =============================================================================
# Exercise 5: Event Sourcing - SOLUTION
# =============================================================================

class Event:
    def __init__(self, aggregate_id: str, event_type: str, data: dict):
        self.aggregate_id = aggregate_id
        self.event_type = event_type
        self.data = data
        self.timestamp = None

class EventStore:
    def __init__(self):
        self._events: Dict[str, List[Event]] = {}
    
    def append(self, event: Event):
        if event.aggregate_id not in self._events:
            self._events[event.aggregate_id] = []
        self._events[event.aggregate_id].append(event)
    
    def get_events(self, aggregate_id: str) -> List[Event]:
        return self._events.get(aggregate_id, [])
    
    def replay(self, aggregate_id: str) -> dict:
        """Replay events to rebuild state."""
        state = {}
        for event in self.get_events(aggregate_id):
            if event.event_type == "created":
                state = event.data.copy()
            elif event.event_type == "updated":
                state.update(event.data)
            elif event.event_type == "deleted":
                state = {}
        return state


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 18 - Architecture Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: SOLID Principles")
    result = exercise_1_solid()
    assert result['user']['id'] == 1
    assert 'Email' in result['email_notif']
    print(f"  User: {result['user']}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Repository Pattern")
    repo = UserRepository()
    user1 = User(1, "John", "john@test.com")
    user2 = User(2, "Jane", "jane@test.com")
    
    repo.add(user1)
    repo.add(user2)
    assert repo.get(1) == user1
    assert len(repo.get_all()) == 2
    
    repo.delete(1)
    assert repo.get(1) is None
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Domain-Driven Design")
    order = Order("order_1", "customer_1")
    order.add_item("product_1", 2, 10.0)
    order.add_item("product_2", 1, 20.0)
    
    assert order.calculate_total() == 40.0
    order.confirm()
    assert order.status == OrderStatus.CONFIRMED
    print(f"  Order total: {order.calculate_total()}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: CQRS Pattern")
    store = InMemoryStore()
    cmd_handler = CommandHandler(store)
    query_handler = QueryHandler(store)
    
    user = cmd_handler.handle(CreateUserCommand("John", "john@test.com"))
    retrieved = query_handler.handle(GetUserQuery(user.id))
    
    assert user.name == retrieved.name
    print(f"  User: {retrieved.name}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Event Sourcing")
    event_store = EventStore()
    event_store.append(Event("agg_1", "created", {"name": "John"}))
    event_store.append(Event("agg_1", "updated", {"email": "john@test.com"}))
    
    state = event_store.replay("agg_1")
    assert state == {"name": "John", "email": "john@test.com"}
    print(f"  Replayed state: {state}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
