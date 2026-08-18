# Alerting Thresholds

## Overview

Alerting thresholds define the boundaries between normal and abnormal system behavior. Properly configured thresholds reduce alert fatigue while ensuring critical issues are detected quickly.

---

## Threshold Types

### Static Thresholds

Fixed values that trigger alerts when exceeded.

```yaml
# Static threshold example
groups:
  - name: static_thresholds
    rules:
      - alert: HighErrorRate
        expr: error_rate > 0.05
        for: 5m
        labels:
          severity: critical
```

### Dynamic Thresholds

Adaptive thresholds based on historical patterns.

```python
import numpy as np
from typing import List

class DynamicThreshold:
    def __init__(self, window_hours=24, sensitivity=2.0):
        self.window_hours = window_hours
        self.sensitivity = sensitivity
    
    def calculate(self, historical_data: List[float]) -> dict:
        """Calculate dynamic threshold from historical data."""
        if len(historical_data) < 10:
            return {'threshold': None, 'insufficient_data': True}
        
        values = np.array(historical_data)
        mean = np.mean(values)
        std = np.std(values)
        
        return {
            'upper': mean + (self.sensitivity * std),
            'lower': max(0, mean - (self.sensitivity * std)),
            'mean': mean,
            'std': std,
            'insufficient_data': False
        }
    
    def should_alert(self, current_value: float, threshold: dict) -> bool:
        """Check if current value should trigger alert."""
        if threshold.get('insufficient_data'):
            return False
        
        return current_value > threshold['upper']
```

### Seasonal Thresholds

Thresholds that adjust based on time patterns.

```python
from datetime import datetime
from typing import Dict

class SeasonalThreshold:
    def __init__(self):
        self.hourly_baselines: Dict[int, float] = {}
    
    def train(self, hourly_data: Dict[int, List[float]]):
        """Train seasonal model with hourly data."""
        for hour, values in hourly_data.items():
            self.hourly_baselines[hour] = {
                'mean': np.mean(values),
                'std': np.std(values)
            }
    
    def get_threshold(self, hour: int, sensitivity: float = 2.0) -> dict:
        """Get threshold for specific hour."""
        if hour not in self.hourly_baselines:
            return {'upper': float('inf'), 'lower': 0}
        
        baseline = self.hourly_baselines[hour]
        return {
            'upper': baseline['mean'] + (sensitivity * baseline['std']),
            'lower': max(0, baseline['mean'] - (sensitivity * baseline['std']))
        }
    
    def should_alert(self, hour: int, value: float) -> bool:
        """Check if value exceeds seasonal threshold."""
        threshold = self.get_threshold(hour)
        return value > threshold['upper']
```

---

## Alert Rules by Metric

### Availability Alerts

```yaml
groups:
  - name: availability
    rules:
      # Service down
      - alert: ServiceDown
        expr: up{job="api"} == 0
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Service is down"
          description: "{{ $labels.instance }} has been down for more than 1 minute"
      
      # High error rate
      - alert: HighErrorRate
        expr: |
          rate(http_requests_total{status=~"5.."}[5m]) / 
          rate(http_requests_total[5m]) > 0.05
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High error rate"
          description: "Error rate is {{ $value | humanizePercentage }}"
      
      # Synthetic check failure
      - alert: SyntheticCheckFailed
        expr: synthetic_check_success == 0
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "Synthetic check failed"
          description: "Check {{ $labels.check_name }} has failed"
```

### Latency Alerts

```yaml
groups:
  - name: latency
    rules:
      # P50 latency
      - alert: HighP50Latency
        expr: histogram_quantile(0.5, rate(http_request_duration_seconds_bucket[5m])) > 0.5
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "High P50 latency"
          description: "P50 latency is {{ $value }}s"
      
      # P95 latency
      - alert: HighP95Latency
        expr: histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 1
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "High P95 latency"
          description: "P95 latency is {{ $value }}s"
      
      # P99 latency
      - alert: HighP99Latency
        expr: histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m])) > 2
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High P99 latency"
          description: "P99 latency is {{ $value }}s"
```

### SLA/SLO Alerts

```yaml
groups:
  - name: sla
    rules:
      # SLA violation
      - alert: SLAViolation
        expr: sla_compliance_ratio < 0.999
        for: 15m
        labels:
          severity: critical
        annotations:
          summary: "SLA violation detected"
          description: "Compliance ratio is {{ $value | humanizePercentage }}"
      
      # Error budget burn rate
      - alert: ErrorBudgetBurnRateHigh
        expr: |
          rate(sla_errors_total[1h]) > 
          (1 - sla_target) * 14.4
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "Error budget burning too fast"
          description: "Current burn rate: {{ $value }}x"
      
      # SLO breach warning
        - alert: SLOBreachWarning
        expr: slo_compliance_ratio < 0.995
        for: 30m
        labels:
          severity: warning
        annotations:
          summary: "SLO breach warning"
          description: "Compliance ratio: {{ $value | humanizePercentage }}"
```

### SSL Certificate Alerts

