"""
Software Architecture Patterns in Python
Demonstrates architectural patterns and best practices
"""

from abc import ABC, abstractmethod
from typing import List, Dict, Any
from dataclasses import dataclass
from enum import Enum

# ============================================
# Layered Architecture
# ============================================

class PresentationLayer:
    """Presentation layer - handles UI/API."""
    
    def __init__(self, service_layer):
        self.service = service_layer
    
    def handle_request(self, request: Dict[str, Any]) -> Dict[str, Any]:
        """Handle incoming request."""
        # Validate input
        if "user_id" not in request:
            return {"error": "Missing user_id"}
        
        # Delegate to service
        result = self.service.get_user(request["user_id"])
        return {"data": result}

class ServiceLayer:
    """Service layer - business logic."""
    
    def __init__(self, repository):
        self.repository = repository
    
    def get_user(self, user_id: int) -> Dict[str, Any]:
        """Get user with business logic."""
        user = self.repository.find_by_id(user_id)
        if not user:
            raise ValueError(f"User {user_id} not found")
        
        # Add business logic here
        return user

class RepositoryLayer:
    """Repository layer - data access."""
    
    def __init__(self):
        self.data = {
            1: {"id": 1, "name": "Alice", "email": "alice@example.com"},
            2: {"id": 2, "name": "Bob", "email": "bob@example.com"}
        }
    
    def find_by_id(self, id: int) -> Dict[str, Any]:
        """Find entity by ID."""
        return self.data.get(id)

# ============================================
# Repository Pattern
# ============================================

class Repository(ABC):
    """Abstract repository interface."""
    
    @abstractmethod
    def find_by_id(self, id: int) -> Any:
        pass
    
    @abstractmethod
    def find_all(self) -> List[Any]:
        pass
    
    @abstractmethod
    def save(self, entity: Any) -> Any:
        pass
    
    @abstractmethod
    def delete(self, id: int) -> bool:
        pass

class InMemoryRepository(Repository):
    """In-memory repository implementation."""
    
    def __init__(self):
        self.data = {}
        self.next_id = 1
    
    def find_by_id(self, id: int) -> Any:
        return self.data.get(id)
    
    def find_all(self) -> List[Any]:
        return list(self.data.values())
    
    def save(self, entity: Dict[str, Any]) -> Any:
        if "id" not in entity or entity["id"] is None:
            entity["id"] = self.next_id
            self.next_id += 1
        self.data[entity["id"]] = entity
        return entity
    
    def delete(self, id: int) -> bool:
        if id in self.data:
            del self.data[id]
            return True
        return False

# ============================================
# Domain-Driven Design
# ============================================

class Entity:
    """Base entity class."""
    
    def __init__(self, id: int = None):
        self.id = id
    
    def __eq__(self, other):
        if not isinstance(other, Entity):
            return False
        return self.id == other.id

@dataclass
class User(Entity):
    """User entity."""
    name: str
    email: str
    is_active: bool = True

@dataclass
class Order(Entity):
    """Order entity."""
    user_id: int
    items: List[str]
    status: str = "pending"

class DomainService:
    """Domain service with business logic."""
    
    def __init__(self, user_repo: Repository, order_repo: Repository):
        self.user_repo = user_repo
        self.order_repo = order_repo
    
    def place_order(self, user_id: int, items: List[str]) -> Order:
        """Place an order for a user."""
        # Check user exists
        user = self.user_repo.find_by_id(user_id)
        if not user:
            raise ValueError(f"User {user_id} not found")
        
        # Create order
        order = Order(user_id=user_id, items=items)
        return self.order_repo.save(order)

# ============================================
# CQRS Pattern
# ============================================

class Command:
    """Base command class."""
    pass

class Query:
    """Base query class."""
    pass

class CreateUserCommand(Command):
    """Create user command."""
    def __init__(self, name: str, email: str):
        self.name = name
        self.email = email

class GetUserQuery(Query):
    """Get user query."""
    def __init__(self, user_id: int):
        self.user_id = user_id

class CommandHandler:
    """Handles commands."""
    
    def __init__(self, repository: Repository):
        self.repository = repository
    
    def handle(self, command: Command) -> Any:
        if isinstance(command, CreateUserCommand):
            user = {"name": command.name, "email": command.email}
            return self.repository.save(user)
        raise ValueError(f"Unknown command: {type(command)}")

class QueryHandler:
    """Handles queries."""
    
    def __init__(self, repository: Repository):
        self.repository = repository
    
    def handle(self, query: Query) -> Any:
        if isinstance(query, GetUserQuery):
            return self.repository.find_by_id(query.user_id)
        raise ValueError(f"Unknown query: {type(query)}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Layered Architecture ===")
    repo = RepositoryLayer()
    service = ServiceLayer(repo)
    presentation = PresentationLayer(service)
    
    result = presentation.handle_request({"user_id": 1})
    print(f"  Result: {result}")
    
    print("\n=== Repository Pattern ===")
    user_repo = InMemoryRepository()
    user_repo.save({"name": "Alice", "email": "alice@example.com"})
    user_repo.save({"name": "Bob", "email": "bob@example.com"})
    print(f"  All users: {user_repo.find_all()}")
    
    print("\n=== Domain-Driven Design ===")
    user_repo = InMemoryRepository()
    order_repo = InMemoryRepository()
    domain_service = DomainService(user_repo, order_repo)
    
    # Save a user
    user_repo.save({"id": 1, "name": "Alice", "email": "alice@example.com"})
    
    # Place order
    order = domain_service.place_order(1, ["widget", "gadget"])
    print(f"  Order: {order}")
    
    print("\n=== CQRS Pattern ===")
    command_repo = InMemoryRepository()
    command_handler = CommandHandler(command_repo)
    query_handler = QueryHandler(command_repo)
    
    # Execute command
    user = command_handler.handle(CreateUserCommand("Charlie", "charlie@example.com"))
    print(f"  Created: {user}")
    
    # Execute query
    result = query_handler.handle(GetUserQuery(user["id"]))
    print(f"  Queried: {result}")
