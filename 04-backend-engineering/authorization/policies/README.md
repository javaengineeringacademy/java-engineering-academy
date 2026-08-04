# Policy Engines

## Comprehensive Guide to Policy Engines and OPA

Policy engines enable dynamic, externalized authorization. This guide covers Open Policy Agent (OPA) and policy-as-code patterns.

---

## Table of Contents

1. [Policy Engines Overview](#policy-engines-overview)
2. [Open Policy Agent (OPA)](#open-policy-agent-opa)
3. [Rego Language](#rego-language)
4. [Integration](#integration)
5. [Best Practices](#best-practices)

---

## Policy Engines Overview

### Why Policy Engines?

```
Hardcoded Policies:
if (user.role == "admin") { allow(); }
if (user.department == resource.department) { allow(); }
// Scattered throughout codebase

Policy Engine:
externalService.check(user, resource, action)
// Centralized, auditable, dynamic
```

### Policy Engine Options

| Engine | Language | Type | Best For |
|--------|----------|------|----------|
| OPA | Rego | General | Cloud-native |
| Casbin | PERM | RBAC/ABAC | Simple policies |
| Cedar | Cedar | ABAC | AWS services |
| XACML | XML | Standard | Enterprise |

---

## Open Policy Agent (OPA)

### OPA Architecture

```
+------------------+     +------------------+
| Application      |     | OPA              |
|                  |     |                  |
| 1. Query OPA    +---->+ 2. Evaluate      |
|    for decision  |     |    policies      |
|                  |     |                  |
| 3. Enforce       |<----+ 4. Return        |
|    decision      |     |    allow/deny    |
+------------------+     +------------------+
```

### Installation

```bash
# Docker
docker pull openpolicyagent/opa

# Run OPA
docker run -p 8181:8181 openpolicyagent/opa run --server

# macOS
brew install opa

# Linux
curl -L -o opa https://openpolicyagent.org/downloads/latest/opa_linux_amd64
chmod +x opa
sudo mv opa /usr/local/bin/
```

### Bundle Management

```bash
# Create policy bundle
tar -czf policies.tar.gz policies/

# Push bundle to S3
aws s3 cp policies.tar.gz s3://my-policies/bundle.tar.gz

# OPA configuration
cat > config.yaml <<EOF
services:
  - name: bundle
    url: https://s3.amazonaws.com/my-policies
    credentials:
      s3:
        metadata_url: http://169.254.169.254/latest/meta-data/iam/security-credentials/
bundle:
  name: policies
  prefix: bundle/
  resource: bundle.tar.gz
  persist: true
EOF
```

---

## Rego Language

### Basic Policy

```rego
# policy.rego
package authz

default allow = false

# Allow if user is admin
allow {
    input.user.role == "admin"
}

# Allow if user owns the resource
allow {
    input.user.id == input.resource.owner
}

# Allow if user is in same department
allow {
    input.user.department == input.resource.department
    input.action == "read"
}
```

### Policy with Rules

```rego
package authz

import future.keywords.if
import future.keywords.in

default allow = false

# Admin can do everything
allow if {
    "admin" in input.user.roles
}

# Manager can read/write in their department
allow if {
    "manager" in input.user.roles
    input.user.department == input.resource.department
    input.action in ["read", "write"]
}

# User can read own resources
allow if {
    input.user.id == input.resource.owner
    input.action == "read"
}

# Time-based access
allow if {
    time.now_ns() > time.parse_rfc3339_ns("2024-01-01T09:00:00Z")
    time.now_ns() < time.parse_rfc3339_ns("2024-01-01T17:00:00Z")
    input.action == "read"
}
```

### Data Model

```rego
package authz

# Role permissions
role_permissions := {
    "admin": ["read", "write", "delete", "manage"],
    "manager": ["read", "write", "approve"],
    "user": ["read"]
}

# Check role-based permission
allow if {
    role := input.user.roles[_]
    permissions := role_permissions[role]
    input.action in permissions
}

# Check resource ownership
allow if {
    input.user.id == input.resource.owner
    input.action in ["read", "update"]
}

# Check department access
allow if {
    input.user.department == input.resource.department
    input.resource.classification != "confidential"
}
```

---

## Integration

### Spring Boot Integration

```java
@Component
public class OpaAuthorizer {

    private final RestTemplate restTemplate;
    private final String opaUrl;

    public boolean isAllowed(String userId, String resource,
                             String action, Map<String, Object> context) {
        Map<String, Object> input = Map.of(
            "user", getUserAttributes(userId),
            "resource", getResourceAttributes(resource),
            "action", action,
            "context", context
        );

        ResponseEntity<OpaResponse> response = restTemplate.postForEntity(
            opaUrl + "/v1/data/authz/allow",
            Map.of("input", input),
            OpaResponse.class);

        return response.getBody().getResult();
    }

    private Map<String, Object> getUserAttributes(String userId) {
        User user = userRepository.findById(userId);
        return Map.of(
            "id", user.getId(),
            "roles", user.getRoles(),
            "department", user.getDepartment()
        );
    }
}
```

### REST API

```java
@RestController
@RequestMapping("/api")
public class ResourceController {

    private final OpaAuthorizer opaAuthorizer;

    @GetMapping("/documents/{id}")
    public ResponseEntity<Document> getDocument(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails user) {

        boolean allowed = opaAuthorizer.isAllowed(
            user.getUsername(),
            "document:" + id,
            "read",
            Map.of());

        if (!allowed) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(documentService.findById(id));
    }
}
```

### gRPC Interceptor

```java
@Component
public class OpaGrpcInterceptor implements ServerInterceptor {

    private final OpaAuthorizer opaAuthorizer;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String userId = extractUserId(headers);
        String method = call.getMethodDescriptor().getFullMethodName();

        if (!opaAuthorizer.isAllowed(userId, method, "execute", Map.of())) {
            call.close(Status.PERMISSION_DENIED
                .withDescription("Access denied"), new Metadata());
            return new NoopListener<>();
        }

        return next.startCall(call, headers);
    }
}
```

---

## Best Practices

### 1. Test Policies

```bash
# Test with OPA CLI
cat input.json | opa test policy.rego -v

# Test file
# test_policy.rego
package authz

test_admin_allow {
    allow with input as {
        "user": {"id": "1", "roles": ["admin"]},
        "resource": {"id": "doc1"},
        "action": "read"
    }
}

test_user_deny_admin_action {
    not allow with input as {
        "user": {"id": "1", "roles": ["user"]},
        "resource": {"id": "doc1"},
        "action": "delete"
    }
}
```

### 2. Version Policies

```bash
# Tag policies
git tag v1.0.0

# Deploy specific version
opa bundle --revision v1.0.0 -o bundle.tar.gz .
```

### 3. Monitor Policy Decisions

```java
@Component
public class AuditingOpaAuthorizer implements OpaAuthorizer {

    @Override
    public boolean isAllowed(...) {
        boolean allowed = delegate.isAllowed(userId, resource, action, context);

        auditService.logPolicyDecision(
            userId, resource, action, allowed, context);

        return allowed;
    }
}
```

### 4. Cache Decisions

```java
@Cacheable(value = "opa-decisions",
    key = "#userId + ':' + #resource + ':' + #action")
public boolean isAllowed(String userId, String resource,
                         String action, Map<String, Object> context) {
    return callOpa(userId, resource, action, context);
}
```

### 5. Handle OPA Failures

```java
@Component
public class FaultTolerantOpaAuthorizer implements OpaAuthorizer {

    private final OpaAuthorizer delegate;
    private final boolean failClosed;

    @Override
    public boolean isAllowed(...) {
        try {
            return delegate.isAllowed(userId, resource, action, context);
        } catch (Exception e) {
            log.error("OPA call failed", e);
            // fail closed = deny on failure
            // fail open = allow on failure
            return !failClosed;
        }
    }
}
```

---

## Further Reading

- [OPA Documentation](https://www.openpolicyagent.org/docs/)
- [Rego Language](https://www.openpolicyagent.org/docs/latest/#rego)
- [OPA Best Practices](https://www.openpolicyagent.org/docs/latest/#best-practices)
- [OPA Spring Boot](https://github.com/OpenPolicyAgent/opa-java)
