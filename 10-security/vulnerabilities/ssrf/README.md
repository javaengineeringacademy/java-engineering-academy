# Server-Side Request Forgery (SSRF)

## Overview

SSRF occurs when an application fetches a resource from a user-supplied URL without proper validation.

## Vulnerable Code

```java
// NEVER DO THIS
@GetMapping("/fetch")
public String fetch(@RequestParam String url) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build();
    return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
}
```

## Prevention

### URL Validation
```java
@Component
public class UrlValidator {
    private static final List<String> ALLOWED_HOSTS = List.of(
        "api.example.com",
        "cdn.example.com"
    );
    
    private static final List<String> BLOCKED_SCHEMES = List.of(
        "file", "ftp", "gopher", "dict", "jdbc"
    );
    
    public void validate(String url) {
        URI uri = URI.create(url);
        
        // Check scheme
        if (BLOCKED_SCHEMES.contains(uri.getScheme().toLowerCase())) {
            throw new ValidationException("URL scheme not allowed");
        }
        
        // Check host
        if (!ALLOWED_HOSTS.contains(uri.getHost())) {
            throw new ValidationException("Host not allowed");
        }
        
        // Check for internal IPs
        if (isInternalHost(uri.getHost())) {
            throw new ValidationException("Internal hosts not allowed");
        }
    }
    
    private boolean isInternalHost(String host) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            return addr.isLoopbackAddress() || 
                   addr.isSiteLocalAddress() ||
                   addr.isAnyLocalAddress();
        } catch (UnknownHostException e) {
            return true;
        }
    }
}
```

### Network Segmentation
```java
// Use network policies to restrict outbound traffic
// In Kubernetes, use NetworkPolicy
```

### DNS Resolution Validation
```java
public void validateAfterDnsResolution(String url) {
    URI uri = URI.create(url);
    InetAddress[] addresses = InetAddress.getAllByName(uri.getHost());
    
    for (InetAddress address : addresses) {
        if (address.isLoopbackAddress() || address.isSiteLocalAddress()) {
            throw new ValidationException("Resolved to internal address");
        }
    }
}
```

## Best Practices

1. Validate and sanitize URLs
2. Use allowlists for hosts
3. Block internal IP ranges
4. Implement network segmentation
5. Use DNS resolution validation
6. Disable unnecessary URL schemes
7. Implement request timeouts
8. Monitor outbound requests
