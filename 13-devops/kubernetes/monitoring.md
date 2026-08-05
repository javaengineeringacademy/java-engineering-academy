# Kubernetes Monitoring

## Prometheus Operator

### Installation

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install prometheus prometheus-community/kube-prometheus-stack -n monitoring --create-namespace
```

### Components

- **Prometheus**: Collects and stores metrics
- **Alertmanager**: Routes and manages alerts
- **Grafana**: Visualizes metrics with dashboards
- **Prometheus Operator**: Manages Prometheus via CRDs
- **ServiceMonitor**: Defines scraping targets
- **PrometheusRule**: Defines alerting rules

### ServiceMonitor Configuration

```yaml
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: my-app
  namespace: monitoring
spec:
  selector:
    matchLabels:
      app: my-app
  namespaceSelector:
    matchNames:
    - production
  endpoints:
  - port: metrics
    interval: 15s
    path: /metrics
```

### Alert Rules

```yaml
apiVersion: monitoring.coreos.com/v1
kind: PrometheusRule
metadata:
  name: my-app-alerts
  namespace: monitoring
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
        summary: High error rate on {{ $labels.instance }}
    - alert: HighMemoryUsage
      expr: container_memory_usage_bytes / container_spec_memory_limit_bytes > 0.9
      for: 5m
      labels:
        severity: warning
```

## kube-state-metrics

Exposes Kubernetes object metadata as Prometheus metrics. Provides metrics for Pods, Deployments, Nodes, and other resources.

### Installation

```bash
helm install kube-state-metrics prometheus-community/kube-state-metrics -n monitoring
```

### Key Metrics

- kube_pod_info: Pod information
- kube_deployment_status_replicas: Deployment replica counts
- kube_node_status_condition: Node conditions
- kube_resourcequota: Resource quota usage

## Grafana

### Dashboards

- Kubernetes Cluster Monitoring: Overall cluster health
- Node Exporter: Node-level metrics
- Pod Metrics: Individual Pod performance
- Custom dashboards for application metrics

### Default Credentials

- Username: admin
- Password: prom-operator (default from Helm chart)

### Access

```bash
kubectl port-forward -n monitoring svc/prometheus-grafana 3000:80
```

## Loki (Log Aggregation)

### Installation

```bash
helm repo add grafana https://grafana.github.io/helm-charts
helm install loki grafana/loki-stack -n monitoring
```

### Promtail Agent

Collects logs and sends to Loki:

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: promtail-config
  namespace: monitoring
data:
  promtail.yaml: |
    server:
      http_listen_port: 9080
    positions:
      filename: /tmp/positions.yaml
    clients:
      - url: http://loki:3100/loki/api/v1/push
    scrape_configs:
    - job_name: kubernetes
      kubernetes_sd_configs:
      - role: pod
      relabel_configs:
      - source_labels: ['__meta_kubernetes_pod_label_app']
        target_label: 'app'
```

## Metrics Collection

### Metrics Server

Required for HPA and kubectl top:

```bash
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

### Custom Application Metrics

Expose application metrics via /metrics endpoint:

```python
# Python Flask example
from prometheus_client import Counter, Histogram

REQUEST_COUNT = Counter('app_requests_total', 'Total requests')
REQUEST_LATENCY = Histogram('app_request_duration_seconds', 'Request latency')

@app.route('/metrics')
def metrics():
    return generate_latest()
```

## Alerting Configuration

### Alertmanager

```yaml
global:
  resolve_timeout: 5m

route:
  group_by: ['alertname', 'namespace']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 12h
  receiver: 'slack'

receivers:
- name: 'slack'
  slack_configs:
  - api_url: 'https://hooks.slack.com/services/...'
    channel: '#alerts'
    title: '{{ .GroupLabels.alertname }}'
    text: '{{ .CommonAnnotations.summary }}'
```

## Monitoring Best Practices

1. Monitor all control plane components
2. Set up alerts for node and Pod health
3. Use custom metrics for application-specific monitoring
4. Implement distributed tracing with Jaeger or Zipkin
5. Monitor resource usage vs quotas
6. Set up log aggregation for debugging
7. Use dashboards for visual monitoring
8. Regular review and tuning of alert thresholds
