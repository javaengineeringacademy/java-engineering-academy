# Java Keystores

## Overview

Java keystores store cryptographic keys and certificates for SSL/TLS, code signing, and authentication.

## Keystore Types

| Type | Extension | Description |
|------|-----------|-------------|
| JKS | .jks | Java KeyStore (legacy) |
| JCEKS | .jceks | Java Cryptography Extension KeyStore |
| PKCS12 | .p12/.pfx | Public-Key Cryptography Standards |
| BKS | .bks | Bouncy Castle KeyStore |

## Key Operations

### Generate Keystore
```bash
# Generate PKCS12 keystore
keytool -genkeypair \
  -alias server \
  -keyalg RSA \
  -keysize 2048 \
  -validity 365 \
  -keystore keystore.p12 \
  -storetype PKCS12 \
  -storepass changeit \
  -dname "CN=localhost"
```

### Import Certificate
```bash
# Import trusted certificate
keytool -importcert \
  -alias root-ca \
  -file root-ca.crt \
  -keystore truststore.p12 \
  -storepass changeit
```

### List Keystore
```bash
keytool -list -keystore keystore.p12 -storepass changeit
```

## Spring Boot Configuration

```yaml
server:
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: ${KEYSTORE_PASSWORD}
    key-store-type: PKCS12
    trust-store: classpath:truststore.p12
    trust-store-password: ${TRUSTSTORE_PASSWORD}
```

## Programmatic Access

```java
// Load keystore
KeyStore ks = KeyStore.getInstance("PKCS12");
try (InputStream is = new FileInputStream("keystore.p12")) {
    ks.load(is, password.toCharArray());
}

// Get private key
PrivateKey key = (PrivateKey) ks.getKey("alias", password.toCharArray());

// Get certificate
Certificate cert = ks.getCertificate("alias");

// Load truststore
KeyStore trustStore = KeyStore.getInstance("PKCS12");
try (InputStream is = new FileInputStream("truststore.p12")) {
    trustStore.load(is, password.toCharArray());
}
```

## Best Practices

1. Use PKCS12 format (industry standard)
2. Use strong passwords for keystores
3. Store keystores securely (not in source control)
4. Rotate certificates regularly
5. Use separate keystores for different purposes
6. Monitor certificate expiration
7. Use hardware security modules for production
8. Back up keystores securely
