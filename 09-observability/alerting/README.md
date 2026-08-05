# Alerting Tools

## Overview

Alerting tools notify teams when system conditions require attention. They route alerts to appropriate recipients, manage escalation policies, and provide incident coordination capabilities.

## Tool Categories

### Alert Routing and Management
Platforms that receive, deduplicate, and route alerts to notification channels.

- **AlertManager** - Prometheus alert routing with grouping and silencing

### Incident Response Platforms
Services for on-call management, escalation, and incident coordination.

- **PagerDuty** - Digital operations management platform
- **OpsGenie** - Incident management and alerting platform

## Key Concepts

### Alert Lifecycle
1. **Firing** - Alert condition detected
2. **Pending** - Alert waiting for duration threshold
3. **Resolved** - Condition no longer true
4. **Silenced** - Alert intentionally suppressed

### Routing Rules
Directing alerts based on labels, severity, or service ownership.

```yaml
route:
  routes:
    - match:
        severity: critical
      receiver: 'pagerduty-critical'
    - match:
        service: 'payment-*'
      receiver: 'payments-team'
```

### Grouping
Combining related alerts into single notifications to reduce noise.

### Inhibition
Suppressing lower-priority alerts when higher-priority alerts fire.

## Notification Channels

| Channel | Use Case |
|---------|----------|
| Email | Non-urgent notifications |
| SMS | Critical alerts requiring immediate attention |
| Phone Call | Escalation for unacknowledged alerts |
| Chat (Slack, Teams) | Team-level awareness |
| Webhook | Integration with custom systems |

## Escalation Policies

### Linear Escalation
Alerts escalate through a fixed sequence of contacts.

### Round-Robin
Alerts rotate through a list of on-call engineers.

### Time-Based
Different responders based on time of day or day of week.

## Best Practices

1. Define clear severity levels with response expectations
2. Implement escalation policies for unacknowledged alerts
3. Use alert grouping to reduce notification fatigue
4. Create runbooks linked to each alert type
5. Test alert routing regularly with chaos engineering
6. Set appropriate silence windows for maintenance
7. Monitor alert statistics and tune thresholds
8. Implement alert fatigue metrics and review cycles
