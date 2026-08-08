"""
Module 09 - Exception Handling: Context Variables Solutions
Difficulty: Intermediate to Advanced
"""

from contextvars import ContextVar
import asyncio

# =============================================================================
# Exercise 1: Basic Context Variables - Solution
# =============================================================================
request_id_var: ContextVar[str] = ContextVar('request_id', default='unknown')
user_var: ContextVar[dict] = ContextVar('user', default={})

def handle_request(request_id, user_data):
    """Handle request with context."""
    request_id_var.set(request_id)
    user_var.set(user_data)

def get_current_request_id():
    """Get current request ID from context."""
    return request_id_var.get()

def get_current_user():
    """Get current user from context."""
    return user_var.get()

handle_request("req-123", {"name": "Alice"})
print(get_current_request_id())  # "req-123"
print(get_current_user())        # {"name": "Alice"}


# =============================================================================
# Exercise 2: Context Manager with Context Vars - Solution
# =============================================================================
class RequestContext:
    """Context manager that sets up request context."""

    def __init__(self, request_id, user):
        self.request_id = request_id
        self.user = user
        self.token = None

    def __enter__(self):
        self.token = request_id_var.set(self.request_id)
        user_var.set(self.user)
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        request_id_var.reset(self.token)
        return False

def process_order(order_id):
    """Process order using context."""
    req_id = get_current_request_id()
    user = get_current_user()
    print(f"Processing order {order_id} for {user['name']} (request: {req_id})")

def validate_order(order):
    """Validate order using context."""
    return True

def save_order(order):
    """Save order using context."""
    return True

with RequestContext("req-456", {"name": "Bob"}):
    process_order("order-789")


# =============================================================================
# Exercise 3: Async Context Variables - Solution
# =============================================================================
async_var: ContextVar[str] = ContextVar('async_var', default='default')

class AsyncRequestContext:
    """Async context manager for request context."""

    def __init__(self, request_id):
        self.request_id = request_id
        self.token = None

    async def __aenter__(self):
        self.token = async_var.set(self.request_id)
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        async_var.reset(self.token)
        return False

async def async_handler(request_id):
    """Handle request asynchronously."""
    async with AsyncRequestContext(request_id):
        print(f"Handling {request_id}, var={async_var.get()}")
        await asyncio.sleep(0.1)

async def async_process():
    """Process multiple requests concurrently."""
    tasks = [async_handler(f"req-{i}") for i in range(3)]
    await asyncio.gather(*tasks)

asyncio.run(async_process())


# =============================================================================
# Exercise 4: Context Variables for Configuration - Solution
# =============================================================================
config_var: ContextVar[dict] = ContextVar('config', default={})

class ConfigContext:
    """Context for runtime configuration."""

    def __init__(self, **config):
        self.config = config
        self.token = None

    def __enter__(self):
        current = config_var.get().copy()
        current.update(self.config)
        self.token = config_var.set(current)
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        config_var.reset(self.token)
        return False

    @staticmethod
    def get(key, default=None):
        return config_var.get().get(key, default)

def get_database_url():
    """Get database URL from context."""
    return ConfigContext.get("db_url", "postgres://default")

def get_api_key():
    """Get API key from context."""
    return ConfigContext.get("api_key", "default-key")

with ConfigContext(debug=True, db_url="postgres://localhost"):
    print(get_database_url())  # "postgres://localhost"
print(ConfigContext.get("debug"))  # None (outside context)


# =============================================================================
# Exercise 5: Context Variables for Testing - Solution
# =============================================================================
test_mode_var: ContextVar[bool] = ContextVar('test_mode', default=False)

class TestContext:
    """Context for test isolation."""

    def __init__(self):
        self.token = None

    def __enter__(self):
        self.token = test_mode_var.set(True)
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        test_mode_var.reset(self.token)
        return False

    @staticmethod
    def set_test_mode(enabled=True):
        test_mode_var.set(enabled)

    @staticmethod
    def is_test_mode():
        return test_mode_var.get()

def get_data():
    """Get data based on context (test vs production)."""
    if TestContext.is_test_mode():
        return {"source": "test", "data": [1, 2, 3]}
    return {"source": "production", "data": [4, 5, 6]}

with TestContext():
    TestContext.set_test_mode(True)
    print(get_data())  # {'source': 'test', 'data': [1, 2, 3]}

print(get_data())  # {'source': 'production', 'data': [4, 5, 6]}
