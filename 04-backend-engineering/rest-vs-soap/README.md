# REST API vs SOAP: A Comprehensive Comparison Guide

## Table of Contents

- [1. Overview](#1-overview)
- [2. Protocol](#2-protocol)
- [3. Message Format](#3-message-format)
- [4. Architecture](#4-architecture)
- [5. WS-* Standards](#5-ws--standards)
- [6. Security](#6-security)
- [7. Performance](#7-performance)
- [8. Error Handling](#8-error-handling)
- [9. Versioning](#9-versioning)
- [10. Documentation](#10-documentation)
- [11. Use Cases](#11-use-cases)
- [12. Code Examples](#12-code-examples)
- [13. Migration Strategies](#13-migration-strategies)
- [14. Decision Matrix](#14-decision-matrix)
- [15. Best Practices](#15-best-practices)
- [16. Real-World Examples](#16-real-world-examples)
- [17. Testing](#17-testing)
- [18. Performance Comparison](#18-performance-comparison)

---

## 1. Overview

### What is REST?

**REST (Representational State Transfer)** is an architectural style for designing networked applications, introduced by Roy Fielding in his 2000 doctoral dissertation. REST treats everything as a **resource**, identified by URIs (Uniform Resource Identifiers), and uses standard HTTP methods to perform operations on those resources.

REST is not a protocol or standard—it is a set of architectural constraints and principles that, when applied as a whole, emphasize scalability, simplicity, modifiability, and visibility.

**Key Characteristics:**
- Resource-based (nouns, not verbs)
- Stateless communication
- Uniform interface (HTTP methods)
- Layered system
- Cacheable responses

### What is SOAP?

**SOAP (Simple Object Access Protocol)** is a protocol specification for exchanging structured information in the implementation of web services. Originally developed by Microsoft, SOAP uses XML as its message format and typically relies on HTTP or SMTP for message transmission.

SOAP defines a strict message structure with an envelope, header, and body, and can operate over various transport protocols beyond HTTP.

**Key Characteristics:**
- Protocol-based (strict specification)
- XML-based messaging
- Built-in error handling (SOAP Fault)
- Support for WS-* standards
- Transport-independent

### Quick Comparison

| Aspect | REST | SOAP |
|--------|------|------|
| Type | Architectural style | Protocol |
| Primary Format | JSON (also XML, YAML) | XML only |
| Transport | HTTP/HTTPS primarily | HTTP, SMTP, JMS, etc. |
| Complexity | Low | High |
| State | Stateless | Can be stateful |
| Caching | Built-in via HTTP | Not supported |
| Learning Curve | Gentle | Steep |

---

## 2. Protocol

### REST Protocol Stack

REST uses HTTP/HTTPS as its primary transport protocol, using standard HTTP methods:

```
┌─────────────────────────────────────────────┐
│                REST Stack                   │
├─────────────────────────────────────────────┤
│  Application Layer                         │
│  ┌─────────────────────────────────────┐   │
│  │  HTTP Methods:                      │   │
│  │  • GET    → Read a resource         │   │
│  │  • POST   → Create a resource       │   │
│  │  • PUT    → Update a resource       │   │
│  │  • DELETE → Delete a resource       │   │
│  │  • PATCH  → Partial update          │   │
│  │  • HEAD   → Get metadata            │   │
│  │  • OPTIONS → Supported methods      │   │
│  └─────────────────────────────────────┘   │
│                                            │
│  Transport Layer: HTTP/HTTPS               │
│  Security: TLS/SSL (HTTPS)                 │
└─────────────────────────────────────────────┘
```

**REST over HTTP Example:**
```http
GET /api/users/123 HTTP/1.1
Host: api.example.com
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
Accept: application/json
```

### SOAP Protocol Stack

SOAP is transport-independent and can work over multiple protocols:

```
┌─────────────────────────────────────────────┐
│               SOAP Stack                    │
├─────────────────────────────────────────────┤
│  Application Layer                         │
│  ┌─────────────────────────────────────┐   │
│  │  SOAP Message Structure:            │   │
│  │  • Envelope (required)              │   │
│  │  • Header (optional)                │   │
│  │  • Body (required)                  │   │
│  │  • Fault (error handling)           │   │
│  └─────────────────────────────────────┘   │
│                                            │
│  Transport Options:                        │
│  • HTTP/HTTPS (most common)               │
│  • SMTP (email-based)                     │
│  • JMS (Java Message Service)             │
│  • TCP                                    │
│  • UDP                                    │
└─────────────────────────────────────────────┘
```

**SOAP over HTTP Example:**
```http
POST /UserService HTTP/1.1
Host: api.example.com
Content-Type: text/xml; charset=utf-8
SOAPAction: "http://example.com/GetUser"

<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <GetUser xmlns="http://example.com/UserService">
      <UserId>123</UserId>
    </GetUser>
  </soap:Body>
</soap:Envelope>
```

### Protocol Comparison

| Feature | REST | SOAP |
|---------|------|------|
| Primary Transport | HTTP/HTTPS | HTTP, SMTP, JMS, TCP |
| Methods | GET, POST, PUT, DELETE, PATCH | Single endpoint (POST) |
| Port Usage | Any HTTP port | 80/443 typically |
| State Management | Stateless by design | Can maintain state |
| Connection | Stateless | Can be stateful |
| HTTP Features | Full utilization | Limited usage |
| Caching | Native HTTP caching | No caching support |

---

## 3. Message Format

### REST Message Formats

REST supports multiple message formats, with JSON being the most popular:

**JSON (Most Common):**
```json
{
  "id": 123,
  "name": "John Doe",
  "email": "john@example.com",
  "roles": ["admin", "user"],
  "address": {
    "street": "123 Main St",
    "city": "New York",
    "zip": "10001"
  }
}
```

**XML:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<User>
  <id>123</id>
  <name>John Doe</name>
  <email>john@example.com</email>
  <roles>
    <role>admin</role>
    <role>user</role>
  </roles>
  <address>
    <street>123 Main St</street>
    <city>New York</city>
    <zip>10001</zip>
  </address>
</User>
```

**YAML:**
```yaml
id: 123
name: John Doe
email: john@example.com
roles:
  - admin
  - user
address:
  street: 123 Main St
  city: New York
  zip: "10001"
```

**Content Negotiation:**
```http
GET /api/users/123 HTTP/1.1
Accept: application/json  (preferred)
Accept: application/xml   (alternative)
Accept: text/yaml         (alternative)
```

### SOAP Message Format

SOAP exclusively uses XML with a strict envelope structure:

**Complete SOAP Message:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:usr="http://example.com/UserService"
    soap:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
    
    <!-- Optional Header -->
    <soap:Header>
        <usr:Authentication>
            <usr:Token>abc123xyz</usr:Token>
            <usr:Timestamp>2024-01-15T10:30:00Z</usr:Timestamp>
        </usr:Authentication>
        <usr:TransactionId>txn-789</usr:TransactionId>
    </soap:Header>
    
    <!-- Required Body -->
    <soap:Body>
        <usr:GetUserRequest>
            <usr:UserId>123</usr:UserId>
            <usr:IncludeAddress>true</usr:IncludeAddress>
        </usr:GetUserRequest>
    </soap:Body>
    
</soap:Envelope>
```

**SOAP Response:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:usr="http://example.com/UserService">
    
    <soap:Body>
        <usr:GetUserResponse>
            <usr:User>
                <usr:Id>123</usr:Id>
                <usr:Name>John Doe</usr:Name>
                <usr:Email>john@example.com</usr:Email>
                <usr:Address>
                    <usr:Street>123 Main St</usr:Street>
                    <usr:City>New York</usr:City>
                    <usr:Zip>10001</usr:Zip>
                </usr:Address>
            </usr:User>
        </usr:GetUserResponse>
    </soap:Body>
    
</soap:Envelope>
```

### Message Format Comparison

| Feature | REST | SOAP |
|---------|------|------|
| Primary Format | JSON | XML |
| Other Formats | XML, YAML, HTML, Binary | None |
| Schema Validation | Optional (JSON Schema) | Required (XSD) |
| Parsing Complexity | Simple (JSON) | Complex (XML DOM) |
| Payload Size | Compact (JSON) | Verbose (XML) |
| Human Readable | Yes (JSON) | Yes (XML) |
| Namespace Support | No | Yes |
| Binary Data | Base64 or multipart | Base64 |

---

## 4. Architecture

### REST Architecture

REST follows a stateless, resource-based architecture:

```
┌─────────────────────────────────────────────────────────┐
│                    REST Architecture                    │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   Client                                               │
│   ┌──────┐     HTTP Request     ┌──────────────┐      │
│   │      │ ───────────────────► │              │      │
│   │      │ ◄─────────────────── │   Server     │      │
│   └──────┘     HTTP Response    │              │      │
│                                 └──────┬───────┘      │
│                                        │              │
│   Resource Identification:             │              │
│   GET /api/users                       │              │
│   GET /api/users/123                   ▼              │
│   GET /api/users/123/orders       ┌──────────┐       │
│                                   │ Resource  │       │
│   HTTP Methods:                   │  Server   │       │
│   • GET → Read                    └──────────┘       │
│   • POST → Create                                      │
│   • PUT → Update (full)                                │
│   • PATCH → Update (partial)                           │
│   • DELETE → Delete                                    │
│                                                         │
│   Statelessness:                                        │
│   • No session state on server                         │
│   • Each request contains all info needed              │
│   • Server doesn't store client context                │
│   • Enables horizontal scaling                         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**REST Resource Hierarchy:**
```
/api
├── /users
│   ├── /{userId}
│   │   ├── /orders
│   │   │   └── /{orderId}
│   │   ├── /profile
│   │   └── /settings
│   └── /search
├── /products
│   ├── /{productId}
│   │   ├── /reviews
│   │   └── /inventory
│   └── /categories
└── /orders
    └── /{orderId}
        └── /status
```

### SOAP Architecture

SOAP follows an operation-based architecture:

```
┌─────────────────────────────────────────────────────────┐
│                    SOAP Architecture                    │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   Client                                               │
│   ┌──────┐     SOAP Request    ┌──────────────┐       │
│   │      │ ──────────────────► │              │       │
│   │      │ ◄────────────────── │   Service    │       │
│   └──────┘     SOAP Response   │   Endpoint   │       │
│                                 └──────┬───────┘       │
│                                        │               │
│   Single Endpoint:                    │               │
│   POST /UserService                   ▼               │
│   (SOAPAction header)            ┌──────────┐        │
│                                  │   WSDL    │        │
│   Operations:                    │  Contract │        │
│   • GetUser                      └──────────┘        │
│   • CreateUser                                        │
│   • UpdateUser                                        │
│   • DeleteUser                                        │
│                                                         │
│   State Management:                                     │
│   • Stateless by default                               │
│   • Can use WS-ReliableMessaging                       │
│   • Can maintain state via WS-Addressing               │
│   • Transaction support via WS-AtomicTransaction       │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**SOAP Service Contract (WSDL Structure):**
```
UserService.wsdl
├── types (XSD schemas)
│   ├── UserType
│   ├── CreateUserRequest
│   └── GetUserResponse
├── messages
│   ├── GetUserRequestMessage
│   ├── GetUserResponseMessage
│   ├── CreateUserRequestMessage
│   └── CreateUserResponseMessage
├── portType (interface)
│   └── UserServicePortType
│       ├── GetUser
│       └── CreateUser
├── binding (protocol details)
│   └── UserServiceBinding
│       ├── SOAP 1.1 over HTTP
│       └── Security settings
└── service (endpoint URL)
    └── UserService
        └── http://api.example.com/UserService
```

### Architecture Comparison

| Aspect | REST | SOAP |
|--------|------|------|
| Design Philosophy | Resource-oriented | Operation-oriented |
| Interface | Uniform (HTTP methods) | Custom (operation names) |
| State | Stateless | Can be stateful |
| Identification | URI-based | Endpoint-based |
| Communication | Request-Response | Request-Response, One-Way |
| Coupling | Loose | Tight (WSDL contract) |
| Scalability | Highly scalable | Moderate scalability |
| Cacheability | Yes (HTTP caching) | No |
| Layered System | Yes | Optional |
| Code-on-Demand | Optional | No |

---

## 5. WS-* Standards

SOAP has access to a comprehensive set of WS-* standards for enterprise features:

### WS-Security

Provides message-level security, integrity, and confidentiality:

```xml
<soap:Header>
    <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/...">
        <wsse:UsernameToken>
            <wsse:Username>admin</wsse:Username>
            <wsse:Password Type="...#PasswordDigest">
                dGhpcyBpcyBhIGRpZ2VzdA==
            </wsse:Password>
            <wsse:Nonce encoding="...">abc123</wsse:Nonce>
            <wsu:Created>2024-01-15T10:30:00Z</wsu:Created>
        </wsse:UsernameToken>
        
        <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
            <ds:SignedInfo>
                <ds:CanonicalizationMethod Algorithm="..."/>
                <ds:SignatureMethod Algorithm="...#rsa-sha256"/>
                <ds:Reference URI="#Body">
                    <ds:Transforms>
                        <ds:Transform Algorithm="...#enveloped-signature"/>
                    </ds:Transforms>
                    <ds:DigestMethod Algorithm="...#sha256"/>
                    <ds:DigestValue>abc123=</ds:DigestValue>
                </ds:Reference>
            </ds:SignedInfo>
            <ds:SignatureValue>xyz789=</ds:SignatureValue>
        </ds:Signature>
        
        <xenc:EncryptedData xmlns:xenc="http://www.w3.org/2001/04/xmlenc#">
            <xenc:EncryptionMethod Algorithm="...#aes256-cbc"/>
            <xenc:CipherData>
                <xenc:CipherValue>encrypted data here</xenc:CipherValue>
            </xenc:CipherData>
        </xenc:EncryptedData>
    </wsse:Security>
</soap:Header>
```

### WS-ReliableMessaging

Ensures reliable message delivery between services:

```xml
<soap:Header>
    <wsrm:Sequence xmlns:wsrm="http://schemas.xmlsoap.org/ws/2005/02/rm">
        <wsrm:Identifier>http://example.com/sequence/123</wsrm:Identifier>
        <wsrm:MessageNumber>1</wsrm:MessageNumber>
        <wsrm:LastMessage/>
    </wsrm:Sequence>
</soap:Header>
```

**Features:**
- At-most-once delivery
- At-least-once delivery
- Exactly-once delivery
- In-order delivery
- Acknowledgment mechanism
- Retransmission on failure

### WS-AtomicTransaction

Distributed transaction support:

```xml
<soap:Header>
    <wsat:Coordination xmlns:wsat="http://docs.oasis-open.org/ws-tx/wsat/2006/06">
        <wsat:Identifier>http://example.com/transaction/456</wsat:Identifier>
        <wsat:CoordinationType>
            http://docs.oasis-open.org/ws-tx/wsat/2006/06/Completion
        </wsat:CoordinationType>
    </wsat:Coordination>
</soap:Header>
```

### WS-Addressing

Transport-independent addressing:

```xml
<soap:Header>
    <wsa:MessageID xmlns:wsa="http://www.w3.org/2005/08/addressing">
        uuid:123e4567-e89b-12d3-a456-426614174000
    </wsa:MessageID>
    <wsa:ReplyTo xmlns:wsa="http://www.w3.org/2005/08/addressing">
        <wsa:Address>http://client.example.com/callback</wsa:Address>
    </wsa:ReplyTo>
    <wsa:To xmlns:wsa="http://www.w3.org/2005/08/addressing">
        http://server.example.com/UserService
    </wsa:To>
    <wsa:Action xmlns:wsa="http://www.w3.org/2005/08/addressing">
        http://example.com/UserService/GetUser
    </wsa:Action>
</soap:Header>
```

### WS-* Standards Summary

| Standard | Purpose | REST Equivalent |
|----------|---------|-----------------|
| WS-Security | Message security | OAuth2 + JWT + HTTPS |
| WS-ReliableMessaging | Guaranteed delivery | Application-level retry |
| WS-AtomicTransaction | Distributed transactions | Saga pattern |
| WS-Addressing | Transport independence | HTTP headers |
| WS-Policy | Service capabilities | OpenAPI spec |
| WS-Trust | Security token exchange | OAuth2 flows |
| WS-Federation | Federated identity | SAML/OIDC |

---

## 6. Security

### REST Security

REST relies on standard HTTP security mechanisms:

```
┌─────────────────────────────────────────────────────────┐
│                   REST Security Stack                   │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   Transport Security:                                  │
│   ┌─────────────────────────────────────────┐         │
│   │  TLS/SSL (HTTPS)                        │         │
│   │  • Encrypts data in transit             │         │
│   │  • Server certificate verification      │         │
│   │  • Strong cipher suites                 │         │
│   └─────────────────────────────────────────┘         │
│                                                         │
│   Authentication:                                       │
│   ┌─────────────────────────────────────────┐         │
│   │  OAuth 2.0                               │         │
│   │  • Authorization Code Flow               │         │
│   │  • Client Credentials Flow               │         │
│   │  • PKCE for mobile apps                  │         │
│   │                                          │         │
│   │  JWT (JSON Web Tokens)                   │         │
│   │  • Stateless authentication              │         │
│   │  • Contains claims and permissions       │         │
│   │  • Signed and optionally encrypted       │         │
│   └─────────────────────────────────────────┘         │
│                                                         │
│   Authorization:                                        │
│   ┌─────────────────────────────────────────┐         │
│   │  • Scope-based (OAuth2)                 │         │
│   │  • Role-based (RBAC)                    │         │
│   │  • Attribute-based (ABAC)               │         │
│   └─────────────────────────────────────────┘         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**OAuth2 + JWT Example:**
```http
POST /oauth/token HTTP/1.1
Host: auth.example.com
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code=abc123
&redirect_uri=https://app.example.com/callback
&client_id=my-app
&client_secret=secret123
```

**JWT Token Structure:**
```json
{
  "header": {
    "alg": "RS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "1234567890",
    "name": "John Doe",
    "email": "john@example.com",
    "roles": ["admin", "user"],
    "iss": "https://auth.example.com",
    "aud": "https://api.example.com",
    "exp": 1705312200,
    "iat": 1705225800
  },
  "signature": "encrypted-signature-here"
}
```

### SOAP Security

SOAP provides comprehensive security through WS-* standards:

```
┌─────────────────────────────────────────────────────────┐
│                   SOAP Security Stack                   │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   Transport Security:                                  │
│   ┌─────────────────────────────────────────┐         │
│   │  TLS/SSL (HTTPS)                        │         │
│   │  Same as REST                           │         │
│   └─────────────────────────────────────────┘         │
│                                                         │
│   Message-Level Security:                              │
│   ┌─────────────────────────────────────────┐         │
│   │  WS-Security                             │         │
│   │  • UsernameToken (username/password)     │         │
│   │  • X.509 Certificates                   │         │
│   │  • Kerberos tokens                      │         │
│   │  • SAML assertions                      │         │
│   └─────────────────────────────────────────┘         │
│                                                         │
│   XML Security:                                        │
│   ┌─────────────────────────────────────────┐         │
│   │  • XML Digital Signatures                │         │
│   │  • XML Encryption                       │         │
│   │  • Element-level encryption             │         │
│   │  • Partial message signing              │         │
│   └─────────────────────────────────────────┘         │
│                                                         │
│   Advanced Security:                                   │
│   ┌─────────────────────────────────────────┐         │
│   │  WS-Trust                                │         │
│   │  • Security token exchange               │         │
│   │  • Token validation                      │         │
│   │  • Credential delegation                 │         │
│   │                                          │         │
│   │  WS-Federation                           │         │
│   │  • Federated identity                    │         │
│   │  • Single sign-on (SSO)                  │         │
│   │  • Cross-domain authentication           │         │
│   └─────────────────────────────────────────┘         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**SAML Assertion Example:**
```xml
<saml:Assertion xmlns:saml="urn:oasis:names:tc:SAML:2.0:assertion"
    ID="_assertion123"
    IssueInstant="2024-01-15T10:30:00Z"
    Version="2.0">
    
    <saml:Issuer>https://idp.example.com</saml:Issuer>
    
    <saml:Subject>
        <saml:NameID Format="urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress">
            john@example.com
        </saml:NameID>
        <saml:SubjectConfirmation Method="urn:oasis:names:tc:SAML:2.0:cm:bearer">
            <saml:SubjectConfirmationData
                NotOnOrAfter="2024-01-15T11:00:00Z"
                Recipient="https://sp.example.com"/>
        </saml:SubjectConfirmation>
    </saml:Subject>
    
    <saml:Conditions NotBefore="2024-01-15T10:00:00Z"
        NotOnOrAfter="2024-01-15T11:00:00Z">
        <saml:AudienceRestriction>
            <saml:Audience>https://api.example.com</saml:Audience>
        </saml:AudienceRestriction>
    </saml:Conditions>
    
    <saml:AttributeStatement>
        <saml:Attribute Name="role">
            <saml:AttributeValue>admin</saml:AttributeValue>
        </saml:Attribute>
        <saml:Attribute Name="department">
            <saml:AttributeValue>Engineering</saml:AttributeValue>
        </saml:Attribute>
    </saml:AttributeStatement>
</saml:Assertion>
```

### Security Comparison

| Feature | REST | SOAP |
|---------|------|------|
| Transport Security | TLS/HTTPS | TLS/HTTPS |
| Message Security | OAuth2, JWT | WS-Security |
| Authentication | OAuth2, API Keys | WS-Trust, SAML |
| Authorization | Scopes, RBAC | WS-Federation |
| Token Format | JWT (JSON) | SAML (XML) |
| Signature | JWT signed payload | XML Digital Signature |
| Encryption | HTTPS + payload encryption | XML Encryption |
| Granularity | Request-level | Element-level |
| Complexity | Lower | Higher |
| Standards | OAuth2, OpenID Connect | WS-Security, SAML |

---

## 7. Performance

### REST Performance Characteristics

**Advantages:**
- Lightweight JSON payloads (smaller size)
- HTTP caching support (ETags, Cache-Control)
- Connection keep-alive
- No XML parsing overhead
- Stateless design enables horizontal scaling

**JSON vs XML Payload Size:**
```json
// JSON (~200 bytes)
{
  "id": 1,
  "name": "John",
  "email": "john@example.com",
  "active": true
}
```

```xml
<!-- XML (~350 bytes) -->
<?xml version="1.0" encoding="UTF-8"?>
<User>
  <id>1</id>
  <name>John</name>
  <email>john@example.com</email>
  <active>true</active>
</User>
```

### SOAP Performance Characteristics

**Overhead Factors:**
- XML envelope wrapping
- Schema validation
- WS-* header processing
- No native caching
- Larger payload sizes

**Typical Overhead:**
```
REST:  100% base payload
SOAP:  150-300% of REST payload (due to envelope and WS-* headers)
```

### Performance Benchmarks

| Metric | REST (JSON) | SOAP (XML) | REST Advantage |
|--------|-------------|------------|----------------|
| Payload Size | 1x | 2-3x | 50-70% smaller |
| Parse Time | 1x | 2-4x | 50-75% faster |
| Throughput | 1x | 0.5-0.8x | 25-50% higher |
| Latency | 1x | 1.2-2x | 20-50% lower |
| Memory Usage | 1x | 1.5-2x | 33-50% lower |
| Caching | Native HTTP | None | Significant boost |

---

## 8. Error Handling

### REST Error Handling

REST uses HTTP status codes for error communication:

**Standard HTTP Status Codes:**
```
1xx - Informational
  100 Continue
  101 Switching Protocols

2xx - Success
  200 OK
  201 Created
  204 No Content

3xx - Redirection
  301 Moved Permanently
  304 Not Modified

4xx - Client Error
  400 Bad Request
  401 Unauthorized
  403 Forbidden
  404 Not Found
  405 Method Not Allowed
  409 Conflict
  422 Unprocessable Entity
  429 Too Many Requests

5xx - Server Error
  500 Internal Server Error
  502 Bad Gateway
  503 Service Unavailable
  504 Gateway Timeout
```

**REST Error Response Example:**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 422,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/users",
  "errors": [
    {
      "field": "email",
      "value": "invalid-email",
      "message": "Must be a valid email address"
    },
    {
      "field": "age",
      "value": -5,
      "message": "Must be a positive number"
    }
  ]
}
```

**Custom Error Response:**
```json
{
  "errorCode": "USER_NOT_FOUND",
  "message": "User with ID 12345 does not exist",
  "details": {
    "userId": "12345",
    "suggestion": "Check if the user was deleted or use a different ID"
  },
  "supportUrl": "https://docs.example.com/errors/USER_NOT_FOUND"
}
```

### SOAP Error Handling

SOAP uses SOAP Fault for error communication:

**SOAP Fault Structure:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    
    <soap:Body>
        <soap:Fault>
            <faultcode>soap:Client</faultcode>
            <faultstring>User not found</faultstring>
            <faultactor>http://example.com/UserService</faultactor>
            <detail>
                <usr:UserError xmlns:usr="http://example.com/UserService">
                    <usr:ErrorCode>USER_NOT_FOUND</usr:ErrorCode>
                    <usr:UserId>12345</usr:UserId>
                    <usr:Timestamp>2024-01-15T10:30:00Z</usr:Timestamp>
                </usr:UserError>
            </detail>
        </soap:Fault>
    </soap:Body>
    
</soap:Envelope>
```

**SOAP Fault Codes:**
```
soap:VersionMismatch - Envelope namespace mismatch
soap:MustUnderstand - Header not understood
soap:Client - Client-side error (malformed request)
soap:Server - Server-side error (processing failure)
```

### Error Handling Comparison

| Feature | REST | SOAP |
|---------|------|------|
| Error Format | HTTP status codes + body | SOAP Fault |
| Standardization | HTTP standard | SOAP specification |
| Error Codes | Numeric (404, 500) | String (Client, Server) |
| Error Details | Response body | Fault detail element |
| Client Handling | Check status code | Parse Fault element |
| Richness | Flexible JSON structure | XML-based structure |
| Validation Errors | Multiple field errors | Single fault per request |
| Documentation | Well-understood | Requires WSDL explanation |

---

## 9. Versioning

### REST Versioning Strategies

**URI Versioning (Most Common):**
```
# Version in URL path
https://api.example.com/v1/users
https://api.example.com/v2/users

# Different resources
/api/v1/products
/api/v2/products
```

**Header Versioning:**
```http
GET /api/users HTTP/1.1
Host: api.example.com
Accept: application/vnd.example.v1+json

# Or custom header
GET /api/users HTTP/1.1
X-API-Version: 1
```

**Query Parameter Versioning:**
```
https://api.example.com/users?version=1
https://api.example.com/users?version=2
```

**Media Type Versioning:**
```http
GET /api/users HTTP/1.1
Accept: application/vnd.myapi.user.v1+json

# Response
HTTP/1.1 200 OK
Content-Type: application/vnd.myapi.user.v1+json
```

### SOAP Versioning

**Namespace-based Versioning:**
```xml
<!-- Version 1.0 -->
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:usr="http://example.com/UserService/v1">

<!-- Version 2.0 -->
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:usr="http://example.com/UserService/v2">

<!-- WSDL Namespace Versioning -->
<definitions xmlns="http://schemas.xmlsoap.org/wsdl/"
    xmlns:usr="http://example.com/UserService/v2">
```

**Binding Versioning:**
```xml
<service name="UserService">
    <port name="UserServiceV1" binding="tns:UserServiceV1Binding">
        <soap:address location="http://api.example.com/UserService/v1"/>
    </port>
    <port name="UserServiceV2" binding="tns:UserServiceV2Binding">
        <soap:address location="http://api.example.com/UserService/v2"/>
    </port>
</service>
```

### Versioning Comparison

| Strategy | REST | SOAP |
|----------|------|------|
| URI/Path | Common | Not used |
| Header | Common | Namespace |
| Query Parameter | Common | Not used |
| Namespace | N/A | Primary method |
| Multiple Versions | Can run simultaneously | Can run simultaneously |
| Breaking Changes | New version needed | New namespace needed |
| Deprecation | Easy (sunset headers) | Complex (WSDL updates) |

---

## 10. Documentation

### REST Documentation (OpenAPI/Swagger)

```yaml
openapi: 3.0.3
info:
  title: User Service API
  description: REST API for managing users
  version: 1.0.0
  contact:
    name: API Support
    email: support@example.com

servers:
  - url: https://api.example.com/v1
    description: Production server

paths:
  /users:
    get:
      summary: List all users
      operationId: listUsers
      tags:
        - Users
      parameters:
        - name: page
          in: query
          required: false
          schema:
            type: integer
            default: 1
        - name: limit
          in: query
          required: false
          schema:
            type: integer
            default: 20
            maximum: 100
      responses:
        '200':
          description: Successful response
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: array
                    items:
                      $ref: '#/components/schemas/User'
                  pagination:
                    $ref: '#/components/schemas/Pagination'
        '401':
          description: Unauthorized
        '429':
          description: Rate limit exceeded

    post:
      summary: Create a new user
      operationId: createUser
      tags:
        - Users
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateUserRequest'
      responses:
        '201':
          description: User created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
        '400':
          description: Validation error

components:
  schemas:
    User:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
        email:
          type: string
          format: email
        createdAt:
          type: string
          format: date-time

    CreateUserRequest:
      type: object
      required:
        - name
        - email
      properties:
        name:
          type: string
          minLength: 2
          maxLength: 100
        email:
          type: string
          format: email
        password:
          type: string
          minLength: 8

  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

security:
  - bearerAuth: []
```

### SOAP Documentation (WSDL)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://schemas.xmlsoap.org/wsdl/"
    xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
    xmlns:tns="http://example.com/UserService"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    targetNamespace="http://example.com/UserService"
    name="UserService">

    <!-- Types -->
    <types>
        <xsd:schema targetNamespace="http://example.com/UserService">
            <xsd:complexType name="User">
                <xsd:sequence>
                    <xsd:element name="Id" type="xsd:int"/>
                    <xsd:element name="Name" type="xsd:string"/>
                    <xsd:element name="Email" type="xsd:string"/>
                    <xsd:element name="CreatedAt" type="xsd:dateTime"/>
                </xsd:sequence>
            </xsd:complexType>

            <xsd:element name="GetUserRequest">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="UserId" type="xsd:int"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>

            <xsd:element name="GetUserResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="User" type="tns:User"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>

            <xsd:element name="CreateUserRequest">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="Name" type="xsd:string"/>
                        <xsd:element name="Email" type="xsd:string"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>

            <xsd:element name="CreateUserResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="UserId" type="xsd:int"/>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
        </xsd:schema>
    </types>

    <!-- Messages -->
    <message name="GetUserInput">
        <part name="parameters" element="tns:GetUserRequest"/>
    </message>
    <message name="GetUserOutput">
        <part name="parameters" element="tns:GetUserResponse"/>
    </message>
    <message name="CreateUserInput">
        <part name="parameters" element="tns:CreateUserRequest"/>
    </message>
    <message name="CreateUserOutput">
        <part name="parameters" element="tns:CreateUserResponse"/>
    </message>

    <!-- Port Type (Interface) -->
    <portType name="UserServicePortType">
        <operation name="GetUser">
            <input message="tns:GetUserInput"/>
            <output message="tns:GetUserOutput"/>
        </operation>
        <operation name="CreateUser">
            <input message="tns:CreateUserInput"/>
            <output message="tns:CreateUserOutput"/>
        </operation>
    </portType>

    <!-- Binding -->
    <binding name="UserServiceBinding" type="tns:UserServicePortType">
        <soap:binding style="document"
            transport="http://schemas.xmlsoap.org/soap/http"/>
        <operation name="GetUser">
            <soap:operation soapAction="http://example.com/GetUser"/>
            <input>
                <soap:body use="literal"/>
            </input>
            <output>
                <soap:body use="literal"/>
            </output>
        </operation>
        <operation name="CreateUser">
            <soap:operation soapAction="http://example.com/CreateUser"/>
            <input>
                <soap:body use="literal"/>
            </input>
            <output>
                <soap:body use="literal"/>
            </output>
        </operation>
    </binding>

    <!-- Service -->
    <service name="UserService">
        <port name="UserServicePort" binding="tns:UserServiceBinding">
            <soap:address location="http://api.example.com/UserService"/>
        </port>
    </service>
</definitions>
```

### Documentation Comparison

| Aspect | REST (OpenAPI) | SOAP (WSDL) |
|--------|----------------|-------------|
| Format | YAML/JSON | XML |
| Readability | High (human-friendly) | Moderate (verbose) |
| Tooling | Swagger UI, Redoc | SoapUI, Eclipse |
| Code Generation | Multiple generators | Limited generators |
| Testing | Built-in try-it-out | Requires SoapUI |
| Version Control | Git-friendly | Complex diffs |
| Ecosystem | Large, modern | Mature, legacy |

---

## 11. Use Cases

### When to Use REST

**Ideal Scenarios:**
- Public APIs (developer platforms)
- Mobile applications
- Web applications (SPAs, microservices)
- IoT devices
- Social media integrations
- Real-time applications (with WebSockets)

**REST is Best For:**
- Simple CRUD operations
- Caching requirements
- High-traffic APIs
- Quick prototyping
- Modern architectures
- Developer-friendly APIs

**Real-World REST Examples:**
```
Twitter API         → Social media integration
GitHub API          → Developer platform
Stripe API          → Payment processing
Twilio API          → Communication services
Google Maps API     → Location services
Netflix API         → Streaming services
```

### When to Use SOAP

**Ideal Scenarios:**
- Enterprise applications
- Financial services (banking, insurance)
- Healthcare systems
- Government systems
- Legacy system integration
- Legal/compliance requirements

**SOAP is Best For:**
- Complex transactions
- ACID compliance needs
- Guaranteed message delivery
- Multi-party agreements
- Strict security requirements
- Long-running transactions

**Real-World SOAP Examples:**
```
Banking Systems     → SWIFT, wire transfers
Payment Gateways    → PayPal (legacy), Authorize.net
Healthcare          → HL7, FHIR
Enterprise ERP      → SAP, Oracle
Government          → IRS, healthcare.gov
Telecom             → SMS, roaming agreements
```

---

## 12. Code Examples

### REST API Example (Spring Boot)

**User Entity:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Getters, setters, constructors
}
```

**User DTO:**
```java
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}

public class CreateUserRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;
    
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    @Size(min = 8)
    private String password;
    
    // Getters, setters
}
```

**REST Controller:**
```java
@RestController
@RequestMapping("/api/v1/users")
@Validated
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(users.getTotalElements()))
            .body(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        UserDTO createdUser = userService.createUser(request);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(createdUser);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        
        UserDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        
        UserDTO updatedUser = userService.partialUpdateUser(id, updates);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Service Layer:**
```java
@Service
@Transactional
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
            .map(userMapper::toDTO);
    }
    
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toDTO);
    }
    
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }
        
        User user = userMapper.toEntity(request);
        user.setPassword(hashPassword(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        log.info("Created user with id: {}", savedUser.getId());
        
        return userMapper.toDTO(savedUser);
    }
    
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }
}
```

**Exception Handler:**
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        
        ErrorResponse error = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message("Invalid input data")
            .timestamp(LocalDateTime.now())
            .errors(fieldErrors.stream()
                .map(fe -> new ErrorDetail(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList()))
            .build();
        
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error: ", ex);
        
        ErrorResponse error = ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message("An unexpected error occurred")
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

### SOAP Service Example (Spring WS)

**User Entity (same as REST):**
```java
@Entity
@Table(name = "users")
@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private LocalDateTime createdAt;
    
    // Getters, setters
}
```

**SOAP Request/Response Objects:**
```java
@XmlRootElement(name = "GetUserRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUserRequest {
    @XmlElement(name = "UserId")
    private Long userId;
    
    public GetUserRequest() {}
    
    public GetUserRequest(Long userId) {
        this.userId = userId;
    }
    
    // Getters, setters
}

@XmlRootElement(name = "GetUserResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUserResponse {
    @XmlElement(name = "User")
    private User user;
    
    public GetUserResponse() {}
    
    public GetUserResponse(User user) {
        this.user = user;
    }
    
    // Getters, setters
}

@XmlRootElement(name = "CreateUserRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserRequest {
    @XmlElement(name = "Name", required = true)
    @NotBlank
    private String name;
    
    @XmlElement(name = "Email", required = true)
    @Email
    private String email;
    
    public CreateUserRequest() {}
    
    // Getters, setters
}

@XmlRootElement(name = "CreateUserResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateUserResponse {
    @XmlElement(name = "UserId")
    private Long userId;
    
    @XmlElement(name = "Success")
    private boolean success;
    
    @XmlElement(name = "Message")
    private String message;
    
    public CreateUserResponse() {}
    
    public CreateUserResponse(Long userId, boolean success, String message) {
        this.userId = userId;
        this.success = success;
        this.message = message;
    }
    
    // Getters, setters
}
```

**SOAP Endpoint:**
```java
@Component
@Slf4j
public class UserEndpoint {
    
    private static final String NAMESPACE_URI = "http://example.com/UserService";
    
    private final UserRepository userRepository;
    
    public UserEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
        log.info("SOAP request received for GetUser: {}", request.getUserId());
        
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new SOAPFaultException(
                createSOAPFault("User not found", "Client")));
        
        GetUserResponse response = new GetUserResponse();
        response.setUser(user);
        
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateUserRequest")
    @ResponsePayload
    public CreateUserResponse createUser(@RequestPayload CreateUserRequest request) {
        log.info("SOAP request received for CreateUser: {}", request.getName());
        
        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setCreatedAt(LocalDateTime.now());
            
            User savedUser = userRepository.save(user);
            log.info("Created user with id: {}", savedUser.getId());
            
            return new CreateUserResponse(
                savedUser.getId(), 
                true, 
                "User created successfully");
                
        } catch (DataIntegrityViolationException ex) {
            return new CreateUserResponse(
                null, 
                false, 
                "Email already exists");
        }
    }
    
    private SOAPFault createSOAPFault(String message, String faultCode) {
        SOAPFactory factory = SOAPFactory.newInstance();
        SOAPFault fault = factory.createFault(
            message, 
            new QName(NAMESPACE_URI, faultCode));
        return fault;
    }
}
```

**SOAP Web Service Configuration:**
```java
@Configuration
@EnableWs
public class SoapWebServiceConfig extends WsConfigurerAdapter {
    
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }
    
    @Bean(name = "users")
    public DefaultWsdl11Definition defaultWsdl11Definition(
            XsdSchema userSchema) {
        
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("UserServicePort");
        wsdl11Definition.setLocationUri("/ws/UserService");
        wsdl11Definition.setTargetNamespace("http://example.com/UserService");
        wsdl11Definition.setSchema(userSchema);
        
        return wsdl11Definition;
    }
    
    @Bean
    public XsdSchema userSchema() {
        return new SimpleXsdSchema(
            new ClassPathResource("schemas/user.xsd"));
    }
}
```

**XSD Schema:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
    targetNamespace="http://example.com/UserService"
    xmlns:tns="http://example.com/UserService"
    elementFormDefault="qualified">

    <xs:complexType name="User">
        <xs:sequence>
            <xs:element name="Id" type="xs:long"/>
            <xs:element name="Name" type="xs:string"/>
            <xs:element name="Email" type="xs:string"/>
            <xs:element name="CreatedAt" type="xs:dateTime"/>
        </xs:sequence>
    </xs:complexType>

    <xs:element name="GetUserRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="UserId" type="xs:long"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="GetUserResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="User" type="tns:User"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="CreateUserRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="Name" type="xs:string"/>
                <xs:element name="Email" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="CreateUserResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="UserId" type="xs:long"/>
                <xs:element name="Success" type="xs:boolean"/>
                <xs:element name="Message" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
</xs:schema>
```

---

## 13. Migration Strategies

### REST to SOAP Migration

**When to Migrate:**
- Enterprise compliance requirements
- Need for WS-Security
- Integration with legacy SOAP systems
- ACID transaction requirements

**Migration Steps:**
1. Analyze existing REST endpoints
2. Create WSDL contract
3. Implement SOAP endpoints alongside REST
4. Add WS-* standards as needed
5. Update clients gradually
6. Deprecate REST endpoints

**Dual Implementation Pattern:**
```java
// REST Controller (existing)
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        // existing implementation
    }
}

// SOAP Endpoint (new)
@Component
public class UserSoapEndpoint {
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
        // reuse same service layer
        return userMapper.toSOAPResponse(
            userService.getUserById(request.getUserId()));
    }
}
```

### SOAP to REST Migration

**When to Migrate:**
- Building modern client applications
- Need for better performance
- Developer experience improvement
- Mobile/web optimization

**Migration Steps:**
1. Document existing SOAP operations
2. Design RESTful resource model
3. Create REST endpoints
4. Implement API versioning
5. Add OpenAPI documentation
6. Migrate clients incrementally
7. Decommission SOAP endpoints

**REST Wrapper for SOAP:**
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserRestWrapper {
    
    private final UserSoapClient soapClient;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        // Call existing SOAP service
        GetUserRequest soapRequest = new GetUserRequest(id);
        GetUserResponse soapResponse = soapClient.getUser(soapRequest);
        
        // Convert to REST response
        UserDTO restResponse = convertToREST(soapResponse);
        return ResponseEntity.ok(restResponse);
    }
    
    private UserDTO convertToREST(GetUserResponse soapResponse) {
        UserDTO dto = new UserDTO();
        dto.setId(soapResponse.getUser().getId());
        dto.setName(soapResponse.getUser().getName());
        dto.setEmail(soapResponse.getUser().getEmail());
        return dto;
    }
}
```

---

## 14. Decision Matrix

### Comprehensive Comparison Table

| Aspect | REST | SOAP | Winner |
|--------|------|------|--------|
| **Protocol** | HTTP/HTTPS | HTTP, SMTP, JMS | REST (simplicity) |
| **Message Format** | JSON, XML, YAML | XML only | REST (flexibility) |
| **Architecture** | Resource-based | Operation-based | REST (scalability) |
| **Standards** | Minimal | WS-* full suite | SOAP (enterprise) |
| **Security** | OAuth2, JWT | WS-Security, SAML | SOAP (granularity) |
| **Performance** | Lightweight | Heavyweight | REST (speed) |
| **Error Handling** | HTTP status codes | SOAP Fault | Tie (both adequate) |
| **Versioning** | URI/Header/Query | Namespace | REST (options) |
| **Documentation** | OpenAPI/Swagger | WSDL | REST (usability) |
| **Caching** | HTTP caching | None | REST (efficiency) |
| **Tooling** | Excellent | Good | REST (modern tools) |
| **Learning Curve** | Gentle | Steep | REST (ease of use) |
| **Scalability** | Highly scalable | Moderate | REST (horizontal) |
| **Enterprise** | Good | Excellent | SOAP (compliance) |
| **Public APIs** | Excellent | Poor | REST (adoption) |
| **Legacy Integration** | Moderate | Excellent | SOAP (compatibility) |
| **Transaction Support** | Saga pattern | WS-AtomicTransaction | SOAP (built-in) |
| **Reliability** | Application-level | WS-ReliableMessaging | SOAP (guaranteed) |
| **Real-time** | WebSocket, SSE | Limited | REST (modern) |
| **Mobile** | Excellent | Poor | REST (efficiency) |

### Decision Guide

```
Choose REST When:
├── Building public APIs
├── Targeting web/mobile clients
├── Performance is critical
├── Caching is important
├── Working with modern frameworks
├── Need for developer adoption
└── Building microservices

Choose SOAP When:
├── Enterprise compliance required
├── Need for WS-Security
├── ACID transactions needed
├── Guaranteed message delivery
├── Integrating with legacy systems
├── Multi-party agreements
└── Strict regulatory requirements

Consider Hybrid When:
├── Mixed client requirements
├── Gradual migration needed
├── Different security needs
└── Performance varies by use case
```

---

## 15. Best Practices

### REST Best Practices

**1. Resource Design:**
```bash
# Good: Nouns, plural, hierarchical
GET /api/v1/users
GET /api/v1/users/123
GET /api/v1/users/123/orders
GET /api/v1/users/123/orders/456

# Bad: Verbs, singular, flat
GET /api/getUser
GET /api/user?id=123
POST /api/createOrder
```

**2. HTTP Methods:**
```bash
# Use correct methods
GET    /users          → List users (read-only)
POST   /users          → Create user
GET    /users/123      → Get specific user
PUT    /users/123      → Full update
PATCH  /users/123      → Partial update
DELETE /users/123      → Delete user

# Idempotency
GET, PUT, DELETE → Idempotent (safe to retry)
POST, PATCH → Not idempotent (use idempotency keys)
```

**3. Status Codes:**
```bash
# Success
200 OK                    → Successful GET, PUT, PATCH
201 Created               → Successful POST
204 No Content            → Successful DELETE

# Client Errors
400 Bad Request           → Validation failed
401 Unauthorized          → Authentication required
403 Forbidden             → Authorization failed
404 Not Found             → Resource doesn't exist
409 Conflict              → Resource already exists
422 Unprocessable Entity  → Business logic error
429 Too Many Requests     → Rate limit exceeded

# Server Errors
500 Internal Server Error → Unexpected error
502 Bad Gateway           → Upstream service error
503 Service Unavailable   → Maintenance/overload
```

**4. Pagination:**
```bash
# Query parameters
GET /api/users?page=1&limit=20&sort=createdAt:desc

# Response headers
Link: <https://api.example.com/users?page=2>; rel="next"
X-Total-Count: 150
X-Page-Count: 8
```

**5. Error Response Format:**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 422,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/users",
  "errors": [
    {
      "field": "email",
      "value": "invalid",
      "message": "Must be a valid email"
    }
  ],
  "supportUrl": "https://docs.example.com/errors/VALIDATION_FAILED"
}
```

**6. Versioning Strategy:**
```bash
# URI versioning (recommended for major versions)
/api/v1/users
/api/v2/users

# Header versioning (for minor versions)
Accept: application/vnd.myapi.v1+json
```

### SOAP Best Practices

**1. WSDL Design:**
```xml
<!-- Use document/literal style (not RPC/encoded) -->
<binding name="UserServiceBinding" type="tns:UserServicePortType">
    <soap:binding style="document"
        transport="http://schemas.xmlsoap.org/soap/http"/>
</binding>

<!-- Use meaningful operation names -->
<operation name="GetUser">
    <input message="tns:GetUserInput"/>
    <output message="tns:GetUserOutput"/>
</operation>
```

**2. Error Handling:**
```xml
<!-- Use appropriate fault codes -->
<soap:Fault>
    <faultcode>soap:Client</faultcode>
    <faultstring>User not found</faultstring>
    <detail>
        <error:Error xmlns:error="http://example.com/errors">
            <error:Code>USER_NOT_FOUND</error:Code>
            <error:Message>User with ID 123 does not exist</error:Message>
        </error:Error>
    </detail>
</soap:Fault>
```

**3. Security Best Practices:**
```xml
<!-- Always use HTTPS -->
<soap:address location="https://api.example.com/UserService"/>

<!-- Use WS-Security for sensitive operations -->
<wsse:Security>
    <wsse:UsernameToken>
        <wsse:Username>client</wsse:Username>
        <wsse:Password Type="...#PasswordDigest">...</wsse:Password>
    </wsse:UsernameToken>
</wsse:Security>

<!-- Sign and encrypt critical messages -->
<ds:Signature>...</ds:Signature>
<xenc:EncryptedData>...</xenc:EncryptedData>
```

**4. Performance Optimization:**
```xml
<!-- Use MTOM for binary data -->
<soap:Header>
    <xop:Include href="cid:binary-data@example.com"/>
</soap:Header>

<!-- Implement proper caching headers -->
Cache-Control: max-age=3600
```

---

## 16. Real-World Examples

### REST API Examples

**Stripe (Payment Processing):**
```bash
# Create a charge
POST https://api.stripe.com/v1/charges
Authorization: Bearer sk_test_...

amount=2000
currency=usd
source=tok_visa
description="Charge for jenny.rosen@example.com"

# Response
{
  "id": "ch_1234567890",
  "object": "charge",
  "amount": 2000,
  "currency": "usd",
  "status": "succeeded",
  "created": 1672531200
}
```

**GitHub (Developer Platform):**
```bash
# Get repository
GET https://api.github.com/repos/owner/repo
Accept: application/vnd.github.v3+json

# Response
{
  "id": 123456,
  "name": "repo",
  "full_name": "owner/repo",
  "private": false,
  "html_url": "https://github.com/owner/repo",
  "description": "Repository description",
  "stargazers_count": 100,
  "forks_count": 25
}
```

**Twitter (Social Media):**
```bash
# Post a tweet
POST https://api.twitter.com/2/tweets
Authorization: OAuth ...

{
  "text": "Hello world!"
}

# Response
{
  "data": {
    "id": "1234567890",
    "text": "Hello world!"
  }
}
```

### SOAP API Examples

**Banking (SWIFT):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:swift="http://swift.com/messaging">
    
    <soap:Header>
        <swift:MessageHeader>
            <swift:MessageType>MT103</swift:MessageType>
            <swift:SenderId>BANKUS33</swift:SenderId>
            <swift:ReceiverId>DEUTDEFF</swift:ReceiverId>
            <swift:MessageReference>REF123456</swift:MessageReference>
        </swift:MessageHeader>
    </soap:Header>
    
    <soap:Body>
        <swift:SingleCustomerCreditTransfer>
            <swift:TransactionReference>TXN789</swift:TransactionReference>
            <swift:Amount currency="USD">10000.00</swift:Amount>
            <swift:OrderingCustomer>
                <swift:Name>John Doe</swift:Name>
                <swift:Account>1234567890</swift:Account>
            </swift:OrderingCustomer>
            <swift:BeneficiaryCustomer>
                <swift:Name>Jane Smith</swift:Name>
                <swift:Account>0987654321</swift:Account>
            </swift:BeneficiaryCustomer>
        </swift:SingleCustomerCreditTransfer>
    </soap:Body>
</soap:Envelope>
```

**Healthcare (HL7):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:hl7="urn:hl7-org:v3">
    
    <soap:Body>
        <hl7:PRPA_IN101318>
            <hl7:id extension="12345"/>
            <hl7:creationTime value="20240115"/>
            <hl7:controlCode value="201"/>
            
            <hl7:patient>
                <hl7:id extension="67890"/>
                <hl7:patientPerson>
                    <hl7:name>
                        <hl7:given>John</hl7:given>
                        <hl7:family>Doe</hl7:family>
                    </hl7:name>
                    <hl7:genderCode code="M"/>
                    <hl7:birthTime value="19800115"/>
                </hl7:patientPerson>
            </hl7:patient>
        </hl7:PRPA_IN101318>
    </soap:Body>
</soap:Envelope>
```

---

## 17. Testing

### REST Testing Tools & Examples

**cURL:**
```bash
# GET request
curl -X GET https://api.example.com/v1/users/123 \
  -H "Authorization: Bearer token123" \
  -H "Accept: application/json"

# POST request
curl -X POST https://api.example.com/v1/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer token123" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }'

# PUT request
curl -X PUT https://api.example.com/v1/users/123 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer token123" \
  -d '{
    "name": "John Updated",
    "email": "john.new@example.com"
  }'

# DELETE request
curl -X DELETE https://api.example.com/v1/users/123 \
  -H "Authorization: Bearer token123"

# With query parameters
curl -X GET "https://api.example.com/v1/users?page=1&limit=20" \
  -H "Authorization: Bearer token123"
```

**Postman:**
```javascript
// Pre-request script
pm.environment.set("token", pm.response.json().access_token);

// Test script
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response time is less than 500ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(500);
});

pm.test("User data is valid", function () {
    var data = pm.response.json();
    pm.expect(data).to.have.property('id');
    pm.expect(data).to.have.property('name');
    pm.expect(data).to.have.property('email');
    pm.expect(data.email).to.include('@');
});
```

**REST Assured (Java):**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void shouldGetUserById() {
        ResponseEntity<UserDTO> response = restTemplate
            .exchange(
                "/api/v1/users/1",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                UserDTO.class
            );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }
    
    @Test
    public void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        
        ResponseEntity<UserDTO> response = restTemplate
            .postForEntity(
                "/api/v1/users",
                new HttpEntity<>(request, createHeaders()),
                UserDTO.class
            );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
    }
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth("test-token");
        return headers;
    }
}
```

### SOAP Testing Tools & Examples

**SoapUI:**
```xml
<!-- SoapUI Test Request -->
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
    <soap:Body>
        <GetUserRequest xmlns="http://example.com/UserService">
            <UserId>123</UserId>
        </GetUserRequest>
    </soap:Body>
</soap:Envelope>

<!-- SoapUI Assertion -->
<Assertion type="SOAP Response">
    <Config>
        <XPath>//GetUserResponse/User/Name</XPath>
        <Value>John Doe</Value>
    </Config>
</Assertion>
```

**SoapUI Groovy Script:**
```groovy
// Setup test context
def testCase = context.testCase
def testSuite = context.testSuite

// Get request property
def userId = testCase.getPropertyValue("UserId")

// Log request
log.info("Testing GetUser for ID: ${userId}")

// Validate response
def response = testRunner.runTestStepByName("GetUser")
def responseXml = response.getResponse().getContentAsString()

// Check for fault
if (responseXml.contains("soap:Fault")) {
    throw new Exception("SOAP Fault received")
}

// Extract values using Groovy XML
def slurper = new XmlSlurper()
def xml = slurper.parseText(responseXml)
def userName = xml.Body.GetUserResponse.User.Name.text()

assert userName == "John Doe" : "Unexpected user name: ${userName}"
```

**Apache CXF Client:**
```java
// Generate client from WSDL
JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
factory.setServiceClass(UserService.class);
factory.setAddress("http://localhost:8080/ws/UserService");

UserService client = (UserService) factory.create();

// Test operations
GetUserRequest request = new GetUserRequest();
request.setUserId(123L);

try {
    GetUserResponse response = client.getUser(request);
    assertNotNull(response.getUser());
    assertEquals("John Doe", response.getUser().getName());
} catch (SOAPFaultException e) {
    fail("SOAP Fault: " + e.getMessage());
}
```

### Testing Comparison

| Aspect | REST | SOAP |
|--------|------|------|
| Primary Tool | Postman, curl | SoapUI |
| Unit Testing | REST Assured, JUnit | CXF Test |
| Mock Servers | WireMock, MockServer | SoapUI Mock |
| Load Testing | JMeter, Gatling | JMeter, SoapUI |
| Contract Testing | Pact, Spring Cloud | WSDL validation |
| API Monitoring | Newman, Postman Cloud | SoapUI |

---

## 18. Performance Comparison

### Payload Size Analysis

```
User Data Payload Comparison:
┌─────────────────────────────────────────────────────────┐
│                    JSON (REST)                          │
├─────────────────────────────────────────────────────────┤
│ {                                                       │
│   "id": 1,                                              │
│   "name": "John Doe",                                   │
│   "email": "john@example.com",                          │
│   "active": true                                        │
│ }                                                       │
│                                                         │
│ Size: ~120 bytes                                        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                    XML (SOAP)                           │
├─────────────────────────────────────────────────────────┤
│ <?xml version="1.0" encoding="UTF-8"?>                 │
│ <soap:Envelope                                         │
│   xmlns:soap="http://schemas.xmlsoap.org/soap/          │
│   envelope/">                                           │
│   <soap:Body>                                           │
│     <GetUserResponse>                                   │
│       <User>                                            │
│         <Id>1</Id>                                      │
│         <Name>John Doe</Name>                           │
│         <Email>john@example.com</Email>                 │
│         <Active>true</Active>                           │
│       </User>                                           │
│     </GetUserResponse>                                  │
│   </soap:Body>                                          │
│ </soap:Envelope>                                        │
│                                                         │
│ Size: ~350 bytes (3x larger)                            │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              SOAP with WS-Security                      │
├─────────────────────────────────────────────────────────┤
│ Size: ~800-1200 bytes (7-10x larger than JSON)         │
│                                                         │
│ Additional overhead from:                               │
│ • WS-Security headers                                   │
│ • Digital signatures                                    │
│ • Encryption elements                                   │
│ • Timestamp and nonce                                   │
└─────────────────────────────────────────────────────────┘
```

### Benchmark Results

```
Performance Benchmark (1000 requests, average):
┌──────────────────┬─────────────┬─────────────┬─────────┐
│ Metric           │ REST (JSON) │ SOAP (XML)  │ Winner  │
├──────────────────┼─────────────┼─────────────┼─────────┤
│ Response Time    │ 45ms        │ 120ms       │ REST    │
│ Throughput       │ 2,500 rps   │ 800 rps     │ REST    │
│ Payload Size     │ 1 KB        │ 3.5 KB      │ REST    │
│ Memory Usage     │ 50 MB       │ 120 MB      │ REST    │
│ CPU Usage        │ 15%         │ 35%         │ REST    │
│ Connection Pool  │ 100         │ 50          │ REST    │
│ Caching Hit Rate │ 80%         │ 0%          │ REST    │
└──────────────────┴─────────────┴─────────────┴─────────┘

Scalability Test (concurrent users):
┌────────────────┬─────────────┬─────────────┐
│ Users          │ REST (ms)   │ SOAP (ms)   │
├────────────────┼─────────────┼─────────────┤
│ 100            │ 50          │ 150         │
│ 500            │ 80          │ 350         │
│ 1,000          │ 120         │ 800         │
│ 5,000          │ 200         │ 2,500       │
│ 10,000         │ 350         │ 5,000+      │
└────────────────┴─────────────┴─────────────┘
```

### Performance Optimization Tips

**REST Optimization:**
```bash
# Enable compression
Content-Encoding: gzip

# Use HTTP/2
HTTP/2 200 OK

# Implement caching
Cache-Control: public, max-age=3600
ETag: "abc123"

# Use pagination
GET /api/users?page=1&limit=50

# Minimize payload
Fields: id,name,email
```

**SOAP Optimization:**
```xml
<!-- Use MTOM for binary data -->
<soap:Header>
    <xop:Include href="cid:data@example.com"/>
</soap:Header>

<!-- Implement WS-Addressing for async -->
<wsa:MessageID>uuid:123</wsa:MessageID>
<wsa:ReplyTo>
    <wsa:Address>http://callback.example.com</wsa:Address>
</wsa:ReplyTo>

<!-- Use WS-ReliableMessaging for reliability -->
<wsrm:Sequence>
    <wsrm:Identifier>sequence-123</wsrm:Identifier>
    <wsrm:MessageNumber>1</wsrm:MessageNumber>
</wsrm:Sequence>
```

---

## Summary

### Key Takeaways

1. **REST** is ideal for:
   - Modern web and mobile applications
   - Public APIs requiring high adoption
   - Systems needing high performance and scalability
   - Applications with caching requirements

2. **SOAP** is ideal for:
   - Enterprise applications with strict compliance
   - Financial and healthcare systems
   - Systems requiring guaranteed delivery
   - Legacy system integration

3. **Choose based on:**
   - Client requirements (web, mobile, enterprise)
   - Security and compliance needs
   - Performance requirements
   - Existing infrastructure
   - Team expertise

4. **Hybrid approach** is often best:
   - REST for public-facing APIs
   - SOAP for internal enterprise systems
   - Gradual migration as needed

---

## Further Reading

- [RESTful Web Services by Leonard Richardson](https://www.oreilly.com/library/view/restful-web-services/9780596809089/)
- [SOAP vs REST - MDN Web Docs](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types)
- [W3C SOAP Specification](https://www.w3.org/TR/soap/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Spring REST Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)
- [Spring Web Services](https://docs.spring.io/spring-ws/docs/current/reference/html/)

---

*Last updated: January 2024*
