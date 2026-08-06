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

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
