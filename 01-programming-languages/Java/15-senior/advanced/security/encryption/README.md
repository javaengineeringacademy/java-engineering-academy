# Encryption Concepts

## Symmetric vs Asymmetric

### Symmetric (AES)
- Same key encrypt and decrypt
- Faster performance
- Key distribution problem
- Good for data at rest

### Asymmetric (RSA)
- Public/private key pair
- Slower performance
- Solves key distribution
- Good for key exchange

## When to Use Which
- **AES**: Large data, file encryption, database fields
- **RSA**: Small data, key exchange, digital signatures
- **Hybrid**: AES for data, RSA for key exchange

## Hashing (SHA-256)
- One-way transformation
- No decryption possible
- Use for passwords with salt
- Verify data integrity

## Key Management
- Never hardcode keys
- Use key vaults (AWS KMS, HashiCorp Vault)
- Rotate keys regularly
- Separate keys per environment
- Use hardware security modules (HSM)

## Security Best Practices
- Use established libraries (Bouncy Castle, Jasypt)
- Always use IV for block ciphers
- Validate all inputs
- Use authenticated encryption (GCM)
- Implement proper key derivation (PBKDF2)

## Common Mistakes
- ECB mode (no IV, patterns visible)
- Static IVs
- Weak key generation
- Not validating ciphertext integrity
- Storing keys with encrypted data

## Interview Questions

1. **Explain the difference between AES-GCM and AES-CBC. When would you use each?**
   AES-CBC: each block depends on previous block (sequential), requires padding, no built-in authentication — use with HMAC for integrity. AES-GCM: combines encryption + authentication (AEAD), parallelizable, no padding needed, includes integrity tag. Use GCM for new applications (TLS 1.3, most modern protocols). CBC only for backward compatibility with legacy systems.

2. **How does key derivation work and why not use passwords directly as keys?**
   Passwords have low entropy (bits of randomness). A 8-character password might have ~40 bits of entropy, but AES-256 needs 256 bits. Key Derivation Functions (PBKDF2, bcrypt, Argon2) stretch a password by: (1) Hashing it thousands/millions of times; (2) Adding a random salt; (3) Producing a fixed-length key. PBKDF2: 600,000+ iterations recommended by OWASP. Argon2: memory-hard, resists GPU attacks.

3. **What is the difference between signing and encrypting?**
   Encryption: plaintext → ciphertext using recipient's public key → only recipient can decrypt with private key. Signing: hash of data → signature using signer's private key → anyone can verify with public key. Signing proves authenticity and integrity, not confidentiality. Use both when you need authenticated, non-repudiable communication.

4. **Why should you never use ECB mode for file encryption?**
   ECB (Electronic Codebook) encrypts each block independently with the same key. Identical plaintext blocks produce identical ciphertext blocks, revealing patterns. For images, the encrypted file still shows the original轮廓. Use CBC, GCM, or CTR modes instead, which use initialization vectors (IVs) to ensure identical plaintexts produce different ciphertexts.

5. **How do you securely store encryption keys in a Java application?**
   Options: (1) AWS KMS / Azure Key Vault / GCP KMS — managed key services; (2) HashiCorp Vault — self-hosted secret management; (3) Environment variables — simple but less secure; (4) Java KeyStore (JKS) for symmetric keys; (5) Never hardcode in source code, never commit to git. Best practice: use cloud KMS with automatic key rotation every 90 days.

6. **Compare AES, ChaCha20, and RSA for different use cases.**
   AES: symmetric, fast (hardware AES-NI), good for bulk data. ChaCha20: symmetric, software-optimized, good for mobile/embedded without AES hardware. RSA: asymmetric, slow (1000x slower than AES), used for key exchange and signatures, not for bulk encryption. Use hybrid: RSA/ECDSA for key exchange, AES-GCM/ChaCha20-Poly1305 for data encryption.

## Performance

### Encryption Speed (on modern CPU with AES-NI)
| Algorithm | Speed | Use Case |
|-----------|-------|----------|
| AES-128-GCM | ~5 GB/s | Bulk data encryption |
| AES-256-GCM | ~3.5 GB/s | High-security data |
| ChaCha20-Poly1305 | ~2 GB/s | Mobile, no AES hardware |
| RSA-2048 sign | ~1,500 ops/s | Digital signatures |
| RSA-2048 verify | ~40,000 ops/s | Signature verification |
| ECDSA P-256 sign | ~30,000 ops/s | Fast signatures |

### Key Derivation Time
| Algorithm | Iterations | Time (ms) |
|-----------|------------|-----------|
| PBKDF2-SHA256 | 600,000 | ~200ms |
| bcrypt | 12 rounds | ~250ms |
| Argon2id | t=3, m=64MB | ~100ms |

## Examples

