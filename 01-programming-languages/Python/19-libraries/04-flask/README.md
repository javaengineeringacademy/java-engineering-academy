# Flask

## Why Flask Exists

Every Python developer who needs to build a web API or prototype faces a choice: use a heavy framework with enforced conventions, or build everything from scratch. Flask was created to fill the middle ground — a minimal core that gives you routing, request/response handling, and a development server, while letting you choose your own database, authentication, and templates. It's the toolkit approach to web development.

## What You'll Learn

By the end of this section, you'll be able to:

- Create REST APIs with routing, JSON responses, and error handling
- Organize applications using Blueprints for modular architecture
- Handle configuration, templates, and middleware for production apps

## When to Use Flask

| Use Case | Why Flask | Alternative |
|----------|----------|-------------|
| REST API | Minimal overhead, clean routes | FastAPI |
| Microservice | Single-file app, easy to containerize | Django |
| Prototype | From idea to working demo in minutes | Full framework |
| Webhook receiver | Lightweight, no framework overhead | Raw WSGI |
| Admin dashboard | Flask-Admin extension | Django Admin |
| Full web app | Batteries added, not included | Django |

## How Flask Works Internally

Flask is built on Werkzeug (WSGI toolkit) and Jinja2 (templating). When a request arrives, Werkzeug's URL router matches it to a view function. The view function receives a `Request` context object and returns a `Response`. Flask's context locals (like `request` and `g`) are thread-local proxies that give you access to request data without passing it explicitly.

Blueprints are Flask's modular architecture system. A Blueprint is a collection of routes that can be registered on the application. When you register a blueprint, Flask adds all its routes, error handlers, and template paths to the app. This lets you split a large application into logical components without tight coupling.

```python
from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route('/api/greet/<name>')
def greet(name):
    return jsonify({'message': f'Hello, {name}!'})

@app.route('/users', methods=['GET', 'POST'])
def users():
    if request.method == 'GET':
        return jsonify({'users': ['Alice', 'Bob']})
    data = request.get_json()
    return jsonify({'created': data}), 201
```

## Production Checklist

### ✅ Before using Flask in production:

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

### ❌ Myth 1: Flask is only for small apps
**Reality:** Flask scales well for large applications. Companies like Netflix, Reddit, and Lyft use Flask in production. The key is proper structure with blueprints, extensions, and good architecture.

### ❌ Myth 2: Flask is simpler than Django, so it's better
**Reality:** "Simpler" depends on context. Flask gives you choice; Django gives you conventions. For complex web apps with many models and admin needs, Django's batteries-included approach is faster.

### ❌ Myth 3: Flask's development server is fine for testing
**Reality:** The development server is single-threaded, not secure, and not optimized. Always use Gunicorn or uWSGI for anything beyond local development.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Lightweight web framework |
| Complexity | O(1) per request |
| Thread Safe | Yes (with proper WSGI server) |
| Best Alternative | FastAPI for async APIs |
| When to Use | APIs, microservices, prototypes |
| When to Avoid | Full-stack apps with admin (use Django) |

## Related Topics

- [05-django](../05-django/) - Full-stack alternative
- [09-fastapi](../09-fastapi/) - Async API framework
- [06-sqlalchemy](../06-sqlalchemy/) - Database integration
