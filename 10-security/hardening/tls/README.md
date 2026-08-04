# TLS Configuration

## Overview

Transport Layer Security (TLS) secures communications by encrypting data in transit between clients and servers.

## TLS 1.3 vs 1.2

| Feature | TLS 1.2 | TLS 1.3 |
|---------|---------|---------|
| Handshake | 2-RTT | 1-RTT |
| Key Exchange | RSA, DHE, ECDHE | ECDHE, DHE only |
| Cipher Suites | Many | 5 only |
| Forward Secrecy | Optional | Mandatory |

## Configuration

### Spring Boot
```yaml
server:
  ssl:
    enabled: true
    protocol: TLS
    enabled-protocols: TLSv1.3,TLSv1.2
    ciphers: TLS_AES_256_GCM_SHA384,TLS_CHACHA20_POLY1305_SHA256
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
    key-store-type: PKCS12
```

### Nginx
```nginx
ssl_protocols TLSv1.2 TLSv1.3;
ssl_ciphers ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305;
ssl_prefer_server_ciphers off;
ssl_session_timeout 1d;
ssl_session_cache shared:SSL:10m;
ssl_session_tickets off;

# OCSP Stapling
ssl_stapling on;
ssl_stapling_verify on;
```

## Certificate Management

### Let's Encrypt
```bash
# Install certbot
sudo apt install certbot python3-certbot-nginx

# Obtain certificate
sudo certbot --nginx -d example.com -d www.example.com

# Auto-renewal
sudo certbot renew --dry-run
```

### Certificate Pinning
```java
// Pin specific certificate
CertificateFactory cf = CertificateFactory.getInstance("X.509");
Certificate cert = cf.generateCertificate(certInputStream);

KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null, null);
ks.setCertificateEntry("pin", cert);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);
```

## Cipher Suites

### Recommended
```
TLS_AES_256_GCM_SHA384
TLS_CHACHA20_POLY1305_SHA256
TLS_AES_128_GCM_SHA256
ECDHE-ECDSA-AES256-GCM-SHA384
ECDHE-RSA-AES256-GCM-SHA384
```

### Deprecated (Disable)
```
RC4
DES
3DES
MD5
NULL
EXPORT
```

## Testing

```bash
# Test SSL configuration
nmap --script ssl-enum-ciphers -p 443 example.com

# SSL Labs test
# https://www.ssllabs.com/ssltest/
```

## Best Practices

1. Use TLS 1.3 when possible
2. Disable weak cipher suites
3. Enable HSTS
4. Use strong key sizes
5. Implement certificate rotation
6. Monitor certificate expiration
7. Use OCSP stapling
8. Implement certificate pinning
