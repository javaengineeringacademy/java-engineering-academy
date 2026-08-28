# SSL/TLS Concepts

## What is TLS
- Transport Layer Security protocol
- Encrypts communication between client/server
- Ensures confidentiality and integrity
- Successor to SSL (SSL 3.0 is deprecated)

## TLS Handshake
1. Client Hello (supported ciphers)
2. Server Hello (chosen cipher)
3. Certificate exchange
4. Key exchange
5. Finished message

## Certificate Management
- **Self-signed**: Development/testing only
- **CA-signed**: Production use
- **Let's Encrypt**: Free certificates
- **Certificate pinning**: Additional security

## Java SSL Configuration
- `SSLContext`: Main entry point
- `TrustManager`: Validates certificates
- `KeyManager`: Handles client certificates
- `SSLSocketFactory`: Creates secure connections

## Common Issues
- Certificate not trusted (missing CA)
- Hostname verification failure
- Expired certificates
- Weak cipher suites
- Protocol version mismatch

## Best Practices
- Use TLS 1.2 or higher
- Disable weak ciphers
- Implement certificate pinning
- Monitor certificate expiration
- Use proper trust stores
- Verify hostnames

## Production Checklist
- Valid certificates from trusted CA
- Strong cipher suites only
- Proper timeout configuration
- Error handling and logging
- Regular security audits

## Interview Questions

1. **Explain the TLS 1.3 handshake vs TLS 1.2. What improved?**
   TLS 1.2: 2 round trips (ClientHello → ServerHello/Certificate → Key Exchange → Finished). TLS 1.3: 1 round trip (ClientHello with key shares → ServerHello/Certificate/Finished). TLS 1.3 removes insecure ciphers (RC4, 3DES, RSA key exchange), mandates forward secrecy, and encrypts more of the handshake. Result: faster connection setup + better security.

2. **What is certificate pinning and when should you use it?**
   Certificate pinning embeds the expected server certificate or public key in the client application. Even if a CA is compromised and issues a fraudulent certificate, the pinned client rejects it. Use for: high-security apps (banking, healthcare), mobile apps communicating with specific servers. Don't pin for: general web browsing, services that change certificates frequently. Risk: pinned certificate expiry requires app update.

3. **How do you configure Java applications to use TLS 1.3?**
   ```java
   SSLContext ctx = SSLContext.getInstance("TLSv1.3");
   ```
   Or via system properties: `-Djdk.tls.client.protocols=TLSv1.3`. Verify with: `java -Djavax.net.debug=ssl,handshake MyApp`. Java 11+ supports TLS 1.3. Disable older versions: `-Djdk.tls.client.disabledProtocols=TLSv1,TLSv1.1,TLSv1.2`.

4. **What happens during certificate validation in Java?**
   Java's `TrustManager` validates: (1) Certificate chain to a trusted CA in the truststore; (2) Certificate not expired (`notBefore`/`notAfter`); (3) Certificate not revoked (CRL/OCSP); (4) Hostname matches certificate SAN/CN. Default `PKIXTrustManager` performs all checks. Custom `TrustManager` can override — but this weakens security.

5. **How do you handle SSL in microservices with mutual TLS (mTLS)?**
   Both client and server present certificates. Server validates client cert, client validates server cert. Implementation: (1) Generate CA-signed certs for each service; (2) Configure both `KeyManager` (client cert) and `TrustManager` (server cert); (3) Use service mesh (Istio, Linkerd) for automatic mTLS; (4) Certificates auto-rotated via cert-manager. Benefit: zero-trust networking without application changes.

6. **What are the performance implications of TLS?**
   TLS adds: (1) Handshake latency: 1-2 round trips + crypto operations (~50-100ms for first connection); (2) Per-record overhead: 5-20 bytes per encrypted message; (3) CPU overhead: ~5-15% for encryption/decryption. Mitigations: TLS session resumption (0-RTT), TLS 1.3 (faster handshake), hardware AES-NI acceleration, connection keep-alive.

## Performance

### TLS Handshake Latency
| Protocol | Round Trips | Time (100ms RTT) |
|----------|-------------|-------------------|
| TLS 1.2 | 2 RTT | ~200ms |
| TLS 1.3 | 1 RTT | ~100ms |
| TLS 1.3 + 0-RTT | 0 RTT | ~0ms |

### Encryption Overhead
| Cipher | Speed | CPU Impact |
|--------|-------|------------|
| AES-128-GCM | 5 GB/s | ~3% |
| AES-256-GCM | 3.5 GB/s | ~5% |
| ChaCha20-Poly1305 | 2 GB/s | ~8% |

### Connection Reuse Impact
```
New TLS connection:    100-200ms overhead
Reused connection:      0ms overhead
Session resumption:    20-50ms overhead
```

