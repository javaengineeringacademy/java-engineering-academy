"""
Module 18: Senior - Architecture Design Exercises
=================================================
Practice designing scalable software architectures.
"""

# =============================================================================
# Exercise 1: Dependency Injection (★☆☆☆☆)
# =============================================================================
# TODO: Implement simple dependency injection container

class Container:
    """Simple dependency injection container."""
    # TODO: Register services
    # TODO: Resolve dependencies
    pass

# Test Cases
class EmailService:
    def send(self, to, message):
        return f"Sent to {to}: {message}"

class UserService:
    def __init__(self, email_service):
        self.email = email_service
    
    def welcome_user(self, name):
        return self.email.send(name, "Welcome!")

def test_dependency_injection():
    container = Container()
    container.register(EmailService)
    container.register(UserService)
    
    service = container.resolve(UserService)
    result = service.welcome_user("alice@example.com")
    assert "alice@example.com" in result
    print("✓ Exercise 1 passed: dependency injection works")

# =============================================================================
# Exercise 2: Plugin System (★★☆☆☆)
# =============================================================================
# TODO: Implement plugin discovery and loading

class PluginManager:
    """Discover and load plugins."""
    # TODO: Scan directory for plugins
    # TODO: Load and register plugins
    pass

# Test Cases
class BasePlugin:
    name = "base"
    def execute(self): pass

class HelloPlugin(BasePlugin):
    name = "hello"
    def execute(self):
        return "Hello from plugin!"

def test_plugin_system():
    manager = PluginManager()
    manager.register(HelloPlugin())
    
    plugins = manager.get_plugins()
    assert len(plugins) >= 1
    assert plugins[0].execute() == "Hello from plugin!"
    print(f"✓ Exercise 2 passed: {len(plugins)} plugins loaded")

# =============================================================================
# Exercise 3: Event-Driven Architecture (★★★☆☆)
# =============================================================================
# TODO: Implement event-driven system

class EventBus:
    """Event-driven message bus."""
    # TODO: Publish/subscribe pattern
    # TODO: Support async handlers
    pass

# Test Cases
class OrderCreated:
    def __init__(self, order_id):
        self.order_id = order_id

def test_event_bus():
    bus = EventBus()
    received = []
    
    def handle_order(event):
        received.append(event.order_id)
    
    bus.subscribe(OrderCreated, handle_order)
    bus.publish(OrderCreated("ORD-001"))
    bus.publish(OrderCreated("ORD-002"))
    
    assert received == ["ORD-001", "ORD-002"]
    print(f"✓ Exercise 3 passed: processed {len(received)} events")

# =============================================================================
# Exercise 4: Repository Pattern (★★★★☆)
# =============================================================================
# TODO: Implement repository pattern for data access

class Repository:
    """Abstract repository for data access."""
    # TODO: Define CRUD interface
    # TODO: Support multiple backends
    pass

class InMemoryRepository(Repository):
    """In-memory repository implementation."""
    # TODO: Implement storage using dict
    pass

# Test Cases
class User:
    def __init__(self, id, name):
        self.id = id
        self.name = name

def test_repository_pattern():
    repo = InMemoryRepository()
    
    user = User(1, "Alice")
    repo.save(user)
    
    retrieved = repo.get(1)
    assert retrieved.name == "Alice"
    
    all_users = repo.get_all()
    assert len(all_users) == 1
    
    repo.delete(1)
    assert repo.get(1) is None
    print("✓ Exercise 4 passed: repository CRUD works")

# =============================================================================
# Exercise 5: CQRS Pattern (★★★★★)
# =============================================================================
# TODO: Implement Command Query Responsibility Segregation

class CommandHandler:
    """Handle write operations."""
    # TODO: Process commands
    pass

class QueryHandler:
    """Handle read operations."""
    # TODO: Process queries
    pass

class CQRSBus:
    """Dispatch commands and queries to handlers."""
    # TODO: Register and dispatch handlers
    pass

# Test Cases
class CreateUserCommand:
    def __init__(self, name, email):
        self.name = name
        self.email = email

class GetUserQuery:
    def __init__(self, user_id):
        self.user_id = user_id

def test_cqrs():
    bus = CQRSBus()
    bus.register_command(CreateUserCommand, CommandHandler())
    bus.register_query(GetUserQuery, QueryHandler())
    
    result = bus.dispatch(CreateUserCommand("Alice", "alice@example.com"))
    assert result["status"] == "created"
    
    user = bus.query(GetUserQuery(1))
    assert user["name"] == "Alice"
    print("✓ Exercise 5 passed: CQRS pattern implemented")

if __name__ == "__main__":
    print("Running Architecture Design Exercises...")
    print("=" * 50)
    test_dependency_injection()
    test_plugin_system()
    test_event_bus()
    test_repository_pattern()
    test_cqrs()
    print("=" * 50)
    print("All tests passed!")
