"""
Module 18 - Senior: Architecture Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Software architecture patterns and principles
"""

from abc import ABC, abstractmethod
from typing import List, Dict, Any
from dataclasses import dataclass
from enum import Enum


# =============================================================================
# Exercise 1: SOLID Principles (⭐⭐⭐⭐)
# =============================================================================

# TODO: Refactor this code to follow SOLID principles

class UserManagerBad:
    """Violates Single Responsibility Principle."""
    
    def create_user(self, data):
        # Creates user
        pass
    
    def save_to_database(self, user):
        # Saves to database
        pass
    
    def send_email(self, user, message):
        # Sends email
        pass
    
    def log_action(self, action):
        # Logs action
        pass

def exercise_1_solid():
    """
    Refactor to follow SOLID principles.
    
    TODO:
    1. Apply Single Responsibility Principle
    2. Apply Open/Closed Principle
    3. Apply Dependency Inversion Principle
    """
    # TODO: Create properly designed classes
    pass


# =============================================================================
# Exercise 2: Repository Pattern (⭐⭐⭐⭐)
# =============================================================================

class Repository(ABC):
    """Abstract repository interface."""
    
    @abstractmethod
    def get(self, id):
        pass
    
    @abstractmethod
    def add(self, entity):
        pass
    
    @abstractmethod
    def update(self, entity):
        pass
    
    @abstractmethod
    def delete(self, id):
        pass


class UserRepository(Repository):
    """TODO: Implement user repository."""
    
    def __init__(self):
        self._users = {}
    
    def get(self, id):
        # TODO: Get user by id
        pass
    
    def add(self, user):
        # TODO: Add user
        pass
    
    def update(self, user):
        # TODO: Update user
        pass
    
    def delete(self, id):
        # TODO: Delete user
        pass


# =============================================================================
# Exercise 3: Domain-Driven Design (⭐⭐⭐⭐⭐)
# =============================================================================

@dataclass
class Entity:
    """Base entity class."""
    id: Any


class Order:
    """
    Order aggregate root.
    
    TODO:
    1. Implement aggregate root pattern
    2. Add domain events
    3. Enforce invariants
    """
    def __init__(self, order_id, customer_id):
        self.order_id = order_id
        self.customer_id = customer_id
        self.items = []
        self.status = "pending"
    
    def add_item(self, product_id, quantity, price):
        # TODO: Add item with validation
        pass
    
    def calculate_total(self):
        # TODO: Calculate order total
        pass
    
    def confirm(self):
        # TODO: Confirm order (enforce invariants)
        pass


# =============================================================================
# Exercise 4: CQRS Pattern (⭐⭐⭐⭐⭐)
# =============================================================================

class Command:
    """Base command class."""
    pass


class Query:
    """Base query class."""
    pass


class CreateUserCommand(Command):
    def __init__(self, name, email):
        self.name = name
        self.email = email


class GetUserQuery(Query):
    def __init__(self, user_id):
        self.user_id = user_id


class CommandHandler:
    """TODO: Implement command handler."""
    
    def handle(self, command):
        pass


class QueryHandler:
    """TODO: Implement query handler."""
    
    def handle(self, query):
        pass


# =============================================================================
# Exercise 5: Event Sourcing (⭐⭐⭐⭐⭐)
# =============================================================================

class Event:
    """Base event class."""
    def __init__(self, aggregate_id, data):
        self.aggregate_id = aggregate_id
        self.data = data


class EventStore:
    """TODO: Implement event store."""
    
    def __init__(self):
        self._events = {}
    
    def append(self, event):
        # TODO: Append event
        pass
    
    def get_events(self, aggregate_id):
        # TODO: Get events for aggregate
        pass
    
    def replay(self, aggregate_id):
        # TODO: Replay events to rebuild state
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 18 - Architecture Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: SOLID Principles")
    try:
        result = exercise_1_solid()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Repository Pattern")
    try:
        repo = UserRepository()
        repo.add({'id': 1, 'name': 'John'})
        user = repo.get(1)
        print(f"  User: {user}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Domain-Driven Design")
    try:
        order = Order('order_1', 'customer_1')
        order.add_item('product_1', 2, 10.0)
        total = order.calculate_total()
        print(f"  Order total: {total}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: CQRS Pattern")
    try:
        command = CreateUserCommand("John", "john@example.com")
        query = GetUserQuery(1)
        print(f"  Command: {command}")
        print(f"  Query: {query}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Event Sourcing")
    try:
        store = EventStore()
        event = Event('agg_1', {'action': 'created'})
        store.append(event)
        events = store.get_events('agg_1')
        print(f"  Events: {events}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
