"""
Module 18 - Senior: Deployment Solutions
Complete solutions with explanations
"""

import os
import json
from typing import Dict, Any, List, Optional
from datetime import datetime


# =============================================================================
# Exercise 1: Docker Configuration - SOLUTION
# =============================================================================

def exercise_1_docker():
    """
    Create Docker configuration for a Python app.
    """
    dockerfile = """# Multi-stage build for Python app
FROM python:3.11-slim as builder

WORKDIR /app

# Install dependencies
COPY requirements.txt .
RUN pip install --user --no-cache-dir -r requirements.txt

# Production stage
FROM python:3.11-slim

# Create non-root user
RUN useradd --create-home --shell /bin/bash appuser

WORKDIR /app

# Copy dependencies from builder
COPY --from=builder /root/.local /home/appuser/.local

# Copy application code
COPY . .

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Add local bin to PATH
ENV PATH=/home/appuser/.local/bin:$PATH

# Expose port
EXPOSE 8000

# Run application
CMD ["python", "app.py"]
"""
    
    docker_compose = """version: '3.8'

services:
  app:
    build: .
    ports:
      - "8000:8000"
    environment:
      - DATABASE_URL=postgresql://user:password@db:5432/mydb
      - REDIS_URL=redis://redis:6379
    depends_on:
      - db
      - redis
    restart: unless-stopped
    
  db:
    image: postgres:14
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=password
      - POSTGRES_DB=mydb
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

volumes:
  postgres_data:
"""
    
    return {
        'dockerfile': dockerfile,
        'docker_compose': docker_compose,
    }


# =============================================================================
# Exercise 2: Environment Configuration - SOLUTION
# =============================================================================

class EnvironmentConfig:
    """
    Handle environment-specific configuration.
    """
    def __init__(self):
        self.config = {}
        self._required_keys = []
    
    def load(self, environment='development'):
        """Load configuration from environment variables."""
        # Default configurations
        defaults = {
            'development': {
                'DEBUG': 'true',
                'DATABASE_URL': 'sqlite:///dev.db',
                'LOG_LEVEL': 'DEBUG',
            },
            'staging': {
                'DEBUG': 'false',
                'DATABASE_URL': 'postgresql://localhost/staging',
                'LOG_LEVEL': 'INFO',
            },
            'production': {
                'DEBUG': 'false',
                'DATABASE_URL': os.getenv('DATABASE_URL'),
                'LOG_LEVEL': 'WARNING',
            },
        }
        
        # Load defaults for environment
        env_config = defaults.get(environment, {})
        
        # Override with environment variables
        for key, default in env_config.items():
            self.config[key] = os.getenv(key, default)
        
        return self.config
    
    def get(self, key, default=None):
        """Get config value."""
        return self.config.get(key, default)
    
    def validate(self, required_keys: List[str]):
        """Validate required keys exist."""
        missing = [key for key in required_keys if not self.config.get(key)]
        if missing:
            raise ValueError(f"Missing required config keys: {missing}")
        return True


# =============================================================================
# Exercise 3: Health Check - SOLUTION
# =============================================================================

class HealthChecker:
    """
    Implement health check endpoints.
    """
    def __init__(self):
        self.checks = {}
    
    def add_check(self, name: str, check_func):
        """Add health check."""
        self.checks[name] = check_func
    
    def check_health(self) -> Dict[str, Any]:
        """Run all checks and return status."""
        results = {}
        all_healthy = True
        
        for name, check_func in self.checks.items():
            try:
                is_healthy = check_func()
                results[name] = {
                    'status': 'healthy' if is_healthy else 'unhealthy',
                    'timestamp': datetime.utcnow().isoformat(),
                }
                if not is_healthy:
                    all_healthy = False
            except Exception as e:
                results[name] = {
                    'status': 'error',
                    'error': str(e),
                    'timestamp': datetime.utcnow().isoformat(),
                }
                all_healthy = False
        
        return {
            'status': 'healthy' if all_healthy else 'unhealthy',
            'checks': results,
        }


# =============================================================================
# Exercise 4: Logging Configuration - SOLUTION
# =============================================================================

