# Python Monitoring

## structlog

Structured logging library.

```bash
pip install structlog
```

### Configuration
```python
import structlog

structlog.configure(
    processors=[
        structlog.processors.TimeStamper(fmt="iso"),
        structlog.processors.add_log_level,
        structlog.processors.JSONRenderer()
    ],
    wrapper_class=structlog.BoundLogger,
    context_class=dict,
    logger_factory=structlog.PrintLoggerFactory(),
)

log = structlog.get_logger()
```

### Usage
```python
log.info("user_login", user_id=123, ip="192.168.1.1")
log.error("payment_failed", order_id="abc", reason="insufficient_funds")
```

## Prometheus Client

Metrics for monitoring.

```bash
pip install prometheus-client
```

### Basic Metrics
```python
from prometheus_client import Counter, Histogram, Gauge
import time

REQUEST_COUNT = Counter('http_requests_total', 'Total requests')
REQUEST_LATENCY = Histogram('http_request_duration_seconds', 'Request latency')
ACTIVE_CONNECTIONS = Gauge('active_connections', 'Active connections')

@app.route('/api/data')
def get_data():
    REQUEST_COUNT.inc()
    with REQUEST_LATENCY.time():
        # Process request
        ACTIVE_CONNECTIONS.inc()
        # ... do work
        ACTIVE_CONNECTIONS.dec()
        return {"data": "value"}
```

### Expose Metrics
```python
from prometheus_client import start_http_server

# Start metrics server
start_http_server(8000)
```

## Sentry

Error tracking and performance monitoring.

```bash
pip install sentry-sdk
```

### Flask Integration
```python
import sentry_sdk
from sentry_sdk.integrations.flask import FlaskIntegration

sentry_sdk.init(
    dsn="YOUR_DSN",
    integrations=[FlaskIntegration()],
    traces_sample_rate=1.0,
    environment="production",
)

@app.route('/error')
def error():
    1 / 0  # This will be reported to Sentry
```

### Custom Events
```python
from sentry_sdk import capture_message, capture_exception

capture_message("Something happened")
try:
    risky_operation()
except Exception as e:
    capture_exception(e)
```

## OpenTelemetry

Distributed tracing and instrumentation.

```bash
pip install opentelemetry-api opentelemetry-sdk opentelemetry-exporter-otlp
```

### Setup
```python
from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter

# Configure
provider = TracerProvider()
processor = BatchSpanProcessor(OTLPSpanExporter())
provider.add_span_processor(processor)
trace.set_tracer_provider(provider)

tracer = trace.get_tracer(__name__)
```

### Manual Span
```python
with tracer.start_as_current_span("my_operation") as span:
    span.set_attribute("operation.value", 42)
    # Do work
    span.add_event("checkpoint_reached")
```

### Auto-instrumentation
```bash
pip install opentelemetry-instrumentation-flask

opentelemetry-instrument -m flask run
```

## Logging Best Practices

### Structured Logging
```python
import structlog

log = structlog.get_logger()

# Good - structured
log.info("order_created", order_id=123, amount=99.99)

# Bad - unstructured
log.info(f"Order 123 created with amount 99.99")
```

### Log Levels
```python
import logging

logger = logging.getLogger(__name__)

logger.debug("Detailed debugging info")
logger.info("General information")
logger.warning("Warning message")
logger.error("Error occurred")
logger.critical("Critical failure")
```

## Health Checks

```python
from flask import jsonify

@app.route('/health')
def health():
    return jsonify({
        "status": "healthy",
        "version": "1.0.0",
        "checks": {
            "database": check_database(),
            "cache": check_cache(),
        }
    })

def check_database():
    try:
        db.execute("SELECT 1")
        return "ok"
    except:
        return "error"
```

## Alerting

### Prometheus Alerting Rules
```yaml
groups:
  - name: python-app
    rules:
      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: High error rate detected
```

## Best Practices

1. Use structured logging (structlog)
2. Include correlation IDs for request tracing
3. Set up error tracking (Sentry)
4. Instrument critical paths with OpenTelemetry
5. Monitor key metrics (latency, errors, throughput)
6. Configure appropriate alerting thresholds
7. Use health checks for load balancers
8. Keep logs in JSON format for parsing
