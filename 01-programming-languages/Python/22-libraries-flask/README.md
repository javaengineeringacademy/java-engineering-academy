# Flask — Lightweight Web Framework

> **Small core, big possibilities. Flask gives you the tools, you build the house.**

## What

Flask is a micro web framework for Python. It provides a minimal core — routing, request/response handling, and a development server — and lets you add everything else (database, authentication, templates) as extensions. It's built on Werkzeug (WSGI toolkit) and Jinja2 (templating).

## Why

- **Minimal:** No bloat. You choose what to include.
- **Flexible:** No enforced project structure, ORM, or admin panel.
- **Quick setup:** `app = Flask(__name__)` and you're running.
- **Extensions:** Rich ecosystem for databases, auth, forms, and more.
- **Community:** Extensive documentation, tutorials, and production deployments.

## When

| Scenario | Flask Approach | Why |
|----------|---------------|-----|
| REST API | `@app.route()` + JSON responses | Minimal overhead, clean routes |
| Microservice | Single-file app, blueprint structure | Fast to deploy, easy to containerize |
| Prototype | Flask + Jinja2 templates | From idea to working demo in minutes |
| Webhook receiver | Simple route handler | Lightweight, no framework overhead |
| Admin dashboard | Flask-Admin extension | Quick CRUD interface |
| Full web app | Flask + SQLAlchemy + Flask-Login | Batteries added, not batteries included |

## How

### Basic Application

```python
from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route('/')
def home():
    return '<h1>Hello, Flask!</h1>'

@app.route('/api/greet/<name>')
def greet(name):
    return jsonify({'message': f'Hello, {name}!'})

if __name__ == '__main__':
    app.run(debug=True)
```

### Routes and HTTP Methods

```python
from flask import Flask, request, jsonify

app = Flask(__name__)

# Different HTTP methods
@app.route('/users', methods=['GET', 'POST'])
def users():
    if request.method == 'GET':
        return jsonify({'users': ['Alice', 'Bob']})
    elif request.method == 'POST':
        data = request.get_json()
        return jsonify({'created': data}), 201

# Route converters
@app.route('/user/<int:user_id>')
def get_user(user_id):
    return jsonify({'user_id': user_id})

@app.route('/file/<path:filepath>')
def get_file(filepath):
    return jsonify({'path': filepath})

# URL parameters
@app.route('/search')
def search():
    query = request.args.get('q', '')
    page = request.args.get('page', 1, type=int)
    return jsonify({'query': query, 'page': page})
```

### Templates (Jinja2)

```python
from flask import Flask, render_template, request

app = Flask(__name__)

@app.route('/hello/<name>')
def hello(name):
    return render_template('hello.html', name=name)

# Template inheritance
# base.html:
# <!DOCTYPE html>
# <html>
# <head><title>{% block title %}{% endblock %}</title></head>
# <body>
#   {% block content %}{% endblock %}
# </body>
# </html>

# page.html:
# {% extends "base.html" %}
# {% block title %}Page Title{% endblock %}
# {% block content %}
#   <h1>Hello, {{ name }}!</h1>
#   {% for item in items %}
#     <p>{{ item }}</p>
#   {% endfor %}
# {% endblock %}
```

### Request and Response

```python
from flask import Flask, request, jsonify, make_response

app = Flask(__name__)

@app.route('/data', methods=['POST'])
def receive_data():
    # JSON body
    json_data = request.get_json()

    # Form data
    form_data = request.form.to_dict()

    # File upload
    file = request.files.get('file')

    # Headers
    auth = request.headers.get('Authorization')

    return jsonify({'received': 'ok'})

@app.route('/custom-response')
def custom_response():
    response = make_response(jsonify({'custom': True}), 200)
    response.headers['X-Custom'] = 'value'
    response.set_cookie('session', 'abc123')
    return response
```

### Blueprints (Modular Apps)

```python
from flask import Flask, Blueprint

# Define a blueprint
api = Blueprint('api', __name__, url_prefix='/api')

@api.route('/users')
def list_users():
    return {'users': ['Alice', 'Bob']}

@api.route('/users/<int:id>')
def get_user(id):
    return {'user_id': id}

# Register in app
app = Flask(__name__)
app.register_blueprint(api)
```

