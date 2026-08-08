# Requests

## Why Requests Exists

Every Python developer who needs to call an API or fetch a web page eventually struggles with `urllib` — it's verbose, error-prone, and hard to use. Requests was created to make HTTP simple: one line to GET a URL, automatic JSON parsing, built-in session management, and graceful error handling. It replaces boilerplate with clarity.

## What You'll Learn

By the end of this section, you'll be able to:

- Make GET, POST, PUT, and DELETE requests with proper error handling
- Use sessions for persistent connections and cookie management
- Handle authentication, timeouts, and file uploads safely

## When to Use Requests

| Use Case | Why Requests | Alternative |
|----------|-------------|-------------|
| Fetch API data | One-liner with auto JSON parsing | `urllib` |
| POST form data | Automatic encoding | `urllib.parse` |
| Upload files | Multipart encoding handled | Manual encoding |
| Authenticated APIs | Built-in auth strategies | Custom headers |
| Session-based auth | Persists cookies/login state | Cookie jar |
| Web scraping | Simple page fetching | `urllib` |
| Health checks | Quick server status | `urllib` |

## How Requests Works Internally

Requests wraps `urllib3` under the hood, adding a user-friendly API on top. When you call `requests.get(url)`, it creates a `Session` object (even for single requests), which manages connection pooling, cookie handling, and SSL verification. The session uses urllib3's connection pool to reuse TCP connections, improving performance for multiple requests to the same host.

Error handling in Requests follows the "easier to ask forgiveness" pattern. `response.raise_for_status()` raises an `HTTPError` for 4xx and 5xx responses, while `response.json()` handles JSON decoding errors gracefully. The library also manages redirects automatically, following up to 30 by default.

```python
import requests

# Basic GET
response = requests.get('https://api.github.com/users/octocat')
print(response.json())

# POST with JSON
response = requests.post('https://httpbin.org/post', json={'name': 'Alice'})

# Session for persistence
session = requests.Session()
session.post('https://example.com/login', data={'user': 'admin'})
response = session.get('https://example.com/dashboard')

# Timeout and error handling
try:
    response = requests.get('https://api.example.com', timeout=5)
    response.raise_for_status()
except requests.Timeout:
    print("Request timed out")
except requests.HTTPError as e:
    print(f"HTTP Error: {e}")
```

## Production Checklist

### ✅ Before using Requests in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Requests is synchronous, so it's slow for APIs
**Reality:** For most applications, the network latency dominates. A synchronous Requests call waiting 100ms for an API response isn't the bottleneck. For high-concurrency needs, use `httpx` or `aiohttp`.

### ❌ Myth 2: You should always disable SSL verification for testing
**Reality:** Even in testing, prefer `verify=False` with warning suppression rather than globally disabling it. In production, always verify SSL.

### ❌ Myth 3: response.text and response.content are the same
**Reality:** `response.content` is raw bytes. `response.text` is decoded to a string using the response's encoding. For binary data, always use `.content`.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Simple HTTP client library |
| Complexity | O(1) per request (network-bound) |
| Thread Safe | Yes (per session) |
| Best Alternative | `httpx` for async |
| When to Use | API calls, web scraping |
| When to Avoid | High-concurrency needs (use httpx) |

## Related Topics

- [04-flask](../04-flask/) - Server-side API building
- [05-django](../05-django/) - REST framework for Django
- [09-fastapi](../09-fastapi/) - Async API framework

## References
- Requests Documentation: https://requests.readthedocs.io/
- Requests Source: https://github.com/psf/requests
- HTTP/1.1 Spec (RFC 7230)

## Version Validation
- Verified against: Requests 2.31+, Python 3.10+
