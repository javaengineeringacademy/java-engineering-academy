# Asymmetric Encryption

## Overview

Asymmetric encryption uses a key pair (public and private) for encryption and decryption, enabling secure key exchange and digital signatures.

## RSA

### Key Generation
```java
KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
generator.initialize(2048);
KeyPair keyPair = generator.generateKeyPair();

PublicKey publicKey = keyPair.getPublic();
PrivateKey privateKey = keyPair.getPrivate();
```

### Encryption/Decryption
```java
// Encrypt with public key
Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encrypted = cipher.doFinal(plaintext);

// Decrypt with private key
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] decrypted = cipher.doFinal(encrypted);
```

### Digital Signatures
```java
// Sign
Signature signature = Signature.getInstance("SHA256withRSA");
signature.initSign(privateKey);
signature.update(data);
byte[] sig = signature.sign();

// Verify
signature.initVerify(publicKey);
signature.update(data);
boolean valid = signature.verify(sig);
```

## ECC (Elliptic Curve Cryptography)

### Key Generation
```java
KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
generator.initialize(new ECGenParameterSpec("secp256r1"));
KeyPair keyPair = generator.generateKeyPair();
```

### ECDH Key Exchange
```java
// Generate shared secret
KeyAgreement ka = KeyAgreement.getInstance("ECDH");
ka.init(privateKey);
ka.doPhase(otherPublicKey, true);
byte[] sharedSecret = ka.generateSecret();
```

## Key Exchange Protocols

### Diffie-Hellman
```java
KeyPairGenerator generator = KeyPairGenerator.getInstance("DH");
generator.initialize(2048);
KeyPair keyPair = generator.generateKeyPair();

KeyAgreement ka = KeyAgreement.getInstance("DH");
ka.init(keyPair.getPrivate());
ka.doPhase(otherPublicKey, true);
byte[] sharedSecret = ka.generateSecret();
```

## Best Practices

1. Use RSA-2048 minimum (RSA-4096 preferred)
2. Use ECC for better performance
3. Use OAEP padding for RSA encryption
4. Validate certificates and keys
5. Implement proper key rotation
6. Use secure key exchange (ECDH)
7. Verify signatures before trust
8. Store private keys securely
