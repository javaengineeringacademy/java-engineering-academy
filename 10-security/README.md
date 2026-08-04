# 10 - Security

## Overview

Application security encompasses protecting systems and data from unauthorized access, use, disclosure, disruption, modification, or destruction. This module covers security fundamentals, vulnerabilities, hardening, compliance, and testing.

## Security Domains

### 1. OWASP (Open Web Application Security Project)
Industry-standard security guidelines and vulnerability awareness.

- **OWASP Top 10** - Critical web application security risks
- **Cheat Sheets** - Security implementation guidance
- **Testing Guide** - Security testing methodology

### 2. Cryptography
Protecting data through encryption, hashing, and digital signatures.

- **Symmetric Encryption** - AES, DES, 3DES
- **Asymmetric Encryption** - RSA, ECC
- **Hashing** - SHA-256, bcrypt, PBKDF2
- **TLS** - Transport Layer Security
- **Keystores** - Java KeyStore and TrustStore

### 3. Authentication & Authorization
Verifying identity and controlling access.

- **JWT** - JSON Web Tokens
- **OAuth 2.0** - Authorization framework
- **OpenID Connect** - Identity layer on OAuth 2.0

### 4. Vulnerabilities
Common attack vectors and mitigations.

- **Injection** - SQL, NoSQL, command injection
- **XSS** - Cross-Site Scripting
- **CSRF** - Cross-Site Request Forgery
- **SSRF** - Server-Side Request Forgery
- **XXE** - XML External Entity
- **IDOR** - Insecure Direct Object References
- **Broken Authentication** - Session management flaws

### 5. Hardening
Reducing attack surface through configuration.

- **TLS Configuration** - Cipher suites, protocols
- **Security Headers** - CSP, HSTS, X-Frame-Options
- **CORS** - Cross-Origin Resource Sharing
- **Secrets Management** - Vault, environment variables

### 6. Compliance
Regulatory and standards compliance.

- **GDPR** - General Data Protection Regulation
- **HIPAA** - Health Insurance Portability and Accountability Act
- **PCI DSS** - Payment Card Industry Data Security Standard
- **SOC 2** - Service Organization Control 2

### 7. Static Analysis
Automated code security scanning.

- **SonarQube** - Code quality and security
- **SpotBugs** - Bug pattern detection
- **Checkstyle** - Code standards enforcement
- **Snyk** - Dependency vulnerability scanning

## Directory Structure

```
10-security/
├── owasp/
│   ├── owasp-top-10/
│   ├── cheat-sheets/
│   └── testing/
├── cryptography/
│   ├── symmetric/
│   ├── asymmetric/
│   ├── hashing/
│   ├── tls/
│   └── keystores/
├── jwt/
│   ├── fundamentals/
│   ├── implementation/
│   └── security/
├── oauth2/
│   ├── flows/
│   ├── implementation/
│   └── scopes/
├── oidc/
│   ├── fundamentals/
│   ├── sessions/
│   └── claims/
├── vulnerabilities/
│   ├── injection/
│   ├── xss/
│   ├── csrf/
│   ├── ssrf/
│   ├── xxe/
│   ├── idor/
│   └── broken-auth/
├── hardening/
│   ├── tls/
│   ├── headers/
│   ├── cors/
│   └── secrets/
├── compliance/
│   ├── gdpr/
│   ├── hipaa/
│   ├── pci-dss/
│   └── soc2/
└── static-analysis/
    ├── sonarqube/
    ├── spotbugs/
    ├── checkstyle/
    └── snyk/
```

## Security Principles

### CIA Triad
- **Confidentiality** - Prevent unauthorized access to data
- **Integrity** - Prevent unauthorized modification of data
- **Availability** - Ensure system is accessible when needed

### Defense in Depth
Apply multiple layers of security controls:
1. **Perimeter** - Firewalls, WAF
2. **Network** - Segmentation, encryption
3. **Host** - OS hardening, endpoint protection
4. **Application** - Input validation, authentication
5. **Data** - Encryption at rest, access controls

### Principle of Least Privilege
Grant minimum permissions required to perform a function.

### Secure by Default
Ship with the most restrictive configuration and require explicit opt-in for less secure options.

## Security Checklist

| Area | Control | Priority |
|------|---------|----------|
| Authentication | Multi-factor authentication | High |
| Authorization | Role-based access control | High |
| Data | Encryption at rest and in transit | High |
| Input | Validation and sanitization | High |
| Output | Encoding to prevent XSS | High |
| Dependencies | Vulnerability scanning | Medium |
| Secrets | Externalized secret management | High |
| Logging | Security event audit trails | Medium |
| Headers | Security headers configured | Medium |
| TLS | Strong cipher suites only | High |

## Common Security Anti-Patterns

1. **Security by Obscurity** - Relying on hidden implementation details
2. **Rolling Your Own Crypto** - Custom cryptographic implementations
3. **Hardcoded Secrets** - Passwords and keys in source code
4. **Overly Permissive CORS** - `Access-Control-Allow-Origin: *`
5. **Missing Rate Limiting** - No brute-force protection
6. **Verbose Error Messages** - Exposing stack traces to users
7. **SQL String Concatenation** - Building queries without parameterization
8. **Client-Side Only Validation** - Trusting client-side checks

## Security Testing Types

| Type | Description | Tools |
|------|-------------|-------|
| SAST | Static Application Security Testing | SonarQube, SpotBugs |
| DAST | Dynamic Application Security Testing | OWASP ZAP |
| SCA | Software Composition Analysis | Snyk, Dependency-Check |
| IAST | Interactive Application Security Testing | Contrast Security |
| Pen Testing | Manual penetration testing | Burp Suite |

## References

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [NIST Cybersecurity Framework](https://www.nist.gov/cyberframework)
- [Java Security Guidelines](https://www.oracle.com/java/technologies/javase/seccodeguide.html)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