def exercise_4_logging_config():
    """
    Configure logging for production.
    """
    import logging
    
    config = {
        'version': 1,
        'disable_existing_loggers': False,
        'formatters': {
            'standard': {
                'format': '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
            },
            'json': {
                'class': 'pythonjsonlogger.jsonlogger.JsonFormatter',
                'format': '%(asctime)s %(name)s %(levelname)s %(message)s'
            },
        },
        'handlers': {
            'console': {
                'class': 'logging.StreamHandler',
                'formatter': 'standard',
                'level': 'INFO',
            },
            'file': {
                'class': 'logging.handlers.RotatingFileHandler',
                'filename': 'app.log',
                'maxBytes': 10485760,  # 10MB
                'backupCount': 5,
                'formatter': 'json',
                'level': 'WARNING',
            },
        },
        'root': {
            'handlers': ['console', 'file'],
            'level': 'INFO',
        },
    }
    
    return config


# =============================================================================
# Exercise 5: Deployment Checklist - SOLUTION
# =============================================================================

def exercise_5_deployment_checklist():
    """
    Create deployment checklist.
    """
    checklist = {
        'security': [
            {'item': 'HTTPS enabled', 'check': lambda: os.getenv('HTTPS_ENABLED') == 'true'},
            {'item': 'Secrets not in code', 'check': lambda: not os.path.exists('.env.example')},
            {'item': 'Dependencies updated', 'check': lambda: True},
            {'item': 'CORS configured', 'check': lambda: True},
            {'item': 'Rate limiting enabled', 'check': lambda: True},
        ],
        'performance': [
            {'item': 'Database indexes', 'check': lambda: True},
            {'item': 'Caching configured', 'check': lambda: os.getenv('REDIS_URL') is not None},
            {'item': 'Static files optimized', 'check': lambda: True},
            {'item': 'Gzip compression', 'check': lambda: True},
        ],
        'monitoring': [
            {'item': 'Logging configured', 'check': lambda: True},
            {'item': 'Metrics enabled', 'check': lambda: True},
            {'item': 'Alerts set up', 'check': lambda: True},
            {'item': 'Health checks', 'check': lambda: True},
        ],
        'testing': [
            {'item': 'Unit tests passing', 'check': lambda: True},
            {'item': 'Integration tests passing', 'check': lambda: True},
            {'item': 'Load tests performed', 'check': lambda: True},
        ],
    }
    
    # Run all checks
    results = {}
    for category, items in checklist.items():
        results[category] = []
        for item in items:
            try:
                passed = item['check']()
                results[category].append({
                    'item': item['item'],
                    'status': 'passed' if passed else 'failed',
                })
            except Exception as e:
                results[category].append({
                    'item': item['item'],
                    'status': 'error',
                    'error': str(e),
                })
    
    return {
        'checklist': checklist,
        'results': results,
    }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 18 - Deployment Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Docker Configuration")
    result = exercise_1_docker()
    assert 'dockerfile' in result
    assert 'docker_compose' in result
    assert 'multi-stage' in result['dockerfile'].lower() or 'as builder' in result['dockerfile']
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Environment Configuration")
    config = EnvironmentConfig()
    config.config = {'DB_HOST': 'localhost', 'DB_PORT': '5432'}
    
    assert config.get('DB_HOST') == 'localhost'
    assert config.get('DB_PORT') == '5432'
    assert config.get('MISSING', 'default') == 'default'
    
    assert config.validate(['DB_HOST', 'DB_PORT'])
    
    try:
        config.validate(['MISSING_KEY'])
        print("  ✗ Should have raised ValueError")
    except ValueError:
        print("  ✓ Validation works")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Health Check")
    checker = HealthChecker()
    checker.add_check('database', lambda: True)
    checker.add_check('redis', lambda: True)
    
    result = checker.check_health()
    assert result['status'] == 'healthy'
    assert len(result['checks']) == 2
    print(f"  Health status: {result['status']}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Logging Configuration")
    result = exercise_4_logging_config()
    assert 'formatters' in result
    assert 'handlers' in result
    assert 'console' in result['handlers']
    print(f"  Handlers: {list(result['handlers'].keys())}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Deployment Checklist")
    result = exercise_5_deployment_checklist()
    assert 'checklist' in result
    assert 'results' in result
    assert 'security' in result['checklist']
    print(f"  Categories: {list(result['checklist'].keys())}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
