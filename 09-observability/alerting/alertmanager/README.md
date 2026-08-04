# AlertManager

## Overview

AlertManager handles alerts sent by Prometheus, routing them to correct receivers with deduplication, grouping, and silencing capabilities.

## Configuration

### alertmanager.yml
```yaml
global:
  smtp_smarthost: 'smtp.gmail.com:587'
  smtp_from: 'alerts@example.com'
  smtp_auth_username: 'alerts@example.com'
  smtp_auth_password: 'password'

route:
  group_by: ['alertname', 'service']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'default'
  routes:
    - match:
        severity: critical
      receiver: 'pagerduty-critical'
    - match:
        severity: warning
      receiver: 'slack-warnings'

receivers:
  - name: 'default'
    email_configs:
      - to: 'team@example.com'
  
  - name: 'pagerduty-critical'
    pagerduty_configs:
      - service_key: 'key'
  
  - name: 'slack-warnings'
    slack_configs:
      - api_url: 'https://hooks.slack.com/...'
        channel: '#alerts'
        title: '{{ .GroupLabels.alertname }}'
        text: '{{ range .Alerts }}{{ .Annotations.summary }}{{ end }}'

inhibit_rules:
  - source_match:
      severity: 'critical'
    target_match:
      severity: 'warning'
    equal: ['alertname', 'service']
```

## Alert Routing

```yaml
route:
  routes:
    - match_re:
        service: 'api-.*'
      receiver: 'api-team'
    - match:
        alertname: InstanceDown
      receiver: 'infrastructure'
```

## Silences

```bash
# Create silence via API
curl -X POST http://localhost:9093/api/v2/silences \
  -H "Content-Type: application/json" \
  -d '{
    "matchers": [{"name": "alertname", "value": "HighCPU"}],
    "startsAt": "2024-01-15T10:00:00Z",
    "endsAt": "2024-01-15T12:00:00Z",
    "createdBy": "admin",
    "comment": "Maintenance window"
  }'
```

## Best Practices

1. Group related alerts together
2. Use inhibit rules to reduce noise
3. Set appropriate repeat intervals
4. Create escalation policies
5. Test alert routing regularly
6. Use templates for consistent messaging
7. Implement silence management
8. Monitor AlertManager health