```java
// AES-GCM encryption/decryption
public class AesGcmExample {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;

    public static byte[] encrypt(byte[] plaintext, SecretKey key) {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        
        byte[] ciphertext = cipher.doFinal(plaintext);
        // Prepend IV to ciphertext
        byte[] result = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(ciphertext, 0, result, iv.length, ciphertext.length);
        return result;
    }
}

// Key generation and storage
KeyStore ks = KeyStore.getInstance("PKCS12");
ks.load(null, password.toCharArray());
SecretKey key = KeyGenerator.getInstance("AES").generateKey();
KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
ks.setEntry("my-key", entry, new KeyStore.PasswordProtection(password.toCharArray()));
try (FileOutputStream fos = new FileOutputStream("keystore.p12")) {
    ks.store(fos, password.toCharArray());
}

// Password hashing with PBKDF2
public String hashPassword(String password) {
    byte[] salt = new byte[16];
    SecureRandom.getInstanceStrong().nextBytes(salt);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 600000, 256);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    byte[] hash = factory.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
}

// Digital signature
KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
keyGen.initialize(new ECGenParameterSpec("secp256r1"));
KeyPair pair = keyGen.generateKeyPair();

Signature sig = Signature.getInstance("SHA256withECDSA");
sig.initSign(pair.getPrivate());
sig.update(data);
byte[] signature = sig.sign();

sig.initVerify(pair.getPublic());
sig.update(data);
boolean verified = sig.verify(signature);
```

## Internal Working

### AES Block Cipher Operation
1. 128-bit plaintext block enters AES
2. Initial Round Key Addition (XOR with round key)
3. 10/12/14 rounds (for AES-128/192/256): SubBytes → ShiftRows → MixColumns → AddRoundKey
4. Final round (no MixColumns): SubBytes → ShiftRows → AddRoundKey
5. 128-bit ciphertext block output

### GCM (Galois/Counter Mode)
1. Counter mode generates keystream: nonce + counter → AES encrypt → keystream
2. Plaintext XORed with keystream → ciphertext
3. GHASH function computes authentication tag over AAD + ciphertext
4. Tag appended to ciphertext for integrity verification
5. On decryption: recompute tag, compare with received tag

### Key Exchange Flow (ECDHE)
1. Both parties generate ephemeral EC key pairs
2. Exchange public keys over insecure channel
3. Both compute shared secret using ECDH: `shared = privateKey * publicKey`
4. Shared secret fed into KDF to derive session keys
5. Forward secrecy: ephemeral keys destroyed after session

## Why This Concept Exists

Encryption solves three fundamental security problems: (1) Confidentiality — prevents unauthorized reading of data; (2) Integrity — detects tampering; (3) Authentication — verifies sender identity. Without encryption: network traffic is readable (MITM attacks), stored data is accessible to anyone with file access, and there's no way to verify data hasn't been modified. Modern applications require encryption for GDPR, HIPAA, PCI-DSS compliance.

## Overview

Encryption transforms plaintext into ciphertext using mathematical algorithms and keys. Symmetric encryption (AES) uses the same key for encrypt/decrypt — fast but requires secure key distribution. Asymmetric encryption (RSA, ECC) uses public/private key pairs — solves key distribution but is 1000x slower. Hashing (SHA-256) creates one-way fingerprints for integrity. In Java, the `javax.crypto` package provides AES, DES, and other ciphers; `java.security` provides key generation, signatures, and certificate management.

## Pitfalls

```java
// PITFALL 1: Using ECB mode
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Patterns visible!

// PITFALL 2: Reusing IV/nonce with same key
// Two messages encrypted with same key+IV → XOR reveals plaintext
// Fix: Always generate random IV per encryption

// PITFALL 3: Hardcoded encryption keys
private static final String KEY = "my-secret-key-123"; // In source code!

// PITFALL 4: Not validating ciphertext integrity (no MAC)
// Attacker can modify ciphertext without detection
// Fix: Use authenticated encryption (GCM, Poly1305)

// PITFALL 5: Using MD5 or SHA-1 for password hashing
MessageDigest md = MessageDigest.getInstance("MD5"); // Broken!

// PITFALL 6: Encrypting with RSA directly for large data
// RSA can only encrypt data smaller than key size
// Fix: Use hybrid encryption (RSA for AES key, AES for data)

// PITFALL 7: Catching and ignoring crypto exceptions
try { cipher.init(...); } catch (Exception e) { } // Silently insecure!
```

## References

- [Java Cryptography Architecture](https://docs.oracle.com/en/java/javase/17/security/java-cryptography-architecture-jca-reference-guide.html)
- [OWASP Cryptographic Failures](https://owasp.org/Top10/A02_2021-Cryptographic_Failures/)
- [NIST SP 800-38D (GCM)](https://csrc.nist.gov/publications/detail/sp/800-38d/final)
- "Serious Cryptography" by Jean-Philippe Aumasson
