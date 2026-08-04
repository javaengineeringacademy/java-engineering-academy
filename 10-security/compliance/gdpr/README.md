# GDPR Compliance

## Overview

The General Data Protection Regulation (GDPR) protects EU citizens' personal data and privacy.

## Key Principles

1. **Lawfulness, Fairness, Transparency** - Process data legally
2. **Purpose Limitation** - Collect for specified purposes
3. **Data Minimization** - Collect only necessary data
4. **Accuracy** - Keep data accurate and up-to-date
5. **Storage Limitation** - Keep data only as long as needed
6. **Integrity & Confidentiality** - Protect data security
7. **Accountability** - Demonstrate compliance

## Implementation

### Consent Management
```java
@Entity
public class UserConsent {
    @Id
    private Long id;
    private String userId;
    private String purpose;
    private boolean granted;
    private Instant grantedAt;
    private Instant expiresAt;
}

@Service
public class ConsentService {
    public void recordConsent(String userId, String purpose, boolean granted) {
        UserConsent consent = new UserConsent();
        consent.setUserId(userId);
        consent.setPurpose(purpose);
        consent.setGranted(granted);
        consent.setGrantedAt(Instant.now());
        consentRepository.save(consent);
    }
}
```

### Data Subject Rights
```java
@RestController
@RequestMapping("/api/privacy")
public class DataSubjectController {
    
    @GetMapping("/data-export/{userId}")
    public ResponseEntity<byte[]> exportData(@PathVariable String userId) {
        UserData data = userDataService.exportAll(userId);
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=userdata.json")
            .body(objectMapper.writeValueAsBytes(data));
    }
    
    @DeleteMapping("/data-deletion/{userId}")
    public ResponseEntity<Void> deleteData(@PathVariable String userId) {
        userDataService.deleteAll(userId);
        return ResponseEntity.noContent().build();
    }
}
```

### Data Protection
```java
@Component
public class DataProtection {
    
    public String encrypt(String data) {
        // Encrypt personal data
        return encryptionService.encrypt(data);
    }
    
    public String pseudonymize(String data) {
        // Replace with pseudonym
        return hashingService.hash(data);
    }
    
    public void anonymize(Object record) {
        // Remove all personal identifiers
        ReflectionUtils.setField("email", record, null);
        ReflectionUtils.setField("name", record, null);
        ReflectionUtils.setField("phone", record, null);
    }
}
```

## Data Protection Impact Assessment

```java
@Service
public class DPIAService {
    
    public DPIA assess(String processingActivity) {
        DPIA dpia = new DPIA();
        dpia.setProcessingActivity(processingActivity);
        dpia.setNecessityAssessment(assessNecessity(processingActivity));
        dpia.setRiskAssessment(assessRisk(processingActivity));
        dpia.setMitigations(defineMitigations(processingActivity));
        return dpia;
    }
}
```

## Breach Notification

```java
@Service
public class BreachNotificationService {
    
    public void notifyBreach(DataBreach breach) {
        // Notify supervisory authority within 72 hours
        authorityNotificationService.notify(breach);
        
        // Notify affected individuals if high risk
        if (breach.getRiskLevel() == RiskLevel.HIGH) {
            breach.getAffectedUsers().forEach(userId -> {
                notificationService.notifyUser(userId, breach);
            });
        }
    }
}
```

## Best Practices

1. Conduct DPIA for high-risk processing
2. Implement consent management
3. Support data subject rights
4. Encrypt personal data
5. Implement breach notification
6. Maintain processing records
7. Appoint Data Protection Officer
8. Regular compliance audits