```yaml
groups:
  - name: ssl
    rules:
      # Certificate expiring soon
      - alert: SSLCertificateExpiringSoon
        expr: ssl_certificate_days_remaining < 30
        for: 1h
        labels:
          severity: warning
        annotations:
          summary: "SSL certificate expiring soon"
          description: "Certificate for {{ $labels.hostname }} expires in {{ $value }} days"
      
      # Certificate critical
      - alert: SSLCertificateCritical
        expr: ssl_certificate_days_remaining < 7
        for: 1h
        labels:
          severity: critical
        annotations:
          summary: "SSL certificate critically close to expiration"
          description: "Certificate for {{ $labels.hostname }} expires in {{ $value }} days"
      
      # Certificate invalid
      - alert: SSLCertificateInvalid
        expr: ssl_certificate_valid == 0
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "SSL certificate is invalid"
          description: "Certificate for {{ $labels.hostname }} is not valid"
```

---

## Alert Routing

### Severity Levels

```yaml
# Alert routing configuration
route:
  receiver: 'default'
  group_by: ['alertname', 'cluster', 'service']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  
  routes:
    # Critical alerts - immediate notification
    - match:
        severity: critical
      receiver: 'critical-pagerduty'
      group_wait: 0s
      repeat_interval: 5m
      
    # Warning alerts - batched notification
    - match:
        severity: warning
      receiver: 'warning-slack'
      group_wait: 30s
      repeat_interval: 4h
      
    # Info alerts - daily digest
    - match:
        severity: info
      receiver: 'info-email'
      repeat_interval: 24h

receivers:
  - name: 'critical-pagerduty'
    pagerduty_configs:
      - service_key: '<pagerduty-key>'
        
  - name: 'warning-slack'
    slack_configs:
      - api_url: '<slack-webhook>'
        channel: '#alerts-warning'
        
  - name: 'info-email'
    email_configs:
      - to: 'team@example.com'
        subject: 'Info Alert: {{ .GroupLabels.alertname }}'
```

### Escalation Policies

```python
from enum import Enum
from typing import List, Dict

class AlertSeverity(Enum):
    INFO = "info"
    WARNING = "warning"
    CRITICAL = "critical"
    EMERGENCY = "emergency"

class EscalationPolicy:
    def __init__(self):
        self.policies: Dict[AlertSeverity, List[Dict]] = {}
    
    def add_policy(self, severity: AlertSeverity, escalation: List[Dict]):
        """Add escalation policy for severity level."""
        self.policies[severity] = escalation
    
    def get_escalation(self, severity: AlertSeverity, escalation_level: int) -> Dict:
        """Get escalation target for given level."""
        policy = self.policies.get(severity, [])
        if escalation_level < len(policy):
            return policy[escalation_level]
        return policy[-1] if policy else {}

# Example escalation policies
escalation = EscalationPolicy()

escalation.add_policy(AlertSeverity.CRITICAL, [
    {'target': 'on-call-engineer', 'method': 'pagerduty', 'wait': 0},
    {'target': 'engineering-manager', 'method': 'sms', 'wait': 15},
    {'target': 'vp-engineering', 'method': 'phone', 'wait': 30},
])

escalation.add_policy(AlertSeverity.WARNING, [
    {'target': 'slack-alerts', 'method': 'slack', 'wait': 0},
    {'target': 'on-call-engineer', 'method': 'email', 'wait': 60},
])
```

---

## Best Practices

### 1. Avoid Alert Fatigue

| Bad Practice | Good Practice |
|--------------|---------------|
| Alert on every anomaly | Alert on actionable conditions |
| Use fixed thresholds | Use dynamic thresholds |
| Alert on symptoms | Alert on causes |
| Multiple alerts per issue | Group related alerts |

### 2. Threshold Tuning

```python
def tune_thresholds(historical_data, target_false_positive_rate=0.01):
    """Automatically tune thresholds based on historical data."""
    values = np.array(historical_data)
    
    # Calculate threshold for target false positive rate
    threshold = np.percentile(values, (1 - target_false_positive_rate) * 100)
    
    return {
        'threshold': threshold,
        'false_positive_rate': target_false_positive_rate,
        'sample_size': len(values)
    }
```

### 3. Multi-Window Alerting

```yaml
# Multi-window alerting for better accuracy
groups:
  - name: multi_window
    rules:
      - alert: HighLatency
        expr: |
          (
            histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m])) > 2
            and
            histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[1h])) > 1.5
          )
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Sustained high latency"
```

---

## Common Anti-Patterns

| Anti-Pattern | Problem | Solution |
|--------------|---------|----------|
| Alert storms | Too many alerts at once | Group related alerts |
| Static thresholds | Miss anomalies | Use dynamic thresholds |
| Symptom alerts | Delayed detection | Alert on causes |
| Noisy alerts | Alert fatigue | Tune thresholds regularly |
| Missing context | Hard to debug | Include relevant context |

---

## Next Steps

- [Dashboard Integration](../07-dashboard-integration/README.md) - Creating dashboards
- [Examples](../examples/README.md) - Complete examples
- [Practices](../practices/README.md) - Exercises
