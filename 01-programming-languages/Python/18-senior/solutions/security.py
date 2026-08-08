"""
Module 18 - Senior: Security Solutions
Complete solutions with explanations
"""

import hashlib
import secrets
import re
import hmac
from typing import Optional, Tuple
from datetime import datetime, timedelta
import json
import base64


# =============================================================================
# Exercise 1: Password Hashing - SOLUTION
# =============================================================================

def exercise_1_password_hashing():
    """
    Implement secure password hashing.
    """
    def hash_password(password: str) -> str:
        """Hash password with random salt using PBKDF2."""
        # Generate random salt
        salt = secrets.token_hex(16)
        
        # Hash password with salt using PBKDF2
        password_hash = hashlib.pbkdf2_hmac(
            'sha256',
            password.encode('utf-8'),
            salt.encode('utf-8'),
            100000  # iterations
        )
        
        # Return salt:hash format
        return f"{salt}:{password_hash.hex()}"
    
    def verify_password(password: str, hashed: str) -> bool:
        """Verify password against hash."""
        try:
            salt, stored_hash = hashed.split(':')
            
            # Hash the input password with the same salt
            password_hash = hashlib.pbkdf2_hmac(
                'sha256',
                password.encode('utf-8'),
                salt.encode('utf-8'),
                100000
            )
            
            # Compare hashes
            return hmac.compare_digest(password_hash.hex(), stored_hash)
        except Exception:
            return False
    
    return hash_password, verify_password


# =============================================================================
# Exercise 2: Input Validation - SOLUTION
# =============================================================================

def exercise_2_input_validation():
    """
    Implement input validation and sanitization.
    """
    def validate_email(email: str) -> bool:
        """Validate email format using regex."""
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))
    
    def sanitize_input(input_str: str) -> str:
        """Sanitize user input to prevent XSS."""
        # HTML escape characters
        replacements = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#x27;',
            '/': '&#x2F;',
        }
        
        result = input_str
        for char, replacement in replacements.items():
            result = result.replace(char, replacement)
        
        return result
    
    def safe_query(query: str, params: dict) -> Tuple[str, tuple]:
        """Create parameterized query to prevent SQL injection."""
        # Replace named parameters with placeholders
        safe_query = query
        values = []
        
        for key, value in params.items():
            safe_query = safe_query.replace(f":{key}", "%s")
            values.append(value)
        
        return safe_query, tuple(values)
    
    return validate_email, sanitize_input, safe_query


# =============================================================================
# Exercise 3: Authentication - SOLUTION
# =============================================================================

class AuthToken:
    """
    Implement JWT-like authentication token.
    """
    def __init__(self, secret_key: str):
        self.secret_key = secret_key
    
    def _sign(self, data: str) -> str:
        """Sign data with HMAC."""
        return hmac.new(
            self.secret_key.encode('utf-8'),
            data.encode('utf-8'),
            hashlib.sha256
        ).hexdigest()
    
    def create_token(self, user_id: int, expires_in: int = 3600) -> str:
        """Create signed token."""
        # Create header
        header = {"alg": "HS256", "typ": "JWT"}
        
        # Create payload
        payload = {
            "user_id": user_id,
            "exp": (datetime.utcnow() + timedelta(seconds=expires_in)).isoformat(),
            "iat": datetime.utcnow().isoformat(),
        }
        
        # Encode header and payload
        header_b64 = base64.urlsafe_b64encode(
            json.dumps(header).encode()
        ).decode().rstrip('=')
        
        payload_b64 = base64.urlsafe_b64encode(
            json.dumps(payload).encode()
        ).decode().rstrip('=')
        
        # Create signature
        message = f"{header_b64}.{payload_b64}"
        signature = self._sign(message)
        
        return f"{header_b64}.{payload_b64}.{signature}"
    
    def verify_token(self, token: str) -> Optional[dict]:
        """Verify and decode token."""
        try:
            parts = token.split('.')
            if len(parts) != 3:
                return None
            
            header_b64, payload_b64, signature = parts
            
            # Verify signature
            message = f"{header_b64}.{payload_b64}"
            expected_signature = self._sign(message)
            
            if not hmac.compare_digest(signature, expected_signature):
                return None
            
            # Decode payload
            # Add padding
            padding = 4 - len(payload_b64) % 4
            if padding != 4:
                payload_b64 += '=' * padding
            
            payload = json.loads(base64.urlsafe_b64decode(payload_b64))
            
            # Check expiration
            exp = datetime.fromisoformat(payload['exp'])
            if datetime.utcnow() > exp:
                return None
            
            return payload
        except Exception:
            return None


