"""
Module 18 - Senior: Deployment Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Deployment and DevOps best practices
"""

import os
import json
from typing import Dict, Any


# =============================================================================
# Exercise 1: Docker Configuration (⭐⭐⭐⭐)
# =============================================================================

def exercise_1_docker():
    """
    Create Docker configuration for a Python app.
    
    TODO:
    1. Create Dockerfile
    2. Create docker-compose.yml
    3. Follow best practices (multi-stage build, non-root user)
    """
    dockerfile = """
    # TODO: Create Dockerfile content
    """
    
    docker_compose = """
    # TODO: Create docker-compose.yml content
    """
    
    return {
        'dockerfile': dockerfile,
        'docker_compose': docker_compose,
    }


# =============================================================================
# Exercise 2: Environment Configuration (⭐⭐⭐⭐)
# =============================================================================

class EnvironmentConfig:
    """
    Handle environment-specific configuration.
    
    TODO:
    1. Load config from environment variables
    2. Support multiple environments
    3. Validate required variables
    """
    def __init__(self):
        self.config = {}
    
    def load(self, environment='development'):
        # TODO: Load configuration
        pass
    
    def get(self, key, default=None):
        # TODO: Get config value
        pass
    
    def validate(self, required_keys):
        # TODO: Validate required keys exist
        pass


# =============================================================================
# Exercise 3: Health Check (⭐⭐⭐⭐)
# =============================================================================

class HealthChecker:
    """
    Implement health check endpoints.
    
    TODO:
    1. Check database connectivity
    2. Check external services
    3. Return health status
    """
    def __init__(self):
        self.checks = []
    
    def add_check(self, name, check_func):
        # TODO: Add health check
        pass
    
    def check_health(self):
        # TODO: Run all checks and return status
        pass


# =============================================================================
# Exercise 4: Logging Configuration (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_logging_config():
    """
    Configure logging for production.
    
    TODO:
    1. Set up structured logging
    2. Configure log levels
    3. Add log rotation
    """
    import logging
    
    # TODO: Configure production logging
    config = {}
    
    return config


# =============================================================================
# Exercise 5: Deployment Checklist (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_deployment_checklist():
    """
    Create deployment checklist.
    
    TODO:
    1. Security checks
    2. Performance checks
    3. Monitoring setup
    """
    checklist = {
        'security': [
            'HTTPS enabled',
            'Secrets not in code',
            'Dependencies updated',
        ],
        'performance': [
            'Database indexes',
            'Caching configured',
            'Static files optimized',
        ],
        'monitoring': [
            'Logging configured',
            'Metrics enabled',
            'Alerts set up',
        ],
    }
    
    # TODO: Implement checklist validation
    return checklist


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 18 - Deployment Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Docker Configuration")
    try:
        result = exercise_1_docker()
        assert isinstance(result, dict)
        print(f"  Dockerfile length: {len(result['dockerfile'])}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Environment Configuration")
    try:
        config = EnvironmentConfig()
        config.config = {'DB_HOST': 'localhost'}
        result = config.get('DB_HOST')
        print(f"  DB_HOST: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Health Check")
    try:
        checker = HealthChecker()
        checker.add_check('test', lambda: True)
        result = checker.check_health()
        print(f"  Health: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Logging Configuration")
    try:
        result = exercise_4_logging_config()
        assert isinstance(result, dict)
        print(f"  Config: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Deployment Checklist")
    try:
        result = exercise_5_deployment_checklist()
        assert 'security' in result
        assert 'performance' in result
        print(f"  Categories: {list(result.keys())}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
