# Kubernetes Monitoring

## Overview

Kubernetes monitoring involves collecting metrics, logs, and traces to ensure cluster health and application performance.

## Prometheus

### Installation
```bash
# Install Prometheus using Helm
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install prometheus prometheus-community/kube-prometheus-stack
```

### ServiceMonitor
```yaml
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: my-app
  labels:
    release: prometheus
spec:
  selector:
    matchLabels:
      app: my-app
  endpoints:
  - port: http-metrics
    path: /metrics
    interval: 30s
```

### PrometheusRule
```yaml
apiVersion: monitoring.coreos.com/v1
kind: PrometheusRule
metadata:
  name: my-app-rules
spec:
  groups:
  - name: my-app
    rules:
    - alert: HighErrorRate
      expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
      for: 5m
      labels:
        severity: critical
      annotations:
        summary: "High error rate detected"
```

## Grafana

### Installation
```bash
# Install Grafana using Helm
helm install grafana grafana/grafana
```

### Dashboard
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: grafana-dashboards
  labels:
    grafana_dashboard: "1"
data:
  my-dashboard.json: |
    {
      "dashboard": {
        "title": "My App Dashboard"
      }
    }
```

## Metrics Server

```bash
# Install Metrics Server
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml

# Check metrics
kubectl top nodes
kubectl top pods
```

## Centralized Logging

### EFK Stack
```yaml
# Elasticsearch
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: elasticsearch
spec:
  replicas: 3
  selector:
    matchLabels:
      app: elasticsearch
  template:
    metadata:
      labels:
        app: elasticsearch
    spec:
      containers:
      - name: elasticsearch
        image: docker.elastic.co/elasticsearch/elasticsearch:8.10.0
        env:
        - name: discovery.type
          value: single-node

# Fluentd
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: fluentd
spec:
  selector:
    matchLabels:
      app: fluentd
  template:
    metadata:
      labels:
        app: fluentd
    spec:
      containers:
      - name: fluentd
        image: fluent/fluentd-kubernetes-daemonset:elasticsearch

# Kibana
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kibana
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kibana
  template:
    metadata:
      labels:
        app: kibana
    spec:
      containers:
      - name: kibana
        image: docker.elastic.co/kibana/kibana:8.10.0
```

## Best Practices

1. **Use Prometheus** - Industry standard for metrics
2. **Implement Grafana** - Visualize metrics effectively
3. **Set up alerting** - Notify on critical issues
4. **Monitor cluster health** - Track node and pod metrics
5. **Implement logging** - Centralize log collection
6. **Use ServiceMonitors** - Auto-discover services
7. **Set resource limits** - Prevent monitoring overhead
8. **Test alerts** - Verify alerting channels
9. **Document dashboards** - Add descriptions for complex panels
10. **Monitor costs** - Track resource usage
