# REST vs SOAP

## What They Are

### REST (Representational State Transfer)
An architectural style for designing networked applications. Uses standard HTTP methods, stateless communication, and resource-based URLs. Relies on existing web standards and conventions.

### SOAP (Simple Object Access Protocol)
A protocol for exchanging structured information in web services. Uses XML exclusively, supports multiple transport protocols (HTTP, SMTP, TCP), and includes built-in standards for security, reliability, and transactions.

## Key Difference Table

| Feature | REST | SOAP |
|---------|------|------|
| Type | Architectural Style | Protocol |
| Data Format | JSON, XML, others | XML only |
| Transport | HTTP/HTTPS | HTTP, SMTP, TCP |
| State | Stateless | Can be stateful |
| Standards | Few, flexible | WS-* stack |
| Caching | Built-in HTTP caching | No caching support |
| Security | OAuth, JWT, HTTPS | WS-Security |
| Error Handling | HTTP status codes | SOAP Fault |
| Discoverability | OpenAPI/Swagger | WSDL |
| Performance | Lightweight | Heavyweight |

## When to Use Which

### Use REST When
- Building public APIs
- Mobile applications need lightweight responses
- Caching is important
- Simple CRUD operations
- Web applications and services
- Team is familiar with HTTP and JSON

### Use SOAP When
- Enterprise security requirements (banking, healthcare)
- Built-in reliability (WS-ReliableMessaging)
- Transaction support (WS-AtomicTransaction)
- Formal contracts required (WSDL)
- Legacy system integration
- Transport flexibility needed (not just HTTP)

## Interview Trap

**Trap**: "REST is always better than SOAP."

**Reality**: REST is simpler and more performant for most use cases, but SOAP provides built-in standards for security, reliability, and transactions that REST lacks. Enterprise environments with strict compliance requirements may need SOAP.

**Follow-up Trap**: "REST is always JSON."

**Reality**: REST is an architectural style that supports multiple data formats. While JSON is most common, REST APIs can use XML, YAML, or any other format. The key is the architectural constraints, not the data format.

## Visual Diagram

```
REST Request:
┌─────────────────────────────────────────────┐
│ GET /api/users/123 HTTP/1.1                 │
│ Host: example.com                           │
│ Accept: application/json                    │
└─────────────────────────────────────────────┘
                    │
                    v
┌─────────────────────────────────────────────┐
│ HTTP Response                               │
│ 200 OK                                      │
│ Content-Type: application/json              │
│                                             │
│ {                                           │
│   "id": 123,                                │
│   "name": "John Doe",                       │
│   "email": "john@example.com"              │
│ }                                           │
└─────────────────────────────────────────────┘

SOAP Request:
┌─────────────────────────────────────────────┐
│ POST /UserService HTTP/1.1                  │
│ Content-Type: text/xml; charset=utf-8       │
│ SOAPAction: "GetUser"                       │
│                                             │
│ <?xml version="1.0" encoding="UTF-8"?>     │
│ <soap:Envelope                             │
│   xmlns:soap="http://schemas.xmlsoap.org/   │
│   soap/envelope/">                          │
│   <soap:Body>                               │
│     <GetUser xmlns="http://example.com">    │
│       <UserId>123</UserId>                  │
│     </GetUser>                              │
│   </soap:Body>                              │
│ </soap:Envelope>                            │
└─────────────────────────────────────────────┘
                    │
                    v
┌─────────────────────────────────────────────┐
│ SOAP Response                               │
│ 200 OK                                      │
│ Content-Type: text/xml; charset=utf-8       │
│                                             │
│ <?xml version="1.0" encoding="UTF-8"?>     │
│ <soap:Envelope                             │
│   xmlns:soap="http://schemas.xmlsoap.org/   │
│   soap/envelope/">                          │
│   <soap:Body>                               │
│     <GetUserResponse                        │
│       xmlns="http://example.com">           │
│       <User>                                │
│         <UserId>123</UserId>                │
│         <Name>John Doe</Name>               │
│         <Email>john@example.com</Email>     │
│       </User>                               │
│     </GetUserResponse>                      │
│   </soap:Body>                              │
│ </soap:Envelope>                            │
└─────────────────────────────────────────────┘
```

## WS-* Standards

SOAP has a rich ecosystem of standards:
- **WS-Security**: Message-level encryption and authentication
- **WS-ReliableMessaging**: Guaranteed message delivery
- **WS-AtomicTransaction**: Distributed transactions
- **WS-Addressing**: Transport-independent messaging
- **WS-Policy**: Service capabilities and requirements

## Performance Comparison

| Metric | REST | SOAP |
|--------|------|------|
| Payload size | Small (JSON) | Large (XML envelope) |
| Parsing speed | Fast | Slow |
| Bandwidth usage | Low | High |
| Processing overhead | Minimal | Significant |

## Key Insight

REST and SOAP are not competitors; they serve different needs:

**REST**: Simplicity, performance, web-native
**SOAP**: Security, reliability, enterprise standards

Modern applications often use:
- REST for public APIs and mobile backends
- SOAP for internal enterprise integrations
- GraphQL as an alternative to both for complex data requirements
