# Python HTTP Reference

## What are HTTP modules?

Python provides several modules for working with HTTP: `http.server` for simple servers, `http.client` for low-level clients, and `urllib` for higher-level operations.

## Why do HTTP modules matter?

Understanding HTTP modules helps you:
- Build web servers and clients
- Interact with REST APIs
- Handle HTTP requests and responses
- Implement web scraping

---

## 1. http.server

```python
from http.server import HTTPServer, BaseHTTPRequestHandler

class MyHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write(b'<h1>Hello, World!</h1>')
    
    def do_POST(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        self.send_response(200)
        self.end_headers()
        self.wfile.write(f'Received: {post_data}'.encode())

# Run server
server = HTTPServer(('localhost', 8000), MyHandler)
print('Server running on port 8000')
server.serve_forever()
```

---

## 2. http.client

```python
import http.client

# Basic GET request
conn = http.client.HTTPSConnection("www.example.com")
conn.request("GET", "/")
response = conn.getresponse()
print(response.status)
print(response.read().decode())
conn.close()

# POST request
conn = http.client.HTTPSConnection("api.example.com")
headers = {'Content-type': 'application/json'}
conn.request("POST", "/data", '{"name": "Alice"}', headers)
response = conn.getresponse()
print(response.status)
conn.close()
```

---

## 3. urllib.request

```python
from urllib.request import urlopen, Request

# Basic GET request
response = urlopen('https://api.example.com/data')
data = response.read().decode()
print(data)

# With headers
request = Request('https://api.example.com/data',
                  headers={'User-Agent': 'Mozilla/5.0'})
response = urlopen(request)
data = response.read().decode()
```

---

## 4. urllib.parse

```python
from urllib.parse import urlparse, urlencode, parse_qs, quote, unquote

# Parse URL
parsed = urlparse('https://example.com/path?query=value')
print(parsed.scheme)    # https
print(parsed.netloc)    # example.com
print(parsed.path)      # /path
print(parsed.query)     # query=value

# URL encode
params = {'name': 'Alice', 'age': 30}
encoded = urlencode(params)
print(encoded)  # name=Alice&age=30

# Parse query string
query = 'name=Alice&age=30'
parsed = parse_qs(query)
print(parsed)  # {'name': ['Alice'], 'age': ['30']}

# Quote/unquote
print(quote('hello world'))  # hello%20world
print(unquote('hello%20world'))  # hello world
```

---

## 5. urllib.error

```python
from urllib.request import urlopen
from urllib.error import URLError, HTTPError

try:
    response = urlopen('https://api.example.com/data')
except HTTPError as e:
    print(f'HTTP Error: {e.code}')
except URLError as e:
    print(f'URL Error: {e.reason}')
```

---

## 6. requests (Third-party)

```python
import requests

# Basic GET request
response = requests.get('https://api.example.com/data')
print(response.json())
print(response.status_code)

# POST request
response = requests.post('https://api.example.com/data',
                         json={'name': 'Alice'})
print(response.json())

# With headers
headers = {'Authorization': 'Bearer token123'}
response = requests.get('https://api.example.com/data',
                        headers=headers)

# Download file
response = requests.get('https://example.com/file.txt', stream=True)
with open('file.txt', 'wb') as f:
    for chunk in response.iter_content(chunk_size=8192):
        f.write(chunk)
```

---

## One-Minute Revision Table

| Module | Description | Example |
|--------|-------------|---------|
| **http.server** | Simple HTTP server | `HTTPServer(('host', port), Handler)` |
| **http.client** | Low-level HTTP client | `HTTPConnection('host')` |
| **urllib.request** | High-level HTTP client | `urlopen('url')` |
| **urllib.parse** | URL parsing | `urlparse('url')` |
| **urllib.error** | HTTP errors | `HTTPError` |
| **requests** | Third-party HTTP library | `requests.get('url')` |

---

## Common Mistakes

### 1. Not Closing Connections

```python
# WRONG
conn = http.client.HTTPSConnection("example.com")
conn.request("GET", "/")
response = conn.getresponse()
# Connection not closed

# RIGHT
conn = http.client.HTTPSConnection("example.com")
try:
    conn.request("GET", "/")
    response = conn.getresponse()
finally:
    conn.close()
```

### 2. Not Handling Errors

```python
# WRONG
response = urlopen('https://api.example.com/data')

# RIGHT
try:
    response = urlopen('https://api.example.com/data')
except HTTPError as e:
    print(f'HTTP Error: {e.code}')
except URLError as e:
    print(f'URL Error: {e.reason}')
```

### 3. Not Using timeout

```python
# WRONG
response = urlopen('https://api.example.com/data')

# RIGHT
response = urlopen('https://api.example.com/data', timeout=10)
```

---

## Production Notes

1. **Use requests for most HTTP** - More user-friendly
2. **Use http.client for low-level** - When you need more control
3. **Use http.server for simple servers** - Don't use in production
4. **Always handle errors** - HTTPError, URLError, etc.
5. **Use timeout** - Prevent hanging connections
6. **Use context managers** - For proper cleanup
7. **Set User-Agent** - Some servers block default
8. **Use SSL/TLS** - For secure communication
9. **Consider aiohttp** - For async HTTP
10. **Respect robots.txt** - For web scraping

---

## Further Reading

- Python documentation on http.server
- Python documentation on http.client
- Python documentation on urllib
- requests documentation
