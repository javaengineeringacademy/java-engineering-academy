# Security Interview Questions

Comprehensive guide to security interview questions and answers.

## OWASP Top 10 Questions

### 1. What is SQL Injection and how do you prevent it?

**Answer:** SQL Injection is an attack where malicious SQL code is inserted into application queries, allowing attackers to access, modify, or delete data.

**Prevention Methods:**

1. **Parameterized Queries:**
```python
# Vulnerable
query = f"SELECT * FROM users WHERE id = {user_id}"

# Secure
query = "SELECT * FROM users WHERE id = %s"
cursor.execute(query, (user_id,))
```

2. **ORM Usage:**
```python
# Django ORM
users = User.objects.filter(id=user_id)
```

3. **Input Validation:**
```python
def validate_user_id(user_id):
    if not user_id.isdigit():
        raise ValueError("Invalid user ID")
    return int(user_id)
```

4. **Stored Procedures:**
```sql
CREATE PROCEDURE GetUser(IN userId INT)
BEGIN
    SELECT * FROM users WHERE id = userId;
END
```

5. **Least Privilege:**
```sql
GRANT SELECT ON mydb.users TO 'app_user'@'localhost';
```

### 2. What is Cross-Site Scripting (XSS)?

**Answer:** XSS attacks inject malicious scripts into web pages viewed by other users.

**Types:**
- **Stored XSS**: Malicious script stored on server
- **Reflected XSS**: Script reflected in URL/response
- **DOM-based XSS**: Script executed in DOM

**Prevention:**

1. **Output Encoding:**
```python
from markupsafe import escape

# Encode user input before rendering
user_input = "<script>alert('XSS')</script>"
safe_output = escape(user_input)
```

2. **Content Security Policy:**
```html
<meta http-equiv="Content-Security-Policy" 
      content="default-src 'self'; script-src 'self'">
```

3. **Input Validation:**
```python
import re

def sanitize_input(input_string):
    # Remove HTML tags
    clean = re.sub('<[^<]+?>', '', input_string)
    return clean
```

### 3. What is Cross-Site Request Forgery (CSRF)?

**Answer:** CSRF attacks trick users into performing unwanted actions on applications where they're authenticated.

**Prevention:**

1. **CSRF Tokens:**
```python
from flask_wtf.csrf import CSRFProtect

csrf = CSRFProtect(app)

@app.route('/transfer', methods=['POST'])
@csrf.exempt
def transfer():
    # CSRF token automatically validated
    pass
```

2. **Same-Site Cookies:**
```python
response.set_cookie('session', value, 
                    samesite='Strict',
                    secure=True,
                    httponly=True)
```

3. **Custom Headers:**
```javascript
fetch('/api/transfer', {
    method: 'POST',
    headers: {
        'X-CSRF-Token': csrfToken,
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({amount: 100})
});
```

## Authentication Questions

### 4. How do you implement secure authentication?

**Answer:** I implement multiple layers of security:

1. **Password Hashing:**
```python
import bcrypt

def hash_password(password):
    salt = bcrypt.gensalt(rounds=12)
    return bcrypt.hashpw(password.encode(), salt)

def verify_password(password, hashed):
    return bcrypt.checkpw(password.encode(), hashed)
```

2. **Multi-Factor Authentication:**
```python
import pyotp

# Generate TOTP secret
secret = pyotp.random_base32()
totp = pyotp.TOTP(secret)

# Verify code
is_valid = totp.verify(user_provided_code)
```

3. **JWT Tokens:**
```python
import jwt
from datetime import datetime, timedelta

def create_tokens(user_id):
    access_token = jwt.encode({
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(minutes=15)
    }, SECRET_KEY, algorithm='HS256')
    
    refresh_token = jwt.encode({
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(days=7)
    }, SECRET_KEY, algorithm='HS256')
    
    return access_token, refresh_token
```

### 5. How do you handle password storage?

**Answer:** I use industry best practices:

1. **Use Strong Hashing Algorithms:**
```python
# bcrypt (recommended)
import bcrypt
hashed = bcrypt.hashpw(password.encode(), bcrypt.gensalt(rounds=12))

# Argon2 (memory-hard)
from argon2 import PasswordHasher
ph = PasswordHasher()
hashed = ph.hash(password)
```

