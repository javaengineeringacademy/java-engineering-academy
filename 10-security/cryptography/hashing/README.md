# Cryptographic Hashing

## Overview

Hashing transforms data into a fixed-size string, providing data integrity verification and password storage.

## SHA-256

```java
// Basic SHA-256
MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(data);
String hexHash = Hex.encodeHexString(hash);

// With盐值
byte[] salt = new byte[16];
SecureRandom.getInstanceStrong().nextBytes(salt);
digest.update(salt);
byte[] hash = digest.digest(password.getBytes());
```

## bcrypt

```java
// Hash password
String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

// Verify password
if (BCrypt.checkpw(password, hashed)) {
    // Password matches
}
```

## PBKDF2

```java
// Generate key
PBEKeySpec spec = new PBEKeySpec(
    password.toCharArray(),
    salt,
    65536,  // iterations
    256     // key length
);
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
byte[] key = factory.generateSecret(spec).getEncoded();

// Verify
PBEKeySpec verifySpec = new PBEKeySpec(
    password.toCharArray(),
    salt,
    65536,
    256
);
byte[] verifyKey = factory.generateSecret(verifySpec).getEncoded();
boolean matches = MessageDigest.isEqual(key, verifyKey);
```

## Comparison

| Algorithm | Speed | Security | Use Case |
|-----------|-------|----------|----------|
| MD5 | Fast | Weak | Checksums only |
| SHA-1 | Fast | Weak | Legacy systems |
| SHA-256 | Medium | Strong | Data integrity |
| bcrypt | Slow | Very strong | Passwords |
| PBKDF2 | Configurable | Strong | Passwords |
| Argon2 | Slow | Very strong | Passwords (winner) |

## Best Practices

1. Use SHA-256 for data integrity
2. Use bcrypt/Argon2 for passwords
3. Never use MD5/SHA-1 for security
4. Add salt to password hashes
5. Use sufficient iterations/work factor
6. Use constant-time comparison
7. Hash before encryption when possible
8. Use HMAC for message authentication