## Examples

```java
// Custom SSLContext with specific protocols
SSLContext ctx = SSLContext.getInstance("TLSv1.3");
ctx.init(keyManagers, trustManagers, new SecureRandom());

HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setSSLSocketFactory(ctx.getSocketFactory());
conn.setHostnameVerifier((hostname, session) -> {
    return hostname.equals("api.example.com");
});

// Programmatic certificate validation
TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
KeyStore trustStore = KeyStore.getInstance("JKS");
try (InputStream is = Files.newInputStream(Paths.get("truststore.jks"))) {
    trustStore.load(is, "changeit".toCharArray());
}
tmf.init(trustStore);

// HTTP client with TLS
HttpClient client = HttpClient.newBuilder()
    .sslContext(ctx)
    .build();
HttpResponse<String> response = client.send(
    HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/data"))
        .build(),
    HttpResponse.BodyHandlers.ofString()
);

// Self-signed cert for development
KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(2048);
KeyPair kp = kpg.generateKeyPair();
Certificate cert = CertGenerator.generateSelfSigned(kp, "localhost");
```

## Internal Working

### TLS 1.3 Handshake
```
Client                              Server
  |                                    |
  |--- ClientHello (key shares) ----->|
  |    - Supported cipher suites       |
  |    - Key share for preferred group |
  |    - SNI (server name)             |
  |                                    |
  |<--- ServerHello (key share) ------|
  |    - Selected cipher suite         |
  |    - Key share for server          |
  |<--- {EncryptedExtensions} --------|
  |<--- {Certificate} ----------------|
  |<--- {CertificateVerify} ----------|
  |<--- {Finished} -------------------|
  |                                    |
  |--- {Finished} ------------------->|
  |                                    |
  |<-------- Application Data -------->|
```

### Certificate Chain Validation
```
Leaf Certificate → Intermediate CA → Root CA (in truststore)
     ↓                    ↓                ↓
  Signature           Signature         Self-signed
  verified            verified          (trusted root)
```

### Java SSL Architecture
```
Application
    ↓
SSLSocketFactory / SSLEngine
    ↓
SSLContext
    ├── KeyManager[] (client certs)
    ├── TrustManager[] (trusted CAs)
    └── SecureRandom
    ↓
JSSE (Java Secure Socket Extension)
    ↓
OS Network Layer
```

## Why This Concept Exists

SSL/TLS exists because network communication is inherently insecure. Without encryption: (1) Anyone on the network can read your traffic (eavesdropping); (2) Attackers can modify data in transit (tampering); (3) Servers can be impersonated (MITM attacks). TLS provides: Confidentiality (encryption), Integrity (MAC/hashing), Authentication (certificates). Every HTTPS connection, API call, and secure email uses TLS. It's the foundation of internet security.

## Overview

TLS (Transport Layer Security) is a cryptographic protocol that secures communication over networks. It provides encrypted, authenticated, and integrity-protected data channels between client and server. TLS uses a combination of asymmetric cryptography (for key exchange and authentication) and symmetric cryptography (for data encryption). In Java, `SSLContext`, `SSLSocketFactory`, and `TrustManager` provide programmatic control over TLS configuration.

## Pitfalls

```java
// PITFALL 1: Disabling certificate validation
TrustManager[] trustAll = new TrustManager[]{
    new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String auth) {}
        public void checkServerTrusted(X509Certificate[] chain, String auth) {}
        public X509Certificate[] getAcceptedIssuers() { return null; }
    }
};
// NEVER do this in production!

// PITFALL 2: Using deprecated protocols
ctx.init(km, tm, null);
// Default may include SSLv3, TLSv1 — insecure!
// Fix: -Djdk.tls.client.protocols=TLSv1.2,TLSv1.3

// PITFALL 3: Not verifying hostname
conn.setHostnameVerifier((h, s) -> true); // Accepts any hostname!

// PITFALL 4: Weak cipher suites
// Using RC4, 3DES, or export ciphers
// Fix: Restrict to strong ciphers only

// PITFALL 5: Self-signed certificates in production
// Clients can't verify server identity → MITM vulnerability

// PITFALL 6: Not monitoring certificate expiration
// Expired cert → application outage
// Fix: Set calendar reminders, use cert monitoring tools
```

## References

- [Java Secure Socket Extension (JSSE)](https://docs.oracle.com/en/java/javase/17/security/java-secure-socket-extension-jsse-reference-guide.html)
- [RFC 8446 (TLS 1.3)](https://tools.ietf.org/html/rfc8446)
- [Mozilla SSL Configuration Generator](https://ssl-config.mozilla.org/)
- [SSL Labs Server Test](https://www.ssllabs.com/ssltest/)
