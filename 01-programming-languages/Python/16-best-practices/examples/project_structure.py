"""
Project Structure Best Practices
Demonstrates proper Python project organization
"""

# ============================================
# Package Structure
# ============================================

"""
my_project/
│
├── src/
│   └── my_project/
│       ├── __init__.py
│       ├── main.py
│       ├── config.py
│       ├── models/
│       │   ├── __init__.py
│       │   ├── user.py
│       │   └── product.py
│       ├── services/
│       │   ├── __init__.py
│       │   ├── auth.py
│       │   └── payment.py
│       └── utils/
│           ├── __init__.py
│           ├── helpers.py
│           └── validators.py
│
├── tests/
│   ├── __init__.py
│   ├── test_models.py
│   └── test_services.py
│
├── docs/
│   └── README.md
│
├── pyproject.toml
├── README.md
└── .gitignore
"""

# ============================================
# __init__.py Usage
# ============================================

# Package __init__.py - exports public API
"""
# my_project/__init__.py
from .main import main
from .config import Config

__version__ = "1.0.0"
__all__ = ["main", "Config"]
"""

# Module __init__.py - empty or minimal
"""
# my_project/models/__init__.py
from .user import User
from .product import Product
"""

# ============================================
# Import Patterns
# ============================================

# Absolute imports (preferred)
"""
from my_project.models.user import User
from my_project.services.auth import authenticate
"""

# Relative imports (within package)
"""
from .models.user import User
from ..services.auth import authenticate
"""

# ============================================
# Configuration Pattern
# ============================================

import os
from dataclasses import dataclass
from typing import Optional

@dataclass
class Config:
    """Application configuration."""
    database_url: str
    api_key: str
    debug: bool = False
    log_level: str = "INFO"
    
    @classmethod
    def from_env(cls) -> 'Config':
        """Create config from environment variables."""
        return cls(
            database_url=os.getenv("DATABASE_URL", "sqlite:///app.db"),
            api_key=os.getenv("API_KEY", ""),
            debug=os.getenv("DEBUG", "false").lower() == "true",
            log_level=os.getenv("LOG_LEVEL", "INFO")
        )

# ============================================
# Module Pattern
# ============================================

# good_module.py - Public API
"""
__all__ = ["public_function", "PublicClass"]

def public_function():
    pass

def _private_function():
    pass

class PublicClass:
    pass
"""

# ============================================
# Testing Structure
# ============================================

# tests/test_models.py
"""
import pytest
from my_project.models.user import User

class TestUser:
    def test_create_user(self):
        user = User(name="Alice")
        assert user.name == "Alice"
    
    def test_user_email(self):
        user = User(name="Alice", email="alice@example.com")
        assert user.email == "alice@example.com"
"""

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Example usage
    config = Config.from_env()
    print(f"Database URL: {config.database_url}")
    print(f"Debug mode: {config.debug}")
