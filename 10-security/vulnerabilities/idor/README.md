# Insecure Direct Object References (IDOR)

## Overview

IDOR vulnerabilities occur when an application exposes internal objects (files, database records, database keys) to users without proper access control checks.

## Vulnerable Code

```java
// NEVER DO THIS
@GetMapping("/orders/{id}")
public Order getOrder(@PathVariable Long id) {
    return orderRepository.findById(id).orElseThrow();
}
```

## Prevention

### Authorization Check
```java
@GetMapping("/orders/{id}")
public Order getOrder(@PathVariable Long id) {
    Order order = orderRepository.findById(id).orElseThrow();
    
    if (!order.getUserId().equals(getCurrentUserId())) {
        throw new AccessDeniedException("Not your order");
    }
    
    return order;
}
```

### UUID Instead of Sequential IDs
```java
// Use UUIDs instead of sequential IDs
@Entity
public class Order {
    @Id
    private UUID id = UUID.randomUUID();
}

@GetMapping("/orders/{id}")
public Order getOrder(@PathVariable UUID id) {
    // UUIDs are not guessable
    return orderRepository.findById(id).orElseThrow();
}
```

### Path-Based Authorization
```java
@PreAuthorize("#order.userId == authentication.principal.id")
public Order getOrder(Order order) {
    return order;
}
```

### Filter-Based Check
```java
@Component
public class ResourceAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) req;
        
        String resourceId = extractResourceId(request);
        String userId = getCurrentUserId(request);
        
        if (!resourceOwnerService.isOwner(resourceId, userId)) {
            ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        chain.doFilter(req, res);
    }
}
```

## Testing for IDOR

```bash
# Test with different IDs
curl -H "Authorization: Bearer token1" https://api.example.com/orders/1
curl -H "Authorization: Bearer token2" https://api.example.com/orders/1

# Test with sequential IDs
for i in {1..100}; do
    curl -H "Authorization: Bearer token" https://api.example.com/orders/$i
done
```

## Best Practices

1. Always check authorization
2. Use UUIDs for public resources
3. Implement object-level access control
4. Use indirect references
5. Log access attempts
6. Implement rate limiting
7. Test for IDOR vulnerabilities
8. Use authorization frameworks
