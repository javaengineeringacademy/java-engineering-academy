# Proxy Pattern in Python

The Proxy pattern provides a surrogate or placeholder for another object to control access to it. Python's `__getattr__` and properties make proxy implementations elegant and Pythonic.

## When to Use

- Lazy initialization of expensive objects
- Access control and permissions
- Logging and monitoring
- Caching remote requests
- Counting references
- Creating distributed objects

## Python Implementation

### Virtual Proxy (Lazy Loading)
```python
class HeavyObject:
    def __init__(self, data: str):
        self._data = data
        print(f"HeavyObject initialized with {data}")
    
    def process(self):
        return f"Processing {self._data}"

class HeavyObjectProxy:
    def __init__(self, data: str):
        self._data = data
        self._real_object = None
    
    def _get_real_object(self):
        if self._real_object is None:
            self._real_object = HeavyObject(self._data)
        return self._real_object
    
    def process(self):
        return self._get_real_object().process()

# Usage
proxy = HeavyObjectProxy("large_dataset")  # No initialization yet
result = proxy.process()  # Initialized on first use
```

### Protection Proxy
```python
class FileAccess:
    def __init__(self, filename: str):
        self.filename = filename
    
    def read(self):
        return f"Reading {self.filename}"
    
    def write(self, data: str):
        return f"Writing to {self.filename}: {data}"

class AccessControlProxy:
    def __init__(self, file_access: FileAccess, user_role: str):
        self._file = file_access
        self._role = user_role
    
    def read(self):
        if self._role in ("admin", "reader"):
            return self._file.read()
        raise PermissionError("Insufficient permissions")
    
    def write(self, data: str):
        if self._role == "admin":
            return self._file.write(data)
        raise PermissionError("Only admins can write")

# Usage
file = FileAccess("document.txt")
admin_proxy = AccessControlProxy(file, "admin")
reader_proxy = AccessControlProxy(file, "reader")
```

### Using `__getattr__`
```python
class LoggingProxy:
    def __init__(self, obj):
        self._obj = obj
        self._calls = []
    
    def __getattr__(self, name):
        attr = getattr(self._obj, name)
        if callable(attr):
            def wrapper(*args, **kwargs):
                self._calls.append(name)
                print(f"Calling {name}")
                return attr(*args, **kwargs)
            return wrapper
        return attr

# Usage
class Service:
    def process(self):
        return "processed"

service = LoggingProxy(Service())
service.process()  # Logs the call
```

## Pythonic Alternative

Use properties for simple proxying:
```python
class CachedProperty:
    def __init__(self, func):
        self._func = func
        self._cache = None
    
    def __get__(self, obj, objtype=None):
        if self._cache is None:
            self._cache = self._func(obj)
        return self._cache
```

## Real-World Example

```python
import time

class APIClient:
    def fetch(self, url: str):
        time.sleep(1)  # Simulate network delay
        return f"Data from {url}"

class CachingProxy:
    def __init__(self, client: APIClient, ttl: int = 60):
        self._client = client
        self._cache = {}
        self._ttl = ttl
    
    def fetch(self, url: str):
        if url in self._cache:
            timestamp, data = self._cache[url]
            if time.time() - timestamp < self._ttl:
                return f"Cached: {data}"
        
        data = self._client.fetch(url)
        self._cache[url] = (time.time(), data)
        return data
```

## Best Practices

1. Use `__getattr__` for transparent proxying
2. Implement caching with expiration for performance
3. Add logging without modifying original class
4. Consider using `functools.wraps` for method proxies
5. Document proxy behavior and limitations

## Interview Questions

1. What are the different types of proxies?
2. How does Python's `__getattr__` help implement proxies?
3. What is the difference between Proxy and Decorator?
4. How would you implement a transparent proxy?
5. What are the performance implications of using proxies?

## References

- *Design Patterns* - GoF, Chapter 4
- Python `__getattr__` documentation
- *Python Cookbook* - Alex Martelli
- PEP 3119 - Abstract Base Classes
