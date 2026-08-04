# HIPAA Compliance

## Overview

The Health Insurance Portability and Accountability Act (HIPAA) protects sensitive patient health information (PHI).

## Key Rules

### Privacy Rule
- Defines permitted uses and disclosures of PHI
- Gives patients rights over their health information
- Requires appropriate safeguards

### Security Rule
- Protects electronic PHI (ePHI)
- Requires administrative, physical, and technical safeguards

### Breach Notification Rule
- Requires notification of breaches
- Different notification requirements based on breach size

## Technical Safeguards

### Access Control
```java
@Configuration
public class HIPAAAccessControl {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests(authorize -> authorize
            .requestMatchers("/api/patient/**").hasRole("CLINICIAN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        ).build();
    }
}
```

### Audit Logging
```java
@Component
public class AuditLogger {
    
    public void logAccess(String userId, String resourceId, String action) {
        AuditLog log = AuditLog.builder()
            .userId(userId)
            .resourceId(resourceId)
            .action(action)
            .timestamp(Instant.now())
            .build();
        
        auditLogRepository.save(log);
    }
}
```

### Encryption
```java
// Encrypt PHI at rest
@Component
public class PHIEncryption {
    
    public String encrypt(String phi) {
        return encryptionService.encrypt(phi, PHI_KEY);
    }
    
    public String decrypt(String encryptedPhi) {
        return encryptionService.decrypt(encryptedPhi, PHI_KEY);
    }
}
```

### Minimum Necessary
```java
@Service
public class PatientService {
    
    public PatientSummary getPatientSummary(String patientId) {
        Patient patient = patientRepository.findById(patientId);
        
        // Return only necessary fields
        return PatientSummary.builder()
            .id(patient.getId())
            .name(patient.getName())
            .build();
    }
}
```

## Best Practices

1. Implement role-based access control
2. Encrypt PHI at rest and in transit
3. Maintain audit logs
4. Implement minimum necessary access
5. Conduct regular risk assessments
6. Train workforce on HIPAA
7. Have incident response plan
8. Business Associate Agreements