2. **Never Store Plaintext Passwords**
3. **Use Salted Hashes**
4. **Implement Password Policies:**
```python
def validate_password(password):
    if len(password) < 12:
        return False
    if not re.search(r'[A-Z]', password):
        return False
    if not re.search(r'[a-z]', password):
        return False
    if not re.search(r'\d', password):
        return False
    if not re.search(r'[!@#$%^&*(),.?":{}|<>]', password):
        return False
    return True
```

### 6. How do you implement OAuth 2.0?

**Answer:** I implement OAuth 2.0 with proper security:

```python
from authlib.integrations.flask_client import OAuth

oauth = OAuth(app)

# Register OAuth provider
oauth.register(
    name='github',
    client_id='your_client_id',
    client_secret='your_client_secret',
    access_token_url='https://github.com/login/oauth/access_token',
    authorize_url='https://github.com/login/oauth/authorize',
    api_base_url='https://api.github.com/',
    client_kwargs={'scope': 'user:email'}
)

@app.route('/login/github')
def github_login():
    redirect_uri = url_for('github_callback', _external=True)
    return oauth.github.authorize_redirect(redirect_uri)

@app.route('/callback/github')
def github_callback():
    token = oauth.github.authorize_access_token()
    user_info = oauth.github.get('user').json()
    # Process user login
```

## Cryptography Questions

### 7. What is the difference between symmetric and asymmetric encryption?

**Answer:**

| Aspect | Symmetric | Asymmetric |
|--------|-----------|------------|
| Keys | Single shared key | Public/Private key pair |
| Speed | Fast | Slow |
| Use Case | Data encryption | Key exchange, signatures |
| Examples | AES, DES | RSA, ECC |

**Symmetric Encryption (AES):**
```python
from cryptography.fernet import Fernet

# Generate key
key = Fernet.generate_key()
cipher = Fernet(key)

# Encrypt
encrypted = cipher.encrypt(b"secret message")

# Decrypt
decrypted = cipher.decrypt(encrypted)
```

**Asymmetric Encryption (RSA):**
```python
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.primitives import hashes

# Generate key pair
private_key = rsa.generate_private_key(
    public_exponent=65537,
    key_size=2048
)
public_key = private_key.public_key()

# Encrypt with public key
encrypted = public_key.encrypt(
    b"secret message",
    padding.OAEP(
        mgf=padding.MGF1(algorithm=hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None
    )
)

# Decrypt with private key
decrypted = private_key.decrypt(
    encrypted,
    padding.OAEP(
        mgf=padding.MGF1(algorithm=hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None
    )
)
```

### 8. How do you secure API keys?

**Answer:** I use multiple strategies:

1. **Environment Variables:**
```python
import os

api_key = os.environ.get('API_KEY')
```

2. **Secrets Management:**
```python
import hvac

client = hvac.Client(url='https://vault.example.com')
secret = client.secrets.kv.v2.read_secret_version(path='api-keys')
api_key = secret['data']['data']['github']
```

3. **Key Rotation:**
```python
def rotate_api_key(old_key):
    # Generate new key
    new_key = generate_new_key()
    
    # Update in secrets manager
    update_secret('api-keys', {'github': new_key})
    
    # Verify new key works
    if verify_api_key(new_key):
        # Invalidate old key
        revoke_api_key(old_key)
        return new_key
    else:
        raise Exception("New key verification failed")
```

4. **Access Control:**
```yaml
# AWS IAM Policy
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "secretsmanager:GetSecretValue",
            "Resource": "arn:aws:secretsmanager:region:account:secret:api-keys-*"
        }
    ]
}
```

## Network Security Questions

### 9. How do you implement TLS/SSL?

**Answer:** I implement TLS with best practices:

1. **Certificate Configuration:**
```nginx
server {
    listen 443 ssl http2;
    server_name example.com;

    ssl_certificate /path/to/certificate.pem;
    ssl_certificate_key /path/to/private.key;
    
    # Strong SSL Configuration
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers 'ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384';
    ssl_prefer_server_ciphers off;
    
    # HSTS Header
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
}
```

2. **Certificate Management:**
```bash
# Let's Encrypt with Certbot
certbot certonly --webroot -w /var/www/html -d example.com

# Auto-renewal
0 0 1 * * certbot renew --quiet
```

### 10. How do you secure microservices communication?

**Answer:** I implement defense in depth:

1. **Mutual TLS (mTLS):**
```yaml
# Istio PeerAuthentication
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
spec:
  mtls:
    mode: STRICT
```

2. **Service Authorization:**
```yaml
# Istio AuthorizationPolicy
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: my-service
spec:
  selector:
    matchLabels:
      app: my-service
  rules:
  - from:
    - source:
        principals: ["cluster.local/ns/default/sa/client-service"]
    to:
    - operation:
        methods: ["GET", "POST"]
```

