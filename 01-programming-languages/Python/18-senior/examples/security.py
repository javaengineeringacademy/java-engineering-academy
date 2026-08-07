"""
Security Best Practices in Python
Demonstrates security patterns and techniques
"""

import hashlib
import hmac
import secrets
import re
from typing import Optional
from dataclasses import dataclass
import html

# ============================================
# Password Hashing
# ============================================

def hash_password(password: str) -> str:
    """Hash password with salt using PBKDF2."""
    salt = secrets.token_hex(16)
    key = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode(),
        salt.encode(),
        100000
    )
    return f"{salt}${key.hex()}"

def verify_password(password: str, stored_hash: str) -> bool:
    """Verify password against stored hash."""
    salt, key_hex = stored_hash.split('$')
    key = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode(),
        salt.encode(),
        100000
    )
    return hmac.compare_digest(key.hex(), key_hex)

# ============================================
# Input Validation
# ============================================

class InputValidator:
    """Input validation utilities."""
    
    @staticmethod
    def validate_email(email: str) -> bool:
        """Validate email format."""
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))
    
    @staticmethod
    def validate_username(username: str) -> bool:
        """Validate username format."""
        pattern = r'^[a-zA-Z0-9_]{3,20}$'
        return bool(re.match(pattern, username))
    
    @staticmethod
    def sanitize_string(input_str: str) -> str:
        """Sanitize string input."""
        return html.escape(input_str.strip())

# ============================================
# Token Generation
# ============================================

class TokenGenerator:
    """Secure token generation."""
    
    @staticmethod
    def generate_api_key() -> str:
        """Generate secure API key."""
        return secrets.token_urlsafe(32)
    
    @staticmethod
    def generate_session_id() -> str:
        """Generate secure session ID."""
        return secrets.token_hex(32)
    
    @staticmethod
    def generate_otp(length: int = 6) -> str:
        """Generate one-time password."""
        return ''.join(secrets.choice('0123456789') for _ in range(length))

# ============================================
# SQL Injection Prevention
# ============================================

class SQLSanitizer:
    """SQL injection prevention."""
    
    @staticmethod
    def sanitize_identifier(identifier: str) -> str:
        """Sanitize SQL identifier."""
        if not re.match(r'^[a-zA-Z_][a-zA-Z0-9_]*$', identifier):
            raise ValueError(f"Invalid identifier: {identifier}")
        return identifier
    
    @staticmethod
    def use_parameterized_query():
        """Example of parameterized query."""
        # Good: Parameterized query
        query = "SELECT * FROM users WHERE id = %s"
        params = (123,)
        
        # Bad: String formatting (vulnerable!)
        # query = f"SELECT * FROM users WHERE id = {user_id}"
        
        return query, params

# ============================================
# Rate Limiting
# ============================================

class RateLimiter:
    """Simple rate limiter."""
    
    def __init__(self, max_requests: int, window_seconds: int):
        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.requests = {}
    
    def is_allowed(self, client_id: str) -> bool:
        """Check if request is allowed."""
        import time
        
        now = time.time()
        window_start = now - self.window_seconds
        
        # Clean old requests
        if client_id in self.requests:
            self.requests[client_id] = [
                req_time for req_time in self.requests[client_id]
                if req_time > window_start
            ]
        else:
            self.requests[client_id] = []
        
        # Check limit
        if len(self.requests[client_id]) >= self.max_requests:
            return False
        
        # Add request
        self.requests[client_id].append(now)
        return True

# ============================================
# Secure Headers
# ============================================

class SecureHeaders:
    """Security headers for HTTP responses."""
    
    @staticmethod
    def get_headers() -> dict:
        """Get security headers."""
        return {
            "X-Content-Type-Options": "nosniff",
            "X-Frame-Options": "DENY",
            "X-XSS-Protection": "1; mode=block",
            "Strict-Transport-Security": "max-age=31536000; includeSubDomains",
            "Content-Security-Policy": "default-src 'self'",
            "Referrer-Policy": "strict-origin-when-cross-origin"
        }

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Password Hashing ===")
    password = "secure_password_123"
    hashed = hash_password(password)
    print(f"  Hashed: {hashed[:50]}...")
    print(f"  Verified: {verify_password(password, hashed)}")
    print(f"  Wrong password: {verify_password('wrong', hashed)}")
    
    print("\n=== Input Validation ===")
    validator = InputValidator()
    print(f"  Valid email: {validator.validate_email('user@example.com')}")
    print(f"  Invalid email: {validator.validate_email('invalid')}")
    print(f"  Valid username: {validator.validate_username('john_doe')}")
    print(f"  Sanitized: {validator.sanitize_string('<script>alert(1)</script>')}")
    
    print("\n=== Token Generation ===")
    print(f"  API Key: {TokenGenerator.generate_api_key()[:20]}...")
    print(f"  Session: {TokenGenerator.generate_session_id()[:20]}...")
    print(f"  OTP: {TokenGenerator.generate_otp()}")
    
    print("\n=== Rate Limiting ===")
    limiter = RateLimiter(max_requests=3, window_seconds=1)
    for i in range(5):
        allowed = limiter.is_allowed("client1")
        print(f"  Request {i+1}: {'Allowed' if allowed else 'Denied'}")
    
    print("\n=== Security Headers ===")
    headers = SecureHeaders.get_headers()
    for key, value in headers.items():
        print(f"  {key}: {value}")