### Error Handling and Middleware

```python
from flask import Flask, jsonify

app = Flask(__name__)

class APIError(Exception):
    def __init__(self, message, status_code=400):
        self.message = message
        self.status_code = status_code

@app.errorhandler(APIError)
def handle_api_error(error):
    return jsonify({'error': error.message}), error.status_code

@app.errorhandler(404)
def not_found(error):
    return jsonify({'error': 'Not found'}), 404

@app.errorhandler(500)
def server_error(error):
    return jsonify({'error': 'Internal server error'}), 500

@app.before_request
def before_request():
    # Run before every request
    pass

@app.after_request
def after_request(response):
    # Run after every request
    response.headers['X-Request-ID'] = 'abc123'
    return response
```

### Configuration and Environment

```python
import os
from flask import Flask

app = Flask(__name__)

# Configuration from dict
app.config.update(
    DEBUG=os.environ.get('FLASK_DEBUG', False),
    SECRET_KEY=os.environ.get('SECRET_KEY', 'dev-key'),
    DATABASE_URI=os.environ.get('DATABASE_URI', 'sqlite:///app.db'),
    TESTING=False
)

# Or load from config file
app.config.from_pyfile('config.py')
app.config.from_object('config.ProductionConfig')
```

## Production Checklist

- [ ] **Never use `debug=True` in production** — exposes debugger and reloader
- [ ] **Set `SECRET_KEY`** — use environment variable, not hardcoded value
- [ ] **Use a production WSGI server** — Gunicorn or uWSGI, not Flask's dev server
- [ ] **Add input validation** — don't trust request data, use marshmallow or pydantic
- [ ] **Handle CORS** — use `flask-cors` for cross-origin requests
- [ ] **Rate limiting** — use `flask-limiter` to prevent abuse
- [ ] **Logging** — configure proper logging, not just `print()`
- [ ] **Use blueprints** — organize large apps into modules

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **Hello World** | Single file, `app.run()`, no structure |
| 2 | **Routes** | Multiple routes, JSON responses, basic error handling |
| 3 | **Structured** | Blueprints, templates, database integration, config management |
| 4 | **Production** | WSGI server, logging, testing, CI/CD, Docker |
| 5 | **Enterprise** | Microservices architecture, async workers, observability, scaling |

## Common Myths

### Myth 1: "Flask is only for small apps"
**Reality:** Flask scales well for large applications. Companies like Netflix, Reddit, and Lyft use Flask in production. The key is proper structure with blueprints, extensions, and good architecture.

### Myth 2: "Flask is simpler than Django, so it's better"
**Reality:** "Simpler" depends on context. Flask gives you choice; Django gives you conventions. For complex web apps with many models and admin needs, Django's batteries-included approach is faster. For APIs and microservices, Flask's minimalism wins.

### Myth 3: "Flask's development server is fine for testing"
**Reality:** The development server is single-threaded, not secure, and not optimized. Always use Gunicorn or uWSGI for anything beyond local development, even for testing with realistic traffic.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Create app | `app = Flask(__name__)` | Initialize application |
| Route | `@app.route('/path')` | Map URL to function |
| JSON | `jsonify(dict)` | Return JSON response |
| Request | `request.get_json()`, `request.args` | Access incoming data |
| Template | `render_template('page.html')` | Render Jinja2 template |
| Blueprint | `Blueprint('name', __name__)` | Modular route grouping |
| Error | `@app.errorhandler(404)` | Custom error pages |
| Config | `app.config['KEY']` | Application settings |
| Run | `app.run(debug=True)` | Start development server |

## Related Topics

- [23-libraries-django](../23-libraries-django/) - Full-stack alternative
- [21-libraries-requests](../21-libraries-requests/) - Client-side HTTP
- [24-libraries-sqlalchemy](../24-libraries-sqlalchemy/) - Database integration
- [25-libraries-pytest](../25-libraries-pytest/) - Testing Flask apps

---

> **Remember:** Flask gives you a toolkit, not a blueprint. That's its strength — and its responsibility. Design your architecture intentionally.