# =============================================================================
# Exercise 4: Rate Limiting - SOLUTION
# =============================================================================

class RateLimiter:
    """
    Implement rate limiting with sliding window.
    """
    def __init__(self, max_requests: int, window_seconds: int):
        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.requests = {}  # client_id -> list of timestamps
    
    def _cleanup_old_requests(self, client_id: str):
        """Remove old requests outside the window."""
        if client_id not in self.requests:
            return
        
        now = datetime.utcnow()
        cutoff = now - timedelta(seconds=self.window_seconds)
        
        self.requests[client_id] = [
            ts for ts in self.requests[client_id]
            if ts > cutoff
        ]
    
    def is_allowed(self, client_id: str) -> bool:
        """Check if request is allowed."""
        self._cleanup_old_requests(client_id)
        
        if client_id not in self.requests:
            return True
        
        return len(self.requests[client_id]) < self.max_requests
    
    def record_request(self, client_id: str):
        """Record a request."""
        self._cleanup_old_requests(client_id)
        
        if client_id not in self.requests:
            self.requests[client_id] = []
        
        self.requests[client_id].append(datetime.utcnow())


# =============================================================================
# Exercise 5: CSRF Protection - SOLUTION
# =============================================================================

def exercise_5_csrf_protection():
    """
    Implement CSRF protection.
    """
    def generate_csrf_token() -> str:
        """Generate random CSRF token."""
        return secrets.token_hex(32)
    
    def validate_csrf_token(token: str, stored_token: str) -> bool:
        """Validate CSRF token using constant-time comparison."""
        if not token or not stored_token:
            return False
        
        return hmac.compare_digest(token, stored_token)
    
    return generate_csrf_token, validate_csrf_token


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 18 - Security Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Password Hashing")
    hash_password, verify_password = exercise_1_password_hashing()
    
    password = "mypassword123"
    hashed = hash_password(password)
    
    assert verify_password(password, hashed)
    assert not verify_password("wrongpassword", hashed)
    assert hashed != hash_password(password)  # Different salt each time
    print(f"  Hash length: {len(hashed)}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Input Validation")
    validate_email, sanitize_input, safe_query = exercise_2_input_validation()
    
    # Email validation
    assert validate_email("user@example.com")
    assert validate_email("name.last@domain.co.uk")
    assert not validate_email("invalid")
    assert not validate_email("@domain.com")
    
    # Input sanitization
    malicious = "<script>alert('xss')</script>"
    sanitized = sanitize_input(malicious)
    assert "<script>" not in sanitized
    assert "&lt;" in sanitized
    
    # Safe query
    query = "SELECT * FROM users WHERE id = :user_id"
    safe, params = safe_query(query, {"user_id": 123})
    assert "%s" in safe
    assert params == (123,)
    
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Authentication")
    auth = AuthToken("super_secret_key")
    
    # Create token
    token = auth.create_token(user_id=123, expires_in=3600)
    assert token.count('.') == 2
    
    # Verify token
    payload = auth.verify_token(token)
    assert payload is not None
    assert payload['user_id'] == 123
    
    # Invalid token
    assert auth.verify_token("invalid.token.here") is None
    
    print(f"  Token created and verified")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Rate Limiting")
    limiter = RateLimiter(max_requests=3, window_seconds=60)
    
    # First 3 requests should be allowed
    assert limiter.is_allowed("client1")
    limiter.record_request("client1")
    
    assert limiter.is_allowed("client1")
    limiter.record_request("client1")
    
    assert limiter.is_allowed("client1")
    limiter.record_request("client1")
    
    # Fourth request should be blocked
    assert not limiter.is_allowed("client1")
    
    # Different client should be allowed
    assert limiter.is_allowed("client2")
    
    print(f"  Rate limiting works correctly")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: CSRF Protection")
    generate_csrf_token, validate_csrf_token = exercise_5_csrf_protection()
    
    token = generate_csrf_token()
    assert len(token) == 64  # 32 bytes = 64 hex chars
    
    # Valid token
    assert validate_csrf_token(token, token)
    
    # Invalid token
    assert not validate_csrf_token("wrong_token", token)
    
    # Empty token
    assert not validate_csrf_token("", token)
    assert not validate_csrf_token(token, "")
    
    print(f"  CSRF token: {token[:20]}...")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
