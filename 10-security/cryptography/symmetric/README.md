# Symmetric Encryption

## Overview

Symmetric encryption uses the same key for both encryption and decryption. It's fast and suitable for encrypting large amounts of data.

## Algorithms

### AES (Advanced Encryption Standard)
```java
// AES-GCM (recommended)
public class AESGCMExample {
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;
    
    public static byte[] encrypt(byte[] data, SecretKey key) {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        
        byte[] encrypted = cipher.doFinal(data);
        return ByteBuffer.allocate(iv.length + encrypted.length)
            .put(iv)
            .put(encrypted)
            .array();
    }
    
    public static byte[] decrypt(byte[] encrypted, SecretKey key) {
        ByteBuffer buffer = ByteBuffer.wrap(encrypted);
        byte[] iv = new byte[IV_SIZE];
        buffer.get(iv);
        
        byte[] ciphertext = new byte[buffer.remaining()];
        buffer.get(ciphertext);
        
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        
        return cipher.doFinal(ciphertext);
    }
}
```

### DES (Data Encryption Standard)
```java
// DES - Deprecated, use AES instead
SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
DESKeySpec spec = new DESKeySpec(keyBytes);
SecretKey key = factory.generateSecret(spec);

Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key);
```

### 3DES (Triple DES)
```java
// 3DES - Legacy systems only
SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
SecretKey key = factory.generateSecret(spec);

Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
```

## Key Management

### Key Generation
```java
// AES-256 key
KeyGenerator keyGen = KeyGenerator.getInstance("AES");
keyGen.init(256);
SecretKey key = keyGen.generateKey();

// From password
PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
```

### Key Storage
```java
// Store in environment variable
String keyBase64 = System.getenv("ENCRYPTION_KEY");
byte[] keyBytes = Base64.getDecoder().decode(keyBase64);

// Store in HSM
KeyStore ks = KeyStore.getInstance("PKCS11");
ks.load(null, pin.toCharArray());
SecretKey key = (SecretKey) ks.getKey("alias", pin.toCharArray());
```

## Best Practices

1. Use AES-256-GCM for new applications
2. Never use DES or 3DES for new code
3. Use unique IVs for each encryption
4. Implement proper key rotation
5. Store keys securely (HSM, vault)
6. Use authenticated encryption (GCM)
7. Validate padding and MAC
8. Handle key material securely in memory
