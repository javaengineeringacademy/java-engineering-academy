# Security Interview Guide

Master security interviews with comprehensive coverage of application security, threat modeling, and security practices.

## Overview

Security interviews test your knowledge of secure coding practices, threat analysis, and security architecture.

## Key Topics

### 1. OWASP Top 10

**Injection Attacks:**
- SQL injection
- NoSQL injection
- Command injection
- LDAP injection

**Prevention:**
- Parameterized queries
- Input validation
- Stored procedures
- ORM usage

```sql
-- Bad: SQL Injection vulnerable
query = "SELECT * FROM users WHERE id = " + userId

-- Good: Parameterized query
query = "SELECT * FROM users WHERE id = ?"
stmt = connection.prepareStatement(query)
stmt.setString(1, userId)
```

**Broken Authentication:**
- Weak passwords
- Session fixation
- Credential stuffing
- Brute force attacks

**Prevention:**
- Multi-factor authentication
- Strong password policies
- Account lockout
- Session management

**Sensitive Data Exposure:**
- Unencrypted data
- Weak encryption
- Hardcoded secrets
- Improper storage

**Prevention:**
- Encryption at rest and in transit
- Key management
- Data classification
- Secure storage

### 2. Authentication and Authorization

**Authentication Methods:**
- Password-based
- Multi-factor authentication (MFA)
- OAuth 2.0
- OpenID Connect
- SAML

**Authorization Models:**
- Role-Based Access Control (RBAC)
- Attribute-Based Access Control (ABAC)
- Access Control Lists (ACL)
- OAuth scopes

**Example RBAC Implementation:**
```python
class Permission:
    def __init__(self, resource, action):
        self.resource = resource
        self.action = action

class Role:
    def __init__(self, name, permissions):
        self.name = name
        self.permissions = permissions

class User:
    def __init__(self, roles):
        self.roles = roles

    def has_permission(self, resource, action):
        for role in self.roles:
            for permission in role.permissions:
                if permission.resource == resource and permission.action == action:
                    return True
        return False
```

### 3. Cryptography

**Symmetric Encryption:**
- AES (Advanced Encryption Standard)
- DES/3DES (deprecated)
- Use cases: Data encryption at rest

**Asymmetric Encryption:**
- RSA
- ECC (Elliptic Curve Cryptography)
- Use cases: Key exchange, digital signatures

**Hashing:**
- SHA-256, SHA-3
- bcrypt, scrypt, Argon2
- Use cases: Password storage, data integrity

**Example Password Hashing:**
```python
import bcrypt

def hash_password(password):
    salt = bcrypt.gensalt()
    hashed = bcrypt.hashpw(password.encode('utf-8'), salt)
    return hashed

def verify_password(password, hashed):
    return bcrypt.checkpw(password.encode('utf-8'), hashed)
```

### 4. Web Application Security

**Cross-Site Scripting (XSS):**
- Stored XSS
- Reflected XSS
- DOM-based XSS

**Prevention:**
- Input validation
- Output encoding
- Content Security Policy (CSP)
- HTTPOnly cookies

**Cross-Site Request Forgery (CSRF):**
- Token-based prevention
- Same-site cookies
- Referer validation
- Custom headers

**Example CSRF Protection:**
```python
from flask_wtf.csrf import CSRFProtect

csrf = CSRFProtect(app)

@app.route('/transfer', methods=['POST'])
@csrf.exempt
def transfer():
    # CSRF protection is applied automatically
    amount = request.form['amount']
    # Process transfer
```

### 5. API Security

**API Key Management:**
- Key generation and distribution
- Key rotation
- Rate limiting
- Usage monitoring

**OAuth 2.0 Flows:**
- Authorization Code
- Client Credentials
- Implicit (deprecated)
- Resource Owner Password

**API Gateway Security:**
- Authentication
- Authorization
- Rate limiting
- Input validation

### 6. Network Security

**TLS/SSL:**
- Certificate management
- Protocol versions
- Cipher suites
- Perfect forward secrecy

**Firewalls:**
- Network firewalls
- Web application firewalls (WAF)
- Host-based firewalls
- Rules and policies

**VPN and Zero Trust:**
- Remote access
- Network segmentation
- Micro-segmentation
- Identity-based access

### 7. Container and Cloud Security

**Container Security:**
- Image scanning
- Runtime protection
- Network policies
- RBAC

**Cloud Security:**
- IAM policies
- Security groups
- Encryption
- Compliance

**Example Kubernetes Security:**
```yaml
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
spec:
  mtls:
    mode: STRICT

apiVersion: networking.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: my-app
spec:
  selector:
    matchLabels:
      app: my-app
  rules:
  - from:
    - source:
        principals: ["cluster.local/ns/default/sa/my-app"]
    to:
    - operation:
        methods: ["GET", "POST"]
```

## Common Interview Questions

### 1. How do you prevent SQL injection?

**Answer:** I use multiple layers of defense:

1. **Parameterized Queries**: Never concatenate user input into SQL
2. **Input Validation**: Validate and sanitize all inputs
3. **Stored Procedures**: Use when appropriate
4. **ORM**: Use object-relational mapping
5. **Least Privilege**: Database users with minimal permissions
6. **WAF**: Web Application Firewall rules

