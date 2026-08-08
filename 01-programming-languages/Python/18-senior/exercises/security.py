"""
Module 18 - Senior: Security Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Application security best practices
"""

import hashlib
import secrets
import re
from typing import Optional


# =============================================================================
# Exercise 1: Password Hashing (⭐⭐⭐⭐)
# =============================================================================

def exercise_1_password_hashing():
    """
    Implement secure password hashing.
    
    TODO:
    1. Hash password with salt
    2. Verify password
    3. Use secure algorithms
    """
    def hash_password(password: str) -> str:
        # TODO: Hash password with salt
        pass
    
    def verify_password(password: str, hashed: str) -> bool:
        # TODO: Verify password against hash
        pass
    
    return hash_password, verify_password


# =============================================================================
# Exercise 2: Input Validation (⭐⭐⭐⭐)
# =============================================================================

def exercise_2_input_validation():
    """
    Implement input validation and sanitization.
    
    TODO:
    1. Validate email format
    2. Sanitize user input
    3. Prevent SQL injection
    """
    def validate_email(email: str) -> bool:
        # TODO: Validate email format
        pass
    
    def sanitize_input(input_str: str) -> str:
        # TODO: Sanitize input
        pass
    
    def safe_query(query: str, params: dict) -> str:
        # TODO: Create parameterized query
        pass
    
    return validate_email, sanitize_input, safe_query


# =============================================================================
# Exercise 3: Authentication (⭐⭐⭐⭐⭐)
# =============================================================================

class AuthToken:
    """
    Implement JWT-like authentication token.
    
    TODO:
    1. Create token with payload
    2. Sign token
    3. Verify and decode token
    """
    def __init__(self, secret_key):
        self.secret_key = secret_key
    
    def create_token(self, user_id, expires_in=3600):
        # TODO: Create signed token
        pass
    
    def verify_token(self, token):
        # TODO: Verify and decode token
        pass


# =============================================================================
# Exercise 4: Rate Limiting (⭐⭐⭐⭐)
# =============================================================================

class RateLimiter:
    """
    Implement rate limiting.
    
    TODO:
    1. Track request counts
    2. Implement sliding window
    3. Block excessive requests
    """
    def __init__(self, max_requests, window_seconds):
        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.requests = {}
    
    def is_allowed(self, client_id):
        # TODO: Check if request is allowed
        pass
    
    def record_request(self, client_id):
        # TODO: Record request
        pass


# =============================================================================
# Exercise 5: CSRF Protection (⭐⭐⭐⭐)
# =============================================================================

def exercise_5_csrf_protection():
    """
    Implement CSRF protection.
    
    TODO:
    1. Generate CSRF token
    2. Validate CSRF token
    3. Implement double-submit pattern
    """
    def generate_csrf_token():
        # TODO: Generate random token
        pass
    
    def validate_csrf_token(token, stored_token):
        # TODO: Validate token
        pass
    
    return generate_csrf_token, validate_csrf_token


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 18 - Security Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Password Hashing")
    try:
        hash_password, verify_password = exercise_1_password_hashing()
        hashed = hash_password("mypassword")
        assert verify_password("mypassword", hashed)
        assert not verify_password("wrongpassword", hashed)
        print(f"  Hash length: {len(hashed)}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Input Validation")
    try:
        validate_email, sanitize_input, safe_query = exercise_2_input_validation()
        assert validate_email("test@example.com")
        assert not validate_email("invalid")
        sanitized = sanitize_input("<script>alert('xss')</script>")
        assert "<script>" not in sanitized
        print(f"  Sanitized: {sanitized}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Authentication")
    try:
        auth = AuthToken("secret_key")
        token = auth.create_token(123)
        payload = auth.verify_token(token)
        print(f"  Token payload: {payload}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Rate Limiting")
    try:
        limiter = RateLimiter(max_requests=5, window_seconds=60)
        for i in range(5):
            limiter.record_request("client1")
        assert not limiter.is_allowed("client1")
        print(f"  Rate limited: {not limiter.is_allowed('client1')}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: CSRF Protection")
    try:
        generate_csrf_token, validate_csrf_token = exercise_5_csrf_protection()
        token = generate_csrf_token()
        assert validate_csrf_token(token, token)
        assert not validate_csrf_token("wrong", token)
        print(f"  Token: {token[:20]}...")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
