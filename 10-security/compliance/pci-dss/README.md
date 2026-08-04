# PCI DSS Compliance

## Overview

Payment Card Industry Data Security Standard (PCI DSS) applies to organizations that handle credit card data.

## Requirements

1. Install and maintain network security controls
2. Apply secure configurations
3. Protect stored account data
4. Protect data in transit with encryption
5. Protect against malicious software
6. Develop secure systems
7. Restrict access by need-to-know
8. Identify users and authenticate access
9. Restrict physical access
10. Log and monitor access
11. Test security regularly
12. Support information security

## Implementation

### Tokenization
```java
@Service
public class TokenizationService {
    
    public String tokenize(String cardNumber) {
        // Replace PAN with token
        String token = UUID.randomUUID().toString();
        tokenRepository.save(token, cardNumber);
        return token;
    }
    
    public String detokenize(String token) {
        // Get PAN from token (restricted access)
        return tokenRepository.find(token);
    }
}
```

### Encryption
```java
@Component
public class CardDataEncryption {
    
    public EncryptedData encrypt(String pan) {
        // Use AES-256
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
        
        byte[] encrypted = cipher.doFinal(pan.getBytes());
        return new EncryptedData(encrypted, iv);
    }
}
```

### Access Control
```java
@Service
public class PCIAccessControl {
    
    @PreAuthorize("hasRole('PCI_USER')")
    public CardData getCardData(String cardId) {
        // Log access
        auditService.log("CARD_ACCESS", cardId, getCurrentUser());
        return cardRepository.findById(cardId);
    }
}
```

### Network Segmentation
```yaml
# Network policies for cardholder data environment
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: cde-network-policy
spec:
  podSelector:
    matchLabels:
      tier: cde
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          tier: app
```

## Best Practices

1. Implement tokenization for card data
2. Encrypt card data at rest and in transit
3. Use network segmentation
4. Implement strong access control
5. Log all access to card data
6. Regular security testing
7. Maintain vulnerability management
8. Incident response procedures
