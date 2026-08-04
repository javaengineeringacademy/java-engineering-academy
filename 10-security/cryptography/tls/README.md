# TLS (Transport Layer Security)

## Overview

TLS provides encrypted communication between clients and servers, ensuring data confidentiality, integrity, and authentication.

## TLS 1.3 Features

- Simplified handshake (1-RTT)
- Removed legacy algorithms
- Encrypted handshake
- 0-RTT resumption
- Forward secrecy mandatory

## Configuration

### Spring Boot
```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: changeit
    key-store-type: PKCS12
    protocol: TLS
    enabled-protocols: TLSv1.3,TLSv1.2
```

### Nginx
```nginx
ssl_protocols TLSv1.2 TLSv1.3;
ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384;
ssl_prefer_server_ciphers off;
ssl_session_timeout 1d;
ssl_session_cache shared:SSL:50m;
```

## Certificate Management

### Self-Signed (Development)
```bash
# Generate key pair
openssl genrsa -out server.key 2048

# Generate CSR
openssl req -new -key server.key -out server.csr

# Generate certificate
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
```

### Let's Encrypt (Production)
```bash
# Install certbot
certbot certonly --standalone -d example.com

# Auto-renewal
certbot renew --dry-run
```

## Cipher Suites

### Recommended (TLS 1.3)
```
TLS_AES_256_GCM_SHA384
TLS_CHACHA20_POLY1305_SHA256
TLS_AES_128_GCM_SHA256
```

### Legacy (TLS 1.2)
```
ECDHE-ECDSA-AES256-GCM-SHA384
ECDHE-RSA-AES256-GCM-SHA384
ECDHE-ECDSA-AES128-GCM-SHA256
```

## Best Practices

1. Use TLS 1.3 when possible
2. Disable weak cipher suites
3. Enable HSTS
4. Use strong key sizes (RSA 2048+, ECDSA 256+)
5. Implement certificate pinning
6. Automate certificate renewal
7. Monitor certificate expiration
8. Use separate certificates per service
