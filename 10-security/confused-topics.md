# Authentication vs Authorization

## What They Are

### Authentication (AuthN)
The process of verifying who someone is. Confirms the identity of a user, device, or system. Answers the question: "Are you who you claim to be?"

### Authorization (AuthZ)
The process of determining what an authenticated entity is allowed to do. Grants or denies access to resources and operations. Answers the question: "What are you allowed to do?"

## Key Difference Table

| Feature | Authentication | Authorization |
|---------|---------------|---------------|
| Purpose | Identity verification | Access control |
| Question | "Who are you?" | "What can you do?" |
| Order | Happens first | Happens after authN |
| Data | Credentials (password, token) | Permissions, roles |
| Storage | Identity store | Policy store |
| Protocols | LDAP, OAuth, SAML | RBAC, ABAC, ACL |
| Failure | Login failed | Access denied |
| Scope | Global identity | Resource-specific |
| Visibility | User-facing | Often behind the scenes |
| Standards | OpenID Connect, Kerberos | XACML, OAuth scopes |

## When to Use Which

### Authentication Concerns
- User login and registration
- Multi-factor authentication
- Single sign-on (SSO)
- Session management
- Credential storage
- Identity federation

### Authorization Concerns
- Role-based access control (RBAC)
- Attribute-based access control (ABAC)
- API permission management
- Resource ownership
- Delegated access
- Audit logging of access decisions

## Interview Trap

**Trap**: "Authentication and authorization are the same thing."

**Reality**: They are distinct processes. Authentication verifies identity; authorization determines permissions. You must authenticate before you can authorize. A user can be authenticated but unauthorized to access specific resources.

**Follow-up Trap**: "OAuth handles both authentication and authorization."

**Reality**: OAuth is primarily an authorization framework. For authentication, it's typically combined with OpenID Connect (OIDC). OAuth tells you what you can access; OIDC tells you who you are.

## Visual Diagram

```
Login Flow:
┌─────────┐     ┌─────────┐     ┌─────────┐
│  User    │     │  App    │     │  Auth   │
│         │     │         │     │  Server │
└─────────┘     └─────────┘     └─────────┘
    │               │               │
    │──1. Credentials──>│           │
    │               │──2. Verify Identity──>│
    │               │<──3. Identity Confirmed──│
    │               │               │
    │               │   (Authentication Complete)
    │               │               │
    │               │──4. Check Permissions──>│
    │               │<──5. Permissions Granted──│
    │               │               │
    │<──6. Access Resource──│       │
    │               │               │
    │               │   (Authorization Complete)

RBAC Example:
┌─────────────────────────────────────────────────────┐
│                Authentication                       │
│  User: john.doe@example.com                        │
│  Method: Password + MFA                            │
│  Status: Authenticated                             │
└─────────────────────────────────────────────────────┘
                    │
                    v
┌─────────────────────────────────────────────────────┐
│                Authorization                        │
│                                                     │
│  Role: Senior Developer                             │
│  Permissions:                                       │
│    - Read: code, documentation                      │
│    - Write: code, tests                             │
│    - Deploy: staging                                │
│    - Admin: none                                    │
│                                                     │
│  Denied:                                            │
│    - Production deployment                          │
│    - User management                                │
│    - Financial data                                 │
└─────────────────────────────────────────────────────┘
```

## Implementation Examples

**Authentication:**
- Username/password login
- OAuth social login (Google, GitHub)
- SAML for enterprise SSO
- API keys for service authentication
- JWT tokens for stateless auth

**Authorization:**
- Role-based access control (RBAC)
- Attribute-based access control (ABAC)
- Access control lists (ACL)
- OAuth scopes
- Policy engines (OPA, Casbin)

## Common Mistakes

1. **Confusing session with authorization**: Session management is authentication; what you can do with the session is authorization
2. **Missing authorization checks**: User is authenticated but not authorized for specific operations
3. **Over-permissioning**: Giving users more access than needed (principle of least privilege)
4. **Hardcoding permissions**: Not implementing dynamic authorization policies
5. **Ignoring audit trails**: Not logging authorization decisions

## Key Insight

Authentication and authorization work together but serve different purposes:

**Authentication**: Identity verification (who you are)
**Authorization**: Access control (what you can do)

A secure system requires both:
1. Authenticate users properly
2. Authorize access to resources
3. Log both authentication and authorization events
4. Regularly review and update permissions

The order matters: Always authenticate before authorizing. Never skip authentication and rely solely on authorization.
