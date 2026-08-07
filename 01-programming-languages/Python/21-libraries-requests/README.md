# Requests — HTTP for Humans

> **If you need to talk to a server, Requests is your translator.**

## What

Requests is the most popular Python HTTP client library. It provides a simple, intuitive API for making HTTP requests — GET, POST, PUT, DELETE, and more. It handles sessions, cookies, authentication, timeouts, retries, and SSL verification out of the box.

## Why

- **Readability:** `requests.get(url)` is self-explanatory compared to `urllib.request.urlopen()`.
- **Automatic features:** Handles redirects, content encoding, cookies, and SSL verification.
- **Session support:** Persistent connections and cookies across requests.
- **Authentication:** Built-in support for Basic, Digest, OAuth, and custom auth.
- **JSON handling:** `.json()` method parses responses directly.

## When

| Scenario | Requests Approach | Why |
|----------|------------------|-----|
| Fetch API data | `requests.get(url)` | One-liner with auto JSON parsing |
| POST form data | `requests.post(url, data={})` | Automatic encoding |
| Upload files | `requests.post(url, files={})` | Multipart encoding handled |
| Authenticated APIs | `requests.get(url, auth=...)` | Built-in auth strategies |
| Session-based auth | `requests.Session()` | Persists cookies/login state |
| Web scraping | `requests.get(url)` + BeautifulSoup | Simple page fetching |
| Health checks | `requests.get(url, timeout=5)` | Quick server status |
| Webhooks | Flask + Requests | Forwarding events to other services |

## How

### Basic Requests

```python
import requests

# GET request
response = requests.get('https://api.github.com/users/octocat')
print(response.status_code)  # 200
print(response.headers['content-type'])
print(response.json())       # Parsed JSON

# POST with JSON
response = requests.post(
    'https://httpbin.org/post',
    json={'name': 'Alice', 'age': 30}
)

# POST with form data
response = requests.post(
    'https://httpbin.org/post',
    data={'username': 'admin', 'password': 'secret'}
)

# Other methods
requests.put(url, json=data)
requests.delete(url)
requests.patch(url, json=data)
requests.head(url)
```

### Headers, Parameters, and Timeouts

```python
import requests

# Custom headers
headers = {
    'Authorization': 'Bearer token123',
    'Content-Type': 'application/json',
    'User-Agent': 'MyApp/1.0'
}
response = requests.get('https://api.example.com/data', headers=headers)

# Query parameters
params = {'page': 1, 'per_page': 20, 'sort': 'name'}
response = requests.get('https://api.example.com/items', params=params)
# URL becomes: https://api.example.com/items?page=1&per_page=20&sort=name

# Timeout (critical for production!)
try:
    response = requests.get('https://api.example.com', timeout=5)  # 5 seconds
except requests.Timeout:
    print("Request timed out")
```

### Sessions

```python
import requests

# Session persists cookies and connection pooling
session = requests.Session()

# Login (cookies saved automatically)
session.post('https://example.com/login', data={
    'username': 'admin',
    'password': 'secret'
})

# Subsequent requests use saved cookies
response = session.get('https://example.com/dashboard')
print(response.status_code)  # 200 (authenticated)

# Session with default headers
session.headers.update({'Authorization': 'Bearer token123'})
session.get('https://api.example.com/data')  # Headers sent automatically
```

### Error Handling

```python
import requests

response = requests.get('https://api.example.com/data', timeout=5)

# Check status code
if response.ok:  # 200-399
    data = response.json()
else:
    print(f"Error: {response.status_code} - {response.text}")

# Raise exception for bad status
try:
    response.raise_for_status()
except requests.HTTPError as e:
    print(f"HTTP Error: {e}")
except requests.ConnectionError:
    print("Connection failed")
except requests.Timeout:
    print("Request timed out")
except requests.RequestException as e:
    print(f"Request failed: {e}")
```

### File Upload and Downloads

