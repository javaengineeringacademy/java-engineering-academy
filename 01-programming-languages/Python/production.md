# Python Production Deployment

## Gunicorn

WSGI HTTP Server.

```bash
pip install gunicorn
```

### Basic Usage
```bash
# Run with 4 workers
gunicorn -w 4 app:app

# Bind to address
gunicorn -b 0.0.0.0:8000 app:app

# Use async workers
gunicorn -k gevent -w 4 app:app
```

### Configuration
```python
# gunicorn.conf.py
bind = "0.0.0.0:8000"
workers = 4
worker_class = "gevent"
timeout = 120
accesslog = "/var/log/gunicorn/access.log"
errorlog = "/var/log/gunicorn/error.log"
loglevel = "info"
```

### systemd Service
```ini
# /etc/systemd/system/myapp.service
[Unit]
Description=My Python App
After=network.target

[Service]
User=www-data
WorkingDirectory=/opt/myapp
ExecStart=/opt/myapp/venv/bin/gunicorn -c gunicorn.conf.py app:app
Restart=always

[Install]
WantedBy=multi-user.target
```

## uWSGI

Alternative WSGI server.

```bash
pip install uwsgi
```

### Configuration
```ini
# uwsgi.ini
[uwsgi]
module = app:app
master = true
processes = 4
socket = /tmp/uwsgi.sock
chmod-socket = 660
vacuum = true
die-on-term = true
```

### Run
```bash
uwsgi --ini uwsgi.ini
```

## Containerization

### Production Dockerfile
```dockerfile
FROM python:3.11-slim AS builder

WORKDIR /app

# Install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir --prefix=/install -r requirements.txt

FROM python:3.11-slim

WORKDIR /app

# Copy installed packages
COPY --from=builder /install /usr/local

# Copy application code
COPY . .

# Create non-root user
RUN useradd --create-home appuser
USER appuser

EXPOSE 8000

CMD ["gunicorn", "-w", "4", "-b", "0.0.0.0:8000", "app:app"]
```

### Docker Compose
```yaml
version: '3.8'

services:
  web:
    build: .
    ports:
      - "8000:8000"
    environment:
      - DATABASE_URL=postgresql://db:5432/mydb
    depends_on:
      - db
    deploy:
      replicas: 3

  db:
    image: postgres:15
    volumes:
      - postgres_data:/var/lib/postgresql/data
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_PASSWORD=secret

volumes:
  postgres_data:
```

## Logging Configuration

### Production Setup
```python
import logging
import logging.config

LOGGING_CONFIG = {
    'version': 1,
    'disable_existing_loggers': False,
    'formatters': {
        'json': {
            'class': 'pythonjsonlogger.jsonlogger.JsonFormatter',
            'format': '%(asctime)s %(levelname)s %(message)s'
        },
    },
    'handlers': {
        'console': {
            'class': 'logging.StreamHandler',
            'formatter': 'json',
        },
    },
    'root': {
        'level': 'INFO',
        'handlers': ['console'],
    },
}

logging.config.dictConfig(LOGGING_CONFIG)
```

## Environment Variables

```python
import os

# Required settings
DATABASE_URL = os.environ["DATABASE_URL"]
SECRET_KEY = os.environ["SECRET_KEY"]

# Optional with defaults
DEBUG = os.getenv("DEBUG", "false").lower() == "true"
PORT = int(os.getenv("PORT", "8000"))
LOG_LEVEL = os.getenv("LOG_LEVEL", "INFO")
```

## Health Checks

```python
from flask import jsonify

@app.route('/health')
def health():
    checks = {
        "database": check_database(),
        "cache": check_cache(),
    }
    
    status = "healthy" if all(v == "ok" for v in checks.values()) else "unhealthy"
    
    return jsonify({
        "status": status,
        "checks": checks
    }), 200 if status == "healthy" else 503
```

## Graceful Shutdown

```python
import signal
import sys

def signal_handler(sig, frame):
    print("Shutting down gracefully...")
    # Cleanup resources
    db.close()
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)
signal.signal(signal.SIGTERM, signal_handler)
```

## Best Practices

1. Use Gunicorn or uWSGI in production
2. Run multiple workers for concurrency
3. Use containerization for consistency
4. Implement health checks
5. Configure structured logging
6. Use environment variables for config
7. Enable graceful shutdown
8. Monitor resource usage
