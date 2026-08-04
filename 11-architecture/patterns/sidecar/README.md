# Sidecar Pattern

## Overview

The Sidecar pattern deploys helper components alongside the main application in the same host or container. The sidecar handles cross-cutting concerns like logging, monitoring, security, and networking, allowing the main application to focus on business logic.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Architecture](#architecture)
- [Use Cases](#use-cases)
- [Implementation](#implementation)
- [Benefits](#benefits)
- [Best Practices](#best-practices)

## Core Concepts

```
+--------------------------------------------------+
|            SIDECAR PATTERN                        |
+--------------------------------------------------+
|                                                  |
|  +------------------+  +------------------+     |
|  | Main Application |  | Sidecar Proxy   |     |
|  |                  |<->|                  |     |
|  | - Business Logic |  | - Logging        |     |
|  | - API Handlers   |  | - Monitoring     |     |
|  |                  |  | - Security       |     |
|  +------------------+  | - Networking     |     |
|                        +------------------+     |
|                                                  |
|  Shared network namespace                        |
|  Shared filesystem (optional)                    |
+--------------------------------------------------+
```

### Key Characteristics

| Characteristic | Description |
|----------------|-------------|
| Co-located | Deployed in same pod/host |
| Transparent | Application unaware of sidecar |
| Language-independent | Works with any language |
| Shared lifecycle | Starts/stops with main app |

## Architecture

### Sidecar Types

1. **Proxy Sidecar**: Network proxy for traffic management
2. **Utility Sidecar**: Logging, monitoring, metrics
3. **Adapter Sidecar**: Normalizes application output
4. **Ambassador Sidecar**: Proxy for external services

### Communication Patterns

```
Main App --> Sidecar --> External Service
    |                      |
    +----(local)-----------+

Main App <-- Sidecar <-- External Service
    |                      |
    +----(local)-----------+
```

## Use Cases

### Service Mesh Proxy

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  template:
    spec:
      containers:
      - name: app
        image: my-app:latest
        ports:
        - containerPort: 8080
      - name: envoy-proxy
        image: envoyproxy/envoy:latest
        ports:
        - containerPort: 15001
        volumeMounts:
        - name: envoy-config
          mountPath: /etc/envoy
      volumes:
      - name: envoy-config
        configMap:
          name: envoy-config
```

### Logging Sidecar

```yaml
containers:
- name: app
  image: my-app:latest
- name: fluentd
  image: fluentd:latest
  volumeMounts:
  - name: app-logs
    mountPath: /var/log/app
volumes:
- name: app-logs
  emptyDir: {}
```

### Monitoring Sidecar

```yaml
containers:
- name: app
  image: my-app:latest
- name: prometheus-exporter
  image: prom/node-exporter:latest
  ports:
  - containerPort: 9090
```

## Implementation

### Ambassador Pattern

```python
class AmbassadorSidecar:
    def __init__(self, service_url):
        self.service_url = service_url
        self.circuit_breaker = CircuitBreaker()
        self.retry_handler = RetryHandler()

    def handle_request(self, request):
        # Add resilience patterns
        def _call():
            return requests.post(self.service_url, json=request)

        return self.retry_handler.execute(
            self.circuit_breaker.call,
            _call
        )

    def add_metadata(self, request):
        # Add tracing headers
        request['X-Request-ID'] = str(uuid4())
        request['X-Timestamp'] = datetime.now().isoformat()
        return request
```

### Adapter Sidecar

```python
class AdapterSidecar:
    def __init__(self, app_port, metrics_port):
        self.app_port = app_port
        self.metrics_port = metrics_port

    def translate_metrics(self, app_metrics):
        # Convert app-specific metrics to Prometheus format
        prometheus_metrics = []
        for name, value in app_metrics.items():
            prometheus_metrics.append(
                f'{name} {value}'
            )
        return '\n'.join(prometheus_metrics)
```

### Utility Sidecar

```python
class UtilitySidecar:
    def __init__(self):
        self.log_collector = LogCollector()
        self.health_checker = HealthChecker()

    def collect_logs(self, log_path):
        # Collect and forward logs
        logs = self.log_collector.collect(log_path)
        self.forward_to_central(logs)

    def check_health(self):
        # Monitor main application health
        return self.health_checker.check('http://localhost:8080/health')
```

## Benefits

1. **Language independence**: Sidecar works with any language
2. **Separation of concerns**: Cross-cutting logic separated
3. **Reuse**: Same sidecar for multiple applications
4. **Independent deployment**: Update sidecar without touching app
5. **Consistent infrastructure**: Standardized approach across services

## Best Practices

### 1. Keep Sidecar Lightweight

```yaml
# Minimal resource usage
resources:
  requests:
    memory: "64Mi"
    cpu: "50m"
  limits:
    memory: "128Mi"
    cpu: "100m"
```

### 2. Share Network Namespace

```yaml
# Sidecar and app share network
spec:
  containers:
  - name: app
    ports:
    - containerPort: 8080
  - name: sidecar
    # Can access app at localhost:8080
```

### 3. Use Health Checks

```yaml
livenessProbe:
  httpGet:
    path: /health
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
```

### 4. Monitor Sidecar Performance

```python
class SidecarMetrics:
    def collect_metrics(self):
        return {
            'requests_proxied': self.request_count,
            'errors': self.error_count,
            'latency_p95': self.calculate_latency_percentile(95)
        }
```

### 5. Graceful Shutdown

```python
class GracefulSidecar:
    def shutdown(self):
        # Stop accepting new connections
        self.stop_listening()
        
        # Wait for in-flight requests
        self.wait_for_pending_requests(timeout=30)
        
        # Cleanup resources
        self.cleanup()
```

## Further Reading

- [Sidecar Pattern - Chris Richardson](https://microservices.io/patterns/deployment/sidecar.html)
- [Service Mesh - Conway](https://buoyant.io/2017/04/25/whats-a-service-mesh-and-how-does-it-work/)
- [Envoy Proxy](https://www.envoyproxy.io/)
