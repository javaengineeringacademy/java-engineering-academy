"""
Context Variables in Python
Demonstrates contextvars for managing context-local state
"""

import contextvars
from typing import Optional

# ============================================
# Basic Context Variable
# ============================================

# Create context variables
request_id: contextvars.ContextVar[str] = contextvars.ContextVar('request_id', default='default')
user_id: contextvars.ContextVar[Optional[int]] = contextvars.ContextVar('user_id', default=None)

def handle_request(req_id: str) -> None:
    """Handle a request with context variable."""
    token = request_id.set(req_id)
    try:
        print(f"Processing request {request_id.get()}")
        process_request()
    finally:
        request_id.reset(token)

def process_request() -> None:
    """Process the current request."""
    req_id = request_id.get()
    print(f"  Inner function sees request: {req_id}")

# ============================================
# Context Variable with User Context
# ============================================

current_user: contextvars.ContextVar[dict] = contextvars.ContextVar('current_user', default={})

def login(user_data: dict) -> None:
    """Simulate user login."""
    token = current_user.set(user_data)
    try:
        print(f"User logged in: {current_user.get()['name']}")
        access_resource()
    finally:
        current_user.reset(token)

def access_resource() -> None:
    """Access a resource using current user context."""
    user = current_user.get()
    print(f"  Accessing resource as {user['name']} with role {user['role']}")

# ============================================
# Context Variable for Database Connections
# ============================================

db_connection: contextvars.ContextVar[str] = contextvars.ContextVar('db_connection', default='default_db')

def get_db() -> str:
    """Get current database connection."""
    return db_connection.get()

def use_database(db_name: str) -> None:
    """Use a specific database."""
    token = db_connection.set(db_name)
    try:
        print(f"Using database: {get_db()}")
        query_database()
    finally:
        db_connection.reset(token)

def query_database() -> None:
    """Query the current database."""
    db = get_db()
    print(f"  Querying {db}")

# ============================================
# Context Variable with Async-like Patterns
# ============================================

trace_id: contextvars.ContextVar[str] = contextvars.ContextVar('trace_id', default='no-trace')

def create_child_context() -> None:
    """Create a child context with modified variable."""
    parent_token = trace_id.set('trace-001')
    try:
        print(f"Parent context: {trace_id.get()}")
        
        # Create child context
        child_token = trace_id.set('trace-002')
        try:
            print(f"Child context: {trace_id.get()}")
            process_with_trace()
        finally:
            trace_id.reset(child_token)
        
        print(f"Back to parent: {trace_id.get()}")
    finally:
        trace_id.reset(parent_token)

def process_with_trace() -> None:
    """Process with current trace ID."""
    print(f"  Processing with trace: {trace_id.get()}")

# ============================================
# Context Variable for Feature Flags
# ============================================

feature_flags: contextvars.ContextVar[dict] = contextvars.ContextVar(
    'feature_flags', 
    default={'new_ui': False, 'beta_features': False}
)

def enable_feature(feature: str) -> None:
    """Enable a feature in current context."""
    flags = feature_flags.get().copy()
    flags[feature] = True
    token = feature_flags.set(flags)
    try:
        print(f"Feature '{feature}' enabled")
        check_features()
    finally:
        feature_flags.reset(token)

def check_features() -> None:
    """Check current feature flags."""
    flags = feature_flags.get()
    print(f"  Active features: {[k for k, v in flags.items() if v]}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Basic Context Variable ===")
    handle_request("req-123")
    handle_request("req-456")
    
    print("\n=== User Context ===")
    login({"name": "Alice", "role": "admin"})
    login({"name": "Bob", "role": "user"})
    
    print("\n=== Database Context ===")
    use_database("production")
    use_database("staging")
    
    print("\n=== Child Context ===")
    create_child_context()
    
    print("\n=== Feature Flags ===")
    enable_feature("new_ui")
    enable_feature("beta_features")
    
    # Demonstrate context isolation
    print("\n=== Context Isolation ===")
    def isolated_task(task_id: str) -> None:
        token = request_id.set(f"task-{task_id}")
        try:
            import time
            time.sleep(0.1)  # Simulate work
            print(f"Task {task_id} completed with ID: {request_id.get()}")
        finally:
            request_id.reset(token)
    
    # Run tasks concurrently (simulated)
    import threading
    threads = [
        threading.Thread(target=isolated_task, args=(str(i),))
        for i in range(3)
    ]
    
    for t in threads:
        t.start()
    for t in threads:
        t.join()