```python
import requests

# Upload files
files = {'file': open('report.csv', 'rb')}
response = requests.post('https://api.example.com/upload', files=files)

# Upload with metadata
files = {'file': ('report.csv', open('report.csv', 'rb'), 'text/csv')}
data = {'description': 'Monthly report'}
response = requests.post('https://api.example.com/upload', files=files, data=data)

# Download file
response = requests.get('https://example.com/large-file.zip', stream=True)
with open('large-file.zip', 'wb') as f:
    for chunk in response.iter_content(chunk_size=8192):
        f.write(chunk)

# Progress tracking
total = int(response.headers.get('content-length', 0))
downloaded = 0
with open('file.zip', 'wb') as f:
    for chunk in response.iter_content(chunk_size=8192):
        downloaded += len(chunk)
        f.write(chunk)
        print(f"\r{downloaded}/{total} bytes ({100*downloaded/total:.1f}%)", end='')
```

### Authentication

```python
import requests
from requests.auth import HTTPBasicAuth, HTTPDigestAuth

# Basic Auth
response = requests.get(
    'https://api.example.com/protected',
    auth=HTTPBasicAuth('user', 'pass')
)

# Digest Auth
response = requests.get(
    'https://api.example.com/protected',
    auth=HTTPDigestAuth('user', 'pass')
)

# Bearer Token
headers = {'Authorization': 'Bearer my-token-here'}
response = requests.get('https://api.example.com/data', headers=headers)

# Custom Auth
class TokenAuth(requests.auth.AuthBase):
    def __init__(self, token):
        self.token = token

    def __call__(self, r):
        r.headers['X-Token'] = self.token
        return r

response = requests.get('https://api.example.com', auth=TokenAuth('my-token'))
```

## Production Checklist

- [ ] **Always set timeouts** — never make requests without `timeout=`
- [ ] **Use `raise_for_status()`** — catch HTTP errors explicitly
- [ ] **Use sessions** — connection pooling and cookie persistence
- [ ] **Don't hardcode credentials** — use environment variables or config files
- [ ] **Verify SSL** — never set `verify=False` in production
- [ ] **Handle retries** — use `urllib3.util.retry` for transient failures
- [ ] **Rate limit** — respect API rate limits, add delays between requests
- [ ] **Stream large downloads** — use `stream=True` for memory efficiency

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **urllib** | Uses `urllib.request` directly. Verbose, error-prone. |
| 2 | **Basic Requests** | `requests.get()`, basic error handling, no sessions. |
| 3 | **Production** | Sessions, timeouts, retries, proper error handling. |
| 4 | **Resilient** | Circuit breakers, exponential backoff, request/response logging. |
| 5 | **Advanced** | Async with `httpx`, connection pooling tuning, custom transport adapters. |

## Common Myths

### Myth 1: "Requests is synchronous, so it's slow for APIs"
**Reality:** For most applications, the network latency dominates. A synchronous Requests call waiting 100ms for an API response isn't the bottleneck. For high-concurrency needs, use `httpx` or `aiohttp`, not parallel threads with Requests.

### Myth 2: "You should always disable SSL verification for testing"
**Reality:** Even in testing, prefer `verify=False` with a warning suppression rather than globally disabling it. In production, always verify SSL. Use a test certificate authority for dev environments.

### Myth 3: "response.text and response.content are the same"
**Reality:** `response.content` is raw bytes. `response.text` is decoded to a string using the response's encoding (or ISO-8859-1 as fallback). For binary data, always use `.content`. For text, use `.text` or `.json()`.

## One-Minute Revision

| Operation | Syntax | Purpose |
|-----------|--------|---------|
| GET | `requests.get(url)` | Fetch resource |
| POST | `requests.post(url, json=data)` | Create resource |
| Headers | `headers={}` parameter | Custom HTTP headers |
| Params | `params={}` parameter | Query string parameters |
| Timeout | `timeout=5` parameter | Prevent hanging |
| Session | `requests.Session()` | Persistent connections/cookies |
| JSON | `response.json()` | Parse JSON response |
| Error | `response.raise_for_status()` | Raise on 4xx/5xx |
| Upload | `files={'file': open(..., 'rb')}` | Multipart upload |
| Stream | `response.iter_content()` | Large file downloads |

## Related Topics

- [22-libraries-flask](../22-libraries-flask/) - Server-side API building
- [23-libraries-django](../23-libraries-django/) - REST framework for Django
- [09-exception-handling](../09-exception-handling/) - Error handling patterns
- [04-concurrency](../04-concurrency/) - Async HTTP with aiohttp/httpx

---

> **Remember:** Always set a timeout. Always handle errors. Always use sessions for repeated calls. These three rules prevent 90% of production HTTP issues.