**Example:**
```java
// Bad: Vulnerable to SQL injection
String query = "SELECT * FROM users WHERE id = " + userId;

// Good: Parameterized query
String query = "SELECT * FROM users WHERE id = ?";
PreparedStatement stmt = connection.prepareStatement(query);
stmt.setInt(1, userId);
ResultSet rs = stmt.executeQuery();
```

### 2. How do you secure APIs?

**Answer:** I implement defense in depth:

1. **Authentication**: Verify identity (OAuth 2.0, API keys)
2. **Authorization**: Check permissions (RBAC, scopes)
3. **Rate Limiting**: Prevent abuse
4. **Input Validation**: Validate all inputs
5. **Encryption**: TLS in transit, encryption at rest
6. **Logging**: Audit and monitoring

**Example Rate Limiting:**
```python
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address

limiter = Limiter(
    app=app,
    key_func=get_remote_address,
    default_limits=["200 per day", "50 per hour"]
)

@app.route("/api/resource")
@limiter.limit("10 per minute")
def get_resource():
    return {"data": "value"}
```

### 3. How do you handle secrets in production?

**Answer:** I use a comprehensive secrets management strategy:

1. **Centralized Storage**: HashiCorp Vault, AWS Secrets Manager
2. **Access Control**: Least privilege, RBAC
3. **Rotation**: Automated secret rotation
4. **Encryption**: At rest and in transit
5. **Audit**: Access logging
6. **Emergency Access**: Break-glass procedures

**Example Vault Usage:**
```python
import hvac

client = hvac.Client(url='https://vault.example.com')
client.token = os.environ['VAULT_TOKEN']

# Read secret
secret = client.secrets.kv.v2.read_secret_version(
    path='myapp/config'
)
db_password = secret['data']['data']['database_password']
```

### 4. How do you implement secure authentication?

**Answer:** I follow security best practices:

1. **Multi-Factor Authentication**: Require MFA for sensitive operations
2. **Strong Password Policies**: Minimum length, complexity requirements
3. **Secure Password Storage**: bcrypt, scrypt, or Argon2
4. **Session Management**: Secure, HttpOnly, SameSite cookies
5. **Account Lockout**: Prevent brute force attacks
6. **OAuth 2.0**: For third-party authentication

**Example JWT Implementation:**
```python
import jwt
from datetime import datetime, timedelta

def create_token(user_id):
    payload = {
        'user_id': user_id,
        'exp': datetime.utcnow() + timedelta(hours=1),
        'iat': datetime.utcnow()
    }
    return jwt.encode(payload, SECRET_KEY, algorithm='HS256')

def verify_token(token):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=['HS256'])
        return payload['user_id']
    except jwt.ExpiredSignatureError:
        return None
    except jwt.InvalidTokenError:
        return None
```

### 5. How do you perform threat modeling?

**Answer:** I use a structured approach:

1. **Identify Assets**: What are we protecting?
2. **Identify Threats**: What could go wrong? (STRIDE model)
3. **Identify Vulnerabilities**: What weaknesses exist?
4. **Assess Risk**: What's the impact and likelihood?
5. **Mitigate**: What controls should we implement?
6. **Validate**: Are controls effective?

**STRIDE Model:**
- **S**poofing: Identity impersonation
- **T**ampering: Data modification
- **R**epudiation: Denying actions
- **I**nformation Disclosure: Data exposure
- **D**enial of Service: Availability attacks
- **E**levation of Privilege: Unauthorized access

### 6. How do you secure microservices?

**Answer:** I implement multiple security layers:

1. **Service Mesh**: mTLS between services
2. **API Gateway**: Centralized authentication
3. **Network Policies**: Restrict traffic
4. **Secrets Management**: Secure credential storage
5. **Container Security**: Image scanning, runtime protection
6. **Logging**: Audit and monitoring

**Example Service Mesh Security:**
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
        methods: ["GET"]
        paths: ["/api/v1/*"]
```

## Security Best Practices

### 1. Secure Development Lifecycle
- Security requirements
- Threat modeling
- Secure coding guidelines
- Code review for security
- Security testing

### 2. Input Validation
- Whitelist validation
- Parameterized queries
- Output encoding
- Content Security Policy

### 3. Authentication and Authorization
- Strong passwords
- Multi-factor authentication
- Least privilege
- Regular access reviews

### 4. Data Protection
- Encryption at rest
- Encryption in transit
- Key management
- Data classification

### 5. Monitoring and Logging
- Security event logging
- Audit trails
- Anomaly detection
- Incident response

## Study Plan

### Week 1-2: Fundamentals
- OWASP Top 10
- Authentication/Authorization
- Cryptography basics

### Week 3-4: Application Security
- Web application security
- API security
- Secure coding practices

### Week 5-6: Infrastructure Security
- Network security
- Container security
- Cloud security

### Week 7-8: Advanced Topics
- Threat modeling
- Incident response
- Compliance

## Resources

### Books
- "Web Application Hacker's Handbook"
- "Security Engineering" by Ross Anderson
- "Application Security Guide" by OWASP

### Online
- OWASP Documentation
- NIST Cybersecurity Framework
- SANS Institute Resources

### Certifications
- CompTIA Security+
- Certified Ethical Hacker (CEH)
- CISSP
- OSCP