3. **Network Policies:**
```yaml
# Kubernetes NetworkPolicy
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: my-service
spec:
  podSelector:
    matchLabels:
      app: my-service
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          app: client
    ports:
    - protocol: TCP
      port: 8080
```

## Application Security Questions

### 11. How do you prevent XSS attacks?

**Answer:** I use multiple prevention methods:

1. **Output Encoding:**
```python
from markupsafe import escape

def render_user_input(user_input):
    return escape(user_input)
```

2. **Content Security Policy:**
```html
<meta http-equiv="Content-Security-Policy" 
      content="default-src 'self'; script-src 'self' 'unsafe-inline'">
```

3. **Input Validation:**
```python
import re

def sanitize_html(input_string):
    # Remove HTML tags
    clean = re.sub('<[^<]+?>', '', input_string)
    # Remove javascript: protocol
    clean = re.sub('javascript:', '', clean, flags=re.IGNORECASE)
    return clean
```

4. **HTTPOnly Cookies:**
```python
response.set_cookie('session', value,
                    httponly=True,
                    secure=True,
                    samesite='Strict')
```

### 12. How do you implement rate limiting?

**Answer:** I implement multiple rate limiting strategies:

1. **Token Bucket Algorithm:**
```python
import time

class TokenBucket:
    def __init__(self, capacity, refill_rate):
        self.capacity = capacity
        self.tokens = capacity
        self.refill_rate = refill_rate
        self.last_refill = time.time()
    
    def consume(self):
        self.refill()
        if self.tokens > 0:
            self.tokens -= 1
            return True
        return False
    
    def refill(self):
        now = time.time()
        elapsed = now - self.last_refill
        self.tokens = min(self.capacity, 
                         self.tokens + elapsed * self.refill_rate)
        self.last_refill = now
```

2. **Redis-based Rate Limiting:**
```python
import redis
import time

def is_rate_limited(user_id, limit=100, window=60):
    r = redis.Redis()
    key = f"rate_limit:{user_id}"
    
    current = r.get(key)
    if current and int(current) >= limit:
        return True
    
    pipe = r.pipeline()
    pipe.incr(key)
    pipe.expire(key, window)
    pipe.execute()
    
    return False
```

## Incident Response Questions

### 13. How do you handle a security breach?

**Answer:** I follow a structured incident response process:

1. **Detection and Analysis:**
```python
def detect_breach(log_entry):
    # Analyze log for suspicious activity
    if is_suspicious(log_entry):
        # Alert security team
        send_alert(log_entry)
        # Preserve evidence
        save_log(log_entry)
        # Begin investigation
        start_investigation(log_entry)
```

2. **Containment:**
- Isolate affected systems
- Block malicious IPs
- Disable compromised accounts
- Preserve evidence

3. **Eradication:**
- Remove malware
- Patch vulnerabilities
- Reset credentials
- Update security controls

4. **Recovery:**
- Restore from backups
- Verify system integrity
- Monitor for recurrence
- Communicate with stakeholders

5. **Post-Incident:**
- Conduct root cause analysis
- Document lessons learned
- Update security policies
- Implement preventive measures

### 14. How do you perform security testing?

**Answer:** I use multiple testing approaches:

1. **Static Application Security Testing (SAST):**
```python
# Example using Bandit for Python
# bandit -r src/

# Example using SonarQube
# sonar-scanner -Dsonar.projectKey=myproject
```

2. **Dynamic Application Security Testing (DAST):**
```bash
# Using OWASP ZAP
zap-baseline.py -t https://example.com

# Using Nikto
nikto -h https://example.com
```

3. **Dependency Scanning:**
```bash
# Using Snyk
snyk test

# Using Safety
safety check
```

4. **Penetration Testing:**
- Vulnerability scanning
- Exploitation testing
- Social engineering tests
- Physical security tests

## Best Practices Summary

### 1. Secure Development
- Security requirements
- Threat modeling
- Secure coding guidelines
- Code review for security

### 2. Authentication
- Strong passwords
- Multi-factor authentication
- Session management
- Account lockout

### 3. Authorization
- Least privilege
- Role-based access control
- Regular access reviews
- Audit logging

### 4. Data Protection
- Encryption at rest
- Encryption in transit
- Key management
- Data classification

### 5. Monitoring
- Security event logging
- Anomaly detection
- Incident response
- Regular audits