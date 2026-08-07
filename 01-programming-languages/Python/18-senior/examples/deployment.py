"""
Deployment Practices in Python
Demonstrates deployment patterns and best practices
"""

import os
import sys
from typing import Dict, Any

# ============================================
# Environment Configuration
# ============================================

class Config:
    """Environment-based configuration."""
    
    def __init__(self):
        self.debug = os.getenv("DEBUG", "false").lower() == "true"
        self.database_url = os.getenv("DATABASE_URL", "sqlite:///app.db")
        self.secret_key = os.getenv("SECRET_KEY", "")
        self.log_level = os.getenv("LOG_LEVEL", "INFO")
    
    def validate(self):
        """Validate configuration."""
        if not self.secret_key and not self.debug:
            raise ValueError("SECRET_KEY required in production")
        return True

# ============================================
# Health Check
# ============================================

class HealthCheck:
    """Application health check."""
    
    def __init__(self):
        self.checks = {}
    
    def register(self, name: str, check_func):
        """Register a health check."""
        self.checks[name] = check_func
    
    def check(self) -> Dict[str, Any]:
        """Run all health checks."""
        results = {}
        for name, func in self.checks.items():
            try:
                results[name] = {"status": "healthy", "details": func()}
            except Exception as e:
                results[name] = {"status": "unhealthy", "error": str(e)}
        
        return {
            "status": "healthy" if all(r["status"] == "healthy" for r in results.values()) else "unhealthy",
            "checks": results
        }

def check_database():
    """Check database connection."""
    return "Database connected"

def check_cache():
    """Check cache connection."""
    return "Cache connected"

# ============================================
# Graceful Shutdown
# ============================================

class GracefulShutdown:
    """Handle graceful shutdown."""
    
    def __init__(self):
        self.handlers = []
        self.should_exit = False
    
    def register(self, handler):
        """Register shutdown handler."""
        self.handlers.append(handler)
    
    def shutdown(self):
        """Execute shutdown handlers."""
        print("  Shutting down...")
        for handler in reversed(self.handlers):
            try:
                handler()
            except Exception as e:
                print(f"  Error in shutdown handler: {e}")
        print("  Shutdown complete")

def cleanup_database():
    """Cleanup database connections."""
    print("  Closing database connections")

def cleanup_cache():
    """Cleanup cache connections."""
    print("  Clearing cache")

# ============================================
# Feature Flags
# ============================================

class FeatureFlags:
    """Feature flag management."""
    
    def __init__(self):
        self.flags = {}
    
    def is_enabled(self, flag: str, default: bool = False) -> bool:
        """Check if feature is enabled."""
        return self.flags.get(flag, default)
    
    def enable(self, flag: str):
        """Enable a feature."""
        self.flags[flag] = True
    
    def disable(self, flag: str):
        """Disable a feature."""
        self.flags[flag] = False

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Configuration ===")
    config = Config()
    print(f"  Debug: {config.debug}")
    print(f"  Database: {config.database_url}")
    
    print("\n=== Health Check ===")
    health = HealthCheck()
    health.register("database", check_database)
    health.register("cache", check_cache)
    
    result = health.check()
    print(f"  Status: {result['status']}")
    
    print("\n=== Feature Flags ===")
    flags = FeatureFlags()
    flags.enable("new_ui")
    flags.enable("beta_features")
    
    print(f"  new_ui: {flags.is_enabled('new_ui')}")
    print(f"  beta_features: {flags.is_enabled('beta_features')}")
    print(f"  dark_mode: {flags.is_enabled('dark_mode')}")
    
    print("\n=== Graceful Shutdown ===")
    shutdown = GracefulShutdown()
    shutdown.register(cleanup_database)
    shutdown.register(cleanup_cache)
    shutdown.shutdown()
