# Attribute-Based Access Control (ABAC)

## Comprehensive Guide to ABAC Implementation

ABAC uses attributes of users, resources, and environment to make access decisions. This guide covers policy design and implementation.

---

## Table of Contents

1. [ABAC Overview](#abac-overview)
2. [Policy Structure](#policy-structure)
3. [Attributes](#attributes)
4. [Implementation](#implementation)
5. [Best Practices](#best-practices)

---

## ABAC Overview

### ABAC Model

```
Decision = Policy(Subject Attributes, Resource Attributes,
                  Action Attributes, Environment Attributes)

Example:
IF subject.role == "manager"
AND resource.department == subject.department
AND action == "read"
AND environment.time BETWEEN 9am AND 5pm
THEN allow
```

### ABAC vs RBAC

| Feature | RBAC | ABAC |
|---------|------|------|
| Access based on | Role | Attributes |
| Flexibility | Low | High |
| Complexity | Simple | Complex |
| Scalability | Limited | Excellent |
| Dynamic policies | No | Yes |

---

## Policy Structure

### Policy Language (ALFA)

```
policy userDepartmentAccess {
    target clause (action == "read" && resource.type == "document")
    
    condition subject.department == resource.department
    
    apply permit
}

policy adminAccess {
    target clause (subject.role == "admin")
    
    apply permit
}
```

### JSON Policy

```json
{
    "policyId": "document-access",
    "description": "Control document access based on department",
    "rules": [
        {
            "effect": "permit",
            "conditions": {
                "allOf": [
                    { "subject.role": "manager" },
                    { "subject.department": "${resource.department}" },
                    { "action": "read" }
                ]
            }
        },
        {
            "effect": "permit",
            "conditions": {
                "allOf": [
                    { "subject.role": "admin" }
                ]
            }
        }
    ]
}
```

---

## Attributes

### Subject Attributes

```java
public class SubjectAttributes {
    private String userId;
    private String username;
    private List<String> roles;
    private String department;
    private String clearanceLevel;
    private Map<String, String> customAttributes;
}
```

### Resource Attributes

```java
public class ResourceAttributes {
    private String resourceId;
    private String resourceType;
    private String department;
    private String owner;
    private String classification;
    private Map<String, String> tags;
}
```

### Environment Attributes

```java
public class EnvironmentAttributes {
    private LocalDateTime currentTime;
    private String ipAddress;
    private String deviceType;
    private boolean isVPN;
    private String location;
}
```

---

## Implementation

### Policy Engine

```java
@Component
public class PolicyEngine {

    private final List<Policy> policies;

    public AccessDecision evaluate(SubjectAttributes subject,
                                    ResourceAttributes resource,
                                    String action,
                                    EnvironmentAttributes environment) {

        for (Policy policy : policies) {
            if (policy.applies(subject, resource, action, environment)) {
                return policy.evaluate(subject, resource, action, environment);
            }
        }

        return AccessDecision.DENY;
    }
}
```

### Policy Interface

```java
public interface Policy {
    boolean applies(SubjectAttributes subject,
                    ResourceAttributes resource,
                    String action,
                    EnvironmentAttributes environment);

    AccessDecision evaluate(SubjectAttributes subject,
                            ResourceAttributes resource,
                            String action,
                            EnvironmentAttributes environment);
}
```

### Spring Security Integration

```java
@Component
public class AbacAccessDecisionVoter implements AccessDecisionVoter<Object> {

    private final PolicyEngine policyEngine;

    @Override
    public int vote(Authentication authentication,
                     Object object,
                     Collection<ConfigAttribute> attributes) {

        SubjectAttributes subject = extractSubject(authentication);
        ResourceAttributes resource = extractResource(object);
        String action = extractAction(object);
        EnvironmentAttributes environment = extractEnvironment();

        AccessDecision decision = policyEngine.evaluate(
            subject, resource, action, environment);

        return decision.isPermit() ?
            ACCESS_GRANTED : ACCESS_DENIED;
    }
}
```

### Annotation-Based

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@abacAuthorizer.hasAccess(#resource)")
public @interface PreAuthorizeAbac {}

@Component
public class AbacAuthorizer {

    private final PolicyEngine policyEngine;

    public boolean hasAccess(ResourceAttributes resource) {
        Authentication auth = SecurityContextHolder.getContext()
            .getAuthentication();

        SubjectAttributes subject = extractSubject(auth);
        EnvironmentAttributes env = extractEnvironment();

        return policyEngine.evaluate(subject, resource, "read", env)
            .isPermit();
    }
}
```

---

## Best Practices

### 1. Start Simple

```java
// Start with basic policies
public class SimpleDepartmentPolicy implements Policy {
    @Override
    public boolean applies(...) {
        return subject.getDepartment() != null;
    }

    @Override
    public AccessDecision evaluate(...) {
        if (subject.getDepartment().equals(resource.getDepartment())) {
            return AccessDecision.PERMIT;
        }
        return AccessDecision.DENY;
    }
}
```

### 2. Cache Policy Decisions

```java
@Cacheable(value = "policy-decisions",
    key = "#subject.userId + ':' + #resource.resourceId")
public AccessDecision evaluate(...) {
    return evaluateUncached(subject, resource, action, environment);
}
```

### 3. Log Access Decisions

```java
@Component
public class AuditingPolicyEngine implements PolicyEngine {

    @Override
    public AccessDecision evaluate(...) {
        AccessDecision decision = delegate.evaluate(
            subject, resource, action, environment);

        auditService.logAccessDecision(
            subject.getUserId(),
            resource.getResourceId(),
            action,
            decision);

        return decision;
    }
}
```

### 4. Test Policies

```java
@Test
void shouldAllowManagerAccessToOwnDepartment() {
    SubjectAttributes manager = SubjectAttributes.builder()
        .userId("1")
        .roles(List.of("manager"))
        .department("engineering")
        .build();

    ResourceAttributes document = ResourceAttributes.builder()
        .resourceId("doc1")
        .department("engineering")
        .build();

    AccessDecision decision = policyEngine.evaluate(
        manager, document, "read", environment);

    assertThat(decision.isPermit()).isTrue();
}
```

### 5. Use Policy Administration

```java
@Service
public class PolicyAdminService {

    public void createPolicy(PolicyRequest request) {
        Policy policy = PolicyMapper.toPolicy(request);
        policyRepository.save(policy);
        policyCache.evictAll();
    }

    public void updatePolicy(Long policyId, PolicyRequest request) {
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new PolicyNotFoundException(policyId));
        policy.update(request);
        policyRepository.save(policy);
        policyCache.evictAll();
    }
}
```

---

## Further Reading

- [NIST ABAC](https://csrc.nist.gov/Projects/ABAC)
- [XACML Specification](https://docs.oasis-open.org/xacml/)
- [Spring Security Authorization](https://docs.spring.io/spring-security/reference/servlet/authorization/index.html)
- [Open Policy Agent](https://www.openpolicyagent.org/)
