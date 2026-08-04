# Service Mesh

## Overview

A service mesh is an infrastructure layer that handles service-to-service communication. It provides traffic management, security, and observability without requiring changes to application code. Popular implementations include Istio, Linkerd, and Consul Connect.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Architecture](#architecture)
- [Components](#components)
- [Features](#features)
- [Implementation](#implementation)
- [Benefits](#benefits)
- [Best Practices](#best-practices)

## Core Concepts

```
+--------------------------------------------------+
|            SERVICE MESH ARCHITECTURE              |
+--------------------------------------------------+
|                                                  |
|  Service A          Service B                    |
|  +----------+      +----------+                |
|  | App      |      | App      |                |
|  +----------+      +----------+                |
|       |                  |                      |
|  +----v----+        +----v----+                |
|  | Sidecar |<------>| Sidecar |                |
|  | Proxy   |        | Proxy   |                |
|  +---------+        +---------+                |
|                                                  |
|  Control Plane: Configuration, Certificates,     |
|                 Service Discovery                |
+--------------------------------------------------+
```

### Key Components

| Component | Description |
|-----------|-------------|
| Data Plane | Proxies handling traffic between services |
| Control Plane | Manages proxy configuration and policies |

## Architecture

### Data Plane

The data plane consists of proxies (sidecars) deployed alongside each service.

```yaml
# Envoy sidecar configuration
static_resources:
  listeners:
  - address:
      socket_address:
        address: 0.0.0.0
        port_value: 15001
    filter_chains:
    - filters:
      - name: envoy.filters.network.http_connection_manager
        typed_config:
          "@type": type.googleapis.com/envoy.extensions.filters.network.http_connection_manager.v3.HttpConnectionManager
          route_config:
            virtual_hosts:
            - name: backend
              routes:
              - match:
                  prefix: /
                route:
                  cluster: backend_service
```

### Control Plane

```yaml
# Istio control plane configuration
apiVersion: install.istio.io/v1alpha1
kind: IstioOperator
spec:
  profile: default
  components:
    pilot:
      k8s:
        resources:
          requests:
            cpu: 500m
            memory: 2Gi
```

## Components

### Traffic Management

```yaml
# Virtual Service - Traffic routing
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: my-service
spec:
  hosts:
  - my-service
  http:
  - route:
    - destination:
        host: my-service
        subset: v1
      weight: 90
    - destination:
        host: my-service
        subset: v2
      weight: 10
```

### Load Balancing

```yaml
# Destination Rule - Load balancing
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
metadata:
  name: my-service
spec:
  host: my-service
  trafficPolicy:
    loadBalancer:
      simple: LEAST_CONN
    connectionPool:
      tcp:
        maxConnections: 100
      http:
        h2UpgradePolicy: DEFAULT
        http1MaxPendingRequests: 100
        http2MaxRequests: 1000
```

### Security

```yaml
# Peer Authentication - mTLS
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
spec:
  mtls:
    mode: STRICT

# Authorization Policy
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: my-policy
spec:
  selector:
    matchLabels:
      app: my-service
  rules:
  - from:
    - source:
        principals: ["cluster.local/ns/default/sa/client"]
    to:
    - operation:
        methods: ["GET"]
```

### Observability

```yaml
# Telemetry configuration
apiVersion: telemetry.istio.io/v1alpha1
kind: Telemetry
metadata:
  name: mesh-default
spec:
  metrics:
  - providers:
    - name: prometheus
  tracing:
  - providers:
    - name: jaeger
  accessLogging:
  - providers:
    - name: otel
```

## Features

### Traffic Management

- **Routing**: Route traffic to specific versions
- **Load Balancing**: Distribute traffic across instances
- **Circuit Breaking**: Prevent cascade failures
- **Retries**: Automatic retry on failure
- **Timeouts**: Configure request timeouts
- **Rate Limiting**: Control request rates

### Security

- **mTLS**: Mutual TLS between services
- **Authorization**: Access control policies
- **JWT Validation**: Token-based authentication
- **Certificate Management**: Automatic cert rotation

### Observability

- **Metrics**: Request rate, latency, errors
- **Distributed Tracing**: Track requests across services
- **Access Logs**: Detailed request/response logs
- **Dashboards**: Real-time monitoring

## Implementation

### Istio Setup

```bash
# Install Istio
istioctl install --set profile=demo

# Enable sidecar injection
kubectl label namespace default istio-injection=enabled

# Deploy application
kubectl apply -f my-app.yaml

# Verify sidecar is injected
kubectl get pods -o jsonpath='{.items[*].spec.containers[*].name}'
```

### Linkerd Setup

```bash
# Install Linkerd
linkerd install | kubectl apply -f -

# Inject sidecar
kubectl get deploy -o yaml | linkerd inject - | kubectl apply -f -

# Verify
linkerd check
```

### Traffic Shifting

```bash
# Shift 10% traffic to v2
kubectl apply -f - <<EOF
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: my-service
spec:
  hosts:
  - my-service
  http:
  - route:
    - destination:
        host: my-service
        subset: v1
      weight: 90
    - destination:
        host: my-service
        subset: v2
      weight: 10
EOF
```

## Benefits

1. **Decouple infrastructure**: Applications focus on business logic
2. **Consistent policies**: Apply policies across all services
3. **Visibility**: Comprehensive observability
4. **Security**: Built-in mTLS and access control
5. **Resilience**: Circuit breaking, retries, timeouts

## Best Practices

### 1. Start with Observability

```yaml
# Enable metrics collection first
apiVersion: telemetry.istio.io/v1alpha1
kind: Telemetry
metadata:
  name: default
spec:
  metrics:
  - providers:
    - name: prometheus
```

### 2. Use Incremental Rollout

```yaml
# Canary deployment with mesh
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
spec:
  http:
  - route:
    - destination:
        host: my-service
        subset: stable
      weight: 95
    - destination:
        host: my-service
        subset: canary
      weight: 5
```

### 3. Implement Circuit Breaking

```yaml
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
spec:
  trafficPolicy:
    outlierDetection:
      consecutive5xxErrors: 5
      interval: 30s
      baseEjectionTime: 3m
```

### 4. Monitor Mesh Health

```python
class MeshMonitor:
    def get_mesh_status(self):
        return {
            'proxy_status': self.get_proxy_status(),
            'config_status': self.get_config_status(),
            'certificate_expiry': self.get_cert_expiry()
        }
```

### 5. Keep Configurations Simple

```yaml
# Start with simple rules
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
spec:
  hosts:
  - my-service
  http:
  - route:
    - destination:
        host: my-service
```

## Further Reading

- [Istio Documentation](https://istio.io/latest/docs/)
- [Linkerd Documentation](https://linkerd.io/2/overview/)
- [Service Mesh Patterns](https://servicesmesh.io/)
- [Envoy Proxy](https://www.envoyproxy.io/)
