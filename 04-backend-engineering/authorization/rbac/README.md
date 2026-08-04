# Role-Based Access Control (RBAC)

## Comprehensive Guide to RBAC Implementation

RBAC restricts access based on user roles. This guide covers role hierarchy, permissions, and implementation with Spring Security.

---

## Table of Contents

1. [RBAC Overview](#rbac-overview)
2. [Role Hierarchy](#role-hierarchy)
3. [Permissions](#permissions)
4. [Implementation](#implementation)
5. [Best Practices](#best-practices)

---

## RBAC Overview

### RBAC Model

```
User -> Role -> Permission

Example:
John (User) -> Developer (Role) -> read:code, write:code, read:docs
Jane (User) -> Admin (Role) -> read:*, write:*, delete:*
```

### Role Hierarchy

```
ROLE_ADMIN
  |
  +-- ROLE_MANAGER
  |     |
  |     +-- ROLE_DEVELOPER
  |     |     |
  |     |     +-- ROLE_USER
  |     |
  |     +-- ROLE_TESTER
  |
  +-- ROLE_AUDITOR
```

---

## Role Hierarchy

### Spring Configuration

```java
@Bean
public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role("ADMIN").implies("MANAGER")
        .role("MANAGER").implies("DEVELOPER")
        .role("DEVELOPER").implies("USER")
        .role("MANAGER").implies("TESTER")
        .build();
}
```

### Usage

```java
@GetMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Dashboard> adminDashboard() {
    return ResponseEntity.ok(dashboardService.getAdminDashboard());
}

@GetMapping("/manager/dashboard")
@PreAuthorize("hasRole('MANAGER')")
public ResponseEntity<Dashboard> managerDashboard() {
    return ResponseEntity.ok(dashboardService.getManagerDashboard());
}

@GetMapping("/projects")
@PreAuthorize("hasAnyRole('DEVELOPER', 'TESTER')")
public ResponseEntity<List<Project>> projects() {
    return ResponseEntity.ok(projectService.findAll());
}
```

---

## Permissions

### Permission Model

```java
@Entity
public class Permission {
    @Id
    private Long id;
    private String name;        // e.g., "read:users"
    private String resource;    // e.g., "users"
    private String action;      // e.g., "read"
}

@Entity
public class Role {
    @Id
    private Long id;
    private String name;        // e.g., "ADMIN"
    private List<Permission> permissions;
}

@Entity
public class User {
    @Id
    private Long id;
    private List<Role> roles;
}
```

### Permission-Based Access

```java
@GetMapping("/users/{id}")
@PreAuthorize("hasPermission('read', 'User')")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
}

@PostMapping("/users")
@PreAuthorize("hasPermission('write', 'User')")
public ResponseEntity<User> createUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.create(user));
}

@DeleteMapping("/users/{id}")
@PreAuthorize("hasPermission('delete', 'User')")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
}
```

### Custom Permission Evaluator

```java
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PermissionService permissionService;

    @Override
    public boolean hasPermission(Authentication auth,
                                  Object targetDomainObject,
                                  Object permission) {
        if (auth == null || !(permission instanceof String)) {
            return false;
        }

        String targetType = targetDomainObject.getClass()
            .getSimpleName().toUpperCase();
        String perm = ((String) permission).toUpperCase();

        return permissionService.hasPermission(
            auth.getName(), targetType, perm);
    }

    @Override
    public boolean hasPermission(Authentication auth,
                                  Serializable targetId,
                                  String targetType,
                                  Object permission) {
        return permissionService.hasPermission(
            auth.getName(), targetType.toUpperCase(),
            ((String) permission).toUpperCase());
    }
}
```

---

## Implementation

### User Principal

```java
public class AppUser implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(p -> new SimpleGrantedAuthority(p.getName()))
            .collect(Collectors.toList());
    }

    public boolean hasRole(String role) {
        return roles.stream()
            .anyMatch(r -> r.getName().equals(role));
    }

    public boolean hasPermission(String permission) {
        return roles.stream()
            .flatMap(r -> r.getPermissions().stream())
            .anyMatch(p -> p.getName().equals(permission));
    }
}
```

### Authorization Service

```java
@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public boolean hasRole(String username, String role) {
        return userRepository.findByUsername(username)
            .map(user -> user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role)))
            .orElse(false);
    }

    public boolean hasPermission(String username, String resource,
                                 String action) {
        return userRepository.findByUsername(username)
            .map(user -> user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> p.getResource().equals(resource)
                    && p.getAction().equals(action)))
            .orElse(false);
    }

    public List<String> getUserPermissions(String username) {
        return userRepository.findByUsername(username)
            .map(user -> user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }
}
```

---

## Best Practices

### 1. Principle of Least Privilege

```java
// Assign minimum required role
@PreAuthorize("hasRole('USER')")
@GetMapping("/profile")
public ResponseEntity<User> profile() { }

// Only admin can delete
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) { }
```

### 2. Use Role Hierarchy

```java
// Instead of checking multiple roles
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public void manageUsers() { }

// Use hierarchy
@PreAuthorize("hasRole('MANAGER')")
public void manageUsers() { }  // ADMIN also has access
```

### 3. Document Roles and Permissions

```java
// Document in API
@Operation(summary = "Delete user",
    security = @SecurityRequirement(name = "bearerAuth"))
@ApiResponse(responseCode = "403",
    description = "Requires ADMIN role")
@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) { }
```

### 4. Audit Access

```java
@Component
public class AuditInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String username = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        String method = invocation.getMethod().getName();

        auditService.logAccess(username, method);

        return invocation.proceed();
    }
}
```

### 5. Separate Roles from Users

```java
// Store roles in database, not in JWT
@Component
public class RoleService {

    @Cacheable("user-roles")
    public List<String> getUserRoles(String userId) {
        return roleRepository.findByUserId(userId);
    }
}
```

---

## Further Reading

- [RBAC Specification](https://csrc.nist.gov/Projects/rbac)
- [Spring Security Authorization](https://docs.spring.io/spring-security/reference/servlet/authorization/index.html)
- [RBAC vs ABAC](https://www.okta.com/identity-101/role-based-access-control-vs-attribute-based-access-control/)
